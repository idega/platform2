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

import is.idega.idegaweb.member.business.NoCustodianFound;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.childcare.check.business.CheckBusiness;
import se.idega.idegaweb.commune.childcare.check.data.Check;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplicationHome;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.data.Message;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;

import com.idega.block.contract.business.ContractBusiness;
import com.idega.block.contract.business.ContractWriter;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.process.data.Case;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.lowagie.text.Font;

/**
 * This class does something very clever.....
 * 
 * @author palli
 * @version 1.0
 */
public class ChildCareBusinessBean extends CaseBusinessBean implements ChildCareBusiness {

	private final static String CASE_CODE_KEY = "MBANBOP";

	private ChildCareApplicationHome getChildCareApplicationHome() throws RemoteException {
		return (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
	}

	public boolean insertApplications(User user, int provider[], String date, int checkId, int childId, String subject, String message, boolean freetimeApplication) {
		UserTransaction t = getSessionContext().getUserTransaction();

		try {
			t.begin();
			ChildCareApplication appl = null;
			ChildCareApplication firstApplication = null;
			ChildCareApplication parent = null;
			CaseBusiness caseBiz = (CaseBusiness) getServiceInstance(CaseBusiness.class);

			IWTimestamp now = new IWTimestamp();
			IWTimestamp stamp = new IWTimestamp();
			for (int i = 0; i < provider.length; i++) {
				try {
					appl = getChildCareApplicationHome().findApplicationByChildAndChoiceNumber(childId, i + 1);
				} catch (FinderException fe) {
					appl = getChildCareApplicationHome().create();
				}

				if (user != null)
					appl.setOwner(user);
				appl.setProviderId(provider[i]);
				IWTimestamp fromDate = new IWTimestamp(date);
				appl.setFromDate(fromDate.getDate());
				appl.setChildId(childId);
				appl.setQueueDate(now.getDate());
				appl.setMethod(1);
				appl.setChoiceNumber(i + 1);
				stamp.addSeconds((1 - ((i + 1) * 1)));
				appl.setCreated(stamp.getTimestamp());
				if (checkId != -1)
					appl.setCheckId(checkId);
				if (i == 0) {
					if (freetimeApplication)
						caseBiz.changeCaseStatus(appl, getCaseStatusInactive().getStatus(), user);
					else {
						caseBiz.changeCaseStatus(appl, getCaseStatusOpen().getStatus(), user);
						sendMessageToParents(appl, subject, message);
					}
				} else {
					appl.setParentCase(parent);
					caseBiz.changeCaseStatus(appl, getCaseStatusInactive().getStatus(), user);
				}

				parent = appl;
			}

			/**
			 * @todo Bæta við að breyta stöðu á tékkanum sem var notaður
			 */

			if (!freetimeApplication) {
				CheckBusiness checkBiz = (CheckBusiness) getServiceInstance(CheckBusiness.class);
				Check check = checkBiz.getCheck(checkId);
				//			check.setStatus(this.getcases)
				//			check.store();

			}

			t.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				t.rollback();
			} catch (SystemException ex) {
				ex.printStackTrace();
			}

			return false;
		}

		return true;
	}

	private void sendMessageToProvider(Integer providerId, String subject, String message) throws RemoteException, CreateException {
		SchoolBusiness schoolBiz = (SchoolBusiness) getServiceInstance(SchoolBusiness.class);
		School prov = schoolBiz.getSchool(providerId);
		UserBusiness userBiz = (UserBusiness) getServiceInstance(UserBusiness.class);
		Collection users = userBiz.getUsersInGroup(prov.getHeadmasterGroupId());

		if (users != null) {
			MessageBusiness messageBiz = (MessageBusiness) getServiceInstance(MessageBusiness.class);
			Iterator it = users.iterator();
			while (it.hasNext()) {
				User providerUser = (User) it.next();
				messageBiz.createUserMessage(providerUser, subject, message);
			}
		} else
			System.out.println("Got no users for group " + prov.getHeadmasterGroupId());
	}

	private void sendMessageToParents(ChildCareApplication application, String subject, String body) {
		try {
			User child = application.getChild();
			Object[] arguments = { child.getNameLastFirst(true), application.getProvider().getSchoolName()};

			User appParent = application.getOwner();
			if (getUserBusiness().getMemberFamilyLogic().isChildInCustodyOf(child, appParent)) {
				Message message = getMessageBusiness().createUserMessage(appParent, subject, MessageFormat.format(body, arguments));
				message.setParentCase(application);
				message.store();
			}

			try {
				Collection parents = getUserBusiness().getMemberFamilyLogic().getCustodiansFor(child);
				Iterator iter = parents.iterator();
				User otherParent = null;
				while (iter.hasNext()) {
					User parent = (User) iter.next();
					if (!getUserBusiness().haveSameAddress(parent, appParent)) {
						Message message = getMessageBusiness().createUserMessage(parent, subject, MessageFormat.format(body, arguments));
						message.setParentCase(application);
						message.store();
					}
				}
			} catch (NoCustodianFound ncf) {
			}
		} catch (RemoteException re) {
		}
	}

	public Collection getUnhandledApplicationsByProvider(int providerId) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);

			return home.findAllCasesByProviderAndStatus(providerId, getCaseStatusOpen());
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		} catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int getNumberOfUnhandledApplicationsByProvider(int providerID) {
		try {
			return getChildCareApplicationHome().getNumberOfApplications(providerID, getCaseStatusOpen().getPrimaryKey().toString());
		} catch (IDOException ie) {
			return 0;
		} catch (RemoteException re) {
			return 0;
		}
	}

	public Collection getUnhandledApplicationsByProvider(int providerId, int numberOfEntries, int startingEntry) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);

			return home.findAllCasesByProviderAndStatus(providerId, getCaseStatusOpen().getPrimaryKey().toString(), numberOfEntries, startingEntry);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		} catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Collection getUnhandledApplicationsByProvider(School provider) {
		//try {
		return getUnhandledApplicationsByProvider(((Integer) provider.getPrimaryKey()).intValue());
		//}
		//catch (RemoteException e) {
		//	e.printStackTrace();
		//	return null;
		//}
	}

	public Collection getUnhandledApplicationsByProvider(User provider) {
		try {
			SchoolChoiceBusiness schoolBiz = (SchoolChoiceBusiness) getServiceInstance(SchoolChoiceBusiness.class);
			School school;
			school = schoolBiz.getFirstProviderForUser(provider);

			return getUnhandledApplicationsByProvider(((Integer) school.getPrimaryKey()).intValue());
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		} catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Collection getUnsignedApplicationsByProvider(int providerId) {
		try {

			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);

			return home.findAllCasesByProviderAndStatus(providerId, this.getCaseStatusPreliminary());
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		} catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Collection getUnsignedApplicationsByProvider(School provider) {
		//try {
		return getUnsignedApplicationsByProvider(((Integer) provider.getPrimaryKey()).intValue());
		//}
		//catch (RemoteException e) {
		//	return null;
		//}
	}

	public Collection getUnsignedApplicationsByProvider(User provider) {
		try {
			SchoolChoiceBusiness schoolBiz = (SchoolChoiceBusiness) getServiceInstance(SchoolChoiceBusiness.class);
			School school;
			school = schoolBiz.getFirstProviderForUser(provider);

			return getUnsignedApplicationsByProvider(((Integer) school.getPrimaryKey()).intValue());
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		} catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean rejectApplication(ChildCareApplication application, String subject, String message, String newSubject, String newBody, User user) {
		UserTransaction t = getSessionContext().getUserTransaction();
		try {
			t.begin();
			CaseBusiness caseBiz = (CaseBusiness) getServiceInstance(CaseBusiness.class);
			IWTimestamp now = new IWTimestamp();
			application.setRejectionDate(now.getDate());
			caseBiz.changeCaseStatus(application, getCaseStatusDenied().getStatus(), user);
			sendMessageToParents(application, subject, message);

			if (application.getChildCount() != 0) {
				Iterator it = application.getChildren();
				if (it.hasNext()) {
					Case proc = (Case) it.next();
					caseBiz.changeCaseStatus(proc, getCaseStatusOpen().getStatus(), user);
					ChildCareApplication child = ((ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class)).findByPrimaryKey(proc.getPrimaryKey());
					sendMessageToParents(child, newSubject, newBody);
				}
			}

			t.commit();

			return true;
		} catch (Exception e) {
			try {
				t.rollback();
			} catch (SystemException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}

		return false;
	}

	public boolean rejectApplication(int applicationId, String rejectSubject, String rejectBody, String newSubject, String newBody, User user) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			ChildCareApplication appl = (ChildCareApplication) home.findByPrimaryKey(new Integer(applicationId));
			return rejectApplication(appl, rejectSubject, rejectBody, newSubject, newBody, user);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean acceptApplication(ChildCareApplication application, String subject, String message, User user) {
		UserTransaction t = getSessionContext().getUserTransaction();
		try {
			t.begin();
			CaseBusiness caseBiz = (CaseBusiness) getServiceInstance(CaseBusiness.class);
			//			application.setCaseStatus(getCaseStatusPreliminary());
			//			application.store();
			caseBiz.changeCaseStatus(application, getCaseStatusPreliminary().getStatus(), user);

			MessageBusiness messageBiz = (MessageBusiness) getServiceInstance(MessageBusiness.class);
			messageBiz.createUserMessage(application.getOwner(), subject, message);

			t.commit();

			return true;
		} catch (Exception e) {
			try {
				t.rollback();
			} catch (SystemException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}

		return false;
	}

	public boolean acceptApplication(int applicationId, String subject, String message, User user) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			ChildCareApplication appl = (ChildCareApplication) home.findByPrimaryKey(new Integer(applicationId));

			return acceptApplication(appl, subject, message, user);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return false;
	}

	/*	public boolean signApplication(ChildCareApplication application) {
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
		}	*/

	public Collection getApplicationsByProvider(int providerId) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			String caseStatus[] = { getCaseStatusOpen().getStatus(), getCaseStatusPreliminary().getStatus(), getCaseStatusContract().getStatus()};

			return home.findAllCasesByProviderStatus(providerId, caseStatus);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		} catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Collection getApplicationsByProvider(School provider) {
		//try {
		return getApplicationsByProvider(((Integer) provider.getPrimaryKey()).intValue());
		//}
		//catch (RemoteException e) {
		//	return null;
		//}
	}

	public Collection getApplicationsByProvider(User provider) {
		try {
			SchoolChoiceBusiness schoolBiz = (SchoolChoiceBusiness) getServiceInstance(SchoolChoiceBusiness.class);
			School school;
			school = schoolBiz.getFirstProviderForUser(provider);

			return getApplicationsByProvider(((Integer) school.getPrimaryKey()).intValue());
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		} catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean assignContractToApplication(int id, User user) {
		UserTransaction t = getSessionContext().getUserTransaction();
		try {
			t.begin();
			ChildCareApplication appl = ((ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class)).findByPrimaryKey(new Integer(id));
			CaseBusiness caseBiz = (CaseBusiness) getServiceInstance(CaseBusiness.class);

			/**
			 * @todo Fix hardcoding of category and add the other parameters to the contract.
			 */
			int contractId = ContractBusiness.createContract(2, IWTimestamp.RightNow(), IWTimestamp.RightNow(), "C", null);

			Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
			Font paraFont = new Font(Font.HELVETICA, 10, Font.BOLD);
			Font nameFont = new Font(Font.HELVETICA, 12, Font.BOLDITALIC);
			Font tagFont = new Font(Font.HELVETICA, 9, Font.BOLDITALIC);
			Font textFont = new Font(Font.HELVETICA, 8, Font.NORMAL);

			int file_id = ContractWriter.writePDF(contractId, 2, Integer.toString(id), titleFont, paraFont, tagFont, textFont);

			appl.setContractFileId(file_id);
			appl.setContractId(contractId);
			//			appl.setCaseStatus(getCaseStatusContract());
			caseBiz.changeCaseStatus(appl, getCaseStatusContract().getStatus(), user);
			//			appl.store();

			t.commit();
		} catch (Exception e) {
			try {
				t.rollback();
			} catch (SystemException ex) {
				ex.printStackTrace();
			}

			return false;
		}

		return true;
	}

	public boolean assignContractToApplication(String ids[], User user) {
		boolean done = false;

		if (ids != null && ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				done = assignContractToApplication(Integer.parseInt(id), user);
				if (!done)
					return done;
			}
		}

		return done;
	}

	public boolean assignApplication(int id, User user, String subject, String body) {
		UserTransaction t = getSessionContext().getUserTransaction();
		try {
			t.begin();
			ChildCareApplication appl = ((ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class)).findByPrimaryKey(new Integer(id));

			CaseBusiness caseBiz = (CaseBusiness) getServiceInstance(CaseBusiness.class);
			caseBiz.changeCaseStatus(appl, getCaseStatusGranted().getStatus(), user);
			sendMessageToParents(appl, subject, body);

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

	public boolean assignApplication(String ids[], User user, String subject, String body) {
		boolean done = false;

		if (ids != null && ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				done = assignApplication(Integer.parseInt(id), user, subject, body);
				if (!done)
					return done;
			}
		}

		return done;
	}

	public Collection getGrantedApplicationsByUser(User owner) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);

			return home.findAllCasesByUserAndStatus(owner, getCaseStatusGranted().getStatus());
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		} catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Collection getApplicationsByUser(User owner) {
		Collection c = null;
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);

			c = home.findAllCasesByUserAndStatus(owner, getCaseStatusGranted().getStatus());
			c.addAll(home.findAllCasesByUserAndStatus(owner, getCaseStatusOpen().getStatus()));
			c.addAll(home.findAllCasesByUserAndStatus(owner, getCaseStatusDenied().getStatus()));
			c.addAll(home.findAllCasesByUserAndStatus(owner, getCaseStatusInactive().getStatus()));

		} catch (RemoteException e) {
			e.printStackTrace();
			c = null;
		} catch (FinderException e) {
			e.printStackTrace();
			c = null;
		}

		return c;
	}

	public Collection getApplicationsForChild(User child) {
		try {
			return getChildCareApplicationHome().findApplicationByChild(((Integer) child.getPrimaryKey()).intValue());
		} catch (FinderException fe) {
			return null;
		} catch (RemoteException re) {
			return null;
		}
	}

	public Collection findAllGrantedApplications() {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);

			return home.findAllCasesByStatus(getCaseStatusGranted().getStatus());
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		} catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Collection findAllApplicationsWithChecksToRedeem() {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			String statusRedeem = getCaseStatusRedeem().getStatus();
			Collection appl = home.findAllCasesByStatus(getCaseStatusGranted().getStatus());
			Iterator it = appl.iterator();
			while (it.hasNext()) {
				ChildCareApplication application = (ChildCareApplication) it.next();
				try {
					Check check = application.getCheck().getCheck();
					if (check.getStatus().equals(statusRedeem))
						it.remove();
				} catch (Exception e) {
				}
			}

			return appl;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public ChildCareApplication getApplicationByPrimaryKey(String key) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);

			return home.findByPrimaryKey(new Integer(key));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean redeemApplication(String applicationId, User performer) {
		ChildCareApplication appl = getApplicationByPrimaryKey(applicationId);
		if (appl == null)
			return false;

		CaseBusiness caseBiz;
		try {
			caseBiz = (CaseBusiness) getServiceInstance(CaseBusiness.class);
			caseBiz.changeCaseStatus(appl, getCaseStatusRedeem().getStatus(), performer);
		} catch (RemoteException e) {
			e.printStackTrace();

			return false;
		}

		return true;
	}

	protected ChildCareApplication getChildCareApplicationInstance(Case theCase) throws RuntimeException {
		String caseCode = "unreachable";
		try {
			caseCode = theCase.getCode();
			if (CASE_CODE_KEY.equals(caseCode)) {
				int caseID = ((Integer) theCase.getPrimaryKey()).intValue();
				return this.getApplicationByPrimaryKey(String.valueOf(caseID));
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		throw new ClassCastException("Case with casecode: " + caseCode + " cannot be converted to a schoolchoice");
	}

	/*public String getBundleIdentifier() {
		return se.idega.idegaweb.commune.presentation.CommuneBlock.IW_BUNDLE_IDENTIFIER;
	}*/

	public String getLocalizedCaseDescription(Case theCase, Locale locale) throws RemoteException {
		ChildCareApplication choice = getChildCareApplicationInstance(theCase);
		Object[] arguments = { choice.getChild().getFirstName(), String.valueOf(choice.getChoiceNumber()), choice.getProvider().getName()};

		String desc = super.getLocalizedCaseDescription(theCase, locale);
		return MessageFormat.format(desc, arguments);
	}

	public MessageBusiness getMessageBusiness() throws RemoteException {
		return (MessageBusiness) this.getServiceInstance(MessageBusiness.class);
	}

	public CommuneUserBusiness getUserBusiness() throws RemoteException {
		return (CommuneUserBusiness) this.getServiceInstance(CommuneUserBusiness.class);
	}

	public SchoolBusiness getSchoolBusiness() throws RemoteException {
		return (SchoolBusiness) this.getServiceInstance(SchoolBusiness.class);
	}
}