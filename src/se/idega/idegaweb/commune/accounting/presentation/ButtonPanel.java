/*
 * $Id: ButtonPanel.java,v 1.6 2003/08/21 15:58:22 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.presentation;

import com.idega.presentation.Table;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.builder.data.IBPage;

/**
 * A class for button panels in Check & Peng application forms.
 * 
 * <p>
 * Last modified: $Date: 2003/08/21 15:58:22 $
 *
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $Revision: 1.6 $
 * @see ApplicationForm
 */
public class ButtonPanel extends AccountingBlock {

	private final static int COLUMN_WIDTH = 40;
	
	private Table table = null;
	private int buttonColumn = 1; 

	/**
	 * Constructs an empty button panel.
	 */
	public ButtonPanel() {
		table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		super.add(table);
	}

	/**
	 * Adds a localized and formatted submit button to the panel.
	 * The buttoms are added from left to right.
	 * @param parameter the form parameter name for the button
	 * @param textKey the text to localize
	 * @param defaultText the default localized text
	 */
	public void addButton(String parameter, String textKey, String defaultText) {
		SubmitButton button = new SubmitButton(parameter, localize(textKey, defaultText));
		addButton(button);
	}

	/**
	 * Adds a localized and formatted button to the panel with a window to open.
	 * The buttoms are added from left to right.
	 * @param parameter the form parameter name for the button
	 * @param textKey the text to localize
	 * @param defaultText the default localized text
	 * @param windowClass the class of the window to open when clicked
 	 */
	public void addButton(String parameter, String textKey, String defaultText, Class windowClass) {
		GenericButton button = new GenericButton(parameter, localize(textKey, defaultText));
		button.setWindowToOpen(windowClass);
		addButton (button);
	}

	/**
	 * Adds a localized and formatted button to the panel with a page to open.
	 * The buttoms are added from left to right.
	 * @param parameter the form parameter name for the button
	 * @param textKey the text to localize
	 * @param defaultText the default localized text
	 * @param page an IBPage to be opened when clicked
	 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
	 */
	public void addButton(String parameter, String textKey, String defaultText, IBPage page) {
		GenericButton button = new GenericButton(parameter, localize(textKey, defaultText));
		button.setPageToOpen(page);
		addButton(button);
	}

	/*
	 * Adds a button to the panel.
	 */
	private void addButton(GenericButton button) {
		button = getButton(button);
		table.add(button, buttonColumn , 1);
		buttonColumn++;
		table.setWidth(buttonColumn * COLUMN_WIDTH);
	}
}
