package component;

import application.AnUtils;
import resource.Resource;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 * An表单，可以轻松设置表单填充数据
 */
public class AnTable extends JTable{

	private DefaultTableModel listModel=null;//默认列表数据模型

	private ArrayList<Point> editPassList=null;//不可编辑的表格


	/**
	 * 表头
	 */
	private Vector<Object> header=null;

	/**
	 * 列表中可显示的控件
	 */
	private Vector<AnTableCellEditor> components =null;

    /**
     * 行筛选开关
     */
	private boolean rowFiltrateFlag =false;

    /**
     * 列筛选开关
     */
	private boolean columnFiltrateFlag =true;

    /**
     *  行筛选标记
     */
	private ArrayList<Integer> rowEditFiltrate=null;

    /**
     * 列筛选标记
     */
	private ArrayList<Integer> columnEditFiltrate=null;






















	/**
	 * 初始化控件和数据模型
	 */
	private void init(){

		getTableHeader().setFont(new Font("微软雅黑",1,14));
		this.setRowHeight(30);
		this.setFont(Resource.FONT_TABLE_ITEM);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setFillsViewportHeight(true);

		header=new Vector<>();//标题
		editPassList=new ArrayList<>();
		rowEditFiltrate=new ArrayList<>();
		columnEditFiltrate=new ArrayList<>();

		//默认listMod，重写其中的isCellEditable方法，控制单元格的编辑规则
		listModel=new DefaultTableModel(){
			@Override
			public boolean isCellEditable(int row, int column) {
                /*
                先进行行列筛选循环，再进行单元格循环，
                因为行列只需要判断一个值就能断定是否点击了筛选的行，
                在循环中只return false是的意思是找到匹配的就返回false
                 */

				//列循环
                for (Integer aColumnEditFiltrate : columnEditFiltrate) if (column == aColumnEditFiltrate) return false;
                //行循环
                for (Integer aRowEditFiltrate : rowEditFiltrate) if (row == aRowEditFiltrate) return false;
                //单元格循环
                /*
                    相等的话就是存在于筛选单里，所以返回false
                     */
                for (Point point : editPassList)
                    if (row == point.x && column == point.y) return false;
                return true;
			}
		};
		this.setModel(listModel);
	}





	/**
	 * 构造函数
	 */
	public AnTable(){
		init();
	}





	/**
	 * 设置标头和表头名字
	 * @param columnNames 表头数组
	 */
	public void setColumn(Object[] columnNames){
		listModel.setColumnIdentifiers(columnNames);
		this.header.addAll(Arrays.asList(columnNames));
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
	}

	public void fillData(Vector<Vector> data){
		listModel.setDataVector(data,header);
	}




	/**
	 *设置单元格的数据是否可以编辑
	 * @param row 行号
	 * @param col 列号
	 * @param editable 布尔值用于确定是否可以编辑
	 */
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






	/**
	 *设置整行单元格是否可以编辑
	 * @param row 行号
	 * @param editable  用于设置的值
	 */
	public void setCellRowEdited(int row ,boolean editable){
		rowFiltrateFlag =false;
		if (editable) rowEditFiltrate.remove(row);
		else rowEditFiltrate.add(row);
		if (rowEditFiltrate.size()==0) rowFiltrateFlag=true;
	}





	/**
	 * 设置指定列的可编辑状态
	 * @param column 列号
	 * @param editable 设置数值
	 */
	public void setCellColumnEdited(int column,boolean editable){
		columnFiltrateFlag =false;//设置筛选已经打开
        if (editable) columnEditFiltrate.remove(column);
        else columnEditFiltrate.add(column);
        if (columnEditFiltrate.size()==0) columnFiltrateFlag=true;
    }





	/**
	 * 增加一个AnTableCellEditor接口的控件到Cell中
	 * @param component 控件
	 * @param row 行
	 * @param column 列
	 */
	public void addComponentCell(AnTableCellEditor component, int row, int column){
		component.setTableCellLocation(row,column);//设置行列，用来确定显示控件的行列
		if (components==null)
			components=new Vector<>();
		//判重复
		if (!components.contains(component))
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






    /**
     *删除坐标所在的单元格编辑器
     * @param row
     * @param column
     */
	public void removeComponentCellAt(int row, int column){
		if (components==null)
			return;
		for (AnTableCellEditor editor:components){
			if (row==editor.getTableCellLocation().x&&column==editor.getTableCellLocation().y){
				components.remove(editor);
			}
		}
	}





    /**
     *获取默认的数据模型
     * @return
     */
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
