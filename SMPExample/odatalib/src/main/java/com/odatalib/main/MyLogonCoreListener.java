package com.odatalib.main;

import android.util.Log;

import com.odatalib.main.interfaces.RegistrationInterface;
import com.odatalib.main.models.RegisteredUser;
import com.sap.maf.tools.logon.core.LogonCore;
import com.sap.maf.tools.logon.core.LogonCoreContext;
import com.sap.maf.tools.logon.core.LogonCoreException;
import com.sap.maf.tools.logon.core.LogonCoreListener;

/**
 * Created by 972391 on 1/5/2017.
 */
public class MyLogonCoreListener implements LogonCoreListener {

    private LogonCore mLogonCore;
    private RegistrationInterface callbackInterface;
    private static final String TAG = MyLogonCoreListener.class.getSimpleName();

    public MyLogonCoreListener(LogonCore mLogonCore, RegistrationInterface callbackInterface) {
        this.mLogonCore = mLogonCore;
        this.callbackInterface = callbackInterface;
    }
    public MyLogonCoreListener() {
        this.mLogonCore = LogonCore.getInstance();
    }

    @Override
    public void registrationFinished(boolean successFlag, String message, int errorCode, com.sybase.persistence.DataVault.DVPasswordPolicy dvPasswordPolicy) {
        Log.d(TAG, "successFlag : " + successFlag);
        Log.d(TAG, "message : " + message);

        switch (message) {

            case Constants.REGISTRATION_CREATED:

                LogonCoreContext context = mLogonCore.getLogonContext();
                try {
                    RegisteredUser.getInstance().setUserName(context.getBackendUser());
                    RegisteredUser.getInstance().setPassword(context.getBackendPassword());
                    RegisteredUser.getInstance().setAppConnectionId(context.getConnId());
                    callbackInterface.onRegistrationCompleted(RegisteredUser.getInstance());

                } catch (LogonCoreException e) {
                    callbackInterface.onRegistrationFailed(e.getMessage(), errorCode);
                }
                break;
            case Constants.REGISTRATION_ALREADY_EXISTS:
                callbackInterface.onRegistrationFailed(message, errorCode);
                break;
            default:
                callbackInterface.onRegistrationFailed(message, errorCode);
                try {
                    LogonCore.getInstance().getLogonContext().setBackendUser("");
                    LogonCore.getInstance().getLogonContext().setBackendPassword("");
                } catch (LogonCoreException ex) {
                    Log.d(MyLogonCoreListener.class.getSimpleName(), "LogonCoreException : " + ex.getMessage());
                }
                break;
        }
    }

    @Override
    public void deregistrationFinished(boolean b) {

    }

    @Override
    public void backendPasswordChanged(boolean b) {

    }

    @Override
    public void applicationSettingsUpdated() {

    }

    @Override
    public void traceUploaded() {

    }
}
