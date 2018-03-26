package dbManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
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

	//用户信息
	private ArrayList<User> userList=null;




    /* =============================== 装载的数据 ===========================*/
    //装载的用户实例
	private User user=null;

    //工人属性
	private boolean workerPropertyLoaded=false;
    private Anbean workerProperty =null;
    //工人数据
	private boolean workerListLoaded=false;
    private ArrayList<Anbean> workerList=null;

    //资产数据
	private boolean assetsArrayListLoaded=false;
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
     * 更新装载的用户数据到文件
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


	/**
	 * 从文件中装载工人属性
	 * @return
	 */
	public Anbean loadingWorkerProperty(){
		//空用户退出
		if(user==null)
			return null;
		//启动加载或者第一次加载
		if(user!=null&&!workerPropertyLoaded){
			try {
				workerProperty =(Anbean) readObject(user.getWorkerPropertyPath());
			} catch (IOException e) {
				Application.errorWindow(e.toString());
				workerProperty=WorkerFactory.createWorker();
			} catch (ClassNotFoundException e) {
				Application.errorWindow(e.toString());
				workerProperty=WorkerFactory.createWorker();
			}finally {
				workerPropertyLoaded=true;
			}
		}
		//多次获取
		return workerProperty;
	}

	public int getWorkerPropertySize(){
		if(workerPropertyLoaded)
			return workerProperty.getSize();
		return 0;
	}





	/**
	 * 从文件中装载工人对象
	 * @return
	 */
	public ArrayList<Anbean> loadingWorkerList() {

		//空对象
		if(user==null)
			return null;
		//首次启动
		if(user!=null&&!workerListLoaded){
			try {
				workerList=(ArrayList<Anbean>)readObject(user.getWorkerListPath());
				if(workerList==null){
					workerList=new ArrayList<>();
				}
			}catch (IOException e){
				Application.errorWindow(e.toString());
				workerList=new ArrayList<>();
			}catch (ClassNotFoundException e){
				Application.errorWindow(e.toString());
				workerList=new ArrayList<>();
			}finally {
				workerListLoaded=true;
			}
		}
		//多次加载
		return workerList;
	}


	public int getWorkerListSize(){
		if(workerListLoaded)
			return workerList.size();
		return 0;
	}







	/**
	 * 增加用户到内存中
	 * @param user
	 */
	public void addUser(User user) {
		userList.add(user);
	}
	

	//User操作

	/**
	 * 装载User数据
	 * @param user
	 */
	public void loadUser(User user){
		this.user=user;
		workerProperty=loadingWorkerProperty();
		workerList=loadingWorkerList();

		//Debug：装载测试列表工人
		Random r=new Random();
		for(int i=0;i<10;i++){
			Anbean w=WorkerFactory.createWorker();
			Info inf1=w.get("名字");
			inf1.setValue("名字"+i);
			Info inf2=w.get("身份证");
			inf2.setValue(String.valueOf(r.nextInt(10000)));
			workerList.add(w);
		}


	}








	/**
	 * 装载工人属性，全盘替换
	 * @param bean
	 */
    public void setWorkerProperty(Anbean bean){
        if(bean!=null){
            workerProperty =bean;
        }
    }







	/**
	 * 增加一条工人属性
	 * @param info
	 */
	public void addWorkerBeanInfo(Info info){
        if(workerProperty !=null){
            workerProperty.addInfo(info);
        }
    }

	/**
	 *
	 * @param info
	 */
	public void removeWorkerBeanInfo(Info info){
        if(workerProperty !=null){
            workerProperty.removeInfo(info);
        }
    }

	/**
	 *
	 * @param index
	 * @return
	 */
	public Info getWorkerBeanInfo(int index){
        if(workerProperty !=null){
            return workerProperty.getAt(index);
        }
        return null;
    }

	/**
	 *
	 * @param name
	 * @return
	 */
	public Info getWorkerBeanInfo(String name){
        if(workerProperty !=null){
            return workerProperty.get(name);
        }
        return null;
    }


	/**
	 *
	 * @throws IOException
	 */
	public void createNewUserFile() throws IOException {
		File file=Resource.getApplicationFile(Resource.FILE_USER);
		file.createNewFile();
		
		ArrayList<User>tmp=new ArrayList<>();
		
		FileOutputStream fos=new FileOutputStream(file);
		ObjectOutputStream oos=new ObjectOutputStream(fos);
		oos.writeObject(tmp);
		oos.close();
	}

	/**
	 *0
	 * @throws IOException
	 */
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

	/**
	 * 直接返回已经实例化的DBManager对象
	 * @return
	 */
	public static DBManager getManager(){
		if(manager==null)
			return null;
		return manager;
	}

	/**
	 *
	 * @return
	 */
	public ArrayList<User> getUserList() {
		if(userList!=null) {
			return userList;
		}
		return null;
	}

	/**
	 * 是否存在用户名字
	 * @param name
	 * @return
	 */
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

	/**
	 *写入对象到文件中，读取文件路径，如果没创建会自动创建文件
	 * @param path 文件绝对路径
	 * @param object 将要写入到文件的对象
	 * @throws IOException
	 */
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

	/**
	 *
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    public static Object readObject(String path) throws IOException, ClassNotFoundException {
	    File file=new File(path);
		FileInputStream fi=new FileInputStream(file);
		ObjectInputStream ois=new ObjectInputStream(fi);
		return ois.readObject();
    }
	
	
	
	
}
