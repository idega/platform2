package com.idega.block.reports.data;

import java.sql.SQLException;
import java.util.StringTokenizer;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class ReportBMPBean extends com.idega.block.category.data.CategoryEntityBMPBean implements com.idega.block.reports.data.Report {

  public static final String NORMAL = "NORMAL";
  public static final String STICKERS = "STICKERS";
  public static final String COLUMNS = "COLUMNS";

  public ReportBMPBean() {
  }
  public ReportBMPBean(int id) throws SQLException {
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameName(),"Name",true,true,java.lang.String.class);
    addAttribute(getColumnNameSql(), "SQL", true, true, java.lang.String.class,4000);
    addAttribute(getColumnNameHeaders(),"Titles",true,true,java.lang.String.class);
    addAttribute(getColumnNameInfo(),"Info",true,true,java.lang.String.class);
    addAttribute(getColumnType(),"Type",true,true,java.lang.String.class);
    addAttribute(getColumnColInfo(),"Fonts",true,true,java.lang.String.class);
    addManyToManyRelationShip(ReportInfo.class);

  }

  public static String getEntityTableName(){return "REP_REPORT";}
  public static String getColumnNameName(){return "NAME";}
  public static String getColumnNameInfo(){return "INFO";}
  public static String getColumnNameSql(){return "SQLSENTENCE";}
  public static String getColumnNameHeaders(){return "HEADERS";}
  public static String getColumnType(){return "REP_TYPE";}
  public static String getColumnColInfo(){return "COLINFO";}

  public String getEntityName() {
    return getEntityTableName();
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
   public String getType(){
    return getStringColumnValue(getColumnType());
  }
  public void setType(String type){
    setColumn(getColumnType(), type);
  }
  public String getColInfo(){
    return getStringColumnValue(getColumnColInfo());
  }
  public void setColInfo(String cols){
    setColumn(getColumnColInfo(), cols);
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

  public void storeColumnInfos(java.util.List columnInfos){
    try{
    java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
    java.io.ObjectOutputStream s = new java.io.ObjectOutputStream(bos);
    s.writeObject(columnInfos);
    s.flush();
    setColInfo(bos.toString());
    }
    catch(java.io.IOException ex){ex.printStackTrace();}
  }

  public java.util.List getColumnInfos(){
    String infos = getColumnColInfo();
    if(infos !=null){
      try{
        java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(infos.getBytes());
        java.io.ObjectInputStream s = new java.io.ObjectInputStream(bis);
        java.util.List infoList = (java.util.List)s.readObject();
        return infoList;
      }
      catch(ClassNotFoundException ex){ex.printStackTrace();}
      catch(java.io.IOException ex){ex.printStackTrace();}

    }
    return null;
  }

}
