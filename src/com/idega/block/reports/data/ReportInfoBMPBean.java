package com.idega.block.reports.data;

import java.sql.SQLException;
/**
 *
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class ReportInfoBMPBean extends com.idega.block.category.data.CategoryEntityBMPBean implements com.idega.block.reports.data.ReportInfo {

  public static String getEntityTableName() {return "rep_report_info";}
  public static String getColumnName(){return "name";}
  public static String getColumnType(){return "info_type";}
  public static String getColumnWidth(){return "width";}
  public static String getColumnHeight(){return "height";}
  public static String getColumnLandscape(){return "landscape";}
  public static String getColumnColumns(){return "columns";}
  public static String getColumnPagesize(){return "pagesize";}
  public static String getColumnBorder(){return "border";}

  public ReportInfoBMPBean() {
    super();
  }
  public ReportInfoBMPBean(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnName(),"Name",true,true,String.class);
    addAttribute(getColumnType(),"Type",true,true,String.class);
    addAttribute(getColumnWidth(),"Width",true,true,Float.class);
    addAttribute(getColumnHeight(),"Height",true,true,Float.class);
    addAttribute(getColumnLandscape(),"Landscape",true,true,Boolean.class);
    addAttribute(getColumnColumns(),"Columns",true,true,Integer.class);
    addAttribute(getColumnPagesize(),"Pagesize",true,true,String.class,10);
    addAttribute(getColumnBorder(),"Border",true,true,Integer.class);
  }

  public String getEntityName(){
    return getEntityTableName();
  }


  public void setName(String name){
    setColumn(getColumnName(),name);
  }

   public String getName(){
    return getStringColumnValue(getColumnName());
  }

  public void setType(String type){
    setColumn(getColumnType(),type);
  }

   public String getType(){
    return getStringColumnValue(getColumnType());
  }

   public void setPagesize(String type){
    setColumn(getColumnPagesize(),type);
  }

   public String getPagesize(){
    return getStringColumnValue(getColumnPagesize());
  }

  public void setWidth(float width){
    setColumn(getColumnWidth(),width);
  }

  public float getWidth(){
    return getFloatColumnValue(getColumnWidth());
  }

  public void setHeight(float height){
    setColumn(getColumnHeight(),height);
  }

  public float getHeight(){
    return getFloatColumnValue(getColumnHeight());
  }

  public void setColumns(int columns){
    setColumn(getColumnColumns(),columns);
  }

  public int getColumns(){
    return getIntColumnValue(getColumnColumns());
  }

  public void setBorder(int border){
    setColumn(getColumnBorder(),border);
  }

  public int getBorder(){
    return getIntColumnValue(getColumnBorder());
  }
  public void setLandscape(boolean landscape){
    setColumn(getColumnLandscape(),landscape);
  }
  public boolean getLandscape(){
    return getBooleanColumnValue(getColumnLandscape());
  }
  public String getDescription(){
    if(getType().equals("sticker"))
      return getWidth()+" x "+getHeight()+" "+getPagesize();
    else
      return getColumnColumns()+" "+getPagesize();
  }
}
