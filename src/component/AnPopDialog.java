package component;

import application.AnUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * 在窗口上生成一个不可选中的Dialog来提供消息，并且显示之后一段时间后会自行消失
 */
public class AnPopDialog extends JFrame{
    public static final int LONG_TIME =3000;
    public static final int SHORT_TIME =2000;
    private static int x=0;
    private static int y=0;

    private static LinkedBlockingQueue<String> texts=new LinkedBlockingQueue<>();
    private static LinkedBlockingQueue<Component> components=new LinkedBlockingQueue<>();
    private static LinkedBlockingQueue<Integer> integers=new LinkedBlockingQueue<>();


    private static Thread thread=null;

    private static Runnable runnable= () -> {
        try {
            while(true){

                String tmpText=texts.take();
                Component tmpCom=components.take();
                Integer tmpTime= integers.take();

                AnPopDialog dialog;
                dialog=new AnPopDialog();
                dialog.setText(tmpText);
                dialog.setOpacity(0);
                dialog.setVisible(true);
                dialog.setFocusable(false);
                dialog.setLocation(x,y-150);
                if (tmpCom!=null) tmpCom.requestFocus();
                for (float i=0;i<=1;i+=0.1f){
                    if (!dialog.isVisible())break;
                    dialog.setOpacity(i);
                    Thread.sleep(25);
                }
                if (!dialog.isVisible())continue;
                for (long time=System.currentTimeMillis(), endTime=0L;(endTime-time)<tmpTime;endTime=System.currentTimeMillis()){
                    //阻塞该线程，除非窗口停止
                    if (dialog.isDispose)break;
                }
                for (float i=1;i>0;i-=0.1f){
                    if (!dialog.isVisible())break;
                    dialog.setOpacity(i);
                    Thread.sleep(25);
                }
                if (!dialog.isVisible())continue;
                dialog.dispose();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    };

    /**
     * 显示Dialog信息
     * @param component 父类组件
     * @param text 文本内容
     * @param showTimeEnum 显示时长的枚举值
     */
    public static void show(Component component,String text,int showTimeEnum){
        try {
            texts.put(text);
            if (component==null)components.offer(new Container());
            else components.offer(component);
            integers.offer(showTimeEnum);

            if (thread==null){
                thread=new Thread(runnable);
                thread.start();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private JLabel labText;
    private boolean isDispose=false;
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
        labText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                isDispose=true;
            }
        });
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
