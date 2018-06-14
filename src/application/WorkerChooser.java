package application;

import component.*;
import dbManager.AnBean;
import dbManager.DBManager;
import dbManager.PropertyFactory;

import javax.swing.JScrollPane;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class WorkerChooser extends Window implements ComponentLoader {

    private final String[] headers=new String[]{"姓名","身份证"};
    private Vector<Vector> tmpList=null;
    
	private AnTable table;
	private AnButton btnOK;
	private AnList list;
	private JTextField tbSearch;
	private AnButton btnSelect;
	private AnButton btnCancel;


    public WorkerChooser(){
    	setResizable(false);
        initializeComponent();
        initializeEvent();
        initializeData();
    }


    @Override
    public void initializeComponent() {
        setTitle("工人选择器");
        setSize(789,589);
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(252, 10, 522, 405);
        getContentPane().add(scrollPane);
        
        table = new AnTable();
        scrollPane.setViewportView(table);
        
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(10, 10, 232, 541);
        getContentPane().add(scrollPane_1);

        list = new AnList(new AnInfoCellRenderer());
        scrollPane_1.setViewportView(list);
        
        btnOK = new AnButton("完成选择");
        btnOK.setBounds(674, 521, 100, 30);
        getContentPane().add(btnOK);
        btnOK.setBorderColor(new Color(13, 171, 173));
        
        btnCancel = new AnButton("取消");
        btnCancel.setBounds(564, 521, 100, 30);
        getContentPane().add(btnCancel);
        
        JLabel label = new JLabel("搜索名字或者身份证号码：");
        label.setForeground(Color.GRAY);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setFont(new Font("等线", Font.PLAIN, 15));
        label.setBounds(252, 425, 180, 24);
        getContentPane().add(label);
        
        tbSearch = new JTextField();
        tbSearch.setFont(new Font("等线", Font.PLAIN, 15));
        tbSearch.setBounds(442, 425, 222, 24);
        getContentPane().add(tbSearch);
        tbSearch.setColumns(10);
        
        JLabel lblNewLabel = new JLabel("帮助：从右边的数据区选择工人到左边的列表中，完成选择即可继续下一步操作");
        lblNewLabel.setForeground(Color.GRAY);
        lblNewLabel.setFont(new Font("等线", Font.PLAIN, 14));
        lblNewLabel.setBounds(252, 459, 522, 15);
        getContentPane().add(lblNewLabel);
        
        btnSelect = new AnButton("选择工人");
        btnSelect.setBounds(674, 425, 100, 24);
        getContentPane().add(btnSelect);
    }

    @Override
    public void initializeEvent() {
        tbSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                System.out.println("asd");
                updateTable(tbSearch.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                System.out.println("asd");
                updateTable(tbSearch.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                System.out.println("asd");
                updateTable(tbSearch.getText());
            }
        });

        btnSelect.addActionListener(e -> {
            addToSiteList();
        });

        btnOK.addActionListener(e -> {
            String[] ids=new String[list.getItemSize()];
            for (int i=0;i<ids.length;i++){
                AnListRenderModel model= (AnListRenderModel) list.getElementAt(i);
                ids[i]=model.getInfo();
            }
            callback(ids);
            dispose();
        });

        btnCancel.addActionListener(e -> {
            dispose();
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()==2) addToSiteList();
            }
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()==2){
                    deleteFromSiteList();
                }
            }
        });
    }

    @Override
    public void initializeData() {
        table.setColumn(headers);
        table.setCellColumnEdited(0,false);
        table.setCellColumnEdited(1,false);

        DBManager manager =DBManager.getManager();
        if (manager==null)return;

        //填充工人列表
        Vector<Vector> vectors=new Vector<>();
        for (AnBean worker:manager.loadingWorkerList()){
            Vector<String> rows=new Vector<>();
            rows.add(DBManager.getBeanInfoStringValue(worker,PropertyFactory.LABEL_NAME));
            rows.add(DBManager.getBeanInfoStringValue(worker,PropertyFactory.LABEL_ID_CARD));
            vectors.add(rows);
        }
        this.tmpList=new Vector<>(vectors);//添加到缓存
        table.getTableModel().setDataVector(vectors,AnUtils.convertToVector(AnUtils.convertObjectArray(headers)));
    }



    private void updateTable(String text){
        Vector<Vector> tmp=new Vector<>();
        for (Vector cells:tmpList){
            String name= (String) cells.get(0);
            String id= (String) cells.get(1);
            if (name.contains(text)||id.contains(text))tmp.add(cells);
        }
        table.getTableModel().setDataVector(tmp,AnUtils.convertToVector(AnUtils.convertObjectArray(headers)));
        table.revalidate();
        repaint();
    }

    private int getTableSelctedRowIndex(){
        return table.getSelectedRow();
    }

    private AnListRenderModel getSelectedRowData(){
        int index=getTableSelctedRowIndex();
        if (index<0)return null;

        AnListRenderModel model=new AnListRenderModel((String) table.getCell(index,0),(String) table.getCell(index,1));
        return model;
    }

    private void addToSiteList(){
        int index=getTableSelctedRowIndex();
        if (index<0){
            AnPopDialog.show(this,"请先选择一个工人",AnPopDialog.SHORT_TIME);
        }else {
            list.addElement(getSelectedRowData());
            table.getTableModel().removeRow(index);
        }
    }

    private void deleteFromSiteList(){
        int index=list.getSelectedIndex();
        if (index<0)AnPopDialog.show(this,"请选择一个",AnPopDialog.SHORT_TIME);
        else {
            AnListRenderModel model= (AnListRenderModel) list.getElementAt(index);
            Vector row=new Vector();
            row.add(model.getTitle());
            row.add(model.getInfo());
            table.getTableModel().addRow(row);
            list.removeElementAt(index);
        }
    }


}
