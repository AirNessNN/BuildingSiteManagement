package test;

import application.AnUtils;
import application.WorkerWindow;
import dbManager.DBManager;
import dbManager.PropertyFactory;
import dbManager.User;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

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
			DBManager.getManager().addBuildingSite("测试工地");







		} catch (Exception e) {
			e.printStackTrace();
		}

		WorkerWindow wd=new WorkerWindow();

		wd.setVisible(true);

		



	}

	private void createUIComponents() {
		// TODO: place custom component creation code here
	}
}
