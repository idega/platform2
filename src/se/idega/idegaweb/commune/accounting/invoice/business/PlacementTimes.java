package se.idega.idegaweb.commune.accounting.invoice.business;

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
	
	public PlacementTimes (final IWTimestamp firstCheckDay, final IWTimestamp lastCheckDay) {
		this.firstCheckDay = firstCheckDay;
		this.lastCheckDay = lastCheckDay;
	}

	public int getDays () {
		final IWTimestamp lastCheckDayPlusOneDay = createDayAfter (lastCheckDay);
		return IWTimestamp.getDaysBetween (firstCheckDay, lastCheckDayPlusOneDay);
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

	private static IWTimestamp createDayAfter (final IWTimestamp sourceDay) {
		final IWTimestamp dayAfter = new IWTimestamp (sourceDay);
		dayAfter.addDays (1);
		return dayAfter;
	}

	private static int daysInMonth (final IWTimestamp date) {
		final IWTimestamp firstDay = new IWTimestamp (date);
		firstDay.setDay (1);
		final IWTimestamp lastDay = new IWTimestamp (firstDay);
		lastDay.addMonths (1);
		final int daysInMonth = IWTimestamp.getDaysBetween (firstDay, lastDay);
		return daysInMonth;
	}
}
