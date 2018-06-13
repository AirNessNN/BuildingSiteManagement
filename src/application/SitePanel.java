package application;

import component.*;
import dbManager.AnDataTable;
import dbManager.DBManager;
import resource.Resource;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

public class SitePanel extends JPanel implements Loadable, ComponentLoader {

    private final String searchText="输入您要搜索的内容";

    private AnImageLabel anImageLabel;
    private JTextField tbSearch;
    private DefaultListModel listModel;
    private AnList list;
    private MyButton btnRefash;
    private MyButton button;
    private JLabel label;
    private JLabel label_1;
    private JLabel label_2;
    private JLabel label_3;








    public SitePanel(){
        initializeComponent();
        initializeEvent();
        initializeData();
    }














    private void initList(ArrayList<AnDataTable> tables){
        if (listModel!=null)
            listModel.clear();


        for (AnDataTable site:tables){
            AnListRenderModel model=new AnListRenderModel(site.getName(),site.getColumnRowCount(0)+"个工人");
            this.listModel.addElement(model);
        }
        assert list != null;
        list.validate();
    }

    private void initList(ArrayList<AnDataTable> tables,String containsChar){
        if (listModel!=null)
            listModel.clear();


        for (AnDataTable site:tables){
            if (site.getName().contains(containsChar)){
                AnListRenderModel model=new AnListRenderModel(site.getName(),site.getColumnRowCount(0)+"个工人");
                this.listModel.addElement(model);
            }
        }
        assert list != null;
        list.validate();
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

        AnImageLabel lblNewLabel = new AnImageLabel(Resource.getResource("workpanel_banner.png"));
        springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, 0, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, lblNewLabel, -934, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, lblNewLabel, 76, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, lblNewLabel, 0, SpringLayout.EAST, this);
        add(lblNewLabel);

        anImageLabel = new AnImageLabel(new Color(24, 96, 48));
        springLayout.putConstraint(SpringLayout.NORTH, anImageLabel, 0, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, anImageLabel, 0, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, anImageLabel, 76, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, anImageLabel, 0, SpringLayout.EAST, this);
        add(anImageLabel);
        
        JScrollPane scrollPane = new JScrollPane();
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
        list.setSelectedColor(new Color(216, 99, 68));
        scrollPane.setViewportView(list);
        add(tbSearch);
        tbSearch.setColumns(10);
        list.setFixedCellHeight(60);
        
        btnRefash = new MyButton("刷新");
        springLayout.putConstraint(SpringLayout.NORTH, btnRefash, 0, SpringLayout.NORTH, tbSearch);
        springLayout.putConstraint(SpringLayout.WEST, btnRefash, -85, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, btnRefash, 0, SpringLayout.SOUTH, tbSearch);
        springLayout.putConstraint(SpringLayout.EAST, btnRefash, -10, SpringLayout.EAST, this);
        btnRefash.setFont(new Font("等线", Font.PLAIN, 15));
        add(btnRefash);
        btnRefash.setNormal(new Color(114, 114, 114));
        btnRefash.setEnter(new Color(216, 99, 68));
        btnRefash.setPress(new Color(249, 156, 51));
        
        button = new MyButton("创建工地");
        springLayout.putConstraint(SpringLayout.NORTH, button, 0, SpringLayout.NORTH, btnRefash);
        springLayout.putConstraint(SpringLayout.WEST, button, 10, SpringLayout.EAST, tbSearch);
        springLayout.putConstraint(SpringLayout.SOUTH, button, 0, SpringLayout.SOUTH, tbSearch);
        springLayout.putConstraint(SpringLayout.EAST, button, 100, SpringLayout.EAST, tbSearch);
        add(button);
        button.setNormal(new Color(114, 114, 114));
        button.setEnter(new Color(216, 99, 68));
        button.setPress(new Color(249, 156, 51));
        
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new LineBorder(new Color(128, 128, 128)), "HUB信息中心", TitledBorder.LEADING, TitledBorder.TOP, new Font("等线",Font.PLAIN,15), Color.GRAY));
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
        
        label_1 = new JLabel("工地创建日期：");
        label_1.setHorizontalAlignment(SwingConstants.RIGHT);
        label_1.setForeground(Color.GRAY);
        label_1.setFont(new Font("等线", Font.PLAIN, 15));
        label_1.setBounds(10, 64, 123, 23);
        panel.add(label_1);
        
        label_2 = new JLabel("未知");
        label_2.setForeground(new Color(216,99,68));
        label_2.setFont(new Font("等线", Font.PLAIN, 15));
        label_2.setBounds(143, 31, 159, 23);
        panel.add(label_2);
        
        label_3 = new JLabel("未知");
        label_3.setForeground(new Color(216, 99, 68));
        label_3.setFont(new Font("等线", Font.PLAIN, 15));
        label_3.setBounds(143, 64, 159, 23);
        panel.add(label_3);
        
        



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


    }

    @Override
    public void initializeData() {
        listModel=new DefaultListModel();
        list.setModel(listModel);
    }
}
