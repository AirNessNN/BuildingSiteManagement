package application;

import component.AnButton;
import component.AnComboBoxEditor;
import component.AnTable;
import component.DialogResult;
import dbManager.*;
import resource.Resource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;

public class EntryWindow extends JDialog {

    public static final String[] HEADER=new String[]{"字段名","字段值"};
    public static EntryWindow window=null;

	private AnTable table;
	private Bean worker=null;
	private DialogResult dialogResult=DialogResult.RESULT_CANCEL;

	public static Bean showWindow(){
	    window=new EntryWindow();

	    return window.getWorker();
    }

    private EntryWindow(){
        initializeComponent();
        initializeData();
        setModal(true);
        setVisible(true);
    }

    private void initializeComponent(){
        setTitle("工人添加");
        setSize(377,641);
        setMinimumSize(new Dimension(377,641));
        SpringLayout springLayout = new SpringLayout();
        getContentPane().setLayout(springLayout);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JScrollPane scrollPane = new JScrollPane();
        springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 0, SpringLayout.NORTH, getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -40, SpringLayout.SOUTH, getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, getContentPane());
        getContentPane().add(scrollPane);

        table = new AnTable();
        scrollPane.setViewportView(table);
        table.setColumn(HEADER);
        table.setCellColumnEdited(0,false);

        JPanel panel = new JPanel();
        springLayout.putConstraint(SpringLayout.NORTH, panel, -40, SpringLayout.SOUTH, getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, panel, 0, SpringLayout.WEST, getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, panel, 0, SpringLayout.SOUTH, getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, panel, 0, SpringLayout.EAST, getContentPane());
        getContentPane().add(panel);
        panel.setLayout(new GridLayout(1, 0, 0, 0));

        AnButton btnOK = new AnButton("确定");
        btnOK.setRound(35);
        panel.add(btnOK);
        btnOK.addActionListener((e)->{
            setModal(false);
            dialogResult= DialogResult.RESULT_OK;
            for (Object o:table.getTableModel().getDataVector()){
                Vector<String> cells= (Vector<String>) o;
                String value=cells.get(1);
                Info info =worker.find(cells.get(0));
                if (info.getType()==Info.TYPE_STRING)
                    info.setValue(value);
                if (info.getType()==Info.TYPE_DOUBLE)
                    try{
                        info.setValue(Double.valueOf(value));
                    }catch (Exception ex){
                        Application.debug(info,ex.toString());
                    }
                if (info.getType()==Info.TYPE_DATE) {
                    try {
                        info.setValue(new SimpleDateFormat((Resource.DATE_FORMATE)).parse(value));
                    } catch (ParseException e1) {
                        Application.debug(info,e1.toString());
                    }
                }
                if (info.getType()==Info.TYPE_INTEGER){
                    try{
                        info.setValue(Integer.valueOf(value));
                    }catch (Exception ex){
                        Application.debug(info,ex.toString());
                    }
                }
                Column column=DBManager.getManager().getWorkerProperty(info.getName());
                if (column!=null&&!info.getValueString().equals("")){
                    DBManager.getManager().addPropertyValue(info.getName(),info.getValueString());
                }
            }
            if (worker.find(PropertyFactory.LABEL_ID_CARD).getValueString().equals("")||worker.find(PropertyFactory.LABEL_NAME).getValueString().equals("")){
                JOptionPane.showMessageDialog(this,"身份证和姓名缺一不可！","提示",JOptionPane.INFORMATION_MESSAGE);
                dialogResult=DialogResult.RESULT_CANCEL;
                return;
            }
            if (!AnUtils.isIDCard(worker.find(PropertyFactory.LABEL_ID_CARD).getValueString())){
                JOptionPane.showMessageDialog(this,"身份证格式错误","提示",JOptionPane.INFORMATION_MESSAGE);
                dialogResult=DialogResult.RESULT_CANCEL;
                return;
            }
            this.dispose();
        });

        AnComboBoxEditor sexEdit= new AnComboBoxEditor();
        sexEdit.setModel(new DefaultComboBoxModel<>(DBManager.getManager().getWorkerPropertyArray(PropertyFactory.LABEL_SEX)));
        table.addComponentCell(sexEdit,4,1);

        AnComboBoxEditor nationEditor=new AnComboBoxEditor();
        nationEditor.setModel(new DefaultComboBoxModel<>(DBManager.getManager().getWorkerPropertyArray(PropertyFactory.LABEL_NATION)));
        table.addComponentCell(nationEditor,5,1);

        AnButton btnCancel = new AnButton("取消");
        btnCancel.setRound(35);
        panel.add(btnCancel);
        btnCancel.addActionListener((e)->{
            dialogResult=DialogResult.RESULT_CANCEL;
            this.dispose();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                dialogResult=DialogResult.RESULT_CANCEL;
                dispose();
            }
        });
    }

    void initializeData(){
        worker=PropertyFactory.createWorker();
        ArrayList tmpList=worker.getValueList();
        Vector<Vector> rows=new Vector<>();
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

            Vector cells=new Vector();
            cells.add(info.getName());
            cells.add("");
            rows.add(cells);

            Column column=DBManager.getManager().getWorkerProperty(info.getName());
            if (column!=null){
                AnComboBoxEditor editor=new AnComboBoxEditor();
                editor.setModel(new DefaultComboBoxModel<>(DBManager.getManager().getWorkerPropertyArray(info.getName())));
                table.addComponentCell(editor,index,1);
            }
            index++;
        }
        table.getTableModel().setDataVector(rows,AnUtils.convertToVector(HEADER));
    }

    private Bean getWorker(){
	    if (dialogResult== DialogResult.RESULT_OK)
            return worker;
	    else
	        return null;
    }

}
