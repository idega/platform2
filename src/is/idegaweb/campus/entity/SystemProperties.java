package is.idegaweb.campus.entity;

import com.idega.data.*;
import com.idega.util.idegaTimestamp;


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
    addAttribute(getColumnNameContractYears(),"Contract Years",true,true,java.lang.Integer.class);
    addAttribute(getColumnNameContractDate(),"Contract Date",true,true,java.sql.Date.class);
    addAttribute(getColumnNameCypherKey(),"Cypher Key",true,true,java.lang.String.class,4000);
    addAttribute(getColumnNameCypherKey(),"Admin email",true,true,java.lang.String.class,1000);
    addAttribute(getColumnNameCypherKey(),"email host",true,true,java.lang.String.class,1000);
    addAttribute(getColumnNameDefaultGroup(),"default group",true,true,java.lang.Integer.class);
  }
  public String getEntityName() {
    return getEntityTableName();
  }

  public static String getEntityTableName(){return "CAM_SYS_PROPS";}
  public static String getColumnNameContractYears(){return "CONTRACT_YEARS";}
  public static String getColumnNameContractDate(){return "CONTRACT_DATE";}
  public static String getColumnNameCypherKey(){return "CYPHERKEY";}
  public static String getColumnNameAdminEmail(){return "ADMIN_EMAIL";}
  public static String getColumnNameEmailHost(){return "EMAIL_HOST";}
  public static String getColumnNameDefaultGroup(){return "DEFAULT_GROUP";}

  public void setContractYears(int years){
    setColumn(getColumnNameContractYears(),years);
  }
  public int getContractYears(){
    return getIntColumnValue(getColumnNameContractYears());
  }
  public void setContractDate(java.sql.Date date){
    setColumn(getColumnNameContractDate(),date);
  }
  public java.sql.Date getContractDate(){
    return((java.sql.Date)getColumnValue(getColumnNameContractDate()));
  }
  public void setCypherKey(String key){
    setColumn(getColumnNameCypherKey(),key);
  }
  public String getCypherKey(){
    return getStringColumnValue(getColumnNameCypherKey());
  }
  public void setAdminEmail(String email){
    setColumn(getColumnNameAdminEmail(),email);
  }
  public String getAdminEmail(){
    return getStringColumnValue(getColumnNameAdminEmail());
  }
  public void setEmailHost(String host){
    setColumn(getColumnNameEmailHost(),host);
  }
  public int getDefaultGroup(){
    return getIntColumnValue(getColumnNameDefaultGroup());
  }
  public void setDefaultGroup(int host){
    setColumn(getColumnNameDefaultGroup(),host);
  }
  public String getEmailHost(){
    return getStringColumnValue(getColumnNameEmailHost());
  }
  public void insert() throws java.sql.SQLException{
  }
  public void delete() throws java.sql.SQLException{
  }

  public java.sql.Date getValidToDate(){
    int years = this.getContractYears();
    if(this.getContractYears() > 0){
      idegaTimestamp now = idegaTimestamp.RightNow();
      idegaTimestamp iT = new idegaTimestamp(1,now.getMonth(),now.getYear()+years);
      return iT.getSQLDate();
    }
    else
     return this.getContractDate();
  }

}