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
import java.util.TimeZone;
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
       
       
       
       private static final int minutestoAdd = 60;
       
       
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
              
              //https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=MSFT&apikey=demo

              //     https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=MSFT&outputsize=full&apikey=demo

              //     Downloadable CSV file:
              //     https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=MSFT&apikey=demo&datatype=csv
              try {
                     // Install the all-trusting host verifier
                     HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

                     URL url = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=^NSEI&interval=60min&outputsize=compact&apikey=J27JKP9HNK701478");
                           //     "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=AMZN&apikey=J27JKP9HNK701478");
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
                           //System.out.println(output);
                           sb.append(output);
                     }
                     //System.out.println(sb.toString());
                     JsonParser parser = new JsonParser();
                     JsonElement jsonElement = parser.parse(sb.toString());
                     JsonObject rootObject = jsonElement.getAsJsonObject();

                     Gson gson = new Gson();
                     Gson gson2 = new GsonBuilder().registerTypeAdapter(OutputObj.class,
                                  new MyDeserializer()).create();

                     OutputObj obj = gson2.fromJson(sb.toString(), OutputObj.class);
                     
//                     System.out.println("Size of Timezone List" + obj.getTimezoneList().size());


                     //Sort the objects 
                     TreeMap<String, Timezone> sortedtimezoneList = new TreeMap<String, Timezone>();

                     // Copy all data from hashMap into TreeMap
                     sortedtimezoneList.putAll(obj.getTimezoneList());
                     
                     
                     //Sort the objects 
                     TreeMap<String, Details> levelList = new TreeMap<String, Details>();
                     TreeMap<String, Details> IstlevelList = new TreeMap<String, Details>();
                     
                     int noOfDaysToBeCalc = 2;
                     
                     String ToDate = "2019-06-27";
                     String fromDate = getEndDate(ToDate, -noOfDaysToBeCalc);
                     //System.out.println(fromDate);
                     //String Defaultminutes = "5";
                     
                     //String dayStartMinutes = "09:15:00";
              //     String dayEndMinutes = "15:25:00";
                     
                     String fromDateWithMins = addStartHrsMins(fromDate);
                     String endDateWithMins = addEndHrsMins(ToDate , 15);
                     
                     System.out.println(fromDateWithMins);
//                     System.out.println(endDateWithMins);
                     
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
                           boolean nseStock = true;
                           if(nseStock){
                                  String istTime = getIstTime(entry.getKey());
                                  //System.out.println("istTime->"+istTime);
                                  
                                  
                                  
                                  
                                  
                                  if (((getDate(istTime).equals((getDate(fromDateWithMins))))
                                                || (getDate(istTime).after((getDate(fromDateWithMins)))))
                                                && (getDate(istTime).before(getDate(endDateWithMins)))) {
                                         IstlevelList.put(istTime, details);
//                                         System.out.println("istTime Added" + istTime);
                                  }
                                  
                                  
                           }
                           
                           
                           
                           
                     
                     }
                     System.out.println("IstLevel List size" + IstlevelList.size());
//                     System.out.println("Level List size" + levelList.size());
                     
                     //Newest to olddest
                     
                     
                     
                     boolean is120 = false;
                     if(is120){
                     TreeMap<String, String> nseStartEndDateKeys = new TreeMap<String, String>();
                     
                     nseStartEndDateKeys = getNSEStarEndDateKeys(fromDateWithMins, endDateWithMins, minutestoAdd);
                     
                     
                     for (Entry<String, String> entry1 : nseStartEndDateKeys.entrySet()) {
//                           System.out.println(entry1.getKey() + "-" + entry1.getValue());
                           
                     }
                     
                     TreeMap<String, Details> consolidatedHighLowList = getNSEConsolidatedHighLowList(
                                  IstlevelList, nseStartEndDateKeys);
                     System.out.println("consolidatedHighLowList size"+ consolidatedHighLowList.size());
                     for (Entry<String, Details> entry1 : consolidatedHighLowList.entrySet()) {
                           System.out.println(entry1.getKey() +"| High - " + entry1.getValue().getHigh() +"| Low - " +entry1.getValue().getLow() +"| Open - " + entry1.getValue().getOpen()+"| Close - " +entry1.getValue().getClose());
                           
                           
                           
                     }
                     }else{
                    	ArrayList<String> nseStartTimeList = new  ArrayList<String>();
                    	nseStartTimeList = getNSEStartDateList(fromDateWithMins, endDateWithMins, minutestoAdd);
                    	for(String s : nseStartTimeList){
                    		//System.out.println(s);
                    		
                    	}
                    	
                    	 TreeMap<String, Details> newlevelList = new TreeMap<String, Details>(Collections.reverseOrder());
                    	 for(Entry<String, Details> entry : IstlevelList
                                 .entrySet()){
                    		 if(nseStartTimeList.contains(entry.getKey())){
                    			 
                    			 newlevelList.put(entry.getKey(), entry.getValue());
                    		 }
                    		 
                    	 }
                    	 
                    	 for (Entry<String, Details> entry1 : newlevelList.entrySet()) {
                             System.out.println(entry1.getKey() +"| High - " + entry1.getValue().getHigh() +"| Low - " +entry1.getValue().getLow() +"| Open - " + entry1.getValue().getOpen()+"| Close - " +entry1.getValue().getClose());
                             
                             
                             
                       }
                     }
                     
                     
                     
                     
                     // getMarkableList(levelList);

              
                     
                     
                     

              //     addMinutes(sortedtimezoneList);

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
       * @param levelList
       */
       private static void getMarkableList(TreeMap<String, Details> levelList) {
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
                     else if(i+1 < keyList.size()){
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
       }

       /**
       * @param sortedtimezoneList
       * @throws ParseException
       */
       /*private static void addMinutes(TreeMap<String, Timezone> sortedtimezoneList)
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
       }*/

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

              /*TreeMap<String, Details> consolidatedHighLowList = getHighLowList(
                           sortedtimezoneList, keys);*/
              return null;
       }
       
       private static TreeMap<String, Details> getNSEConsolidatedHighLowList(
                     TreeMap<String, Details> IstlevelList,
                     TreeMap<String, String> keys) {
              TreeMap<String, Details> consolidatedHighLowList = getHighLowList(
                           IstlevelList, keys);
              return consolidatedHighLowList;
              
       }

       /**
       * @param sortedtimezoneList
       * @param keys
       * @return
       * @throws ParseException 
        */
       private static TreeMap<String, Details> getHighLowList(
                     TreeMap<String, Details> IstlevelList,
                     TreeMap<String, String> keys)  {
              TreeMap<String, Details> consolidatedHighLowList = new TreeMap<String, Details>(Collections.reverseOrder());
              try {
              for (Map.Entry<String, String> entry : keys.entrySet()) {
                     Details details = new Details();
                     
                     details.setStartDate(entry.getKey());
                     details.setEndDate(entry.getValue());
                     

                     for (Map.Entry<String, Details> entry2 : IstlevelList
                                  .entrySet()) {
                           
                           if (getDate(entry2.getKey()).equals(getDate(entry.getKey()))) {
                                  Details zone2 = entry2.getValue();
                                  details.setOpen(zone2.getOpen());
                                  

                           }
                     }
                     for (Map.Entry<String, Details> entry2 : IstlevelList
                                  .entrySet()) {
                           if (getDate(entry2.getKey()).equals(getDate(entry.getValue()))) {
//                                  System.out.println("in");
                                  Details zone2 = entry2.getValue();
                                  details.setClose(zone2.getClose());
                                  

                           }
                     }

                     String highValue = "";
                     String lowValue = "";

                     Map<String, ArrayList<String>> highLow = gethighGetLow(
                                  entry.getKey(), entry.getValue(), IstlevelList);
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
                     
                     if(IstlevelList.containsKey(entry.getKey()) && (IstlevelList.containsKey(entry.getValue()))) {
                     consolidatedHighLowList.put(entry.getKey(), details);
                     }
              }
              }catch(Exception e) {
                     
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
                           enddate = getDateAfterAddingMins(entry.getKey(), minutes);
                           System.out.println(enddate);
                           keys.put(startdate, enddate);
                           flag = false;
                     } else if ((getDate(startdate).after(getDate(enddate)))) {
                           enddate = getDateAfterAddingMins(entry.getKey(), minutes);
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

       private static Map<String, ArrayList<String>> gethighGetLow(String startDate,
                     String endDate, TreeMap<String, Details> IstlevelList) {

              Map<String, ArrayList<String>> highLow = new HashMap<String, ArrayList<String>>();

              ArrayList<String> highList = new ArrayList<>();
              ArrayList<String> lowList = new ArrayList<>();

              for (Map.Entry<String, Details> entry2 : IstlevelList.entrySet()) {
                     try {
                           if ((((getDate(entry2.getKey()).equals(getDate(startDate))) || (getDate(entry2.getKey()).after(getDate(startDate)))) && ( (getDate(entry2
                                         .getKey()).equals(getDate(endDate))) ||
                                         (getDate(entry2
                                         .getKey()).before(getDate(endDate)))))) {
                                  Details zone2 = entry2.getValue();
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

       private static String getDateAfterAddingMins(String dateString, int minutes)
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
       
       private static String getIstTime(String estTime){
       
       SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    dateTimeFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
     //(EST)
    Date date = null;
       try {
              date = dateTimeFormat.parse(estTime);
       } catch (ParseException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
       }
    DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
    String istTime = timeFormat.format(date);
  /*  System.out.println("Time EST = " + estTime);
    System.out.println("Time IST = " + istTime);*/
    return istTime;
       }
       
       private static String getEndDate(String strtDate, int noOfDays){
              
              System.out.println("Date before Addition: "+strtDate);
              //Specifying date format that matches the given date
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
              Calendar c = Calendar.getInstance();
              try{
                 //Setting the date to the given date
                 c.setTime(sdf.parse(strtDate));
              }catch(ParseException e){
                     e.printStackTrace();
              }
                 
              //Number of Days to add
              c.add(Calendar.DAY_OF_MONTH, noOfDays);  
              //Date after adding the days to the given date
              String newDate = sdf.format(c.getTime());  
              //Displaying the new Date after addition of Days
              //System.out.println("Date after Addition: "+newDate);
              return newDate;
       }
       
       private static String addStartHrsMins(String fromDate){
              
              
              //Specifying date format that matches the given date
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
              Calendar calendar = Calendar.getInstance();
              try{
                 //Setting the date to the given date
                     calendar.setTime(sdf.parse(fromDate));
              }catch(ParseException e){
                     e.printStackTrace();
              }
                 
             
              // System.out.println("Original = " + calendar.getTime());
       
               // Substract 2 hour from the current time
               calendar.add(Calendar.HOUR, 9);
       
               // Add 30 minutes to the calendar time
               calendar.add(Calendar.MINUTE, 15);
       
               // Add 300 seconds to the calendar time
               calendar.add(Calendar.SECOND, 00);
               //System.out.println("Updated  = " + calendar.getTime());
               DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             //Date after adding the days to the given date
                     String newDate = timeFormat.format(calendar.getTime());  
                     //Displaying the new Date after addition of Days
                     //System.out.println("Date after Addition: "+newDate);
              return newDate;
              
       }
       
       private static String getyyyyMMddFormat(String fromDate){
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
              Calendar calendar = Calendar.getInstance();
              try{
                 //Setting the date to the given date
                     calendar.setTime(sdf.parse(fromDate));
              }catch(ParseException e){
                     e.printStackTrace();
              }

               DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
             //Date after adding the days to the given date
                     String newDate = timeFormat.format(calendar.getTime());  
                     //Displaying the new Date after addition of Days
                     //System.out.println("Date after Addition: "+newDate);
              return newDate;
              
       }
       
private static String addADayWithMins(String fromDate){
              
              
              //Specifying date format that matches the given date
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
              Calendar calendar = Calendar.getInstance();
              try{
                 //Setting the date to the given date
                     calendar.setTime(sdf.parse(fromDate));
              }catch(ParseException e){
                     e.printStackTrace();
              }
                 
             
              // System.out.println("Original = " + calendar.getTime());
              calendar.add(Calendar.DATE, 1);
       
               // Substract 2 hour from the current time
               calendar.add(Calendar.HOUR, 9);
       
               // Add 30 minutes to the calendar time
               calendar.add(Calendar.MINUTE, 15);
       
               // Add 300 seconds to the calendar time
               calendar.add(Calendar.SECOND, 00);
               //System.out.println("Updated  = " + calendar.getTime());
               DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             //Date after adding the days to the given date
                     String newDate = timeFormat.format(calendar.getTime());  
                     //Displaying the new Date after addition of Days
                     //System.out.println("Date after Addition: "+newDate);
              return newDate;
              
       }
       
private static String addEndHrsMins(String dateString, int endtimeInhrs){
              
              
              //Specifying date format that matches the given date
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
              Calendar calendar = Calendar.getInstance();
              try{
                 //Setting the date to the given date
                     calendar.setTime(sdf.parse(dateString));
              }catch(ParseException e){
                     e.printStackTrace();
              }
                 
             
               //System.out.println("Original = " + calendar.getTime());
       
               // Substract 2 hour from the current time
               calendar.add(Calendar.HOUR, endtimeInhrs);
       
               // Add 30 minutes to the calendar time
               calendar.add(Calendar.MINUTE, 15);
       
               // Add 300 seconds to the calendar time
               calendar.add(Calendar.SECOND, 00);
               //System.out.println("Updated  = " + calendar.getTime());
          
               DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             //Date after adding the days to the given date
                     String newDate = timeFormat.format(calendar.getTime());  
                     //Displaying the new Date after addition of Days
                     //System.out.println("Date after Addition: "+newDate);
              return newDate;
              
       }

private static TreeMap<String, String> getNSEStarEndDateKeys(
              String starttime, String endTime, int minutes) throws ParseException {
       TreeMap<String, String> keys = new TreeMap<String, String>();

       String endate = null;
       String startDate = starttime;
       
       do{
              endate = getDateAfterAddingMins(startDate, minutes);
              //keys.put((getDateAfterAddingMins(startDate, minutestoAdd)), endate);
              keys.put(startDate, endate);
              String startdateWithEndHrs = addEndHrsMins(getyyyyMMddFormat(startDate), 14);
       //     System.out.println(startdateWithEndHrs);
              
              if(getDate(endate).before(getDate(endTime))){
              if(getDate(endate).after(getDate(startdateWithEndHrs)) || getDate(endate).equals(getDate(startdateWithEndHrs))){
                     startDate = addADayWithMins(getyyyyMMddFormat(startdateWithEndHrs));
              }
              else{
                     //startDate = endate;
                     startDate = getDateAfterAddingMins(endate, 60);
                     //startDate = getDateAfterAddingMins(endate, 5);
              }
              }
              
              
              
       //System.out.println(startDate);        
       //System.out.println(endTime);   
              
              
       }while(getDate(endate).before(getDate(endTime)) && !(getDate(getDateAfterAddingMins(startDate, 60)).equals(getDate(endTime))));

      /* for (Entry<String, String> entry : keys.entrySet())
              System.out.println("Start date " + entry.getKey() + ", End date = "
                           + entry.getValue());*/

//       System.out.println("Start date and end date keys Size" + keys.size());
       return keys;
}

private static ArrayList<String> getNSEStartDateList(
        String starttime, String endTime, int minutes) throws ParseException {
ArrayList<String> keys = new ArrayList<String>();

 
 String startDate = starttime;
 
 do{
	 
       
        //keys.put((getDateAfterAddingMins(startDate, minutestoAdd)), endate);
       
        String startdateWithEndHrs = addEndHrsMins(getyyyyMMddFormat(startDate), 15);
 //     System.out.println(startdateWithEndHrs);
        
        if(getDate(startDate).before(getDate(endTime)) || getDate(startDate).equals(getDate(endTime))){
        if(getDate(startDate).after(getDate(startdateWithEndHrs))){
               startDate = addADayWithMins(getyyyyMMddFormat(startdateWithEndHrs));
        }
        else{
        		keys.add(startDate);
               startDate = getDateAfterAddingMins(startDate, 60);
               
        }
        }
        
        
        
 //System.out.println(startDate);        
 //System.out.println(endTime);   
        
        
 }while(getDate(startDate).before(getDate(endTime)) || getDate(startDate).equals(getDate(endTime)) || (getDate(getDateAfterAddingMins(startDate, 60)).equals(getDate(endTime))));

/* for (Entry<String, String> entry : keys.entrySet())
        System.out.println("Start date " + entry.getKey() + ", End date = "
                     + entry.getValue());*/

// System.out.println("Start date and end date keys Size" + keys.size());
 return keys;
}

}

