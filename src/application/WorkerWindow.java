package application;

public class WorkerWindow extends Window{

    public WorkerWindow(){
        getContentPane().add(new WorkerPanel());
    }
}

class SiteWindow extends Window{
    public SiteWindow(){
        setTitle("工地管理");
        SitePanel sitePanel=new SitePanel();
        getContentPane().add(sitePanel);
        sitePanel.loading(null);
    }
}
