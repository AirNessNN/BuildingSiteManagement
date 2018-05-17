package test;

import application.AnUtils;
import application.WorkerWindow;
import component.AnDialog;
import dbManager.DBManager;
import dbManager.PropertyFactory;
import dbManager.User;
import resource.Resource;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Test extends JFrame{


	private Test() {
		getContentPane().setFont(new Font("微软雅黑 Light", Font.PLAIN, 14));
		
		setSize(1049, 800);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		

	}

	
	public static void main(String[] args) {
		User user=new User();
		user.userName="hahaha";
		user.password="123456";
		
		AnUtils.setLookAndFeel(AnUtils.LOOK_AND_FEEL_NIMBUS);
		//添加工地
		try {
			DBManager.prepareDataBase();//准备DB
			DBManager.getManager().loadUser(user);
			DBManager.getManager().addBuildingSite("测试工地");
			//DBManager.getManager().addBuildingSite("测试工地");







		} catch (Exception e) {
			e.printStackTrace();
		}

		//WorkerWindow wd=new WorkerWindow();

		//wd.setVisible(true);
		//AnDialog.show(null,"阿萨德和考拉上的痕迹卡的阿手机客户端按实际的",AnDialog.SHORT_SHOW)
		String id=IDRandom();
		System.out.println(id);
		System.out.println(AnUtils.convertBornDate(id));
		System.out.println(AnUtils.convertAge(id));
		



	}

	/**
	 * 身份证随机器
	 * @return
	 */
	public static String IDRandom(){
		Random r=new Random();
		int v1=300000;
		int v2=1970;
		int v3;

		StringBuilder sb=new StringBuilder();
		sb.append((v1+r.nextInt(99999)));
		sb.append((v2+r.nextInt(50)));
		v3=r.nextInt(13);
		if (v3<10)
			sb.append(0);
		sb.append(v3);
		int v4=r.nextInt(31);
		if (v4<10)
			sb.append(0);
		sb.append(v4);
		for (int i=0;i<4;i++)
			sb.append(r.nextInt(10));


		return sb.toString();
	}

	private void createUIComponents() {
		// TODO: place custom component creation code here
	}
}
