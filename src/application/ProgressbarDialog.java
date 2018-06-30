package application;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ProgressbarDialog extends JDialog {

    private static ProgressbarDialog dialog;


	private ProgressBar progressBar;
	private JLabel labTitle;
	private JLabel labInfo;

	private int minValue=0;
	private int maxValue=100;
	private int value=0;

    private ProgressbarDialog(){
        setAlwaysOnTop(true);
    	getContentPane().setBackground(Color.WHITE);
    	getContentPane().setForeground(Color.WHITE);
        setUndecorated(true);
        getContentPane().setLayout(null);
        setSize(452,132);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.white);
        setBackground(Color.GRAY);
        setForeground(Color.LIGHT_GRAY);
        
        labTitle = new JLabel("进程正忙");
        labTitle.setFont(new Font("等线", Font.PLAIN, 20));
        labTitle.setBounds(10, 10, 430, 37);
        getContentPane().add(labTitle);
        
        progressBar = new ProgressBar();
        progressBar.setBounds(10, 93, 430, 29);
        getContentPane().add(progressBar);
        
        labInfo = new JLabel("");
        labInfo.setForeground(Color.GRAY);
        labInfo.setFont(new Font("等线", Font.PLAIN, 15));
        labInfo.setBounds(10, 66, 430, 17);
        getContentPane().add(labInfo);
    }

    public static void showDialog(String title,int minValue,int maxValue){
        if (dialog==null)dialog=new ProgressbarDialog();
        if (dialog.isVisible()) {
            dialog.setVisible(false);
            dialog=new ProgressbarDialog();
        }
        dialog.maxValue=maxValue;
        dialog.minValue=minValue;
        dialog.labTitle.setText(title);
        dialog.setVisible(true);
    }

    public static void setState(String info,int progress){
        dialog.labInfo.setText(info);
        dialog.value=progress;
        dialog.progressBar.setState(progress);
    }


    private class ProgressBar extends Canvas{

        final Object lock=new Object();

        int value=0;
        int index=0;

        Thread thread=null;

        Runnable runnable= () -> {
            while(true){

                synchronized (lock){
                    if (index==maxValue)return;
                    if (index==value) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                index++;
                repaint();
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        void setState(int state){
            value=state;
            if (thread==null){
                thread=new Thread(runnable);
                thread.start();
            }
            synchronized (lock){
                lock.notify();
            }
        }

        @Override
        public void update(Graphics g) {
            BufferedImage image=new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d=image.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            paint(g2d);
            g2d.dispose();
            g.drawImage(image,0,0,null);
        }

        @Override
        public void paint(Graphics g) {
            g.setColor(dialog.getContentPane().getBackground());
            g.fillRect(0,0,getWidth(),getHeight());

            g.setColor(dialog.getBackground());
            g.fillRoundRect(0,0,getWidth(),getHeight(),30,30);
            g.setColor(dialog.getForeground());
            double d1=index;
            double d2=maxValue;
            double d3=d1/d2;
            double recWidth=getWidth()*d3;
            g.fillRoundRect(0,0,new Double(recWidth).intValue(),getHeight(),30,30);
        }
    }
}
