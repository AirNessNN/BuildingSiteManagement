package dbManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import javax.swing.*;

import application.Application;
import application.EntryWindow;
import resource.Resource;
import test.Test;

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
	private ArrayList<User> userList;




    /* =============================== 装载的数据 ===========================*/
    //装载的用户实例
	private boolean userLoaded=false;
	private User user=null;
    //工人属性
	private boolean workerPropertyLoaded=false;
    private AnDataTable workerProperty =null;
    //工人数据
	private boolean workerListLoaded=false;
    private ArrayList<AnBean> workerList=null;
    //工地数据
	private boolean buildingSiteLoaded=false;
	private ArrayList<AnDataTable> buildingSiteLIst=null;
    //资产数据
	private boolean assetsArrayListLoaded=false;
    private ArrayList<Assets> assetsArrayList=null;
    //出勤管理器
	private boolean checkInManagerLoaded=false;
	private ChildrenManager checkInManager=null;
	//工资管理器
	private boolean salaryManagerLoaded=false;
	private ChildrenManager salaryManager=null;



	//包工属性

	//设置信息
	private AnBean settings=null;
	private boolean settingLoaded=false;


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

            if (checkInManagerLoaded){
            	checkInManager.saveToFile();
			}

			if (salaryManagerLoaded){
            	salaryManager.saveToFile();
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
	 * 装载User数据并且装载各种管理器
	 * @param user
	 */
	public void loadUser(User user){
		this.user=user;
		if (user!=null)
			userLoaded=true;

		workerProperty=loadingWorkerProperty();
		workerList=loadingWorkerList();
		buildingSiteLIst=loadingBuildingSiteList();
		assetsArrayList=null;



		//装载各种管理器
		if (userLoaded){
			checkInManager=new ChildrenManager(user.getCheckInInfoPath());
			checkInManager.loading(user);
			checkInManagerLoaded=true;

			salaryManager=new ChildrenManager(user.getSalaryInfoPath());
			salaryManager.loading(user);
			salaryManagerLoaded=true;
		}



		//Debug：装载测试列表工人

		try {
			manager.createBuildingSite("测试工地1");
			manager.createBuildingSite("测试工地2");
			manager.createWorkerProperty(true,"测试属性");
		} catch (Exception e) {
			e.printStackTrace();
		}


		Random r=new Random();
		for(int i=0;i<10;i++){
			AnBean w= PropertyFactory.createWorker();
			w.find(PropertyFactory.LABEL_NUMBER).setValue(i);
			Info inf1=w.find("名字");
			inf1.setValue("名字"+r.nextInt(456123));
			Info inf2=w.find("身份证");
			inf2.setValue(Test.IDRandom());
			w.find(PropertyFactory.LABEL_PHONE).setValue("13123376032");
			w.find(PropertyFactory.LABEL_SEX).setValue(workerProperty.find(PropertyFactory.LABEL_SEX).getValues().get(r.nextInt(workerProperty.find(PropertyFactory.LABEL_SEX).getSize())));//性别
			w.find(PropertyFactory.LABEL_BANK_ADDRESS).setValue("乱写的地址");
			//工地
			addWorker(w);
			int ind=r.nextInt(workerProperty.find(PropertyFactory.LABEL_WORKER_TYPE).getSize());
			System.out.println(addWorkerToSite(
					w.find(PropertyFactory.LABEL_ID_CARD).getValueString(),
					buildingSiteLIst.get(r.nextInt(buildingSiteLIst.size())).getName(),
					(double) r.nextInt(10000),
					(String) workerProperty.find(PropertyFactory.LABEL_WORKER_TYPE).get(ind)
			));
		}
	}

	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++








	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//WorkerProperty

	/**
	 * 增加一条工人属性
	 * @param info
	 */
	public void addWorkerProperty(AnColumn info) throws Exception {
		if(workerProperty !=null){
			workerProperty.addColumn(info);
			PropertyFactory.addUserData(new Info(info.getName(),""));
		}
	}

	public void createWorkerProperty(boolean b,String name) throws Exception {
		AnColumn<String> anColumn =new AnColumn<>(b,name);
		addWorkerProperty(anColumn);
	}

	/**
	 *	移除工人属性
	 * @param info
	 */
	public void removeWorkerProperty(AnColumn info){
		if(workerProperty !=null){
			workerProperty.removeInfoArray(info);
		}
	}

	/**
	 *	通过节点号获取工人属性InfoArray
	 * @param index
	 * @return
	 */
	public AnColumn getWorkerProperty(int index){
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
	public AnColumn getWorkerProperty(String name){
		if(workerProperty !=null){
			return workerProperty.find(name);
		}
		return null;
	}

	/**
	 * 通过名字获取该属性下所有字段枚举，由String数组封装
	 * @param name
	 * @return
	 */
	public String[] getWorkerPropertyArray(String name){
		AnColumn array=getWorkerProperty(name);
		String[] strs=new String[array.getSize()];
		for (int i=0;i<array.getSize();i++){
			strs[i]= (String) array.get(i);
		}
		return strs;
	}


	/**
	 * 装载工人属性，全盘替换
	 * @param bean
	 */
	public void setWorkerProperty(AnDataTable bean){
		if(bean!=null){
			workerProperty =bean;
		}
	}

	/**
	 * 从文件中装载工人属性
	 * @return
	 */
	public AnDataTable loadingWorkerProperty(){
		//空用户退出
		if(user==null)
			return null;
		//启动加载或者第一次加载
		if(!workerPropertyLoaded){
			try {
				workerProperty =(AnDataTable) readObject(user.getWorkerPropertyPath());
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
		if(!(workerListLoaded)){
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

	/**
	 * 添加工人，如果存在返回false
	 * @param bean
	 * @return
	 */
	public boolean addWorker(AnBean bean){
		if (bean==null)
			return false;
		String id=bean.find(PropertyFactory.LABEL_ID_CARD).getValueString();
		for (AnBean w:loadingWorkerList()){
			if (w.find(PropertyFactory.LABEL_ID_CARD).equalsValue(id))
				return false;
		}
		loadingWorkerList().add(bean);
		return true;
	}

	public int getWorkerListSize(){
		if(workerListLoaded)
			return workerList.size();
		return 0;
	}

	/**
	 * 根据索引号返回一个工人实例
	 * @param index
	 * @return
	 */
	public AnBean getWorker(int index){
		if(workerListLoaded){
			return workerList.get(index);
		}
		return null;
	}

	/**
	 * 根据工人身份证返回实例
	 * @param id
	 * @return
	 */
	public AnBean getWorker(String id){
		if (workerListLoaded){
			for (AnBean bean:workerList){
				if (bean.find(PropertyFactory.LABEL_ID_CARD).getValue().equals(id)){
					return bean;
				}
			}
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
					if(info.getName().equals(name)){
						if (info.getValue() instanceof ArrayList){
							ArrayList list= (ArrayList) info.getValue();
							for (Object object:list){
								if (object.toString().equals(value.toString())){
									tmpList.add(bean);
								}
							}
						}else {
							if (info.equalsValue(value)){
								tmpList.add(bean);
							}
						}
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
	public ArrayList<AnDataTable> loadingBuildingSiteList(){
		if (!userLoaded)
			return null;

		if (buildingSiteLoaded)
			return buildingSiteLIst;
		try{
			buildingSiteLIst= (ArrayList<AnDataTable>) readObject(user.getBuildingSitePath());
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
	public void createBuildingSite(AnDataTable buildingSite) throws Exception {
		if (!buildingSiteLoaded)
			return;

		for (AnDataTable bean:buildingSiteLIst){
			if (bean.getName().equals(buildingSite.getName()))
				throw new Exception("工地重名，无法添加到列表中！");
		}
		buildingSiteLIst.add(buildingSite);
		//下面的步骤是用户创建工地之后，一定要将工地添加到属性中去
		if (workerPropertyLoaded){
			workerProperty.find(PropertyFactory.LABEL_SITE).addValue(buildingSite.getName());
		}
	}

	/**
	 * 向DB中添加一个空的工地
	 * @param name 工地名称
	 */
	public void createBuildingSite(String name) throws Exception {
		if (!buildingSiteLoaded)
			return;
		AnDataTable site=PropertyFactory.createBuildingSite();
		site.setName(name);
		createBuildingSite(site);
	}

	/**
	 * 返回该工地的实例
	 * @param siteName 工地名字
	 * @return
	 */
	public AnDataTable getBuildingSite(String siteName){
		if (!buildingSiteLoaded)
			return null;
		for (AnDataTable bean:buildingSiteLIst)
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

		AnDataTable site=getBuildingSite(siteName);
		AnColumn array=site.find(PropertyFactory.LABEL_ID_CARD);
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
		for (AnDataTable site:buildingSiteLIst){
			for (Object workerId:site.find(PropertyFactory.LABEL_ID_CARD).getValues()){
				String tmpID= (String) workerId;
				if (tmpID.equals(id)){
					tmpList.add(site.getName());
				}
			}
		}
		return  tmpList;
	}

	public void deleteBulidingSite(AnDataTable site){
		if (!buildingSiteLoaded)
			return;
		buildingSiteLIst.remove(site);

		//更新属性中的数据
		if (workerPropertyLoaded){
			workerProperty.find(PropertyFactory.LABEL_SITE).getValues().remove(site.getName());
		}
		//更新所有工人中的数据
		if (workerListLoaded){
			for (AnBean bean:workerList){
				ArrayList<String> siteTamp= (ArrayList<String>) bean.find(PropertyFactory.LABEL_SITE).getValue();
				siteTamp.remove(site.getName());
			}
		}
	}

	/**
	 * 将工人列表中找到的工人实例添加到工地列表中指定的工地中去
	 * @param id 身份证
	 * @param site 工地
	 * @param dealSalary 协议工价
	 * @param workType 工种
	 * @return 添加成功返回true
	 */
	public boolean addWorkerToSite(String id,String site,Double dealSalary,String workType){
		if (!workerListLoaded&&!buildingSiteLoaded)
			return false;

		AnDataTable dt=getBuildingSite(site);
		if (dt==null)
			return false;
		if (!dt.addRow(id,dealSalary,workType))
			return false;

		updateChildrenManager();
		return true;
	}

	/**
	 * 获取现存所有工地的名称数组
	 * @return
	 */
	public String[] getFullBuildingSiteName(){
		String[] sites=new String[loadingBuildingSiteList().size()];
		for (int i=0;i<sites.length;i++){
			sites[i]=loadingBuildingSiteList().get(i).getName();
		}
		return sites;
	}

	@Deprecated
	public void updateBuildingSiteInfo(){
		if (!workerListLoaded&&!buildingSiteLoaded)
			return;
		//循环工人列表
		for (AnBean worker:workerList){

			Info<ArrayList> site= (Info<ArrayList>) worker.find(PropertyFactory.LABEL_SITE).getValue();

		}
	}
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++







	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//CheckInManager
	public ChildrenManager getCheckInManager(){
		if (checkInManagerLoaded)
			return checkInManager;
		return null;
	}
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++







	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//SalaryManager

	/**
	 * 返回工资子管理器
	 * @return
	 */
	public ChildrenManager getSalaryManager(){
		if (salaryManagerLoaded)
			return salaryManager;
		return null;
	}

	/**
	 * 更新所有子管理器，调用子管理器自身的更新方法
	 */
	public void updateChildrenManager(){
		if (salaryManagerLoaded)
			salaryManager.updateWorkerList();
		if (checkInManagerLoaded)
			checkInManager.updateWorkerList();
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


	/**
	 * DB的整合方法，调用PropertyFactory工厂类来初始化一个工人实例，并且该工人实例装有部分属性
	 * @param name
	 * @param ID
	 * @param phone
	 * @param address
	 * @param bankID
	 * @param bankAddress
	 * @param nation
	 * @param enD
	 * @param tag
	 * @return
	 */
	@Deprecated
    public static AnBean createWorker(String name, String ID, String phone, String address, String bankID, String bankAddress, String nation, Date enD,String type, String tag){
		if (!manager.userLoaded)
			return null;
		if (!manager.workerListLoaded)
			return null;
		if(!manager.salaryManagerLoaded)
			return null;
		if (!manager.checkInManagerLoaded)
			return null;

		AnBean worker=PropertyFactory.createWorker();
		worker.find(PropertyFactory.LABEL_NAME).setValue(name);
		worker.find(PropertyFactory.LABEL_ID_CARD).setValue(ID);
		worker.find(PropertyFactory.LABEL_PHONE).setValue(phone);
		worker.find(PropertyFactory.LABEL_ADDRESS).setValue(address);
		worker.find(PropertyFactory.LABEL_BANK_ADDRESS).setValue(bankAddress);
		worker.find(PropertyFactory.LABEL_BANK_ID).setValue(bankID);
		worker.find(PropertyFactory.LABEL_NATION).setValue(nation);
		worker.find(PropertyFactory.LABEL_ENTRY_TIME).setValue(enD);
		worker.find(PropertyFactory.LABEL_TAG).setValue(tag);
		worker.find(PropertyFactory.LABEL_WORKER_TYPE).setValue(type);
		return worker;
	}

	/**
	 * DB工具方法，快速设置AnBean的值
	 * @param bean 包装类
	 * @param propertyName 属性名
	 * @param value 属性值
	 * @return 返回该包装
	 */
	public static void setBeanInfo(AnBean bean, String propertyName,Object value){
    	if(bean.find(propertyName)==null)
    		return;
    	bean.find(propertyName).setValue(value);
	}

	/**
	 * DB工具方法，快速获取AnBean的String值
	 * @param bean 包装类
	 * @param propertyName 属性名
	 * @return 返回该包装
	 */
	public static String getBeanInfoStringValue(AnBean bean,String propertyName){
		if (bean!=null){
			return bean.find(propertyName).getValueString();
		}
		return null;
	}

	/**
	 * DB工具方法：快速添加动态数组型的Info，如果不存在的话
	 * @param bean 包装
	 * @param propertyName 属性名
	 * @param value 数值
	 * @return 返回该包装
	 */
	public static void addBeanArrayInfo(AnBean bean, String propertyName, Object value){
		Info info=bean.find(propertyName);
		if (info==null)
			return;
		if (!(info.getValue()instanceof ArrayList))
			return;
		ArrayList<String> tmpList= (ArrayList<String>) info.getValue();
		if (tmpList.contains(value))
			return;
		tmpList.add((String) value);
	}

	/**
	 * DB工具方法：快速移除动态数组型的Info中的选中元素，如果存在的话
	 * @param bean 包装
	 * @param propertyName 属性名
	 * @param value 数值
	 */
	public static void removeBeanArrayInfo(AnBean bean,String propertyName,Object value){
		Info info=bean.find(propertyName);
		if (info==null)
			return;
		if (!(info.getValue()instanceof ArrayList))
			return;
		ArrayList<String> tmpList= (ArrayList<String>) info.getValue();
		if (tmpList.contains(value))
			tmpList.remove(value);
	}

	/**
	 * DB工具方法：快速获取动态数组型的Info中的所有元素
	 * @param bean 包装
	 * @param propertyName 属性名
	 * @param value 数值
	 * @return 返回该包装
	 */
	public static String[] getBeanArrayInfoValues(AnBean bean,String propertyName,Object value){
		Info info=bean.find(propertyName);
		if (info==null)
			return null;
		if (!(info.getValue()instanceof ArrayList))
			return null;
		ArrayList<String> tmpList= (ArrayList<String>) info.getValue();
		return (String[]) tmpList.toArray();
	}


	/**
	 * DB工具方法，更新工人的工地数据
	 * 本地需要更新，工地中需要更新工人，工资和日期管理器中也需要更新工人的数据
	 * @param bean
	 * @param site
	 * @return
	 */
	@Deprecated
	public boolean addWorkerToBuildingSite(AnBean bean, AnDataTable site){
		if (!manager.workerListLoaded&&!manager.buildingSiteLoaded)
			return false;

		boolean workerFound=false;

		//判断工人是否存在
		for (AnBean worker:manager.workerList){
			if (bean.find(PropertyFactory.LABEL_ID_CARD).getValue().equals(worker.find(PropertyFactory.LABEL_ID_CARD).getValue())){
				workerFound=true;
				break;
			}
		}
		if (!workerFound)manager.workerList.add(bean);

		//给工人自带的属性中加入该工地
		bean.find(PropertyFactory.LABEL_SITE).addListValue(site.getName());
		site.find(PropertyFactory.LABEL_SITE).addValue(bean.find(PropertyFactory.LABEL_ID_CARD).getValueString());

		manager.updateChildrenManager();//将数据更新到管理器
		return true;
	}


	/**
	 * DB工具方法，更新工人的工地数据
	 * 本地需要更新，工地中需要更新工人，工资和日期管理器中也需要更新工人的数据
	 * @param id 身份证
	 * @param siteName 工地名称
	 * @param dealSalary 约定工资
	 * @param workType 工种
	 * @return 添加成功返回true
	 */
	public boolean addWorkerToBuildingSite(String id ,String siteName,double dealSalary,String workType){
		AnBean worker=getWorker(id);
		if (worker==null)
			return false;
		AnDataTable site=getBuildingSite(siteName);
		if (site==null)
			return false;

		//在工人自身属性上添加
		boolean b=worker.find(PropertyFactory.LABEL_SITE).addListValue(siteName);
		if (!b)
			return false;

		//在工地中添加
		boolean bs=site.addRow(id,dealSalary,workType);
		if (!bs){//如果添加失败就移除工人自身属性
			worker.find(PropertyFactory.LABEL_SITE).removeListValue(siteName);
			return false;
		}

		//在所有管理器中添加信息
		updateChildrenManager();
		return true;
	}

	/**
	 * DB工具方法<br/>
	 * 从工地中删除该工人的记录，受影响的属性也包括工人自身的工地记录
	 * @param id 工人身份证
	 * @param siteName 工地名称
	 * @return
	 */
	public boolean deleteWorkerFromBuildingSite(String id, String siteName){
		AnBean worker=manager.getWorker(id);
		if (worker==null)
			return false;
		AnDataTable site=manager.getBuildingSite(siteName);
		if (site==null)
			return false;

		worker.find(PropertyFactory.LABEL_SITE).removeListValue(siteName);
		site.setKey(PropertyFactory.LABEL_ID_CARD);
		site.removeRow(id);
		manager.updateChildrenManager();
		return true;
	}

	/**
	 * DB工具方法：<p>描述创建工人到选择工人所在的工地或者创建工地的业务逻辑</p><br/>
	 * 打开工人录入窗口，由窗口自带的构造创建一个拥有主要信息的工人（身份证和姓名）
	 * 将该工人添加到工人列表中，并收集该工人的其他属性，
	 * <br/>
	 * <P>创建的时候不需要注意工人旧属性的问题</P>
	 * @return
	 */
	public boolean createWorker(){
		if (!workerListLoaded)
			return false;

		AnBean worker=EntryWindow.showWindow();
		boolean createFlag=addWorker(worker);//创建完成之后就添加
		if (worker!=null&&createFlag){
			//工人选择工地
		}
		return false;
	}


	/**
	 * 更新工人的工地数据，需要注意的是第二个参数工地数组是指更新之后的数据。
	 * <br/>
	 * <p>也就是说从工地选择器中选择的数据，如果不是从选择器中选择的数据将会删除所有工人数据，
	 * 然后把这个列表中的数据给添加进工人信息中，包括工地管理的数据中也会更新工人数据，受影响的还有子管理器</p>
	 * @param id 身份证
	 * @param sites 工地集合
	 * @param dealSalarys 工资集合
	 * @param types 工种集合
	 */
	public void updateWorkerBuildingSite(String id,Object[] sites,Double[] dealSalarys,String[] types){
		AnBean worker=getWorker(id);
		if (worker==null)
			return;

		ArrayList<String> oldSite= (ArrayList<String>) worker.find(PropertyFactory.LABEL_SITE).getValue();

		//删除旧工地
		ArrayList<String> delete=new ArrayList<>();//要删除的工地
		for (int i=0;i<oldSite.size();i++){
			boolean found=false;
			for (Object site:sites){
				if (site.equals(oldSite.get(i)))
					found=true;
			}
			if (!found)
				delete.add(oldSite.get(i));//不存在就加入到删除列队中
		}
		for (String site:delete){
			deleteWorkerFromBuildingSite(id,site);
		}

		//增加新工地
		ArrayList<String> add=new ArrayList<>();
		for (Object o:sites){
			boolean found=false;
			for (String site:oldSite){
				if (o.equals(site))
					found=true;
			}
			//不存在就加入到添加列表中
			if (!found)
				add.add((String) o);
		}
		for(int i=0;i<sites.length;i++){
			addWorkerToSite(id, (String) sites[i],dealSalarys[i],types[i]);
		}
	}

}
