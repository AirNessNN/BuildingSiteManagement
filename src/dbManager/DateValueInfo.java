package dbManager;

import java.io.Serializable;
import java.util.Date;

public class DateValueInfo implements Serializable {
    public Date date;
    public Object value;

    public DateValueInfo(Date date,Object value){
        this.date=date;
        this.value=value;
    }
}
