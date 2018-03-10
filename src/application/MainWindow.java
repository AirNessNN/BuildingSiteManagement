package application;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import compoent.AnImageButton;
import compoent.AnImageLabel;
import resource.Resource;

/**
 * 工地管理系统的主窗口
 * 
 * @author Dell
 *
 */
public class MainWindow extends JFrame {
	
	private JPanel panel = null;
	private JPanel startBar=null;
	private AnImageButton btnNewButton_3;
	private AnImageButton btnSetting;
	private AnImageButton btnPlaceManager;
	private AnImageButton btnEmployeeManager;
	private AnImageLabel bigScreen=null;
	
	
	

	public MainWindow() {
		this.setSize(1000, 800);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		// 实例容器
		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		initView();
	}

	private void initView() {
		
		startBar = new JPanel();
		startBar.setBounds(0, 660, 994, 100);
		panel.add(startBar);
		startBar.setLayout(null);
		
		bigScreen=new AnImageLabel(AnUtils.getImage(Resource.getResource("big_screen.png")));
		bigScreen.setSize(994,660);
		bigScreen.setLocation(0, 0);
		panel.add(bigScreen);
		
		btnEmployeeManager = new AnImageButton("工人管理：维护账户内的所有工人");
		btnEmployeeManager.setLocation(0, 0);
		btnEmployeeManager.setImage(AnUtils.getImageIcon(Resource.getResource("employee_normal.png")), AnUtils.getImageIcon(Resource.getResource("employee_press.png")), AnUtils.getImageIcon(Resource.getResource("employee_enter.png")));
		startBar.add(btnEmployeeManager);

		btnPlaceManager = new AnImageButton("工地管理：维护工地的资产以及各种信息");
		btnPlaceManager.setLocation(155, 0);
		btnPlaceManager.setImage(AnUtils.getImageIcon(Resource.getResource("buildingSite_normal.png")), AnUtils.getImageIcon(Resource.getResource("buildingSite_press.png")), AnUtils.getImageIcon(Resource.getResource("buildingSite_enter.png")));
		startBar.add(btnPlaceManager);

		btnNewButton_3 = new AnImageButton("包工管理：管理包工信息");
		btnNewButton_3.setLocation(310, 0);
		btnNewButton_3.setImage(AnUtils.getImageIcon(Resource.getResource("employee_normal.png")), AnUtils.getImageIcon(Resource.getResource("employee_press.png")), AnUtils.getImageIcon(Resource.getResource("employee_enter.png")));
		startBar.add(btnNewButton_3);

		btnSetting = new AnImageButton("设置中心");
		btnSetting.setLocation(844, 0);
		btnSetting.setImage(AnUtils.getImageIcon(Resource.getResource("setting_normal.png")), AnUtils.getImageIcon(Resource.getResource("setting_press.png")), AnUtils.getImageIcon(Resource.getResource("setting_enter.png")));
		startBar.add(btnSetting);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
