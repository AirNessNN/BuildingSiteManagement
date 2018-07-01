package application;

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
import java.util.Objects;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.TableModelListener;

/**
 * 详细信息窗口
 */
public class InfoWindow extends Window implements ComponentLoader {

    private static final String[] INFO_HEADER =new String[]{"属性名","属性值"};
    private static final String[] CHECK_IN_HEADER =new String[]{"日期","出勤记录","备注"};
    private static final String[] SALARY_HEADER=new String[]{"日期","领取数额","备注"};
    //工人
    private Bean worker;
    private String id="";
    //数据表格
    private AnTable infoTable;
    private AnTable checkInTable;
    private AnTable salaryTable;
    private AnTable siteTable;
    //工地名称
    private String siteName ="";
    private AnButton btnSite;
    private AnButton btnSave;

    private boolean infoF,salaryF,checkInF,siteF;//表格监听器改动flag
    private JComboBox cobSite;//选择的工地，出勤数据和工资领取情况都是要选择工地才能显示



	InfoWindow() {
        initializeComponent();
        initializeEvent();
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
        
        btnSite = new AnButton("修改工地");
        btnSite.setEnabled(false);
        sl_panel_3.putConstraint(SpringLayout.NORTH, cobSite, 0, SpringLayout.NORTH, btnSite);
        sl_panel_3.putConstraint(SpringLayout.EAST, cobSite, -40, SpringLayout.WEST, btnSite);
        sl_panel_3.putConstraint(SpringLayout.NORTH, btnSite, 7, SpringLayout.NORTH, panel_3);
        sl_panel_3.putConstraint(SpringLayout.SOUTH, btnSite, -7, SpringLayout.SOUTH, panel_3);
        panel_3.add(btnSite);
        btnSite.setText("  修改工地  ");
        
        btnSave = new AnButton("保存数据");
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
        infoTable.setCellColumnEdited(0,false);


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
        
        JPanel panel_4 = new JPanel();
        tabbedPane.addTab("所在工地信息", null, panel_4, null);
        panel_4.setLayout(new BorderLayout(0, 0));
        
        JScrollPane scrollPane_3 = new JScrollPane();
        panel_4.add(scrollPane_3);
        
        siteTable = new AnTable();
        scrollPane_3.setViewportView(siteTable);
        siteTable.setCellColumnEdited(0,false);
        siteTable.setColumn(INFO_HEADER);
        AnDateComboBoxEditor dateComboBoxEditor=new AnDateComboBoxEditor();
        siteTable.addComponentCell(dateComboBoxEditor,2,1);

        AnComboBoxEditor typeEditor=new AnComboBoxEditor();
        typeEditor.setModel(new DefaultComboBoxModel<>(DBManager.getManager().getWorkerPropertyArray(PropertyFactory.LABEL_WORKER_TYPE)));
        siteTable.addComponentCell(typeEditor,1,1);
    }

    @Override
    public void initializeEvent() {
        //每个表格的监听器
        TableModelListener infoListener = e -> {
            infoF = infoTable.getChangedCells().getSize() > 0;
            btnSave.setEnabled((infoF || salaryF || checkInF || siteF));
        };
        infoTable.getTableModel().addTableModelListener(infoListener);

        TableModelListener salaryListener = e -> {
            salaryF = salaryTable.getChangedCells().getSize() > 0;
            btnSave.setEnabled((infoF || salaryF || checkInF || siteF));
        };
        salaryTable.getTableModel().addTableModelListener(salaryListener);

        TableModelListener checkInListener = e -> {
            checkInF = checkInTable.getChangedCells().getSize() > 0;
            btnSave.setEnabled((infoF || salaryF || checkInF || siteF));
        };
        checkInTable.getTableModel().addTableModelListener(checkInListener);

        TableModelListener siteListener = e -> {
            siteF = siteTable.getChangedCells().getSize() > 0;
            btnSave.setEnabled((infoF || salaryF || checkInF || siteF));
        };
        siteTable.getTableModel().addTableModelListener(siteListener);

        //关闭的保存提示功能
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
                        save();
                    }
                }
                dispose();
                try {
                    finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });

        cobSite.addActionListener((e)-> initializeWorker(worker.find(PropertyFactory.LABEL_ID_CARD).getValueString(),Objects.requireNonNull(cobSite.getSelectedItem()).toString()));

        //修改工地
        btnSite.addActionListener((e)->{

        });

        btnSave.addActionListener((e)-> {
            long time=System.currentTimeMillis();
            save();
            System.out.println("保存耗时："+(System.currentTimeMillis()-time));
        });
    }


    @Deprecated
    public void initializeWorker(Bean worker, String site){
	    if (worker==null)
	        return;
	    this.worker=worker;
	    this.siteName =site;
	    initializeWorker();
    }

    void initializeWorker(String id, String site){
        if (id==null)
            return;

        assert DBManager.getManager() != null;
        worker=DBManager.getManager().getWorker(id);
        this.siteName =site;
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
        int index=0;
        for (Object o:tmpList){
            Info info = (Info) o;

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
            if (info.getName().equals(PropertyFactory.LABEL_ID_CARD)){
                id=info.getValueString();
            }

            Vector cells=new Vector();
            cells.add(info.getName());
            cells.add(info.getValue());
            infoRows.add(cells);

            Column column=DBManager.getManager().getWorkerProperty(info.getName());
            if (column!=null){
                AnComboBoxEditor editor=new AnComboBoxEditor();
                editor.setModel(new DefaultComboBoxModel<>(DBManager.getManager().getWorkerPropertyArray(info.getName())));
                infoTable.addComponentCell(editor,index,1);
            }
            index++;
        }
        infoTable.clearCheckPoint();
        infoTable.getTableModel().setDataVector(infoRows,AnUtils.convertToVector(INFO_HEADER));
        infoTable.setCheckPoint();

        //以下的所有数据加载都需要SiteName，所以SiteName要判空
        //加载出勤
        SimpleDateFormat dateFormat=new SimpleDateFormat(Resource.DATE_FORMATE);
        if (siteName ==null)
            return;
        assert DBManager.getManager() != null;
        ArrayList<IDateValueItem> tmpCheckIn=DBManager.getManager().getCheckInManager()
                .getWorkerDateValueList(
                        id,
                        siteName
                );
        //加载工资
        ArrayList<IDateValueItem> tmpSalary=DBManager.getManager().getSalaryManager()
                .getWorkerDateValueList(
                        id,
                        siteName
                );
        Vector<Vector> checkInRows=new Vector<>();
        Vector<Vector> salaryRows=new Vector<>();

        int maxSize=tmpCheckIn.size()>tmpSalary.size()?tmpCheckIn.size():tmpSalary.size();


        AnDateComboBoxEditor editor=new AnDateComboBoxEditor();

        for (int i=0;i<maxSize;i++){
            if (i<tmpCheckIn.size()){
                IDateValueItem item=tmpCheckIn.get(i);
                Vector<String> cells=new Vector<>();
                cells.add(dateFormat.format(item.getDate()));
                cells.add(item.getValue().toString());
                cells.add(item.getTag());
                checkInRows.add(cells);


                checkInTable.addComponentCell(editor,checkInRows.size()-1,0);
            }
            if (i<tmpSalary.size()){
                IDateValueItem item=tmpSalary.get(i);
                Vector<String> cells=new Vector<>();
                cells.add(dateFormat.format(item.getDate()));
                cells.add(item.getValue().toString());
                cells.add(item.getTag());
                salaryRows.add(cells);


                salaryTable.addComponentCell(editor,salaryRows.size()-1,0);
            }
        }
        checkInTable.clearCheckPoint();
        checkInTable.getTableModel().setDataVector(checkInRows,AnUtils.convertToVector(CHECK_IN_HEADER));
        checkInTable.setCheckPoint();

        salaryTable.clearCheckPoint();
        salaryTable.getTableModel().setDataVector(salaryRows,AnUtils.convertToVector(SALARY_HEADER));
        salaryTable.setCheckPoint();

        //加载工地选择器
        cobSite.setModel(
                new DefaultComboBoxModel(AnUtils.toArray(DBManager.getManager().getWorkerAt(id)))
        );
        cobSite.setSelectedItem(siteName);

        //加载工地信息
        Vector<Vector> vectors=new Vector<>();
        DataTable site=DBManager.getManager().getBuildingSite(siteName);

        if (site==null)
            return;
        site.setSelectedRowValue(PropertyFactory.LABEL_ID_CARD,worker.find(PropertyFactory.LABEL_ID_CARD).getValueString());

        Vector rowDealSalary=new Vector<>();
        rowDealSalary.add(PropertyFactory.LABEL_DEAL_SALARY);
        rowDealSalary.add(site.getSelectedRowAt(PropertyFactory.LABEL_DEAL_SALARY));

        Vector rowType=new Vector();
        rowType.add(PropertyFactory.LABEL_WORKER_TYPE);
        rowType.add(site.getSelectedRowAt(PropertyFactory.LABEL_WORKER_TYPE));

        Vector rowEntry=new Vector();
        rowEntry.add(PropertyFactory.LABEL_ENTRY_TIME);
        try {
            rowEntry.add(new SimpleDateFormat(Resource.DATE_FORMATE).format(site.getSelectedRowAt(PropertyFactory.LABEL_ENTRY_TIME)));
        }catch (Exception e){
            rowEntry.add("");
        }

        vectors.add(rowDealSalary);
        vectors.add(rowType);
        vectors.add(rowEntry);
        siteTable.clearCheckPoint();
        siteTable.getTableModel().setDataVector(vectors,AnUtils.convertToVector(INFO_HEADER));
        siteTable.setCheckPoint();

    }

    /**
     * 保存数据
     */
    private void save(){
	    try {
            if (!saveInfo())
                return;

            if (cobSite.getSelectedItem()==null){//没选择工地
                callback(id,null);//工地传空值回去
                btnSave.setEnabled(false);
                infoTable.setCheckPoint();
                salaryF=false;
                checkInF=false;
                infoF=false;
                AnPopDialog.show(this,"保存成功！",AnPopDialog.SHORT_TIME);
                return;
            }

            //保存工地的信息

            //保存子管理器的数据
            assert DBManager.getManager() != null;
            saveChildingManagerData(checkInTable,DBManager.getManager().getCheckInManager());
            saveChildingManagerData(salaryTable,DBManager.getManager().getSalaryManager());
            saveSiteInfo();//保存工地信息
            btnSave.setEnabled(false);
            infoTable.setCheckPoint();
            salaryTable.setCheckPoint();
            checkInTable.setCheckPoint();
            salaryF=false;
            checkInF=false;
            infoF=false;
            //回调
            callback(id,cobSite.getSelectedItem().toString());
            AnPopDialog.show(this,"保存成功！",AnPopDialog.SHORT_TIME);
        }catch (Exception e){
            Application.errorWindow(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 储存表格中的内容
     * @return 成功返回true
     */
    private boolean saveInfo(){
        //检查身份证是否更改
        boolean isIDChanged=false;
        boolean isIDError=false;
        String oldID=null;
        String newID=null;
        TableProperty bean=infoTable.getChangedCells();

        //判断身份证是否正确
        for (int i=0;i<bean.getSize();i++){
            int row=bean.getRank(i).getRow();
            String pn= (String) infoTable.getCell(row,0);//获取单元格属性
            if (pn.equals(PropertyFactory.LABEL_ID_CARD)) {
                if (!AnUtils.isIDCard(bean.getNewValue(i).toString())) {
                    isIDError=true;
                }else {
                    isIDChanged=true;
                    oldID= (String) bean.getOldValue(i);
                    newID=(String) bean.getNewValue(i);
                }
            }else{
                //更新其他信息
                try {
                    Info info =worker.find(pn);
                    Object object=bean.getNewValue(i);
                    info.setValue(object);
                    //加入属性
                    Column column=DBManager.getManager().getWorkerProperty(info.getName());
                    if (column!=null&&!info.getValueString().equals("")){
                        DBManager.getManager().addPropertyValue(info.getName(),info.getValueString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (isIDError){
            Application.informationWindow("身份证信息错误，请重新填写，已保存其他数据。");
        }

        if (isIDChanged){
            //更新ID
            worker.find(PropertyFactory.LABEL_ID_CARD).setValue(newID);//设置工人列表的身份证属性
            id=newID;

            assert DBManager.getManager() != null;
            ArrayList<String> siteList=DBManager.getManager().getWorkerAt(oldID);
            for (String aSiteList : siteList) {
                DataTable tmpSite = DBManager.getManager().getBuildingSite(aSiteList);
                Column array = tmpSite.findColumn(PropertyFactory.LABEL_ID_CARD);
                boolean flag = array.changeValue(oldID, newID);//设置工地列表中存放的工人身份信息
                //找到工地的话，一定会存在工资和出勤，所以也要更新工资和出勤信息
                if (flag) {
                    //工资
                    DBManager.getManager().getSalaryManager().getWorker(oldID).setName(newID);
                    //出勤
                    DBManager.getManager().getCheckInManager().getWorker(oldID).setName(newID);
                }
            }
            DBManager.getManager().updateTmpWorkerList();//更新了ID之后要更新DB中的缓存数据
        }
        return true;
    }

    /**
     * 储存其他信息
     */
    private void saveChildingManagerData(AnTable table, ChildrenManager manager) throws ParseException {
        TableProperty ctb=table.getChangedCells();
        ArrayList<Integer> skips=new ArrayList<>();
        SimpleDateFormat format=new SimpleDateFormat(Resource.DATE_FORMATE);

        String id=worker.find(PropertyFactory.LABEL_ID_CARD).getValueString();
        if (cobSite.getSelectedItem()==null)
            return;
        String site=cobSite.getSelectedItem().toString();

        for (int i=0;i<ctb.getSize();i++){
            int row=ctb.getRank(i).getRow();
            int col=ctb.getRank(i).getColumn();

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
                int tr=ctb.getRank(j).getRow(),tc=ctb.getRank(j).getColumn();

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
                        manager.deleteData(
                                id
                                ,site
                                ,od
                        );
                        //新增一条数据
                        manager.updateData(
                                id
                                ,site
                                ,nd
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
                        manager.updateData(id,site,nd,nv,nt);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (col==1){
                    nd=format.parse((String) table.getCell(row,0));
                    nv=Double.valueOf((String) ctb.getNewValue(i));
                    nt= (String) table.getCell(row,2);

                        try {
                            manager.updateData(id,site,nd,nv,nt);
                        } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (col==2){
                    nd=format.parse((String) table.getCell(row,0));
                    nt= (String)ctb.getNewValue(i);
                    nv= Double.valueOf((String) table.getCell(row,1));

                    try {
                        manager.updateData(id,site,nd,nv,nt);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                skips.add(row);
            }
        }
    }

    private void saveSiteInfo() throws ParseException {
        //获取更改条目的属性名
        TableProperty tableProperty=siteTable.getChangedCells();
        String[] pns=new String[tableProperty.getSize()];//属性名
        for (int i=0;i<tableProperty.getSize();i++){
            pns[i]= (String) siteTable.getCell(tableProperty.getRank(i).getRow(),0);
        }
        assert DBManager.getManager() != null;
        DataTable site=DBManager.getManager().getBuildingSite(siteName);
        if (siteTable==null)
            return;
        site.selectRow(PropertyFactory.LABEL_ID_CARD,id);

        //填充
        for (int i=0;i<tableProperty.getSize();i++){
            if (pns[i].equals(PropertyFactory.LABEL_DEAL_SALARY))
                site.setSelectedRowValue(pns[i],Double.valueOf((String) tableProperty.getNewValue(i)));
            if (pns[i].equals(PropertyFactory.LABEL_ENTRY_TIME)){
                SimpleDateFormat format=new SimpleDateFormat(Resource.DATE_FORMATE);
                site.setSelectedRowValue(pns[i],format.parse((String) tableProperty.getNewValue(i)));
            }
            if (pns[i].equals(PropertyFactory.LABEL_WORKER_TYPE)){
                site.setSelectedRowValue(PropertyFactory.LABEL_WORKER_TYPE,tableProperty.getNewValue(i));
            }
        }
    }

    @Override @Deprecated
    public void initializeData(Object... args) {
    }
}
