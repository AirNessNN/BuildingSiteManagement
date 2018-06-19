package component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AnList<E> extends JList {

    private DefaultListModel<E> listModel=null;
    //private ListCellRenderer<E> cellRenderer=null;
    private DoubleClickListener listener=null;

    private Dimension itemSize=new Dimension(350,60);

    private Color selectedColor=new Color(0, 146, 128);



    private void init(ListCellRenderer cellRenderer){
        listModel=new DefaultListModel<>();
        this.setModel(listModel);
        this.setCellRenderer(cellRenderer);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    if(listener!=null){
                        int index=AnList.this.getSelectedIndex();
                        Object value=AnList.this.getSelectedValue();
                        listener.onDoubleClick(index,value);
                    }
                }
            }
        });
    }


    private boolean isScrollBarVisible(){
        int height=getParent().getParent().getSize().height;
        int number=height/itemSize.height;
        if(listModel.getSize()>number){
            return true;
        }
        return false;
    }



    public AnList(ListCellRenderer renderer){
        init(renderer);
        setFixedCellWidth(itemSize.width);
        setFixedCellHeight(itemSize.height);
    }

    public AnList(ListCellRenderer renderer,int lineWith,int lineHeight){
        init(renderer);
        setFixedCellHeight(lineHeight);
        setFixedCellWidth(lineWith);
        itemSize.width=lineWith;
        itemSize.height=lineHeight;
    }


    public void addElement(E e){
        if (listModel==null)
            return;
        listModel.addElement(e);
        if(isScrollBarVisible()){
            setFixedCellWidth(itemSize.width-18);
        }else{
            setFixedCellWidth(itemSize.width);
        }
    }

    public void removeElement(E e){
        if(listModel==null)
            return;
        listModel.removeElement(e);
        if(isScrollBarVisible()){
            setFixedCellWidth(itemSize.width-18);
        }else{
            setFixedCellWidth(itemSize.width);
        }
    }

    public void removeElementAt(int index){
        if(listModel==null)
            return;
        listModel.remove(index);
        if(isScrollBarVisible()){
            setFixedCellWidth(itemSize.width-18);
        }else{
            setFixedCellWidth(itemSize.width);
        }
    }

    public E getElementAt(int index){
        if(listModel==null)
            return null;
        if(index>listModel.size()-1)
            return null;
        if(index==-1)
            return null;
        return listModel.get(index);
    }

    public void clear(){
        if(listModel==null)
            return;
        listModel.clear();

    }

    public int getItemSize(){
        return listModel.getSize();
    }


    public interface DoubleClickListener{
        void onDoubleClick(int index,Object sender);
    }


    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    public DefaultListModel getDefaultListModel(){
        return listModel;
    }
}
