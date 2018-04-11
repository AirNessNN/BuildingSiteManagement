package dbManager;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * 信息类，存放字符型变量和数字型变量，用于自定义信息
 */
public class Info<T> implements Serializable ,Cloneable{

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
    public Info(){
    }

    public Info(String name){
        init(name,null);
    }

    public Info(int type,String name){
        init(name,null);
        try {
            this.type=TYPE_NAME[type];
        }catch (IndexOutOfBoundsException e){

        }
    }

    public Info(String name,T value){
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

    public void setValue(T object){
        if (object==null)
            return;
        value=object;
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
        Info<T> info=(Info<T>)obj;
        return info.name.equals(this.name)&&info.getType()==this.getType()&&equalsValue(info.getValue());
    }
}
