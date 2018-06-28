package application;

import javax.swing.*;
import java.awt.Font;
import java.awt.Color;

public class ProgressbarDialog extends JDialog {

    private static ProgressbarDialog dialog;


	private JProgressBar progressBar;
	private JLabel labTitle;
	private JLabel labInfo;

    private ProgressbarDialog(){
    	getContentPane().setBackground(Color.WHITE);
    	getContentPane().setForeground(Color.WHITE);
        setUndecorated(true);
        getContentPane().setLayout(null);
        setSize(452,132);
        setLocationRelativeTo(null);
        
        labTitle = new JLabel("进程正忙");
        labTitle.setFont(new Font("等线", Font.PLAIN, 20));
        labTitle.setBounds(10, 10, 430, 37);
        getContentPane().add(labTitle);
        
        progressBar = new JProgressBar();
        progressBar.setBounds(10, 93, 430, 29);
        getContentPane().add(progressBar);
        
        labInfo = new JLabel("");
        labInfo.setForeground(Color.GRAY);
        labInfo.setFont(new Font("等线", Font.PLAIN, 15));
        labInfo.setBounds(10, 66, 430, 17);
        getContentPane().add(labInfo);
    }

    public static void showDialog(String title,int minValue,int maxValue){
        if (dialog==null)dialog=new ProgressbarDialog();
        if (dialog.isVisible()) dialog.setVisible(false);
        dialog.labTitle.setText(title);
        dialog.progressBar.setMaximum(maxValue);
        dialog.progressBar.setMinimum(minValue);
        dialog.setVisible(true);
    }

    public static void setState(String info,int progress){
        dialog.labInfo.setText(info);
        dialog.progressBar.setValue(progress);
    }
}
