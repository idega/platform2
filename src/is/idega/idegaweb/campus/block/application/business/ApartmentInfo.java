/*
 * Created on Jul 21, 2003
 *
 */
package is.idega.idegaweb.campus.block.application.business;

import com.idega.util.IWTimestamp;

/**
 * ApartmentInfo
 * @author aron 
 * @version 1.0
 */

public class ApartmentInfo {
	IWTimestamp rentFrom;
	Boolean furniture;
	Boolean waitOnList;
	String comment;
	String appliedKey1;
	String appliedKey2;
	String appliedKey3;
	
	public ApartmentInfo(IWTimestamp rentFrom,Boolean furniture,Boolean waitOnList,String comment,String key1,String key2,String key3){
		this.rentFrom = rentFrom;
		this.furniture = furniture;
		this.waitOnList = waitOnList;
		this.comment = comment;
		appliedKey1 = key1;
		appliedKey2 = key2;
		appliedKey3 = key3;
	}
	/**
	 * @return
	 */
	public String getAppliedKey1() {
		return appliedKey1;
	}

	/**
	 * @return
	 */
	public String getAppliedKey2() {
		return appliedKey2;
	}

	/**
	 * @return
	 */
	public String getAppliedKey3() {
		return appliedKey3;
	}

	/**
	 * @return
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @return
	 */
	public Boolean getFurniture() {
		return furniture;
	}

	/**
	 * @return
	 */
	public IWTimestamp getRentFrom() {
		return rentFrom;
	}

	/**
	 * @return
	 */
	public Boolean getWaitOnList() {
		return waitOnList;
	}

	/**
	 * @param string
	 */
	public void setAppliedKey1(String string) {
		appliedKey1 = string;
	}

	/**
	 * @param string
	 */
	public void setAppliedKey2(String string) {
		appliedKey2 = string;
	}

	/**
	 * @param string
	 */
	public void setAppliedKey3(String string) {
		appliedKey3 = string;
	}

	/**
	 * @param string
	 */
	public void setComment(String string) {
		comment = string;
	}

	/**
	 * @param boolean1
	 */
	public void setFurniture(Boolean boolean1) {
		furniture = boolean1;
	}

	/**
	 * @param timestamp
	 */
	public void setRentFrom(IWTimestamp timestamp) {
		rentFrom = timestamp;
	}

	/**
	 * @param boolean1
	 */
	public void setWaitOnList(Boolean boolean1) {
		waitOnList = boolean1;
	}

}
