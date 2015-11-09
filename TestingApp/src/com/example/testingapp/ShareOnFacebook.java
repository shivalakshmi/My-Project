package com.example.testingapp;


import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class ShareOnFacebook{
	private static final String APP_ID = "330377297089863";
	private static final String[] PERMISSIONS = new String[] {"publish_stream","friends_location", "friends_hometown","publish_stream","friends_birthday"};
	private static final String TOKEN = "access_token";
	private static final String EXPIRES = "expires_in";
	private static final String KEY = "facebook-credentials";
	private Facebook facebook;
	private String messageToPost;
	Context mContext;
	Activity activity;

	public ShareOnFacebook(Context context, Activity act){
		Log.i("99999999", "====================== ShareOnFacebook constructor");
		mContext = context;
		activity = act;
	}

	public void postOnFacebook(String facebookMessage){
		Log.i("99999999", "====================== ShareOnFacebook postOnFacebook");
		facebook = new Facebook(APP_ID);
		restoreCredentials(facebook);		
		messageToPost = facebookMessage;
		share();
	}


	public boolean saveCredentials(Facebook facebook) {
		Log.i("99999999", "====================== ShareOnFacebook saveCredentials");
		Editor editor = mContext.getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
		editor.putString(TOKEN, facebook.getAccessToken());
		editor.putLong(EXPIRES, facebook.getAccessExpires());
		return editor.commit();
	}

	public boolean restoreCredentials(Facebook facebook) {
		Log.i("99999999", "====================== ShareOnFacebook restoreCredentials");
		SharedPreferences sharedPreferences = mContext.getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE);
		facebook.setAccessToken(sharedPreferences.getString(TOKEN, null));
		facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
		return facebook.isSessionValid();
	}

	public void doNotShare(View button){
	}
	public void share(){
		if (! facebook.isSessionValid()) {
			loginAndPostToWall();
		}
		else {
			new PsotWallAsync(mContext).execute();
		}
	}

	public void loginAndPostToWall(){
		Log.i("99999999", "====================== ShareOnFacebook loginAndPostToWall");

		facebook.authorize(activity, PERMISSIONS, Facebook.FORCE_DIALOG_AUTH, new LoginDialogListener(mContext));
		//		facebook.authorize(activity, new String[] { "publish_stream","friends_birthday" }, Facebook.FORCE_DIALOG_AUTH,
		//				new LoginDialogListener(mContext) );



	}

	public void postToWall(String message,Context c){
		Log.i("99999999", "====================== ShareOnFacebook postToWall");
		Bundle parameters = new Bundle();
		int what = 0;
		parameters.putString("fields", "picture");
		parameters.putString("fields", "name");
		//                parameters.putString("description", "topic share");



		try {



			//        	        facebook.request("me");
			String query = "SELECT uid, name, pic, current_location, birthday_date FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1 = me())";

			Bundle params = new Bundle();
			params.putString("method", "fql.query");
			params.putString("query", query);
			String response = facebook.request(null, params);
			//			String Response = facebook.request("me/friends",parameters);
			Log.d("Tests", "got Response: " + response);
			ArrayList<Friend> friendsList=Util.parseFriendsList(response);
			if(response!=null){

				Singleton.frendsList=friendsList;
			}


		} catch (Exception e) {
			what = 2;
			e.printStackTrace();
		}
	}


	class LoginDialogListener implements DialogListener {
		public LoginDialogListener(Context mContext) {
			// TODO Auto-generated constructor stub
		}
		public void onComplete(Bundle values) {
			saveCredentials(facebook);
			if (messageToPost != null){
				new PsotWallAsync(mContext).execute();
				/*MainActivity mAct = new MainActivity();
				mAct.fbasync()*/;
			}
		}
		public void onFacebookError(FacebookError error) {
			showToast("Authentication with Facebook failed!");
		}
		public void onError(DialogError error) {
			showToast("Authentication with Facebook failed!");
		}
		public void onCancel() {
			showToast("Authentication with Facebook cancelled!");
		}


	}

	private void showToast(String message){
		Toast.makeText(mContext.getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}

	private class PsotWallAsync extends AsyncTask<Void, Void, Void>
	{
		Context c;
		ProgressDialog progressDialog;
		
		ProgressDialog progressDialog1;


		public PsotWallAsync(Context c)
		{
			this.c=c;
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			postToWall(messageToPost,c);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);


			/*Intent intent = new Intent(c,
					MainActivity.class);
			intent.putExtra("navigate", "FacebookNotificationFragment");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			c.startActivity(intent);*/
			
			Intent intent = new Intent(c,
					FacebookNotificationsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			c.startActivity(intent);

			try{
				
				progressDialog.dismiss();
			}
			catch(Exception e)
			{
//				progressDialog1.dismiss();
			}


//			activity.finish();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			try{

				progressDialog = ProgressDialog.show(Singleton.activity,"Dialog","Loading...",true);
			}
			catch(Exception e)
			{
//				progressDialog1 = ProgressDialog.show(Singleton.activity,"Dialog","Loading...",true);
			}

		}


	}
	
}

