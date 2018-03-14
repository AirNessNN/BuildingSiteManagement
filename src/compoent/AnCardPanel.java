package compoent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * An卡片容器
 */
public class AnCardPanel extends JPanel{

    interface OnSelectedListener{
        void onSelected(AnCardPanelItem item);
    }



    /**
     * 支持按钮组显示的容器
     */
    private JPanel sourcePanel=null;


    /**
     * 按钮组
     */
    private ArrayList<AnCardPanelItem> buttons=null;


    /**
     * 上个选择的项目
     */
    private AnCardPanelItem lastSelected=null;

    /**
     * 按钮的大小
     */
    private Dimension itemSize=null;
    private int location=0;



    private void init(){
        itemSize=new Dimension(50,50);

        setLayout(null);
        setBackground(Color.WHITE);
        buttons=new ArrayList<>();
    }


    public AnCardPanel(){
        init();
    }



    public void setItemSize(int width,int height){
        itemSize=new Dimension(width,height);
        //设置Size
        if(buttons!=null){
            for(AnCardPanelItem item:buttons){
                item.getButton().setSize(itemSize);
            }
        }
        //排列
        array();
    }

    public void addButton(AnCardPanelItem item1){
        if(item1!=null){
            AnCardPanelItem.Button button=item1.getButton();
            add(button);
            button.setSize(itemSize);
            button.setLocation(0,location);
            location+=itemSize.getHeight();//location递增
            buttons.add(item1);

            item1.I= (item) -> {
                if(lastSelected!=null){
                    //取消上一个选择的button
                    if(!lastSelected.equals(item)){
                        lastSelected.cancel();
                    }


                }
                lastSelected=item;
                //本次的操作
                sourcePanel=item.getPanel();

            };
        }
    }

    public void removeItem(AnCardPanelItem item){
        if(item!=null){
            item.I=null;
            buttons.remove(item);
            remove(item.getButton());

            //重新排列
            array();
        }
    }

    public void array(){
        location=0;
        if(buttons==null||buttons.size()==0){
            return;
        }
        for(AnCardPanelItem item:buttons){
            AnCardPanelItem.Button button=item.getButton();
            button.setLocation(0,location);
            location+=itemSize.getHeight();//location递增
        }
    }






}
