package component;

/**
 * <h2>AnTable的行列包装类</h2>
 * <li>封装 int row坐标信息，表示实例对象的行下标</li>
 * <li>封装 int column坐标信息，表示实例对象的列下标</li>
 * <li>parent信息：实例该坐标的AnTable对象</li>
 */
public class Rank {
    //行列
    private int row,column;

    //父对象
    private AnTable table=null;


    /**
     * 构造一个初始化行列属性的Rank对象
     * @param row
     * @param column
     */
    public Rank(int row,int column){
        this.row=row;
        this.column=column;
    }

    /**
     * 对外不可见的构造，用于AnTable实例的Rank对象
     * @param row
     * @param column
     * @param table
     */
    Rank(int row,int column,AnTable table){
        this.row=row;
        this.column=column;
        this.table=table;
    }

    /**
     * 获取实例该Rank对象的AnTable
     * @return 返回null代表不是由AnTable构造的
     */
    public AnTable getTable() {
        return table;
    }

    /**
     * 获取列
     * @return 列
     */
    public int getColumn() {
        return column;
    }

    /**
     * 获取行
     * @return 行
     */
    public int getRow() {
        return row;
    }

    /**
     * 设置列
     * @param column 列下标
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * 设置行
     * @param row 行下标
     */
    public void setRow(int row) {
        this.row = row;
    }
}
