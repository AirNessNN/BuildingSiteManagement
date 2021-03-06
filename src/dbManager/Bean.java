package dbManager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 基本的信息Bean，封装一个拥有属性名称和属性值的集合
 */
public class Bean implements Serializable {

    private ArrayList<Info> valueList=null;

    private String beanName="";





    private void init(){
        valueList=new ArrayList<>();
    }


    public Bean(){
        init();
    }

    public Bean(String beanName){
        init();
        this.beanName=beanName;
    }


    /**
     * 填充Bean值数组
     * @param arr
     */
    public void setList(Info[] arr){
        if(valueList!=null){
            for(Info info :arr){
                if(!valueList.contains(info)){
                    valueList.add(info);
                }
            }
        }
    }

    /**
     * 添加Info对象
     * @param info
     */
    public void addInfo(Info info){
       if(valueList==null)
           return;
       if(valueList.contains(info))
           return;
       valueList.add(info);
    }

    /**
     * 移除Info对象
     * @param info
     */
    public void removeInfo(Info info){
        if(valueList==null)
            return;
        valueList.remove(info);
    }

    /**
     * 从Index处获取到Info实例
     * @param index
     * @return
     */
    public Info getAt(int index){
        if(valueList!=null){
            return valueList.get(index);
        }
        return null;
    }

    /**
     * 通过Info的名字标签获取对象实例
     * @param name
     * @return
     */
    public Info find(String name){
        if(valueList!=null){
            for(Info info :valueList){
                if(info.getName().equals(name)){
                    return info;
                }
            }
        }
        return null;
    }

    /**
     *  通过名字标签放置Info的值
     * @param name
     * @param value
     * @return
     */
    public boolean putValueTo(String name,Object value){
        for (Info info :valueList){
            if (info.getName().equals(name)){
                info.setValue(value);
                return true;
            }
        }
        return false;
    }


    /**
     * 设置Bean的名称
     * @param beanName
     */
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    /**
     * 获取Bean的名称
     * @return
     */
    public String getBeanName() {
        return beanName;
    }

    /**
     * 将Bean转化成数组
     * @return
     */
    public Info[] getArray(){
        if(valueList!=null){
            Info[] infoList =new Info[valueList.size()];
            for(int i=0;i<valueList.size();i++){
                infoList[i]=valueList.get(i);
            }
            return infoList;
        }
        return null;
    }

    /**
     * 清空所有节点
     */
    public void clear(){
        if (valueList!=null)
            valueList.clear();
    }

    /**
     * 返回Bean元素的数量
     * @return
     */
    public int getSize(){
        if(valueList==null)
            return 0;
        return valueList.size();
    }

    /**
     * 返回Bean的ArrayLis实例
     * @return
     */
    public ArrayList<Info> getValueList() {
        return valueList;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        for (Info info :getArray()){
            sb.append(info.toString()+"  ");
        }
        return sb.toString();
    }
}
