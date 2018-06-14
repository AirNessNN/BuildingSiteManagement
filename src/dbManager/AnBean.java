package dbManager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 基本的信息Bean，封装一个拥有属性名称和属性值的集合
 */
public class AnBean implements Serializable {

    private ArrayList<Node> valueList=null;

    private String beanName="";





    private void init(){
        valueList=new ArrayList<>();
    }


    public AnBean(){
        init();
    }

    public AnBean(String beanName){
        init();
        this.beanName=beanName;
    }


    /**
     * 填充Bean值数组
     * @param arr
     */
    public void setList(Node[] arr){
        if(valueList!=null){
            for(Node node :arr){
                if(!valueList.contains(node)){
                    valueList.add(node);
                }
            }
        }
    }

    /**
     * 添加Info对象
     * @param node
     */
    public void addInfo(Node node){
       if(valueList==null)
           return;
       if(valueList.contains(node))
           return;
       valueList.add(node);
    }

    /**
     * 移除Info对象
     * @param node
     */
    public void removeInfo(Node node){
        if(valueList==null)
            return;
        valueList.remove(node);
    }

    /**
     * 从Index处获取到Info实例
     * @param index
     * @return
     */
    public Node getAt(int index){
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
    public Node find(String name){
        if(valueList!=null){
            for(Node node :valueList){
                if(node.getName().equals(name)){
                    return node;
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
        for (Node node :valueList){
            if (node.getName().equals(name)){
                node.setValue(value);
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
    public Node[] getArray(){
        if(valueList!=null){
            Node[] nodeList =new Node[valueList.size()];
            for(int i=0;i<valueList.size();i++){
                nodeList[i]=valueList.get(i);
            }
            return nodeList;
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
    public ArrayList<Node> getValueList() {
        return valueList;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        for (Node node :getArray()){
            sb.append(node.toString()+"  ");
        }
        return sb.toString();
    }
}
