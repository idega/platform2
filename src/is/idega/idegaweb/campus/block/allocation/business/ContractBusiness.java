/*
 * $Id: ContractBusiness.java,v 1.8 2002/02/20 00:06:00 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.allocation.business;


import is.idega.idegaweb.campus.presentation.SysPropsSetter;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.application.data.WaitingList;
import is.idega.idegaweb.campus.data.SystemProperties;
import is.idega.idegaweb.campus.block.mailinglist.business.MailingListBusiness;
import is.idega.idegaweb.campus.block.mailinglist.business.LetterParser;

import java.sql.SQLException;
import java.util.List;
import com.idega.data.EntityFinder;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Map;
import java.util.Iterator;
import com.idega.core.user.data.User;
import com.idega.block.application.data.Applicant;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.core.data.Email;
import com.idega.core.user.business.UserBusiness;
import com.idega.block.finance.business.AccountManager;
import com.idega.block.finance.data.Account;
import com.idega.block.login.business.LoginCreator;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.user.data.User;
import com.idega.core.data.GenericGroup;
import com.idega.core.accesscontrol.data.PermissionGroup;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.accesscontrol.business.AccessControl;
import java.util.List;
import java.util.Iterator;
import com.idega.util.idegaTimestamp;
import com.idega.util.SendMail;
import com.idega.block.application.data.Application;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
 *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */
public  class ContractBusiness {

  public static String signCampusContract(int iContractId,int iGroupId,int iCashierId,String sEmail,boolean sendMail,
                boolean newAccount,boolean newPhoneAccount,boolean newLogin ,boolean generatePasswd,IWResourceBundle iwrb,String login,String passwd){
    Contract eContract = null;
    String pass = null;
    javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();

    try{
     t.begin();

      eContract = new Contract(iContractId );
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
        deleteFromWaitingList(eContract);

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
        eContract.update();
        //System.err.println("lets try to commit");
        MailingListBusiness.processMailEvent(iContractId,LetterParser.SIGNATURE);
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

    User eUser = new User(iUserId);
    //GenericGroup gg = new GenericGroup(iGroupId);
    PermissionGroup pg = new PermissionGroup(iGroupId);
    AccessControl.addUserToPermissionGroup(pg,eUser.getID());
    //gg.addTo(eUser);
    login = LoginCreator.createLogin(eUser.getName());
    //passwd = LoginCreator.createPasswd(8);
    if( generatePasswd )
      pass = LoginCreator.createPasswd(8);
    else
      pass = login;

    //System.err.println(login+" "+pass);

    //idegaTimestamp today = idegaTimestamp.RightNow();
    //int validDays = today.getDaysBetween(today,new idegaTimestamp(eContract.getValidTo()));
    LoginDBHandler.createLogin(iUserId,login,pass);
    //LoginDBHandler.createLogin(iUserId,login,passwd,new Boolean(true),today,validDays,new Boolean(false),new Boolean(true),new Boolean(false),"");
  }

  public static void changeApplicationStatus(Contract eContract)throws SQLException{
    String status = Application.statusSigned;
    List L = null;
    L = EntityFinder.findAllByColumn(new Application(),Application.getColumnNameApplicantId(),eContract.getApplicantId().intValue());
    if(L!=null){
			Iterator I = L.iterator();
      while(I.hasNext()){
        Application A = (Application) I.next();
        A.setStatusSigned();
        A.update();
      }

    }
  }

  public static void deleteFromWaitingList(Contract eContract){
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

  public static void sendMail(int iUserId,String login,String pass,IWResourceBundle iwrb){
    SystemProperties sp = SysPropsSetter.seekProperties();
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

  public static void endContract(int iContractId,idegaTimestamp movingDate,String info,boolean datesync){
    try {
      Contract C = new Contract(iContractId );
      C.setMovingDate(movingDate.getSQLDate());
      if(datesync)
        C.setValidTo(movingDate.getSQLDate());
      C.setResignInfo(info);
      C.setStatusEnded();
      C.update();
    }
    catch (SQLException ex) {
      ex.printStackTrace( );
    }
  }

  public static void returnKey(int iContractId){
    try {
      Contract C = new Contract(iContractId );
      C.setEnded();
      C.update();
      MailingListBusiness.processMailEvent(iContractId,LetterParser.RETURN);
    }
    catch (SQLException ex) {
      ex.printStackTrace( );
    }
  }

  public static void deliverKey(int iContractId){
     try {
      Contract C = new Contract(iContractId );
      C.setStarted();
      C.update();
       MailingListBusiness.processMailEvent(iContractId,LetterParser.DELIVER);
    }
    catch (SQLException ex) {
      ex.printStackTrace( );
    }
  }

  public static void resignContract(int iContractId,idegaTimestamp movingDate,String info,boolean datesync){
    try {
      Contract C = new Contract(iContractId );
      C.setMovingDate(movingDate.getSQLDate());
      if(datesync)
        C.setValidTo(movingDate.getSQLDate());
      C.setResignInfo(info);
      C.setStatusResigned();
      C.update();
      MailingListBusiness.processMailEvent(iContractId,LetterParser.RESIGN);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
}
