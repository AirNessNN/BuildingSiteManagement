package animation;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * <h2>An动画管理器</h2>
 * <li>工厂类，生产各种类型的迭代值</li>
 * <li>单例，一个程序只能有一个管理器</li>
 */
public class AnimationManager {

    /**
     * 迭代器接口的实现类
     */
    class AnimItor implements Iterator{

        //长度总和
        private int length;

        //已经迭代的值
        private float point;

        //起始值
        private int startPoint;

        //结束值
        private int endPoint;


        private float pointTime;

        //节点的时长
        private int allTimes=0;

        //时间节点
        private ArrayList<TimeNode> timeNodes=new ArrayList<>();

        //状态回调
        private StateCallback stateCallback=null;

        //数值回调，用于用户获取迭代的数值
        private ValueCallback valueCallback=null;

        //销毁标记
        private boolean disposed=false;


        private boolean runState=false;

        private float stepLength=0f;

        private boolean reverse=false;



        private AnimItor(int startPoint,int endPoint){
            length=endPoint-startPoint;
            this.endPoint=endPoint;
            pointTime=0f;
            point=startPoint;
            this.startPoint=startPoint;
        }



        @Override
        public void update() {
            if (!reverse){
                if (pointTime>=allTimes){
                    stop();
                    return;
                }
            }else if (pointTime<=0){
                stop();
                return;
            }

            for ( TimeNode node:timeNodes){
                //在此时间区间内的操作
                if (pointTime>=node.getStartTime()&&pointTime<(node.getStartTime()+node.getDuringTime())){
                    float frames=node.getDuringTime()/sleepTime;//获取在区间内的帧数
                    float frameLength=stepLength/frames;//一帧的步长

                    if (!reverse){
                        if (point+frameLength>endPoint) point=endPoint;
                        else point += frameLength;
                    }else {
                        if (point-frameLength<startPoint)point=startPoint;
                        else point-=frameLength;
                    }
                    break;
                }
            }

            //时间增加
            if (!reverse) pointTime+=sleepTime;
            else pointTime-=sleepTime;
        }

        @Override
        public int getUpdateValue() {
            return new Float(point).intValue();
        }

        @Override
        public void reset() {
            point=startPoint;
            pointTime=0;
        }

        //倒置
        @Override
        public void reverse() {
            reverse=!reverse;
        }

        @Override
        public boolean isReverse() {
            return reverse;
        }

        @Override
        public void stop() {
            if (stateCallback!=null)stateCallback.stateCallback(true);
            runState=false;
        }

        @Override
        public void start() {
            if (disposed)return;
            if (stateCallback!=null)stateCallback.stateCallback(false);
            runState=true;
        }

        @Override
        public void setCallback(ValueCallback callback) {
            valueCallback=callback;
        }

        @Override
        public void dispose() {
            disposed=true;
        }

        @Override
        public void addNode(long startTime,long duringTime){
            TimeNode node=new TimeNode(startTime,duringTime);
            node.setNodeInndex(timeNodes.size());
            timeNodes.add(node);

            //设置步长
            stepLength=length/timeNodes.size();
            allTimes+=duringTime;
        }

        public void addNode(long duringTime){
            if (timeNodes.size()==0){
                addNode(0,duringTime);
                return;
            }
            TimeNode node=timeNodes.get(timeNodes.size()-1);
            int startTime= (int) (node.getStartTime()+node.getDuringTime());
            addNode(startTime,duringTime);
        }
    }





    //状态回调：回调的数据true是正在运行，如果没开始，运行状态是false，所以调用start的时候是false
    private interface StateCallback{
        void stateCallback(boolean isRunning);
    }


















    //迭代服务
    private Thread service;

    //受管理的迭代器
    private final ArrayList<AnimItor> animItors = new ArrayList<>();

    //线程状态
    private boolean serviceRunning=false;

    private int sleepTime=16;

    private final ArrayList<AnimItor> delete=new ArrayList<>();



















    private AnimationManager(){

    }




    private static AnimationManager manager;

    public static AnimationManager getManager(){
        if (manager==null)
            manager=new AnimationManager();
        return manager;
    }


    public Iterator createAnimationIterator(int beginValue,int endValue,int mod){
        AnimItor animItor=new AnimItor(beginValue,endValue);
        //设置状态回调
        animItor.stateCallback= isRunning -> {
            if (!isRunning){
                startService();
            }
        };

        animItors.add(animItor);
        return animItor;
    }




    private void startService(){
        if (serviceRunning)
            return;
        serviceRunning=true;
        if (service==null){
            service=new Thread(runnable);
            service.start();
            System.out.println("线程创建");
            return;
        }
        synchronized (animItors){
            animItors.notify();
            System.out.println("线程唤醒");
        }
    }

    private void stopService(){
        serviceRunning=false;
    }


    public boolean getServiceRunningState(){
        return serviceRunning;
    }



    /*
    实现逻辑，利用线程中的循环，对列表内的所有迭代器做遍历，
    迭代器由此管理器创建、构造。创建时设置内部的监听方法，
    在迭代器使用start的方法时，唤醒管理器的迭代线程，
    迭代线程中若所有迭代器处于销毁状态或者停止状态，
    线程休眠直到迭代器重新调用start方法
     */
    //线程方法
    private Runnable runnable= () -> {
        //此为一次迭代
        while(serviceRunning){
            synchronized (animItors){
                //迭代器运行标记
                boolean hasTask=false;

                delete.clear();

                long timeStart=System.currentTimeMillis();

                for (AnimItor animItor:animItors){
                    if (animItor==null)continue;
                    if (animItor.disposed){delete.add(animItor);continue;}
                    if (!animItor.runState)continue;
                    animItor.update();
                    if (animItor.valueCallback!=null)
                        animItor.valueCallback.callback(animItor.getUpdateValue());

                    hasTask=true;
                }

                //删除已经销毁的迭代器
                for (AnimItor animItor:delete){
                    animItors.remove(animItor);
                }

                long timeEnd=System.currentTimeMillis();

                try {//线程休眠
                    Thread.sleep(sleepTime-(timeEnd-timeStart));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!hasTask) {
                    try {
                        serviceRunning=false;
                        System.out.println("线程暂停");
                        animItors.wait(1000*60*3);
                        if (!serviceRunning){
                            service=null;
                            System.out.println("线程终止");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
}
