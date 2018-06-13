package application;

import SwingTool.MyButton;
import component.AnDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * <h2>横向填充器</h2>
 * <li>用一个键集合填充每个键包含的数据，在填充结束时以回调的方式将集合用ArrayList < String >的方式返回 </li>
 * <li>使用之前先要设置一个主键，然后设置需要填充的数据：addComponent等于添加一个与key对应的String数组</li>
 * @see ArrayList
 * @see Callback
 */
public class HorizonDataFiller extends JDialog{
    /**
     * <h2>数据回调</h2>
     * 在填充完成时系统会回调此接口的方法，将每一个String[]数组打包成一个ArrayList
     * @see ArrayList
     */
    public interface Callback{
        boolean callback(ArrayList<String[]> values);
    }
    //按键事件
    KeyListener listener=new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getKeyChar()=='\n')
                next();
        }
    };
    private Callback callback=null;
	private JButton btnCancel;
	private JButton btnNext;

	private String[] keys;
	private ArrayList<String[]> values=new ArrayList<>();

	private ArrayList<Component> components=null;
	private ArrayList<JLabel> labels=null;

    private int index=0;
    private JComboBox cobKeys;

    private Dimension comSize =new Dimension(215,25);
    private Dimension labSize=new Dimension(80,25);
    private int margin=35;
    private int lineHeight =50+25+10;
    private int comX=100;
    private int labX=10;
    private JLabel labKey;



    private void initializeComponent(String title,String toolTip){
        setResizable(false);
        setTitle(title);
        getContentPane().setLayout(null);

        btnNext = new JButton("下一步");
        btnNext.setFont(new Font("等线", Font.PLAIN, 15));
        btnNext.addActionListener(e -> next());
        btnNext.setBounds(221, 105, 93, 28);
        getContentPane().add(btnNext);

        btnCancel = new JButton("取消");
        btnCancel.setFont(new Font("等线", Font.PLAIN, 15));
        btnCancel.addActionListener(e -> dispose());
        btnCancel.setBounds(118, 105, 93, 28);
        getContentPane().add(btnCancel);

        labKey = new JLabel("主键：");
        labKey.setFont(new Font("等线", Font.PLAIN, 15));
        labKey.setHorizontalAlignment(SwingConstants.RIGHT);
        labKey.setBounds(10, 50, 79, 23);
        getContentPane().add(labKey);

        JLabel lblNewLabel = new JLabel(toolTip);
        lblNewLabel.setForeground(SystemColor.textHighlight);
        lblNewLabel.setFont(new Font("幼圆", Font.PLAIN, 20));
        lblNewLabel.setBounds(10, 10, 304, 30);
        getContentPane().add(lblNewLabel);

        cobKeys = new JComboBox();
        cobKeys.addActionListener(e ->{
            index= cobKeys.getSelectedIndex();
            if (index!=keys.length-1){
                btnNext.setText("下一个");
            }else {
                btnNext.setText("完成");
            }
        });
        cobKeys.setFont(new Font("等线", Font.PLAIN, 15));
        cobKeys.setBounds(100, 50, 215, 25);
        getContentPane().add(cobKeys);


        setSize(330,171);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initializeData(String[] siteName){
        this.keys =siteName;
        components=new ArrayList<>();
        labels=new ArrayList<>();
    }

    /**
     * 构造一个空的没有主键的横向数据填充器
     */
	public HorizonDataFiller() {
        initializeComponent("选择器","请依次输入数据");
        initializeData(null);
	}

    /**
     * 构造一个初始化好的横向数据填充器
     * @param title 标题
     * @param toolTip 提示文字
     * @param keys 主键
     */
	public HorizonDataFiller(String title, String toolTip, String[] keys){
        initializeComponent(title,toolTip);
        initializeData(null);
        setKeys("主键",keys);
    }


	private void next(){
        index= cobKeys.getSelectedIndex();
        //填充数组
        try{
            for (int i=0;i<values.size();i++){
                String[] tmp=values.get(i);
                if (components.get(i) instanceof JTextField)
                    tmp[index]= ((JTextField) components.get(i)).getText();
                else if(components.get(i) instanceof MyButton)
                    tmp[index]=((MyButton)components.get(i)).getText();
                else
                    tmp[index]=Objects.requireNonNull(((JComboBox) components.get(i)).getSelectedItem()).toString();
            }
            //判断是不是最后一个数据
            if (index== keys.length-1){
                //完成
                if (callback!=null)
                    callback.callback(values);
                this.dispose();
                return;
            }
            //下一个数据
            //判断是否是最后一个的前一个
            if (index== keys.length-2){
                btnNext.setText("完成");
            }
            cobKeys.setSelectedIndex(++index);
        }catch (Exception ex){
            Application.informationWindow(ex.getMessage());
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    /**
     * 像容器中添加一个文本框
     * @param toolTip 提示文字
     */
    public void addTextBox(String toolTip){
        JTextField textField=new JTextField();
        textField.setFont(new Font("等线", Font.PLAIN, 15));
        textField.addKeyListener(listener);
        textField.setSize(comSize);
        textField.setLocation(comX, lineHeight);
        getContentPane().add(textField);
        components.add(textField);
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField tx= (JTextField) e.getSource();
                tx.selectAll();
            }
        });

        JLabel label=new JLabel(toolTip+"：");
        label.setFont(new Font("等线", Font.PLAIN, 15));
        label.setSize(labSize);
        label.setLocation(labX, lineHeight);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        getContentPane().add(label);
        label.setToolTipText(toolTip);
        labels.add(label);

        lineHeight +=margin;
        btnNext.setLocation(221,lineHeight+30);
        btnCancel.setLocation(118,lineHeight+30);
        setSize(getWidth(),lineHeight+100);
        setLocationRelativeTo(null);

        index++;

        if (keys!=null)
            this.values.add(new String[keys.length]);
    }

    /**
     * 向容器中添加一个选择框，并初始化选择框的数据
     * @param toolTip 提示文字
     * @param values 数据
     */
    public void addComoBox(String toolTip,String[] values){
        JComboBox comboBox=new JComboBox();
        comboBox.setModel(new DefaultComboBoxModel(values));
        comboBox.setFont(new Font("等线", Font.PLAIN, 15));
        comboBox.addKeyListener(listener);
        comboBox.setSize(comSize);
        comboBox.setLocation(comX, lineHeight);
        getContentPane().add(comboBox);
        components.add(comboBox);
        comboBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                comboBox.setPopupVisible(true);
            }
        });

        JLabel label=new JLabel(toolTip+"：");
        label.setFont(new Font("等线", Font.PLAIN, 15));
        label.setSize(labSize);
        label.setLocation(labX, lineHeight);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        getContentPane().add(label);
        label.setToolTipText(toolTip);
        labels.add(label);

        lineHeight +=margin;
        btnNext.setLocation(221,lineHeight+30);
        btnCancel.setLocation(118,lineHeight+30);
        setSize(getWidth(),lineHeight+100);
        setLocationRelativeTo(null);

        index++;

        if (keys!=null)
            this.values.add(new String[keys.length]);

    }

    public void addCalendar(String toolTip, String initializeText){

        MyButton button=new MyButton(initializeText);
        button.setFont(new Font("等线", Font.PLAIN, 15));
        button.addKeyListener(listener);
        button.setSize(comSize);
        button.setLocation(comX, lineHeight);
        getContentPane().add(button);
        components.add(button);
        button.addActionListener((e)->{
            AnDateChooser chooser1=new AnDateChooser();
            button.setText(chooser1.getDateFormate());
        });


        JLabel label=new JLabel(toolTip+"：");
        label.setFont(new Font("等线", Font.PLAIN, 15));
        label.setSize(labSize);
        label.setLocation(labX, lineHeight);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        getContentPane().add(label);
        label.setToolTipText(toolTip);
        labels.add(label);


        lineHeight +=margin;
        btnNext.setLocation(221,lineHeight+30);
        btnCancel.setLocation(118,lineHeight+30);
        setSize(getWidth(),lineHeight+100);
        setLocationRelativeTo(null);

        index++;

        if (keys!=null)
            this.values.add(new String[keys.length]);
    }

    /**
     * 设置主键
     * @param keys 主键
     */
    public void setKeys(String toolTip,String[] keys){
        labKey.setText(toolTip+"：");
        this.keys=keys;
        cobKeys.setModel(new DefaultComboBoxModel(keys));
        cobKeys.setSelectedIndex(0);
        index=0;

        //填充
        values.clear();
        for (int i=0;i<components.size();i++){
            values.set(i,new String[keys.length]);
        }
    }
}
