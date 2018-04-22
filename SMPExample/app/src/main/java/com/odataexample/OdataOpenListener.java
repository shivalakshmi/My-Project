package com.odataexample;

import com.sap.smp.client.odata.offline.ODataOfflineStore;
import com.sap.smp.client.odata.online.OnlineODataStore;

/**
 * Created by 972391 on 1/10/2017.
 */
public class OdataOpenListener {

    private ODataOfflineStore lovOfflineStore;
    private ODataOfflineStore userOfflineStore;
    private OnlineODataStore lovOnlineStore;
    private OnlineODataStore userOnlineStore;

    private static OdataOpenListener odataOpenListener;

    private OdataOpenListener() {
    }

    public static OdataOpenListener getInstance() {
        if (odataOpenListener == null) {
            odataOpenListener = new OdataOpenListener();
        }
        return odataOpenListener;
    }

    public ODataOfflineStore getLovOfflineStore() {
        return lovOfflineStore;
    }

    public void setLovOfflineStore(ODataOfflineStore lovOfflineStore) {
        this.lovOfflineStore = lovOfflineStore;
    }

    public ODataOfflineStore getUserOfflineStore() {
        return userOfflineStore;
    }

    public void setUserOfflineStore(ODataOfflineStore userOfflineStore) {
        this.userOfflineStore = userOfflineStore;
    }

    public OnlineODataStore getLovOnlineStore() {
        return lovOnlineStore;
    }

    public void setLovOnlineStore(OnlineODataStore lovOnlineStore) {
        this.lovOnlineStore = lovOnlineStore;
    }

    public OnlineODataStore getUserOnlineStore() {
        return userOnlineStore;
    }

    public void setUserOnlineStore(OnlineODataStore userOnlineStore) {
        this.userOnlineStore = userOnlineStore;
    }

    public boolean isOfflineStoresOpened() {
        return lovOfflineStore != null && userOfflineStore != null;
    }

    public boolean isOnlineStoresOpened() {
        return lovOnlineStore != null && userOnlineStore != null;
    }
}
