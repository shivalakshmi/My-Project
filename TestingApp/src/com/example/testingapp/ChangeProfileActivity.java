package com.example.testingapp;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pojo.ProfileDetails;
import com.example.testingapp.ImageOperation.ImageResultListener;
import com.example.webservices.WebServices;
import com.nostra13.universalimageloader.core.ImageLoader;



public class ChangeProfileActivity extends Activity {

	private TextView dob;
	private TextView designation;
	private TextView about;
	private EditText dobEditText;
	private EditText designationEditText;
	private EditText aboutEditText;

	private TextView dobEditButton;
	private TextView designationEditButton;
	private TextView aboutEditButton;

	private TextView cancelProfile;


	private String unchangedDob;
	private String unchangedDesignation;
	private String unchangedAboutMe;
	private TextView profile_name;

	private FrameLayout tvChangeProfilePic;
	private ImageView imageViewEdit;

	private Bitmap unchangedProfilepic;

	private ImageView profilePic;
	private LinearLayout mcamera_layout;
	private LinearLayout mgallery_layout;

	private Uri photoUri;
	private String userNameSaved;


	private LinearLayout saveStatusDOB;
	private LinearLayout saveStatusdesignation;
	private LinearLayout saveStatusAbout;


	private ImageView saveStatusDOBtrue;
	private ImageView saveStatusDOBfalse;

	private ImageView saveStatusdesignationtrue;
	private ImageView saveStatusdesignationfalse;

	private ImageView saveStatusAbouttrue;
	private ImageView saveStatusAboutfalse;

	private Activity activity;

	private Bitmap bm;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_profile_screen);

		activity = ChangeProfileActivity.this;

		SharedPreferences pref=getSharedPreferences("UserName123", 0);
		userNameSaved = pref.getString("userName123", null);



		dob=(TextView)findViewById(R.id.dob);
		designation=(TextView)findViewById(R.id.designation);
		about=(TextView)findViewById(R.id.about);
		profile_name=(TextView)findViewById(R.id.username);

		tvChangeProfilePic=(FrameLayout)findViewById(R.id.change_profile_pic);
		imageViewEdit=(ImageView)findViewById(R.id.edit_user);



		dobEditText=(EditText)findViewById(R.id.dob_edit);
		designationEditText=(EditText)findViewById(R.id.designation_edit);
		aboutEditText=(EditText)findViewById(R.id.about_edit);

		dobEditButton=(TextView)findViewById(R.id.dob_edit_button);
		designationEditButton=(TextView)findViewById(R.id.designation_edit_button);
		aboutEditButton=(TextView)findViewById(R.id.about_edit_button);

		profilePic=(ImageView)findViewById(R.id.profile_picture);

		unchangedProfilepic=((BitmapDrawable)profilePic.getDrawable()).getBitmap();


		cancelProfile=(TextView)findViewById(R.id.cancel_profile);

		saveStatusAbout = (LinearLayout)findViewById(R.id.save_status_about);
		saveStatusdesignation = (LinearLayout)findViewById(R.id.save_status_designation);
		saveStatusDOB = (LinearLayout)findViewById(R.id.save_status_dob);

		saveStatusAbouttrue = (ImageView)findViewById(R.id.save_status_about_true);
		saveStatusAboutfalse= (ImageView)findViewById(R.id.save_status_about_false);

		saveStatusdesignationtrue = (ImageView)findViewById(R.id.save_status_designation_true);
		saveStatusdesignationfalse= (ImageView)findViewById(R.id.save_status_designation_false);

		saveStatusDOBtrue = (ImageView)findViewById(R.id.save_status_dob_true);
		saveStatusDOBfalse= (ImageView)findViewById(R.id.save_status_dob_false);


		if(Util.isOnline(getApplicationContext()))
		{


			new ProfileDetailsTask().execute(userNameSaved);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
		}



		if(userNameSaved!=null)
		{

			profile_name.setText(userNameSaved);
		}


		imageViewEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


				if(tvChangeProfilePic.getVisibility()==View.VISIBLE)
				{
					
					tvChangeProfilePic.setVisibility(View.GONE);
				}
				else
				{
					tvChangeProfilePic.setVisibility(View.VISIBLE);
				}


			}
		});


		tvChangeProfilePic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//				showDialog();
				initiatePopupWindowSelectPhoto();

			}
		});


		dobEditButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


				saveStatusDOB.setVisibility(View.VISIBLE);
				//				saveStatusAbout.setVisibility(View.GONE);
				//				saveStatusdesignation.setVisibility(View.GONE);

				dob.setVisibility(View.GONE);
				dobEditText.setVisibility(View.VISIBLE);
				dobEditText.requestFocus();

				if(dob.getText().toString().equals("Not Specified"))
				{

				}
				else
				{
					dobEditText.setText(dob.getText().toString());

				}

			}
		});


		designationEditButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				saveStatusdesignation.setVisibility(View.VISIBLE);


				//				saveStatusDOB.setVisibility(View.GONE);
				//				saveStatusAbout.setVisibility(View.GONE);
				designationEditText.requestFocus();

				designation.setVisibility(View.GONE);
				designationEditText.setVisibility(View.VISIBLE);
				designationEditText.requestFocus();

				if(designation.getText().toString().equals("Not Specified"))
				{

				}
				else
				{
					designationEditText.setText(designation.getText().toString());
				}


			}
		});

		aboutEditButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				saveStatusAbout.setVisibility(View.VISIBLE);
				//				saveStatusDOB.setVisibility(View.GONE);
				//				saveStatusdesignation.setVisibility(View.GONE);
				aboutEditText.requestFocus();

				about.setVisibility(View.GONE);
				aboutEditText.setVisibility(View.VISIBLE);
				aboutEditText.requestFocus();

				if(about.getText().toString().equals("Not Specified"))
				{

				}
				else
				{
					aboutEditText.setText(about.getText().toString());
				}


			}
		});


		saveStatusAbouttrue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				about.setVisibility(View.VISIBLE);
				about.setText(aboutEditText.getText().toString());
				saveStatusAbout.setVisibility(View.GONE);
				aboutEditText.setVisibility(View.GONE);


				if(Util.isOnline(getApplicationContext()))
				{


					new UpdateAboutTask().execute(userNameSaved,aboutEditText.getText().toString());
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
				}

			}
		});


		saveStatusAboutfalse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				about.setVisibility(View.VISIBLE);
				about.setText(unchangedAboutMe);
				saveStatusAbout.setVisibility(View.GONE);
				aboutEditText.setVisibility(View.GONE);
			}
		});


		saveStatusdesignationtrue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				designation.setVisibility(View.VISIBLE);
				designation.setText(designationEditText.getText().toString());
				saveStatusdesignation.setVisibility(View.GONE);
				designationEditText.setVisibility(View.GONE);

				if(Util.isOnline(getApplicationContext()))
				{


					new UpdateDesignationTask().execute(userNameSaved,designationEditText.getText().toString());
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
				}
			}
		});


		saveStatusdesignationfalse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				designation.setVisibility(View.VISIBLE);
				designation.setText(unchangedDesignation);
				saveStatusdesignation.setVisibility(View.GONE);
				designationEditText.setVisibility(View.GONE);

			}
		});


		saveStatusDOBtrue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dob.setVisibility(View.VISIBLE);
				dob.setText(dobEditText.getText().toString());
				saveStatusDOB.setVisibility(View.GONE);
				dobEditText.setVisibility(View.GONE);

				if(Util.isOnline(getApplicationContext()))
				{

					new UpdateDOBTask().execute(userNameSaved,dobEditText.getText().toString());

				}
				else
				{
					Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
				}
			}
		});


		saveStatusDOBfalse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dob.setVisibility(View.VISIBLE);
				dob.setText(unchangedDob);
				saveStatusDOB.setVisibility(View.GONE);
				dobEditText.setVisibility(View.GONE);

			}
		});




		cancelProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}
		});

	}




	PopupWindow pwindo;

	private void initiatePopupWindowSelectPhoto() {

		try {


			LayoutInflater inflater = (LayoutInflater)this
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.alert,
					null);

			pwindo = new PopupWindow(v);
			pwindo
			.setHeight(WindowManager.LayoutParams.FILL_PARENT);
			pwindo
			.setWidth(WindowManager.LayoutParams.FILL_PARENT);
			pwindo.showAtLocation(v, Gravity.CENTER, 0, 0);


			LinearLayout camgallLayout=(LinearLayout)v.findViewById(R.id.camgall_layout);

			mcamera_layout = (LinearLayout)v
					.findViewById(R.id.alert_camera_layout);


			mgallery_layout = (LinearLayout) v
					.findViewById(R.id.alert_gallery_layout);
			mcamera_layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					openCamera();
					tvChangeProfilePic.setVisibility(View.GONE);

					/*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
					//         startActivityForResult(cameraIntent, CAMERA_REQUEST); 
					startActivityForResult(cameraIntent, 100);*/

					pwindo.dismiss();
				}
			});
			mgallery_layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					/*
					Intent i = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

					startActivityForResult(i, 200);*/
					openGallery();

					tvChangeProfilePic.setVisibility(View.GONE);
					pwindo.dismiss();
				}
			});

			camgallLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					pwindo.dismiss();

				}
			});


		} catch (Exception e) {
			e.printStackTrace();
		}


	}


	protected boolean isDeviceSupportCamera() {
		if (getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}

	}

	private String getImagePath() {
		File file = new File(Environment.getExternalStorageDirectory()
				.getPath(), "Rumor");
		if (!file.exists()) {
			file.mkdirs();
		}
		String uriSting = (file.getAbsolutePath() + "/" + "temp" + ".jpg");
		return uriSting;

	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==100)
		{

			if (data != null) {
				/*getContentResolver().notifyChange(photoUri, null);
				String photoPath = photoUri.getPath();*/
				ImageOperation imageOperation = new ImageOperation(
						this, imageListener);
				imageOperation.cropResizeCompressImage(data.getDataString());

			} else {
				Toast.makeText(getApplicationContext(), "Problem accessing a picture",
						Toast.LENGTH_LONG).show();
			}
		}
		else
		{

			if (data != null) {
				if (data.getDataString().toString()
						.contains("gallery3d.provider/picasa")) {
					Toast.makeText(getApplicationContext(),
							"Access denied to access this folder",
							Toast.LENGTH_LONG).show();
				} else {
					ImageOperation imageOperation = new ImageOperation(
							getApplicationContext(), imageListener);
					imageOperation.cropResizeCompressImage(data.getDataString());

				}
			}
		}


	}

	ImageResultListener imageListener = new ImageResultListener() {
		private String path;

		@Override
		public void onImageResult(String fileAbsolutePath) {
			path = fileAbsolutePath;
			if(path==null)
			{
				Toast.makeText(getApplicationContext(), "Error retrieving the image.Please try again.", Toast.LENGTH_SHORT).show();
			}
			else
			{
				
				profilePic.setImageURI(Uri.fromFile(new File(fileAbsolutePath)));
				
				Log.i("************************", "new File(fileAbsolutePath)   new File(fileAbsolutePath)"+fileAbsolutePath);
				
				bm = BitmapFactory.decodeFile(fileAbsolutePath);
				
				new ChangePic().execute();
				
			/*	 int loader = R.drawable.ic_stub;
				 String image_url = "http://api.androidhive.info/images/sample.jpg";
		         
			        // ImageLoader class instance
			        ImageLoaderProfile imgLoader = new ImageLoaderProfile(getApplicationContext());
			         
			        // whenever you want to load an image from url
			        // call DisplayImage function
			        // url - image url to load
			        // loader - loader image, will be displayed before getting image
			        // image - ImageView 
			        imgLoader.DisplayImage(image_url, loader, profilePic);*/
				

			}	

		}
	};
	
	

	public void executeMultipartPost() throws Exception {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bm.compress(CompressFormat.JPEG, 75, bos);
            byte[] data = bos.toByteArray();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(Util.URL_CHANGE_PICTURE);
            ByteArrayBody bab = new ByteArrayBody(data, "forest.jpg");
            // File file= new File("/mnt/sdcard/forest.png");
            // FileBody bin = new FileBody(file);
            MultipartEntity reqEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("profilePic", bab);
            reqEntity.addPart("userName", new StringBody(userNameSaved));
            postRequest.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(postRequest);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();
 
            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
            System.out.println("Response: Image MUlti part......" + s);
            
            String result = String.valueOf(s);
            
            if(result.equals("true"))
            {
            	Toast.makeText(activity.getApplicationContext(), "PIC Changed Successfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
            	Toast.makeText(activity.getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // handle exception here
            e.printStackTrace();
        }
    }

	protected void openCamera() {
		if (isDeviceSupportCamera()) {
			/*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File photoFile = new File(getImagePath());
			photoUri = Uri.fromFile(photoFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			startActivityForResult(intent, 100);*/
			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, 100);
		} else {
			Toast.makeText(getApplicationContext(),
					"Your device doesn't support Camera", Toast.LENGTH_LONG)
					.show();
		}
	}

	protected void openGallery() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, 200);
	}

	
	protected class ChangePic extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			try {
				executeMultipartPost();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}

	protected class UpdateAboutTask extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String username = params[0];
			String aboutMeText = params[1];

			WebServices webservices = new WebServices();
			String status = webservices.updateAboutMe(username, aboutMeText);

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
					Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();



				}
				else
				{
					Toast.makeText(getApplicationContext(), "Not Saved!!", Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Server Down!!", Toast.LENGTH_LONG).show();
			}
		}
	}




	protected class UpdateDesignationTask extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String username = params[0];
			String designationText = params[1];

			WebServices webservices = new WebServices();
			String status = webservices.updateDesignation(username, designationText);

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
					Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();



				}
				else
				{
					Toast.makeText(getApplicationContext(), "Not Saved!!", Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Server Down!!", Toast.LENGTH_LONG).show();
			}
		}
	}




	protected class UpdateDOBTask extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String username = params[0];
			String dobText = params[1];

			WebServices webservices = new WebServices();
			String status = webservices.updateDOB(username, dobText);

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
					Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();



				}
				else
				{
					Toast.makeText(getApplicationContext(), "Not Saved!!", Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Server Down!!", Toast.LENGTH_LONG).show();
			}
		}
	}



	protected class ProfileDetailsTask extends AsyncTask<String,Void,ProfileDetails>{

		@Override
		protected ProfileDetails doInBackground(String... params) {
			// TODO Auto-generated method stub

			String username = params[0];

			WebServices webservices = new WebServices();
			ProfileDetails profiledetails = webservices.getProfileDetails(username);

			return profiledetails;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Util.showProgressDialog(activity);
		}

		@Override
		protected void onPostExecute(ProfileDetails result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Util.dismissProgressDialog();
			if(result!=null)
			{

				dob.setText(result.getDOB());
				designation.setText(result.getDesignation());
				about.setText(result.getAboutMe());

				unchangedDob=dob.getText().toString();
				unchangedDesignation=designation.getText().toString();
				unchangedAboutMe=about.getText().toString();
				

				 int loader = R.drawable.ic_stub;
				 String image_url = result.getProfilePic();
		         
			        // ImageLoader class instance
			        ImageLoaderProfile imgLoader = new ImageLoaderProfile(getApplicationContext());
			         
			        // whenever you want to load an image from url
			        // call DisplayImage function
			        // url - image url to load
			        // loader - loader image, will be displayed before getting image
			        // image - ImageView 
			        imgLoader.DisplayImage(image_url, loader, profilePic);
				


				////// Assign values to profile info
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Server Down!!", Toast.LENGTH_LONG).show();
			}
		}
	}

}






