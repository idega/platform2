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
	
	/**
	 * See documentation for: getDayDiff (Calendar, Calendar)
	 */	
	public static int getDayDiff (final Date startDate,
																final Date endDate) {
		final Calendar startCalendar = new GregorianCalendar ();
		startCalendar.setTime (startDate);
		final Calendar endCalendar = new GregorianCalendar ();
		endCalendar.setTime (endDate);
		return getDayDiff (startCalendar, endCalendar);
	}

	/**
	 * See documentation for: getDayDiff (Calendar, Calendar)
	 */	
	public static int getDayDiff (final IWTimestamp startStamp,
																final IWTimestamp endStamp) {
		final Calendar startCalendar = new GregorianCalendar ();
		startCalendar.setTime (startStamp.getDate ());
		final Calendar endCalendar = new GregorianCalendar ();
		endCalendar.setTime (endStamp.getDate ());
		return getDayDiff (startCalendar, endCalendar);
	}
	
	/**
	 * Evaluates the differens in days between to dates. This method is
	 * independent of dayligt saving effects of different local calendars. It also
	 * handles leap years correctly, as well as spans over different years.
	 * Se examples below to understand the algorithm:
	 * <pre>
	 * 2003-dec-31 : 2004-jan-01 =   1
	 * 2004-mar-11 : 2004-mar-31 =  20
	 * 2003-dec-31 : 2005-jan-01 = 367
	 * 2004-jan-01 : 2003-dec-31 =  -1
	 * 2004-jan-01 : 2004-jan-01 =   0
	 * 2002-dec-31 : 2004-jan-01 = 366
	 * </pre>
	 */
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

	public static void main (final String[] arg) {
		final Calendar c1 = new GregorianCalendar (2004, 0, 1);
		final Calendar c2 = new GregorianCalendar (2003, 11, 31);
		final Calendar c3 = new GregorianCalendar (2004, 2, 11);
		final Calendar c4 = new GregorianCalendar (2004, 2, 31);
		final Calendar c5 = new GregorianCalendar (2005, 0, 1);
		final Calendar c6 = new GregorianCalendar (2002, 11, 31);

		final java.text.DateFormat df = java.text.DateFormat.getDateInstance ();

		System.out.println (df.format (new Date (c2.getTimeInMillis())) + " : " + df.format (new Date (c1.getTimeInMillis ())) + " = " + getDayDiff (c2, c1));
		System.out.println (df.format (new Date (c3.getTimeInMillis())) + " : " + df.format (new Date (c4.getTimeInMillis())) + " = " + getDayDiff (c3, c4));
		System.out.println (df.format (new Date (c2.getTimeInMillis())) + " : " + df.format (new Date (c5.getTimeInMillis())) + " = " + getDayDiff (c2, c5));
		System.out.println (df.format (new Date (c1.getTimeInMillis())) + " : " + df.format (new Date (c2.getTimeInMillis())) + " = " + getDayDiff (c1, c2));
		System.out.println (df.format (new Date (c1.getTimeInMillis())) + " : " + df.format (new Date (c1.getTimeInMillis())) + " = " + getDayDiff (c1, c1));
		System.out.println (df.format (new Date (c6.getTimeInMillis())) + " : " + df.format (new Date (c1.getTimeInMillis())) + " = " + getDayDiff (c6, c1));
	}
}
