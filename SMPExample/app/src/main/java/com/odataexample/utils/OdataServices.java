package com.odataexample.utils;

import android.util.Log;

import com.odataexample.OdataOpenListener;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.ODataEntitySet;
import com.sap.smp.client.odata.ODataPayload;
import com.sap.smp.client.odata.ODataPropMap;
import com.sap.smp.client.odata.ODataProperty;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.offline.ODataOfflineStore;
import com.sap.smp.client.odata.offline.lodata.Property;
import com.sap.smp.client.odata.online.OnlineODataStore;
import com.sap.smp.client.odata.store.ODataResponseSingle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 1312383 on 1/23/2017.
 */
public class OdataServices {

    public HashMap<String, String> getLoginUserDetails(String usrName) {

        HashMap<String, String> userDetailsMap = new HashMap<>();
        OnlineODataStore userOnlineStore = OdataOpenListener.getInstance().getUserOnlineStore();

        if (userOnlineStore != null && userOnlineStore.isOpen()) {
            ODataProperty property;
            ODataPropMap properties;
            try {
                ODataResponseSingle resp = userOnlineStore.executeReadEntity(ServiceConstants.LOGIN_SET + "('" + usrName + "')", null);
                if (resp != null && resp.getPayloadType() == ODataPayload.Type.Entity) {
                    ODataEntity entity = (ODataEntity) resp.getPayload();

                    String name = "", details = "";
                    properties = entity.getProperties();
                    property = properties.get(Properties.NAME);
                    name = property.getValue().toString();
                    property = properties.get(Properties.ILOGINID);
                    details = property.getValue().toString();
                    property = properties.get(Properties.ROLE);
                    details = details + " - " + property.getValue().toString();
                    userDetailsMap.put(name, details);
                }
            } catch (Exception e) {
                Log.v(OdataServices.class.getSimpleName(), "" + e.getMessage());
            }
        }

        return userDetailsMap;
    }

    public ArrayList<String> getCityDetails() {

        ArrayList<String> cityList = new ArrayList<>();
        ODataOfflineStore offlineStore = OdataOpenListener.getInstance().getLovOfflineStore();

        if (offlineStore != null) {
            try {

                ODataResponseSingle resp;
                ODataPropMap properties;

                resp = offlineStore.executeReadEntitySet(ServiceConstants.CITYSET, null);

                if (resp != null && resp.getPayloadType() == ODataPayload.Type.EntitySet) {
                    //Get the response payload
                    ODataEntitySet feed = (ODataEntitySet) resp.getPayload();
                    //Get the list of ODataEntity
                    List<ODataEntity> entities = feed.getEntities();
                    for (ODataEntity entity : entities) {

                        StringBuilder builder = new StringBuilder();
                        String value;
                        properties = entity.getProperties();
                        value = properties.get(Properties.CITYC).getValue().toString();
                        builder.append(value);
                        value = properties.get(Properties.STATE).getValue().toString();
                        builder.append(" , ").append(value);
                        value = properties.get(Properties.STATECODE).getValue().toString();
                        builder.append(" - ").append(value);
                        value = properties.get(Properties.COUNTRY).getValue().toString();
                        builder.append(" , ").append(value);

                        cityList.add(builder.toString());
                    }
                }
            } catch (ODataException e) {
                e.printStackTrace();
            }
        }
        return cityList;

    }
}
