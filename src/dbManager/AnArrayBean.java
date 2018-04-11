package dbManager;

import java.io.Serializable;
import java.util.ArrayList;

public class AnArrayBean implements Serializable{

    private ArrayList<InfoArray> values;

    public ArrayList<InfoArray> getValues() {
        if (values==null)
            return new ArrayList<>();
        return values;
    }

    public void setValues(ArrayList<InfoArray> values) {
        this.values = values;
    }



    //构造
    public AnArrayBean(){
        values=new ArrayList<>();
    }

    public AnArrayBean(InfoArray<?> ... arrays){
        values=new ArrayList<>();
        for(InfoArray array:arrays){
            values.add(array);
        }
    }


    //方法
    public void addInfoArray(InfoArray<?> e){
        if(values==null)
            return;
        values.add(e);
    }

    public void removeInfoArray(InfoArray<?> e){

        if(values==null)
            return;
        values.remove(e);
    }

    public void removeAt(int index){
        if (values==null)
            return;
        values.remove(index);
    }

    public InfoArray<?> get(int index){
        if (values==null)
            return null;
        return values.get(index);
    }

    /**
     * 通过名字添加info的数据值
     * @param name
     * @param value
     * @return
     */
    public boolean addValueTo(String name,Object value){
        if(null==value)
            return false;
        for (InfoArray infoArray :values){
            if (infoArray.getName().equals(name)){
                infoArray.addValue(value);
                return true;
            }
        }
        return false;
    }

    /**
     * 通过名字查找出Info节点
     * @param name
     * @return
     */
    public InfoArray find(String name){
        if (null==values)
            return null;
        for(InfoArray info:values){
            if (info.getName().equals(name)){
                return info;
            }
        }
        return null;
    }

    public InfoArray findAt(int index){
        if (null==values)
            return null;
        return values.get(index);
    }


    public int getSize(){
        if (null==values)
            return 0;
        return values.size();
    }

}
