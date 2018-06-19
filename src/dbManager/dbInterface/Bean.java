package dbManager.dbInterface;

import dbManager.Info;

/**
 * Bean的属性集合接口
 */
public interface Bean {
    //增加node
    boolean addNode(Info info);
    boolean insertNode(int index, Info info);

    //移除node
    boolean removeNode(Info info);
    boolean removeNodeAt(int index);

    //查找node
    Info findNode(String propertyName);
    Info get(int index);

    //查找node下标
    int nodeOf(Info info);
    int nodeOf(String propertyName);

    //设置node
    boolean set(int index, Info info);

    Info[] toArray();
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
