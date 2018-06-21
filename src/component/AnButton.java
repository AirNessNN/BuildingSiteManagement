package component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * 带特效的普通按钮
 */
public class AnButton  extends JLabel implements MouseListener,Runnable{

    private ArrayList<ActionListener> actionListeners=new ArrayList<>();//监听器

    private Color borderColor=new Color(97, 92, 95);
    private Color borderPressColor=new Color(16, 205, 207);
    private Color borderEnterColor =new Color(28, 138, 145);
    private Color paintBorderColor=borderColor;

    private Color disabledBorder=new Color(150, 150, 150);
    private Color disabledBackground=new Color(209, 209, 209);
    private Color disabledForeground =new Color(149, 141, 146);

    private int roundValue=0;
    private volatile boolean entered=false;
    private int iterator =1;
    private volatile boolean running=false;

    private int round=18;

    private final Object lock=new Object();//线程锁

    private Thread thread=null;


    private void init(){
        setFont(new Font("等线",Font.PLAIN,14));
        setOpaque(false);
        setHorizontalAlignment(CENTER);
        thread=new Thread(this);
        thread.start();
        addMouseListener(this);

        setBackground(Color.white);
        setForeground(Color.BLACK);
    }

    public AnButton(){
        init();
    }

    public AnButton(String text){
        setText(text);
        init();
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        paintBorderColor=borderColor;
    }

    public void setBorderEnterColor(Color borderEnterColor) {
        this.borderEnterColor = borderEnterColor;
    }

    public void setBorderPressColor(Color borderPressColor) {
        this.borderPressColor = borderPressColor;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void addActionListener(ActionListener l){
        if (l!=null)
            actionListeners.add(l);
    }

    public void removeActionListener(ActionEvent l){
        actionListeners.remove(l);
    }

    public void clearListeners(){
        actionListeners.clear();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d= (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);



        if (!isEnabled())g2d.setColor(disabledBackground);
        else  g2d.setColor(getBackground());
        g2d.fillRoundRect(2,2,getWidth()-4,getHeight()-4,roundValue,roundValue);


        if (!isEnabled())g2d.setColor(disabledForeground);
        else g2d.setColor(getForeground());
        Font font=getFont();
        g2d.setFont(font);
        FontRenderContext context=g2d.getFontRenderContext();
        Rectangle2D stringBounds=font.getStringBounds(getText(),context);
        int x= (int) ((getWidth()-stringBounds.getWidth())/2);
        int y= (int) ((getHeight()/2)+(stringBounds.getHeight()/2)-2);

        g2d.drawString(getText(),x,y);

        g2d.dispose();


    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d= (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        if (!isEnabled()){
            g2d.setColor(disabledBorder);
            g2d.drawRoundRect(1,1,getWidth()-3,getHeight()-3,0,0);
            g2d.dispose();
            return;
        }

        g2d.setColor(paintBorderColor);
        BasicStroke stroke=new BasicStroke(2);
        g2d.setStroke(stroke);
        g2d.drawRoundRect(1,1,getWidth()-3,getHeight()-3,roundValue,roundValue);
        g2d.dispose();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (isEnabled()){
            ActionEvent actionEvent=new ActionEvent(this,this.getClass().hashCode(),this.getClass().getName());
            for (ActionListener l:actionListeners){
                l.actionPerformed(actionEvent);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(isEnabled()){
            paintBorderColor=borderPressColor;
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isEnabled()){
            if (entered)paintBorderColor= borderEnterColor;
            else paintBorderColor=borderColor;
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (isEnabled()){
            entered=true;
            paintBorderColor= borderEnterColor;
            if (running)return;
            synchronized (lock){
                lock.notify();
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (isEnabled()){
            entered=false;
            paintBorderColor=borderColor;
        }
    }

    @Override
    public void run() {
        while(true){
            synchronized (lock){
                try {
                    running=true;
                    if (entered){
                        if (roundValue>round){
                            roundValue=round;
                            repaint();
                            Thread.sleep(16);
                            continue;
                        }
                        roundValue+= iterator;
                    }else {
                        if (roundValue<0){
                            roundValue=0;
                            running=false;
                            lock.wait();
                        }
                        roundValue-= iterator;
                    }
                    repaint();
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
