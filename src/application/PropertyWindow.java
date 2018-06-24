package application;

import component.*;
import dbManager.Column;
import dbManager.DBManager;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * <h2>属性窗口</h2>
 * <li>管理工人的自定义属性</li>
 */
public class PropertyWindow extends JFrame implements  ComponentLoader {


    private DBManager manager;//管理器
    private AnList<AnListRenderModel> list;//列表
    private AnButton btnAdd;
    private AnButton btnDelete;
    private AnButton btnAlert;




    public PropertyWindow(){
    	getContentPane().setBackground(Color.WHITE);
        initializeComponent();
        initializeEvent();
        initializeData();
    }


    @Override
    public void initializeComponent() {
        setTitle("属性修改");
        setSize(600,500);
        setLocationRelativeTo(null);
        setMinimumSize(getSize());
        SpringLayout springLayout = new SpringLayout();
        getContentPane().setLayout(springLayout);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        JScrollPane scrollPane = new JScrollPane();
        springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -10, SpringLayout.SOUTH, getContentPane());
        getContentPane().add(scrollPane);
        
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        springLayout.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.WEST, panel);
        springLayout.putConstraint(SpringLayout.NORTH, panel, 0, SpringLayout.NORTH, scrollPane);
        
        list = new AnList(new AnInfoCellRenderer());
        list.setFixedCellHeight(60);
        list.setFont(new Font("等线", Font.PLAIN, 15));
        scrollPane.setViewportView(list);
        springLayout.putConstraint(SpringLayout.WEST, panel, -150, SpringLayout.EAST, getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, panel, -10, SpringLayout.SOUTH, getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, panel, -10, SpringLayout.EAST, getContentPane());
        getContentPane().add(panel);
        panel.setLayout(new GridLayout(13, 0, 0, 5));
        
        btnAdd = new AnButton("增加属性");
        panel.add(btnAdd);

        btnDelete = new AnButton("删除属性");
        panel.add(btnDelete);

        btnAlert = new AnButton("修改属性");
        panel.add(btnAlert);
        
        
    }

    @Override
    public void initializeEvent() {
        btnAdd.addActionListener((e)->{
            String rv=JOptionPane.showInputDialog(this,"请输入属性名称",null);
            if (rv!=null&&!rv.equals("")){
                try {
                    manager.createWorkerProperty(false,rv);
                    initializeData();
                    AnPopDialog.show(this,"添加完成。",AnPopDialog.SHORT_TIME);
                } catch (Exception e1) {
                    Application.errorWindow("属性重名，无法添加！");
                }
            }
        });

        btnDelete.addActionListener((e)->{
            if (list.getSelectedIndex()==-1) {
                Application.errorWindow("没有选择属性，请先选择再进行操作！");
                return;
            }
            String name=list.getElementAt(list.getSelectedIndex()).getTitle();
            if (name.equals("性别")||name.equals("民族")||name.equals("所属工地")||name.equals("工种")||name.equals("工人状态")||name.equals("备注")){
                Application.informationWindow("这是系统必要的属性，请不要删除。");
                return;
            }
            manager.removeWorkerProperty(list.getElementAt(list.getSelectedIndex()).getTitle());
            list.removeElementAt(list.getSelectedIndex());
            AnPopDialog.show(this,"删除完成，已经使用此属性的工人无法删除属性，当您增加工人的时候录入时会要求填写该属性。",AnPopDialog.LONG_TIME);
        });

        btnAlert.addActionListener((e)->{
            if (list.getSelectedIndex()==-1){
                Application.informationWindow("先选择数据再更改！");
                return;
            }
            String name=list.getElementAt(list.getSelectedIndex()).getTitle();
            if (name.equals("性别")||name.equals("民族")||name.equals("所属工地")||name.equals("工种")||name.equals("工人状态")){
                Application.informationWindow("这是系统必要的属性，请不要修改。");
                return;
            }

            String rv=JOptionPane.showInputDialog(null,"输入新属性名，此名称会覆盖现有的所有工人的旧名称。",null);

            assert DBManager.getManager() != null;
            if (DBManager.getManager().updateWorkerProperty(list.getElementAt(list.getSelectedIndex()).getTitle(),rv))
                initializeData();
        });

        list.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()==2){
                    //双击
                    AnDataChooser chooser=new AnDataChooser(
                            "属性修改器",
                            "从右边的新增和删除按钮增删数据",
                            new Chooser() {
                        @Override
                        public String[] addEvent() {
                            return null;
                        }

                        @Override
                        public boolean newEvent(String[] values,String newValue) {
                            if (values==null)
                                return true;
                            for (String v:values){
                                if (v.equals(newValue))
                                    return false;
                            }
                            return true;
                        }

                        @Override
                        public void done(String[] values) {
                            ArrayList tm = new ArrayList(Arrays.asList(values));
                            assert DBManager.getManager() != null;
                            DBManager.getManager().getWorkerProperty(list.getElementAt(list.getSelectedIndex()).getTitle()).setValues(tm);
                            initializeData();
                        }

                        @Override
                        public String[] getAddText() {
                            return null;
                        }

                        @Override
                        public String[] getNewText() {
                            return new String[]{"添加一个新字段，在添加工人时可以快速填充。","输入字段名"};
                        }
                    },
                            false,
                            manager.getWorkerProperty(list.getElementAt(list.getSelectedIndex()).getTitle()).toArray()
                    );
                }
            }
        });
    }

    @Override
    public void initializeData() {
        if (list!=null)
            list.clear();
        manager=DBManager.getManager();
        assert manager != null;
        for (Column property:manager.loadingWorkerProperty().getValues()){
            AnListRenderModel model=new AnListRenderModel(property.getName(),String.valueOf(property.size())+"个数据");
            list.addElement(model);
        }
    }
}
