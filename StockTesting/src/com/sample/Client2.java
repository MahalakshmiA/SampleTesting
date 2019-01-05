package com.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Client2 {

	// class constants for various file names
	private static final String STOCK_SYMBOL_FILE = "company_names_and_symbols.txt";
	private static final String DATA_SYMBOL_FILE = "data_fields.txt";
	private static final String SAMPLE_FILE = "sample_stock_data.txt";

	private static TreeMap<String, String> stockSymbolsAndNames;
	/**
	 * Helper object to interpret the json.
	 * 
	 * @see Gson
	 */
	protected static Gson GSON = new Gson();

	public static void main(String[] args) throws Exception {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		try {
			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			URL url = new URL(
					"https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=AMZN&apikey=J27JKP9HNK701478");
		//"https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=AMZN&interval=5min&apikey=J27JKP9HNK701478");
			/*
			 * URL url = new URL(
			 * "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=AMZN&interval=1min&apikey=J27JKP9HNK701478"
			 * );
			 */
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			StringBuilder sb = new StringBuilder();

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				sb.append(output);
			}
			System.out.println(sb.toString());
			JsonParser parser = new JsonParser();
			JsonElement jsonElement = parser.parse(sb.toString());
			JsonObject rootObject = jsonElement.getAsJsonObject();

			Gson gson = new Gson();
			Gson gson2 = new GsonBuilder().registerTypeAdapter(OutputObj.class,
					new MyDeserializer()).create();

			OutputObj obj = gson2.fromJson(sb.toString(), OutputObj.class);
			
			System.out.println("List of Timezoe List" + obj.getTimezoneList().size());


			//Sort the objects 
			TreeMap<String, Timezone> sortedtimezoneList = new TreeMap<String, Timezone>();

			// Copy all data from hashMap into TreeMap
			sortedtimezoneList.putAll(obj.getTimezoneList());
			
			
			//Sort the objects 
			TreeMap<String, Details> levelList = new TreeMap<String, Details>();
			
			for (Entry<String, Timezone> entry : sortedtimezoneList
					.entrySet()) {
				
				Details details = new Details();
				
				double close = Double.parseDouble(entry.getValue().getClose());
				double open = Double.parseDouble(entry.getValue().getOpen());
				double high = Double.parseDouble(entry.getValue().getHigh());
				double low  = Double.parseDouble(entry.getValue().getLow());
				
						details.setOpen(String.valueOf(open));
				details.setClose(String.valueOf(close));
				details.setHigh(String.valueOf(high));
				details.setLow(String.valueOf(low));
				
				
				
				if(close > open){
					details.setCandleColour("Green");
				}else{
					details.setCandleColour("Red");
				}
				
				
				double candleHeight = Math.abs(high - low);
				double candleBody = Math.abs(close - open);
				
				details.setCandleHeight(candleHeight);
				
				details.setCandleBody(candleBody);
				
				if(candleHeight/candleBody > 2){
					details.setCandleType("Excited");
				}else{
					details.setCandleType("Boring");
				}
				
				
				
				
				
				// Copy all data from hashMap into TreeMap
				levelList.put(entry.getKey(), details);
				
			
			}
			 boolean isfirst = true;
			 boolean isSecond = true;
			 TreeMap<String, Details> MarkablelevelList = new TreeMap<String, Details>();
			List<String> keyList = new ArrayList<String>(levelList.keySet());
			for(int i = 0; i < keyList.size(); i++) {
			    String currentkey = keyList.get(i);
			    Details currentDetails = levelList.get(currentkey);
			    
		    	if(isfirst){
		    		
		    		isfirst = false;
		    	}else if(isSecond){
		    	
		    		isSecond = false;
		    		}
		    	else{
		    		String previousKey = keyList.get(i-1);
		    		String nextKey = keyList.get(i+1);
		    		
		    		 Details previousDetails = levelList.get(previousKey);
		    		
		    		 Details nextDetails = levelList.get(nextKey);
		    		 
		    		 if(previousDetails.getCandleType().equalsIgnoreCase("Excited") && nextDetails.getCandleType().equalsIgnoreCase("Excited")
		    				 && currentDetails.getCandleType().equalsIgnoreCase("Boring")){
		    			 currentDetails.setMarkableLevel(true);
		    			 String previousColour = previousDetails.getCandleColour();
		    			 String nextColour = nextDetails.getCandleColour();
		    			 if(previousColour.equalsIgnoreCase("Red") && nextColour.equalsIgnoreCase("Red")){
		    				 currentDetails.setLevelType("DBD");
		    			 }else if(previousColour.equalsIgnoreCase("Green") && nextColour.equalsIgnoreCase("Green")){
		    				 currentDetails.setLevelType("RBR");
		    			 }else if(previousColour.equalsIgnoreCase("Red") && nextColour.equalsIgnoreCase("Green")){
		    				 currentDetails.setLevelType("DBR");
		    			 }else if(previousColour.equalsIgnoreCase("Green") && nextColour.equalsIgnoreCase("Red")){
		    				 currentDetails.setLevelType("RBD");
		    			 }else{
		    				 currentDetails.setLevelType("YTD");
		    			 }
		    			 
		    			 i++; 
		    			 isfirst = true;
		    			 isSecond = true;
		    		 }
		    		 
		    	}
		    	MarkablelevelList.put(currentkey, currentDetails);
		    	
		    }
			
			for (Entry<String, Details> entry : MarkablelevelList.entrySet()) {
				System.out.println("Start date "
						+ entry.getKey()
						+ ", High = "
						+ entry.getValue().getHigh()
						+ ","
						+ "Low = "
						+ entry.getValue().getLow()
						+ ", Open = "
						+ entry.getValue().getOpen()
						+ ","
						+ ", Close = "
						+ entry.getValue().getClose()
						+ ","
						+ ", Candle Color = "
						+ entry.getValue().getCandleColour()
						+ ", Candle type = "
						+ entry.getValue().getCandleType()
						+ ", Candle Height = "
						+ entry.getValue().getCandleHeight()
						+ ", Candle Body = "
						+ entry.getValue().getCandleBody()
						+ ", Candle Markable List = "
						+ entry.getValue().isMarkableLevel()
						+ ", Candle Level Type = "
						+ entry.getValue().getLevelType()
						+ ", facotr"
						+ ", facotr"
						+ (entry.getValue().getCandleHeight() / entry
								.getValue().getCandleBody()));
			}
			
			System.out.println("Final Consolidated List--> Starts");
			for (Entry<String, Details> entry : MarkablelevelList.entrySet()) {
				
				if(entry.getValue().isMarkableLevel()){
				System.out.println("Start date "
						+ entry.getKey()
						+ ", High = "
						+ entry.getValue().getHigh()
						+ ","
						+ "Low = "
						+ entry.getValue().getLow()
						+ ", Open = "
						+ entry.getValue().getOpen()
						+ ","
						+ ", Close = "
						+ entry.getValue().getClose()
						+ ","
						+ ", Candle Color = "
						+ entry.getValue().getCandleColour()
						+ ", Candle type = "
						+ entry.getValue().getCandleType()
						+ ", Candle Height = "
						+ entry.getValue().getCandleHeight()
						+ ", Candle Body = "
						+ entry.getValue().getCandleBody()
						+ ", Candle Markable List = "
						+ entry.getValue().isMarkableLevel()
						+ ", Candle Level Type = "
						+ entry.getValue().getLevelType()
						+ ", facotr"
						+ ", facotr"
						+ (entry.getValue().getCandleHeight() / entry
								.getValue().getCandleBody()));
				}
			}
			
			System.out.println("Final Consolidated List--> Ends");
			for (Entry<String, Details> entry : levelList.entrySet()) {
				System.out.println("Start date "
						+ entry.getKey()
						+ ", High = "
						+ entry.getValue().getHigh()
						+ ","
						+ "Low = "
						+ entry.getValue().getLow()
						+ ", Open = "
						+ entry.getValue().getOpen()
						+ ","
						+ ", Close = "
						+ entry.getValue().getClose()
						+ ","
						+ ", Candle Color = "
						+ entry.getValue().getCandleColour()
						+ ", Candle type = "
						+ entry.getValue().getCandleType()
						+ ", Candle Height = "
						+ entry.getValue().getCandleHeight()
						+ ", Candle Body = "
						+ entry.getValue().getCandleBody()
						+ ", facotr"
						+ (entry.getValue().getCandleHeight() / entry
								.getValue().getCandleBody()));
			}

		
			
			
			

		//	addMinutes(sortedtimezoneList);

			/*
			 * String baseURL = "http://finance.yahoo.com/d/quotes.csv?s=";
			 * 
			 * // add the stocks we want data for baseURL = addSymbols(baseURL,
			 * stockSymbolsAndNames.keySet(), "+");
			 * 
			 * // after the stock symbols are added the symbols // for the
			 * requested data fields are added baseURL += "&f="; baseURL =
			 * addSymbols(baseURL, readSymbols(DATA_SYMBOL_FILE), "");
			 */

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	/**
	 * @param sortedtimezoneList
	 * @throws ParseException
	 */
	private static void addMinutes(TreeMap<String, Timezone> sortedtimezoneList)
			throws ParseException {
		int minutesToAdd = 25;
		TreeMap<String, Details> consolidatedHighLowList = getConsolidatedHighLowList(
				sortedtimezoneList, minutesToAdd);

		System.out.println("Final Consolidated Size"
				+ consolidatedHighLowList.size());

		for (Entry<String, Details> entry : consolidatedHighLowList
				.entrySet()) {
			System.out.println("Start date " + entry.getKey() + ", High = "
					+ entry.getValue().getHigh() + "," + "Low = "
					+ entry.getValue().getLow() + ", Open = "
					+ entry.getValue().getOpen() + "," + ", Close = "
					+ entry.getValue().getClose() + "," + ", end Date = "
					+ entry.getValue().getEndDate());
		}
	}

	/**
	 * @param sortedtimezoneList
	 * @param minutesToAdd
	 * @return
	 * @throws ParseException
	 */
	private static TreeMap<String, Details> getConsolidatedHighLowList(
			TreeMap<String, Timezone> sortedtimezoneList, int minutesToAdd)
			throws ParseException {

		
		TreeMap<String, String> keys = getStarEndDateKeys(sortedtimezoneList, minutesToAdd);

		TreeMap<String, Details> consolidatedHighLowList = getHighLowList(
				sortedtimezoneList, keys);
		return consolidatedHighLowList;
	}

	/**
	 * @param sortedtimezoneList
	 * @param keys
	 * @return
	 */
	private static TreeMap<String, Details> getHighLowList(
			TreeMap<String, Timezone> sortedtimezoneList,
			TreeMap<String, String> keys) {
		TreeMap<String, Details> consolidatedHighLowList = new TreeMap<String, Details>();
		for (Map.Entry<String, String> entry : keys.entrySet()) {
			Details details = new Details();
			
			details.setStartDate(entry.getKey());
			details.setEndDate(entry.getValue());

			for (Map.Entry<String, Timezone> entry2 : sortedtimezoneList
					.entrySet()) {
				if (entry.getKey().equalsIgnoreCase(entry2.getKey())) {
					Timezone zone2 = entry2.getValue();
					details.setOpen(zone2.getOpen());
					break;

				}
			}
			for (Map.Entry<String, Timezone> entry2 : sortedtimezoneList
					.entrySet()) {
				if (entry.getValue().equalsIgnoreCase(entry2.getKey())) {
					Timezone zone2 = entry2.getValue();
					details.setClose(zone2.getClose());
					break;

				}
			}

			String highValue = "";
			String lowValue = "";

			Map<String, ArrayList<String>> highLow = gethigh(
					entry.getKey(), entry.getValue(), sortedtimezoneList);
			ArrayList<String> highList = highLow.get("High");
			

			ArrayList<String> lowList = highLow.get("Low");
			if(null != highList && !highList.isEmpty()){

			Collections.sort(highList, Collections.reverseOrder());
			details.setHigh(highList.get(0));
			}
			if(null != lowList && !lowList.isEmpty()){

			Collections.sort(lowList);
			details.setLow(lowList.get(0));
			}

			consolidatedHighLowList.put(entry.getKey(), details);
		}
		return consolidatedHighLowList;
	}

	/**
	 * @param sortedtimezoneList
	 * @return
	 * @throws ParseException
	 */
	private static TreeMap<String, String> getStarEndDateKeys(
			TreeMap<String, Timezone> sortedtimezoneList, int minutes) throws ParseException {
		TreeMap<String, String> keys = new TreeMap<String, String>();

		// using iterators
		Iterator<Entry<String, Timezone>> itr = sortedtimezoneList
				.entrySet().iterator();
		boolean flag = true;
		String enddate = "";
		while (itr.hasNext()) {
			Entry<String, Timezone> entry = itr.next();
			String startdate = entry.getKey();

			startdate = entry.getKey();
			if (flag == true) {
				enddate = getDateAfterAdding(entry.getKey(), minutes);
				System.out.println(enddate);
				keys.put(startdate, enddate);
				flag = false;
			} else if ((getDate(startdate).after(getDate(enddate)))) {
				enddate = getDateAfterAdding(entry.getKey(), minutes);
				System.out.println(enddate);
				keys.put(startdate, enddate);
				flag = false;
			}

		}

		for (Entry<String, String> entry : keys.entrySet())
			System.out.println("Start date " + entry.getKey() + ", End date = "
					+ entry.getValue());

		System.out.println("Start date and end date keys Size" + keys.size());
		return keys;
	}

	private static Map<String, ArrayList<String>> gethigh(String startDate,
			String endDate, TreeMap<String, Timezone> sortedtimezoneList) {

		Map<String, ArrayList<String>> highLow = new HashMap<String, ArrayList<String>>();

		ArrayList<String> highList = new ArrayList<>();
		ArrayList<String> lowList = new ArrayList<>();

		for (Map.Entry<String, Timezone> entry2 : sortedtimezoneList.entrySet()) {
			try {
				if ((getDate(entry2.getKey()).after(getDate(startDate)) && (getDate(entry2
						.getKey()).before(getDate(endDate))))) {
					Timezone zone2 = entry2.getValue();
					highList.add(zone2.getHigh());
					lowList.add(zone2.getLow());

				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		highLow.put("High", highList);
		highLow.put("Low", lowList);

		return highLow;
	}

	private static String getDateAfterAdding(String dateString, int minutes)
			throws ParseException {

		DateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date tempdate = inFormat.parse(dateString);
		final long ONE_MINUTE_IN_MILLIS = 60000;// millisecs

		Calendar cal = Calendar.getInstance();
		cal.setTime(tempdate);
		long t = cal.getTimeInMillis();
		Date afteradding = new Date(t + (minutes * ONE_MINUTE_IN_MILLIS));
		DateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String outdateString = outFormat.format(afteradding);
		return outdateString;
	}

	// read in the data symbols from a file
	private static ArrayList<String> readSymbols(String fileName) {
		ArrayList<String> result = new ArrayList<String>();
		try {
			Scanner sc = new Scanner(new File(fileName));
			while (sc.hasNextLine()) {
				result.add(sc.nextLine().trim());
			}
			;
			sc.close();
		} catch (IOException e) {
			System.out.println("ERROR reading from file: " + e);
			System.out.println("returning emptying string for url");
		}
		return result;
	}

	private static String addSymbols(String baseURL, Collection<String> values,
			String seperator) {
		StringBuilder result = new StringBuilder(baseURL);
		for (String symbol : values) {
			result.append(symbol);
			result.append(seperator);
		}
		// remove last seperator, unless empty string
		if (seperator.length() > 0) {
			result.delete(result.length() - seperator.length(), result.length());
		}
		return result.toString();
	}

	private static Date getDate(String dateString) throws ParseException {

		DateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date tempdate = inFormat.parse(dateString);
		return tempdate;
	}

}