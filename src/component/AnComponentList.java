package component;

import javax.swing.*;
import java.awt.*;

public class AnComponentList extends JPanel {

    GridLayout gridLayout=null;


    private void initComponent(){
        this.setLayout(gridLayout);
    }

    public AnComponentList(){
        initComponent();

    }

}
