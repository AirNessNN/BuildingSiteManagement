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
	private boolean userLoaded=false;
	private User user=null;

    //工人属性
	private boolean workerPropertyLoaded=false;
    private AnArrayBean workerProperty =null;
    //工人数据
	private boolean workerListLoaded=false;
    private ArrayList<AnBean> workerList=null;
    //工地数据
	private boolean buildingSiteLoaded=false;
	private ArrayList<AnArrayBean> buildingSiteLIst=null;

    //资产数据
	private boolean assetsArrayListLoaded=false;
    private ArrayList<Assets> assetsArrayList=null;






    //包工属性



    /*
    ===================================================================
     */


	/**
	 * 单例模式的私有构造函数
	 */
	private DBManager() {
		userList=new ArrayList<>();
	}




	/**
	 * 初始化应用路径，查找应用路径，不存在则创建，当初次使用
	 *
	 * 准备用户列表，供登录窗口使用
	 *
	 * @throws IOException
	 */
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
	 *创建设置的配置文件
	 * @throws IOException
	 */
	public void createNewSettingFile() throws IOException {
		File file=Resource.getApplicationFile(Resource.FILE_SETTING);
		file.createNewFile();
	}
	
	
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//User

	/**
	 *	创建新的存放用户数据的文件
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
	 * 增加用户到内存中
	 * @param user
	 */
	public void addUser(User user) {
		userList.add(user);
	}

	/**
	 * 装载User数据
	 * @param user
	 */
	public void loadUser(User user){
		this.user=user;
		workerProperty=loadingWorkerProperty();
		workerList=loadingWorkerList();
		assetsArrayList=null;

		if (user!=null)
			userLoaded=true;

		//Debug：装载测试列表工人
		Random r=new Random();
		for(int i=0;i<10;i++){
			AnBean w= PropertyFactory.createWorker();
			Info inf1=w.find("名字");
			inf1.setValue("名字"+r.nextInt(456123));
			Info inf2=w.find("身份证");
			inf2.setValue(String.valueOf(r.nextInt(10000)));
			workerList.add(w);
		}


	}

	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++








	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//WorkerProperty

	/**
	 * 增加一条工人属性
	 * @param info
	 */
	public void addProperty(InfoArray info) throws Exception {
		if(workerProperty !=null){
			workerProperty.addInfoArray(info);
		}
	}

	/**
	 *	移除工人属性
	 * @param info
	 */
	public void removeWorkerBeanInfo(InfoArray info){
		if(workerProperty !=null){
			workerProperty.removeInfoArray(info);
		}
	}

	/**
	 *	通过节点号获取工人属性InfoArray
	 * @param index
	 * @return
	 */
	public InfoArray getWorkerBeanInfo(int index){
		if(workerProperty !=null){
			return workerProperty.findAt(index);
		}
		return null;
	}

	/**
	 *	通过名称获取工人属性InfoArray
	 * @param name
	 * @return
	 */
	public InfoArray getWorkerBeanInfo(String name){
		if(workerProperty !=null){
			return workerProperty.find(name);
		}
		return null;
	}


	/**
	 * 装载工人属性，全盘替换
	 * @param bean
	 */
	public void setWorkerProperty(AnArrayBean bean){
		if(bean!=null){
			workerProperty =bean;
		}
	}

	/**
	 * 从文件中装载工人属性
	 * @return
	 */
	public AnArrayBean loadingWorkerProperty(){
		//空用户退出
		if(user==null)
			return null;
		//启动加载或者第一次加载
		if(!workerPropertyLoaded){
			try {
				workerProperty =(AnArrayBean) readObject(user.getWorkerPropertyPath());
			} catch (IOException | ClassNotFoundException exception) {
				workerProperty= PropertyFactory.createWorkerProperty();
			} finally {
				workerPropertyLoaded=true;
			}
		}
		//多次获取
		return workerProperty;
	}

	/**
	 * 获取属性的大小
	 * @return
	 */
	public int getWorkerPropertySize(){
		if(workerPropertyLoaded)
			return workerProperty.getSize();
		return 0;
	}

	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++







	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//WorkerList

	/**
	 * 从文件中装载工人对象
	 * @return
	 */
	public ArrayList<AnBean> loadingWorkerList() {

		//首次启动
		if(!workerListLoaded&&userLoaded){
			try {
				workerList=(ArrayList<AnBean>)readObject(user.getWorkerListPath());
				if(workerList==null){
					workerList=new ArrayList<>();
				}
			}catch (IOException | ClassNotFoundException e){
				//Application.errorWindow(e.toString());
				workerList=new ArrayList<>();
			} finally {
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

	public AnBean getWorker(int index){
		if(workerListLoaded){
			return workerList.get(index);
		}
		return null;
	}

	/**
	 * 从节点信息获取所有符合该要求的工人实例
	 * @param name 节点名称
	 * @param value 节点值
	 * @return 返回一个ArrayList ,如果找不到，则返回空list
	 */
	public ArrayList<AnBean> getWorkerListWhere(String name, Object value){
		ArrayList<AnBean> tmpList=new ArrayList<>();

		if(workerListLoaded){
			for(AnBean bean:workerList){
				for (Info info: bean.getArray()){
					if(info.getName().equals(name)&&info.equalsValue(value)){
						tmpList.add(bean);
					}
				}
			}
		}
		return tmpList;
	}

	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++



	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//AssetList


	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++



	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//AssetPackage


	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//buildingSiteList

	/**
	 * 加载工地
	 * @return
	 */
	public ArrayList<AnArrayBean> loadingBuildingSiteList(){
		if (!userLoaded)
			return null;

		try{
			buildingSiteLIst= (ArrayList<AnArrayBean>) readObject(user.getBuildingSitePath());
			if (buildingSiteLIst!=null)
				buildingSiteLoaded=true;
			return buildingSiteLIst;
		} catch (IOException | ClassNotFoundException e) {
			buildingSiteLIst=new ArrayList<>();
		}finally {
			buildingSiteLoaded=true;
		}
		return buildingSiteLIst;
	}

	/**
	 * 向DB中添加工地
	 * @param buildingSite 添加一个工地的实例
	 */
	public void addBuildingSite(AnArrayBean buildingSite) throws Exception {
		if (!buildingSiteLoaded)
			return;

		for (AnArrayBean bean:buildingSiteLIst){
			if (bean.getName().equals(buildingSite.getName()))
				throw new Exception("工地重名，无法添加到列表中！");
		}
		buildingSiteLIst.add(buildingSite);
	}

	/**
	 * 向DB中添加一个空的工地
	 * @param name 工地名称
	 */
	public void addBuildingSite(String name) throws Exception {
		if (!buildingSiteLoaded)
			return;

		AnArrayBean site=PropertyFactory.createBuildingSite();
		for (AnArrayBean bean:buildingSiteLIst){
			if (bean.getName().equals(name))
				throw new Exception("工地重名，无法添加到列表中！");
		}
		buildingSiteLIst.add(site);
	}

	/**
	 * 返回该工地的实例
	 * @param siteName
	 * @return
	 */
	public AnArrayBean getBuildingSite(String siteName){
		if (!buildingSiteLoaded)
			return null;
		for (AnArrayBean bean:buildingSiteLIst)
			if (bean.getName().equals(siteName))
				return bean;
		return null;
	}

	/**
	 * 返回该工地实例中的所有员工
	 * @param siteName 工地
	 * @return 不存在返回null
	 */
	public String[] getBuildingSiteWorkers(String siteName){
		String[] tmpWorker;

		AnArrayBean site=getBuildingSite(siteName);
		InfoArray array=site.find(PropertyFactory.LABEL_ID_CARD);
		if (array!=null){
			int size=array.getSize();

			if (size==0)
				return null;

			tmpWorker=new String[size];

			int index=0;

			for (Object o:array.getValues()){
				tmpWorker[index++]=(String) o;
			}
			return tmpWorker;
		}
		return null;
	}

	/**
	 * 返回工人所在的所有工地
	 * @param id 身份证号
	 * @return
	 */
	public ArrayList<String> getWorkerAt(String id){
		if (!buildingSiteLoaded&&!workerListLoaded)
			return null;
		ArrayList<String> tmpList=new ArrayList<>();
		for (AnArrayBean site:buildingSiteLIst){
			for (Object workerId:site.find(PropertyFactory.LABEL_ID_CARD).getValues()){
				String tmpID= (String) workerId;
				if (tmpID.equals(id)){
					tmpList.add(site.getName());
				}
			}
		}
		return  tmpList;
	}

	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++




	
	
	//静态方法
	/**
	 * 准备数据 实例化单例，并且返回对象
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
	 *获取用户列表 如果列表为空则会返回一个实例化了的空列表
	 * @return
	 */
	public ArrayList<User> getUserList() {
		if(userList!=null) {
			return userList;
		}
		return new ArrayList<>();
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
