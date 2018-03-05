package Main;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Main.Login.ILoginResultCallback;

public class Application {
	
	public static final String VERSION="工程版";
	
	//欢迎界面
	private static StartWidnwo startWidnwo=null;

	private JFrame frame;
	
	
	
	
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
					//打开欢迎窗口
					showStartWindow();
					Login login=new Login();
					login.resultCallback=new ILoginResultCallback() {
						
						@Override
						public void loginResult(boolean result) {
							// TODO Auto-generated method stub
							if(result) {
								login.dispose();
								new MainWindow().setVisible(true);
							}else {
								JOptionPane.showMessageDialog(null, "用户名或密码错误!","提示消息",JOptionPane.INFORMATION_MESSAGE);
							}
						}
					};
					login.setVisible(true);
					//开始载入数据
					
					closeStartWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	

}
