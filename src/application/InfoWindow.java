package application;
import SwingTool.MyButton;
import component.*;
import dbManager.*;
import resource.Resource;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.TableModelListener;

/**
 * 详细信息窗口
 */
public class InfoWindow extends Window implements ComponentLoader {

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

    //每个表格的监听器
    private TableModelListener infoListener=null,salaryListener=null,checkInListener=null;
    private boolean infoF,salaryF,checkInF;//表格监听器改动flag
    private JComboBox cobSite;//选择的工地，出勤数据和工资领取情况都是要选择工地才能显示


	public InfoWindow() {
        initializeComponent();
        initializeEvent();
        initializeData();
	}

    @Override
    public void initializeComponent() {
        setTitle("详细信息");
        setMinimumSize(new Dimension(500,700));
        setSize(new Dimension(527, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        SpringLayout springLayout = new SpringLayout();
        getContentPane().setLayout(springLayout);
        
        JPanel panel_3 = new JPanel();
        springLayout.putConstraint(SpringLayout.NORTH, panel_3, -40, SpringLayout.SOUTH, getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, panel_3, 0, SpringLayout.WEST, getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, panel_3, 0, SpringLayout.SOUTH, getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, panel_3, 0, SpringLayout.EAST, getContentPane());
        panel_3.setBackground(SystemColor.menu);
        getContentPane().add(panel_3);
        panel_3.setSize(this.getWidth(),200);
        SpringLayout sl_panel_3 = new SpringLayout();
        panel_3.setLayout(sl_panel_3);
        
        cobSite = new JComboBox();
        cobSite.setFont(new Font("等线", Font.PLAIN, 15));
        sl_panel_3.putConstraint(SpringLayout.SOUTH, cobSite, -7, SpringLayout.SOUTH, panel_3);
        panel_3.add(cobSite);
        
        btnSite = new MyButton("修改工地");
        sl_panel_3.putConstraint(SpringLayout.NORTH, cobSite, 0, SpringLayout.NORTH, btnSite);
        sl_panel_3.putConstraint(SpringLayout.EAST, cobSite, -40, SpringLayout.WEST, btnSite);
        sl_panel_3.putConstraint(SpringLayout.NORTH, btnSite, 7, SpringLayout.NORTH, panel_3);
        sl_panel_3.putConstraint(SpringLayout.SOUTH, btnSite, -7, SpringLayout.SOUTH, panel_3);
        panel_3.add(btnSite);
        btnSite.setText("  修改工地  ");
        
        btnSave = new MyButton("保存数据");
        sl_panel_3.putConstraint(SpringLayout.WEST, btnSite, -100, SpringLayout.WEST, btnSave);
        sl_panel_3.putConstraint(SpringLayout.EAST, btnSite, -10, SpringLayout.WEST, btnSave);
        sl_panel_3.putConstraint(SpringLayout.NORTH, btnSave, 7, SpringLayout.NORTH, panel_3);
        sl_panel_3.putConstraint(SpringLayout.WEST, btnSave, -100, SpringLayout.EAST, panel_3);
        sl_panel_3.putConstraint(SpringLayout.SOUTH, btnSave, -7, SpringLayout.SOUTH, panel_3);
        sl_panel_3.putConstraint(SpringLayout.EAST, btnSave, -10, SpringLayout.EAST, panel_3);
        panel_3.add(btnSave);
        btnSave.setText("  保存数据  ");
        
        JLabel label = new JLabel("选择的工地");
        sl_panel_3.putConstraint(SpringLayout.WEST, cobSite, 10, SpringLayout.EAST, label);
        sl_panel_3.putConstraint(SpringLayout.EAST, label, 90, SpringLayout.WEST, panel_3);
        label.setFont(new Font("等线", Font.PLAIN, 15));
        sl_panel_3.putConstraint(SpringLayout.NORTH, label, 7, SpringLayout.NORTH, panel_3);
        sl_panel_3.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, panel_3);
        sl_panel_3.putConstraint(SpringLayout.SOUTH, label, -7, SpringLayout.SOUTH, panel_3);
        panel_3.add(label);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        springLayout.putConstraint(SpringLayout.NORTH, tabbedPane, 0, SpringLayout.NORTH, getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, tabbedPane, 0, SpringLayout.WEST, getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, tabbedPane, -40, SpringLayout.SOUTH, getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, tabbedPane, 0, SpringLayout.EAST, getContentPane());
        tabbedPane.setFont(new Font("幼圆", Font.PLAIN, 15));
        getContentPane().add(tabbedPane);

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
            infoF=infoTable.getChangedCells().getSize()>0;
            btnSave.setEnabled((infoF||salaryF||checkInF));
        };
        infoTable.getTableModel().addTableModelListener(infoListener);

        salaryListener= e -> {
            salaryF=salaryTable.getChangedCells().getSize()>0;
            btnSave.setEnabled((infoF||salaryF||checkInF));
        };
        salaryTable.getTableModel().addTableModelListener(salaryListener);

        checkInListener= e -> {
            checkInF=checkInTable.getChangedCells().getSize()>0;
            btnSave.setEnabled((infoF||salaryF||checkInF));
        };
        checkInTable.getTableModel().addTableModelListener(checkInListener);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                boolean b=infoF||salaryF||checkInF;
                if (b){
                    int opa=JOptionPane.showConfirmDialog(
                            InfoWindow.this,
                            "有数据更改，是否保存",
                            "保存提示",JOptionPane.YES_NO_OPTION);
                    if (opa==JOptionPane.YES_OPTION){
                        saveInfo();
                    }
                }
                dispose();
            }
        });

        btnSite.addActionListener((e)->{
            String id=worker.find(PropertyFactory.LABEL_ID_CARD).getValueString();
            //Object[] sites=WindowBuilder.showBuildingSiteSelectingWindow(id);
            //DBManager.getManager().updateWorkerBuildingSite(id,sites);
        });

        btnSave.addActionListener((e)->{
            save();
        });
    }

    @Override
    public void initializeData() {
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
        infoTable.getTableModel().setDataVector(infoRows,AnUtils.convertToVector(INFO_HEADER));
        infoTable.setCheckPoint();

        //加载出勤
        if (site==null)
            return;
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
        checkInTable.getTableModel().setDataVector(checkInRows,AnUtils.convertToVector(CHECK_IN_HEADER));
        checkInTable.setCheckPoint();

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
        salaryTable.getTableModel().setDataVector(salaryRows,AnUtils.convertToVector(SALARY_HEADER));
        salaryTable.setCheckPoint();

        //加载工地选择器
        cobSite.setModel(new DefaultComboBoxModel(AnUtils.toArray((ArrayList) worker.find(PropertyFactory.LABEL_SITE).getValue())));
    }

    private void save(){
	    try {
            saveInfo();
            saveChildingManagerData(checkInTable,DBManager.getManager().getCheckInManager());
            saveChildingManagerData(salaryTable,DBManager.getManager().getSalaryManager());
            btnSave.setEnabled(false);
            infoTable.setCheckPoint();
            salaryTable.setCheckPoint();
            checkInTable.setCheckPoint();
            salaryF=false;
            checkInF=false;
            infoF=false;
            //回调
            callback(worker.find(PropertyFactory.LABEL_ID_CARD).getValueString(),cobSite.getSelectedItem().toString());
        }catch (Exception e){
            Application.errorWindow(e.getMessage());
        }
    }

    /**
     * 储存表格中的内容
     * @return
     */
    private void saveInfo(){
        //检查身份证是否更改
        boolean isIDChanged=false;
        String oldID=null;
        String newID=null;
        TableProperty bean=infoTable.getChangedCells();
        for (int i = 0; i<bean.getSize(); i++){
            Point pointInfo= bean.getPoint(i);
            String pn= (String) infoTable.getCell(pointInfo.x,0);//获取单元格属性名PropertyName
            if (pn.equals(PropertyFactory.LABEL_ID_CARD)){
                isIDChanged=true;
                oldID= (String) bean.getOldValue(i);
                newID=(String) bean.getNewValue(i);
                break;
            }else{
                //更新其他信息
                try {
                    Info info=worker.find(pn);
                    Object object=bean.getNewValue(i);
                    info.setValue(object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (isIDChanged){
            //更新ID
            worker.find(PropertyFactory.LABEL_ID_CARD).setValue(newID);//设置工人列表的身份证属性

            ArrayList<String> siteList=DBManager.getManager().getWorkerAt(oldID);
            for (int i=0;i<siteList.size();i++){
                AnDataTable tmpSite=DBManager.getManager().getBuildingSite(siteList.get(i));
                AnColumn array=tmpSite.find(PropertyFactory.LABEL_ID_CARD);
                boolean flag=array.changeValue(oldID,newID);//设置工地列表中存放的工人身份信息
                //找到工地的话，一定会存在工资和出勤，所以也要更新工资和出勤信息
                if (flag){
                    //工资
                    DBManager.getManager().getSalaryManager().getWorker(oldID).setName(newID);
                    //出勤
                    DBManager.getManager().getCheckInManager().getWorker(oldID).setName(newID);
                }
            }
        }
        Application.debug(this,worker.toString());
    }

    /**
     * 储存其他信息
     */
    private void saveChildingManagerData(AnTable table, ChildrenManager manager) throws ParseException {
        TableProperty ctb=table.getChangedCells();
        ArrayList<Integer> skips=new ArrayList<>();
        SimpleDateFormat format=new SimpleDateFormat(Resource.DATE_FORMATE);

        String id=worker.find(PropertyFactory.LABEL_ID_CARD).getValueString();
        String site=cobSite.getSelectedItem().toString();

        for (int i=0;i<ctb.getSize();i++){
            int row=ctb.getPoint(i).x;
            int col=ctb.getPoint(i).y;

            //已经规列完的行就不检查
            boolean skip=false;
            for (Integer integer:skips)
                if (integer==row)
                    skip=true;
            if (skip)
                continue;

            //要填充的数据
            Date od = null,nd = null;
            Double ov = null,nv = null;
            String ot = null,nt = null;

            //找到一行数据并修改
            for (int j=i;j<ctb.getSize();j++){
                int tr=ctb.getPoint(j).x,tc=ctb.getPoint(j).y;

                if (tr==row){//当此元素行号和在检测的行号相同
                    if (tc==0) {
                        od = format.parse((String) ctb.getOldValue(j));
                        nd=format.parse((String) ctb.getNewValue(j));
                    }
                    if (tc==1){
                        ov=Double.valueOf((String) ctb.getOldValue(j));
                        nv=Double.valueOf((String) ctb.getNewValue(j));
                    }
                    if (tc==2){
                        ot= (String) ctb.getOldValue(j);
                        nt=(String)ctb.getNewValue(j);
                    }
                }
                //如果数据全部填充物完成，就在下次的循环中跳过这一行
                if (od!=null&&ov!=null&&ot!=null){
                    //开始填充数据
                    //首先删除旧数据
                    try {
                        manager.updateData(
                                id
                                ,site
                                ,od
                                ,ChildrenManager.MOD_DEL
                                ,ov
                                ,ot
                        );
                        //新增一条数据
                        manager.updateData(
                                id
                                ,site
                                ,nd
                                ,ChildrenManager.MOD_ADD
                                ,nv
                                ,nt
                        );
                        skips.add(tr);//跳过这个行号的所有数据
                        break;//跳出此次循环
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            //对单条记录进行搜索后发现还有数据没有填充
            if (od==null||ov==null||ot==null){
                if (col==0){
                    od = format.parse((String) ctb.getOldValue(i));
                    nd=format.parse((String) ctb.getNewValue(i));
                    nv=Double.valueOf((String) table.getCell(row,1));
                    nt= (String) table.getCell(row,2);

                    manager.updateDate(id,site,od,nd);
                    try {
                        manager.updateData(id,site,nd,ChildrenManager.MOD_ALTER,nv,nt);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (col==1){
                    nd=format.parse((String) table.getCell(row,0));
                    nv=Double.valueOf((String) ctb.getNewValue(i));
                    nt= (String) table.getCell(row,2);

                    try {
                        manager.updateData(id,site,nd,ChildrenManager.MOD_ALTER,nv,nt);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (col==2){
                    nd=format.parse((String) table.getCell(row,0));
                    nt= (String)ctb.getNewValue(i);
                    nv= Double.valueOf((String) table.getCell(row,1));

                    try {
                        manager.updateData(id,site,nd,ChildrenManager.MOD_ALTER,nv,nt);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                skips.add(row);
            }
        }
    }
}
