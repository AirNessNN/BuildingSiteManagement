package application;

import component.Chooser;
import dbManager.DBManager;
import dbManager.User;
import dbManager.dbInterface.BuildingSiteOperator;

import java.awt.*;

/**
 * 窗口工厂
 */
public class WindowBuilder {

    private static MainWindow mainWindow=null;
    private static EntryWindow entryWindow=null;
    private static WorkerWindow workerWindow=null;
    private static  AnDataChooser buildingSiteChooser =null;
    private static InfoWindow infoWindow=null;





    /**
     *显示并获取到用户所选的工地，返回工地的名字数组
     */
    public static Object[] showBuildingSiteSelectingWindow(String id){
        Chooser chooser=new Chooser() {
            @Override
            public String[] addEvent() {
                return DBManager.getManager().getFullBuildingSiteName();
            }

            @Override
            public boolean newEvent(String newValue) {
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

        buildingSiteChooser =new AnDataChooser(id,chooser);
        buildingSiteChooser.setChooser(chooser);
        return buildingSiteChooser.getValues();
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
        if (infoWindow!=null&&infoWindow.isVisible()){
            infoWindow.requestFocus();
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
        workerWindow=new WorkerWindow();
        workerWindow.initializeData(id,site);
        workerWindow.setCallback(callBack);
    }


}
