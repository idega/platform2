package is.idegaweb.campus.entity;

import com.idega.data.*;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class SystemProperties extends GenericEntity {

  public SystemProperties() {

  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getContractYearsColumnName(),"Contract Years",true,true,"java.lang.Integer");
    addAttribute(getContractDateColumnName(),"Contract Date",true,true,"java.sql.Date");
    addAttribute(getCypherKeyColumnName(),"Cypher Key",true,true,"java.lang.String",4000);
  }
  public String getEntityName() {
    return getSystemPropertiesEnitityName();
  }

  public static String getSystemPropertiesEnitityName(){return "CAM_SYS_PROPS";}
  public static String getContractYearsColumnName(){return "CONTRACT_YEARS";}
  public static String getContractDateColumnName(){return "CONTRACT_DATE";}
  public static String getCypherKeyColumnName(){return "CYPHERKEY";}

  public void setContractYears(int years){
    setColumn(getContractYearsColumnName(),years);
  }
  public int getContractYears(){
    return getIntColumnValue(getContractYearsColumnName());
  }
  public void setContractDate(java.sql.Date date){
    setColumn(getContractDateColumnName(),date);
  }
  public java.sql.Date getContractDate(){
    return((java.sql.Date)getColumnValue(getContractDateColumnName()));
  }
  public void setCypherKey(String key){
    setColumn(getCypherKeyColumnName(),key);
  }
  public String getCypherKey(){
    return getStringColumnValue(getCypherKeyColumnName());
  }
  public void insert() throws java.sql.SQLException{
  }
  public void delete() throws java.sql.SQLException{
  }

}