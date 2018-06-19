package dbManager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 属性集合Bean，封装拥有一个名称，和多个值的属性
 * <li>可空的列集合：其中列表可以设置为可空或不可空，可空和重复不影响，空值不会影响排重，null值不会进入排重</li>
 */
public class Column implements Serializable{

    private String name=null;//列名
    private boolean repetable;//可重复标记
    private boolean nullAble;//可空标记

    private ArrayList values;//表数据





    //构造
    public Column(boolean repetable){
        values=new ArrayList<>();
        this.repetable=repetable;
    }

    public Column(boolean repetable, String name){
        values=new ArrayList<>();
        this.name=name;
        this.repetable=repetable;
    }

    public Column(boolean repetable, boolean nullAble, String name){
        values=new ArrayList<>();
        this.name=name;
        this.repetable=repetable;
        this.nullAble=nullAble;
    }

    public Column(boolean repetable, String name, ArrayList arrayList){
        this.name=name;
        if (arrayList==null)
            values=new ArrayList<>();
        else
            this.values=arrayList;
        this.repetable=repetable;
    }

    public Column(boolean repetable, boolean nullAble, String name, ArrayList arrayList){
        this.name=name;
        if (arrayList==null)
            values=new ArrayList<>();
        else
            this.values=arrayList;
        this.repetable=repetable;
        this.nullAble=nullAble;
    }






    /**
     * 添加一个元素，添加成功与否取决于列表是否可空或可重复
     * @param value 值
     * @return 成功返回true
     */
    public boolean addValue(Object value){
        if (values==null)
            return false;
        if (!nullAble&&value==null)
            return false;
        if (isRepetable()){//可重复
            values.add(value);
        }else {
            if (values.contains(value))
                return false;
            values.add(value);
        }
        return true;
    }






    /**
     * 移除找到的第一个值
     * @param value
     */
    public void removeValue(Object value){
        if(values==null)
            return;
        values.remove(value);
    }






    /**
     * 排重
     * @param value 要检测的值
     * @return 在列表中出现返回true
     */
    public boolean contains(Object value){
        if (values==null)
            return false;
        return values.contains(value);
    }






    /**
     * 清空List
     */
    public void clearList(){
        if (values==null)
            return;
        values.clear();
    }






    /**
     * 获取容器内所有元素的数量
     * @return 数量
     */
    public int size(){
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






    /**
     * 通过下标获取数据
     * @param index
     * @return
     */
    public Object get(int index){
        if (values==null)return null;
        if (index> size()||index<0)
            return null;
        return values.get(index);
    }






    /**
     * 插入一条数据
     * @param index 下标
     * @param v 值
     * @return 成功返回true
     */
    public boolean set(int index,Object v){
        if (values==null) return false;
        if (!nullAble&&v==null)return false;
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
    public boolean changeValue(Object ov,Object nv){
        if (values==null)
            return false;
        int index=indexOf(ov);
        return set(index,nv);
    }






    /**
     * 获取重复标记
     * @return true是可重复
     */
    public boolean isRepetable() {
        return repetable;
    }

    /**
     * 是否可空
     * @return
     */
    public boolean isNullAble() {
        return nullAble;
    }






    /**
     * 设置为不可空的列，清除所有null值
     * @param b 标记
     */
    public void setNullAble(boolean b){
        nullAble=b;
        if (values!=null&&!b){
            while (true) {
                if (indexOf(null)==-1||size()==0)
                    return;
                removeValue(null);
            }
        }
    }






    /**
     * 获取列名
     * @return 列名
     */
    public String getName() {
        return name;
    }






    /**
     * 设置列名
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }






    /***
     * 填充值
     * @param values 值
     */
    public void setValues(ArrayList values) {
        if (values==null)
            return;
        this.values = values;
    }






    /**
     * 获取内建的ArrayList
     * @return ArrayList
     */
    public ArrayList getValues() {
        if (values==null)
            return new ArrayList<>();
        final ArrayList list = new ArrayList(values);
        return list;
    }






    /**
     * 转换成数组
     * @return 数组
     */
    public Object[] toArray(){
        if (values==null)
            return null;
        return values.toArray();
    }






    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Column){
            Column column= (Column) obj;
            if (column.getName()!=null&&this.getName()!=null&&column.getName().equals(getName())){
                return column.size()== size();
            }
            return false;
        }
        return false;
    }






    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();

        sb.append("( ");
        sb.append("Name："+getName()+"：");
        for (Object o:values){
            sb.append(" [ "+o.toString()+" ] ");
            if (!(indexOf(o)== size()-1))
                sb.append("、");
        }
        sb.append(" )");
        return sb.toString();
    }
}
