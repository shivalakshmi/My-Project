package com.odatalib.main.interfaces;

import com.sap.smp.client.odata.offline.ODataOfflineStore;
import com.sap.smp.client.odata.online.OnlineODataStore;

import java.util.Set;

/**
 * Created by 972391 on 1/10/2017.
 */
public interface OdataStoreInterface {

    void onOnlineStoreOpened(OnlineODataStore onlineODataStore, Set<String> nameSpaces);

    void onOnlineStoreFailed(String erroeMessage);

    void onOfflineStoreOpened(ODataOfflineStore oDataOfflineStore, Set<String> nameSpaces);

    void onOfflineStoreFailed(String erroeMessage);
}
