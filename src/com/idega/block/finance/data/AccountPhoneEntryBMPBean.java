package com.idega.block.finance.data;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOQuery;
import com.idega.util.IWTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class AccountPhoneEntryBMPBean extends com.idega.data.GenericEntity implements com.idega.block.finance.data.AccountPhoneEntry,com.idega.block.finance.data.Entry {
  public static final String statusUnread = "U";
  public static final String statusRead = "R";
  public static final String statusBilled = "B";

  public AccountPhoneEntryBMPBean() {
    super();
  }
  public AccountPhoneEntryBMPBean(int id)throws SQLException{
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameAccountId(),"Account", true, true, Integer.class,"many-to-one",com.idega.block.finance.data.Account.class);
    addAttribute(getColumnNameAccountEntryId(),"Account_entry",true,true,Integer.class,"many-to-one",com.idega.block.finance.data.AccountEntry.class);
    addAttribute(getColumnNameCashierId(),"Cashier",true,true,Integer.class,"many-to-one",com.idega.block.finance.data.Cashier.class);
    addAttribute(getRoundIdColumnName(),"Round",true,true,java.lang.Integer.class,"many-to-one",com.idega.block.finance.data.AssessmentRound.class);
    addAttribute(getColumnNameMainNumber(),"main number",true,true,String.class);
    addAttribute(getColumnNameSubNumber(),"sub number",true,true,String.class);
    addAttribute(getColumnNamePhonedNumber(),"main number",true,true,String.class);
    addAttribute(getColumnNamePhonedStamp(),"phone stamp",true,true,java.sql.Timestamp.class);
    addAttribute(getColumnNameDayDuration(),"day duration",true,true,Integer.class);
    addAttribute(getColumnNameNightDuration(),"night duration",true,true,Integer.class);
    addAttribute(getColumnNameDuration(),"duration",true,true,Integer.class);
    addAttribute(getColumnNamePrice(),"price",true,true,Float.class);
    addAttribute(getColumnNameLastUpdated(),"Last updated",true,true,java.sql.Timestamp.class);
    addAttribute(getColumnNameStatus(),"status",true,true,String.class);
  }

  public static String getEntityTableName(){ return "FIN_PHONE_ENTRY"; }
  public static String getRoundIdColumnName(){ return "FIN_ASSESSMENT_ROUND_ID"; }
  public static String getColumnNameAccountId(){ return "FIN_ACCOUNT_ID"; }
  public static String getColumnNameAccountEntryId(){ return "FIN_ACC_ENTRY_ID"; }
  public static String getColumnNameCashierId(){ return "FIN_CASHIER_ID"; }
  public static String getColumnNameMainNumber(){ return "MAIN_NUMBER"; }
  public static String getColumnNameSubNumber(){ return "SUB_NUMBER"; }
  public static String getColumnNamePhonedNumber(){ return "PHONED_NUMBER"; }
  public static String getColumnNamePhonedStamp(){ return "PHONED_STAMP"; }
  public static String getColumnNameDayDuration(){ return "DAY_DURATION";}
  public static String getColumnNameNightDuration(){ return "NIGHT_DURATION";}
  public static String getColumnNameDuration(){ return "DURATION";}
  public static String getColumnNamePrice(){ return "TOTAL_PRICE"; }
  public static String getColumnNameLastUpdated(){ return "LAST_UPDATED"; }
  public static String getColumnNameStatus(){ return "STATUS"; }

  public String getEntityName() {
    return getEntityTableName();
  }
  public int getAccountId(){
    return getIntColumnValue(getColumnNameAccountId());
  }
  public void setAccountId(Integer account_id){
    setColumn(getColumnNameAccountId(), account_id);
  }
  public void setAccountId(int account_id){
    setColumn(getColumnNameAccountId(), account_id);
  }

  public int getAccountEntryId(){
    return getIntColumnValue(getColumnNameAccountEntryId());
  }
  public void setAccountEntryId(Integer account_id){
    setColumn(getColumnNameAccountEntryId(), account_id);
  }
  public void setAccountEntryId(int account_id){
    setColumn(getColumnNameAccountEntryId(), account_id);
  }

  public int getCashierId(){
    return getIntColumnValue(getColumnNameCashierId());
  }
  public void setCashierId(Integer member_id){
    setColumn(getColumnNameCashierId(), member_id);
  }
  public void setCashierId(int member_id){
    setColumn(getColumnNameCashierId(), member_id);
  }
  public String getMainNumber(){
    return getStringColumnValue(getColumnNameMainNumber());
  }
  public void setMainNumber(String number){
    setColumn(getColumnNameMainNumber(), number);
  }
  public String getSubNumber(){
    return getStringColumnValue(getColumnNameSubNumber());
  }
  public void setSubNumber(String number){
    setColumn(getColumnNameSubNumber(), number);
  }
  public String getPhonedNumber(){
    return getStringColumnValue(getColumnNamePhonedNumber());
  }
  public void setPhoneNumber(String number){
    setColumn(getColumnNamePhonedNumber(), number);
  }

  public Timestamp getPhonedStamp(){
    return (Timestamp) getColumnValue(getColumnNamePhonedStamp());
  }
  public void setPhonedStamp(Timestamp stamp){
    setColumn(getColumnNamePhonedStamp(), stamp);
  }
  public Timestamp getLastUpdated(){
    return (Timestamp) getColumnValue(getColumnNameLastUpdated());
  }
  public void setLastUpdated(Timestamp last_updated){
    setColumn(getColumnNameLastUpdated(), last_updated);
  }
  public void setPrice(float price){
    setColumn(getColumnNamePrice(), price);
  }
  public void setPrice(Float price){
    setColumn(getColumnNamePrice(), price);
  }
  public float getPrice(){
    return getFloatColumnValue(getColumnNamePrice());
  }

  public void setDayDuration(int seconds){
    setColumn(getColumnNameDayDuration(),seconds);
  }
  public void setDayDuration(Integer seconds){
    setColumn(getColumnNameDayDuration(),seconds);
  }
  public int getDayDuration(){
    return getIntColumnValue(getColumnNameDayDuration());
  }

  public void setDuration(int seconds){
    setColumn(getColumnNameDuration(),seconds);
  }
  public void setDuration(Integer seconds){
    setColumn(getColumnNameDuration(),seconds);
  }
  public int getDuration(){
    return getIntColumnValue(getColumnNameDuration());
  }

  public void setNightDuration(int seconds){
    setColumn(getColumnNameNightDuration(),seconds);
  }
  public void setNightDuration(Integer seconds){
    setColumn(getColumnNameNightDuration(),seconds);
  }
  public int getNightDuration(){
    return getIntColumnValue(getColumnNameNightDuration());
  }

  public int getRoundId(){
    return getIntColumnValue(getRoundIdColumnName());
  }
  public void setRoundId(Integer round){
    setColumn(getRoundIdColumnName(), round);
  }
  public void setRoundId(int round){
    setColumn(getRoundIdColumnName(), round);
  }

  public String getStatus(){
    return getStringColumnValue(getColumnNameStatus());
  }
  public void setStatus(String status) throws IllegalStateException {
    if ((status.equalsIgnoreCase(statusUnread)) ||
        (status.equalsIgnoreCase(statusRead)) ||
        (status.equalsIgnoreCase(statusBilled))){
      setColumn(getColumnNameStatus(),status);
      setLastUpdated(com.idega.util.IWTimestamp.getTimestampRightNow());
    }
    else
      throw new IllegalStateException("Undefined state : " + status);
  }

  // interface specific:
  public String getType(){
    return typePhone;
  }
  public String getFieldNameLastUpdated(){
    return getColumnNameLastUpdated();
  }
  public String getFieldNameAccountId(){
    return getColumnNameAccountId();
  }
  public String getTableName(){
    return getEntityTableName();
  }
   public String getFieldNameStatus(){
    return getColumnNameStatus();
  }
   
   public Collection ejbFindByAccountAndStatus(Integer accountID,String status, Date fromDate,Date toDate)throws FinderException{
   		IDOQuery query = super.idoQueryGetSelect().appendWhereEquals(getColumnNameAccountId(),accountID);
   		if(status!=null){
   			query.appendAndEquals(getColumnNameStatus(),status);
   		}
   		if(fromDate!=null && toDate!=null){
   			IWTimestamp from = new IWTimestamp(fromDate);
   			IWTimestamp to = new IWTimestamp(toDate);
   			to.setTime(23,59,59);
   			query.appendAnd();
   			query.appendWithinStamps(getColumnNamePhonedStamp(), from.getTimestamp(),to.getTimestamp());
   		}
   		return super.idoFindPKsByQuery(query);
   }
   
   public Collection ejbFindUnbilledByAccountAndPeriod(Integer accountID,Date fromDate,Date toDate)throws FinderException{
   		IDOQuery 	query = super.idoQueryGetSelect();
   		query.appendWhereIsNull(getColumnNameAccountEntryId());
   		if(accountID!=null){
   			query.appendAndEquals(getColumnNameAccountId(),accountID);
   		}
   		IWTimestamp to = new IWTimestamp(toDate);
   		to.setTime(23,59,59);
   		query.appendAnd().appendWithinDates(getColumnNamePhonedStamp(),fromDate,to.getDate());
   		return super.idoFindPKsByQuery(query);
   }
   
   public Collection ejbFindByPhoneNumber(String number)throws FinderException{
   		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEqualsQuoted(getColumnNameSubNumber(),number));
   }

}

