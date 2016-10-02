package com.amisrs.gavin.stratdex.db;

/**
 * Created by Gavin on 2/10/2016.
 */
public interface LoadResponse {
    void sayHasLoaded();
    void updateLoadingMsg(String msg);
}
