package com.example.testingapp;

import java.io.UnsupportedEncodingException;

import com.example.testingapp.SignUpFragment.SignUpTask;
import com.example.webservices.WebServices;

import android.app.AlertDialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment{


	//	private Button signup;
	//	private Button login;
	private Button submit;

	private EditText name;
	private EditText password;
	private AlertDialog alert;
	private TextView signupTv;
	private FragmentManager fm;
	private LoginFragment viewPager;

	private String passwordString;
	private String usernameString;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.login_screen, container,false);

		init(view);

		Singleton.loginName = name;
		Singleton.loginPassword = password;

		name.requestFocus();
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {




				if(name.getText().toString().length()==0)
				{
					String nameText="Please enter Name";
					showToast(nameText);
				}

				else if(password.getText().toString().length()==0)
				{
					String passwordText="Please enter Password";
					showToast(passwordText);
				}

				else
				{


					usernameString = name.getText().toString();
					passwordString = password.getText().toString();

					byte[] data = null;
					try {
						data = passwordString.getBytes("UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String base64 = Base64.encodeToString(data, Base64.DEFAULT);

					if(Util.isOnline(getActivity()))
					{


						new LoginTask().execute(usernameString,base64);

					}
					else
					{
						Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
					}



					/*
					SharedPreferences pref=getActivity().getSharedPreferences("LoginDetails", 0);

					Editor editor=pref.edit();
					editor.putBoolean("text",true);

					 editor.commit();

					Intent intent = new Intent(getActivity(), MainActivity.class);
					startActivity(intent);
					name.setText("");
					password.setText("");

					getActivity().finish();*/

				}



			}
		});



		signupTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				((Viewpager)getActivity()).setCurrentItem (1, true);
				new SignUpFragment();

			}
		});




		return view;
	}



	public void navigate(Fragment frag, String tag)	{
		fm = getFragmentManager();
		if (fm != null)	{

			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(android.R.id.content, frag, tag);
			ft.commit();
		}
	}


	private void init(View view)
	{
		/*signup=(Button)view.findViewById(R.id.login_button_signup);
		login=(Button)view.findViewById(R.id.login_button_login);*/
		submit=(Button)view.findViewById(R.id.login_button_submit);
		name=(EditText)view.findViewById(R.id.login_edit_name);
		password=(EditText)view.findViewById(R.id.login_edit_password);
		signupTv=(TextView)view.findViewById(R.id.signup_tv);
		name.setHintTextColor(getResources().getColor(R.color.orange));
		password.setHintTextColor(getResources().getColor(R.color.orange));

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




	protected class LoginTask extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String username = params[0];
			String password = params[1];

			WebServices webservices = new WebServices();
			String status = webservices.Login(username, password);

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

					SharedPreferences pref=getActivity().getSharedPreferences("LoginDetails", 0);

					Editor editor=pref.edit();
					editor.putBoolean("text",true);

					editor.commit();
					
					SharedPreferences prefPassword=getActivity().getSharedPreferences("password", 0);

					Editor editor5=prefPassword.edit();
					editor5.putString("password",passwordString);


					editor5.commit();

					SharedPreferences pref2=getActivity().getSharedPreferences("UserName123", 0);

					Editor editor2=pref2.edit();
					editor2.putString("userName123",name.getText().toString());


					editor2.commit();

					Intent intent = new Intent(getActivity(), MainActivity.class);
					startActivity(intent);
					/*name.setText("");
					password.setText("");*/

					getActivity().finish();
				}
				else
				{
					showToast("Sorry!! Try again. Invalid username and password!!");
				}
			}
			else
			{
				Toast.makeText(getActivity(), "Server Down!!", Toast.LENGTH_LONG).show();
			}
		}
	}

}
