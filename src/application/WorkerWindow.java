package application;

import component.AnImageLabel;
import component.AnLabel;
import dbManager.AnBean;
import dbManager.DBManager;
import dbManager.PropertyFactory;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

public class WorkerWindow extends JFrame {
    private AnImageLabel imageLabel;
    private AnLabel labIDCard;
    private AnLabel labName;
    private AnLabel labType;

    private AnBean worker;
    private AnLabel labPhone;

    //数据
	private String[] checkinName=new String[] {"1月考勤：", "2月考勤：", "3月考勤：", "4月考勤：", "5月考勤：", "6月考勤：", "7月考勤：", "8月考勤：", "9月考勤：", "10月考勤：", "11月考勤：", "12月考勤："};
	private Float[] checkinValue=new Float[12];


	
	
	public void initializeComponent(String title) {
		getContentPane().setBackground(Color.WHITE);
    	setIconImage(Toolkit.getDefaultToolkit().getImage(WorkerWindow.class.getResource("/resource/app_icon_60.png")));
    	setSize(1021, 771);
    	setMinimumSize(new Dimension(800,771));
    	setLocationRelativeTo(null);
    	setTitle(title);
    	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);

		imageLabel=new AnImageLabel("app_icon.png");
		springLayout.putConstraint(SpringLayout.NORTH, imageLabel, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, imageLabel, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, imageLabel, 282, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, imageLabel, 201, SpringLayout.WEST, getContentPane());
		getContentPane().add(imageLabel);

		JPanel infoPanel = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, infoPanel, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, infoPanel, 336, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, infoPanel, 282, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, infoPanel, 520, SpringLayout.WEST, getContentPane());
		infoPanel.setBackground(Color.WHITE);
		getContentPane().add(infoPanel);
		infoPanel.setLayout(new GridLayout(5, 0, 0, 0));
		
				labName = new AnLabel("张三");
				infoPanel.add(labName);
				labName.setForeground(SystemColor.textHighlight);
				labName.setHorizontalAlignment(SwingConstants.LEFT);
				labName.setFont(new Font("微软雅黑", Font.PLAIN, 20));
						
						labPhone = new AnLabel("联系方式：13123376032");
						labPhone.setText("13123376032");
						labPhone.setForeground(Color.GRAY);
						labPhone.setFont(new Font("微软雅黑", Font.PLAIN, 15));
						labPhone.setHorizontalAlignment(SwingConstants.LEFT);
						infoPanel.add(labPhone);
						
								labIDCard = new AnLabel("身份证：62334199712100072");
								labIDCard.setText("362334199712100072");
								infoPanel.add(labIDCard);
								labIDCard.setForeground(Color.GRAY);
								labIDCard.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								labIDCard.setHorizontalAlignment(SwingConstants.LEFT);
								
								AnLabel labelState = new AnLabel("状态：在职");
								labelState.setText("在职");
								labelState.setForeground(Color.GRAY);
								labelState.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								labelState.setHorizontalAlignment(SwingConstants.LEFT);
								infoPanel.add(labelState);
								
								labType = new AnLabel("工种：点工");
								labType.setText("点工");
								labType.setForeground(Color.GRAY);
								labType.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								infoPanel.add(labType);
								
								JPanel panel = new JPanel();
								springLayout.putConstraint(SpringLayout.NORTH, panel, 10, SpringLayout.NORTH, getContentPane());
								springLayout.putConstraint(SpringLayout.WEST, panel, 249, SpringLayout.WEST, getContentPane());
								springLayout.putConstraint(SpringLayout.SOUTH, panel, 282, SpringLayout.NORTH, getContentPane());
								springLayout.putConstraint(SpringLayout.EAST, panel, 324, SpringLayout.WEST, getContentPane());
								panel.setBackground(Color.WHITE);
								getContentPane().add(panel);
								panel.setLayout(new GridLayout(5, 0, 0, 0));
								
								JLabel label_1 = new JLabel("姓名：");
								label_1.setForeground(Color.DARK_GRAY);
								label_1.setFont(new Font("幼圆", Font.PLAIN, 20));
								label_1.setHorizontalAlignment(SwingConstants.RIGHT);
								panel.add(label_1);
								
								JLabel label_2 = new JLabel("联系方式：");
								label_2.setForeground(Color.GRAY);
								label_2.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								label_2.setHorizontalAlignment(SwingConstants.RIGHT);
								panel.add(label_2);
								
								JLabel label_3 = new JLabel("身份证：");
								label_3.setForeground(Color.GRAY);
								label_3.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								label_3.setHorizontalAlignment(SwingConstants.RIGHT);
								panel.add(label_3);
								
								JLabel label_4 = new JLabel("状态：");
								label_4.setForeground(Color.GRAY);
								label_4.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								label_4.setHorizontalAlignment(SwingConstants.RIGHT);
								panel.add(label_4);
								
								JLabel label_5 = new JLabel("工种：");
								label_5.setForeground(Color.GRAY);
								label_5.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								label_5.setHorizontalAlignment(SwingConstants.RIGHT);
								panel.add(label_5);
								
								JPanel panel_1 = new JPanel();
								springLayout.putConstraint(SpringLayout.NORTH, panel_1, -500, SpringLayout.SOUTH, getContentPane());
								springLayout.putConstraint(SpringLayout.WEST, panel_1, -112, SpringLayout.EAST, getContentPane());
								springLayout.putConstraint(SpringLayout.SOUTH, panel_1, 0, SpringLayout.SOUTH, getContentPane());
								springLayout.putConstraint(SpringLayout.EAST, panel_1, 0, SpringLayout.EAST, getContentPane());
								panel_1.setBackground(Color.WHITE);
								getContentPane().add(panel_1);
								panel_1.setLayout(null);
								
								JButton btnLeave = new JButton("离职登记");
								btnLeave.setBounds(11, 422, 90, 30);
								panel_1.add(btnLeave);
								
								JButton btnSave = new JButton("保存编辑");
								btnSave.setBounds(11, 464, 90, 30);
								panel_1.add(btnSave);
								
								JButton btnQuickCheck = new JButton("快速考勤");
								btnQuickCheck.setBounds(11, 380, 90, 30);
								panel_1.add(btnQuickCheck);
								
								JButton btnSalaryDetails = new JButton("工资详情");
								btnSalaryDetails.setBounds(11, 338, 90, 30);
								panel_1.add(btnSalaryDetails);
								
								JPanel panel_2 = new JPanel();
								springLayout.putConstraint(SpringLayout.NORTH, panel_2, 300, SpringLayout.NORTH, getContentPane());
								TitledBorder tb= new TitledBorder(new LineBorder(new Color(0, 120, 215), 1, true), "出勤信息", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(59, 59, 59));
								tb.setTitleFont(new Font("微软雅黑", Font.PLAIN, 13));
								panel_2.setBorder(null);
								panel_2.setBackground(Color.WHITE);
								springLayout.putConstraint(SpringLayout.WEST, panel_2, 10, SpringLayout.WEST, getContentPane());
								springLayout.putConstraint(SpringLayout.SOUTH, panel_2, -10, SpringLayout.SOUTH, getContentPane());
								springLayout.putConstraint(SpringLayout.EAST, panel_2, 450, SpringLayout.WEST, getContentPane());
								getContentPane().add(panel_2);
								panel_2.setLayout(new BorderLayout(0, 0));
								
								JScrollPane scrollPane = new JScrollPane();
								scrollPane.setViewportBorder(null);
								panel_2.add(scrollPane);
								
								JList lstCheckIn = new JList();
								lstCheckIn.setFixedCellHeight(40);
								lstCheckIn.setForeground(Color.DARK_GRAY);
								lstCheckIn.setFont(new Font("微软雅黑", Font.PLAIN, 12));
								lstCheckIn.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
								lstCheckIn.setModel(new AbstractListModel() {
									String[] values = new String[] {"1月考勤：", "2月考勤：", "3月考勤：", "4月考勤：", "5月考勤：", "6月考勤：", "7月考勤：", "8月考勤：", "9月考勤：", "10月考勤：", "11月考勤：", "12月考勤："};
									public int getSize() {
										return values.length;
									}
									public Object getElementAt(int index) {
										return values[index];
									}
								});
								lstCheckIn.setBorder(null);
								scrollPane.setViewportView(lstCheckIn);
	}
	
	public void initializeEvent() {
		
	}

	/**
	 * 为窗口装载所有工人信息，加载成功返回true，失败返回false
	 * @param model 列表信息
	 * @param workSite 工地
	 * @return
	 */
	public boolean initializeData(AnInfoListDataModel model,String workSite){
		return initializeData(model.getInfo(),workSite);
	}

	/**
	 * 为窗口装载所有工人信息
	 * @param ID 身份ID
	 * @param site 工地
	 * @return 加载成功返回true，否则返回false
	 */
	@SuppressWarnings("unlikely-arg-type")
	public boolean initializeData(String ID,String site){
		for (AnBean anBean :DBManager.getManager().loadingWorkerList()){
			if(anBean.find(PropertyFactory.LABEL_ID_CARD).equals(ID)&&
					site.equals(anBean.find(PropertyFactory.LABEL_SITE))){
				worker= anBean;//找到了该工人，这么判断是因为身份相同的人在同一个工地只能出现一次

				labName.setText(worker.find(PropertyFactory.LABEL_NAME).getValueString());
				labIDCard.setText(worker.find(PropertyFactory.LABEL_ID_CARD).getValueString());
				labType.setText(worker.find(PropertyFactory.LABEL_WORKER_TYPE).getValueString());
				labPhone.setText(worker.find(PropertyFactory.LABEL_PHONE).getValueString());

				return true;
			}
		}
		return false;
	}



	/**
	 * 构造
	 */
    public WorkerWindow(){
    	
    	initializeComponent(null);
    	initializeEvent();
    }
}
