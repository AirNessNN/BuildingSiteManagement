package dbManager;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import javax.swing.*;

import application.AnUtils;
import application.Application;
import application.EntryWindow;
import application.WindowBuilder;
import component.AnPopDialog;
import component.IDateValueItem;
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
	 * @param callback 回调
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
    private DataTable workerProperty =null;
    //工人数据
	private boolean workerListLoaded=false;
    private ArrayList<Bean> workerList=null;
    //工地数据
	private boolean buildingSiteLoaded=false;
	private ArrayList<DataTable> buildingSiteLIst=null;
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
	private Bean settings=null;
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
		for(int i=0;i<20;i++){
			Bean w= PropertyFactory.createWorker();
			w.find(PropertyFactory.LABEL_NUMBER).setValue(i);
			Info inf1=w.find("名字");
			inf1.setValue("名字"+r.nextInt(456123));
			Info inf2=w.find("身份证");
			inf2.setValue(Test.IDRandom());
			w.find(PropertyFactory.LABEL_PHONE).setValue("13123376032");
			w.find(PropertyFactory.LABEL_SEX).setValue(workerProperty.findColumn(PropertyFactory.LABEL_SEX).getValues().get(r.nextInt(workerProperty.findColumn(PropertyFactory.LABEL_SEX).size())));//性别
			w.find(PropertyFactory.LABEL_BANK_ADDRESS).setValue("乱写的地址");
			//工地
			addWorker(w);
			int ind=r.nextInt(workerProperty.findColumn(PropertyFactory.LABEL_WORKER_TYPE).size());
			addWorkerToSite(
					w.find(PropertyFactory.LABEL_ID_CARD).getValueString(),
					buildingSiteLIst.get(r.nextInt(buildingSiteLIst.size())).getName(),
					(double) r.nextInt(300),
					(String) workerProperty.findColumn(PropertyFactory.LABEL_WORKER_TYPE).get(ind),
					new Date()
			);
		}
		//添加一个工人
		Bean wk=PropertyFactory.createWorker();
		setBeanInfo(wk,PropertyFactory.LABEL_BANK_ID,Test.IDRandom());
		setBeanInfo(wk,PropertyFactory.LABEL_ID_CARD,Test.IDRandom());
		setBeanInfo(wk,PropertyFactory.LABEL_ADDRESS,"厦门市集美区孙坂南路1199号");
		setBeanInfo(wk,PropertyFactory.LABEL_PHONE,"1131123376032");
		setBeanInfo(wk,PropertyFactory.LABEL_NAME,"胡浩伟");
		setBeanInfo(wk,PropertyFactory.LABEL_NATION,"汉族");
		setBeanInfo(wk,PropertyFactory.LABEL_TAG,"这是个测试用例");
		addWorker(wk);
		//添加到工地
		addWorkerToSite(wk.find(PropertyFactory.LABEL_ID_CARD).getValueString(),"测试工地1",200d,"包工",AnUtils.getDate(2016,11,20));
		addWorkerToSite(wk.find(PropertyFactory.LABEL_ID_CARD).getValueString(),"测试工地2",150d,"包工",AnUtils.getDate(2016,10,20));
	}

	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++








	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//WorkerProperty

	/**
	 * 增加一条工人属性
	 * @param info
	 */
	public void addWorkerProperty(Column info) throws Exception {
		if(workerProperty !=null){
			workerProperty.addColumn(info);
			PropertyFactory.addUserData(new Info(info.getName(),""));
		}
	}

	public void createWorkerProperty(boolean b,String name) throws Exception {
		Column column =new Column(b,name);
		addWorkerProperty(column);
	}

	/**
	 *	移除工人属性
	 * @param info
	 */
	public void removeWorkerProperty(Column info){
		if(workerProperty !=null){
			workerProperty.removeColumn(info);
			PropertyFactory.removeUserDate(info.getName());
		}
	}

	public void removeWorkerProperty(String name){
		if (workerProperty!=null){
			workerProperty.removeColumn(getWorkerProperty(name));
			PropertyFactory.removeUserDate(name);
		}
	}

	public boolean updateWorkerProperty(String oldName,String rv){

		String oldV=oldName;
		//找到旧数据
		if (rv==null||rv.equals(""))
			return false;
		if (manager.getWorkerProperty(rv)!=null||PropertyFactory.createWorker().find(rv)!=null){
			Application.errorWindow("此新的属性名与系统属性冲突，无法添加！");
			return false;
		}
		//替换数据
		for (Bean bean:manager.loadingWorkerList()){
			Info info =bean.find(oldV);
			if (info ==null)
				continue;
			info.setName(rv);
		}
		manager.getWorkerProperty(oldV).setName(rv);
		PropertyFactory.removeUserDate(oldV);
		PropertyFactory.addUserData(new Info(rv,""));
		return true;
	}

	/**
	 *	通过节点号获取工人属性InfoArray
	 * @param index
	 * @return
	 */
	public Column getWorkerProperty(int index){
		if(workerProperty !=null){
			return workerProperty.columnAt(index);
		}
		return null;
	}

	/**
	 *	通过名称获取工人属性InfoArray
	 * @param name
	 * @return
	 */
	public Column getWorkerProperty(String name){
		if(workerProperty !=null){
			return workerProperty.findColumn(name);
		}
		return null;
	}

	/**
	 * 通过名字获取该属性下所有字段枚举，由String数组封装
	 * @param name
	 * @return
	 */
	public String[] getWorkerPropertyArray(String name){
		Column array=getWorkerProperty(name);
		String[] strs=new String[array.size()];
		for (int i = 0; i<array.size(); i++){
			strs[i]= (String) array.get(i);
		}
		return strs;
	}


	/**
	 * 装载工人属性，全盘替换
	 * @param table 属性表
	 */
	public void setWorkerProperty(DataTable table){
		if(table!=null){
			workerProperty =table;
			PropertyFactory.setUserDatas(workerProperty);
		}
	}

	/**
	 * 从文件中装载工人属性
	 * @return
	 */
	public DataTable loadingWorkerProperty(){
		//空用户退出
		if(user==null)
			return null;
		//启动加载或者第一次加载
		if(!workerPropertyLoaded){
			try {
				workerProperty =(DataTable) readObject(user.getWorkerPropertyPath());
				//把读取到的数据放到工厂用于生产
				for (Column column:workerProperty.getValues()){
					PropertyFactory.addUserData(new Info(column.getName(),""));
				}
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
	public ArrayList<Bean> loadingWorkerList() {

		//首次启动
		if(!(workerListLoaded)){
			try {
				workerList=(ArrayList<Bean>)readObject(user.getWorkerListPath());

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
	public boolean addWorker(Bean bean){
		if (bean==null)
			return false;
		String id=bean.find(PropertyFactory.LABEL_ID_CARD).getValueString();
		//排重
		for (Bean w:loadingWorkerList()){
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
	public Bean getWorker(int index){
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
	public Bean getWorker(String id){
		if (workerListLoaded){
			for (Bean bean:workerList){
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
	public ArrayList<Bean> getWorkerListWhere(String name, Object value){
		ArrayList<Bean> tmpList=new ArrayList<>();

		if(workerListLoaded){
			for(Bean bean:workerList){
				for (Info info : bean.getArray()){
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
	public ArrayList<DataTable> loadingBuildingSiteList(){
		if (!userLoaded)
			return null;

		if (buildingSiteLoaded)
			return buildingSiteLIst;
		try{
			buildingSiteLIst= (ArrayList<DataTable>) readObject(user.getBuildingSitePath());
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
	public void createBuildingSite(DataTable buildingSite) throws Exception {
		if (!buildingSiteLoaded)
			return;

		for (DataTable bean:buildingSiteLIst){
			if (bean.getName().equals(buildingSite.getName()))
				throw new Exception("工地重名，无法添加到列表中！");
		}
		buildingSiteLIst.add(buildingSite);
		//下面的步骤是用户创建工地之后，一定要将工地添加到属性中去
		if (workerPropertyLoaded){
			workerProperty.findColumn(PropertyFactory.LABEL_SITE).addValue(buildingSite.getName());
		}
	}

	/**
	 * 向DB中添加一个空的工地
	 * @param name 工地名称
	 */
	public DataTable createBuildingSite(String name) throws Exception {
		if (!buildingSiteLoaded)
			return null;
		DataTable site=PropertyFactory.createBuildingSite();
		site.setName(name);
		createBuildingSite(site);
		return site;
	}

	/**
	 * 返回该工地的实例
	 * @param siteName 工地名字
	 * @return
	 */
	public DataTable getBuildingSite(String siteName){
		if (!buildingSiteLoaded)
			return null;
		for (DataTable bean:buildingSiteLIst)
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

		DataTable site=getBuildingSite(siteName);
		Column column=site.findColumn(PropertyFactory.LABEL_ID_CARD);
		if (column!=null){
			int size=column.size();

			if (size==0)
				return null;

			tmpWorker=new String[size];

			int index=0;

			for (Object o:column.getValues()){
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
		if (!buildingSiteLoaded)
			return null;
		ArrayList<String> tmpList=new ArrayList<>();
		for (DataTable site:buildingSiteLIst){
			if (site.findColumn(PropertyFactory.LABEL_ID_CARD).contains(id)){
				tmpList.add(site.getName());
			}
		}
		return  tmpList;
	}

	public void deleteBulidingSite(DataTable site){
		if (!buildingSiteLoaded)
			return;
		buildingSiteLIst.remove(site);

		//更新属性中的数据
		if (workerPropertyLoaded){
			workerProperty.findColumn(PropertyFactory.LABEL_SITE).getValues().remove(site.getName());
		}
		//更新所有工人中的数据
		if (workerListLoaded){
			for (Bean bean:workerList){
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
	public boolean addWorkerToSite(String id,String site,Double dealSalary,String workType,Date entry){
		if (!buildingSiteLoaded)
			return false;

		DataTable dt=getBuildingSite(site);
		if (dt==null)
			return false;
		Object[] obj=new Object[]{id,dealSalary,workType,entry,null};
		if (!dt.addRow(obj))
			return false;

		try {
			updateChildrenManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	/**
	 * 获取工人在此工地的工作状态
	 * @param id 身份证
	 * @param siteName 工地名
	 * @return true已经离职或找不到该工人，false工人在职
	 */
	public boolean isWorkerLeave(String id,String siteName){
		if (id==null||id.equals(""))
			return true;
		if (siteName==null||siteName.equals(""))
			return true;

		DataTable site=getBuildingSite(siteName);
		if (site==null)
			return true;
		site.selectRow(PropertyFactory.LABEL_ID_CARD,id);
		Date date= (Date) site.getSelectedRowAt(PropertyFactory.LABEL_LEAVE_TIME);
		return date != null;
	}

	/**
	 * 获取工人是否在所有工地离职
	 * @param id 身份证
	 * @return true已经离职所有工地，false还有工地没有离职
	 */
	public boolean isWorkerLeaveAllSite(String id){
		ArrayList<String> siteNames= getWorkerAt(id);
		if (siteNames.size()>0){
			for (String siteName:siteNames){
				if (!isWorkerLeave(id,siteName))
					return false;
			}
		}
		return true;
	}

	/**
	 * 获取工人离职的工地名称
	 * @param id 身份证
	 * @return 返回找的到记录的离职的工地的数组
	 */
	public String[] getWorkerLeave(String id){
		ArrayList<String> leaving=new ArrayList<>();
		ArrayList<String> siteNames= getWorkerAt(id);
		if (siteNames.size()>0){
			for (String siteName:siteNames){
				if (isWorkerLeave(id,siteName))
					leaving.add(siteName);
			}
		}
		return AnUtils.toStringArray(leaving.toArray());
	}

	/**
	 * 获取在职员工的人数
	 * @return 人数
	 */
	public int getWorkingWorkerCount(){
		int count=workerList.size();
		for (Bean worker:workerList){
			String id=getBeanInfoStringValue(worker,PropertyFactory.LABEL_ID_CARD);
			if (isWorkerLeaveAllSite(id))
				count--;
		}
		return count;
	}



	private int leaveCount=0;
	private int leaveToday=0;
	private int birthdayTodayCount=0;
	private int workingCount=0;
	private int sumWorkerCount=0;

	/**
	 * 更新离职总人数，在职总人数，今日生日人数，总人数，今日离职人数
	 * 注意：今日离职的人数是根据工地计算，也就是说一个工人在两个工地同时离职，算两个今日离职。
	 * 离职总数判断，只要工人还在一个工地做事，那就不算离职，所以今日离职和离职总数计算会有出入
	 * 生日人数是只有在职员工才会计算，离职员工不会统计是生日人数
	 */
	public void updateWorkerBaseData(){
		sumWorkerCount=loadingWorkerList().size();//总工人数量
		leaveCount=0;
		leaveToday=0;
		birthdayTodayCount=0;
		workingCount=0;
		Date date=new Date();

		for (Bean bean:loadingWorkerList()){
			String id=getBeanInfoStringValue(bean,PropertyFactory.LABEL_ID_CARD);
			boolean workingFlag = false,leaveFlag=true,birthdayFlag=false,leaveTodayFlag=false;
			ArrayList<String> siteList=getWorkerAt(id);

			for (String aSiteList : siteList) {
				DataTable site = getBuildingSite(aSiteList);
				site.selectRow(PropertyFactory.LABEL_ID_CARD, id);
				Date leave= (Date) site.getSelectedRowAt(PropertyFactory.LABEL_LEAVE_TIME);
				if ( leave!= null) {
					if (AnUtils.isDateYMDEquality(leave,date)){
						leaveToday++;
					}

				} else {
					workingFlag = true;
					leaveFlag=false;
					if (AnUtils.isDateMDEquality(date, AnUtils.convertBornDate(id))) {//生日
						birthdayFlag = true;
					}
				}
			}
			if (workingFlag)
				workingCount++;
			if (birthdayFlag)
				birthdayTodayCount++;
			if (leaveFlag)
				leaveCount++;
		}
	}

	public int getLeaveCount() {
		return leaveCount;
	}

	public int getBirthdayTodayCount() {
		return birthdayTodayCount;
	}

	public int getLeaveToday() {
		return leaveToday;
	}

	public int getSumWorkerCount() {
		return sumWorkerCount;
	}

	public int getWorkingCount() {
		return workingCount;
	}


	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++







	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//CheckInManager
	public ChildrenManager getCheckInManager(){
		if (checkInManagerLoaded)
			return checkInManager;
		return null;
	}

	/**
	 * 获取该天工人在任意工地工作超过指定出勤指数的工人数量
	 * @param date 日期
	 * @return 返回数量
	 */
	public int getCheckInCount(Date date,double value){
		int count =0;
		for (Bean worker:workerList){
			String id=getBeanInfoStringValue(worker,PropertyFactory.LABEL_ID_CARD);
			ArrayList<String> siteNames=getWorkerAt(id);//获取工地
			for (String siteName:siteNames){
				Double d= (Double) checkInManager.getValueAt(id,siteName,date);
				if (d!=null&&d>=value){
					count++;
					break;
				}
			}
		}
		return count;
	}

	@Deprecated
	public void updateCheckInManagerData(){

	}
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++







	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//SalaryManager

	private double sumSalary=0f;//发放生活费数额
	private int sumGotSalaryTodayCount=0;//今日获取生活费的人数
	private double sumSalaryToday=0;//今日发放数额

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
	public void updateChildrenManager() throws Exception {
		if (salaryManagerLoaded)
			salaryManager.updateWorkerList();
		if (checkInManagerLoaded)
			checkInManager.updateWorkerList();
	}

	/**
	 * 更新工资数据
	 */
	public void updateSalaryManagerData(){
		Date  date=new Date();
		sumGotSalaryTodayCount=0;
		sumSalary=0;
		sumSalaryToday=0;
		for (DataTable id:salaryManager.getDataBase()){
			for (Column site:id.getValues()){
				for (Object o:site.toArray()){
					IDateValueItem item= (IDateValueItem) o;
					Double d;
					if (item==null)
						continue;
					d= (Double) item.getValue();
					//今日
					if (AnUtils.isDateYMDEquality(date,item.getDate())){
						sumSalaryToday+=d;
						sumGotSalaryTodayCount++;
					}
					//总数
					sumSalary+=d;
				}
			}
		}
	}

	/**
	 * 获取领取生活费的总数
	 * @return 总数
	 */
	public double getSumSalary() {
		return sumSalary;
	}

	/**
	 * 获取今日领取生活费的总数
	 * @return zongshu
	 */
	public double getSumSalaryToday() {
		return sumSalaryToday;
	}

	/**
	 * 获取今日领取生活费的人数
	 * @return 人数
	 */
	public int getSumGotSalaryTodayCount() {
		return sumGotSalaryTodayCount;
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
    public static Bean createWorker(String name, String ID, String phone, String address, String bankID, String bankAddress, String nation, Date enD, String type, String tag){
		if (!manager.userLoaded)
			return null;
		if (!manager.workerListLoaded)
			return null;
		if(!manager.salaryManagerLoaded)
			return null;
		if (!manager.checkInManagerLoaded)
			return null;

		Bean worker=PropertyFactory.createWorker();
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
	public static void setBeanInfo(Bean bean, String propertyName, Object value){
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
	public static String getBeanInfoStringValue(Bean bean, String propertyName){
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
	public static void addBeanArrayInfo(Bean bean, String propertyName, Object value){
		Info info =bean.find(propertyName);
		if (info ==null)
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
	public static void removeBeanArrayInfo(Bean bean, String propertyName, Object value){
		Info info =bean.find(propertyName);
		if (info ==null)
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
	public static String[] getBeanArrayInfoValues(Bean bean, String propertyName, Object value){
		Info info =bean.find(propertyName);
		if (info ==null)
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
	public boolean addWorkerToBuildingSite(Bean bean, DataTable site){
		if (!manager.workerListLoaded&&!manager.buildingSiteLoaded)
			return false;

		boolean workerFound=false;

		//判断工人是否存在
		for (Bean worker:manager.workerList){
			if (bean.find(PropertyFactory.LABEL_ID_CARD).getValue().equals(worker.find(PropertyFactory.LABEL_ID_CARD).getValue())){
				workerFound=true;
				break;
			}
		}
		if (!workerFound)manager.workerList.add(bean);

		//给工人自带的属性中加入该工地
		bean.find(PropertyFactory.LABEL_SITE).addListValue(site.getName());
		site.findColumn(PropertyFactory.LABEL_SITE).addValue(bean.find(PropertyFactory.LABEL_ID_CARD).getValueString());

		try {
			manager.updateChildrenManager();//将数据更新到管理器
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	@Deprecated
	public boolean addWorkerToBuildingSite(String id ,String siteName,double dealSalary,String workType){
		Bean worker=getWorker(id);
		if (worker==null)
			return false;
		DataTable site=getBuildingSite(siteName);
		if (site==null)
			return false;

		//在工人自身属性上添加
		boolean b=worker.find(PropertyFactory.LABEL_SITE).addListValue(siteName);
		if (!b)
			return false;

		//在工地中添加
		Object[] obj=new Object[]{id,dealSalary,workType};
		boolean bs=site.addRow(obj);
		if (!bs){//如果添加失败就移除工人自身属性
			worker.find(PropertyFactory.LABEL_SITE).removeListValue(siteName);
			return false;
		}

		//在所有管理器中添加信息
		try {
			updateChildrenManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		Bean worker=manager.getWorker(id);
		if (worker==null)
			return false;
		DataTable site=manager.getBuildingSite(siteName);
		if (site==null)
			return false;

		worker.find(PropertyFactory.LABEL_SITE).removeListValue(siteName);
		site.setKey(PropertyFactory.LABEL_ID_CARD);
		site.removeKeyRow(id);

		try {
			manager.updateChildrenManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

		Bean worker=EntryWindow.showWindow();
		boolean createFlag=addWorker(worker);//创建完成之后就添加
		if (worker==null)
			return false;
		worker.find(PropertyFactory.LABEL_NUMBER).setValue(workerList.size());
		if (createFlag){
			//工人选择工地
			int res=JOptionPane.showConfirmDialog(null,"是否要添加这个工人的工地信息","提示",JOptionPane.YES_NO_OPTION);
			if (res==JOptionPane.OK_OPTION){
				//确认要添加
				WindowBuilder.showBuildingSiteSelectingWindow(worker.find(PropertyFactory.LABEL_ID_CARD).getValueString(),(e)->{
					String[] sites= (String[]) e[0];
					String[] dealSal= (String[]) e[1];
					String[] wt= (String[]) e[2];
					String[] et= (String[]) e[3];

					//转换单位
					Double[] doubles=new Double[dealSal.length];
					Date[] dates=new Date[et.length];
					SimpleDateFormat dateFormat=new SimpleDateFormat(Resource.DATE_FORMATE);
					for (int i=0;i<dealSal.length;i++){
						try {
							doubles[i]=Double.valueOf(dealSal[i]);
							dates[i]=dateFormat.parse(et[i]);
						} catch (Exception e1) {
							Application.errorWindow("有数据在转换的时候出错，无法继续添加工地信息："+e1.getMessage());
						}
					}

					for (int i=0;i<sites.length;i++){
						addWorkerToSite(
								worker.find(PropertyFactory.LABEL_ID_CARD).getValueString(),
								sites[i],
								doubles[i],
								wt[i],
								dates[i]
						);
					}
					AnPopDialog.show(null,"工人的工地设置完成，一共设置"+sites.length+"个。",AnPopDialog.SHORT_TIME);
				});
			}else
				AnPopDialog.show(null,"创建完成，要查看未选择工地的员工请选中【显示离职】",AnPopDialog.SHORT_TIME);
		}else {
			Application.errorWindow("工人创建失败，可能原因：重复的身份证。");
		}
		return createFlag;
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
	@Deprecated
	public void updateWorkerBuildingSite(String id,Object[] sites,Double[] dealSalarys,String[] types){
		Bean worker=getWorker(id);
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
			//addWorkerToSite(id, (String) sites[i],dealSalarys[i],types[i]);
		}
	}

}
