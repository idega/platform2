package is.idega.idegaweb.project.presentation;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;

/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public abstract class AbstractContentList extends Block {

  protected Table table = null;

  protected int extraRowsBefore = 0;
  protected int extraRowsAfter = 0;
  protected int minimumNumberOfRows = 8;

  protected int cellspacing = 0;
  protected int cellpadding = 0;
  protected boolean linesBeetween = true;
  protected boolean bottomLine = false;
  protected boolean topLine = false;

  protected String sebracolor1 = "#FFFFFF";
  protected String sebracolor2 = "#CCCCCC";
  protected String selectedColor = "#E9E9B7";
  protected String lineColor = "#333333";

  protected String width = "181";
  protected String rowHeight = "20";

  protected Vector rowColors = null;


  public synchronized Object clone(){
    AbstractContentList obj = (AbstractContentList)super.clone();
    if(table != null){
      obj.table = (Table)this.table.clone();
      obj.empty();
      obj.add(obj.table);
    }

    obj.extraRowsBefore = this.extraRowsBefore;
    obj.extraRowsAfter = this.extraRowsAfter;
    obj.minimumNumberOfRows = this.minimumNumberOfRows;

    obj.cellspacing = this.cellspacing;
    obj.cellpadding = this.cellpadding;
    obj.linesBeetween = this.linesBeetween;
    obj.bottomLine = this.bottomLine;
    obj.topLine = this.topLine;

    obj.sebracolor1 = this.sebracolor1;
    obj.sebracolor2 = this.sebracolor2;
    obj.selectedColor = this.selectedColor;
    obj.lineColor = this.lineColor;

    obj.width = this.width;
    obj.rowHeight = this.rowHeight;

    return obj;
  }

  public AbstractContentList() {
    super();
    rowColors = new Vector();
    table = new Table();
    this.add(table);
  }

  public void setMinimumNumberOfRows(int number){
    minimumNumberOfRows = number;
  }

  public void setSebraColor(String color1, String color2){
    sebracolor1 = color1;
    sebracolor2 = color2;
  }

  public void setRowColor(String color){
    sebracolor1 = color;
    sebracolor2 = color;
  }

  public void setLineColor(String color){
    lineColor = color;
  }

  public void setWidth(String width){
    this.width = width;
  }

  public void setRowHeight(String rowHeight){
    this.rowHeight = rowHeight;
  }

  public void setSelectedColor(String color){
    selectedColor = color;
  }

  public void setRowColor(int row, String color){
    rowColors.add(Integer.toString(row));
    rowColors.add(color);
  }


  public void add(PresentationObject prObject, int xpos, int ypos){
    table.add(prObject,xpos,ypos);
  }


  public abstract List getEntityList(IWContext iwc) throws Exception;
  public abstract PresentationObject getObjectToAddToColumn(int colIndex, int rowIndex, Object item, IWContext iwc, boolean beforeEntities)throws Exception;
  public abstract void initColumns(IWContext iwc) throws Exception;

  public void initializeInMain(IWContext iwc) throws Exception{
    super.initializeInMain(iwc);
    this.initColumns(iwc);
  }

  public void main(IWContext iwc) throws Exception {
    table.empty();
    List objectList = getEntityList(iwc);
    int rowIndex = 1;
    if( objectList != null && objectList.size() > 0){
      table.resize(table.getColumns(),Math.max(objectList.size()+extraRowsBefore+extraRowsAfter,minimumNumberOfRows));

      for (int i = 0; i < extraRowsBefore; i++) {
        for (int j = 1; j <= table.getColumns(); j++) {
          table.add(getObjectToAddToColumn(j,rowIndex,null,iwc,true),j,rowIndex);
        }
        rowIndex++;
      }

      ListIterator lIter = objectList.listIterator();
      while (lIter.hasNext()) {
        Object lItem = (Object)lIter.next();
        for (int j = 1; j <= table.getColumns(); j++) {
          table.add(getObjectToAddToColumn(j,rowIndex,lItem,iwc,false),j,rowIndex);
        }
        rowIndex++;
      }
      table.setHorizontalZebraColored(this.sebracolor1,this.sebracolor2);

    } else {
      int min = Math.max(extraRowsBefore+extraRowsAfter,minimumNumberOfRows);
      table.resize(table.getColumns(),Math.max(1,min));

      for (int i = 0; i < extraRowsBefore; i++) {
        for (int j = 1; j <= table.getColumns(); j++) {
          table.add(getObjectToAddToColumn(j,rowIndex,null,iwc,true),j,rowIndex);
        }
        rowIndex++;
      }
      table.setHorizontalZebraColored(this.sebracolor1,this.sebracolor2);
    }

    for (int i = 0; i < extraRowsAfter; i++) {
      for (int j = 1; j <= table.getColumns(); j++) {
        table.add(getObjectToAddToColumn(j,rowIndex,null,iwc,false),j,rowIndex);
      }
      rowIndex++;
    }

    table.setCellpadding(this.cellpadding);
    table.setCellspacing(this.cellspacing);
    table.setLinesBetween(linesBeetween);
    table.setBottomLine(bottomLine);
    table.setTopLine(topLine);
    table.setLineColor(lineColor);
    table.setWidth(this.width);

    Iterator iter = rowColors.iterator();
    while (iter.hasNext()) {
      String row = (String)iter.next();
      if(iter.hasNext()){
        String color = (String)iter.next();
        table.setRowColor(Integer.parseInt(row),color);
      }
    }


    for (int i = 1; i <= table.getRows(); i++) {
      table.setHeight(i,rowHeight);
    }




    //super._main(iwc);
  }


  public void setColumns(int cols){
    table.resize(cols,table.getRows());
  }

  public void setColumnWidth(int col, String width){
    table.setWidth(col,width);
  }

  public void setColumnHorizontalAlignemnt(int col, String alignment){
    int rows = table.getRows();
    for (int i = 1; i <= rows; i++) {
      table.setAlignment(col,i,alignment);
    }
  }

  public void setColumnVerticalAlignemnt(int col, String alignment){
    int rows = table.getRows();
    for (int i = 1; i <= rows; i++) {
      table.setVerticalAlignment(col+1,i,alignment);
    }
  }


  public void setExtraRowsAtBeginning(int numberOfRows){
    extraRowsBefore = numberOfRows;
  }

  public void setExtraRowsAtEnding(int numberOfRows){
    extraRowsAfter = numberOfRows;
  }

  public void setLinesBetween(boolean value){
    linesBeetween = value;
  }



}

