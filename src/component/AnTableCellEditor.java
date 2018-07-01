package component;

import javax.swing.table.TableCellEditor;
import java.awt.*;

public interface AnTableCellEditor extends TableCellEditor{


    //Rank getTableCellLocation();

    boolean isCellEditor(int row,int col);

    void addTableCellLocation(int row , int col);

    void removeTableCellLocation(int row,int col);

    boolean isEmpty();
}
