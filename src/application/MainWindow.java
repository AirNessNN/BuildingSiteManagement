package application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import component.*;
import dbManager.DBManager;
import dbManager.User;
import resource.Resource;

/**
 * 工地管理系统的主窗口
 * 
 * @author Dell
 *
 */
public class MainWindow extends Window implements ComponentLoader {

	private Rectangle rectangle=new Rectangle(295,390,90,60);


	private static MainWindow mainWindow;

	private AnImageButton btnWorker;
	private AnImageButton btnSite;
	private AnImageButton btnAcess;
	private AnImageButton btnSetting;

	private WorkerWindow workerWindow=null;
	private SiteWindow siteWindow=null;
	private JLabel label;


	

	private MainWindow() {
		setResizable(false);
		setBackground(Color.WHITE);
		getContentPane().setBackground(SystemColor.window);
		initializeComponent();
		initializeEvent();
		initializeData();
	}



	public static MainWindow getMainWindow(){
		//准备数据
		if(mainWindow==null){
			mainWindow=new MainWindow();
		}
		return mainWindow;
	}



	@Override
	public void initializeComponent() {
		setTitle("首页");
		setSize(926, 602);
		setMinimumSize(getSize());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.WHITE);
		
		btnWorker = new AnImageButton("工人管理");
		btnWorker.setFont(new Font("等线", Font.PLAIN, 40));
		btnWorker.setBounds(53, 81, 464, 82);
		getContentPane().add(btnWorker);


		btnSite = new AnImageButton("工地管理");
		btnSite.setFont(new Font("等线", Font.PLAIN, 40));
		btnSite.setBounds(53, 173, 464, 82);
		getContentPane().add(btnSite);

		btnAcess = new AnImageButton("资产管理");
		btnAcess.setFont(new Font("等线", Font.PLAIN, 40));
		btnAcess.setBounds(53, 265, 464, 82);
		getContentPane().add(btnAcess);
		
		label = new JLabel("工地管理系统");
		label.setForeground(SystemColor.textHighlight);
		label.setFont(new Font("等线", Font.PLAIN, 99));
		label.setBounds(340, 413, 704, 206);
		getContentPane().add(label);
		
		btnSetting = new AnImageButton("设置");
		btnSetting.setToolTipText("设置");
		btnSetting.setFont(new Font("等线", Font.PLAIN, 40));
		btnSetting.setBounds(53, 357, 464, 82);
		getContentPane().add(btnSetting);
		
	}

	@Override
	public void initializeEvent() {
		//窗口关闭事件（保存数据）
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Application.updateUserData();
				Application.saveSetting();
			}
		});

		btnWorker.setActionListener(event -> {
			if (event.getAction()==AnActionEvent.CILCKED){
				setVisible(false);
				if (workerWindow==null){
					workerWindow=new WorkerWindow();
					workerWindow.setCallback(values -> setVisible(true));
				}
				workerWindow.setVisible(true);
			}
		});

		btnAcess.setActionListener(event -> {
			if (event.getAction()==AnActionEvent.CILCKED){

			}
		});

		btnSetting.setActionListener(event -> {
			if (event.getAction()==AnActionEvent.CILCKED){

			}
		});

		btnSite.setActionListener(event -> {
			if (event.getAction()==AnActionEvent.CILCKED){
				this.setVisible(false);
				if (siteWindow==null) {
					siteWindow = new SiteWindow();
					siteWindow.setCallback(values -> this.setVisible(true));
				}
				siteWindow.setVisible(true);
			}
		});






		new Thread(()->{
			while (true){

				Point mouse=MouseInfo.getPointerInfo().getLocation();
				float pensentX=mouse.x/20;
				float pensentY=mouse.y/20;

				label.setLocation(rectangle.x+Math.round(pensentX),rectangle.y+Math.round(pensentY));
				try {
					Thread.sleep(16);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void initializeData() {
		btnWorker.setImage(
				AnUtils.getImageIcon(Resource.getResource("btn_worker_normal.png")),
				AnUtils.getImageIcon(Resource.getResource("btn_worker_press.png")),
				AnUtils.getImageIcon(Resource.getResource("btn_worker_enter.png")));
		btnSite.setImage(
				AnUtils.getImageIcon(Resource.getResource("btn_site_normal.png")),
				AnUtils.getImageIcon(Resource.getResource("btn_site_press.png")),
				AnUtils.getImageIcon(Resource.getResource("btn_site_enter.png"))
		);
		btnSetting.setImage(
				AnUtils.getImageIcon(Resource.getResource("btn_setting_normal.png")),
				AnUtils.getImageIcon(Resource.getResource("btn_setting_press.png")),
				AnUtils.getImageIcon(Resource.getResource("btn_setting_enter.png"))
		);
		btnAcess.setImage(
				AnUtils.getImageIcon(Resource.getResource("btn_access_noraml.png")),
				AnUtils.getImageIcon(Resource.getResource("btn_access_press.png")),
				AnUtils.getImageIcon(Resource.getResource("btn_access_enter.png"))
		);
	}
}
