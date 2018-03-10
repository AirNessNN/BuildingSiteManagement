package compoent;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import application.AnUtils;


public class AnImagePanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	
	public AnImagePanel(BufferedImage image) {
		// TODO Auto-generated constructor stub
		if(image!=null) {
			this.image=image;
		}
	}
	
	public AnImagePanel(String src) {
		// TODO Auto-generated constructor stub
		this.image=AnUtils.getImage(src);
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		if(image!=null) {
			g.drawImage(image, 0, 0, null);
		}
	}
}
