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
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.childcare.check.business.CheckBusiness;
import se.idega.idegaweb.commune.childcare.check.data.Check;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplicationHome;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractArchive;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractArchiveHome;
import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosis;
import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosisHome;
import se.idega.idegaweb.commune.childcare.data.ChildCareQueue;
import se.idega.idegaweb.commune.childcare.data.ChildCareQueueHome;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.data.Message;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;

import com.idega.block.contract.business.ContractService;
import com.idega.block.contract.data.Contract;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.process.data.Case;
import com.idega.block.school.business.SchoolAreaComparator;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolComparator;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.core.data.Address;
import com.idega.core.data.ICFile;
import com.idega.core.data.Phone;
import com.idega.core.data.PostalCode;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOStoreException;
import com.idega.idegaweb.IWBundle;
import com.idega.io.ITextXMLHandler;
import com.idega.io.MemoryFileBuffer;
import com.idega.user.data.User;
import com.idega.util.FileUtil;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.TextSoap;
import com.lowagie.text.ElementTags;
import com.lowagie.text.xml.XmlPeer;

/**
 * This class does something very clever.....
 * 
 * @author palli
 * @version 1.0
 */
public class ChildCareBusinessBean extends CaseBusinessBean implements ChildCareBusiness {

	final int DBV_WITH_PLACE = 0;
	final int DBV_WITHOUT_PLACE = 1;
	final int FS_WITH_PLACE = 2;
	final int FS_WITHOUT_PLACE = 3;

	private final static String CASE_CODE_KEY = "MBANBOP";
	public final static char STATUS_SENT_IN = 'A';
	private final static char STATUS_PRIORITY = 'B';
	private final static char STATUS_ACCEPTED = 'C';
	private final static char STATUS_PARENTS_ACCEPT = 'D';
	private final static char STATUS_CONTRACT = 'E';
	private final static char STATUS_READY = 'F';
	private final static char STATUS_CANCELLED = 'V';
	private final static char STATUS_MOVED = 'W';
	private final static char STATUS_NEW_CHOICE = 'X';
	private final static char STATUS_NOT_ANSWERED = 'Y';
	private final static char STATUS_REJECTED = 'Z';
	
	public String getBundleIdentifier() {
		return se.idega.idegaweb.commune.presentation.CommuneBlock.IW_BUNDLE_IDENTIFIER;
	}

	private ChildCareApplicationHome getChildCareApplicationHome() throws RemoteException {
		return (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
	}

	public ChildCarePrognosisHome getChildCarePrognosisHome() throws RemoteException {
		return (ChildCarePrognosisHome) IDOLookup.getHome(ChildCarePrognosis.class);
	}
	
	public ChildCareContractArchiveHome getChildCareContractArchiveHome() throws RemoteException {
		return (ChildCareContractArchiveHome) IDOLookup.getHome(ChildCareContractArchive.class);
	}

	public ChildCareQueueHome getChildCareQueueHome() throws RemoteException {
		return (ChildCareQueueHome) IDOLookup.getHome(ChildCareQueue.class);
	}

	public ChildCarePrognosis getPrognosis(int providerID) throws RemoteException {
		try {
			return getChildCarePrognosisHome().findPrognosis(providerID);
		}
		catch (FinderException e) {
			return null;
		}
	}
	
	public ChildCareApplication getApplication(int childID, int choiceNumber) throws RemoteException {
		try {
			return getChildCareApplicationHome().findApplicationByChildAndChoiceNumber(childID, choiceNumber);
		}
		catch (FinderException fe) {
			return null;
		}
	}
	
	public ChildCareApplication getNonActiveApplication(int childID, int choiceNumber) throws RemoteException {
		try {
			String[] caseStatus = { getCaseStatusOpen().getStatus(), getCaseStatusGranted().getStatus() };
			return getChildCareApplicationHome().findApplicationByChildAndChoiceNumberInStatus(childID, choiceNumber, caseStatus);
		}
		catch (FinderException fe) {
			return null;
		}
	}
	
	public ChildCareApplication getNewestApplication(int providerID, Date date) throws RemoteException {
		try {
			return getChildCareApplicationHome().findNewestApplication(providerID, date);
		}
		catch (FinderException fe) {
			return null;
		}
	}
	
	private ChildCareApplication getOldestApplication(int providerID, Date date) throws RemoteException {
		try {
			return getChildCareApplicationHome().findOldestApplication(providerID, date);
		}
		catch (FinderException fe) {
			return null;
		}
	}
	
	public boolean hasApplications(int childID) throws RemoteException {
		try {
			int applications = getChildCareApplicationHome().getNumberOfApplicationsForChild(childID);
			if (applications > 0)
				return true;
			return false;
		}
		catch (IDOException e) {
			return false;
		}
	}
	
	public void updatePrognosis(int providerID, int threeMonthsPrognosis, int oneYearPrognosis, int threeMonthsPriority, int oneYearPriority) throws RemoteException {
		try {
			ChildCarePrognosis prognosis = getPrognosis(providerID);
			if (prognosis == null)
				prognosis = getChildCarePrognosisHome().create();
				
			prognosis.setProviderID(providerID);
			prognosis.setThreeMonthsPrognosis(threeMonthsPrognosis);
			prognosis.setOneYearPrognosis(oneYearPrognosis);
			prognosis.setThreeMonthsPriority(threeMonthsPriority);
			prognosis.setOneYearPriority(oneYearPriority);
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
	
	public void setChildCareQueueExported(ChildCareQueue queue) {
		queue.setExported(true);
		queue.store();
	}
	
	public void exportQueue(Collection choices) {
		if (choices != null) {
			UserTransaction t = getSessionContext().getUserTransaction();

			try {
				t.begin();
				Iterator iter = choices.iterator();
				while (iter.hasNext()) {
					ChildCareQueue element = (ChildCareQueue) iter.next();
					element.setExported(true);
					element.store();
				}
				t.commit();
			}
			catch (Exception e) {
				e.printStackTrace();
				try {
					t.rollback();
				}
				catch (SystemException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	public boolean getHasUnexportedChoices(int childID) {
		try {
			int choices = getChildCareQueueHome().getNumberOfNotExported(childID);
			if (choices > 0)
				return true;
			return false;
		}
		catch (RemoteException e) {
			return false;
		}
		catch (IDOException e) {
			return false;
		}
	}
	
	public boolean insertApplications(User user, int provider[], String date, int checkId, int childId, String subject, String message, boolean freetimeApplication) {
		String[] dates = new String[provider.length];
		for (int a = 0; a < dates.length; a++) {
			dates[a] = date;
		}
		return insertApplications(user, provider, dates, null, checkId, childId, subject, message, freetimeApplication);
	}
	
	public boolean insertApplications(User user, int provider[], String[] dates, String message, int checkId, int childId, String subject, String body, boolean freetimeApplication) {
		return insertApplications(user, provider, dates, message, checkId, childId, subject, subject, freetimeApplication, true, null, null);
	}
	
	public boolean insertApplications(User user, int provider[], String[] dates, String message, int childID, Date[] queueDates, boolean[] hasPriority) {
		return insertApplications(user, provider, dates, message, -1, childID, null, null, false, false, queueDates, hasPriority);
	}
	
	public boolean insertApplications(User user, int provider[], String[] dates, String message, int checkId, int childId, String subject, String body, boolean freetimeApplication, boolean sendMessages, Date[] queueDates, boolean[] hasPriority) {
		UserTransaction t = getSessionContext().getUserTransaction();

		try {
			t.begin();
			ChildCareApplication appl = null;
			User child = getUserBusiness().getUser(childId);
			IWTimestamp now;
			String[] caseStatus = { getCaseStatusOpen().getStatus(), getCaseStatusGranted().getStatus() };
						
			try {
				IWTimestamp dateOfBirth = new IWTimestamp(child.getDateOfBirth());
				now = new IWTimestamp();
				int days = IWTimestamp.getDaysBetween(dateOfBirth, now);
				if (days < 90) {
					dateOfBirth.addMonths(3);
					now = new IWTimestamp(dateOfBirth);
				}
			}
			catch (NullPointerException e) {
				now = new IWTimestamp();
			}

			IWTimestamp stamp = new IWTimestamp();
			for (int i = 0; i < provider.length; i++) {
				int providerID = provider[i];
				if (providerID != -1) {
					try {
						appl = getChildCareApplicationHome().findApplicationByChildAndChoiceNumberInStatus(childId, i + 1, caseStatus);
					}
					catch (FinderException fe) {
						appl = getChildCareApplicationHome().create();
					}
	
					IWTimestamp fromDate = new IWTimestamp(dates[i]);
					if (canChangeApplication(appl, providerID, fromDate)) {
						if (appl.getProviderId() != providerID && appl.getProviderId() != -1) {
							removeFromQueue(appl, user);
							appl = getChildCareApplicationHome().create();
						}
						if (user != null)
							appl.setOwner(user);
						appl.setProviderId(providerID);
						appl.setFromDate(fromDate.getDate());
						if (message != null)
							appl.setMessage(message);
						else
							appl.setMessage("");
						appl.setPresentation("");
						appl.setChildId(childId);
						if (queueDates != null) {
							if (queueDates[i] != null)
								appl.setQueueDate(queueDates[i]);
							else
								appl.setQueueDate(now.getDate());
						}
						else
							appl.setQueueDate(now.getDate());
						appl.setMethod(1);
						appl.setChoiceNumber(i + 1);
						stamp.addSeconds((1 - ((i + 1) * 1)));
						appl.setCreated(stamp.getTimestamp());
						appl.setQueueOrder(((Integer)appl.getPrimaryKey()).intValue());
						appl.setApplicationStatus(getStatusSentIn());
						
						if (hasPriority != null)
							appl.setHasQueuePriority(hasPriority[i]);
						else {
							if (hasQueuePriority(child, providerID))
								appl.setHasQueuePriority(true);
						}
						
						if (checkId != -1)
							appl.setCheckId(checkId);
						if (freetimeApplication)
							changeCaseStatus(appl, getCaseStatusInactive().getStatus(), user);
						else {
							changeCaseStatus(appl, getCaseStatusOpen().getStatus(), user);
							if (sendMessages) {
								sendMessageToParents(appl, subject, body);
							}
							updateQueue(appl);
						}
					}
				}
			}

			t.commit();
		}
		catch (Exception e) {
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
	
	private boolean hasQueuePriority(User child, int providerID) throws RemoteException {
		SchoolClassMember member = null;
		try {
			member = getSchoolBusiness().getSchoolClassMemberHome().findLatestByUserAndSchool(((Integer) child.getPrimaryKey()).intValue(), providerID);
			if (member != null && member.getNeedsSpecialAttention())
				return true;
		}
		catch (FinderException e) {
			member = null;
		}
		
		Collection parents = getUserBusiness().getParentsForChild(child);
		if (parents != null) {
			IWTimestamp stamp = new IWTimestamp();
			Iterator iter = parents.iterator();
			while (iter.hasNext()) {
				User parent = (User) iter.next();
				Collection children = getUserBusiness().getChildrenForUser(parent);
				if (children != null) {
					Iterator iterator = children.iterator();
					while (iterator.hasNext()) {
						User sibling = (User) iterator.next();
						if (((Integer)child.getPrimaryKey()).intValue() != ((Integer)sibling.getPrimaryKey()).intValue()) {
							try {
								member = getSchoolBusiness().getSchoolClassMemberHome().findLatestByUserAndSchool(((Integer) sibling.getPrimaryKey()).intValue(), providerID);
							}
							catch (FinderException e) {
								member = null;
							}
							
							if (member != null) {
								if (member.getNeedsSpecialAttention())
									return true;
									
								if (member.getRemovedDate() != null) {
									IWTimestamp removed = new IWTimestamp(member.getRemovedDate());
									if (removed.isLaterThan(stamp))
										return true;
								}
								else
									return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
	private boolean canChangeApplication(ChildCareApplication application, int newProviderID, IWTimestamp newFromDate) {
		int oldProviderID = application.getProviderId();
		IWTimestamp oldFromDate = new IWTimestamp();
		if (application.getFromDate() != null)
			oldFromDate = new IWTimestamp(application.getFromDate());
		
		if (oldProviderID != newProviderID)
			return true;
		else {
			if (!oldFromDate.equals(newFromDate))
				return true;
			return false;
		}
	}
	
	public void changePlacingDate(int applicationID, Date placingDate) throws RemoteException {
		try {
			ChildCareApplication application = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
			application.setHasDateSet(true);
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
			ChildCareApplication appl;
			int queueOrder = -1;
			
			List applications = new Vector(getChildCareApplicationHome().findApplicationsByProviderAndDate(application.getProviderId(), application.getQueueDate()));
			if (applications.size() > 1) {
				queueOrder = ((ChildCareApplication)applications.get(0)).getQueueOrder();
				Collections.sort(applications, new ChildCareApplicationComparator());
				
				Iterator iter = applications.iterator();
				while (iter.hasNext()) {
					appl = (ChildCareApplication) iter.next();
					appl.setQueueOrder(queueOrder++);
					appl.store();
				}
			}
			else {
				ChildCareApplication newestApplication = getNewestApplication(application.getProviderId(), application.getQueueDate());
				ChildCareApplication oldestApplication = getOldestApplication(application.getProviderId(), application.getQueueDate());
				if (newestApplication != null && oldestApplication != null) {
					queueOrder = (newestApplication.getQueueOrder() + oldestApplication.getQueueOrder()) / 2;
					application.setQueueOrder(queueOrder);
					application.store();
				}
				else if (newestApplication == null && oldestApplication != null){
					queueOrder = oldestApplication.getQueueOrder() - 100;
					application.setQueueOrder(queueOrder);
					application.store();
				}
				else if (newestApplication != null && oldestApplication == null){
					queueOrder = newestApplication.getQueueOrder() + 100;
					application.setQueueOrder(queueOrder);
					application.store();
				}
				else {
					application.setQueueOrder(((Integer)application.getPrimaryKey()).intValue());
					application.store();
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
	
	public void sendMessageToProvider(ChildCareApplication application, String subject, String message, User sender) throws RemoteException {
		Collection users = getSchoolBusiness().getHeadmasters(application.getProvider());
		Object[] arguments = { application.getChild().getNameLastFirst(true), application.getProvider().getSchoolName(), new IWTimestamp(application.getFromDate()).toSQLDateString() };
		
		if (users != null) {
			MessageBusiness messageBiz = getMessageBusiness();
			Iterator it = users.iterator();
			while (it.hasNext()) {
				User providerUser = (User) it.next();
				messageBiz.createUserMessage(application,providerUser, sender, subject, MessageFormat.format(message, arguments), false);
			}
		}
		else
			System.out.println("Got no users for provider " + application.getProviderId());
	}
	
	private void sendMessageToProvider(ChildCareApplication application, String subject, String message) throws RemoteException {
		Collection users = getSchoolBusiness().getHeadmasters(application.getProvider());
		User child = application.getChild();
		Object[] arguments = { child.getNameLastFirst(true), application.getProvider().getSchoolName(), new IWTimestamp(application.getFromDate()).toSQLDateString(), child.getPersonalID() };
		
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

	public void sendMessageToParents(ChildCareApplication application, String subject, String body) {
		try {
			User child = application.getChild();
			Object[] arguments = { child.getNameLastFirst(true), application.getProvider().getSchoolName() };

			User appParent = application.getOwner();
			if (getUserBusiness().getMemberFamilyLogic().isChildInCustodyOf(child, appParent)) {
				Message message = getMessageBusiness().createUserMessage(appParent, subject, MessageFormat.format(body, arguments));
				message.setParentCase(application);
				message.store();
			}

			try {
				Collection parents = getUserBusiness().getMemberFamilyLogic().getCustodiansFor(child);
				Iterator iter = parents.iterator();
				while (iter.hasNext()) {
					User parent = (User) iter.next();
					if (!getUserBusiness().haveSameAddress(parent, appParent)) {
						Message message = getMessageBusiness().createUserMessage(parent, subject, MessageFormat.format(body, arguments));
						message.setParentCase(application);
						message.store();
					}
				}
			}
			catch (NoCustodianFound ncf) {
				ncf.printStackTrace();
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
	}

	public Collection getQueueChoices(int childID) throws RemoteException {
		try {
			return getChildCareQueueHome().findQueueByChild(childID);
		}
		catch (FinderException e) {
			return null;
		}		
	}
	
	public int getPositionInQueue(ChildCareQueue queue) throws RemoteException {
		try {
			int order = getChildCareQueueHome().getNumberInQueue(queue.getProviderId(), queue.getQueueDate());
			List choices = new ArrayList(getChildCareQueueHome().findQueueByProviderAndDate(queue.getProviderId(), queue.getQueueDate()));
			if (choices != null && !choices.isEmpty()) {
				Collections.sort(choices, new ChildCareQueueComparator());
				Iterator iter = choices.iterator();
				while (iter.hasNext()) {
					order++;
					ChildCareQueue element = (ChildCareQueue) iter.next();
					if (((Integer)element.getPrimaryKey()).intValue() == ((Integer)queue.getPrimaryKey()).intValue())
						return order;
				}
			}
			return ++order;
		}
		catch (IDOException e) {
			e.printStackTrace();
			return 1;
		}
		catch (FinderException e) {
			e.printStackTrace();
			return 1;
		}
	}

	public Collection getInactiveApplicationsByProvider(int providerID) {
		try {
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusDenied().getStatus() };

			return getChildCareApplicationHome().findAllCasesByProviderStatus(providerID, caseStatus);
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
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus() };

			return getChildCareApplicationHome().getNumberOfApplications(providerID, caseStatus);
		} catch (IDOException ie) {
			return 0;
		} catch (RemoteException re) {
			return 0;
		}
	}
	
	public int getNumberOfApplicationsByProvider(int providerID, int sortBy, Date fromDate, Date toDate) {
		try {
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus() };

			return getChildCareApplicationHome().getNumberOfApplications(providerID, caseStatus, sortBy, fromDate, toDate);
		} catch (IDOException ie) {
			return 0;
		} catch (RemoteException re) {
			return 0;
		}
	}
	
	public int getNumberInQueue(ChildCareApplication application) {
		try {
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus() };
			int numberInQueue = 0;
			numberInQueue += getChildCareApplicationHome().getPositionInQueue(application.getQueueDate(), application.getProviderId(), caseStatus);
			numberInQueue += getChildCareApplicationHome().getPositionInQueueByDate(application.getQueueOrder(), application.getQueueDate(), application.getProviderId(), caseStatus);
			return numberInQueue;
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
			int numberInQueue = 0;
			numberInQueue += getChildCareApplicationHome().getPositionInQueue(application.getQueueDate(), application.getProviderId(), application.getCaseStatus().getStatus());
			numberInQueue += getChildCareApplicationHome().getPositionInQueueByDate(application.getQueueOrder(), application.getQueueDate(), application.getProviderId(), application.getCaseStatus().getStatus());
			return numberInQueue;
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
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus() };

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
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus() };

			return home.findAllCasesByProviderAndNotInStatus(providerId, sortBy, fromDate, toDate, caseStatus, numberOfEntries, startingEntry);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		} catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Collection getUnhandledApplicationsByChild(int childID) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus(), getCaseStatusDenied().getStatus(), getCaseStatusContract().getStatus(), getCaseStatusPreliminary().getStatus() };

			return home.findApplicationByChildAndNotInStatus(childID, caseStatus);
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
			application.setApplicationStatus(getStatusRejected());
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

	public boolean placeApplication(int applicationID, String subject, String body, int childCareTime, int groupID, User user, Locale locale) throws RemoteException {
		try {
			ChildCareApplication application = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
			application.setCareTime(childCareTime);
			alterValidFromDate(application, application.getFromDate(), locale, user);
			IWTimestamp fromDate = new IWTimestamp(application.getFromDate());
			/*try {
				SchoolClassMember classMember = getSchoolBusiness().getSchoolClassMemberHome().findByUserAndSchool(application.getChildId(), application.getProviderId());
				classMember.setSchoolClassId(groupID);
				classMember.setRegisterDate(fromDate.getTimestamp());
				classMember.store();
			}
			catch (FinderException e) {*/
				getSchoolBusiness().storeSchoolClassMember(application.getChildId(), groupID, fromDate.getTimestamp(), ((Integer)user.getPrimaryKey()).intValue());
			//}
			sendMessageToParents(application, subject, body);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public void alterValidFromDate(int applicationID, Date newDate, Locale locale, User user) throws RemoteException {
		try {
			ChildCareApplication application = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
			alterValidFromDate(application, newDate, locale, user); 
		}
		catch (FinderException e) {
			e.printStackTrace();
		}		
	}

	public void alterValidFromDate(ChildCareApplication application, Date newDate, Locale locale, User user) throws RemoteException {
		application.setApplicationStatus(getStatusReady());
		int oldFileID = application.getContractFileId();
		IWTimestamp fromDate = new IWTimestamp(newDate);

		ITextXMLHandler pdfHandler = new ITextXMLHandler(ITextXMLHandler.PDF);
		List  buffers = pdfHandler.writeToBuffers(getTagMap(application, locale, fromDate, false),getXMLContractURL(getIWApplicationContext().getApplication().getBundle(se.idega.idegaweb.commune.presentation.CommuneBlock.IW_BUNDLE_IDENTIFIER), locale));
	
			ICFile contractFile = pdfHandler.writeToDatabase((MemoryFileBuffer)buffers.get(0),"contract.pdf",pdfHandler.getPDFMimeType());
			int fileID = ((Integer)contractFile.getPrimaryKey()).intValue();

		application.setContractFileId(fileID);
		changeCaseStatus(application, getCaseStatusReady().getStatus(), user);
		addContractToArchive(oldFileID, application, -1, fromDate.getDate());
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
	
	public void removeFromProvider(int childID, int providerID, Timestamp date, boolean parentalLeave, String message) throws RemoteException {
		try {
			SchoolClassMember classMember = getSchoolBusiness().getSchoolClassMemberHome().findByUserAndSchool(childID, providerID);
			classMember.setRemovedDate(date);
			classMember.setNeedsSpecialAttention(parentalLeave);
			classMember.setNotes(message);
			classMember.store();
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public boolean acceptApplication(ChildCareApplication application, IWTimestamp validUntil, String subject, String message, User user) {
		UserTransaction t = getSessionContext().getUserTransaction();
		try {
			t.begin();
			CaseBusiness caseBiz = (CaseBusiness) getServiceInstance(CaseBusiness.class);
			application.setApplicationStatus(getStatusAccepted());
			application.setOfferValidUntil(validUntil.getDate());
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

	public boolean retractOffer(int applicationID, String subject, String message, User user) throws RemoteException {
		try {
			ChildCareApplication appl = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));

			return retractOffer(appl, subject, message, user);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean retractOffer(ChildCareApplication application, String subject, String message, User user) {
		UserTransaction t = getSessionContext().getUserTransaction();
		try {
			t.begin();
			CaseBusiness caseBiz = (CaseBusiness) getServiceInstance(CaseBusiness.class);
			application.setApplicationStatus(getStatusNotAnswered());
			caseBiz.changeCaseStatus(application, getCaseStatusInactive().getStatus(), user);

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

	public boolean reactivateApplication(int applicationID, User user) throws RemoteException {
		try {
			ChildCareApplication appl = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));

			return reactivateApplication(appl, user);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean reactivateApplication(ChildCareApplication application, User user) throws RemoteException {
		application.setApplicationStatus(getStatusSentIn());
		changeCaseStatus(application, getCaseStatusOpen().getStatus(), user);
		return true;
	}

	public boolean cancelContract(ChildCareApplication application, boolean parentalLeave, IWTimestamp date, String message, String subject, String body, User user) {
		UserTransaction t = getSessionContext().getUserTransaction();
		try {
			t.begin();
			CaseBusiness caseBiz = (CaseBusiness) getServiceInstance(CaseBusiness.class);
			application.setApplicationStatus(getStatusCancelled());
			application.setRejectionDate(date.getDate());
			caseBiz.changeCaseStatus(application, this.getCaseStatusCancelled().getStatus(), user);
			terminateContract(application.getContractFileId(), date.getDate());
			
			removeFromProvider(application.getChildId(), application.getProviderId(), date.getTimestamp(), parentalLeave, message);
			sendMessageToParents(application, subject, body);

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

	public boolean acceptApplication(int applicationId, IWTimestamp validUntil, String subject, String message, User user) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			ChildCareApplication appl = (ChildCareApplication) home.findByPrimaryKey(new Integer(applicationId));

			return acceptApplication(appl, validUntil, subject, message, user);
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
				application.setApplicationStatus('Z');
//				application.store();
				caseBiz.changeCaseStatus(application, getCaseStatusInactive().getStatus(), user);
			
				String subject = getLocalizedString("child_care.rejected_offer_subject", "A placing offer replied to.");
				String body = getLocalizedString("child_care.rejected_offer_body", "Custodian for {0}, {5} rejects an offer for placing at {1}.");
				sendMessageToProvider(application, subject, body);
			
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
	
	public boolean rejectOfferWithNewDate(int applicationId, User user, Date date) {
		try {
			ChildCareApplication application = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationId));
			
			UserTransaction t = getSessionContext().getUserTransaction();
			try {
				t.begin();
				CaseBusiness caseBiz = (CaseBusiness)getServiceInstance(CaseBusiness.class);
				application.setFromDate(date);
				application.setApplicationStatus(getStatusSentIn());
				caseBiz.changeCaseStatus(application, getCaseStatusOpen().getStatus(), user);
				
				String subject = getLocalizedString("child_care.rejected_with_new_date_subject", "A placing offer replied to.");
				String body = getLocalizedString("child_care.rejected_with_new_date_body", "Custodian for {0}, {5} would like to move the placement date to {2}.");
				sendMessageToProvider(application, subject, body);
			
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
			application.setApplicationStatus(this.getStatusRejected());
			changeCaseStatus(application, getCaseStatusInactive().getStatus(), user);

			String subject = getLocalizedString("child_care.removed_from_queue_subject", "A child removed from the queue.");
			String body = getLocalizedString("child_care.removed_from_queue_body", "Custodian for {0}, {5} has removed you as a choice alternative.  {0} can therefore no longer be found in the queue but in the list of those removed from the queue.");
			sendMessageToProvider(application, subject, body);
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

	public ChildCareApplication getAcceptedApplicationsByChild(int childID) {
		try {
			String caseStatus[] = { getCaseStatusPreliminary().getStatus(), getCaseStatusContract().getStatus()};

			return getChildCareApplicationHome().findActiveApplicationByChildAndStatus(childID, caseStatus);
		}
		catch (RemoteException e) {
			return null;
		}
		catch (FinderException e) {
			return null;
		}
	}
	
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
	
	public Collection getOpenAndGrantedApplicationsByProvider(int providerId) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			String caseStatus[] = { getCaseStatusOpen().getStatus(), getCaseStatusGranted().getStatus()};

			return home.findAllCasesByProviderStatus(providerId, caseStatus);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		} catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}
		

	public Collection getAcceptedApplicationsByProvider(int providerID) throws RemoteException {
		try {
			String[] caseStatus = { getCaseStatusReady().getStatus(), getCaseStatusCancelled().getStatus() };
			return getChildCareApplicationHome().findApplicationsByProviderAndStatus(providerID, caseStatus);
		}
		catch (FinderException e) {
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
			parentsAgree(application, user, subject, message);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void parentsAgree(ChildCareApplication application, User user, String subject, String message) throws RemoteException {
		application.setApplicationStatus(this.getStatusParentsAccept());
		changeCaseStatus(application, getCaseStatusPreliminary().getStatus(), user);
		sendMessageToProvider(application, subject, message);
	}
	
	public void saveComments(int applicationID, String comment) throws RemoteException {
		if (comment != null) {
			try {
				ChildCareApplication application = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
				application.setPresentation(comment);
				application.store();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean assignContractToApplication(int applicationID, int childCareTime, IWTimestamp validFrom, User user, Locale locale, boolean changeStatus) {
		UserTransaction transaction = getSessionContext().getUserTransaction();
		try {
			transaction.begin();
			ChildCareApplication application = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
			application.setCareTime(childCareTime);

			if (validFrom == null)
				validFrom = new IWTimestamp(application.getFromDate());
				
			if (application.getContractFileId() != -1) {
				IWTimestamp terminationDate = new IWTimestamp(validFrom);
				terminationDate.addDays(-1);
				terminateContract(application.getContractFileId(), terminationDate.getDate());
			}
			ITextXMLHandler pdfHandler = new ITextXMLHandler(ITextXMLHandler.TXT+ITextXMLHandler.PDF);
						List  buffers = pdfHandler.writeToBuffers(getTagMap(application, locale, validFrom, !changeStatus),getXMLContractURL(getIWApplicationContext().getApplication().getBundle(se.idega.idegaweb.commune.presentation.CommuneBlock.IW_BUNDLE_IDENTIFIER), locale));
						if(buffers !=null && buffers.size() == 2){
							String contractText = pdfHandler.bufferToString((MemoryFileBuffer)buffers.get(1));
							ICFile contractFile = pdfHandler.writeToDatabase((MemoryFileBuffer)buffers.get(0),"contract.pdf",pdfHandler.getPDFMimeType());
							ContractService service = (ContractService) getServiceInstance(ContractService.class);
							Contract contract = service.getContractHome().create(application.getChildId(),2,validFrom,null,"C",contractText);
							int contractID = ((Integer)contract.getPrimaryKey()).intValue();
							contractFile.addTo(Contract.class,contractID);
			
			
						application.setContractId(contractID);
						application.setContractFileId(((Integer)contractFile.getPrimaryKey()).intValue());
			
			/*
			PDFTemplateWriter pdfWriter = new PDFTemplateWriter();
			int fileID = pdfWriter.writeToDatabase(getTagMap(application, locale, validFrom, !changeStatus), getXMLContractURL(getIWApplicationContext().getApplication().getBundle(se.idega.idegaweb.commune.presentation.CommuneBlock.IW_BUNDLE_IDENTIFIER), locale));
			//TODO Change to a more appropriate method call
			Contract contract = ContractBusiness.createContract(application.getChildId(), 2, validFrom, null, "C", (Map)null);
			int contractID = ((Integer)contract.getPrimaryKey()).intValue();
			application.setContractId(contractID);
			application.setContractFileId(fileID);
			*/
			if (changeStatus) {
				application.setApplicationStatus(getStatusContract());
				changeCaseStatus(application, getCaseStatusContract().getStatus(), user);
				
				String subject = getLocalizedString("child_care.contract_created_subject", "A child care contract has been created", locale);
				String body = getLocalizedString("child_care.contract_created_body", "Your child care contract for {0} has been created and will be sent to you in a few days. Please write in the desired care time, sign it and then return the contract to us.\n\nWith best regards,\n{1}", locale);
				sendMessageToParents(application, subject, body);
			}
			else {
				application.store();

				String subject = getLocalizedString("child_care.alter_caretime_subject", "A contract with changed care time has been created", locale);
				String body = getLocalizedString("child_care.alter_caretime_body", "Your child care contract with altered care time for {0} has been created and will be sent to you in a few days. Please write in the desired care time, sign it and then return the contract to us.\n\nWith best regards,\n{1}", locale);
				sendMessageToParents(application, subject, body);
			}
			addContractToArchive(-1, application, contractID, validFrom.getDate());
						}
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
				done = assignContractToApplication(Integer.parseInt(id), -1, null, user, locale, false);
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
		return getApplicationsForChild(((Integer) child.getPrimaryKey()).intValue());
	}
	
	public Collection getApplicationsForChild(int childId) {
		try {
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusDenied().getStatus(), getCaseStatusReady().getStatus() };
			return getChildCareApplicationHome().findApplicationByChildAndNotInStatus(childId, caseStatus);
		} catch (FinderException fe) {
			return null;
		} catch (RemoteException re) {
			return null;
		}
	}		

	public int getNumberOfApplicationsForChildByStatus(int childID, String caseStatus) throws RemoteException {
		try {
			return getChildCareApplicationHome().getNumberOfApplicationsForChild(childID, caseStatus);
		} 
		catch (IDOException ie) {
			return 0;
		}
	}
	
	public int getNumberOfApplicationsForChild(int childID) throws RemoteException {
		try {
			return getChildCareApplicationHome().getNumberOfApplicationsForChild(childID);
		} 
		catch (IDOException ie) {
			return 0;
		}
	}
	
	public int getNumberOfApplicationsForChildNotInactive(int childID) throws RemoteException {
		try {
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusDenied().getStatus() };
			return getChildCareApplicationHome().getNumberOfApplicationsForChildNotInStatus(childID, caseStatus);
		} 
		catch (IDOException ie) {
			return 0;
		}
	}
	
	public boolean hasOutstandingOffers(int childID) throws RemoteException {
		int numberOfOffers = getNumberOfApplicationsForChildByStatus(childID, getCaseStatusGranted().getStatus());
		if (numberOfOffers > 0)
			return true;
		return false;
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
	
	protected HashMap getTagMap(ChildCareApplication application, Locale locale, IWTimestamp validFrom, boolean isChange) throws RemoteException {
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
     
		peer = new XmlPeer(ElementTags.CHUNK, "dateFrom");
		if (validFrom != null) {
			peer.setContent(validFrom.getLocaleDate(locale, IWTimestamp.SHORT));
		}
		else {
			stamp = new IWTimestamp(application.getFromDate());
			peer.setContent(stamp.getLocaleDate(locale, IWTimestamp.SHORT));
		}
		map.put(peer.getAlias(), peer);
     
		peer = new XmlPeer(ElementTags.CHUNK, "careTime");
		if (application.getCareTime() != -1 && !isChange)
			peer.setContent(String.valueOf(application.getCareTime()));
		else
			peer.setContent("....");
		map.put(peer.getAlias(), peer);
     
		peer = new XmlPeer(ElementTags.CHUNK, "careTimeChange");
		if (application.getCareTime() != -1 && isChange)
			peer.setContent(String.valueOf(application.getCareTime()));
		else
			peer.setContent("....");
		map.put(peer.getAlias(), peer);
     
		peer = new XmlPeer(ElementTags.CHUNK, "childName");
		peer.setContent(TextSoap.convertSpecialCharactersToNumeric(child.getName()));
		map.put(peer.getAlias(), peer);
     
		peer = new XmlPeer(ElementTags.CHUNK, "personalID");
		peer.setContent(PersonalIDFormatter.format(child.getPersonalID(), locale));
		map.put(peer.getAlias(), peer);
     
		peer = new XmlPeer(ElementTags.CHUNK, "provider");
		peer.setContent(TextSoap.convertSpecialCharactersToNumeric(provider.getSchoolName()));
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "parent1");
		peer.setContent(TextSoap.convertSpecialCharactersToNumeric(parent1Name));
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "parent2");
		peer.setContent(TextSoap.convertSpecialCharactersToNumeric(parent2Name));
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "personalID1");
		peer.setContent(parent1PersonalID);
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "personalID2");
		peer.setContent(parent2PersonalID);
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "address");
		peer.setContent(TextSoap.convertSpecialCharactersToNumeric(addressString));
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "phone");
		peer.setContent(phoneString);
		map.put(peer.getAlias(), peer);
		
   return map;          
	}
	
	public String getXMLContractURL(IWBundle iwb, Locale locale){
		return iwb.getResourcesRealPath(locale) + FileUtil.getFileSeparator() + "childcare_contract.xml";
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
	
	public boolean hasBeenPlacedWithOtherProvider(int childID, int providerID) throws RemoteException {
		try {
			String caseStatus[] = {	getCaseStatusReady().getStatus() };
			int applications = getChildCareApplicationHome().getNumberOfPlacedApplications(childID, providerID, caseStatus);
			if (applications > 0)
				return true;
			return false;
		}
		catch (IDOException e) {
			return false;
		}
	}
	
	private void addContractToArchive(int contractFileID, ChildCareApplication application, int contractID, Date validFrom) {
		try {
			ChildCareContractArchive archive = null;
			if (contractFileID != -1)
				archive = getChildCareContractArchiveHome().findByContractFileID(contractFileID);
			else
				archive = getChildCareContractArchiveHome().create();
				
			archive.setChildID(application.getChildId());
			archive.setContractFileID(application.getContractFileId());
			if (contractID != -1)
				archive.setContractID(contractID);
			archive.setApplication(application);
			archive.setCreatedDate(new IWTimestamp().getDate());
			archive.setValidFromDate(validFrom);
			if (application.getRejectionDate() != null)
				archive.setTerminatedDate(application.getRejectionDate());
			archive.setCareTime(application.getCareTime());
			archive.store();
			
			if (contractFileID != -1) {
				Contract contract = archive.getContract();
				contract.setValidFrom(validFrom);
				contract.store();
			}
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
	}
	
	public ChildCareContractArchive getContractFile(int contractFileID) throws RemoteException {
		try {
			return getChildCareContractArchiveHome().findByContractFileID(contractFileID);
		}
		catch (FinderException e) {
			return null;
		}
	}
	
	public Collection getContractsByChild(int childID) throws RemoteException {
		try {
			return getChildCareContractArchiveHome().findByChild(childID);	
		}
		catch (FinderException e) {
			return new Vector();
		}
	}

	public Collection getContractsByChildAndProvider(int childID, int providerID) throws RemoteException {
		try {
			return getChildCareContractArchiveHome().findByChildAndProvider(childID, providerID);	
		}
		catch (FinderException e) {
			return new Vector();
		}
	}

	public Collection getContractsByApplication(int applicationID) throws RemoteException {
		try {
			return getChildCareContractArchiveHome().findByApplication(applicationID);	
		}
		catch (FinderException e) {
			return new Vector();
		}
	}

	private void terminateContract(int contractFileID, Date terminatedDate) {
		try {
			ChildCareContractArchive archive = getContractFile(contractFileID);
			if (archive != null) {
				IWTimestamp terminate = new IWTimestamp(terminatedDate);
				IWTimestamp validFrom = new IWTimestamp(archive.getValidFromDate());
				if (validFrom.isLaterThan(terminate)) {
					Contract contract = archive.getContract();
					if (contract != null) {
						contract.setStatus("T");
						contract.store();
					}
					archive.remove();
				}
				else {
					archive.setTerminatedDate(terminatedDate);
					Contract contract = archive.getContract();
					if (contract != null) {
						contract.setValidTo(terminatedDate);
						contract.store();
					}
					archive.store();
				}
			}
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (RemoveException e) {
			e.printStackTrace();
		}
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

	public CheckBusiness getCheckBusiness() throws RemoteException {
		return (CheckBusiness) this.getServiceInstance(CheckBusiness.class);
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
	public char getStatusPriority() {
		return STATUS_PRIORITY;
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
	
	/**
	 * @return char
	 */
	public char getStatusCancelled() {
		return STATUS_CANCELLED;
	}
	
	/**
	 * @return char
	 */
	public char getStatusMoved() {
		return STATUS_MOVED;
	}
	
	/**
	 * @return char
	 */
	public char getStatusRejected() {
		return STATUS_REJECTED;
	}
	
	/**
	 * @return char
	 */
	public char getStatusNotAnswered() {
		return STATUS_NOT_ANSWERED;
	}
	
	/**
	 * @return char
	 */
	public char getStatusNewChoice() {
		return STATUS_NEW_CHOICE;
	}
	
	public int getQueueTotalByProvider(int providerID) throws RemoteException {
		try {
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus() };
			return getChildCareApplicationHome().getQueueSizeNotInStatus(providerID, caseStatus);
		}
		catch (IDOException e) {
			return 0;
		}
	}

	public int getQueueByProvider(int providerID) throws RemoteException {
		try {
			return getChildCareApplicationHome().getQueueSizeInStatus(providerID, getCaseStatusOpen().getStatus());
		}
		catch (IDOException e) {
			return 0;
		}
	}

	public int getQueueTotalByArea(int areaID) throws RemoteException {
		try {
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus() };
			return getChildCareApplicationHome().getQueueSizeByAreaNotInStatus(areaID, caseStatus);
		}
		catch (IDOException e) {
			return 0;
		}
	}

	public int getQueueByArea(int areaID) throws RemoteException {
		try {
			return getChildCareApplicationHome().getQueueSizeByAreaInStatus(areaID, getCaseStatusOpen().getStatus());
		}
		catch (IDOException e) {
			return 0;
		}
	}
	
	public int getOldQueueTotal(String[] queueType, boolean exported) {
		try {
			return getChildCareQueueHome().getTotalCount(queueType, exported);
		}
		catch (RemoteException e) {
			return 0;
		}
		catch (IDOException e) {
			return 0;
		}
	}
	
	public Map getProviderAreaMap(Collection schoolAreas, Locale locale, String emptyString) throws RemoteException {
		try {
			SortedMap areaMap = new TreeMap(new SchoolAreaComparator(locale));
			if (schoolAreas != null) {
				List areas = new ArrayList(schoolAreas);

				Collection schoolTypes = getSchoolBusiness().findAllSchoolTypesInCategory("CHILDCARE");
				Iterator iter = areas.iterator();
				while (iter.hasNext()) {
					SortedMap providerMap = new TreeMap(new SchoolComparator(locale));
					providerMap.put("-1", emptyString);
					
					SchoolArea area = (SchoolArea) iter.next();
					Collection providers = getSchoolBusiness().findAllSchoolsByAreaAndTypes(((Integer) area.getPrimaryKey()).intValue(), schoolTypes);
					if (providers != null) {
						Iterator iterator = providers.iterator();
						while (iterator.hasNext()) {
							School provider = (School) iterator.next();
							providerMap.put(provider, provider);
						}
					}
					areaMap.put(area, providerMap);
				}
			}
			return areaMap;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}
	
	public ChildCareContractArchive getValidContract(int applicationID) throws RemoteException {
		IWTimestamp stamp = new IWTimestamp();
		return getValidContract(applicationID, stamp.getDate());
	}
	
	public ChildCareContractArchive getValidContract(int applicationID, Date validDate) throws RemoteException {
		try {
			return getChildCareContractArchiveHome().findValidContractByApplication(applicationID, validDate);
		}
		catch (FinderException fe) {
			try {
				return getContractFile(getApplication(applicationID).getContractFileId());
			}
			catch (NullPointerException e) {
				return null;
			}
		}
	}
	
	public ChildCareApplication getActiveApplicationByChild(int childID) throws RemoteException {
		try {
			return getChildCareApplicationHome().findActiveApplicationByChild(childID);
		}
		catch (FinderException fe) {
			return null;
		}
	}
	
	public boolean hasActiveApplication(int childID) throws RemoteException {
		try {
			int numberOfApplications = getChildCareApplicationHome().getNumberOfActiveApplications(childID);
			if (numberOfApplications > 0)
				return true;
			return false;
		}
		catch (IDOException fe) {
			return false;
		}
	}
	
	public ChildCareContractArchive getValidContractByChild(int childID) throws RemoteException {
		IWTimestamp stamp = new IWTimestamp();
		return getValidContractByChild(childID, stamp.getDate());
	}
	
	public ChildCareContractArchive getValidContractByChild(int childID, Date validDate) throws RemoteException {
		try {
			return getChildCareContractArchiveHome().findValidContractByChild(childID, validDate);
		}
		catch (FinderException fe) {
			return null;
		}
	}
	
	public void removeFutureContracts(int applicationID) throws RemoteException {
		IWTimestamp stamp = new IWTimestamp();
		removeFutureContracts(applicationID, stamp.getDate());
	}
	
	public void removeFutureContracts(int applicationID, Date date) throws RemoteException {
		UserTransaction t = getSessionContext().getUserTransaction();

		try {
			t.begin();
			ChildCareApplication application = getApplication(applicationID);
			ChildCareContractArchive archive = getValidContract(applicationID);
			application.setContractFileId(archive.getContractFileID());
			application.setContractId(archive.getContractID());
			application.setCareTime(archive.getCareTime());
			application.store();
			if (application.getRejectionDate() != null)
				archive.setTerminatedDate(application.getRejectionDate());
			else
				archive.setTerminatedDate(null);
			archive.store();

			Collection contracts = getChildCareContractArchiveHome().findFutureContractsByApplication(applicationID, date);
			Iterator iter = contracts.iterator();
			while (iter.hasNext()) {
				archive = (ChildCareContractArchive) iter.next();
				try {
					Contract contract = archive.getContract();
					contract.setStatus("T");
					contract.store();
				}
				catch (Exception e) {
				}
				archive.remove();
			}
			t.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				t.rollback();
			}
			catch (SystemException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void addMissingGrantedChecks() {
		try {
			Collection childcareapps = this.getChildCareApplicationHome().findAll();
			Iterator it = childcareapps.iterator();
			int size = childcareapps.size();
			int a = 0;
			while (it.hasNext()) {
				System.out.println("Handling entry " + ++a + " of " + size);
				ChildCareApplication app = (ChildCareApplication)it.next();
				User child = app.getChild();
				if (!getCheckBusiness().hasGrantedCheck(child))
					getCheckBusiness().createGrantedCheck(child);
			}	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void convertOldQueue() {
		try {
			Collection childIDs = getChildCareQueueHome().getDistinctNotExportedChildIds();
			Map noParents = new HashMap();
			int size = childIDs.size();
			int a = 1;
			Iterator iter = childIDs.iterator();
			while (iter.hasNext()) {
				Integer element = (Integer) iter.next();
				System.out.println("Working on child " + a++ + " of " + size);
				convertQueueToApplications(noParents, element.intValue());
			}
			
			Iterator iterator = noParents.values().iterator();
			System.out.println("");
			System.out.println("Found no parents for the following child IDs (total of " + noParents.size() + " children):");
			while (iterator.hasNext()) {
				User child = (User) iterator.next();
				System.out.println(child.getPrimaryKey() + " - " + child.getPersonalID());
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	private void convertQueueToApplications(Map noParents, int childID) {
		try {
			User performer = null;
			User child = getUserBusiness().getUser(childID);
			Collection parents = getUserBusiness().getParentsForChild(child);
			if (parents != null) {
				Iterator iter = parents.iterator();
				while (iter.hasNext()) {
					User parent = (User) iter.next();
					if (getUserBusiness().hasCitizenAccount(parent)) {
						performer = parent;
						break;	
					}
					if (!iter.hasNext()) {
						performer = parent;
					}
				}
			}
			else {
				noParents.put(child.getPrimaryKey().toString(), child);
			}
			
			if (performer != null) {
				int length = 0;
				int[] provider = null;
				String[] dates = null;
				Date[] queueDates = null;
				boolean[] hasPriority = null;

				Collection choices = getQueueChoices(childID);
				if (choices != null) {
					Iterator iter = choices.iterator();
					int a = 0;
					while (iter.hasNext()) {
						ChildCareQueue queue = (ChildCareQueue) iter.next();
						if (a == 0) {
							switch (queue.getQueueType()) {
								case DBV_WITH_PLACE :
									length = 4;
									break;
								case DBV_WITHOUT_PLACE :
									length = 5;
									break;
								case FS_WITH_PLACE :
									length = 4;
									break;
								case FS_WITHOUT_PLACE :
									length = 5;
									break;
							}
							if (choices.size() < length)
								length = choices.size();

							provider = new int[length];
							dates = new String[length];
							queueDates = new Date[length];
							hasPriority = new boolean[length];
						}
						if (a < length) {
							provider[a] = queue.getProviderId();
							dates[a] = new IWTimestamp(queue.getStartDate()).toString();
							queueDates[a] = queue.getQueueDate();
							if (queue.getPriority() != null)
								hasPriority[a] = true;
							else
								hasPriority[a] = false;
						}
						a++;
					}

					boolean success = insertApplications(performer, provider, dates, null, childID, queueDates, hasPriority);
					if (success)
						exportQueue(choices);
				}
			}
			else {
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	public boolean hasActivePlacement(int childID) throws RemoteException {
		return getActivePlacement(childID) != null;
	}
	
	public boolean hasFutureContracts(int applicationID) throws RemoteException {
		int numberOfContracts = getNumberOfFutureContracts(applicationID);
		if (numberOfContracts > 0)
			return true;
		return false;
	}
	
	public boolean hasActiveContract(int applicationID) throws RemoteException {
		try {
			IWTimestamp stampNow = new IWTimestamp();
			int numberOfContracts = getChildCareContractArchiveHome().getNumberOfActiveForApplication(applicationID, stampNow.getDate());
			if (numberOfContracts > 0)
				return true;
			return false;
		}
		catch (IDOException e) {
			return false;
		}
	}
	
	public int getNumberOfFutureContracts(int applicationID) throws RemoteException {
		try {
			IWTimestamp stampNow = new IWTimestamp();
			return getChildCareContractArchiveHome().getFutureContractsCountByApplication(applicationID, stampNow.getDate());
		}
		catch (IDOException e) {
			return 0;
		}
	}
	
	public boolean hasUnansweredOffers(int childID) throws RemoteException {
		try {
			int numberOfOffers = getChildCareApplicationHome().getNumberOfApplicationsForChild(childID, getCaseStatusGranted().getStatus());
			if (numberOfOffers > 0)
				return true;
			return false;
		}
		catch (IDOException e) {
			return false;
		}
	}
	
	public ChildCareApplication getActivePlacement(int childID) throws RemoteException {
		try{
		
			Collection applications = getChildCareApplicationHome().findApplicationByChild(childID);
			Iterator i = applications.iterator();
			while(i.hasNext()){
				ChildCareApplication app = (ChildCareApplication) i.next();
				if (app.isActive()){
					return app;
				}
			}
			return null;
		}catch(FinderException ex) {
			return null;
		}
	}
	
	public boolean hasActivePlacementNotWithProvider(int childID, int providerID) throws RemoteException {
		try {
			int numberOfPlacings = getChildCareContractArchiveHome().getNumberOfActiveNotWithProvider(childID, providerID);
			if (numberOfPlacings > 0)
				return true;
			return false;
		}
		catch (IDOException e) {
			return false;
		}
	}

	public boolean hasTerminationInFutureNotWithProvider(int childID, int providerID) throws RemoteException {
		try {
			IWTimestamp stamp = new IWTimestamp();
			int numberOfPlacings = getChildCareContractArchiveHome().getNumberOfTerminatedLaterNotWithProvider(childID, providerID, stamp.getDate());
			if (numberOfPlacings > 0)
				return true;
			return false;
		}
		catch (IDOException e) {
			return false;
		}
	}

	public ChildCareContractArchive getLatestTerminatedContract(int childID) throws RemoteException {
		try {
			IWTimestamp stamp = new IWTimestamp();
			return getChildCareContractArchiveHome().findLatestTerminatedContractByChild(childID, stamp.getDate());
		}
		catch (FinderException e) {
			return null;
		}
	}

	public ChildCareContractArchive getLatestContract(int childID) throws RemoteException {
		try {
			return getChildCareContractArchiveHome().findLatestContractByChild(childID);
		}
		catch (FinderException e) {
			return null;
		}
	}
	
	public void importChildToProvider(int childID, int providerID, int groupID, int careTime, IWTimestamp fromDate, IWTimestamp toDate, Locale locale, User parent, User admin) {
		UserTransaction t = getSessionContext().getUserTransaction();

		try {
			t.begin();
			ChildCareApplication application = getChildCareApplicationHome().create();
			application.setChildId(childID);
			application.setProviderId(providerID);
			application.setFromDate(fromDate.getDate());
			application.setCareTime(careTime);
			application.setOwner(parent);
			if (toDate != null) {
				application.setRejectionDate(toDate.getDate());
			}
	
			ITextXMLHandler pdfHandler = new ITextXMLHandler(ITextXMLHandler.TXT+ITextXMLHandler.PDF);
			List buffers = pdfHandler.writeToBuffers(getTagMap(application,locale,fromDate,false),getXMLContractURL(getIWApplicationContext().getApplication().getBundle(se.idega.idegaweb.commune.presentation.CommuneBlock.IW_BUNDLE_IDENTIFIER), locale));
			if(buffers !=null && buffers.size() == 2){
				String contractText = pdfHandler.bufferToString((MemoryFileBuffer)buffers.get(1));
				ICFile contractFile = pdfHandler.writeToDatabase((MemoryFileBuffer)buffers.get(0),"contract.pdf",pdfHandler.getPDFMimeType());
				ContractService service = (ContractService) getServiceInstance(ContractService.class);
				Contract contract = service.getContractHome().create(application.getChildId(),2,fromDate,toDate,"C",contractText);
				int contractID = ((Integer)contract.getPrimaryKey()).intValue();
				contractFile.addTo(Contract.class,contractID);
			
				application.setContractId(contractID);
				application.setContractFileId(((Integer)contractFile.getPrimaryKey()).intValue());
				if (toDate != null) {
					application.setApplicationStatus(getStatusCancelled());
					changeCaseStatus(application, getCaseStatusCancelled().getStatus(), admin);
				}
				else {
					application.setApplicationStatus(getStatusReady());
					changeCaseStatus(application, getCaseStatusReady().getStatus(), admin);
				}
				
				addContractToArchive(-1, application, contractID, fromDate.getDate());
				getSchoolBusiness().storeSchoolClassMember(childID, groupID, fromDate.getTimestamp(), toDate.getTimestamp(), ((Integer)admin.getPrimaryKey()).intValue(), null);
			}
			t.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				t.rollback();
			}
			catch (SystemException ex) {
				ex.printStackTrace();
			}
		}
	}
}