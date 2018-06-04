package dbManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 封装拥有名字集合和它对应的集合值的集合，
 * 例如：属性封装器，封装工地中所有工地
 */
public class AnDataTable implements Serializable{

    private int selectedRowIndex =-1;//集合在操作的下标
    private ArrayList<AnColumn> values;//集合表
    private String name="";//该集合的名称
    private String key=null;//主键

    /**
     * 获取所有元素
     * @return 元素
     */
    public ArrayList<AnColumn> getValues() {
        if (values==null)
            return new ArrayList<>();
        return values;
    }

    /**
     * 填充元素
     * @param values 元素
     */
    public void setValues(ArrayList<AnColumn> values) {
        this.values = values;
        selectedRowIndex =-1;
        key=null;
    }



    //构造
    public AnDataTable(){
        values=new ArrayList<>();
    }
    public AnDataTable(String name){
        values=new ArrayList<>();
        this.name=name;
    }
    public AnDataTable(AnColumn<?>... arrays){
        values=new ArrayList<>();
        values.addAll(Arrays.asList(arrays));
    }


    //方法

    /**
     * 添加一个属性集合列
     * @param e 属性结婚
     * @throws Exception 相同抛出异常
     */
    public void addColumn(AnColumn<?> e) throws Exception {
        if(values==null)
            return;
        //属性名不能相同
        for (AnColumn anColumn :values){
            if (e.getName().equals(anColumn.getName()))
                throw new Exception("属性名称不能相同");
        }
        values.add(e);
        selectedRowIndex =-1;
    }

    /**
     * 移除一个属性
     * @param e 要移除的属性
     */
    public void removeInfoArray(AnColumn<?> e){

        if(values==null)
            return;
        values.remove(e);
        selectedRowIndex =-1;
        if (e.getName().equals(key))
            key=null;
    }

    /**
     * 通过下标移除一个属性
     * @param index 下标
     */
    public void removeAt(int index){
        if (values==null)
            return;
        if (values.get(index).getName().equals(key))
            key=null;
        values.remove(index);
        selectedRowIndex =-1;
    }

    public AnColumn<?> get(int index){
        if (values==null)
            return null;
        return values.get(index);
    }

    /**
     * 通过名字添加info的数据值
     * @param name 名称
     * @param value 值
     * @return 操作成功返回True
     */
    public boolean addValueTo(String name,Object value){
        if(null==value)
            return false;
        for (AnColumn anColumn :values){
            if (anColumn.getName().equals(name)){
                anColumn.addValue(value);
                return true;
            }
        }
        return false;
    }

    /**
     * 通过名字查找出属性节点
     * @param name 属性名
     * @return 返回属性节点
     */
    public AnColumn find(String name){
        if (null==values)
            return null;
        for(AnColumn info:values){
            if (info.getName().equals(name)){
                return info;
            }
        }
        return null;
    }

    /**
     * 通过下标返回节点
     * @param index 下标
     * @return 节点
     */
    public AnColumn findAt(int index){
        if (null==values)
            return null;
        return values.get(index);
    }

    /**
     * 返回该节点所在的下标
     * @param property 节点
     * @return 下标
     */
    public int indexOf(AnColumn property){
        for (int i=0;i<this.values.size();i++){
            if (values.get(i).equals(property))
                return i;
        }
        return -1;
    }

    /**
     * 通过属性名返回下标
     * @param propertyName 属性名
     * @return 下标
     */
    public int indexOf(String propertyName){
        return indexOf(find(propertyName));
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





    /*

    下面是模拟表格的操作，把一个InfoArray当成一列，把整个Bean当成表格，
    在每个InfoArray中的每一个数据都是一个单元格，
    而一行数据就是所有InfoArray中的固定下标的一个值，我们通过下面的方法模拟
     */

    /**
     * 增加一行，并且填充数据，存在表头多少就添加多少数据，多或少数据都导致添加失败
     * @param objects 数据集
     * @return 成功返回True
     */
    public boolean addRow(Object... objects){
        if (objects==null)
            return false;
        if (objects.length==0)
            return false;
        if (objects.length!=values.size())
            return false;

        //排重
        boolean found=false;
        for (int i=0;i<objects.length;i++){
            if (values.get(i).contains(objects[i])&&!values.get(i).isRepetable())
                found=true;//发现有一条重复数据就停止添加
        }
        if (found)return false;
        for (int i=0;i<objects.length;i++) values.get(i).addValue(objects[i]);
        return true;
    }

    /**
     * 插入一条数据，数据不能相同，相同则失败
     * @param objects
     * @return
     */
    @Deprecated
    public boolean insertRow(Object... objects){
        return true;
    }

    /**
     * 通过Index移除一整行数据
     * @param rowIndex 下标
     * @return 成功返回true
     */
    public boolean removeRow(int rowIndex){
        if (values.get(0).getSize()<rowIndex)
            return false;
        for (int i=0;i<values.size();i++){
            values.get(i).removeValue(values.get(i).get(rowIndex));
        }
        return true;
    }

    /**
     * 根据主键值移除整行数据，如果没设置主键，移除失败
     * @param value 主键的值
     * @return 成功返回True
     */
    public boolean removeRow(Object value){
        if (value==null)
            return false;
        if (values.size()==0)
            return false;
        if (key==null)
            return false;
        int index=-1;

        AnColumn column=find(key);
        index=column.indexOf(value);
        if (index==-1)
            return false;
        for (int i=0;i<values.size();i++){
            values.get(i).removeValue(values.get(i).get(index));
        }
        return true;
    }

    /**
     * 获取单元格中的数据
     * @param row 行
     * @param col 列
     * @return 单元格的值
     */
    public Object getCell(int row,int col){
        if (values.size()<col)
            return null;
        if (values.size()==0)
            return null;
        AnColumn column=values.get(col);
        if (column.getSize()==0||column.getSize()<row)
            return null;

        return column.get(row);
    }

    /**
     * 设置单元格 ，注意：单元格中一行的数据不能相同，且此方法用于更改单元格中的信息
     * @param row 行
     * @param col 列
     * @param value 值
     */
    public void setCell(int row,int col,Object value){
        if (getCell(row,col)==null)return;
        values.get(col).getValues().set(row,value);
    }

    /**
     * 返回选中行中存在的属性名的值
     * @param propertyName 属性名
     * @return 返回单元格数据
     */
    public Object getSelectedRowAt(String propertyName){
        if (selectedRowIndex ==-1)return null;

        AnColumn column=find(propertyName);
        if (column==null)return null;

        return column.get(selectedRowIndex);
    }

    /**
     * 设置选中行中的单元格的数据
     * @param propertyName 属性名
     * @param value 值
     */
    public void setSelectedRowAt(String propertyName, Object value){
        if (selectedRowIndex ==-1)return;

        AnColumn column=find(propertyName);
        column.set(selectedRowIndex,value);
    }

    /**
     * 返回选中行列的值
     * @param col 列下标
     * @return 返回单元格数据
     */
    public Object getSelectedRowAt(int col){
        if (selectedRowIndex ==-1)return null;
        if (col>values.size())return null;
        return values.get(col).get(selectedRowIndex);
    }

    /**
     * 设置选中行中的单元格的数据
     * @param col 属性名
     * @param value 值
     */
    public void setSelectedRowAt(int col, Object value){
        if (getSelectedRowAt(col)==null)return;
        values.get(col).set(selectedRowIndex,value);
    }

    /**
     * 选择一行
     * @param rowIndex 要选择的行下标
     */
    public int selectRow(int rowIndex){
        if (rowIndex<0)
            return-1;
        return selectedRowIndex =rowIndex;
    }

    /**
     * 通过一个列的值选择一行
     * @param key 键
     * @param v 值
     */
    public int selectRow(String key,Object v){
        selectedRowIndex =-1;//初始化选择

        AnColumn column=find(key);
        if (column==null)return selectedRowIndex;

        return selectRow(column.indexOf(v));
    }

    /**
     * 通过主键选择一行
     * @param v 值
     */
    public int selectRow(Object v){
        return selectRow(key,v);
    }

    /**
     * 清除选择的项目，选择值为-1
     */
    public void clearSelection(){
        selectedRowIndex =-1;
    }

    /**
     * 返回表格选择的行的下标
     * @return 返回下标
     */
    public int getSelectedRowIndex() {
        return selectedRowIndex;
    }

    /**
     * 设置主键，如果将主键设置为null，则是清除主键
     * @param key 主键
     */
    public void setKey(String key) {
        this.key = key;
    }






    /*
    下面是重写的方法
     */

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("{ ");
        sb.append("AnDataTable : \""+getName()+"\" \n");
        for (int i=0;i<values.size();i++){
            sb.append(values.get(i).getName()+" : ");
            for (Object o:values.get(i).getValues()){
                sb.append("["+o+"]、");
            }
            sb.append('\n');
        }
        sb.append("rowSize："+values.get(0).getSize()+"\n");
        sb.append("columnSize："+values.size()+" }\n");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AnDataTable))
            return false;
        AnDataTable bean= (AnDataTable) obj;
        if (bean.getSize()!=this.getSize())
            return false;
        for (AnColumn anColumn :bean.getValues()){
            boolean found=false;
            for (AnColumn tmp:this.getValues()){
                if (anColumn.getName().equals(tmp.getName())){
                    found=true;
                }
            }
            if (!found)
                return false;
        }
        return true;
    }
}
