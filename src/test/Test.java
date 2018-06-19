package test;

import animation.AnimationManager;
import animation.Iterator;
import application.*;
import component.AnAnimButton;
import component.AnimButton;
import dbManager.DBManager;
import dbManager.User;
import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Random;

public class Test extends JFrame{

	Iterator iterator=AnimationManager.getManager().createAnimationIterator(200,100,1);
	Iterator iterator2=AnimationManager.getManager().createAnimationIterator(100,200,1);

	private Test() {
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setFont(new Font("微软雅黑 Light", Font.PLAIN, 14));
		
		setSize(1049, 800);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JButton button=new JButton("开始迭代器");
		button.setBounds(6, 5, 88, 30);
		getContentPane().add(button);
		button.addActionListener((e)->{
			iterator.start();
		});

		JButton button1=new JButton("停止迭代器");
		button1.setBounds(106, 5, 88, 30);
		getContentPane().add(button1);
		button1.addActionListener(e -> {
			iterator.stop();
		});
		
		JButton button_1 = new JButton("测试");
		button_1.setBounds(100, 360, 90, 30);
		getContentPane().add(button_1);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(100, 411, 90, 30);
		getContentPane().add(btnNewButton);
		
		JButton button_2 = new JButton("开始迭代器2");
		button_2.setBounds(6, 47, 88, 30);
		getContentPane().add(button_2);
		button_2.addActionListener(e -> {
			iterator2.start();
		});
		
		JButton button_3 = new JButton("停止迭代器2");
		button_3.setBounds(106, 47, 88, 30);
		getContentPane().add(button_3);
		button_3.addActionListener(e -> {
			iterator2.reverse();
		});


		iterator.setCallback((value -> {
			button_1.setLocation(value,360);
		}));

		iterator2.setCallback(value -> {
			btnNewButton.setLocation(value,411);
		});

		AnAnimButton anAnimButton=new AnAnimButton("asdasd");
		anAnimButton.setBounds(100,200,401,73);
		anAnimButton.setTextBounds(20,10);
		anAnimButton.setFont(new Font("微软雅黑",Font.PLAIN,20));
		getContentPane().add(anAnimButton);
		anAnimButton.repaint();

		AnimButton animButton=new AnimButton();
		animButton.setText("asdasdas");
		animButton.setBounds(300,300,60,30);
		getContentPane().add(animButton);



		
		setVisible(true);
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


		SiteInfoWindow siteInfoWindow=new SiteInfoWindow("测试工地");
		siteInfoWindow.setVisible(true);


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
