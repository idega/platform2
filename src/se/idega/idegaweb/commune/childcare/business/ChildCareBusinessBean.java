/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.childcare.business;

import com.idega.block.process.business.CaseBusinessBean;
import com.idega.data.IDOLookup;
import com.idega.transaction.IdegaTransactionManager;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplicationHome;

import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

/**
 * This class does something very clever.....
 * 
 * @author palli
 * @version 1.0
 */
public class ChildCareBusinessBean extends CaseBusinessBean implements ChildCareBusiness {
	public boolean insertApplications(User user, int type, int provider[], String date[], int checkId, int childId, boolean agree) {
//		System.out.println("user = " + user);
//		System.out.println("type = " + type);
//		System.out.println("provider = " + provider);
//		System.out.println("date = " + date);
//		System.out.println("checkId = " + checkId);
//		System.out.println("childId = " + childId);
//		System.out.println("agree = " + agree);

		TransactionManager t = IdegaTransactionManager.getInstance();

		try {
			t.begin();
			ChildCareApplication appl = null;
			ChildCareApplication parent = null;
			
			IWTimestamp now = new IWTimestamp();
			for (int i = 0; i < 5; i++) {
				appl = ((ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class)).create();
				if (user != null)
					appl.setOwner(user);
				appl.setChildrenCareTypeId(type);
				appl.setProviderId(provider[i]);
				IWTimestamp fromDate = new IWTimestamp(date[i]);
				appl.setFromDate(fromDate.getDate());
				appl.setChildId(childId);
				appl.setParentsAgree(agree);
				appl.setQueueDate(now.getDate());
				appl.setMethod(1);
				appl.setChoiceNumber(i+1);
				appl.setCheckId(checkId);
				if (i == 0) {
					appl.setCaseStatus(getCaseStatusOpen());
				}
				else {
					appl.setCaseStatus(getCaseStatusInactive());
					appl.setParentCase(parent);					
				}
				appl.store();
				
				parent = appl;				
			}
			
			/**
			 * @todo Bæta við að breyta stöðu á tékkanum sem var notaður
			 */
			t.commit();
		}
		catch(Exception e) {
			e.printStackTrace();
			try {
				t.rollback();
			}
			catch (SystemException ex) {
				ex.printStackTrace();
			}
			
			return false;	
		}
//		try {
//			CitizenAccount application = ((CitizenAccountHome) IDOLookup.getHome(CitizenAccount.class)).create();
//			application.setPID(pid);
//			if (user != null)
//				application.setOwner(user);
//			application.setPhoneHome(phoneHome);
//			if (email != null)
//				application.setEmail(email);
//			if (phoneWork != null)
//				application.setPhoneWork(phoneWork);
//			application.setCaseStatus(getCaseStatusOpen());
//
//			application.store();
//
//			int applicationID = ((Integer) application.getPrimaryKey()).intValue();
//			if (acceptApplicationOnCreation) {
//				acceptApplication(applicationID, user);
//			}
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//
//			return false;
//		}

		return true;
	}

}
