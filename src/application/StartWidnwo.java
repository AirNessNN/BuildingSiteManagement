package application;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JFrame;

import compoent.AnLabel;
import resource.Resource;

/**
 * 启动欢迎窗口窗口
 * @author Dell
 *
 */
public class StartWidnwo extends JFrame{
	
	private AnLabel label=null;
	private static final long serialVersionUID = 1L;

	public StartWidnwo() {
		setUndecorated(true);
		setSize(600, 400);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		
		label=new AnLabel(null);
		label.setBounds(38, 141, 77, 24);
		getContentPane().add(label);
		setText("初始化");
	}
	
	
	
	
	
	
	public void setText(String text) {
		label.setText(text);
		repaint();
	}
	
	
	public void closeWindow() {
		this.dispose();
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(AnUtils.getImage(Resource.getResource("欢迎界面.png")), 0, 0, null);
		Graphics2D g2d=(Graphics2D)g;
		g2d.setColor(Color.white);
		g2d.setFont(new Font(Resource.FONT_WEI_RUAN_YA_HEI, 0, 18));
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//绘制状态文字
		g2d.drawString(label.getText(), 30, 180);
		//绘制版本号
		g2d.setFont(new Font(Resource.FONT_WEI_RUAN_YA_HEI, 0, 15));
		g2d.drawString(Application.VERSION, 540, 380);
	}

}
