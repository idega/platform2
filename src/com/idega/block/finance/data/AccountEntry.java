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

public class AccountEntry extends GenericEntity {

  public AccountEntry() {
    super();
  }
  public AccountEntry(int id)throws SQLException{
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getAccountIdColumnName(), "Account", true, true, "java.lang.Integer","many-to-one","com.idega.block.finance.data.Account");
    addAttribute(getNameColumnName(),"Name",true,true,"java.lang.String");
    addAttribute(getInfoColumnName(),"Info",true,true,"java.lang.String");
    addAttribute(getAccountKeyIdColumnName(),"Account key",true,true,"java.lang.Integer");
    addAttribute(getEntryKeyIdColumnName(),"Entry key",true,true,"java.lang.String");
    addAttribute(getPriceColumnName(), "Amount", true, true, "java.lang.Integer");
    addAttribute(getPaymentDateColumnName(),"Payment date",true,true,"java.sql.Timestamp");
    addAttribute(getLastUpdatedColumnName(),"Last updated",true,true,"java.sql.Timestamp");
    addAttribute(getCashierIdColumnName(),"Cashier",true,true,"java.lang.Integer");
    addAttribute(getRoundIdColumnName(),"Round",true,true,"java.lang.Integer");
  }

  public static String getEntityTableName(){return "ACCOUNT_ENTRY";}
  public static String getAccountIdColumnName(){return "ACCOUNT_ID";}
  public static String getNameColumnName(){return "NAME";}
  public static String getInfoColumnName(){return "INFO";}
  public static String getAccountKeyIdColumnName(){return "ACCOUNT_KEY_ID";}
  public static String getEntryKeyIdColumnName(){return "ENTRY_KEY_ID";}
  public static String getPriceColumnName(){return "ENTRY_KEY_ID";}
  public static String getPaymentDateColumnName() {return "PAYMENT_DATE";}
  public static String getLastUpdatedColumnName() {return "LAST_UPDATED";}
  public static String getCashierIdColumnName() {return "CASHIER_ID";}
  public static String getRoundIdColumnName() {return "ASSESSMENT_ROUND_ID";}


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
  public int getEntryKeyId(){
    return getIntColumnValue(getEntryKeyIdColumnName());
  }
  public void setEntryKeyId(Integer entry_key_id){
    setColumn(getEntryKeyIdColumnName(), entry_key_id);
  }
  public void setEntryKeyId(int entry_key_id){
    setColumn(getEntryKeyIdColumnName(), entry_key_id);
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
  public int getPrice(){
    return getIntColumnValue(getPriceColumnName());
  }
  public void setPrice(Integer price){
    setColumn(getPriceColumnName(), price);
  }
  public void setPrice(int price){
    setColumn(getPriceColumnName(), price);
  }
  public int getRound(){
    return getIntColumnValue(getRoundIdColumnName());
  }
  public void setRound(Integer round){
    setColumn(getRoundIdColumnName(), round);
  }
  public void setRound(int round){
    setColumn(getRoundIdColumnName(), round);
  }
}
