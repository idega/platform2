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

import com.idega.block.contract.business.ContractBusiness;
import com.idega.block.contract.business.ContractWriter;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.process.data.Case;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.data.IDOLookup;
import com.idega.transaction.IdegaTransactionManager;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.lowagie.text.Font;

import se.idega.idegaweb.commune.childcare.check.business.CheckBusiness;
import se.idega.idegaweb.commune.childcare.check.data.Check;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplicationHome;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.data.SystemArchivationMessage;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

/**
 * This class does something very clever.....
 * 
 * @author palli
 * @version 1.0
 */
public class ChildCareBusinessBean extends CaseBusinessBean implements ChildCareBusiness {
	public boolean insertApplications(User user, int provider[], String date[], int checkId, int childId, String subject, String message) {
		UserTransaction t = getSessionContext().getUserTransaction();

		try {
			t.begin();
			ChildCareApplication appl = null;
			ChildCareApplication parent = null;
			
			IWTimestamp now = new IWTimestamp();
			for (int i = 0; i < 5; i++) {
				appl = ((ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class)).create();
				if (user != null)
					appl.setOwner(user);
//				appl.setChildrenCareTypeId(type);
				appl.setProviderId(provider[i]);
				IWTimestamp fromDate = new IWTimestamp(date[i]);
				appl.setFromDate(fromDate.getDate());
				appl.setChildId(childId);
//				appl.setParentsAgree(agree);
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

			sendMessageToProvider(new Integer(provider[0]),subject,message);
			
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

	private void sendMessageToProvider(Integer providerId, String subject, String message) throws RemoteException, CreateException {
		SchoolBusiness schoolBiz = (SchoolBusiness)getServiceInstance(SchoolBusiness.class);
		School prov = schoolBiz.getSchool(providerId);
		UserBusiness userBiz = (UserBusiness)getServiceInstance(UserBusiness.class);
		Collection users = userBiz.getUsersInGroup(prov.getHeadmasterGroupId());

		if (users != null) {
			MessageBusiness messageBiz = (MessageBusiness)getServiceInstance(MessageBusiness.class);
			Iterator it = users.iterator();
			while (it.hasNext()) {
				User providerUser = (User)it.next();
				messageBiz.createUserMessage(providerUser,subject,message);
			}				
		}
		else 
			System.out.println("Got no users for group " + prov.getHeadmasterGroupId());		
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
			school = schoolBiz.getFirstProviderForUser(provider);

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
	
	public boolean rejectApplication(ChildCareApplication application, String subject, String message) {
		UserTransaction t = getSessionContext().getUserTransaction();
		try {
			t.begin();
			application.setCaseStatus(getCaseStatusInactive());
			IWTimestamp now = new IWTimestamp();
			application.setRejectionDate(now.getDate());
			application.store();
			
			if (application.getChildCount() != 0) {
				Iterator it = application.getChildren();
				if (it.hasNext()) {
					Case proc = (Case)it.next();
					proc.setCaseStatus(getCaseStatusOpen());
					proc.store();
					ChildCareApplication child = ((ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class)).findByPrimaryKey(proc.getPrimaryKey());
					sendMessageToProvider(new Integer(child.getProviderId()),subject,message);
				}
			}
			
			t.commit();
			
			return true;
		}
		catch (Exception e) {
			try {
				t.rollback();
			}
			catch (SystemException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
				
		return false;	
	}
	
	public boolean rejectApplication(int applicationId, String subject, String message) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			ChildCareApplication appl = (ChildCareApplication)home.findByPrimaryKey(new Integer(applicationId));
			return rejectApplication(appl,subject,message);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean acceptApplication(ChildCareApplication application, String subject, String message) {
		TransactionManager t = IdegaTransactionManager.getInstance();
		try {
			t.begin();
			application.setCaseStatus(getCaseStatusPreliminary());
			application.store();
			
			MessageBusiness messageBiz = (MessageBusiness)getServiceInstance(MessageBusiness.class);
			messageBiz.createUserMessage(application.getOwner(),subject,message);
					
			t.commit();
			
			return true;
		}
		catch(Exception e) {
			try {
				t.rollback();
			}
			catch (SystemException ex) {
				ex.printStackTrace();
			}	
			e.printStackTrace();
		}
		
		return false;	
	}
	
	public boolean acceptApplication(int applicationId, String subject, String message) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			ChildCareApplication appl = (ChildCareApplication)home.findByPrimaryKey(new Integer(applicationId));

			return acceptApplication(appl,subject,message);
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
	
	public Collection getApplicationsByProvider(int providerId) {
		try {			
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			String caseStatus[] = {getCaseStatusOpen().toString(), getCaseStatusPreliminary().toString(), getCaseStatusContract().toString()};
			
			return home.findAllCasesByProviderStatus(providerId,caseStatus);
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
	
	public Collection getApplicationsByProvider(School provider) {
		try {
			return getApplicationsByProvider(((Integer)provider.getPrimaryKey()).intValue());
		}
		catch (RemoteException e) {
			return null;
		}
	}

	public Collection getApplicationsByProvider(User provider) {
		try {
			SchoolChoiceBusiness schoolBiz = (SchoolChoiceBusiness)getServiceInstance(SchoolChoiceBusiness.class);
			School school;
			school = schoolBiz.getFirstProviderForUser(provider);

			return getApplicationsByProvider(((Integer)school.getPrimaryKey()).intValue());
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
	
	public boolean assignContractToApplication(int id, int userId) {
		TransactionManager t = IdegaTransactionManager.getInstance();
		try {
			t.begin();
			ChildCareApplication appl = ((ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class)).findByPrimaryKey(new Integer(id));
			/**
			 * @todo Fix hardcoding of category and add the other parameters to the contract.
			 */
			int contractId = ContractBusiness.createContract(2,IWTimestamp.RightNow(),IWTimestamp.RightNow(),"C",null);
						
			appl.setContractId(contractId);
			appl.setCaseStatus(getCaseStatusContract());
			appl.store();

			Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
			Font paraFont = new Font(Font.HELVETICA, 10, Font.BOLD);
			Font nameFont = new Font(Font.HELVETICA, 12, Font.BOLDITALIC);
			Font tagFont = new Font(Font.HELVETICA,9,Font.BOLDITALIC);
			Font textFont = new Font(Font.HELVETICA, 8, Font.NORMAL);

			int file_id = ContractWriter.writePDF(contractId, appl.getContract().getCategoryId().intValue(), Integer.toString(id), titleFont, paraFont, tagFont, textFont);

			appl.setContractFileId(file_id);
			appl.store();

			t.commit();
		}
		catch (Exception e) {
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
	
	public boolean assignContractToApplication(String ids[], int userId) {
		boolean done = false;
		
		if (ids != null && ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				done = assignContractToApplication(Integer.parseInt(id),userId);
				if (!done)
					return done;	
			}
		}
		
		return done;	
	}

	public boolean assignApplication(int id, int userId, String subject, String body) {
		TransactionManager t = IdegaTransactionManager.getInstance();
		try {
			t.begin();
			ChildCareApplication appl = ((ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class)).findByPrimaryKey(new Integer(id));

			appl.setCaseStatus(getCaseStatusGranted());
			appl.store();

/*			MessageBusiness messageBiz = (MessageBusiness)getServiceInstance(MessageBusiness.class);
			SystemArchivationMessage msg = messageBiz.createPrintArchivationMessage(((Integer)appl.getOwner().getPrimaryKey()).intValue(),userId,subject,body,appl.getContractFileId());
			msg.setParentCase(appl);*/

			t.commit();
		}
		catch (Exception e) {
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
	
	public boolean assignApplication(String ids[], int userId, String subject, String body) {
		boolean done = false;
		
		if (ids != null && ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				done = assignApplication(Integer.parseInt(id),userId,subject,body);
				if (!done)
					return done;	
			}
		}
		
		return done;	
	}
	
}