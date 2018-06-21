package application;

import component.*;

import javax.swing.*;
import java.awt.SystemColor;
import java.awt.Font;
import java.awt.Color;
import java.util.Date;
import java.util.Vector;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

import dbManager.*;

public class SiteInfoWindow extends Window implements ComponentLoader {

    private static final Object[] headers=new Object[]{"身份证号","姓名","协议工价","工种","入职日期","离职日期"};

    private String siteName=null;//工地定位名称
    private DataTable site=null;

    private AnTable table=null;
    private JTextField tbPorjectName;
    private JTextField tbDeginUnit;
    private JTextField tbBuildUnit;
    private AnButton btnSave;


    public SiteInfoWindow(String siteName){
        this.siteName=siteName;
        site=DBManager.getManager().getBuildingSite(siteName);

        initializeComponent();
        initializeEvent();
        initializeData();
    }








    @Override
    public void initializeComponent() {
        setSize(1000,600);
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
        setTitle("");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        SpringLayout springLayout = new SpringLayout();
        getContentPane().setLayout(springLayout);
        getContentPane().setBackground(SystemColor.window);
        
        JLabel lblNewLabel = new JLabel("工人列表");
        springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, 10, SpringLayout.NORTH, getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, lblNewLabel, 10, SpringLayout.WEST, getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, lblNewLabel, 35, SpringLayout.NORTH, getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, lblNewLabel, 78, SpringLayout.WEST, getContentPane());
        lblNewLabel.setForeground(Color.GRAY);
        lblNewLabel.setFont(new Font("等线", Font.PLAIN, 15));
        getContentPane().add(lblNewLabel);
        
        JScrollPane scrollPane = new JScrollPane();
        springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 6, SpringLayout.SOUTH, lblNewLabel);
        springLayout.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -74, SpringLayout.SOUTH, getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, scrollPane, -261, SpringLayout.EAST, getContentPane());
        scrollPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
        getContentPane().add(scrollPane);

        table=new AnTable();
        table.setBorder(null);
        scrollPane.setViewportView(table);
        
        JPanel panel = new JPanel();
        springLayout.putConstraint(SpringLayout.WEST, panel, 6, SpringLayout.EAST, scrollPane);
        springLayout.putConstraint(SpringLayout.SOUTH, panel, 0, SpringLayout.SOUTH, scrollPane);
        springLayout.putConstraint(SpringLayout.EAST, panel, -10, SpringLayout.EAST, getContentPane());
        panel.setForeground(Color.LIGHT_GRAY);
        panel.setBorder(
                new TitledBorder(
                        new LineBorder(
                                new Color(192, 192, 192),
                                1, true),
                        "信息",
                        TitledBorder.TRAILING,
                        TitledBorder.TOP,
                        new Font("等线",Font.PLAIN,15),
                        new Color(114, 114, 114)
                )
        );
        panel.setBackground(Color.WHITE);
        getContentPane().add(panel);
        
        btnSave = new AnButton("创建工地");
        springLayout.putConstraint(SpringLayout.NORTH, panel, 24, SpringLayout.SOUTH, btnSave);
        panel.setLayout(null);
        
        JLabel label = new JLabel("项目名称：");
        label.setForeground(Color.GRAY);
        label.setFont(new Font("等线", Font.PLAIN, 15));
        label.setBounds(10, 31, 75, 20);
        panel.add(label);
        
        JLabel label_1 = new JLabel("建设单位：");
        label_1.setForeground(Color.GRAY);
        label_1.setFont(new Font("等线", Font.PLAIN, 15));
        label_1.setBounds(10, 61, 75, 20);
        panel.add(label_1);
        
        JLabel label_2 = new JLabel("施工单位：");
        label_2.setForeground(Color.GRAY);
        label_2.setFont(new Font("等线", Font.PLAIN, 15));
        label_2.setBounds(10, 91, 75, 20);
        panel.add(label_2);
        
        tbPorjectName = new JTextField();
        tbPorjectName.setEditable(false);
        tbPorjectName.setFont(new Font("等线", Font.PLAIN, 15));
        tbPorjectName.setBounds(95, 31, 140, 20);
        panel.add(tbPorjectName);
        tbPorjectName.setColumns(10);
        
        tbDeginUnit = new JTextField();
        tbDeginUnit.setEditable(false);
        tbDeginUnit.setFont(new Font("等线", Font.PLAIN, 15));
        tbDeginUnit.setColumns(10);
        tbDeginUnit.setBounds(95, 61, 140, 20);
        panel.add(tbDeginUnit);
        
        tbBuildUnit = new JTextField();
        tbBuildUnit.setEditable(false);
        tbBuildUnit.setFont(new Font("等线", Font.PLAIN, 15));
        tbBuildUnit.setColumns(10);
        tbBuildUnit.setBounds(95, 91, 140, 20);
        panel.add(tbBuildUnit);
        springLayout.putConstraint(SpringLayout.NORTH, btnSave, 10, SpringLayout.NORTH, getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, btnSave, -100, SpringLayout.EAST, getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, btnSave, 35, SpringLayout.NORTH, getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, btnSave, -10, SpringLayout.EAST, getContentPane());
        btnSave.setToolTipText("");
        btnSave.setText("打开编辑");
        btnSave.setBorderPressColor(new Color(249, 156, 51));
        btnSave.setBorderEnterColor(new Color(216, 99, 68));
        btnSave.setBorderColor(new Color(114, 114, 114));
        getContentPane().add(btnSave);
    }

    @Override
    public void initializeEvent() {
        btnSave.addActionListener(e -> {
            if (tbBuildUnit.isEditable()){
                //编辑已经打开的状态
                if (table.getChangedCells().getSize()>0){
                    //保存表


                }
                if (table.isEditing())
                    table.getCellEditor().stopCellEditing();
                //保存

            }else {

            }
            setEnable(!tbBuildUnit.isEditable());

        });
    }

    @Override
    public void initializeData() {
        table.setColumn(headers);
        table.setCellColumnEdited(0,false);
        table.setCellColumnEdited(1,false);
        table.setCellColumnEdited(2,false);
        table.setCellColumnEdited(3,false);
        table.setCellColumnEdited(4,false);
        table.setCellColumnEdited(5,false);

        fillData();
    }


    private void setEnable(boolean b){
        tbBuildUnit.setEditable(b);
        tbDeginUnit.setEditable(b);
        tbPorjectName.setEditable(b);

        table.setCellColumnEdited(2,b);
        table.setCellColumnEdited(3,b);
        table.setCellColumnEdited(4,b);
        table.setCellColumnEdited(5,b);

        if (b) btnSave.setText("保存编辑");
        else btnSave.setText("打开编辑");
    }

    /**
     *
     */
    private void fillData(){
        assert DBManager.getManager() != null;
        DataTable site=DBManager.getManager().getBuildingSite(siteName);
        if (site==null)return;

        Vector<Vector> vectors=new Vector<>();
        Column idc=site.findColumn(PropertyFactory.LABEL_ID_CARD);
        if (idc==null)return;

        Object[] ids=idc.toArray();
        Object[] names=new Object[ids.length];
        for (int i=0;i<ids.length;i++){
            Bean worker=DBManager.getManager().getWorker((String) ids[i]);
            names[i]=worker.find(PropertyFactory.LABEL_NAME).getValueString();
        }



        for (int i=0;i<site.findColumn(PropertyFactory.LABEL_ID_CARD).size();i++){
            Vector<String> cells=new Vector<>();
            site.selectRow(i);
            cells.add((String) site.getSelectedRowAt(PropertyFactory.LABEL_ID_CARD));
            cells.add(names[i].toString());
            cells.add(site.getSelectedRowAt(PropertyFactory.LABEL_DEAL_SALARY).toString());
            cells.add(site.getSelectedRowAt(PropertyFactory.LABEL_WORKER_TYPE).toString());
            cells.add(AnUtils.formateDate((Date) site.getSelectedRowAt(PropertyFactory.LABEL_ENTRY_TIME)));
            cells.add(AnUtils.formateDate((Date) site.getSelectedRowAt(PropertyFactory.LABEL_LEAVE_TIME)));
            vectors.add(cells);

            AnDateComboBoxEditor dateComboBoxEditor=new AnDateComboBoxEditor();
            table.addComponentCell(dateComboBoxEditor,i,4);

            AnDateComboBoxEditor dateComboBoxEditor1=new AnDateComboBoxEditor();
            table.addComponentCell(dateComboBoxEditor1,i,5);

            AnComboBoxEditor comboBoxEditor=new AnComboBoxEditor();
            table.addComponentCell(comboBoxEditor,i,3);
            comboBoxEditor.setModel(new DefaultComboBoxModel<>(DBManager.getManager().getWorkerPropertyArray(PropertyFactory.LABEL_WORKER_TYPE)));
        }
        table.getTableModel().setDataVector(vectors,AnUtils.convertToVector(headers));

        tbPorjectName.setText((String) site.getInfosValue(PropertyFactory.LABEL_PROJECT_NAME));
        tbDeginUnit.setText((String) site.getInfosValue(PropertyFactory.LAB_UNIT_OF_DEGIN));
        tbBuildUnit.setText((String) site.getInfosValue(PropertyFactory.LAB_UNIT_OF_BULID));

        table.clearCheckPoint();
        table.setCheckPoint();

        table.setColumnWidth(0,130);
        table.setColumnWidth(1,50);
        table.setColumnWidth(2,40);
        table.setColumnWidth(3,40);
        table.setColumnWidth(4,60);
        table.setColumnWidth(5,60);
    }


    private void save(){
        for (int i=0;i<table.getTableModel().getRowCount();i++){
            String id =table.getCell(i,0).toString();
            Double dealSalary=0d;
            try{
                dealSalary=Double.valueOf(table.getCell(i,2).toString());
            }catch (Exception e){
                Application.errorWindow("协议工价数字转换错误："+e.getMessage());
                continue;
            }
            String type=table.getCell(i,3).toString();


        }
    }
}
