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
    addAttribute(getColumnNameName(), "Category", true, true, java.lang.String.class);
    addAttribute(getColumnNameInfo(), "Info", true, true, java.lang.String.class);
		addAttribute(getValidColumnName(), "Valid", true, true, Boolean.class);
		addAttribute(getColumnNameCreated(),"created",true,true,java.sql.Date.class);
		addManyToManyRelationShip(com.idega.core.data.ICObjectInstance.class);
  }

  public static String getEntityTableName(){return "REP_CATEGORY";}
  public static String getColumnNameName(){return "NAME";}
  public static String getColumnNameInfo(){return "INFO";}
	public static String getColumnNameCreated(){return "CREATED";}
	public static String getValidColumnName(){return "VALID";}


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
	public void setCreationDate(Date date) {
    setColumn(getColumnNameCreated(),date);
  }
  public Date getCreationDate(){
    return (Date) getColumnValue(getColumnNameCreated() );
  }

	public boolean getValid(){
    return getBooleanColumnValue(getValidColumnName());
  }
  public void setValid(boolean valid){
    setColumn(getValidColumnName(), valid);
  }
}
