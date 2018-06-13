package animation;

public interface Iterator {
    /**
     * 迭代
     */
    void update();

    /**
     * 获取更新后的值
     * @return 迭代值
     */
    int getUpdateValue();

    /**
    重置迭代器到初始状态
     */
    void reset();

    /**
     * 倒置迭代，调用两次恢复正常状态
     */
    void reverse();

    /**
     * 返回是否是倒置状态
     * @return
     */
    boolean isReverse();

    void stop();

    void start();

    void setCallback(ValueCallback callback);

    void dispose();
}
