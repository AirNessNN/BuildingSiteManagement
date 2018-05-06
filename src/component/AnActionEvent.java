package component;

/**
 * An按键监听事件
 */
public class AnActionEvent {
    public static final int CILCKED=0;
    public static final int ENTERED=1;
    public static final int PRESSED=2;
    public static final int RELEASEED=3;
    public static final int EXITED=4;

    private Object source;
    private int action;
    private String tag;

    public AnActionEvent(Object source,int action,String tag){
        this.source=source;
        this.action=action;
        this.tag=tag;
    }

    public int getAction() {
        return action;
    }

    public Object getSource() {
        return source;
    }

    public String getTag() {
        return tag;
    }
}
