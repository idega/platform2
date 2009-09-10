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

public class ReportColumnInfoBMPBean extends com.idega.data.GenericEntity implements com.idega.block.reports.data.ReportColumnInfo {

  public static String getEntityTableName() {return "rep_col_info";}
  public static String getColumnColNumber(){return "col_number";}
  public static String getColumnFontFamily(){return "font_family";}
  public static String getColumnFontSize(){return "font_size";}
  public static String getColumnFontStyle(){return "font_style";}
  public static String getColumnColspan(){return "col_span";}
  public static String getColumnEndString(){return "endstring";}
  public static String getColumnShowName(){return "show_name";}
  public static String getColumnReportId(){return "rep_report_id";}


  public ReportColumnInfoBMPBean() {
    super();
  }
  public ReportColumnInfoBMPBean(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnReportId(),"Report",true,true,Integer.class,MANY_TO_ONE,Report.class);
    addAttribute(getColumnColNumber(),"Column number",true,true,Integer.class);
    addAttribute(getColumnFontFamily(),"Font family",true,true,Integer.class);
    addAttribute(getColumnFontSize(),"Font size",true,true,Integer.class);
    addAttribute(getColumnFontStyle(),"Font style",true,true,Integer.class);
    addAttribute(getColumnColspan(),"Column span",true,true,Integer.class);
    addAttribute(getColumnEndString(),"Endstring",true,true,String.class);
    addAttribute(getColumnShowName(),"show name",true,true,Boolean.class);
  }

    public String getEntityName(){
      return getEntityTableName();
    }

    public void setReportId(int id)
    {
        setColumn(getColumnReportId(),id);
    }

    public int getReportId()
    {
        return getIntColumnValue(getColumnReportId());
    }

    public void setColumnNumber(int col)
    {
        setColumn(getColumnColNumber(),col);
    }

    public int getColumnNumber()
    {
        return getIntColumnValue(getColumnColNumber());
    }

    public void setFontFamily(int family)
    {
        setColumn(getColumnFontFamily(),family);
    }

    public int getFontFamily()
    {
        return getIntColumnValue(getColumnFontFamily());
    }

    public void setFontSize(int FontSize)
    {
        setColumn(getColumnFontSize(),FontSize);
    }

    public int getFontSize()
    {
        return getIntColumnValue(getColumnFontSize());
    }

    public void setFontStyle(int style)
    {
        setColumn(getColumnFontStyle(),style);
    }

    public int getFontStyle()
    {
        return getIntColumnValue(getColumnFontStyle());
    }

    public void setColumnSpan(int span)
    {
        setColumn(getColumnColspan(),span);
    }

    public int getColumnSpan()
    {
        return getIntColumnValue(getColumnColspan());
    }

    public void setShowName(boolean showname)
    {
        setColumn(getColumnShowName(),showname);
    }

    public boolean getShowName()
    {
        return getBooleanColumnValue(getColumnShowName());
    }

    public void setEndChar(String endstring)
    {
        setColumn(getColumnEndString(),endstring);
    }

    public String getEndChar()
    {
        return getStringColumnValue(getColumnEndString());
    }
}
