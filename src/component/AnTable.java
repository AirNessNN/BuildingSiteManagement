package component;

import application.AnUtils;
import resource.Resource;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * An表单，可以轻松设置表单填充数据
 */
public class AnTable extends JTable{

	private DefaultTableModel listModel=null;//默认列表数据模型

	private ArrayList<Point> editPassList=null;//不可编辑的表格

	private Vector<Vector> data=null;//元数据

	private Vector header=null;//表头缓存

	private Vector<AnTableCellEditor> components =null;//显示的控件列表



	private void init(){

		getTableHeader().setFont(new Font("微软雅黑",1,14));
		this.setRowHeight(30);
		this.setFont(Resource.FONT_TABLE_ITEM);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setFillsViewportHeight(true);

		header=new Vector();
		data=new Vector<>();
		editPassList=new ArrayList<>();

		//默认listMod
		listModel=new DefaultTableModel(){
			@Override
			public boolean isCellEditable(int row, int column) {
				for (Point point : editPassList){
					if (row==point.x&&column==point.y){
						return false;
					}
				}
				return true;
			}
		};

		this.setModel(listModel);
	}

	public AnTable(){
		init();
	}


	/**
	 * 设置标头和表头名字
	 * @param columnNames
	 */
	public void setColumn(Object[] columnNames){
		listModel.setColumnIdentifiers(columnNames);
		for (Object object:columnNames){
			this.header.add(object);
		}
	}

	/**
	 * 添加表头
	 * @param object
	 */
	public void addColumn(Object object){
		header.add(object);
		listModel.setColumnIdentifiers(header.toArray());
	}

	/**
	 * 填充数据
	 * @param data
	 */
	public void fillData(Object[][] data){
		listModel.setDataVector(data,header.toArray());
		Vector<Vector> tmpData= AnUtils.conventToVector(data);

		for (Vector<Object> tv:tmpData){
			Vector<Object> t=new Vector();
			this.data.add(t);

			for (Object object:t){
				java.lang.String string=new java.lang.String(object.toString());
				t.add(string);
			}
		}
	}



	public void setCellEdited(int row, int col, boolean editable){
		Point p=new Point(row,col);
		for (Point point:editPassList){
			if (p.equals(point)){
				if (editable){
					//可以编辑
					editPassList.remove(point);
					return;
				}
			}
		}
		if (!editable){
			editPassList.add(p);
		}
	}

	public void setCellRowEdited(int row ,boolean editable){
		if (!editable){
			for (int i=0;i<header.size();i++){
				Point p=new Point(row,i);
				if (!editPassList.contains(p)){
					editPassList.add(p);
				}
			}
		}else {
			for (int i=0;i<header.size();i++){
				Point p=new Point(row,i);
				if (editPassList.contains(p)){
					editPassList.remove(p);
				}
			}
		}
	}

	public void setCellColumnEdited(int column,boolean editable){
		if (!editable){
			for (int i=0;i<data.size();i++){
				Point p=new Point(i,column);
				if (!editPassList.contains(p)){
					editPassList.add(p);
				}
			}
		}else {
			for (int i=0;i<data.size();i++){
				Point p=new Point(i,column);
				if (editPassList.contains(p)){
					editPassList.remove(p);
				}
			}
		}
	}
	/**
	 * 增加一个AnTableCellEditor接口的控件到Cell中
	 * @param component 控件
	 * @param row 行
	 * @param column 列
	 */
	public void addComponentCell(AnTableCellEditor component, int row, int column){
		component.setTableCellLocation(row,column);
		if (components==null)
			components=new Vector<>();
		components.add(component);
	}


	/**
	 * 移除控件,并且自动清除控件的CellLocation
	 * @param component
	 */
	public void removeComponentCell(AnTableCellEditor component){
		if (components==null)
			return;
		components.remove(component);
		component.setTableCellLocation(-1,-1);
	}

	public void removeComponentCellAt(int row, int column){
		if (components==null)
			return;
		for (AnTableCellEditor editor:components){
			if (row==editor.getTableCellLocation().x&&column==editor.getTableCellLocation().y){
				components.remove(editor);
			}
		}
	}




	public DefaultTableModel getListModel() {
		return listModel;
	}

	@Override
	public TableCellEditor getCellEditor(int row, int column) {
		// TODO Auto-generated method stub
		if (components==null)
			return super.getCellEditor(row,column);
		for (AnTableCellEditor editor:components){
			int r=editor.getTableCellLocation().x;
			int c=editor.getTableCellLocation().y;
			if (r==row&&c==column)
				return editor;
		}
		return super.getCellEditor(row, column);
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		return super.getCellRenderer(row, column);
	}
}
