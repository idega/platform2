/*
 * $Id: ListTable.java,v 1.18 2003/10/10 11:56:07 kjell Exp $
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

/**
 * This class generates a list that uses the layout 
 * guide rules for Check & Peng.
 * <p>
 * Last modified: $Date: 2003/10/10 11:56:07 $
 *
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $Revision: 1.18 $
 */
public class ListTable extends AccountingBlock {

	private int cols = 0;
	private int col = 1;
	private int row = 2;
	private Table table = null;
	private AccountingBlock parent = null;

	/**
	 * Constructs a ListTable with the specified number of columns.
	 */
	public ListTable(AccountingBlock parent, int cols) {
		this.parent = parent;
		this.cols = cols;
		this.table = new Table();
		table.setWidth(getWidth());
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		super.add(table);
	}

	/**
	 * Sets a localized header text label at the specified column.
	 * @param textKey the text key for the title
	 * @param defaultText the default text for the title
	 * @param col the header column for the label 
	 */
	public void setLocalizedHeader(String textKey, String defaultText, int col) {
		// Check boundaries, null?
		Text t = getSmallHeader(localize(textKey, defaultText));
		table.add(t, col, 1);
		table.setRowColor(1, getHeaderColor());
		table.setNoWrap(col, 1);
	}

	/**
	 * Sets a Header text label at the specified column.
	 * @param defaultText the default text for the title
	 * @param col the header column for the label 
	 */
	public void setHeader(String defaultText, int col) {
		// Check boundaries, null?
		Text t = getSmallHeader(defaultText);
		table.add(t, col, 1);
		table.setRowColor(1, getHeaderColor());
		table.setNoWrap(col, 1);
	}


	/**
	 * Sets a header label object at the specified column.
	 * @param po the header label object to set
	 * @param col the header column for the label 
	 */
	public void setHeader(PresentationObject po, int col) {
		// Check boundaries, null?
		table.add(po, col, 1);
		table.setRowColor(1, getHeaderColor());
		table.setNoWrap(col, 1);
	}

	/**
	 * Sets the width for the specified column.
	 * @param column the column position
	 * @param width the width in pixels or percent
	 */
	public void setColumnWidth(int column, String width) {
		table.setColumnWidth(column, width);
	}

	/**
	 * Sets the horisontal alignment for the specified column.
	 * @param column the column position
	 * @param alignment code
	 */
	public void setColumnAlignment(int column, String align) {
		table.setColumnAlignment(column, align);
	}
	
	/**
	 * Adds a presentation object at the current row and column.
	 * The column position is automatically increased and rows
	 * are automatically wrapped when the current column is full.
	 * @param po the presentation object to add
	 */
	public void add(PresentationObject po) {
		_add(po);
	}

	/**
	 * Adds a text object at the current row and column with default font style.
	 * The column position is automatically increased and rows
	 * are automatically wrapped when the current column is full.
	 * @param text the string to add as a text object
	 */
	public void add(String text) {
		if (text == null) {
			text = "";
		}
		Text t = getSmallText(text);
		_add(t);
	}

	/**
	 * Adds a localized text object at the current row and column with default font style.
	 * The column position is automatically increased and rows
	 * are automatically wrapped when the current column is full.
	 * @param textKey the text key for the text object to add
	 * @param defaultKey the default localized text for the text object to add
	 */
	public void add(String textKey, String defaultText) {
		_add(getSmallText(localize(textKey, defaultText)));
	}

	/**
	 * Adds an integer text object current row and column with default font style.
	 * The column position is automatically increased and rows
	 * are automatically wrapped when the current column is full.
	 * @param intValue the integer to add as a text object
	 */
	public void add(int intValue) {
		Text t = getSmallText("" + intValue);
		_add(t);
	}

	/**
	 * Adds (skips) an empty cell to the list.
	 * The column position is automatically increased and rows
	 * are automatically wrapped when the current row is full.
	 */
	public void skip(){
		col++;
		if (col > cols) {
			col = 1;
			if(row%2==0){
				table.setRowColor(row, getZebraColor1());
			} else {
				table.setRowColor(row, getZebraColor2());
			}
			row++;
		}
	}

	/**
	 * Adds (skips) an empty cell to the list.
	 * The column position is automatically increased and rows
	 * are automatically wrapped when the current row is full.
	 * @param nrOfColumns the number of columns to skip
	 */
	public void skip(int nrOfColumns){
		for(int i=0; i<nrOfColumns; i++){
	  		skip();
		}
	}
	
	private void _add(PresentationObject po) {
		table.add(po, col, row);
		table.setNoWrap(col, row);
		skip();
	}
	
	public String localize(String textKey, String defaultText) {
		if (parent != null) {
			return parent.localize(textKey, defaultText);
		} else {
			return defaultText;
		}
	}
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.presentation.AccountingBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
	}

}
