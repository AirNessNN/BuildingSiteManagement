package application;

public class AnInfoListDataModel {
	
	private String title,info;
	private boolean isSelected=false;
	
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

	public AnInfoListDataModel(String title, String info) {
		// TODO Auto-generated constructor stub
		this.title=title;
		this.info=info;
		this.isSelected=isSelected;
	}

}
