package com.odatalib.main.models;
import java.util.HashMap;

/**
 * Created by 972391 on 1/9/2017.
 */
public class OpenStoreRequestModel {

    private String storeName;
    private HashMap<String, String> setFilterMap;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public HashMap<String, String> getSetFilterMap() {
        return setFilterMap;
    }

    public void setSetFilterMap(HashMap<String, String> setFilterMap) {
        this.setFilterMap = setFilterMap;
    }
}
