package com.idega.block.reports.business;

import com.idega.block.reports.data.*;
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

  public ReportCondition(ReportItem Item) {
   this.Item = Item;
   init();
  }
  public ReportCondition(int ItemId){
    try {
      this.Item  = new ReportItem(ItemId);
    }
    catch (SQLException ex) {
    }
  }
  private void init(){
    StringBuffer sb = new StringBuffer(this.Item.getMainTable());
    sb.append(".");
    sb.append(this.Item.getField());
    this.sJoin = sb.toString();
    this.sOps = this.Item.getOps();
  }
  public void setCondition(){

  }
  public ReportItem getItem(){
    return this.Item;
  }
  public String getCondition(){
    int len = this.Item.getOps().length;
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < len; i++) {
      sb.append(this.sJoin);
      sb.append(" ");
      sb.append(this.sOps[i]);
      sb.append(" ");
      sb.append(this.sVars[i]);
      sb.append(" ");
    }
    this.sCondition = sb.toString();
    return this.sCondition;
  }
  public void setOperator(String[] sOps){
    this.sOps = sOps;
  }
  public void setVariables(String[] sVars){
    this.sVars = sVars;
  }
}