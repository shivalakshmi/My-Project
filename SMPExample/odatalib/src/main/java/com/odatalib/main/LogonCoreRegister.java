package com.odatalib.main;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.odatalib.main.interfaces.RegistrationInterface;
import com.sap.maf.tools.logon.core.LogonCore;
import com.sap.maf.tools.logon.core.LogonCoreContext;
import com.sap.maf.tools.logon.core.LogonCoreException;

/**
 * Created by 972391 on 1/6/2017.
 */
public class LogonCoreRegister {

    private LogonCore mLogonCore;
    private LogonCoreContext mLogonCoreContext;
    private MyLogonCoreListener myLogonCoreListener;
    private Context mContext;
    private String appName;
    private static final String TAG = LogonCoreRegister.class.getSimpleName();
    private static LogonCoreRegister logonCoreRegister;
    private String mBaseUrlInString = null;
    private boolean isHttpsEnable;

    public static LogonCoreRegister getInstance() {
        if (logonCoreRegister == null) {
            logonCoreRegister = new LogonCoreRegister();
        }
        return logonCoreRegister;
    }

    private LogonCoreRegister() {
    }

    public void initialize(Context context, String appName, RegistrationInterface callBackInterface) {
        this.mContext = context;
        this.appName = appName;
        this.mLogonCore = LogonCore.getInstance();
        this.mLogonCore.init(mContext, appName);
        this.mLogonCore.setAppInForegroundState(true);
        this.mLogonCoreContext = mLogonCore.getLogonContext();
        this.myLogonCoreListener = new MyLogonCoreListener(mLogonCore, callBackInterface);
        this.mLogonCore.setLogonCoreListener(myLogonCoreListener);
    }

    public void setRegistrationUrl(String host, Integer port, boolean httpsEnable) {
        mLogonCoreContext.setHttps(httpsEnable);
        mLogonCoreContext.setPort(port);
        mLogonCoreContext.setHost(host);
        mLogonCoreContext.setUserCreationPolicy(LogonCore.UserCreationPolicy.automatic);
        setBaseUrl(host, port, httpsEnable);
        this.isHttpsEnable = httpsEnable;
    }

    public void registerDevice(String userName, String password) {

        String appConnectionId = appConnIdExists(appName);
        if (TextUtils.isEmpty(appConnectionId)) {
            clearLogonCoreParameters();
        }
        try {
            mLogonCoreContext.setBackendUser(userName.trim());
            mLogonCoreContext.setBackendPassword(password.trim());
            mLogonCore.register(mLogonCoreContext);
        } catch (LogonCoreException e) {
            Log.e(TAG, "parameters initialising : " + e.getMessage());
            Log.e(TAG, "Error code : " + e.getErrorCode());
        }
    }

    public void deRegisterDevice() {
        //logonCoreRegister = null;
        if (mLogonCore != null && mLogonCore.isRegistered()) {
            mLogonCore.deregister();
        }
        clearLogonCoreParameters();
    }

    private void clearLogonCoreParameters() {

        try {
            if (!mLogonCore.isStoreOpen()) {
                mLogonCore.unlockStore(appName);
            }
            mLogonCore.addObjectToStore(Constants.APP_CONNECTION_ID, "");
            mLogonCore.getLogonContext().setBackendUser("");
            mLogonCore.getLogonContext().setBackendPassword("");

        } catch (LogonCoreException ex) {
            Log.d(TAG, "Unable to clear the App Id from Data Vault");
        }
    }

    private String appConnIdExists(String appName) {
        String appConnId = "";
        if (mLogonCore.isStoreAvailable()) {
            try {
                mLogonCore.unlockStore(appName);
                appConnId = mLogonCore.getObjectFromStore(Constants.APP_CONNECTION_ID);
            } catch (LogonCoreException e) {
                Log.e(TAG, "unlock error: " + e.getMessage() + " error code :" + e.getErrorCode());
            }
        }
        return appConnId;
    }

    private void setBaseUrl(String host, Integer port, boolean httpsEnable) {

        StringBuilder baseUrl = new StringBuilder();
        if (httpsEnable) {
            baseUrl.append("https://");
        } else {
            baseUrl.append("http://");
        }
        if (host != null && !host.isEmpty()) {
            baseUrl.append(host);
        }
        if (port >= 0) {
            baseUrl.append(":").append(port).append("/");
        }
        mBaseUrlInString = baseUrl.toString();
    }

    public String getBaseUrl() {
        return mBaseUrlInString;
    }

    public boolean isHttpsEnable() {
        return isHttpsEnable;
    }
}
