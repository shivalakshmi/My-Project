package com.odatalib.main.managers;

import android.content.Context;
import android.util.Log;

import com.odatalib.main.Constants;
import com.odatalib.main.LogonCoreRegister;
import com.odatalib.main.MyLogonCoreListener;
import com.odatalib.main.interfaces.OdataStoreInterface;
import com.odatalib.main.models.OpenStoreRequestModel;
import com.odatalib.main.models.RegisteredUser;
import com.sap.maf.tools.logon.core.LogonCore;
import com.sap.maf.tools.logon.core.LogonCoreContext;
import com.sap.maf.tools.logon.core.LogonCoreListener;
import com.sap.smp.client.httpc.HttpConversationManager;
import com.sap.smp.client.httpc.authflows.CommonAuthFlowsConfigurator;
import com.sap.smp.client.httpc.authflows.UsernamePasswordProvider;
import com.sap.smp.client.httpc.authflows.UsernamePasswordToken;
import com.sap.smp.client.httpc.events.IReceiveEvent;
import com.sap.smp.client.httpc.events.ISendEvent;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.offline.ODataOfflineStore;
import com.sap.smp.client.odata.offline.ODataOfflineStoreListener;
import com.sap.smp.client.odata.offline.ODataOfflineStoreNotification;
import com.sap.smp.client.odata.offline.ODataOfflineStoreOptions;
import com.sap.smp.client.odata.offline.ODataOfflineStoreState;
import com.sybase.persistence.DataVault;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by 972391 on 1/9/2017.
 */
public class OdataOfflineManager implements ODataOfflineStoreListener {

    private static OdataOfflineManager odataOfflineManager;
    private static final String TAG = OdataOfflineManager.class.getSimpleName();
    private OdataStoreInterface odataStoreInterface;

    private OdataOfflineManager() {
    }

    public static OdataOfflineManager getInstance() {

        if (odataOfflineManager == null) {
            odataOfflineManager = new OdataOfflineManager();
        }
        return odataOfflineManager;
    }

    public void openOffilneStoreInNetwork(Context context, OpenStoreRequestModel storeRequestModel, OdataStoreInterface storeInterface) {

        if (storeRequestModel != null && storeRequestModel.getStoreName() != null) {
            this.odataStoreInterface = storeInterface;
            String endPointUrl = LogonCoreRegister.getInstance().getBaseUrl() + storeRequestModel.getStoreName();
            final String userName = RegisteredUser.getInstance().getUserName();
            final String userPassword = RegisteredUser.getInstance().getPassword();
            try {

                ODataOfflineStore.globalInit();
                URL url = new URL(endPointUrl);
                ODataOfflineStoreOptions storeOptions = new ODataOfflineStoreOptions();
                storeOptions.host = url.getHost();
                storeOptions.port = url.getPort() + "";
                storeOptions.enableHTTPS = LogonCoreRegister.getInstance().isHttpsEnable();
                storeOptions.serviceRoot = storeRequestModel.getStoreName();
                storeOptions.storeEncryptionKey = storeRequestModel.getStoreName();
                storeOptions.storeName = storeRequestModel.getStoreName();
                storeOptions.enableRepeatableRequests = true;
                storeOptions.customHeaders.put(Constants.SMP_APPCID, RegisteredUser.getInstance().getAppConnectionId());

                if (storeRequestModel.getSetFilterMap() != null && storeRequestModel.getSetFilterMap().size() > 0) {
                    for (String requestSet : storeRequestModel.getSetFilterMap().keySet()) {
                        String requestFilter = storeRequestModel.getSetFilterMap().get(requestSet);
                        String filterCondition = "";
                        if (requestFilter != null && !requestFilter.isEmpty()) {
                            filterCondition = requestFilter;
                        }
                        storeOptions.definingRequests.put(requestSet, requestSet + filterCondition);
                    }
                }
                HttpConversationManager httpConversationManager = new HttpConversationManager(context);
                CommonAuthFlowsConfigurator commonAuthFlowsConfigurator = new CommonAuthFlowsConfigurator(context);
                UsernamePasswordProvider usernamePasswordProvider = new UsernamePasswordProvider() {
                    @Override
                    public Object onCredentialsNeededUpfront(ISendEvent iSendEvent) {
                        return new UsernamePasswordToken(userName, userPassword);
                    }

                    @Override
                    public Object onCredentialsNeededForChallenge(IReceiveEvent iReceiveEvent) {
                        return new UsernamePasswordToken(userName, userPassword);
                    }
                };

                commonAuthFlowsConfigurator.supportBasicAuthUsing(usernamePasswordProvider);
                commonAuthFlowsConfigurator.configure(httpConversationManager);
                storeOptions.conversationManager = httpConversationManager;

                ODataOfflineStore offlineStore = new ODataOfflineStore(context);
                offlineStore.setOfflineStoreListener(this);
                offlineStore.openStoreAsync(storeOptions);

            } catch (MalformedURLException e) {
                Log.e(TAG, e.getMessage());
            } catch (ODataException e) {
                Log.e(TAG, "ODataException " + e.getMessage());
            }
        }

    }

    public void openOfflineStoreInOffline(Context context, String storeName, OdataStoreInterface storeInterface) {

        String endPointURL = LogonCoreRegister.getInstance().getBaseUrl() + storeName;
        final String usrName = RegisteredUser.getInstance().getUserName();
        final String usrPwd = RegisteredUser.getInstance().getPassword();
        Log.d(TAG, " OfflineStoreManager.offlineStoreOpen : starting");

        try {
            ODataOfflineStore.globalInit();

            URL url = new URL(endPointURL);
            ODataOfflineStoreOptions optionsODataOfflineStore = new ODataOfflineStoreOptions();
            optionsODataOfflineStore.host = url.getHost();
            optionsODataOfflineStore.port = "" + url.getPort();
            optionsODataOfflineStore.enableHTTPS = LogonCoreRegister.getInstance().isHttpsEnable();
            optionsODataOfflineStore.serviceRoot = storeName;
            optionsODataOfflineStore.enableRepeatableRequests = true;
            optionsODataOfflineStore.storeName = storeName;
            optionsODataOfflineStore.storeEncryptionKey = storeName;
            optionsODataOfflineStore.customHeaders.put(Constants.SMP_APPCID, RegisteredUser.getInstance().getAppConnectionId());

            HttpConversationManager httpConversationManager = new HttpConversationManager(context);
            CommonAuthFlowsConfigurator configuratorCommonAuthFlows = new CommonAuthFlowsConfigurator(context);

            UsernamePasswordProvider providerUsernamePassword = new UsernamePasswordProvider() {
                public Object onCredentialsNeededUpfront(ISendEvent event) {
                    return new UsernamePasswordToken(usrName, usrPwd);
                }

                public Object onCredentialsNeededForChallenge(IReceiveEvent event) {
                    return new UsernamePasswordToken(usrName, usrPwd);
                }
            };

            configuratorCommonAuthFlows.supportBasicAuthUsing(providerUsernamePassword);
            configuratorCommonAuthFlows.configure(httpConversationManager);
            optionsODataOfflineStore.conversationManager = httpConversationManager;

            ODataOfflineStore offlineStore = new ODataOfflineStore(context);
            offlineStore.setOfflineStoreListener(this);
            offlineStore.openStoreAsync(optionsODataOfflineStore);

        } catch (Exception exception) {
            Log.d(TAG, " OfflineStoreManager.offlineStoreOpen() : error : " + exception);
        }
    }

    public void clearOfflineData(Context context, List<ODataOfflineStore> offlineStores, ArrayList<String> storeNames) {


        try {
            //Assuming all the Offline stores are in open state.
            for (ODataOfflineStore offlineStore : offlineStores) {
                if (offlineStore != null) {
                    offlineStore.closeStore();
                }
            }

            if (storeNames != null) {
                for (String storeName : storeNames) {
                    ODataOfflineStoreOptions bpopts = new ODataOfflineStoreOptions();
                    bpopts.storeName = storeName;
                    ODataOfflineStore.removeStore(context, bpopts);
                }
            }
            ODataOfflineStore.globalFini();
            LogonCore.getInstance().setLogonCoreListener(new MyLogonCoreListener());
            LogonCore.getInstance().deregister();

        } catch (ODataException ex) {
            Log.d(TAG, "Offline Store Exception while clearing existing user data." + ex.getMessage());
        }
    }

    @Override
    public void offlineStoreStateChanged(ODataOfflineStore oDataOfflineStore, ODataOfflineStoreState oDataOfflineStoreState) {
        Log.d(TAG, "offlineStoreStateChanged : " + oDataOfflineStoreState);
    }

    @Override
    public void offlineStoreOpenFailed(ODataOfflineStore oDataOfflineStore, com.sap.smp.client.odata.exception.ODataException e) {
        Log.d(TAG, "offlineStoreOpenFailed : " + oDataOfflineStore + " " + e.getMessage());
        odataStoreInterface.onOfflineStoreFailed(e.getMessage());
    }

    @Override
    public void offlineStoreOpenFinished(ODataOfflineStore oDataOfflineStore) {
        Log.d(TAG, "offlineStoreOpenFinished : " + oDataOfflineStore);
        if (oDataOfflineStore.getMetadata() != null && oDataOfflineStore.getMetadata().getMetaNamespaces() != null) {
            odataStoreInterface.onOfflineStoreOpened(oDataOfflineStore, oDataOfflineStore.getMetadata().getMetaNamespaces());
        }
    }

    @Override
    public void offlineStoreNotification(ODataOfflineStore oDataOfflineStore, ODataOfflineStoreNotification oDataOfflineStoreNotification) {

    }
}
