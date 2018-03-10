package application;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import application.LoginWindow.ILoginResultCallback;
import dbManager.DBManager;

/**
 * 主程序类，控制程序各种组件运行
 * @author AN
 *
 */
public class Application {
	
	static final String VERSION="工程版";
	
	//欢迎界面
	private static StartWindow startWindow =null;
	//主窗口
	private static MainWindow mainWindow=null;
	//资源管理类
	private static DBManager dbManager=null; 
	//用户管理类
	
	
	
	//初始化程序
	private static void initialize() {
		//初始化欢迎界面
		startWindow =new StartWindow();
		
		//初始化数据服务组件
		try {
			dbManager=DBManager.prepareDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Application.setLoadMessage("无法初始化组件");
			Application.errorWindow("无法初始化组件");
		}
	}
	
	
	
	
	
	
	
	
	/**
	 * 开启欢迎界面
	 */
	public static void showStartWindow() {
		startWindow.setVisible(true);
	}
	
	/**
	 * 关闭欢迎界面
	 */
	public static void closeStartWindow() {
		startWindow.setVisible(false);
	}
	
	/**
	 * 设置欢迎页面载入状态
	 * @param message
	 */
	public static void setLoadMessage(String message) {
		if(startWindow !=null&& startWindow.isVisible()) {
			startWindow.setText(message);
		}
	}
	
	
	
	
	
	/*
	 * DbManager操作
	 */
	public static void addUser(User user) {
		if(dbManager!=null) {
			dbManager.addUser(user);
		}
	}
	
	public static void updateUserData() {
		if(dbManager!=null) {
			try {
				dbManager.updateUserListFile();
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
		JOptionPane.showMessageDialog(null, message,"错误",JOptionPane.ERROR_MESSAGE);
	}
	public static void informationWindow(String message) {
		JOptionPane.showMessageDialog(null, message,"提示信息",JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		//设置样式
		AnUtils.setLookAndFeel(AnUtils.LOOK_AND_FEEL_DEFAULT);
		
		//事件列队
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//载入静态数据
					initialize();
					
					//Debug
					//System.out.println(Resource.getApplicationDirectoryPath());
					
					
					//打开欢迎窗口
					showStartWindow();
					//读取数据库
					
					//复制到内存
					
					//准备数据
					
					
					setLoadMessage("确认用户信息");
					
					
					
					//确认用户信息
					
					
					
					
					LoginWindow login=new LoginWindow();
					login.resultCallback=new ILoginResultCallback() {
						
						@Override
						public void loginResult(String user, String password) {
							// TODO Auto-generated method stub
							ArrayList<User>tmp=dbManager.getUserList();
							for(User name:tmp) {
								if(name.equals(user)&&name.password.equals(password)) {
									//准备该用户数据
									
									//读取数据到内存
									
									//显示数据
									
									//完成登录
									mainWindow=new MainWindow();
									mainWindow.setVisible(true);
									//关闭开始窗口
									closeStartWindow();
								}else {
									JOptionPane.showMessageDialog(null, "用户名或密码错误！", "登录提示", JOptionPane.ERROR_MESSAGE);
								}
							}
							
						}
					};
					login.setVisible(true);
					//开始载入数据
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
