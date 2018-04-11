package dbManager;

import application.AnUtils;
import application.Application;

import java.lang.reflect.Type;
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
    public static final String LABEL_BANK_ADDRES="开户地址";
    public static final String LABEL_WORKER_TYPE="工种";
    public static final String LABEL_WORKER_STATE="工人状态";
    public static final String LABEL_TAG="备注";
    public static final String LABEL_AGREED_MONTHDLY_WAGE="约定月工资";
    public static final String LABEL_TOTAL_WORKING_DAY="合计工日";
    public static final String LABEL_SURPLUS_SALARY="结余工资";
    public static final String LABEL_SALARY_GET_ARR="工资领取信息";
    public static final String LABEL_DUTY_ARR="出勤信息";
    public static final String LABEL_COST_OF_LIVING_ARR="生活费领取情况";
    public static final String LABEL_SITE="所属工地";


    /**
     * 预设数据：index
     * 0=名字
     * 1=住址
     *
     */
    private static final Info<?>[] data={
            new Info<Integer>(Info.TYPE_INTEGER,"序号"),
            new Info<String>(Info.TYPE_STRING,"名字"),
            new Info<String>(Info.TYPE_STRING,"住址"),
            new Info<String>(Info.TYPE_STRING,"联系方式"),
            new Info<String>(Info.TYPE_STRING,"身份证"),
            new Info<Integer>(Info.TYPE_INTEGER,"年龄"),
            new Info<Date>(Info.TYPE_DATE,"出生日期"),
            new Info<String>(Info.TYPE_STRING,"性别"),
            new Info<String>(Info.TYPE_STRING,"民族"),
            new Info<Date>(Info.TYPE_DATE,"入职时间"),
            new Info<Date>(Info.TYPE_DATE,"离职时间"),
            new Info<String>(Info.TYPE_STRING,"银行卡号"),
            new Info<String>(Info.TYPE_STRING,"开户地址"),
            new Info<String>(Info.TYPE_STRING,"工种"),
            new Info<String>(Info.TYPE_STRING,"工人状态"),
            new Info<String>(Info.TYPE_STRING,"备注"),
            new Info<Double>(Info.TYPE_DOUBLE,"约定月工资"),
            new Info<Double>(Info.TYPE_DOUBLE,"合计工日"),
            new Info<Double>(Info.TYPE_DOUBLE,"结余工资"),
            new Info<ArrayList<? extends DateValue<?>>>(Info.TYPE_ARRAY_LIST,"工资领取信息"),
            new Info<ArrayList<? extends DateValue<?>>>(Info.TYPE_ARRAY_LIST,"出勤信息"),
            new Info<ArrayList<? extends DateValue<?>>>(Info.TYPE_ARRAY_LIST,"生活费领取情况"),
            new Info<String>(Info.TYPE_STRING,"所属工地")
    };
    /**
     * 预设的工人属性，可以
     */
    //private static final ArrayList<? extends Info<?>>data=new ArrayList<>();

    /**
     * 用户定义的数据
     */
    private static ArrayList<Info> userData=null;





    public static Anbean createWorker(){
        Anbean worker=new Anbean();
        for(Info info:data){
            switch (info.getType()){
                case Info.TYPE_ARRAY_LIST:{
                    Info<ArrayList<DateValue<?>>> tmp=new Info<>(info.getName());
                    worker.addInfo(tmp);
                    break;
                }
                case Info.TYPE_DATE:{
                    Info<Date> tmp=new Info<>(info.getName());
                    worker.addInfo(tmp);
                    break;
                }
                case Info.TYPE_DOUBLE:{
                    Info<Double> tmp=new Info<>(info.getName());
                    worker.addInfo(tmp);
                    break;
                }
                case Info.TYPE_INTEGER:{
                    Info<Integer> tmp=new Info<>(info.getName());
                    worker.addInfo(tmp);
                    break;
                }
                case Info.TYPE_STRING:{
                    Info<String> tmp=new Info<>(info.getName());
                    worker.addInfo(tmp);
                    break;
                }
                default:
                    worker.addInfo(new Info<String>(info.getName()));
            }
        }
        if(userData!=null){
            for(Info info:userData){
                Info<String> tmp=new Info(info.getName());
                worker.addInfo(tmp);
            }
        }
        return worker;
    }

    public static AnArrayBean createWorkerProperty(){
        AnArrayBean tmpBean=new AnArrayBean();
        for (Info info:data){
            switch (info.getType()){
                case Info.TYPE_ARRAY_LIST:{
                    InfoArray<ArrayList> tmp=new InfoArray<>(info.getName());
                    tmpBean.addInfoArray(tmp);
                    break;
                }
                case Info.TYPE_DATE:{
                    InfoArray<Date> tmp=new InfoArray<>(info.getName());
                    tmpBean.addInfoArray(tmp);
                    break;
                }
                case Info.TYPE_DOUBLE:{
                    InfoArray<Double> tmp=new InfoArray<>(info.getName());
                    tmpBean.addInfoArray(tmp);
                    break;
                }
                case Info.TYPE_INTEGER:{
                    InfoArray<Integer> tmp=new InfoArray<>(info.getName());
                    tmpBean.addInfoArray(tmp);
                    break;
                }
                default:
                    InfoArray<String> tmp=new InfoArray<>(info.getName());
                    tmpBean.addInfoArray(tmp);
                    if (info.getName().equals("性别")){
                        tmp.addValue("男");
                        tmp.addValue("女");
                    }
            }
        }
        if(userData!=null){
            for(Info info:userData){
                InfoArray<String> tmp=new InfoArray<>(info.getName());
                tmpBean.addInfoArray(tmp);
            }
        }
        return tmpBean;
    }


    public static int getPreinstallSize(){
        return data.length;
    }

    public static Info[] getPreinstall(){
        return data;
    }

    public static void setUserData(Info info){
        if(userData!=null){
            userData.add(info);
        }
    }

    public static void removeUserDate(Info info){
        if (userData!=null){
            userData.remove(info);
        }
    }

}
