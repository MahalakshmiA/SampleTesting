package com.sample;

import java.util.List;

import com.google.gson.annotations.SerializedName;
//groups
public class Timezone {/*
	
	//Details timeZoneList;

	*//**
	 * @return the timeZoneList
	 *//*
	public Details getTimeZoneList() {
		return timeZoneList;
	}

	*//**
	 * @param timeZoneList the timeZoneList to set
	 *//*
	public void setTimeZoneList(Details timeZoneList) {
		this.timeZoneList = timeZoneList;
	}

	

*/
	

	
	/**
	 * @return the open
	 */
	public String getOpen() {
		return open;
	}
	/**
	 * @param open the open to set
	 */
	public void setOpen(String open) {
		this.open = open;
	}
	/**
	 * @return the high
	 */
	public String getHigh() {
		return high;
	}
	/**
	 * @param high the high to set
	 */
	public void setHigh(String high) {
		this.high = high;
	}
	/**
	 * @return the low
	 */
	public String getLow() {
		return low;
	}
	/**
	 * @param low the low to set
	 */
	public void setLow(String low) {
		this.low = low;
	}
	/**
	 * @return the close
	 */
	public String getClose() {
		return close;
	}
	/**
	 * @param close the close to set
	 */
	public void setClose(String close) {
		this.close = close;
	}
	/**
	 * @return the volume
	 */
	public String getVolume() {
		return volume;
	}
	/**
	 * @param volume the volume to set
	 */
	public void setVolume(String volume) {
		this.volume = volume;
	}
	@SerializedName("1. open")
	public  String open;
	@SerializedName("2. high")
	public String high;
	@SerializedName("3. low")
	public String low;
	@SerializedName("4. close")
	public String close;
	@SerializedName("5. volume")
	public String volume;

}
