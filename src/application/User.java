package application;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 用户类
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
	private ArrayList<String> dirs;
	
	
	
	public String question;
	public String answer;
	
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String name,String passworld,String question,String answer) {
		// TODO Auto-generated constructor stub
		userName=name;
		this.password=passworld;
		this.question=question;
		this.answer=answer;
	}
	
	
	/*
	 * 操作Dirs
	 */
	public void setDirs(String dir) {
		if(dirs==null) {
			dirs=new ArrayList<>();
		}
		dirs.add(dir);
	}
	public String getIndex(int index) {
		if(dirs!=null) {
			return dirs.get(index);
		}
		return null;
	}
	public ArrayList<String> getArrList() {
		return dirs;
	}
	
	
	//重写
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
