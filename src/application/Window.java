package application;

import javax.swing.*;

public class Window extends JFrame{

    public Window(){
        setSize(1000,800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public Window(String name){
        this.setTitle(name);
        setSize(1000,800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
