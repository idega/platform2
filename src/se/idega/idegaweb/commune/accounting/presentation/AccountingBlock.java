/*
 * Created on Aug 18, 2003
 *
 */
package se.idega.idegaweb.commune.accounting.presentation;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;

import se.idega.idegaweb.commune.presentation.CommuneBlock;

/**
 * AccountingBlock a super class of all blocks in the accounting framework
 * @author aron 
 * @version 1.0
 */

public class AccountingBlock extends CommuneBlock {
	
	public final static String IW_ACCOUNTING_BUNDLE_IDENTIFER = "se.idega.idegaweb.commune.accounting";
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_ACCOUNTING_BUNDLE_IDENTIFER;
	}
	
	/**
	 * Gets the common number format for the current locale
	 */
	public NumberFormat getNumberFormat(Locale locale){
		return NumberFormat.getInstance(locale);
	}
	
	/**
	 * Gets the common short date format for the given locale
	 */
	public DateFormat getShortDateFormat(Locale locale){
		return DateFormat.getDateInstance(DateFormat.SHORT,locale);
	}
	
	/**
	 * Gets the common long date format for the given locale
	 */
	public DateFormat getLongDateFormat(Locale locale){
		return DateFormat.getDateInstance(DateFormat.LONG,locale);
	}
	
	/**
	 * Gets the common  date-time-format for the given locale
	 */
	public DateFormat getDateTimeFormat(Locale locale){
		return DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,locale);
	}

}
