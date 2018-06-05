package dbManager;

import java.util.ArrayList;
import java.util.Date;

/**
 * 工人工厂，静态负责维护工人属性，工厂创造工人的实例，属性和静态数据同步，并且支持序列化持久储存
 */
public class PropertyFactory {

    public static final String LABEL_NUMBER="序号";
    public static final String LABEL_NAME="名字";
    public static final String LABEL_ADDRESS="住址";
    public static final String LABEL_ID_CARD="身份证";
    public static final String LABEL_AGE="年龄";
    public static final String LABEL_BIRTH="出生日期";
    public static final String LABEL_SEX="性别";
    public static final String LABEL_NATION="民族";
    public static final String LABEL_ENTRY_TIME="入职时间";
    public static final String LABEL_LEAVE_TIME="离职时间";
    public static final String LABEL_BANK_ID="银行卡号";
    public static final String LABEL_BANK_ADDRESS ="开户地址";
    public static final String LABEL_WORKER_TYPE="工种";
    public static final String LABEL_WORKER_STATE="工人状态";
    public static final String LABEL_TAG="备注";
    public static final String LABEL_DEAL_SALARY ="协议工价";
    public static final String LABEL_TOTAL_WORKING_DAY="合计工日";
    public static final String LABEL_SURPLUS_SALARY="结余工资";
    public static final String LABEL_SALARY_GET_ARR="工资领取信息";
    public static final String LABEL_DUTY_ARR="出勤信息";
    public static final String LABEL_COST_OF_LIVING ="已领取的生活费";
    public static final String LABEL_SITE="所属工地";
    public static final String LABEL_PHONE="电话号码";
    public static final String LABEL_PROJECT_NAME="项目名称";
    public static final String LAB_UNIT_OF_BUILD="建设单位";
    public static final String LAB_UNIT_OF_DO="施工单位";

    //设置属性
    public static final String SETTING_WORKER_INDEX="工人编号";


    /**
     * 工人属性模型
     */
    private static final Info<?>[] WORKER_MODEL={
            new Info<Integer>(Info.TYPE_INTEGER,LABEL_NUMBER),//序号
            new Info<String>(Info.TYPE_STRING,LABEL_NAME),//名字
            new Info<String>(Info.TYPE_STRING,LABEL_ADDRESS),//地址
            new Info<String>(Info.TYPE_STRING,LABEL_PHONE),//电话号码
            new Info<String>(Info.TYPE_STRING,LABEL_ID_CARD),//身份证
            new Info<Integer>(Info.TYPE_INTEGER,LABEL_AGE),//年龄
            new Info<Date>(Info.TYPE_DATE,LABEL_BIRTH),//生日
            new Info<String>(Info.TYPE_STRING,LABEL_SEX),//性别
            new Info<String>(Info.TYPE_STRING,LABEL_NATION),//民族
            new Info<Date>(Info.TYPE_DATE,LABEL_ENTRY_TIME),//入职
            new Info<Date>(Info.TYPE_DATE,LABEL_LEAVE_TIME),//离职
            new Info<String>(Info.TYPE_STRING,LABEL_BANK_ID),//银行卡号
            new Info<String>(Info.TYPE_STRING, LABEL_BANK_ADDRESS),//开户地址
            new Info<String>(Info.TYPE_STRING,LABEL_TAG)//备注
    };

    /**
     * 普通属性模型
     */
    private static final Info<?>[] PROPERTY_MODEL={
            new Info<String>(Info.TYPE_STRING,LABEL_SEX),//性别
            new Info<String>(Info.TYPE_STRING,LABEL_NATION),//民族
            new Info<>(Info.TYPE_STRING,LABEL_SITE),//工地
            new Info<>(Info.TYPE_STRING,LABEL_WORKER_TYPE),//工种
            new Info<>(Info.TYPE_STRING,LABEL_WORKER_STATE)//状态
    };

    /**
     * 设置属性模板
     */
    private static final Info<?>[] SETTING_MODEL={
            new Info<Integer>(Info.TYPE_INTEGER,SETTING_WORKER_INDEX)
    };

    /**
     * 用户定义的数据
     */
    private static ArrayList<Info> userData=null;


    /**
     * 创建一个空的工人
     * @return
     */
    public static AnBean createWorker(){
        AnBean worker=new AnBean();
        for(Info info:WORKER_MODEL){
            switch (info.getType()){
                case Info.TYPE_ARRAY_LIST:{
                    Info<ArrayList> tmp=new Info<>(Info.TYPE_ARRAY_LIST,info.getName());
                    tmp.setValue(new ArrayList());
                    worker.addInfo(tmp);
                    break;
                }
                case Info.TYPE_DATE:{
                    Info<Date> tmp=new Info<>(Info.TYPE_DATE,info.getName());
                    worker.addInfo(tmp);
                    break;
                }
                case Info.TYPE_DOUBLE:{
                    Info<Double> tmp=new Info<>(Info.TYPE_DOUBLE,info.getName());
                    tmp.setValue(0d);
                    worker.addInfo(tmp);
                    break;
                }
                case Info.TYPE_INTEGER:{
                    Info<Integer> tmp=new Info<>(Info.TYPE_INTEGER,info.getName());
                    tmp.setValue(0);
                    worker.addInfo(tmp);
                    break;
                }
                case Info.TYPE_STRING:{
                    Info<String> tmp=new Info<>(Info.TYPE_STRING,info.getName());
                    tmp.setValue("");
                    worker.addInfo(tmp);
                    break;
                }
                default:
                    worker.addInfo(new Info<String>(Info.TYPE_STRING,info.getName()));
            }
        }
        if(userData!=null){
            for(Info info:userData){
                Info tmp=new Info(info.getName());
                worker.addInfo(tmp);
            }
        }
        return worker;
    }

    /**
     * 创建一个空的属性，用于收集所有工人属性的值
     * @return
     */
    public static AnDataTable createWorkerProperty(){
        AnDataTable tmpBean=new AnDataTable();
        for (Info info:PROPERTY_MODEL){
            try {
                switch (info.getType()){
                    case Info.TYPE_ARRAY_LIST:{
                        AnColumn<ArrayList> tmp=new AnColumn<>(true,info.getName());
                        tmpBean.addColumn(tmp);
                        break;
                    }
                    case Info.TYPE_DATE:{
                        AnColumn<Date> tmp=new AnColumn<>(true,info.getName());
                        tmpBean.addColumn(tmp);
                        break;
                    }
                    case Info.TYPE_DOUBLE:{
                        AnColumn<Double> tmp=new AnColumn<>(true,info.getName());
                        tmpBean.addColumn(tmp);
                        break;
                    }
                    case Info.TYPE_INTEGER:{
                        AnColumn<Integer> tmp=new AnColumn<>(true,info.getName());
                        tmpBean.addColumn(tmp);
                        break;
                    }
                    default:
                        AnColumn<String> tmp=new AnColumn<>(true,info.getName());
                        tmpBean.addColumn(tmp);
                        if (info.getName().equals(PropertyFactory.LABEL_SEX)){
                            tmp.addValue("男");
                            tmp.addValue("女");
                        }
                        if (info.getName().equals(PropertyFactory.LABEL_WORKER_STATE)){
                            tmp.addValue("在职");
                            tmp.addValue("离职");
                            tmp.addValue("其他");
                        }
                        if (info.getName().equals(PropertyFactory.LABEL_WORKER_TYPE)){
                            tmp.addValue("包工");
                            tmp.addValue("点工");
                            tmp.addValue("其他");
                        }
                        if (info.getName().equals(PropertyFactory.LABEL_NATION)){
                            tmp.addValue("汉族");
                            tmp.addValue("回族");
                            tmp.addValue("维吾尔族");
                            tmp.addValue("壮族");
                            tmp.addValue("俄罗斯族");
                            tmp.addValue("蒙古族");
                            tmp.addValue("藏族");
                        }
                }
            }catch (Exception e){
            }
        }
        if(userData!=null){
            for(Info info:userData){
                AnColumn<String> tmp=new AnColumn<>(true,info.getName());
                try {
                    tmpBean.addColumn(tmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return tmpBean;
    }

    /**
     * 创建一个空的工地，不包含员工
     * @return
     */
    public static AnDataTable createBuildingSite(){
        AnColumn<String> id=new AnColumn<>(false);//ID：不可重复
        id.setName(PropertyFactory.LABEL_ID_CARD);

        AnColumn<String> workType=new AnColumn<>(true);//月工资：可重复
        workType.setName(PropertyFactory.LABEL_WORKER_TYPE);

        AnColumn<Double> dealSalary=new AnColumn<>(true);//工种：可重复
        dealSalary.setName(PropertyFactory.LABEL_DEAL_SALARY);

        AnColumn<Date> entry=new AnColumn<>(true);//入职日期：可重复
        entry.setName(PropertyFactory.LABEL_ENTRY_TIME);

        AnColumn<Date> leave=new AnColumn<>(true);
        leave.setName(LABEL_LEAVE_TIME);

        AnColumn<String> state=new AnColumn<>(true);
        state.setName(LABEL_WORKER_STATE);

        AnColumn<String> projectName=new AnColumn<>(false);
        projectName.setName(LABEL_PROJECT_NAME);

        AnColumn<String> build=new AnColumn<>(false);
        build.setName(LAB_UNIT_OF_BUILD);

        AnColumn<String> doit=new AnColumn<>(false);
        doit.setName(LAB_UNIT_OF_DO);


        AnDataTable bean=new AnDataTable();
        try {
            bean.addColumn(id);
            bean.addColumn(dealSalary);
            bean.addColumn(workType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  bean;
    }

    /**
     *     获取工人所有自带属性
     */
    public static int getPreinstallSize(){
        return WORKER_MODEL.length;
    }

    /**
     * 获取自带属性的数组
     * @return
     */
    public static Info[] getPreinstall(){
        return WORKER_MODEL;
    }

    public static void addUserData(Info info){
        if(userData!=null){
            userData.add(info);
        }else{
            userData=new ArrayList<>();
            userData.add(info);
        }
    }

    public static void removeUserDate(Info info){
        if (userData!=null){
            userData.remove(info);
        }
    }

}
