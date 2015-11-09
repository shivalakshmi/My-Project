package com.example.testingapp;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class NewsLetterFragment extends Fragment {
	private WebView webView;
	private ProgressDialog progDailog;  // loader 
	String GoogleDocs="http://docs.google.com/gview?embedded=true&url="; 
	private ImageView button1;

	@SuppressLint("NewApi")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.newsletter_screen, container, false);
		webView = (WebView)view.findViewById(R.id.webview);
		button1 = (ImageView)view.findViewById(R.id.button1);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);


		if(Util.isOnline(getActivity()))
		{

			progDailog = ProgressDialog.show(getActivity(), "Loading","Please wait...", true);
			progDailog.setCancelable(false);  

			webView.getSettings().setJavaScriptEnabled(true);    
			webView.getSettings().setLoadWithOverviewMode(true);
			webView.getSettings().setUseWideViewPort(true); 

			webView.setWebViewClient(new WebViewClient(){

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					progDailog.show();
					view.loadUrl(url);

					return true;               
				}
				@Override
				public void onPageFinished(WebView view, final String url) {
					progDailog.dismiss();
				}
			});
			webView.loadUrl(GoogleDocs+"http://www.adobe.com/content/dam/Adobe/en/devnet/acrobat/pdfs/pdf_open_parameters.pdf");   // webview loader to load the URL of file
		}
		else
		{
			Toast.makeText(getActivity(), "Please Your Check Internet Connection", Toast.LENGTH_LONG).show();
		}

		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.adobe.com/content/dam/Adobe/en/devnet/acrobat/pdfs/pdf_open_parameters.pdf"));
				startActivity(browserIntent);*/
				
				
				File folder = new File(Environment.getExternalStorageDirectory() + "/FissionLabs");
				boolean success = true;
				if (!folder.exists()) {
				    success = folder.mkdir();
				}
				if (success) {
				    // Do something on success
					File myFile=new File(Environment.getExternalStorageDirectory() +"/FissionLabs/"+ "NewsLetter.pdf");
					
					new downloadPDF("http://www.adobe.com/content/dam/Adobe/en/devnet/acrobat/pdfs/pdf_open_parameters.pdf", myFile).execute();
				} else {
				    // Do something else on failure 
				}


			}
		});


		return view;
	}

	class downloadPDF extends AsyncTask<Void, Void, Void>
	{

		String url;
		File outputFile;
		
		//  constructor
		public downloadPDF(String url,File outputFle) {

			this.outputFile=outputFle;
			this.url=url;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progDailog.show();
			
			Toast.makeText(getActivity(), "Started Download...", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progDailog.dismiss();
			Toast.makeText(getActivity(), "File Download Succesfully...", Toast.LENGTH_SHORT).show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			try {
				URL u = new URL(url);
				URLConnection conn = u.openConnection();
				int contentLength = conn.getContentLength();

				DataInputStream stream = new DataInputStream(u.openStream());

				byte[] buffer = new byte[contentLength];
				stream.readFully(buffer);
				stream.close();

				DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
				fos.write(buffer);
				fos.flush();
				fos.close();
			} catch(FileNotFoundException e) {
				return null; // swallow a 404
			} catch (IOException e) {
				return null; // swallow a 404
			}
			return null;
		}
		
		



	}




}
