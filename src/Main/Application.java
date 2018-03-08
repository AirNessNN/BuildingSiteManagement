package Main;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Main.Login.ILoginResultCallback;
import Resoure.Resource;

public class Application {
	
	public static final String VERSION="工程版";
	
	//欢迎界面
	private static StartWidnwo startWidnwo=null;

	private static MainWindow mainWindow=null;
	
	
	
	
	private static void initialize() {
		startWidnwo=new StartWidnwo();
	}
	
	
	
	
	
	
	
	
	/**
	 * 开启欢迎界面
	 */
	public static void showStartWindow() {
		startWidnwo.setVisible(true);
	}
	
	/**
	 * 关闭欢迎界面
	 */
	public static void closeStartWindow() {
		startWidnwo.setVisible(false);
	}
	
	/**
	 * 设置欢迎页面载入状态
	 * @param message
	 */
	public static void setLoadMessage(String message) {
		if(startWidnwo!=null) {
			startWidnwo.setText(message);
		}
	}
	
	
	
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		
		//事件列队
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//载入静态数据
					initialize();
					
					//Debug
					System.out.println(Resource.getApplicationDirectoryPath());
					
					//打开欢迎窗口
					showStartWindow();
					//读取数据库
					
					//复制到内存
					
					//准备数据
					
					//确认用户信息
					
					
					setLoadMessage("确认用户信息");
					Login login=new Login();
					login.resultCallback=new ILoginResultCallback() {
						
						@Override
						public void loginResult(String user, String password) {
							// TODO Auto-generated method stub
							if(user.equals("test")&&password.equals("123456")) {
								//准备该用户数据
								
								//读取数据到内存
								
								//显示数据
								
								//完成登录
								closeStartWindow();
								mainWindow=new MainWindow();
								mainWindow.setVisible(true);
							}else {
								JOptionPane.showMessageDialog(null, "用户名或密码错误！", "登录提示", JOptionPane.INFORMATION_MESSAGE);
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

	

}
