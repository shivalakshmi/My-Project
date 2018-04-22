package com.odataexample.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.odataexample.OdataOpenListener;
import com.odataexample.R;
import com.odataexample.utils.OdataServices;
import com.odatalib.main.models.RegisteredUser;

import java.util.ArrayList;
import java.util.HashMap;

import static com.odataexample.R.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvUserName;
    private TextView tvUserDetails;
    private ListView listViewDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        initializeWidgets();
    }

    private void initializeWidgets() {

        tvUserName = (TextView) findViewById(id.tv_user_name);
        tvUserDetails = (TextView) findViewById(id.tv_user_details);
        listViewDetails = (ListView) findViewById(id.listView_details);
        Button btnUserDetails = (Button) findViewById(id.btn_user_details);
        Button btnCityDetails = (Button) findViewById(id.btn_city_details);
        btnUserDetails.setOnClickListener(this);
        btnCityDetails.setOnClickListener(this);
        tvUserName.setText(getString(string.userName));
        tvUserDetails.setText(getString(string.userDetails));
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
            case id.btn_user_details:
                displayUserDetails();
                break;
            case id.btn_city_details:
                displayCityDetails();
                break;
            default:
                break;
        }
    }

    private void displayCityDetails() {

        OdataServices odataServices = new OdataServices();
        ArrayList<String> cityList = odataServices.getCityDetails();
        if(cityList!=null)
        {
            ArrayAdapter adapter= new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,cityList);
            listViewDetails.setAdapter(adapter);
        }
    }

    private void displayUserDetails() {


        AsyncTask<Object, Void, Object> asyncTask = new AsyncTask<Object, Void, Object>() {
            private ProgressDialog progressDialog;

            @Override
            protected Object doInBackground(Object... params) {

                OdataServices odataServices = new OdataServices();
                return odataServices.getLoginUserDetails(RegisteredUser.getInstance().getUserName());
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                tvUserName.setText(getString(string.userName));
                tvUserDetails.setText(getString(string.userDetails));
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);
                progressDialog.dismiss();
                if (result != null) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) result;
                    for (String key : hashMap.keySet()) {
                        tvUserName.setText(getString(string.userName) + key);
                        tvUserDetails.setText(getString(string.userDetails) + hashMap.get(key));
                    }
                }
            }
        };

        asyncTask.execute();

    }
}
