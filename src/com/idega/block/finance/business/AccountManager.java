package com.idega.block.finance.business;

import com.idega.block.finance.data.*;
import com.idega.util.idegaTimestamp;
import com.idega.data.genericentity.Member;
import com.idega.core.user.data.User;
import java.sql.SQLException;
import com.idega.data.EntityFinder;
import com.idega.data.GenericEntity;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;
/**
 * Title:        AccountManager
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class AccountManager {

  private Account eAccount;
  private Member eMember;

  public AccountManager() {

  }

  public static Account[] findAccounts(int iUserId){
    Account[] A;
    try{
       A = (Account[])new Account().findAllByColumn("ic_user_id",iUserId);
    }
    catch(Exception e){A=null;}
    return A;
  }

  public static List listOfAccounts(int iUserId){
    List A = null;
    try{
       A = EntityFinder.findAllByColumn(new Account(),"ic_user_id",iUserId);
    }
    catch(Exception e){A=null;}
    return A;
  }

  public static List listOfAccounts(int iUserId,String sType){
    List A = null;
    try{
      Account a = new Account();
       A = EntityFinder.findAllByColumn(a,a.getUserIdColumnName(),String.valueOf(iUserId),a.getTypeColumnName(),sType);
    }
    catch(Exception e){A=null;}
    return A;
  }

  public static List listOfAccounts(){
    List A = null;
    try{
       A = EntityFinder.findAll(new Account());
    }
    catch(Exception e){A=null;}
    return A;
  }

  public static List listOfAccountEntries(int iAccountId,idegaTimestamp from,idegaTimestamp to){
    return listOfAccEntries(iAccountId,new AccountEntry(), from,to);
  }

  public static List listOfPhoneEntries(int iAccountId,idegaTimestamp from,idegaTimestamp to){
    return listOfAccEntries(iAccountId,new AccountPhoneEntry(), from,to);
  }
  private static List listOfAccEntries(int iAccountId,Entry entry,idegaTimestamp from,idegaTimestamp to){
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append(entry.getTableName());
    sql.append(" where ");
    sql.append(entry.getFieldNameAccountId());
    sql.append(" = ");
    sql.append(iAccountId);
    sql.append(" and ");
    sql.append(entry.getFieldNameLastUpdated());
    sql.append(" >= '");
    sql.append(from.getSQLDate());
    sql.append("' and ");
    sql.append(entry.getFieldNameLastUpdated());
    sql.append(" <= '");
    sql.append(to.getSQLDate());
    sql.append(" 23:59:59'");
    //System.err.println(sql.toString());
    List A = null;
    try{
      if(entry.getType().equals(entry.typeFinancial))
        A = EntityFinder.findAll(new AccountEntry(),sql.toString());
      else if(entry.getType().equals(entry.typePhone)){
        A = EntityFinder.findAll(new AccountPhoneEntry(),sql.toString());
      }
    }
    catch(Exception e){A=null;}
    return A;
  }

  public static List listOfAccountKeys(){
    try {
      return EntityFinder.findAll(new AccountKey());
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List listOfTariffKeys(){
    try {
      return EntityFinder.findAll(new TariffKey());
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static Hashtable hashOfAccountKeys(){
    List L = listOfAccountKeys();
    if(L != null){
      int len = L.size();
      Hashtable H = new Hashtable(len);
      for (int i = 0; i < len; i++) {
        AccountKey AK = (AccountKey) L.get(i);
        H.put(new Integer(AK.getID()),AK);
      }
      return H;
    }
    else
      return null;
  }

  public static Hashtable hashOfTariffKeys(){
    List L = listOfTariffKeys();
    if(L != null){
      int len = L.size();
      Hashtable H = new Hashtable(len);
      for (int i = 0; i < len; i++) {
        TariffKey AK = (TariffKey) L.get(i);
        H.put(new Integer(AK.getID()),AK);
      }
      return H;
    }
    else
      return null;
  }

  public static List listOfKeySortedEntries(int iAccountId,idegaTimestamp from,idegaTimestamp to){
    Hashtable acckeys = hashOfAccountKeys();
    Hashtable takeys = hashOfTariffKeys();
    if(acckeys != null && takeys != null){
      List entries = listOfAccountEntries(iAccountId,from,to);
      if(entries != null){
        int len = entries.size();
        Hashtable hash = new Hashtable(len);
        AccountEntry AE;
        for (int i = 0; i < len; i++) {
          AE = (AccountEntry) entries.get(i);
          Integer AEid = new Integer(AE.getAccountKeyId());
          if(acckeys.containsKey(AEid)){
            AccountKey AK = (AccountKey) acckeys.get(AEid);
            Integer AKid = new Integer(AK.getTariffKeyId());
            TariffKey TK = (TariffKey) takeys.get(AKid);
            // have to add amounts
            if(hash.containsKey(AKid)){
              AccountEntry a = (AccountEntry)hash.get(AKid);
              a.setPrice(a.getPrice()+AE.getPrice());
            }
            else{
              AE.setName(TK.getName());
              AE.setInfo(TK.getInfo());
              hash.put(AKid,AE);
            }
          }
        }
        Vector V = new Vector(hash.values());
        return V;
      }
      else
        return null;
    }
    else
      return null;
  }
  public  static Account makeNewAccount(int iUserId, String sName,String sExtra, int iCashierId,String type){
    Account A = new Account();
    A.setBalance(0);
    A.setCreationDate(idegaTimestamp.getTimestampRightNow() );
    A.setLastUpdated(idegaTimestamp.getTimestampRightNow()) ;
    A.setUserId(iUserId);
    A.setName(sName) ;
    A.setExtraInfo(sExtra);
    A.setCashierId(iCashierId);
    A.setValid(true);
    A.setType(type);
    try{
      A.insert();
    }
    catch(SQLException sql){A = null;}
    return A;
  }

  public static Account makeNewFinanceAccount(int iUserId, String sName,String sExtra, int iCashierId){
    return makeNewAccount(iUserId,sName,sExtra,iCashierId,Account.typeFinancial);
  }

  public static Account makeNewPhoneAccount(int iUserId, String sName,String sExtra, int iCashierId){
    return makeNewAccount(iUserId,sName,sExtra,iCashierId,Account.typePhone);
  }

  public  static Account makeNewAccount(int iUserId, String sName,String sExtra, int iCashierId){
   return makeNewAccount(iUserId,sName,sExtra,iCashierId,"");
  }
}