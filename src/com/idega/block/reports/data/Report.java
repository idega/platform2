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
    addAttribute("category","Category",true,true,"java.lang.Integer","many-to-one","com.idega.block.reports.data.ReportCategory");
    addAttribute("name","Name",true,true,"java.lang.String");
    addAttribute("sqlsentence", "SQL", true, true, "java.lang.String");
    addAttribute("headers","Titles",true,true,"java.lang.String");
    addAttribute("info","ATH",true,true,"java.lang.String");
    setMaxLength("sqlsentence",2000);
  }
  public String getEntityName() {
    return "report";
  }
  public int getCategory(){
    return getIntColumnValue("category");
  }
  public void setCategory(int category){
    setColumn("category", new Integer(category));
  }
  public String getName(){
    return getStringColumnValue("name");
  }
  public void setName(String name){
    setColumn("name", name);
  }
  public String getSQL(){
    return getStringColumnValue("sqlsentence");
  }
  public void setSQL(String sqlsentence){
    setColumn("sqlsentence", sqlsentence);
  }
  public String getInfo(){
    return getStringColumnValue("info");
  }
  public void setInfo(String info){
    setColumn("info", info);
  }
  public void setHeaders(String[] headers){
    setColumn("headers",array2str(headers,delim()));
  }
  public String[] getHeaders(){
    return str2array(getStringColumnValue("headers"),delim());
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