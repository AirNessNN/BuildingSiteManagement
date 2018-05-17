package component;

import application.AnUtils;

import javax.swing.*;
import java.awt.*;

/**
 * 在窗口上生成一个不可选中的Dialog来提供消息，并且显示之后一段时间后会自行消失
 */
public class AnDialog extends JFrame{
    public static final int LONG_SHOW=3000;
    public static final int SHORT_SHOW=2000;

    /**
     * 显示Dialog信息
     * @param component 父类组件
     * @param text 文本内容
     * @param showTimeEnum 显示时长的枚举值
     */
    public static void show(Component component,String text,int showTimeEnum){

        new Thread(()->{
            try {
                AnDialog dialog;
                dialog=new AnDialog();
                dialog.setText(text);
                dialog.setOpacity(0);
                dialog.setVisible(true);
                dialog.setFocusable(false);
                if (component!=null) component.requestFocus();
                for (float i=0;i<=1;i+=0.1f){
                    dialog.setOpacity(i);
                    Thread.sleep(20);
                }
                Thread.sleep(showTimeEnum);
                for (float i=1;i>0;i-=0.1f){
                    dialog.setOpacity(i);
                    Thread.sleep(20);
                }
                dialog.dispose();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private JLabel labText;
    private AnDialog(){
        setUndecorated(true);
        labText =new JLabel();
        getContentPane().add(labText);
        labText.setFont(new Font("微软雅黑",Font.PLAIN,20));
        labText.setText("");
        labText.setHorizontalAlignment(SwingConstants.CENTER);
        labText.setForeground(Color.white);
        setAlwaysOnTop(true);
        setType(Type.UTILITY);
        getContentPane().setBackground(Color.GRAY);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void setText(String text){
        Dimension textSize=AnUtils.getStringPx(text, labText.getFont());
        Dimension windowSize=Toolkit.getDefaultToolkit().getScreenSize();

        textSize.width+=80;
        textSize.height+=20;

        setBounds((windowSize.width-textSize.width)/2,windowSize.height-150,textSize.width,textSize.height);
        labText.setText(text);
    }
}
