package component;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.util.Date;

import application.AnInfoListDataModel;
import resource.Resource;

public class AnInfoCellRenderer extends JLabel implements ListCellRenderer<AnInfoListDataModel>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ImageIcon normalImg,selectedImg;
	private Dimension size=null;
	private Font titleFont=new Font("微软雅黑 Light", Font.PLAIN, 23);
	private Font InfoFont=new Font("微软雅黑", Font.PLAIN, 15);
	
	
	

	public ImageIcon getNormalImg() {
		return normalImg;
	}

	public void setNormalImg(ImageIcon normalImg) {
		this.normalImg = normalImg;
	}


	public ImageIcon getSelectedImg() {
		return selectedImg;
	}

	public void setSelectedImg(ImageIcon selectedImg) {
		this.selectedImg = selectedImg;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}






	
	
	private boolean selected=false;
	
	
	
	public AnInfoCellRenderer() {
		
		// TODO Auto-generated constructor stub
		
	}
	
	
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		/*if(isSelected()) {
			g.setColor(Color.RED);
		}else {
			g.setColor(Color.BLACK);
		}
		g.drawString(this.getText(), 10, 0);*/
		super.paint(g);
	}
	

	@Override
	public Component getListCellRendererComponent(JList<? extends AnInfoListDataModel> list, AnInfoListDataModel value,
			int index, boolean isSelected, boolean cellHasFocus) {
		// TODO Auto-generated method stub
		//this.setText(value.getTitle());
		setSelected(isSelected);
		int w=list.getFixedCellWidth();
		int h=list.getFixedCellHeight();
		int timeW=(int)(w-80);
		
		//创建图片
		BufferedImage image=new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d=image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//绘制背景
		if(isSelected) {
			g2d.setColor(Resource.COLOR_LIGHT_BLUE);
		}else {
			g2d.setColor(Color.WHITE);
		}
		g2d.fillRect(0, 0, w, h);
		
		//绘制元素
		g2d.setColor(Color.black);
		g2d.setFont(titleFont);
		g2d.drawString(value.getTitle(), 10, 28);
		
		
		g2d.setColor(Color.darkGray);
		g2d.setFont(InfoFont);
		g2d.drawString(value.getInfo(), 10, 47);

		g2d.setColor(Color.DARK_GRAY);
		g2d.setFont(InfoFont);
		g2d.drawString(DateFormat.getDateInstance(DateFormat.DEFAULT).format(new Date()),timeW,30);
		
		ImageIcon icon=new ImageIcon(image);
		setIcon(icon);
		
		return this;
	}
}
