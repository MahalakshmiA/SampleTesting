/**
 * 
 */
package com.api.testing;

/**
 * @author 525523
 *
 */
public class StockLevels {
	public String date;
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Double getOldLevelEnd() {
		return oldLevelEnd;
	}

	public void setOldLevelEnd(Double oldLevelEnd) {
		this.oldLevelEnd = oldLevelEnd;
	}



	public Double oldLevelEnd;
	
	
	/**
	 * @return the stockName
	 */
	public String getStockName() {
		return stockName;
	}

	/**
	 * @param stockName the stockName to set
	 */
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

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

	

	public String stockName;
	
	public String levelType;
	
	public Double oldLevel;
	
	
	/**
	 * @return the oldLevel
	 */
	public Double getOldLevel() {
		return oldLevel;
	}

	/**
	 * @param oldLevel the oldLevel to set
	 */
	public void setOldLevel(Double oldLevel) {
		this.oldLevel = oldLevel;
	}

	/**
	 * @return the oldLevelPercent
	 */
	public Double getOldLevelPercent() {
		return oldLevelPercent;
	}

	/**
	 * @param oldLevelPercent the oldLevelPercent to set
	 */
	public void setOldLevelPercent(Double oldLevelPercent) {
		this.oldLevelPercent = oldLevelPercent;
	}

	/**
	 * @return the newLevel
	 */
	public Double getNewLevel() {
		return newLevel;
	}

	/**
	 * @param newLevel the newLevel to set
	 */
	public void setNewLevel(Double newLevel) {
		this.newLevel = newLevel;
	}

	/**
	 * @return the newLevelPercent
	 */
	public Double getNewLevelPercent() {
		return newLevelPercent;
	}

	/**
	 * @param newLevelPercent the newLevelPercent to set
	 */
	public void setNewLevelPercent(Double newLevelPercent) {
		this.newLevelPercent = newLevelPercent;
	}



	public Double oldLevelPercent;
	
	public Double newLevel;
	
	public Double newLevelPercent;
	
	public int score = 0;
	
	public String newlyAdded ="";

	/**
	 * @return the newlyAdded
	 */
	public String getNewlyAdded() {
		return newlyAdded;
	}

	/**
	 * @param newlyAdded the newlyAdded to set
	 */
	public void setNewlyAdded(String newlyAdded) {
		this.newlyAdded = newlyAdded;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}

}
