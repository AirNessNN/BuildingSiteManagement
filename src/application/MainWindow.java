package application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import compoent.AnCardPanel;
import compoent.AnCardPanelItem;
import compoent.ImagePanel;
import dbManager.User;
import resource.Resource;

/**
 * ���ع���ϵͳ��������
 * 
 * @author Dell
 *
 */
public class MainWindow extends JFrame {

	private User user=null;
	
	private JPanel panel = null;
	private AnCardPanel cardPanel=null;
	private AnCardPanelItem workerItem=null;
	private AnCardPanelItem buildSiteItem =null;
	private AnCardPanelItem settingItem=null;
	private AnCardPanelItem resourceItem=null;
	private AnCardPanelItem workItem=null;
	private JPanel sourcePanel=null;

	private static MainWindow mainWindow;


	

	private MainWindow() {
		init();
	}

	private MainWindow(User user){
		init();

		this.user=user;
	}

	private void initView() {
		cardPanel=new AnCardPanel();
		panel.add(cardPanel);

		cardPanel.setSize(60,771);
		setLocation(0,0);
		cardPanel.setItemSize(60,60);
		cardPanel.setSourcePanel(sourcePanel);
		//�����̬����panel
		cardPanel.setOnSelectedListener(item -> {

        });

		//���˹���
		workerItem=new AnCardPanelItem();
		workerItem.setNormalImage(AnUtils.getImageIcon(Resource.getResource("worker_normal.png")));
		workerItem.setEnterImage(AnUtils.getImageIcon(Resource.getResource("worker_enter.png")));
		workerItem.setSelectedImage(AnUtils.getImageIcon(Resource.getResource("worker_selected.png")));
		cardPanel.addButton(workerItem);
		workerItem.TAG="���˹���";

		WorkerPanel workPanel=new WorkerPanel();
		//sourcePanel.add(workPanel);
		workerItem.setPanel(workPanel);



		//���ع���
		buildSiteItem =new AnCardPanelItem();
		buildSiteItem.setNormalImage(AnUtils.getImageIcon(Resource.getResource("resource_normal.png")));
		buildSiteItem.setEnterImage(AnUtils.getImageIcon(Resource.getResource("resource_enter.png")));
		buildSiteItem.setSelectedImage(AnUtils.getImageIcon(Resource.getResource("resource_selected.png")));
		cardPanel.addButton(buildSiteItem);

		
		//����
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
		// ʵ������
		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		sourcePanel=new JPanel(null);
		sourcePanel.setBounds(60,0,934,771);
		panel.add(sourcePanel);

		//���ڹر��¼����������ݣ�
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
