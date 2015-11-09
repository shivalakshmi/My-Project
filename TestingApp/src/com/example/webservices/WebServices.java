package com.example.webservices;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.pojo.FrndDetails;
import com.example.pojo.ProfileDetails;
import com.example.pojo.UserAvailability;
import com.example.testingapp.Constants;
import com.example.testingapp.Util;

public class WebServices {

	static final String TAG = "** WebServices **";
	private UserAvailability userAvailabilityPojo;
	private String dob;
	private String designation;
	private String aboutMe;
	private String profilePic;


	public String createUser(String userName, String password) {
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 50000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		httpClient.setParams(httpParameters);
		HttpPost httppost = new HttpPost(Util.URL_SIGNUP);
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("userName", userName));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			// number userId, contact list
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			org.apache.http.HttpResponse response = httpClient
					.execute(httppost);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String jsonString = reader.readLine();

			if (jsonString.contains("Apache Tomcat")) {
				return null;
			}
			if (response.getStatusLine().getStatusCode() == 200) {

				return jsonString;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}



	public String Login(String userName, String password) {
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 50000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		httpClient.setParams(httpParameters);
		HttpPost httppost = new HttpPost(Util.URL_LOGIN);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("userName", userName));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			// number userId, contact list
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			org.apache.http.HttpResponse response = httpClient
					.execute(httppost);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String jsonString = reader.readLine();

			if (jsonString.contains("Apache Tomcat")) {
				return null;
			}
			if (response.getStatusLine().getStatusCode() == 200) {

				return jsonString;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}




	public UserAvailability userAvalibility(String userName) {
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		httpClient.setParams(httpParameters);
		HttpPost httppost = new HttpPost(Util.URL_USER_AVAILABILITY);
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("userName", userName));
			// number userId, contact list
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			org.apache.http.HttpResponse response = httpClient
					.execute(httppost);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String jsonString = reader.readLine();

			if (jsonString.contains("Apache Tomcat")) {
				return null;
			}
			try {

				userAvailabilityPojo = new UserAvailability();


				String options = jsonString.substring(1,jsonString.length()-1);

				String subStringOpt = options.substring(1,options.length()-1);

				if(subStringOpt.equals("success"))
				{
					userAvailabilityPojo.setSuccess(subStringOpt);
				}
				else
				{

					String[] items = options.split(",");

					Log.i("****************", "***************item[0]*****************" + items[0]);
					Log.i("****************", "***************item[1]*****************" + items[1]);
					Log.i("****************", "***************item[2]*****************" + items[2]);


					List<String> elephantList = Arrays.asList(options.split(","));

					Log.i("elephantList", "elephantList elephantList elephantList"+elephantList.get(0)+ elephantList.get(1)+ elephantList.get(2));

					String option1 = elephantList.get(0).substring(1,elephantList.get(0).length()-1);
					String option2 = elephantList.get(1).substring(1,elephantList.get(1).length()-1);
					String option3 = elephantList.get(2).substring(1,elephantList.get(2).length()-1);



					userAvailabilityPojo.setOption1(option1);
					userAvailabilityPojo.setOption2(option2);
					userAvailabilityPojo.setOption3(option3);

				}


				JSONArray jsonArray = new JSONArray(jsonString);


			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (jsonString.contains("Apache Tomcat")) {
				return null;
			} else {
				return userAvailabilityPojo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}


	public String CreateFeed(String userName, String feedText) {
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 50000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		httpClient.setParams(httpParameters);
		HttpPost httppost = new HttpPost(Util.URL_CREATE_FEED_TEXT);
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("userName", userName));
			nameValuePairs.add(new BasicNameValuePair("Feedtext", feedText));
			// number userId, contact list
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			org.apache.http.HttpResponse response = httpClient
					.execute(httppost);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String jsonString = reader.readLine();

			if (jsonString.contains("Apache Tomcat")) {
				return null;
			}
			if (response.getStatusLine().getStatusCode() == 200) {

				return jsonString;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public ArrayList<Constants> getFeed() {


		String name = null;
		String feedText = null;
		String imageUrl = null;
		String time = null;

		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 50000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		httpClient.setParams(httpParameters);

		HttpGet httpget = new HttpGet(Util.URL_Get_FEED);

		Constants constants;
		ArrayList<Constants> arraylist = new ArrayList<Constants>();
		try {
			org.apache.http.HttpResponse response = httpClient.execute(httpget);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String jsonString = reader.readLine();
			if (jsonString.contains("Apache Tomcat")) {
				return null;
			}

			try {

				JSONArray jsonArray = new JSONArray(jsonString);

				for(int i=0; i<jsonArray.length();i++)
				{

					constants = new Constants();
					JSONObject jsonObject = jsonArray.getJSONObject(i);


					if(jsonObject.has("username"))
					{

						name = jsonObject.getString("username");
					}
					if(jsonObject.has("feedtext"))
					{
						feedText = jsonObject.getString("feedtext");
					}
					if(jsonObject.has("imageURL"))
					{
						imageUrl = jsonObject.getString("imageURL");
					}
					if(jsonObject.has("TimeStamp"))
					{
						time = jsonObject.getString("TimeStamp");
					}
					constants.setNames(name);
					constants.setTime(time);
					constants.setTextStatus(feedText);

					if(imageUrl.equals("null"))
					{
						constants.setImageUrlStatus(imageUrl);
					}
					else
					{
						constants.setImageUrlStatus(imageUrl);
					}

					arraylist.add(constants);



				}

			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		///// return result///



		return arraylist;
	}


	public String updateAboutMe(String userName, String aboutMeText) {
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 50000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		httpClient.setParams(httpParameters);
		HttpPost httppost = new HttpPost(Util.URL_UPDATE_ABOUT_ME);
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("userName", userName));
			nameValuePairs.add(new BasicNameValuePair("aboutMeText", aboutMeText));
			// number userId, contact list
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			org.apache.http.HttpResponse response = httpClient
					.execute(httppost);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String jsonString = reader.readLine();

			if (jsonString.contains("Apache Tomcat")) {
				return null;
			}
			if (response.getStatusLine().getStatusCode() == 200) {

				return jsonString;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}


	public String updateDesignation(String userName, String designationText) {
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 50000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		httpClient.setParams(httpParameters);
		HttpPost httppost = new HttpPost(Util.URL_UPDATE_DESIGNATION);
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("userName", userName));
			nameValuePairs.add(new BasicNameValuePair("designation", designationText));
			// number userId, contact list
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			org.apache.http.HttpResponse response = httpClient
					.execute(httppost);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String jsonString = reader.readLine();

			if (jsonString.contains("Apache Tomcat")) {
				return null;
			}
			if (response.getStatusLine().getStatusCode() == 200) {

				return jsonString;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}


	public String updateDOB(String userName, String dobText) {
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 50000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		httpClient.setParams(httpParameters);
		HttpPost httppost = new HttpPost(Util.URL_UPDATE_DOB);
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("userName", userName));
			nameValuePairs.add(new BasicNameValuePair("DOB", dobText));
			// number userId, contact list
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			org.apache.http.HttpResponse response = httpClient
					.execute(httppost);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String jsonString = reader.readLine();

			if (jsonString.contains("Apache Tomcat")) {
				return null;
			}
			if (response.getStatusLine().getStatusCode() == 200) {

				return jsonString;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}


	public ProfileDetails getProfileDetails(String userName) {
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		httpClient.setParams(httpParameters);
		HttpPost httppost = new HttpPost(Util.URL_PROFILE_DETAILS);
		ProfileDetails profileDetails = null;
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("userName", userName));
			// number userId, contact list
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			org.apache.http.HttpResponse response = httpClient
					.execute(httppost);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String jsonString = reader.readLine();

			if (jsonString.contains("Apache Tomcat")) {
				return null;
			}
			try {

				profileDetails = new ProfileDetails();



				JSONObject jsonObject = new JSONObject(jsonString);


				if(jsonObject.has("DOB"))
				{

					dob = jsonObject.getString("DOB");
					if(dob.equals("null"))
					{
						dob = "Not Specified";
					}
				}
				else
				{
					dob = "Not Specified";
				}
				if(jsonObject.has("designation"))
				{
					designation = jsonObject.getString("designation");
					if(designation.equals("null"))
					{
						designation =  "Not Specified";
					}
				}
				else
				{
					designation =  "Not Specified";
				}

				if(jsonObject.has("Aboutme"))
				{
					aboutMe = jsonObject.getString("Aboutme");
					if(aboutMe.equals("null"))
					{
						aboutMe = "Not Specified";
					}
				}
				else
				{
					aboutMe = "Not Specified";
				}



				profilePic = jsonObject.getString("profileURL");




				profileDetails.setDOB(dob);
				profileDetails.setDesignation(designation);
				profileDetails.setAboutMe(aboutMe);
				profileDetails.setProfilePic(profilePic);






			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (jsonString.contains("Apache Tomcat")) {
				return null;
			} else {
				return profileDetails;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public String changePassword(String userName, String newpassword) {
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 50000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		httpClient.setParams(httpParameters);
		HttpPost httppost = new HttpPost(Util.URL_CHANGE_PASSWORD);
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("userName", userName));
			nameValuePairs.add(new BasicNameValuePair("newpassword", newpassword));
			// number userId, contact list
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			org.apache.http.HttpResponse response = httpClient
					.execute(httppost);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String jsonString = reader.readLine();

			if (jsonString.contains("Apache Tomcat")) {
				return null;
			}
			if (response.getStatusLine().getStatusCode() == 200) {

				return jsonString;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public FrndDetails getFrndDetails(String userName) {
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		httpClient.setParams(httpParameters);
		HttpPost httppost = new HttpPost(Util.URL_PROFILE_DETAILS);
		FrndDetails frndDetails = null;
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("userName", userName));
			// number userId, contact list
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			org.apache.http.HttpResponse response = httpClient
					.execute(httppost);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String jsonString = reader.readLine();

			if (jsonString.contains("Apache Tomcat")) {
				return null;
			}
			try {
				frndDetails = new FrndDetails();



				JSONObject jsonObject = new JSONObject(jsonString);



				String frnddob1;

				String frnddesignation1;

				String frndaboutMe1;

				String frndprofilePic1;



				if(jsonObject.has("DOB"))
				{

					frnddob1 = jsonObject.getString("DOB");
					if(frnddob1.equals("null"))
					{
						frnddob1 = "Not Specified";
					}
				}
				else
				{
					frnddob1 = "Not Specified";
				}
				if(jsonObject.has("designation"))
				{
					frnddesignation1 = jsonObject.getString("designation");
					if(frnddesignation1.equals("null"))
					{
						frnddesignation1 =  "Not Specified";
					}
				}
				else
				{
					frnddesignation1 =  "Not Specified";
				}

				if(jsonObject.has("Aboutme"))
				{
					frndaboutMe1 = jsonObject.getString("Aboutme");
					if(frndaboutMe1.equals("null"))
					{
						frndaboutMe1 = "Not Specified";
					}
				}
				else
				{
					frndaboutMe1 = "Not Specified";
				}


				frndprofilePic1 = jsonObject.getString("profileURL");




				String frndNAme1 = userName;

				frndDetails.setFrndDob(frnddob1);
				frndDetails.setFrndDestination(frnddesignation1);
				frndDetails.setFrndabout(frndaboutMe1);
				frndDetails.setFrndUrl(frndprofilePic1);
				frndDetails.setFrndName(frndNAme1);






			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (jsonString.contains("Apache Tomcat")) {
				return null;
			} else {
				return frndDetails;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}


}
