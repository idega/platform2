//idega 2000 - aron

package com.idega.block.finance.data;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.util.IWTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class AssessmentRoundBMPBean extends com.idega.block.category.data.CategoryEntityBMPBean implements com.idega.block.finance.data.AssessmentRound {

  public AssessmentRoundBMPBean(){
    super();
  }

  public AssessmentRoundBMPBean(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes(){
	addAttribute(getIDColumnName());
    addAttribute(getColumnTariffGroupId(),"Tariff group",true,true,Integer.class,"",TariffGroup.class);
    addAttribute(getNameColumnName(),"Name",true,true,java.lang.String.class);
    addAttribute(getRoundStampColumnName(),"Round stamp",true,true,java.sql.Timestamp.class);
    addAttribute(getPeriodFromColumnName(),"Period from date",true,true,java.sql.Date.class);
    addAttribute(getPeriodToColumnName(),"Period to date",true,true,java.sql.Date.class);
    addAttribute(getDueDateColumnName(),"Due date",true,true,java.sql.Date.class);
    addAttribute(getTotalsColumnName(), "Totals", true, true, java.lang.Float.class);
    addAttribute(getStatusColumnName(),"Status",true,true,java.lang.String.class,1);
    addAttribute(getAccountCountColumnName(),"Account count",true,true,java.lang.Integer.class);


  }


  public static String getEntityTableName(){return "FIN_ASSESSMENT_ROUND";}
  public static String getColumnTariffGroupId(){return "FIN_TARIFF_GROUP_ID";}
  public static String getNameColumnName(){return "NAME";}
  public static String getRoundStampColumnName(){return "ROUND_STAMP";}
  public static String getTotalsColumnName(){return "TOTALS";}
  public static String getStatusColumnName(){return "STATUS";}
  public static String getTypeColumnName(){return "ENTRY_TYPE";}
  public static String getAccountCountColumnName(){return "ACC_COUNT";}
  public static String getPeriodFromColumnName(){return "PERIOD_FROM";}
  public static String getPeriodToColumnName(){return "PERIOD_TO";}
  public static String getDueDateColumnName(){return "DUE_DATE";}

  public String getEntityName(){
    return getEntityTableName();
  }

  public String getName(){
    return getStringColumnValue(getNameColumnName());
  }

  public void setName(String name){
    setColumn(getNameColumnName(), name);
  }

  public int getAccountCount(){
    return getIntColumnValue(getAccountCountColumnName());
  }

  public void setAccountCount(int count){
    setColumn( getAccountCountColumnName(),count);
  }

  public Timestamp getRoundStamp(){
    return (Timestamp) getColumnValue(getRoundStampColumnName());
  }

  public void setRoundStamp(Timestamp round_stamp){
    setColumn(getRoundStampColumnName(), round_stamp);
  }
  
  public Date getDueDate(){
  	return (Date) getColumnValue(getDueDateColumnName());
  }
  
  public void setDueDate(Date date){
  	setColumn(getDueDateColumnName(),date);
  }
  
  public Date getPeriodToDate(){
  	return (Date) getColumnValue(getPeriodToColumnName());
  }
  
  public void setPeriodToDate(Date date){
  	setColumn(getPeriodToColumnName(),date);
  }
  public Date getPeriodFromDate(){
  	return (Date) getColumnValue(getPeriodFromColumnName());
  }
  
  public void setPeriodFromDate(Date date){
  	setColumn(getPeriodFromColumnName(),date);
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
    if ((status.equalsIgnoreCase(AssessmentStatus.ASSESSED)) ||
        (status.equalsIgnoreCase(AssessmentStatus.PUBLISHED)) ||
        (status.equalsIgnoreCase(AssessmentStatus.SENT))||
		(status.equalsIgnoreCase(AssessmentStatus.RECEIVED)))
      setColumn(getStatusColumnName(),status);
    else
      throw new IllegalStateException("Undefined state : " + status);
  }
  public String getStatus() {
    return((String)getColumnValue(getStatusColumnName()));
  }
  public void setStatusAssessed() {
    setStatus(AssessmentStatus.ASSESSED);
  }
  public void setStatusSent() {
    setStatus(AssessmentStatus.SENT);
  }
  public void setStatusReceived() {
    setStatus(AssessmentStatus.RECEIVED);
  }
  public void setStatusPublished() {
    setStatus(AssessmentStatus.PUBLISHED);
  }

  public void setType(String type){
    setColumn(getTypeColumnName(),type);
  }

  public String getType(){
    return getStringColumnValue( getTypeColumnName());
  }

  public int getTariffGroupId(){
    return getIntColumnValue( getColumnTariffGroupId() );
  }
  public void setTariffGroupId(int groupId){
    setColumn(getColumnTariffGroupId(),groupId);
  }


  public void setAsNew(String name){
    setName(name);
    setStatusAssessed();
    setRoundStamp(IWTimestamp.getTimestampRightNow());
    setTotals(0);
  }

  public void setAsSent(String name){
    setName(name);
    setStatusSent();
    setRoundStamp(IWTimestamp.getTimestampRightNow());

  }

  public void setAsReceived(String name){
    setName(name);
    setStatusReceived();
    setRoundStamp(IWTimestamp.getTimestampRightNow());
  }
  
  public void setAsPublished(String name){
    setName(name);
    setStatusPublished();
    setRoundStamp(IWTimestamp.getTimestampRightNow());
  }
  
  public Collection ejbFindByCategoryAndTariffGroup(Integer categoryID,Integer groupID,Date fromDate,Date toDate,String status,int resultSetSize,int startIndex )throws FinderException{
  		IDOQuery query = super.idoQueryGetSelect().appendWhereEquals(getColumnCategoryId(),categoryID);
  		if(groupID!=null)
  		query.appendAndEquals(getColumnTariffGroupId(),groupID);
  		if(fromDate!=null && toDate!=null)
  			query.appendAnd().appendWithinDates(getRoundStampColumnName(),fromDate,toDate);
  		if(status!=null)
  			query.appendAndEqualsQuoted(getStatusColumnName(),status);
  		query.appendOrderByDescending(getRoundStampColumnName());
  		return super.idoFindPKsByQuery(query,resultSetSize,startIndex);
  }
  
  public int ejbHomeGetCountByCategoryAndTariffGroup(Integer categoryID,Integer groupID,Date fromDate,Date toDate,String status)throws IDOException{
  		IDOQuery query = super.idoQueryGetSelectCount().appendWhereEquals(getColumnCategoryId(),categoryID);
  		if(groupID!=null)
		query.appendAndEquals(getColumnTariffGroupId(),groupID);
		if(toDate!=null && fromDate!=null)
			query.appendAnd().appendWithinDates(getRoundStampColumnName(),fromDate,toDate);
		if(status!=null)
			query.appendAndEqualsQuoted(getStatusColumnName(),status);
		return super.idoGetNumberOfRecords(query);
  }
  


}