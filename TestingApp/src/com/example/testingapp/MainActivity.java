package com.example.testingapp;



import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.webservices.WebServices;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class MainActivity extends FragmentActivity {

	FragmentManager fm;
	FragmentTransaction ft;
	Fragment myFragment;
	
	private EditText currentPassword;
	private EditText newPassword;
	private EditText confirmPassword;

	private Button home;
	private Button newsLetter;
	private Button events;

	private LinearLayout popup;

	private ImageLoadingListener animateFirstListener =null;
	private ImageView settings;

	private FrameLayout content;

	View popView;

	private String passwordOld;
	
	private AlertDialog alert;

	private TextView title;

	private Activity activity;
	private String naviagteTo;
	private String userNameSaved;
	private String passwordSaved;
	private ImageView back;


	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		SharedPreferences prefusername=getSharedPreferences("UserName123", 0);
		userNameSaved = prefusername.getString("userName123", "lakshmi");

		SharedPreferences prefpassword=getSharedPreferences("password", 0);
		passwordSaved = prefpassword.getString("password", "lakshmi");

		activity = MainActivity.this;
		Singleton.activity=activity;
		content=(FrameLayout)findViewById(R.id.content_layout);
		animateFirstListener = new AnimateFirstDisplayListener();

		home = (Button)findViewById(R.id.setting_button_home);
		newsLetter = (Button)findViewById(R.id.setting_button_newsletter);
		events = (Button)findViewById(R.id.setting_button_events);
		settings=(ImageView)findViewById(R.id.settings);
		popup=(LinearLayout)findViewById(R.id.home_layout_popup);
		back = (ImageView)findViewById(R.id.back);
		back.setVisibility(View.GONE);
		
		Singleton.back = back;

		title=(TextView)findViewById(R.id.header_title);


		navigate(new HomeFragment(), "HomeFragment");
		home.setTextColor(getResources().getColor(R.color.orange));
		home.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_white_background));
		newsLetter.setTextColor(getResources().getColor(R.color.white));
		newsLetter.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_orange_background));
		events.setTextColor(getResources().getColor(R.color.white));
		events.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_orange_background));


		naviagteTo = getIntent().getStringExtra("navigate");

		if (naviagteTo != null) {
			
			if(naviagteTo.equals("HomeFragment"))
			{
				navigate(new HomeFragment(), "HomeFragment");
				finish();
			}
		}


		settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initiatePopupWindowinvitefriends();
			}
		});

		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				navigate(new HomeFragment(), "HomeFragment");
				title.setText("Home");
				home.setTextColor(getResources().getColor(R.color.orange));
				home.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_white_background));
				newsLetter.setTextColor(getResources().getColor(R.color.white));
				newsLetter.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_orange_background));
				events.setTextColor(getResources().getColor(R.color.white));
				events.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_orange_background));
				Singleton.back.setVisibility(View.GONE);
			}
		});

		newsLetter.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				title.setText("NewsLetter");
				navigate(new NewsLetterFragment(), "NewsLetterFragment");
				newsLetter.setTextColor(getResources().getColor(R.color.orange));
				newsLetter.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_white_background));
				events.setTextColor(getResources().getColor(R.color.white));
				events.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_orange_background));
				home.setTextColor(getResources().getColor(R.color.white));
				home.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_orange_background));
				Singleton.back.setVisibility(View.GONE);
			}
		});

		events.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				title.setText("Events");
				navigate(new EventsFragment(), "EventsFragment");
				events.setTextColor(getResources().getColor(R.color.orange));
				events.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_white_background));
				home.setTextColor(getResources().getColor(R.color.white));
				home.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_orange_background));
				newsLetter.setTextColor(getResources().getColor(R.color.white));
				newsLetter.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_orange_background));
				Singleton.back.setVisibility(View.GONE);
			}
		});


		content.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});

	}



	PopupWindow pwindoInviteFriends;

	private void initiatePopupWindowinvitefriends() {



		try {


			LayoutInflater inflater = (LayoutInflater) this
					.getSystemService(this.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.popup,
					null);

			LinearLayout dismissPopup;
			TextView changePassword;
			TextView ChangeUserName;
			TextView changeProfile;
			TextView logout;

			dismissPopup=(LinearLayout)v.findViewById(R.id.dismiss_popup);

			changePassword=(TextView)v.findViewById(R.id.change_password);
			//			ChangeUserName=(TextView)v.findViewById(R.id.change_username);
			changeProfile=(TextView)v.findViewById(R.id.change_profile);
			logout=(TextView)v.findViewById(R.id.logout);



			pwindoInviteFriends = new PopupWindow(v);
			pwindoInviteFriends
			.setHeight(WindowManager.LayoutParams.FILL_PARENT);
			pwindoInviteFriends
			.setWidth(WindowManager.LayoutParams.FILL_PARENT);
			pwindoInviteFriends.showAtLocation(v, Gravity.CENTER, 0, 0);


			dismissPopup.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pwindoInviteFriends.dismiss();
				}
			});

			logout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					SharedPreferences pref=getSharedPreferences("LoginDetails", 0);

					Editor editor=pref.edit();
					editor.putBoolean("text",false);

					editor.commit();

					Intent intent=new Intent(MainActivity.this,Viewpager.class);
					startActivity(intent);

					pwindoInviteFriends.dismiss();

					finish();

				}
			});

			changePassword.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					String changePasswordMsg="Are you sure? Do you want to change password?";

					showPasswordChangeAlert(changePasswordMsg);
					pwindoInviteFriends.dismiss();
				}
			});

			/*	ChangeUserName.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					String changeUserNameMsg="Are you sure? Do you want to change UserName?";

					showUserNameChangeAlert(changeUserNameMsg);
					pwindoInviteFriends.dismiss();
				}
			});*/

			changeProfile.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					//					navigate(new ChaneProfileFragment(), "ChaneProfileFragment");

					Intent intent = new Intent(MainActivity.this,ChangeProfileActivity.class);
					startActivity(intent);
					pwindoInviteFriends.dismiss();

					/*title.setText("Profile");

					home.setTextColor(getResources().getColor(R.color.white));
					home.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_orange_background));
					newsLetter.setTextColor(getResources().getColor(R.color.white));
					newsLetter.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_orange_background));
					events.setTextColor(getResources().getColor(R.color.white));
					events.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_orange_background));*/
				}
			});



		} catch (Exception e) {
			e.printStackTrace();
		}


	}


	private void showPasswordChangeAlert(String msg) {


		Button confirm;
		Button cancel;
		
		TextView alertHeader;

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater)this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


		View view_body = inflater.inflate(R.layout.change_password,
				null);
		builder.setView(view_body);

		builder.setInverseBackgroundForced(true);
		confirm = (Button) view_body.findViewById(R.id.confirm);
		cancel = (Button) view_body.findViewById(R.id.cancel);
		currentPassword=(EditText)view_body.findViewById(R.id.current_password);
		newPassword=(EditText)view_body.findViewById(R.id.new_password);
		alertHeader=(TextView)view_body.findViewById(R.id.alert_header);
		confirmPassword=(EditText)view_body.findViewById(R.id.confirm_password);
		confirmPassword.setHint("Confirm Password");
		confirmPassword.setVisibility(View.VISIBLE);
		alertHeader.setText(msg);
		currentPassword.setHint("Old Password");
		newPassword.setHint("New Password");

		passwordOld = currentPassword.getText().toString();

		newPassword.setOnFocusChangeListener(new OnFocusChangeListener() {


			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					//					Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();



					if(currentPassword.getText().toString().equals(""))
					{
						Toast.makeText(getApplicationContext(), "password can't be empty!!", Toast.LENGTH_SHORT).show();
					}
					else
					{


						if(passwordSaved.equals(currentPassword.getText().toString()))
						{
							currentPassword.setError(null);
							Toast.makeText(getApplicationContext(), "password matched", Toast.LENGTH_SHORT).show();
						}
						else
						{
							Toast.makeText(getApplicationContext(), "Wrong Password!! Try Again.", Toast.LENGTH_SHORT).show();
							currentPassword.setError("Wrong Password");
						}
					}

				}else {

				}
			}
		});


		confirm.setOnClickListener(new View.OnClickListener() {


			@Override
			public void onClick(View arg0) { 

/*
				if(passwordOld.equals(""))
				{
					Toast.makeText(getApplicationContext(), "password can't be empty!!", Toast.LENGTH_SHORT).show();
				}*/
				
				if(confirmPassword.getText().toString().equals("") || newPassword.getText().toString().equals(""))
				{
					Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
				}



				else if(confirmPassword.getText().toString()!=null && newPassword.getText().toString()!=null)
				{
					if(!confirmPassword.getText().toString().equals(newPassword.getText().toString()))
					{
						Toast.makeText(getApplicationContext(), "These passwords don't match. Try again?", Toast.LENGTH_SHORT).show();
					}

					else
					{

						if(Util.isOnline(getApplicationContext()))
						{
							new ChangePasswordTask().execute(userNameSaved,newPassword.getText().toString());

							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(currentPassword.getWindowToken(), 0);

							imm.hideSoftInputFromWindow(newPassword.getWindowToken(), 0);
							
							alert.dismiss();
						}
						else
						{
							Toast.makeText(getApplicationContext(), "Please check your internet connection!!", Toast.LENGTH_SHORT).show();
						}


					}
				}
				else
				{
					
				}
			
				



		}
	});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				
				imm.hideSoftInputFromWindow(currentPassword.getWindowToken(), 0);

				imm.hideSoftInputFromWindow(newPassword.getWindowToken(), 0);

				alert.dismiss();
			}
		});

		alert = builder.create();
		if(!alert.isShowing()){
			alert.show();			
		}
}

@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	//		super.onBackPressed();
	//		finish();
	AnimateFirstDisplayListener.displayedImages.clear();

	showAlert("Do you want to exit?");


}



public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

	final static List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		if (loadedImage != null) {
			ImageView imageView = (ImageView) view;
			boolean firstDisplay = !displayedImages.contains(imageUri);
			if (firstDisplay) {
				FadeInBitmapDisplayer.animate(imageView, 500);
				displayedImages.add(imageUri);
			}
		}
	}
}



private void showUserNameChangeAlert(String msg) {


	Button confirm;
	Button cancel;
	final EditText currentPassword;
	final EditText newPassword;
	TextView alertHeader;

	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	LayoutInflater inflater = (LayoutInflater)this
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


	View view_body = inflater.inflate(R.layout.change_password,
			null);
	builder.setView(view_body);

	builder.setInverseBackgroundForced(true);
	confirm = (Button) view_body.findViewById(R.id.confirm);
	cancel = (Button) view_body.findViewById(R.id.cancel);
	currentPassword=(EditText)view_body.findViewById(R.id.current_password);
	newPassword=(EditText)view_body.findViewById(R.id.new_password);
	alertHeader=(TextView)view_body.findViewById(R.id.alert_header);
	alertHeader.setText(msg);
	currentPassword.setHint("Current UserName");
	newPassword.setHint("New UserName");
	confirm.setOnClickListener(new View.OnClickListener() {


		@Override
		public void onClick(View arg0) { 


			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(currentPassword.getWindowToken(), 0);

			imm.hideSoftInputFromWindow(newPassword.getWindowToken(), 0);

			alert.dismiss();


			Toast.makeText(getApplicationContext(), "UserName Changed Successfully!!", Toast.LENGTH_SHORT).show();
		}
	});
	cancel.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {

			alert.dismiss();
		}
	});

	alert = builder.create();
	if(!alert.isShowing()){
		alert.show();			
	}
}


/*	public void fbasync()

	{
		new PsotWallAsync(getApplicationContext()).execute();
	}*/
public void navigate(Fragment frag, String tag)	{
	fm = getSupportFragmentManager();
	if (fm != null)	{

		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.content_layout, frag, tag);
		ft.commit();
	}
}

private void showAlert(String message) {


	Button buttonAlertPositive;
	Button buttonAlertNegative;
	TextView textViewAlertBody;
	TextView alertdialog_rmtv_header;

	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	LayoutInflater inflater = (LayoutInflater)this
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


	View view_body = inflater.inflate(R.layout.alert_body,
			null);
	builder.setView(view_body);

	builder.setInverseBackgroundForced(true);
	alertdialog_rmtv_header=(TextView) view_body.findViewById(R.id.alertdialog_rmtv_header);
	alertdialog_rmtv_header.setText("Are you sure?");
	buttonAlertPositive = (Button) view_body.findViewById(R.id.alert_rmbutton_positive);
	buttonAlertNegative = (Button) view_body.findViewById(R.id.alert_rmbutton_negative);
	textViewAlertBody = (TextView) view_body.findViewById(R.id.alert_rmtv_body);
	textViewAlertBody.setText(message);
	buttonAlertPositive.setText("OK");
	buttonAlertPositive.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View arg0) { 
			finish();
		}
	});
	buttonAlertNegative.setText("Cancel");
	buttonAlertNegative.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			alert.dismiss();
		}
	});

	alert = builder.create();
	if(!alert.isShowing()){
		alert.show();			
	}
}



public class ChangePasswordTask extends AsyncTask<String,Void,String>{

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub

		String username = params[0];
		String newpassword = params[1];


		byte[] data = null;
		try {
			data = newpassword.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String base64 = Base64.encodeToString(data, Base64.DEFAULT);

		WebServices webservices = new WebServices();
		String status = webservices.changePassword(username, base64);

		return status;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		Util.showProgressDialog(activity);
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		Util.dismissProgressDialog();
		if(result!=null)
		{

			if(result.equalsIgnoreCase("true"))
			{
				Toast.makeText(getApplicationContext(), "Password Changed!!", Toast.LENGTH_SHORT).show();


			}
			else
			{
				Toast.makeText(getApplicationContext(), "Sorry password not changed!!", Toast.LENGTH_LONG).show();
			}
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Server Down!!", Toast.LENGTH_LONG).show();
		}
	}
}





}