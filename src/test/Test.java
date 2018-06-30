package test;

import application.AnUtils;
import application.ProgressbarDialog;
import application.StartWindow;
import dbManager.DBManager;
import dbManager.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Random;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Test extends JFrame{

	private StartWindow startWindow=new StartWindow();

	private Test() {
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setFont(new Font("微软雅黑 Light", Font.PLAIN, 14));
		
		setSize(500, 500);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("切换文字");
		btnNewButton.setBounds(6, 6, 90, 30);
		getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(e -> setDialogText());
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER)setDialogText();
			}
		});
		textField.setBounds(6, 48, 122, 30);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton button = new JButton("获取身份");
		button.addActionListener(e -> System.out.println(Test.IDRandom()));
		button.setBounds(6, 90, 90, 30);
		getContentPane().add(button);


		//startWindow.setVisible(true);



		
		setVisible(true);
	}
	
	private void setDialogText() {
		startWindow.setText(textField.getText());
	}


	final String JDBC_DRIVE="com.mysql.cj.jdbc.Driver";
	final String DB_URL="jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Hongkong";
	final String NAME="root";
	private JTextField textField;


	private Connection getCon(String user,String password) throws ClassNotFoundException, SQLException {
		Class.forName(JDBC_DRIVE);

		System.out.println("连接数据库");
		Connection connection=DriverManager.getConnection(DB_URL,user,password);
		return connection;
	}

	private int insert(DBUser user){
		try {
			String sql="insert into user(user_account,password,user_name,limits,sex) values(?,?,?,?,?)";
			Connection connection=getCon(NAME,"");
			PreparedStatement statement=connection.prepareStatement(sql);
			statement.setString(1,user.id);
			statement.setString(2,user.ps);
			statement.setString(3,user.name);
			statement.setInt(4,user.limits);
			statement.setString(5,user.sex);
			int i= statement.executeUpdate();
			statement.close();
			connection.close();
			return i;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}



	
	public static void main(String[] args) throws InterruptedException {
		User user=new User();
		user.userName="test";
		user.password="123456";
		
		AnUtils.setLookAndFeel(AnUtils.LOOK_AND_FEEL_DEFAULT);
		//添加工地
		try {
			DBManager.prepareDataBase();//准备DB
			DBManager.getManager().loadUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ProgressbarDialog.showDialog("测试",0,100);
		ProgressbarDialog.setState("haha",50);
		Thread.sleep(3000);
		ProgressbarDialog.setState("haha",60);
		Thread.sleep(500);
		ProgressbarDialog.setState("haha",80);
		Thread.sleep(500);
		ProgressbarDialog.setState("haha",100);

		Thread.sleep(3000);
		ProgressbarDialog.showDialog("测试",0,100);
		ProgressbarDialog.setState("haha",50);
		Thread.sleep(3000);
		ProgressbarDialog.setState("haha",60);
		Thread.sleep(500);
		ProgressbarDialog.setState("haha",80);
		Thread.sleep(500);
		ProgressbarDialog.setState("haha",100);

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
		sb.append((v2+r.nextInt(40)));
		v3=r.nextInt(13);
		if (v3<10)
			sb.append(0);
		sb.append(v3);
		int v4=r.nextInt(31);
		if (v4<10)
			sb.append(0);
		sb.append(v4);
		for (int i=0;i<3;i++)
			sb.append(r.nextInt(10));
		
		return AnUtils.getID(sb.toString());
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

class DBUser{
		String id;
		String ps;
		String name;
		int limits;
		String sex;

		public DBUser(String id,String ps,String name,int limits,String sex){
			this.id=id;
			this.ps=ps;
			this.name=name;
			this.limits=limits;
			this.sex=sex;
		}
}
