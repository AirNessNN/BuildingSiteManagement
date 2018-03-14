package application;
import java.awt.*;
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
	//��Դ������
	private static DBManager dbManager=null; 
	//�û�������
	
	
	
	//��ʼ������
	private static void initialize() throws InterruptedException {
		//��ʼ����ӭ����


        //�򿪻�ӭ����
        showStartWindow();
        Thread.sleep(1000);

        //��ʼ�����ݷ������
		try {
			dbManager=DBManager.prepareDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Application.setLoadMessage("�޷���ʼ�����");
			Application.errorWindow("�޷���ʼ�����");
		}

		Thread.sleep(1000);


	}
	
	
	
	
	
	
	
	
	/**
	 * ������ӭ����
	 */
	public static void showStartWindow() {
		new Thread(()->{
            startWindow =new StartWindow();
            startWindow.setVisible(true);
        }).start();
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
		new Thread(()->{
            if(startWindow !=null&& startWindow.isVisible()) {
                startWindow.setText(message);
            }
        }).start();
	}
	
	
	
	
	
	/*
	 * DbManager����
	 */
	public static void addUser(User user) {
		if(dbManager!=null) {
			dbManager.addUser(user);
		}
	}

	public static void saveSetting(){

    }

    /**
     * ��DB�и����û����ݵ��ļ�
     */
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
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(null, message,"����",JOptionPane.ERROR_MESSAGE);
	}
	public static void informationWindow(String message) {
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(null, message,"��ʾ��Ϣ",JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		//������ʽ
		AnUtils.setLookAndFeel(AnUtils.LOOK_AND_FEEL_DEFAULT);


        //���뾲̬����
        try {
            initialize();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Debug
		MainWindow.getMainWindow(null).setVisible(true);
        //System.out.println(Resource.getApplicationDirectoryPath());

        //ȷ���û���Ϣ
        setLoadMessage("ȷ���û���Ϣ");
        LoginWindow login=new LoginWindow();
        login.resultCallback= (user, password) -> {
            // TODO Auto-generated method stub
            if(user==null||user.equals("")||password==null||password.equals("")){
                Application.errorWindow("�������û��������룡");
            }else {
                boolean loginFlag=false;
                User loginUser=null;
                ArrayList<User>tmp=dbManager.getUserList();
                for(User name:tmp) {
                    if(name.equals(user)&&name.password.equals(password)) {
                        loginFlag=true;
                        //��ɵ�¼
                        loginUser=name;
                        //�رտ�ʼ����
                        closeStartWindow();
                    }
                }
                if(loginFlag){
                    MainWindow.getMainWindow(loginUser).setVisible(true);
                }else {
                    Application.informationWindow("δ�ҵ��û�����ע�ᡣ");
                }
            }
        };
        login.setVisible(true);
        //��ʼ��������

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
