package com.example.testingapp;

import com.example.testingapp.CreateFeedActivity.CreateFeedTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class LauncherActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher_screen);


		SharedPreferences pref=getSharedPreferences("LoginDetails", 0);
		boolean restoredText = pref.getBoolean("text", false);
		
		
		
		
		
		/*SharedPreferences pref2=getSharedPreferences("Logout", 0);

		String logoutText = pref2.getString("Logout", null);*/
		
		
		if (restoredText==true) 
		{
			Handler handler = new Handler(); 
			handler.postDelayed(new Runnable() { 
				public void run() { 
					
					if(Util.isOnline(getApplicationContext()))
					{


						

						Intent intent = new Intent(LauncherActivity.this,MainActivity.class);
						startActivity(intent);
						finish();

					}
					else
					{
						Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
					}

				} 
			}, 1000);
		}
		else
		{

			Handler handler = new Handler(); 
			handler.postDelayed(new Runnable() { 
				public void run() { 

					if(Util.isOnline(getApplicationContext()))
					{
						Intent intent = new Intent(LauncherActivity.this,Viewpager.class);
						startActivity(intent);
						finish();
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
					}
					
					
				} 
			}, 1000);
		}


		
		
		
		
		
		
		


	}

}
