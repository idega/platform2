/*
 * $Id: ApartmentTypePeriods.java,v 1.1 2001/08/01 10:59:43 aron Exp $
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
import com.idega.util.idegaTimestamp;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class ApartmentTypePeriods extends GenericEntity{

  public final static int ZEROYEAR = 2000;

  public ApartmentTypePeriods(){
          super();
  }

  public ApartmentTypePeriods(int id)throws SQLException{
          super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getApartmentTypeIdColumnName(), "ApartmenttypeId", true, true, "java.lang.Integer","one-to-one","com.idega.block.building.data.ApartmentType");
    addAttribute(getNameColumnName(), "Name", true, true, "java.lang.String");
    addAttribute(getFirstDateColumnName(), "First date", true, true, "java.sql.Date");
    addAttribute(getSecondDateColumnName(), "Second date", true, true, "java.sql.Date");
  }

  public static String getEntityTableName(){ return "CAM_CONTRACT_DATE"; }
  public static String getApartmentTypeIdColumnName(){return "BU_APRT_TYPE_ID";}
  public static String getNameColumnName(){ return "NAME";}
  public static String getFirstDateColumnName(){return "FIRSTDATE";}
  public static String getSecondDateColumnName(){return "SECONDDATE";}

  public String getEntityName(){
    return getEntityTableName();
  }
  public int getApartmentTypeId(){
    return getIntColumnValue(getApartmentTypeIdColumnName());
  }
  public void setApartmentTypeId(int id){
    setColumn(getApartmentTypeIdColumnName(),id);
  }
  public void setApartmentTypeId(Integer id){
    setColumn(getApartmentTypeIdColumnName(),id);
  }
  public String getName(){
    return getStringColumnValue(getNameColumnName());
  }
  public void setName(String name){
    setColumn(getNameColumnName(), name);
  }
  public java.sql.Date getFirstDate(){
    return (java.sql.Date) getColumnValue(getFirstDateColumnName());
  }
  public void setFirstDate(java.sql.Date use_date){
    setColumn(getFirstDateColumnName(),use_date);
  }
  public void setFirstDate(int day,int month){
    int year = ZEROYEAR;
    if(day > 0 && month > 0)
      year = idegaTimestamp.RightNow().getYear();
    else{
      day = 1;
      month = 1;
    }
    idegaTimestamp it = new idegaTimestamp(day,month,year);
    setFirstDate(it.getSQLDate());
  }
  public java.sql.Date getSecondDate(){
    return (java.sql.Date) getColumnValue(getSecondDateColumnName());
  }

  public void setSecondDate(java.sql.Date use_date){
    setColumn(getSecondDateColumnName(),use_date);
  }
  public void setSecondDate(int day,int month){
    int year = ZEROYEAR;
    if(day > 0 && month > 0)
      year = idegaTimestamp.RightNow().getYear();
    else{
      day = 1;
      month = 1;
    }
    idegaTimestamp it = new idegaTimestamp(day,month,year);
    setSecondDate(it.getSQLDate());
  }
  public int getFirstDateDay(){
    idegaTimestamp it = new idegaTimestamp(getFirstDate());
    return (it.getYear() != ZEROYEAR)?it.getDay():0;
  }
  public int getFirstDateMonth(){
    idegaTimestamp it = new idegaTimestamp(getFirstDate());
    return (it.getYear() != ZEROYEAR)?it.getMonth():0;
  }
  public int getSecondDateDay(){
    idegaTimestamp it = new idegaTimestamp(getSecondDate());
    return (it.getYear() != ZEROYEAR)?it.getDay():0;
  }
  public int getSecondDateMonth(){
    idegaTimestamp it = new idegaTimestamp(getSecondDate());
    return (it.getYear() != ZEROYEAR)?it.getMonth():0;
  }
}
