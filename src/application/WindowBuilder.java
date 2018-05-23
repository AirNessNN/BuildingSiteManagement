package application;

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
    private static BuildingSiteOperator buildingSiteChooser =null;





    /**
     *显示并获取到用户所选的工地，返回工地的名字数组
     */
    public static Object[] showBuildingSiteSelectingWindow(String id){
        buildingSiteChooser =new BuildingSiteChooser(id);
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



}
