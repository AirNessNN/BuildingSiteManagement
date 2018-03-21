package dbManager;

import java.util.ArrayList;

/**
 * 工人工厂，静态负责维护工人属性，工厂创造工人的实例，属性和静态数据同步，并且支持序列化持久储存
 */
public class WorkerFactory {


    /**
     * 预设数据：index
     * 0=名字
     * 1=住址
     *
     */
    private static final Info[] data={
            new Info(Info.TYPE_DOUBLE,"序号"),
            new Info(Info.TYPE_STRING,"名字"),
            new Info(Info.TYPE_STRING,"住址"),
            new Info(Info.TYPE_STRING,"联系方式"),
            new Info(Info.TYPE_STRING,"身份证"),
            new Info(Info.TYPE_DOUBLE,"年龄"),
            new Info(Info.TYPE_DATE,"出生日期"),
            new Info(Info.TYPE_DOUBLE,"性别"),
            new Info(Info.TYPE_STRING,"民族"),
            new Info(Info.TYPE_DATE,"入职时间"),
            new Info(Info.TYPE_DATE,"离职时间"),
            new Info(Info.TYPE_STRING,"银行卡号"),
            new Info(Info.TYPE_STRING,"开户地址"),
            new Info(Info.TYPE_DOUBLE,"工种"),
            new Info(Info.TYPE_DOUBLE,"工人状态"),
            new Info(Info.TYPE_STRING,"备注"),
            new Info(Info.TYPE_DOUBLE,"约定月工资"),
            new Info(Info.TYPE_DOUBLE,"合计工日"),
            new Info(Info.TYPE_DOUBLE,"结余工资"),
            new Info(Info.TYPE_DATE_LIST,"工资领取信息"),
            new Info(Info.TYPE_DATE_LIST,"出勤信息"),
            new Info(Info.TYPE_DATE_LIST,"生活费领取情况")
    };

    /**
     * 用户定义的数据
     */
    private static ArrayList<Info> userData=null;





    public static Worker createWorker(){
        Worker worker=new Worker();
        for(Info info:data){
            Info tmp=new Info(info.getType(),info.getName());
            worker.addInfo(tmp);
        }
        if(userData!=null){
            for(Info info:userData){
                Info tmp=new Info(info.getType(),info.getName());
                worker.addInfo(tmp);

            }
        }
        return worker;
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
