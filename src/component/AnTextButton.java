package component;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;

import resource.Resource;

@SuppressWarnings("serial")
public class AnTextButton extends JLabel implements MouseListener ,IBackgroundSetting{
	
	private boolean handFlag=true;
	private Color normalColor =null;
	private Color enteredColor=null;
	private Color pressedColor=null;
	private Color releasedColor=null;
	private Color exitedColor=null;
	
	//事件监听器
	private AnActionListener actionListener=null;
	public void setActionListener(AnActionListener I) {
		actionListener=I;
	}
	
	public AnTextButton() {
		// TODO Auto-generated constructor stub
		setSize(60,25);
		setFont(new Font(Resource.FONT_WEI_RUAN_YA_HEI, Font.PLAIN, 14));
		setForeground(Resource.COLOR_LIGHT_BLUE);
		addMouseListener(this);
	}

	public AnTextButton(String text) {
		// TODO Auto-generated constructor stub
		setSize(60,25);
		setFont(new Font(Resource.FONT_WEI_RUAN_YA_HEI, Font.PLAIN, 14));
		setForeground(Resource.COLOR_LIGHT_BLUE);
		addMouseListener(this);
		setText(text);
	}
	
	
	public int getFontSize() {
		return getFont().getSize();
	}
	
	public void setHandCursor(boolean b) {
		handFlag=b;
	}

	/**
	 * 设置正常颜色
	 * @param normalColor
	 */
	public void setNormalColor(Color normalColor) {
		this.normalColor = normalColor;
		setBackground(normalColor);
	}

	@Override
	public void setEnteredColor(Color color) {
		enteredColor=color;
	}

	@Override
	public void setPressedColor(Color color) {
		pressedColor=color;
	}

	@Override
	public void setReleasedColor(Color color) {
		releasedColor=color;
	}

	@Override
	public void setExitedColor(Color color) {
		exitedColor=color;
	}

	public Color getNormalColor() {
		return normalColor;
	}

	@Override
	public Color getEnteredColor() {
		return enteredColor;
	}

	@Override
	public Color getPressedColor() {
		return pressedColor;
	}

	@Override
	public Color getReleasedColor() {
		return releasedColor;
	}

	@Override
	public Color getExitedColor() {
		return exitedColor;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(actionListener!=null) {
			actionListener.actionPerformed(new AnActionEvent(this, AnActionEvent.CILCKED, this.getText()));
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if (actionListener!=null) {
			actionListener.actionPerformed(new AnActionEvent(this, AnActionEvent.PRESSED, this.getText()));
		}
		if (pressedColor!=null)
			setBackground(pressedColor);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if (actionListener!=null) {
			actionListener.actionPerformed(new AnActionEvent(this, AnActionEvent.RELEASEED, this.getText()));
		}
		if (releasedColor!=null)
			setBackground(releasedColor);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		if (actionListener!=null) {
			actionListener.actionPerformed(new AnActionEvent(this, AnActionEvent.ENTERED, this.getText()));
		}
		if(handFlag)
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		if (enteredColor!=null)
			setBackground(enteredColor);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		if (actionListener!=null) {
			actionListener.actionPerformed(new AnActionEvent(this, AnActionEvent.EXITED, this.getText()));
		}
		if(handFlag)
			setCursor(Cursor.getDefaultCursor());
		if (exitedColor!=null)
			setBackground(exitedColor);
	}

	
}
