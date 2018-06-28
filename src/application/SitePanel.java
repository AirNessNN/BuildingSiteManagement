package application;

import component.*;
import dbManager.DataTable;
import dbManager.DBManager;
import dbManager.PropertyFactory;
import resource.Resource;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;

public class SitePanel extends JPanel implements Loadable, ComponentLoader {

    private final String SEARCH_TEXT ="输入您要搜索的内容";

    private AnImageLabel anImageLabel;
    private JTextField tbSearch;
    private DefaultListModel listModel;
    private AnList list;
    private AnButton btnRefresh;
    private AnButton btnCreate;
    private JLabel label;
    private JLabel labCreateDate;

    //color
    private Color pressColor=new Color(249, 156, 51);
    private Color enterColor=new Color(216, 99, 68);
    private Color normalColor=new Color(114, 114, 114);

    //窗口组件
    private SiteCreateWindow createWindow=null;//创建工地窗口
    private AnButton btnPrint;
    private AnButton btnDelete;
    private JLabel labBn;
    private JLabel labDn;
    private JLabel labPn;
    private JLabel labCheckToday;
    private JLabel labCheckRatio;
    private JLabel labLeaveCount;







    public SitePanel(){
    	setBackground(Color.WHITE);
        initializeComponent();
        initializeEvent();
        initializeData();
    }














    private void initList(ArrayList<DataTable> tables){
        initList(tables,"");
    }

    private void initList(ArrayList<DataTable> tables, String containsChar){
        if (listModel!=null)
            listModel.clear();

        for (DataTable site:tables){
            if (site.getName().contains(containsChar)){

                int num=0;
                for (int i=0;i<site.getColumn(0).size();i++){
                    if (site.getCellAt(PropertyFactory.LABEL_LEAVE_TIME,i)==null){
                        num++;
                    }
                }
                AnListRenderModel model=new AnListRenderModel(site.getName(),num+"个工人");
                this.listModel.addElement(model);


            }
        }
        assert list != null;
        list.validate();
    }

    void refresh(){
        tbSearch.setText(SEARCH_TEXT);
        assert DBManager.getManager() != null;
        initList(DBManager.getManager().loadingBuildingSiteList());
    }


    private void showSiteInfo(String siteName){
        DataTable site=DBManager.getManager().getBuildingSite(siteName);
        if (site==null)return;
        labCreateDate.setText(AnUtils.formateDate((Date) site.getInfosValue(PropertyFactory.LABEL_CREATE_DATE)));
        //今日出勤人数
        String[] ids=AnUtils.toStringArray(site.findColumn(PropertyFactory.LABEL_ID_CARD).toArray());
        int checkCount=0;
        int leaveCount=0;
        if (ids!=null){
            for (int i=0;i<ids.length;i++){
                Double d= (Double) DBManager.getManager().getCheckInManager().getValueAt(ids[i],site.getName(),Application.TODAY);
                if (site.getCellAt(PropertyFactory.LABEL_LEAVE_TIME,i)!=null)leaveCount++;
                if (d==null)continue;
                if (d>0)checkCount++;
            }
        }
        labCheckToday.setText(String.valueOf(checkCount)+"人");
        //出勤比
        try{
            assert ids != null;
            labCheckRatio.setText(((checkCount/ids.length)*100)+"%");
        }catch (Exception e){
            labCheckRatio.setText("0%");
        }
        //离职工人数量
        labLeaveCount.setText(leaveCount+"人");
        //工地属性
        labBn.setText((String) site.getInfosValue(PropertyFactory.LAB_UNIT_OF_BUILD));
        labDn.setText((String) site.getInfosValue(PropertyFactory.LAB_UNIT_OF_DEIGN));
        labPn.setText((String) site.getInfosValue(PropertyFactory.LABEL_PROJECT_NAME));
    }













    @Override
    public void loading(Object data) {
        assert DBManager.getManager() != null;
        initList(DBManager.getManager().loadingBuildingSiteList());

    }

    @Override
    public void initializeComponent() {
        this.setSize(934,671);
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        AnLabel lblTitle = new AnLabel("工地管理");
        springLayout.putConstraint(SpringLayout.NORTH, lblTitle, 10, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, lblTitle, 10, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, lblTitle, 62, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, lblTitle, 185, SpringLayout.WEST, this);
        lblTitle.setFont(new Font("方正兰亭超细黑简体", Font.PLAIN, 40));
        lblTitle.setForeground(Color.WHITE);
        add(lblTitle);

        AnImageLabel lblNewLabel = new AnImageLabel(Resource.getResource("sitepanel_banner.png"));
        springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, 0, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, lblNewLabel, -934, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, lblNewLabel, 76, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, lblNewLabel, 0, SpringLayout.EAST, this);
        add(lblNewLabel);

        anImageLabel = new AnImageLabel(enterColor);
        springLayout.putConstraint(SpringLayout.NORTH, anImageLabel, 0, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, anImageLabel, 0, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, anImageLabel, 76, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, anImageLabel, 0, SpringLayout.EAST, this);
        add(anImageLabel);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        springLayout.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -10, SpringLayout.SOUTH, this);
        springLayout.putConstraint(SpringLayout.EAST, scrollPane, 294, SpringLayout.WEST, this);
        add(scrollPane);
        
        tbSearch = new JTextField();
        tbSearch.setForeground(Color.GRAY);
        tbSearch.setText(SEARCH_TEXT);
        tbSearch.setFont(new Font("等线", Font.PLAIN, 15));
        springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.SOUTH, tbSearch);
        springLayout.putConstraint(SpringLayout.NORTH, tbSearch, 10, SpringLayout.SOUTH, anImageLabel);
        springLayout.putConstraint(SpringLayout.WEST, tbSearch, 10, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, tbSearch, 35, SpringLayout.SOUTH, anImageLabel);
        springLayout.putConstraint(SpringLayout.EAST, tbSearch, 0, SpringLayout.EAST, scrollPane);

        list = new AnList(new AnInfoCellRenderer());
        list.setSelectedColor(enterColor);
        scrollPane.setViewportView(list);
        add(tbSearch);
        tbSearch.setColumns(10);
        list.setFixedCellHeight(60);
        
        btnRefresh = new AnButton("刷新");
        btnRefresh.setToolTipText("刷新列表");
        springLayout.putConstraint(SpringLayout.NORTH, btnRefresh, 0, SpringLayout.NORTH, tbSearch);
        springLayout.putConstraint(SpringLayout.WEST, btnRefresh, -85, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, btnRefresh, 0, SpringLayout.SOUTH, tbSearch);
        springLayout.putConstraint(SpringLayout.EAST, btnRefresh, -10, SpringLayout.EAST, this);
        btnRefresh.setFont(new Font("等线", Font.PLAIN, 15));
        add(btnRefresh);
        btnRefresh.setBorderColor(normalColor);
        btnRefresh.setBorderEnterColor(enterColor);
        btnRefresh.setBorderPressColor(pressColor);
        
        btnCreate = new AnButton("创建工地");
        btnCreate.setToolTipText("创建一个新的工地");
        springLayout.putConstraint(SpringLayout.NORTH, btnCreate, 0, SpringLayout.NORTH, btnRefresh);
        springLayout.putConstraint(SpringLayout.WEST, btnCreate, 10, SpringLayout.EAST, tbSearch);
        springLayout.putConstraint(SpringLayout.SOUTH, btnCreate, 0, SpringLayout.SOUTH, tbSearch);
        springLayout.putConstraint(SpringLayout.EAST, btnCreate, 100, SpringLayout.EAST, tbSearch);
        add(btnCreate);
        btnCreate.setBorderColor(normalColor);
        btnCreate.setBorderEnterColor(enterColor);
        btnCreate.setBorderPressColor(pressColor);
        
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(
                new TitledBorder(
                        new LineBorder(
                                new Color(128, 128, 128)
                        ),
                        "HUB信息中心",
                        TitledBorder.LEADING,
                        TitledBorder.TOP,
                        new Font("等线",Font.PLAIN,15),
                        Color.GRAY)
        );
        springLayout.putConstraint(SpringLayout.NORTH, panel, 0, SpringLayout.NORTH, scrollPane);
        springLayout.putConstraint(SpringLayout.WEST, panel, 10, SpringLayout.EAST, scrollPane);
        springLayout.putConstraint(SpringLayout.SOUTH, panel, 200, SpringLayout.NORTH, scrollPane);
        springLayout.putConstraint(SpringLayout.EAST, panel, -10, SpringLayout.EAST, this);
        add(panel);
        panel.setLayout(null);
        
        label = new JLabel("工地创建日期：");
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setForeground(Color.GRAY);
        label.setFont(new Font("等线", Font.PLAIN, 15));
        label.setBounds(10, 31, 123, 23);
        panel.add(label);
        
        labCreateDate = new JLabel("未知");
        labCreateDate.setForeground(enterColor);
        labCreateDate.setFont(new Font("等线", Font.PLAIN, 15));
        labCreateDate.setBounds(143, 31, 159, 23);
        panel.add(labCreateDate);
        
        JLabel label_4 = new JLabel("今日出勤：");
        label_4.setHorizontalAlignment(SwingConstants.RIGHT);
        label_4.setForeground(Color.GRAY);
        label_4.setFont(new Font("等线", Font.PLAIN, 15));
        label_4.setBounds(10, 97, 123, 23);
        panel.add(label_4);
        
        labCheckToday = new JLabel("未知");
        labCheckToday.setForeground(new Color(216, 99, 68));
        labCheckToday.setFont(new Font("等线", Font.PLAIN, 15));
        labCheckToday.setBounds(143, 97, 159, 23);
        panel.add(labCheckToday);
        
        JLabel label_6 = new JLabel("出勤比：");
        label_6.setHorizontalAlignment(SwingConstants.RIGHT);
        label_6.setForeground(Color.GRAY);
        label_6.setFont(new Font("等线", Font.PLAIN, 15));
        label_6.setBounds(10, 130, 123, 23);
        panel.add(label_6);
        
        labCheckRatio = new JLabel("未知");
        labCheckRatio.setForeground(new Color(216, 99, 68));
        labCheckRatio.setFont(new Font("等线", Font.PLAIN, 15));
        labCheckRatio.setBounds(143, 130, 159, 23);
        panel.add(labCheckRatio);
        
        JLabel label_8 = new JLabel("离职工人数量：");
        label_8.setHorizontalAlignment(SwingConstants.RIGHT);
        label_8.setForeground(Color.GRAY);
        label_8.setFont(new Font("等线", Font.PLAIN, 15));
        label_8.setBounds(10, 163, 123, 23);
        panel.add(label_8);
        
        labLeaveCount = new JLabel("未知");
        labLeaveCount.setForeground(new Color(216, 99, 68));
        labLeaveCount.setFont(new Font("等线", Font.PLAIN, 15));
        labLeaveCount.setBounds(143, 163, 159, 23);
        panel.add(labLeaveCount);
        
        JLabel label_10 = new JLabel("项目名称：");
        label_10.setHorizontalAlignment(SwingConstants.RIGHT);
        label_10.setForeground(Color.GRAY);
        label_10.setFont(new Font("等线", Font.PLAIN, 15));
        label_10.setBounds(318, 31, 123, 23);
        panel.add(label_10);
        
        labPn = new JLabel("未知");
        labPn.setForeground(new Color(216, 99, 68));
        labPn.setFont(new Font("等线", Font.PLAIN, 15));
        labPn.setBounds(451, 31, 159, 23);
        panel.add(labPn);
        
        JLabel label_1 = new JLabel("建设单位：");
        label_1.setHorizontalAlignment(SwingConstants.RIGHT);
        label_1.setForeground(Color.GRAY);
        label_1.setFont(new Font("等线", Font.PLAIN, 15));
        label_1.setBounds(318, 64, 123, 23);
        panel.add(label_1);
        
        labDn = new JLabel("未知");
        labDn.setForeground(new Color(216, 99, 68));
        labDn.setFont(new Font("等线", Font.PLAIN, 15));
        labDn.setBounds(451, 64, 159, 23);
        panel.add(labDn);
        
        JLabel label_12 = new JLabel("施工单位：");
        label_12.setHorizontalAlignment(SwingConstants.RIGHT);
        label_12.setForeground(Color.GRAY);
        label_12.setFont(new Font("等线", Font.PLAIN, 15));
        label_12.setBounds(318, 97, 123, 23);
        panel.add(label_12);
        
        labBn = new JLabel("未知");
        labBn.setForeground(new Color(216, 99, 68));
        labBn.setFont(new Font("等线", Font.PLAIN, 15));
        labBn.setBounds(451, 97, 159, 23);
        panel.add(labBn);
        
        btnPrint = new AnButton("创建工地");
        btnPrint.setEnabled(false);
        btnPrint.setToolTipText("打印所选择的数据");
        btnPrint.setText("打印数据");
        springLayout.putConstraint(SpringLayout.NORTH, btnPrint, 0, SpringLayout.NORTH, tbSearch);
        springLayout.putConstraint(SpringLayout.WEST, btnPrint, 10, SpringLayout.EAST, btnCreate);
        springLayout.putConstraint(SpringLayout.SOUTH, btnPrint, 0, SpringLayout.SOUTH, tbSearch);
        springLayout.putConstraint(SpringLayout.EAST, btnPrint, 100, SpringLayout.EAST, btnCreate);
        btnPrint.setBorderPressColor(new Color(249, 156, 51));
        btnPrint.setBorderEnterColor(new Color(216, 99, 68));
        btnPrint.setBorderColor(new Color(114, 114, 114));
        add(btnPrint);
        
        btnDelete = new AnButton("创建工地");
        springLayout.putConstraint(SpringLayout.NORTH, btnDelete, 0, SpringLayout.NORTH, tbSearch);
        springLayout.putConstraint(SpringLayout.WEST, btnDelete, 10, SpringLayout.EAST, btnPrint);
        springLayout.putConstraint(SpringLayout.SOUTH, btnDelete, 0, SpringLayout.SOUTH, tbSearch);
        springLayout.putConstraint(SpringLayout.EAST, btnDelete, 100, SpringLayout.EAST, btnPrint);
        btnDelete.setToolTipText("删除选择的工地");
        btnDelete.setText("删除工地");
        btnDelete.setBorderPressColor(new Color(219, 112, 147));
        btnDelete.setBorderEnterColor(new Color(220, 20, 60));
        btnDelete.setBorderColor(new Color(114, 114, 114));
        add(btnDelete);
        
        



    }

    @Override
    public void initializeEvent() {
        tbSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                tbSearch.selectAll();
            }
        });

        tbSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

                String searchText= tbSearch.getText();

                ArrayList<AnListRenderModel> delete=new ArrayList<>();

                for (int i=0;i<listModel.getSize();i++){
                    AnListRenderModel item= (AnListRenderModel) listModel.get(i);
                    if (!item.getTitle().contains(searchText))
                        delete.add(item);
                }
                for (AnListRenderModel item:delete){
                    listModel.removeElement(item);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                assert DBManager.getManager() != null;
                initList(DBManager.getManager().loadingBuildingSiteList(),tbSearch.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });


        btnCreate.addActionListener(e -> {
            if (createWindow==null)createWindow=new SiteCreateWindow();
            createWindow.setVisible(true);
            createWindow.setCallback(values -> refresh());
        });

        btnRefresh.addActionListener(e -> refresh());

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()>=2){
                    AnListRenderModel model= (AnListRenderModel) listModel.get(list.getSelectedIndex());
                    if (model==null)return;
                    WindowBuilder.showSiteInfoWindow(model.getTitle(),values -> refresh());
                }
            }
        });

        list.addListSelectionListener(e -> {
            if (list.getSelectedIndex()==-1)return;
            AnListRenderModel model= (AnListRenderModel) listModel.elementAt(list.getSelectedIndex());
            showSiteInfo(model.getTitle());
        });
        
        btnDelete.addActionListener(e -> {
            if (list.getSelectedIndex()==-1){
                AnPopDialog.show(this,"先选择一个工地。",1000);
                return;
            }
            AnListRenderModel model= (AnListRenderModel) listModel.elementAt(list.getSelectedIndex());
            String siteName=model.getTitle();

            int r=JOptionPane.showConfirmDialog(this,"即将删除此工地，包括工人信息，是否继续？","操作提示",JOptionPane.YES_NO_OPTION);
            if (r==JOptionPane.OK_OPTION){

                DataTable site=DBManager.getManager().getBuildingSite(siteName);
                if (site==null)return;
                System.out.println(site.getName());

                String[] ids=DBManager.getManager().getBuildingSiteWorkers(siteName);
                for (String id:ids){
                    DBManager.getManager().getCheckInManager().removeSite(id,siteName);
                    DBManager.getManager().getCheckInManager().removeSite(id,siteName);
                }

                DBManager.getManager().deleteBuildingSite(site);
                //删除子管理器信息

                AnPopDialog.show(this,siteName+" 删除完成！",2000);
                refresh();
            }
        });
    }

    @Override
    public void initializeData(Object... args) {
        listModel=new DefaultListModel();
        list.setModel(listModel);
    }
}
