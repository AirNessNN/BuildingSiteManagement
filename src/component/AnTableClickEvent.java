package component;

/**
 * <h2>AnTable表格的点击事件</h2>
 */
public class AnTableClickEvent {
    AnTable table;
    int row;
    int column;
    boolean isEditable;
    Object value;
    int clickCount;

    public AnTableClickEvent(AnTable table,int row,int column,boolean isEditable,Object value,int clickCount){
        this.value=value;
        this.row=row;
        this.column=column;
        this.isEditable=isEditable;
        this.table=table;
        this.clickCount=clickCount;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public AnTable getTable() {
        return table;
    }

    public Object getValue() {
        return value;
    }

    public int getClickCount() {
        return clickCount;
    }
}
