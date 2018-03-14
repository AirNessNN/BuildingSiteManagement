package application;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import compoent.AnCardPanel;
import compoent.AnCardPanelItem;
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
	private AnCardPanel cardPanel=null;
	private AnCardPanelItem workerItem=null;
	private AnCardPanelItem buildSietItem=null;
	private AnCardPanelItem settingItem=null;
	private AnCardPanelItem resourceItem=null;
	private AnCardPanelItem workItem=null;

	private static MainWindow mainWindow;
	
	
	

	private MainWindow() {
		init();
	}

	private MainWindow(User user){
		init();

	}

	private void initView() {
		cardPanel=new AnCardPanel();
		panel.add(cardPanel);

		cardPanel.setSize(60,771);
		setLocation(0,0);
		cardPanel.setItemSize(60,60);

		workerItem=new AnCardPanelItem();
		workerItem.setNormalImage(AnUtils.getImageIcon(Resource.getResource("worker_normal.png")));
		workerItem.setEnterImage(AnUtils.getImageIcon(Resource.getResource("worker_enter.png")));
		workerItem.setSelectedImage(AnUtils.getImageIcon(Resource.getResource("worker_selected.png")));
		cardPanel.addButton(workerItem);

		buildSietItem=new AnCardPanelItem();
		buildSietItem.setNormalImage(AnUtils.getImageIcon(Resource.getResource("resource_normal.png")));
		buildSietItem.setEnterImage(AnUtils.getImageIcon(Resource.getResource("resource_enter.png")));
		buildSietItem.setSelectedImage(AnUtils.getImageIcon(Resource.getResource("resource_selected.png")));
		cardPanel.addButton(buildSietItem);

		settingItem=new AnCardPanelItem();
		settingItem.setNormalImage(AnUtils.getImageIcon(Resource.getResource("setting_normal.png")));
		settingItem.setEnterImage(AnUtils.getImageIcon(Resource.getResource("setting_enter.png")));
		settingItem.setSelectedImage(AnUtils.getImageIcon(Resource.getResource("setting_selected.png")));
		cardPanel.addButton(settingItem);
	}



	private void init(){
		this.setSize(1000, 800);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		// 实例容器
		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		//窗口关闭事件（保存数据）
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Application.updateUserData();
				Application.saveSetting();
			}
		});
		initView();
	}



	public static MainWindow getMainWindow(User user){
		if(mainWindow==null){
			mainWindow=new MainWindow(user);
		}
		return mainWindow;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
