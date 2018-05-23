package application;
import component.ComponentLoader;

import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.Font;

/**
 * 详细信息窗口
 */
public class InfoWindow extends Window implements ComponentLoader {

	public InfoWindow() {
        initializeComponent();
        initializeEvent();
        initializeData();
	}

    @Override
    public void initializeComponent() {
        setTitle("详细信息");
        getContentPane().setLayout(new BorderLayout(0, 0));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("幼圆", Font.PLAIN, 15));
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("个人信息", null, panel, null);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("敏感数据", null, panel_1, null);
    }

    @Override
    public void initializeEvent() {

    }

    @Override
    public void initializeData() {

    }
}
