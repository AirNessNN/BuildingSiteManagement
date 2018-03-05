package Main;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import Resoure.Resource;

public class Util {
	
	public static BufferedImage getImage(String src) {
			
			try {
				File file=new File(src);
				BufferedImage image=ImageIO.read(file);
				return image;
			}catch (Exception e) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(null, e.getMessage(),"¥ÌŒÛ",JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	public static BufferedImage getImage(File file) {
		try {
			BufferedImage image=ImageIO.read(file);
			return image;
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, e.getMessage(),"¥ÌŒÛ",JOptionPane.ERROR_MESSAGE);
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
	 * ªÒ»°ImageIconÕº±Í
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
			ImageIcon imageIcon=new ImageIcon(Util.getImage(resource));
			return imageIcon;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	
	
}
