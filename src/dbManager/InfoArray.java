package dbManager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 属性集合Bean，封装拥有一个名称，和多个值的属性
 * @param <T>
 */
public class InfoArray<T>  implements Serializable{

    private String name=null;
    private ArrayList<T> values;

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
    public InfoArray(){
        values=new ArrayList<>();
    }

    public InfoArray(String name){
        values=new ArrayList<>();
        this.name=name;
    }

    public InfoArray(String name, ArrayList<T> arrayList){
        this.name=name;
        if (arrayList==null)
            values=new ArrayList<>();
        else
            this.values=arrayList;
    }




    //方法
    public void addValue(T value){
        if (values==null)
            return;
        if (contains(value))
            return;
        values.add(value);
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

}
