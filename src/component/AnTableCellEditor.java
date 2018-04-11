package component;

import javax.swing.table.TableCellEditor;
import java.awt.*;

public interface AnTableCellEditor extends TableCellEditor{


    Point getTableCellLocation();

    void setTableCellLocation(int row ,int col);
}
