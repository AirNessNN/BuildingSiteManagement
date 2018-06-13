package application;

import component.AnDataChooser;
import component.Chooser;
import dbManager.DBManager;
import dbManager.PropertyFactory;
import dbManager.User;

import java.awt.*;
import java.util.ArrayList;

/**
 * 窗口工厂
 */
public class WindowBuilder {

    private static MainWindow mainWindow=null;
    private static EntryWindow entryWindow=null;
    private static WorkerInfoWindow workerWindow=null;
    private static AnDataChooser buildingSiteChooser =null;
    private static InfoWindow infoWindow=null;





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
                    Application.informationWindow(e.getMessage());
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
     * @param user 需要载入的用户
     */
    public static void showMainWindow(User user){
        mainWindow=MainWindow.getMainWindow(user);
        mainWindow.setVisible(true);
    }


    public static void showInfoWindow(String id, String site, CloseCallback callBack){
        if (infoWindow!=null){
            infoWindow.dispose();
            System.gc();
        }
        infoWindow=new InfoWindow();
        infoWindow.initializeWorker(id,site);
        infoWindow.setCallback(callBack);
        infoWindow.setVisible(true);
    }


    public static void showWorkWindow(String id , String site, CloseCallback callBack){
        if (workerWindow!=null)
            if (workerWindow.isVisible())
                workerWindow.dispose();
        long time=System.currentTimeMillis();
        workerWindow=new WorkerInfoWindow();
        workerWindow.initializeData(id,site);
        workerWindow.setCallback(callBack);
        workerWindow.setVisible(true);
        System.out.println(System.currentTimeMillis()-time);
    }


    static PropertyWindow propertyWindow=null;
    public static void showPropertyWindow(){
        if (propertyWindow==null||!propertyWindow.isVisible())
            propertyWindow=new PropertyWindow();
        propertyWindow.setVisible(true);
        propertyWindow.requestFocus();
    }


}
