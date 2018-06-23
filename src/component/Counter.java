package component;

/**
 * <h2>计数器</h2>
 *<li>调用check来计数，如果两次调用间隔超过overtime，则计数器清零</li>
 */
public class Counter {
    private int overtime =800;//计数器计数间隔

    private int count =1;//计数

    private long intervalTime =0L;//


    public int getCount() {
        return count;
    }

    public long getIntervalTime() {
        return intervalTime;
    }

    public int getOvertime() {
        return overtime;
    }

    public void setOvertime(int overtime) {
        this.overtime = overtime;
    }

    /**
     * 计数，通过调用点击来计算此计数次数
     */
    public void check(){
        if (shouldClearCount()){//用户逻辑
            count =1;
            intervalTime =0L;
        }
        if (intervalTime ==0){
            intervalTime =System.currentTimeMillis();//记录点击的时间
        }else {
            if ((System.currentTimeMillis()- intervalTime)>800){
                count =1;
                intervalTime =0;
            } else count++;
            intervalTime =System.currentTimeMillis();
        }
    }


    /**
     * 请重写来补充清除次数的逻辑
     * @return 返回true为清除计数器，false则正常计数
     */
    public boolean shouldClearCount(){
        return false;
    }
}
