package application;

import component.*;
import dbManager.*;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

/**
 * 详细的工人数据，支持保存
 */
public class WorkerWindow extends Window {
    private AnImageLabel imageLabel;//头像
    private AnLabel labIDCard;//身份证
    private AnLabel labName;//名字
    private AnLabel labType;//工种
	private AnLabel labPhone;//电话号码
	private AnLabel labDealSalary;
	private AnLabel labSurplus;
	private AnLabel labPaidLivingCosts;
	private AnLabel labPaidWages;
	private AnLabel labelState;
	private JComboBox cobSite;
	private AnDataValuePanel checkInPanel;
	private AnDataValuePanel salaryPanel;

    private AnBean worker;//工人属性集合




    //数据
	private ChildrenManager checkInManager=null;
	private ChildrenManager salaryManager=null;
	private JLabel labBornDate;
	private JLabel lab;
	private JLabel labWorkDay;
	private JLabel labAge;
	private JButton btnInfo;





	
	
	public void initializeComponent(String title) {
		getContentPane().setBackground(Color.WHITE);
    	setIconImage(Toolkit.getDefaultToolkit().getImage(WorkerWindow.class.getResource("/resource/app_icon_60.png")));
    	setSize(1042, 771);
    	setMinimumSize(new Dimension(800,771));
    	setLocationRelativeTo(null);
    	setTitle(title);
    	setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		imageLabel=new AnImageLabel("app_icon.png");
		imageLabel.setBounds(10, 10, 191, 272);
		getContentPane().add(imageLabel);

		JPanel infoPanel = new JPanel();
		infoPanel.setBounds(336, 10, 169, 272);
		infoPanel.setBackground(Color.WHITE);
		getContentPane().add(infoPanel);
		infoPanel.setLayout(new GridLayout(8, 0, 0, 0));
		
				labName = new AnLabel("张三");
				labName.setText("");
				infoPanel.add(labName);
				labName.setForeground(SystemColor.textHighlight);
				labName.setHorizontalAlignment(SwingConstants.LEFT);
				labName.setFont(new Font("微软雅黑", Font.PLAIN, 20));
						
						labPhone = new AnLabel("联系方式：13123376032");
						labPhone.setText("");
						labPhone.setForeground(Color.GRAY);
						labPhone.setFont(new Font("微软雅黑", Font.PLAIN, 15));
						labPhone.setHorizontalAlignment(SwingConstants.LEFT);
						infoPanel.add(labPhone);
						
								labIDCard = new AnLabel("身份证：62334199712100072");
								labIDCard.setText("");
								infoPanel.add(labIDCard);
								labIDCard.setForeground(Color.GRAY);
								labIDCard.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								labIDCard.setHorizontalAlignment(SwingConstants.LEFT);
								
								labelState = new AnLabel("状态：在职");
								labelState.setText("");
								labelState.setForeground(Color.GRAY);
								labelState.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								labelState.setHorizontalAlignment(SwingConstants.LEFT);
								infoPanel.add(labelState);
								
								labType = new AnLabel("工种：点工");
								labType.setText("");
								labType.setForeground(Color.GRAY);
								labType.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								infoPanel.add(labType);
								
								labBornDate = new JLabel("");
								labBornDate.setForeground(Color.GRAY);
								labBornDate.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								infoPanel.add(labBornDate);
								
								labAge = new JLabel("");
								labAge.setForeground(Color.GRAY);
								labAge.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								infoPanel.add(labAge);
								
								JPanel panel_3 = new JPanel();
								panel_3.setBackground(Color.WHITE);
								infoPanel.add(panel_3);
								SpringLayout sl_panel_3 = new SpringLayout();
								panel_3.setLayout(sl_panel_3);
								
								cobSite = new JComboBox();
								cobSite.setFont(new Font("等线", Font.PLAIN, 15));
								sl_panel_3.putConstraint(SpringLayout.NORTH, cobSite, 0, SpringLayout.NORTH, panel_3);
								sl_panel_3.putConstraint(SpringLayout.WEST, cobSite, 0, SpringLayout.WEST, panel_3);
								sl_panel_3.putConstraint(SpringLayout.SOUTH, cobSite, 0, SpringLayout.SOUTH, panel_3);
								sl_panel_3.putConstraint(SpringLayout.EAST, cobSite, 0, SpringLayout.EAST, panel_3);
								panel_3.add(cobSite);
								
								JPanel panel = new JPanel();
								panel.setBounds(249, 10, 75, 272);
								panel.setBackground(Color.WHITE);
								getContentPane().add(panel);
								panel.setLayout(new GridLayout(8, 0, 0, 0));
								
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
								
								JLabel label_11 = new JLabel("出生日期：");
								label_11.setForeground(Color.GRAY);
								label_11.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								label_11.setHorizontalAlignment(SwingConstants.RIGHT);
								panel.add(label_11);
								
								JLabel label_14 = new JLabel("年龄：");
								label_14.setForeground(Color.GRAY);
								label_14.setHorizontalAlignment(SwingConstants.RIGHT);
								label_14.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								panel.add(label_14);
								
								JLabel label = new JLabel("所属工地：");
								label.setForeground(Color.GRAY);
								label.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								label.setHorizontalAlignment(SwingConstants.RIGHT);
								panel.add(label);
								
								JPanel panel_1 = new JPanel();
								panel_1.setBounds(893, 232, 137, 500);
								panel_1.setBackground(Color.WHITE);
								getContentPane().add(panel_1);
								panel_1.setLayout(null);
								
								JButton btnLeave = new JButton("离职登记");
								btnLeave.setBounds(11, 422, 120, 30);
								panel_1.add(btnLeave);
								
								JButton btnSave = new JButton("保存编辑");
								btnSave.setEnabled(false);
								btnSave.setBounds(11, 464, 120, 30);
								panel_1.add(btnSave);
								
								JButton btnCheck = new JButton("考勤当天");
								btnCheck.setBounds(11, 380, 120, 30);
								panel_1.add(btnCheck);
								
								JButton btnFile = new JButton("相关文件");
								btnFile.setBounds(11, 338, 120, 30);
								panel_1.add(btnFile);
								
								btnInfo = new JButton("详细信息");
								btnInfo.setBounds(11, 296, 120, 30);
								panel_1.add(btnInfo);
								
								JButton btnPrint = new JButton("打印信息");
								btnPrint.setBounds(11, 254, 120, 30);
								panel_1.add(btnPrint);
								
								JPanel panel_2 = new JPanel();
								panel_2.setBounds(10, 332, 420, 390);
								TitledBorder tb= new TitledBorder(new LineBorder(new Color(0, 120, 215), 1, true), "出勤信息", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(59, 59, 59));
								tb.setTitleFont(new Font("微软雅黑", Font.PLAIN, 13));
								panel_2.setBorder(null);
								panel_2.setBackground(Color.WHITE);
								getContentPane().add(panel_2);
								
								JLabel lblNewLabel = new JLabel("出勤信息");
								lblNewLabel.setBounds(10, 303, 80, 23);
								lblNewLabel.setForeground(SystemColor.textHighlight);
								lblNewLabel.setBackground(SystemColor.desktop);
								lblNewLabel.setFont(new Font("幼圆", Font.PLAIN, 20));
								panel_2.setLayout(new BorderLayout(0, 0));
								getContentPane().add(lblNewLabel);
								
								checkInPanel =new AnDataValuePanel();
								checkInPanel.setBorder(new LineBorder(new Color(143, 188, 143)));
								panel_2.add(checkInPanel, BorderLayout.CENTER);
								
								JLabel lblNewLabel_1 = new JLabel("工资信息表");
								lblNewLabel_1.setBounds(473, 303, 100, 23);
								lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
								lblNewLabel_1.setFont(new Font("幼圆", Font.PLAIN, 20));
								lblNewLabel_1.setForeground(SystemColor.textHighlight);
								getContentPane().add(lblNewLabel_1);

								salaryPanel = new AnDataValuePanel();
								salaryPanel.setBorder(new LineBorder(new Color(143, 188, 143)));
								salaryPanel.setBounds(473, 332, 420, 390);
								getContentPane().add(salaryPanel);

								JPanel panel_5 = new JPanel();
								panel_5.setBackground(Color.WHITE);
								panel_5.setBounds(550, 10, 121, 272);
								getContentPane().add(panel_5);
								panel_5.setLayout(new GridLayout(6, 0, 0, 0));

								JLabel label_6 = new JLabel("合计工日：");
								label_6.setHorizontalAlignment(SwingConstants.RIGHT);
								label_6.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								label_6.setForeground(Color.GRAY);
								panel_5.add(label_6);

								JLabel label_12 = new JLabel("");
								panel_5.add(label_12);

								JLabel label_7 = new JLabel("约定月工资：");
								label_7.setHorizontalAlignment(SwingConstants.RIGHT);
								label_7.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								label_7.setForeground(Color.GRAY);
								panel_5.add(label_7);

								JLabel label_8 = new JLabel("结余工资：");
								label_8.setHorizontalAlignment(SwingConstants.RIGHT);
								label_8.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								label_8.setForeground(Color.GRAY);
								panel_5.add(label_8);

								JLabel label_9 = new JLabel("已领取生活费：");
								label_9.setHorizontalAlignment(SwingConstants.RIGHT);
								label_9.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								label_9.setForeground(Color.GRAY);
								panel_5.add(label_9);

								JLabel label_10 = new JLabel("已领取的工资：");
								label_10.setHorizontalAlignment(SwingConstants.RIGHT);
								label_10.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								label_10.setForeground(Color.GRAY);
								panel_5.add(label_10);

								JPanel panel_6 = new JPanel();
								panel_6.setBackground(Color.WHITE);
								panel_6.setBounds(683, 10, 175, 272);
								getContentPane().add(panel_6);
								panel_6.setLayout(new GridLayout(0, 2, 0, 0));

								labWorkDay = new JLabel("0");
								labWorkDay.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								labWorkDay.setForeground(SystemColor.textHighlight);
								labWorkDay.setHorizontalAlignment(SwingConstants.RIGHT);
								panel_6.add(labWorkDay);

								JLabel label_16 = new JLabel(" 天");
								label_16.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								panel_6.add(label_16);

								JLabel label_13 = new JLabel("");
								panel_6.add(label_13);

								lab = new JLabel("");
								panel_6.add(lab);

								labDealSalary = new AnLabel("0");
								labDealSalary.setHorizontalAlignment(SwingConstants.RIGHT);
								labDealSalary.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								labDealSalary.setForeground(SystemColor.textHighlight);
								panel_6.add(labDealSalary);

								JLabel label_17 = new JLabel(" 元");
								label_17.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								panel_6.add(label_17);
								//结余工资
								labSurplus = new AnLabel("0");
								labSurplus.setHorizontalAlignment(SwingConstants.RIGHT);
								labSurplus.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								labSurplus.setForeground(SystemColor.textHighlight);
								panel_6.add(labSurplus);

								JLabel label_18 = new JLabel(" 元");
								label_18.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								panel_6.add(label_18);

								labPaidLivingCosts = new AnLabel("0");
								labPaidLivingCosts.setHorizontalAlignment(SwingConstants.RIGHT);
								labPaidLivingCosts.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								labPaidLivingCosts.setForeground(SystemColor.textHighlight);
								panel_6.add(labPaidLivingCosts);

								JLabel label_19 = new JLabel(" 元");
								label_19.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								panel_6.add(label_19);

								labPaidWages = new AnLabel("0");
								labPaidWages.setHorizontalAlignment(SwingConstants.RIGHT);
								labPaidWages.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								labPaidWages.setForeground(SystemColor.textHighlight);
								panel_6.add(labPaidWages);

								JLabel label_20 = new JLabel(" 元");
								label_20.setFont(new Font("微软雅黑", Font.PLAIN, 15));
								panel_6.add(label_20);
								
								JButton button = new JButton("修改");
								button.setBounds(940, 10, 90, 30);
								getContentPane().add(button);

	}

	public void initializeEvent() {
		//点击之后就保存数据
		//因为数据保存是在数据修改之后，事件监听器之间有先后顺序
		checkInPanel.setActionListener((e)->{
			if (e.getAction()==AnActionEvent.CILCKED){
				if (cobSite.getSelectedItem()==null)
					return;
				checkInManager.setWorkerDateValueList(labIDCard.getText(),cobSite.getSelectedItem().toString(), checkInPanel.getSource());
				showCheckInData(checkInPanel.getSource());
			}
		});
		//这里设置每次点击的值，第一次设置为全勤，第二次让用户自己选择
		checkInPanel.setValueCallback((obj)->{
			if (obj==null) {
				obj = 1d;
				AnPopDialog.show(this,"已设置该天为全勤，如果需要自定义出勤比例，请再次点击色块。",AnPopDialog.LONG_TIME);
				return obj;
			}
			String value=JOptionPane.showInputDialog(this,"请输入要修改的出勤值。","请修改",JOptionPane.INFORMATION_MESSAGE);
			double d;
			try{
				d=Double.valueOf(value);
				if (d>1d)
					d=1d;
			}catch (Exception ex){
				Application.debug(this,ex);
				return obj;
			}
			return d;
		});

		//这里的事件是设置值的颜色，出勤一天为1，所以颜色设置为10，我们扩大10倍因为传回去的值只能是整数
		checkInPanel.setParam((e)->{
			try{
				double f= (double) e;
				return (int) (f*10);
			}catch (Exception ex){
				return -1;
			}

		});

		salaryPanel.setActionListener((e)->{
			if(e.getAction()==AnActionEvent.CILCKED){
				if (cobSite.getSelectedItem()==null)
					return;
				salaryManager.setWorkerDateValueList(labIDCard.getText(),cobSite.getSelectedItem().toString(),salaryPanel.getSource());
				showSalaryData(salaryPanel.getSource());
			}
		});

		salaryPanel.setValueCallback((value -> {
			try{
				double d=Double.valueOf(JOptionPane.showInputDialog(this,"请输入该天领取的生活费。","请输入",JOptionPane.INFORMATION_MESSAGE));
				return d;
			}catch (Exception e){ }
			return 0d;
		}));

		salaryPanel.setParam(value -> {
			double d= (double) value;
			return (int) d;
		});


		cobSite.addActionListener((e)-> initializeData());

		btnInfo.addActionListener((e)->{
			//创建InfoWindow
			String siteName;
			if (cobSite.getSelectedItem()==null)
				siteName=null;
			else siteName=cobSite.getSelectedItem().toString();

			WindowBuilder.showInfoWindow(labIDCard.getText(),siteName,(values)->{
				String id= (String) values[0];
				String site= (String) values[1];
				initializeData(id, site);
				return true;
			});
		});
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

	private boolean initializeData(){
		if (cobSite.getSelectedItem()==null)
			return initializeData(labIDCard.getText(),null);
		return initializeData(labIDCard.getText(),cobSite.getSelectedItem().toString());
	}

	/**
	 * 为窗口装载所有工人信息
	 * @param ID 身份ID
	 * @param site 工地
	 * @return 加载成功返回true，否则返回false
	 */
	@SuppressWarnings("unlikely-arg-type")
	public boolean initializeData(String ID,String site){
		//获取用户
		worker=DBManager.getManager().getWorker(ID);
		if (worker==null)
			return false;

		//填补工人的属性信息：与工人相关的信息
		SimpleDateFormat sm=new SimpleDateFormat("yyy年MM月dd日");
		labName.setText(worker.find(PropertyFactory.LABEL_NAME).getValueString());
		labIDCard.setText(worker.find(PropertyFactory.LABEL_ID_CARD).getValueString());
		labPhone.setText(worker.find(PropertyFactory.LABEL_PHONE).getValueString());
		labBornDate.setText(sm.format(AnUtils.convertBornDate(labIDCard.getText())));
		labAge.setText(String.valueOf(AnUtils.convertAge(labIDCard.getText()))+"岁");


		/*
		**获取工地列表**  填充工人所在工地的信息
		 */
		try{
			showBuildingSiteInfo(ID,site);
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 计算合计工日，并保存到工人属性中去
	 * @param list
	 */
	private void showCheckInData(ArrayList<IDateValueItem> list){
		double days=0f;

		for (IDateValueItem item:list){
			try{
				double d= (double) item.getValue();
				days+=d;
			}catch (Exception ex){ }
		}
		labWorkDay.setText(String.valueOf(days));
	}

	/**
	 * 计算工人领取的生活费，并保存到工人属性中
	 * @param list 时间和数据的包装表
	 */
	private void showSalaryData(ArrayList<IDateValueItem> list){
		double livingCosts=0d;
		for (IDateValueItem item:list){
			try{
				double d=(double) item.getValue();
				livingCosts+=d;
			}catch (Exception ex){ }
		}
		labPaidLivingCosts.setText(String.valueOf(livingCosts));
	}


	/**
	 * 展示此工地上工人的信息
	 * @param id
	 * @param siteName
	 */
	public void showBuildingSiteInfo(String id,String siteName){

		DBManager manager=DBManager.getManager();//获取管理器
		//加载工人的工地是全部的时候，需要判断工人有没有加入工地
		if (siteName.equals("全部")){
			//检查工人是不是没有加入工地
			if (manager.getWorkerAt(id).size()==0){
				cobSite.setVisible(false);//关闭工地选择的显示
				checkInPanel.setEnabled(false);//关闭日期容器
				salaryPanel.setEnabled(false);//同上
				return;
			}
			//工人有工地，更改siteName为第一个找到的工地
			siteName=manager.getWorkerAt(id).get(0);
		}

		AnDataTable site=DBManager.getManager().getBuildingSite(siteName);
		if (site==null)
			return;
		//获取工人全部的工地列表，如果工地不存在，退出
		if (!manager.getWorkerAt(id).contains(siteName))//判断有没有在该工地上班
			return;
		//以下都是在此工地找到工人

		site.selectRow(PropertyFactory.LABEL_ID_CARD,id);//选择该工人的信息
		//显示
		cobSite.setModel(new DefaultComboBoxModel(AnUtils.toArray(manager.getWorkerAt(id))));
		cobSite.setSelectedItem(siteName);
		labDealSalary.setText(String.valueOf(site.getSelectedRowAt(PropertyFactory.LABEL_DEAL_SALARY)));
		labType.setText((String) site.getSelectedRowAt(PropertyFactory.LABEL_WORKER_TYPE));

		//对于工地显示的两个子管理器
		checkInPanel.setMaxValue(10);//设置出勤容器的颜色标准数值
		Double dv=(Double) site.getSelectedRowAt(PropertyFactory.LABEL_DEAL_SALARY);
		salaryPanel.setMaxValue(dv.intValue());//设置工资容器颜色标准数值为约定的工资

		//填充容器
		checkInManager=manager.getCheckInManager();
		salaryManager=manager.getSalaryManager();

		checkInPanel.setSourceDates(checkInManager.getWorkerDateValueList(id,siteName));
		salaryPanel.setSourceDates(salaryManager.getWorkerDateValueList(id,siteName));

		showSalaryData(salaryPanel.getSource());
		showCheckInData(checkInPanel.getSource());



	}


	/**
	 * 构造
	 */
    public WorkerWindow(){
		setResizable(false);
    	
    	initializeComponent(null);
    	initializeEvent();
    }

    public void update(){
		initializeData();
	}
}
