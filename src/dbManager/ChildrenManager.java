package dbManager;

import application.AnUtils;
import application.Application;
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
 *          ArrayList《AnDataTable》是所有工人的出勤信息集合
 *          AnArrayBean是一个工人（用身份证ID识别）的所有工地的出勤信息
 *                  AnArrayBean中的InfoArray《IDataValueItem》一个工地的出勤信息（工地名称识别）
 *
 *  工资数据结构：
 *           ArrayList管理所有工资
 *              AnArrayBean是一个工人的实例，其中包含几个属性，一个是工资领取记录，也该是生活费发放记录
 *                  AnColumn 是上述每个属性的实例，其中存放了日期和领取的钱数量
 */
public class ChildrenManager implements Loadable {
    public static final int MOD_ADD=0;
    public static final int MOD_ALTER=1;
    public static final int MOD_DEL=2;


    private User user;
    private boolean prepared;
    private String path;

    private ArrayList<AnDataTable> workList;//单个工人在所有工地信息的集合




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
    public boolean updateData(String id, String site, Date date, int mod, Object value,String tag) throws Exception{

        AnDataTable worker;
        AnColumn<IDateValueItem> dateList = null;
        for (AnDataTable bean: workList){
            if (bean.getName().equals(id)) {
                worker = bean;
                dateList=(AnColumn<IDateValueItem>) worker.find(site);
                break;
            }
        }
        if (dateList==null)
            return false;

        //开始操作
        switch (mod){
            case ChildrenManager.MOD_ADD:
                for (IDateValueItem item:dateList.getValues()){
                    if (AnUtils.isDateYMDEquality(item.getDate(),date))
                        throw new Exception("在数据中存在相同的日期！");
                }
                dateList.addValue(new DateValueInfo(date,value,tag));
                return true;
            case ChildrenManager.MOD_ALTER: {
                ArrayList<IDateValueItem> tmp = dateList.getValues();//获取该员工在该工地的所有考勤记录
                for (IDateValueItem info : tmp) {
                    //日期相等的情况下
                    if (AnUtils.isDateYMDEquality(date,info.getDate())) {
                        info.setValue(value);
                        info.setTag(tag);
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
     * 更新条目的日期信息
     * @param id
     * @param site
     * @param od
     * @param nd
     * @return
     */
    public boolean updateDate(String id,String site,Date od,Date nd){
        AnDataTable worker;
        AnColumn<IDateValueItem> dateList = null;
        for (AnDataTable bean: workList){
            if (bean.getName().equals(id)) {
                worker = bean;
                dateList=(AnColumn<IDateValueItem>) worker.find(site);
                break;
            }
        }
        if (dateList==null)
            return false;
        //开始修改
        ArrayList<IDateValueItem> tmp=dateList.getValues();
        boolean odFound=false,ndFound=false;
        for (IDateValueItem item:tmp){
            if (AnUtils.isDateYMDEquality(od,item.getDate()))odFound=true;//旧数据必须找到
            if (AnUtils.isDateYMDEquality(nd,item.getDate()))ndFound=true;//新数据必须找不到
        }
        if (odFound&&!ndFound){
            for (IDateValueItem item:tmp){
                if (AnUtils.isDateYMDEquality(od,item.getDate())){
                    DateValueInfo dateValueInfo=(DateValueInfo) item;
                    dateValueInfo.setDate(nd);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 从DB管理中获取到所有工人列表，更新此子管理器中工人的信息
     * 调用次方法，可以保持此子管理器的数据与主管理器中内容保持一致
     */
    public void updateWorkerList(){
        if (prepared){
            ArrayList<AnBean> beans= DBManager.getManager() != null ? DBManager.getManager().loadingWorkerList() : null;

           //更新出勤信息
            //新增工人
            assert beans != null;
            for (AnBean bean:beans){
                String id=bean.find(PropertyFactory.LABEL_ID_CARD).getValueString();
               boolean found=false;

               for (AnDataTable info: workList){
                    if (id.equals(info.getName()))
                        found=true;
               }

               //未发现就创建这个工人的日期信息
               if (!found){
                   AnDataTable tmp=new AnDataTable();//创建出勤信息
                   tmp.setName(id);

                   //填充工人的工地信息
                   assert DBManager.getManager() != null;
                   ArrayList<String> sites=DBManager.getManager().getWorkerAt(id);
                   for (String siteName:sites){
                       AnColumn<ArrayList> tmpCheckIn=new AnColumn<>(false);
                       tmpCheckIn.setName(siteName);
                       try {
                           tmp.addColumn(tmpCheckIn);
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                   }
                    workList.add(tmp);
               }
            }

            ArrayList<Integer> delete=new ArrayList<>();//要删除的集合
            //找到已经失效的日期信息
            for (AnDataTable bean: workList){

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
                workList = (ArrayList<AnDataTable>) DBManager.readObject(path);
            } catch (IOException | ClassNotFoundException e) {
                Application.debug(this,e.toString());
                workList =new ArrayList<>();
            }
            prepared=true;
        }
    }


    /**
     * 获取所有员工的所有工地上的考勤数据
     * @return
     */
    public ArrayList<AnDataTable> getDataBase() {
        return workList;
    }

    /**
     * 从DB中取出指定工人指定工地的日期包装数据
     * @param id 工人身份证
     * @param site 工地
     * @return
     */
    public ArrayList getWorkerDateValueList(String id, String site){
        for (AnDataTable bean : workList){
            if (bean.getName().equals(id)){
                for (AnColumn info :bean.getValues()){
                    if (info.getName().equals(site)){
                        return  info.getValues();
                    }
                }
            }
        }
        return  null;
    }

    /**
     * 更新指定工人指定工地的数据
     * @param id
     * @param site
     * @param source
     */
    public void setWorkerDateValueList(String id,String site,ArrayList<IDateValueItem> source){
        for (AnDataTable bean:workList){
            if (bean.getName().equals(id)){
                for (AnColumn info:bean.getValues()){
                    if (info.getName().equals(site)){
                        info.setValues(source);
                        return;
                    }
                }
            }
        }
    }


    /**
     * 返回一个工人的实例
     * @param id 身份证
     * @return
     */
    public AnDataTable getWorker(String id){
        for (AnDataTable bean:workList){
            if (bean.getName().equals(id))
                return bean;
        }
        return null;
    }
}