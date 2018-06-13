package component;

import application.AnUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.SynchronousQueue;

/**
 * 在窗口上生成一个不可选中的Dialog来提供消息，并且显示之后一段时间后会自行消失
 */
public class AnPopDialog extends JFrame{
    public static final int LONG_TIME =3000;
    public static final int SHORT_TIME =2000;
    private static int x=0;
    private static int y=0;

    private static Queue<String> texts=new SynchronousQueue<>();
    private static Queue<Component> components=new SynchronousQueue<>();
    private static Queue<Integer> integers=new SynchronousQueue<>();

    volatile private boolean running=false;

    /**
     * 显示Dialog信息
     * @param component 父类组件
     * @param text 文本内容
     * @param showTimeEnum 显示时长的枚举值
     */
    public static void show(Component component,String text,int showTimeEnum){
        new Thread(()->{
            try {
                AnPopDialog dialog;
                dialog=new AnPopDialog();
                dialog.setText(text);
                dialog.setOpacity(0);
                dialog.setVisible(true);
                dialog.setFocusable(false);
                dialog.setLocation(x,y-150);
                if (component!=null) component.requestFocus();
                for (float i=0;i<=1;i+=0.1f){
                    dialog.setOpacity(i);
                    //dialog.setLocation(x,y-(int)(i*10*15));
                    Thread.sleep(25);
                }
                Thread.sleep(showTimeEnum);
                for (float i=1;i>0;i-=0.1f){
                    dialog.setOpacity(i);
                    //dialog.setLocation(x,y-(int)(i*10*15));
                    Thread.sleep(25);
                }
                dialog.dispose();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private JLabel labText;
    private AnPopDialog(){
        setUndecorated(true);
        labText =new JLabel();
        getContentPane().add(labText);
        labText.setFont(new Font("微软雅黑",Font.PLAIN,20));
        labText.setText("");
        labText.setHorizontalAlignment(SwingConstants.CENTER);
        labText.setForeground(Color.white);
        setAlwaysOnTop(true);
        setType(Type.UTILITY);
        getContentPane().setBackground(new Color(0, 146, 128));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void setText(String text){
        Dimension textSize=AnUtils.getStringPx(text, labText.getFont());
        Dimension windowSize=Toolkit.getDefaultToolkit().getScreenSize();

        textSize.width+=80;
        textSize.height+=20;
        x=(windowSize.width-textSize.width)/2;
        y=windowSize.height;

        setBounds(x,y,textSize.width,textSize.height);
        labText.setText(text);
    }
}
