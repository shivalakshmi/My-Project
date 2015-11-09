package com.example.testingapp;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class EventsFragment extends Fragment {
	private Button ButtonLogin;
	private ImageView fbImage;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.events_screen, container, false);


		fbImage=(ImageView) view.findViewById(R.id.login_with_facebook);


		fbImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Util.isOnline(Singleton.activity))
				{

					ShareOnFacebook sof = new ShareOnFacebook(getActivity(),Singleton.activity);
					sof.postOnFacebook("face book test");

				}
				else
				{
					Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
				}

			}
		});






		return view;
	}
}
