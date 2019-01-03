package com.sample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class OutputObj {
	@SerializedName("Meta Data")
	Metadata metaData;
	
	Map<String, Timezone> timezoneList = new HashMap<String, Timezone>();

	/**
	 * @return the metaData
	 */
	public Metadata getMetaData() {
		return metaData;
	}

	/**
	 * @param metaData the metaData to set
	 */
	public void setMetaData(Metadata metaData) {
		this.metaData = metaData;
	}

	/**
	 * @return the timezoneList
	 */
	public Map<String, Timezone> getTimezoneList() {
		return timezoneList;
	}

	/**
	 * @param timezoneList the timezoneList to set
	 */
	public void setTimezoneList(Map<String, Timezone> timezoneList) {
		this.timezoneList = timezoneList;
	}
	
	

}
