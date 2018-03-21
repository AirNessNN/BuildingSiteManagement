package dbManager;

import java.io.*;
import java.util.ArrayList;
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
	
	private ArrayList<User> userList=null;

    /* =============================== װ�ص����� ===========================*/
	private User user=null;

    //��������
    private Anbean workerProperty =null;
    //��������
    private ArrayList<Anbean> workerList=null;


    //�ʲ�����
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
     * �����û��������Ե��ļ�
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
	

	//User����
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
