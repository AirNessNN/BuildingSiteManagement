package component;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.*;

import application.AnUtils;

public class AnLabel extends JLabel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//复制功能
	private void addMenu(){
		JPopupMenu menu=new JPopupMenu();
		JMenuItem item=new JMenuItem("复制");
		menu.add(item);

		item.addActionListener((e)->{
			Clipboard clipboard=Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection stringSelection=new StringSelection(getText());
			clipboard.setContents(stringSelection,null);
		});
		this.setComponentPopupMenu(menu);
	}

	public AnLabel(String text) {
		// TODO Auto-generated constructor stub
		super(text);
		setFont(new Font("微软雅黑", 0, 15));
		setSize(AnUtils.getStringPx(text, getFont()));
		addMenu();
	}
	
	public AnLabel() {
		// TODO Auto-generated constructor stub
		setFont(new Font("微软雅黑", Font.PLAIN, 15));
		addMenu();
	}
}
