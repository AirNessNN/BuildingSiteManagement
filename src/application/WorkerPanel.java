package application;

import compoent.AnLabel;
import compoent.AnTextField;
import compoent.ImagePanel;
import dbManager.Anbean;
import dbManager.User;
import resource.Resource;
import java.awt.Color;
import javax.swing.*;
import java.awt.Font;
import javax.swing.border.EmptyBorder;
import SwingTool.MyButton;


public class WorkerPanel extends ImagePanel implements Loadable{

	private User user=null;
	private AnTextField textField;







    private void initView(){
        this.setSize(934,771);
        setIcon(AnUtils.getImageIcon(Resource.getResource("workpanel.png")));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
        scrollPane.setBounds(10, 114, 357, 647);
        add(scrollPane);
        
        AnLabel title = new AnLabel("工人管理");
        title.setBounds(10, 10, 175, 52);
        title.setFont(new Font("方正兰亭超细黑简体", Font.PLAIN, 40));
        title.setForeground(Color.WHITE);
        add(title);
        
        textField = new AnTextField();
        textField.setText("输入名字或身份证信息查找");
        textField.setBounds(10, 83, 271, 23);
        add(textField);
        
        textField.setColumns(10);
        
        MyButton btnNewButton = new MyButton("\u67E5\u627E");
        btnNewButton.setBounds(291, 83, 76, 23);
        add(btnNewButton);
        
        JButton button = new JButton("\u6253\u5370\u8868");
        button.setBounds(480, 84, 76, 23);
        add(button);
        
        JButton btnNewButton_1 = new JButton("\u589E\u52A0\u5DE5\u4EBA");
        btnNewButton_1.setBounds(377, 84, 93, 23);
        add(btnNewButton_1);
        
        JButton btnNewButton_2 = new JButton("\u5165\u804C\u767B\u8BB0");
        btnNewButton_2.setBounds(566, 84, 93, 23);
        add(btnNewButton_2);
        
        JButton button_1 = new JButton("\u79BB\u804C\u767B\u8BB0");
        button_1.setBounds(669, 84, 93, 23);
        add(button_1);

        
    }

    private void initEvent(){

    }

    public void initData(){

    }

    public WorkerPanel(){
        initView();
        initEvent();
        initData();
    }

    @Override
    public void loading(Object data) {
        if(this.user==null){
            this.user=(User) data;
        }
    }
}
