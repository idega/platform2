package se.idega.idegaweb.commune.accounting.invoice.business;

import com.idega.util.IWTimestamp;

/**
 * Holder for times that are calculated for placement times
 * 
 * @author Joakim
 *
 */
public class PlacementTimes {
	IWTimestamp startTime = new IWTimestamp();
	IWTimestamp endTime = new IWTimestamp();
	float months = 0;
	int days = 0;
	
	public PlacementTimes(IWTimestamp startTime, IWTimestamp endTime, float months, int days){
		this.startTime = startTime;
		this.endTime = endTime;
		this.months = months;
		this.days = days;
	}
	
	
	/**
	 * @return
	 */
	public int getDays() {
		return days;
	}

	/**
	 * @return
	 */
	public IWTimestamp getEndTime() {
		return endTime;
	}

	/**
	 * @return
	 */
	public float getMonths() {
		return months;
	}

	/**
	 * @return
	 */
	public IWTimestamp getStartTime() {
		return startTime;
	}

}
