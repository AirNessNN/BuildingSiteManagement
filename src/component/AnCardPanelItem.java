package component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class AnCardPanelItem {

    AnCardPanel.OnSelectedListener I=null;

    //附带的容器
    private JPanel panel=null;

    //内建按钮
    private Button button=null;

    //信息
    public String TAG=this.getClass().getName();


    private void init(ImageIcon normal,ImageIcon enter,ImageIcon selected,boolean b){
        panel=new JPanel(null);
        panel.setLocation(0,0);
        button=new Button(b);
        button.setNormalImage(normal);
        button.setEnterImage(enter);
        button.setSelectedImage(selected);
        button.setActionListener((e)->{
            if(I!=null){
                I.onSelected(this);
            }
        });

    }


    public AnCardPanelItem(){
        panel=new JPanel(null);
        panel.setLocation(0,0);
        button=new Button(false);
        button.setActionListener((e)->{
            if(I!=null){
                I.onSelected(this);
            }
        });
    }

    public AnCardPanelItem(ImageIcon normal,ImageIcon enter,ImageIcon selected){
        init(normal,enter,selected ,false);
    }



    public void add(Component c){
        if(panel!=null){
            panel.add(c);
        }
    }

    public void remove(Component c){
        if(panel!=null){
            panel.remove(c);
        }
    }


    public void setPanel(JPanel panel){
        if(panel==null)
            return;

        this.panel=panel;
        /*if(this.panel.getSize().width!=0&&this.panel.getSize().height!=0){
            panel.setSize(this.panel.getSize());
            this.panel=panel;
            this.panel.setLocation(0,0);
            return;
        }
        this.panel=panel;
        this.panel.setLocation(0,0);
        this.panel.setSize(0,0);*/
        panel.repaint();
    }


    public void setPanelSize(Dimension dimension){
        if(dimension==null)
            return;
        panel.setSize(dimension);
    }

    public void setNormalImage(ImageIcon normalImage){
        button.setNormalImage(normalImage);
    }
    public void setSelectedImage(ImageIcon selectedImage){
        button.setSelectedImage(selectedImage);
    }
    public void setEnterImage(ImageIcon enterImage){
        button.setEnterImage(enterImage);
    }

    public void setPanelVisable(boolean b){
        if(panel!=null){
            panel.setVisible(b);
        }
    }




    public void cancel(){
        button.setSelected(false);
    }

    public JPanel getPanel(){
        return this.panel;
    }

    public Button getButton(){
        return this.button;
    }








    /**
     * 受支持的按钮
     */
    class Button extends JLabel implements MouseListener{

        private ImageIcon normal,selected,enter;

        private AnActionListener I=null;

        private boolean isSelected=false;

        @Override
        public void mouseClicked(MouseEvent e) {
            isSelected=true;
            if(selected==null){
                return;
            }
            setIcon(selected);
            if(I!=null){
            	AnActionEvent event=new AnActionEvent(this, AnActionEvent.CILCKED, this.getText());
                I.actionPerformed(event);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(!isSelected&&enter!=null){
                setIcon(enter);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(!isSelected&&normal!=null){
                setIcon(normal);
            }
        }


        /**
         * 私有构造
         * @param isSelected
         */
        private Button(boolean isSelected){
            this.isSelected=isSelected;
            addMouseListener(this);
        }

        private Button(ImageIcon normal,ImageIcon selected,ImageIcon enter){
            this.enter=enter;
            this.normal=normal;
            this.selected=selected;
        }





        public void setSelected(boolean b){
            this.isSelected=b;
            if(b&&selected!=null){
                setIcon(selected);
                return;
            }
            if(normal!=null){
                setIcon(normal);
            }
        }


        public void setActionListener(AnActionListener I){
            this.I=I;
        }



        public void setNormalImage(ImageIcon normal){
            this.normal=normal;
            setIcon(normal);
        }
        public void setEnterImage(ImageIcon enter){
            this.enter=enter;
        }

        public void setSelectedImage(ImageIcon selected){
            this.selected=selected;
        }
    }
}
