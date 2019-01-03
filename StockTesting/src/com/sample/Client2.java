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
import java.util.Date;
import java.util.Iterator;
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
					"https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=AMZN&interval=5min&apikey=J27JKP9HNK701478");
			/*URL url = new URL(
					"https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=AMZN&interval=1min&apikey=J27JKP9HNK701478");*/
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
			Gson gson2 = new GsonBuilder()
            .registerTypeAdapter(OutputObj.class, new MyDeserializer()).create();

			OutputObj obj = gson2.fromJson(sb.toString(), OutputObj.class);  
			
			System.out.println(obj.timezoneList.size());
			
			TreeMap<String, Timezone> sortedtimezoneList = new TreeMap<String, Timezone>();
			
	  
	        // Copy all data from hashMap into TreeMap 
			sortedtimezoneList.putAll(obj.getTimezoneList()); 
			System.out.println(""+sortedtimezoneList.size());
			
			 for (Map.Entry<String, Timezone> entry : sortedtimezoneList.entrySet())  
		            System.out.println("Key = " + entry.getKey() +  
		                         ", Value = " + entry.getValue());   
			 
			  String minutes = "15";
			  
			  TreeMap<String, Timezone> tempMap1 = new TreeMap<String, Timezone>();
			  
			  TreeMap<String,String> keys = new TreeMap<String,String>();
			  
			  // using iterators 
		        Iterator<Entry<String, Timezone>> itr = sortedtimezoneList.entrySet().iterator(); 
		          boolean flag = true;
		          String enddate ="";
		        while(itr.hasNext()) 
		        { 
		             Entry<String, Timezone> entry = itr.next(); 
		            
		             System.out.println("Key = " + entry.getKey() +  
		                                 ", Value = " + entry.getValue());
		             String startdate =entry.getKey();
		             
		              startdate = entry.getKey();
		             if(flag == true){
		            	  enddate = getDateAfterAdding(entry.getKey(), 15);
			             System.out.println(enddate);
			             keys.put(startdate, enddate);
			             flag = false;
		             }else if((getDate(startdate).after(getDate(enddate)))){
		            	 enddate = getDateAfterAdding(entry.getKey(), 15);
			             System.out.println(enddate);
			             keys.put(startdate, enddate);
			             flag = false;
		             }
		            
		        }
		        

				 for (Entry<String, String> entry : keys.entrySet())  
			            System.out.println("Key = " + entry.getKey() +  
			                         ", Value = " + entry.getValue());  
			        
				 System.out.println("keys Size"+keys.size());
			  

			  
			  

/*String baseURL = "http://finance.yahoo.com/d/quotes.csv?s=";

// add the stocks we want data for
baseURL = addSymbols(baseURL, stockSymbolsAndNames.keySet(), "+");

// after the stock symbols are added the symbols 
// for the requested data fields are added
baseURL += "&f=";
baseURL = addSymbols(baseURL, readSymbols(DATA_SYMBOL_FILE), "");*/
			
			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	private static String getDateAfterAdding(String dateString, int minutes) throws ParseException {
		 
		  DateFormat inFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");

		  Date tempdate = inFormat.parse(dateString);
		  final long ONE_MINUTE_IN_MILLIS=60000;//millisecs

		  Calendar cal = Calendar.getInstance();
		  cal.setTime(tempdate);
		  long t= cal.getTimeInMillis();
		  Date afteradding=new Date(t + (minutes * ONE_MINUTE_IN_MILLIS));
		  DateFormat outFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
		    String outdateString = outFormat.format(afteradding);
		    return outdateString;
	}
	
	 // read in the data symbols from a file
    private static ArrayList<String> readSymbols(String fileName) {
        ArrayList<String> result = new ArrayList<String>();
        try {
            Scanner sc = new Scanner(new File(fileName));
            while(sc.hasNextLine()) {
                result.add(sc.nextLine().trim());
            };
            sc.close();
        }
        catch(IOException e) {
            System.out.println("ERROR reading from file: " + e);
            System.out.println("returning emptying string for url");
        }
        return result;
    }
    
    private static String addSymbols(String baseURL, Collection<String> values, String seperator) {
        StringBuilder result = new StringBuilder(baseURL);
        for(String symbol : values) {
            result.append(symbol);
            result.append(seperator);
        }
        // remove last seperator, unless empty string
        if(seperator.length() > 0) {
            result.delete(result.length() 
                    - seperator.length(), result.length());
        }
        return result.toString();
    }
    
	private static Date getDate(String dateString) throws ParseException {
		 
		  DateFormat inFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss");

		  Date tempdate = inFormat.parse(dateString);
		  return tempdate;
	}
    
    
    
	
	  
}