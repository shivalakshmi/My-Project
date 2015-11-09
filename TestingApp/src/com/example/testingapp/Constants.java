package com.example.testingapp;

public class Constants implements Comparable<Constants>{

	String textStatus;
	String imageUrlStatus;
	String names;
	String time;
	

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getTextStatus() {
		return textStatus;
	}

	public void setTextStatus(String textStatus) {
		this.textStatus = textStatus;
	}

	public String getImageUrlStatus() {
		return imageUrlStatus;
	}

	public void setImageUrlStatus(String imageUrlStatus) {
		this.imageUrlStatus = imageUrlStatus;
	}

	public static String[] getImages() {
		return IMAGES;
	}

	public static final String[] IMAGES = new String[] {
		// Heavy images
		"http://danielazwan.files.wordpress.com/2013/04/barbie-barbie-31795242-1024-768.jpg",
		"http://investorplace.com/wp-content/uploads/2013/12/barbie-mattel-stock-mat.jpg",
		"http://shop.sarahjaneflowers.com/ekmps/shops/ecpchelp/resources/Design/roses-flowers-31067210-1440-900.jpg",
		"http://images.fanpop.com/images/image_uploads/Flower-Wallpaper-flowers-249398_1693_1413.jpg",
		"http://ibmsmartercommerce.sourceforge.net/wp-content/uploads/2012/09/Roses_Bunch_Of_Flowers.jpeg",
		"http://4.bp.blogspot.com/-3S3-C0km7oE/T_ZLES_qAqI/AAAAAAAAAm0/qlp7Gk0pslY/s640/Cute%2BDolls37.jpg",
		"http://www.tumblr18.com/t18/2013/10/Most-beautiful-doll-face.jpg",
		"http://files.myopera.com/inloveclub/albums/13288792/Friendship_Quotes_lovingyou.png",
		"http://www.apnatalks.com/wp-content/uploads/2012/12/quotes-sad-love.png",
		"http://www.personal.psu.edu/sdh5174/Mario_png.png","https://sequedex.readthedocs.org/en/latest/_images/hmb_proll.png",
		"http://youmoron.org/wp-content/uploads/2012/11/I-hate-you%E2%80%A6.png",
		"http://1.bp.blogspot.com/-7Q4rSOc3Mhw/UH5IWCaNDNI/AAAAAAAABmE/Sj111duAxbk/s1600/sweety.png",
		// Light images
		"http://tabletpcssource.com/wp-content/uploads/2011/05/android-logo.png",
		"http://simpozia.com/pages/images/stories/windows-icon.png",
		"https://si0.twimg.com/profile_images/1135218951/gmail_profile_icon3_normal.png",
		"http://www.krify.net/wp-content/uploads/2011/09/Macromedia_Flash_dock_icon.png",
		"http://radiotray.sourceforge.net/radio.png",
		"http://www.bandwidthblog.com/wp-content/uploads/2011/11/twitter-logo.png",
		"http://weloveicons.s3.amazonaws.com/icons/100907_itunes1.png",
		"http://weloveicons.s3.amazonaws.com/icons/100929_applications.png",
		"http://www.idyllicmusic.com/index_files/get_apple-iphone.png",
		"http://www.frenchrevolutionfood.com/wp-content/uploads/2009/04/Twitter-Bird.png",
		"http://3.bp.blogspot.com/-ka5MiRGJ_S4/TdD9OoF6bmI/AAAAAAAAE8k/7ydKtptUtSg/s1600/Google_Sky%2BMaps_Android.png",
		"http://www.desiredsoft.com/images/icon_webhosting.png",
		"http://goodereader.com/apps/wp-content/uploads/downloads/thumbnails/2012/01/hi-256-0-99dda8c730196ab93c67f0659d5b8489abdeb977.png",

		// Special cases

	};


	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}


	@Override
	public int compareTo(Constants arg0) {
		// TODO Auto-generated method stub

		return getTime().compareTo(arg0.getTime());

	}

}
