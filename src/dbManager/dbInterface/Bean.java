package dbManager.dbInterface;

import dbManager.Node;

import java.util.Collection;

/**
 * Bean的属性集合接口
 */
public interface Bean {
    //增加node
    boolean addNode(Node node);
    boolean insertNode(int index,Node node);

    //移除node
    boolean removeNode(Node node);
    boolean removeNodeAt(int index);

    //查找node
    Node findNode(String propertyName);
    Node get(int index);

    //查找node下标
    int nodeOf(Node node);
    int nodeOf(String propertyName);

    //设置node
    boolean set(int index,Node node);

    Node[] toArray();
    int size();

    //设置查找到的node值
    boolean setValue(int index,Object value);
    boolean setValue(String propertyName,Object value);

    //获取查找到的node值
    Object getValue(int index);
    Object getValue(String propertyName);

    boolean addValue(int nodeIndex,Object value);
    boolean addValue(String propertyName,Object value);

    //移除列表型的Node的值
    boolean removeValue(int nodeIndex,Object nodeValue);
    boolean removeValue(String propertyName,Object value);

    void clear();
}
