package component;

/**
 * 选择器实现接口
 */
public interface Chooser {
    String[] addEvent();

    boolean newEvent(String[] values,String newValue);

    void done(String[] values);

    String[] getAddText();

    String[] getNewText();
}
