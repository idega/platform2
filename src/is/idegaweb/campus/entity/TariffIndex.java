/*
 * $Id: TariffIndex.java,v 1.6 2001/08/17 10:23:49 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.entity;

import java.sql.*;
import com.idega.data.GenericEntity;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class TariffIndex extends GenericEntity{

  public static final String A ="A",B="B",C="C",D="D",E="E";

  public TariffIndex(){
          super();
  }

  public TariffIndex(int id)throws SQLException{
          super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameIndex(), "Index", true, true, java.lang.Float.class);
    addAttribute(getColumnNameDate(), "LastUpdated", true, true, java.sql.Date.class);
    addAttribute(getColumnNameName(), "Name", true, true, java.lang.String.class);
    addAttribute(getColumnNameInfo(), "Info", true, true, java.lang.String.class);
    addAttribute(getColumnNameType(), "type", true, true, java.lang.String.class);
  }

  public static String getTariffIndexEntityName(){ return "CAM_TARIFF_INDEX"; }
  public static String getColumnNameIndex(){ return "RENT_INDEX";}
  public static String getColumnNameName(){return "NAME";}
  public static String getColumnNameInfo(){return "INFO";}
  public static String getColumnNameType(){return "INDEX_TYPE";}
  public static String getColumnNameDate(){return "FROM_DATE";}

  public String getEntityName(){
    return getTariffIndexEntityName();
  }
  public float getIndex(){
    return getFloatColumnValue(getColumnNameIndex());
  }
  public void setIndex(float index){
    setColumn(getColumnNameIndex(),index);
  }
  public void setIndex(Float index){
    setColumn(getColumnNameIndex(),index);
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
