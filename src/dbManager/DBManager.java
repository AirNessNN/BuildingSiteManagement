package dbManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import application.Application;
import application.User;
import resource.Resource;

/**
 * ���ݹ������
 * @author AN
 *
 */
public class DBManager {
	
	private IRunStateCallback runStateCallback=null;
	public void setRunStateCallback(IRunStateCallback callback) {
		runStateCallback=callback;
	}

	/**
	 * ����״̬�ص����ж��Ƿ��һ��ʹ�ó���
	 * @author AN
	 *
	 */
	interface IRunStateCallback{
		public void runningState(boolean b);
	}
	
	
	
	
	//���
	//����
	private static DBManager manager=null;
	
	//��Ա���
	private boolean runningState=true;
	
	private ArrayList<User> userList=null;
	
	
	
	//˽�й���
	
	private DBManager() {
		userList=new ArrayList<>();
	}
	
	//��ʼ��
	private void init()throws IOException {
		File applicationPath=Resource.getApplicationDirectoryFile();
		//������·���򴴽�
		if(!applicationPath.exists()) {
			applicationPath.mkdirs(); 
			runningState=false;
		}
		//����֪ͨ
		Application.setLoadMessage("��ʼ�����");
		
		//�����¼�
		if(runStateCallback!=null) {
			runStateCallback.runningState(runningState);
		}
		
		//׼������
		Application.setLoadMessage("׼������");
		try {
			readUserList();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(JOptionPane.showConfirmDialog(null, "�޷����л����û��ļ����ļ��Ѿ��𻵣��Ƿ����ڴ��е�������ļ���","����",
					JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION) {
				updateUserListFile();
			}else {
				createNewUserFile();
			}
			
		}
		
	}
	
	
	/**
	 * ���ļ��ж�ȡ�û��б�
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void readUserList() throws IOException ,ClassNotFoundException{
		File userFile=Resource.getApplicationFile(Resource.FILE_USER);
		if(userFile.exists()) {
			FileInputStream fi=new FileInputStream(userFile);
			ObjectInputStream ois=new ObjectInputStream(fi);
			
			@SuppressWarnings("unchecked")
			ArrayList<User> tmp=(ArrayList<User>)ois.readObject();
			ois.close();
			
			userList=tmp;
		}
	}
	
	
	
	
	
	
	
	/**
	 * �����ļ��е�����
	 * @throws IOException
	 */
	public void updateUserListFile() throws IOException{
		File file=Resource.getApplicationFile(Resource.FILE_USER);
		if(userList!=null) {
			if(!file.exists()) {
				createNewUserFile();
			}
			FileOutputStream fos=new FileOutputStream(file);
			ObjectOutputStream oos=new ObjectOutputStream(fos);
			oos.writeObject(userList);
			oos.close();
		}
	}
	
	
	
	
	
	public void addUser(User user) {
		userList.add(user);
	}
	
	
	
	
	
	public void createNewUserFile() throws IOException {
		File file=Resource.getApplicationFile(Resource.FILE_USER);
		file.createNewFile();
		
		ArrayList<User>tmp=new ArrayList<>();
		
		FileOutputStream fos=new FileOutputStream(file);
		ObjectOutputStream oos=new ObjectOutputStream(fos);
		oos.writeObject(tmp);
		oos.close();
	}
	
	public void createNewSettingFile() throws IOException {
		File file=Resource.getApplicationFile(Resource.FILE_SETTING);
		file.createNewFile();
	}
	
	
	
	
	
	
	//��̬����
	/**
	 * ׼������
	 * @return ׼���õ�DBManager
	 */
	public static DBManager prepareDataBase()throws IOException {
		if(manager==null) {
			manager=new DBManager();
			manager.init();
		}
		return manager;
	}
	
	public ArrayList<User> getUserList() {
		if(userList!=null) {
			return userList;
		}
		return null;
	}

	public static boolean isExisitUserName(String name){
		if(manager==null){
			return false;
		}
		if(manager.userList==null){
			return false;
		}
		for(User user:manager.userList){
			if(user.userName.equals(name)){
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	
}
