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
import java.sql.Date;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.childcare.check.business.CheckBusiness;
import se.idega.idegaweb.commune.childcare.check.data.Check;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplicationHome;
import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosis;
import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosisHome;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.data.Message;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;

import com.idega.block.contract.business.ContractBusiness;
import com.idega.block.contract.business.ContractFinder;
import com.idega.block.contract.business.ContractWriter;
import com.idega.block.contract.data.Contract;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.process.data.Case;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.core.data.Address;
import com.idega.core.data.Phone;
import com.idega.core.data.PostalCode;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOStoreException;
import com.idega.idegaweb.IWBundle;
import com.idega.io.PDFTemplateWriter;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.lowagie.text.ElementTags;
import com.lowagie.text.Font;
import com.lowagie.text.xml.XmlPeer;

/**
 * This class does something very clever.....
 * 
 * @author palli
 * @version 1.0
 */
public class ChildCareBusinessBean extends CaseBusinessBean implements ChildCareBusiness {

	private final static String CASE_CODE_KEY = "MBANBOP";
	private final static char STATUS_SENT_IN = 'A';
	private final static char STATUS_ACCEPTED = 'B';
	private final static char STATUS_PARENTS_ACCEPT = 'C';
	private final static char STATUS_CONTRACT = 'D';
	private final static char STATUS_READY = 'E';

	private ChildCareApplicationHome getChildCareApplicationHome() throws RemoteException {
		return (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
	}

	public ChildCarePrognosisHome getChildCarePrognosisHome() throws RemoteException {
		return (ChildCarePrognosisHome) IDOLookup.getHome(ChildCarePrognosis.class);
	}
	
	public ChildCarePrognosis getPrognosis(int providerID) throws RemoteException {
		try {
			return getChildCarePrognosisHome().findPrognosis(providerID);
		}
		catch (FinderException e) {
			return null;
		}
	}
	
	public void updatePrognosis(int providerID, int threeMonthsPrognosis, int oneYearPrognosis) throws RemoteException {
		try {
			ChildCarePrognosis prognosis = getPrognosis(providerID);
			if (prognosis == null)
				prognosis = getChildCarePrognosisHome().create();
				
			prognosis.setProviderID(providerID);
			prognosis.setThreeMonthsPrognosis(threeMonthsPrognosis);
			prognosis.setOneYearPrognosis(oneYearPrognosis);
			prognosis.setUpdatedDate(new IWTimestamp().getDate());
			prognosis.store();
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
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
				appl.setQueueOrder(((Integer)appl.getPrimaryKey()).intValue());
				appl.setApplicationStatus(getStatusSentIn());
				if (checkId != -1)
					appl.setCheckId(checkId);
				if (freetimeApplication)
					caseBiz.changeCaseStatus(appl, getCaseStatusInactive().getStatus(), user);
				else {
					caseBiz.changeCaseStatus(appl, getCaseStatusOpen().getStatus(), user);
					sendMessageToParents(appl, subject, message);
					updateQueue(appl);
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
	
	public void changePlacingDate(int applicationID, Date placingDate) throws RemoteException {
		try {
			ChildCareApplication application = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
			application.setFromDate(placingDate);
			application.store();
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	private void updateQueue(ChildCareApplication application) {
		try {
			User child = application.getChild();
			Age age = new Age(child.getDateOfBirth());
			User compareChild;
			Age compareAge;
			int queueOrder = -1;
			boolean hasAltered = false;
			
			List applications = new Vector(getChildCareApplicationHome().findApplicationsByProviderAndDate(application.getProviderId(), application.getQueueDate()));
			if (applications.size() > 1) {
				queueOrder = ((ChildCareApplication)applications.get(0)).getQueueOrder();
				Collections.sort(applications, new ChildCareApplicationComparator());
				
				Iterator iter = applications.iterator();
				while (iter.hasNext()) {
					ChildCareApplication element = (ChildCareApplication) iter.next();
					element.setQueueOrder(queueOrder++);
					element.store();
				}
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}

	private void sendMessageToProvider(ChildCareApplication application, String subject, String message) throws RemoteException {
		Collection users = getSchoolBusiness().getHeadmasters(application.getProvider());
		Object[] arguments = { application.getChild().getNameLastFirst(true), application.getProvider().getSchoolName(), new IWTimestamp(application.getFromDate()).toSQLDateString() };
		
		if (users != null) {
			MessageBusiness messageBiz = getMessageBusiness();
			Iterator it = users.iterator();
			while (it.hasNext()) {
				User providerUser = (User) it.next();
				messageBiz.createUserMessage(application,providerUser, subject, MessageFormat.format(message, arguments), false);
			}
		}
		else
			System.out.println("Got no users for provider " + application.getProviderId());
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
	
	public int getNumberOfApplicationsByProvider(int providerID) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusReady().getStatus() };

			return getChildCareApplicationHome().getNumberOfApplications(providerID, caseStatus);
		} catch (IDOException ie) {
			return 0;
		} catch (RemoteException re) {
			return 0;
		}
	}
	
	public int getNumberOfApplicationsByProvider(int providerID, int sortBy, Date fromDate, Date toDate) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusReady().getStatus() };

			return getChildCareApplicationHome().getNumberOfApplications(providerID, caseStatus, sortBy, fromDate, toDate);
		} catch (IDOException ie) {
			return 0;
		} catch (RemoteException re) {
			return 0;
		}
	}
	
	public int getNumberInQueue(ChildCareApplication application) {
		try {
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusReady().getStatus() };
			return getChildCareApplicationHome().getPositionInQueue(application.getQueueOrder(), application.getProviderId(), caseStatus);
		}
		catch (RemoteException e) {
			return -1;
		}
		catch (IDOException e) {
			return -1;
		}
	}

	public int getNumberInQueueByStatus(ChildCareApplication application) {
		try {
			return getChildCareApplicationHome().getPositionInQueue(application.getQueueOrder(), application.getProviderId(), application.getCaseStatus().getStatus());
		}
		catch (RemoteException e) {
			return -1;
		}
		catch (IDOException e) {
			return -1;
		}
	}

	public Collection getUnhandledApplicationsByProvider(int providerId, int numberOfEntries, int startingEntry) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusReady().getStatus() };

			return home.findAllCasesByProviderAndNotInStatus(providerId, caseStatus, numberOfEntries, startingEntry);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		} catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Collection getUnhandledApplicationsByProvider(int providerId, int numberOfEntries, int startingEntry, int sortBy, Date fromDate, Date toDate) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusReady().getStatus() };

			return home.findAllCasesByProviderAndNotInStatus(providerId, sortBy, fromDate, toDate, caseStatus, numberOfEntries, startingEntry);
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

	public boolean placeApplication(int applicationID, String subject, String body, int childCareTime, int groupID, User user) throws RemoteException {
		try {
			ChildCareApplication application = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
			application.setCareTime(childCareTime);
			changeCaseStatus(application, getCaseStatusReady().getStatus(), user);
			
			getSchoolBusiness().storeSchoolClassMember(application.getChildId(), groupID, IWTimestamp.getTimestampRightNow(), ((Integer)user.getPrimaryKey()).intValue());
			sendMessageToParents(application, subject, body);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return false;
	}

	public void moveToGroup(int childID, int providerID , int schoolClassID) throws RemoteException {
		try {
			SchoolClassMember classMember = getSchoolBusiness().getSchoolClassMemberHome().findByUserAndSchool(childID, providerID);
			classMember.setSchoolClassId(schoolClassID);
			classMember.store();
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void removeFromProvider(int childID, int providerID) throws RemoteException {
		try {
			SchoolClassMember classMember = getSchoolBusiness().getSchoolClassMemberHome().findByUserAndSchool(childID, providerID);
			classMember.remove();
		}
		catch (EJBException e1) {
			e1.printStackTrace();
		}
		catch (RemoveException e1) {
			e1.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public boolean acceptApplication(ChildCareApplication application, String subject, String message, User user) {
		UserTransaction t = getSessionContext().getUserTransaction();
		try {
			t.begin();
			CaseBusiness caseBiz = (CaseBusiness) getServiceInstance(CaseBusiness.class);
			application.setApplicationStatus(getStatusAccepted());
			caseBiz.changeCaseStatus(application, getCaseStatusGranted().getStatus(), user);

			sendMessageToParents(application, subject, message);

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

	public boolean cancelContract(ChildCareApplication application, String subject, String message, User user) {
		UserTransaction t = getSessionContext().getUserTransaction();
		try {
			t.begin();
			CaseBusiness caseBiz = (CaseBusiness) getServiceInstance(CaseBusiness.class);
			application.setApplicationStatus('Z');
			caseBiz.changeCaseStatus(application, this.getCaseStatusInactive().getStatus(), user);
			
			removeFromProvider(application.getChildId(), application.getProviderId());
			sendMessageToParents(application, subject, message);

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

	public boolean acceptApplication(int applicationId, String subject, String message, User user) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			ChildCareApplication appl = (ChildCareApplication) home.findByPrimaryKey(new Integer(applicationId));

			return acceptApplication(appl, subject, message, user);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return false;
	}

	
	public boolean rejectOffer(int applicationId, User user) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			ChildCareApplication application = (ChildCareApplication)home.findByPrimaryKey(new Integer(applicationId));
			
			UserTransaction t = getSessionContext().getUserTransaction();
			try {
				t.begin();
				CaseBusiness caseBiz = (CaseBusiness)getServiceInstance(CaseBusiness.class);
//				application.setCaseStatus(getCaseStatusInactive());
				IWTimestamp now = new IWTimestamp();
				application.setRejectionDate(now.getDate());
//				application.store();
				caseBiz.changeCaseStatus(application, getCaseStatusCancelled().getStatus(), user);
			
			
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
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		
		return false;
		
	}
	

/*	public boolean signApplication(ChildCareApplication application) {
		return false;	
	}
	*/

	
	public boolean removeFromQueue(int applicationId, User user) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			ChildCareApplication appl = (ChildCareApplication) home.findByPrimaryKey(new Integer(applicationId));

			return removeFromQueue(appl, user);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public boolean removeFromQueue(ChildCareApplication application, User user) {
		try {
			changeCaseStatus(application, getCaseStatusInactive().getStatus(), user);
			return true;
		}
		catch (RemoteException e) {
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

	public void parentsAgree(int applicationID, User user, String subject, String message) throws RemoteException {
		try {
			ChildCareApplication application = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
			application.setApplicationStatus(this.getStatusParentsAccept());
			changeCaseStatus(application, getCaseStatusPreliminary().getStatus(), user);
			sendMessageToProvider(application, subject, message);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public boolean assignContractToApplication(int applicationID, int childCareTime, User user, Locale locale) {
		UserTransaction transaction = getSessionContext().getUserTransaction();
		try {
			transaction.begin();
			ChildCareApplication application = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
			application.setCareTime(childCareTime);

			/**
			 * @todo Fix hardcoding of category and add the other parameters to the contract.
			 */
			int contractID = ContractBusiness.createContract(2, IWTimestamp.RightNow(), IWTimestamp.RightNow(), "C", null);

			PDFTemplateWriter pdfWriter = new PDFTemplateWriter();
			int file_id = pdfWriter.writeToDatabase(getTagMap(application, locale), getXMLContractURL(getIWApplicationContext().getApplication().getBundle(se.idega.idegaweb.commune.presentation.CommuneBlock.IW_BUNDLE_IDENTIFIER), locale));

			application.setContractFileId(file_id);
			application.setContractId(contractID);
			application.setApplicationStatus(getStatusContract());
			changeCaseStatus(application, getCaseStatusContract().getStatus(), user);

			transaction.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				transaction.rollback();
			}
			catch (SystemException ex) {
				ex.printStackTrace();
			}
			return false;
		}
		return true;
	}

	public boolean assignContractToApplication(String ids[], User user, Locale locale) {
		boolean done = false;

		if (ids != null && ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				done = assignContractToApplication(Integer.parseInt(id), -1, user, locale);
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

	public ChildCareApplication getApplicationForChildAndProvider(int childID, int providerID) throws RemoteException {
		try {
			return getChildCareApplicationHome().findApplicationByChildAndProvider(childID, providerID);
		} 
		catch (FinderException fe) {
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

	public ChildCareApplication getApplication(int applicationID) throws RemoteException {
		try {
			return getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
		}
		catch (FinderException e) {
			return null;
		}
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
	
	protected HashMap getTagMap(ChildCareApplication application, Locale locale) throws RemoteException {
		HashMap map = new HashMap();
		User child = application.getChild();
		User parent1 = application.getOwner();
		Address address = getUserBusiness().getUsersMainAddress(parent1);
		Phone phone = getUserBusiness().getHomePhone(parent1);
		
		School provider = application.getProvider();
		User parent2 = null;
		Collection parents = getUserBusiness().getParentsForChild(child);
		if (parents != null) {
			Iterator iter = parents.iterator();
			while (iter.hasNext()) {
				User parent = (User) iter.next();
				if (((Integer)parent.getPrimaryKey()).intValue() != ((Integer)parent1.getPrimaryKey()).intValue())
					parent2 = parent;
			}
		}
		
		String parent1Name = parent1.getName();
		String parent1PersonalID = PersonalIDFormatter.format(parent1.getPersonalID(), locale);
		String parent2Name = "-";
		String parent2PersonalID = "-";
		if (parent2 != null) {
			parent2Name = parent2.getName();
			parent2PersonalID = PersonalIDFormatter.format(parent2.getPersonalID(), locale);
		}
		
		String addressString = "-";
		if (address != null) {
			addressString = address.getStreetAddress();
			try {
				PostalCode code = address.getPostalCode();
				if (code != null) {
					addressString += ", " + code.getPostalAddress();
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
			
		String phoneString = "-";
		if (phone != null)
			phoneString = phone.getNumber();

		IWTimestamp stamp = new IWTimestamp();
		XmlPeer peer = new XmlPeer(ElementTags.CHUNK, "created");
	  peer.setContent(stamp.getLocaleDate(locale, IWTimestamp.SHORT));
		map.put(peer.getAlias(), peer);
     
		stamp = new IWTimestamp(application.getFromDate());
		peer = new XmlPeer(ElementTags.CHUNK, "dateFrom");
		peer.setContent(stamp.getLocaleDate(locale, IWTimestamp.SHORT));
		map.put(peer.getAlias(), peer);
     
		peer = new XmlPeer(ElementTags.CHUNK, "careTime");
		peer.setContent(String.valueOf(application.getCareTime()));
		map.put(peer.getAlias(), peer);
     
		peer = new XmlPeer(ElementTags.CHUNK, "childName");
		peer.setContent(child.getName());
		map.put(peer.getAlias(), peer);
     
		peer = new XmlPeer(ElementTags.CHUNK, "personalID");
		peer.setContent(PersonalIDFormatter.format(child.getPersonalID(), locale));
		map.put(peer.getAlias(), peer);
     
		peer = new XmlPeer(ElementTags.CHUNK, "provider");
		peer.setContent(provider.getSchoolName());
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "parent1");
		peer.setContent(parent1Name);
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "parent2");
		peer.setContent(parent2Name);
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "personalID1");
		peer.setContent(parent1PersonalID);
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "personalID2");
		peer.setContent(parent2PersonalID);
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "address");
		peer.setContent(addressString);
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "phone");
		peer.setContent(phoneString);
		map.put(peer.getAlias(), peer);
		
   return map;          
	}

	public String getXMLContractURL(IWBundle iwb, Locale locale){
		return "file://"+ iwb.getResourcesRealPath(locale)+"/childcare_contract.xml";
	}
	
	/*public String getBundleIdentifier() {
		return se.idega.idegaweb.commune.presentation.CommuneBlock.IW_BUNDLE_IDENTIFIER;
	}*/
	
	public void setAsPriorityApplication(int applicationID, String message, String body) throws RemoteException {
		try {
			setAsPriorityApplication(getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID)), message, body);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void setAsPriorityApplication(ChildCareApplication application, String message, String body) throws RemoteException {
		application.setHasPriority(true);
		application.store();
		getMessageBusiness().sendMessageToCommuneAdministrators(application, message, body);
	}

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
	/**
	 * @return char
	 */
	public char getStatusAccepted() {
		return STATUS_ACCEPTED;
	}

	/**
	 * @return char
	 */
	public char getStatusContract() {
		return STATUS_CONTRACT;
	}

	/**
	 * @return char
	 */
	public char getStatusParentsAccept() {
		return STATUS_PARENTS_ACCEPT;
	}

	/**
	 * @return char
	 */
	public char getStatusReady() {
		return STATUS_READY;
	}

	/**
	 * @return char
	 */
	public char getStatusSentIn() {
		return STATUS_SENT_IN;
	}

}