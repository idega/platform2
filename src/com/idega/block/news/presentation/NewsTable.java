package com.idega.block.news.presentation;

import com.idega.presentation.*;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

import java.util.*;

public class NewsTable extends PresentationObjectContainer {

  public static final int SINGLE_FILE_LAYOUT = 1;
  public static final int NEWS_SITE_LAYOUT = 2;
  public static final int NEWS_PAPER_LAYOUT = 3;
  private int iLayout = NEWS_SITE_LAYOUT;
  private int iObjectCount = 0;
  private int iUndividedCount = 1;
  private int iDividedColumnCount = 2;
  private int iPlannedObjectCount = 1;

  private int linejump = 1;
  protected int tableRows;
  protected int tableColumns;
  protected int rowToAddIn;
  protected int colToAddIn;
	private int cellPadding = 0;
	private int cellSpacing = 0;
	private String firstColor = null;
	private String secondColor = null;
	private String color = "";

  private String sAlign = "left";
	boolean zebracolored = false,usecolor = false;;

  private Table table = null;

  public NewsTable(){
    iLayout = NEWS_SITE_LAYOUT;
  }

  public NewsTable(int iLayout){
    iLayout = iLayout ;
  }

  public NewsTable(int iLayout,int iNumberOfObjects){
    iLayout = iLayout ;
    iPlannedObjectCount = iNumberOfObjects;
  }

	public NewsTable(int iLayout,int cellPadding,int cellSpacing,String firstColor,String secondColor){
    iLayout = iLayout ;
		this.cellPadding = cellPadding;
		this.cellSpacing = cellSpacing;
		this.firstColor = firstColor;
		this.secondColor = secondColor;
		if(firstColor != null ){
			if(secondColor != null)
			  zebracolored =true;

			color = firstColor;
			usecolor = true;
		}

  }

  private void init(){
    tableRows = 1;
    tableColumns = 1;
    rowToAddIn = 1;
    colToAddIn = 1;
    if(iLayout == NEWS_SITE_LAYOUT){
      int rows = 1;
      // calculate rows needed
      if(iPlannedObjectCount > iDividedColumnCount ){
        int left = iPlannedObjectCount-iUndividedCount;
        rows = iUndividedCount + (left/iDividedColumnCount);
        if((left%iDividedColumnCount)>0)
          rows++;
      }
      table = new Table(iDividedColumnCount,rows);
    }
    else{
      table = new Table(1,iPlannedObjectCount);
    }
    table.setWidth("100%");
		table.setCellpadding(cellPadding);
		table.setCellspacing(cellSpacing);
    table.setResizable(true);
    //table.setBorder(1);
  }

  // Stilla töflu vegna óákveðinnar stærðar
  private void finite(){

    for (int i = 1; i <= table.getColumns(); i++) {
      int percent = 100/iDividedColumnCount ;
      table.setWidth(i,percent+"%");
      table.setColumnVerticalAlignment(i,"top");
			int mod = i%2;
		}
  }

  public void add(PresentationObject Mo,boolean useSetDivison,String sAlign){
    if(table == null)
      init();

    if(useSetDivison && iLayout == NEWS_SITE_LAYOUT){
      if(iObjectCount < iUndividedCount){
        table.mergeCells(1,rowToAddIn ,2,rowToAddIn );
        table.add(Mo,colToAddIn,rowToAddIn);
        table.setVerticalAlignment(colToAddIn,rowToAddIn,"top");
				if(usecolor)
				table.setColor(colToAddIn,rowToAddIn,color);
        iObjectCount++;
        rowToAddIn++;
      }
      else if(colToAddIn < iDividedColumnCount){
        table.add(Mo,colToAddIn,rowToAddIn);
        table.setVerticalAlignment(colToAddIn,rowToAddIn,"top");
				if(usecolor)
				table.setColor(colToAddIn,rowToAddIn,color);
        colToAddIn++;
        iObjectCount++;
      }
      else{
        table.add(Mo,colToAddIn,rowToAddIn);
        table.setVerticalAlignment(colToAddIn,rowToAddIn,"top");
				if(usecolor)
				table.setColor(colToAddIn,rowToAddIn,color);
        colToAddIn--;
        rowToAddIn++;
        iObjectCount++;
      }
    }
    else{
      if(colToAddIn <= iDividedColumnCount  && colToAddIn > iUndividedCount){
        rowToAddIn++;
      }
      colToAddIn = 1;
      table.mergeCells(1,rowToAddIn ,2,rowToAddIn );
      table.setAlignment(1,rowToAddIn,sAlign);
			if(usecolor)
				table.setColor(colToAddIn,rowToAddIn,color);
      table.add(Mo,1,rowToAddIn);
      rowToAddIn++;
      iObjectCount++;
    }

		if(zebracolored){
		  if(color.equals(firstColor))
				color = secondColor;
			else
				color = firstColor;
		}

  }

  public void add(PresentationObject Mo){
   add(Mo,false,sAlign);
  }

  public void add(PresentationObject Mo,String sAlign){
   add(Mo,false,sAlign);
  }

  public void main(IWContext iwc){
    finite();
    super.add(table);
  }

} // Class ListTable