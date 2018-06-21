package component;

import application.AnUtils;
import dbManager.DateValueInfo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;

/**
 * <h2>带数据标记的日历</h2>
 * <li>每一个日期都可以存放一个值，这个日期控件可以根据值的颜色显示不同的颜色</li>
 */
public class AnDataCalendar extends JPanel {

	boolean isEnabled=true;

    /**
     * AnDateValuePanel的数据对比回调
     */
    public interface IValueColorSetCallback {
        int valueOf(Object value);
    }

	/**
	 *  点击日期的值设置回调
	 */
	public interface IValueCallback{
    	Object setObject(Object value);
	}

	private AnTextButton btnPreviousMonth;
	private AnTextButton btnPreviousYear;
	private JLabel labDataTitle;
	private AnTextButton btnNextYear;
	private AnTextButton btnNextMonth;
	private JPanel panel_0;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
	private JPanel panel_4;
	private JPanel panel_5;

	private AnActionListener listener=null;

	private AnTextButton[] dates;
	private ArrayList<IDateValueItem> source=null;
	private int minValue=0;
	private int maxValue=100;

	// 日期控件开始下标和结束下标
	private int startIndex = 0;
	private int endIndex = 0;

	// 内建日期选择器的日期
	private Date date = null;

	//回调
    private IValueColorSetCallback callback=null;
    private IValueCallback valueCallback=null;

	private void initComponent() {
		setSize(654, 589);
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);

		JPanel titlePanel = new JPanel();
		springLayout.putConstraint(SpringLayout.SOUTH, titlePanel, 80, SpringLayout.NORTH, this);
		titlePanel.setBackground(new Color(143, 188, 143));
		springLayout.putConstraint(SpringLayout.WEST, titlePanel, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, titlePanel, 0, SpringLayout.EAST, this);
		add(titlePanel);
		titlePanel.setLayout(new GridLayout(0, 7, 0, 0));

		JLabel label = new JLabel("\u5468\u65E5");
		label.setFont(new Font("微软雅黑 Light", Font.BOLD, 16));
		label.setForeground(new Color(255, 255, 255));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(label);

		JLabel label_1 = new JLabel("\u5468\u4E00");
		label_1.setFont(new Font("微软雅黑 Light", Font.BOLD, 16));
		label_1.setForeground(new Color(255, 255, 255));
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(label_1);

		JLabel label_2 = new JLabel("\u5468\u4E8C");
		label_2.setFont(new Font("微软雅黑 Light", Font.BOLD, 16));
		label_2.setForeground(new Color(255, 255, 255));
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(label_2);

		JLabel label_3 = new JLabel("\u5468\u4E09");
		label_3.setFont(new Font("微软雅黑 Light", Font.BOLD, 16));
		label_3.setForeground(new Color(255, 255, 255));
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(label_3);

		JLabel label_4 = new JLabel("\u5468\u56DB");
		label_4.setFont(new Font("微软雅黑 Light", Font.BOLD, 16));
		label_4.setForeground(new Color(255, 255, 255));
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(label_4);

		JLabel label_5 = new JLabel("\u5468\u4E94");
		label_5.setFont(new Font("微软雅黑 Light", Font.BOLD, 16));
		label_5.setForeground(new Color(255, 255, 255));
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(label_5);

		JLabel label_6 = new JLabel("\u5468\u516D");
		label_6.setFont(new Font("微软雅黑 Light", Font.BOLD, 16));
		label_6.setForeground(new Color(255, 255, 255));
		label_6.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(label_6);

		JPanel valuePanel = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, valuePanel, 0, SpringLayout.SOUTH, titlePanel);
		springLayout.putConstraint(SpringLayout.WEST, valuePanel, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, valuePanel, 0, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, valuePanel, 0, SpringLayout.EAST, this);
		add(valuePanel);
		valuePanel.setLayout(new GridLayout(6, 0, 0, 0));

		panel_0 = new JPanel();
		valuePanel.add(panel_0);
		panel_0.setLayout(new GridLayout(0, 7, 0, 0));

		panel_1 = new JPanel();
		valuePanel.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 7, 0, 0));

		panel_2 = new JPanel();
		valuePanel.add(panel_2);
		panel_2.setLayout(new GridLayout(0, 7, 0, 0));

		panel_3 = new JPanel();
		valuePanel.add(panel_3);
		panel_3.setLayout(new GridLayout(0, 7, 0, 0));

		panel_4 = new JPanel();
		valuePanel.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 7, 0, 0));
		
		panel_5 = new JPanel();
		valuePanel.add(panel_5);
		panel_5.setLayout(new GridLayout(0, 7, 0, 0));

		JPanel controlPanel = new JPanel();
		controlPanel.setBackground(new Color(143, 188, 143));
		springLayout.putConstraint(SpringLayout.NORTH, titlePanel, 0, SpringLayout.SOUTH, controlPanel);
		springLayout.putConstraint(SpringLayout.NORTH, controlPanel, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, controlPanel, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, controlPanel, 40, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, controlPanel, 0, SpringLayout.EAST, this);
		add(controlPanel);
		SpringLayout sl_controlPanel = new SpringLayout();
		controlPanel.setLayout(sl_controlPanel);

		btnPreviousMonth = new AnTextButton("< \u4E0A\u6708");
		btnPreviousMonth.setBackground(new Color(143, 188, 143));
		sl_controlPanel.putConstraint(SpringLayout.EAST, btnPreviousMonth, 70, SpringLayout.WEST, controlPanel);
		btnPreviousMonth.setForeground(new Color(255, 255, 255));
		btnPreviousMonth.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		btnPreviousMonth.setHorizontalAlignment(SwingConstants.CENTER);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, btnPreviousMonth, 0, SpringLayout.NORTH, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.WEST, btnPreviousMonth, 0, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.SOUTH, btnPreviousMonth, 0, SpringLayout.SOUTH, controlPanel);
		controlPanel.add(btnPreviousMonth);
		btnPreviousMonth.setOpaque(true);


		btnNextMonth = new AnTextButton("\u4E0B\u6708 >");
		btnNextMonth.setBackground(new Color(143, 188, 143));
		sl_controlPanel.putConstraint(SpringLayout.WEST, btnNextMonth, -70, SpringLayout.EAST, controlPanel);
		btnNextMonth.setForeground(new Color(255, 255, 255));
		btnNextMonth.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		sl_controlPanel.putConstraint(SpringLayout.NORTH, btnNextMonth, 0, SpringLayout.NORTH, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.SOUTH, btnNextMonth, 0, SpringLayout.SOUTH, controlPanel);
		btnNextMonth.setHorizontalAlignment(SwingConstants.CENTER);
		sl_controlPanel.putConstraint(SpringLayout.EAST, btnNextMonth, 0, SpringLayout.EAST, controlPanel);
		controlPanel.add(btnNextMonth);
		btnNextMonth.setOpaque(true);


		btnPreviousYear = new AnTextButton("\u524D\u5E74");
		btnPreviousYear.setBackground(new Color(143, 188, 143));
		sl_controlPanel.putConstraint(SpringLayout.EAST, btnPreviousYear, 140, SpringLayout.WEST, controlPanel);
		btnPreviousYear.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		btnPreviousYear.setForeground(new Color(255, 255, 255));
		btnPreviousYear.setHorizontalAlignment(SwingConstants.CENTER);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, btnPreviousYear, 0, SpringLayout.NORTH, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.WEST, btnPreviousYear, 0, SpringLayout.EAST, btnPreviousMonth);
		sl_controlPanel.putConstraint(SpringLayout.SOUTH, btnPreviousYear, 0, SpringLayout.SOUTH, controlPanel);
		controlPanel.add(btnPreviousYear);
		btnPreviousYear.setOpaque(true);


		btnNextYear = new AnTextButton("\u4E0B\u5E74");
		btnNextYear.setBackground(new Color(143, 188, 143));
		sl_controlPanel.putConstraint(SpringLayout.WEST, btnNextYear, -140, SpringLayout.EAST, controlPanel);
		btnNextYear.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		btnNextYear.setForeground(new Color(255, 255, 255));
		btnNextYear.setHorizontalAlignment(SwingConstants.CENTER);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, btnNextYear, 0, SpringLayout.NORTH, btnPreviousMonth);
		sl_controlPanel.putConstraint(SpringLayout.SOUTH, btnNextYear, 0, SpringLayout.SOUTH, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, btnNextYear, 0, SpringLayout.WEST, btnNextMonth);
		controlPanel.add(btnNextYear);
		btnNextYear.setOpaque(true);


		labDataTitle = new JLabel("");
		labDataTitle.setForeground(new Color(255, 255, 255));
		labDataTitle.setFont(new Font("微软雅黑", Font.BOLD, 18));
		labDataTitle.setHorizontalAlignment(SwingConstants.CENTER);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, labDataTitle, 0, SpringLayout.NORTH, btnPreviousMonth);
		sl_controlPanel.putConstraint(SpringLayout.WEST, labDataTitle, 0, SpringLayout.EAST, btnPreviousYear);
		sl_controlPanel.putConstraint(SpringLayout.SOUTH, labDataTitle, 0, SpringLayout.SOUTH, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, labDataTitle, 0, SpringLayout.WEST, btnNextYear);
		controlPanel.add(labDataTitle);
	}

	private void initData() {
		// 填充控件
		int index = 0;
		dates = new AnTextButton[42];

		JPanel[] panels = new JPanel[6];
		panels[0] = panel_0;
		panels[1] = panel_1;
		panels[2] = panel_2;
		panels[3] = panel_3;
		panels[4] = panel_4;
		panels[5] = panel_5;

		Font font = new Font("微软雅黑", Font.PLAIN, 15);
		for (int i = 0; i < 6; i++) {
			panels[i].removeAll();
			for (int j = 0; j < 7; index++, j++) {
				dates[index] = new AnTextButton();
				dates[index].setFont(font);
				dates[index].setHorizontalAlignment(SwingConstants.CENTER);
				dates[index].setForeground(Color.black);
				dates[index].setOpaque(true);
				dates[index].setHandCursor(false);
				dates[index].setBackground(Color.WHITE);
				panels[i].add(dates[index]);
				if (isEnabled)
					dates[index].setActionListener((e)->{
						action(e);
					});
			}
			//System.gc();//清理上次填充的控件
		}
	}

	private void initEvent(){
		btnPreviousMonth.setActionListener((e)->{
			switch (e.getAction()) {
				case AnActionEvent.CILCKED:
					Calendar c=Calendar.getInstance();
					c.setTime(date);
					c.add(Calendar.MONTH,-1);
					date=new Date();
					date=null;
					date=c.getTime();
					c.setTime(date);
					setDateComponentLocation();
					break;
				case AnActionEvent.ENTERED:
					btnPreviousMonth.setBackground(AnColor.DARK_GREEN);
					break;
				case AnActionEvent.PRESSED:
					btnPreviousMonth.setForeground(Color.GRAY);
					break;
				case AnActionEvent.RELEASEED:
					btnPreviousMonth.setForeground(Color.white);
					break;
				case AnActionEvent.EXITED:
					btnPreviousMonth.setBackground(AnColor.LIGHT__GREEN);
					break;
			}
			repaint();
		});

		btnNextMonth.setActionListener((e)->{
			switch (e.getAction()) {
				case AnActionEvent.CILCKED:
					Calendar c=Calendar.getInstance();
					c.setTime(date);
					c.add(Calendar.MONTH,1);
					date=new Date();
					date=null;
					date=c.getTime();
					c.setTime(date);
					setDateComponentLocation();
					break;
				case AnActionEvent.ENTERED:
					btnNextMonth.setBackground(AnColor.DARK_GREEN);
					break;
				case AnActionEvent.PRESSED:
					btnNextMonth.setForeground(Color.GRAY);
					break;
				case AnActionEvent.RELEASEED:
					btnNextMonth.setForeground(Color.white);
					break;
				case AnActionEvent.EXITED:
					btnNextMonth.setBackground(AnColor.LIGHT__GREEN);
					break;
			}
			repaint();
		});

		btnNextYear.setActionListener((e)->{
			switch (e.getAction()) {
				case AnActionEvent.CILCKED:
					Calendar c=Calendar.getInstance();
					c.setTime(date);
					c.add(Calendar.YEAR,1);
					date=new Date();
					date=null;
					date=c.getTime();
					c.setTime(date);

					setDateComponentLocation();
					break;
				case AnActionEvent.ENTERED:
					btnNextYear.setBackground(AnColor.DARK_GREEN);
					break;
				case AnActionEvent.PRESSED:
					btnNextYear.setForeground(Color.GRAY);
					break;
				case AnActionEvent.RELEASEED:
					btnNextYear.setForeground(Color.white);
					break;
				case AnActionEvent.EXITED:
					btnNextYear.setBackground(AnColor.LIGHT__GREEN);
					break;
			}
			repaint();
		});

		btnPreviousYear.setActionListener((e)->{
			switch (e.getAction()) {
				case AnActionEvent.CILCKED:
					Calendar c=Calendar.getInstance();
					c.setTime(date);
					c.add(Calendar.YEAR,-1);
					date=new Date();
					date=null;
					date=c.getTime();
					c.setTime(date);
					setDateComponentLocation();
					break;
				case AnActionEvent.ENTERED:
					btnPreviousYear.setBackground(AnColor.DARK_GREEN);
					break;
				case AnActionEvent.PRESSED:
					btnPreviousYear.setForeground(Color.GRAY);
					break;
				case AnActionEvent.RELEASEED:
					btnPreviousYear.setForeground(Color.white);
					break;
				case AnActionEvent.EXITED:
					btnPreviousYear.setBackground(AnColor.LIGHT__GREEN);
					break;
			}
			repaint();
		});
	}

	private void setDateComponentLocation() {
		initData();
		//设置日期标题
		if (date==null)
			date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
		labDataTitle.setText(sdf.format(date));

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		int index=c.get(Calendar.DAY_OF_WEEK)-1;
		int month=c.get(Calendar.MONTH)+1;
		int year=c.get(Calendar.YEAR);

		//填充控件数据
		startIndex=index;//设置开始标志

		int tmpIndex=1;//日期开始遍历的值
		for(int i = index; i< dates.length; i++,tmpIndex++) {
			if(month==1||month==3||month==5||month==7||month==8||month==10||month==12) {
				if(tmpIndex>31)
					break;
			}else if(month==2&&((year%4==0&&year%100!=0)||year%400==0)) {
				if(tmpIndex>29)
					break;
			}else if(month==2&&!((year%4==0&&year%100!=0)||year%400==0)) {
				if(tmpIndex>28)
					break;
			}else
				if(tmpIndex>30)
					break;
			dates[i].setText(String.valueOf(tmpIndex));
			//按数据设置颜色
			if (source!=null&&callback!=null){
				for (IDateValueItem item:source){
				    Date date=item.getDate();
				    Calendar calendar=Calendar.getInstance();
				    calendar.setTime(date);
                    int tmpYear=calendar.get(Calendar.YEAR);
                    int tmpMonth=calendar.get(Calendar.MONTH)+1;
                    int tmpDay=calendar.get(Calendar.DATE);
                    //日期相等
                    if (tmpYear==year&&tmpMonth==month&&tmpDay==tmpIndex){
                        //调用接口
                        dates[i].setNormalColor(getColor(callback.valueOf(item.getValue())));
                        dates[i].setToolTipText(item.getValue().toString());//设置文本
                        break;
                    }
				}
			}
		}
		endIndex=startIndex+tmpIndex-2;//设置endindex
		repaint();
	}

	public AnDataCalendar() {
		initComponent();
		initEvent();
		setDateComponentLocation();
	}

	/**
	 * 设置日历中日期的监听事件，
	 * 事件源是AnTextButton
	 * @param l
	 */
	public void setActionListener(AnActionListener l){
		listener=l;
	}

	public void setSourceDates(ArrayList<IDateValueItem> sourceDates){
		this.source=sourceDates;
        setDateComponentLocation();
	}

	public void setSourceDates(IDateValueItem[] sourceDates){
		source=new ArrayList<>();
		source.addAll(Arrays.asList(sourceDates));
		setDateComponentLocation();
	}

	public ArrayList<IDateValueItem> getSource() {
		return source;
	}

	public Color getColor(float value){
		if (value==-1f)
			return new Color(255,255,255);
		if (value<minValue)
			value=minValue;
		if (value>maxValue)
			value=maxValue;
		if (maxValue==0)
			maxValue=1;

		//算出数值区间的百分比
		float percent= (value/maxValue);
		float v1=(float) 190*percent;//取得颜色的值

		if (v1<108){
			//G变动 114-222
            return new Color(222, (int)(114+v1),114);
		}else {
			//R变动 222-140
            return new Color(222-((int) v1-108), 222,114);
		}

	}

	/**
	 * 根据日期返回值
	 * @param date
	 * @return
	 */
	public Object getValueFromDate(Date date){
		if (source!=null){
			for (IDateValueItem info:source){
				if (AnUtils.isDateYMDEquality(date,info.getDate())){
					return info.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * 设置该日期的值，如果没找到该日期就创建
	 * @param date
	 * @param value
	 */
	public void setValueFromDate(Date date,Object value){
		if (source==null)
			source=new ArrayList<>();
		if (source!=null){
			for (IDateValueItem item:source){
				if (AnUtils.isDateYMDEquality(date,item.getDate())){
					item.setValue(value);
					return;
				}
			}
			DateValueInfo info=new DateValueInfo(date,value,null);
			source.add(info);
		}
	}

	public void setMinValue(int min){
	    minValue=min;
    }

    public void setMaxValue(int max){
	    maxValue=max;
    }

    public void setParam(IValueColorSetCallback callback){
	    this.callback=callback;
    }
    public void setValueCallback(IValueCallback callback){
		this.valueCallback=callback;
	}

	public void setEnabled(boolean enabled){
		isEnabled=enabled;
		if (enabled){
			for (AnTextButton textButton:dates){
				if (textButton==null||actionListener==null)continue;
				textButton.setActionListener(actionListener);
			}
		}else{
			for (AnTextButton textButton:dates){
				if (textButton==null||actionListener==null)continue;
				textButton.setActionListener(null);
			}
		}
	}


	void action(AnActionEvent e){
		AnTextButton tb;
		//获取当前日期
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		int year=c.get(Calendar.YEAR);
		int month=c.get(Calendar.MONTH);
		int day;
		Date tmpd;
		SimpleDateFormat s;
		String stringDate="";
		if (!e.getTag().equals("")){
			//设置点击的日期
			day=Integer.valueOf(e.getTag());
			tmpd=new Date();
			c.setTime(tmpd);
			c.set(year,month,day);
			tmpd=c.getTime();
			//格式化日期
			s=new SimpleDateFormat("yyyy-MM-dd");
			stringDate=s.format(tmpd);
		}
		switch (e.getAction()){
			case AnActionEvent.CILCKED:
				if (valueCallback!=null){
					try {
						Date date=AnUtils.getDate(stringDate,"yyy-MM-dd");
						//判断返回的值是不是无效
						Object value=valueCallback.setObject(getValueFromDate(date));
						 if (value!=null){
							 setValueFromDate(date,value);
							 setDateComponentLocation();
						 }
					} catch (ParseException e1) {
					}
				}
				if (listener!=null){
					listener.actionPerformed(new AnActionEvent(e.getSource(),AnActionEvent.CILCKED,stringDate));
				}
				break;
			case AnActionEvent.ENTERED:
				tb= (AnTextButton) e.getSource();
				if (e.getTag().equals(""))
					break;
				tb.setBackground(Color.lightGray);
				if (listener!=null)
					listener.actionPerformed(new AnActionEvent(e.getSource(),AnActionEvent.ENTERED,stringDate));
				break;
			case AnActionEvent.EXITED:
				tb= (AnTextButton) e.getSource();
				if (e.getTag().equals(""))
					break;
				if (tb.getNormalColor()==null)
					tb.setBackground(Color.white);
				else
					tb.setBackground(tb.getNormalColor());
				if (listener!=null)
					listener.actionPerformed(new AnActionEvent(e.getSource(),AnActionEvent.EXITED,stringDate));
				break;
			case AnActionEvent.PRESSED:
				if (listener!=null)
					listener.actionPerformed(new AnActionEvent(e.getSource(),AnActionEvent.PRESSED,stringDate));
				break;
			case AnActionEvent.RELEASEED:
				if (listener!=null)
					listener.actionPerformed(new AnActionEvent(e.getSource(),AnActionEvent.RELEASEED,stringDate));
				break;
		}
	}

	AnActionListener actionListener=new AnActionListener() {
		@Override
		public void actionPerformed(AnActionEvent event) {
			action(event);
		}
	};
}
