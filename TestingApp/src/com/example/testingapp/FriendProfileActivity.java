package com.example.testingapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pojo.FrndDetails;
import com.example.webservices.WebServices;

public class FriendProfileActivity extends Activity{
	
	
	private TextView dob;
	private TextView designation;
	private TextView about;
	private ImageView profilepic;
	private TextView frndName;
	Activity activity;
	
	private String usernameFriend;
	
	private TextView header;
	
	private TextView cancel_profile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_profile);
		activity = FriendProfileActivity.this;
		
		dob = (TextView) findViewById(R.id.frnd_dob);
		designation = (TextView)findViewById(R.id.frnd_designation);
		about = (TextView)findViewById(R.id.frnd_about);
		frndName = (TextView) findViewById(R.id.frnd_username);
		profilepic = (ImageView)findViewById(R.id.frnd_profile_picture);
		cancel_profile = (TextView)findViewById(R.id.cancel_profile);
		header = (TextView)findViewById(R.id.heading_profile);
		
		
		
		usernameFriend = getIntent().getExtras().getString("frndName");
		
		frndName.setText(usernameFriend);
		
//		header.setText(usernameFriend+" 's Profile");
		
		new ProfileFrndDetailsTask().execute(usernameFriend);
		
		cancel_profile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		
	}
	
	
	protected class ProfileFrndDetailsTask extends AsyncTask<String,Void,FrndDetails>{

		@Override
		protected FrndDetails doInBackground(String... params) {
			// TODO Auto-generated method stub

			String username = params[0];

			WebServices webservices = new WebServices();
			FrndDetails frndDetails = webservices.getFrndDetails(username);

			return frndDetails;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Util.showProgressDialog(activity);
		}

		@Override
		protected void onPostExecute(FrndDetails result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Util.dismissProgressDialog();
			if(result!=null)
			{

				dob.setText(result.getFrndDob());
				designation.setText(result.getFrndDestination());
				about.setText(result.getFrndabout());
				frndName.setText(result.getFrndName());
				

			
				
				

				 int loader = R.drawable.ic_stub;
				 String image_url = result.getFrndUrl();
		         
			        // ImageLoader class instance
			        ImageLoaderProfile imgLoader = new ImageLoaderProfile(getApplicationContext());
			         
			        // whenever you want to load an image from url
			        // call DisplayImage function
			        // url - image url to load
			        // loader - loader image, will be displayed before getting image
			        // image - ImageView 
			        imgLoader.DisplayImage(image_url, loader, profilepic);
				


				////// Assign values to profile info
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Server Down!!", Toast.LENGTH_LONG).show();
			}
		}
	}

	
	

}
