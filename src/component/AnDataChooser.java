package component;

import application.AnUtils;
import application.Application;
import component.Chooser;
import component.DialogResult;
import dbManager.DBManager;
import dbManager.PropertyFactory;
import dbManager.dbInterface.BuildingSiteOperator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * An数据选择器
 */
public class AnDataChooser extends JDialog implements BuildingSiteOperator {
    public static final int MESSAGE_ADD_TITLE=1;
    public static final int MESSAGE_ADD_MESSAGE=0;
    public static final int MESSAGE_ADD_INNERTEXT=2;
    public static final int MESSAGE_NEW_MESSAGE=0;
    public static final int MESSAGE_NEW_INNERTEXT=1;

    private JList list;
	private DefaultListModel model=null;
	private JButton btnOK;
	private JButton btnCancel;
	private DialogResult dialogResult=DialogResult.RESULT_CANCEL;

	private Object[] selectedValue=null;//选中的数据
	private JButton btnNew;
	private JButton btnAdd;
	private JButton btnDel;

	private Chooser chooser;//选择器自定义模型

    private void initComponent(String title,String toolTip){
        setTitle(title);
        getContentPane().setLayout(null);
        setSize(450,533);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel lblctrl = new JLabel(toolTip);
        lblctrl.setForeground(SystemColor.textHighlight);
        lblctrl.setFont(new Font("幼圆", Font.PLAIN, 19));
        lblctrl.setBounds(10, 10, 404, 26);
        getContentPane().add(lblctrl);
        getContentPane().setBackground(Color.white);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 46, 356, 404);
        getContentPane().add(scrollPane);

        list = new JList();
        list.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        list.setFixedCellHeight(30);
        scrollPane.setViewportView(list);
        model=new DefaultListModel();
        list.setModel(model);

        btnCancel = new JButton("取消");
        btnCancel.setFont(new Font("幼圆", Font.PLAIN, 15));
        btnCancel.setBounds(113, 460, 93, 34);
        getContentPane().add(btnCancel);

        btnOK = new JButton("确定");
        btnOK.setFont(new Font("幼圆", Font.PLAIN, 15));
        btnOK.setBounds(10, 460, 93, 34);
        getContentPane().add(btnOK);
        
        btnAdd = new JButton("加");
        btnAdd.setFont(new Font("幼圆", Font.PLAIN, 15));
        btnAdd.setBounds(376, 98, 56, 42);
        getContentPane().add(btnAdd);
        
        btnDel = new JButton("减");
        btnDel.setFont(new Font("幼圆", Font.PLAIN, 15));
        btnDel.setBounds(376, 150, 56, 42);
        getContentPane().add(btnDel);
        
        btnNew = new JButton("新");
        btnNew.setFont(new Font("幼圆", Font.PLAIN, 15));
        btnNew.setBounds(376, 46, 56, 42);
        getContentPane().add(btnNew);
    }

    private void initEvent(){
        btnOK.addActionListener((e)->{
            dialogResult=DialogResult.RESULT_OK;
            selectedValue=model.toArray();
            if (chooser!=null)
                chooser.done(AnUtils.toStringArray(selectedValue));
            dispose();
        });

        btnCancel.addActionListener((e)->{
            dispose();
        });

        btnAdd.addActionListener((e)->{
            if (chooser==null)
                return;
            String site= (String) JOptionPane.showInputDialog(
                    this,
                    chooser.getAddText()[MESSAGE_ADD_MESSAGE],
                    chooser.getAddText()[MESSAGE_ADD_TITLE],JOptionPane.PLAIN_MESSAGE,
                    null,chooser.addEvent(),
                    chooser.getAddText()[MESSAGE_ADD_INNERTEXT]);
            if (site!=null&&!site.equals(""))
                add(site);
        });

        btnDel.addActionListener((e)->{
            model.removeElement(list.getSelectedValue());
        });

        btnNew.addActionListener((e)->{
            if (chooser==null)
                return;
            String string=JOptionPane.showInputDialog(this,chooser.getNewText()[MESSAGE_NEW_MESSAGE],chooser.getNewText()[MESSAGE_NEW_INNERTEXT]);
            if (string!=null&&!string.equals("")&&!string.equals(chooser.getNewText()[MESSAGE_NEW_INNERTEXT])){

                if (chooser.newEvent(AnUtils.toStringArray(model.toArray()),string))
                    add(string);
            }else Application.informationWindow("请输入正确的值");
        });
    }

    /**
     * 构造一个自定义标题，自定义内容提示文本，自定义所有事件，并且输入固定集合的选择器
     * @param title 标题
     * @param toolTip 提示文本
     * @param chooser 选择器
     */
    public AnDataChooser(String title, String toolTip, Chooser chooser,boolean addButtonEnable,Object[] source){
        initComponent(title,toolTip);
        initEvent();
        setSource(source);
        setAddButtonEnable(addButtonEnable);
        this.chooser=chooser;
        setModal(true);
        setVisible(true);
    }


    public void setSource(Object[] objects){
        if (model==null)
            model=new DefaultListModel();
        model.clear();
        for (Object o:objects)
            model.addElement(o);
    }

    /**
     * 设置选择器事件自定义器
     * @param chooser
     */
    public void setChooser(Chooser chooser){
        this.chooser=chooser;
    }

    private boolean contains(String value){
        return model.contains(value);
    }

    private void add(String value){
        if (model==null)
            model=new DefaultListModel();
        if (!contains(value))
            model.addElement(value);
        list.revalidate();
    }

    public void setAddButtonEnable(boolean enable){
        btnAdd.setEnabled(enable);
    }

    Object[] getSelectedValues(){
        return selectedValue;
    }

    @Override
    public Object[] getValues(){
        return model.toArray();
    }

    @Override
    public Component getComponent() {
        return this;
    }

}
