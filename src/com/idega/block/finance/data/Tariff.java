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

public class Tariff extends GenericEntity {

  public Tariff() {
  }
  public Tariff(int id) throws SQLException {
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getNameColumnName(),"Heiti",true,true,"java.lang.String");
    addAttribute(getPriceColumnName(), "Verð", true, true, "java.lang.Integer");
    addAttribute(getInfoColumnName(),"Athugasemd",true,true,"java.lang.String",4000);
    addAttribute(getAccountKeyIdColumnName(),"Bókhaldsliður",true,true,"java.lang.Integer","one-to-many","com.idega.block.finance.data.AccountKey");
    addAttribute(getFromdateColumnName(),"Upphafsdags",true,true,"java.sql.Timestamp");
    addAttribute(getToDateColumnName(),"Lokadags",true,true,"java.sql.Timestamp");
    addAttribute(getAttributeColumnName(),"",true,true,"java.lang.String");
    addAttribute(getUseIndexColumnName(),"Gild",true,true,"java.lang.Boolean");
    addAttribute(getInUseColumnName(),"In Use",true,true,"java.lang.Boolean");

  }
  public static String getTariffEntityName(){return "FIN_TARIFF";}
  public static String getAccountKeyIdColumnName(){return "FIN_ACC_KEY_ID";}
  public static String getNameColumnName(){return "NAME";}
  public static String getInfoColumnName(){return "INFO";}
  public static String getPriceColumnName(){return "PRICE";}
  public static String getFromdateColumnName(){return "FROM_DATE";}
  public static String getToDateColumnName(){return "TO_DATE";}
  public static String getInUseColumnName(){return "IN_USE";}
  public static String getUseIndexColumnName(){return "USE_INDEX";}
  public static String getAttributeColumnName(){return "ATTRIBUTE";}

  public String getEntityName() {
    return getTariffEntityName();
  }
  public String getName(){
    return getStringColumnValue(getNameColumnName());
  }
  public void setName(String name){
    setColumn(getNameColumnName(), name);
  }
  public String getTariffAttribute(){
    return getStringColumnValue(getAttributeColumnName());
  }
  public void setTariffAttribute(String attribute){
    setColumn(getAttributeColumnName(), attribute);
  }
  public int getPrice(){
    return getIntColumnValue(getPriceColumnName());
  }
  public void setPrice(int price){
    setColumn(getPriceColumnName(),price);
  }
  public void setPrice(Integer price){
    setColumn(getPriceColumnName(),price);
  }
  public String getInfo(){
    return getStringColumnValue(getInfoColumnName());
  }
  public void setInfo(String info){
    setColumn(getInfoColumnName(), info);
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

  public Timestamp getUseFromDate(){
    return (Timestamp) getColumnValue(getFromdateColumnName());
  }
  public void setUseFromDate(Timestamp use_date){
    setColumn(getFromdateColumnName(),use_date);
  }
   public Timestamp getUseToDate(){
    return (Timestamp) getColumnValue(getToDateColumnName());
  }
  public void setUseToDate(Timestamp use_date){
    setColumn(getToDateColumnName(),use_date);
  }

  public void setUseIndex(boolean useindex){
    setColumn(getUseIndexColumnName(),useindex);
  }
  public boolean getUseIndex(){
    return getBooleanColumnValue(getUseIndexColumnName());
  }
  public void setInUse(boolean useindex){
    setColumn(getInUseColumnName(),useindex);
  }
  public boolean getInUse(){
    return getBooleanColumnValue(getInUseColumnName());
  }
}