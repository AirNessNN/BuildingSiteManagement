package component;

/**
 * An组件接口
 */
public interface ComponentLoader {
    void initializeComponent();
    void initializeEvent();
    void initializeData(Object... args);
}
