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
 *          ArrayList《DataTable》是所有工人的出勤信息集合
 *          AnArrayBean是一个工人（用身份证ID识别）的所有工地的出勤信息
 *                  AnArrayBean中的InfoArray《IDataValueItem》一个工地的出勤信息（工地名称识别）
 *
 *  工资数据结构：
 *           ArrayList管理所有工资
 *              AnDataTable是一个工人的实例，其中包含几个属性，一个是工资领取记录，也该是生活费发放记录
 *                  Column 是上述每个属性的实例，其中存放了日期和领取的钱数量
 */
public class ChildrenManager implements Loadable {
    public static final int MOD_ADD=0;
    public static final int MOD_ALTER=1;
    public static final int MOD_DEL=2;


    private boolean prepared;
    private String path;

    private ArrayList<DataTable> workList;//单个工人在所有工地信息的集合




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
     * @param value 此次操作的值
     * @return 操作成功返回true，操作失败或者无操作返回false
     */
    public boolean updateData(String id, String site, Date date, Object value,String tag){

        DataTable worker;
        Column siteData = null;
        for (DataTable bean: workList){
            if (bean.getName().equals(id)) {
                worker = bean;
                siteData=worker.findColumn(site);
                break;
            }
        }
        if (siteData==null)
            return false;

        ArrayList<IDateValueItem> tmp1 = siteData.getValues();
        for (IDateValueItem item:tmp1)
            if (AnUtils.isDateYMDEquality(item.getDate(), date)) {
                item.setValue(value);
                item.setTag(tag);
                return true;
            }
        return siteData.addValue(new DateValueInfo(date,value,tag));
    }

    /**
     * 删除一条数据
     * @param id 身份信息
     * @param siteName 工地名称
     * @param date 日期
     * @return 成功返回true
     */
    public boolean deleteData(String id,String siteName,Date date){
        DataTable worker;
        Column siteData = null;
        for (DataTable bean: workList){
            if (bean.getName().equals(id)) {
                worker = bean;
                siteData=worker.findColumn(siteName);
                break;
            }
        }
        if (siteData==null)
            return false;

        ArrayList<IDateValueItem> tmpValue = siteData.getValues();
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

    /**
     * 更新条目的日期信息
     * @param id id
     * @param site 工地
     * @param od 旧日期
     * @param nd 新日期
     * @return 成功返回true
     */
    public boolean updateDate(String id,String site,Date od,Date nd){
        DataTable worker;
        Column dateList = null;
        for (DataTable bean: workList){
            if (bean.getName().equals(id)) {
                worker = bean;
                dateList=worker.findColumn(site);
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
    public void updateWorkerList() throws Exception {
        if (prepared){
            DBManager manager=DBManager.getManager();
            assert manager != null;

            String[] siteNames=manager.getFullBuildingSiteName();

            for (String siteName : siteNames){
                DataTable site=manager.getBuildingSite(siteName);
                for (int i=0;i<site.findColumn(PropertyFactory.LABEL_ID_CARD).size();i++){
                    String id= (String) site.findColumn(PropertyFactory.LABEL_ID_CARD).get(i);
                    /*
                    上面的两层循环用于遍历工地中的所有工人，
                    主要获得两个参数：工地和身份证。
                    这两个参数用来更新子管理器的所有数据。

                    子管理器是一个DataTable一个工人，
                    Column是工地的所有记录。
                    子管理器中没有找到工地，就需要添加进入，因为所有数据以工地表中的数据为准
                     */
                    //判断此工人是否存在
                    boolean found=false;
                    for (DataTable worker:workList) if (worker.getName().equals(id))found=true;
                    if (!found){//没有找到就要添加一个工人和此工地
                        DataTable dt=new DataTable(id);
                        Column column=new Column(false,false,siteName,new ArrayList());
                        dt.addColumn(column);
                        workList.add(dt);
                        Application.debug(this,id+"添加进管理器");
                        continue;
                    }
                    //判断工人的工地是否存在
                    DataTable child=getWorker(id);//获取子管理器的工地
                    if (child.findColumn(siteName)==null){
                        //这里在子管理器中没找到这个工地
                        child.addColumn(new Column(false,false,siteName,new ArrayList()));
                        Application.debug(this,siteName+"工地添加");
                    }
                }
            }

            //删除子管理器中多余的数据
            ArrayList<DataTable> deleteTable=new ArrayList<>();//要删除的工人
            ArrayList<DataTable> foundTable=new ArrayList<>();//要删除工地的工人表
            ArrayList<Column> deleteColumn=new ArrayList<>();//要删除的工地
            for (DataTable dt : workList){
                //获取此子管理器中的所有工地
                for (Column column : dt.getValues()){
                    //判断工人是否在
                    String id=dt.getName();
                    //工人没有在任何一个工地中工作
                    if (manager.getWorkerAt(id).size()==0) {
                        deleteTable.add(dt);
                        continue;
                    }
                    //工人没有在这个工地中工作
                    if (!manager.getWorkerAt(id).contains(column.getName())){
                        deleteColumn.add(column);
                        foundTable.add(dt);
                        continue;
                    }
                }
            }
            //删除工人
            for (DataTable dataTable:deleteTable){
                workList.remove(dataTable);
            }
            //删除
            for (int i=0;i<foundTable.size();i++){
                foundTable.get(i).removeColumn(deleteColumn.get(i));
            }

            //更新到文件
            //saveToFile();
        }
    }


    /**
     * <h2>移除该工地</h2>
     * @param id 身份证
     * @param siteName 工地名称
     */
    public void removeSite(String id ,String siteName){
        DataTable worker=null;
        for (DataTable tmpWorker:workList){
            if (tmpWorker.getName().equals(id)){
                worker=tmpWorker;
                break;
            }
        }
        if (worker!=null)
            worker.removeRow(worker.selectRow(PropertyFactory.LABEL_SITE,siteName));
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
            User user = (User) data;

            try {
                if (path==null)
                    return;
                workList = (ArrayList<DataTable>) DBManager.readObject(path);
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
    public ArrayList<DataTable> getDataBase() {
        return workList;
    }

    /**
     * 从DB中取出指定工人指定工地的日期包装数据
     * @param id 工人身份证
     * @param site 工地
     * @return
     */
    public ArrayList getWorkerDateValueList(String id, String site){
        for (DataTable bean : workList){
            if (bean.getName().equals(id)){
                for (Column info :bean.getValues()){
                    if (info.getName().equals(site)){
                        return  info.getValues();
                    }
                }
            }
        }
        return  null;
    }


    public Object getValueAt(String id,String siteName,Date date){
        DataTable worker=getWorker(id);
        Column site=worker.findColumn(siteName);
        for (int i=0;i<site.size();i++){
            IDateValueItem item= (IDateValueItem) site.get(i);
            if (AnUtils.isDateYMDEquality(item.getDate(),date)){
                return item.getValue();
            }
        }
        return null;
    }
    /**
     * 更新指定工人指定工地的数据
     * @param id
     * @param site
     * @param source
     */
    public void setWorkerDateValueList(String id,String site,ArrayList<IDateValueItem> source){
        for (DataTable bean:workList){
            if (bean.getName().equals(id)){
                for (Column info:bean.getValues()){
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
    public DataTable getWorker(String id){
        for (DataTable bean:workList){
            if (bean.getName().equals(id))
                return bean;
        }
        return null;
    }
}
