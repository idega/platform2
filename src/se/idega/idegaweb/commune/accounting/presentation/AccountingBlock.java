/*
 * Created on Aug 18, 2003
 *
 */
package se.idega.idegaweb.commune.accounting.presentation;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import java.util.Locale;
import java.sql.Date;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.text.Text;

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

	/**
	 * Returns a formatted and localized form label.
	 * @param textKey the text key to localize
	 * @param defaultText the default localized text
	 * @author anders
	 */
	protected Text getFormLabel(String textKey, String defaultText) {
		return getSmallHeader(localize(textKey, defaultText) + ":");
	}
	
	/**
	 * Returns a formatted and localized form text.
	 * @param textKey the text key to localize
	 * @param defaultText the default localized text
	 * @author anders
	 */
	protected Text getFormText(String textKey, String defaultText) {
		return getSmallText(localize(textKey, defaultText));
	}
	
	/**
	 * Returns a formatted text input.
	 * @param parameter the form parameter
	 * @param text the text to set
	 * @author anders
	 */
	protected TextInput getFormTextInput(String parameter, String text) {
		return (TextInput) getStyledInterface(new TextInput(parameter, text));
	}
	
	/**
	 * Returns a formatted text input with the specified width.
	 * @param parameter the form parameter
	 * @param text the text to set
	 * @param width the width of the text input
	 * @author anders
	 */
	protected TextInput getFormTextInput(String parameter, String text, int width) {
		TextInput ti = getFormTextInput(parameter, text);
		ti.setWidth("" + width);
		return ti;
	}
	
	/**
	 * Returns a formatted text input with the specified width and size.
	 * @param parameter the form parameter
	 * @param text the text to set
	 * @param width the width of the text input
	 * @param size the number of character in the text input
	 * @author anders
	 */
	protected TextInput getFormTextInput(String parameter, String text, int width, int size) {
		TextInput ti = getFormTextInput(parameter, text, width);
		ti.setSize(width);
		return ti;
	}
		
	/**
	 * Returns a formatted and localized button.
	 * @param parameter the form parameter
	 * @param textKey the text key to localize
	 * @param defaultText the default localized text
	 * @author anders
	 */
	protected SubmitButton getFormButton(String parameter, String textKey, String defaultText) {
		return getSubmitButton(new SubmitButton(parameter, localize(textKey, defaultText)));
	}

	/**
	 * Sets the style for the specified button.
	 * @param button the submit button to stylize
	 * @author anders
	 */
	protected SubmitButton getSubmitButton(SubmitButton button) {
		button.setHeight("20");
		return (SubmitButton) setStyle(button,STYLENAME_INTERFACE_BUTTON);
	}
	
	/**
	 * Parses the specified string to a java.sql.Date object. 
	 * The date formats yyMM, yyMMdd, yyyyMMdd, yy-MM-dd, yyyy-MM-dd are accepted.
	 * @param dateString the date string to parse
	 * @author anders
	 */
	protected Date parseDate(String dateString) {
		if (dateString == null) {
			return null;
		}
		
		SimpleDateFormat formatter = null;
		ParsePosition pos = null;
		java.util.Date d = null;
		String s = dateString.trim();
		
		if ((d == null) && (s.length() == 4)) {
			pos = new ParsePosition(0);
			formatter = new SimpleDateFormat ("yyMM");
			d = formatter.parse(s, pos);
		}
		if ((d == null) && (s.length() == 6)) {
			pos = new ParsePosition(0);
			formatter = new SimpleDateFormat ("yyMMdd");
			d = formatter.parse(s, pos);
		}
		if ((d == null) && (s.length() == 8) && (s.indexOf('-') == -1)) {
			pos = new ParsePosition(0);
			formatter = new SimpleDateFormat ("yyyyMMdd");
			d = formatter.parse(s, pos);
		}
		if ((d == null) && (s.length() == 8)) {
			pos = new ParsePosition(0);
			formatter = new SimpleDateFormat ("yy-MM-dd");
			d = formatter.parse(s, pos);
		}
		if ((d == null) && (s.length() == 10)) {
			pos = new ParsePosition(0);
			formatter = new SimpleDateFormat ("yyyy-MM-dd");
			d = formatter.parse(s, pos);
		}
				
		Date date = null;
		
		if (d != null) {
			date = new Date(d.getTime());
		}
		 	
		return date;
	}
	
	/**
	 * Formats the specified java.sql.Date object into a string.
	 * The length can be 4, 6 or 8 characters resulting in the
	 * formats yyMM, yyMMdd or yyyyMMdd.
	 * @param date the date object to format
	 * @param length the length of the formatted date
	 * @return the formatted string
	 * @author anders 
	 */
	protected String formatDate(Date date, int length) {
		if (date == null) {
			return "null";
		}
		
		SimpleDateFormat formatter = null;
		
		if (length == 4) {
			formatter = new SimpleDateFormat ("yyMM"); 
		} else if (length == 6) {
			formatter = new SimpleDateFormat ("yyMMdd"); 
		} else if (length == 8) {
			formatter = new SimpleDateFormat ("yyyyMMdd"); 
		}

		String dateString = "";
		
		if (formatter != null) {
			java.util.Date d = new java.util.Date(date.getTime());
			dateString = formatter.format(d);		
		}
		
		return dateString;
	}
	
	/**
	 * Returns the form parameter with the specified parameter name
	 * from the specified IWContext object. Returns an empty string
	 * if the parameter is not set instead of null. 
	 * @param iwc the idegaWeb context object
	 * @param parameterName the name of the form parameter
	 * @author anders
	 */
	protected String getParameter(IWContext iwc, String parameterName) {
		String p = iwc.getParameter(parameterName);
		if (p == null) {
			p = "";
		}
		return p;
	}
}
