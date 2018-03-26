package compoent;

import resource.Resource;

import java.awt.*;

import javax.swing.*;

public class ImagePanel extends JPanel{

    private ImageIcon icon=null;


    private void init(){
        this.setLayout(null);
    }


	public ImagePanel() {
        init();
	}

	public ImagePanel(ImageIcon icon){
        init();
        setIcon(icon);
    }



	public void setIcon(ImageIcon icon){
	    this.icon=icon;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(getIcon()==null)
            return;
        g.drawImage(getIcon().getImage(),0,0,this.getWidth(),this.getHeight(),null);
    }
}
