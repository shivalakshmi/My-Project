package com.example.testingapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FacebookNotificationsActivity extends Activity {

	private ListView listview;
	String uid;
	String name;
	String pic;
	String city;
	TextView tvname;
	TextView dob;
	TextView tvlocation;
	ImageView Imgpic;
	ArrayList<Friend> data;
	Bitmap[] bitmap;
	ImageView cake;

	private TextView upcomingBirthday;
	private Button wish;

	private ArrayList<Friend> friendsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facebook_second_layout);

		listview = (ListView) findViewById(R.id.listview);

		friendsList = Singleton.frendsList;

		if (friendsList.size() > 0) {
			Collections.sort(friendsList);

			FriendsAdapter adapter = new FriendsAdapter(
					getApplicationContext(), friendsList);

			listview.setAdapter(adapter);
		} else {

			Intent intent = new Intent(FacebookNotificationsActivity.this,
					NoEventFragment.class);
			// navigate(new NoEventFragment(), "NoEventFragment");
			startActivity(intent);

			finish();
		}
	}

	protected class FriendsAdapter extends ArrayAdapter<Friend> {

		// ArrayList<Friend> friendslist;
		Context context;
		ArrayList<Friend> data;

		boolean flag = false;
		boolean mondayFlag =false;

		int upcoming = 0;

		public FriendsAdapter(Context context, ArrayList<Friend> data) {
			super(context, R.layout.extra, data);
			// TODO Auto-generated constructor stub
			this.context = context;
			this.data = data;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View v = convertView;
			int todayCount = 0;
			int mondayCount = 0;
			int tueCount = 0;
			int wedCount = 0;
			int thursCount = 0;
			int friCount = 0;
			int satCount = 0;
			int sunCount = 0;

			if (v == null) {

				LayoutInflater vi;
				vi = LayoutInflater.from(context);
				v = vi.inflate(R.layout.extra, null);

			}

			Friend p = data.get(position);

			if (p != null) {

				tvname = (TextView) v.findViewById(R.id.name);
				tvlocation = (TextView) v.findViewById(R.id.location);
				Imgpic = (ImageView) v.findViewById(R.id.imageView1);
				dob = (TextView) v.findViewById(R.id.dob);
				cake = (ImageView) v.findViewById(R.id.cake);
				upcomingBirthday = (TextView) v.findViewById(R.id.upcoming);
				wish = (Button) v.findViewById(R.id.wish);

				// / converting string to date

				String dateString = p.getDob();
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");

				Date convertedDate = new Date();
				try {
					try {
						convertedDate = dateFormat.parse(dateString);
					} catch (java.text.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// // finding day of the week

				Calendar calendar = Calendar.getInstance();

				calendar.setTime(convertedDate);

				int weekday = calendar.get(Calendar.DAY_OF_WEEK);

				Calendar c = Calendar.getInstance();
				SimpleDateFormat simpledate = new SimpleDateFormat("MM/dd");
				String systemDate = simpledate.format(c.getTime());

				/*
				 * Calendar c2 = Calendar.getInstance(); SimpleDateFormat
				 * simpledate2 = new SimpleDateFormat("MMMMMMMMM dd"); String
				 * systemDate2 = simpledate2.format(c.getTime());
				 */

				Log.i("******************", "****************************"
						+ systemDate);

				String systemDateSubString = systemDate.substring(3);

				String dobSubstring = dateString.substring(3, 5);

				if (systemDateSubString.equals(dobSubstring)) {

					// wish.setVisibility(View.VISIBLE);
					upcomingBirthday.setVisibility(View.GONE);
					// cake.setVisibility(View.VISIBLE);
					// LinearLayout colorLayout= (LinearLayout)
					// v.findViewById(R.id.colorlayout);

					// colorLayout.setBackgroundColor(getResources().getColor(R.color.gray));

					todayCount = todayCount + 1;

					if (todayCount == 1) {
						if (flag == false)

						{
							tvlocation.setVisibility(View.VISIBLE);
							flag = true;

						}
						// tvlocation.setText("Saturday");
						tvlocation.setText("Today, " + systemDate);
						tvlocation.setTypeface(null, Typeface.BOLD);
						tvlocation.setBackgroundColor(getResources().getColor(
								R.color.gray));
					}

					if (tvname != null) {

						tvname.setText(p.getName());
						tvname.setTypeface(null, Typeface.BOLD);
					}

					if (Imgpic != null) {

						Imgpic.setImageBitmap(p.getBitmap());

					}
					if (dob != null) {
						dob.setText(p.getDob());
						dob.setTypeface(null, Typeface.BOLD);
					}

				} else {
					tvlocation.setVisibility(View.GONE);
					tvlocation.setTypeface(null, Typeface.NORMAL);

					tvlocation.setBackgroundColor(getResources().getColor(
							R.color.white));
					tvname.setTypeface(null, Typeface.NORMAL);
					dob.setTypeface(null, Typeface.NORMAL);

					upcoming = upcoming + 1;

					if (upcoming == 1) {
						upcomingBirthday.setVisibility(View.VISIBLE);
					}
					/*
					 * else { upcomingBirthday.setVisibility(View.GONE);
					 * upcoming=0; }
					 */

					if (weekday == 1) {
						satCount = satCount + 1;

						if (satCount == 1) {
							tvlocation.setVisibility(View.VISIBLE);
							tvlocation.setText("Saturday");
						}
						/*
						 * else { tvlocation.setVisibility(View.GONE); }
						 */

					} else if (weekday == 2) {
						sunCount = sunCount + 1;

						if (sunCount == 1) {
							tvlocation.setVisibility(View.VISIBLE);
							tvlocation.setText("Sunday");
						}
						/*
						 * else { tvlocation.setVisibility(View.GONE); }
						 */
					} else if (weekday == 3) {
						mondayCount = mondayCount + 1;

						if (mondayCount == 1) {
							tvlocation.setVisibility(View.VISIBLE);
							tvlocation.setText("Monday");
						}
						/*
						 * else { tvlocation.setVisibility(View.GONE); }
						 */

					} else if (weekday == 4) {
						tueCount = tueCount + 1;

						if (tueCount == 1) {
							tvlocation.setVisibility(View.VISIBLE);
							tvlocation.setText("Tuesday");
						}
						/*
						 * else { tvlocation.setVisibility(View.GONE); }
						 */
					} else if (weekday == 5) {
						wedCount = wedCount + 1;

						if (wedCount == 1) {
							tvlocation.setVisibility(View.VISIBLE);
							tvlocation.setText("Wednesday");
						}
						/*
						 * else { tvlocation.setVisibility(View.GONE); }
						 */
					} else if (weekday == 6) {
						thursCount = thursCount + 1;

						if (thursCount == 1) {
							tvlocation.setVisibility(View.VISIBLE);
							tvlocation.setText("Thursday");
						}
						/*
						 * else { tvlocation.setVisibility(View.GONE); }
						 */
					} else if (weekday == 7) {
						friCount = friCount + 1;

						if (friCount == 1) {
							tvlocation.setVisibility(View.VISIBLE);
							tvlocation.setText("Friday");
						}
						/*
						 * else { tvlocation.setVisibility(View.GONE); }
						 */
					}

					if (tvname != null) {

						tvname.setText(p.getName());
					}

					if (Imgpic != null) {

						Imgpic.setImageBitmap(p.getBitmap());

					}
					if (dob != null) {
						dob.setText(p.getDob());
					}

				}

			}

			return v;
		}
	}

}
