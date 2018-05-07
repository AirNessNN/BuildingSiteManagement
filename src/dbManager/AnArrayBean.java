package dbManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 封装拥有名字集合和它对应的集合值的集合，
 * 例如：属性封装器，封装工地中所有工地
 */
public class AnArrayBean implements Serializable{

    private ArrayList<InfoArray> values;
    private String name="";

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
    public AnArrayBean(String name){
        values=new ArrayList<>();
        this.name=name;
    }
    public AnArrayBean(InfoArray<?> ... arrays){
        values=new ArrayList<>();
        values.addAll(Arrays.asList(arrays));
    }


    //方法
    public void addInfoArray(InfoArray<?> e) throws Exception {
        if(values==null)
            return;
        //属性名不能相同
        for (InfoArray infoArray :values){
            if (e.getName().equals(infoArray.getName()))
                throw new Exception("属性名称不能相同");
        }
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

    public int indexOf(InfoArray values){
        for (int i=0;i<this.values.size();i++){
            if (values.getName().equals(this.values.get(i).getName()))
                return i;
        }
        return -1;
    }

    public int getSize(){
        if (null==values)
            return 0;
        return values.size();
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AnArrayBean))
            return false;
        AnArrayBean bean= (AnArrayBean) obj;
        if (bean.getSize()!=this.getSize())
            return false;
        for (InfoArray infoArray:bean.getValues()){
            boolean found=false;
            for (InfoArray tmp:this.getValues()){
                if (infoArray.getName().equals(tmp.getName())){
                    found=true;
                }
            }
            if (!found)
                return false;
        }
        return true;
    }
}
