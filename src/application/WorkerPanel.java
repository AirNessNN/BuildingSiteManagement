package application;

import com.mysql.fabric.xmlrpc.base.Data;
import component.*;
import dbManager.*;
import resource.Resource;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;


public class WorkerPanel extends JPanel implements Loadable{

    private boolean isPropertyRunning =false;//属性正在初始化
    private ArrayList<AnListRenderModel> searchTmpList=null;

    private AnTextField searchBox;
    private AnList list=null;
    private DefaultListModel<AnListRenderModel> listModel=null;
    private AnButton btnEntry;
    private AnButton btnEntryFromExcel;
    private AnButton btnPrint;
    private AnButton btnRefresh;
    private AnButton btnPropertyAlert;

    private JComboBox<String> cobSite;//所属工地筛选器
    private AnLabel labWorkingWorker;
    private AnLabel labGotLivingCostCount;
    private AnLabel labGotLivingCostToday;
    private AnLabel labSumLeave;
    private AnLabel labLeaveToday;
    private AnLabel labFullCheckToday;
    private AnLabel labBirthdayToday;
    private JCheckBox cbShowLeave;
    private AnLabel labCheckToday;
    private AnLabel imgBirthday;
    private AnLabel labAge;
    private AnLabel labBorn;
    private AnLabel labAddress;
    private AnLabel labPhone;
    private AnLabel labBankID;
    private AnLabel labBankAddress;
    private AnLabel labNation;
    private AnLabel labWorkingSiteCount;

    private int birthdayCount=0;//生日人数

    private Color pressColor=new Color(40, 160, 79);
    private Color enterColor=new Color(24, 96, 48);
    private Color normalColor=new Color(114, 114, 114);







    /**
     * 载入控件布局和事件
     */
    private void initView(){
        this.setSize(934,671);
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        JScrollPane scrollPane = new JScrollPane();
        springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 147, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -10, SpringLayout.SOUTH, this);
        scrollPane.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
        add(scrollPane);
        
        list=new AnList<>(new AnInfoCellRenderer(),355,60);
        scrollPane.setViewportView(list);
        listModel=new DefaultListModel<>();
        list.setModel(listModel);

        AnLabel lblTitle = new AnLabel("工人管理");
        springLayout.putConstraint(SpringLayout.NORTH, lblTitle, 10, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, lblTitle, 10, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, lblTitle, 62, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, lblTitle, 185, SpringLayout.WEST, this);
        lblTitle.setFont(new Font("方正兰亭超细黑简体", Font.PLAIN, 40));
        lblTitle.setForeground(Color.WHITE);
        add(lblTitle);
        
        searchBox = new AnTextField();
        searchBox.setForeground(SystemColor.textInactiveText);
        searchBox.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        springLayout.putConstraint(SpringLayout.NORTH, searchBox, 83, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, searchBox, 10, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, searchBox, 106, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, searchBox, 275, SpringLayout.WEST, this);
        searchBox.setText("输入名字或身份证信息查找");
        add(searchBox);

        //身份证的字符限制
        searchBox.setColumns(18);

        btnPrint = new AnButton("打印信息");
        springLayout.putConstraint(SpringLayout.NORTH, btnPrint, 1, SpringLayout.NORTH, searchBox);
        springLayout.putConstraint(SpringLayout.SOUTH, btnPrint, 107, SpringLayout.NORTH, this);
        btnPrint.setToolTipText("将列表中的所有工人的个人信息，打印成列表");
        add(btnPrint);
        btnPrint.setBorderColor(normalColor);
        btnPrint.setBorderEnterColor(enterColor);
        btnPrint.setBorderPressColor(pressColor);

        btnEntry = new AnButton("添加工人");
        springLayout.putConstraint(SpringLayout.NORTH, btnEntry, 1, SpringLayout.NORTH, searchBox);
        springLayout.putConstraint(SpringLayout.SOUTH, btnEntry, 107, SpringLayout.NORTH, this);
        btnEntry.setToolTipText("创建一个新的工人，并且可以快捷设置他的工地");
        add(btnEntry);
        btnEntry.setBorderColor(normalColor);
        btnEntry.setBorderEnterColor(enterColor);
        btnEntry.setBorderPressColor(pressColor);

        btnEntryFromExcel = new AnButton("导入工人");
        springLayout.putConstraint(SpringLayout.NORTH, btnEntryFromExcel, 1, SpringLayout.NORTH, searchBox);
        springLayout.putConstraint(SpringLayout.SOUTH, btnEntryFromExcel, 107, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, btnEntryFromExcel, -95, SpringLayout.WEST, btnEntry);
        springLayout.putConstraint(SpringLayout.EAST, btnEntryFromExcel, -10, SpringLayout.WEST, btnEntry);
        btnEntryFromExcel.setToolTipText("从Excel模板中导入工人");
        add(btnEntryFromExcel);
        btnEntryFromExcel.setBorderColor(normalColor);
        btnEntryFromExcel.setBorderEnterColor(enterColor);
        btnEntryFromExcel.setBorderPressColor(pressColor);
        
        btnRefresh = new AnButton("刷新");
        springLayout.putConstraint(SpringLayout.WEST, btnPrint, -90, SpringLayout.WEST, btnRefresh);
        springLayout.putConstraint(SpringLayout.WEST, btnRefresh, -70, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.EAST, btnPrint, -6, SpringLayout.WEST, btnRefresh);
        springLayout.putConstraint(SpringLayout.NORTH, btnRefresh, 1, SpringLayout.NORTH, searchBox);
        springLayout.putConstraint(SpringLayout.SOUTH, btnRefresh, 107, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, btnRefresh, -10, SpringLayout.EAST, this);
        add(btnRefresh);
        btnRefresh.setBorderColor(normalColor);
        btnRefresh.setBorderEnterColor(enterColor);
        btnRefresh.setBorderPressColor(pressColor);

        btnPropertyAlert = new AnButton("属性修改");
        springLayout.putConstraint(SpringLayout.WEST, btnEntry, -90, SpringLayout.WEST, btnPropertyAlert);
        springLayout.putConstraint(SpringLayout.EAST, btnEntry, -6, SpringLayout.WEST, btnPropertyAlert);
        springLayout.putConstraint(SpringLayout.WEST, btnPropertyAlert, -90, SpringLayout.WEST, btnPrint);
        springLayout.putConstraint(SpringLayout.NORTH, btnPropertyAlert, 1, SpringLayout.NORTH, searchBox);
        springLayout.putConstraint(SpringLayout.SOUTH, btnPropertyAlert, 107, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, btnPropertyAlert, -6, SpringLayout.WEST, btnPrint);
        btnPropertyAlert.setToolTipText("增删改属性");
        add(btnPropertyAlert);
        btnPropertyAlert.setBorderColor(normalColor);
        btnPropertyAlert.setBorderEnterColor(enterColor);
        btnPropertyAlert.setBorderPressColor(pressColor);
        
        cobSite = new JComboBox<>();
        cobSite.setFont(new Font("等线", Font.PLAIN, 15));
        springLayout.putConstraint(SpringLayout.NORTH, cobSite, 116, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, cobSite, 83, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.EAST, cobSite, 367, SpringLayout.WEST, this);
        add(cobSite);
        cobSite.addItem("全部");
        //cobSite.addItem("所有");//Debug
        
        JLabel label = new JLabel("\u6240\u5C5E\u5DE5\u5730");
        label.setForeground(SystemColor.textInactiveText);
        springLayout.putConstraint(SpringLayout.NORTH, label, 120, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, label, 135, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, label, 73, SpringLayout.WEST, this);
        label.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        add(label);
        
        AnImageLabel lblNewLabel = new AnImageLabel(Resource.getResource("workpanel_banner.png"));
        springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, 0, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, lblNewLabel, -934, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, lblNewLabel, 76, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, lblNewLabel, 0, SpringLayout.EAST, this);
        add(lblNewLabel);

        AnImageLabel anImageLabel = new AnImageLabel(new Color(24, 96, 48));
        springLayout.putConstraint(SpringLayout.NORTH, anImageLabel, 0, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, anImageLabel, 0, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, anImageLabel, 76, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, anImageLabel, 0, SpringLayout.EAST, this);
        add(anImageLabel);
        
        cbShowLeave = new JCheckBox("显示离职");
        cbShowLeave.setForeground(SystemColor.textInactiveText);
        cbShowLeave.setBackground(Color.WHITE);
        cbShowLeave.setFont(new Font("等线", Font.PLAIN, 15));
        springLayout.putConstraint(SpringLayout.WEST, cbShowLeave, 6, SpringLayout.EAST, searchBox);
        springLayout.putConstraint(SpringLayout.SOUTH, cbShowLeave, 0, SpringLayout.SOUTH, searchBox);
        springLayout.putConstraint(SpringLayout.EAST, cbShowLeave, 0, SpringLayout.EAST, cobSite);
        add(cbShowLeave);
        
        JPanel panel = new JPanel();
        springLayout.putConstraint(SpringLayout.NORTH, panel, 0, SpringLayout.NORTH, cobSite);
        springLayout.putConstraint(SpringLayout.WEST, panel, 10, SpringLayout.EAST, cobSite);
        springLayout.putConstraint(SpringLayout.SOUTH, panel, 200, SpringLayout.NORTH, cobSite);
        springLayout.putConstraint(SpringLayout.EAST, panel, -10, SpringLayout.EAST, this);
        panel.setBorder(
                new TitledBorder(
                        new LineBorder(
                                new Color(192, 192, 192), 1, true),
                        "HUB信息中心",
                        TitledBorder.LEADING, TitledBorder.TOP, 
                        new Font(Resource.FONT_WEI_RUAN_YA_HEI,Font.PLAIN,15),
                        new Color(114, 114, 114)
                )
        );
        panel.setBackground(Color.WHITE);
        add(panel);
        panel.setLayout(null);
        
        JLabel label_1 = new JLabel("在职工人数量：");
        label_1.setForeground(SystemColor.textInactiveText);
        label_1.setFont(new Font("等线", Font.PLAIN, 16));
        label_1.setBounds(10, 27, 117, 15);
        panel.add(label_1);
        
        labWorkingWorker = new AnLabel("未知");
        labWorkingWorker.setForeground(SystemColor.textHighlight);
        labWorkingWorker.setFont(new Font("等线", Font.PLAIN, 16));
        labWorkingWorker.setBounds(137, 27, 90, 15);
        panel.add(labWorkingWorker);
        
        JLabel label_3 = new AnLabel("今日出勤人数：");
        label_3.setForeground(SystemColor.textInactiveText);
        label_3.setFont(new Font("等线", Font.PLAIN, 16));
        label_3.setBounds(10, 76, 117, 15);
        panel.add(label_3);
        
        labCheckToday = new AnLabel("未知");
        labCheckToday.setForeground(SystemColor.textHighlight);
        labCheckToday.setFont(new Font("等线", Font.PLAIN, 16));
        labCheckToday.setBounds(137, 76, 90, 15);
        panel.add(labCheckToday);
        
        JLabel label_5 = new JLabel("今日领取生活费人数：");
        label_5.setForeground(SystemColor.textInactiveText);
        label_5.setFont(new Font("等线", Font.PLAIN, 16));
        label_5.setBounds(253, 27, 165, 15);
        panel.add(label_5);
        
        labGotLivingCostCount = new AnLabel("未知");
        labGotLivingCostCount.setForeground(SystemColor.textHighlight);
        labGotLivingCostCount.setFont(new Font("等线", Font.PLAIN, 16));
        labGotLivingCostCount.setBounds(428, 27, 109, 15);
        panel.add(labGotLivingCostCount);
        
        JLabel label_7 = new JLabel("今日领取生活费总额：");
        label_7.setForeground(SystemColor.textInactiveText);
        label_7.setFont(new Font("等线", Font.PLAIN, 16));
        label_7.setBounds(253, 52, 165, 15);
        panel.add(label_7);
        
        labGotLivingCostToday = new AnLabel("未知");
        labGotLivingCostToday.setForeground(SystemColor.textHighlight);
        labGotLivingCostToday.setFont(new Font("等线", Font.PLAIN, 16));
        labGotLivingCostToday.setBounds(428, 52, 109, 15);
        panel.add(labGotLivingCostToday);
        
        JLabel label_9 = new JLabel("离职工人数量：");
        label_9.setForeground(SystemColor.textInactiveText);
        label_9.setFont(new Font("等线", Font.PLAIN, 16));
        label_9.setBounds(10, 52, 117, 15);
        panel.add(label_9);
        
        labSumLeave = new AnLabel("未知");
        labSumLeave.setForeground(SystemColor.textHighlight);
        labSumLeave.setFont(new Font("等线", Font.PLAIN, 16));
        labSumLeave.setBounds(137, 52, 90, 15);
        panel.add(labSumLeave);
        
        JLabel label_11 = new JLabel("今日离职数量：");
        label_11.setForeground(SystemColor.textInactiveText);
        label_11.setFont(new Font("等线", Font.PLAIN, 16));
        label_11.setBounds(10, 125, 117, 15);
        panel.add(label_11);
        
        labLeaveToday = new AnLabel("未知");
        labLeaveToday.setForeground(SystemColor.textHighlight);
        labLeaveToday.setFont(new Font("等线", Font.PLAIN, 16));
        labLeaveToday.setBounds(137, 125, 90, 15);
        panel.add(labLeaveToday);
        
        JLabel label_13 = new JLabel("今日全勤人数：");
        label_13.setForeground(SystemColor.textInactiveText);
        label_13.setFont(new Font("等线", Font.PLAIN, 16));
        label_13.setBounds(10, 100, 117, 15);
        panel.add(label_13);
        
        labFullCheckToday = new AnLabel("未知");
        labFullCheckToday.setForeground(SystemColor.textHighlight);
        labFullCheckToday.setFont(new Font("等线", Font.PLAIN, 16));
        labFullCheckToday.setBounds(137, 101, 90, 15);
        panel.add(labFullCheckToday);
        
        JLabel label_2 = new JLabel("今日生日人数：");
        label_2.setForeground(SystemColor.textInactiveText);
        label_2.setFont(new Font("等线", Font.PLAIN, 16));
        label_2.setBounds(10, 151, 117, 15);
        panel.add(label_2);
        
        labBirthdayToday = new AnLabel("未知");
        labBirthdayToday.setForeground(SystemColor.textHighlight);
        labBirthdayToday.setFont(new Font("等线", Font.PLAIN, 16));
        labBirthdayToday.setBounds(137, 151, 90, 15);
        panel.add(labBirthdayToday);
        
        imgBirthday = new AnLabel("生日提醒");
        imgBirthday.setFont(new Font("等线", Font.PLAIN, 15));
        springLayout.putConstraint(SpringLayout.NORTH, imgBirthday, -60, SpringLayout.SOUTH, this);
        springLayout.putConstraint(SpringLayout.WEST, imgBirthday, -70, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, imgBirthday, -10, SpringLayout.SOUTH, this);
        springLayout.putConstraint(SpringLayout.EAST, imgBirthday, -10, SpringLayout.EAST, btnRefresh);
        add(imgBirthday);
        
        JPanel panel_1 = new JPanel();
        springLayout.putConstraint(SpringLayout.NORTH, panel_1, 10, SpringLayout.SOUTH, panel);
        springLayout.putConstraint(SpringLayout.WEST, panel_1, 0, SpringLayout.WEST, panel);
        springLayout.putConstraint(SpringLayout.SOUTH, panel_1, -10, SpringLayout.NORTH, imgBirthday);
        springLayout.putConstraint(SpringLayout.EAST, panel_1, 0, SpringLayout.EAST, panel);
        panel_1.setBorder(
                new TitledBorder(
                        new LineBorder(
                                new Color(192, 192, 192), 1, true),
                        "工人简略信息",
                        TitledBorder.LEADING,
                        TitledBorder.TOP,
                        new Font(Resource.FONT_WEI_RUAN_YA_HEI,Font.PLAIN,15),
                        new Color(114, 114, 114)
                )
        );
        panel_1.setBackground(Color.WHITE);
        add(panel_1);
        SpringLayout sl_panel_1 = new SpringLayout();
        panel_1.setLayout(sl_panel_1);
        
        JLabel label_4 = new JLabel("年龄：");
        sl_panel_1.putConstraint(SpringLayout.NORTH, label_4, 5, SpringLayout.NORTH, panel_1);
        sl_panel_1.putConstraint(SpringLayout.WEST, label_4, 10, SpringLayout.WEST, panel_1);
        sl_panel_1.putConstraint(SpringLayout.SOUTH, label_4, 20, SpringLayout.NORTH, panel_1);
        sl_panel_1.putConstraint(SpringLayout.EAST, label_4, 127, SpringLayout.WEST, panel_1);
        label_4.setForeground(SystemColor.textInactiveText);
        label_4.setHorizontalAlignment(SwingConstants.RIGHT);
        label_4.setFont(new Font("等线", Font.PLAIN, 16));
        panel_1.add(label_4);
        
        JLabel label_6 = new JLabel("出生日期：");
        sl_panel_1.putConstraint(SpringLayout.NORTH, label_6, 10, SpringLayout.SOUTH, label_4);
        sl_panel_1.putConstraint(SpringLayout.WEST, label_6, 10, SpringLayout.WEST, panel_1);
        sl_panel_1.putConstraint(SpringLayout.SOUTH, label_6, 25, SpringLayout.SOUTH, label_4);
        sl_panel_1.putConstraint(SpringLayout.EAST, label_6, 127, SpringLayout.WEST, panel_1);
        label_6.setForeground(SystemColor.textInactiveText);
        label_6.setHorizontalAlignment(SwingConstants.RIGHT);
        label_6.setFont(new Font("等线", Font.PLAIN, 16));
        panel_1.add(label_6);
        
        JLabel label_8 = new JLabel("家庭住址：");
        sl_panel_1.putConstraint(SpringLayout.NORTH, label_8, 10, SpringLayout.SOUTH, label_6);
        sl_panel_1.putConstraint(SpringLayout.WEST, label_8, 10, SpringLayout.WEST, panel_1);
        sl_panel_1.putConstraint(SpringLayout.SOUTH, label_8, 25, SpringLayout.SOUTH, label_6);
        sl_panel_1.putConstraint(SpringLayout.EAST, label_8, 124, SpringLayout.WEST, panel_1);
        label_8.setForeground(SystemColor.textInactiveText);
        label_8.setHorizontalAlignment(SwingConstants.RIGHT);
        label_8.setFont(new Font("等线", Font.PLAIN, 16));
        panel_1.add(label_8);
        
        JLabel label_10 = new JLabel("联系方式：");
        sl_panel_1.putConstraint(SpringLayout.NORTH, label_10, 10, SpringLayout.SOUTH, label_8);
        sl_panel_1.putConstraint(SpringLayout.WEST, label_10, 0, SpringLayout.WEST, label_4);
        sl_panel_1.putConstraint(SpringLayout.SOUTH, label_10, 25, SpringLayout.SOUTH, label_8);
        sl_panel_1.putConstraint(SpringLayout.EAST, label_10, 127, SpringLayout.WEST, panel_1);
        label_10.setForeground(SystemColor.textInactiveText);
        label_10.setHorizontalAlignment(SwingConstants.RIGHT);
        label_10.setFont(new Font("等线", Font.PLAIN, 16));
        panel_1.add(label_10);
        
        JLabel label_12 = new JLabel("银行账户：");
        sl_panel_1.putConstraint(SpringLayout.NORTH, label_12, 10, SpringLayout.SOUTH, label_10);
        sl_panel_1.putConstraint(SpringLayout.WEST, label_12, 0, SpringLayout.WEST, label_4);
        sl_panel_1.putConstraint(SpringLayout.SOUTH, label_12, 25, SpringLayout.SOUTH, label_10);
        sl_panel_1.putConstraint(SpringLayout.EAST, label_12, 127, SpringLayout.WEST, panel_1);
        label_12.setForeground(SystemColor.textInactiveText);
        label_12.setHorizontalAlignment(SwingConstants.RIGHT);
        label_12.setFont(new Font("等线", Font.PLAIN, 16));
        panel_1.add(label_12);
        
        labAge = new AnLabel("未知");
        sl_panel_1.putConstraint(SpringLayout.NORTH, labAge, 5, SpringLayout.NORTH, panel_1);
        sl_panel_1.putConstraint(SpringLayout.WEST, labAge, 137, SpringLayout.WEST, panel_1);
        sl_panel_1.putConstraint(SpringLayout.SOUTH, labAge, 20, SpringLayout.NORTH, panel_1);
        sl_panel_1.putConstraint(SpringLayout.EAST, labAge, -10, SpringLayout.EAST, panel_1);
        labAge.setForeground(SystemColor.textHighlight);
        labAge.setFont(new Font("等线", Font.PLAIN, 16));
        panel_1.add(labAge);
        
        labBorn = new AnLabel("未知");
        sl_panel_1.putConstraint(SpringLayout.NORTH, labBorn, 0, SpringLayout.NORTH, label_6);
        sl_panel_1.putConstraint(SpringLayout.WEST, labBorn, 137, SpringLayout.WEST, panel_1);
        sl_panel_1.putConstraint(SpringLayout.SOUTH, labBorn, 0, SpringLayout.SOUTH, label_6);
        sl_panel_1.putConstraint(SpringLayout.EAST, labBorn, -10, SpringLayout.EAST, panel_1);
        labBorn.setForeground(SystemColor.textHighlight);
        labBorn.setFont(new Font("等线", Font.PLAIN, 16));
        panel_1.add(labBorn);
        
        labAddress = new AnLabel("未知");
        sl_panel_1.putConstraint(SpringLayout.NORTH, labAddress, 0, SpringLayout.NORTH, label_8);
        sl_panel_1.putConstraint(SpringLayout.WEST, labAddress, 0, SpringLayout.WEST, labAge);
        sl_panel_1.putConstraint(SpringLayout.SOUTH, labAddress, 0, SpringLayout.SOUTH, label_8);
        sl_panel_1.putConstraint(SpringLayout.EAST, labAddress, -10, SpringLayout.EAST, panel_1);
        labAddress.setForeground(SystemColor.textHighlight);
        labAddress.setFont(new Font("等线", Font.PLAIN, 16));
        panel_1.add(labAddress);
        
        labPhone = new AnLabel("未知");
        sl_panel_1.putConstraint(SpringLayout.NORTH, labPhone, 0, SpringLayout.NORTH, label_10);
        sl_panel_1.putConstraint(SpringLayout.WEST, labPhone, 137, SpringLayout.WEST, panel_1);
        sl_panel_1.putConstraint(SpringLayout.SOUTH, labPhone, 0, SpringLayout.SOUTH, label_10);
        sl_panel_1.putConstraint(SpringLayout.EAST, labPhone, -10, SpringLayout.EAST, panel_1);
        labPhone.setForeground(SystemColor.textHighlight);
        labPhone.setFont(new Font("等线", Font.PLAIN, 16));
        panel_1.add(labPhone);
        
        labBankID = new AnLabel("未知");
        sl_panel_1.putConstraint(SpringLayout.NORTH, labBankID, 0, SpringLayout.NORTH, label_12);
        sl_panel_1.putConstraint(SpringLayout.WEST, labBankID, 137, SpringLayout.WEST, panel_1);
        sl_panel_1.putConstraint(SpringLayout.SOUTH, labBankID, 0, SpringLayout.SOUTH, label_12);
        sl_panel_1.putConstraint(SpringLayout.EAST, labBankID, -10, SpringLayout.EAST, panel_1);
        labBankID.setForeground(SystemColor.textHighlight);
        labBankID.setFont(new Font("等线", Font.PLAIN, 16));
        panel_1.add(labBankID);
        
        JLabel label_15 = new JLabel("开户地址：");
        sl_panel_1.putConstraint(SpringLayout.NORTH, label_15, 10, SpringLayout.SOUTH, label_12);
        sl_panel_1.putConstraint(SpringLayout.WEST, label_15, 0, SpringLayout.WEST, label_4);
        sl_panel_1.putConstraint(SpringLayout.SOUTH, label_15, 25, SpringLayout.SOUTH, label_12);
        sl_panel_1.putConstraint(SpringLayout.EAST, label_15, 124, SpringLayout.WEST, panel_1);
        label_15.setHorizontalAlignment(SwingConstants.RIGHT);
        label_15.setForeground(SystemColor.textInactiveText);
        label_15.setFont(new Font("等线", Font.PLAIN, 16));
        panel_1.add(label_15);
        
        labBankAddress = new AnLabel("未知");
        sl_panel_1.putConstraint(SpringLayout.NORTH, labBankAddress, 0, SpringLayout.NORTH, label_15);
        sl_panel_1.putConstraint(SpringLayout.WEST, labBankAddress, 137, SpringLayout.WEST, panel_1);
        sl_panel_1.putConstraint(SpringLayout.SOUTH, labBankAddress, 0, SpringLayout.SOUTH, label_15);
        sl_panel_1.putConstraint(SpringLayout.EAST, labBankAddress, -10, SpringLayout.EAST, panel_1);
        labBankAddress.setForeground(SystemColor.textHighlight);
        labBankAddress.setFont(new Font("等线", Font.PLAIN, 16));
        panel_1.add(labBankAddress);
        
        JLabel label_16 = new JLabel("民族：");
        sl_panel_1.putConstraint(SpringLayout.NORTH, label_16, 10, SpringLayout.SOUTH, label_15);
        sl_panel_1.putConstraint(SpringLayout.WEST, label_16, 0, SpringLayout.WEST, label_4);
        sl_panel_1.putConstraint(SpringLayout.SOUTH, label_16, 25, SpringLayout.SOUTH, label_15);
        sl_panel_1.putConstraint(SpringLayout.EAST, label_16, 127, SpringLayout.WEST, panel_1);
        label_16.setHorizontalAlignment(SwingConstants.RIGHT);
        label_16.setForeground(SystemColor.textInactiveText);
        label_16.setFont(new Font("等线", Font.PLAIN, 16));
        panel_1.add(label_16);
        
        labNation = new AnLabel("未知");
        sl_panel_1.putConstraint(SpringLayout.NORTH, labNation, 0, SpringLayout.NORTH, label_16);
        sl_panel_1.putConstraint(SpringLayout.WEST, labNation, 137, SpringLayout.WEST, panel_1);
        sl_panel_1.putConstraint(SpringLayout.SOUTH, labNation, 0, SpringLayout.SOUTH, label_16);
        sl_panel_1.putConstraint(SpringLayout.EAST, labNation, -10, SpringLayout.EAST, panel_1);
        labNation.setForeground(SystemColor.textHighlight);
        labNation.setFont(new Font("等线", Font.PLAIN, 16));
        panel_1.add(labNation);
        
        JLabel label_14 = new JLabel("工作工地数量：");
        sl_panel_1.putConstraint(SpringLayout.NORTH, label_14, -30, SpringLayout.SOUTH, panel_1);
        sl_panel_1.putConstraint(SpringLayout.WEST, label_14, 10, SpringLayout.WEST, panel_1);
        sl_panel_1.putConstraint(SpringLayout.SOUTH, label_14, -10, SpringLayout.SOUTH, panel_1);
        sl_panel_1.putConstraint(SpringLayout.EAST, label_14, 127, SpringLayout.WEST, panel_1);
        label_14.setHorizontalAlignment(SwingConstants.RIGHT);
        label_14.setForeground(SystemColor.textInactiveText);
        label_14.setFont(new Font("等线", Font.PLAIN, 16));
        panel_1.add(label_14);
        
        labWorkingSiteCount = new AnLabel("未知");
        sl_panel_1.putConstraint(SpringLayout.NORTH, labWorkingSiteCount, 0, SpringLayout.NORTH, label_14);
        sl_panel_1.putConstraint(SpringLayout.WEST, labWorkingSiteCount, 137, SpringLayout.WEST, panel_1);
        sl_panel_1.putConstraint(SpringLayout.SOUTH, labWorkingSiteCount, 0, SpringLayout.SOUTH, label_14);
        sl_panel_1.putConstraint(SpringLayout.EAST, labWorkingSiteCount, 227, SpringLayout.WEST, panel_1);
        labWorkingSiteCount.setForeground(SystemColor.textHighlight);
        labWorkingSiteCount.setFont(new Font("等线", Font.PLAIN, 16));
        panel_1.add(labWorkingSiteCount);
    }

    private void initEvent(){
        //搜索框焦点逻辑
        searchBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                searchBox.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(searchBox.getText().equals("")){
                    searchBox.setText("输入名字或身份证信息查找");
                }
            }
        });

        btnEntryFromExcel.addActionListener(e -> {
            ExcelTemplate excelTemplate=new ExcelTemplate();
            excelTemplate.createTemplate();
            AnUtils.open(ExcelTemplate.filePath);
            AnPopDialog.show(this,"请在模板中编辑，编辑完成后退出Excel，点击确认。",4000);
            int r=JOptionPane.showConfirmDialog(this,"是否已经完成编辑？","编辑提示",JOptionPane.OK_CANCEL_OPTION);
            if (r==JOptionPane.OK_OPTION){
                Application.startService(()->{
                    //获取到Excel中的数据
                    DataTable dataTable=excelTemplate.getTemplateData();
                    int size=dataTable.getMaxRowCount();

                    int succeedCount=0;

                    for (int i=0;i<size;i++){
                        Bean worker=PropertyFactory.createWorker();//创建工人
                        dataTable.selectRow(i);//选中行
                        for (int j=0;j<dataTable.getSize();j++){
                            Info info=worker.find(dataTable.getColumn(j).getName());
                            if (info==null)continue;
                            info.setValue(dataTable.getSelectedRowAt(j));
                        }
                        boolean b=DBManager.getManager().addWorker(worker);
                       if (b)succeedCount++;
                    }
                    AnPopDialog.show(this,"添加完成，成功"+succeedCount+"个，共"+size+"个",3000);
                    ProgressbarDialog.CloseDialog();
                });
               ProgressbarDialog.showDialog("正在添加工人到工人列表中");
               refresh();
            }
        });

        //入职登记
        btnEntry.addActionListener((e)->{
            assert DBManager.getManager() != null;
            DBManager.getManager().createWorker();
            refresh();
        });

        //刷新列表
        btnRefresh.addActionListener((e)->{
            refresh();
            list.requestFocus();
        });

        //搜索框逻辑
        searchBox.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(searchBox.getText().equals("输入名字或身份证信息查找"))
                    return;
                search(searchBox.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(searchBox.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });



        //List点击
        list.addListSelectionListener(e -> {
            if (list.getSelectedIndex()==-1)return;
            AnListRenderModel tmpModel= listModel.getElementAt(list.getSelectedIndex());
            showWorkerInfo(tmpModel);
        });

        //双击打开工人窗口
        list.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()==2){
                    if (list.getSelectedIndex()==-1)return;
                    AnListRenderModel model= listModel.getElementAt(list.getSelectedIndex());
                    String siteName=Objects.requireNonNull(cobSite.getSelectedItem()).toString();

                    WindowBuilder.showWorkWindow(model.getInfo(),siteName,(values)-> refresh());
                }
            }
        });

        //工地筛选事件
        cobSite.addItemListener(e -> {
            searchBox.setText("输入名字或身份证信息查找");
            if (isPropertyRunning)return;
            loadingList();
        });

        cbShowLeave.addActionListener((e)-> loadingList());

        btnPropertyAlert.addActionListener(e-> WindowBuilder.showPropertyWindow());

        btnPrint.addActionListener(e -> {
            Vector<Vector> vectors=new Vector<>();
            Vector<String> header=new Vector<>();
            ArrayList<Bean> beans=DBManager.getManager().loadingWorkerList();
            if (beans==null)return;
            for (Bean bean:beans){
                Vector<String> cells=new Vector<>();
                for (Info info:bean.getArray()){
                    if (vectors.size()==0){
                        //录入表头
                        header.add(info.getName());
                    }
                    //数据录入
                    if (info.getName().equals(PropertyFactory.LABEL_BIRTH)) cells.add(AnUtils.formateDate((Date) info.getValue()));
                    else cells.add(info.getValueString());
                }
                if (vectors.size()==0) vectors.add(header);
                vectors.add(cells);
            }
            //传入打印
            if (AnUtils.showPrintWindow(this,vectors))
                AnPopDialog.show(this,"打印完成！",AnPopDialog.SHORT_TIME);
            else AnPopDialog.show(this,"打印取消或失败！",AnPopDialog.SHORT_TIME);
        });
    }

    /**
     * 初始化
     */
    private void initData(){
        loading(null);
    }


    /**
     *
     *
     * 构造函数
     *
     *
     */
    WorkerPanel(){
    	setBackground(Color.WHITE);
        initView();
        initEvent();
        initData();
    }


    /**
     * 搜索工人
     */
    private void search(String text){
        //1增加字符，0减少字符
        listModel.clear();
        if (searchTmpList==null)return;
        for (AnListRenderModel model:searchTmpList){
            if (model.getTitle().contains(text)||model.getInfo().contains(text)){
                if (!cbShowLeave.isSelected()&&DBManager.getManager().isWorkerLeaveAllSite(model.getInfo()))continue;
                listModel.addElement(model);
            }
        }
        list.revalidate();
    }

    /**
     * 读取DBManager中的已经装载好的
     */
    private void loadingList(){
        if(listModel!=null)
            listModel.clear();
        //读取工人、创建副本
        if (searchTmpList==null)searchTmpList=new ArrayList<>();
       //新逻辑
        String siteName=(cobSite.getSelectedItem()!=null)&&!cobSite.getSelectedItem().equals("全部")?cobSite.getSelectedItem().toString() :"";
        String[] ids=DBManager.getManager().getWorkerIdAt(siteName,cbShowLeave.isSelected());
        if (ids!=null){
            //已经筛选出
            for (String id:ids){
                AnListRenderModel model=new AnListRenderModel(DBManager.getManager().getWorkerName(id),id);
                listModel.addElement(model);
                searchTmpList.add(model);
            }
        }
    }

    /**
     * 刷新所有数据
     */
    private void refresh(){
        int ol=cobSite.getSelectedIndex();
        assert DBManager.getManager() != null;
        DBManager.getManager().updateSalaryManagerData();
        DBManager.getManager().updateWorkerBaseData();
        loading(null);
        list.revalidate();
        cobSite.setSelectedIndex(ol);
    }


    /**
     * 显示工人信息
     * @param model 模型
     */
    private void showWorkerInfo(AnListRenderModel model){
        if (model==null)
            return;
        assert DBManager.getManager() != null;
        Bean worker=DBManager.getManager().getWorker(model.getInfo());
        if (worker==null)
            return;
        labAge.setText(String.valueOf(AnUtils.convertAge(model.getInfo())));
        labBorn.setText(new SimpleDateFormat(Resource.DATE_FORMATE).format(AnUtils.convertBornDate(model.getInfo())));
        labAddress.setText(DBManager.getBeanInfoStringValue(worker,PropertyFactory.LABEL_ADDRESS));
        labPhone.setText(DBManager.getBeanInfoStringValue(worker,PropertyFactory.LABEL_PHONE));
        labBankID.setText(DBManager.getBeanInfoStringValue(worker,PropertyFactory.LABEL_BANK_ID));
        labBankAddress.setText(DBManager.getBeanInfoStringValue(worker,PropertyFactory.LABEL_BANK_ADDRESS));
        labNation.setText(DBManager.getBeanInfoStringValue(worker,PropertyFactory.LABEL_NATION));
        labWorkingSiteCount.setText(String.valueOf(DBManager.getManager().getWorkerAt(DBManager.getBeanInfoStringValue(worker,PropertyFactory.LABEL_ID_CARD)).size()));
    }

    /**
     * 显示生日信息
     */
    private void showBirthday(){
        StringBuilder sb=new StringBuilder();
        Date td=new Date();
        boolean hasBirthday=false;
        birthdayCount=0;

        for (Bean worker:DBManager.getManager().loadingWorkerList()){
            String id=DBManager.getBeanInfoStringValue(worker,PropertyFactory.LABEL_ID_CARD);
            if (DBManager.getManager().isWorkerLeaveAllSite(id))
                continue;
            if (AnUtils.isDateMDEquality(td, AnUtils.convertBornDate(DBManager.getBeanInfoStringValue(worker,PropertyFactory.LABEL_ID_CARD)))){
                sb.append(DBManager.getBeanInfoStringValue(worker, PropertyFactory.LABEL_NAME)).append("今天生日！\n");
                hasBirthday=true;
                birthdayCount++;
            }
        }
        if (hasBirthday){
            //设置图标为生日图标
            imgBirthday.setToolTipText(sb.toString());
            imgBirthday.setText("有人生日");
        }else{
            imgBirthday.setToolTipText(null);
            imgBirthday.setText("无人生日");
        }
    }


    /**
     * 装载工人属性，提供给表格中的单元格编辑器使用
     */
    private void loadingProperty(){
        //读取工人属性
        isPropertyRunning=true;
        assert DBManager.getManager() != null;
        DataTable property=DBManager.getManager().loadingWorkerProperty();//在这里装载属性
        if (property==null){
            isPropertyRunning=false;
            return;
        }


        cobSite.removeAllItems();
        cobSite.addItem("全部");
        String[] siteNames=DBManager.getManager().getFullBuildingSiteName();
        for (String  value: siteNames){
            cobSite.addItem(value);
        }
        cobSite.setSelectedIndex(0);

        //装载HUB信息

        labWorkingWorker.setText(String.valueOf(DBManager.getManager().getSumWorkerCount()));
        labFullCheckToday.setText(String.valueOf(DBManager.getManager().getCheckInCount(new Date(),1)));
        labCheckToday.setText(String.valueOf(DBManager.getManager().getCheckInCount(new Date(),0.1)));
        labSumLeave.setText(String.valueOf(DBManager.getManager().getLeaveCount()));
        labGotLivingCostCount.setText(String.valueOf(DBManager.getManager().getSumGotSalaryTodayCount()));
        labGotLivingCostToday.setText(String.valueOf(DBManager.getManager().getSumSalaryToday()));
        labBirthdayToday.setText(String.valueOf(birthdayCount));
        labLeaveToday.setText(String.valueOf(DBManager.getManager().getLeaveToday()));
        labSumLeave.setText(String.valueOf(DBManager.getManager().getLeaveCount()));
        isPropertyRunning=false;
    }

    @Override
    public void loading(Object data) {
        assert DBManager.getManager() != null;
        DBManager.getManager().updateWorkerBaseData();
        loadingProperty();
        loadingList();
        showBirthday();
    }
}
