package com.odatalib.main.managers;

import android.content.Context;
import android.util.Log;

import com.odatalib.main.XCSRFTokenRequestFilter;
import com.odatalib.main.XCSRFTokenResponseFilter;
import com.odatalib.main.interfaces.OdataStoreInterface;
import com.odatalib.main.models.RegisteredUser;
import com.sap.smp.client.httpc.HttpConversationManager;
import com.sap.smp.client.httpc.authflows.CommonAuthFlowsConfigurator;
import com.sap.smp.client.httpc.authflows.UsernamePasswordProvider;
import com.sap.smp.client.httpc.authflows.UsernamePasswordToken;
import com.sap.smp.client.httpc.events.IReceiveEvent;
import com.sap.smp.client.httpc.events.ISendEvent;
import com.sap.smp.client.odata.exception.ODataContractViolationException;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.online.OnlineODataStore;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 972391 on 1/9/2017.
 */
public class OdataOnlineManager implements OnlineODataStore.OpenListener {

    private static OdataOnlineManager odataOnlineManager;
    private static final String TAG = OdataOnlineManager.class.getSimpleName();
    private OdataStoreInterface odataStoreInterface;

    private OdataOnlineManager() {
    }

    public static OdataOnlineManager getInstance() {

        if (odataOnlineManager == null) {
            odataOnlineManager = new OdataOnlineManager();
        }
        return odataOnlineManager;
    }


    public void openOnlineStore(Context context, String storeName, OdataStoreInterface storeInterface) {

        if (storeName != null && !storeName.isEmpty()) {

            this.odataStoreInterface = storeInterface;
            final String userName = RegisteredUser.getInstance().getUserName();
            final String userPassword = RegisteredUser.getInstance().getPassword();

            try {

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

                XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance();
                XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(requestFilter);
                httpConversationManager.addFilter(requestFilter);
                httpConversationManager.addFilter(responseFilter);

                Log.d(TAG, "Opening Online Store : " + storeName);

                URL url = new URL(storeName);
                OnlineODataStore.OnlineStoreOptions onlineStoreOptions = new OnlineODataStore.OnlineStoreOptions();
                onlineStoreOptions.useCache = true;
                onlineStoreOptions.forceMetadataDownload = true;

                OnlineODataStore.open(context, url, httpConversationManager, this, onlineStoreOptions);

            } catch (MalformedURLException | ODataContractViolationException e) {
                Log.e(TAG, "MalformedURL|ODataContractViolation " + e.getMessage());
            }
        }
    }

    @Override
    public void storeOpened(OnlineODataStore onlineODataStore) {
        Log.d(TAG, onlineODataStore.getMetadata().getMetaNamespaces().toString());
        if (onlineODataStore.getMetadata() != null && onlineODataStore.getMetadata().getMetaNamespaces() != null) {
            odataStoreInterface.onOnlineStoreOpened(onlineODataStore, onlineODataStore.getMetadata().getMetaNamespaces());
        }
    }

    @Override
    public void storeOpenError(ODataException e) {
        Log.d(TAG, "OnlineStoreException " + e.getMessage());
        odataStoreInterface.onOnlineStoreFailed(e.getMessage());
    }
}
