package com.idega.block.reports.business;

import com.idega.block.reports.data.*;
import java.util.StringTokenizer;
import java.sql.SQLException;
 /**
  * Title:
  * Description:
  * Copyright:    Copyright (c) 2001
  * Company:      idega multimedia
  * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
  * @version 1.0
  */

 public class ReportCondition{

  private ReportItem Item;
  private String sJoin="",sOperator="",sVariable="";
  private String[] sOps,sVars;
  private String sCondition="";
  private boolean bCondition,bSelect,bField = false;
  private boolean bBetween = false;
  private Integer orderNumber = null;
  private Integer colOrder = null;

  public ReportCondition(ReportItem Item) {
   this.Item = Item;
   init();
  }
  public ReportCondition(int ItemId){
    try {
      this.Item  = new ReportItem(ItemId);
      init();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
  private void init(){
    if(Item.getField()!= null){
      bField = true;
      StringBuffer sb = new StringBuffer(this.Item.getMainTable());
      sb.append(".");
      sb.append(this.Item.getField());
      this.sJoin = sb.toString();
      this.sOps = this.Item.getOps();
    }
    this.bCondition = false;
    this.bSelect   = false;
  }
  public void setCondition(){

  }
  public ReportItem getItem(){
    return this.Item;
  }
  public String getCondition(){
    StringBuffer sb = new StringBuffer("");
    if(bCondition && bField){
      if(bBetween){
        sb.append(this.sJoin);
        sb.append(" ");
        sb.append(" between '");
        sb.append(sVars[0]);
        sb.append("' and '");
        sb.append(sVars[1]);
        sb.append("'");
      }
      else{
        int len = this.Item.getOps().length;
        for (int i = 0; i < len; i++) {
          sb.append(this.sJoin);
          sb.append(" ");
          sb.append(this.sOps[i]);
          sb.append(" ");
          if(sVars != null){
            sb.append("'");
            sb.append(this.sVars[i]);
            sb.append("' ");
          }
        }
      }
    }
    this.sCondition = sb.toString();
    return this.sCondition;
  }
  public void setOperator(String[] sOps){
    this.sOps = sOps;
  }
  public void setVariable(String sVar){
    StringTokenizer st = new StringTokenizer(sVar,":");
    if(st.countTokens() == 2){
      bBetween = true;
      sVars = new String[2];
      sVars[0] = st.nextToken();
      sVars[1] = st.nextToken();
    }
    else{
      this.sVars = new String[1];
      sVars[0] = sVar;
    }
    this.bCondition = true;
  }
  public void setVariables(String[] sVars){
    this.sVars = sVars;
    this.bCondition = true;
  }
  public String getDisplay(){
    return this.getItem().getName();
  }
  public void setIsSelect(){
    this.bSelect = true;
  }
  public boolean isSelect(){
    return bSelect;
  }
  public void setColumnOrder(Integer order){
    colOrder = order;
  }
  public Integer getColumnOrder(){
    return colOrder;
  }
  public void setOrder(Integer order){
    orderNumber = order;
  }
  public Integer getOrder(){
    return orderNumber;
  }
}