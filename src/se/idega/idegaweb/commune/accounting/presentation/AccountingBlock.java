/*
 * Created on Aug 18, 2003
 *
 */
package se.idega.idegaweb.commune.accounting.presentation;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;

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

}
