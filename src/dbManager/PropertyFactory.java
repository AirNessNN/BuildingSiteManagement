package dbManager;

import application.Application;

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
    private static final Node<?>[] WORKER_MODEL={
            new Node<Integer>(Node.TYPE_INTEGER,LABEL_NUMBER),//序号
            new Node<String>(Node.TYPE_STRING,LABEL_NAME),//名字
            new Node<String>(Node.TYPE_STRING,LABEL_ADDRESS),//地址
            new Node<String>(Node.TYPE_STRING,LABEL_PHONE),//电话号码
            new Node<String>(Node.TYPE_STRING,LABEL_ID_CARD),//身份证
            new Node<Integer>(Node.TYPE_INTEGER,LABEL_AGE),//年龄
            new Node<Date>(Node.TYPE_DATE,LABEL_BIRTH),//生日
            new Node<String>(Node.TYPE_STRING,LABEL_SEX),//性别
            new Node<String>(Node.TYPE_STRING,LABEL_NATION),//民族
            new Node<String>(Node.TYPE_STRING,LABEL_BANK_ID),//银行卡号
            new Node<String>(Node.TYPE_STRING, LABEL_BANK_ADDRESS),//开户地址
            new Node<String>(Node.TYPE_STRING,LABEL_TAG)//备注
    };

    /**
     * 普通属性模型
     */
    private static final Node<?>[] PROPERTY_MODEL={
            new Node<String>(Node.TYPE_STRING,LABEL_SEX),//性别
            new Node<String>(Node.TYPE_STRING,LABEL_NATION),//民族
            new Node<>(Node.TYPE_STRING,LABEL_SITE),//工地
            new Node<>(Node.TYPE_STRING,LABEL_WORKER_TYPE),//工种
            new Node<>(Node.TYPE_STRING,LABEL_WORKER_STATE)//状态
    };

    /**
     * 设置属性模板
     */
    private static final Node<?>[] SETTING_MODEL={
            new Node<Integer>(Node.TYPE_INTEGER,SETTING_WORKER_INDEX)
    };

    /**
     * 用户定义的数据
     */
    private static ArrayList<Node> userData=null;


    /**
     * 创建一个空的工人
     * @return
     */
    public static AnBean createWorker(){
        AnBean worker=new AnBean();
        for(Node node :WORKER_MODEL){
            switch (node.getType()){
                case Node.TYPE_ARRAY_LIST:{
                    Node<ArrayList> tmp=new Node<>(Node.TYPE_ARRAY_LIST, node.getName());
                    tmp.setValue(new ArrayList());
                    worker.addInfo(tmp);
                    break;
                }
                case Node.TYPE_DATE:{
                    Node<Date> tmp=new Node<>(Node.TYPE_DATE, node.getName());
                    worker.addInfo(tmp);
                    break;
                }
                case Node.TYPE_DOUBLE:{
                    Node<Double> tmp=new Node<>(Node.TYPE_DOUBLE, node.getName());
                    tmp.setValue(0d);
                    worker.addInfo(tmp);
                    break;
                }
                case Node.TYPE_INTEGER:{
                    Node<Integer> tmp=new Node<>(Node.TYPE_INTEGER, node.getName());
                    tmp.setValue(0);
                    worker.addInfo(tmp);
                    break;
                }
                case Node.TYPE_STRING:{
                    Node<String> tmp=new Node<>(Node.TYPE_STRING, node.getName());
                    tmp.setValue("");
                    worker.addInfo(tmp);
                    break;
                }
                default:
                    worker.addInfo(new Node<String>(Node.TYPE_STRING, node.getName()));
            }
        }
        if(userData!=null){
            for(Node node :userData){
                Node tmp=new Node(node.getName());
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
        for (Node node :PROPERTY_MODEL){
            try {
                switch (node.getType()){
                    case Node.TYPE_ARRAY_LIST:{
                        AnColumn tmp=new AnColumn(true, node.getName());
                        tmpBean.addColumn(tmp);
                        break;
                    }
                    case Node.TYPE_DATE:{
                        AnColumn tmp=new AnColumn(true, node.getName());
                        tmpBean.addColumn(tmp);
                        break;
                    }
                    case Node.TYPE_DOUBLE:{
                        AnColumn tmp=new AnColumn(true, node.getName());
                        tmpBean.addColumn(tmp);
                        break;
                    }
                    case Node.TYPE_INTEGER:{
                        AnColumn tmp=new AnColumn(true, node.getName());
                        tmpBean.addColumn(tmp);
                        break;
                    }
                    default:
                        AnColumn tmp=new AnColumn(true, node.getName());
                        tmpBean.addColumn(tmp);
                        if (node.getName().equals(PropertyFactory.LABEL_SEX)){
                            tmp.addValue("男");
                            tmp.addValue("女");
                        }
                        if (node.getName().equals(PropertyFactory.LABEL_WORKER_STATE)){
                            tmp.addValue("在职");
                            tmp.addValue("离职");
                            tmp.addValue("其他");
                        }
                        if (node.getName().equals(PropertyFactory.LABEL_WORKER_TYPE)){
                            tmp.addValue("包工");
                            tmp.addValue("点工");
                            tmp.addValue("点工加包工");
                            tmp.addValue("包月");
                            tmp.addValue("其他");
                        }
                        if (node.getName().equals(PropertyFactory.LABEL_NATION)){
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
            for(Node node :userData){
                AnColumn tmp=new AnColumn(true, node.getName());
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
        AnColumn id=new AnColumn(false,true,LABEL_ID_CARD);//ID：不可重复

        AnColumn dealSalary=new AnColumn(true,true,LABEL_DEAL_SALARY);//工种：可重复

        AnColumn workType=new AnColumn(true,true,LABEL_WORKER_TYPE);//工作状态：可重复

        AnColumn entry=new AnColumn(true,true,LABEL_ENTRY_TIME);//入职日期：可重复

        AnColumn leave=new AnColumn(true,true,LABEL_LEAVE_TIME);//离职


        AnDataTable bean=new AnDataTable();
        try {
            bean.addColumn(id);
            bean.addColumn(dealSalary);
            bean.addColumn(workType);
            bean.addColumn(entry);
            bean.addColumn(leave);
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
    public static Node[] getPreinstall(){
        return WORKER_MODEL;
    }

    public static void addUserData(Node node){
        if(userData!=null){
            userData.add(node);
        }else{
            userData=new ArrayList<>();
            userData.add(node);
        }
    }

    public static void removeUserDate(Node node){
        if (userData!=null){
            userData.remove(node);
        }
    }

    public static void removeUserDate(final String name){
        Application.startService(()->{
            Node delete=null;
            for (Node node :userData){
                if (node.getName().equals(name))delete= node;
            }
            if (delete!=null)userData.remove(delete);
        });
    }

    public static Node[] getUserDatas(){
        return (Node[]) userData.toArray();
    }

    public static void setUserDatas(AnDataTable table){
        if (table!=null){
            userData.clear();
            for (AnColumn column:table.getValues()){
                userData.add(new Node(column.getName(),""));
            }
        }
    }

}
