package component;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import resource.Resource;

/**
 * An图片标签
 * @author Dell
 *
 */
public class AnImageLabel extends JLabel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;
	
	
	
	public AnImageLabel(String src) {
		// TODO Auto-generated constructor stub
		try {
			image=ImageIO.read(Resource.getResource(src));
			this.setSize(image.getWidth(), image.getHeight());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			image=null;
		}
	}
	
	public AnImageLabel(BufferedImage image) {
		this.setSize(image.getWidth(), image.getHeight());
		this.image=image;
	}
	
	
	public void setImage(BufferedImage image) {
		this.image=image;
		repaint();
	}
	
	
	
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		if(image==null)
			return;
		g.drawImage(image, 0, 0, this.getWidth(),this.getHeight(),null);
	}

}
