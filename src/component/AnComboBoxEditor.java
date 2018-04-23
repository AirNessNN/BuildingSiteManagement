package component;

import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.event.*;

public class AnComboBoxEditor extends JComboBox<String> implements AnTableCellEditor{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Point location=null;
	private JTextField textField=null;
	

	protected EventListenerList listenerList = new EventListenerList();
    protected ChangeEvent changeEvent = new ChangeEvent(this);
    
    public AnComboBoxEditor() {
        super();
        setEditable(true);
        setFont(new Font("微软雅黑",Font.PLAIN,14));

        textField=(JTextField)getEditor().getEditorComponent();

        addActionListener(e -> fireEditingStopped());

        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setPopupVisible(true);
            }
        });
    }

    public void addCellEditorListener(CellEditorListener listener) {
        listenerList.add(CellEditorListener.class, listener);
    }

    public void removeCellEditorListener(CellEditorListener listener) {
        listenerList.remove(CellEditorListener.class, listener);
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

    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    public boolean isCellEditable(EventObject event) {
        return true;
    }

    public boolean shouldSelectCell(EventObject event) {
        return true;
    }

    public Object getCellEditorValue() {
        if (isEditable){
            return textField.getText();
        }else
            return getSelectedItem();
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {

        if (value==null||value.equals(""))
            return this;
        //选择
        if (getItemCount()==0){
            addItem(value.toString());
            setSelectedIndex(0);
        }else{
            for (int i=0;i<getItemCount();i++){
                if (getItemAt(i).equals(value.toString())){
                    setSelectedIndex(i);
                    return this;
                }
            }
            insertItemAt(value.toString(),0);
            setSelectedIndex(0);
        }
        return this;
    }

    @Override
    public Point getTableCellLocation() {
        return location;
    }

    @Override
    public void setTableCellLocation(int row, int col) {
        location=new Point(row,col);
    }


    public void setSelectValue(String value){
        if (value==null)
            return;
        if (value.equals(""))
            return;
        for (int i=0;i<getItemCount();i++){
            if (getItemAt(i).equals(value)){
                setSelectedIndex(i);
                return;
            }
        }
        addItem(value);
        setSelectValue(value);
    }


}
