package com.idega.block.trade.stockroom.data;

import java.sql.SQLException;
import java.sql.Timestamp;


/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class SuppliesBMPBean extends com.idega.data.GenericEntity implements com.idega.block.trade.stockroom.data.Supplies {

  public static final int PERIOD_NOPERIOD = 0;
  public static final int PERIOD_DAILY = 1;
  public static final int PERIOD_WEEKLY = 2;
  public static final int PERIOD_MONTHLY = 3;
  public static final int PERIOD_YEARLY = 4;

  public SuppliesBMPBean() {
    super();
  }

  public SuppliesBMPBean(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(this.getIDColumnName());
    addAttribute(getColumnNameProductId(),"Vara",true,true,Integer.class,"many-to-one",Product.class);
    addAttribute(getColumnNameRecordTime(),"Tími færslu",true,true,Timestamp.class);
    addAttribute(getColumnNameRecord(), "Færsla", true, true, Float.class);
    addAttribute(getColumnNameCurrentSupplies(), "Núverandi birgðir", true, true, Float.class);
    addAttribute(getColumnNamePeriod(),"Lota",true,true,Integer.class);
  }


  public String getEntityName() {
    return "SR_SUPPLIES";
  }



  public static String getColumnNameProductId(){return"TB_PRODUCT_ID";}
  public static String getColumnNameRecordTime(){return"RECORD_TIME";}
  public static String getColumnNameRecord() {return "RECORD";}
  public static String getColumnNameCurrentSupplies() {return "CURRENT_SUPPLIES";}
  public static String getColumnNamePeriod(){return "PERIOD";}


  /* Setters */

  public void setProductId(int value){
    this.setColumn(getColumnNameProductId(),value);
  }

  public void setRecordTime(Timestamp value){
    this.setColumn(getColumnNameRecordTime(),value);
  }

  public void setRecord(float value){
    this.setColumn(getColumnNameRecord(),value);
  }

  public void setCurrentSupplies(float value){
    this.setColumn(getColumnNameCurrentSupplies(),value);
  }

  public void setPeriod(int value){
    this.setColumn(getColumnNamePeriod(),value);
  }


  /*Getters*/

  public int getProductId(){
    return getIntColumnValue(getColumnNameProductId());
  }

  public Timestamp getRecordTime(){
    return (Timestamp)this.getColumnValue(getColumnNameRecordTime());
  }

  public float getRecord(){
    return getFloatColumnValue(getColumnNameRecord());
  }

  public float getCurrentSupplies(){
    return getFloatColumnValue(getColumnNameCurrentSupplies());
  }

  public int getPeriod(){
    return getIntColumnValue(getColumnNamePeriod());
  }

}
