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

	public PlacementTimes(final IWTimestamp firstCheckDay, final IWTimestamp lastCheckDay, int specificNumberOfDays) {
		this.firstCheckDay = firstCheckDay;
		this.lastCheckDay = lastCheckDay;
		specificNumberOfDaysPrMonth = specificNumberOfDays;
	}

	public int getDays() {
		return 1 + AccountingUtil.getDayDiff(firstCheckDay, lastCheckDay);
	}

	public float getMonths() {
		if (isWholeNumberOfMonths()) {
			return getNumberOfMonths(daysInMonth(firstCheckDay), daysInMonth(lastCheckDay));
		}

		int daysInMonthFirstCheckDay = (specificNumberOfDaysPrMonth > 0) ? specificNumberOfDaysPrMonth : daysInMonth(firstCheckDay);
		int daysInMonthLastCheckDay = (specificNumberOfDaysPrMonth > 0) ? specificNumberOfDaysPrMonth : daysInMonth(lastCheckDay);
		
		return getNumberOfMonths(daysInMonthFirstCheckDay, daysInMonthLastCheckDay);
	}

	public IWTimestamp getLastCheckDay() {
		return lastCheckDay;
	}

	public IWTimestamp getFirstCheckDay() {
		return firstCheckDay;
	}

	private float getNumberOfMonths(int daysInMonthFirstCheckDay, int daysInMonthLastCheckDay) {
		float months = 1.0f + (lastCheckDay.getYear() * 12 + lastCheckDay.getMonth())
				- (firstCheckDay.getYear() * 12 + firstCheckDay.getMonth());

		// decrease with days before start date
		months -= (float) (firstCheckDay.getDay() - 1) / (float) daysInMonthFirstCheckDay;
		// decrease with days after end date
		months -= 1.0f - (float) lastCheckDay.getDay() / (float) daysInMonthLastCheckDay;

		return months;
	}

	/*
	 * A method that checks if the number of days between the two check dates is
	 * a whole number of months. Will return true if the difference between the
	 * dates + 1 is 0 or is equal to the number of days in the first month (this
	 * is true if we are handling only one month or multiple months, but the
	 * start date is not the 1st and the end date is not the last day of that
	 * month). Will alse return true if the first date is the 1st of some month
	 * and the last date is the last day of some other month.
	 * 
	 */
	private boolean isWholeNumberOfMonths() {
		int days = lastCheckDay.getDay() - firstCheckDay.getDay() + 1;
		if (days == 0 || days == daysInMonth(firstCheckDay)) {
			return true;
		}

		if (firstCheckDay.getDay() == 1 && (lastCheckDay.getDay() == daysInMonth(lastCheckDay))) {
			return true;
		}

		return false;
	}

	private int daysInMonth(final IWTimestamp date) {
		final IWTimestamp firstDay = new IWTimestamp(date);
		firstDay.setDay(1);
		final IWTimestamp lastDay = new IWTimestamp(firstDay);
		lastDay.addMonths(1);
		final int daysInMonth = AccountingUtil.getDayDiff(firstDay, lastDay);
		return daysInMonth;
	}
}