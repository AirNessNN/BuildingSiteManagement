package dbManager;

/**
 * 增删改查接口，为DB的增删改查提供标准
 */
public interface IRevisable {
    void addValue(Object value);

    void removeValue(Object value);

    void removeValueAt(Object value);
}
