package com.idega.block.finance.business;



import com.idega.block.finance.data.*;

import com.idega.util.idegaTimestamp;

import com.idega.data.genericentity.Member;

import com.idega.core.user.data.User;

import java.sql.SQLException;

import com.idega.data.EntityFinder;

import com.idega.data.GenericEntity;

import java.util.*;

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

       A = EntityFinder.findAllByColumn(a,Account.getUserIdColumnName(),String.valueOf(iUserId),Account.getTypeColumnName(),sType);

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



  public static List listOfAccountEntries( int iAssessmentRoundId){

    try {

      return EntityFinder.findAllByColumnOrdered(new AccountEntry(),AccountEntry.getRoundIdColumnName(),String.valueOf(iAssessmentRoundId) ,AccountEntry.getAccountIdColumnName());

    }

    catch (SQLException ex) {

      ex.printStackTrace();

      return null;

    }

  }



  public static List listOfAccountEntries(int iAccountId,idegaTimestamp from,idegaTimestamp to){

    return listOfAccEntries(iAccountId,new AccountEntry(), from,to,null);

  }

  public static List listOfPhoneEntries(int iAccountId,idegaTimestamp from,idegaTimestamp to){

    return listOfPhoneEntries(iAccountId, from,to,null);

  }

  public static List listOfPhoneEntries(int iAccountId,idegaTimestamp to,String status){

    return listOfPhoneEntries(iAccountId,null,to,status);

  }

  private static List listOfAccEntries(int iAccountId,Entry entry,idegaTimestamp from,idegaTimestamp to,String status){

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

        A = EntityFinder.findAll(new AccountEntry(),sql.toString());

      else if(entry.getType().equals(entry.typePhone)){

        A = EntityFinder.findAll(new AccountPhoneEntry(),sql.toString());

      }

    }

    catch(Exception e){A=null;}

    return A;

  }



  private static List listOfPhoneEntries(int iAccountId,idegaTimestamp from,idegaTimestamp to,String status){

    StringBuffer sql = new StringBuffer("select * from ");

    sql.append(AccountPhoneEntry.getEntityTableName());

    sql.append(" where ");

    sql.append(AccountPhoneEntry.getColumnNameAccountId());

    sql.append(" = ");

    sql.append(iAccountId);

    if(from !=null){

      sql.append(" and ");

      sql.append(AccountPhoneEntry.getColumnNamePhonedStamp());

      sql.append(" >= '");

      sql.append(from.getSQLDate());

      sql.append("'");

    }

    if(to != null){

      sql.append(" and ");

      sql.append(AccountPhoneEntry.getColumnNamePhonedStamp());

      sql.append(" <= '");

      sql.append(to.getSQLDate());

      sql.append(" 23:59:59'");

    }

    if(status!=null){

      sql.append(" and ");

      sql.append(AccountPhoneEntry.getColumnNameStatus());

      sql.append(" = '");

      sql.append(status);

      sql.append("'");

    }

    //System.err.println(sql.toString());

    List A = null;

    try{

        A = EntityFinder.findAll(new AccountPhoneEntry(),sql.toString());

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



  public static List listOfKeySortedEntries(int iAccountId,idegaTimestamp from,idegaTimestamp to){

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



  public  static Account makeNewAccount(int iUserId, String sName,String sExtra, int iCashierId,String type,int iCategoryId)throws SQLException{

    Account A = new Account();

    A.setBalance(0);

    A.setCreationDate(idegaTimestamp.getTimestampRightNow() );

    A.setLastUpdated(idegaTimestamp.getTimestampRightNow()) ;

    A.setUserId(iUserId);

    A.setName(sName) ;

    A.setExtraInfo(sExtra);

    //if(iCashierId > 0)

    A.setCashierId(iCashierId);

    A.setValid(true);

    A.setType(type);

    A.setCashierId(iCategoryId);



    A.insert();

    //System.err.println("account id "+A.getID());



    return A;

  }



  public static Account makeNewFinanceAccount(int iUserId, String sName,String sExtra, int iCashierId,int iCategoryId)throws SQLException{

    return makeNewAccount(iUserId,sName,sExtra,iCashierId,Account.typeFinancial,iCategoryId);

  }



  public static Account makeNewPhoneAccount(int iUserId, String sName,String sExtra, int iCashierId,int iCategoryId)throws SQLException{

    return makeNewAccount(iUserId,sName,sExtra,iCashierId,Account.typePhone,iCategoryId);

  }



  public  static Account makeNewAccount(int iUserId, String sName,String sExtra, int iCashierId,int iCategoryId)throws SQLException{

   return makeNewAccount(iUserId,sName,sExtra,iCashierId,"",iCategoryId);

  }

}