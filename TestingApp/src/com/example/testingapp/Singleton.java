package com.example.testingapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Singleton {
	
	private static Singleton INSTANCE;

	public static Activity activity;
	public static ArrayList<Friend> frendsList;
	public static EditText loginName;
	public static EditText loginPassword;
	public static EditText signUpName;
	public static EditText signUpPassword;
	public static LinearLayout alternativeUserName;
	public static EditText confirmPassword;

	public static Bitmap urlToBmp;

	public static ImageView back;

	public static String profilepicUrl;

	
	
	public static Singleton getInstance() 
	{
		if(INSTANCE == null) 
		{
			INSTANCE = new Singleton();
		}
		return INSTANCE;
	}
	
	public void setNormalFont2(Context context,View tv,int code)
	{
		Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/helvetica_normal_font.otf");
		if(code==0)
		{
			Button view_font=(Button) tv;
			view_font.setTypeface(tf);

		} 

		else  if(code==1)
		{
			EditText view_font=(EditText) tv;
			view_font.setTypeface(tf);

		}
		else if(code==2)
		{
			TextView view_font=(TextView)tv;
			view_font.setTypeface(tf);
		}
	}
	
	public void setNormalTypeFace_other(Context context,View tv,int code)
	{
		Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/helvetica_other_fonts.otf");
		if(code==0)
		{
			Button view_font=(Button) tv; 
			view_font.setTypeface(tf);

		} 

		else  if(code==1)
		{
			EditText view_font=(EditText) tv;
			view_font.setTypeface(tf);

		}
		else if(code==2)
		{
			TextView view_font=(TextView)tv;
			view_font.setTypeface(tf);
		}
	}

}
