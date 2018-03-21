package dbManager;

import resource.Resource;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * �û���
 * @author Dell
 *
 */
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String userName;
	public String password;

	private String workerPropertyPath =null;
	private String workerListPath=null;

	private String resourcePath=null;
	
	
	public String question;
	public String answer;
	
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String name,String password,String question,String answer) {
		// TODO Auto-generated constructor stub
		userName=name;
		this.password=password;
		this.question=question;
		this.answer=answer;
	}
	
	
	/*
	 * ======================����Dirs============================
	 */
    //AnBean����
	public String getWorkerPropertyPath(){
		return Resource.getDataDirectoryPath()+"\\"+userName+"workerproperty.ab";
	}

	//ArrayList����
	public String getAssetsPath(){
		return Resource.getDataDirectoryPath()+"\\"+userName+"assetslist.lst";
	}

	//ArrayList����
    public String getWorkerListPath() {
        return Resource.getDataDirectoryPath()+"\\"+userName+"_workerlist.lst";
    }



    //��д
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return userName;
	}
	
	
	public boolean equals(String string) {
		// TODO Auto-generated method stub
		return this.userName.equals(string);
	}
	
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		User user=(User)obj;
		if(!user.userName.equals(this.userName)) 
			return false;
		if(!user.password.equals(this.password))
			return false;
		
		return true;
	}
}
