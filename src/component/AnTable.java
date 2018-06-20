package component;

import resource.Resource;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 * An表单，可以轻松设置表单填充数据
 */
public class AnTable extends JTable{

	private DefaultTableModel tableModel =null;//默认列表数据模型

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

	private Object[][] oldValues=null;

	private int row=-1;
	private int col=-1;












	/**
	 * 初始化控件和数据模型
	 */
	private void init(){

		getTableHeader().setFont(new Font("等线",1,14));
		setFont(new Font("等线",0,14));
		this.setRowHeight(30);
		this.setFont(Resource.FONT_TABLE_ITEM);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setFillsViewportHeight(true);

		header=new Vector<>();//标题
		editPassList=new ArrayList<>();
		rowEditFiltrate=new ArrayList<>();
		columnEditFiltrate=new ArrayList<>();

		//默认listMod，重写其中的isCellEditable方法，控制单元格的编辑规则
		tableModel =new DefaultTableModel(){
			@Override
			public boolean isCellEditable(int row, int column) {
                /*
                先进行行列筛选循环，再进行单元格循环，
                因为行列只需要判断一个值就能断定是否点击了筛选的行，
                在循环中只return false是的意思是找到匹配的就返回false
                 */

                AnTable.this.row=row;
                AnTable.this.col=column;
				System.out.println("row="+row+" col="+column);

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
		this.setModel(tableModel);
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
		tableModel.setColumnIdentifiers(columnNames);
		this.header.addAll(Arrays.asList(columnNames));
	}





	/**
	 * 添加表头
	 * @param object
	 */
	public void addColumn(Object object){
		header.add(object);
		tableModel.setColumnIdentifiers(header.toArray());
	}





	/**
	 * 填充数据
	 * @param data
	 */
	public void fillData(Object[][] data){
		tableModel.setDataVector(data,header.toArray());
	}

	public void fillData(Vector<Vector> data){
		tableModel.setDataVector(data,header);
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
			if (editPassList.contains(p))return;
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
		else {
			if (!rowEditFiltrate.contains(row))rowEditFiltrate.add(row);
		}
		if (rowEditFiltrate.size()==0) rowFiltrateFlag=true;
	}





	/**
	 * 设置指定列的可编辑状态
	 * @param column 列号
	 * @param editable 设置数值
	 */
	public void setCellColumnEdited(Integer column,boolean editable){
		columnFiltrateFlag =false;//设置筛选已经打开
        if (editable) columnEditFiltrate.remove(column);
        else {
			if (!columnEditFiltrate.contains(column))columnEditFiltrate.add(column);
		}
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

	public void clearComponentCell(){
		if (components!=null)
			components.clear();
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
	 * 设置一次检查点，之后表格的变化与否都与此次设置的检查点做比较
	 */
	public void setCheckPoint(){
		Vector<Vector> vectors= getTableModel().getDataVector();
		if (vectors==null)
			return;

		oldValues=new Object[vectors.size()][getColumnCount()];
		for (int i=0;i<vectors.size();i++){
			for(int j=0;j<getColumnCount();j++){
				oldValues[i][j]=vectors.get(i).get(j);
			}
		}
	}

	/**
	 * 清除检查点
	 */
	public void clearCheckPoint(){
		oldValues=null;
	}

	/**
	 * 检查已经设置的检查点，判断是否有数据发生改变
	 * @return 如果已经设置检查点，则判断数据是否一致，返回改动的单元格坐标
	 */
	public TableProperty getChangedCells(){
		if (oldValues==null)
			return new TableProperty();
		TableProperty bean=new TableProperty();

		Vector<Vector> vectors= getTableModel().getDataVector();
		for (int row=0;row<vectors.size();row++){
			for(int col=0;col<getColumnCount();col++){
				String rv;
				String old;
				if (vectors.get(row).get(col)==null) rv="";
				else rv=vectors.get(row).get(col).toString();

				if (oldValues[row][col]==null) old="";
				else old=oldValues[row][col].toString();

				if (!rv.equals(old)) {
					//x是cell下标，y是行号
					bean.addValue(new Point(row,col),old,rv);
				}
			}
		}
		return bean;
	}




    /**
     *获取默认的数据模型
     * @return
     */
	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	/**
	 * 根据行列号获取单元格中的内容
	 * @param row
	 * @param column
	 * @return
	 */
	public Object getCell(int row,int column){
		Vector cells= (Vector) getTableModel().getDataVector().get(row);
		return cells.get(column);
	}

	/**
	 * 设置指定单元格的值
	 * @param row
	 * @param column
	 * @param value
	 */
	public void setCell(int row,int column,Object value){
		if (this.getRowCount()<row)
			return;
		if (this.getColumnCount()<column)
			return;

		Vector cells= (Vector) getTableModel().getDataVector().get(row);
		cells.set(column,value);
	}


	public void setColumnWidth(int index,int width){
		getColumnModel().getColumn(index).setWidth(width);
	}

	public void setColumnWidth(String columnName,int width){
		int i=getColumnModel().getColumnIndex(columnName);
		setColumnWidth(i,width);
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
