package dbManager;

import application.Application;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 信息类，存放字符型变量和数字型变量，用于自定义信息
 */
public class Node<T> implements Serializable ,Cloneable{

    /**
     * 类型 1 2 3 4
     */
    private final String[] TYPE_NAME={"","Double","String","Date","ArrayList","Integer"};

    public static final int TYPE_DOUBLE=1;
    public static final int TYPE_STRING=2;
    public static final int TYPE_DATE=3;
    public static final int TYPE_ARRAY_LIST =4;
    public static final int TYPE_INTEGER=5;


    private String name="";
    private T value=null;
    private String type="";
    



    private void init(String name,T value){
        setName(name);
        if (value==null)
            return;
        //获取type
        for (int i=0;i<TYPE_NAME.length;i++){
            if (value.getClass().getName().contains(TYPE_NAME[i])){
                type=TYPE_NAME[i];
            }
        }
        setValue(value);
    }

    //构造
    public Node(){
    }

    public Node(String name){
        init(name,null);
    }

    public Node(int type, String name){
        init(name,null);
        try {
            this.type=TYPE_NAME[type];
        }catch (IndexOutOfBoundsException e){

        }
    }

    public Node(String name, T value){
        init(name,value);
    }



    //方法
    public T getValue(){
        return value;
    }

    public String getValueString(){
        if(value==null)
            return "";
        return value.toString();
    }

    public void setValue(Object object){
        if (object==null)
            return;

        if (type==null||type==""){
            if (value!=null){
                for (int i=0;i<TYPE_NAME.length;i++){
                    if (value.getClass().getName().contains(TYPE_NAME[i])){
                        type=TYPE_NAME[i];
                    }
                }
            }else{
                for (int i=0;i<TYPE_NAME.length;i++){
                    if (object.getClass().getName().contains(TYPE_NAME[i])){
                        type=TYPE_NAME[i];
                    }
                }
            }

            if (getType()==0){
                type="";
                return;
            }
        }

        try{
            switch (getType()){
                case TYPE_STRING:{
                    if (object instanceof String) value = (T) object.toString();
                    break;
                }
                case TYPE_DOUBLE:{
                    if (object instanceof String) value= (T) new Double(String.valueOf(object));
                    if (object instanceof Double||object instanceof Integer) value=(T)object;
                    break;
                }
                case TYPE_DATE:{
                    if (object instanceof String) {
                        Date oldv= (Date) value;
                        try {
                            value=(T)new SimpleDateFormat("yyyy-MM-dd").parse((String) object);
                        } catch (ParseException e) {
                            value= (T) oldv;
                        }
                    }
                    if (object instanceof Date){
                        value= (T) object;
                    }
                    break;
                }
                case TYPE_INTEGER:{
                    if (object instanceof Integer)value= (T) object;
                    if (object instanceof  String) value= (T) new Integer(String.valueOf(object));
                    break;
                }
                case TYPE_ARRAY_LIST:{
                    if (object instanceof List)
                        value= (T) object;
                    break;
                }
            }
        }catch (Exception e){
            Application.debug(this,e.toString());
        }
    }

    public int getType() {
        for(int i=0;i<TYPE_NAME.length;i++){
            if(type.equals(TYPE_NAME[i])){
                return i;
            }
        }
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        if(name!=null||!name.equals("")){
            this.name=name;
        }
    }

    public boolean isShow(){
        if (type.equals("ArrayList")){
            return false;
        }
        return true;
    }

    public boolean equalsValue(T object){
        return object.equals(value);
    }

    /**
     * 获得值的类型
     * @return
     */
    public Type getValueType(){
        if (null==value)
            return null;
        return value.getClass().getGenericSuperclass();
    }

    @Override
    public String toString() {
        if(value==null)
            return name+"："+"NULL";
        return name+"："+value.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().getName().equals(this.getClass().getName()))
            return false;
        Node<T> node =(Node<T>)obj;
        return node.name.equals(this.name)&& node.getType()==this.getType()&&equalsValue(node.getValue());
    }

    /**
     * 移除一个属性值，如果是ArrayList的话
     * @param value
     * @return 移除成功返回true，失败或不是ArrayList返回false
     */
    public boolean removeListValue(String value){
        if (type.equals(TYPE_ARRAY_LIST)){
            ArrayList<String> tmpList= (ArrayList<String>) this.value;
            return tmpList.remove(value);
        }
        return false;
    }

    /**
     * 增加一个属性值，如果该属性是ArrayList的话，属性值不能相等
     * @param value
     * @return 成功返回true 重复或不是ArrayList属性返回false
     */
    public boolean addListValue(String value){
        if (!this.value.getClass().getName().contains(TYPE_NAME[TYPE_ARRAY_LIST]))
            return false;
        ArrayList<String> tmpList= (ArrayList<String>) this.value;
        boolean found=false;
        for (String s:tmpList){
            if (s.equals(value)) found=true;
        }
        if (!found)return tmpList.add(value);else return false;
    }
}
