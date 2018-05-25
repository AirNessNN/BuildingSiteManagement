package application;
import SwingTool.MyButton;
import component.*;
import dbManager.*;
import resource.Resource;
import test.Test;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * 详细信息窗口
 */
public class InfoWindow extends JDialog implements ComponentLoader {
    static final String[] INFO_HEADER =new String[]{"属性名","属性值"};
    static final String[] CHECK_IN_HEADER =new String[]{"日期","出勤记录","备注"};
    static final String[] SALARY_HEADER=new String[]{"日期","领取数额","备注"};
    private AnBean worker;
    private AnTable infoTable;
    private AnTable checkInTable;
    private AnTable salaryTable;

    private String site="";
    private MyButton btnSite;
    private MyButton btnSave;

    private ArrayList<String> oldInfoData=null;
    private ArrayList<ArrayList> oldCheckInData=null;
    private ArrayList<ArrayList> oldSalaryData=null;

    private TableModelListener infoListener=null,salaryListener=null,checkInListener=null;


	public InfoWindow() {
        initializeComponent();
        initializeEvent();
        initializeData();
        setModal(true);
	}

    @Override
    public void initializeComponent() {
        setTitle("详细信息");
        getContentPane().setLayout(new BorderLayout(0, 0));
        setMinimumSize(new Dimension(500,700));
        setSize(getMinimumSize());
        setLocationRelativeTo(null);
        
        JPanel panel_3 = new JPanel();
        panel_3.setBackground(SystemColor.menu);
        getContentPane().add(panel_3, BorderLayout.SOUTH);
        panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        btnSite = new MyButton("修改工地");
        btnSite.setText("  修改工地  ");
        panel_3.add(btnSite);
        
        btnSave = new MyButton("保存数据");
        btnSave.setText("  保存数据  ");
        panel_3.add(btnSave);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("幼圆", Font.PLAIN, 15));
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("个人信息", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));
        
        JScrollPane scrollPane = new JScrollPane();
        panel.add(scrollPane, BorderLayout.CENTER);
        
        infoTable = new AnTable();
        scrollPane.setViewportView(infoTable);
        infoTable.setColumn(INFO_HEADER);

        AnDateComboBoxEditor dateComboPanel=new AnDateComboBoxEditor();
        infoTable.addComponentCell(dateComboPanel,6,1);

        AnComboBoxEditor sexEdit= new AnComboBoxEditor();
        sexEdit.setModel(new DefaultComboBoxModel<>(DBManager.getManager().getWorkerPropertyArray(PropertyFactory.LABEL_SEX)));
        infoTable.addComponentCell(sexEdit,4,1);

        AnComboBoxEditor workerTypeEditor=new AnComboBoxEditor();
        workerTypeEditor.setModel(new DefaultComboBoxModel<>(DBManager.getManager().getWorkerPropertyArray(PropertyFactory.LABEL_WORKER_TYPE)));
        infoTable.addComponentCell(workerTypeEditor,9,1);

        AnComboBoxEditor nationEditor=new AnComboBoxEditor();
        nationEditor.setModel(new DefaultComboBoxModel<>(DBManager.getManager().getWorkerPropertyArray(PropertyFactory.LABEL_NATION)));
        infoTable.addComponentCell(nationEditor,5,1);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("出勤表", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));
        
        JScrollPane scrollPane_1 = new JScrollPane();
        panel_1.add(scrollPane_1);
        
        checkInTable = new AnTable();
        scrollPane_1.setViewportView(checkInTable);
        checkInTable.setColumn(CHECK_IN_HEADER);
        
        JPanel panel_2 = new JPanel();
        tabbedPane.addTab("工资表", null, panel_2, null);
        panel_2.setLayout(new BorderLayout(0, 0));
        
        JScrollPane scrollPane_2 = new JScrollPane();
        panel_2.add(scrollPane_2);
        
        salaryTable = new AnTable();
        scrollPane_2.setViewportView(salaryTable);
        salaryTable.setColumn(SALARY_HEADER);
    }

    @Override
    public void initializeEvent() {
        infoListener= e -> {
            btnSave.setEnabled(isChangedData());
        };
        infoTable.getListModel().addTableModelListener(infoListener);

        salaryListener= e -> {
            btnSave.setEnabled(isChangedData());
        };
        salaryTable.getListModel().addTableModelListener(salaryListener);

        checkInListener= e -> {
            btnSave.setEnabled(isChangedData());
        };
        checkInTable.getListModel().addTableModelListener(checkInListener);
    }

    @Override
    public void initializeData() {
        initializeWorker();
    }

    public void initializeWorker(AnBean worker,String site){
	    if (worker==null)
	        return;
	    this.worker=worker;
	    this.site=site;
	    initializeWorker();
    }

    public void initializeWorker(String id,String site){
        if (id==null)
            return;

        worker=DBManager.getManager().getWorker(id);
        this.site=site;
        if (worker==null)
            return;
        initializeWorker();
    }

    private void initializeWorker(){
	    if (this.worker==null)
	        return;

	    //加载个人信息
        ArrayList tmpList=worker.getValueList();
        Vector<Vector> infoRows=new Vector<>();
        for (Object o:tmpList){
            Info info= (Info) o;

            if (info.getName().equals(PropertyFactory.LABEL_NUMBER))
                continue;
            if (info.getName().equals(PropertyFactory.LABEL_AGE))
                continue;
            if (info.getName().equals(PropertyFactory.LABEL_BIRTH))
                continue;
            if (info.getName().equals(PropertyFactory.LABEL_SITE))
                continue;
            if (info.getName().equals(PropertyFactory.LABEL_LEAVE_TIME))
                continue;
            if (info.getName().equals(PropertyFactory.LABEL_WORKER_STATE))
                continue;
            if (info.getName().equals(PropertyFactory.LABEL_TOTAL_WORKING_DAY))
                continue;
            if (info.getName().equals(PropertyFactory.LABEL_SURPLUS_SALARY))
                continue;
            if (info.getName().equals(PropertyFactory.LABEL_DUTY_ARR))
                continue;
            if (info.getName().equals(PropertyFactory.LABEL_COST_OF_LIVING))
                continue;
            if (info.getName().equals(PropertyFactory.LABEL_SITE))
                continue;

            Vector cells=new Vector();
            cells.add(info.getName());
            cells.add(info.getValue());
            infoRows.add(cells);
        }
        infoTable.getListModel().setDataVector(infoRows,AnUtils.convertToVector(INFO_HEADER));

        //加载出勤
        assert DBManager.getManager() != null;
        ArrayList<IDateValueItem> tmpCheckIn=DBManager.getManager().getCheckInManager()
                .getWorkerDateValueList(
                        DBManager.getBeanInfoStringValue(worker,PropertyFactory.LABEL_ID_CARD),
                        site
                );
        Vector<Vector> checkInRows=new Vector<>();
        for (IDateValueItem item:tmpCheckIn){
            Vector<String> cells=new Vector<>();
            cells.add(new SimpleDateFormat(Resource.DATE_FORMATE).format(item.getDate()));
            cells.add(item.getValue().toString());
            cells.add(item.getTag());
            checkInRows.add(cells);
        }
        checkInTable.getListModel().setDataVector(checkInRows,AnUtils.convertToVector(CHECK_IN_HEADER));

        //加载工资
        ArrayList<IDateValueItem> tmpSalary=DBManager.getManager().getSalaryManager()
                .getWorkerDateValueList(
                        DBManager.getBeanInfoStringValue(worker,PropertyFactory.LABEL_ID_CARD),
                        site
                );
        Vector<Vector> salaryRows=new Vector<>();
        for (IDateValueItem item:tmpSalary){
            Vector<String> cells=new Vector<>();
            cells.add(new SimpleDateFormat(Resource.DATE_FORMATE).format(item.getDate()));
            cells.add(item.getValue().toString());
            cells.add(item.getTag());
            salaryRows.add(cells);
        }
        salaryTable.getListModel().setDataVector(salaryRows,AnUtils.convertToVector(SALARY_HEADER));

        getOldData();
    }

    /**
     * 检查表格数据是否已经发生改变
     * @return
     */
    public boolean isChangedData(){
	    //检查个人信息
        int infoIndex=0;
        for (Object value:infoTable.getListModel().getDataVector()){
            Vector<String> cells= (Vector<String>) value;
            String cell=cells.get(1);
            String oldC=oldInfoData.get(infoIndex++);
            //非空，空的情况不用判断
            if (!((cell==null||cell.equals(""))&&(oldC == null || oldC.equals("")))) {
                if (!cell.equals(oldC))return true;//更改直接退出
            }
        }
        //检查出勤
        for (int i=0;i<checkInTable.getListModel().getDataVector().size();i++){
            Vector<String> cells= (Vector<String>) checkInTable.getListModel().getDataVector().get(i);
            ArrayList<String> oldCells=oldCheckInData.get(i);
            for (int j=0;j<3;j++){
                String cell=cells.get(j);
                String oldC=oldCells.get(j);

                if (!((cell==null||cell.equals(""))&&(oldC == null || oldC.equals("")))) {
                    if (!cell.equals(oldC))return true;//更改直接退出
                }
            }
        }

        //检查工资
        for (int i=0;i<salaryTable.getListModel().getDataVector().size();i++){
            Vector<String> cells= (Vector<String>) salaryTable.getListModel().getDataVector().get(i);
            ArrayList<String> oldCells=oldSalaryData.get(i);
            for (int j=0;j<3;j++){
                String cell=cells.get(j);
                String oldC=oldCells.get(j);

                if (!((cell==null||cell.equals(""))&&(oldC == null || oldC.equals("")))) {
                    if (!cell.equals(oldC))return true;//更改直接退出
                }
            }
        }
        return false;
    }

    /**
     * 复制一份表格数据，用来对比
     */
    public void getOldData(){
        for (Object value:infoTable.getListModel().getDataVector()){
            Vector<String> cells= (Vector<String>) value;
            if (oldInfoData==null)
                oldInfoData=new ArrayList<>();
            oldInfoData.add(cells.get(1));
        }

        for (Object o:checkInTable.getListModel().getDataVector()){
            Vector<String> cells= (Vector<String>) o;
            if (oldCheckInData==null)
                oldCheckInData=new ArrayList<>();
            ArrayList<String> oldCells=new ArrayList<>();
            oldCells.add(cells.get(0));
            oldCells.add(cells.get(1));
            oldCells.add(cells.get(2));
            oldCheckInData.add(oldCells);
        }

        for (Object o:salaryTable.getListModel().getDataVector()){
            Vector<String> cells= (Vector<String>) o;
            if (oldSalaryData==null)
                oldSalaryData=new ArrayList<>();
            ArrayList<String> oldCells=new ArrayList<>();
            oldCells.add(cells.get(0));
            oldCells.add(cells.get(1));
            oldCells.add(cells.get(2));
            oldSalaryData.add(oldCells);
        }

        Test.printList(this,oldInfoData,null);
        Test.printList(this,oldCheckInData,null);
        Test.printList(this,oldSalaryData,null);
    }
}
