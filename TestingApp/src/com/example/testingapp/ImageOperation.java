package com.example.testingapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/*
 * This class does the image operations like compress and crop on the Bitmap
 * Do not forget to include below permission in the manifest
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
 */
public class ImageOperation {
	private Context context;
	private ImageResultListener onImageResult;
	private String fileCropScaled;

	public interface ImageResultListener{
		public void onImageResult(String fileAbsolutePath);
	}

	public ImageOperation(Context context, ImageResultListener onImageResult){
		this.context = context;
		this.onImageResult = onImageResult;
	}

	/*
	 * Image is cropped from center position and resized to portrait 640x920(widthxheight) pixels
	 * Image is compressed to around 80kb size
	 * All these operations are done in AsyncTask and result image is saved on SD card
	 * and filePath is passed back to the ImageResultListener's onImageResult method
	 * It is adviced to delete this result image after usage.
	 * Input parameter is filePath of selected image from gallery
	 */
	public void cropResizeCompressImage(String filePath){
		ImageCompressionAsyncTask asyncTask = new ImageCompressionAsyncTask(context, onImageResult);
		asyncTask.execute(filePath);
	}

	private class ImageCompressionAsyncTask extends AsyncTask<String, Void, String>{
		private Context context;
		private ImageResultListener onImageResult;
		private ImageCompressionAsyncTask(Context context, ImageResultListener onImageResult){
			this.context = context;
			this.onImageResult = onImageResult;
		}

		@Override
		protected String doInBackground(String... params) {
			
			String filePath = compressImage(params[0]);
			return filePath;
		}

		private String compressImage(String imageUri) {
			String filePath;
			if(imageUri.startsWith("http")){
				String fileName = getFilename(); // 1
				//				getImageSavedFromUrl(imageUri,fileName);
				//				Bitmap bitmap = getBitmapFromURL(imageUri);				
				Bitmap bitmap = fetchImageAsBitmap(imageUri);
				if(bitmap==null){
					return null;
				}
				FileOutputStream out;
				try {
					out = new FileOutputStream(new File(fileName));
					bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
					out.flush();
					out.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				filePath = fileName;
			} else{
				filePath = getRealPathFromURI(imageUri);				
			}

			Bitmap scaledBitmap = null;

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;				
			Bitmap bmp = BitmapFactory.decodeFile(filePath,options);

			int actualHeight = options.outHeight;
			int actualWidth = options.outWidth;
			float maxHeight = 816.0f;
			float maxWidth = 612.0f;
			if(actualHeight==0)
			{
				Log.i("", "********************");
				fileCropScaled = filePath;
				return filePath;
//				return null;
//								Toast.makeText(context, "Error occured", Toast.LENGTH_SHORT).show();
			}else
			{
				float imgRatio = actualWidth / actualHeight;

				float maxRatio = maxWidth / maxHeight;

				if (actualHeight > maxHeight || actualWidth > maxWidth) {
					if (imgRatio < maxRatio) {
						imgRatio = maxHeight / actualHeight;
						actualWidth = (int) (imgRatio * actualWidth);
						actualHeight = (int) maxHeight;
					} else if (imgRatio > maxRatio) {
						imgRatio = maxWidth / actualWidth;
						actualHeight = (int) (imgRatio * actualHeight);
						actualWidth = (int) maxWidth;
					} else {
						actualHeight = (int) maxHeight;
						actualWidth = (int) maxWidth;     

					}
				}


				int samplesize = calculateInSampleSize(options, actualWidth, actualHeight);
				options.inSampleSize = samplesize;
				options.inJustDecodeBounds = false;
				options.inDither = false;
				options.inPurgeable = true;
				options.inInputShareable = true;
				options.inTempStorage = new byte[16*1024];

				try{	
					bmp = BitmapFactory.decodeFile(filePath,options);
				}
				catch(OutOfMemoryError exception){
					exception.printStackTrace();

				}
				try{
					scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
				}
				catch(OutOfMemoryError exception){
					exception.printStackTrace();
				}

				float ratioX = actualWidth / (float) options.outWidth;
				float ratioY = actualHeight / (float)options.outHeight;
				float middleX = actualWidth / 2.0f;
				float middleY = actualHeight / 2.0f;

				Matrix scaleMatrix = new Matrix();
				scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

				Canvas canvas = new Canvas(scaledBitmap);
				canvas.setMatrix(scaleMatrix);
				canvas.drawBitmap(bmp, middleX - bmp.getWidth()/2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


				ExifInterface exif;
				try {
					exif = new ExifInterface(filePath);

					int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
					//				Log.d("EXIF", "Exif: " + orientation);
					Matrix matrix = new Matrix();
					if (orientation == 6) {
						matrix.postRotate(90);
						Log.d("EXIF", "Exif: " + orientation);
					} else if (orientation == 3) {
						matrix.postRotate(180);
						Log.d("EXIF", "Exif: " + orientation);
					} else if (orientation == 8) {
						matrix.postRotate(270);
						Log.d("EXIF", "Exif: " + orientation);
					}
					scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
				} catch (IOException e) {
					e.printStackTrace();
				}
				// delete the old file
				//File file = new File(filePath);
				//boolean deleted = file.delete();
				FileOutputStream out = null;
				String filename = getFilename(); // 2
				try {
					out = new FileOutputStream(filename);
					//				Bitmap croppedBitmap = scaleCenterCrop(scaledBitmap, 640, 920);
					scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
					scaledBitmap.recycle();
					bmp.recycle();
					out.flush();
					out.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Bitmap centerCropBitmap = BitmapFactory.decodeFile((filename));
				File file2 = new File(filename);
				boolean deleted2 = file2.delete();
				Bitmap finalBitmap = scaleCenterCrop(centerCropBitmap, 920, 640);
				centerCropBitmap.recycle();
				///
				FileOutputStream out2 = null;
				fileCropScaled = getFilename(); // 3
				try {
					out2 = new FileOutputStream(fileCropScaled);
					//				Bitmap croppedBitmap = scaleCenterCrop(scaledBitmap, 640, 920);
					finalBitmap.compress(Bitmap.CompressFormat.JPEG, 60, out2);
					out2.flush();
					out2.close();
					//				File file = new File(filename);
					//				boolean deleted = file.delete();
					//				Log.i("&&&&&&&&&&&&&", "***************deleted****************** "+deleted);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return fileCropScaled;

		}

		private Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
			int sourceWidth = source.getWidth();
			int sourceHeight = source.getHeight();

			// Compute the scaling factors to fit the new height and width, respectively.
			// To cover the final image, the final scaling will be the bigger 
			// of these two.
			float xScale = (float) newWidth / sourceWidth;
			float yScale = (float) newHeight / sourceHeight;
			float scale = Math.max(xScale, yScale);

			// Now get the size of the source bitmap when scaled
			float scaledWidth = scale * sourceWidth;
			float scaledHeight = scale * sourceHeight;

			// Let's find out the upper left coordinates if the scaled bitmap
			// should be centered in the new size give by the parameters
			float left = (newWidth - scaledWidth) / 2;
			float top = (newHeight - scaledHeight) / 2;

			// The target rectangle for the new, scaled version of the source bitmap will now
			// be
			RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

			// Finally, we create a new bitmap of the specified size and draw our new,
			// scaled bitmap onto it.
			Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(dest);
			canvas.drawBitmap(source, null, targetRect, null);

			return dest;
		}		

		@Override
		protected void onPostExecute(String result) {			 
			super.onPostExecute(result);
			String fileAbsolutePath = result;
			onImageResult.onImageResult(fileAbsolutePath);
		}

	}

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		final float totalPixels = width * height;
		final float totalReqPixelsCap = reqWidth * reqHeight * 2;

		while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
			inSampleSize++;
		}

		return inSampleSize;
	}

	/*private Bitmap decodeBitmapFromPath(String filePath){
		Bitmap scaledBitmap = null;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;						
		scaledBitmap = BitmapFactory.decodeFile(filePath,options);

		options.inSampleSize = calculateInSampleSize(options, convertDipToPixels(150), convertDipToPixels(200));
		options.inDither = false;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inJustDecodeBounds = false;

		scaledBitmap = BitmapFactory.decodeFile(filePath, options);		
		return scaledBitmap;
	}*/

	/*private int convertDipToPixels(float dips){
		 Resources r = context.getResources();
		 return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dips, r.getDisplayMetrics());
	}*/

	private String getRealPathFromURI(String contentURI) {
		Uri contentUri = Uri.parse(contentURI);
		Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
		if (cursor == null) {
			return contentUri.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			return cursor.getString(idx);
		}
	}

	private String getFilename() {
		File file = new File(Environment.getExternalStorageDirectory().getPath(), "FissionApp");
		if (!file.exists()) {
			file.mkdirs();
		}
		String uriSting = (file.getAbsolutePath() + "/"+ System.currentTimeMillis() + ".jpg");
		return uriSting;

	}

	public String screenshot(View view){
		String mPath = getFilename();

		// create bitmap screen capture
		Bitmap bitmap;
		View v1 = view;
		v1.setDrawingCacheEnabled(true);
		bitmap = Bitmap.createBitmap(v1.getDrawingCache());
		v1.setDrawingCacheEnabled(false);
		// The target rectangle for the new, scaled version of the source bitmap will now
		// be
		RectF targetRect = new RectF(0, 0, 640, 920);
		// Finally, we create a new bitmap of the specified size and draw our new,
		// scaled bitmap onto it.
		Bitmap dest = Bitmap.createBitmap(640, 920, bitmap.getConfig());
		Canvas canvas = new Canvas(dest);
		canvas.drawBitmap(bitmap, null, targetRect, null);
		OutputStream fout = null;
		File imageFile = new File(mPath);
		try {
			fout = new FileOutputStream(imageFile);
			dest.compress(Bitmap.CompressFormat.JPEG, 100, fout);
			fout.flush();
			fout.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mPath;
	}

	public void getImageSavedFromUrl(String link, String fileName) {
		URL url;
		try {
			url = new URL(link);
			InputStream input = url.openStream();
			File file = new File(fileName);
			OutputStream output = new FileOutputStream (file);
			byte[] buffer = new byte[16*1024];
			int bytesRead = 0;
			while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
				try {
					output.write(buffer, 0, bytesRead);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			output.close();
			input.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Bitmap getBitmapFromURL(String link) {
		/*--- this method downloads an Image from the given URL, 
		 *  then decodes and returns a Bitmap object
	     ---*/
		try {
			URL url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			//  connection.se
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);

			return myBitmap;

		} catch (IOException e) {
			e.printStackTrace();
			Log.e("getBmpFromUrl error: ", e.getMessage().toString());
			return null;
		}
	}

	private Bitmap fetchImageAsBitmap(String link){
		Bitmap bm = null;
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(link);
		try {
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			bm = BitmapFactory.decodeStream(new FlushedInputStream(entity.getContent()));
		} catch (Exception e) {
			bm = null;
		}
		return bm;
	}


}
