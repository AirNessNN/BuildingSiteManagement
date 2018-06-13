package animation;

/**
 * 时间节点
 */
public class TimeNode {

    private long startTime;
    private long duringTime;
    int nodeInndex;

    public TimeNode(long startTime,long duringTime){
        this.startTime=startTime;
        this.duringTime =duringTime;
    }


    public int getNodeInndex() {
        return nodeInndex;
    }

    public long getDuringTime() {
        return duringTime;
    }

    public long getStartTime() {
        return startTime;
    }

    void setNodeInndex(int index){
        this.nodeInndex=index;
    }
}
