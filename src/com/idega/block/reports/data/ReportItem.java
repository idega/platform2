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

public class ReportItem extends GenericEntity {

  public ReportItem() {
  }
  public ReportItem(int id) throws SQLException {
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute("category","Category",true,true,"java.lang.Integer","many-to-one","com.idega.block.reports.data.ReportCategory");
    addAttribute("name","Name",true,true,"java.lang.String");
    addAttribute("field", "Field", true, true, "java.lang.String");
    addAttribute("maintable", "Table", true, true, "java.lang.String");
    addAttribute("joins","Join",true,true,"java.lang.String");
    addAttribute("jointables", "Join Tables", true, true, "java.lang.String");
    addAttribute("condtype","Condition type",true,true,"java.lang.String");
    addAttribute("conddata","Condition data",true,true,"java.lang.String");
    addAttribute("condoperator","Condition data",true,true,"java.lang.String");
    addAttribute("entity","Entity Class",true,true,"java.lang.String");
    addAttribute("info","Info",true,true,"java.lang.String");
    super.setMaxLength("condtype",1);

  }
  public String getEntityName() {
    return "report_item";
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
  public String getField(){
    return getStringColumnValue("field");
  }
  public void setField(String field){
    setColumn("field", field);
  }
  public String getMainTable(){
    return getStringColumnValue("maintable");
  }
  public void setMainTable(String main_table){
    setColumn("maintable", main_table);
  }
  public String getJoin(){
    return getStringColumnValue("joins");
  }
  public void setJoin(String joins){
    setColumn("joins", joins);
  }
  public String getJoinTables(){
    return getStringColumnValue("jointables");
  }
  public void setJoinTables(String jointables){
    setColumn("jointables", jointables);
  }
  public String getInfo(){
    return getStringColumnValue("info");
  }
  public void setInfo(String info){
    setColumn("info", info);
  }
  public String getEntity(){
    return getStringColumnValue("entity");
  }
  public void setEntity(String entity){
    setColumn("entity", entity);
  }
  public void setEntity(GenericEntity E){
    setColumn("entity",E.getClass().getName());
  }
  public String getConditionType(){
    return getStringColumnValue("condtype");
  }
  public void setConditionType(String condtype){
    setColumn("condtype", condtype);
  }
  public String getConditionData(){
    return getStringColumnValue("conddata");
  }
  public void setConditionData(String conddata){
    setColumn("conddata", conddata);
  }
  public String getConditionOperator(){
    return getStringColumnValue("condoperator");
  }
  public void setConditionOperator(String condoperator){
    setColumn("condoperator", condoperator);
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
    StringTokenizer st = new StringTokenizer(s,",");
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
    System.err.println(s);
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