package se.idega.idegaweb.commune.accounting.business;

import com.idega.util.IWTimestamp;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Last modified: $Date: 2004/03/10 08:11:11 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.4 $
 */
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
	 * Evaluates the difference in days between to dates. This method is aware of
	 * some obstacles, otherwise easy forgotten:
	 * <ul>
	 * <li> Daylight saving effects - days could be 23, 24 or 25 hours<li/>
	 * <li> Leap years - years could be 365 or 366 days<li/>
	 * <li> Time differences - there's one day between 23:55:55 and 00:05:05 next
	 * day, even though only 10 minutes have passed</li>
	 * <li> Negative intervals - if endCalendar is before startCalendar, then a 
	 * negative result will be returned</li>
	 * <li> Intervals spanning over new years</li>
	 * <ul/>
	 * <p/>
	 * See examples below to understand the algorithm:
	 * <pre>
	 * 2003-dec-31 : 2004-jan-01 =   1
	 * 2004-mar-11 : 2004-mar-31 =  20
	 * 2003-dec-31 : 2005-jan-01 = 367
	 * 2004-jan-01 : 2003-dec-31 =  -1
	 * 2004-jan-01 : 2004-jan-01 =   0
	 * 2002-dec-31 : 2004-jan-01 = 366
	 * </pre>
	 *
	 * @param startCalendar the first day of the interval
	 * @param endCalendar the last day of the interval
	 * @return subtracted value between day numbers - positive, zero or negative
	 */
	public static int getDayDiff (final Calendar startCalendar,
																final Calendar endCalendar) {
		final int startYear = startCalendar.get (Calendar.YEAR);
		final int endYear = endCalendar.get (Calendar.YEAR);
		final int yearBasedOffset = getYearBasedOffset (startYear, endYear);
		final int startDay = startCalendar.get (Calendar.DAY_OF_YEAR);
		final int endDay = endCalendar.get (Calendar.DAY_OF_YEAR) + yearBasedOffset;
		return endDay - startDay;
	}

	/**
	 * Gets the offset in days between to different years, i.e. the number of
	 * days in each year in which new year limit is in the interval. This method
	 * is aware of leap years.
	 * <p/>
	 * See examples below to understand the algorithm:
	 * <pre>
	 * 2003 : 2004 =  365
	 * 2004 : 2004 =    0
	 * 2003 : 2005 =  731
	 * 2004 : 2003 = -365
	 * 2004 : 2004 =    0
	 * 2002 : 2004 =  730
	 * </pre>
	 *
	 * @param startYear the first year of the interval
	 * @param endYear the last year of the interval
	 * @return offset in days in the interval
	 */
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

	/**
	 * This main is only for testing. It's never invoked in IdegaWeb.
	 */
	public static void main (final String[] arg) {
		final Calendar c1 = new GregorianCalendar (2004, 0, 1);
		final Calendar c2 = new GregorianCalendar (2003, 11, 31);
		final Calendar c3 = new GregorianCalendar (2004, 2, 11);
		final Calendar c4 = new GregorianCalendar (2004, 2, 31);
		final Calendar c5 = new GregorianCalendar (2005, 0, 1);
		final Calendar c6 = new GregorianCalendar (2002, 11, 31);

		final java.text.DateFormat df = java.text.DateFormat.getDateInstance ();

		// testing getDayDiff
		System.out.println (df.format (new Date (c2.getTimeInMillis())) + " : " + df.format (new Date (c1.getTimeInMillis ())) + " = " + getDayDiff (c2, c1));
		System.out.println (df.format (new Date (c3.getTimeInMillis())) + " : " + df.format (new Date (c4.getTimeInMillis())) + " = " + getDayDiff (c3, c4));
		System.out.println (df.format (new Date (c2.getTimeInMillis())) + " : " + df.format (new Date (c5.getTimeInMillis())) + " = " + getDayDiff (c2, c5));
		System.out.println (df.format (new Date (c1.getTimeInMillis())) + " : " + df.format (new Date (c2.getTimeInMillis())) + " = " + getDayDiff (c1, c2));
		System.out.println (df.format (new Date (c1.getTimeInMillis())) + " : " + df.format (new Date (c1.getTimeInMillis())) + " = " + getDayDiff (c1, c1));
		System.out.println (df.format (new Date (c6.getTimeInMillis())) + " : " + df.format (new Date (c1.getTimeInMillis())) + " = " + getDayDiff (c6, c1));

		// testing getYearBasedOffset
		System.out.println (2003 + " : " + 2004 + " = " + getYearBasedOffset (2003, 2004));
		System.out.println (2004 + " : " + 2004 + " = " + getYearBasedOffset (2004, 2004));
		System.out.println (2003 + " : " + 2005 + " = " + getYearBasedOffset (2003, 2005));
		System.out.println (2004 + " : " + 2003 + " = " + getYearBasedOffset (2004, 2003));
		System.out.println (2004 + " : " + 2004 + " = " + getYearBasedOffset (2004, 2004));
		System.out.println (2002 + " : " + 2004 + " = " + getYearBasedOffset (2002, 2004));
	}
}
