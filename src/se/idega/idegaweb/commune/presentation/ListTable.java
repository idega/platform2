/*
 * $Id: ListTable.java,v 1.3 2003/08/20 10:59:31 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.presentation;

import com.idega.presentation.PresentationObject;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;

/**
 * This class generates a list that uses the layout 
 * guide rules for Check & Peng.
 * <p>
 * Last modified: $Date: 2003/08/20 10:59:31 $
 *
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $Revision: 1.3 $
 */
public class ListTable extends PresentationObjectContainer {

	private int cols = 0;
	private int col = 1;
	private int row = 2;
	private CommuneBlock cb = null;
	private Table table = null;

	/**
	 * Constructs a ListTable with the specified number of columns.
	 */
	public ListTable(CommuneBlock cb, int cols) {
		this.cb = cb;
		this.cols = cols;
		this.table = new Table();
		table.setWidth(cb.getWidth());
		table.setCellpadding(cb.getCellpadding());
		table.setCellspacing(cb.getCellspacing());
		super.add(table);
	}

	/**
	 * Sets a header text label at the specified column.
	 * @param headerText the header text label to set
	 * @param col the header column for the label 
	 */
	public void setHeader(String headerText, int col) {
		// Check boundaries, null?
		Text t = cb.getSmallHeader(headerText);
		table.add(t, col, 1);
		table.setRowColor(1, cb.getHeaderColor());
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
	 * @param text the string for the text object to add
	 */
	public void add(String text){
		Text t = cb.getSmallText(text);
		add(t);
	}

	/**
	 * Adds (skips) an empty cell to the list.
	 * The column position is automatically increased and rows
	 * are automatically wrapped when the current column is full.
	 */
	public void skip(){
		col++;
		if (col > cols) {
			col = 1;
			row++;
			if(row%2==0){
				table.setRowColor(row, cb.getZebraColor1());
			} else {
				table.setRowColor(row, cb.getZebraColor2());
			}
		}
	}

	/**
	 * Adds (skips) an empty cell to the list.
	 * The column position is automatically increased and rows
	 * are automatically wrapped when the current column is full.
	 * @param nrOfColumns the number of columns to skip
	 */
	public void skip(int nrOfColumns){
		for(int i=0; i<nrOfColumns; i++){
	  		skip();
		}
	}
}
