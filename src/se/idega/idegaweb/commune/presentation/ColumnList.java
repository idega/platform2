package se.idega.idegaweb.commune.presentation;

import java.util.*;

import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Anders Lindman
 * @version 1.0
 */

public class ColumnList extends CommuneBlock {

  private static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune.presentation";

  private int cols = 0;
  private List rowList = null;
  private List bottomRowList = null;
  private PresentationObject[] headerRow = null;
  private PresentationObject[] tempRow = null;
  private int tempCol = 0;

  /**
   * Constructs a ListTable with the specified number of columns.
   */
  public ColumnList(int cols) {
    this.cols = cols;
    headerRow = new PresentationObject[cols];
    rowList = new ArrayList();
    bottomRowList = new ArrayList();
    tempRow = new PresentationObject[cols];
    setWidth("100%");
  }

  public void setHeader(String headerText,int col){
    // Check boundaries, null?
    Text t = new Text(headerText);
    t.setFontStyle(getListHeaderFontStyle());
    headerRow[col-1] = t;
  }

  public void setHeader(PresentationObject po,int col){
    // Check boundaries, null?
    headerRow[col-1] = po;
  }

  public void addRow(PresentationObject[] rowObjects){
    // Check boundaries, null?
    rowList.add(rowObjects);
  }

  public void addRow(String[] rowTexts){
    // Check boundaries, null?
    PresentationObject[] rowObjects = new Text[this.cols];
    for(int i=0; i<cols; i++){
      String s = rowTexts[i];
      if(s!=null){
        Text t = new Text(rowTexts[i]);
        t.setFontStyle(getListFontStyle());
        rowObjects[i] = t;
      }
    }
    addRow(rowObjects);
  }

  public void addBottomRow(PresentationObject[] rowObjects){
    // Check boundaries, null?
    bottomRowList.add(rowObjects);
  }

  public void add(PresentationObject po){
    if(po.getClass().getName().equals("com.idega.presentation.text.Link")){
      ((Link) po).setFontStyle(getListLinkFontStyle());
    }
    tempRow[tempCol] = po;
    tempCol++;
    if(tempCol==cols){
      addRow(tempRow);
      tempCol = 0;
      tempRow = new PresentationObject[this.cols];
    }
  }

  public void add(String text){
    Text t = new Text(text);
    t.setFontStyle(getListFontStyle());
    add(t);
  }

  public void skip(){
    tempCol++;
    if(tempCol==cols){
      addRow(tempRow);
      tempCol = 0;
      tempRow = new PresentationObject[this.cols];
    }
  }

  public void skip(int nrOfColumns){
    for(int i=0; i<nrOfColumns; i++){
      skip();
    }
  }

  public void main(IWContext iwc)throws Exception{
    super.add(createListTable(iwc));
  }

  private Table createListTable(IWContext iwc){
    IWResourceBundle iwrb = this.getResourceBundle(iwc);

    int rows = 1 + rowList.size();
    int cols = this.cols;
    Table t = new Table(cols,rows+bottomRowList.size());
    t.setCellpadding(3);
    t.setCellspacing(0);
    t.setWidth(getWidth());

    for(int col=1; col<=cols; col++){
      PresentationObject po = headerRow[col-1];
      if(po!=null){
        t.add(po,col,1);
      }
    }

    for(int row=2; row<=rows; row++){
      PresentationObject[] rowObjects = (PresentationObject[])rowList.get(row-2);
      for(int col=1; col<=cols; col++){
        PresentationObject po = rowObjects[col-1];
        if(po!=null){
          t.add(po,col,row);
        }
        if(row%2==0){
          t.setRowColor(row, getBackgroundColor());
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
          t.add(po,col,row);
        }
      }
      row++;
    }
    return t;
  }
}
