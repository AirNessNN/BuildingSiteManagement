package component;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;

import resource.Resource;

public class AnTextButton extends JLabel implements MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//事件监听器
	private AnActionListener actionListener=null;
	public void addActionListener(AnActionListener I) {
		actionListener=I;
	}
	public void removeActionListener() {
		actionListener=null;
	}

	public AnTextButton(String text) {
		// TODO Auto-generated constructor stub
		setFont(new Font(Resource.FONT_WEI_RUAN_YA_HEI, Font.PLAIN, 14));
		setForeground(Resource.COLOR_LIGHT_BLUE);
		addMouseListener(this);
		setText(text);
		setSize(application.AnUtils.getStringPx(text, getFont()));
	}
	
	
	public int getFontSize() {
		return getFont().getSize();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(actionListener!=null) {
			actionListener.actionPerformed(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		setCursor(Cursor.getDefaultCursor());
	}

	
}
