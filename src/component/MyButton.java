package component;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyButton extends JButton implements MouseListener {
    private static final long serialVersionUID = 4090165624901676063L;

    private Color normal =new Color(125, 125, 125);

    public void setNormal(Color normal) {
        this.normal = normal;
    }

    private Color press=new Color(0, 160, 233);

    public void setPress(Color press) {
        this.press = press;
    }

    private Color enter=new Color(0, 120, 215);

    public void setEnter(Color enter) {
        this.enter = enter;
    }

    public MyButton(String tex) {
        this.setText(tex);
        this.setFont(new Font("等线", Font.PLAIN, 14));
        this.setUI(new BasicButtonUI());
        Border b = new LineBorder(normal, 2);
        this.setBorder(b);
        this.addMouseListener(this);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (isEnabled()){
            Border b = new LineBorder(press, 2);
            this.setBorder(b);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isEnabled()){
            Border b = new LineBorder(enter, 2);
            this.setBorder(b);
        }
    }

    public void mouseEntered(MouseEvent e) {
        if (isEnabled()){
            Border b = new LineBorder(enter, 2);
            this.setBorder(b);
        }
    }

    public void mouseExited(MouseEvent e) {
        if (isEnabled()){
            Border b = new LineBorder(normal, 2);
            this.setBorder(b);
        }
    }
}
