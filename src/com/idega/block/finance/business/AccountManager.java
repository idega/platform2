package com.idega.block.finance.business;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.idega.block.finance.data.Account;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.data.AccountPhoneEntry;
import com.idega.block.finance.data.Entry;
import com.idega.block.finance.data.TariffKey;
import com.idega.data.EntityFinder;
import com.idega.data.genericentity.Member;
import com.idega.util.IWTimestamp;
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

  public static List listOfAccountEntries( int iAssessmentRoundId){
    try {
      return EntityFinder.findAllByColumnOrdered(((com.idega.block.finance.data.AccountEntryHome)com.idega.data.IDOLookup.getHomeLegacy(AccountEntry.class)).createLegacy(),com.idega.block.finance.data.AccountEntryBMPBean.getRoundIdColumnName(),String.valueOf(iAssessmentRoundId) ,com.idega.block.finance.data.AccountEntryBMPBean.getAccountIdColumnName());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public static List listOfAccountEntries(int iAccountId,IWTimestamp from,IWTimestamp to){
    return listOfAccEntries(iAccountId,((com.idega.block.finance.data.AccountEntryHome)com.idega.data.IDOLookup.getHomeLegacy(AccountEntry.class)).createLegacy(), from,to,null);
  }
  public static List listOfPhoneEntries(int iAccountId,IWTimestamp from,IWTimestamp to){
    return listOfPhoneEntries(iAccountId, from,to,null);
  }
  public static List listOfPhoneEntries(int iAccountId,IWTimestamp to,String status){
    return listOfPhoneEntries(iAccountId,null,to,status);
  }
  private static List listOfAccEntries(int iAccountId,Entry entry,IWTimestamp from,IWTimestamp to,String status){
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append(entry.getTableName());
    sql.append(" where ");
    sql.append(entry.getFieldNameAccountId());
    sql.append(" = ");
    sql.append(iAccountId);
    if(from !=null){
      sql.append(" and ");
      sql.append(entry.getFieldNameLastUpdated());
      sql.append(" >= '");
      sql.append(from.getSQLDate());
      sql.append("'");
    }
    if(to != null){
      sql.append(" and ");
      sql.append(entry.getFieldNameLastUpdated());
      sql.append(" <= '");
      sql.append(to.getSQLDate());
      sql.append(" 23:59:59'");
    }
    if(status!=null){
      sql.append(" and ");
      sql.append(entry.getFieldNameStatus());
      sql.append(" = '");
      sql.append(status);
      sql.append("'");
    }
    //System.err.println(sql.toString());
    List A = null;
    try{
      if(entry.getType().equals(entry.typeFinancial))
        A = EntityFinder.findAll(((com.idega.block.finance.data.AccountEntryHome)com.idega.data.IDOLookup.getHomeLegacy(AccountEntry.class)).createLegacy(),sql.toString());
      else if(entry.getType().equals(entry.typePhone)){
        A = EntityFinder.findAll(((com.idega.block.finance.data.AccountPhoneEntryHome)com.idega.data.IDOLookup.getHomeLegacy(AccountPhoneEntry.class)).createLegacy(),sql.toString());
      }
    }
    catch(Exception e){A=null;}
    return A;
  }

  private static List listOfPhoneEntries(int iAccountId,IWTimestamp from,IWTimestamp to,String status){
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getEntityTableName());
    sql.append(" where ");
    sql.append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getColumnNameAccountId());
    sql.append(" = ");
    sql.append(iAccountId);
    if(from !=null){
      sql.append(" and ");
      sql.append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getColumnNamePhonedStamp());
      sql.append(" >= '");
      sql.append(from.getSQLDate());
      sql.append("'");
    }
    if(to != null){
      sql.append(" and ");
      sql.append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getColumnNamePhonedStamp());
      sql.append(" <= '");
      sql.append(to.getSQLDate());
      sql.append(" 23:59:59'");
    }
    if(status!=null){
      sql.append(" and ");
      sql.append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getColumnNameStatus());
      sql.append(" = '");
      sql.append(status);
      sql.append("'");
    }
    //System.err.println(sql.toString());
    List A = null;
    try{
        A = EntityFinder.findAll(((com.idega.block.finance.data.AccountPhoneEntryHome)com.idega.data.IDOLookup.getHomeLegacy(AccountPhoneEntry.class)).createLegacy(),sql.toString());
    }
    catch(Exception e){A=null;}
    return A;
  }



  public static List listOfAccountKeys(){
   return FinanceFinder.getInstance().listOfAccountKeys();
  }

  public static List listOfTariffKeys(){
    return FinanceFinder.getInstance().listOfTariffKeys();
  }

  public static Map hashOfAccountKeys(){
    return FinanceFinder.getInstance().mapOfAccountKeys();
  }

  public static Map hashOfTariffKeys(){
    return FinanceFinder.getInstance().mapOfTariffKeys();
  }

  public static List listOfKeySortedEntries(int iAccountId,IWTimestamp from,IWTimestamp to){
    Map acckeys = hashOfAccountKeys();
    Map takeys = hashOfTariffKeys();
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
              a.setTotal(a.getTotal()+AE.getTotal());
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

  public  static Account makeNewAccount(int iUserId, String sName,String sExtra, int iCashierId,String type,int iCategoryId)throws SQLException,java.rmi.RemoteException,javax.ejb.CreateException{
    Account A = ((com.idega.block.finance.data.AccountHome)com.idega.data.IDOLookup.getHome(Account.class)).create();
    A.setBalance(0);
    A.setCreationDate(IWTimestamp.getTimestampRightNow() );
    A.setLastUpdated(IWTimestamp.getTimestampRightNow()) ;
    A.setUserId(iUserId);
    A.setName(sName) ;
    A.setExtraInfo(sExtra);
    //if(iCashierId > 0)
    A.setCashierId(iCashierId);
    A.setValid(true);
    A.setType(type);
    A.setCategoryId(iCategoryId);

    A.store();
    //System.err.println("account id "+A.getID());

    return A;
  }

  public static Account makeNewFinanceAccount(int iUserId, String sName,String sExtra, int iCashierId,int iCategoryId)throws Exception{
    return makeNewAccount(iUserId,sName,sExtra,iCashierId,com.idega.block.finance.data.AccountBMPBean.typeFinancial,iCategoryId);
  }

  public static Account makeNewPhoneAccount(int iUserId, String sName,String sExtra, int iCashierId,int iCategoryId)throws Exception{
    return makeNewAccount(iUserId,sName,sExtra,iCashierId,com.idega.block.finance.data.AccountBMPBean.typePhone,iCategoryId);
  }

  public  static Account makeNewAccount(int iUserId, String sName,String sExtra, int iCashierId,int iCategoryId)throws Exception{
   return makeNewAccount(iUserId,sName,sExtra,iCashierId,"",iCategoryId);
  }
}
