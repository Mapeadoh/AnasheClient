package me.travis.wurstplus.wurstplustwo.util;

public class WurstplusMSTimer {
    private long time;
    public WurstplusMSTimer() {
        time = -1;
    }

    public boolean passed(long ms) {
        return System.currentTimeMillis() - time >= ms;
    }

    public void reset() {
        time = System.currentTimeMillis();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}