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
 * 数据管理服务
 * @author AN
 *
 */
public class DBManager {
	
	private IRunStateCallback runStateCallback=null;
	public void setRunStateCallback(IRunStateCallback callback) {
		runStateCallback=callback;
	}

	/**
	 * 运行状态回调：判断是否第一次使用程序
	 * @author AN
	 *
	 */
	interface IRunStateCallback{
		public void runningState(boolean b);
	}
	
	
	
	
	//组件
	//单例
	private static DBManager manager=null;
	
	//成员组件
	private boolean runningState=true;
	
	private ArrayList<User> userList=null;
	
	
	
	//私有构造
	
	private DBManager() {
		userList=new ArrayList<>();
	}
	
	//初始化
	private void init()throws IOException {
		File applicationPath=Resource.getApplicationDirectoryFile();
		//不存在路径则创建
		if(!applicationPath.exists()) {
			applicationPath.mkdirs(); 
			runningState=false;
		}
		//设置通知
		Application.setLoadMessage("初始化组件");
		
		//发送事件
		if(runStateCallback!=null) {
			runStateCallback.runningState(runningState);
		}
		
		//准备数据
		Application.setLoadMessage("准备数据");
		try {
			readUserList();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(JOptionPane.showConfirmDialog(null, "无法序列化该用户文件，文件已经损坏，是否用内存中的数据填补文件？","错误",
					JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION) {
				updateUserListFile();
			}else {
				createNewUserFile();
			}
			
		}
		
	}
	
	
	/**
	 * 从文件中读取用户列表
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
	 * 更新文件中的数据
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
	
	
	
	
	
	
	//静态方法
	/**
	 * 准备数据
	 * @return 准备好的DBManager
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
