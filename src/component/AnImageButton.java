package component;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class AnImageButton extends JLabel{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	//各种状态的Image
	private ImageIcon normal,press,enter, disabled;

	private AnActionListener actionListener=null;

	
	private MouseAdapter mouseAdapter=new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			if(!isEnabled())
				return;
			setIcon(press);
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			if(!isEnabled())
				return;
			setIcon(enter);
		}
		@Override
		public void mouseExited(MouseEvent e) {
			if(!isEnabled())
				return;
			setIcon(normal);
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if(!isEnabled())
				return;
			setIcon(enter);
			Dimension dimension=getSize();
			Point point=e.getPoint();
			if (!((point.x<0||point.y<0)||(point.x>dimension.width||point.y>dimension.height))){
				if(actionListener!=null)
					actionListener.actionPerformed(new AnActionEvent(AnImageButton.this, AnActionEvent.CILCKED, AnImageButton.this.getText()));
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
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

	public void setDisabledImage(ImageIcon disabled){
		this.disabled=disabled;
	}


	

	public void setActionListener(AnActionListener I){
		actionListener=I;
	}

	public void removeActionListener(){
		actionListener=null;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if(disabled!=null)
			if(!enabled)
				setIcon(disabled);
			else
				setIcon(normal);
	}
}
