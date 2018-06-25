package application;

import component.*;

import javax.swing.*;
import java.awt.SystemColor;
import java.awt.Font;
import java.awt.Color;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

import dbManager.*;
import resource.Resource;

public class SiteInfoWindow extends Window implements ComponentLoader {

    private static final Object[] headers=new Object[]{"身份证号","姓名","协议工价","工种","入职日期","离职日期"};

    private String siteName;//工地定位名称
    private DataTable site;

    private AnTable table=null;
    private JTextField tbPorjectName;
    private JTextField tbDeginUnit;
    private JTextField tbBuildUnit;
    private AnButton btnSave;
    private AnButton btnAdd;

    private WorkerChooser workerChooser=null;
    private String[] ids=null;
    private AnButton btnPrint;
    private AnButton btnDelete;


    public SiteInfoWindow(String siteName){
        this.siteName=siteName;
        assert DBManager.getManager() != null;
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
        setTitle(siteName+" 详情");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        SpringLayout springLayout = new SpringLayout();
        getContentPane().setLayout(springLayout);
        getContentPane().setBackground(SystemColor.window);
        getContentPane().setBackground(Color.white);
        
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
        
        btnAdd = new AnButton("增加工人");
        btnAdd.setBorderPressColor(new Color(249, 156, 51));
        btnAdd.setBorderEnterColor(new Color(216, 99, 68));
        btnAdd.setBorderColor(new Color(114, 114, 114));
        springLayout.putConstraint(SpringLayout.NORTH, btnAdd, 10, SpringLayout.SOUTH, scrollPane);
        springLayout.putConstraint(SpringLayout.WEST, btnAdd, 10, SpringLayout.WEST, getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, btnAdd, 35, SpringLayout.SOUTH, scrollPane);
        springLayout.putConstraint(SpringLayout.EAST, btnAdd, 100, SpringLayout.WEST, getContentPane());
        getContentPane().add(btnAdd);
        
        btnDelete = new AnButton("增加工人");
        btnDelete.setText("移除工人");
        springLayout.putConstraint(SpringLayout.NORTH, btnDelete, 0, SpringLayout.NORTH, btnAdd);
        springLayout.putConstraint(SpringLayout.WEST, btnDelete, 10, SpringLayout.EAST, btnAdd);
        springLayout.putConstraint(SpringLayout.SOUTH, btnDelete, 0, SpringLayout.SOUTH, btnAdd);
        springLayout.putConstraint(SpringLayout.EAST, btnDelete, 100, SpringLayout.EAST, btnAdd);
        btnDelete.setBorderPressColor(new Color(219, 112, 147));
        btnDelete.setBorderEnterColor(new Color(220, 20, 60));
        btnDelete.setBorderColor(new Color(114, 114, 114));
        getContentPane().add(btnDelete);
        
        AnButton btnContract = new AnButton("增加工人");
        btnContract.setText("包工管理");
        springLayout.putConstraint(SpringLayout.NORTH, btnContract, 0, SpringLayout.NORTH, btnAdd);
        springLayout.putConstraint(SpringLayout.WEST, btnContract, 10, SpringLayout.EAST, btnDelete);
        springLayout.putConstraint(SpringLayout.SOUTH, btnContract, 0, SpringLayout.SOUTH, btnAdd);
        springLayout.putConstraint(SpringLayout.EAST, btnContract, 100, SpringLayout.EAST, btnDelete);
        btnContract.setBorderPressColor(new Color(249, 156, 51));
        btnContract.setBorderEnterColor(new Color(216, 99, 68));
        btnContract.setBorderColor(new Color(114, 114, 114));
        getContentPane().add(btnContract);
        
        btnPrint = new AnButton();
        springLayout.putConstraint(SpringLayout.NORTH, btnPrint, 0, SpringLayout.NORTH, btnAdd);
        springLayout.putConstraint(SpringLayout.WEST, btnPrint, 10, SpringLayout.EAST, btnContract);
        springLayout.putConstraint(SpringLayout.SOUTH, btnPrint, 0, SpringLayout.SOUTH, btnAdd);
        springLayout.putConstraint(SpringLayout.EAST, btnPrint, 100, SpringLayout.EAST, btnContract);
        btnPrint.setText("打印");
        btnPrint.setBorderPressColor(new Color(249, 156, 51));
        btnPrint.setBorderEnterColor(new Color(216, 99, 68));
        btnPrint.setBorderColor(new Color(114, 114, 114));
        getContentPane().add(btnPrint);

        AnButton btnQuickCheck=new AnButton("快速考勤");
        springLayout.putConstraint(SpringLayout.NORTH, btnQuickCheck, 0, SpringLayout.NORTH, btnAdd);
        springLayout.putConstraint(SpringLayout.WEST, btnQuickCheck, 10, SpringLayout.EAST, btnPrint);
        springLayout.putConstraint(SpringLayout.SOUTH, btnQuickCheck, 0, SpringLayout.SOUTH, btnAdd);
        springLayout.putConstraint(SpringLayout.EAST, btnQuickCheck, 100, SpringLayout.EAST, btnPrint);
        getContentPane().add(btnQuickCheck);
        btnQuickCheck.setBorderPressColor(new Color(249, 156, 51));
        btnQuickCheck.setBorderEnterColor(new Color(216, 99, 68));
        btnQuickCheck.setBorderColor(new Color(114, 114, 114));


    }

    @Override
    public void initializeEvent() {
        btnSave.addActionListener(e -> {
            table.clearSelection();
            if (tbBuildUnit.isEditable()){
                //编辑已经打开的状态
                if (table.getChangedCells().getSize()>0){
                    //保存
                    save();
                    AnPopDialog.show(this,"编辑已经保存。",AnPopDialog.SHORT_TIME);
                }
            }
            setEnable(!tbBuildUnit.isEditable());//设置各个控件开关状态
        });

        btnAdd.addActionListener(e -> {
            if (workerChooser==null)workerChooser=new WorkerChooser(){
                @Override
                public boolean filtrator(String id) {
                    for (int i=0;i<ids.length;i++){
                        if (id.equals(ids[i]))return false;
                    }
                    return true;
                }
            };
            workerChooser.setCallback(values -> {
                //此处有两个值传入，如果，在工人选择器中没有填充属性的话，第二个值是无效的
                String[] ids= (String[]) values[0];
                Vector<Vector> vectors= (Vector<Vector>) values[1];

                int i=0;
                for (String id : ids) {
                    Vector<String> tmpPn=null;
                    if (vectors.size()>0)tmpPn=vectors.get(i++);

                    //获取协议工价
                    double d=0d;
                    if (tmpPn!=null){
                        try{
                            d=Double.valueOf(tmpPn.get(2));
                        }catch (Exception ex){
                            AnPopDialog.show(this,"协议工价转换错误，采用默认值：0",AnPopDialog.LONG_TIME);
                        }
                    }

                    //获取日期
                    Date date=new Date();
                    if (tmpPn!=null){
                        try {
                            date=AnUtils.getDate(tmpPn.get(4),Resource.DATE_FORMATE);
                        } catch (ParseException e1) {
                            AnPopDialog.show(this,"入职日期转换错误，采用默认值：今天",AnPopDialog.LONG_TIME);
                        }
                    }

                    //获取类型
                    String type="";
                    if (tmpPn!=null)type=tmpPn.get(3);

                    assert DBManager.getManager() != null;
                    DBManager.getManager().addWorkerToSite(id,siteName,d,type,date);
                }
                workerChooser=null;
                fillData();
                updateIds();
                AnPopDialog.show(this,"加入"+ids.length+"个工人。",AnPopDialog.SHORT_TIME);

            });
            workerChooser.setVisible(true);
        });

        table.addTableClickListener(e -> {
            if (e.getClickCount()>=2){
                String id = (String) table.getCell(e.getRow(),0);
                WindowBuilder.showWorkWindow(id,siteName,values -> fillData());
            }
        });

        btnPrint.addActionListener(e -> {
            if (AnUtils.showPrintWindow(this,table.getPrintVector()))
                AnPopDialog.show(this,"打印完成！",AnPopDialog.SHORT_TIME);
            else AnPopDialog.show(this,"打印取消或失败！",AnPopDialog.SHORT_TIME);
        });

        btnDelete.addActionListener(e -> {
            int seletedIndex=table.getSelectedRow();
            if (seletedIndex!=-1){
                if (JOptionPane.showConfirmDialog(
                        this,
                        "移除该工人将删除所有相关的数据，是否删除？",
                        "操作提示",
                        JOptionPane.YES_NO_OPTION
                )==JOptionPane.OK_OPTION){
                    String id = (String) table.getCell(table.getSelectedRow(),0);
                    DBManager.getManager().removeWorkerFrom(id,siteName);//删除工地
                    table.removeSelectedRow();
                    AnPopDialog.show(this,"删除完成。",2000);
                    callback();
                }
            }else AnPopDialog.show(this,"请先选择",2000);
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

        Application.startService(()->{
            fillData();
            updateIds();
        });
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
            if (worker==null)continue;
            names[i]=worker.find(PropertyFactory.LABEL_NAME).getValueString();
        }



        for (int i=0;i<site.findColumn(PropertyFactory.LABEL_ID_CARD).size();i++){
            if (names[i]==null)continue;
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
        ArrayList<Integer> rows=new ArrayList<>();
        for (int i=0;i<table.getChangedCells().getSize();i++){
            int row=table.getChangedCells().getRank(i).getRow();

            boolean found=false;
            for (Integer rw:rows){
                if (rw==row)found=true;
            }
            if (found)continue;
            rows.add(row);

            String id =table.getCell(row,0).toString();
            Double dealSalary;
            try{
                dealSalary=Double.valueOf(table.getCell(row,2).toString());
            }catch (Exception e){
                Application.errorWindow("协议工价数字转换错误："+e.getMessage());
                continue;
            }
            String type=table.getCell(row,3).toString();

            Date entry;
            try {
                entry=AnUtils.getDate(table.getCell(i,4).toString(),Resource.DATE_FORMATE);
            } catch (ParseException e) {
                Application.errorWindow("入职日期转换错误："+e.getMessage());
                continue;
            }

            Date leave=null;
            try {
                String dateFormate=table.getCell(row,5).toString();
                if (dateFormate!=null&&!dateFormate.equals(""))
                    leave=AnUtils.getDate(dateFormate,Resource.DATE_FORMATE);
            } catch (ParseException e) {
                Application.errorWindow("离职日期转换错误："+e.getMessage());
            }

            site.setInfosValue(PropertyFactory.LAB_UNIT_OF_BULID,tbBuildUnit.getText());
            site.setInfosValue(PropertyFactory.LAB_UNIT_OF_DEGIN,tbDeginUnit.getText());
            site.setInfosValue(PropertyFactory.LABEL_PROJECT_NAME,tbPorjectName.getText());

            site.selectRow(PropertyFactory.LABEL_ID_CARD,id);
            site.setSelectedRowValue(PropertyFactory.LABEL_DEAL_SALARY,dealSalary);
            site.setSelectedRowValue(PropertyFactory.LABEL_ENTRY_TIME,entry);
            site.setSelectedRowValue(PropertyFactory.LABEL_LEAVE_TIME,leave);
            site.setSelectedRowValue(PropertyFactory.LABEL_WORKER_TYPE,type);
        }
        table.clearCheckPoint();
        table.setCheckPoint();
    }


    private void updateIds(){
        ids=new String[table.getTableModel().getRowCount()];
        for (int i=0;i<table.getTableModel().getRowCount();i++){
            ids[i]=table.getCell(i,0).toString();
        }
    }
}
