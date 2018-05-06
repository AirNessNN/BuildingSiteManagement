package dbManager;

import application.Loadable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 考勤系统：
 * loading一个工人，对其中的Data类型的Info进行管理
 *
 * 出勤数据结构：
 *          ArrayList《AnArrayBean》是所有工人的出勤信息集合
 *          AnArrayBean是一个工人（用身份证ID识别）的所有工地的出勤信息
 *                  AnArrayBean中的InfoArray《DataValueInfo》一个工地的出勤信息（工地名称识别）
 */
public class CheckInManager implements Loadable ,IManager{
    public static final int MOD_ADD=0;
    public static final int MOD_ALTER=1;
    public static final int MOD_DEL=2;


    private User user;
    private boolean prepared;
    private CheckInManager manager=null;//单例

    private ArrayList<AnArrayBean> workerCheckInInfo=null;//单个工人在所有工地出勤信息的集合




    private CheckInManager(){
        workerCheckInInfo=new ArrayList<>();
        prepared=true;
    }


    /**
     * 操作一个工人的某一天的考勤信息，根据枚举值确认操作是修改、删除、增加
     * @param id 工人身份证
     * @param site 要操作的工地
     * @param date 操作的日期
     * @param mod 操作模式
     * @param value 此次操作的值
     * @return
     */
    public boolean updateCheckInDate(String id, String site, Date date, int mod, Object value){

        Calendar c=Calendar.getInstance();
        int year,month,day;

        year=c.get(Calendar.YEAR);
        month=c.get(Calendar.MONTH);
        day=c.get(Calendar.DATE);

        AnArrayBean worker;
        InfoArray<DateValueInfo> checkInData = null;
        for (AnArrayBean bean:workerCheckInInfo){
            if (bean.getName().equals(id)) {
                worker = bean;
                checkInData=(InfoArray<DateValueInfo>) worker.find(site);
                break;
            }
        }
        if (checkInData==null)
            return false;

        //开始操作
        switch (mod){
            case CheckInManager.MOD_ADD:
                checkInData.addValue(new DateValueInfo(date,value));
                return true;
            case CheckInManager.MOD_ALTER:
                ArrayList<DateValueInfo> tmpl=checkInData.getValues();
                for (DateValueInfo info:tmpl){
                    Calendar cal=Calendar.getInstance();
                    cal.setTime(info.getDate());

                    //日期相等的情况下
                    if (year==cal.get(Calendar.YEAR)&&month==cal.get(Calendar.MONTH)&&day==cal.get(Calendar.DATE)){
                        info.setValue(value);
                        return true;
                    }
                }
                return true;
            case CheckInManager.MOD_DEL:
                /*
                 *CheckInManager没写完，
                 */
                return true;
        }
        return true;
    }

    /**
     * 从DB管理中获取到所有工人列表，更新此子管理器中工人的信息
     * @param beans 工人列表（AnBean）
     */
    public void updateWorkerList(ArrayList<AnBean> beans){
        if (prepared){

           //更新出勤信息
            //新增工人
            for (AnBean bean:beans){
                String id=bean.find(PropertyFactory.LABEL_ID_CARD).getValueString();
               boolean found=false;

               for (AnArrayBean info:workerCheckInInfo){
                    if (id.equals(info.getName()))
                        found=true;
               }

               //未发现就创建这个工人的出勤信息
               if (!found){
                   AnArrayBean tmp=new AnArrayBean();//创建出勤信息
                   tmp.setName(id);

                   //填充工人的工地信息
                   assert DBManager.getManager() != null;
                   ArrayList<String> sites=DBManager.getManager().getWorkerAt(id);
                   for (String siteName:sites){
                       InfoArray<ArrayList> tmpCheckIn=new InfoArray<>();
                       tmpCheckIn.setName(siteName);
                       try {
                           tmp.addInfoArray(tmpCheckIn);
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                   }
                    workerCheckInInfo.add(tmp);
               }
            }

            ArrayList<Integer> delete=new ArrayList<>();//要删除的集合
            //找到已经失效的出勤信息
            for (AnArrayBean bean: workerCheckInInfo){

               boolean found=false;
               for (AnBean b:beans){
                   String id=b.find(PropertyFactory.LABEL_ID_CARD).getValueString();
                    if (bean.getName().equals(id))
                        found=true;
               }
               //未找到ID的，就删除
               if (!found){
                    delete.add(workerCheckInInfo.indexOf(bean));
               }
            }
            //删除
            for (Integer i :delete){
                workerCheckInInfo.remove(i);
            }
            //更新到文件
            //saveToFile();
        }
    }


    /**
     * 更新到文件中持久储存
     * @return
     */
    public boolean saveToFile(){
        if (!prepared)
            return false;
        try {
            DBManager.writeObject(user.getCheckInInfoPath(),workerCheckInInfo);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    public void loading(Object data) {
        if (data instanceof User) {
            user = (User) data;

            try {
                workerCheckInInfo= (ArrayList<AnArrayBean>) DBManager.readObject(user.getCheckInInfoPath());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                workerCheckInInfo=new ArrayList<>();
            }
            prepared=true;
        }
    }

    @Override
    public Object preparedManager() {
        if (manager==null)
            manager=new CheckInManager();
        return manager;
    }

    @Override
    public Object getManager() {
        return manager;
    }
}
