package com.sample;

import com.google.gson.annotations.SerializedName;

public class Metadata {
	
	@SerializedName("1. Information")
	public  String info;
	
	@SerializedName("2. Symbol")
	public  String symbol ;
	@SerializedName("3. Last Refreshed")
	public  String refresh ;
	@SerializedName("4. Interval")
	public  String interval  ;
	@SerializedName("5. Output Size")
	public  String output;
	@SerializedName("6. Time Zone")
	public  String timeZone;
	

}
