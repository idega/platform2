/*
 * $Id: ContractBusiness.java,v 1.3 2001/10/01 13:07:20 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.allocation.business;

import is.idegaweb.campus.allocation.SysPropsSetter;
import is.idegaweb.campus.entity.*;
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
import com.idega.util.SendMail;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public  class ContractBusiness {

   public static String signContract(int iContractId,int iGroupId,int iCashierId,String sEmail,boolean sendMail,
                boolean newAccount,boolean newPhoneAccount,boolean newLogin,IWResourceBundle iwrb,String login,String passwd){
    Contract eContract = null;
    String pass = null;
    try {
      eContract = new Contract(iContractId );
    }
    catch (SQLException ex) {
      eContract = null;
    }
    if(eContract != null ){
      int iUserId = eContract.getUserId().intValue();
      if(sEmail !=null && sEmail.trim().length() >0){
        UserBusiness.addNewUserEmail(iUserId,sEmail);
      }
      if(newAccount){
        String prefix = iwrb.getLocalizedString("finance","Finance");
        AccountManager.makeNewFinanceAccount(iUserId,prefix+" - "+String.valueOf(iUserId),"",iCashierId);
      }
      if(newPhoneAccount){
        String prefix = iwrb.getLocalizedString("phone","Phone");
        AccountManager.makeNewPhoneAccount(iUserId,prefix+" - "+String.valueOf(iUserId),"",iCashierId);
      }
      if(newLogin  && iGroupId > 0){
        try {
          User eUser = new User(iUserId);
          //GenericGroup gg = new GenericGroup(iGroupId);
          PermissionGroup pg = new PermissionGroup(iGroupId);
          AccessControl.addUserToPermissionGroup(pg,eUser.getID());
          //gg.addTo(eUser);
          login = LoginCreator.createLogin(eUser.getName());
          //passwd = LoginCreator.createPasswd(8);
          pass = LoginCreator.createPasswd(8);

          //idegaTimestamp today = idegaTimestamp.RightNow();
          //int validDays = today.getDaysBetween(today,new idegaTimestamp(eContract.getValidTo()));
          try{
            LoginDBHandler.createLogin(iUserId,login,pass);
            //LoginDBHandler.createLogin(iUserId,login,passwd,new Boolean(true),today,validDays,new Boolean(false),new Boolean(true),new Boolean(false),"");
          }
          catch(Exception ex){
            ex.printStackTrace();
            login = null;
            passwd = null;
          }
          eContract.setStatusSigned();
          eContract.update();
        }
        catch (SQLException ex) {
          ex.printStackTrace();
        }

        // Delete from waitinglist
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
      if(sendMail){
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
    }
    return pass;
  }
}