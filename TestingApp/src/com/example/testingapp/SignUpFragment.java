package com.example.testingapp;

import java.io.UnsupportedEncodingException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pojo.UserAvailability;
import com.example.testingapp.CreateFeedActivity.CreateFeedTask;
import com.example.webservices.WebServices;

public class SignUpFragment extends Fragment{


	private Button submit;

	private EditText name;
	private EditText password;
	private EditText confirmPassword;
	private AlertDialog alert;
	private String passwordString;
	private String usernameString;

	private String optionText1;
	private String optionText2;
	private String optionText3;

	private LinearLayout alternateUserNames;
	private TextView tvOptionText1;
	private TextView tvOptionText2;
	private TextView tvOptionText3;




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.signup_screen, container,false);



		init(view);

		
		
		Singleton.signUpName = name;
		Singleton.signUpPassword = password;

		password.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					//					Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();

					usernameString = name.getText().toString();
					
					if(Util.isOnline(getActivity()))
					{

						
						new UserAvailabilityTask().execute(usernameString);

					}
					else
					{
						Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
					}



				}else {

				}
			}
		});

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(name.getText().toString()==null || name.getText().toString().length()<6)
				{
					String nameText="Name should be more than 6 characters";
					showToast(nameText);
				}
				else if(password.getText().toString()==null || password.getText().toString().length()<8)
				{
					String passwordText="Short passwords are easy to guess. Try one with at least 8 characters";
					showToast(passwordText);
				}
				else if(confirmPassword.getText().toString()!=null)
				{
					if(!confirmPassword.getText().toString().equals(password.getText().toString()))
					{
						String confirmPasswordText="These passwords don't match. Try again?";
						showToast(confirmPasswordText);
					}
					else
					{

						usernameString = name.getText().toString();
						passwordString = password.getText().toString();

						//////String to base 64///////////////

						byte[] data = null;
						try {
							data = passwordString.getBytes("UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String base64 = Base64.encodeToString(data, Base64.DEFAULT);

						///////////////// base64 to string/////////////

						//						byte[] data = Base64.decode(base64, Base64.DEFAULT);
						//						String text = new String(data, "UTF-8");


						//// call webservice//////

						if(Util.isOnline(getActivity()))
						{


							new SignUpTask().execute(usernameString,base64);

						}
						else
						{
							Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
						}



						/*Intent intent2 = new Intent(getActivity(), MainActivity.class);
						startActivity(intent2);
						name.setText("");
						password.setText("");
						confirmPassword.setText("");

						getActivity().finish();*/

					}
				}


			}
		});


		return view;
	}



	private void init(View view)
	{
		/*signup=(Button)view.findViewById(R.id.signup_button_signup);
		login=(Button)view.findViewById(R.id.signup_button_login);*/
		submit=(Button)view.findViewById(R.id.signup_button_submit);
		name=(EditText)view.findViewById(R.id.signup_edit_name);
		password=(EditText)view.findViewById(R.id.signup_edit_password);
		confirmPassword=(EditText)view.findViewById(R.id.signup_edit_confirm_password);
		tvOptionText1 = (TextView)view.findViewById(R.id.option_text1);
		tvOptionText2 = (TextView)view.findViewById(R.id.option_text2);
		tvOptionText3 = (TextView)view.findViewById(R.id.option_text3);

		name.setHintTextColor(getResources().getColor(R.color.orange));
		password.setHintTextColor(getResources().getColor(R.color.orange));
		confirmPassword.setHintTextColor(getResources().getColor(R.color.orange));
		alternateUserNames = (LinearLayout)view.findViewById(R.id.alternative_usernames);
		
		Singleton.alternativeUserName = alternateUserNames;
		
		Singleton.confirmPassword = confirmPassword;


	}

	private void showToast(String message) {


		LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.custom_toast_layout,
				null);

		TextView toastMsg = (TextView)layout.findViewById(R.id.toast_message);


		toastMsg.setText(message);

		int margin = Integer.parseInt(getActivity().getResources().getString(R.string.toastbotton_margin));

		Toast toast = new Toast(getActivity());
		toast.setGravity(Gravity.BOTTOM, 0, margin);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}


	protected class SignUpTask extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String username = params[0];
			String password = params[1];

			WebServices webservices = new WebServices();
			String status = webservices.createUser(username, password);

			return status;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Util.showProgressDialog(getActivity());
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
					Toast.makeText(getActivity(), "Successfully Registered..", Toast.LENGTH_SHORT).show();

					SharedPreferences pref=getActivity().getSharedPreferences("UserName123", 0);

					Editor editor=pref.edit();
					editor.putString("userName123",usernameString);


					editor.commit();
					
					
					SharedPreferences prefPassword=getActivity().getSharedPreferences("password", 0);

					Editor editor2=prefPassword.edit();
					editor2.putString("password",passwordString);


					editor2.commit();

					Intent intent2 = new Intent(getActivity(), MainActivity.class);
					startActivity(intent2);
					/*name.setText("");
					password.setText("");
					confirmPassword.setText("");*/

					getActivity().finish();
				}
				else
				{
					Toast.makeText(getActivity(), "You've already registerd. Please Login!!", Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				Toast.makeText(getActivity(), "Server Down!!", Toast.LENGTH_LONG).show();
			}
		}
	}



	protected class UserAvailabilityTask extends AsyncTask<String,Void,UserAvailability>{

		@Override
		protected UserAvailability doInBackground(String... params) {
			// TODO Auto-generated method stub

			String username = params[0];

			WebServices webservices = new WebServices();
			UserAvailability status = webservices.userAvalibility(username);

			return status;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Util.showProgressDialog(getActivity());
		}

		@Override
		protected void onPostExecute(final UserAvailability result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Util.dismissProgressDialog();
			if(result!=null)
			{

				Log.i("****************", "**************result.getSuccess()****************"+result.getSuccess());
				if(result.getSuccess()!=null)
				{
					if(result.getSuccess().equals("success"))
					{
						Toast.makeText(getActivity(), "UserName Available", Toast.LENGTH_LONG).show();
						alternateUserNames.setVisibility(View.GONE);
						name.setError(null);
					}
				}
				else
				{
					//					Toast.makeText(getActivity(), "UserName  not Available try from any of these" + result.getOption1()+ "\n" +result.getOption2(), Toast.LENGTH_LONG).show();
					InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
							Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(name.getWindowToken(), 0);



					name.setError("Sorry userName alredy used!!");

					alternateUserNames.setVisibility(View.VISIBLE);

					tvOptionText1.setText(result.getOption1());
					tvOptionText2.setText(result.getOption2());
					tvOptionText3.setText(result.getOption3());

					tvOptionText1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							name.setText(result.getOption1());
							alternateUserNames.setVisibility(View.GONE);
							name.setError(null);


						}
					});

					tvOptionText2.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							name.setText(result.getOption2());
							alternateUserNames.setVisibility(View.GONE);
							name.setError(null);
						}
					});

					tvOptionText3.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							name.setText(result.getOption3());
							alternateUserNames.setVisibility(View.GONE);
							name.setError(null);
						}
					});



				}


			}
			else
			{
				Toast.makeText(getActivity(), "Server Down!!", Toast.LENGTH_LONG).show();
			}
		}
	}




}
