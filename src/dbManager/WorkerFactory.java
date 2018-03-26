package dbManager;

import java.util.ArrayList;

/**
 * ���˹�������̬����ά���������ԣ��������칤�˵�ʵ�������Ժ;�̬����ͬ��������֧�����л��־ô���
 */
public class WorkerFactory {


    /**
     * Ԥ�����ݣ�index
     * 0=����
     * 1=סַ
     *
     */
    private static final Info[] data={
            new Info(Info.TYPE_DOUBLE,"���"),
            new Info(Info.TYPE_STRING,"����"),
            new Info(Info.TYPE_STRING,"סַ"),
            new Info(Info.TYPE_STRING,"��ϵ��ʽ"),
            new Info(Info.TYPE_STRING,"���֤"),
            new Info(Info.TYPE_DOUBLE,"����"),
            new Info(Info.TYPE_DATE,"��������"),
            new Info(Info.TYPE_DOUBLE,"�Ա�"),
            new Info(Info.TYPE_STRING,"����"),
            new Info(Info.TYPE_DATE,"��ְʱ��"),
            new Info(Info.TYPE_DATE,"��ְʱ��"),
            new Info(Info.TYPE_STRING,"���п���"),
            new Info(Info.TYPE_STRING,"������ַ"),
            new Info(Info.TYPE_DOUBLE,"����"),
            new Info(Info.TYPE_DOUBLE,"����״̬"),
            new Info(Info.TYPE_STRING,"��ע"),
            new Info(Info.TYPE_DOUBLE,"Լ���¹���"),
            new Info(Info.TYPE_DOUBLE,"�ϼƹ���"),
            new Info(Info.TYPE_DOUBLE,"���๤��"),
            new Info(Info.TYPE_DATE_LIST,"������ȡ��Ϣ"),
            new Info(Info.TYPE_DATE_LIST,"������Ϣ"),
            new Info(Info.TYPE_DATE_LIST,"�������ȡ���")
    };

    /**
     * �û����������
     */
    private static ArrayList<Info> userData=null;





    public static Anbean createWorker(){
        Anbean worker=new Anbean();
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
