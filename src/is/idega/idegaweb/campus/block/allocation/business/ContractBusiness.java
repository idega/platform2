/*
 * $Id: ContractBusiness.java,v 1.29 2004/06/04 17:36:06 aron Exp $
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

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.block.application.data.ApplicationBMPBean;
import com.idega.block.building.data.Apartment;
import com.idega.block.finance.business.AccountManager;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.core.accesscontrol.business.LoginCreator;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.PermissionGroup;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.data.User;
import com.idega.core.user.data.UserHome;
import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.IWTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
 *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */
public  class ContractBusiness {

  public static String signCampusContract(IWApplicationContext iwac,int iContractId,int iGroupId,int iCashierId,String sEmail,boolean sendMail,
                boolean newAccount,boolean newPhoneAccount,boolean newLogin ,boolean generatePasswd,IWResourceBundle iwrb,String login,String passwd){
    Contract eContract = null;
    String pass = null;
    javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();

    try{
     t.begin();

      eContract = ((is.idega.idegaweb.campus.block.allocation.data.ContractHome)com.idega.data.IDOLookup.getHome(Contract.class)).findByPrimaryKey(new Integer(iContractId) );
      if(eContract != null ){
        int iUserId = eContract.getUserId().intValue();
				System.err.println("Signing user "+iUserId +" contract id : "+iContractId);
        if(sEmail !=null && sEmail.trim().length() >0){
					//System.err.println("adding email "+sEmail);
          UserBusiness.addNewUserEmail(iUserId,sEmail);
        }
        if(newAccount){
          String prefix = iwrb.getLocalizedString("finance","Finance");
          //System.err.println("adding finance account ");
          AccountManager.makeNewFinanceAccount(iUserId,prefix+" - "+String.valueOf(iUserId),"",iCashierId,1);
        }
        if(newPhoneAccount){
          //System.err.println("adding phone account ");
          String prefix = iwrb.getLocalizedString("phone","Phone");
          AccountManager.makeNewPhoneAccount(iUserId,prefix+" - "+String.valueOf(iUserId),"",iCashierId,1);
        }
        if(newLogin  && iGroupId > 0){
          //System.err.println("creating login "+login);
          createLogin( iUserId,iGroupId,login,pass,generatePasswd );
        }

        //System.err.println("deleteing from waitinglist ");
        deleteFromWaitingList(eContract.getApplicantId());

        //System.err.println("changing application status ");
        changeApplicationStatus( eContract);


        /*
        if(sendMail){
          sendMail(iUserId,login,pass,iwrb);
        }
        */
        //System.err.println("changing contract status ");
        eContract.setStatusSigned();
        //eContract.setIsRented(true);
        //System.err.println("updateing contract ");
        eContract.store();
        //System.err.println("lets try to commit");
        MailingListBusiness.processMailEvent(iwac,iContractId,LetterParser.SIGNATURE);
      }


     t.commit();
     //System.err.println("done committing ");

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

  public static void createLogin(int iUserId,int iGroupId,String login,String pass,boolean generatePasswd) throws Exception{

    User eUser = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(iUserId);
    //GenericGroup gg = ((com.idega.core.data.GenericGroupHome)com.idega.data.IDOLookup.getHomeLegacy(GenericGroup.class)).findByPrimaryKeyLegacy(iGroupId);
    PermissionGroup pg = ((com.idega.core.accesscontrol.data.PermissionGroupHome)com.idega.data.IDOLookup.getHomeLegacy(PermissionGroup.class)).findByPrimaryKeyLegacy(iGroupId);
    AccessControl.addUserToPermissionGroup(pg,eUser.getID());
    
    //gg.addTo(eUser);
    login = LoginCreator.createLogin(eUser.getName());
    //passwd = LoginCreator.createPasswd(8);
    if( generatePasswd )
      pass = LoginCreator.createPasswd(8);
    else
      pass = login;

    //System.err.println(login+" "+pass);

    //IWTimestamp today = IWTimestamp.RightNow();
    //int validDays = today.getDaysBetween(today,new IWTimestamp(eContract.getValidTo()));
    LoginDBHandler.createLogin(iUserId,login,pass);
    //LoginDBHandler.createLogin(iUserId,login,passwd,new Boolean(true),today,validDays,new Boolean(false),new Boolean(true),new Boolean(false),"");
  }

  public static void changeApplicationStatus(Contract eContract)throws Exception{
    String status = com.idega.block.application.data.ApplicationBMPBean.STATUS_SIGNED;
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
  

  public static void deleteFromWaitingList(Integer applicantID){
    List L = WaitingListFinder.listOfWaitingList(WaitingListFinder.APPLICANT,applicantID.intValue(),0,0);
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
/*
  public static void sendMail(int iUserId,String login,String pass,IWResourceBundle iwrb){
  	
    List lEmails = UserBusiness.listOfUserEmails(iUserId);
    if(lEmails != null){
      String address = ((Email)lEmails.get(0)).getEmailAddress();
      try {
        String body = iwrb.getLocalizedString("signed_contract_body","You have a signed contract to a apartment");
        StringBuffer sbody = new StringBuffer(body);
        sbody.append("\n");
        sbody.append(" Login  :");
        sbody.append(login );
        sbody.append("\n");
        sbody.append(" Passwd :");
        sbody.append(pass );
        //System.err.println("passwd "+pass);
        sbody.append("\n");
        String header = iwrb.getLocalizedString("signed_contract","Signed Contract");
        String from = sp!=null?sp.getAdminEmail():"admin@campus.is";
        if(from==null || "".equals(from))
          from = "admin@campus.is";
        String host = sp != null?sp.getEmailHost():"mail.idega.is";
        if(host ==null || "".equals(host))
          host = "mail.idega.is";
        if(address == null || "".equals(address))
          address = "aron@idega.is";
        SendMail.send(from,address,"","aron@idega.is",host,header,sbody.toString());

        //SendMail.send("admin@campus.is","aron@idega.is","","","mail.idega.is",header,sbody.toString());
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
*/
  public static Contract endContract(int iContractId,IWTimestamp movingDate,String info,boolean datesync){
    try {
      Contract C = ((is.idega.idegaweb.campus.block.allocation.data.ContractHome)com.idega.data.IDOLookup.getHomeLegacy(Contract.class)).findByPrimaryKey(new Integer(iContractId) );
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
    }return null;
  }

  public static void returnKey(IWApplicationContext iwac,int iContractId){
    try {
      Contract C = ((is.idega.idegaweb.campus.block.allocation.data.ContractHome)com.idega.data.IDOLookup.getHome(Contract.class)).findByPrimaryKey(new Integer(iContractId ));
      C.setEnded();
      C.store();
      MailingListBusiness.processMailEvent(iwac,iContractId,LetterParser.RETURN);
    }
    catch (Exception ex) {
      ex.printStackTrace( );
    }
  }

  public static void deliverKey(IWApplicationContext iwac,int iContractId, Timestamp when) {
     try {
      Contract C = ((is.idega.idegaweb.campus.block.allocation.data.ContractHome)com.idega.data.IDOLookup.getHomeLegacy(Contract.class)).findByPrimaryKey(new Integer(iContractId) );
      if (when == null)
	      C.setStarted();
	    else
	    	C.setStarted(when);
      C.store();
       MailingListBusiness.processMailEvent(iwac,iContractId,LetterParser.DELIVER);
    }
    catch (Exception ex) {
      ex.printStackTrace( );
    }
  }

  public static void deliverKey(IWApplicationContext iwac,int iContractId){
  	deliverKey(iwac,iContractId,null);
  }

  public static void resignContract(IWApplicationContext iwac,int iContractId,IWTimestamp movingDate,String info,boolean datesync){
    try {
      Contract C = ((is.idega.idegaweb.campus.block.allocation.data.ContractHome)com.idega.data.IDOLookup.getHome(Contract.class)).findByPrimaryKey(new Integer(iContractId ));
      C.setMovingDate(movingDate.getSQLDate());
      if(datesync)
        C.setValidTo(movingDate.getSQLDate());
      C.setResignInfo(info);
      C.setStatusResigned();
      C.store();
      MailingListBusiness.processMailEvent(iwac,iContractId,LetterParser.RESIGN);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static boolean makeNewContract(IWApplicationContext iwc,User eUser,Applicant eApplicant,int iApartmentId,IWTimestamp from,IWTimestamp to){

  	try{  
  	Contract eContract = ((ContractHome)IDOLookup.getHome(Contract.class)).create();
      eContract.setApartmentId(iApartmentId);
      eContract.setApplicantId(eApplicant.getID());
      eContract.setUserId(eUser.getID());
      eContract.setStatusCreated();
      eContract.setValidFrom(from.getSQLDate());
      eContract.setValidTo(to.getSQLDate());
      
        eContract.store();
        MailingListBusiness.processMailEvent(iwc, ((Integer)eContract.getPrimaryKey()).intValue(),LetterParser.ALLOCATION);
        return true;
      }
      catch(Exception ex){
        return false;
      }
  }

  public static User makeNewUser(Applicant A,String[] emails){
    UserBusiness ub = new UserBusiness();
    try{
    	User user = null;
    
    	String ssn = A.getSSN();
    	if(ssn!=null){
    		try {
				user = ((UserHome) IDOLookup.getHome(User.class)).findByPersonalID(ssn);
			}
			catch (IDOLookupException e) {
				e.printStackTrace();
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
    	}
    	else{
    		user = ub.insertUser(A.getFirstName(),A.getMiddleName(),A.getLastName(),A.getFirstName(),"",null,null,null,A.getSSN());
    	}
    	if(user!=null && emails !=null && emails.length >0)
    		ub.addNewUserEmail(user.getID(),emails[0]);

    	return user;
    }
    catch(SQLException ex){
      ex.printStackTrace();
    }
    return null;
  }

  public static boolean deleteAllocation(int iContractId){
    return doGarbageContract(iContractId);
    /*
    try {
      Contract eContract = ((is.idega.idegaweb.campus.block.allocation.data.ContractHome)com.idega.data.IDOLookup.getHomeLegacy(Contract.class)).findByPrimaryKeyLegacy(iContractId);
      User eUser = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(eContract.getUserId().intValue());
      eContract.delete();
      eUser.delete();

      return true;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return false;
    }
    */
  }

  public static IWTimestamp[] getContractStampsForApartment(int apartmentId){
    Apartment ap = com.idega.block.building.business.BuildingCacher.getApartment(apartmentId);
    return getContractStampsForApartment(ap);
  }

  public static IWTimestamp[] getContractStampsForApartment(Apartment apartment){
    ApartmentTypePeriods ATP = ContractFinder.getPeriod(apartment.getApartmentTypeId());
    return getContractStampsFromPeriod(ATP,1);

  }

   public static IWTimestamp[] getContractStampsFromPeriod(ApartmentTypePeriods ATP,int monthOverlap){
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

  public static String getLocalizedStatus(com.idega.idegaweb.IWResourceBundle iwrb,String status){
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

  public static  boolean doGarbageContract(int iContract){
    int id = iContract;
    try {
      Contract eContract = ((is.idega.idegaweb.campus.block.allocation.data.ContractHome)com.idega.data.IDOLookup.getHome(Contract.class)).findByPrimaryKey(new Integer(id));
      eContract.setStatusGarbage();
      eContract.store();
    }
    catch (Exception ex) {
		return false;
    }
    return true;
  }
}
