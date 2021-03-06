package application;

import component.AnButton;
import component.AnPopDialog;
import component.ComponentLoader;
import dbManager.DataTable;
import dbManager.DBManager;
import dbManager.PropertyFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class SiteCreateWindow extends Window implements ComponentLoader {
	private JTextField tbName;
	private JTextField tbProjectName;
	private JTextField tbBuildUnit;
	private JTextField tbDeignUnit;
	private AnButton btnOK;
	private AnButton btnCancel;

    private WorkerChooser chooser=null;

	private String[] ids=null;
	private Vector<Vector> vectors=null;
	private AnButton btnSelectWorker;



    public SiteCreateWindow(){
    	setResizable(false);
    	getContentPane().setBackground(Color.WHITE);
        initializeComponent();
        initializeEvent();
        initializeData();
    }















    @Override
    public void initializeComponent() {
        setTitle("工地添加");
        setSize(569,392);
        setMinimumSize(getSize());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        
        JLabel label = new JLabel("工地名称：");
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setFont(new Font("等线", Font.PLAIN, 15));
        label.setBounds(10, 10, 116, 24);
        getContentPane().add(label);
        
        tbName = new JTextField();
        tbName.setFont(new Font("等线", Font.PLAIN, 15));
        tbName.setBounds(136, 10, 164, 24);
        getContentPane().add(tbName);
        tbName.setColumns(10);
        
        JLabel label_1 = new JLabel("项目名称：");
        label_1.setHorizontalAlignment(SwingConstants.RIGHT);
        label_1.setFont(new Font("等线", Font.PLAIN, 15));
        label_1.setBounds(10, 44, 116, 24);
        getContentPane().add(label_1);
        
        tbProjectName = new JTextField();
        tbProjectName.setFont(new Font("等线", Font.PLAIN, 15));
        tbProjectName.setColumns(10);
        tbProjectName.setBounds(136, 44, 164, 24);
        getContentPane().add(tbProjectName);
        
        JLabel label_2 = new JLabel("施工单位：");
        label_2.setHorizontalAlignment(SwingConstants.RIGHT);
        label_2.setFont(new Font("等线", Font.PLAIN, 15));
        label_2.setBounds(10, 78, 116, 24);
        getContentPane().add(label_2);
        
        tbBuildUnit = new JTextField();
        tbBuildUnit.setFont(new Font("等线", Font.PLAIN, 15));
        tbBuildUnit.setColumns(10);
        tbBuildUnit.setBounds(136, 78, 164, 24);
        getContentPane().add(tbBuildUnit);
        
        JLabel label_3 = new JLabel("建设单位：");
        label_3.setHorizontalAlignment(SwingConstants.RIGHT);
        label_3.setFont(new Font("等线", Font.PLAIN, 15));
        label_3.setBounds(10, 112, 116, 24);
        getContentPane().add(label_3);
        
        tbDeignUnit = new JTextField();
        tbDeignUnit.setFont(new Font("等线", Font.PLAIN, 15));
        tbDeignUnit.setColumns(10);
        tbDeignUnit.setBounds(136, 112, 164, 24);
        getContentPane().add(tbDeignUnit);
        
        btnOK = new AnButton("完成");
        btnOK.setBounds(461, 326, 93, 30);
        getContentPane().add(btnOK);
        btnOK.setBorderColor(new Color(0, 146, 128));
        
        btnCancel = new AnButton("取消");
        btnCancel.setBounds(358, 326, 93, 30);
        getContentPane().add(btnCancel);
        
        btnSelectWorker = new AnButton("选择工人");
        btnSelectWorker.setBounds(136, 146, 164, 30);
        getContentPane().add(btnSelectWorker);


        JLabel label_4 = new JLabel("此工地的名称");
        label_4.setForeground(Color.GRAY);
        label_4.setFont(new Font("等线", Font.PLAIN, 14));
        label_4.setBounds(310, 14, 339, 15);
        getContentPane().add(label_4);

        JLabel label_5 = new JLabel("此工地所在项目的名称");
        label_5.setForeground(Color.GRAY);
        label_5.setFont(new Font("等线", Font.PLAIN, 14));
        label_5.setBounds(310, 49, 339, 15);
        getContentPane().add(label_5);

        JLabel label_6 = new JLabel("施工方单位名称");
        label_6.setForeground(Color.GRAY);
        label_6.setFont(new Font("等线", Font.PLAIN, 14));
        label_6.setBounds(310, 83, 339, 15);
        getContentPane().add(label_6);

        JLabel label_7 = new JLabel("建设此工地单位的名称");
        label_7.setForeground(Color.GRAY);
        label_7.setFont(new Font("等线", Font.PLAIN, 14));
        label_7.setBounds(310, 117, 339, 15);
        getContentPane().add(label_7);
    }

    @Override
    public void initializeEvent() {
        btnOK.addActionListener(e -> {
            String siteName=tbName.getText();

            assert DBManager.getManager() != null;
            if (siteName==null||siteName.equals("")||DBManager.getManager().getBuildingSite(siteName)!=null){
                AnPopDialog.show(this,"工地名称错误或重复，重新填写",AnPopDialog.SHORT_TIME);
                return;
            }
            //名称已经通过
            try {
                DataTable site=DBManager.getManager().createBuildingSite(siteName);
                site.setInfosValue(PropertyFactory.LABEL_PROJECT_NAME,tbProjectName.getText());
                site.setInfosValue(PropertyFactory.LAB_UNIT_OF_BUILD, tbBuildUnit.getText());
                site.setInfosValue(PropertyFactory.LAB_UNIT_OF_DEIGN, tbDeignUnit.getText());

                if (ids!=null){
                    Application.fillWorkers(this,ids,vectors,siteName);
                    AnPopDialog.show(this,"已经添加"+ids.length+"个工人到"+siteName+"。",AnPopDialog.LONG_TIME);
                }else
                    AnPopDialog.show(this,"工地创建完成！",AnPopDialog.SHORT_TIME);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            callback();
            dispose();
        });

        btnCancel.addActionListener(e -> dispose());

        btnSelectWorker.addActionListener(e -> {
            if (chooser!=null) chooser.dispose();
            chooser=new WorkerChooser();
            chooser.initializeData();
            chooser.setVisible(true);
            chooser.setCallback(values -> {
                String[] ids= (String[]) values[0];
                btnSelectWorker.setText("选择了 "+ids.length+" 个工人");
                this.ids=ids;

                Vector<Vector> vectors= (Vector<Vector>) values[1];
                this.vectors=vectors;

                AnPopDialog.show(this,"工人数据填充完成",AnPopDialog.SHORT_TIME);
            });
        });
    }

    @Override
    public void initializeData(Object... args) {

    }
}
