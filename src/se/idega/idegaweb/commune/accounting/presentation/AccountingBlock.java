/*
 * Created on Aug 18, 2003
 *
 */
package se.idega.idegaweb.commune.accounting.presentation;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;

import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;

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
	protected Text getLocalizedLabel(String textKey, String defaultText) {
		return getSmallHeader(localize(textKey, defaultText) + ":");
	}
	
	/**
	 * Returns a formatted and localized form text.
	 * @param textKey the text key to localize
	 * @param defaultText the default localized text
	 * @author anders
	 */
	public Text getLocalizedText(String textKey, String defaultText) {
		return getSmallText(localize(textKey, defaultText));
	}
	
	/**
	 * Returns a formatted and form text.
	 * @param text the text string for the Text object
	 * @author anders
	 */
	public Text getText(String text) {
		return getSmallText(text);
	}
	
	/**
	 * Returns a formatted text input.
	 * @param parameter the form parameter
	 * @param text the text to set
	 * @author anders
	 */
	protected TextInput getTextInput(String parameter, String text) {
		return (TextInput) getStyledInterface(new TextInput(parameter, text));
	}
	
	
	
	/**
	 * Returns a formatted text input with the specified width.
	 * @param parameter the form parameter
	 * @param text the text to set
	 * @param width the width of the text input
	 * @author anders
	 */
	protected TextInput getTextInput(String parameter, String text, int width) {
		TextInput ti = getTextInput(parameter, ""+text);
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
	protected TextInput getTextInput(String parameter, String text, int width, int size) {
		TextInput ti = getTextInput(parameter, text, width);
		ti.setSize(width);
		return ti;
	}
	
	/**
	 * Returns a formatted link.
	 * @param text the link text
	 * @param parameter the form parameter
	 * @param value the parameter value
	 * @author anders
	 */
	protected Link getLink(String text, String parameter, String value) {
		Link l = getSmallLink(text);
		l.addParameter(parameter, value);
		return l;
	}

	/**
	 * Returns a formatted and localized button.
	 * @param parameter the form parameter
	 * @param textKey the text key to localize
	 * @param defaultText the default localized text
	 * @author anders
	 */
	protected SubmitButton getLocalizedButton(String parameter, String textKey, String defaultText) {
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
		Date date = null;
		
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

		if (d != null) {
			date = validateDate(d, s);		 	
		}
		
		return date;
	}
	
	/**
	 * Formats the specified java.sql.Date object into a string.
	 * The length can be 4, 6, 8 or 10 characters resulting in the
	 * formats yyMM, yyMMdd, yyyyMMdd, yyyy-MM-dd.
	 * @param date the date object to format
	 * @param length the length of the formatted date
	 * @return the formatted string
	 * @author anders 
	 */
	protected String formatDate(Date date, int length) {
		if (date == null) {
			return "";
		}
		
		SimpleDateFormat formatter = null;
		
		if (length == 4) {
			formatter = new SimpleDateFormat ("yyMM"); 
		} else if (length == 6) {
			formatter = new SimpleDateFormat ("yyMMdd"); 
		} else if (length == 8) {
			formatter = new SimpleDateFormat ("yyyyMMdd"); 
		} else if (length == 10) {
			formatter = new SimpleDateFormat ("yyyy-MM-dd"); 
		}

		String dateString = "";
		
		if (formatter != null) {
			java.util.Date d = new java.util.Date(date.getTime());
			dateString = formatter.format(d);		
		}
		
		return dateString;
	}
	
	/*
	 * Returns a java.sqlDate object if s has valid date format.
	 */
	private Date validateDate(java.util.Date d, String s) {
		Date date = null;
		if (d != null) {
			date = new Date(d.getTime());
			String validate = null;
			if ((s.length() == 8) && (s.indexOf('-') != -1)) {
				SimpleDateFormat formatter = new SimpleDateFormat ("yy-MM-dd");
				validate = formatter.format(d);		
			} else {
				validate = formatDate(date, s.length());
			}
			if (!validate.equals(s)) {
				date = null;
			}
		}
		return date;
	}
	
	/**
	 * Formats the specified java.sql.Timestamp object into a string.
	 * The length can be 4, 6, 8 or 10 characters resulting in the
	 * formats yyMM, yyMMdd, yyyyMMdd, yyyy-MM-dd.
	 * @param timestamp the timestamp object to format
	 * @param length the length of the formatted date
	 * @return the formatted string
	 * @author anders 
	 */
	protected String formatDate(Timestamp timestamp, int length) {
		return formatDate(new Date(timestamp.getTime()), length);
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
	
	/**
	 * Returns the form parameter with the specified parameter name
	 * from the specified IWContext object as an integer. Returns -1 if
	 * the parameter is not set. 
	 * @param iwc the idegaWeb context object
	 * @param parameterName the name of the form parameter
	 * @author anders
	 */
	protected int getIntParameter(IWContext iwc, String parameterName) {
		int intValue = 0;
		String s = getParameter(iwc, parameterName);
		try {
			intValue = Integer.parseInt(s);
		} catch (NumberFormatException  e) {
			intValue = -1;
		}
		return intValue;
	}
	
	/**
	 * Returns a <code>DropdownMenu</code> that uses the given <code>Collection</code> of entities as options.
	 * @param name The form name for the returned <code>DropdownMenu</code>
	 * @param entities The entity beans to use as values.
 	 * @param methodName The name of the method from which the values are retrieved.
	 * @param defaultValue The default value to set if method returns null
	 * @return
	 */
	protected DropdownMenu getDropdownMenu(String name, Collection entities, String methodName) {
		SelectorUtility util = new SelectorUtility();
		DropdownMenu menu = (DropdownMenu) util.getSelectorFromIDOEntities(new DropdownMenu(name), entities, methodName);
		
		return menu;
	}

	/**
	 * Returns a <code>DropdownMenu</code> that uses the given <code>Collection</code> of entities as options where the
	 * value is a localization key.
	 * @param name The form name for the returned <code>DropdownMenu</code>
	 * @param entities The entity beans to use as values.
	 * @param methodName The name of the method from which the values are retrieved.
	 * @return
	 */
	protected DropdownMenu getDropdownMenuLocalized(String name, Collection entities, String methodName) {
		return getDropdownMenuLocalized(name, entities, methodName, null);
	}

	/**
	 * Returns a <code>DropdownMenu</code> that uses the given <code>Collection</code> of entities as options where the
	 * value is a localization key.
	 * @param name The form name for the returned <code>DropdownMenu</code>
	 * @param entities The entity beans to use as values.
	 * @param methodName The name of the method from which the values are retrieved.
	 * @param defaultValue The default value to set if method returns null
	 * @return
	 */
	protected DropdownMenu getDropdownMenuLocalized(String name, Collection entities, String methodName, String defaultValue) {
		SelectorUtility util = new SelectorUtility();
		DropdownMenu menu = (DropdownMenu) util.getSelectorFromIDOEntities(new DropdownMenu(name), entities, methodName, getResourceBundle(), defaultValue);
		
		return menu;
	}
	
	/**
	 * Returns a formatted check box.
	 * @param parameter the form parameter
	 * @param value the value to set
	 * @author aron
	 */
	protected CheckBox getCheckBox(String parameter, String value) {
		return (CheckBox) getStyledInterface(new CheckBox(parameter, value));
	}
}
