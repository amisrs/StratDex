package com.amisrs.gavin.stratdex.model;

/**
 * Created by Gavin on 29/09/2016.
 */
public class Stat {
    private int base_stat;
    private StatDetail statDetail;

    public int getBaseStat() {
        return base_stat;
    }

    public StatDetail getStatDetail() {
        return statDetail;
    }

    private class StatDetail {
        private String name;

        public String getName() {
            return name;
        }
    }
}
