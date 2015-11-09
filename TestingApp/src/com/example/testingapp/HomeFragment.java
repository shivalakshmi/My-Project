package com.example.testingapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pojo.ProfileDetails;
import com.example.webservices.WebServices;
import com.gpapps.rumormill.pulltorefresh.PullToRefreshListView;
import com.gpapps.rumormill.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.imagezoom.ImageAttacher;
import com.imagezoom.ImageAttacher.OnMatrixChangedListener;
import com.imagezoom.ImageAttacher.OnPhotoTapListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


public class HomeFragment extends Fragment{

	static final double SECOND = 1;
	static final double MINUTE = 60*SECOND;
	static final double HOUR = 60*MINUTE;
	static final double DAY = 24*HOUR;
	static final double MONTH = 30*DAY;
	static final double YEAR = 12*MONTH;
	private Bitmap urlToBmp;


	private PullToRefreshListView listview=null;
	protected ImageLoader imageLoader=null;
	DisplayImageOptions doption=null;
	private ImageLoadingListener animateFirstListener =null;
	Constants constants;
	private ImageView user_pic;
	private TextView user_name;
	private ImageView writeFeed;
	private FragmentManager fm;
	private String userNameSaved;
	private LinearLayout zoomImage;
	private ImageView zoomPicture;
	private RelativeLayout relativeClickable;
	private LinearLayout homeHeader;


	private Matrix matrix = new Matrix();
	private float scale = 1f;
	private ScaleGestureDetector SGD;


	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.home_screen, container, false);





		SharedPreferences pref=getActivity().getSharedPreferences("UserName123", 0);
		userNameSaved = pref.getString("userName123", "lakshmi");



		user_pic=(ImageView)view.findViewById(R.id.home_profile_pic);
		user_name=(TextView)view.findViewById(R.id.home_profile_name);
		writeFeed = (ImageView)view.findViewById(R.id.create_feed);
		zoomImage = (LinearLayout)view.findViewById(R.id.zoom_image);
		zoomPicture = (ImageView)view.findViewById(R.id.zoom_picture);
		relativeClickable = (RelativeLayout)view.findViewById(R.id.relative_clickable);
		homeHeader = (LinearLayout)view.findViewById(R.id.home_header_layout);


		new ProfileDetailsTask().execute(userNameSaved);
		Typeface tfName = Typeface.createFromAsset(getResources().getAssets(),"fonts/helveticaeu_button.otf");
		user_name.setTypeface(tfName,Typeface.BOLD);
		user_name.setText(userNameSaved);

		writeFeed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent= new Intent(getActivity(),CreateFeedActivity.class);
				startActivity(intent);

				getActivity().finish();
				//				navigate(new CreateFeedFragment(), "CreateFeedFragment");
			}
		});

		user_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent= new Intent(getActivity(),ChangeProfileActivity.class);

				startActivity(intent);
			}
		});

	
		listview=(PullToRefreshListView)view.findViewById(R.id.listview);

		doption=new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.placeholder2).showImageOnFail(R.drawable.placeholder2).showStubImage(R.drawable.placeholder2).cacheInMemory(true).cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(0)).build();
		//		MainActivity mACt = new MainActivity();
		animateFirstListener = new MainActivity.AnimateFirstDisplayListener();
		imageLoader = ImageLoader.getInstance();

		String[] names = {"Bindhu" ,"Divya","Rohini","Shylu","Anusha","Vineela","Uday","Rajesh","Meenakshi","Rakesh","Pradeep","Vijay","Vamshi","Karthik"};

		String[] textStatus={"God loves each of us as if there were only one of us.Love of beauty is taste. Love of beauty is taste. Love of beauty is taste. Love of beauty is taste. All you need is love. But a little chocolate now and then doesn't hurt. Love of beauty is taste. ","All you need is love. But a little chocolate now and then doesn't hurt","Love of beauty is taste","He who stops being better stops being good","Don't cry because it's over, smile because it happened","Be yourself; everyone else is already taken","You only live once, but if you do it right, once is enough","So many books, so little time","A room without books is like a body without a soul","Be the change that you wish to see in the world","In three words I can sum up everything I've learned about life: it goes on","No one can make you feel inferior without your consent","If you tell the truth, you don't have to remember anything","A friend is someone who knows all about you and still loves you"};

		String[] imageUrl={"null","http://danielazwan.files.wordpress.com/2013/04/barbie-barbie-31795242-1024-768.jpg","http://investorplace.com/wp-content/uploads/2013/12/barbie-mattel-stock-mat.jpg","http://shop.sarahjaneflowers.com/ekmps/shops/ecpchelp/resources/Design/roses-flowers-31067210-1440-900.jpg","http://images.fanpop.com/images/image_uploads/Flower-Wallpaper-flowers-249398_1693_1413.jpg","http://ibmsmartercommerce.sourceforge.net/wp-content/uploads/2012/09/Roses_Bunch_Of_Flowers.jpeg","http://4.bp.blogspot.com/-3S3-C0km7oE/T_ZLES_qAqI/AAAAAAAAAm0/qlp7Gk0pslY/s640/Cute%2BDolls37.jpg","http://www.tumblr18.com/t18/2013/10/Most-beautiful-doll-face.jpg","http://files.myopera.com/inloveclub/albums/13288792/Friendship_Quotes_lovingyou.png","http://www.apnatalks.com/wp-content/uploads/2012/12/quotes-sad-love.png","http://www.personal.psu.edu/sdh5174/Mario_png.png","https://sequedex.readthedocs.org/en/latest/_images/hmb_proll.png","http://youmoron.org/wp-content/uploads/2012/11/I-hate-you%E2%80%A6.png","http://1.bp.blogspot.com/-7Q4rSOc3Mhw/UH5IWCaNDNI/AAAAAAAABmE/Sj111duAxbk/s1600/sweety.png"};

		String[] time = {"Bindhu" ,"Divya","Rohini","Shylu","Anusha","Vineela","Uday","Rajesh","Meenakshi","Rakesh","Pradeep","Vijay","Vamshi","Karthik"};
		ArrayList<Constants> detailsList=new ArrayList<Constants>();

		for(int i=0;i<textStatus.length;i++)
		{

			String alphabets=textStatus[i];
			String imgurl=imageUrl[i];
			String receivedNames= names[i];
			String timeString = time[i];
			constants = new Constants();
			constants.setTextStatus(alphabets);
			constants.setImageUrlStatus(imgurl);
			constants.setNames(receivedNames);
			constants.setTime(timeString);

			detailsList.add(constants);
		}


		listview.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

				new GetFeedTask().execute();
				new ProfileDetailsTask().execute(userNameSaved);
			}
		});


		if(Util.isOnline(getActivity()))
		{

			new GetFeedTask().execute();
			new ProfileDetailsTask().execute(userNameSaved);
		}
		else
		{
			Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
		}



		Singleton.back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				zoomImage.setVisibility(View.GONE);
				Singleton.back.setVisibility(View.GONE);
			}
		});


		return view;
	}


	private class CustomAdapter extends ArrayAdapter<Constants>
	{

		Context context; 
		ArrayList<Constants> list;



		public CustomAdapter(Context context,ArrayList<Constants> list) {
			super(context, R.layout.item_list_row_feed,list);
			this.context=context;
			this.list=list;
		}


		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			final ViewHolder holder;
			if(convertView == null) 
			{
				holder = new ViewHolder();            
				LayoutInflater vi;
				vi = LayoutInflater.from(getContext());
				convertView = vi.inflate(R.layout.item_list_row_feed, null);

				holder.textStatus = (TextView) convertView.findViewById(R.id.text);
				holder.imageStatus = (ImageView) convertView.findViewById(R.id.image);
				holder.names = (TextView) convertView.findViewById(R.id.name);
				holder.time = (TextView)convertView.findViewById(R.id.time);
				holder.imageHidden = (ImageView)convertView.findViewById(R.id.imagehidden);

				convertView.setTag(holder);
			}
			else
			{     
				holder = (ViewHolder) convertView.getTag();     
			} 
			Constants d=list.get(position);

			holder.textStatus.setText(d.getTextStatus());
			holder.names.setText(d.getNames());

			Double value = Double.parseDouble(d.getTime());

			String createdTime = getTimeString(value);

			holder.time.setText(createdTime);

			//			holder.imageStatus.setVisibility(View.GONE);

			///// un comment when image urls are ready
			
			
			
			final ImageLoader imageLoader = ImageLoader.getInstance();

			imageLoader.init(ImageLoaderConfiguration.createDefault(context));

			if(d.imageUrlStatus.equals("null"))
			{
				holder.imageStatus.setVisibility(View.GONE);
			}
			else{

				holder.imageStatus.setVisibility(View.VISIBLE);

				imageLoader.displayImage(list.get(position).imageUrlStatus, holder.imageStatus, doption, animateFirstListener);
				imageLoader.displayImage(list.get(position).imageUrlStatus, holder.imageHidden, doption, animateFirstListener);
			}

			holder.imageStatus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {


					Bitmap bitmap = ((BitmapDrawable)holder.imageHidden.getDrawable()).getBitmap();

					/*	int loader = R.drawable.placeholder2;

					// ImageLoader class instance
					ImageLoaderProfile imgLoader = new ImageLoaderProfile(getActivity().getApplicationContext());

					// whenever you want to load an image from url
					// call DisplayImage function
					// url - image url to load
					// loader - loader image, will be displayed before getting image
					// image - ImageView 
					imgLoader.DisplayImage(list.get(position).imageUrlStatus, loader, zoomPicture);*/



					zoomPicture.setImageBitmap(bitmap);
					usingSimpleImage(zoomPicture);


					/*new GetBitmap().execute(list.get(position).imageUrlStatus);


					zoomPicture.setImageBitmap(urlToBmp);*/


					if(zoomImage.getVisibility()==View.VISIBLE)
					{
						zoomImage.setVisibility(View.GONE);
						Singleton.back.setVisibility(View.GONE);
					}
					else
					{
						zoomImage.setVisibility(View.VISIBLE);
						Singleton.back.setVisibility(View.VISIBLE);
						//						usingSimpleImage(zoomPicture);
						/*Animation anim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.scale);
						zoomImage.startAnimation(anim);*/

					}


				}
			});

			zoomImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {


					zoomImage.setVisibility(View.GONE);

				}
			});


			holder.names.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if(holder.names.getText().toString().equals(userNameSaved))
					{

						Intent intent= new Intent(getActivity(),ChangeProfileActivity.class);

						startActivity(intent);
					}
					else
					{

						Intent intent= new Intent(getActivity(),FriendProfileActivity.class);

						intent.putExtra("frndName",holder.names.getText().toString());
						startActivity(intent);
					}

				}
			});


			return convertView;

		}

		class ViewHolder
		{
			TextView textStatus;
			ImageView imageStatus;
			TextView names;
			TextView time;
			ImageView imageHidden;
		}

	}

	public void navigate(Fragment frag, String tag)	{
		fm = getFragmentManager();
		if (fm != null)	{

			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.content_layout, frag, tag);
			ft.commit();
		}
	}


	protected class GetFeedTask extends AsyncTask<Void,Void,ArrayList<Constants>>{

		ArrayList<Constants> listArray = new ArrayList<Constants>();


		@Override
		protected ArrayList<Constants> doInBackground(Void... params) {

			WebServices webservices = new WebServices();
			listArray = webservices.getFeed();

			return listArray;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Util.showProgressDialog(getActivity());
		}

		@Override
		protected void onPostExecute(ArrayList<Constants> result) {
			super.onPostExecute(result);
			Util.dismissProgressDialog();

			if(result!=null)
			{
				ArrayList<Constants> list = result;

				Collections.sort(list ,Collections.reverseOrder());

				if(list!=null)
				{

					CustomAdapter adapter=new CustomAdapter(getActivity().getApplicationContext(), list);
					listview.setAdapter(adapter);
				}
			}
			else
			{
				Toast.makeText(getActivity().getApplicationContext(), "Server Down", Toast.LENGTH_SHORT).show();
			}

			listview.postDelayed(new Runnable() {

				@Override
				public void run() {
					listview.onRefreshComplete();
				}
			}, 1000);
		}
	}

	public String getTimeString(double timeOnServer){
		double timeOnClient = System.currentTimeMillis()/1000;
		double diff = timeOnClient-(timeOnServer/1000);
		if (diff < 1 * MINUTE) {     
			return String.format("secs ago");
		} else if(diff < 2 * MINUTE){
			return String.format("a min ago");
		} else if(diff < 59 * MINUTE){
			int minutes = (int)((int)diff/(int)MINUTE);
			return String.format("%d mins ago",minutes);
		} else if(diff < 2 * HOUR){
			return String.format("an hr ago");
		} else if(diff < 23 * HOUR){
			int hours = (int)((int)diff/(int)HOUR);
			return String.format("%d hrs ago",hours);
		} else if(diff < 2 * DAY){
			return String.format("a day ago");
		} else if(diff < 29 * DAY){
			int days = (int)((int)diff/(int)DAY);
			return String.format("%d days ago",days);
		} else if(diff < 2 * MONTH){
			return String.format("a month ago");
		} else if(diff < 29 * MONTH){
			int days = (int)((int)diff/(int)MONTH);
			return String.format("%d months ago",days);
		} else if(diff < 2 * YEAR){
			return String.format("a year ago");
		} else if(diff < 29 * YEAR){
			int days = (int)((int)diff/(int)YEAR);
			return String.format("%d years ago",days);
		}		
		return String.valueOf(diff);
	}

	protected class ProfileDetailsTask extends AsyncTask<String,Void,ProfileDetails>{

		@Override
		protected ProfileDetails doInBackground(String... params) {

			String username = params[0];

			WebServices webservices = new WebServices();
			ProfileDetails profiledetails = webservices.getProfileDetails(username);

			return profiledetails;
		}

		@Override
		protected void onPostExecute(ProfileDetails result) {
			super.onPostExecute(result);
			if(result!=null)
			{

			/*	int loader = R.drawable.ic_stub;
				String image_url = result.getProfilePic();

				// ImageLoader class instance
				ImageLoaderProfile imgLoader = new ImageLoaderProfile(getActivity().getApplicationContext());

				// whenever you want to load an image from url
				// call DisplayImage function
				// url - image url to load
				// loader - loader image, will be displayed before getting image
				// image - ImageView 
				imgLoader.DisplayImage(image_url, loader, user_pic);
*
*/
				
				new GetBitmap().execute(result.getProfilePic());
				
				Singleton.profilepicUrl = result.getProfilePic();
			}
			else
			{
				new GetBitmap().execute(Singleton.profilepicUrl);
				
				//				Toast.makeText(getActivity().getApplicationContext(), "Server Down!!", Toast.LENGTH_LONG).show();
			}
		}
	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	protected class GetBitmap extends AsyncTask<String,Void,Bitmap>{

		ArrayList<Constants> listArray = new ArrayList<Constants>();



		@Override
		protected Bitmap doInBackground(String... params) {

			String src = params[0];

			urlToBmp = getBitmapFromURL(src);


			Singleton.urlToBmp = urlToBmp;
			return urlToBmp;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			user_pic.setImageBitmap(result);

		}
	}

	public void usingSimpleImage(ImageView imageView) {
		ImageAttacher mAttacher = new ImageAttacher(imageView);
		ImageAttacher.MAX_ZOOM = 2.0f; // Double the current Size
		ImageAttacher.MIN_ZOOM = 1.0f; // Half the current Size
		MatrixChangeListener mMaListener = new MatrixChangeListener();
		mAttacher.setOnMatrixChangeListener(mMaListener);
		PhotoTapListener mPhotoTap = new PhotoTapListener();
		mAttacher.setOnPhotoTapListener(mPhotoTap);
	}

	private class PhotoTapListener implements OnPhotoTapListener {

		@Override
		public void onPhotoTap(View view, float x, float y) {
		}
	}

	private class MatrixChangeListener implements OnMatrixChangedListener {

		@Override
		public void onMatrixChanged(RectF rect) {

		}
	}

}
