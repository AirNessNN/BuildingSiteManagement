package application;
import java.awt.*;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import component.AnPopDialog;
import dbManager.DBManager;
import dbManager.User;
import resource.Resource;

/**
 * 主程序类，控制程序各种组件运行
 * @author AN
 *
 */
public class Application {
	
	static final String VERSION="工程版";
	public static final Date TODAY=new Date();

	private static ExecutorService executorService=null;
	
	//欢迎界面
	private static StartWindow startWindow =null;
	//资源管理类
	private static DBManager dbManager=null;
	//用户管理类
	
	
	
	//初始化程序
	private static void initialize() throws InterruptedException {

		executorService= Executors.newCachedThreadPool();

		//初始化欢迎界面
        //打开欢迎窗口
        showStartWindow();
        Thread.sleep(500);

        //初始化数据服务组件
		try {
			dbManager=DBManager.prepareDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Application.setLoadMessage("无法初始化组件");
			Application.errorWindow("无法初始化组件");
		}

		Thread.sleep(500);
	}
	
	
	
	
	
	
	
	
	/**
	 * 开启欢迎界面
	 */
	private static void showStartWindow() {
		new Thread(()->{
            startWindow =new StartWindow();
            startWindow.setVisible(true);
        }).start();
	}
	
	/**
	 * 关闭欢迎界面
	 */
	private static void closeStartWindow() {
		startWindow.setVisible(false);
	}
	
	/**
	 * 设置欢迎页面载入状态
	 * @param message
	 */
	public static void setLoadMessage(String message) {
		startService(() -> {
            if(startWindow !=null&& startWindow.isVisible()) {
                startWindow.setText(message);
            }
        });
	}
	
	
	
	
	
	/*
	 * DbManager操作
	 */
	static void addUser(User user) {
		if(dbManager!=null) {
			dbManager.addUser(user);
			dbManager.loadUser(user);
		}
	}

	static void saveSetting(){

    }

    /**
     * 在DB中更新用户数据到文件
     */
	static void updateUserData() {
		if(dbManager!=null) {
			try {
				//保存用户配置文件
				dbManager.updateUserListFile();
				//保存用户数据
				dbManager.saveData();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	

	
	
	/*
	 * 
	 * 程序的弹窗提示
	 */
	public static void errorWindow(String message) {
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(null, message,"错误",JOptionPane.ERROR_MESSAGE);
	}
	public static void informationWindow(String message) {
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(null, message,"提示信息",JOptionPane.INFORMATION_MESSAGE);

	}

	public static String inputWindow(Component parent,String message,String title){
		Toolkit.getDefaultToolkit().beep();
		return JOptionPane.showInputDialog(parent,message,title,JOptionPane.INFORMATION_MESSAGE);
	}


	/**
	 * 线程操作
	 * @param run 任务
	 */
	public static void startService(Runnable run){
		if (executorService==null){
			new Thread(run).start();
			return;
		}
		executorService.execute(run);
	}


	/**
	 * 填充工人到工地中去
	 * @param component 窗口
	 * @param ids 工人id
	 * @param vectors 数据源
	 * @param siteName 工地名称
	 */
	static void fillWorkers(Component component, String[] ids, Vector<Vector> vectors, String siteName){
		int i=0;
		for (String id : ids) {
			Vector tmpPn=null;
			if (vectors.size()>0)tmpPn=vectors.get(i++);

			//获取协议工价
			double d=0d;
			if (tmpPn!=null){
				try{
					d=Double.valueOf((String) tmpPn.get(2));
				}catch (Exception ex){
					AnPopDialog.show(component,"协议工价转换错误，采用默认值：0",AnPopDialog.LONG_TIME);
				}
			}

			//获取日期
			Date date=new Date();
			if (tmpPn!=null){
				try {
					date=AnUtils.getDate((String) tmpPn.get(4),Resource.DATE_FORMATE);
				} catch (ParseException e1) {
					AnPopDialog.show(component,"入职日期转换错误，采用默认值：今天",AnPopDialog.LONG_TIME);
				}
			}

			//获取类型
			String type="";
			if (tmpPn!=null)type= (String) tmpPn.get(3);

			assert DBManager.getManager() != null;
			DBManager.getManager().addWorkerToSite(id,siteName,d,type,date);
		}
	}








	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		//设置样式
		AnUtils.setLookAndFeel(AnUtils.LOOK_AND_FEEL_DEFAULT);

        //载入静态数据
        try {
            initialize();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

		//Debug
		assert DBManager.getManager() != null;
		DBManager.getManager().loadUser(new User("test","951753","",""));
		MainWindow.getMainWindow().setVisible(true);
		//

        //确认用户信息
        setLoadMessage("确认用户信息");
        LoginWindow login=new LoginWindow();
        login.resultCallback= (user, password) -> {
            // TODO Auto-generated method stub
            if(user==null||user.equals("")||password==null||password.equals("")){
                Application.errorWindow("请输入用户名或密码！");
			}else {
                boolean loginFlag=false;
                User loginUser=null;
                ArrayList<User>tmp=dbManager.getUserList();
                for(User name:tmp) {
                    if(name.equals(user)&&name.password.equals(password)) {
                        loginFlag=true;
                        //完成登录
                        loginUser=name;
                        //关闭开始窗口
                        closeStartWindow();
                        login.dispose();
                    }
                }
				if(loginFlag){
                	DBManager.getManager().loadUser(loginUser);
					MainWindow.getMainWindow().setVisible(true);
                }else {
                    Application.informationWindow("用户名或密码错误，请重试");
                }
            }
        };
        login.setVisible(true);
        //Debug
        closeStartWindow();
        login.setVisible(false);
        //
	}





	/**
	 * 输出信息
	 * @param owner
	 * @param str
	 */
	public static void debug(Object owner,Object str){
		System.out.println(owner.getClass().getName()+"："+str);
	}

	
	/**
	 * 程序设置类
	 * @author AN
	 *
	 */
	class Setting implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		public Setting() {
			// TODO Auto-generated constructor stub
		}
		
	}

}
