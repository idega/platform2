/*
 * $Id: ListTable.java,v 1.1 2003/08/18 11:42:26 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.presentation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

/**
 * This class generates a list that uses the layout 
 * guide rules for Check & Peng.
 * <p>
 * Last modified: $Date: 2003/08/18 11:42:26 $
 *
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $Revision: 1.1 $
 */
public class ListTable extends Table {

	private int cols = 0;
	private List rowList = null;
	private List bottomRowList = null;
	private PresentationObject[] headerRow = null;
	private PresentationObject[] tempRow = null;
	private int tempCol = 0;
	private CommuneBlock cb = null;

	/**
	 * Constructs a ListTable with the specified number of columns.
	 */
	public ListTable(CommuneBlock cb, int cols) {
		this.cb = cb;
		this.cols = cols;
		headerRow = new PresentationObject[cols];
		rowList = new ArrayList();
		bottomRowList = new ArrayList();
		tempRow = new PresentationObject[cols];
		setWidth(cb.getWidth());
		setCellpadding(cb.getCellpadding());
		setCellspacing(cb.getCellspacing());
	}

	/**
	 * Sets a header text label at the specified column.
	 * @param headerText the header text label to set
	 * @param col the header column for the label 
	 */
	public void setHeader(String headerText,int col){
		// Check boundaries, null?
		Text t = cb.getSmallHeader(headerText);
		headerRow[col-1] = t;
	}

	/**
	 * Sets a header label object at the specified column.
	 * @param po the header label object to set
	 * @param col the header column for the label 
	 */
	public void setHeader(PresentationObject po,int col){
		// Check boundaries, null?
		headerRow[col-1] = po;
	}

	/**
	 * Adds a row with presentation objects.
	 * @param rowObjects the row of objects to add
	 */
	public void addRow(PresentationObject[] rowObjects){
		// Check boundaries, null?
		rowList.add(rowObjects);
	}

	/**
	 * Adds a row with text objects with default list font style.
	 * @param rowText the text strings for the row
	 */
	public void addRow(String[] rowTexts){
		// Check boundaries, null?
		PresentationObject[] rowObjects = new Text[this.cols];
		for(int i=0; i<cols; i++){
			String s = rowTexts[i];
			if(s!=null){
				Text t = cb.getSmallText(rowTexts[i]);
				rowObjects[i] = t;
			}
		}
		addRow(rowObjects);
  	}

	/**
	 * Adds a row with presentation objects at the bottom of the list.
	 * The background color for bottom rows will not be set (clear).
	 * @param rowObjects the presentation objects for the row
	 */
	public void addBottomRow(PresentationObject[] rowObjects){
		// Check boundaries, null
		bottomRowList.add(rowObjects);
  	}

	/**
	 * Adds a presentation object at the current row and column.
	 * The column position is automatically increased and rows
	 * are automatically wrapped when the current column is full.
	 * @param po the presentation object to add
	 */
	public void add(PresentationObject po){
		tempRow[tempCol] = po;
		tempCol++;
		if(tempCol==cols){
			addRow(tempRow);
			tempCol = 0;
			tempRow = new PresentationObject[this.cols];
		}
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
		tempCol++;
		if(tempCol==cols){
			addRow(tempRow);
			tempCol = 0;
			tempRow = new PresentationObject[this.cols];
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

	/**
	 * Builds the entire list table.
	 * This method must be called for the items in the list to appear.
	 *
	 */
	public void build() {
		int rows = 1 + rowList.size();
		int cols = this.cols;
		
		for(int col=1; col<=cols; col++){
			PresentationObject po = headerRow[col-1];
			if(po!=null){
				this.add(po,col,1);
	  		}
		}

		this.setRowColor(1, cb.getHeaderColor());

		for(int row=2; row<=rows; row++){
			PresentationObject[] rowObjects = (PresentationObject[])rowList.get(row-2);
	  		for(int col=1; col<=cols; col++){
				PresentationObject po = rowObjects[col-1];
				if(po!=null){
		  			this.add(po,col,row);
				}
				if(row%2==0){
					this.setRowColor(row, cb.getZebraColor1());
				} else {
					this.setRowColor(row, cb.getZebraColor2());
				}
	  		}
		}

		Iterator iter = bottomRowList.iterator();
		int row = rows+1;
		while(iter.hasNext()){
			PresentationObject[] rowObjects = (PresentationObject[])iter.next();
			for(int col=1; col<=cols; col++){
				PresentationObject po = rowObjects[col-1];
				if(po!=null){
					this.add(po,col,row);
				}
			}
			row++;
		}
	}
}

