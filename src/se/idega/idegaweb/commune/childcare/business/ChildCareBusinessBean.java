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
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.data.IDOLookup;
import com.idega.transaction.IdegaTransactionManager;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

import se.idega.idegaweb.commune.childcare.check.business.CheckBusiness;
import se.idega.idegaweb.commune.childcare.check.data.Check;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplicationHome;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;
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
			
			CheckBusiness checkBiz = (CheckBusiness)getServiceInstance(CheckBusiness.class);
			Check check = checkBiz.getCheck(checkId);
//			check.setStatus(this.getcases)
//			check.store();

			SchoolBusiness schoolBiz = (SchoolBusiness)getServiceInstance(SchoolBusiness.class);
			School prov = schoolBiz.getSchool(new Integer(provider[0]));
			UserBusiness userBiz = (UserBusiness)getServiceInstance(UserBusiness.class);
			Collection users = userBiz.getUsersInGroup(prov.getHeadmasterGroupId());
	
			if (users != null) {
				MessageBusiness messageBiz = (MessageBusiness)getServiceInstance(MessageBusiness.class);
				Iterator it = users.iterator();
				while (it.hasNext()) {
					User providerUser = (User)it.next();
					messageBiz.createUserMessage(providerUser,"New application","You have new mail");
				}				
			}
			else 
				System.out.println("Got no users for group " + prov.getHeadmasterGroupId());
			
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

		return true;
	}

	public Collection getUnhandledApplicationsByProvider(int providerId) {
		try {			
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			
			return home.findAllCasesByProviderAndStatus(providerId,getCaseStatusOpen());
		}
		catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
		catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Collection getUnhandledApplicationsByProvider(School provider) {
		try {
			return getUnhandledApplicationsByProvider(((Integer)provider.getPrimaryKey()).intValue());
		}
		catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Collection getUnhandledApplicationsByProvider(User provider) {
		try {
			SchoolChoiceBusiness schoolBiz = (SchoolChoiceBusiness)getServiceInstance(SchoolChoiceBusiness.class);
			School school;
			school = schoolBiz.getFirstProviderForUser(provider);

			return getUnhandledApplicationsByProvider(((Integer)school.getPrimaryKey()).intValue());
		}
		catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
		catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Collection getUnsignedApplicationsByProvider(int providerId) {
		try {
			
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			
			return home.findAllCasesByProviderAndStatus(providerId,this.getCaseStatusPreliminary());
		}
		catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
		catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Collection getUnsignedApplicationsByProvider(School provider) {
		try {
			return getUnsignedApplicationsByProvider(((Integer)provider.getPrimaryKey()).intValue());
		}
		catch (RemoteException e) {
			return null;
		}
	}

	public Collection getUnsignedApplicationsByProvider(User provider) {
		try {
			SchoolChoiceBusiness schoolBiz = (SchoolChoiceBusiness)getServiceInstance(SchoolChoiceBusiness.class);
			School school;
			school = schoolBiz.getFirstManagingSchoolForUser(provider);

			return getUnsignedApplicationsByProvider(((Integer)school.getPrimaryKey()).intValue());
		}
		catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
		catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean rejectApplication(ChildCareApplication application) {
		return false;	
	}
	
	public boolean rejectApplication(int applicationId) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			ChildCareApplication appl = (ChildCareApplication)home.findByPrimaryKey(new Integer(applicationId));
			return rejectApplication(appl);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean acceptApplication(ChildCareApplication application) {
		return false;	
	}
	
	public boolean acceptApplication(int applicationId) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			ChildCareApplication appl = (ChildCareApplication)home.findByPrimaryKey(new Integer(applicationId));

			return acceptApplication(appl);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public boolean signApplication(ChildCareApplication application) {
		return false;	
	}
	
	public boolean signApplication(int applicationId) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			ChildCareApplication appl = (ChildCareApplication)home.findByPrimaryKey(new Integer(applicationId));

			return signApplication(appl);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		
		return false;
	}	
}