package dbManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * <H2>An轻量级数据容器</H2>
 * <li>表格模拟：使用Cell、Row、Column操作其中的数据，作为表格使用时，将无法识别空数据</li>
 * <li>JavaBean：属性集合的集合，在此容器中可以存放封装有一个对象集合和属性名的属性包装对象，并且支持属性查询和内容查询</li>
 * <br/>
 * <p>属性包装类使用AnColumn来实现</p>
 * @see Column
 */
public class DataTable implements Serializable{

    private int selectedRowIndex =-1;//集合在操作的下标
    private ArrayList<Column> values;//集合表
    private String name="";//该集合的名称
    private String key=null;//主键

    private final Bean infos=new Bean("附加属性");//该数据表的信息


    /**
     * 加载表格属性
     */
    private void initBean(){
        infos.clear();
    }


    //构造
    public DataTable(){
        values=new ArrayList<>();
        initBean();
    }

    public DataTable(String name){
        values=new ArrayList<>();
        this.name=name;
        initBean();
    }

    public DataTable(Column... arrays){
        values=new ArrayList<>();
        values.addAll(Arrays.asList(arrays));
        initBean();
    }







    //方法
    /**
     * 获取所有元素
     * @return 元素
     */
    public ArrayList<Column> getValues() {
        if (values==null)
            return new ArrayList<>();
        return new ArrayList<>(values);
    }






    /**
     * 填充元素
     * @param values 元素
     */
    public void setValues(ArrayList<Column> values) {
        this.values = values;
        selectedRowIndex =-1;
        key=null;
    }






    /**
     * 添加一个属性集合列
     * @param e 属性结婚
     * @throws Exception 相同抛出异常
     */
    public void addColumn(Column e) throws Exception {
        if(values==null)
            return;
        //属性名不能相同
        if (values.contains(e))
            throw new Exception("属性名称不能相同");
        values.add(e);
        selectedRowIndex =-1;
    }






    /**
     * 移除一个属性
     * @param e 要移除的属性
     */
    public void removeColumn(Column e){
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
    public void removeColumnAt(int index){
        if (values==null)
            return;
        if (values.get(index).getName().equals(key))
            key=null;
        values.remove(index);
        selectedRowIndex =-1;
    }






    /**
     * 通过下标获取行
     * @param index
     * @return
     */
    public Column getColumn(int index){
        if (values==null)
            return null;
        return values.get(index);
    }






    /**
     * 通过名字添加info的数据值
     * @param columnName 名称
     * @param value 值
     * @return 操作成功返回True
     */
    public boolean addValueTo(String columnName,Object value){
        if(null==value)
            return false;
        for (Column column :values){
            if (column.getName().equals(columnName)){
                column.addValue(value);
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
    public Column findColumn(String name){
        if (null==values)
            return null;
        for(Column info:values){
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
    public Column columnAt(int index){
        if (null==values)
            return null;
        return values.get(index);
    }






    /**
     * 返回该节点所在的下标
     * @param property 节点
     * @return 下标
     */
    public int columnIndexOf(Column property){
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
    public int columnIndexOf(String propertyName){
        return columnIndexOf(findColumn(propertyName));
    }






    /**
     * 返回AnColumn的数量
     * @return Size
     */
    public int getSize(){
        if (null==values)
            return 0;
        return values.size();
    }






    /**
     * 获取表名
     * @return 名称
     */
    public String getName(){
        return name;
    }






    /**
     * 设置表名
     * @param name 名称
     */
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
     * @return 成功返回True，需要注意的是，之后一条数据都没有添加进去的时候才会返回false，添加部分数据返回都是true
     */
    public boolean addRow(Object[] objects){
        if (objects==null)
            return false;
        if (objects.length==0)
            return false;
        if (objects.length!=values.size())
            return false;

        int index= getMinRowCount();
        int i=0;
        boolean added=false;
        for (Column column:values){
            if (index==column.size()){
                column.addValue(objects[i]);
                added=true;
            }
            i++;
        }
        return added;
    }






    /**
     * 向表中某一行插入指定列的数据，
     * <li>插入的指定列中是否容纳重复的数据要依据该列是否支持可重复的数据插入</li>
     * @param columnNames 列名集合
     * @param objects 数据集合
     * @param index 行下标
     * @return 成功返回true
     */
    public boolean insertRow(String[] columnNames,Object[] objects,int index) {
        if (columnNames==null||columnNames.length==0)
            return false;
        if (columnNames.length!=objects.length)
            return false;

        //检查下标
        for (int i=0;i<columnNames.length;i++){
            Column column=findColumn(columnNames[i]);
            if (index>=column.size()){//大于下标
                return false;
            }
        }
        //添加
        for (int i=0;i<columnNames.length;i++){
            Column column=findColumn(columnNames[i]);
            column.set(index,objects[i]);
        }
        return true;
    }






    /**
     * 插入一条数据，数据不能相同，相同则失败
     * @param objects
     * @return
     */
    @Deprecated
    public boolean insertRow(Object... objects){
        return false;
    }






    /**
     * 通过Index移除一整行数据
     * @param rowIndex 下标
     * @return 成功返回true
     */
    public boolean removeRow(int rowIndex){
        if (values.get(0).size()<rowIndex)
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
    public boolean removeKeyRow(Object value){
        if (value==null)
            return false;
        if (values.size()==0)
            return false;
        if (key==null)
            return false;
        int index=-1;

        Column column= findColumn(key);
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
        if (values.size()<col||col<0)
            return null;
        if (values.size()==0)
            return null;
        Column column=values.get(col);
        if (column.size()==0||column.size()<row)
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
        values.get(col).set(row,value);
    }






    /**
     * 返回选中行中存在的属性名的值
     * @param propertyName 属性名
     * @return 返回单元格数据
     */
    public Object getSelectedRowAt(String propertyName){
        if (selectedRowIndex ==-1)return null;

        Column column= findColumn(propertyName);
        if (column==null)return null;

        return column.get(selectedRowIndex);
    }






    /**
     * 设置选中行中的单元格的数据
     * @param propertyName 属性名
     * @param value 值
     */
    public void setSelectedRow(String propertyName, Object value){
        if (selectedRowIndex ==-1)return;

        Column column= findColumn(propertyName);
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
     * @param columnName 键
     * @param v 值
     */
    public int selectRow(String columnName,Object v){
        selectedRowIndex =-1;//初始化选择

        Column column= findColumn(columnName);
        if (column==null)return selectedRowIndex;

        return selectRow(column.indexOf(v));
    }






    /**
     * 通过主键选择一行
     * @param v 值
     */
    public int selectRowOfKey(Object v){
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






    /**
     * 返回最小行数，因为有可能在表中的某一行并不是这个值，为了保证不发生异常，我们返回表中最小行数
     * @return 最小行数
     */
    public int getMinRowCount(){
        int count=Integer.MAX_VALUE;
        for (Column column:values){
            if (count>column.size())
                count=column.size();
        }
        return count;
    }






    /**
     * 返回其中最大行数
     * @return 最大行数
     */
    public int getMaxRowCount(){
        int count=Integer.MIN_VALUE;
        for (Column column:values){
            if (column.size()>count)
                count=column.size();
        }
        return count;
    }






    /**
     * 获取该列的行数
     * @param columnIndex 列下标
     * @return 行数
     */
    public int getColumnRowCount(int columnIndex){
        if (columnIndex>=getColumnCount())
            return 0;
        return getColumn(columnIndex).size();
    }






    /**
     * 返回列数
     * @return 返回列数
     */
    public int getColumnCount(){
        return values.size();
    }






    /**
     * 返回表名
     * @return 表名数组
     */
    public String[] getColumnName(){
        String[] names=new String[getColumnCount()];
        for (int i=0;i<getColumnCount();i++){
            names[i]=values.get(i).getName();
        }
        return names;
    }






    /**
     *获取主键的列下标
     * @return 列下标
     */
    public int getKeyIndex(){
        if (key==null)
            return -1;
        return getColumnIndex(key);
    }






    /**
     * 获取该列的下标
     * @param column 列名
     * @return 下标
     */
    public int getColumnIndex(String column){
        ArrayList<String> tmp=new ArrayList<>();
        tmp.addAll(Arrays.asList(getColumnName()));
        return tmp.indexOf(column);
    }






    /**
     * <h2>获取下标所在的列的值</h2>
     * <P>使用模拟表格操作需要确保所有列的单元格数量应该相同，否则将无法读取到数据</P>
     * @param columName 列名
     * @param rowIndex 单元格下标
     * @return 读取返回非空值
     */
    public Object getCellAt(String columName, int rowIndex){
        if (rowIndex>= getMinRowCount())
            return null;
        int col=getColumnIndex(columName);
        if (col==-1)
            return null;
        return getCell(rowIndex,col);
    }






    /**
     * <h2>设置下标所在列的值</h2>
     * @param columnName 列名
     * @param rowIndex 行下标
     * @param v 值
     * @return 成功返回true
     */
    public boolean setCellAt(String columnName,int rowIndex,Object v){
        Column column=findColumn(columnName);
        if (column==null)
            return false;
        if (rowIndex>=column.size()||rowIndex<0)
            return false;
        return column.set(rowIndex,v);
    }


    //对表格属性的操作

    public Bean getInfos() {
        return infos;
    }

    /**
     * 向表格属性中添加一个属性节点
     * @param info 属性节点
     * @return 成功返回true
     */
    public boolean addInfo(Info info){
        int oldSize=infos.getSize();
        infos.addInfo(info);
        return infos.getSize()>oldSize;
    }

    public void addInfo(String propertyName,Object value){
        Info info =new Info(propertyName,value);
        infos.addInfo(info);
    }

    public void removeInfo(String propertyName){
        infos.removeInfo(infos.find(propertyName));
    }

    public void setInfosValue(String propertyName,Object value){
        Info info =infos.find(propertyName);
        if (info !=null){
            info.setValue(value);
        }
    }

    public Object getInfosValue(String propertyName){
        Info info = infos.find(propertyName);
        if (info ==null)return null;
        return info.getValue();
    }

    public String[] getInfoPropertyNames(){
        String[] names=new String[infos.getSize()];
        for (int i=0;i<infos.getSize();i++){
            names[i]=infos.getAt(i).getName();
        }
        return names;
    }

    public Object[] getInfoValues(){
        Object[] values=new Object[infos.getSize()];
        for (int i=0;i<infos.getSize();i++){
            values[i]=infos.getAt(i).getValue();
        }
        return values;
    }




    /*
    下面是重写的方法
     */

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("{ ");
        sb.append("DataTable : \""+getName()+"\" \n");
        for (int i=0;i<values.size();i++){
            sb.append(values.get(i).getName()+" : ");
            for (Object o:values.get(i).getValues()){
                sb.append("["+o+"]、");
            }
            sb.append('\n');
        }
        sb.append("rowSize："+values.get(0).size()+"\n");
        sb.append("columnSize："+values.size()+" }\n");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DataTable))
            return false;
        DataTable bean= (DataTable) obj;
        if (bean.getSize()!=this.getSize())
            return false;
        for (Column column :bean.getValues()){
            boolean found=false;
            for (Column tmp:this.getValues()){
                if (column.getName().equals(tmp.getName())){
                    found=true;
                }
            }
            if (!found)
                return false;
        }
        return true;
    }
}
