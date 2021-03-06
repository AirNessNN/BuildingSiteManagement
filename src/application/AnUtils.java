package application;
import dbManager.ExcelFile;
import org.omg.SendingContext.RunTime;
import resource.Resource;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * An工具包
 * @author AN
 *
 */
public class AnUtils {
	
	public static final int LOOK_AND_FEEL_WINDOW=1;
	public static final int LOOK_AND_FEEL_METAL=2;
	public static final int LOOK_AND_FEEL_CLASSIC=3;
	public static final int LOOK_AND_FEEL_DEFAULT=4;
	public static final int LOOK_AND_FEEL_CrossPlatform=5;
	public static final int LOOK_AND_FEEL_MOTIF=6;
	public static final int LOOK_AND_FEEL_NIMBUS=7;
	
	
	public static BufferedImage getImage(String src) {
			
			try {
				File file=new File(src);
				BufferedImage image=ImageIO.read(file);
				return image;
			}catch (Exception e) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(null, e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	public static BufferedImage getImage(File file) {
		try {
			BufferedImage image=ImageIO.read(file);
			return image;
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	public static BufferedImage getImage(InputStream in) {
		try {
			BufferedImage image=ImageIO.read(in);
			return image;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取ImageIcon图标
	 * @param src
	 * @return
	 */
	public static ImageIcon getImageIcon(String src) {
		try {
			Image image=ImageIO.read(Resource.getResource(src));
			ImageIcon imageIcon=new ImageIcon(image);
			return imageIcon;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	
	public static ImageIcon getImageIcon(File file) {
		try {
			Image image=ImageIO.read(file);
			ImageIcon imageIcon=new ImageIcon(image);
			return imageIcon;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	public static ImageIcon getImageIcon(InputStream resource) {
		// TODO Auto-generated method stub
		try {
			ImageIcon imageIcon=new ImageIcon(AnUtils.getImage(resource));
			return imageIcon;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获得文本渲染的像素大小
	 * @param text
	 * @param font
	 * @return
	 */
	public static Dimension getStringPx(String text,Font font) {
		
		//计算组件Size
		int enc=0,chc=0;
		if(text==null) {
			return new Dimension(0, 0);
		}
		for(int i=0;i<text.length();i++) {
			char c=text.charAt(i);
			if(c>0&&c<128) {
				enc++;
			}else {
				chc++;
			}
		}
		int width=(chc*font.getSize())+(enc*(font.getSize()/2+2));
		int height=font.getSize();
		
		return new Dimension(width, height);
	}
	
	/**
	 * 反序列化器
	 * @param bytes
	 * @return
	 */
	public static Object toObject (byte[] bytes) {      
        Object obj = null;      
        try {        
            ByteArrayInputStream bis = new ByteArrayInputStream (bytes);        
            ObjectInputStream ois = new ObjectInputStream (bis);        
            obj = ois.readObject();      
            ois.close();
            bis.close();
        } catch (IOException ex) {        
            ex.printStackTrace();   
        } catch (ClassNotFoundException ex) {        
            ex.printStackTrace();   
        }      
        return obj;    
    }   
	
	
	/**
	 * 对象序列化
	 * @param obj
	 * @return
	 */
	 public static  byte[] toByteArray (Object obj) {      
	        byte[] bytes = null;      
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();      
	        try {        
	            ObjectOutputStream oos = new ObjectOutputStream(bos);         
	            oos.writeObject(obj);        
	            oos.flush();         
	            bytes = bos.toByteArray ();      
	            oos.close();         
	            bos.close();        
	        } catch (IOException ex) {        
	            ex.printStackTrace();   
	        }      
	        return bytes;    
	    }   
	
	public static void setLookAndFeel(int mod) {
		try {
			String lookAndFeel=null;
			switch (mod) {
			case 1:
				lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
				UIManager.setLookAndFeel(lookAndFeel);
				break;
			case 2:
				lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";
				UIManager.setLookAndFeel(lookAndFeel);
				break;
			case 3:
				lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";
				UIManager.setLookAndFeel(lookAndFeel);
				break;
			case 4:
				lookAndFeel = UIManager.getSystemLookAndFeelClassName();
				UIManager.setLookAndFeel(lookAndFeel);
				break;
			case 5:
				lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
				UIManager.setLookAndFeel(lookAndFeel);
				break;
			case 6:
				lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
				UIManager.setLookAndFeel(lookAndFeel);
				break;
			case 7:
				lookAndFeel ="com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
				UIManager.setLookAndFeel(lookAndFeel);
			default:
				break;
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Log打印
	 * @param className 调用的类对象
	 * @param object 输入的对象
	 */
	public static void log(Object className,Object object){
		System.out.println(className.getClass().getName()+"："+object);
	}

	/**
	 * 将数组转成二维向量
	 * @param data
	 * @return
	 */
	public static Vector<Vector>conventToVector(Object[][] data){
		Vector<Vector> tmpData=new Vector<>();
		for (Object[] objects:data){
			Vector v=convertToVector(objects);
			tmpData.add(v);
		}
		return tmpData;
	}

	public static Vector convertToVector(Object[] data){
		Vector vector=new Vector();
		for (Object o:data){
			vector.add(o);
		}
		return vector;
	}
	/**
	 * 将二维向量转为二维数组
	 * @param data
	 * @return
	 */
	public static Object[][] conventToArrays(Vector<Vector> data){
		int length=0;
		for (Vector v:data){
			if (v.size()>length)
				length=v.size();
		}

		Object[][] tmp=new Object[data.size()][length];

		for (int i=0;i<data.size();i++){
			for (int j=0;j<length;j++){
				try {
					tmp[i][j]=data.get(i).get(j);
				}catch (IndexOutOfBoundsException e){
					continue;
				}
			}
		}
		return tmp;
	}


    /**
     * 检查对象的类型
     * @param object
     * @param name
     * @return
     */
	public static boolean isObjectContains(Object object,String name){
        return object.getClass().getName().contains(name);
    }

	/**
	 * 获取自定义的日期
	 * @param year 年份
	 * @param month 月份
	 * @param day 天
	 * @return 返回指定日期但不指定具体小时分钟的Date
	 */
    public static Date getDate(int year,int month,int day){
		Calendar c=Calendar.getInstance();
		c.setTime(new Date());
		c.set(year,month-1,day);
		return c.getTime();
	}

	/**
	 * 将字符串转换为时间日期
	 * @param date
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date getDate(String date,String format) throws ParseException {
		SimpleDateFormat sf=new SimpleDateFormat(format);
		return sf.parse(date);
	}
	/**
	 * 给定两个日期，比较年份月份日期是否相等
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isDateYMDEquality(Date d1,Date d2){
		if (d1==null||d2==null)
			return false;
    	int d1y,d2y,d1m,d2m,d1d,d2d;
    	Calendar c=Calendar.getInstance();
    	c.setTime(d1);
    	d1y=c.get(Calendar.YEAR);
    	d1m=c.get(Calendar.MONTH);
    	d1d=c.get(Calendar.DATE);

    	c.setTime(d2);
    	d2y=c.get(Calendar.YEAR);
    	d2m=c.get(Calendar.MONTH);
    	d2d=c.get(Calendar.DATE);
    	return d1y==d2y&&d1m==d2m&&d1d==d2d;
	}

	/**
	 * 判断两个时间类是否月份日期相同
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isDateMDEquality(Date d1,Date d2){
		if (d1==null||d2==null)
			return false;
		int d1m,d2m,d1d,d2d;
		Calendar c=Calendar.getInstance();
		c.setTime(d1);

		d1m=c.get(Calendar.MONTH);
		d1d=c.get(Calendar.DATE);

		c.setTime(d2);

		d2m=c.get(Calendar.MONTH);
		d2d=c.get(Calendar.DATE);
		return d1m==d2m&&d1d==d2d;
	}

	/**
	 * 比较两个时间，获得更晚的时间
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static Date getMaxDate(Date d1,Date d2){

		if (!d1.before(d2)){
			return d1;
		}else
			return d2;
	}


	/**
	 * 将身份证号码转换为出生年月日
	 * @param id 身份证
	 * @return 日期
	 */
	public static Date convertBornDate(String id){

		if (id==null)
			return null;
		if (id.length()<18||id.length()>18)
			return null;

		char[] tmpArr=id.toCharArray();
		StringBuilder sb=new StringBuilder();
		for (int i=6;i<10;i++){
			sb.append(tmpArr[i]);
		}
		int year=Integer.valueOf(sb.toString());
		sb=new StringBuilder();
		for (int i=10;i<12;i++){
			sb.append(tmpArr[i]);
		}
		int month=Integer.valueOf(sb.toString());
		sb=new StringBuilder();
		for (int i=12;i<14;i++){
			sb.append(tmpArr[i]);
		}
		int day=Integer.valueOf(sb.toString());
		return getDate(year,month,day);
}

	/**
	 * 提取身份证中的年龄
	 * @param id 身份证
	 * @return 返回年龄
	 */
	public static int convertAge(String id){
		if (id==null)
			return 0;
		int year;
		int month;
		int day;
		Date date=convertBornDate(id);
		if (date==null)
			return 0;
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		year=c.get(Calendar.YEAR);
		month=c.get(Calendar.MONTH+1);
		day=c.get(Calendar.DATE);

		int tyear,tmonth,tday;
		Date thisDay=new Date();
		c.setTime(thisDay);
		tyear=c.get(Calendar.YEAR);
		tmonth=c.get(Calendar.MONTH);
		tday=c.get(Calendar.DATE);

		int age=tyear-year;
		if (tmonth>month&&tday>day)
			age++;

		return age;
	}

	/**
	 * 判断是否是身份证
	 * @param id
	 * @return
	 */
	public static boolean isIDCard(String id){
		if (id.length()!=18)
			return false;
		id.toLowerCase();

		int[] y=new int[]{7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};//系数
		char[] result=new char[]{'1','0','x','9','8','7','6','5','4','3','2'};//计算对应的结果
        int sum=0;

        char[] tmpC=id.substring(0,17).toCharArray();
        for (int i=0;i<tmpC.length;i++) {
            sum+= ((int) tmpC[i]-48) *y[i];
        }

        int mod=sum%11;//模出来的结果
        char rv=id.toCharArray()[17];//最后一个数

        if (rv==result[mod])
            return true;
		return false;
	}

	public static String getID(String id){
		int[] y=new int[]{7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};//系数
		char[] result=new char[]{'1','0','x','9','8','7','6','5','4','3','2'};//计算对应的结果
		int sum=0;

		char[] tmpC=id.toCharArray();
		for (int i=0;i<tmpC.length;i++) {
			sum+= ((int) tmpC[i]-48) *y[i];
		}

		int mod=sum%11;//模出来的结果

		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append(tmpC);
		stringBuilder.append(result[mod]);
		return stringBuilder.toString();
	}

	@Deprecated
	public static Component getParentFrame(Component component){
	    for (Component c=component;c.getParent()!=null;c=c.getParent()){
            System.out.println(c.getClass().getName()+" "+c.getName()+"  size:"+c.getSize().toString());

	        if (c.getClass().getName().contains("JFrame")){
	            return c;
            }
        }
        return null;
    }

	/**
	 * 将集合转换为数组
	 * @param collection
	 * @return
	 */
	public static Object[] toArray(Collection collection){
		if (collection==null)
			return null;
		Object[] array=new Object[collection.size()];
		int index=0;
		for (Object o:collection){
			array[index++]=o;
		}
		return array;
	}


	/**
	 * 将Object数组转换为String数组，前提是Objec数组可转换成String数组
	 * @param objects
	 * @return
	 */
	public static String[] toStringArray(Object[] objects){
		if (objects.length>0&&objects[0] instanceof String){
			String[] strings=new String[objects.length];
			for (int i=0;i<strings.length;i++){
				strings[i]= (String) objects[i];
			}
			return strings;
		}
		return null;
	}

	/**
	 * 比较D1和D2的日期
	 * @param d1 日期1
	 * @param d2 日期2
	 * @return 1表示日期1更大，2表示日期2更大，0表示等于
	 */
	public static int dateCompareInt(Date d1,Date d2){
		if (d1==null)
			return 2;
		if (d2==null)
			return 1;

		Calendar cd1=Calendar.getInstance();
		cd1.setTime(d1);

		Calendar cd2=Calendar.getInstance();
		cd2.setTime(d2);

		/*

		如果d1年份比d2大或者小，后面直接不用比较，只有等于的需要比较月份和日期，月份相等同理年份
		 */
		if (cd1.get(Calendar.YEAR)==cd2.get(Calendar.YEAR)){
			if (cd1.get(Calendar.MONTH)==cd2.get(Calendar.MONTH)){
				if (cd1.get(Calendar.DATE)==cd2.get(Calendar.DATE))
					return 0;
				else return cd1.get(Calendar.DATE)>cd2.get(Calendar.DATE)? 1:2;
			}else
				return cd1.get(Calendar.MONTH)>cd2.get(Calendar.MONTH)? 1:2;
		}else
			return cd1.get(Calendar.YEAR)>cd2.get(Calendar.YEAR)? 1:2;
	}

	/**
	 * 比较D1在D2的后面
	 * @param d1 日期1
	 * @param d2 日期2
	 * @return true D1在D2的后面
	 */
	public static boolean dateAfter(Date d1,Date d2){
		return dateCompareInt(d1,d2)==1;
	}

	public static Object[] convertObjectArray(String[] values){
		Object[] objects=new Object[values.length];
		for (int i=0;i<values.length;i++){
			objects[i]=values[i];
		}
		return objects;
	}


	private static SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");

	public static String formateDate(Date date){
	    if (date==null)return "";
		return dateFormat.format(date);
	}

	/**
	 * 打印数据到Excel中
	 * @param filePath 文件名
	 * @param datas 数据源
	 */
	public static boolean printToExcel(String filePath,Vector<Vector> datas){
		ExcelFile excelFile=new ExcelFile();
		excelFile.createWorkbook(filePath);
		return excelFile.fillData(datas);
	}

	/**
	 * 显示文件保存窗口，并且将文件打印到Exce中
	 * @param component 父组件
	 * @param datas 数据源
	 */
	public static boolean showPrintWindow(Component component,Vector<Vector> datas){
		JFileChooser chooser=new JFileChooser();
		FileNameExtensionFilter filter=new FileNameExtensionFilter("*.xlsx","xlsx");
		chooser.setFileFilter(filter);
		chooser.showSaveDialog(component);
		if (chooser.getSelectedFile()==null)return false;
		String selectedFile=chooser.getSelectedFile().getAbsolutePath();
		if (selectedFile.equals(""))return false;
		if (!selectedFile.contains(".xls")){
			selectedFile+=".xlsx";
		}
		return printToExcel(selectedFile,datas);
	}


	/**
	 * 打开一个路径，可以是文件可以是文件夹
	 * @param cmdPath 路径URL
	 * @return 成功返回true
	 */
	public static boolean open(String cmdPath){
		String[] strings=new String[5];
		strings[0]="cmd";
		strings[1]="/c";
		strings[2]="start";
		strings[3]=" ";
		strings[4]=cmdPath;
		new Thread(()->{
			try {
				Runtime.getRuntime().exec(strings);
			} catch (IOException e) {
				AnUtils.log(Runtime.getRuntime(),e.toString());
			}
		}).start();
		return true;
	}


	/**
	 * 获取两个日期之间的所有日期，不包括此区间的开始结束两个日期
	 * @param start 开始日期
	 * @param end 结束日期
	 * @return 日期数组
	 */
	public static Date[] getSectionDates(Date start,Date end){
		if (start==null||end==null)return null;

		ArrayList<Date> result=new ArrayList<>();
		Calendar tempStart = Calendar.getInstance();
		tempStart.setTime(start);
		tempStart.add(Calendar.DAY_OF_YEAR, 1);

		Calendar tempEnd = Calendar.getInstance();
		tempEnd.setTime(end);
		while (tempStart.before(tempEnd)) {
			result.add(tempStart.getTime());
			tempStart.add(Calendar.DAY_OF_YEAR, 1);
		}
		Date[] dates=new Date[result.size()];
		for (int i=0;i<dates.length;i++){
			dates[i]=result.get(i);
		}
		return dates;
	}

}
