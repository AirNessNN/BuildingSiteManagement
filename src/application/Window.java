package application;

import javax.swing.*;

public class Window extends JFrame{
    private CloseCallback callback=null;

    public Window(){
        setSize(1000,800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public Window(String name){
        this.setTitle(name);
        setSize(1000,800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }


    /**
     * 设置窗口关闭回调
     * @param callback
     */
    public void setCallback(CloseCallback callback){
        this.callback=callback;
    }

    /**
     * 执行回调
     * @param values
     */
    public void  callback(Object... values){
        if (callback!=null)
            callback.callback(values);
    }
}
