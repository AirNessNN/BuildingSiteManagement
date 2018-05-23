package application;

import component.*;
import dbManager.*;
import resource.Resource;
import java.awt.Color;
import javax.swing.*;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;

import SwingTool.MyButton;


public class WorkerPanel extends ImagePanel implements Loadable, TableModelListener{
	
	//工人属性二维数组
    private final String[] tableHeader={"数据类型","项目值"};

	private volatile boolean isSearching=false;//搜索线程运行标记
    private volatile int textChanged=0;//文字是否发生改变  -1是删除   0是未改变   1增加
    private Runnable searchTask=null;//动态搜索Runnable
    private Runnable searchStateChangeTask=null;//动态搜索监听线程

    private AnTable table;//表单

    //列表控制===============
    private int selectedIndex=-1;//选中的列表项

    private Vector<Vector<String>> data=null;//表单中的数据
    //控件
    private MyButton btnSave;
    private AnTextField searchBox;
    private AnList<AnInfoListDataModel> list=null;
    private MyButton btnEntry;
    private MyButton btnLeave;
    private MyButton btnPrint;
    private MyButton btnRefresh;
    private MyButton button;

    private AnComboBoxEditor cobSex =null;
    private AnComboBoxEditor cobNation =null;
    private AnComboBoxEditor cobWorkerState =null;
    private AnComboBoxEditor cobWorkerType =null;
    private AnComboBoxEditor cobSiteFrom=null;
    private JComboBox<String> cobBuildingSite;//所属工地筛选器

    private AnDateComboBoxEditor dataCob=null;







    /**
     * 载入控件布局和事件
     */
    private void initView(){
        this.setSize(934,671);
        setIcon(AnUtils.getImageIcon(Resource.getResource("workpanel.png")));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
        scrollPane.setBounds(10, 147, 357, 514);
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
        searchBox.setBounds(10, 83, 357, 23);
        add(searchBox);

        //身份证的字符限制
        searchBox.setColumns(18);

        btnPrint = new MyButton("\u6253\u5370\u8868");
        btnPrint.setToolTipText("\u5C06\u6240\u6709\u5DE5\u4EBA\u4FE1\u606F\u6253\u5370\u5230\u8868\u4E2D");
        btnPrint.setBounds(647, 84, 76, 23);
        add(btnPrint);


        btnEntry = new MyButton("\u5165\u804C\u767B\u8BB0");
        btnEntry.setToolTipText("\u589E\u52A0\u65B0\u5458\u5DE5\u5230\u6570\u636E\u5E93\u4E2D");
        btnEntry.setBounds(377, 84, 80, 23);
        add(btnEntry);

        btnLeave = new MyButton("\u79BB\u804C\u767B\u8BB0");
        btnLeave.setToolTipText("\u9009\u5B9A\u7684\u5DE5\u4EBA\u5C06\u4F1A\u79BB\u804C");
        btnLeave.setBounds(467, 84, 80, 23);
        add(btnLeave);
        
        btnRefresh = new MyButton("\u5237\u65B0");
        btnRefresh.setBounds(733, 84, 63, 23);
        add(btnRefresh);
        
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(377, 114, 547, 547);
        add(scrollPane_1);

        //表单
        table=new AnTable();
        table.setRowHeight(30);
        table.setFont(Resource.FONT_TABLE_ITEM);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane_1.setViewportView(table);

        //定义表单模型
        table.setCellColumnEdited(0,false);
        //table.setCellEdited(0,0,false);
        
        btnSave = new MyButton("\u5237\u65B0");
        btnSave.setEnabled(false);
        btnSave.setText("\u4FDD\u5B58");
        btnSave.setBounds(861, 84, 63, 23);
        add(btnSave);

        button = new MyButton("\u5C5E\u6027\u4FEE\u6539");
        button.setToolTipText("\u6DFB\u52A0\u6216\u5220\u9664\u5DE5\u4EBA\u7684\u5C5E\u6027");
        button.setBounds(557, 84, 80, 23);
        add(button);
        
        cobBuildingSite = new JComboBox<>();
        cobBuildingSite.setBounds(83, 116, 284, 21);
        add(cobBuildingSite);
        cobBuildingSite.addItem("全部");
        //cobBuildingSite.addItem("所有");//Debug
        
        JLabel label = new JLabel("\u6240\u5C5E\u5DE5\u5730");
        label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        label.setBounds(10, 120, 63, 15);
        add(label);

        table.setColumn(tableHeader);
        table.getColumnModel().getColumn(0).setPreferredWidth(261);
        table.getColumnModel().getColumn(1).setPreferredWidth(318);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font(Resource.FONT_WEI_RUAN_YA_HEI,Font.PLAIN,17));
        table.getTableHeader().setResizingAllowed(false);

        cobSex=new AnComboBoxEditor();
        cobSex.setEditable(false);
        cobNation=new AnComboBoxEditor();
        cobWorkerState=new AnComboBoxEditor();
        cobWorkerType=new AnComboBoxEditor();
        cobSiteFrom=new AnComboBoxEditor();
        cobSiteFrom.setEditable(false);
        dataCob=new AnDateComboBoxEditor();
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

        //入职登记
        btnEntry.addActionListener((e)->{
            DBManager.getManager().createWorker();
        });

        //保存表单
        btnSave.addActionListener((e)->{
            saveToMemory();
        });

        //刷新列表
        btnRefresh.addActionListener((e)->{
            loadingProperty();
            loadingList();
            list.revalidate();
            list.requestFocus();
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
        table.getListModel().addTableModelListener(this);

        //搜索线程方法
        searchTask= () -> {
            //锁住List
            //noinspection SynchronizeOnNonFinalField
            synchronized (list){
                list.clear();
                String value=searchBox.getText();
                assert DBManager.getManager() != null;
                for (int i = 0; i<DBManager.getManager().getWorkerListSize(); i++){

                    if(textChanged==-1){
                        list.clear();
                        i=0;//复位搜索
                        textChanged=0;
                    }

                    AnBean anBean =DBManager.getManager().loadingWorkerList().get(i);
                    //获取Info属性
                    Info name= anBean.find("名字");
                    Info number= anBean.find("身份证");
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

            //noinspection SynchronizeOnNonFinalField
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

            AnInfoListDataModel tmpModel=list.getElementAt(list.getSelectedIndex());
            //保存按钮开启，意味着有数据改动（数据改动由方法控制，是否开启保存按钮由监听事件控制）
            if(btnSave.isEnabled()){
                int opa=JOptionPane.showConfirmDialog(WorkerPanel.this,"有改动的数据，是否保存？","保存提示",JOptionPane.YES_NO_OPTION);
                if(opa==JOptionPane.YES_OPTION){
                    saveToMemory();
                    fillTableData(tmpModel);
                }else{
                    //舍弃填充
                    btnSave.setEnabled(false);
                    fillTableData(tmpModel);
                }
                list.requestFocus();
            }
            //在存在焦点的情况下才会填充数据，为了防止在loading list的时候触发
            if(list.hasFocus()){
                fillTableData(tmpModel);
            }
        });

        list.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()==2){
                    AnInfoListDataModel model=list.getElementAt(list.getSelectedIndex());
                    WorkerWindow wd=new WorkerWindow();
                    boolean b=wd.initializeData(model.getInfo(), cobBuildingSite.getSelectedItem().toString());
                    System.out.println(b);
                    wd.setVisible(true);
                }
            }
        });

        //工地筛选事件
        cobBuildingSite.addItemListener(e -> {
            loadingList();
        });
    }

    public void initData(){
        loading(null);
    }


    /**
     *
     *
     * 构造函数
     *
     *
     */
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
            Application.startService(searchStateChangeTask);
        }else{
            //线程未启动
            Application.startService(searchTask);
        }
    }


    /**
     * 填充工人数据到列表
     * @param listDataModel
     */
    public void fillTableData(AnInfoListDataModel listDataModel){
        if(list.getItemSize()<=0)
            return;
        if(listDataModel==null)
            return;
       if(table==null)
           return;

        //从原表中搜索到Bean
        AnBean bean=null;
        for(AnBean anBean :DBManager.getManager().loadingWorkerList()){
            if(anBean.find(PropertyFactory.LABEL_NAME).getValue().equals(listDataModel.getTitle())&&
                    anBean.find(PropertyFactory.LABEL_ID_CARD).getValue().equals(listDataModel.getInfo())){
                bean= anBean;
                selectedIndex=DBManager.getManager().loadingWorkerList().indexOf(anBean);
                break;
            }
        }

       //定义数据
       data=new Vector<>();
       //获取列表中的数据
        assert bean != null;
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
        table.getListModel().setDataVector(data,name);
    }


    /**
     * 读取DBManager中的已经装载好的
     */
    public void loadingList(){
        if(list!=null)
            list.clear();
        //读取工人列表
        ArrayList<AnBean> beans=null;
       if (cobBuildingSite==null){
            beans=DBManager.getManager().loadingWorkerList();
       }else if(cobBuildingSite.getSelectedItem()==null){
            beans=DBManager.getManager().loadingWorkerList();
       }else if(cobBuildingSite.getSelectedItem().equals("全部")){
           beans=DBManager.getManager().loadingWorkerList();
       }else{
           String value =(String)cobBuildingSite.getSelectedItem();
           beans=DBManager.getManager().getWorkerListWhere("所属工地",value);
       }
       for(AnBean anBean :beans){
            //获取Info属性
            Info name= anBean.find("名字");
            Info number= anBean.find("身份证");
            //获取值
            String strName=(String) name.getValue();
            String strNum=(String)number.getValue();
            //转换成列表模型
            AnInfoListDataModel model=new AnInfoListDataModel(strName,strNum);
            //添加到列表
            list.addElement(model);
        }
        repaint();
    }


    /**
     * 装载工人属性，提供给表格中的单元格编辑器使用
     */
    public void loadingProperty(){
        //读取工人属性
        assert DBManager.getManager() != null;
        AnArrayBean property=DBManager.getManager().loadingWorkerProperty();//在这里装载属性
        if (property==null)
            return;

        cobBuildingSite.removeAllItems();
        cobBuildingSite.addItem("全部");
        InfoArray<String> infoArray=property.find(PropertyFactory.LABEL_SITE);
        for (String value:infoArray.getValues()){
            cobBuildingSite.addItem(value);
            cobSiteFrom.addItem(value);
        }
        cobBuildingSite.setSelectedIndex(0);
        table.addComponentCell(cobSiteFrom,23,1);

        cobSex.removeAllItems();
        InfoArray<String> sex=property.find(PropertyFactory.LABEL_SEX);
        cobSex.addItem(sex.getValues().get(0));
        cobSex.addItem(sex.getValues().get(1));
        table.addComponentCell(cobSex,8,1);

        cobNation.removeAllItems();
        cobNation.removeAllItems();
        InfoArray<String> nation=property.find(PropertyFactory.LABEL_NATION);
        for (String value:nation.getValues()){
            cobNation.addItem(value);
        }
        table.addComponentCell(cobNation,9,1);

        cobWorkerState.removeAllItems();
        InfoArray<String> workerState=property.find(PropertyFactory.LABEL_WORKER_STATE);
        for (String value:workerState.getValues())
            cobWorkerState.addItem(value);
        table.addComponentCell(cobWorkerState,15,1);

        cobWorkerType.removeAllItems();
        InfoArray<String> workerType=property.find(PropertyFactory.LABEL_WORKER_TYPE);
        for (String value:workerType.getValues())
            cobWorkerType.addItem(value);
        table.addComponentCell(cobWorkerType,14,1);

        table.addComponentCell(dataCob,7,1);
        table.addComponentCell(new AnDateComboBoxEditor(),10,1);
        table.addComponentCell(new AnDateComboBoxEditor(),11,1);
    }


    /**
     * 将文件储存到文件中，调用DB的update的方法/
     */
    public void saveToFile(){
        assert DBManager.getManager() != null;
        DBManager.getManager().updateUserData();
    }

    /**
     * 将表单数据储存到DB中
     */
    public void saveToMemory(){

        for(int i=0;i<data.size();i++){
            String tmp= data.get(i).get(1);
            AnBean tmpBean=DBManager.getManager().getWorker(selectedIndex);
            Info tmpInfo=tmpBean.find(data.get(i).get(0));
            //在数据非空且有意义的情况下，写入到DB中
            if(tmp!=null&&!tmp.equals("")){
                tmpInfo.setValue(tmp);
            }
        }
        btnSave.setEnabled(false);
        loadingList();
    }

    public void deleteAt(int index){

    }

    /**
     * 全盘对比DB中的数据，是否相同
     * @return 发现改动返回True
     */
    public boolean isTableChange(){

        for(int i=0;i<data.size();i++){
            AnBean ab=DBManager.getManager().getWorker(selectedIndex);
            Info info;
            if (ab!=null) {
                info = ab.find(data.get(i).get(0));
                if (info != null) {
                    String origin = String.valueOf(DBManager.getManager().getWorker(selectedIndex).find(data.get(i).get(0)).getValue());
                    if (origin==null||origin.equals("null"))//从String转换过来，可能被转换为“null”
                        origin="";
                    String tmp = data.get(i).get(1);
                    if (tmp == null && origin == null) {
                        continue;
                    }
                    assert tmp != null;
                    if (!tmp.equals(origin)) {
                        if (tmp.equals("") && origin == null) {
                            continue;
                        }
                        return true;
                    }
                }else
                    return false;
            }else
                return false;
        }
        return false;
    }




    @Override
    public void loading(Object data) {
        Application.startService(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        loadingList();
        loadingProperty();
    }


    //表格数据监听
    @Override
    public void tableChanged(TableModelEvent e) {
        if(e.getFirstRow()==-1)
            return;
        if (selectedIndex==-1)
            return;
        //判断是否有数据更改
        if(isTableChange()) {
            AnUtils.log(this,"表单数据更改");
            btnSave.setEnabled(true);
        }
        else{
            AnUtils.log(this,"表单数据未更改");
            btnSave.setEnabled(false);
        }
    }
}
