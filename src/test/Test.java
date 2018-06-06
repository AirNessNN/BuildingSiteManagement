package test;

import application.AnUtils;
import dbManager.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

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
		
		AnUtils.setLookAndFeel(AnUtils.LOOK_AND_FEEL_DEFAULT);
		//添加工地
		try {
			DBManager.prepareDataBase();//准备DB
			DBManager.getManager().loadUser(user);
			DBManager.getManager().createBuildingSite("测试工地");
			//DBManager.getManager().createBuildingSite("测试工地");







		} catch (Exception e) {
			e.printStackTrace();
		}

		//WorkerWindow wd=new WorkerWindow();

		//wd.setVisible(true);
		//AnPopDialog.show(null,"阿萨德和考拉上的痕迹卡的阿手机客户端按实际的",AnPopDialog.SHORT_TIME)
		//System.out.println(EntryWindow.showWindow());
		/*AnBean worker=DBManager.getManager().getWorker(0);
		InfoWindow infoWindow=new InfoWindow();
		infoWindow.initializeWorker(
				DBManager.getBeanInfoStringValue(worker,PropertyFactory.LABEL_ID_CARD),
				DBManager.getManager().getWorkerAt(DBManager.getBeanInfoStringValue(worker,PropertyFactory.LABEL_ID_CARD)).getColumn(0)
				);
		infoWindow.setVisible(true);*/

		/*AnColumn column=new AnColumn(true,true,"哈哈");
		column.addValue(2);
		column.addValue(null);
		column.addValue(null);
		column.addValue(12);
		column.addValue(null);
		column.set(100,"");
		System.out.println(column.indexOf(null));
		System.out.println(column.size());
		column.setNullAble(false);
		System.out.println(column.size());
		AnDataTable dataTable=PropertyFactory.createBuildingSite();
		dataTable.setName("测试工地");
		System.out.println(dataTable);*/

		System.out.println(DBManager.getManager().getSalaryManager().getDataBase().get(0));


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

	/**
	 * 打印List中的所有元素
	 * @param owner
	 * @param list
	 * @param index
	 */
	public static void printList(Object owner,Collection list,String index){
		if (index==null)
			index="";
		System.out.println(index+owner.getClass().getName()+"：");
		if (list==null){
			return;
		}
		for (Object o:list){
			if (o==null) {
				System.out.println(index + "null");
				continue;
			}
			if (o instanceof Collection){
				printList(o, (Collection) o,index+"\t");
			}else
				System.out.println(index+o.toString());
		}
	}
}
