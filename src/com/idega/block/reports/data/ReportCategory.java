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
          addAttribute("name", "Flokkur", true, true, "java.lang.String");
          addAttribute("info", "Lýsing", true, true, "java.lang.String");
  }
  public String getEntityName(){
    return "report_category";
  }
  public void setName(String name){
    setColumn("name", name);
  }
  public String getName(){
    return getStringColumnValue("name");
  }
  public String getInfo(){
    return getStringColumnValue("info");
  }
  public void setInfo(String info){
    setColumn("info", info);
  }
}
