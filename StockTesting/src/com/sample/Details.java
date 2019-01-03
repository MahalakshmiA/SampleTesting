package com.sample;

import com.google.gson.annotations.SerializedName;

public class Details {
	
	@SerializedName("1. open")
	public  String open;
	@SerializedName("2. high")
	public String high;
	@SerializedName("3. low")
	public String low;
	@SerializedName("4. low")
	public String close;
	@SerializedName("5. volume")
	public String volume;

}
