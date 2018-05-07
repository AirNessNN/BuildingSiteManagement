package application;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import dbManager.DateValueInfo;
import resource.Resource;

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

			Vector v=new Vector();
			for (Object object:objects){
				v.add(object);
			}
			tmpData.add(v);
		}
		return tmpData;
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
		c.set(year,month,day);
		return c.getTime();
	}

	/**
	 * 给定两个日期，比较年份月份日期是否相等
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isDateYMDEquality(Date d1,Date d2){
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
}
