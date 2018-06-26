package application;

import component.AnDataChooser;
import component.AnPopDialog;
import component.Chooser;
import dbManager.CheckMgr;
import dbManager.DBManager;
import dbManager.PropertyFactory;
import resource.Resource;

import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 窗口工厂
 */
public class WindowBuilder {

    private static MainWindow mainWindow=null;
    private static EntryWindow entryWindow=null;
    private static WorkerInfoWindow workerWindow=null;
    private static AnDataChooser buildingSiteChooser =null;
    private static InfoWindow infoWindow=null;
    private static SiteInfoWindow siteInfoWindow=null;
    private static QuickCheckWindow quickCheckWindow=null;





    /**
     *显示并获取到用户所选的工地，返回工地的名字数组
     */
    public static void showBuildingSiteSelectingWindow(String id,CloseCallback callback){
        Chooser chooser=new Chooser() {
            @Override
            public String[] addEvent() {
                return DBManager.getManager().getFullBuildingSiteName();
            }

            @Override
            public boolean newEvent(String[] values,String newValue) {
                try {
                    DBManager.getManager().createBuildingSite(newValue);
                    return true;
                } catch (Exception e) {
                    Application.informationWindow(e.toString());
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            public void done(String[] values) {

            }

            @Override
            public String[] getAddText() {
                return new String[]{"从列表中选择工地","选择工地",""};
            }

            @Override
            public String[] getNewText() {
                return new String[]{"添加一个新工地。","输入工地名称"};
            }
        };
        ArrayList<String> arrayList;
        assert DBManager.getManager() != null;
        arrayList= DBManager.getManager().getWorkerAt(id);
        buildingSiteChooser =new AnDataChooser("工地选择器","从右边的按钮进行操作",chooser,true,arrayList.toArray());

        Object[] tmp=buildingSiteChooser.getValues();
        String[] sites= new String[tmp.length];
        for (int i=0;i<tmp.length;i++){
            sites[i]= (String) tmp[i];
        }

        HorizonDataFiller filler=new HorizonDataFiller();
        filler.setKeys("工地", sites);
        filler.addTextBox(PropertyFactory.LABEL_DEAL_SALARY);
        filler.addComoBox(
                PropertyFactory.LABEL_WORKER_TYPE,
                AnUtils.toStringArray(DBManager.getManager().getWorkerProperty(PropertyFactory.LABEL_WORKER_TYPE).toArray())
        );
        filler.addCalendar(PropertyFactory.LABEL_ENTRY_TIME,"点击选择日期");
        filler.setCallback((values -> {
            if (callback!=null)callback.callback(sites,values.get(0),values.get(1),values.get(2));
            return true;
        }));
        filler.setVisible(true);
    }

    public static Component getBuildingSiteChooser(){
        if (buildingSiteChooser==null)
            return null;
        return buildingSiteChooser.getComponent();
    }

    /**
     * 显示主窗口
     */
    public static void showMainWindow(){
        mainWindow=MainWindow.getMainWindow();
        mainWindow.setVisible(true);
    }


    static void showInfoWindow(String id, String site, CloseCallback callBack){
        if (infoWindow!=null){
            infoWindow.dispose();
        }
        infoWindow=new InfoWindow();
        infoWindow.initializeWorker(id,site);
        infoWindow.setCallback(callBack);
        infoWindow.setVisible(true);
    }

    static void closeInfoWindow(){
        if (infoWindow!=null)infoWindow.dispose();
    }


    static void showWorkWindow(String id, String site, CloseCallback callBack){
        if (workerWindow!=null)
            if (workerWindow.isVisible())
                workerWindow.setVisible(false);
        if (workerWindow==null)workerWindow=new WorkerInfoWindow();
        long time=System.currentTimeMillis();
        workerWindow.initializeData(id,site);
        workerWindow.setCallback(callBack);
        workerWindow.setVisible(true);
        System.out.println("工人信息窗口打开耗时"+(System.currentTimeMillis()-time));
    }

    static void closeWorkWindow(){
        if (workerWindow!=null)workerWindow.dispose();
    }


    private static PropertyWindow propertyWindow=null;
    static void showPropertyWindow(){
        if (propertyWindow==null||!propertyWindow.isVisible())
            propertyWindow=new PropertyWindow();
        propertyWindow.setVisible(true);
        propertyWindow.requestFocus();
    }

    static void showSiteInfoWindow(String siteName, CloseCallback callback){
        long time=System.currentTimeMillis();
        if (siteInfoWindow!=null){
            siteInfoWindow.dispose();
        }
        siteInfoWindow=new SiteInfoWindow(siteName);
        siteInfoWindow.setCallback(callback);
        siteInfoWindow.setVisible(true);
        System.out.println("工地信息窗口打开耗时"+(System.currentTimeMillis()-time));
    }

    static void closeSiteInfoWindow(){
        if (siteInfoWindow!=null)siteInfoWindow.dispose();
    }


    public static void showQuickCheckWindow(String siteName){
        CheckMgr mgr=new CheckMgr(siteName);
        if (quickCheckWindow!=null){
            quickCheckWindow.dispose();
        }
        quickCheckWindow=new QuickCheckWindow(mgr);
        quickCheckWindow.setTitle("快速考勤");
        quickCheckWindow.setVisible(true);
    }

    static void closeQuickCheckWindow(){
        if (quickCheckWindow!=null)quickCheckWindow.dispose();
    }

}
