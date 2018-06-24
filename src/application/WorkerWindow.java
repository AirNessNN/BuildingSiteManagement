package application;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WorkerWindow extends Window{



    public WorkerWindow(){
        setTitle("工人管理");
        getContentPane().add(new WorkerPanel());
        setSize(1000,680);
        setMinimumSize(getSize());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                callback( );
                WindowBuilder.closeWorkWindow();
            }
        });
    }
}

class SiteWindow extends Window{

    SitePanel sitePanel;

    public SiteWindow(){
        setTitle("工地管理");
        setSize(1000,680);
        setMinimumSize(getSize());
        sitePanel= new SitePanel();
        getContentPane().add(sitePanel);
        sitePanel.loading(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                callback((Object) null);
            }
        });
    }

    public void refash(){
        sitePanel.refash();
    }
}
