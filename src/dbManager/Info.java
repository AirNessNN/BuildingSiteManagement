package dbManager;

import java.io.Serializable;

/**
 * ��Ϣ�࣬����ַ��ͱ����������ͱ����������Զ�����Ϣ
 */
public class Info implements Serializable {

    /**
     * ���� 1 2 3 4
     */
    private final String[] TYPE_NAME={"","Double","String","Date","ArrayList"};

    public static final int TYPE_DOUBLE=1;
    public static final int TYPE_STRING=2;
    public static final int TYPE_DATE=3;
    public static final int TYPE_DATE_LIST=4;


    private String name=null;
    private Object value=null;
    private String type=null;

    private int colWidth=50;



    private void init(int type,String name,Object value){
       setType(type);
        setName(name);
        setValue(value);
    }

    //����
    public Info(){
    }

    public Info(int type,String name){
        init(type,name,null);
    }

    public Info(int type,String name,Object value){
        init(type,name,value);
    }



    //����
    public Object getValue(){
        return value;
    }

    public String getValueString(){
        if(value==null)
            return "";
        return value.toString();
    }

    public void setValue(Object object){
        if(type==null)
            return;
        if(object==null)
            return;
        if(object.getClass().getName().contains(type)){
            value=object;
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

    public void setType(int type){
        try{
            this.type=TYPE_NAME[type];
        }catch (Exception e){
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        if(name!=null||!name.equals("")){
            this.name=name;
        }
    }

    public void setColWidth(int colWidth) {
        this.colWidth = colWidth;
    }

    public int getColWidth() {
        return colWidth;
    }

    public boolean isShow(){
        if (type.equals("ArrayList")){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if(value==null)
            return name+"��"+"NULL";
        return name+"��"+value.toString();
    }

    @Override
    public boolean equals(Object obj) {
        Info info=(Info)obj;

        return info.name.equals(this.name)&&info.getType()==this.getType();
    }
}