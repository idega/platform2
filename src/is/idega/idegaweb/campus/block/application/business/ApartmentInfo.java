/*
 * Created on Jul 21, 2003
 *
 */
package is.idega.idegaweb.campus.block.application.business;

import com.idega.util.IWTimestamp;

/**
 * ApartmentInfo
 * 
 * @author aron
 * @version 1.0
 */

public class ApartmentInfo {
	private IWTimestamp rentFrom = null;
	private boolean furniture = false;
	private boolean waitOnList = false;
	private String comment = null;
	private String appliedKey1 = null;
	private String appliedKey2 = null;
	private String appliedKey3 = null;

	public ApartmentInfo(IWTimestamp rentFrom, boolean furniture,
			boolean waitOnList, String comment, String key1, String key2,
			String key3) {
		this.rentFrom = rentFrom;
		this.furniture = furniture;
		this.waitOnList = waitOnList;
		this.comment = comment;
		appliedKey1 = key1;
		appliedKey2 = key2;
		appliedKey3 = key3;
	}

	public String getAppliedKey1() {
		return appliedKey1;
	}

	public String getAppliedKey2() {
		return appliedKey2;
	}

	public String getAppliedKey3() {
		return appliedKey3;
	}

	public String getComment() {
		return comment;
	}

	public boolean getFurniture() {
		return furniture;
	}

	public IWTimestamp getRentFrom() {
		return rentFrom;
	}

	public boolean getWaitOnList() {
		return waitOnList;
	}

	public void setAppliedKey1(String key) {
		appliedKey1 = key;
	}

	public void setAppliedKey2(String key) {
		appliedKey2 = key;
	}

	public void setAppliedKey3(String key) {
		appliedKey3 = key;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setFurniture(boolean furniture) {
		this.furniture = furniture;
	}

	public void setRentFrom(IWTimestamp rentFrom) {
		this.rentFrom = rentFrom;
	}

	public void setWaitOnList(boolean onList) {
		waitOnList = onList;
	}
}