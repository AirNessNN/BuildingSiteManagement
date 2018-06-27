package application;

import component.AnButton;
import component.AnDateChooser;
import component.ComponentLoader;
import resource.Resource;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 批量操作窗口
 */
public class QuickCheckWindow extends Window implements ComponentLoader {

    private QuickOpaControl defaultControl;
    private DefaultListModel checkModel;
    private DefaultListModel listModel;
    private AnDateChooser chooser;
    private AnButton btnStartDate;
    private AnButton btnEndDate;
    private JList checkList;
    private JList list;
    private AnButton btnAllCheck;
    private AnButton btnAllCancel;
    private AnButton btnCancel;
    private AnButton btnOK;
    private JTextField txValue;


    public QuickCheckWindow(QuickOpaControl control){
        defaultControl=control;
        initializeComponent();
        initializeEvent();
        initializeData();
    }

    public void setDefaultControl(QuickOpaControl defaultControl) {
        this.defaultControl = defaultControl;
    }

    public void checkSelectedList(){
        if (defaultControl!=null){
            String[] tmpArrays=new String[checkModel.size()];
            for (int i=0;i<checkModel.size();i++){
                tmpArrays[i]= (String) checkModel.get(i);
            }
            defaultControl.selectedCallback(tmpArrays);
        }
    }

    @Override
    public void initializeComponent() {
        setTitle("快速填充");
        setSize(724,592);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        getContentPane().setBackground(Color.WHITE);
        getContentPane().setForeground(Color.WHITE);
        setResizable(false);
        getContentPane().setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 44, 278, 509);
        getContentPane().add(scrollPane);
        
        checkList = new JList();
        checkList.setFont(new Font("等线", Font.PLAIN, 15));
        scrollPane.setViewportView(checkList);
        checkList.setFixedCellHeight(30);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(429, 44, 278, 509);
        getContentPane().add(scrollPane_1);
        
        list = new JList();
        list.setFont(new Font("等线", Font.PLAIN, 15));
        scrollPane_1.setViewportView(list);
        list.setFixedCellHeight(30);

        JLabel lblNewLabel = new JLabel("选中列表");
        lblNewLabel.setForeground(Color.GRAY);
        lblNewLabel.setFont(new Font("等线", Font.PLAIN, 15));
        lblNewLabel.setBounds(10, 10, 109, 24);
        getContentPane().add(lblNewLabel);

        JLabel label = new JLabel("备选列表");
        label.setForeground(Color.GRAY);
        label.setFont(new Font("等线", Font.PLAIN, 15));
        label.setBounds(429, 10, 109, 24);
        getContentPane().add(label);

        btnAllCheck = new AnButton("全部出勤");
        btnAllCheck.setText("全部选中");
        btnAllCheck.setBounds(298, 44, 121, 37);
        getContentPane().add(btnAllCheck);

        btnAllCancel = new AnButton("全部不出勤");
        btnAllCancel.setText("清空选中列表");
        btnAllCancel.setBounds(298, 91, 121, 37);
        getContentPane().add(btnAllCancel);

        btnStartDate = new AnButton("选择起始日期");
        btnStartDate.setBounds(298, 138, 121, 37);
        getContentPane().add(btnStartDate);

        btnEndDate = new AnButton("选择截止日期");
        btnEndDate.setBounds(298, 185, 121, 37);
        getContentPane().add(btnEndDate);
        
        JLabel label_1 = new JLabel("设置值：");
        label_1.setForeground(Color.GRAY);
        label_1.setFont(new Font("等线", Font.PLAIN, 15));
        label_1.setBounds(298, 232, 121, 24);
        getContentPane().add(label_1);

        btnOK = new AnButton("完成");
        btnOK.setBounds(298, 516, 121, 37);
        getContentPane().add(btnOK);
        
        btnCancel = new AnButton("完成");
        btnCancel.setText("取消");
        btnCancel.setBounds(298, 469, 121, 37);
        getContentPane().add(btnCancel);
        
        txValue = new JTextField();
        txValue.setFont(new Font("等线", Font.PLAIN, 15));
        txValue.setBounds(298, 266, 121, 21);
        getContentPane().add(txValue);
        txValue.setColumns(10);
    }

    @Override
    public void initializeEvent() {

        btnStartDate.addActionListener(e -> {
            chooser=new AnDateChooser();
            if (chooser.getDateFormate()==null)return;
            if (defaultControl!=null&&defaultControl.onStartDateSet(chooser.getDateFormate()))
                btnStartDate.setText(chooser.getDateFormate());
        });

        btnEndDate.addActionListener(e -> {
            chooser=new AnDateChooser();
            if (chooser.getDate()==null)return;
            Date date;
            try {
                date=new SimpleDateFormat(Resource.DATE_FORMATE).parse(btnStartDate.getText());
                if (AnUtils.dateAfter(date,chooser.getDate())){
                    Application.informationWindow("起始日期不能在结束日期的后面");
                    btnEndDate.setText("请选择起始日期");
                    return;
                }
            } catch (ParseException e1) {
                Application.informationWindow("请先选择起始日期！");
                return;
            }
            if (defaultControl!=null&&defaultControl.onEndDateSet(chooser.getDateFormate()))
                btnEndDate.setText(chooser.getDateFormate());
        });

        btnAllCheck.addActionListener(e -> moveList(listModel,checkModel));

        btnAllCancel.addActionListener(e -> moveList(checkModel,listModel));

        btnOK.addActionListener(e -> {
            try {
                Date d1=AnUtils.getDate(btnStartDate.getText(),Resource.DATE_FORMATE);
                Date d2=AnUtils.getDate(btnEndDate.getText(),Resource.DATE_FORMATE);
                if (defaultControl!=null)defaultControl.fillData(d1,d2);
                dispose();
            } catch (ParseException e1) {
                Application.errorWindow("填充数据时出错："+e1.toString());
            }

        });

        btnCancel.addActionListener(e -> dispose());

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()>=2) moveListItem(list, listModel, checkModel);
            }
        });

        checkList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()>=2) moveListItem(checkList, checkModel, listModel);
            }
        });

        //文本改变
        txValue.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (defaultControl!=null)defaultControl.setValue(txValue.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (defaultControl!=null)defaultControl.setValue(txValue.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
    }

    @Override
    public void initializeData(Object... args) {

        listModel=new DefaultListModel();
        for (String name:(String[]) defaultControl.getSourceData()){
            listModel.addElement(name);
        }
        list.setModel(listModel);

        checkModel=new DefaultListModel();
        checkList.setModel(checkModel);
    }

    //迁移一个列表模型到目标列表模型
    private void moveList(DefaultListModel sourceModel,DefaultListModel targetModel){
        for (int i=0;i<sourceModel.size();i++){
            targetModel.addElement(sourceModel.get(i));
        }
        sourceModel.clear();
        checkSelectedList();
    }


    //迁移一个列表数据到目标列表
    private void moveListItem(JList clickList, DefaultListModel clickModel, DefaultListModel targetModel){
        if (clickList.getSelectedIndex()==-1)return;
        targetModel.addElement(clickModel.get(clickList.getSelectedIndex()));
        clickModel.removeElement(clickModel.get(clickList.getSelectedIndex()));
        clickList.revalidate();
        checkSelectedList();
    }




    /**
     * <h2>快速操作的控制器接口</h2>
     */
    public interface QuickOpaControl{

        Object getSourceData();

        void setSourceData(Object data);

        void fillData(Date d1,Date d2);

        void setValue(Object value);

        void selectedCallback(String[] arrays);

        boolean onStartDateSet(String dateFormat);

        boolean onEndDateSet(String dateFormat);

    }
}
