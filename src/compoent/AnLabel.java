package compoent;

import java.awt.Font;

import javax.swing.JLabel;

import application.AnUtils;

public class AnLabel extends JLabel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AnLabel(String text) {
		// TODO Auto-generated constructor stub
		super(text);
		setFont(new Font("Î¢ÈíÑÅºÚ", 0, 18));
		setSize(AnUtils.getStringPx(text, getFont()));
	}
	
	public AnLabel() {
		// TODO Auto-generated constructor stub
		setFont(new Font("Î¢ÈíÑÅºÚ", 0, 18));
	}
}
