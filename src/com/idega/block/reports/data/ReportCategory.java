package com.idega.block.reports.data;

import java.sql.*;
import com.idega.data.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class ReportCategory extends GenericEntity{

  public ReportCategory(){
          super();
  }
  public ReportCategory(int id)throws SQLException{
          super(id);
  }
  public void initializeAttributes(){
          addAttribute(getIDColumnName());
          addAttribute("name", "Flokkur", true, true, java.lang.String.class);
          addAttribute("info", "Lýsing", true, true, java.lang.String.class);
  }

  public static String getEntityTableName(){return "REP_CATEGORY";}
  public static String getColumnNameName(){return "NAME";}
  public static String getColumnNameInfo(){return "INFO";}

  public String getEntityName(){
    return getEntityTableName();
  }
  public void setName(String name){
    setColumn(getColumnNameName(), name);
  }
  public String getName(){
    return getStringColumnValue(getColumnNameName());
  }
  public String getInfo(){
    return getStringColumnValue(getColumnNameInfo());
  }
  public void setInfo(String info){
    setColumn(getColumnNameInfo(), info);
  }
}
