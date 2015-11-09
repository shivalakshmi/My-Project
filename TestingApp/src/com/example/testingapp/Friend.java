package com.example.testingapp;


import android.graphics.Bitmap;

public class Friend implements Comparable<Friend>{
	String uid;
	String name;
	String pic;
	String city;
	private Bitmap bitmap;
	int diffDates; 

	public int getDiffDates() {
		return diffDates;
	}
	public void setDiffDates(int diffDates) {
		this.diffDates = diffDates;	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	/**
	 * 
	 */
	String dob;


	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String location) {
		this.city = location;
	}
	@Override
	public int compareTo(Friend frnd) {
		return getDob().compareTo(frnd.getDob());
	}
}

