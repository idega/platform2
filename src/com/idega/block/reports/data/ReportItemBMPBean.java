package com.idega.block.reports.data;

import java.sql.*;
import java.util.StringTokenizer;
import com.idega.data.*;
import java.util.Vector;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class ReportItemBMPBean extends com.idega.block.category.data.CategoryEntityBMPBean implements com.idega.block.reports.data.ReportItem {

  public ReportItemBMPBean() {
  }
  public ReportItemBMPBean(int id) throws SQLException {
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameName(),"Name",true,true,java.lang.String.class);
    addAttribute(getColumnNameInfo(),"Info",true,true,java.lang.String.class);
    addAttribute(getColumnNameField(), "Field", true, true, java.lang.String.class);
    addAttribute(getColumnNameConditionType(),"Condition type",true,true,java.lang.String.class,1);
    addAttribute(getColumnNameConditionData(),"Condition data",true,true,java.lang.String.class);
    addAttribute(getColumnNameConditionOperator(),"Condition data",true,true,java.lang.String.class);
    addAttribute(getColumnNameEntityId(),"Entity",true,true,java.lang.Integer.class,"many-to-one",com.idega.block.reports.data.ReportEntity.class);
    addAttribute(getColumnNameEntity(),"Entity Class",true,true,java.lang.String.class);
    addAttribute(getColumnNameMaintable(), "Table", true, true, java.lang.String.class);
    addAttribute(getColumnNameJoins(),"Join",true,true,java.lang.String.class);
    addAttribute(getColumnNameJoinTables(), "Join Tables", true, true, java.lang.String.class);
    addAttribute(getColumnNameDisplayOrder(),"Display order",true,true,java.lang.Integer.class);
    addAttribute(getColumnNameIsFunction(),"Is function",true,true,java.lang.Boolean.class);


  }

  public static String getEntityTableName(){return "REP_ITEM";}
  public static String getColumnNameName(){return "NAME";}
  public static String getColumnNameInfo(){return "INFO";}
  public static String getColumnNameField(){return "FIELD";}
  public static String getColumnNameConditionData(){return "CONDDATA";}
  public static String getColumnNameConditionType(){return "CONDTYPE";}
  public static String getColumnNameConditionOperator(){return "CONDOPERATOR";}
  public static String getColumnNameEntityId(){return "REP_ENTITY_ID";}
  public static String getColumnNameEntity(){return "DEP_ENTITY";}
  public static String getColumnNameMaintable(){return "DEP_MAINTABLE";}
  public static String getColumnNameJoins(){return "DEP_JOINS";}
  public static String getColumnNameJoinTables(){return "DEP_JOINTABLE";}
  public static String getColumnNameDisplayOrder(){return "DISPLAY_ORDER";}
	public static String getColumnNameIsFunction(){return "IS_FUNCTION";}

  public String getEntityName() {
    return getEntityTableName();
  }

  public int getEntityId(){
    return getIntColumnValue(getColumnNameEntityId());
  }
  public void setEntityId(int id){
    setColumn(getColumnNameEntityId(), new Integer(id));
  }
  public int getDisplayOrder(){
    return getIntColumnValue(getColumnNameDisplayOrder());
  }
  public void setDisplayOrder(int order){
    setColumn(getColumnNameDisplayOrder(), new Integer(order));
  }
  public String getName(){
    return getStringColumnValue(getColumnNameName());
  }
  public void setName(String name){
    setColumn(getColumnNameName(), name);
  }
  public String getField(){
    return getStringColumnValue(getColumnNameField());
  }
  public void setField(String field){
    setColumn(getColumnNameField(), field);
  }
  public String getMainTable(){
    return getStringColumnValue(getColumnNameMaintable());
  }
  public void setMainTable(String main_table){
    setColumn(getColumnNameMaintable(), main_table);
  }
  public String getJoin(){
    return getStringColumnValue(getColumnNameJoins());
  }
  public void setJoin(String joins){
    setColumn(getColumnNameJoins(), joins);
  }
  public String getJoinTables(){
    return getStringColumnValue(getColumnNameJoinTables());
  }
  public void setJoinTables(String jointables){
    setColumn(getColumnNameJoinTables(), jointables);
  }
  public String getInfo(){
    return getStringColumnValue(getColumnNameInfo());
  }
  public void setInfo(String info){
    setColumn(getColumnNameInfo(), info);
  }
  public String getEntity(){
    return getStringColumnValue(getColumnNameEntity());
  }
  public void setEntity(String entity){
    setColumn(getColumnNameEntity(), entity);
  }
  public void setEntity(IDOLegacyEntity E){
    setColumn(getColumnNameEntity(),E.getClass().getName());
  }
  public String getConditionType(){
    return getStringColumnValue(getColumnNameConditionType());
  }
  public void setConditionType(String condtype){
    setColumn(getColumnNameConditionType(), condtype);
  }
  public String getConditionData(){
    return getStringColumnValue(getColumnNameConditionData());
  }
  public void setConditionData(String conddata){
    setColumn(getColumnNameConditionData(), conddata);
  }
  public String getConditionOperator(){
    return getStringColumnValue(getColumnNameConditionOperator());
  }
  public void setConditionOperator(String condoperator){
    setColumn(getColumnNameConditionOperator(), condoperator);
  }
	public boolean getIsFunction(){
    return getBooleanColumnValue(getColumnNameIsFunction());
  }
  public void setIsFunction(boolean isFunction){
    setColumn(getColumnNameIsFunction(), isFunction);
  }
  public String[] getOps(){
    String s = this.getConditionOperator();
    return ms(s,";");
  }
  public void setOps(String[] s){
    this.setConditionOperator(this.sm(s,";"));
  }

  public String[] getJoinTable(){
    String s = this.getJoinTables();
    if(!"".equalsIgnoreCase(s)){
    StringTokenizer st = new StringTokenizer(s,",;: ");
    String[] S = new String[st.countTokens()];
    int i = 0;
    while(st.hasMoreTokens()){
      S[i] = st.nextToken();
      i++;
    }
    return S;
    }
    else
      return null;
  }

  public String[][] getData(){
    String[][] sA = null;

    String s = this.getConditionData();
    //System.err.println(s);
    if(s != null){
    String[] sB = ms(s,";");
      sA = new String[sB.length][];
      for (int i = 0; i < sB.length; i++) {
          sA[i] = ms(sB[i],",");
      }
    }
    else
       System.err.println("data is null");
    return sA;
  }
  public void setData(String[][] s){
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < s.length; i++) {
      if(i!= 0)
        sb.append(";");
      for (int j = 0; j < s[i].length; j++) {
        if(j != 0)
          sb.append(",");
        sb.append(s[i][j]);
      }
    }
   this.setConditionData(sb.toString());
  }
  private String[] ms(String s,String delim){
    StringTokenizer st = new StringTokenizer(s,delim);
    Vector v = new Vector();
    while(st.hasMoreTokens()){
      v.addElement(st.nextToken());
    }
    int len = v.size();
    String[] sArray = new String[len];
    for (int i = 0; i < len; i++) {
      sArray[i] = (String)v.get(i);
    }
    return sArray;
  }
  private String sm(String[] s,String delim){
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < s.length; i++) {
      if(i != 0)
        sb.append(delim);
      sb.append(s[i]);

    }
    return sb.toString();
  }
}
