package component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Objects;

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

	private Color color=null;


	private void init(){
		//setSize(100,100);
	}

    /**
     * 无参构造
     */
	public AnImageLabel(){
		setSize(100,100);
		image=new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
	}

	public AnImageLabel(Color color){
		setSize(100,100);
		image=new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
		Graphics graphics=image.createGraphics();
		graphics.setColor(color);
		graphics.fillRect(0,0,getWidth(),getHeight());
	}
	
	public AnImageLabel(InputStream inputStream) {
		init();
		// TODO Auto-generated constructor stub
		try {
			image=ImageIO.read(inputStream);
			this.setSize(image.getWidth(), image.getHeight());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			image=null;
		}
	}
	
	public AnImageLabel(BufferedImage image) {
		init();
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
		if (color!=null){
			Graphics graphics=image.getGraphics();
			graphics.setColor(color);
			graphics.fillRect(0,0,getWidth(),getHeight());
		}
		g.drawImage(image, 0, 0, this.getWidth(),this.getHeight(),null);
	}

}
