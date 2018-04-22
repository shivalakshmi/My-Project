package com.odataexample.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.odataexample.OdataOpenListener;
import com.odataexample.R;
import com.odataexample.utils.NetworkUtils;
import com.odataexample.utils.ServiceConstants;
import com.odatalib.main.Constants;
import com.odatalib.main.LogonCoreRegister;
import com.odatalib.main.interfaces.OdataStoreInterface;
import com.odatalib.main.managers.OdataOfflineManager;
import com.odatalib.main.managers.OdataOnlineManager;
import com.odatalib.main.models.OpenStoreRequestModel;
import com.odatalib.main.models.RegisteredUser;
import com.odatalib.main.interfaces.RegistrationInterface;
import com.sap.smp.client.odata.offline.ODataOfflineStore;
import com.sap.smp.client.odata.online.OnlineODataStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, RegistrationInterface, OdataStoreInterface {

    private EditText mTvUserName;
    private EditText mTvPassword;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private String mUserName = null;
    private String mPassword = null;
    private LogonCoreRegister mLogonCoreRegister;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeWidgets();
        initializeLogonCoreListener();
    }

    private void initializeLogonCoreListener() {
        mLogonCoreRegister = LogonCoreRegister.getInstance();
        mLogonCoreRegister.initialize(LoginActivity.this, ServiceConstants.APPLICATION_NAME, this);
        mLogonCoreRegister.setRegistrationUrl(ServiceConstants.HOST_NAME, ServiceConstants.PORT, ServiceConstants.HTTPS_ENABLE);
    }

    private void initializeWidgets() {
        mTvUserName = (EditText) findViewById(R.id.etv_username_login);
        mTvPassword = (EditText) findViewById(R.id.etv_password_login);
        Button btnSignIn = (Button) findViewById(R.id.btn_sign_login);
        btnSignIn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_login:
                clickSignIn();
                break;
            default:
                break;
        }

    }

    private void clickSignIn() {

        mUserName = mTvUserName.getText().toString();
        mPassword = mTvPassword.getText().toString();

        if (!mUserName.isEmpty() && !mPassword.isEmpty()) {
            if (NetworkUtils.isNetworkAvailable(LoginActivity.this)) {
                displayProgressDialog("LogonCore registration..");
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        mLogonCoreRegister.registerDevice(mUserName, mPassword);
                    }
                }.start();

            } else {
                Toast.makeText(LoginActivity.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(LoginActivity.this, "Please Enter Credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(LoginActivity.this);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.setMessage(message);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }

    }


    @Override
    public void onRegistrationCompleted(RegisteredUser registeredUser) {
        Log.d(TAG, "registration details : " + registeredUser.getUserName() + " " + registeredUser.getPassword() + " " + registeredUser.getAppConnectionId());
        //   displayProgressDialog("Odata stores opening ..");
        odataOfflineStoreOpen();
        odataOnlineStoreOpen();
    }

    private void odataOnlineStoreOpen() {

        OdataOnlineManager.getInstance().openOnlineStore(LoginActivity.this, ServiceConstants.CONNECTION_ID_USER_PROFILE, this);
        OdataOnlineManager.getInstance().openOnlineStore(LoginActivity.this, ServiceConstants.CONNECTION_ID_LOV_CALLS, this);
    }

    private void odataOfflineStoreOpen() {
        if (NetworkUtils.isNetworkAvailable(this)) {
            defineStoreRequest();
        } else {
            openStoreOffline();
        }
    }

    private void openStoreOffline() {

        OdataOfflineManager.getInstance().openOfflineStoreInOffline(LoginActivity.this, ServiceConstants.SERVICE_ROOT_LOV, this);
        OdataOfflineManager.getInstance().openOfflineStoreInOffline(LoginActivity.this, ServiceConstants.SERVICE_ROOT_USER_PROFILE, this);
    }

    private void defineStoreRequest() {

        //opening LovStore service containing ModelSet and CitySet inner sets

        HashMap<String, String> setsFilterMap = new HashMap<>();
        setsFilterMap.put(ServiceConstants.MODEL_SET, Constants.FILTER + "StartDate eq '" + "20151001" + "' and EndDate eq '20161212'");
        setsFilterMap.put(ServiceConstants.CITYSET, "");

        OpenStoreRequestModel openStoreRequestModel = new OpenStoreRequestModel();
        openStoreRequestModel.setStoreName(ServiceConstants.SERVICE_ROOT_LOV);
        openStoreRequestModel.setSetFilterMap(setsFilterMap);
        OdataOfflineManager.getInstance().openOffilneStoreInNetwork(LoginActivity.this, openStoreRequestModel, this);

        //opening UserProfile service containing LoginSet

        HashMap<String, String> setsFilterMap1 = new HashMap<>();
        setsFilterMap1.put(ServiceConstants.LOGIN_SET, "('" + RegisteredUser.getInstance().getUserName() + "')");

        OpenStoreRequestModel openStoreRequestModel1 = new OpenStoreRequestModel();
        openStoreRequestModel1.setStoreName(ServiceConstants.SERVICE_ROOT_USER_PROFILE);
        openStoreRequestModel1.setSetFilterMap(setsFilterMap1);
        OdataOfflineManager.getInstance().openOffilneStoreInNetwork(LoginActivity.this, openStoreRequestModel1, this);

    }

    @Override
    public void onRegistrationFailed(String errorMessage, int errorCode) {
        if (errorMessage.equals(Constants.REGISTRATION_ALREADY_EXISTS)) {
            dismissProgressDialog();
            mLogonCoreRegister.deRegisterDevice();
            // mLogonCoreRegister.registerDevice(mUserName, mPassword);
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    @Override
    public void onOnlineStoreOpened(OnlineODataStore onlineODataStore, Set<String> nameSpaces) {

        for (String nameSpace : nameSpaces) {
            switch (nameSpace) {
                case ServiceConstants.NAME_SPACE_LOV:
                    OdataOpenListener.getInstance().setLovOnlineStore(onlineODataStore);
                    break;
                case ServiceConstants.NAME_SPACE_USER_PROFILE:
                    OdataOpenListener.getInstance().setUserOnlineStore(onlineODataStore);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onOnlineStoreFailed(String erroeMessage) {

    }

    @Override
    public void onOfflineStoreOpened(ODataOfflineStore oDataOfflineStore, Set<String> nameSpaces) {

        for (String nameSpace : nameSpaces) {
            switch (nameSpace) {
                case ServiceConstants.NAME_SPACE_LOV:
                    OdataOpenListener.getInstance().setLovOfflineStore(oDataOfflineStore);
                    break;
                case ServiceConstants.NAME_SPACE_USER_PROFILE:
                    OdataOpenListener.getInstance().setUserOfflineStore(oDataOfflineStore);
                    break;
                default:
                    break;
            }
        }

        if (OdataOpenListener.getInstance().isOfflineStoresOpened()) {
            dismissProgressDialog();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onOfflineStoreFailed(String erroeMessage) {

    }

    @Override
    protected void onDestroy() {
        closeOfflineStores();
        super.onDestroy();
    }

    private void closeOfflineStores() {

        List<ODataOfflineStore> offlineStores = new ArrayList<>();
        offlineStores.add(OdataOpenListener.getInstance().getLovOfflineStore());
        offlineStores.add(OdataOpenListener.getInstance().getUserOfflineStore());

        ArrayList<String> storeNames = new ArrayList<>();
        storeNames.add(ServiceConstants.SERVICE_ROOT_LOV);
        storeNames.add(ServiceConstants.SERVICE_ROOT_USER_PROFILE);

        OdataOfflineManager.getInstance().clearOfflineData(LoginActivity.this, offlineStores, storeNames);

    }
}
