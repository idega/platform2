//idega 2000 - aron

package com.idega.block.finance.data;

import com.idega.data.GenericEntity;
import com.idega.util.idegaTimestamp;
import java.sql.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class AssessmentRound extends GenericEntity{

  public AssessmentRound(){
    super();
  }

  public AssessmentRound(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getNameColumnName(),"Name",true,true,java.lang.String.class);
    addAttribute(getRoundStampColumnName(),"Round stamp",true,true,java.sql.Timestamp.class);
    addAttribute(getTotalsColumnName(), "Totals", true, true, java.lang.Float.class);
    addAttribute(getStatusColumnName(),"Status",true,true,java.lang.String.class,1);
    addAttribute(getTypeColumnName(),"Type",true,true,java.lang.String.class,1);

  }

  public static final String statusAssessed = "A";
  public static final String statusSent = "S";
  public static final String statusReceived = "R";

  public static String getEntityTableName(){return "FIN_ASSESSMENT_ROUND";}
  public static String getNameColumnName(){return "NAME";}
  public static String getRoundStampColumnName(){return "ROUND_STAMP";}
  public static String getTotalsColumnName(){return "TOTALS";}
  public static String getStatusColumnName(){return "STATUS";}
  public static String getTypeColumnName(){return "ENTRY_TYPE";}

  public String getEntityName(){
    return getEntityTableName();
  }

  public String getName(){
    return getStringColumnValue(getNameColumnName());
  }

  public void setName(String name){
    setColumn(getNameColumnName(), name);
  }

  public Timestamp getRoundStamp(){
    return (Timestamp) getColumnValue(getRoundStampColumnName());
  }

  public void setRoundStamp(Timestamp round_stamp){
    setColumn(getRoundStampColumnName(), round_stamp);
  }

  public float getTotals(){
    return getFloatColumnValue(getTotalsColumnName());
  }

  public void setTotals(Float totals){
    setColumn(getTotalsColumnName(), totals);
  }

  public void setTotals(float totals){
    setColumn(getTotalsColumnName(), totals);
  }

   public void setStatus(String status) throws IllegalStateException {
    if ((status.equalsIgnoreCase(statusAssessed)) ||
        (status.equalsIgnoreCase(statusSent)) ||
        (status.equalsIgnoreCase(statusReceived)) )
      setColumn(getStatusColumnName(),status);
    else
      throw new IllegalStateException("Undefined state : " + status);
  }
  public String getStatus() {
    return((String)getColumnValue(getStatusColumnName()));
  }
  public void setStatusAssessed() {
    setStatus(statusAssessed);
  }
  public void setStatusSent() {
    setStatus(statusSent);
  }
  public void setStatusReceived() {
    setStatus(statusReceived);
  }

  public void setType(String type){
    setColumn(getTypeColumnName(),type);
  }

  public String getType(){
    return getStringColumnValue( getTypeColumnName());
  }


  public void setAsNew(String name){
    setName(name);
    setStatusAssessed();
    setRoundStamp(idegaTimestamp.getTimestampRightNow());
    setTotals(0);
  }

  public void setAsSent(String name){
    setName(name);
    setStatusSent();
    setRoundStamp(idegaTimestamp.getTimestampRightNow());

  }

  public void setAsReceived(String name){
    setName(name);
    setStatusReceived();
    setRoundStamp(idegaTimestamp.getTimestampRightNow());
  }

}
