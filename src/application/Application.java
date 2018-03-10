package application;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import application.LoginWindow.ILoginResultCallback;
import dbManager.DBManager;

/**
 * �������࣬���Ƴ�������������
 * @author AN
 *
 */
public class Application {
	
	static final String VERSION="���̰�";
	
	//��ӭ����
	private static StartWindow startWindow =null;
	//������
	private static MainWindow mainWindow=null;
	//��Դ������
	private static DBManager dbManager=null; 
	//�û�������
	
	
	
	//��ʼ������
	private static void initialize() {
		//��ʼ����ӭ����
		startWindow =new StartWindow();
		
		//��ʼ�����ݷ������
		try {
			dbManager=DBManager.prepareDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Application.setLoadMessage("�޷���ʼ�����");
			Application.errorWindow("�޷���ʼ�����");
		}
	}
	
	
	
	
	
	
	
	
	/**
	 * ������ӭ����
	 */
	public static void showStartWindow() {
		startWindow.setVisible(true);
	}
	
	/**
	 * �رջ�ӭ����
	 */
	public static void closeStartWindow() {
		startWindow.setVisible(false);
	}
	
	/**
	 * ���û�ӭҳ������״̬
	 * @param message
	 */
	public static void setLoadMessage(String message) {
		if(startWindow !=null&& startWindow.isVisible()) {
			startWindow.setText(message);
		}
	}
	
	
	
	
	
	/*
	 * DbManager����
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
	 * ����ĵ�����ʾ
	 */
	public static void errorWindow(String message) {
		JOptionPane.showMessageDialog(null, message,"����",JOptionPane.ERROR_MESSAGE);
	}
	public static void informationWindow(String message) {
		JOptionPane.showMessageDialog(null, message,"��ʾ��Ϣ",JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		//������ʽ
		AnUtils.setLookAndFeel(AnUtils.LOOK_AND_FEEL_DEFAULT);
		
		//�¼��ж�
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//���뾲̬����
					initialize();
					
					//Debug
					//System.out.println(Resource.getApplicationDirectoryPath());
					
					
					//�򿪻�ӭ����
					showStartWindow();
					//��ȡ���ݿ�
					
					//���Ƶ��ڴ�
					
					//׼������
					
					
					setLoadMessage("ȷ���û���Ϣ");
					
					
					
					//ȷ���û���Ϣ
					
					
					
					
					LoginWindow login=new LoginWindow();
					login.resultCallback=new ILoginResultCallback() {
						
						@Override
						public void loginResult(String user, String password) {
							// TODO Auto-generated method stub
							ArrayList<User>tmp=dbManager.getUserList();
							for(User name:tmp) {
								if(name.equals(user)&&name.password.equals(password)) {
									//׼�����û�����
									
									//��ȡ���ݵ��ڴ�
									
									//��ʾ����
									
									//��ɵ�¼
									mainWindow=new MainWindow();
									mainWindow.setVisible(true);
									//�رտ�ʼ����
									closeStartWindow();
								}else {
									JOptionPane.showMessageDialog(null, "�û������������", "��¼��ʾ", JOptionPane.ERROR_MESSAGE);
								}
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

	
	/**
	 * ����������
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
