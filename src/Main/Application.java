package Main;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Main.Login.ILoginResultCallback;
import Resoure.Resource;

public class Application {
	
	public static final String VERSION="���̰�";
	
	//��ӭ����
	private static StartWidnwo startWidnwo=null;

	private static MainWindow mainWindow=null;
	
	
	
	
	private static void initialize() {
		startWidnwo=new StartWidnwo();
	}
	
	
	
	
	
	
	
	
	/**
	 * ������ӭ����
	 */
	public static void showStartWindow() {
		startWidnwo.setVisible(true);
	}
	
	/**
	 * �رջ�ӭ����
	 */
	public static void closeStartWindow() {
		startWidnwo.setVisible(false);
	}
	
	/**
	 * ���û�ӭҳ������״̬
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
		
		
		//�¼��ж�
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//���뾲̬����
					initialize();
					
					//Debug
					System.out.println(Resource.getApplicationDirectoryPath());
					
					//�򿪻�ӭ����
					showStartWindow();
					//��ȡ���ݿ�
					
					//���Ƶ��ڴ�
					
					//׼������
					
					//ȷ���û���Ϣ
					
					
					setLoadMessage("ȷ���û���Ϣ");
					Login login=new Login();
					login.resultCallback=new ILoginResultCallback() {
						
						@Override
						public void loginResult(String user, String password) {
							// TODO Auto-generated method stub
							if(user.equals("test")&&password.equals("123456")) {
								//׼�����û�����
								
								//��ȡ���ݵ��ڴ�
								
								//��ʾ����
								
								//��ɵ�¼
								closeStartWindow();
								mainWindow=new MainWindow();
								mainWindow.setVisible(true);
							}else {
								JOptionPane.showMessageDialog(null, "�û������������", "��¼��ʾ", JOptionPane.INFORMATION_MESSAGE);
							}
						}
					};
					login.setVisible(true);
					//��ʼ��������
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	

}
