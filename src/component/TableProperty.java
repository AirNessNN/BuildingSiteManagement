package component;

import java.awt.*;
import java.util.ArrayList;

/**
 * AnTable的单元格更改信息的包装类，此类存放改动单元格的行列坐标、更改前的数据、更改后的数据
 */
public class TableProperty {
    ArrayList<Rank> ranks;
    ArrayList<Object> oldValues;
    ArrayList<Object> newValues;

    int size=0;

    public TableProperty(){
        ranks =new ArrayList<>();
        oldValues=new ArrayList<>();
        newValues=new ArrayList<>();
    }

    /**
     * 添加一个更改的数据
     * @param rank
     * @param ov
     * @param nv
     */
    void addValue(Rank rank, Object ov, Object nv){
        if (rank==null||ov==null||nv==null)
            return;
        this.ranks.add(rank);
        oldValues.add(ov);
        newValues.add(nv);
        size++;
    }

    public ArrayList<Object> getNewValues() {
        return newValues;
    }

    public ArrayList<Object> getOldValues() {
        return oldValues;
    }

    public ArrayList<Rank> getRanks() {
        return ranks;
    }

    public void setNewValues(ArrayList<Object> newValues) {
        this.newValues = newValues;
    }

    public void setOldValues(ArrayList<Object> oldValues) {
        this.oldValues = oldValues;
    }

    public void setRanks(ArrayList<Rank> ranks) {
        this.ranks = ranks;
    }

    public int getSize() {
        return size;
    }

    public Rank getRank(int i){
        return ranks.get(i);
    }
    
    public Object getOldValue(int i){
        return oldValues.get(i);
    }

    public Object getNewValue(int i){
        return newValues.get(i);
    }
}
