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
    addAttribute("name","Heiti",true,true,"java.lang.String");
    addAttribute("price", "Verð", true, true, "java.lang.Integer");
    addAttribute("info","Athugasemd",true,true,"java.lang.String");
    addAttribute("tariff_key_id","GjaldaLiður",true,true,"java.lang.Integer","one-to-many","com.idega.block.finance.data.TariffKey");
    addAttribute("account_key_id","Bókhaldsliður",true,true,"java.lang.Integer","one-to-many","com.idega.block.finance.data.AccountKey");
    addAttribute("from_date","Upphafsdags",true,true,"java.sql.Timestamp");
    addAttribute("to_date","Lokadags",true,true,"java.sql.Timestamp");
    addAttribute("tariff_attribute","",true,true,"java.lang.String");
    addAttribute("useindex","Gild",true,true,"java.lang.Boolean");

  }
  public String getEntityName() {
    return "tariff";
  }
  public String getName(){
    return getStringColumnValue("name");
  }
  public void setName(String name){
    setColumn("name", name);
  }
  public String getTariffAttribute(){
    return getStringColumnValue("tariff_attribute");
  }
  public void setTariffAttribute(String attribute){
    setColumn("tariff_attribute", attribute);
  }
  public int getPrice(){
    return getIntColumnValue("price");
  }
  public void setPrice(int price){
    setColumn("price",price);
  }
  public void setPrice(Integer price){
    setColumn("price",price);
  }
  public String getInfo(){
    return getStringColumnValue("info");
  }
  public void setInfo(String info){
    setColumn("info", info);
  }
  public int getAccountKeyId(){
    return getIntColumnValue("account_key_id");
  }
  public void setAccountKeyId(Integer account_key_id){
    setColumn("account_key_id", account_key_id);
  }
  public void setAccountKeyId(int account_key_id){
    setColumn("account_key_id", account_key_id);
  }
  public int getTariffKeyId(){
    return getIntColumnValue("tariff_key_id");
  }
  public void setTariffKeyId(Integer tariff_key_id){
    setColumn("tariff_key_id", tariff_key_id);
  }
  public void setTariffKeyId(int tariff_key_id){
    setColumn("tariff_key_id", tariff_key_id);
  }
  public Timestamp getUseFromDate(){
    return (Timestamp) getColumnValue("from_date");
  }
  public void setUseFromDate(Timestamp use_date){
    setColumn("from_date",use_date);
  }
   public Timestamp getUseToDate(){
    return (Timestamp) getColumnValue("to_date");
  }
  public void setUseToDate(Timestamp use_date){
    setColumn("to_date",use_date);
  }

  public void setUseIndex(boolean useindex){
    setColumn("useindex",useindex);
  }
  public boolean getUseIndex(){
    return getBooleanColumnValue("useindex");
  }

}