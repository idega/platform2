/*
 * $Id: TariffIndex.java,v 1.1 2001/11/08 14:43:05 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.finance.data;


import java.sql.*;
import com.idega.data.GenericEntity;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class TariffIndex extends GenericEntity{

  public static final String A ="A",B="B",C="C",D="D",E="E";
  public static final String indexType =  "ABCDEFGHIJK";

  public TariffIndex(){
          super();
  }

  public TariffIndex(int id)throws SQLException{
          super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameNewValue(), "New Value", true, true, java.lang.Float.class);
    addAttribute(getColumnNameOldValue(), "Old alue", true, true, java.lang.Float.class);
    addAttribute(getColumnNameDate(), "LastUpdated", true, true, java.sql.Date.class);
    addAttribute(getColumnNameName(), "Name", true, true, java.lang.String.class);
    addAttribute(getColumnNameInfo(), "Info", true, true, java.lang.String.class);
    addAttribute(getColumnNameType(), "type", true, true, java.lang.String.class);
  }

  public static String getTariffIndexEntityName(){ return "CAM_TARIFF_INDEX"; }
  public static String getColumnNameNewValue(){ return "NEW_VALUE";}
  public static String getColumnNameOldValue(){ return "OLD_VALUE";}
  public static String getColumnNameName(){return "NAME";}
  public static String getColumnNameInfo(){return "INFO";}
  public static String getColumnNameType(){return "INDEX_TYPE";}
  public static String getColumnNameDate(){return "FROM_DATE";}

  public String getEntityName(){
    return getTariffIndexEntityName();
  }
  public float getIndex(){
    return getFloatColumnValue(getColumnNameNewValue());
  }
  public void setIndex(float index){
    setColumn(getColumnNameNewValue(),index);
  }
  public void setIndex(Float index){
    setColumn(getColumnNameNewValue(),index);
  }
  public float getNewValue(){
    return getFloatColumnValue(getColumnNameNewValue());
  }
  public void setNewValue(float index){
    setColumn(getColumnNameNewValue(),index);
  }
  public void setNewValue(Float index){
    setColumn(getColumnNameNewValue() ,index);
  }
  public float getOldValue(){
    return getFloatColumnValue(getColumnNameOldValue());
  }
  public void setOldValue(float index){
    setColumn(getColumnNameOldValue(),index);
  }
  public void setOldValue(Float index){
    setColumn(getColumnNameOldValue() ,index);
  }
  public String getName(){
    return getStringColumnValue(getColumnNameName());
  }
  public void setName(String name){
    setColumn(getColumnNameName(), name);
  }
  public String getInfo(){
    return getStringColumnValue(getColumnNameInfo());
  }
  public void setInfo(String info){
    setColumn(getColumnNameInfo(), info);
  }
  public String getType(){
    return getStringColumnValue(getColumnNameType());
  }
  public void setType(String type){
    setColumn(getColumnNameType(), type);
  }
  public java.sql.Date getDate(){
    return (java.sql.Date) getColumnValue(getColumnNameDate());
  }
  public void setDate(java.sql.Date use_date){
    setColumn(getColumnNameDate(),use_date);
  }
}
