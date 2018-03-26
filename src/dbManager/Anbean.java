package dbManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Anbean implements Serializable {

    private ArrayList<Info> valueList=null;





    private void init(){
        valueList=new ArrayList<>();
    }


    public Anbean(){
        init();
    }


    public void setValueList(Info[] arr){
        if(valueList!=null){
            for(Info info:arr){
                if(!valueList.contains(info)){
                    valueList.add(info);
                }
            }
        }
    }

    public void addInfo(Info info){
       if(valueList==null)
           return;
       if(valueList.contains(info))
           return;
       valueList.add(info);
    }

    public void removeInfo(Info info){
        if(valueList==null)
            return;
        valueList.remove(info);
    }

    public Info getAt(int index){
        if(valueList!=null){
            return valueList.get(index);
        }
        return null;
    }

    public Info get(String name){
        if(valueList!=null){
            for(Info info:valueList){
                if(info.getName().equals(name)){
                    return info;
                }
            }
        }
        return null;
    }

    public Info[] getArray(){
        if(valueList!=null){
            Info[] infoList=new Info[valueList.size()];
            for(int i=0;i<valueList.size();i++){
                infoList[i]=valueList.get(i);
            }
            return infoList;
        }
        return null;
    }

    public int getSize(){
        if(valueList==null)
            return 0;
        return valueList.size();
    }

    public ArrayList<Info> getValueList() {
        return valueList;
    }
}