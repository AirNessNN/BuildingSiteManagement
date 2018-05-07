package dbManager;

import application.AnUtils;
import application.Loadable;
import component.IDateValueItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * 考勤系统：
 * loading一个工人，对其中的Data类型的Info进行管理
 *
 * 出勤数据结构：
 *          ArrayList《AnArrayBean》是所有工人的出勤信息集合
 *          AnArrayBean是一个工人（用身份证ID识别）的所有工地的出勤信息
 *                  AnArrayBean中的InfoArray《IDataValueItem》一个工地的出勤信息（工地名称识别）
 */
public class ChildrenManager implements Loadable {
    public static final int MOD_ADD=0;
    public static final int MOD_ALTER=1;
    public static final int MOD_DEL=2;


    private User user;
    private boolean prepared;
    private String path;

    private ArrayList<AnArrayBean> workList;//单个工人在所有工地信息的集合




    public ChildrenManager(String path){
        workList =new ArrayList<>();
        prepared=true;
        this.path=path;
    }


    /**
     * 核心方法  ：操作一个工人的某一天的信息，根据枚举值确认操作是修改、删除、增加
     * @param id 工人身份证
     * @param site 要操作的工地
     * @param date 操作的日期
     * @param mod 操作模式
     * @param value 此次操作的值
     * @return 操作成功返回true，操作失败返回false
     */
    public boolean updateData(String id, String site, Date date, int mod, Object value){

        AnArrayBean worker;
        InfoArray<IDateValueItem> dateList = null;
        for (AnArrayBean bean: workList){
            if (bean.getName().equals(id)) {
                worker = bean;
                dateList=(InfoArray<IDateValueItem>) worker.find(site);
                break;
            }
        }
        if (dateList==null)
            return false;

        //开始操作
        switch (mod){
            case ChildrenManager.MOD_ADD:
                dateList.addValue(new DateValueInfo(date,value));
                return true;
            case ChildrenManager.MOD_ALTER: {
                ArrayList<IDateValueItem> tmp = dateList.getValues();//获取该员工在该工地的所有考勤记录
                for (IDateValueItem info : tmp) {
                    //日期相等的情况下
                    if (AnUtils.isDateYMDEquality(date,info.getDate())) {
                        info.setValue(value);
                        return true;
                    }
                }
                return true;
            }
            case ChildrenManager.MOD_DEL: {
                ArrayList<IDateValueItem> tmpValue = dateList.getValues();
                IDateValueItem delete = null;
                for (IDateValueItem info : tmpValue) {

                    if (AnUtils.isDateYMDEquality(date,info.getDate())) {
                        delete=info;
                    }
                }
                if (delete!=null){
                    tmpValue.remove(delete);
                    return true;
                }
                return false;
            }
        }
        return false;
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

               for (AnArrayBean info: workList){
                    if (id.equals(info.getName()))
                        found=true;
               }

               //未发现就创建这个工人的日期信息
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
                    workList.add(tmp);
               }
            }

            ArrayList<Integer> delete=new ArrayList<>();//要删除的集合
            //找到已经失效的日期信息
            for (AnArrayBean bean: workList){

               boolean found=false;
               for (AnBean b:beans){
                   String id=b.find(PropertyFactory.LABEL_ID_CARD).getValueString();
                    if (bean.getName().equals(id))
                        found=true;
               }
               //未找到ID的，就删除
               if (!found){
                    delete.add(workList.indexOf(bean));
               }
            }
            //删除
            for (Integer i :delete){
                workList.remove(i);
            }
            //更新到文件
            saveToFile();
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
            DBManager.writeObject(path, workList);
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
                if (path==null)
                    return;
                workList = (ArrayList<AnArrayBean>) DBManager.readObject(path);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.toString());
                workList =new ArrayList<>();
            }
            prepared=true;
        }
    }


    /**
     * 获取所有员工的所有工地上的考勤数据
     * @return
     */
    public ArrayList<AnArrayBean> getDataBase() {
        return workList;
    }

    /**
     * 从DB中取出指定工人指定工地的日期包装数据
     * @param id 工人身份证
     * @param site 工地
     * @return
     */
    public ArrayList getWorkerDateValueList(String id, String site){
        for (AnArrayBean bean : workList){
            if (bean.getName().equals(id)){
                for (InfoArray info :bean.getValues()){
                    if (info.getName().equals(site)){
                        return  info.getValues();
                    }
                }
            }
        }
        return  null;
    }
}
