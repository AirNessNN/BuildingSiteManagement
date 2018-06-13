package component;

import application.AnUtils;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.Font;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AnDateComboPanel extends JPanel{
	private JSpinner year;
	private JSpinner month;
	private JSpinner day;
	private Date minDate;//最小约束
	private Date maxDate;//最大约束

    private Date oldDate;//旧数据;

    private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");//格式化器

    private ChangeListener changeListener=(e)->{
    };

    public AnDateComboPanel(){
        setSize(297,60);

        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);
        
        year = new JSpinner();
        springLayout.putConstraint(SpringLayout.NORTH, year, 15, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, year, 10, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, year, -15, SpringLayout.SOUTH, this);
        springLayout.putConstraint(SpringLayout.EAST, year, -211, SpringLayout.EAST, this);
        year.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        year.setModel(new SpinnerNumberModel(2018, 1900, 3000, 1));
        add(year);
        year.addChangeListener(changeListener);
        
        JLabel label = new JLabel("年");
        springLayout.putConstraint(SpringLayout.NORTH, label, 23, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.EAST, year);
        springLayout.putConstraint(SpringLayout.SOUTH, label, -20, SpringLayout.SOUTH, this);
        springLayout.putConstraint(SpringLayout.EAST, label, -178, SpringLayout.EAST, this);
        label.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        add(label);
        
        month = new JSpinner();
        month.setModel(new SpinnerNumberModel(1, 1, 12, 1));
        springLayout.putConstraint(SpringLayout.NORTH, month, 15, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, month, 20, SpringLayout.WEST, label);
        springLayout.putConstraint(SpringLayout.SOUTH, month, -15, SpringLayout.SOUTH, this);
        springLayout.putConstraint(SpringLayout.EAST, month, -120, SpringLayout.EAST, this);
        add(month);
        month.addChangeListener(changeListener);

        
        JLabel label_1 = new JLabel("月");
        springLayout.putConstraint(SpringLayout.NORTH, label_1, 23, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, label_1, 6, SpringLayout.EAST, month);
        springLayout.putConstraint(SpringLayout.SOUTH, label_1, -20, SpringLayout.SOUTH, this);
        springLayout.putConstraint(SpringLayout.EAST, label_1, -100, SpringLayout.EAST, this);
        label_1.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        add(label_1);
        
        day = new JSpinner();
        day.setModel(new SpinnerNumberModel(1, 1, 31, 1));
        springLayout.putConstraint(SpringLayout.NORTH, day, 15, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, day, 0, SpringLayout.EAST, label_1);
        springLayout.putConstraint(SpringLayout.SOUTH, day, -15, SpringLayout.SOUTH, this);
        springLayout.putConstraint(SpringLayout.EAST, day, -30, SpringLayout.EAST, this);
        add(day);
        day.addChangeListener(changeListener);
        day.addChangeListener(e -> {

            int value= (int) year.getValue();
            if ((value%4==0 && value%100!=0)||value%400==0){
                if ((int)month.getValue()==2){
                    if ((int)day.getValue()>29){
                        day.setValue(29);
                    }
                    if ((int)month.getValue()==4){
                        if ((int)day.getValue()>30){
                            day.setValue(30);
                        }
                    }
                    if ((int)month.getValue()==6){
                        if ((int)day.getValue()>30){
                            day.setValue(30);
                        }
                    }
                    if ((int)month.getValue()==9){
                        if ((int)day.getValue()>30){
                            day.setValue(30);
                        }
                    }
                    if ((int)month.getValue()==11){
                        if ((int)day.getValue()>30){
                            day.setValue(30);
                        }
                    }
                }
            }else{
                if ((int)month.getValue()==2){
                    if ((int)day.getValue()>28){
                        day.setValue(28);
                    }
                }
                if ((int)month.getValue()==4){
                    if ((int)day.getValue()>30){
                        day.setValue(30);
                    }
                }
                if ((int)month.getValue()==6){
                    if ((int)day.getValue()>30){
                        day.setValue(30);
                    }
                }
                if ((int)month.getValue()==9){
                    if ((int)day.getValue()>30){
                        day.setValue(30);
                    }
                }
                if ((int)month.getValue()==11){
                    if ((int)day.getValue()>30){
                        day.setValue(30);
                    }
                }
            }
        });

        JLabel label_2 = new JLabel("日");
        springLayout.putConstraint(SpringLayout.NORTH, label_2, 23, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, label_2, 6, SpringLayout.EAST, day);
        springLayout.putConstraint(SpringLayout.SOUTH, label_2, -20, SpringLayout.SOUTH, this);
        springLayout.putConstraint(SpringLayout.EAST, label_2, -10, SpringLayout.EAST, this);
        label_2.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        add(label_2);

        //设置日期
        Date date=new Date();
        setDate(date);
        oldDate=date;
    }

    public AnDateComboPanel(Date date){
        this();
        setDate(date);
        oldDate=date;
    }

    public void setDate(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        year.setValue(calendar.get(Calendar.YEAR));
        month.setValue(calendar.get(Calendar.MONTH)+1);
        day.setValue(calendar.get(Calendar.DATE));
    }

    /**
     * 设置区间
     */
    public void setSection(Date min,Date max){
        minDate=min;
        maxDate=max;
    }

    /**
     * 重置时间选择器
     */
    public void Rest(){
        setDate(new Date());
    }

    @Deprecated
    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(maxDate);

        SpinnerNumberModel model= (SpinnerNumberModel) year.getModel();
        model.setMaximum(calendar.get(Calendar.YEAR));
    }

    @Deprecated
    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    @Override
    public String toString() {
        Date date;
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try {
            date=dateFormat.parse(year.getValue()+"-"+month.getValue()+"-"+day.getValue());
        } catch (ParseException e) {
            Date date1=new Date();
            return dateFormat.format(date1);
        }
        return dateFormat.format(date);
    }
}
