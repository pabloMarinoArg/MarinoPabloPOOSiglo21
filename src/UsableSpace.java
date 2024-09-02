package src;

import java.util.List;

public abstract class UsableSpace <T>{
    protected int segmentLimit;
    protected int leve;
    protected List<T> segmentList;
    protected boolean active;

    public int getSegmentLimit() {
        return segmentLimit;
    }

    public void setSegmentLimit(int segmentLimit) {
        this.segmentLimit = segmentLimit;
    }

    public int getLeve() {
        return leve;
    }

    public void setLeve(int leve) {
        this.leve = leve;
    }

    public List<T> getSegmentList() {
        return segmentList;
    }

    public void setSegmentList(List<T> segmentList) {
        this.segmentList = segmentList;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
