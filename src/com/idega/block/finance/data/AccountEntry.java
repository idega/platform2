package com.idega.block.finance.data;

import java.sql.*;
import com.idega.data.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class AccountEntry extends GenericEntity implements Entry {
  public static final String statusCreated = "C";
  public static final String statusBilled = "B";
  public static final String statusPayed = "P";

  public AccountEntry() {
    super();
  }
  public AccountEntry(int id)throws SQLException{
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getAccountIdColumnName(), "Account", true, true, java.lang.Integer.class,"many-to-one",com.idega.block.finance.data.Account.class);
    addAttribute(getNameColumnName(),"Name",true,true,java.lang.String.class);
    addAttribute(getInfoColumnName(),"Info",true,true,java.lang.String.class);
    addAttribute(getAccountKeyIdColumnName(),"Account key",true,true,java.lang.Integer.class,"many-to-one",com.idega.block.finance.data.AccountKey.class);
    addAttribute(getEntryGroupIdColumnName(),"Entry group",true,true,java.lang.Integer.class,"many-to-one",com.idega.block.finance.data.EntryGroup.class);
    addAttribute(getEntryTypeColumnName(),"Entry type",true,true,java.lang.String.class);
    addAttribute(getPriceColumnName(), "Amount", true, true, java.lang.Float.class);
    addAttribute(getPaymentDateColumnName(),"Payment date",true,true,java.sql.Timestamp.class);
    addAttribute(getLastUpdatedColumnName(),"Last updated",true,true,java.sql.Timestamp.class);
    addAttribute(getCashierIdColumnName(),"Cashier",true,true,java.lang.Integer.class,"many-to-one",com.idega.block.finance.data.Cashier.class);
    addAttribute(getRoundIdColumnName(),"Round",true,true,java.lang.Integer.class,"many-to-one",com.idega.block.finance.data.AssessmentRound.class);
    addAttribute(getColumnNameStatus(),"status",true,true,String.class);
  }

  public static String getEntityTableName(){ return "FIN_ACC_ENTRY"; }
  public static String getRoundIdColumnName(){ return "FIN_ASSESSMENT_ROUND_ID"; }
  public static String getEntryGroupIdColumnName(){ return "FIN_ENTRY_GROUP_ID"; }
  public static String getAccountIdColumnName(){ return "FIN_ACCOUNT_ID"; }
  public static String getCashierIdColumnName(){ return "FIN_CASHIER_ID"; }
  public static String getAccountKeyIdColumnName(){ return "FIN_ACC_KEY_ID"; }
  public static String getEntryTypeColumnName(){ return "ENTRY_TYPE"; }
  public static String getNameColumnName(){ return "NAME"; }
  public static String getInfoColumnName(){ return "INFO"; }
  public static String getPriceColumnName(){ return "PRICE"; }
  public static String getPaymentDateColumnName(){ return "PAYMENT_DATE"; }
  public static String getLastUpdatedColumnName(){ return "LAST_UPDATED"; }
  public static String getColumnNameStatus(){ return "STATUS"; }


  public String getEntityName() {
    return getEntityTableName();
  }
  public int getAccountId(){
    return getIntColumnValue(getAccountIdColumnName());
  }
  public void setAccountId(Integer account_id){
    setColumn(getAccountIdColumnName(), account_id);
  }
  public void setAccountId(int account_id){
    setColumn(getAccountIdColumnName(), account_id);
  }
  public int getEntryGroupId(){
    return getIntColumnValue(getEntryGroupIdColumnName());
  }
  public void setEntryGroupId(int entry_group_id){
    setColumn(getEntryGroupIdColumnName(), entry_group_id);
  }
  public String getEntryType(){
    return getStringColumnValue(getEntryTypeColumnName());
  }
  public void setEntryType(String entryType){
    setColumn(getEntryTypeColumnName(), entryType);
  }
  public int getAccountKeyId(){
    return getIntColumnValue(getAccountKeyIdColumnName());
  }
  public void setAccountKeyId(Integer account_key_id){
    setColumn(getAccountKeyIdColumnName(), account_key_id);
  }
  public void setAccountKeyId(int account_key_id){
    setColumn(getAccountKeyIdColumnName(), account_key_id);
  }
  public Timestamp getPaymentDate(){
    return (Timestamp) getColumnValue(getPaymentDateColumnName());
  }
  public void setPaymentDate(Timestamp payment_date){
    setColumn(getPaymentDateColumnName(), payment_date);
  }
  public Timestamp getLastUpdated(){
    return (Timestamp) getColumnValue(getLastUpdatedColumnName());
  }
  public void setLastUpdated(Timestamp last_updated){
    setColumn(getLastUpdatedColumnName(), last_updated);
  }
  public int getCashierId(){
    return getIntColumnValue(getCashierIdColumnName());
  }
  public void setCashierId(Integer member_id){
    setColumn(getCashierIdColumnName(), member_id);
  }
  public void setCashierId(int member_id){
    setColumn(getCashierIdColumnName(), member_id);
  }
  public String getName(){
    return getStringColumnValue(getNameColumnName());
  }
  public void setName(String name){
    setColumn(getNameColumnName(), name );
  }
  public String getInfo(){
    return getStringColumnValue(getInfoColumnName());
  }
  public void setInfo(String info){
    setColumn(getInfoColumnName(), info);
  }
  public float getPrice(){
    return getFloatColumnValue(getPriceColumnName());
  }
  public void setPrice(Float price){
    setColumn(getPriceColumnName(), price);
  }
  public void setPrice(float price){
    setColumn(getPriceColumnName(), price);
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
    return getStringColumnValue( getColumnNameStatus());
  }

  public void setStatus(String status) throws IllegalStateException {
    if ((status.equalsIgnoreCase(statusCreated)) ||
        (status.equalsIgnoreCase(statusCreated)) ||
        (status.equalsIgnoreCase(statusBilled))){
      setColumn(getColumnNameStatus(),status);
      setLastUpdated(com.idega.util.idegaTimestamp.getTimestampRightNow());
    }
    else
      throw new IllegalStateException("Undefined state : " + status);
  }

  // interface specific:
  public String getType(){
    return typeFinancial;
  }
  public String getFieldNameLastUpdated(){
    return getLastUpdatedColumnName();
  }
  public String getFieldNameAccountId(){
    return getAccountIdColumnName();
  }
  public String getTableName(){
    return getEntityTableName();
  }
  public String getFieldNameStatus(){
    return getColumnNameStatus();
  }
}
