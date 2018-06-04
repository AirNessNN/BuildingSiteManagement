package dbManager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 属性集合Bean，封装拥有一个名称，和多个值的属性
 * @param <T>
 */
public class AnColumn<T>  implements Serializable{

    private String name=null;
    private ArrayList<T> values;

    private boolean repetable=false;//可重复标记

    public boolean isRepetable() {
        return repetable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValues(ArrayList<T> values) {
        if (values==null)
            return;
        this.values = values;
    }

    public ArrayList<T> getValues() {
        if (values==null)
            return new ArrayList<>();
        return values;
    }



    //构造
    public AnColumn(boolean repetable){
        values=new ArrayList<>();
        this.repetable=repetable;
    }

    public AnColumn(boolean repetable,String name){
        values=new ArrayList<>();
        this.name=name;
        this.repetable=repetable;
    }

    public AnColumn(boolean repetable,String name, ArrayList<T> arrayList){
        this.name=name;
        if (arrayList==null)
            values=new ArrayList<>();
        else
            this.values=arrayList;
        this.repetable=repetable;
    }




    //方法
    public boolean addValue(T value){
        if (values==null)
            return false;
        if (!isRepetable())
            if (contains(value))
                return false;
        values.add(value);
        return true;
    }

    public void removeValue(T value){
        if(values==null)
            return;
        values.remove(value);
    }

    public boolean contains(T value){
        if (values==null)
            return false;
        for(T t:values){
            if (t.equals(value)){
                return true;
            }
        }
        return false;
    }

    public void clearList(){
        if (values==null)
            return;
        values.clear();
    }

    public int getSize(){
        if (values==null)
            return 0;
        return values.size();
    }

    /**
     * 返回下标
     * @param object 值
     * @return 下标
     */
    public int indexOf(Object object){
        if (values==null)
            return -1;
        return values.indexOf(object);
    }

    public Object get(int index){
        return values.get(index);
    }

    /**
     * 插入一条数据
     * @param index 下标
     * @param v 值
     * @return 成功返回true
     */
    public boolean set(int index,T v){
        if (values.size()==0)return false;
        if (index>values.size())return false;
        if (!isRepetable()){
            if (contains(v)){
                return false;
            }
        }
        values.remove(index);
        values.add(index, v);
        return true;
    }
    /**
     * 修改数据，并且保持数据在列表中的位置不变
     * @param ov
     * @param nv
     * @return
     */
    public boolean changeValue(T ov,T nv){
        boolean found=false;
        int index;
        if(values==null)
            return false;
        for (T t:values){
            if (t.equals(ov))found=true;
        }
        if (found){
            index=values.indexOf(ov);
            values.remove(ov);
            values.add(index,nv);
        }
        return found;
    }
}
