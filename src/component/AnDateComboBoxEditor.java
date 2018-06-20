package component;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventObject;

public class AnDateComboBoxEditor extends JButton implements AnTableCellEditor{

    private EventListenerList listenerList = new EventListenerList();
    private ChangeEvent changeEvent = new ChangeEvent(this);
    private Point location=null;
    private AnDateComboPanel dateComboPanel;
    private JDialog frame;
    private boolean cancelFlag=false;//取消标记

    private Object value=null;




    public AnDateComboBoxEditor(){
        addActionListener(e->{
            frame.setVisible(true);
        });


        dateComboPanel=new AnDateComboPanel();
        dateComboPanel.setLocation(1,15);
        frame=new JDialog(){
            @Override
            public void dispose() {
                super.dispose();
                stopCellEditing();
            }
        };

        frame.setTitle("日期编辑");
        frame.setSize(310,140);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.getContentPane().add(dateComboPanel);

        JButton button=new MyButton("取消");
        button.setBounds(210,75,70,25);
        button.addActionListener((e)->{
            cancelFlag=true;
            frame.dispose();
            cancelFlag=false;
        });
        frame.getContentPane().add(button);

        JButton btnCancel=new MyButton("确定");
        btnCancel.setBounds(120,75,70,25);
        btnCancel.addActionListener(e->frame.dispose());
        frame.getContentPane().add(btnCancel);
        frame.setModal(true);
    }




    @Override
    public Point getTableCellLocation() {
        return location;
    }

    @Override
    public void setTableCellLocation(int row, int col) {
        location=new Point(row,col);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value==null)
            value="";
        setText(value.toString());
        this.value=value;
        //设置日期格式
        Date date;
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try{
            date=dateFormat.parse(value.toString());
            dateComboPanel.setDate(date);
        }catch (Exception e){
            dateComboPanel.Rest();
            return  this;
        }
        return this;
    }

    @Override
    public Object getCellEditorValue() {
        if (dateComboPanel==null)
            return null;
        if (cancelFlag)
            return "";
        return dateComboPanel.toString();
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
