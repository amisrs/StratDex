package com.amisrs.gavin.stratdex.db;

/**
 * Created by Gavin on 3/10/2016.
 */
public class EvoSlot implements Comparable<EvoSlot> {
    public int pid;
    public int tier;


    public EvoSlot(int pid, int tier) {
        this.pid = pid;
        this.tier = tier;
    }
    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    @Override
    public int compareTo(EvoSlot another) {
        int toCompare = another.getTier();
        return this.getTier() - toCompare;
    }
}
