package component;

import resource.Resource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AnPasswordField extends JPasswordField {

    private static final long serialVersionUID = 1L;

    private int maxLength=20;
    public void setMaxLength(int value) {
        if(value>0) {
            maxLength=value;
        }
    }

    public int getMaxLength() {
        return maxLength;
    }

    public AnPasswordField(){
        setBorder(BorderFactory.createLineBorder(Resource.COLOR_LIGHT_BLUE,1));
        setFont(new Font(Resource.FONT_WEI_RUAN_YA_HEI,Font.PLAIN,14));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                setNormalBorder();
                int code=e.getKeyCode();
                if(code==KeyEvent.VK_BACK_SPACE) {
                    e.isActionKey();
                }else if(getDocument().getLength()>=maxLength){
                    e.consume();
                }
            }
        });
    }

    public void setBorderColor(Color c) {
        setBorder(BorderFactory.createLineBorder(c, 1));
    }

    public void setErrorBorder() {
        setBorder(BorderFactory.createLineBorder(Color.red, 1));
    }

    public void setNormalBorder() {
        setBorder(BorderFactory.createLineBorder(Resource.COLOR_LIGHT_BLUE, 1));
    }
}
