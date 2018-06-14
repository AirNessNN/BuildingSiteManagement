package component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AnimButton  extends JButton {

    private final Object lock=new Object();
    private int rand=0;
    private boolean entered =false;

    private void init(){
        new Thread(()->{
            //noinspection InfiniteLoopStatement
            while(true){
                synchronized (lock){
                    if (entered){
                        if (rand>30) {
                            try {
                                rand=30;
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        rand+=2;
                    }else {
                        if (rand<0) {
                            try {
                                rand=0;
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        rand-=2;
                    }
                }

                repaint();
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                synchronized (lock){
                    entered=true;
                    lock.notify();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                synchronized (lock){
                    entered=false;
                    lock.notify();
                }
            }
        });
    }


    public AnimButton(){
        setBorder(null);
        init();
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d= (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        BasicStroke stroke=new BasicStroke(2);
        g2d.setStroke(stroke);
        g2d.drawRoundRect(0,0,getWidth()-1,getHeight()-1,rand,rand);
        g.dispose();
    }
}
