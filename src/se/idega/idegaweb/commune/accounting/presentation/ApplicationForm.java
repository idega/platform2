/*
 * $Id: ApplicationForm.java,v 1.10 2003/08/28 10:31:08 aron Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;

/**
 * A generic form for Check & Peng presentation blocks.
 * 
 * <p>
 * Last modified: $Date: 2003/08/28 10:31:08 $
 *
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $Revision: 1.10 $
 */
public class ApplicationForm extends AccountingBlock {

	private Form form = null;
	private Table table = null;
	private AccountingBlock parent = null;
	
	/**
	 * Constructs an empty application form.
	 */
	public ApplicationForm(){
		form = new Form();
		table = new Table(1, 4);
		table.setWidth(getWidth());
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		form.add(table);
		super.add(form);
	}
	
	

	/**
	 * Constructs an empty application form.
	 */
	public ApplicationForm(AccountingBlock parent) {
		this();
		setParent(parent);
	}
	
	public void setParent(AccountingBlock parent){
		this.parent = parent;
	}

	/**
	 * Should not be used.
	 */
	public void add(PresentationObject po) {
		form.add(new Text("add() should not be used in application forms." +				" Use setTitle(), setSearchPanel(), setMainPanel() and setButtonPanel()."));
	}
	
	/**
	 * Sets a localized title for this application form.
	 * The title will appear at the top of the application form.
	 * @param textkey the text key for the title
	 * @param defaultText the default localized text for the title
	 */
	public void setLocalizedTitle(String textKey, String defaultText) {
		table.add(getHeader(localize(textKey, defaultText)), 1, 1);
		table.setRowColor(1, getHeaderColor());
		table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_CENTER);
	}

	/**
	 * Sets the search panel for this application form.
	 * The search panel will appear below title in the application form.
	 * @param searchPanel the table containing the search panel
	 */
	public void setSearchPanel(PresentationObject searchPanel) {
		table.add(searchPanel, 1, 2);
	}

	/**
	 * Sets the main panel for this application form.
	 * The main panel will appear below the search panel in the application form.
	 * @param mainPanel the table containing the main panel
	 */
	public void setMainPanel(Table mainPanel) {
		table.add(mainPanel, 1, 3);
	}

	/**
	 * Sets the main panel (a list table) for this application form.
	 * The main panel will appear below the search panel in the application form.
	 * @param mainPanel the list table to set
	 */
	public void setMainPanel(ListTable mainPanel) {
		table.add(mainPanel, 1, 3);
	}

	/**
	 * Sets the button panel for this application form.
	 * The button panel will appear at the bottom of the application form.
	 * @param buttonPanel the button panel to set
	 * @see ButtonPanel
	 */
	public void setButtonPanel(ButtonPanel buttonPanel) {
		table.add(buttonPanel, 1, 4);
	}
	
	/**
	 * Adds a hidden input to this application form.
	 * @param parameter the hidden input parameter name
	 * @param value the hidden input parameter va?ue
	 */
	public void addHiddenInput(String parameter, String value) {
		table.add(new HiddenInput(parameter, value), 1, 4);
	}
	
	/**
	 * Maintains the specified parameter in the form request
	 * @param parameterName
	 */
	public void maintainParameter(String parameterName){
		if(this.form!=null)
			this.form.maintainParameter(parameterName);
	}
	
	public String localize(String textKey, String defaultText) {
		if (parent != null) {
			return parent.localize(textKey, defaultText);
		} else {
			return defaultText;
		}
	}
	

}
 