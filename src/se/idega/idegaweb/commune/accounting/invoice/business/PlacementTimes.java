package se.idega.idegaweb.commune.accounting.invoice.business;

import se.idega.idegaweb.commune.accounting.business.AccountingUtil;

import com.idega.util.IWTimestamp;

/**
 * Holder for times that are calculated for placement times
 * 
 * @author Joakim
 *
 */
public class PlacementTimes {
	private final IWTimestamp firstCheckDay;
	private final IWTimestamp lastCheckDay;
	private final int specificNumberOfDaysPrMonth;
	
	public PlacementTimes (final IWTimestamp firstCheckDay, final IWTimestamp lastCheckDay, int specificNumberOfDays) {
		this.firstCheckDay = firstCheckDay;
		this.lastCheckDay = lastCheckDay;
		specificNumberOfDaysPrMonth = specificNumberOfDays;
	}

	public int getDays () {
		return 1 + AccountingUtil.getDayDiff (firstCheckDay, lastCheckDay);
	}

	public float getMonths () {
		float months = 1.0f
				+ (lastCheckDay.getYear () * 12 + lastCheckDay.getMonth ())
				- (firstCheckDay.getYear () * 12 + firstCheckDay.getMonth ());
		// decrease with days before start date
		months -= (float) (firstCheckDay.getDay () - 1) / (float) daysInMonth (firstCheckDay);
		// decrease with days after end date
		months -= 1.0f - (float) lastCheckDay.getDay () / (float) daysInMonth (lastCheckDay);
		return months;
	}

	public IWTimestamp getLastCheckDay () {
		return lastCheckDay;
	}

	public IWTimestamp getFirstCheckDay () {
		return firstCheckDay;
	}

	private int daysInMonth (final IWTimestamp date) {
		if (specificNumberOfDaysPrMonth > 0) {
			return this.specificNumberOfDaysPrMonth;
		}
		final IWTimestamp firstDay = new IWTimestamp (date);
		firstDay.setDay (1);
		final IWTimestamp lastDay = new IWTimestamp (firstDay);
		lastDay.addMonths (1);
		final int daysInMonth = AccountingUtil.getDayDiff (firstDay, lastDay);
		return daysInMonth;
	}
}