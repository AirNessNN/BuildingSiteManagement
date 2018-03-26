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

    //������
	private AnTextField searchBox;
	//�����б�ģ��
	private AnList<AnInfoListDataModel> list=null;

	//�������Զ�ά����
    private final String[] tableHeader={"��������","��Ŀֵ"};

	private volatile boolean isSearching=false;//�����߳����б��
    private volatile int textChanged=0;//�����Ƿ����ı�  -1��ɾ��   0��δ�ı�   1����
    private Runnable searchTask=null;
    private Runnable searchStateChangeTask=null;

    private ExecutorService  executorService=null;//�̳߳�
    private JTable table;

    //�б����===============
    private int selectedIndex=-1;//ѡ�е��б���
    private DefaultTableModel listModel=null;








    /**
     * ����ؼ����ֺ��¼�
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

        AnLabel lblTitle = new AnLabel("���˹���");
        lblTitle.setBounds(10, 10, 175, 52);
        lblTitle.setFont(new Font("������ͤ��ϸ�ڼ���", Font.PLAIN, 40));
        lblTitle.setForeground(Color.WHITE);
        add(lblTitle);
        
        searchBox = new AnTextField();
        searchBox.setText("�������ֻ����֤��Ϣ����");
        searchBox.setBounds(10, 84, 357, 23);
        add(searchBox);

        //���֤���ַ�����
        searchBox.setColumns(18);

        MyButton btnPrint = new MyButton("\u6253\u5370\u8868");
        btnPrint.setBounds(583, 84, 76, 23);
        add(btnPrint);


        MyButton btnEntry = new MyButton("\u5165\u804C\u767B\u8BB0");
        btnEntry.setBounds(377, 84, 93, 23);
        add(btnEntry);
        btnEntry.addActionListener((e)->{
            list.addElement(new AnInfoListDataModel("����","����"));
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

        //��
        table = new JTable();
        table.setRowHeight(30);
        table.setFont(Resource.FONT_TABLE_ITEM);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane_1.setViewportView(table);

        //�����ģ��
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
        //�����򽹵��߼�
        searchBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                searchBox.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(searchBox.getText().equals("")){
                    searchBox.setText("�������ֻ����֤��Ϣ����");
                }
            }
        });
        //�������߼�
        searchBox.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(searchBox.getText().equals("�������ֻ����֤��Ϣ����"))
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


        //������ݼ���
        listModel.addTableModelListener(this);

        //�����̷߳���
        searchTask= () -> {
            //��סList
            synchronized (list){
                list.clear();
                String value=searchBox.getText();
                for (int i=0;i<DBManager.getManager().getWorkerListSize();i++){

                    if(textChanged==-1){
                        list.clear();
                        i=0;//��λ����
                        textChanged=0;
                    }

                    Anbean anbean=DBManager.getManager().loadingWorkerList().get(i);
                    //��ȡInfo����
                    Info name=anbean.get("����");
                    Info number=anbean.get("���֤");
                    //��ȡֵ
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

        //����״̬�����߳�
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


        //List���
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
     * ��������
     * @param state
     */
    public void search(int state){
        if(list==null)
            return;

        //����״̬
        textChanged=state;

        if(isSearching){
            //�����߳�����
            executorService.execute(searchStateChangeTask);
        }else{
            //�߳�δ����
            executorService.execute(searchTask);
        }
    }


    /**
     * ��乤�����ݵ��б�
     * @param listDataModel
     */
    public void addTableData(AnInfoListDataModel listDataModel){
        if(list.getItemSize()<=0)
            return;
        if(listDataModel==null)
            return;
       if(table==null)
           return;

        //��ԭ����������Bean
        Anbean bean=null;
        for(Anbean anbean:DBManager.getManager().loadingWorkerList()){
            if(anbean.get("����").getValue().equals(listDataModel.getTitle())&&anbean.get("���֤").getValue().equals(listDataModel.getInfo())){
                bean=anbean;
                selectedIndex=DBManager.getManager().loadingWorkerList().indexOf(anbean);
                System.out.println(selectedIndex);
                break;
            }
        }

       //��������
       Vector<Vector> data=new Vector<>();
       //��ȡ�б��е�����
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


        //�����������������
        listModel.setDataVector(data,name);
    }


    /**
     * ��ȡDBManager�е��Ѿ�װ�غõ�
     */
    public void loadingList(){
        if(list!=null)
            list.clear();
        //��ȡ�����б�
        for(Anbean anbean :DBManager.getManager().loadingWorkerList()){
            //��ȡInfo����
            Info name=anbean.get("����");
            Info number=anbean.get("���֤");
            //��ȡֵ
            String strName=(String) name.getValue();
            String strNum=(String)number.getValue();
            //ת�����б�ģ��
            AnInfoListDataModel model=new AnInfoListDataModel(strName,strNum);
            //��ӵ��б�
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

    //������ݼ���
    @Override
    public void tableChanged(TableModelEvent e) {

    }
}
