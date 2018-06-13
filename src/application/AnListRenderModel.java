package application;

import java.util.Date;

public class AnListRenderModel {
	
	private String title,info;
	private boolean isSelected;
	private Date date;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public AnListRenderModel(String title, String info) {
		// TODO Auto-generated constructor stub
		this.title=title;
		this.info=info;
	}

}
