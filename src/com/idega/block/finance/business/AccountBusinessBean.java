package com.idega.block.finance.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.finance.data.Account;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.AccountHome;
import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.data.TariffKey;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.util.IWTimestamp;

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

  public Collection getUserAccounts(int iUserId){
    Collection A = null;
    try{
      AccountHome aHome = (AccountHome)IDOLookup.getHome(Account.class);
      A = aHome.findAllByUserId(iUserId);
      // A = EntityFinder.findAllByColumn(((com.idega.block.finance.data.AccountHome)com.idega.data.IDOLookup.getHomeLegacy(Account.class)).createLegacy(),"ic_user_id",iUserId);
    }
    catch(Exception e){A=null;}
    return A;
  }

  public Collection getUserAccounts(int iUserId,String sType){

    try{
      AccountHome aHome = (AccountHome)IDOLookup.getHome(Account.class);
      return aHome.findAllByUserIdAndType(iUserId,sType);
      //Account a = ((com.idega.block.finance.data.AccountHome)com.idega.data.IDOLookup.getHomeLegacy(Account.class)).createLegacy();
     //  A = EntityFinder.findAllByColumn(a,com.idega.block.finance.data.AccountBMPBean.getUserIdColumnName(),String.valueOf(iUserId),com.idega.block.finance.data.AccountBMPBean.getTypeColumnName(),sType);
    }
    catch(Exception e){}
    return null;
  }

  public Collection getAccounts(){
    /*try{
       //AccountHome aHome = (AccountHome)IDOLookup.getHome(Account.class);

       //A = EntityFinder.findAll(((com.idega.block.finance.data.AccountHome)com.idega.data.IDOLookup.getHomeLegacy(Account.class)).createLegacy());
    }
    catch(Exception e){}*/
    return null;
  }

  public Collection getAccountEntries( Integer assessmentRoundId){
    try {
    	return getFinanceService().getAccountEntryHome().findByAssessmentRound(assessmentRoundId);
      //return EntityFinder.findAllByColumnOrdered(((com.idega.block.finance.data.AccountEntryHome)com.idega.data.IDOLookup.getHomeLegacy(AccountEntry.class)).createLegacy(),com.idega.block.finance.data.AccountEntryBMPBean.getRoundIdColumnName(),String.valueOf(iAssessmentRoundId) ,com.idega.block.finance.data.AccountEntryBMPBean.getAccountIdColumnName());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public  Collection getAccountEntries(int iAccountId,IWTimestamp from,IWTimestamp to){
    return getAccountEntries(iAccountId, from,to,null,null);
  }
  public  Collection getPhoneEntries(int iAccountId,IWTimestamp from,IWTimestamp to){
    return getPhoneEntries(iAccountId, from,to,null);
  }
  public  Collection getPhoneEntries(int iAccountId,IWTimestamp to,String status){
    return getPhoneEntries(iAccountId,null,to,status);
  }
  public  Collection getAccountEntries(int iAccountId,IWTimestamp from,IWTimestamp to,String status,String roundStatus){
    try {
		/*
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
		*/
		return getFinanceService().getAccountEntryHome().findByAccountAndStatus(new Integer(iAccountId),status,from.getDate(),to.getDate(),roundStatus);
	} catch (RemoteException e) {
		e.printStackTrace();
	} catch (FinderException e) {
		e.printStackTrace();
	}
	return null;
  }

  private  Collection getPhoneEntries(int iAccountId,IWTimestamp from,IWTimestamp to,String status){
    try {
		/*
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
		*/
		return getFinanceService().getAccountPhoneEntryHome().findByAccountAndStatus(new Integer(iAccountId),status,from.getDate(),to.getDate());
	} catch (RemoteException e) {
		e.printStackTrace();
	} catch (FinderException e) {
		e.printStackTrace();
	}
	return null;
  }



  public  Collection getAccountKeys(){
		   try {
			return getFinanceService().getAccountKeyHome().findAll();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
  }

  public  Collection getTariffKeys(){
	    try {
			return getFinanceService().getTariffKeyHome().findAll();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
  }

  public  Map getAccountKeyMap(){
  	 Collection L = getAccountKeys();
     if(L != null){
       int len = L.size();
       Hashtable H = new Hashtable(len);
       for (Iterator iter = L.iterator(); iter.hasNext();) {
       	AccountKey AK = (AccountKey)  iter.next();
         H.put((Integer)AK.getPrimaryKey(),AK);
       }
       return H;
     }
     else
       return null;
  }

  public Map getTariffKeyMap(){
  	    Collection L = getTariffKeys();
  	    if(L != null){
  	      int len = L.size();
  	      Hashtable H = new Hashtable(len);
  	      for (Iterator iter = L.iterator(); iter.hasNext();) {
  	      	TariffKey AK = (TariffKey) iter.next();
  	        H.put((Integer)(AK.getPrimaryKey()),AK);
  	      }
  	      return H;
  	    }
  	    else
  	      return null;
  }


  public Collection getKeySortedAccountEntries(int iAccountId,IWTimestamp from,IWTimestamp to,String roundStatus){
    Map acckeys = getAccountKeyMap();
    Map takeys = getTariffKeyMap();
    if(acckeys != null && takeys != null){
      Collection entries = getAccountEntries(iAccountId,from,to,null,roundStatus);
      if(entries != null){
        int len = entries.size();
        Hashtable hash = new Hashtable(len);
        AccountEntry AE;
        for (Iterator iter = entries.iterator(); iter.hasNext();) {
        	AE = (AccountEntry) iter.next();
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

  public  Account createNewAccount(int iUserId, String sName,String sExtra, int iCashierId,String type,int iCategoryId)throws java.rmi.RemoteException,javax.ejb.CreateException{
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
    A.setCategoryId(iCategoryId);

    A.store();
    //System.err.println("account id "+A.getID());

    return A;
  }

  public Account createNewFinanceAccount(int iUserId, String sName,String sExtra, int iCashierId,int iCategoryId)throws java.rmi.RemoteException,javax.ejb.CreateException{
    return createNewAccount(iUserId,sName,sExtra,iCashierId,com.idega.block.finance.data.AccountBMPBean.typeFinancial,iCategoryId);
  }

  public Account createNewPhoneAccount(int iUserId, String sName,String sExtra, int iCashierId,int iCategoryId)throws  java.rmi.RemoteException,javax.ejb.CreateException{
    return createNewAccount(iUserId,sName,sExtra,iCashierId,com.idega.block.finance.data.AccountBMPBean.typePhone,iCategoryId);
  }

  public Account createNewAccount(int iUserId, String sName,String sExtra, int iCashierId,int iCategoryId)throws  java.rmi.RemoteException,javax.ejb.CreateException{
   return createNewAccount(iUserId,sName,sExtra,iCashierId,"",iCategoryId);
  }
  
  public FinanceService getFinanceService()throws RemoteException{
  		return (FinanceService)getServiceInstance(FinanceService.class);
  }
}