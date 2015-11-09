package com.example.testingapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.util.Log;

public class Util {


	private static Bitmap bitmapImage;
	
	
	private static int diffInDays=0;
	
	private static ProgressDialog progressDialog;
	
	public static String BASE_URL = "http://192.168.2.28:8080/RumorMill";
	
	
	public static String URL_SIGNUP = BASE_URL+"/signUp";
	public static String URL_LOGIN = BASE_URL+"/login";
	public static String URL_USER_AVAILABILITY = BASE_URL+"/userAvalaibility";
	public static String URL_CREATE_FEED = BASE_URL+"/createFeed";
	public static String URL_Get_FEED = BASE_URL+"/getFeed";
	public static String URL_UPDATE_ABOUT_ME = BASE_URL+"/updateaboutme";
	public static String URL_UPDATE_DESIGNATION = BASE_URL+"/updatedesignation";
	public static String URL_UPDATE_DOB = BASE_URL+"/updateDOB";

	public static String URL_PROFILE_DETAILS = BASE_URL+"/profileDetails";
	
	public static String URL_FRND_DETAILS = BASE_URL+"/getFriendDetails";
	
	public static String URL_CHANGE_PASSWORD = BASE_URL+"/changepassword";
	
	public static String URL_CHANGE_PICTURE = BASE_URL+"/changepic";
	
	public static String URL_CREATE_FEED_TEXT = BASE_URL+"/createFeedtext";

	public static ArrayList<Friend> parseFriendsList(String jsonResponse){
		Friend friend = null;
		JSONArray respArray;
		String city = null;
		try {
			respArray = new JSONArray(jsonResponse);
			ArrayList<Friend> friendsList=new ArrayList<Friend>();
			//			JSONArray jsonArray = respObject.getJSONArray("data");
			for(int i=0;i<respArray.length(); i++){
				JSONObject jsonObject = respArray.getJSONObject(i);
				//				Log.i("***********", "*********** Response ************"+respArray);
				String uid = jsonObject.getString("uid");
				String name = jsonObject.getString("name");
				String pic = jsonObject.getString("pic");
				String location = jsonObject.getString("current_location");
				String dob= jsonObject.getString("birthday_date");
				Log.i("*******", " *********************** "+name);
				Log.i("***********","***********"+pic);
				//				location="null";
				if(!location.equals("null"))	{
					JSONObject jsonObject2 = new JSONObject(location);
					city=jsonObject2.getString("city");


				}

				if(!dob.equals("null"))
				{
					Calendar c = Calendar.getInstance();
					SimpleDateFormat simpledate = new SimpleDateFormat("MM/dd");
					String formattedDate = simpledate.format(c.getTime());

					long diff = 0;

					try {

						//Convert to Date
						Date startDate = null;
						try {
							startDate = simpledate.parse(dob);
						} catch (java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Calendar c1 = Calendar.getInstance();
						//Change to Calendar Date
						c1.setTime(startDate);

						//Convert to Date
						Date endDate = null;
						try {
							endDate = simpledate.parse(formattedDate);
						} catch (java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if(startDate.compareTo(endDate)>0)
						{


							Calendar c2 = Calendar.getInstance();
							//Change to Calendar Date
							c2.setTime(endDate);

							//get Time in milli seconds
							long ms1 = c1.getTimeInMillis();
							long ms2 = c2.getTimeInMillis(); 
							//get difference in milli seconds
							diff = ms1 - ms2;

							diffInDays = (int) (diff / (24 * 60 * 60 * 1000));

							Log.i("********************", "************** Diff in days ***************"+diffInDays);

						}
						else
						{

							Calendar c2 = Calendar.getInstance();
							//Change to Calendar Date
							c2.setTime(endDate);

							//get Time in milli seconds
							long ms1 = c1.getTimeInMillis();
							long ms2 = c2.getTimeInMillis(); 
							//get difference in milli seconds
							diff = ms1 - ms2;

							diffInDays = (int) (diff / (24 * 60 * 60 * 1000));

							Log.i("********************", "************** Negative Diff in days ***************"+diffInDays);
						}

					} catch (ParseException e) {
						e.printStackTrace();
					}






				}



				if(diffInDays>=0 && diffInDays<=7 && !dob.equals("null"))
				{



					friend = new Friend();
					friend.setUid(uid);
					friend.setName(name);
					friend.setPic(pic);
					friend.setCity(city);
					friend.setDob(dob);
					if(diffInDays>0)
					{
						friend.setDiffDates(diffInDays);
					}
					bitmapImage = getBitmapFromURL(pic);

					friend.setBitmap(bitmapImage);



					friendsList.add(friend);
				}



			}
			return friendsList;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	public static boolean isOnline(Context context) {
		ConnectivityManager cm =
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}


	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static void showProgressDialog(Activity activity){

		progressDialog = new ProgressDialog(activity);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		if(progressDialog!=null)
		{
			if(!progressDialog.isShowing()){
				progressDialog.show();
			}
		} 
	}

	public static void dismissProgressDialog(){
		if(progressDialog!=null){
			if(progressDialog.isShowing()){
				progressDialog.dismiss();
			}
			progressDialog=null;
		}
	}

}
