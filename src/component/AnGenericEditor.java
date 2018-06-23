package component;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.EventObject;

public class AnGenericEditor extends JTextField implements TableCellEditor {

    private EventListenerList listenerList = new EventListenerList();
    private ChangeEvent changeEvent = new ChangeEvent(this);
    private Counter counter;

    {
        counter = new Counter() {
            @Override
            public boolean shouldClearCount() {
                if (oldCol != column || oldRow != row) {
                    oldRow = row;
                    oldCol = column;
                    return true;
                } else {
                    oldRow = row;
                    oldCol = column;
                    return false;
                }
            }
        };
    }

    private int clickCount=1;
    private long clickTime= 0L;

    private int oldRow=-1;
    private int oldCol=-1;
    private int row,column;

    public AnGenericEditor(){
        setFont(new Font("微软雅黑",Font.PLAIN,15));
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                selectAll();
            }
        });
        setBorder(null);
    }



    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row=row;
        this.column=column;

        counter.check();

        if (counter.getCount()<2)return null;
        if (value==null)
            value="";
        setText(value.toString());
        clickCount=1;
        clickTime=0L;
        return this;
    }

    @Override
    public Object getCellEditorValue() {
        return getText();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    @Override
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class,l);
    }



    protected void fireEditingStopped() {
        CellEditorListener listener;
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == CellEditorListener.class) {
                listener = (CellEditorListener) listeners[i + 1];
                listener.editingStopped(changeEvent);
            }
        }
    }

    protected void fireEditingCanceled() {
        CellEditorListener listener;
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == CellEditorListener.class) {
                listener = (CellEditorListener) listeners[i + 1];
                listener.editingCanceled(changeEvent);
            }
        }
    }
}
