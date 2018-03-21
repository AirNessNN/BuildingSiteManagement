package dbManager;

import java.io.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import application.Application;
import resource.Resource;

/**
 * 数据管理服务
 * @author AN
 *
 */
public class DBManager {
	
	private IRunStateCallback runStateCallback=null;

	/**
	 * 设置运行状态回调
	 * @param callback
	 */
	public void setRunStateCallback(IRunStateCallback callback) {
		runStateCallback=callback;
	}

	/**
	 * 运行状态回调：判断是否第一次使用程序
	 * @author AN
	 *
	 */
	interface IRunStateCallback{
		void runningState(boolean b);
	}
	
	
	
	
	//组件
	//单例
	private static DBManager manager=null;
	
	//成员组件
	private boolean runningState=true;
	
	private ArrayList<User> userList=null;

    /* =============================== 装载的数据 ===========================*/
	private User user=null;

    //工人属性
    private Anbean workerProperty =null;
    //工人数据
    private ArrayList<Anbean> workerList=null;


    //资产数据
    private ArrayList<Assets> assetsArrayList=null;


    //包工属性



    /*
    ===================================================================
     */

	
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
			//e.printStackTrace();
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
	 * 更新用户数据到文件中
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

    /**
     * 更新用户工人属性到文件
     */
	public void updateUserData(){
        if(user!=null){
            if(workerProperty !=null){
                try {
                    writeObject(user.getWorkerPropertyPath(), workerProperty);
                } catch (IOException e) {
                    Application.errorWindow("无法写入工人数据到文件，请检查是否该目录有权限读写。"+e.getMessage());
                }
            }
            if(assetsArrayList !=null){
                try {
                    writeObject(user.getAssetsPath(), assetsArrayList);
                } catch (IOException e) {
                    Application.errorWindow("无法写入资产数据到文件，请检查是否该目录有权限读写。"+e.getMessage());
                }
            }
            if(workerList!=null){
                try {
                    writeObject(user.getWorkerListPath(),workerList);
                }catch (IOException e){
                    Application.errorWindow(e.toString());
                }
            }

        }
    }



    public void loadUserData(){
        if(user!=null){
            try {
                workerProperty =(Anbean) readObject(user.getAssetsPath());
            } catch (IOException e) {
                Application.errorWindow(e.toString());
            } catch (ClassNotFoundException e) {
                Application.errorWindow(e.toString());
            }
        }
    }
	
	
	
	
	
	public void addUser(User user) {
		userList.add(user);
	}
	

	//User操作
	public void loadUser(User user){

	}

    public void setWorkerProperty(Anbean bean){
        if(bean!=null){
            workerProperty =bean;
        }
    }

    public void addWorkBeanInfo(Info info){
        if(workerProperty !=null){
            workerProperty.addInfo(info);
        }
    }

    public void removeWorkBeanInfo(Info info){
        if(workerProperty !=null){
            workerProperty.removeInfo(info);
        }
    }

    public Info getWorkerBeanInfo(int index){
        if(workerProperty !=null){
            return workerProperty.getAt(index);
        }
        return null;
    }

    public Info getWorkerBeanInfo(String name){
        if(workerProperty !=null){
            return workerProperty.get(name);
        }
        return null;
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

	public static boolean isExistUserName(String name){
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
	
	public static void writeObject(String path,Object object) throws IOException {
	    File file=new File(path);
	    if(!file.exists()){
	        file.createNewFile();
        }
        FileOutputStream fos=new FileOutputStream(file);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
    }

    public static Object readObject(String path) throws IOException, ClassNotFoundException {
	    File file=new File(path);
	    if(file.exists()){
            FileInputStream fi=new FileInputStream(file);
            ObjectInputStream ois=new ObjectInputStream(fi);
            return ois.readObject();
        }
        return null;
    }
	
	
	
	
}
