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

public class SitePanel extends JPanel implements Loadable, ComponentLoader {

    private final String searchText="输入您要搜索的内容";

    private AnImageLabel anImageLabel;
    private JTextField tbSearch;
    private DefaultListModel listModel;
    private AnList list;
    private AnButton btnRefash;
    private AnButton btnCreate;
    private JLabel label;
    private JLabel label_2;

    //color
    private Color pressColor=new Color(249, 156, 51);
    private Color enterColor=new Color(216, 99, 68);
    private Color normalColor=new Color(114, 114, 114);

    //窗口组件
    private SiteCreateWindow createWindow=null;//创建工地窗口
    private SiteInfoWindow infoWindow=null;//工地详情窗口








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

    void refash(){
        tbSearch.setText("");
        assert DBManager.getManager() != null;
        initList(DBManager.getManager().loadingBuildingSiteList());
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
        tbSearch.setText(searchText);
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
        
        btnRefash = new AnButton("刷新");
        btnRefash.setToolTipText("刷新列表");
        springLayout.putConstraint(SpringLayout.NORTH, btnRefash, 0, SpringLayout.NORTH, tbSearch);
        springLayout.putConstraint(SpringLayout.WEST, btnRefash, -85, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, btnRefash, 0, SpringLayout.SOUTH, tbSearch);
        springLayout.putConstraint(SpringLayout.EAST, btnRefash, -10, SpringLayout.EAST, this);
        btnRefash.setFont(new Font("等线", Font.PLAIN, 15));
        add(btnRefash);
        btnRefash.setBorderColor(normalColor);
        btnRefash.setBorderEnterColor(enterColor);
        btnRefash.setBorderPressColor(pressColor);
        
        btnCreate = new AnButton("创建工地");
        btnCreate.setToolTipText("创建一个新的工地");
        springLayout.putConstraint(SpringLayout.NORTH, btnCreate, 0, SpringLayout.NORTH, btnRefash);
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
        
        label_2 = new JLabel("未知");
        label_2.setForeground(enterColor);
        label_2.setFont(new Font("等线", Font.PLAIN, 15));
        label_2.setBounds(143, 31, 159, 23);
        panel.add(label_2);
        
        JLabel label_4 = new JLabel("今日出勤：");
        label_4.setHorizontalAlignment(SwingConstants.RIGHT);
        label_4.setForeground(Color.GRAY);
        label_4.setFont(new Font("等线", Font.PLAIN, 15));
        label_4.setBounds(10, 97, 123, 23);
        panel.add(label_4);
        
        JLabel label_5 = new JLabel("未知");
        label_5.setForeground(new Color(216, 99, 68));
        label_5.setFont(new Font("等线", Font.PLAIN, 15));
        label_5.setBounds(143, 97, 159, 23);
        panel.add(label_5);
        
        JLabel label_6 = new JLabel("出勤比：");
        label_6.setHorizontalAlignment(SwingConstants.RIGHT);
        label_6.setForeground(Color.GRAY);
        label_6.setFont(new Font("等线", Font.PLAIN, 15));
        label_6.setBounds(10, 130, 123, 23);
        panel.add(label_6);
        
        JLabel label_7 = new JLabel("未知");
        label_7.setForeground(new Color(216, 99, 68));
        label_7.setFont(new Font("等线", Font.PLAIN, 15));
        label_7.setBounds(143, 130, 159, 23);
        panel.add(label_7);
        
        JLabel label_8 = new JLabel("今日出勤：");
        label_8.setHorizontalAlignment(SwingConstants.RIGHT);
        label_8.setForeground(Color.GRAY);
        label_8.setFont(new Font("等线", Font.PLAIN, 15));
        label_8.setBounds(10, 163, 123, 23);
        panel.add(label_8);
        
        JLabel label_9 = new JLabel("未知");
        label_9.setForeground(new Color(216, 99, 68));
        label_9.setFont(new Font("等线", Font.PLAIN, 15));
        label_9.setBounds(143, 163, 159, 23);
        panel.add(label_9);
        
        JLabel label_10 = new JLabel("项目名称：");
        label_10.setHorizontalAlignment(SwingConstants.RIGHT);
        label_10.setForeground(Color.GRAY);
        label_10.setFont(new Font("等线", Font.PLAIN, 15));
        label_10.setBounds(318, 31, 123, 23);
        panel.add(label_10);
        
        JLabel label_11 = new JLabel("未知");
        label_11.setForeground(new Color(216, 99, 68));
        label_11.setFont(new Font("等线", Font.PLAIN, 15));
        label_11.setBounds(451, 31, 159, 23);
        panel.add(label_11);
        
        JLabel label_1 = new JLabel("建设单位：");
        label_1.setHorizontalAlignment(SwingConstants.RIGHT);
        label_1.setForeground(Color.GRAY);
        label_1.setFont(new Font("等线", Font.PLAIN, 15));
        label_1.setBounds(318, 64, 123, 23);
        panel.add(label_1);
        
        JLabel label_3 = new JLabel("未知");
        label_3.setForeground(new Color(216, 99, 68));
        label_3.setFont(new Font("等线", Font.PLAIN, 15));
        label_3.setBounds(451, 64, 159, 23);
        panel.add(label_3);
        
        JLabel label_12 = new JLabel("施工单位：");
        label_12.setHorizontalAlignment(SwingConstants.RIGHT);
        label_12.setForeground(Color.GRAY);
        label_12.setFont(new Font("等线", Font.PLAIN, 15));
        label_12.setBounds(318, 97, 123, 23);
        panel.add(label_12);
        
        JLabel label_13 = new JLabel("未知");
        label_13.setForeground(new Color(216, 99, 68));
        label_13.setFont(new Font("等线", Font.PLAIN, 15));
        label_13.setBounds(451, 97, 159, 23);
        panel.add(label_13);
        
        AnButton btnPrint = new AnButton("创建工地");
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
            createWindow.setCallback(values -> {

                refash();
            });
        });

        btnRefash.addActionListener(e -> {
            refash();
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()>=2){
                    if (infoWindow!=null){
                        infoWindow.dispose();
                    }
                    AnListRenderModel model= (AnListRenderModel) listModel.get(list.getSelectedIndex());
                    if (model==null)return;
                    infoWindow=new SiteInfoWindow(model.getTitle());
                    infoWindow.setVisible(true);
                }
            }
        });

    }

    @Override
    public void initializeData() {
        listModel=new DefaultListModel();
        list.setModel(listModel);
    }
}
