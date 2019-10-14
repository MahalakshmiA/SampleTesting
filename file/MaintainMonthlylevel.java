/**
 * 
 */
package com.api.testing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * @author 525523
 *
 */
public class MaintainMonthlylevel {
	

	public static String defaultPath = "D:\\Maha\\workspace\\APITesting-S\\config\\file\\";
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String niftyStocksfileName = "niftyStocksLevels";
		
		generateDailyAndMonthlyFiles(niftyStocksfileName);

	}

	private static void generateDailyAndMonthlyFiles(String niftyStocksfileName) {
		String niftyStocksFilePath = defaultPath + niftyStocksfileName + ".txt";
		System.out.println(niftyStocksFilePath);

		Calendar cal = Calendar.getInstance();
		String currMonth = new SimpleDateFormat("MMM").format(cal.getTime());
		String niftyStocksMonthlyfileName = niftyStocksfileName + "-" + currMonth;
		String niftyStocksMonthlyFilePath = defaultPath + niftyStocksMonthlyfileName + ".txt";

		File tempNiftyDailyFile = new File(niftyStocksFilePath);
		ArrayList<StockLevels> dailyStocksLevelsList = getShortListedStocksList(niftyStocksFilePath);

		if (tempNiftyDailyFile.exists()) {
			File tempNiftyMonthlyFile = new File(niftyStocksMonthlyFilePath);
			if (!tempNiftyMonthlyFile.exists()) {
				System.out.println("Monthly file doesn't exists, hence creating new");
				cal.add(Calendar.MONTH, -1);
				String prevMonth = new SimpleDateFormat("MMM").format(cal.getTime());
				System.out.println(prevMonth);
				String niftyStocksPrevMonthlyfileName = niftyStocksfileName + "-" + prevMonth;
				String niftyStocksPrevMonthlyFilePath = defaultPath + niftyStocksPrevMonthlyfileName + ".txt";
				File tempNiftyPrevMonthlyFile = new File(niftyStocksPrevMonthlyFilePath);
				if (tempNiftyPrevMonthlyFile.exists()) {
					System.out.println("Previous Month File Exists - cloning it to current month");
					try {
						ArrayList<StockLevels> prevMonthStocksLevelsList = getShortListedStocksList(
								niftyStocksPrevMonthlyFilePath);
						writeStockListToFile(niftyStocksMonthlyFilePath, prevMonthStocksLevelsList);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					System.out.println("Previous Month file also doesn't exists - First time loading monthly file");
					try {
						writeStockListToFile(niftyStocksMonthlyFilePath, dailyStocksLevelsList);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				System.out.println("Monthly file exists");

			}
		} else {
			System.out.println("Daily file doesn't exists");
		}

		ArrayList<StockLevels> monthlyStocksLevelsList = getShortListedStocksList(niftyStocksMonthlyFilePath);
		System.out.println("dailyStocksLevelsList Size " + dailyStocksLevelsList.size());
		System.out.println("MonthlyStocksLevelsList Size " + monthlyStocksLevelsList.size());

		HashMap<String, ArrayList<StockLevels>> addedRemovedMap = generateAddedRemovedStocksList(monthlyStocksLevelsList,
				dailyStocksLevelsList,niftyStocksfileName);
		ArrayList<StockLevels> newlyAddedStocksList = addedRemovedMap.get("ADDED");
		ArrayList<StockLevels> removedStocksList = addedRemovedMap.get("REMOVED");
		System.out.println("newlyAddedStocksList Size " + newlyAddedStocksList.size());
		System.out.println("removedStocksList Size " + removedStocksList.size());

		try {
		
			monthlyStocksLevelsList.addAll(newlyAddedStocksList);
			System.out.println("MonthlyStocksLevelsList Size After added new Stocks " + monthlyStocksLevelsList.size());
			writeStockListToFile(niftyStocksMonthlyFilePath, monthlyStocksLevelsList);
		} catch (Exception e) {
			System.out.println("Exception occurs");
		}
	}

	public static void maintainMonthlyFile() {

	}

	public static ArrayList<StockLevels> getShortListedStocksList(String niftyStocksFilePath) {
		BufferedReader br = null;
		FileReader fr = null;
		ArrayList<StockLevels> shortListedStocks = new ArrayList<>();

		try {

			fr = new FileReader(niftyStocksFilePath);
			br = new BufferedReader(fr);

			// read line by line
			String line;

			while ((line = br.readLine()) != null) {
				// System.out.println(line);
				String[] splitLines = line.split("\\|");
				StockLevels levels = new StockLevels();
				levels.setStockName(splitLines[0]);
				levels.setDate(splitLines[1]);
				levels.setLevelType(splitLines[2]);
				levels.setOldLevel(Double.valueOf(splitLines[3]));
				levels.setOldLevelEnd((Double.valueOf(splitLines[4])));
				levels.setOldLevelPercent(Double.valueOf(splitLines[5]));
				/*
				 * if (levels.getOldLevelPercent() < inputLevelPercent) {
				 * shortListedStocks.add(levels); }
				 */
				shortListedStocks.add(levels);
			}

		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		} finally {
			try {
				if (br != null)
					br.close();

				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				System.err.format("IOException: %s%n", ex);
			}
		}
		return shortListedStocks;
	}

	public static void writeStockListToFile(String pFilename, ArrayList<StockLevels> stocksList) throws IOException {
		StringBuffer pData = new StringBuffer();
		for (StockLevels levels : stocksList) {
			String notificationLevel = levels.getStockName() + "|" + levels.getDate() + "|" + levels.getLevelType()
					+ "|" + levels.getOldLevel() + "|" + levels.getOldLevelEnd() + "|" + levels.getOldLevelPercent()
					+ "|" + levels.getNewlyAdded();
			pData.append(notificationLevel + "\n");

		}
		BufferedWriter out = new BufferedWriter(new FileWriter(pFilename));
		out.write(pData.toString());
		out.flush();
		out.close();
	}

	public static HashMap<String, ArrayList<StockLevels>> generateAddedRemovedStocksList(
			ArrayList<StockLevels> file1Higher, ArrayList<StockLevels> file2Lower, String genericFileName) {
		
		String newlyAddedStocksFileName = genericFileName+"-Added";
		String removedStocksFileName = genericFileName+"-Removed";
		String newlyAddedStocksFilePath = defaultPath + newlyAddedStocksFileName + ".txt";
		String removedStocksFilePath = defaultPath + removedStocksFileName + ".txt";

		
		ArrayList<StockLevels> newlyAddedStocksList = new ArrayList<StockLevels>();
		ArrayList<StockLevels> removedStocksList = new ArrayList<StockLevels>();
		HashMap<String, ArrayList<StockLevels>> addedRemovedMap = new HashMap<>();

		for (StockLevels levels : file1Higher) {

			boolean isexists = false;

			for (StockLevels levels2 : file2Lower) {

				if (levels.getStockName().equalsIgnoreCase(levels2.getStockName())
						&& levels.getDate().equalsIgnoreCase(levels2.getDate())
						&& levels.getLevelType().equalsIgnoreCase(levels2.getLevelType())
						&& levels.getOldLevel().equals(levels2.getOldLevel())) {

					isexists = true;
					break;
				}

			}
			if (!isexists) {
				removedStocksList.add(levels);

			}

		}

		for (StockLevels levels : file2Lower) {

			boolean isexists = false;
			for (StockLevels levels2 : file1Higher) {

				if (levels.getStockName().equalsIgnoreCase(levels2.getStockName())
						&& levels.getDate().equalsIgnoreCase(levels2.getDate())
						&& levels.getLevelType().equalsIgnoreCase(levels2.getLevelType())
						&& levels.getOldLevel().equals(levels2.getOldLevel())) {

					isexists = true;
					break;
				}

			}
			if (!isexists) {
				levels.setNewlyAdded("YES");
				newlyAddedStocksList.add(levels);
			}

		}

		addedRemovedMap.put("ADDED", newlyAddedStocksList);
		addedRemovedMap.put("REMOVED", removedStocksList);
		try {
		writeStockListToFile(newlyAddedStocksFilePath, newlyAddedStocksList);
		writeStockListToFile(removedStocksFilePath, removedStocksList);
		}catch(Exception e) {
			System.out.println("Exception occurs while writing the file");
		}
		
		return addedRemovedMap;

	}

}
