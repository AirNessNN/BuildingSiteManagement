package application;

import compoent.*;
import dbManager.Anbean;
import dbManager.DBManager;
import dbManager.Info;
import resource.Resource;
import java.awt.Color;
import javax.swing.*;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import SwingTool.MyButton;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;


public class WorkerPanel extends ImagePanel implements Loadable, TableModelListener{

    //搜索框
	private AnTextField searchBox;
	//工人列表模型
	private AnList<AnInfoListDataModel> list=null;

	//工人属性二维数组
    private final String[] tableHeader={"数据类型","项目值"};

	private volatile boolean isSearching=false;//搜索线程运行标记
    private volatile int textChanged=0;//文字是否发生改变  -1是删除   0是未改变   1增加
    private Runnable searchTask=null;
    private Runnable searchStateChangeTask=null;

    private ExecutorService  executorService=null;//线程池
    private JTable table;

    //列表控制===============
    private int selectedIndex=-1;//选中的列表项
    private DefaultTableModel listModel=null;








    /**
     * 载入控件布局和事件
     */
    private void initView(){
        this.setSize(934,771);
        setIcon(AnUtils.getImageIcon(Resource.getResource("workpanel.png")));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
        scrollPane.setBounds(10, 114, 357, 647);
        add(scrollPane);
        
        list=new AnList<>(new AnInfoCellRenderer(),355,60);
        scrollPane.setViewportView(list);

        AnLabel lblTitle = new AnLabel("工人管理");
        lblTitle.setBounds(10, 10, 175, 52);
        lblTitle.setFont(new Font("方正兰亭超细黑简体", Font.PLAIN, 40));
        lblTitle.setForeground(Color.WHITE);
        add(lblTitle);
        
        searchBox = new AnTextField();
        searchBox.setText("输入名字或身份证信息查找");
        searchBox.setBounds(10, 84, 357, 23);
        add(searchBox);

        //身份证的字符限制
        searchBox.setColumns(18);

        MyButton btnPrint = new MyButton("\u6253\u5370\u8868");
        btnPrint.setBounds(583, 84, 76, 23);
        add(btnPrint);


        MyButton btnEntry = new MyButton("\u5165\u804C\u767B\u8BB0");
        btnEntry.setBounds(377, 84, 93, 23);
        add(btnEntry);
        btnEntry.addActionListener((e)->{
            list.addElement(new AnInfoListDataModel("标题","内容"));
        });
        
        MyButton btnLeave = new MyButton("\u79BB\u804C\u767B\u8BB0");
        btnLeave.setBounds(480, 84, 93, 23);
        add(btnLeave);
        
        MyButton btnRefresh = new MyButton("\u5237\u65B0");
        btnRefresh.setBounds(669, 84, 76, 23);
        add(btnRefresh);
        
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(377, 162, 547, 599);
        add(scrollPane_1);

        //表单
        table = new JTable();
        table.setRowHeight(30);
        table.setFont(Resource.FONT_TABLE_ITEM);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane_1.setViewportView(table);

        //定义表单模型
        listModel=new DefaultTableModel(null,tableHeader){
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column==0)
                    return false;
                return true;
            }
        };
        table.setModel(listModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(261);
        table.getColumnModel().getColumn(1).setPreferredWidth(318);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font(Resource.FONT_WEI_RUAN_YA_HEI,Font.PLAIN,17));
        table.getTableHeader().setResizingAllowed(false);

        
        AnLabel btnSave = new AnLabel("\u5DE5\u4EBA\u4FE1\u606F");
        btnSave.setBounds(377, 137, 76, 15);
        add(btnSave);
        
        JButton btnNewButton = new JButton("New button");
        btnNewButton.setBounds(480, 134, 93, 23);
        add(btnNewButton);

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
        //搜索框逻辑
        searchBox.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(searchBox.getText().equals("输入名字或身份证信息查找"))
                    return;
                search(1);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(-1);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });


        //表格数据监听
        listModel.addTableModelListener(this);

        //搜索线程方法
        searchTask= () -> {
            //锁住List
            synchronized (list){
                list.clear();
                String value=searchBox.getText();
                for (int i=0;i<DBManager.getManager().getWorkerListSize();i++){

                    if(textChanged==-1){
                        list.clear();
                        i=0;//复位搜索
                        textChanged=0;
                    }

                    Anbean anbean=DBManager.getManager().loadingWorkerList().get(i);
                    //获取Info属性
                    Info name=anbean.get("名字");
                    Info number=anbean.get("身份证");
                    //获取值
                    String strName=(String) name.getValue();
                    String strNum=(String)number.getValue();
                    if(strName.contains(value)||strNum.contains(value)){
                        AnInfoListDataModel listDataModel=new AnInfoListDataModel(strName,strNum);
                        list.addElement(listDataModel);
                    }
                }
                if(textChanged==1){
                    list.notify();
                }
            }

            isSearching=false;
        };

        //搜索状态更改线程
        searchStateChangeTask=()->{

            synchronized (list){
                if(isSearching){
                    try {
                        list.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                String value= searchBox.getText();
                for(int i=0;i<list.getItemSize();i++){
                    if(!list.getElementAt(i).getTitle().contains(value)||!list.getElementAt(i).getInfo().contains(value)){
                        list.removeElementAt(i);
                    }
                }
            }
            textChanged=0;
        };


        //List点击
        list.addListSelectionListener(e -> {
            
            if(list.hasFocus()){
                addTableData(list.getElementAt(list.getSelectedIndex()));
            }
        });
    }

    private void initData(){
        executorService= Executors.newFixedThreadPool(3);
        loading(null);
    }

    public WorkerPanel(){
        initView();
        initEvent();
        initData();
    }


    /**
     * 搜索工人
     * @param state
     */
    public void search(int state){
        if(list==null)
            return;

        //设置状态
        textChanged=state;

        if(isSearching){
            //搜索线程启动
            executorService.execute(searchStateChangeTask);
        }else{
            //线程未启动
            executorService.execute(searchTask);
        }
    }


    /**
     * 填充工人数据到列表
     * @param listDataModel
     */
    public void addTableData(AnInfoListDataModel listDataModel){
        if(list.getItemSize()<=0)
            return;
        if(listDataModel==null)
            return;
       if(table==null)
           return;

        //从原表中搜索到Bean
        Anbean bean=null;
        for(Anbean anbean:DBManager.getManager().loadingWorkerList()){
            if(anbean.get("名字").getValue().equals(listDataModel.getTitle())&&anbean.get("身份证").getValue().equals(listDataModel.getInfo())){
                bean=anbean;
                selectedIndex=DBManager.getManager().loadingWorkerList().indexOf(anbean);
                System.out.println(selectedIndex);
                break;
            }
        }

       //定义数据
       Vector<Vector> data=new Vector<>();
       //获取列表中的数据
        for(Info info : bean.getArray()){
            if(!info.isShow()){
                continue;
            }
            Vector<String> tmpD=new Vector<>();
            tmpD.add(info.getName());
            tmpD.add(info.getValueString());
            data.add(tmpD);
        }
        Vector<String> name=new Vector<>();
        name.add(tableHeader[0]);
        name.add(tableHeader[1]);


        //填充表单并设置相关属性
        listModel.setDataVector(data,name);
    }


    /**
     * 读取DBManager中的已经装载好的
     */
    public void loadingList(){
        if(list!=null)
            list.clear();
        //读取工人列表
        for(Anbean anbean :DBManager.getManager().loadingWorkerList()){
            //获取Info属性
            Info name=anbean.get("名字");
            Info number=anbean.get("身份证");
            //获取值
            String strName=(String) name.getValue();
            String strNum=(String)number.getValue();
            //转换成列表模型
            AnInfoListDataModel model=new AnInfoListDataModel(strName,strNum);
            //添加到列表
            list.addElement(model);
        }
        if(list.getItemSize()>0){
            list.setSelectedIndex(0);
            addTableData(list.getElementAt(list.getSelectedIndex()));
            repaint();
        }
    }



    public void saveToFile(){

    }

    public void saveToMemery(){

    }

    public void deleteAt(int index){

    }


    @Override
    public void loading(Object data) {
        new Thread(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            loadingList();
        }).start();
    }

    //表格数据监听
    @Override
    public void tableChanged(TableModelEvent e) {

    }
}
