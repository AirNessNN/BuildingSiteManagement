package component;

import animation.AnimationManager;
import animation.Iterator;
import application.AnUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class AnAnimButton extends JLabel {


    private Point textBounds;

    private Image backgroundImage=null;

    private Point boxPoint=new Point(0,0);

    private Image boxImage=null;

    private Iterator iterator=null;




    private void init(){

        setOpaque(false);
        setBackground(Color.BLACK);
        setForeground(Color.red);
        repaint();

        if (getSize().width>0&&getSize().height>0)
            iterator=AnimationManager.getManager().createAnimationIterator(getWidth(),new Float((float) getWidth() *0.6f).intValue(),1);

        boxPoint.x=new Float((float) getWidth() *0.6f).intValue();
        boxPoint.y=0;

        if (iterator!=null)
            iterator.setCallback(value-> {
                //System.out.println(value);
                //boxPoint.x=value;
                //repaint();
            });

        new Thread(()->{
            while(true){
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
                if (iterator!=null){iterator.start();iterator.reverse();}
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (iterator!=null){iterator.start();iterator.reverse();}
            }
        });


    }


    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        if (iterator!=null)iterator.dispose();
        init();
    }

    public AnAnimButton(){
        init();
    }

    public AnAnimButton(String text){
        setText(text);
        init();
    }


    public void setTextBounds(int x,int y) {
        this.textBounds = new Point(x,y);
    }

    @Override
    public void update(Graphics g) {
        BufferedImage image=new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d=image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        paint(g2d);
        g.drawImage(image,0,0,null);
        g2d.dispose();
        g.dispose();
    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getBackground());
        g.fillRect(0,0,getWidth(),getHeight());

        if (backgroundImage!=null)
            g.drawImage(backgroundImage,0,0,null);

        if (boxImage!=null)
            g.drawImage(boxImage,boxPoint.x,0,null);

        g.drawRect(10,10,20,20);
        g.setFont(getFont());
        g.setColor(getForeground());
        if (getText()!=null)
            g2d.drawString(getText(),0,20);
        Dimension dimension=AnUtils.getStringPx(getText(),getFont());

        BasicStroke bs2=new BasicStroke(2);
        g2d.setStroke(bs2);

        g2d.drawRect(0,-0,dimension.width,dimension.height);
        g2d.drawRoundRect(0,0,getWidth()-1,getHeight()-1,30,30);

        g.dispose();
    }
}
