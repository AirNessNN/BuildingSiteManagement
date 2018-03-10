package compoent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class AnImageButton extends JLabel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//¸÷ÖÖ×´Ì¬µÄImage
	private ImageIcon normal,press,enter;
	
	MouseAdapter mouseAdapter=new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			super.mousePressed(e);
			setIcon(press);
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			super.mouseEntered(e);
			setIcon(enter);
		}
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			super.mouseExited(e);
			setIcon(normal);
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			super.mouseReleased(e);
			setIcon(enter);
		}
	};
	
	public AnImageButton() {
		// TODO Auto-generated constructor stub
		this.setIcon(normal);
		this.addMouseListener(mouseAdapter);
	}
	
	public AnImageButton(String toolTip) {
		this.setToolTipText(toolTip);
		this.setIcon(normal);
		this.addMouseListener(mouseAdapter);
	}
	
	public AnImageButton(ImageIcon normal,ImageIcon press,ImageIcon enter) {
		setImage(normal, press, enter);
		this.addMouseListener(mouseAdapter);
	}
	
	
	public void setImage(ImageIcon normal,ImageIcon press,ImageIcon enter) {
		this.normal=normal;
		this.press=press;
		this.enter=enter;
		if(normal!=null) {
			this.setSize(normal.getIconWidth(), normal.getIconHeight());
		}
		setIcon(normal);
	}
	
	
	
	
	
	
}
