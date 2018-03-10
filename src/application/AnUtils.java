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

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import resource.Resource;

/**
 * An���߰�
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
	
	
	public static BufferedImage getImage(String src) {
			
			try {
				File file=new File(src);
				BufferedImage image=ImageIO.read(file);
				return image;
			}catch (Exception e) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(null, e.getMessage(),"����",JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	public static BufferedImage getImage(File file) {
		try {
			BufferedImage image=ImageIO.read(file);
			return image;
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, e.getMessage(),"����",JOptionPane.ERROR_MESSAGE);
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
	 * ��ȡImageIconͼ��
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
	 * ����ı���Ⱦ�����ش�С
	 * @param text
	 * @param font
	 * @return
	 */
	public static Dimension getStringPx(String text,Font font) {
		
		//�������Size
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
	 * �����л���
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
	 * �������л�
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
			default:
				break;
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}