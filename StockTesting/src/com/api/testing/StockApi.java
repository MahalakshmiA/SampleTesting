package com.api.testing;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.net.HttpURLConnection;

import java.net.MalformedURLException;

import java.net.ProtocolException;

import java.net.URL;

import java.text.DateFormat;

import java.text.ParseException;

import java.text.SimpleDateFormat;

import java.util.Calendar;

import java.util.Date;

import java.util.HashMap;

import java.util.Map;

import com.google.gson.Gson;

import com.google.gson.JsonSyntaxException;

public class StockApi {

	private static String lastCandle = "";

	private static String currCandle = "";

	private static int noOfLG = 0;

	private static int noOfLR = 0;

	private static int noOfSL = 0;

	private static boolean supportLevel = false;

	private static boolean resistLevel = false;

	private static boolean markLevel = false;

	public static void main(String[] args) {

		try {

			// System.out.println("Java version "+
			// System.getProperty("java.version"));

			String fnName = "TIME_SERIES_MONTHLY";

			// String symbol = "tcs.ns";

			// String symbol = "tatasteel.ns";

			// String symbol = "sbin.ns";

			// String symbol = "infy.ns";

			String symbol = "^NSEI";

			// outputsize = full/compact

			getHTFLevels(symbol, "monthly");

			getDailyLevels(symbol);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	static void getHTFLevels(String symbol, String timeFrame) {

		String fnName = "TIME_SERIES_MONTHLY";

		if ("monthly".equalsIgnoreCase(timeFrame)) {

			// fnName = "TIME_SERIES_MONTHLY";

			fnName = "TIME_SERIES_MONTHLY";

		} else if ("weekly".equalsIgnoreCase(timeFrame)) {

			fnName = "TIME_SERIES_WEEKLY";

		} else if ("daily".equalsIgnoreCase(timeFrame)) {

			fnName = "TIME_SERIES_DAILY";

		}

		String urlString = formURL(fnName, symbol);

		Map map = retriveAPIdata(urlString);

		listSupportResistance(map, timeFrame, "high");

	}

	static void getWeeklyLevels(String symbol) {

	}

	static void getDailyLevels(String symbol) {

		String fnName = "TIME_SERIES_DAILY";

		String urlString = formURL(fnName, symbol);

		Map map = retriveAPIdata(urlString);

		listSupportResistance(map, "daily", "low");

	}

	static String formURL(String fnName, String symbol) {

		String urlBase = "https://www.alphavantage.co/query?function=";

		String outputsize = "&outputsize=full";

		String apiKey = "&apikey=F4ASHUF1BONNF5AQ";

		// symbol = "NSE:ASHOKLEY";

		String urlString = urlBase + fnName + "&symbol=" + symbol + outputsize
				+ apiKey;
		
		System.out.println(urlString);

		return urlString;

	}

	static void listSupportResistance(Map map, String timeSeries,
			String timeFrame) {

		/*
		 * 1. Identify Candle type
		 * 
		 * 2. check leg-in or leg-out candle
		 * 
		 * a. If gap found check gap type and decide leg type
		 * 
		 * 3. Check if level(basing) found
		 * 
		 * 4. Check level Type - RBR, DBR, RBD or DBD
		 * 
		 * 5. Set level high/low
		 * 
		 * 6. set all time high/low
		 */

		String timeSeriesKey = "";

		if ("daily".equalsIgnoreCase(timeSeries)) {

			timeSeriesKey = "Time Series (Daily)";

		} else if ("monthly".equalsIgnoreCase(timeSeries)) {

			timeSeriesKey = "Monthly Time Series";

		}

		System.out.println(timeFrame + " Support Resistance levels \n");

		Map map1 = (Map) map.get(timeSeriesKey);

		String strDate = "";

		String startDate = "2018-12-01";

		int noOfdays = 2500;

		int i = 0;

		Double supportHigh = 0.0;

		Double supportLow = 0.0;

		Double resistHigh = 0.0;

		Double resistLow = 0.0;

		Double allTimeHigh = 0.0;

		Double allTimeLow = 0.0;

		Double candleHigh = 0.0;

		Double candlelow = 0.0;

		Double candleOpen = 0.0;

		Double candleClose = 0.0;

		Double lastCandleHigh = 0.0;

		Double lastCandlelow = 0.0;

		boolean isSuppAvailble = false;

		boolean isResistAvailble = false;

		boolean supportGap;

		boolean resistGap;

		while (i != noOfdays) {

			supportGap = false;

			resistGap = false;

			Calendar cal = Calendar.getInstance();

			cal.add(Calendar.DATE, -i);

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			strDate = formatter.format(cal.getTime());

			try {

				if (cal.getTime().after(
						new SimpleDateFormat("yyyy-MM-dd").parse(startDate))) {

					System.out.println(" Date to process " + cal.getTime());

					i++;

					continue;

				}

			} catch (ParseException e) {

				// TODO Auto-generated catch block

				e.printStackTrace();

			}

			// System.out.println("\n" + strDate);

			if (map1.containsKey(strDate)) {

				Map map2 = (Map) map1.get(strDate);

				candleHigh = Double.parseDouble(map2.get("2. high").toString());

				candlelow = Double.parseDouble(map2.get("3. low").toString());

				candleOpen = Double.parseDouble(map2.get("1. open").toString());

				candleClose = Double.parseDouble(map2.get("4. close")
						.toString());

				currCandle = getCandleType(map2);

				// TODO fix the logic to consider gap. below logic is wrongly
				// updating resistance and support flags and not resetting them.

				if (!"high".equalsIgnoreCase(timeFrame) && lastCandlelow != 0.0
						&& lastCandleHigh != 0.0) {

					// If gap identified, consider LG and LR count as 2

					if (candleClose > lastCandlelow
							&& candleClose > lastCandleHigh)

						resistGap = true;

					if (candleClose < lastCandlelow
							&& candleClose < lastCandleHigh)

						supportGap = true;

					// System.out.println(" Gap on "+ strDate+" candleClose - "+
					// candleClose +" lastCandleHigh - "+ lastCandleHigh +
					// " lastCandlelow - "+ lastCandlelow);

				}

				markSupportResistance(currCandle, timeFrame, supportGap,
						resistGap);

				// lastCandle = currCandle;

				if (supportLevel) {// Only Candles inside Support

					if ("high".equalsIgnoreCase(timeFrame)) {

						if (candleOpen > supportHigh)

							supportHigh = candleOpen;

						if (candleClose > supportHigh)

							supportHigh = candleClose;

					} else {

						if (candleHigh > supportHigh)

							supportHigh = candleHigh;

					}

					if (supportLow == 0.0 || candlelow < supportLow)

						supportLow = candlelow;

					// System.out.println(" Support "+ strDate+" CandleType "+
					// currCandle);

				} else if (resistLevel) {// Only Candles inside Resistance

					if ("high".equalsIgnoreCase(timeFrame)) {

						if (resistLow == 0.0 || candleOpen < resistLow)

							resistLow = candleOpen;

						if (resistLow == 0.0 || candleClose < resistLow)

							resistLow = candleClose;

					} else {

						if (resistLow == 0.0 || candlelow < resistLow)

							resistLow = candlelow;

					}

					if (candleHigh > resistHigh)

						resistHigh = candleHigh;

					// System.out.println(" Resistance "+
					// strDate+" CandleType "+ currCandle);

				} else {// Candle is LG or LR or SL outside levels

					// TODO fix the logic to avoid only leg-in and leg out
					// candle for RBR, DBR, RBD & DBD -- Avoiding all
					// consecutive LG/LR now

					if (!markLevel) {

						if (candleHigh > allTimeHigh && currCandle != "LR") {

							// TODO avoid first candle if LR

							allTimeHigh = candleHigh;

						}

						if (allTimeLow == 0.0 || candlelow < allTimeLow
								&& currCandle != "LG") { // TODO avoid first
															// candle if LG

							allTimeLow = candlelow;

						}

					} else {// Use leg-out candle extreme as level extreme for
							// DBR & RBD

						if (candleHigh > resistHigh && currCandle != "LR")

							resistHigh = candleHigh;

						if (supportLow == 0.0
								|| (candlelow < supportLow && currCandle != "LG"))

							supportLow = candlelow;

					}

				}

				/*
				 * if(supportGap || resistGap){
				 * 
				 * supportLevel = false;
				 * 
				 * resistLevel = false;
				 * 
				 * }
				 */

				// if(markLevel || ((resistLevel || supportLevel ) &&
				// (supportGap || resistGap))){

				if (markLevel) {

					System.out.println(" Mark level " + strDate
							+ " CandleType " + currCandle + "  " + supportLow
							+ "  " + supportHigh + "  " + allTimeLow);

					if (supportHigh != 0.0 && supportLow != 0.0
							&& supportHigh < allTimeLow && !isSuppAvailble) {

						System.out
								.println(" Support level " + strDate
										+ " from  " + supportHigh + " to "
										+ supportLow);

						isSuppAvailble = true;

						allTimeLow = supportLow;

					}

					// System.out.println(" Mark level "+
					// strDate+" CandleType "+ currCandle +"  " +
					// resistHigh+"  " + resistLow +"  " + allTimeHigh);

					if (resistHigh != 0.0 && resistLow != 0.0
							&& resistLow > allTimeHigh && !isResistAvailble) {

						System.out.println(" Resistance level " + strDate
								+ " from  " + resistLow + " to " + resistHigh);

						isResistAvailble = true;

						allTimeHigh = resistHigh;

					}

					if (isSuppAvailble && isResistAvailble)

						break;

					supportHigh = 0.0;

					supportLow = 0.0;

					resistHigh = 0.0;

					resistLow = 0.0;

					if (candleHigh > allTimeHigh && currCandle != "LR")// set
																		// leg-out
																		// candle
																		// high
																		// as
																		// all
																		// time
																		// high

						allTimeHigh = candleHigh;

					if (candlelow < allTimeLow && currCandle != "LG")// set
																		// leg-out
																		// candle
																		// low
																		// as
																		// all
																		// time
																		// low

						allTimeLow = candlelow;

				}

			}

			lastCandleHigh = candleHigh;

			lastCandlelow = candlelow;

			i++;

		}

		if (!isSuppAvailble)

			System.out.println("No Support level ");

		if (!isResistAvailble)

			System.out.println("No Resistance level ");

	}

	static Map retriveAPIdata(String urlString) {

		Map map = new HashMap<>();

		try {

			URL url = new URL(urlString);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");

			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {

				throw new RuntimeException("Failed : HTTP error code : "

				+ conn.getResponseCode());

			}

			BufferedReader br = new BufferedReader(new InputStreamReader(

			(conn.getInputStream())));

			String output;

			StringBuffer buffer = new StringBuffer();

			System.out.println("URL ...." + urlString + " \n");

			while ((output = br.readLine()) != null) {

				buffer = buffer.append(output);

			}

			conn.disconnect();

			Gson gson = new Gson();

			map = gson.fromJson(buffer.toString(), Map.class);

		} catch (JsonSyntaxException e) {

			e.printStackTrace();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (ProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		return map;

	}

	static void markSupportResistance(String currCandle, String timeFrame,
			boolean supportGap, boolean resistGap) {

		/*
		 * noOfLG = noOfLG+countLG;
		 * 
		 * noOfLR = noOfLR+countLR;
		 */

		boolean legInCandle = false;

		if (supportGap || resistGap) {

			if (supportLevel || resistLevel) {

				legInCandle = true;

			}

		}

		if ("LG".equalsIgnoreCase(currCandle)) {

			noOfLR = 0;

			if (supportLevel || resistLevel) {// Restart the LG candle count
												// after a level

				noOfLG = 1;

				noOfSL = 0;

				markLevel = true;

				supportLevel = false;

				resistLevel = false;

			}

			else {

				markLevel = false;

				noOfLG++;

			}

		}

		else if ("LR".equalsIgnoreCase(currCandle)) {

			noOfLG = 0;

			if (supportLevel || resistLevel) {// Restart the LR candle count
												// after a level

				noOfLR = 1;

				noOfSL = 0;

				markLevel = true;

				supportLevel = false;

				resistLevel = false;

			}

			else {

				markLevel = false;

				noOfLR++;

			}

		}

		else if ("SL".equalsIgnoreCase(currCandle)) {

			noOfSL++;

			/*
			 * if((supportGap || resistGap) && (supportLevel || resistLevel)){
			 * 
			 * markLevel = true;
			 * 
			 * 
			 * 
			 * }else{
			 * 
			 * markLevel = false;
			 * 
			 * }
			 */

			markLevel = false;

			// gap is considered as 2 large green candles

			if ((noOfLG >= 2 || supportGap || ("high"
					.equalsIgnoreCase(timeFrame) && noOfLG >= 1)) && noOfSL < 6)

				supportLevel = true;

			// gap is considered as 2 large red candles

			else if ((noOfLR >= 2 || resistGap || ("high"
					.equalsIgnoreCase(timeFrame) && noOfLR >= 1)) && noOfSL < 6)

				resistLevel = true;

			else {

				noOfLG = 0;

				noOfLR = 0;

				noOfSL = 0;

				supportLevel = false;

				resistLevel = false;

			}

		}

		// if((supportGap || resistGap) && legInCandle){

		if ((supportGap || resistGap) && (supportLevel || resistLevel)) {

			markLevel = true;

			// TODO set mark level to true only for leg-in candle, currently
			// setting for both leg-in and leg-out candle(Only for Gaps)

			/*
			 * supportLevel = false;
			 * 
			 * resistLevel = false;
			 */

		}

	}

	static String getCandleType(Map map) {

		Double daysRange = Double.parseDouble(map.get("2. high").toString())
				- Double.parseDouble(map.get("3. low").toString());

		Double bodyRange = Double.parseDouble(map.get("4. close").toString())
				- Double.parseDouble(map.get("1. open").toString());

		String candleType = "";

		double candleSize = bodyRange / daysRange;

		if (candleSize > 0.5) {

			candleType = "LG";

		} else if (candleSize < -0.5) {

			candleType = "LR";

		} else {

			candleType = "SL";

		}

		/*
		 * System.out.println("\n daysRange " + daysRange.floatValue());
		 * 
		 * System.out.println("\n bodyRange " + bodyRange.floatValue());
		 */

		// System.out.println("\n bodyRange/daysRange " + bodyRange/daysRange);

		// System.out.println("\n candleType " + candleType);

		return candleType;

	}

}

/*
 * public class DayQuote {
 * 
 * @JsonProperty("date")
 * 
 * public String date;
 * 
 * 
 * 
 * @JsonProperty("quote")
 * 
 * public List<Quote> quote;
 * 
 * }
 * 
 * public class Quote {
 * 
 * @JsonProperty("1. open") public String open;
 * 
 * @JsonProperty("2. high") public String high;
 * 
 * @JsonProperty("3. low") public String low;
 * 
 * @JsonProperty("4. close") public String close;
 * 
 * @JsonProperty("Artist Name") public String artistName;
 * 
 * @JsonProperty("5. volume") public String volume;
 * 
 * }
 * 
 * 
 * 
 * Library lib = mapper.readValue(jsonString, Library.class);
 */