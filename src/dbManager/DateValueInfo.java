package dbManager;

import application.AnUtils;
import component.IDateValueItem;

import java.io.Serializable;
import java.util.Date;

/**
 * 日期和值的包装类
 */
public class DateValueInfo implements Serializable, IDateValueItem {
    private Date date;
    private Object value;
    private String tag;

    public DateValueInfo(Date date,Object value,String tag){
        this.date=date;
        this.value=value;
        this.tag=tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    void setDate(Date date){
        this.date=date;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IDateValueItem){
            IDateValueItem item= (IDateValueItem) obj;
            return AnUtils.isDateYMDEquality(date,item.getDate());
        }
        return false;
    }
}
