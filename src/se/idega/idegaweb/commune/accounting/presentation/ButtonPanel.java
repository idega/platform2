/*
 * $Id: ButtonPanel.java,v 1.1 2003/08/19 10:22:15 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.presentation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

/**
 * A class for button panels in Check & Peng application forms.
 * 
 * <p>
 * Last modified: $Date: 2003/08/19 10:22:15 $
 *
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $Revision: 1.1 $
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
	 * Adds a button to the panel.
	 * The buttoms are added from left to right.
	 * @param label the text label for the button
	 * @param parameter the form parameter name for the button
	 */
	public void addButton(String label, String parameter) {
		SubmitButton button = new SubmitButton(label, parameter);
		button.setAsImageButton(true);
		table.add(button, buttonColumn , 1);
		buttonColumn++;
		table.setWidth(buttonColumn * COLUMN_WIDTH);
	}

	/**
	 * Adds a button to the panel.
	 * The buttoms are added from left to right.
	 * @param label the text label for the button
	 * @param parameter the form parameter name for the button
	 * @param value the parameter value for the button
	 */
	public void addButton(String label, String parameter, String value) {
		SubmitButton button = new SubmitButton(label, parameter, value);
		button.setAsImageButton(true);
		table.add(button, buttonColumn , 1);
		buttonColumn++;
		table.setWidth(buttonColumn * COLUMN_WIDTH);
	}
}
