/*
 * $Id: ContractServiceBean.java,v 1.7 2004/06/04 17:36:06 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.allocation.business;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.block.application.data.WaitingList;
import is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriods;
import is.idega.idegaweb.campus.block.mailinglist.business.LetterParser;
import is.idega.idegaweb.campus.block.mailinglist.business.MailingListBusiness;
import is.idega.idegaweb.campus.business.CampusSettings;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.block.application.data.ApplicationBMPBean;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.data.Apartment;
import com.idega.block.finance.business.AccountBusiness;
import com.idega.business.IBOServiceBean;
import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.IWTimestamp;


/**
 * Title: Service Bean for the campus contract system
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 */

public class ContractServiceBean extends IBOServiceBean  implements ContractService{
	
	public IWResourceBundle getResourceBundle(IWUserContext context){
		return getIWMainApplication().getBundle(CampusSettings.IW_BUNDLE_IDENTIFIER).getResourceBundle(context.getCurrentLocale());
	}

  public  String signContract(IWUserContext context,Integer contractId,Integer groupId,Integer cashierId,String email,boolean sendMail,
                boolean newAccount,boolean newPhoneAccount,boolean newLogin ,boolean generatePasswd,String login,String passwd){
    Contract eContract = null;
    String pass = null;
    javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
    
    try{
     t.begin();
     IWResourceBundle iwrb = getResourceBundle(context);
      eContract =getContractHome().findByPrimaryKey(contractId );
      if(eContract != null ){
        Integer userId = eContract.getUserId();
				System.err.println("Signing user "+userId +" contract id : "+contractId);
        if(email !=null && email.trim().length() >0){
          getUserService().addNewUserEmail(userId.intValue(),email);
        }
        if(newAccount){
          String prefix = iwrb.getLocalizedString("finance","Finance");
          getAccountService().makeNewFinanceAccount(userId.intValue(),prefix+" - "+String.valueOf(userId),"",cashierId.intValue(),1);
        }
        if(newPhoneAccount){
          String prefix = iwrb.getLocalizedString("phone","Phone");
          getAccountService().makeNewPhoneAccount(userId.intValue(),prefix+" - "+String.valueOf(userId),"",cashierId.intValue(),1);
        }
        if(newLogin  && groupId.intValue() > 0){
          createUserLogin( userId.intValue(),groupId.intValue(),login,pass,generatePasswd );
        }
        deleteFromWaitingList(eContract);
        changeApplicationStatus( eContract);
        eContract.setStatusSigned();
        eContract.store();
        MailingListBusiness.processMailEvent(getIWApplicationContext(),contractId.intValue(),LetterParser.SIGNATURE);
      }
     t.commit();
    }
    catch(Exception e) {
      e.printStackTrace();
      try {
        t.rollback();
      }
      catch(javax.transaction.SystemException ex) {
        ex.printStackTrace();
      }
    }
    return pass;
  }

  public void createUserLogin(int iUserId,int iGroupId,String login,String pass,boolean generatePasswd) throws Exception{
    User user = getUserService().getUser(iUserId);
    getUserService().generateUserLogin(user);
    Group group = getUserService().getGroupHome().findByPrimaryKey(new Integer(iGroupId));
    group.addGroup(user);
  }

  public void changeApplicationStatus(Contract eContract)throws Exception{
    String status = ApplicationBMPBean.STATUS_SIGNED;
    List L = null;
    L = EntityFinder.getInstance().findAllByColumn(Application.class,ApplicationBMPBean.getApplicantIdColumnName(),eContract.getApplicantId().intValue());
    if(L!=null){
			Iterator I = L.iterator();
      while(I.hasNext()){
        Application A = (Application) I.next();
        A.setStatusSigned();
        A.update();
      }

    }
  }

  public  void deleteFromWaitingList(Contract eContract){
    List L = WaitingListFinder.listOfWaitingList(WaitingListFinder.APPLICANT,eContract.getApplicantId().intValue(),0,0);
      if(L!=null){
        Iterator I = L.iterator();
        while(I.hasNext()){
          try{
          ((WaitingList) I.next()).delete();
          }
          catch(SQLException ex){
           ex.printStackTrace();
          }
        }
      }
  }

  public Contract endContract(Integer contractId,IWTimestamp movingDate,String info,boolean datesync){
    try {
      Contract C = getContractHome().findByPrimaryKey(contractId );
      C.setMovingDate(movingDate.getSQLDate());
      if(datesync)
        C.setValidTo(movingDate.getSQLDate());
      C.setResignInfo(info);
      C.setStatusEnded();
      C.store();
      return C;
    }
    catch (Exception ex) {
      ex.printStackTrace( );
    }
    return null;
  }
  
  public Contract updateAllocation(Integer contractId,Integer apartmentId,Date from,Date to){
  	Contract contract = null;
    try {
      contract = getContractHome().findByPrimaryKey(contractId);
      contract.setValidFrom(from);
      contract.setValidTo(to);
      if (apartmentId != null) {
        contract.setApartmentId(apartmentId);
      }
      contract.store();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
   return contract;

  }
  

  public  void returnKey(IWApplicationContext iwac,Integer contractId){
    try {
      Contract C = getContractHome().findByPrimaryKey(contractId );
      C.setEnded();
      C.store();
      MailingListBusiness.processMailEvent(iwac,contractId.intValue(),LetterParser.RETURN);
    }
    catch (Exception ex) {
      ex.printStackTrace( );
    }
  }

  public  void deliverKey(IWApplicationContext iwac,Integer contractId, Timestamp when) {
     try {
      Contract C = getContractHome().findByPrimaryKey(contractId );
      if (when == null)
	      C.setStarted();
	    else
	    	C.setStarted(when);
      C.store();
       MailingListBusiness.processMailEvent(iwac,contractId.intValue(),LetterParser.DELIVER);
    }
    catch (Exception ex) {
      ex.printStackTrace( );
    }
  }

  public  void deliverKey(IWApplicationContext iwac,Integer contractId){
  		deliverKey(iwac,contractId,null);
  }

  public  void resignContract(IWApplicationContext iwac,Integer contractId,IWTimestamp movingDate,String info,boolean datesync){
    try {
      Contract C = getContractHome().findByPrimaryKey(contractId );
      C.setMovingDate(movingDate.getSQLDate());
      if(datesync)
        C.setValidTo(movingDate.getSQLDate());
      C.setResignInfo(info);
      C.setStatusResigned();
      C.store();
      MailingListBusiness.processMailEvent(iwac,contractId.intValue(),LetterParser.RESIGN);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public  boolean makeNewContract(IWApplicationContext iwc,User user,Applicant applicant,Integer apartmentId,IWTimestamp from,IWTimestamp to)throws java.rmi.RemoteException{
    try{
      Contract contract = getContractHome().create();
      contract.setApartmentId(apartmentId);
      contract.setApplicantId(((Integer)applicant.getPrimaryKey()).intValue());
      contract.setUserId(((Integer)user.getPrimaryKey()).intValue());
      contract.setStatusCreated();
      contract.setValidFrom(from.getSQLDate());
      contract.setValidTo(to.getSQLDate());

        contract.store();
        MailingListBusiness.processMailEvent(iwc,((Integer) contract.getPrimaryKey()).intValue(),LetterParser.ALLOCATION);
        return true;
      }
      catch(Exception ex){
        return false;
      }
  }

  public User makeNewUser(Applicant A,String[] emails){

    try{
    	User u = null;
    	
    	String ssn = A.getSSN();
    	if(ssn!=null){
    		
    			try {
					u = ((UserHome) IDOLookup.getHome(User.class)).findByPersonalID(ssn);
				}
				catch (RuntimeException e) {
					e.printStackTrace();
				}
    		
    		
    	}
    	else{
    		u = getUserService().insertUser(A.getFirstName(),A.getMiddleName(),A.getLastName(),A.getFirstName(),"",null,null,null);
    	}
    if(emails !=null && emails.length >0)
      getUserService().addNewUserEmail( ((Integer) u.getPrimaryKey()).intValue(),emails[0]);

    return u;
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    return null;
  }

  public  boolean deleteAllocation(Integer contractId, User currentUser){
    try {
      Contract eContract =  getContractHome().findByPrimaryKey(contractId);
      getUserService().deleteUser(eContract.getUserId().intValue(), currentUser);
      eContract.remove();

      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  public  IWTimestamp[] getContractStampsForApartment(int apartmentId){
    Apartment ap = BuildingCacher.getApartment(apartmentId);
    return getContractStampsForApartment(ap);
  }

  public  IWTimestamp[] getContractStampsForApartment(Apartment apartment){
    ApartmentTypePeriods ATP = ContractFinder.getPeriod(apartment.getApartmentTypeId());
    return getContractStampsFromPeriod(ATP,1);

  }

   public  IWTimestamp[] getContractStampsFromPeriod(ApartmentTypePeriods ATP,int monthOverlap){
     IWTimestamp contractDateFrom = IWTimestamp.RightNow();
     IWTimestamp contractDateTo = IWTimestamp.RightNow();
     if(ATP!=null){
        // Period checking
        //System.err.println("ATP exists");
        boolean first = ATP.hasFirstPeriod();
        boolean second = ATP.hasSecondPeriod();
         IWTimestamp today = new IWTimestamp();

        // Two Periods
        if(first && second){

          if(today.getMonth() > ATP.getFirstDateMonth()+monthOverlap && today.getMonth() <= ATP.getSecondDateMonth()+monthOverlap ){
            contractDateFrom = new IWTimestamp(ATP.getSecondDateDay(),ATP.getSecondDateMonth(),today.getYear());
            contractDateTo = new IWTimestamp(ATP.getFirstDateDay(),ATP.getFirstDateMonth(),today.getYear()+1);
          }
          else if(today.getMonth() <= 12){
            contractDateFrom = new IWTimestamp(ATP.getFirstDateDay(),ATP.getFirstDateMonth(),today.getYear()+1);
            contractDateTo = new IWTimestamp(ATP.getSecondDateDay(),ATP.getSecondDateMonth(),today.getYear()+1);
          }
          else{
            contractDateFrom = new IWTimestamp(ATP.getFirstDateDay(),ATP.getFirstDateMonth(),today.getYear());
            contractDateTo = new IWTimestamp(ATP.getSecondDateDay(),ATP.getSecondDateMonth(),today.getYear());
          }

        }
        // One Periods
        else if(first && !second){
          //System.err.println("two sectors");
          contractDateFrom = new IWTimestamp(ATP.getFirstDateDay(),ATP.getFirstDateMonth(),today.getYear());
          contractDateTo = new IWTimestamp(ATP.getFirstDateDay(),ATP.getFirstDateMonth(),today.getYear()+1);
        }
        else if(!first && second){
          //System.err.println("two sectors");
          contractDateFrom = new IWTimestamp(ATP.getSecondDateDay(),ATP.getSecondDateMonth(),today.getYear());
          contractDateTo = new IWTimestamp(ATP.getSecondDateDay(),ATP.getSecondDateMonth(),today.getYear()+1);
        }
     }

      IWTimestamp[] stamps = {contractDateFrom,contractDateTo};
      return stamps;
  }

  public String getLocalizedStatus(com.idega.idegaweb.IWResourceBundle iwrb,String status){
    String r = "";
    char c = status.charAt(0);
    switch (c) {
      case 'C': r = iwrb.getLocalizedString("created","Created"); break;
      case 'P': r = iwrb.getLocalizedString("printed","Printed"); break;
      case 'S': r = iwrb.getLocalizedString("signed","Signed");   break;
      case 'R': r = iwrb.getLocalizedString("rejected","Rejected");  break;
      case 'T': r = iwrb.getLocalizedString("terminated","Terminated");   break;
      case 'E': r = iwrb.getLocalizedString("ended","Ended");  break;
      case 'G': r = iwrb.getLocalizedString("garbage","Garbage");  break;
    }
    return r;
  }

  public   boolean  doGarbageContract(Integer contract){
   
    try {
      Contract eContract = getContractHome().findByPrimaryKey(contract);
      eContract.setStatusGarbage();
      eContract.store();
	  return true;
    }
    catch (Exception ex) {
		return false;
    }
    
  }

  public UserBusiness getUserService() throws java.rmi.RemoteException{
    return (UserBusiness) getServiceInstance(UserBusiness.class);
  }

  public ContractHome getContractHome()throws java.rmi.RemoteException{
    return (ContractHome)IDOLookup.getHome(Contract.class);
  }
  
  public AccountBusiness getAccountService() throws RemoteException{
  	return (AccountBusiness) getServiceInstance(AccountBusiness.class);
  }
}
