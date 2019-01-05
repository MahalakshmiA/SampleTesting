package com.sample;

import com.google.gson.annotations.SerializedName;

public class Details {
	
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
	
	public String startDate;
	
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String endDate;
	
	public String candleColour;
	
	/**
	 * @return the candleColour
	 */
	public String getCandleColour() {
		return candleColour;
	}
	/**
	 * @param candleColour the candleColour to set
	 */
	public void setCandleColour(String candleColour) {
		this.candleColour = candleColour;
	}
	/**
	 * @return the candleType
	 */
	public String getCandleType() {
		return candleType;
	}
	/**
	 * @param candleType the candleType to set
	 */
	public void setCandleType(String candleType) {
		this.candleType = candleType;
	}
	/**
	 * @return the candleHeight
	 */
	public double getCandleHeight() {
		return candleHeight;
	}
	/**
	 * @param candleHeight the candleHeight to set
	 */
	public void setCandleHeight(double candleHeight) {
		this.candleHeight = candleHeight;
	}
	/**
	 * @return the candleBody
	 */
	public double getCandleBody() {
		return candleBody;
	}
	/**
	 * @param candleBody the candleBody to set
	 */
	public void setCandleBody(double candleBody) {
		this.candleBody = candleBody;
	}
	public String candleType;
	
	public double candleHeight;
	
	public double candleBody;
	
	/**
	 * @return the markableLevel
	 */
	public boolean isMarkableLevel() {
		return markableLevel;
	}
	/**
	 * @param markableLevel the markableLevel to set
	 */
	public void setMarkableLevel(boolean markableLevel) {
		this.markableLevel = markableLevel;
	}
	public boolean markableLevel;
	
	public String levelType;

	/**
	 * @return the levelType
	 */
	public String getLevelType() {
		return levelType;
	}
	/**
	 * @param levelType the levelType to set
	 */
	public void setLevelType(String levelType) {
		this.levelType = levelType;
	}

}
