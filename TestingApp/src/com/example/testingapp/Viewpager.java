package com.example.testingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

public class Viewpager extends FragmentActivity {

	private ViewPager pager;

	private Button login;
	private Button signup;

	private TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);    
		login=(Button)findViewById(R.id.signup_button_login);
		signup=(Button)findViewById(R.id.signup_button_signup);
		title=(TextView)findViewById(R.id.signup_tv_title);


		pager = (ViewPager) findViewById(R.id.myfivepanelpager);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

		PageListener pageListener = new PageListener();
		pager.setOnPageChangeListener(pageListener);



		signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				title.setText("Sign Up");
				signup.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_orange_background));
				signup.setTextColor(getResources().getColor(R.color.white));

				login.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_white_background));
				login.setTextColor(getResources().getColor(R.color.orange));

				setCurrentItem (1, true);

			}
		});

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				title.setText("Login");






				login.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_orange_background));
				login.setTextColor(getResources().getColor(R.color.white));

				signup.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_white_background));
				signup.setTextColor(getResources().getColor(R.color.orange));

				setCurrentItem (0, true);
			}
		});



	}

	private class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {
			switch(pos) {

			case 0:


				return new LoginFragment();
			case 1: 
				return new SignUpFragment();

			default: 
				return new LoginFragment();
			}
		}



		@Override
		public int getCount() {
			return 2;
		} 


	}

	private class PageListener extends SimpleOnPageChangeListener{
		private int currentPage;

		public void onPageSelected(int position) {
			currentPage = position;

			if(currentPage==0)
			{
							
				title.setText("Login");
				
				Singleton.loginName.setText("");
				Singleton.loginPassword.setText("");
				Singleton.signUpName.setText("");
				Singleton.signUpPassword.setText("");
				Singleton.confirmPassword.setText("");

				login.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_orange_background));
				login.setTextColor(getResources().getColor(R.color.white));

				signup.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_white_background));
				signup.setTextColor(getResources().getColor(R.color.orange));
			}
			else
			{
				title.setText("Sign Up");

				Singleton.loginName.setText("");
				Singleton.loginPassword.setText("");
				Singleton.signUpName.setText("");
				Singleton.signUpPassword.setText("");
				Singleton.confirmPassword.setText("");
				Singleton.signUpName.setError(null);
				Singleton.alternativeUserName.setVisibility(View.GONE);



				signup.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_orange_background));
				signup.setTextColor(getResources().getColor(R.color.white));

				login.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_white_background));
				login.setTextColor(getResources().getColor(R.color.orange));
			}

		}
	}

	public void setCurrentItem (int item, boolean smoothScroll) {
		pager.setCurrentItem(item, smoothScroll);
	}

}





