package Main;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Main.Login.ILoginResultCallback;

public class Application {
	
	public static final String VERSION="���̰�";
	
	//��ӭ����
	private static StartWidnwo startWidnwo=null;

	private JFrame frame;
	
	
	
	
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
					//�򿪻�ӭ����
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
								JOptionPane.showMessageDialog(null, "�û������������!","��ʾ��Ϣ",JOptionPane.INFORMATION_MESSAGE);
							}
						}
					};
					login.setVisible(true);
					//��ʼ��������
					
					closeStartWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	

}
