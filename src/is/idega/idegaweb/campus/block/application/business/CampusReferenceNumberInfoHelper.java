/*
 * $Id: CampusReferenceNumberInfoHelper.java,v 1.21 2004/07/05 14:50:41 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.application.business;

import java.rmi.RemoteException;

import com.idega.block.contract.business.ContractService;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;


/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CampusReferenceNumberInfoHelper {
	/*
  public static void updatePhoneAndEmail(CampusApplicationHolder holder, String phone, String email) {
    Applicant applicant = (Applicant)holder.getApplicant();
    applicant.setResidencePhone(phone);
    try {
      applicant.store();
    }
    catch(Exception e) {
      e.printStackTrace();
    }

    CampusApplication application = holder.getCampusApplication();
    application.setEmail(email);
    try {
      application.store();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  */
/*
  public static boolean stayOnWaitingList(int waitingListId, boolean stayOnList) {
    try {
      WaitingList li = ((WaitingListHome)IDOLookup.getHome(WaitingList.class)).findByPrimaryKey(new Integer(waitingListId));
      if (stayOnList) {
        li.setRemovedFromList(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.NO);
        li.setLastConfirmationDate(IWTimestamp.getTimestampRightNow());
      }
      else
        li.setRemovedFromList(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.YES);
      li.store();
      return(true);
    }
    catch(Exception e) {
      e.printStackTrace();
      return(false);
    }
  }
  */

  /*
  public static List getUserLogin(String ssn) {
    String ref = ssn;
    Vector l = new Vector();


    try {
      ApplicantHome aHome = (ApplicantHome) IDOLookup.getHome(Applicant.class);
      Applicant applicant ;
      Collection li = aHome.findBySSN(ref);
      if (li != null && !li.isEmpty()) {
        Iterator it = li.iterator();
        Collection contracts = null;
        Contract contract = null;
        ContractHome cHome = (ContractHome) IDOLookup.getHome(Contract.class);
        while (it.hasNext()) {
          applicant = (Applicant)it.next();
          
          contracts = cHome.findByApplicant((Integer)applicant.getPrimaryKey());
          if (contracts != null && !contracts.isEmpty()) {
          	Iterator it2 = contracts.iterator();
          	while (it2.hasNext()) {
          		contract = (Contract)it2.next();

		          //System.out.println("Contract id = " + contract.getPrimaryKey());

		          
  	  	      if (contract != null) {
    		        if (contract.getIsRented())
      		        break;
        		    else
          		    contract = null;
	          	}
          	}
          }
          
          if (contract != null)
          	break;
        }

        if (contract == null)
          return(null);


        User user = contract.getUser();
        Integer userID = contract.getUserId();
        l.add((Integer)user.getPrimaryKey());
        LoginTable login = LoginDBHandler.getUserLogin(userID.intValue());

        java.sql.Timestamp t = login.getLastChanged();
        if (t == null){
          String passwd = LoginCreator.createPasswd(8);
          LoginDBHandler.updateLogin(userID.intValue(),login.getUserLogin(),passwd);

          l.add(login.getUserLogin());
          l.add(passwd);
        }
      }
    }
    catch(SQLException e) {

    }
    catch(Exception ex) {
    		ex.printStackTrace();
    }

    return(l);
  }
  */
	public ContractService getContractService(IWContext iwc) throws RemoteException{
		return (ContractService) IBOLookup.getServiceInstance(iwc.getApplicationContext(),ContractService.class);
	}
	  
	public ApplicationService getApplicationService(IWContext iwc) throws RemoteException{
		return (ApplicationService) IBOLookup.getServiceInstance(iwc.getApplicationContext(),ApplicationService.class);
	}
}
