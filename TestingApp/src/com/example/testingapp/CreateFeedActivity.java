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
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testingapp.ImageOperation.ImageResultListener;
import com.example.webservices.WebServices;

public class CreateFeedActivity extends Activity{

	private ImageView camera;
	private Activity activity;
	private Uri photoUri = null;
	private ImageView feedImage;
	private TextView feedSave;
	private TextView feedCancel;
	private Button popupCamera;
	private Button popupGallery;
	private FrameLayout frame;
	private RelativeLayout rlayout;
	private EditText feedEditText;
	private String userNameSaved;

	private Button cancelButton;
	
	private Bitmap bm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_feed);
		activity = CreateFeedActivity.this;



		SharedPreferences pref=getSharedPreferences("UserName123", 0);
		userNameSaved = pref.getString("userName123", null);



		camera = (ImageView) findViewById(R.id.cam_gallery_img);
		feedImage = (ImageView) findViewById(R.id.feed_image);
		feedSave = (TextView) findViewById(R.id.feed_save);
		feedCancel = (TextView) findViewById(R.id.feed_cancel);
		popupCamera = (Button)findViewById(R.id.camera);
		popupGallery = (Button)findViewById(R.id.gallery);
		frame = (FrameLayout)findViewById(R.id.frame);
		rlayout = (RelativeLayout)findViewById(R.id.main_layout_feed);
		feedEditText = (EditText)findViewById(R.id.feed_edittext);
		cancelButton = (Button)findViewById(R.id.cancel_button);

		rlayout.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
				return false;
			}
		});

		frame.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
				return false;
			}
		});




		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//				initiatePopupWindowCamGall();

				if(popupCamera.getVisibility()==View.VISIBLE)
				{
					popupCamera.setVisibility(View.INVISIBLE);
					popupGallery.setVisibility(View.INVISIBLE);
					cancelButton.setVisibility(View.INVISIBLE);
				}
				else{
					popupCamera.setVisibility(View.VISIBLE);
					popupGallery.setVisibility(View.VISIBLE);
					cancelButton.setVisibility(View.VISIBLE);
				}


			}
		});
		frame.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupCamera.setVisibility(View.INVISIBLE);
				popupGallery.setVisibility(View.INVISIBLE);
				cancelButton.setVisibility(View.INVISIBLE);

			}
		});
		feedCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent= new Intent(CreateFeedActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		feedSave.setOnClickListener(new OnClickListener() {

			

			@Override
			public void onClick(View v) {


				if(Util.isOnline(CreateFeedActivity.this))
				{

					if(feedEditText.getText().toString().length()==0)
					{
						Toast.makeText(getApplicationContext(), "Feed Cannot be empty!!", Toast.LENGTH_LONG).show();
					}
					else{
						
						/*Bitmap bitmapOrg = ((BitmapDrawable)feedImage.getDrawable()).getBitmap();
						
//						Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),  R.drawable.image);
						ByteArrayOutputStream bao = new ByteArrayOutputStream();
						bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 100, bao);
						byte [] ba = bao.toByteArray();
						String ba1=Base64.encodeToString(ba,Base64.DEFAULT);*/
						
						
//						Bitmap viewBitmap = Bitmap.createBitmap(feedImage.getWidth(),feedImage.getHeight(),Bitmap.Config.ARGB_8888);//i is imageview whch u want to convert in bitmap
						
						if(feedImage.getDrawable() == null)
						{
						
							new CreateFeedTextTask().execute(userNameSaved,feedEditText.getText().toString());
						
						}
						else
						{
							final BitmapDrawable bitmapDrawable = (BitmapDrawable) feedImage.getDrawable();
				            final Bitmap yourBitmap = bitmapDrawable.getBitmap();

							new CreateFeedTask(userNameSaved,feedEditText.getText().toString(),yourBitmap).execute();
						}
						
					}

				}
				else
				{
					Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
				}


			}
		});
		popupCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openCamera();
				popupCamera.setVisibility(View.GONE);
				popupGallery.setVisibility(View.GONE);
				cancelButton.setVisibility(View.GONE);
			}
		});

		popupGallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openGallery();
				popupGallery.setVisibility(View.GONE);
				popupCamera.setVisibility(View.GONE);
				cancelButton.setVisibility(View.GONE);
			}
		});

		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupGallery.setVisibility(View.GONE);
				popupCamera.setVisibility(View.GONE);
				cancelButton.setVisibility(View.GONE);
			}
		});


	}




	protected boolean isDeviceSupportCamera() {
		if (activity.getPackageManager().hasSystemFeature(
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
		Log.i("onActivityResult", "****** requestCode = "+requestCode);
//		Toast.makeText(getApplicationContext(), "requestCode "+requestCode, Toast.LENGTH_LONG).show();
		if(requestCode==100)
		{

			if (data != null) {
			/*	activity.getContentResolver().notifyChange(photoUri, null);
				String photoPath = photoUri.getPath();*/
				ImageOperation imageOperation = new ImageOperation(
						activity, imageListener);
				imageOperation.cropResizeCompressImage(data.getDataString());
//				imageOperation.cropResizeCompressImage(data.getDataString());

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
							activity, imageListener);
					imageOperation.cropResizeCompressImage(data.getDataString());
//					Toast.makeText(getApplicationContext(), "result from gallery", Toast.LENGTH_LONG).show();
					
					 /*Uri selectedImageUri = data.getData();
//		                
		                feedImage.setImageURI(selectedImageUri);*/

				}
			}
		}


	}
	
	public String getPath(Uri uri) {
        // just some safety built in 
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
}

	ImageResultListener imageListener = new ImageResultListener() {
		private String path;

		@Override
		public void onImageResult(String fileAbsolutePath) {
			path = fileAbsolutePath;
			if(fileAbsolutePath==null)
			{
				Toast.makeText(getApplicationContext().getApplicationContext(), "Error retrieving the image.Please try again.", Toast.LENGTH_SHORT).show();
			}
			else
			{
				feedImage.setImageURI(Uri.fromFile(new File(fileAbsolutePath)));

			}	

		}
	};
	private String userName;

	protected void openCamera() {
		if (isDeviceSupportCamera()) {
			
			/*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File photoFile = new File(getImagePath());
			photoUri = Uri.fromFile(photoFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);*/
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


	protected class CreateFeedTextTask extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String username = params[0];
			String feedText = params[1];

			WebServices webservices = new WebServices();
			String status = webservices.CreateFeed(username, feedText);

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



					Toast.makeText(getApplicationContext(), "Feed Created Successfully", Toast.LENGTH_SHORT).show();
					Intent intent= new Intent(CreateFeedActivity.this,MainActivity.class);

					startActivity(intent);
					finish();



					finish();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Sorry try again!! Feed not created.", Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Server Down!!", Toast.LENGTH_LONG).show();
			}
		}
	}

	
	
	
	protected class CreateFeedTask extends AsyncTask<String,Void,Void>{
		
		String username;
		String feedText;
		Bitmap bmp;

		public CreateFeedTask(String username,String feedText,Bitmap bmp)
		{
			this.username = username;
			this.feedText = feedText;
			this.bmp=bmp;
		}

		@Override
		protected Void doInBackground(String... params) {

			
			try {
				executeMultipartPost(username, feedText, bmp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Util.showProgressDialog(activity);
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Util.dismissProgressDialog();
			Intent intent= new Intent(CreateFeedActivity.this,MainActivity.class);

			startActivity(intent);
			finish();
			/*if(result!=null)
			{

				if(result.equalsIgnoreCase("true"))
				{



					Toast.makeText(getApplicationContext(), "Feed Created Successfully", Toast.LENGTH_SHORT).show();
					Intent intent= new Intent(CreateFeedActivity.this,MainActivity.class);

					startActivity(intent);
					finish();



					finish();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Sorry try again!! Feed not created.", Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Server Down!!", Toast.LENGTH_LONG).show();
			}*/
		}
	}

	
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent= new Intent(CreateFeedActivity.this,MainActivity.class);

		startActivity(intent);
		finish();
	}
	
	
	
	

	public void executeMultipartPost(String username, String feedText, Bitmap bm) throws Exception {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bm.compress(CompressFormat.JPEG, 75, bos);
            byte[] data = bos.toByteArray();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(Util.URL_CREATE_FEED);
            ByteArrayBody bab = new ByteArrayBody(data, "forest.jpg");
            // File file= new File("/mnt/sdcard/forest.png");
            // FileBody bin = new FileBody(file);
            MultipartEntity reqEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("feedURL", bab);
            reqEntity.addPart("userName", new StringBody(userNameSaved));
            
            reqEntity.addPart("Feedtext", new StringBody(feedText));
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



}
