package com.idega.block.reports.data;

import java.sql.*;
import java.util.StringTokenizer;
import com.idega.data.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class Report extends GenericEntity {

  public Report() {
  }
  public Report(int id) throws SQLException {
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameCategory(),"Category",true,true,java.lang.Integer.class,"many-to-one",com.idega.block.reports.data.ReportCategory.class);
    addAttribute(getColumnNameName(),"Name",true,true,java.lang.String.class);
    addAttribute(getColumnNameSql(), "SQL", true, true, java.lang.String.class,2000);
    addAttribute(getColumnNameHeaders(),"Titles",true,true,java.lang.String.class);
    addAttribute(getColumnNameInfo(),"ATH",true,true,java.lang.String.class);

  }

  public static String getEntityTableName(){return "REP_REPORT";}
  public static String getColumnNameName(){return "NAME";}
  public static String getColumnNameInfo(){return "INFO";}
  public static String getColumnNameSql(){return "SQLSENTENCE";}
  public static String getColumnNameHeaders(){return "HEADERS";}
  public static String getColumnNameCategory(){return "REP_CATEGORY_ID";}

  public String getEntityName() {
    return getEntityTableName();
  }
  public int getCategory(){
    return getIntColumnValue(getColumnNameCategory());
  }
  public void setCategory(int category){
    setColumn(getColumnNameCategory(), new Integer(category));
  }
  public String getName(){
    return getStringColumnValue(getColumnNameName());
  }
  public void setName(String name){
    setColumn(getColumnNameName(), name);
  }
  public String getSQL(){
    return getStringColumnValue(getColumnNameSql());
  }
  public void setSQL(String sqlsentence){
    setColumn(getColumnNameSql(), sqlsentence);
  }
  public String getInfo(){
    return getStringColumnValue(getColumnNameInfo());
  }
  public void setInfo(String info){
    setColumn(getColumnNameInfo() , info);
  }
  public void setHeaders(String[] headers){
    setColumn(getColumnNameHeaders(),array2str(headers,delim()));
  }
  public String getHeader(){
    return getStringColumnValue(getColumnNameHeaders());
  }
  public String[] getHeaders(){
    return str2array(getStringColumnValue(getColumnNameHeaders()),delim());
  }
  private String delim(){
    return ",";
  }
  private String array2str(String[] array,String delim){
    StringBuffer s = new StringBuffer();
    for (int i = 0; i < array.length; i++) {
      if(i != 0)
        s.append(delim());
      s.append(array[i]);
    }
    return s.toString();
  }
  private String[] str2array(String s,String delim){
    StringTokenizer st = new StringTokenizer(s,delim);
    String[] array = new String[st.countTokens()];
    int i = 0;
    while(st.hasMoreTokens()){
      array[i++] = st.nextToken();
    }
    return array;
  }

}