package test;

import animation.AnimationManager;
import animation.Iterator;
import application.*;
import component.*;
import dbManager.DBManager;
import dbManager.DataTable;
import dbManager.Info;
import dbManager.User;
import resource.Resource;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.Collection;
import java.util.Random;

public class Test extends JFrame{


	AnTable table=new AnTable();
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	private Test() {
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setFont(new Font("微软雅黑 Light", Font.PLAIN, 14));
		
		setSize(1049, 800);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 6, 595, 715);
		getContentPane().add(scrollPane);
		scrollPane.setViewportView(table);
		
		JButton button = new JButton("增加一列");
		button.setBounds(747, 11, 90, 30);
		getContentPane().add(button);
		button.addActionListener(e -> {
			table.addColumn(textField.getText());
		});
		
		textField = new JTextField();
		textField.setBounds(613, 11, 122, 30);
		getContentPane().add(textField);
		textField.setColumns(10);

		
		JButton button_1 = new JButton("删除选中行");
		button_1.setBounds(849, 11, 90, 30);
		getContentPane().add(button_1);
		button_1.addActionListener(e -> {
			AnPopDialog.show(null,"sadasd",AnPopDialog.SHORT_TIME);
		});
		
		textField_1 = new JTextField();
		textField_1.setBounds(613, 53, 122, 30);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JButton button_2 = new JButton("设置数据");
		button_2.setBounds(747, 53, 90, 30);
		getContentPane().add(button_2);
		button_2.addActionListener(e -> {
			Rank rank=table.getSelectedRank();
			if (rank==null)return;
			table.setCell(rank.getRow(),rank.getColumn(),textField_1.getText());
		});
		
		textField_2 = new JTextField();
		textField_2.setBounds(613, 95, 326, 30);
		getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		JButton btnNewButton = new JButton("增加一行");
		btnNewButton.setBounds(613, 131, 90, 30);
		getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(e -> {
			Object[] values=textField_2.getText().split(" ");
			table.addRow(values);
		});



		
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

		new Test().setVisible(true);
		new Thread(()->{
			JFrame frame=new JFrame();
			frame.dispose();
			System.out.println("关闭");
		}).start();




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
