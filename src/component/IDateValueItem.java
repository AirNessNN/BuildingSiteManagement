package component;

import java.util.Date;

/**
 * AnDateValuePanel数据模型接口
 */
public interface IDateValueItem {
    Date getDate();
    Object getValue();
    String getTag();
    void setValue(Object value);
}
