package se.idega.idegaweb.commune.accounting.business;

import com.idega.util.IWTimestamp;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AccountingUtil {
	public static long roundAmount (float f) {
		final long round = Math.round (f);
		return round - (0.5f == f - Math.floor (f) ? (round % 2) : 0);
	}
	
	public static int getDayDiff (final Date startDate,
																final Date endDate) {
		final Calendar startCalendar = new GregorianCalendar ();
		startCalendar.setTime (startDate);
		final Calendar endCalendar = new GregorianCalendar ();
		endCalendar.setTime (endDate);
		return getDayDiff (startCalendar, endCalendar);
	}
	
	public static int getDayDiff (final IWTimestamp startStamp,
																final IWTimestamp endStamp) {
		final Calendar startCalendar = new GregorianCalendar ();
		startCalendar.setTime (startStamp.getDate ());
		final Calendar endCalendar = new GregorianCalendar ();
		endCalendar.setTime (endStamp.getDate ());
		return getDayDiff (startCalendar, endCalendar);
	}
	
	public static int getDayDiff (final Calendar startCalendar,
																final Calendar endCalendar) {
		final int startYear = startCalendar.get (Calendar.YEAR);
		final int endYear = endCalendar.get (Calendar.YEAR);
		final int startDay = startCalendar.get (Calendar.DAY_OF_YEAR);
		final int endDay = endCalendar.get (Calendar.DAY_OF_YEAR)
				+ getYearBasedOffset (startYear, endYear);
		return endDay - startDay;
	}

	private static int getYearBasedOffset (final int startYear,
																				 final int endYear) {
		final int result;
		if (startYear < endYear) {
			final Calendar calendar = new GregorianCalendar (startYear, 0, 1);
			result = calendar.getActualMaximum (Calendar.DAY_OF_YEAR)
					+ getYearBasedOffset (startYear + 1, endYear);
		} else if (startYear > endYear) {
			result = -getYearBasedOffset (endYear, startYear);
		} else {
			result = 0;
		}
		return result;
	}
}
