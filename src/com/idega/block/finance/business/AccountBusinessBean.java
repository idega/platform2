package com.idega.block.finance.business;

import com.idega.business.IBOServiceBean;
import com.idega.util.IWTimestamp;
import java.util.*;
import com.idega.data.*;
import com.idega.block.finance.data.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class AccountBusinessBean extends IBOServiceBean implements AccountBusiness {

  public AccountHome getAccountHome()throws java.rmi.RemoteException{
    return (AccountHome) IDOLookup.getHome(Account.class);
  }

  public Account getAccount(int iAccountId)throws java.rmi.RemoteException{
    try{
    return getAccountHome().findByPrimaryKey(new Integer(iAccountId));
    }
    catch(javax.ejb.FinderException ex){
      throw new java.rmi.RemoteException(ex.getMessage());
    }
  }

  public Collection listOfAccounts(int iUserId){
    Collection A = null;
    try{
      AccountHome aHome = (AccountHome)IDOLookup.getHome(Account.class);
      A = aHome.findAllByUserId(iUserId);
      // A = EntityFinder.findAllByColumn(((com.idega.block.finance.data.AccountHome)com.idega.data.IDOLookup.getHomeLegacy(Account.class)).createLegacy(),"ic_user_id",iUserId);
    }
    catch(Exception e){A=null;}
    return A;
  }

  public Collection listOfAccounts(int iUserId,String sType){

    try{
      AccountHome aHome = (AccountHome)IDOLookup.getHome(Account.class);
      return aHome.findAllByUserIdAndType(iUserId,sType);
      //Account a = ((com.idega.block.finance.data.AccountHome)com.idega.data.IDOLookup.getHomeLegacy(Account.class)).createLegacy();
     //  A = EntityFinder.findAllByColumn(a,com.idega.block.finance.data.AccountBMPBean.getUserIdColumnName(),String.valueOf(iUserId),com.idega.block.finance.data.AccountBMPBean.getTypeColumnName(),sType);
    }
    catch(Exception e){}
    return null;
  }

  public Collection listOfAccounts(){
    /*try{
       //AccountHome aHome = (AccountHome)IDOLookup.getHome(Account.class);

       //A = EntityFinder.findAll(((com.idega.block.finance.data.AccountHome)com.idega.data.IDOLookup.getHomeLegacy(Account.class)).createLegacy());
    }
    catch(Exception e){}*/
    return null;
  }

  public  List listOfAccountEntries( int iAssessmentRoundId){
    try {
      return EntityFinder.findAllByColumnOrdered(((com.idega.block.finance.data.AccountEntryHome)com.idega.data.IDOLookup.getHomeLegacy(AccountEntry.class)).createLegacy(),com.idega.block.finance.data.AccountEntryBMPBean.getRoundIdColumnName(),String.valueOf(iAssessmentRoundId) ,com.idega.block.finance.data.AccountEntryBMPBean.getAccountIdColumnName());
    }
    catch (java.sql.SQLException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public  List listOfAccountEntries(int iAccountId,IWTimestamp from,IWTimestamp to){
    return listOfAccEntries(iAccountId,((com.idega.block.finance.data.AccountEntryHome)com.idega.data.IDOLookup.getHomeLegacy(AccountEntry.class)).createLegacy(), from,to,null);
  }
  public  List listOfPhoneEntries(int iAccountId,IWTimestamp from,IWTimestamp to){
    return listOfPhoneEntries(iAccountId, from,to,null);
  }
  public  List listOfPhoneEntries(int iAccountId,IWTimestamp to,String status){
    return listOfPhoneEntries(iAccountId,null,to,status);
  }
  private  List listOfAccEntries(int iAccountId,Entry entry,IWTimestamp from,IWTimestamp to,String status){
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

  private  List listOfPhoneEntries(int iAccountId,IWTimestamp from,IWTimestamp to,String status){
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



  public  List listOfAccountKeys(){
   return FinanceFinder.getInstance().listOfAccountKeys();
  }

  public  List listOfTariffKeys(){
    return FinanceFinder.getInstance().listOfTariffKeys();
  }

  public  Map hashOfAccountKeys(){
    return FinanceFinder.getInstance().mapOfAccountKeys();
  }

  public Map hashOfTariffKeys(){
    return FinanceFinder.getInstance().mapOfTariffKeys();
  }

  public List listOfKeySortedEntries(int iAccountId,IWTimestamp from,IWTimestamp to){
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

  public  Account makeNewAccount(int iUserId, String sName,String sExtra, int iCashierId,String type,int iCategoryId)throws java.rmi.RemoteException,javax.ejb.CreateException{
    AccountHome aHome = (AccountHome) IDOLookup.getHome(Account.class);
    Account A = aHome.create();
    //Account A = ((com.idega.block.finance.data.AccountHome)com.idega.data.IDOLookup.getHomeLegacy(Account.class)).createLegacy();
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
    A.setCashierId(iCategoryId);

    A.store();
    //System.err.println("account id "+A.getID());

    return A;
  }

  public Account makeNewFinanceAccount(int iUserId, String sName,String sExtra, int iCashierId,int iCategoryId)throws java.rmi.RemoteException,javax.ejb.CreateException{
    return makeNewAccount(iUserId,sName,sExtra,iCashierId,com.idega.block.finance.data.AccountBMPBean.typeFinancial,iCategoryId);
  }

  public Account makeNewPhoneAccount(int iUserId, String sName,String sExtra, int iCashierId,int iCategoryId)throws  java.rmi.RemoteException,javax.ejb.CreateException{
    return makeNewAccount(iUserId,sName,sExtra,iCashierId,com.idega.block.finance.data.AccountBMPBean.typePhone,iCategoryId);
  }

  public Account makeNewAccount(int iUserId, String sName,String sExtra, int iCashierId,int iCategoryId)throws  java.rmi.RemoteException,javax.ejb.CreateException{
   return makeNewAccount(iUserId,sName,sExtra,iCashierId,"",iCategoryId);
  }
}