/*
 * $Id: ListTable.java,v 1.6 2003/08/24 06:50:02 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.presentation;

import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;

/**
 * This class generates a list that uses the layout 
 * guide rules for Check & Peng.
 * <p>
 * Last modified: $Date: 2003/08/24 06:50:02 $
 *
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $Revision: 1.6 $
 */
public class ListTable extends AccountingBlock {

	private int cols = 0;
	private int col = 1;
	private int row = 2;
	private Table table = null;

	/**
	 * Constructs a ListTable with the specified number of columns.
	 */
	public ListTable(int cols) {
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
	}

	/**
	 * Sets a header label object at the specified column.
	 * @param po the header label object to set
	 * @param col the header column for the label 
	 */
	public void setHeader(PresentationObject po, int col) {
		// Check boundaries, null?
		table.add(po, col, 1);
	}

	/**
	 * Adds a presentation object at the current row and column.
	 * The column position is automatically increased and rows
	 * are automatically wrapped when the current column is full.
	 * @param po the presentation object to add
	 */
	public void add(PresentationObject po) {
		table.add(po, col, row);
		skip();
	}

	/**
	 * Adds a text object at the current row and column with default font style.
	 * The column position is automatically increased and rows
	 * are automatically wrapped when the current column is full.
	 * @param text the string to add as a text object
	 */
	public void add(String text) {
		Text t = getSmallText(text);
		add(t);
	}

	/**
	 * Adds a localized text object at the current row and column with default font style.
	 * The column position is automatically increased and rows
	 * are automatically wrapped when the current column is full.
	 * @param textKey the text key for the text object to add
	 * @param defaultKey the default localized text for the text object to add
	 */
	public void add(String textKey, String defaultText) {
		add(localize(textKey, defaultText));
	}

	/**
	 * Adds an integer text object current row and column with default font style.
	 * The column position is automatically increased and rows
	 * are automatically wrapped when the current column is full.
	 * @param intValue the integer to add as a text object
	 */
	public void add(int intValue) {
		Text t = getSmallText("" + intValue);
		add(t);
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
}
