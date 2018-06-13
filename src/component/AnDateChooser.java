package component;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 轻量的日期选择器
 */
public class AnDateChooser extends JDialog {

        AnDateComboPanel comboPanel;

        Date date=null;
        DialogResult dialogResult=DialogResult.RESULT_CANCEL;

	public AnDateChooser() {
		setResizable(false);
		setTitle("日期选择器");
	    setSize(328,137);
	    setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        comboPanel=new AnDateComboPanel();
        comboPanel.setBounds(0, 0, 322, 61);
        getContentPane().add(comboPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JButton button = new JButton("取消");
        button.setBounds(172, 71, 93, 25);
        getContentPane().add(button);
        button.addActionListener((e)->{
            dispose();
        });
        
        JButton button_1 = new JButton("确定");
        button_1.setBounds(69, 71, 93, 25);
        getContentPane().add(button_1);
        button_1.addActionListener((e)->{
            dialogResult=DialogResult.RESULT_OK;
            setModal(false);
            dispose();
        });

        setModal(true);
        setVisible(true);
	}

    /**
     * 获取选择的日期
     * @return
     */
	public Date getDate(){
	    if (dialogResult==DialogResult.RESULT_CANCEL)
	        return null;
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sf.parse(comboPanel.toString());
        } catch (ParseException e) {
            return null;
        }
    }

    public String getDateFormate(){
	    if (dialogResult==DialogResult.RESULT_CANCEL)
	        return null;
	    return comboPanel.toString();
    }


    @Deprecated
    public void setMaxDate(Date date){
	    comboPanel.setMaxDate(date);
    }

    @Deprecated
    public void setMinDate(Date date){
	    comboPanel.setMinDate(date);
    }


}
