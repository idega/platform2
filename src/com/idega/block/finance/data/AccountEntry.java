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
    addAttribute("account_id", "Reikningur", true, true, "java.lang.Integer","many-to-one","com.idega.block.finance.data.Account");
    addAttribute("name","Heiti",true,true,"java.lang.String");
    addAttribute("info","Upplýsingar",true,true,"java.lang.String");
    addAttribute("tariff_key_id","GjaldaLiður",true,true,"java.lang.Integer");
    addAttribute("account_key_id","Bókhaldslykill",true,true,"java.lang.Integer");
    addAttribute("entry_key_id","Færslutegund",true,true,"java.lang.String");
    addAttribute("price", "Upphæð", true, true, "java.lang.Integer");
    addAttribute("payment_date","Greiðsludagur",true,true,"java.sql.Timestamp");
    addAttribute("last_updated","Síðast Breytt",true,true,"java.sql.Timestamp");
    addAttribute("cashier_id","Gjaldkeri",true,true,"java.lang.Integer");
  }
  public String getEntityName() {
    return "account_entry";
  }
  public int getAccountId(){
    return getIntColumnValue("account_id");
  }
  public void setAccountId(Integer account_id){
    setColumn("account_id", account_id);
  }
  public void setAccountId(int account_id){
    setColumn("account_id", account_id);
  }
  public int getEntryKeyId(){
    return getIntColumnValue("entry_key_id");
  }
  public void setEntryKeyId(Integer entry_key_id){
    setColumn("entry_key_id", entry_key_id);
  }
  public void setEntryKeyId(int entry_key_id){
    setColumn("entry_key_id", entry_key_id);
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
  public Timestamp getPaymentDate(){
    return (Timestamp) getColumnValue("payment_date");
  }
  public void setPaymentDate(Timestamp payment_date){
    setColumn("payment_date", payment_date);
  }
  public Timestamp getLastUpdated(){
    return (Timestamp) getColumnValue("last_updated");
  }
  public void setLastUpdated(Timestamp last_updated){
    setColumn("last_updated", last_updated);
  }
  public int getCashierId(){
    return getIntColumnValue("cashier_id");
  }
  public void setCashierId(Integer member_id){
    setColumn("cashier_id", member_id);
  }
  public void setCashierId(int member_id){
    setColumn("cashier_id", member_id);
  }
  public String getName(){
    return getStringColumnValue("name");
  }
  public void setName(String name){
    setColumn("name", name );
  }
  public String getInfo(){
    return getStringColumnValue("info");
  }
  public void setInfo(String info){
    setColumn("info", info);
  }
  public int getPrice(){
    return getIntColumnValue("price");
  }
  public void setPrice(Integer price){
    setColumn("price", price);
  }
  public void setPrice(int price){
    setColumn("price", price);
  }
}
