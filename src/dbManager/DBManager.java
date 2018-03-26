package dbManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
import application.Application;
import resource.Resource;

/**
 * ���ݹ������
 * @author AN
 *
 */
public class DBManager {
	
	private IRunStateCallback runStateCallback=null;

	/**
	 * ��������״̬�ص�
	 * @param callback
	 */
	public void setRunStateCallback(IRunStateCallback callback) {
		runStateCallback=callback;
	}

	/**
	 * ����״̬�ص����ж��Ƿ��һ��ʹ�ó���
	 * @author AN
	 *
	 */
	interface IRunStateCallback{
		void runningState(boolean b);
	}
	




	
	
	//���
	//����
	private static DBManager manager=null;
	
	//��Ա���
	private boolean runningState=true;

	//�û���Ϣ
	private ArrayList<User> userList=null;




    /* =============================== װ�ص����� ===========================*/
    //װ�ص��û�ʵ��
	private User user=null;

    //��������
	private boolean workerPropertyLoaded=false;
    private Anbean workerProperty =null;
    //��������
	private boolean workerListLoaded=false;
    private ArrayList<Anbean> workerList=null;

    //�ʲ�����
	private boolean assetsArrayListLoaded=false;
    private ArrayList<Assets> assetsArrayList=null;






    //��������



    /*
    ===================================================================
     */

	
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
			//e.printStackTrace();
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
	 * �����û����ݵ��ļ���
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
     * ����װ�ص��û����ݵ��ļ�
     */
	public void updateUserData(){
        if(user!=null){

            if(workerProperty !=null){
                try {
                    writeObject(user.getWorkerPropertyPath(), workerProperty);
                } catch (IOException e) {
                    Application.errorWindow("�޷�д�빤�����ݵ��ļ��������Ƿ��Ŀ¼��Ȩ�޶�д��"+e.getMessage());
                }
            }

            if(assetsArrayList !=null){
                try {
                    writeObject(user.getAssetsPath(), assetsArrayList);
                } catch (IOException e) {
                    Application.errorWindow("�޷�д���ʲ����ݵ��ļ��������Ƿ��Ŀ¼��Ȩ�޶�д��"+e.getMessage());
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
	 * ���ļ���װ�ع�������
	 * @return
	 */
	public Anbean loadingWorkerProperty(){
		//���û��˳�
		if(user==null)
			return null;
		//�������ػ��ߵ�һ�μ���
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
		//��λ�ȡ
		return workerProperty;
	}

	public int getWorkerPropertySize(){
		if(workerPropertyLoaded)
			return workerProperty.getSize();
		return 0;
	}





	/**
	 * ���ļ���װ�ع��˶���
	 * @return
	 */
	public ArrayList<Anbean> loadingWorkerList() {

		//�ն���
		if(user==null)
			return null;
		//�״�����
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
		//��μ���
		return workerList;
	}


	public int getWorkerListSize(){
		if(workerListLoaded)
			return workerList.size();
		return 0;
	}







	/**
	 * �����û����ڴ���
	 * @param user
	 */
	public void addUser(User user) {
		userList.add(user);
	}
	

	//User����

	/**
	 * װ��User����
	 * @param user
	 */
	public void loadUser(User user){
		this.user=user;
		workerProperty=loadingWorkerProperty();
		workerList=loadingWorkerList();

		//Debug��װ�ز����б���
		Random r=new Random();
		for(int i=0;i<10;i++){
			Anbean w=WorkerFactory.createWorker();
			Info inf1=w.get("����");
			inf1.setValue("����"+i);
			Info inf2=w.get("���֤");
			inf2.setValue(String.valueOf(r.nextInt(10000)));
			workerList.add(w);
		}


	}








	/**
	 * װ�ع������ԣ�ȫ���滻
	 * @param bean
	 */
    public void setWorkerProperty(Anbean bean){
        if(bean!=null){
            workerProperty =bean;
        }
    }







	/**
	 * ����һ����������
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

	/**
	 * ֱ�ӷ����Ѿ�ʵ������DBManager����
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
	 * �Ƿ�����û�����
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
	 *д������ļ��У���ȡ�ļ�·�������û�������Զ������ļ�
	 * @param path �ļ�����·��
	 * @param object ��Ҫд�뵽�ļ��Ķ���
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
