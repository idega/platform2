/*
 * $Id:$ Copyright (C) 2002 Idega hf. All Rights Reserved. This software is the
 * proprietary information of Idega hf. Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;


import is.idega.block.family.business.NoCustodianFound;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import se.idega.block.pki.business.NBSLoginBusinessBean;
import se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.block.importer.business.AlreadyCreatedException;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.care.business.CareBusiness;
import se.idega.idegaweb.commune.care.check.data.Check;
import se.idega.idegaweb.commune.care.check.data.GrantedCheck;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import se.idega.idegaweb.commune.care.data.ChildCareApplicationHome;
import se.idega.idegaweb.commune.care.data.ChildCareContract;
import se.idega.idegaweb.commune.care.data.ChildCareContractHome;
import se.idega.idegaweb.commune.care.data.EmploymentType;
import se.idega.idegaweb.commune.care.data.EmploymentTypeHome;
import se.idega.idegaweb.commune.childcare.check.business.CheckBusiness;
import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosis;
import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosisHome;
import se.idega.idegaweb.commune.childcare.data.ChildCareQueue;
import se.idega.idegaweb.commune.childcare.data.ChildCareQueueHome;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.data.Message;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.business.SchoolConstants;
import com.idega.block.contract.business.ContractService;
import com.idega.block.contract.data.Contract;
import com.idega.block.contract.data.ContractTag;
import com.idega.block.contract.data.ContractTagHome;
import com.idega.block.pdf.ITextXMLHandler;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.process.data.Case;
import com.idega.block.school.business.SchoolAreaComparator;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolComparator;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.block.school.data.SchoolUser;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Phone;
import com.idega.core.file.data.ICFile;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.IDORuntimeException;
import com.idega.data.IDOStoreException;
import com.idega.exception.IWBundleDoesNotExist;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWPropertyList;
import com.idega.idegaweb.IWUserContext;
import com.idega.io.MemoryFileBuffer;
import com.idega.user.data.User;
import com.idega.util.FileUtil;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.database.ConnectionBroker;
import com.lowagie.text.ElementTags;
import com.lowagie.text.xml.XmlPeer;

/**
 * This class does something very clever.....
 * 
 * @author palli
 * @version 1.0
 */
public class ChildCareBusinessBean extends CaseBusinessBean implements ChildCareBusiness,CaseBusiness {

	public static final String PROPERTIES_CHILD_CARE = "child_care";
	public static final String PROPERTY_MAX_MONTHS_IN_QUEUE = "max_months_in_queue";
	public static final String PROPERTY_DAYS_TO_REPLY = "days_to_reply";

	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	private static String PROP_OUTSIDE_SCHOOL_AREA = "not_in_commune_school_area";

	final int DBV_WITH_PLACE = 0;
	final int DBV_WITHOUT_PLACE = 1;
	final int FS_WITH_PLACE = 2;
	final int FS_WITHOUT_PLACE = 3;

	private final static String PROPERTY_CONTRACT_CATEGORY = "childcare_contract_category";

	private final static String STATUS_NOT_PROCESSED = String.valueOf(ChildCareConstants.STATUS_SENT_IN);
	private final static String[] STATUS_IN_QUEUE = { String.valueOf(ChildCareConstants.STATUS_SENT_IN), String.valueOf(ChildCareConstants.STATUS_PRIORITY), String.valueOf(ChildCareConstants.STATUS_ACCEPTED), String.valueOf(ChildCareConstants.STATUS_PARENTS_ACCEPT), String.valueOf(ChildCareConstants.STATUS_CONTRACT)};
    private static final String PLACEMENT_HELPER = "PlacementHelper";
    
	private SchoolChoiceBusiness schoolChoiceBusiness = null;

	public String getBundleIdentifier() {
		return se.idega.idegaweb.commune.presentation.CommuneBlock.IW_BUNDLE_IDENTIFIER;
	}

	private ChildCareApplicationHome getChildCareApplicationHome() {
		try {
			return (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public ChildCarePrognosisHome getChildCarePrognosisHome() {
		try {
			return (ChildCarePrognosisHome) IDOLookup.getHome(ChildCarePrognosis.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public ChildCareContractHome getChildCareContractArchiveHome() {
		try {
			return (ChildCareContractHome) IDOLookup.getHome(ChildCareContract.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public ChildCareQueueHome getChildCareQueueHome() {
		try {
			return (ChildCareQueueHome) IDOLookup.getHome(ChildCareQueue.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public ChildCarePrognosis getPrognosis(int providerID) {
		try {
			return getChildCarePrognosisHome().findPrognosis(providerID);
		}
		catch (FinderException e) {
			return null;
		}
	}

	public ChildCareApplication getApplication(int childID, int choiceNumber) {
		try {
			return getChildCareApplicationHome().findApplicationByChildAndChoiceNumber(childID, choiceNumber);
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public ChildCareApplication getNonActiveApplication(int childID, int choiceNumber) {
		try {
			String[] caseStatus = { getCaseStatusOpen().getStatus(), getCaseStatusGranted().getStatus()};
			return getChildCareApplicationHome().findApplicationByChildAndChoiceNumberInStatus(childID, choiceNumber, caseStatus);
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public ChildCareApplication getNewestApplication(int providerID, Date date) {
		try {
			return getChildCareApplicationHome().findNewestApplication(providerID, date);
		}
		catch (FinderException fe) {
			return null;
		}
	}

	private ChildCareApplication getOldestApplication(int providerID, Date date) {
		try {
			return getChildCareApplicationHome().findOldestApplication(providerID, date);
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public boolean hasApplications(int childID) {
		try {
			int applications = getChildCareApplicationHome().getNumberOfApplicationsForChild(childID);
			if (applications > 0) return true;
			return false;
		}
		catch (IDOException e) {
			return false;
		}
	}

	public void updatePrognosis(int providerID, int threeMonthsPrognosis, int oneYearPrognosis, int threeMonthsPriority, int oneYearPriority, int providerCapacity) {
		try {
			ChildCarePrognosis prognosis = getPrognosis(providerID);
			if (prognosis == null) prognosis = getChildCarePrognosisHome().create();

			prognosis.setProviderID(providerID);
			prognosis.setThreeMonthsPrognosis(threeMonthsPrognosis);
			prognosis.setOneYearPrognosis(oneYearPrognosis);
			prognosis.setThreeMonthsPriority(threeMonthsPriority);
			prognosis.setOneYearPriority(oneYearPriority);
			prognosis.setProviderCapacity(providerCapacity);
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
			if (choices > 0) return true;
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
			String[] caseStatus = { getCaseStatusOpen().getStatus(), getCaseStatusGranted().getStatus()};

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
					IWTimestamp queueDate = null;
					if (queueDates != null && queueDates[i] != null) {
						queueDate = new IWTimestamp(queueDates[i]);
					}

					if (canChangeApplication(appl, providerID, fromDate, queueDate)) {
						if (appl.getProviderId() != providerID && appl.getProviderId() != -1) {
							if (removeFromQueue(appl, user, provider)) {
								appl = getChildCareApplicationHome().create();
							}
						}
						if (user != null) appl.setOwner(user);
						appl.setProviderId(providerID);
						appl.setFromDate(fromDate.getDate());
						if (message != null)
							appl.setMessage(message);
						else
							appl.setMessage("");
						appl.setPresentation("");
						appl.setChildId(childId);
						if (queueDate != null) {
							appl.setQueueDate(queueDate.getDate());
						}
						else
							appl.setQueueDate(now.getDate());
						appl.setMethod(1);
						appl.setChoiceNumber(i + 1);
						stamp.addSeconds((1 - ((i + 1) * 1)));
						appl.setCreated(stamp.getTimestamp());
						appl.setQueueOrder(((Integer) appl.getPrimaryKey()).intValue());
						appl.setApplicationStatus(getStatusSentIn());

						if (hasPriority != null)
							appl.setHasQueuePriority(hasPriority[i]);
						else {
							if (hasQueuePriority(child, providerID)) appl.setHasQueuePriority(true);
						}

						if (checkId != -1) appl.setCheckId(checkId);
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
			member = getLatestPlacement(((Integer) child.getPrimaryKey()).intValue(), providerID);
			if (member != null && member.getNeedsSpecialAttention()) return true;
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
						if (((Integer) child.getPrimaryKey()).intValue() != ((Integer) sibling.getPrimaryKey()).intValue()) {
							try {
								member = getLatestPlacement(((Integer) sibling.getPrimaryKey()).intValue(), providerID);
							}
							catch (FinderException e) {
								member = null;
							}

							if (member != null) {
								if (member.getNeedsSpecialAttention()) return true;

								if (member.getRemovedDate() != null) {
									IWTimestamp removed = new IWTimestamp(member.getRemovedDate());
									if (removed.isLaterThan(stamp)) return true;
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

	private boolean canChangeApplication(ChildCareApplication application, int newProviderID, IWTimestamp newFromDate, IWTimestamp newQueueDate) {
		int oldProviderID = application.getProviderId();
		IWTimestamp oldFromDate = new IWTimestamp();
		IWTimestamp oldQueueDate = new IWTimestamp();
		if (application.getFromDate() != null) oldFromDate = new IWTimestamp(application.getFromDate());
		if (application.getQueueDate() != null) oldQueueDate = new IWTimestamp(application.getQueueDate());

		if (oldProviderID != newProviderID)
			return true;
		else {
			if (!oldFromDate.equals(newFromDate)) return true;
			if (!oldQueueDate.equals(newQueueDate)) return true;
			return false;
		}
	}

	public void changePlacingDate(int applicationID, Date placingDate, String preSchool) {
		try {
			ChildCareApplication application = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
			application.setHasDateSet(true);
			application.setFromDate(placingDate);
			application.setPreSchool(preSchool);
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
				queueOrder = ((ChildCareApplication) applications.get(0)).getQueueOrder();
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
				else if (newestApplication == null && oldestApplication != null) {
					queueOrder = oldestApplication.getQueueOrder() - 100;
					application.setQueueOrder(queueOrder);
					application.store();
				}
				else if (newestApplication != null && oldestApplication == null) {
					queueOrder = newestApplication.getQueueOrder() + 100;
					application.setQueueOrder(queueOrder);
					application.store();
				}
				else {
					application.setQueueOrder(((Integer) application.getPrimaryKey()).intValue());
					application.store();
				}
			}
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void sendMessageToProvider(ChildCareApplication application, String subject, String message, User sender) {
		try {
			Collection users = getSchoolBusiness().getSchoolUsers(application.getProvider());
			User child = application.getChild();
			//Object[] arguments = { child.getNameLastFirst(true), application.getProvider().getSchoolName(), new IWTimestamp(application.getFromDate()).toSQLDateString(), child.getPersonalID()}; //Malin 040824
			Object[] arguments = { child.getName(), application.getProvider().getSchoolName(), new IWTimestamp(application.getFromDate()).toSQLDateString(), child.getPersonalID()};

			if (users != null) {
				MessageBusiness messageBiz = getMessageBusiness();
				Iterator it = users.iterator();
				while (it.hasNext()) {
					SchoolUser providerUser = (SchoolUser) it.next();
					User user = providerUser.getUser();
					System.out.println("School user: " + user.getName());
					messageBiz.createUserMessage(application, user, sender, subject, MessageFormat.format(message, arguments), false);
				}
			}
			else
				System.out.println("Got no users for provider " + application.getProviderId());
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
	}

	public void sendMessageToProvider(ChildCareApplication application, String subject, String message) {
		sendMessageToProvider(application, subject, message, null);
	}



	public Collection getQueueChoices(int childID) {
		try {
			return getChildCareQueueHome().findQueueByChild(childID);
		}
		catch (FinderException e) {
			return null;
		}
	}

	public int getPositionInQueue(ChildCareQueue queue) {
		try {
			int order = getChildCareQueueHome().getNumberInQueue(queue.getProviderId(), queue.getQueueDate());
			List choices = new ArrayList(getChildCareQueueHome().findQueueByProviderAndDate(queue.getProviderId(), queue.getQueueDate()));
			if (choices != null && !choices.isEmpty()) {
				Collections.sort(choices, new ChildCareQueueComparator());
				Iterator iter = choices.iterator();
				while (iter.hasNext()) {
					order++;
					ChildCareQueue element = (ChildCareQueue) iter.next();
					if (((Integer) element.getPrimaryKey()).intValue() == ((Integer) queue.getPrimaryKey()).intValue()) return order;
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

	public Collection getApplicationsByProviderAndApplicationStatus(int providerID, String applicationStatus) {
		try {
			String[] caseStatus = { applicationStatus};
			return getChildCareApplicationHome().findApplicationsByProviderAndApplicationStatus(providerID, caseStatus);
		}
		catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Collection getInactiveApplicationsByProvider(int providerID) {
		try {
			String[] caseStatus = { String.valueOf(getStatusCancelled()), String.valueOf(getStatusDenied()), String.valueOf(getStatusNotAnswered()), String.valueOf(getStatusRejected()), String.valueOf(getStatusTimedOut())};
			return getChildCareApplicationHome().findApplicationsByProviderAndApplicationStatus(providerID, caseStatus);
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

	public int getNumberOfUnhandledApplicationsByProvider(int providerID) {
		try {
			String[] caseStatus = { getCaseStatusOpen().getStatus(), getCaseStatusPending().getStatus()};
			return getChildCareApplicationHome().getNumberOfApplicationsByStatus(providerID, caseStatus);
		}
		catch (IDOException ie) {
			return 0;
		}
	}

	public int getNumberOfApplicationsByProvider(int providerID) {
		try {
			String[] caseStatus = { getCaseStatusDeleted().getStatus(), getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus()};

			return getChildCareApplicationHome().getNumberOfApplications(providerID, caseStatus);
		}
		catch (IDOException ie) {
			return 0;
		}
	}

	public int getNumberOfApplicationsByProvider(int providerID, int sortBy, Date fromDate, Date toDate) {
		try {
			String[] caseStatus = { getCaseStatusDeleted().getStatus(), getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus()};

			return getChildCareApplicationHome().getNumberOfApplications(providerID, caseStatus, sortBy, fromDate, toDate);
		}
		catch (IDOException ie) {
			return 0;
		}
	}

	public int getNumberOfFirstHandChoicesByProvider(int providerID) {
		try {
			return getChildCareApplicationHome().getNumberOfApplicationsByProviderAndChoiceNumber(providerID, 1);
		}
		catch (IDOException ie) {
			return 0;
		}
	}

	public int getNumberOfFirstHandChoicesByProvider(int providerID, Date from, Date to) {
		try {
			return getChildCareApplicationHome().getQueueByProviderAndChoiceNumber(providerID, 1, STATUS_NOT_PROCESSED, from, to);
		}
		catch (IDOException ie) {
			return 0;
		}
	}

	public int getNumberOfFirstHandNettoChoicesByProvider(int providerID, Date from, Date to) {
		try {
			return getChildCareApplicationHome().getNettoQueueByProviderAndChoiceNumber(providerID, 1, STATUS_NOT_PROCESSED, from, to);
		}
		catch (IDOException ie) {
			return 0;
		}
	}

	public int getNumberOfFirstHandBruttoChoicesByProvider(int providerID, Date from, Date to) {
		try {
			return getChildCareApplicationHome().getBruttoQueueByProviderAndChoiceNumber(providerID, 1, STATUS_NOT_PROCESSED, from, to);
		}
		catch (IDOException ie) {
			return 0;
		}
	}

	private Collection getApplicationsInQueueBeforeDate(int providerID, Date beforeDate) throws FinderException {
		String[] caseStatus = { getCaseStatusOpen().getStatus()};
		return getChildCareApplicationHome().findApplicationsByProviderAndBeforeDate(providerID, beforeDate, caseStatus);
	}

	public Collection getPendingApplications(int childID) {
		return getPendingApplications(childID, null);
	}

	public Collection getPendingApplications(int childID, String caseCode) {
		try {
			String[] caseStatus = { getCaseStatusPending().getStatus()};
			return getChildCareApplicationHome().findApplicationByChildAndInStatus(childID, caseStatus, caseCode);
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public boolean hasPendingApplications(int childID, String caseCode) {
		try {
			String[] caseStatus = { getCaseStatusPending().getStatus()};
			return getChildCareApplicationHome().getNumberOfApplicationsForChildInStatus(childID, caseStatus, caseCode) > 0;
		}
		catch (IDOException ie) {
			return false;
		}
	}
	
	public boolean cleanQueue(int providerID, User performer, IWUserContext iwuc) {
	    final int providID = providerID;
	    final User perf = performer;
	    final IWUserContext iw = iwuc;
	    if (iwuc.getSessionAttribute(ChildCareConstants.CLEAN_QUEUE_RUNNING) == null) {
		    new Thread () {
				public void run () {
				    try {
                        cleanQueueInThread( providID,  perf,  iw);
                    } catch (FinderException e) {
                        e.printStackTrace();
                    }
				}
			}.start ();
			return true;
	    }
	    else
	        return false;
	    
	}

	public boolean cleanQueueInThread(int providerID, User performer, IWUserContext iwuc) throws FinderException {
		iwuc.setSessionAttribute(ChildCareConstants.CLEAN_QUEUE_RUNNING, "TRUE");
		IWPropertyList properties = getIWApplicationContext().getSystemProperties().getProperties(PROPERTIES_CHILD_CARE);
		int monthsInQueue = Integer.parseInt(properties.getProperty(PROPERTY_MAX_MONTHS_IN_QUEUE, "6"));
		int daysToReply = Integer.parseInt(properties.getProperty(PROPERTY_DAYS_TO_REPLY, "30"));

		IWTimestamp beforeDate = new IWTimestamp();
		beforeDate.addMonths(-monthsInQueue);

		IWTimestamp stamp = new IWTimestamp();
		Collection applications = getApplicationsInQueueBeforeDate(providerID, beforeDate.getDate());

		UserTransaction transaction = getSessionContext().getUserTransaction();
		try {
			transaction.begin();

			IWTimestamp lastReplyDate = new IWTimestamp();
			lastReplyDate.addDays(daysToReply);

			String subject = getLocalizedString("child_care.clean_queue_subject", "Old application in queue");
			String body = getLocalizedString("child_care.clean_queue_body", "Your application for {0}, {2},Êto {1}Êhas been in the queue for 6 months.  You now have until {3}Êto update your choices in the childcare overview.  After that, the choices will be removed from our queue. \n\nBest regards,\n{1}");
			String letterBody = getLocalizedString("child_care.clean_queue_body_letter", "Your application for {0}, {2},Êto {1}Êhas been in the queue for 6 months.  You now have until {3}Êto update your choices in the childcare overview.  After that, the choices will be removed from our queue. \n\nBest regards,\n{1}");

			Iterator iter = applications.iterator();
			while (iter.hasNext()) {
				ChildCareApplication application = (ChildCareApplication) iter.next();
				if (!hasOutstandingOffers(application.getChildId(), application.getCode()) && hasActiveApplications(application.getChildId(), application.getCaseCode().getCode(), stamp.getDate())) {
					application.setLastReplyDate(lastReplyDate.getDate());
					changeCaseStatus(application, getCaseStatusPending().getStatus(), performer);

					sendMessageToParents(application, subject, body, letterBody, true); 
				}
			}

			transaction.commit();
			iwuc.removeSessionAttribute(ChildCareConstants.CLEAN_QUEUE_RUNNING);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				transaction.rollback();
			}
			catch (SystemException ex) {
				ex.printStackTrace();
			}
		}

		iwuc.removeSessionAttribute(ChildCareConstants.CLEAN_QUEUE_RUNNING);
		return false;
	}

	public boolean hasActiveApplications(int childID, String caseCode, Date activeDate) {
		String[] caseStatus = { getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus()};
		try {
			return getChildCareApplicationHome().getNumberOfApplicationsByStatusAndActiveDate(childID, caseStatus, caseCode, activeDate) > 0;
		}
		catch (IDOException ie) {
			return false;
		}
	}

	public boolean removePendingFromQueue(User performer) {
		IWTimestamp lastReplyDate = new IWTimestamp();
		String[] caseStatus = { getCaseStatusPending().getStatus()};

		Collection applications;
		try {
			applications = getChildCareApplicationHome().findApplicationsBeforeLastReplyDate(lastReplyDate.getDate(), caseStatus);
		}
		catch (FinderException fe) {
			return false;
		}

		UserTransaction transaction = getSessionContext().getUserTransaction();
		try {
			transaction.begin();

			String subject = getLocalizedString("child_care.application_timed_out_subject", "Old application removed from queue");
			String body = getLocalizedString("child_care.application_timed_out_body", "Your application for {0}, {2},Êto {1}Êhas been removed from out queue after you failed to renew it. \n\nBest regards,\n{1}");

			String providerBody = getLocalizedString("child_care.application_timed_out_provider_body", "An application for {0}, {3},Êhas been removed from your queue because the parents didn't renew the application.");

			Iterator iter = applications.iterator();
			while (iter.hasNext()) {
				ChildCareApplication application = (ChildCareApplication) iter.next();
				application.setRejectionDate(lastReplyDate.getDate());
				application.setApplicationStatus(getStatusTimedOut());
				changeCaseStatus(application, getCaseStatusInactive().getStatus(), performer);

				sendMessageToParents(application, subject, body, true);
				sendMessageToProvider(application, subject, providerBody);
			}

			transaction.commit();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				transaction.rollback();
			}
			catch (SystemException ex) {
				ex.printStackTrace();
			}
		}

		return false;
	}

	public void renewApplication(int applicationID, User performer) {
		try {
			ChildCareApplication application = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));

			application.setApplicationStatus(getStatusSentIn());
			changeCaseStatus(application, getCaseStatusOpen().getStatus(), performer);

			String subject = getLocalizedString("child_care.application_renewed_subject", "An application was renewed.");
			String body = getLocalizedString("child_care.application_renewed_body", "Custodian for {0}, {3} has renewed his choice to your establishment.");
			sendMessageToProvider(application, subject, body);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public int getNumberInQueue(ChildCareApplication application) {
		try {
			String[] caseStatus = { getCaseStatusDeleted().getStatus(), getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus()};
			int numberInQueue = 0;
			numberInQueue += getChildCareApplicationHome().getPositionInQueue(application.getQueueDate(), application.getProviderId(), caseStatus);
			numberInQueue += getChildCareApplicationHome().getPositionInQueueByDate(application.getQueueOrder(), application.getQueueDate(), application.getProviderId(), caseStatus);
			return numberInQueue;
		}
		catch (IDOException e) {
			return -1;
		}
	}

	public int getNumberInQueueByStatus(ChildCareApplication application) {
		try {
			int numberInQueue = 0;
			numberInQueue += getChildCareApplicationHome().getPositionInQueue(application.getQueueDate(), application.getProviderId(), String.valueOf(application.getApplicationStatus()));
			numberInQueue += getChildCareApplicationHome().getPositionInQueueByDate(application.getQueueOrder(), application.getQueueDate(), application.getProviderId(), String.valueOf(application.getApplicationStatus()));
			return numberInQueue;
		}
		catch (IDOException e) {
			return -1;
		}
	}

	public Collection getUnhandledApplicationsByProvider(int providerId, int numberOfEntries, int startingEntry) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			String[] caseStatus = { getCaseStatusDeleted().getStatus(), getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus()};

			return home.findAllCasesByProviderAndNotInStatus(providerId, caseStatus, numberOfEntries, startingEntry);
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

	public Collection getUnhandledApplicationsByProvider(int providerId, int numberOfEntries, int startingEntry, int sortBy, Date fromDate, Date toDate) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			String[] caseStatus = { getCaseStatusDeleted().getStatus(), getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus()};

			return home.findAllCasesByProviderAndNotInStatus(providerId, sortBy, fromDate, toDate, caseStatus, numberOfEntries, startingEntry);
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

	public Collection getUnhandledApplicationsByChild(int childID, String caseCode) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			String[] caseStatus = { getCaseStatusDeleted().getStatus(), getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus(), getCaseStatusDenied().getStatus(), getCaseStatusContract().getStatus(), getCaseStatusPreliminary().getStatus()};

			return home.findApplicationByChildAndNotInStatus(childID, caseStatus, caseCode);
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

	public Collection getUnhandledApplicationsByChild(int childID) {
		return getUnhandledApplicationsByChild(childID, null);
	}

	public ChildCareApplication getUnhandledApplicationsByChildAndProvider(int childID, int providerID) throws FinderException {
		String[] statuses = { String.valueOf(getStatusSentIn()), String.valueOf(getStatusPriority()), String.valueOf(getStatusAccepted()), String.valueOf(getStatusParentsAccept())};
		return getChildCareApplicationHome().findApplicationByChildAndProviderAndStatus(childID, providerID, statuses);
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

			return home.findAllCasesByProviderAndStatus(providerId, this.getCaseStatusPreliminary());
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



	public boolean isAfterSchoolApplication(int applicationID) throws RemoteException {
		try {
			return isAfterSchoolApplication(getChildCareApplication(applicationID));
		}
		catch (FinderException fe) {
			return false;
		}
	}


	public boolean placeApplication(int applicationID, String subject, String body, int childCareTime, int groupID, int schoolTypeID, int employmentTypeID, User user, Locale locale) {
		UserTransaction t = super.getSessionContext().getUserTransaction();
		try {
			t.begin();
			ChildCareApplication application = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
			application.setCareTime(childCareTime);
			if (groupID != -1) {
				IWTimestamp fromDate = new IWTimestamp(application.getFromDate());
				getSchoolBusiness().storeSchoolClassMemberCC(application.getChildId(), groupID, schoolTypeID, fromDate.getTimestamp(), ((Integer) user.getPrimaryKey()).intValue());
				sendMessageToParents(application, subject, body);
			}
			alterValidFromDate(application, application.getFromDate(), employmentTypeID, locale, user);
			application.setApplicationStatus(getStatusReady());
			application.store();
			t.commit();
		}
		catch (FinderException e) {
			e.printStackTrace();
			try {
				t.rollback();
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		catch (NoPlacementFoundException e) {
			e.printStackTrace();
			try {
				t.rollback();
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		catch (Exception e) {
			try {
				t.rollback();
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		return false;
	}

	public void alterValidFromDate(int applicationID, Date newDate, int employmentTypeID, Locale locale, User user) throws RemoteException, NoPlacementFoundException {
		try {
			ChildCareApplication application = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
			alterValidFromDate(application, newDate, employmentTypeID, locale, user);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void alterValidFromDate(ChildCareApplication application, Date newDate, int employmentTypeID, Locale locale, User user) throws RemoteException, NoPlacementFoundException {
		application.setApplicationStatus(getStatusReady());
		int oldFileID = application.getContractFileId();
		IWTimestamp fromDate = new IWTimestamp(newDate);

		ITextXMLHandler pdfHandler = new ITextXMLHandler(ITextXMLHandler.PDF);
		List buffers = pdfHandler.writeToBuffers(getTagMap(application, locale, fromDate, false), getXMLContractPdfURL(getIWApplicationContext().getIWMainApplication().getBundle(se.idega.idegaweb.commune.presentation.CommuneBlock.IW_BUNDLE_IDENTIFIER), locale));

		ICFile contractFile = pdfHandler.writeToDatabase((MemoryFileBuffer) buffers.get(0), "contract.pdf", pdfHandler.getPDFMimeType());
		int fileID = ((Integer) contractFile.getPrimaryKey()).intValue();

		application.setContractFileId(fileID);
		application.setFromDate(newDate);
		changeCaseStatus(application, getCaseStatusReady().getStatus(), user);
		addContractToArchive(oldFileID,-1,false, application, -1, fromDate.getDate(), employmentTypeID,-1,user,false,-1,-1,null);
	    application.store();
		try {
			SchoolClassMember member = getLatestPlacement(application.getChildId(), application.getProviderId());
			member.setRegisterDate(fromDate.getTimestamp());
			member.store();
		}
		catch (FinderException e) {
			//Placing not yet created...
		}
	}

	public boolean alterContract(int childcareContractID, int careTime, Date fromDate, Date endDate, Locale locale, User performer,int employmentType, int invoiceReceiver, int schoolType, int schoolClass) {
		try {
			return alterContract(getChildCareContractArchiveHome().findByPrimaryKey(new Integer(childcareContractID)), careTime, fromDate, endDate, locale, performer,employmentType,invoiceReceiver,schoolType,schoolClass);
		}
		catch (FinderException fe) {
			return false;
		}
	}

	/**
	 * Changes childcare contract, care time, start date, end date.
	 * When change made to those fields a new file is created and old file swapped out
	 * 
	 * 
	 */
	public boolean alterContract(ChildCareContract childcareContract, int careTime, Date fromDate, Date endDate, Locale locale, User performer,int employmentType, int invoiceReceiver, int schoolType, int schoolClass) {
		UserTransaction trans = getSessionContext().getUserTransaction();
		try {
			trans.begin();

			if(fromDate==null)
			    throw new NullPointerException("Argument from date is null");
			

			ChildCareApplication application = childcareContract.getApplication();
			
			// possible bug found here, allowing null values for start date, which should be possible (aron 06.09.2004)
			if(fromDate!=null){
			    childcareContract.setValidFromDate(fromDate);
			}
			childcareContract.setTerminatedDate(endDate);
			
			if(invoiceReceiver>0)
			    childcareContract.setInvoiceReceiverID(invoiceReceiver);
			
			if(employmentType>0)
			    childcareContract.setEmploymentType(employmentType);
			
			if(careTime>0 && childcareContract.getCareTime()!= careTime){
			    childcareContract.setCareTime(careTime);
			    
			}
			
			
			ICFile contractFile = recreateContractFile(childcareContract,locale);
			childcareContract.setContractFile(contractFile);
			childcareContract.store();
			
			if (application.getContractId() == childcareContract.getContractID()) {
				application.setContractFileId(((Integer) contractFile.getPrimaryKey()).intValue());
				//application.store();
			}
			verifyApplication(childcareContract,application, null, performer,schoolType,schoolClass);
			application.store();

			trans.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				trans.rollback();
			}
			catch (SystemException ex) {
				ex.printStackTrace();
			}
			return false;
		}
		return true;
	}
	
	
	/**
	 *  Update contract with new field values, and recreate file attached to it.
	 *  The old file is removed from the contract, and replaced with a new one
	 * @throws IDORemoveRelationshipException
	 * @throws IWBundleDoesNotExist
	 * @throws RemoteException
	 * @throws IDOAddRelationshipException
	 */
	public ICFile recreateContractFile(ChildCareContract archive,Locale locale) throws IDORemoveRelationshipException, RemoteException, IWBundleDoesNotExist, IDOAddRelationshipException{
	    Contract contract = archive.getContract();
		contract.removeFileFromContract(archive.getContractFile());
		ITextXMLHandler pdfHandler = new ITextXMLHandler(ITextXMLHandler.PDF);
		List buffers = pdfHandler.writeToBuffers(getTagMap(archive.getApplication(), locale, archive.getCareTime(), new IWTimestamp(archive.getValidFromDate()), false), getXMLContractPdfURL(getIWApplicationContext().getIWMainApplication().getBundle(se.idega.idegaweb.commune.presentation.CommuneBlock.IW_BUNDLE_IDENTIFIER), locale));

		ICFile contractFile = pdfHandler.writeToDatabase((MemoryFileBuffer) buffers.get(0), "contract.pdf", pdfHandler.getPDFMimeType());
		Date endDate = archive.getTerminatedDate();
		contract.setValidTo(endDate);
		if (endDate != null) {
			contract.setStatusTerminated();
		}
		else {
			contract.setStatusCreated();
		}
		contract.store();
		contract.addFileToContract(contractFile);
		return contractFile;
	}
	
	protected void verifyApplication(ChildCareContract lastContract,ChildCareApplication application, SchoolClassMember member, User performer)throws RemoteException {
	    verifyApplication(lastContract,application,member,performer,-1,-1);
	}

	protected void verifyApplication(ChildCareContract lastContract,ChildCareApplication application, SchoolClassMember member, User performer,int schoolTypeId,int schoolClassId)throws RemoteException {
		try {
		    //Collection archives = getChildCareContractArchiveHome().findLatestByApplication(((Integer) application.getPrimaryKey()).intValue(),2);
		    //Iterator iter = archives.iterator();
		    //ChildCareContract lastContract = iter.hasNext()?(ChildCareContract)iter.next():null;
		    if(lastContract==null)
		        lastContract = getChildCareContractArchiveHome().findLatestContractByApplication(((Integer) application.getPrimaryKey()).intValue());
			//ChildCareContract firstContract = iter.hasNext()?(ChildCareContract)iter.next():lastContract;
		    ChildCareContract firstContract = getChildCareContractArchiveHome().findFirstContractByApplication(((Integer) application.getPrimaryKey()).intValue());
			

			application.setFromDate(firstContract.getValidFromDate());
			application.setRejectionDate(lastContract.getTerminatedDate());
			application.setCareTime(lastContract.getCareTime());
			application.setContractId(lastContract.getContractID());
			application.setContractFileId(lastContract.getContractFileID());
			if (application.getRejectionDate() == null) {
				application.setApplicationStatus(getStatusReady());
				changeCaseStatus(application, getCaseStatusReady().getStatus(), performer);
			}
			else {
				application.setApplicationStatus(getStatusCancelled());
				changeCaseStatus(application, getCaseStatusCancelled().getStatus(), performer);
			}
			// update school class member with correct dates
			SchoolClassMember placement = lastContract.getSchoolClassMember();
			if (placement != null ){
			    Collection contractPlacements = getChildCareContractArchiveHome().findAllBySchoolClassMember(placement);
			    // only allow update when only one contract linked to the classmember
			    // or the one being changed is the first contract
			    if(contractPlacements.size()==1 ){//|| lastContract.getPrimaryKey().equals(firstContract.getPrimaryKey()) ) {
					placement.setRegisterDate((new IWTimestamp(lastContract.getValidFromDate())).getTimestamp());
					if (lastContract.getTerminatedDate() != null) {
						placement.setRemovedDate((new IWTimestamp(lastContract.getTerminatedDate())).getTimestamp());
					}
					else {
						placement.setRemovedDate(null);
					}
					if(schoolTypeId>0)
					    placement.setSchoolTypeId(schoolTypeId);
					if(schoolClassId>0)
					    placement.setSchoolClassId(schoolClassId);
					
					placement.store();
				}
			    // create new placement and attach to archive
			    else if( schoolTypeId>0 && schoolClassId>0){
			        SchoolClassMember newPlacement = createNewPlacement(new Integer(lastContract.getChildID()),new Integer(schoolTypeId),new Integer(schoolClassId),placement,new IWTimestamp(lastContract.getValidFromDate()), performer);
			        lastContract.setSchoolClassMember(newPlacement);
			        if (lastContract.getTerminatedDate() != null) {
						placement.setRemovedDate((new IWTimestamp(lastContract.getTerminatedDate())).getTimestamp());
						placement.store();
			        }
			        lastContract.store();
				
			    }
			}

		}
		catch (FinderException fe) {
			application.setContractId(null);
			application.setContractFileId(null);
			application.setApplicationStatus(getStatusDeleted());
			if (member != null) {
				member.setRemovedDate(member.getRegisterDate());
				member.store();
			}
			changeCaseStatus(application, getCaseStatusDeleted().getStatus(), performer);
		}
	}

	public void moveToGroup(int childID, int providerID, int schoolClassID) {
		try {
			SchoolClassMember classMember = getLatestPlacement(childID, providerID);
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

	public void moveToGroup(int placementID, int schoolClassID) throws RemoteException {
		try {
			SchoolClassMember classMember = getSchoolBusiness().getSchoolClassMemberHome().findByPrimaryKey(new Integer(placementID));
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

	public void removeFromProvider(int placementID, Timestamp date, boolean parentalLeave, String message) {
		try {
			SchoolClassMember classMember = getSchoolBusiness().getSchoolClassMemberHome().findByPrimaryKey(new Integer(placementID));
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
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private SchoolClassMember getLatestPlacement(int childID, int providerID) throws FinderException {
		try {
			Collection types = getSchoolBusiness().getSchoolTypesForCategory(getSchoolBusiness().getCategoryChildcare(), true);
			return getSchoolBusiness().getSchoolClassMemberHome().findLatestByUserAndSchool(childID, providerID, types);
		}
		catch (RemoteException e) {
			throw new IDORuntimeException(e.getMessage());
		}
	}

	private void deleteFromProvider(int childID, int providerID) {
		try {
			SchoolClassMember classMember = getLatestPlacement(childID, providerID);
			classMember.remove();
		}
		catch (RemoveException e) {
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

			if (hasPendingApplications(application.getChildId(), application.getCaseCode().getCode())) {
				Collection pending = getPendingApplications(application.getChildId(), application.getCaseCode().getCode());
				Iterator iter = pending.iterator();
				while (iter.hasNext()) {
					ChildCareApplication app = (ChildCareApplication) iter.next();
					changeCaseStatus(app, getCaseStatusOpen().getStatus(), user, null);
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

	public boolean retractOffer(int applicationID, String subject, String message, User user) {
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
			IWTimestamp removed = new IWTimestamp();
			application.setRejectionDate(removed.getDate());
			application.setApplicationStatus(getStatusNotAnswered());
			caseBiz.changeCaseStatus(application, getCaseStatusInactive().getStatus(), user);

			sendMessageToParents(application, subject, message);

			if (isAfterSchoolApplication(application) && application.getChildCount() > 0) {
				Iterator iter = application.getChildrenIterator();
				while (iter.hasNext()) {
					Case element = (Case) iter.next();
					if (element instanceof ChildCareApplication) {
						application = (ChildCareApplication) element;
						application.setApplicationStatus(getStatusSentIn());
						caseBiz.changeCaseStatus(application, getCaseStatusOpen().getStatus(), user);
					}
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

	public boolean reactivateApplication(int applicationID, User user) {
		try {
			ChildCareApplication appl = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));

			return reactivateApplication(appl, user);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean reactivateApplication(ChildCareApplication application, User user) {
		if (application.getApplicationStatus() == getStatusNotAnswered() || application.getApplicationStatus() == getStatusTimedOut()) {
			application.setApplicationStatus(getStatusSentIn());
			application.setRejectionDate(null);
			changeCaseStatus(application, getCaseStatusOpen().getStatus(), user);
			return true;
		}
		else if (application.getApplicationStatus() == getStatusCancelled()) {
			application.setApplicationStatus(getStatusReady());
			application.setRejectionDate(null);
			ChildCareContract childcareContract = getContractFile(application.getContractFileId());
			if (childcareContract != null) {
				Contract contract = childcareContract.getContract();
				contract.setValidTo(null);
				contract.setStatusCreated();
				contract.store();

				childcareContract.setTerminatedDate(null);
				childcareContract.store();

				SchoolClassMember member = childcareContract.getSchoolClassMember();
				if (member != null) {
					member.setRemovedDate(null);
					member.store();
				}

				changeCaseStatus(application, getCaseStatusReady().getStatus(), user);

				return true;
			}
		}
		return false;
	}

	public boolean changeApplicationStatus(int applicationID, char newStatus, User performer) throws IllegalArgumentException {
		try {
			return changeApplicationStatus(getChildCareApplication(applicationID), newStatus, performer);
		}
		catch (FinderException e) {
			return false;
		}
	}

	public boolean changeApplicationStatus(ChildCareApplication application, char newStatus, User performer) throws IllegalArgumentException {
		UserTransaction t = getSessionContext().getUserTransaction();
		try {
			if (newStatus == getStatusRejected()) {
				t.begin();
				if (application.getApplicationStatus() == getStatusContract()) {
					Contract contract = application.getContract();
					contract.removeFileFromContract(application.getContractFile());

					ChildCareContract childcareContract = getContractFile(application.getContractFileId());
					childcareContract.remove();

					application.setContractId(null);
					application.setContractFileId(null);
					application.store();
					contract.remove();
				}
				application.setApplicationStatus(getStatusRejected());
				application.setRejectionDate(new IWTimestamp().getDate());
				changeCaseStatus(application, getCaseStatusInactive().getStatus(), performer);
			}
			else if (newStatus == getStatusAccepted()) {
				t.begin();
				application.setApplicationStatus(getStatusAccepted());
				application.setRejectionDate(null);
				changeCaseStatus(application, getCaseStatusGranted().getStatus(), performer);
			}
			else if (newStatus == getStatusDeleted()) {
				t.begin();
				application.setApplicationStatus(getStatusDeleted());
				application.setRejectionDate(new IWTimestamp().getDate());
				changeCaseStatus(application, getCaseStatusDeleted().getStatus(), performer);
			}
			else if (newStatus == getStatusSentIn()) {
				t.begin();
				application.setApplicationStatus(getStatusSentIn());
				application.setRejectionDate(null);
				changeCaseStatus(application, getCaseStatusOpen().getStatus(), performer);
			}
			else {
				throw new IllegalArgumentException("Changing to application status '" + newStatus + "' is not supported.");
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

	public boolean cancelContract(ChildCareApplication application, boolean parentalLeave, IWTimestamp date, String message, String subject, String body, User user) {
		UserTransaction t = getSessionContext().getUserTransaction();
		try {
			t.begin();
			CaseBusiness caseBiz = (CaseBusiness) getServiceInstance(CaseBusiness.class);
			application.setApplicationStatus(getStatusCancelled());
			application.setRejectionDate(date.getDate());
			caseBiz.changeCaseStatus(application, this.getCaseStatusCancelled().getStatus(), user);
			int placementID = terminateContract(application.getContractFileId(), date.getDate(), true);

			removeFromProvider(placementID, date.getTimestamp(), parentalLeave, message);
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
			ChildCareApplication appl = home.findByPrimaryKey(new Integer(applicationId));

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
			ChildCareApplication application = home.findByPrimaryKey(new Integer(applicationId));

			UserTransaction t = getSessionContext().getUserTransaction();
			try {
				t.begin();
				CaseBusiness caseBiz = (CaseBusiness) getServiceInstance(CaseBusiness.class);
				IWTimestamp now = new IWTimestamp();
				application.setRejectionDate(now.getDate());
				application.setApplicationStatus(getStatusRejected());
				caseBiz.changeCaseStatus(application, getCaseStatusInactive().getStatus(), user);

				String subject = getLocalizedString("child_care.rejected_offer_subject", "A placing offer replied to.");
				String body = getLocalizedString("child_care.rejected_offer_body", "Custodian for {0}, {5} rejects an offer for placing at {1}.");
				sendMessageToProvider(application, subject, body);

				if (isAfterSchoolApplication(application) && application.getChildCount() > 0) {
					Iterator iter = application.getChildrenIterator();
					while (iter.hasNext()) {
						Case element = (Case) iter.next();
						if (element instanceof ChildCareApplication) {
							application = (ChildCareApplication) element;
							application.setApplicationStatus(getStatusSentIn());
							caseBiz.changeCaseStatus(application, getCaseStatusOpen().getStatus(), user);
						}
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
				CaseBusiness caseBiz = (CaseBusiness) getServiceInstance(CaseBusiness.class);
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
		catch (FinderException e) {
			e.printStackTrace();
		}

		return false;

	}

	public boolean removeFromQueue(int applicationId, User user) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			ChildCareApplication appl = home.findByPrimaryKey(new Integer(applicationId));

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

	public boolean removeFromQueue(ChildCareApplication application, User user) throws RemoteException {
		return removeFromQueue(application, user, null);
	}

	private boolean removeFromQueue(ChildCareApplication application, User user, int[] providerIDs) {
		if (providerIDs != null) {
			for (int i = 0; i < providerIDs.length; i++) {
				if (application.getProviderId() == providerIDs[i]) { return false; }
			}
		}

		IWTimestamp removed = new IWTimestamp();
		application.setApplicationStatus(getStatusRejected());
		application.setRejectionDate(removed.getDate());
		changeCaseStatus(application, getCaseStatusInactive().getStatus(), user);

		String subject = getLocalizedString("child_care.removed_from_queue_subject", "A child removed from the queue.");
		String body = getLocalizedString("child_care.removed_from_queue_body", "Custodian for {0}, {3} has removed you as a choice alternative.  {0} can therefore no longer be found in the queue but in the list of those removed from the queue.");
		sendMessageToProvider(application, subject, body);

		if (isAfterSchoolApplication(application) && application.getChildCount() > 0) {
			Iterator iter = application.getChildrenIterator();
			while (iter.hasNext()) {
				Case element = (Case) iter.next();
				if (element instanceof ChildCareApplication) {
					application = (ChildCareApplication) element;
					application.setApplicationStatus(getStatusSentIn());
					changeCaseStatus(application, getCaseStatusOpen().getStatus(), user);
				}
			}
		}

		return true;
	}

	/*
	 * public boolean signApplication(ChildCareApplication application) { return
	 * false; } public boolean signApplication(int applicationId) { try {
	 * ChildCareApplicationHome home = (ChildCareApplicationHome)
	 * IDOLookup.getHome(ChildCareApplication.class); ChildCareApplication appl =
	 * (ChildCareApplication)home.findByPrimaryKey(new Integer(applicationId));
	 * return signApplication(appl); } catch (RemoteException e) {
	 * e.printStackTrace(); } catch (FinderException e) { e.printStackTrace(); }
	 * return false;
	 */

	public ChildCareApplication getAcceptedApplicationsByChild(int childID) {
		try {
			String caseStatus[] = { getCaseStatusPreliminary().getStatus(), getCaseStatusContract().getStatus()};

			return getChildCareApplicationHome().findActiveApplicationByChildAndStatus(childID, caseStatus);
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

	public Collection getOpenAndGrantedApplicationsByProvider(int providerId) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			String caseStatus[] = { getCaseStatusOpen().getStatus(), getCaseStatusGranted().getStatus()};

			return home.findAllCasesByProviderStatus(providerId, caseStatus);
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

	public Collection getAcceptedApplicationsByProvider(int providerID) {
		try {
			String[] caseStatus = { getCaseStatusReady().getStatus(), getCaseStatusCancelled().getStatus()};
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

	public Collection getRejectedApplicationsByProvider(Integer providerID, String fromDateOfBirth, String toDateOfBirth, String fromDate, String toDate) throws FinderException {
		if (fromDateOfBirth.length() == 0) {
			fromDateOfBirth = null;
		}
		if (toDateOfBirth.length() == 0) {
			toDateOfBirth = null;
		}
		if (fromDate.length() == 0) {
			fromDate = null;
		}
		if (toDate.length() == 0) {
			toDate = null;
		}
		String applicationStatus[] = { String.valueOf(getStatusRejected()), String.valueOf(getStatusTimedOut()), String.valueOf(getStatusDenied())};
		return getChildCareApplicationHome().findApplicationsByProviderAndStatus(providerID, applicationStatus, fromDateOfBirth != null ? new IWTimestamp(fromDateOfBirth).getDate() : null, toDateOfBirth != null ? new IWTimestamp(toDateOfBirth).getDate() : null, fromDate != null ? new IWTimestamp(fromDate).getDate() : null, toDate != null ? new IWTimestamp(toDate).getDate() : null);
	}

	public void parentsAgree(int applicationID, User user, String subject, String message) {
		try {
			ChildCareApplication application = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
			parentsAgree(application, user, subject, message);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void parentsAgree(ChildCareApplication application, User user, String subject, String message) {
		application.setApplicationStatus(this.getStatusParentsAccept());
		changeCaseStatus(application, getCaseStatusPreliminary().getStatus(), user);
		sendMessageToProvider(application, subject, message);
	}

	public void saveComments(int applicationID, String comment) {
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
	
	public SchoolClassMember createNewPlacement(int applicationID, int schooltypeID, int schoolclassID,SchoolClassMember oldStudent,IWTimestamp validFrom ,User user) throws RemoteException, EJBException{
		return createNewPlacement(getApplication(applicationID),(schooltypeID),(schoolclassID),oldStudent,validFrom,user);
	}
	public SchoolClassMember createNewPlacement(ChildCareApplication application, int schooltypeID, int schoolclassID,SchoolClassMember oldStudent,IWTimestamp validFrom ,User user) throws RemoteException, EJBException{
		return createNewPlacement(new Integer(application.getChildId()),new Integer(schooltypeID),new Integer(schoolclassID),oldStudent,validFrom,user);
	}
	
	public SchoolClassMember createNewPlacement(Integer childID, Integer schooltypeID, Integer schoolclassID,SchoolClassMember oldStudent,IWTimestamp validFrom ,User user) throws RemoteException, EJBException{
		
		SchoolClassMember member = null;
		if (schoolclassID.intValue() != -1) {
			IWTimestamp endDate = new IWTimestamp(validFrom);
			endDate.addDays(-1);
			
			if(oldStudent!=null){
				oldStudent.setRemovedDate(endDate.getTimestamp());
				oldStudent.store();
			}
			
			member = getSchoolBusiness().storeSchoolClassMemberCC(childID.intValue(), schoolclassID.intValue(), schooltypeID.intValue(), validFrom.getTimestamp(), ((Integer) user.getPrimaryKey()).intValue());
			//archive.setSchoolClassMember(member);
			//archive.store();
		}
		return member;
	
	}

	public boolean assignContractToApplication(int applicationID, int oldArchiveID,int childCareTime, IWTimestamp validFrom, int employmentTypeID, User user, Locale locale, boolean changeStatus) {
		return assignContractToApplication( applicationID,-1,  childCareTime,  validFrom,  employmentTypeID,  user,  locale,  changeStatus,false,-1,-1);
	}
	public boolean assignContractToApplication(int applicationID,int archiveID, int childCareTime, IWTimestamp validFrom, int employmentTypeID, User user, Locale locale, boolean changeStatus,boolean createNewStudent,int schoolTypeId,int schoolClassId) {
		UserTransaction transaction = getSessionContext().getUserTransaction();
		try {
			transaction.begin();
			ChildCareApplication application = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
			application.setCareTime(childCareTime);

			if (validFrom == null) validFrom = new IWTimestamp(application.getFromDate());
			int oldArchiveID = archiveID;
			if(oldArchiveID<=0){
				ChildCareContract con = getValidContractByChild(application.getChildId());//getLatestContract(application.getChildId());
			    oldArchiveID = con!=null?((Integer)con.getPrimaryKey()).intValue():-1;
				
			}	
		

			ContractTagHome contractHome = (ContractTagHome) IDOLookup.getHome(ContractTag.class);
			Collection tags = contractHome.findAllByNameAndCategory("care-time", getContractCategory());

			if (tags.isEmpty()) {
				try {
					ContractTag tag = contractHome.create();

					tag.setName("care-time");
					tag.setType(java.lang.Integer.class);
					tag.setCategoryId(getContractCategory());
					tag.store();
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			boolean hasBankId = new NBSLoginBusinessBean().hasBankLogin(application.getOwner());
			
			if(createContractContentToApplication(application,locale,validFrom,changeStatus,hasBankId)){
			
			    if(validFrom!=null)
			        application.setFromDate(validFrom.getDate());
			    
			    if(changeStatus){
			        application.setApplicationStatus(getStatusContract());
		        		changeCaseStatus(application, getCaseStatusContract().getStatus(), user);
		        		createMessagesForParentsOnContractCreation(application, locale, hasBankId);
			    }
			    else{
			    		changeCaseStatus(application, application.getCaseStatus().getStatus(), user);
			    		createMessagesForParentsOnContractCareTimeAlter(application,locale,hasBankId);
			    }
			    
				
				int invoiceReceiverId = -1;
				SchoolClassMember oldStudent = null;
				if(oldArchiveID>0 ){//&& application.getContractFileId()>0){
					
                    ChildCareContract con = getChildCareContractArchiveHome().findByPrimaryKey(new Integer(oldArchiveID));
                    if(con!=null){
                    	invoiceReceiverId = con.getInvoiceReceiverID();
                    	oldStudent = con.getSchoolClassMember();
                    }
                   
				}
				addContractToArchive(-1,oldArchiveID,true, application,application.getContractId(), validFrom.getDate(), employmentTypeID,invoiceReceiverId,user,createNewStudent,schoolTypeId,schoolClassId, oldStudent);
				application.store();
			
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

	/**
     * @param user
     * @param locale
     * @param changeStatus
     * @param application
     * @param hasBankId
     * @param defaultContractCreatedBody
     * @param defaultContractChangedBody
	 * @throws RemoteException
     */
    private void createMessagesForParentsOnContractCreation( ChildCareApplication application, Locale locale, boolean hasBankId) {
        String localizeBankIdPrefix = hasBankId ? "_bankId" : "";
        String defaultContractCreatedBody = hasBankId ? "Your child care contract for {0} has been created. " + "Please sign the contract.\n\nWith best regards,\n{1}" : "Your child care contract for {0} has been created and will be sent to you in a few days. " + "Please write in the desired care time, sign it and then return the contract to us.\n\nWith best regards,\n{1}";
        	String subject = getLocalizedString("child_care.contract_created_subject", "A child care contract has been created", locale);
        	String body = getLocalizedString("child_care.contract_created_body" + localizeBankIdPrefix, defaultContractCreatedBody, locale);
        	sendMessageToParents(application, subject, body);
    }
    
    private void createMessagesForParentsOnContractCareTimeAlter(ChildCareApplication application, Locale locale,  boolean hasBankId) {
        String localizeBankIdPrefix = hasBankId ? "_bankId" : "";
        String defaultContractChangedBody = hasBankId ? "Your child care contract with altered care time for {0} has been created. " + "Please sign the contract.\n\nWith best regards,\n{1}" : "Your child care contract with altered care time for {0} has been created and will be sent to you in a few days. " + "Please write in the desired care time, sign it and then return the contract to us.\n\nWith best regards,\n{1}";
        String subject = getLocalizedString("child_care.alter_caretime_subject", "A contract with changed care time has been created", locale);
    		String body = getLocalizedString("child_care.alter_caretime_body" + localizeBankIdPrefix, defaultContractChangedBody, locale);
    		sendMessageToParents(application, subject, body);
        
    }

    private boolean createContractContentToApplication(ChildCareApplication application,Locale locale,IWTimestamp validFrom, boolean changeStatus,boolean hasBankId){
		ITextXMLHandler pdfHandler = new ITextXMLHandler(ITextXMLHandler.PDF);
		ITextXMLHandler txtHandler = new ITextXMLHandler(ITextXMLHandler.TXT);
		try {
            List pdfBuffers = pdfHandler.writeToBuffers(getTagMap(application, locale, validFrom, !changeStatus), getXMLContractPdfURL(getIWApplicationContext().getIWMainApplication().getBundle(se.idega.idegaweb.commune.presentation.CommuneBlock.IW_BUNDLE_IDENTIFIER), locale));
            List txtBuffers = txtHandler.writeToBuffers(getTagMap(application, locale, validFrom, !changeStatus, hasBankId ? "<care-time/>" : "..."), getXMLContractTxtURL(getIWApplicationContext().getIWMainApplication().getBundle(se.idega.idegaweb.commune.presentation.CommuneBlock.IW_BUNDLE_IDENTIFIER), locale));
            if (pdfBuffers != null && pdfBuffers.size() == 1 && txtBuffers != null && txtBuffers.size() == 1) {
            	String contractText = txtHandler.bufferToString((MemoryFileBuffer) txtBuffers.get(0));
            	contractText = breakString(contractText, 80);
            	ICFile contractFile = pdfHandler.writeToDatabase((MemoryFileBuffer) pdfBuffers.get(0), "contract.pdf", pdfHandler.getPDFMimeType());
            	ContractService service = (ContractService) getServiceInstance(ContractService.class);
            	Contract contract = service.getContractHome().create(((Integer) application.getOwner().getPrimaryKey()).intValue(), getContractCategory(), validFrom, null, "C", contractText);
            	int contractID = ((Integer) contract.getPrimaryKey()).intValue();
            
            	//contractFile.addTo(Contract.class,contractID);
            	contract.addFileToContract(contractFile);
            
            	application.setContractId(contractID);
            	application.setContractFileId(((Integer) contractFile.getPrimaryKey()).intValue());
            	return true;
            }
        } catch (IBOLookupException e) {
            e.printStackTrace();
        } catch (IDOLookupException e) {
            e.printStackTrace();
        } catch (IDOAddRelationshipException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IWBundleDoesNotExist e) {
            e.printStackTrace();
        } catch (EJBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return false;
	}


	private String breakString(String page, int maxLineLength) {
		StringBuffer pageWrapped = new StringBuffer();
		StringBuffer line = new StringBuffer();
		StringTokenizer stLine = new StringTokenizer(page, "\n", true);
		while (stLine.hasMoreTokens()) {
			String readLine = stLine.nextToken();
			//			System.out.println("Token: " + readLine);
			if (readLine.equals("\n")) {
				pageWrapped.append(line.toString().trim());
				pageWrapped.append("\n");
				line = new StringBuffer();
			}
			else {
				StringTokenizer stWord = new StringTokenizer(readLine, " ", true);
				while (stWord.hasMoreTokens()) {
					String word = stWord.nextToken();
					if (line.length() + word.length() > maxLineLength) {
						String trimmedLine = line.toString().trim();
						if (trimmedLine.length() > 0) {
							pageWrapped.append(trimmedLine);
							pageWrapped.append("\n");
						}
						line = new StringBuffer();
					}
					line.append(word);

				}
			}
		}
		pageWrapped.append(line.toString().trim());

		return pageWrapped.toString();
	}

	private int getContractCategory() {
		IWBundle bundle = getIWApplicationContext().getIWMainApplication().getBundle(getBundleIdentifier());
		return Integer.parseInt(bundle.getProperty(PROPERTY_CONTRACT_CATEGORY, "2"));
	}

	public boolean assignContractToApplication(String ids[], User user, Locale locale) {
		boolean done = false;

		if (ids != null && ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				done = assignContractToApplication(Integer.parseInt(id), -1,-1, null, -1, user, locale, false,false,-1,-1);
				if (!done) return done;
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
				if (!done) return done;
			}
		}

		return done;
	}

	public Collection getGrantedApplicationsByUser(User owner) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);

			return home.findAllCasesByUserAndStatus(owner, getCaseStatusGranted().getStatus());
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

	public Collection getApplicationsByUser(User owner) {
		Collection c = null;
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);

			c = home.findAllCasesByUserAndStatus(owner, getCaseStatusGranted().getStatus());
			c.addAll(home.findAllCasesByUserAndStatus(owner, getCaseStatusOpen().getStatus()));
			c.addAll(home.findAllCasesByUserAndStatus(owner, getCaseStatusDenied().getStatus()));
			c.addAll(home.findAllCasesByUserAndStatus(owner, getCaseStatusInactive().getStatus()));

		}
		catch (RemoteException e) {
			e.printStackTrace();
			c = null;
		}
		catch (FinderException e) {
			e.printStackTrace();
			c = null;
		}

		return c;
	}

	public Collection getApplicationsForChild(User child) {
		return getApplicationsForChild(child, null);
	}
	
	public Collection getApplicationsForChild(User child, String caseCode) {
		return getApplicationsForChild(((Integer) child.getPrimaryKey()).intValue(), caseCode);
	}

	public Collection getApplicationsForChild(int childId) {
		return getApplicationsForChild(childId, null);
	}
	
	public Collection getApplicationsForChild(int childId, String caseCode) {
		try {
			String[] caseStatus = { getCaseStatusDeleted().getStatus(), getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusDenied().getStatus(), getCaseStatusReady().getStatus()};
			return getChildCareApplicationHome().findApplicationByChildAndNotInStatus(childId, caseStatus, caseCode);
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public int getNumberOfApplicationsForChildByStatus(int childID, String caseStatus) {
		return getNumberOfApplicationsForChildByStatus(childID, caseStatus, null);
	}

	public int getNumberOfApplicationsForChildByStatus(int childID, String caseStatus, String caseCode) {
		try {
			return getChildCareApplicationHome().getNumberOfApplicationsForChild(childID, caseStatus, caseCode);
		}
		catch (IDOException ie) {
			return 0;
		}
	}

	public int getNumberOfApplicationsForChild(int childID) {
		try {
			return getChildCareApplicationHome().getNumberOfApplicationsForChild(childID);
		}
		catch (IDOException ie) {
			return 0;
		}
	}

	public int getNumberOfApplicationsForChildNotInactive(int childID) {
		return getNumberOfApplicationsForChildNotInactive(childID, null);
	}

	public int getNumberOfApplicationsForChildNotInactive(int childID, String caseCode) {
		try {
			String[] caseStatus = { getCaseStatusDeleted().getStatus(), getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusDenied().getStatus()};
			return getChildCareApplicationHome().getNumberOfApplicationsForChildNotInStatus(childID, caseStatus, caseCode);
		}
		catch (IDOException ie) {
			return 0;
		}
	}

	public boolean hasOutstandingOffers(int childID, String caseCode) {
		int numberOfOffers = getNumberOfApplicationsForChildByStatus(childID, getCaseStatusGranted().getStatus(), caseCode);
		if (numberOfOffers > 0) return true;
		return false;
	}

	public ChildCareApplication getApplicationForChildAndProvider(int childID, int providerID) {
		try {
			String[] statuses = { String.valueOf(getStatusReady())};
			return getChildCareApplicationHome().findApplicationByChildAndProviderAndStatus(childID, providerID, statuses);
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public Collection findAllGrantedApplications() {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);

			return home.findAllCasesByStatus(getCaseStatusGranted().getStatus());
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
					if (check.getStatus().equals(statusRedeem)) it.remove();
				}
				catch (Exception e) {
				}
			}

			return appl;
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public ChildCareApplication getApplicationByPrimaryKey(String key) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);

			return home.findByPrimaryKey(new Integer(key));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}



	private ChildCareApplication getChildCareApplication(int applicationID) throws FinderException {
		return getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
	}

	public boolean redeemApplication(String applicationId, User performer) {
		ChildCareApplication appl = getApplicationByPrimaryKey(applicationId);
		if (appl == null) return false;

		CaseBusiness caseBiz;
		try {
			caseBiz = (CaseBusiness) getServiceInstance(CaseBusiness.class);
			caseBiz.changeCaseStatus(appl, getCaseStatusRedeem().getStatus(), performer);
		}
		catch (RemoteException e) {
			e.printStackTrace();

			return false;
		}

		return true;
	}

	protected ChildCareApplication getChildCareApplicationInstance(Case theCase) throws RuntimeException {
		String caseCode = "unreachable";
		try {
			caseCode = theCase.getCode();
			if (ChildCareConstants.CASE_CODE_KEY.equals(caseCode) || ChildCareConstants.AFTER_SCHOOL_CASE_CODE_KEY.equals(caseCode)) {
				int caseID = ((Integer) theCase.getPrimaryKey()).intValue();
				return this.getApplicationByPrimaryKey(String.valueOf(caseID));
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		throw new ClassCastException("Case with casecode: " + caseCode + " cannot be converted to a schoolchoice");
	}

	public Collection getPreSchoolTypes() {
		ArrayList l = new ArrayList();
		try {
			SchoolTypeHome home = getSchoolBusiness().getSchoolTypeHome();
			l.add(home.findByTypeKey("sch_type.school_type_forskola"));
			l.add(home.findByTypeKey("sch_type.school_type_allman_forskola"));
		}
		catch (Exception e) {
		}
		return l;
	}

	public Collection getFamilyDayCareTypes() {
		ArrayList l = new ArrayList();
		try {
			SchoolTypeHome home = getSchoolBusiness().getSchoolTypeHome();
			l.add(home.findByTypeKey("sch_type.school_type_familjedaghem"));
			l.add(home.findByTypeKey("sch_type.school_type_familjedaghem_allman_forskola"));
		}
		catch (Exception e) {
		}
		return l;
	}

	public Collection getFamilyAfterSchoolTypes() {
		ArrayList l = new ArrayList();
		try {
			SchoolTypeHome home = getSchoolBusiness().getSchoolTypeHome();
			l.add(home.findByTypeKey("sch_type.school_type_fam_fritids6"));
			l.add(home.findByTypeKey("sch_type.school_type_fam_fritids7-9"));
		}
		catch (Exception e) {
		}
		return l;
	}

	protected HashMap getTagMap(ChildCareApplication application, Locale locale, IWTimestamp validFrom, boolean isChange) throws RemoteException {
		return getTagMap(application, locale, validFrom, isChange, "...");
	}

	protected HashMap getTagMap(ChildCareApplication application, Locale locale, int careTime, IWTimestamp validFrom, boolean isChange) throws RemoteException {
		return getTagMap(application, locale, careTime, validFrom, isChange, "...");
	}

	protected HashMap getTagMap(ChildCareApplication application, Locale locale, IWTimestamp validFrom, boolean isChange, String emptyCareTimeValue) throws RemoteException {
		return getTagMap(application, locale, application.getCareTime(), validFrom, isChange, emptyCareTimeValue);
	}

	protected HashMap getTagMap(ChildCareApplication application, Locale locale, int careTime, IWTimestamp validFrom, boolean isChange, String emptyCareTimeValue) throws RemoteException {
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
				if (((Integer) parent.getPrimaryKey()).intValue() != ((Integer) parent1.getPrimaryKey()).intValue()) parent2 = parent;
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
			PostalCode code = address.getPostalCode();
			if (code != null) {
				addressString += ", " + code.getPostalAddress();
			}
		}

		String phoneString = "-";
		if (phone != null) phoneString = phone.getNumber();

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

		peer = new XmlPeer(ElementTags.CHUNK, "preschool");
		peer.setContent(application.getPreSchool());
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "careTime");
		if (careTime != -1 && !isChange)
			peer.setContent(String.valueOf(careTime));

		else
			peer.setContent(emptyCareTimeValue);
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "careTimeChange");
		if (careTime != -1 && isChange)
			peer.setContent(String.valueOf(careTime));
		else
			peer.setContent("....");
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "childName");
		//peer.setContent(TextSoap.convertSpecialCharactersToNumeric(child.getName()));
		peer.setContent(child.getName());
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "personalID");
		peer.setContent(PersonalIDFormatter.format(child.getPersonalID(), locale));
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "provider");
		//peer.setContent(TextSoap.convertSpecialCharactersToNumeric(provider.getSchoolName()));
		peer.setContent(provider.getSchoolName());
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "parent1");
		//peer.setContent(TextSoap.convertSpecialCharactersToNumeric(parent1Name));
		peer.setContent(parent1Name);
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "parent2");
		//peer.setContent(TextSoap.convertSpecialCharactersToNumeric(parent2Name));
		peer.setContent(parent2Name);
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "personalID1");
		peer.setContent(parent1PersonalID);
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "personalID2");
		peer.setContent(parent2PersonalID);
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "address");
		//peer.setContent(TextSoap.convertSpecialCharactersToNumeric(addressString));
		peer.setContent(addressString);
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "phone");
		peer.setContent(phoneString);
		map.put(peer.getAlias(), peer);

		return map;
	}

	public String getXMLContractTxtURL(IWBundle iwb, Locale locale) {
		return iwb.getResourcesRealPath(locale) + FileUtil.getFileSeparator() + "childcare_contract_txt.xml";
	}

	public String getXMLContractPdfURL(IWBundle iwb, Locale locale) {
		return iwb.getResourcesRealPath(locale) + FileUtil.getFileSeparator() + "childcare_contract.xml";
	}

	/*
	 * public String getBundleIdentifier() { return
	 * se.idega.idegaweb.commune.presentation.CommuneBlock.IW_BUNDLE_IDENTIFIER;
	 */

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

	public boolean hasBeenPlacedWithOtherProvider(int childID, int providerID) {
		try {
			String caseStatus[] = { getCaseStatusReady().getStatus()};
			int applications = getChildCareApplicationHome().getNumberOfPlacedApplications(childID, providerID, caseStatus);
			if (applications > 0) return true;
			return false;
		}
		catch (IDOException e) {
			return false;
		}
	}

	private ChildCareContract addContractToArchive(int contractFileID,int oldArchiveID,boolean createNew, ChildCareApplication application, int contractID, Date validFrom, int employmentTypeID,int invoiceReceiverId,User user,boolean createNewStudent,int schoolTypeId,int schoolClassId,SchoolClassMember oldStudent) throws NoPlacementFoundException {
		try {
			ChildCareContract archive = null,oldArchive = null;
			
			int applicationID = ((Integer)application.getPrimaryKey()).intValue();
			if (contractFileID != -1){
				if(createNew)
					archive = getChildCareContractArchiveHome().create();
				else
					archive = getChildCareContractArchiveHome().findByContractFileID(contractFileID);
			}
			else
				archive = getChildCareContractArchiveHome().create();

			if(oldArchiveID>0)
				oldArchive = getChildCareContractArchiveHome().findByPrimaryKey(new Integer(oldArchiveID));
			

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
			
			if (employmentTypeID != -1) 
				archive.setEmploymentType(employmentTypeID);
			
			if(invoiceReceiverId>0 )
				archive.setInvoiceReceiverID(invoiceReceiverId);
			else if(oldArchive!=null && oldArchive.getInvoiceReceiverID()>0 && oldArchive.getChildID() == application.getChildId())
				archive.setInvoiceReceiverID(oldArchive.getInvoiceReceiverID());
			if (application.getApplicationStatus() != getStatusContract()) {
				try {
					SchoolClassMember student= null;
					if(oldStudent==null && oldArchive!=null)
						oldStudent = oldArchive.getSchoolClassMember();
					if(oldStudent ==null)
						oldStudent = getLatestPlacement(application.getChildId(), application.getProviderId());
					//if(student!=null && createNewStudent && (schoolTypeId != student.getSchoolTypeId() || schoolClassId!= student.getSchoolClassId())){
					if(createNewStudent && oldStudent!=null && oldStudent.getSchoolTypeId()!=schoolTypeId  && oldStudent.getSchoolClassId() != schoolClassId){
						// end old placement with the chosen date -1 and create new placement
						student = createNewPlacement(application,schoolTypeId,schoolClassId,oldStudent,new IWTimestamp(validFrom),user);
					}
					if(student==null)
						student = oldStudent;//
					if(student==null)
						student = getLatestPlacement(application.getChildId(), application.getProviderId());
					if(((Integer)student.getStudent().getPrimaryKey()).intValue()==application.getChildId())
						archive.setSchoolClassMember(student);
					else{
					    throw new NoPlacementFoundException("Classmember record's child id doesn't match application's child id");
					}
				}
				catch (FinderException fe) {
					throw new NoPlacementFoundException(fe);
				} catch (RemoteException e) {
					throw new NoPlacementFoundException(e);
				} catch (EJBException e) {
					throw new NoPlacementFoundException(e);
				}
			}
			// test also for futurecontracts if oldContract is provided
			//IWTimestamp fromDate = new IWTimestamp(validFrom);
			if(oldArchive!=null && hasFutureContracts(applicationID)){
				try {
					Collection futureContracts = getChildCareContractArchiveHome().findFutureContractsByApplication(applicationID,validFrom);
					IWTimestamp earliestFutureStartDate = null;
					for (Iterator iter = futureContracts.iterator(); iter.hasNext();) {
						ChildCareContract futureContract = (ChildCareContract) iter.next();
						IWTimestamp newDate = new IWTimestamp(futureContract.getValidFromDate());
						if(earliestFutureStartDate ==null){
							earliestFutureStartDate = new IWTimestamp(newDate);
						}
						else if(newDate.isEarlierThan(earliestFutureStartDate)){
							earliestFutureStartDate = new IWTimestamp(newDate);
						}
					}
					earliestFutureStartDate.addDays(-1);
					terminateContract(archive,earliestFutureStartDate.getDate(),false);
					//archive.setTerminatedDate(earliestFutureStartDate.getDate());
					//fromDate.addDays(-1);
					
				} catch (FinderException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			
			archive.store();
			IWTimestamp terminationDate = new IWTimestamp(validFrom);
			terminationDate.addDays(-1);
			if(oldArchive!=null){
				terminateContract(oldArchive,terminationDate.getDate(),false);
			}
			
			
			if (contractFileID != -1) {
				
				terminateContract(contractFileID, terminationDate.getDate(), false);
			}

			if (contractFileID != -1) {
				Contract contract = archive.getContract();
				contract.setValidFrom(validFrom);
				contract.store();
			}
			return archive;
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ChildCareContract getContractFile(int contractFileID) {
		try {
			return getChildCareContractArchiveHome().findByContractFileID(contractFileID);
		}
		catch (FinderException e) {
			return null;
		}
	}

	public Collection getContractsByChild(int childID) {
		try {
			return getChildCareContractArchiveHome().findByChild(childID);
		}
		catch (FinderException e) {
			return new Vector();
		}
	}

	public Collection getContractsByChildAndProvider(int childID, int providerID) {
		try {
			return getChildCareContractArchiveHome().findByChildAndProvider(childID, providerID);
		}
		catch (FinderException e) {
			return new Vector();
		}
	}

	public Collection getContractsByApplication(int applicationID) {
		try {
			return getChildCareContractArchiveHome().findByApplication(applicationID);
		}
		catch (FinderException e) {
			return new Vector();
		}
	}
	
	

	private int terminateContract(int contractFileID, Date terminatedDate, boolean removePlacing) {
		
		return terminateContract(getContractFile(contractFileID),terminatedDate,removePlacing);
	}
	
	
	private int terminateContract(ChildCareContract archive,Date terminatedDate,boolean removePlacing){
			
		int placementID = -1;
		try {
			if (archive != null) {
				placementID = archive.getSchoolClassMemberId();
				IWTimestamp terminate = new IWTimestamp(terminatedDate);
				IWTimestamp validFrom = new IWTimestamp(archive.getValidFromDate());
				if (validFrom.isLaterThan(terminate)) {
					ChildCareApplication application = archive.getApplication();
					application.setFromDate(null);
					application.store();

					if (removePlacing) deleteFromProvider(application.getChildId(), application.getProviderId());

					Contract contract = archive.getContract();
					if (contract != null) {
						contract.setValidTo(terminatedDate);
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
						contract.setStatus("T");
						contract.store();
					}
					archive.store();
				}
			}
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
		}
		catch (RemoveException e) {
			e.printStackTrace();
		}
		return placementID;
	}

	public String getLocalizedCaseDescription(Case theCase, Locale locale) {
		ChildCareApplication choice = getChildCareApplicationInstance(theCase);
		Object[] arguments = { choice.getChild().getFirstName(), String.valueOf(choice.getChoiceNumber()), choice.getProvider().getName()};

		String desc = super.getLocalizedCaseDescription(theCase, locale);
		return MessageFormat.format(desc, arguments);
	}

	public MessageBusiness getMessageBusiness() {
		try {
			return (MessageBusiness) this.getServiceInstance(MessageBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	public CommuneUserBusiness getUserBusiness() {
		try {
			return (CommuneUserBusiness) this.getServiceInstance(CommuneUserBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	public SchoolBusiness getSchoolBusiness() {
		try {
			return (SchoolBusiness) this.getServiceInstance(SchoolBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	public CheckBusiness getCheckBusiness() {
		try {
			return (CheckBusiness) this.getServiceInstance(CheckBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	public SchoolChoiceBusiness getSchoolChoiceBusiness() {
		if (schoolChoiceBusiness == null) {
			try {
				schoolChoiceBusiness = (SchoolChoiceBusiness) this.getServiceInstance(SchoolChoiceBusiness.class);
			}
			catch (RemoteException e) {
				throw new IBORuntimeException(e.getMessage());
			}
		}
		return schoolChoiceBusiness;
	}

	public AfterSchoolBusiness getAfterSchoolBusiness() {
		try {
			return (AfterSchoolBusiness) this.getServiceInstance(AfterSchoolBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	/**
	 * @return char
	 */
	public char getStatusAccepted() {
		return ChildCareConstants.STATUS_ACCEPTED;
	}

	/**
	 * @return char
	 */
	public char getStatusContract() {
		return ChildCareConstants.STATUS_CONTRACT;
	}

	/**
	 * @return char
	 */
	public char getStatusParentsAccept() {
		return ChildCareConstants.STATUS_PARENTS_ACCEPT;
	}

	/**
	 * @return char
	 */
	public char getStatusPriority() {
		return ChildCareConstants.STATUS_PRIORITY;
	}

	/**
	 * @return char
	 */
	public char getStatusReady() {
		return ChildCareConstants.STATUS_READY;
	}

	/**
	 * @return char
	 */
	public char getStatusSentIn() {
		return ChildCareConstants.STATUS_SENT_IN;
	}

	/**
	 * @return char
	 */
	public char getStatusTimedOut() {
		return ChildCareConstants.STATUS_TIMED_OUT;
	}

	/**
	 * @return char
	 */
	public char getStatusCancelled() {
		return ChildCareConstants.STATUS_CANCELLED;
	}

	/**
	 * @return char
	 */
	public char getStatusDenied() {
		return ChildCareConstants.STATUS_DENIED;
	}

	/**
	 * @return char
	 */
	public char getStatusDeleted() {
		return ChildCareConstants.STATUS_DELETED;
	}

	/**
	 * @return char
	 */
	public char getStatusMoved() {
		return ChildCareConstants.STATUS_MOVED;
	}

	/**
	 * @return char
	 */
	public char getStatusRejected() {
		return ChildCareConstants.STATUS_REJECTED;
	}

	/**
	 * @return char
	 */
	public char getStatusNotAnswered() {
		return ChildCareConstants.STATUS_NOT_ANSWERED;
	}

	/**
	 * @return char
	 */
	public char getStatusNewChoice() {
		return ChildCareConstants.STATUS_NEW_CHOICE;
	}

	public int getQueueTotalByProvider(int providerID) {
		try {
			String[] caseStatus = { getCaseStatusDeletedString(), getCaseStatusInactiveString(), getCaseStatusCancelledString(), getCaseStatusReadyString()};
			return getChildCareApplicationHome().getQueueSizeNotInStatus(providerID, caseStatus);
		}
		catch (IDOException e) {
			return 0;
		}
	}

	public int getQueueByProvider(int providerID) {
		try {
			return getChildCareApplicationHome().getQueueSizeInStatus(providerID, getCaseStatusOpenString());
		}
		catch (IDOException e) {
			return 0;
		}
	}

	public int getQueueTotalByProvider(int providerID, Date from, Date to, boolean isOnlyFirstHand) {
		try {
			return getChildCareApplicationHome().getQueueSizeInStatus(providerID, STATUS_IN_QUEUE, from, to, isOnlyFirstHand);
		}
		catch (IDOException e) {
			return 0;
		}
	}

	public int getQueueByProvider(int providerID, Date from, Date to, boolean isOnlyFirstHand) {
		try {
			return getChildCareApplicationHome().getQueueSizeInStatus(providerID, STATUS_NOT_PROCESSED, from, to, isOnlyFirstHand);
		}
		catch (IDOException e) {
			return 0;
		}
	}

	public int getQueueTotalByProviderWithinMonths(int providerID, int months, boolean isOnlyFirstHand) {
		try {
			IWTimestamp to = IWTimestamp.RightNow();
			to.addMonths(months);
			Date toDate = to.getDate();
			return getChildCareApplicationHome().getQueueSizeInStatus(providerID, STATUS_IN_QUEUE, IWTimestamp.RightNow().getDate(), toDate, isOnlyFirstHand);
		}
		catch (IDOException e) {
			return 0;
		}
	}

	public int getBruttoQueueTotalByProvider(int providerID, Date from, Date to, boolean isOnlyFirstHand) {
		try {
			return getChildCareApplicationHome().getBruttoQueueSizeInStatus(providerID, STATUS_IN_QUEUE, from, to, isOnlyFirstHand);
		}
		catch (IDOException e) {
			return 0;
		}
	}

	public int getBruttoQueueByProvider(int providerID, Date from, Date to, boolean isOnlyFirstHand) {
		try {
			return getChildCareApplicationHome().getBruttoQueueSizeInStatus(providerID, STATUS_NOT_PROCESSED, from, to, isOnlyFirstHand);
		}
		catch (IDOException e) {
			return 0;
		}
	}

	public int getBruttoQueueTotalByProviderWithinMonths(int providerID, int months, boolean isOnlyFirstHand) {
		try {
			IWTimestamp to = IWTimestamp.RightNow();
			to.addMonths(months);
			Date toDate = to.getDate();
			return getChildCareApplicationHome().getBruttoQueueSizeInStatus(providerID, STATUS_IN_QUEUE, IWTimestamp.RightNow().getDate(), toDate, isOnlyFirstHand);
		}
		catch (IDOException e) {
			return 0;
		}
	}

	public int getNettoQueueTotalByProvider(int providerID, Date from, Date to, boolean isOnlyFirstHand) {
		try {
			return getChildCareApplicationHome().getNettoQueueSizeInStatus(providerID, STATUS_IN_QUEUE, from, to, isOnlyFirstHand);
		}
		catch (IDOException e) {
			return 0;
		}
	}

	public int getNettoQueueByProvider(int providerID, Date from, Date to, boolean isOnlyFirstHand) {
		try {
			return getChildCareApplicationHome().getNettoQueueSizeInStatus(providerID, STATUS_NOT_PROCESSED, from, to, isOnlyFirstHand);
		}
		catch (IDOException e) {
			return 0;
		}
	}

	public int getNettoQueueTotalByProviderWithinMonths(int providerID, int months, boolean isOnlyFirstHand) {
		try {
			IWTimestamp to = IWTimestamp.RightNow();
			to.addMonths(months);
			Date toDate = to.getDate();
			return getChildCareApplicationHome().getNettoQueueSizeInStatus(providerID, STATUS_IN_QUEUE, IWTimestamp.RightNow().getDate(), toDate, isOnlyFirstHand);
		}
		catch (IDOException e) {
			return 0;
		}
	}

	public int getQueueTotalByArea(int areaID) {
		try {

			String[] caseStatus = { getCaseStatusDeleted().getStatus(), getCaseStatusInactiveString(), getCaseStatusCancelledString(), getCaseStatusReadyString()};

			return getChildCareApplicationHome().getQueueSizeByAreaNotInStatus(areaID, caseStatus);
		}
		catch (IDOException e) {
			return 0;
		}
	}

	public int getQueueByArea(int areaID) {
		try {
			return getChildCareApplicationHome().getQueueSizeByAreaInStatus(areaID, getCaseStatusOpenString());
		}
		catch (IDOException e) {
			return 0;
		}
	}

	public int getOldQueueTotal(String[] queueType, boolean exported) {
		try {
			return getChildCareQueueHome().getTotalCount(queueType, exported);
		}
		catch (IDOException e) {
			return 0;
		}
	}

	public Map getProviderAreaMap(Collection schoolAreas, Locale locale, String emptyString, boolean isFreetime) {
		return getProviderAreaMap(schoolAreas, null, locale, emptyString, isFreetime);
	}
	
	public Map getProviderAreaMap(Collection schoolAreas, School currentSchool, Locale locale, String emptyString, boolean isFreetime) {
		try {
			SortedMap areaMap = new TreeMap(new SchoolAreaComparator(locale));
			if (schoolAreas != null) {
				List areas = new ArrayList(schoolAreas);

				Collection schoolTypes = getChildCareSchoolTypes(isFreetime);
				Iterator iter = areas.iterator();
				while (iter.hasNext()) {
					SortedMap providerMap = new TreeMap(new SchoolComparator(locale));
					providerMap.put("-1", emptyString);

					SchoolArea area = (SchoolArea) iter.next();
					SchoolBusiness sb = getSchoolBusiness();
					Collection providers = sb.findAllSchoolsByAreaAndTypes(((Integer) area.getPrimaryKey()).intValue(), schoolTypes);
					boolean addSchool = true;
					if (providers != null) {
						Iterator iterator = providers.iterator();
						while (iterator.hasNext()) {
							School provider = (School) iterator.next();
							if (!provider.getInvisibleForCitizen()) {
								addSchool = true;
							}
							else {
								addSchool = false;
							}
							
							if (currentSchool != null) {
								addSchool = !currentSchool.equals(provider);
							}
							
							if (addSchool) {
								providerMap.put(provider, provider);
							}
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

	private Collection getChildCareSchoolTypes(boolean isFreetime) {
		try {
			if (isFreetime) {
				return getSchoolBusiness().getSchoolTypeHome().findAllFreetimeTypes();
			}
			else {
				return getSchoolBusiness().findAllSchoolTypesInCategory(getSchoolBusiness().getChildCareSchoolCategory(), false);
			}
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
		catch (FinderException e) {
			return new ArrayList();
		}
	}

	public ChildCareContract getValidContract(int applicationID) {
		IWTimestamp stamp = new IWTimestamp();
		return getValidContract(applicationID, stamp.getDate());
	}

	public ChildCareContract getValidContract(int applicationID, Date validDate) {
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

	public ChildCareApplication getActiveApplicationByChild(int childID) {
		try {
			return getChildCareApplicationHome().findActiveApplicationByChild(childID);
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public boolean hasActiveApplication(int childID) {
		return hasActiveApplication(childID, null);
	}

	public boolean hasActiveApplication(int childID, String caseCode) {
		try {
			int numberOfApplications = getChildCareApplicationHome().getNumberOfActiveApplications(childID, caseCode);
			if (numberOfApplications > 0) return true;
			return false;
		}
		catch (IDOException fe) {
			return false;
		}
	}

	public ChildCareContract getValidContractByChild(int childID) {
		try {
			return getChildCareContractArchiveHome().findValidContractByChild(childID);
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public void removeFutureContracts(int applicationID) {
		IWTimestamp stamp = new IWTimestamp();
		removeFutureContracts(applicationID, stamp.getDate());
	}

	public void removeFutureContracts(int applicationID, Date date) {
		UserTransaction t = getSessionContext().getUserTransaction();

		try {
			t.begin();
			ChildCareApplication application = getApplication(applicationID);
			ChildCareContract archive = getValidContract(applicationID);
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
				archive = (ChildCareContract) iter.next();
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

	public boolean removeContract(int childcareContractID, User performer) {
		try {
			return removeContract(getChildCareContractArchiveHome().findByPrimaryKey(new Integer(childcareContractID)), performer);
		}
		catch (FinderException e) {
			return false;
		}
	}

	public boolean removeContract(ChildCareContract childcareContract, User performer) {
		UserTransaction t = getSessionContext().getUserTransaction();

		try {
			t.begin();

			Contract contract = childcareContract.getContract();
			if (contract != null) {
				contract.removeFileFromContract(childcareContract.getContractFile());
			}

			ChildCareApplication application = childcareContract.getApplication();
			SchoolClassMember member = childcareContract.getSchoolClassMember();

			removeInvoiceRecords(childcareContract);
			childcareContract.remove();
			verifyApplication(null,application, member, performer);

			if (contract != null) {
				contract.removeAllFiles();
				contract.remove();
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

	private void removeInvoiceRecords(ChildCareContract contract) throws RemoveException {
		try {
			InvoiceBusiness business = (InvoiceBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), InvoiceBusiness.class);
			Collection records = business.findInvoiceRecordsByContract(contract);
			Iterator iter = records.iterator();
			while (iter.hasNext()) {
				InvoiceRecord element = (InvoiceRecord) iter.next();
				element.remove();
			}
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
		catch (FinderException fe) {
			//Nothing found, which is OK...
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
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
				ChildCareApplication app = (ChildCareApplication) it.next();
				User child = app.getChild();
				if (!getCareBusiness().hasGrantedCheck(child)) getCheckBusiness().createGrantedCheck(child);
			}
		}
		catch (Exception e) {
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
								case DBV_WITH_PLACE:
									length = 4;
									break;
								case DBV_WITHOUT_PLACE:
									length = 5;
									break;
								case FS_WITH_PLACE:
									length = 4;
									break;
								case FS_WITHOUT_PLACE:
									length = 5;
									break;
							}
							if (choices.size() < length) length = choices.size();

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
					if (success) exportQueue(choices);
				}
			}
			else {
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public boolean hasActivePlacement(int childID) {
		return getActivePlacement(childID) != null;
	}

	public boolean canCancelContract(int applicationID) {
		int numberOfContracts = getNumberOfContractsForApplication(applicationID);
		if (numberOfContracts > 1) {
			if (hasFutureContracts(applicationID)) return false;
		}
		return true;
	}

	public int getNumberOfContractsForApplication(int applicationID) {
		try {
			return getChildCareContractArchiveHome().getContractsCountByApplication(applicationID);
		}
		catch (IDOException e) {
			return -1;
		}
	}

	public boolean hasFutureContracts(int applicationID) {
		int numberOfContracts = getNumberOfFutureContracts(applicationID);
		if (numberOfContracts > 0) return true;
		return false;
	}

	public boolean hasActiveContract(int applicationID) {
		try {
			IWTimestamp stampNow = new IWTimestamp();
			int numberOfContracts = getChildCareContractArchiveHome().getNumberOfActiveForApplication(applicationID, stampNow.getDate());
			if (numberOfContracts > 0) return true;
			return false;
		}
		catch (IDOException e) {
			return false;
		}
	}

	public int getNumberOfFutureContracts(int applicationID) {
		try {
			IWTimestamp stampNow = new IWTimestamp();
			return getChildCareContractArchiveHome().getFutureContractsCountByApplication(applicationID, stampNow.getDate());
		}
		catch (IDOException e) {
			return 0;
		}
	}

	public boolean hasUnansweredOffers(int childID, String caseCode) {
		try {
			int numberOfOffers = getChildCareApplicationHome().getNumberOfApplicationsForChild(childID, getCaseStatusGranted().getStatus(), caseCode);
			if (numberOfOffers > 0) return true;
			return false;
		}
		catch (IDOException e) {
			return false;
		}
	}

	public ChildCareApplication getActivePlacement(int childID) {
		try {

			Collection applications = getChildCareApplicationHome().findApplicationByChild(childID);
			Iterator i = applications.iterator();
			while (i.hasNext()) {
				ChildCareApplication app = (ChildCareApplication) i.next();
				if (app.isActive()) { return app; }
			}
			return null;
		}
		catch (FinderException ex) {
			return null;
		}
	}

	public boolean hasActivePlacementNotWithProvider(int childID, int providerID) {
		try {
			int numberOfPlacings = getChildCareContractArchiveHome().getNumberOfActiveNotWithProvider(childID, providerID);
			if (numberOfPlacings > 0) return true;
			return false;
		}
		catch (IDOException e) {
			return false;
		}
	}

	public boolean hasTerminationInFutureNotWithProvider(int childID, int providerID) {
		try {
			IWTimestamp stamp = new IWTimestamp();
			int numberOfPlacings = getChildCareContractArchiveHome().getNumberOfTerminatedLaterNotWithProvider(childID, providerID, stamp.getDate());
			if (numberOfPlacings > 0) return true;
			return false;
		}
		catch (IDOException e) {
			return false;
		}
	}
	
	public boolean hasTerminationInFuture(int childID) {
		return hasTerminationInFutureNotWithProvider(childID, -1);
	}
	
	public Date getEarliestPossiblePlacementDate(int childID) {
		ChildCareContract contract = getLatestTerminatedContract(childID);
		if (contract != null) {
			IWTimestamp stamp = new IWTimestamp(contract.getTerminatedDate());
			stamp.addDays(1);
			
			return stamp.getDate();
		}
		return null;
	}

	public ChildCareContract getLatestTerminatedContract(int childID) {
		try {
			IWTimestamp stamp = new IWTimestamp();
			return getChildCareContractArchiveHome().findLatestTerminatedContractByChild(childID, stamp.getDate());
		}
		catch (FinderException e) {
			return null;
		}
	}
	
	public School getCurrentProviderByPlacement(int childID) {
		ChildCareApplication application = getActiveApplicationByChild(childID);
		if (application != null) {
			return application.getProvider();
		}
		else {
			ChildCareContract contract = getLatestTerminatedContract(childID);
			if (contract != null) {
				return contract.getApplication().getProvider();
			}
		}
		
		return null;
	}

	public ChildCareContract getLatestContract(int childID) {
		try {
			return getChildCareContractArchiveHome().findLatestContractByChild(childID);
		}
		catch (FinderException e) {
			return null;
		}
	}

	public Collection getCaseLogNewContracts(Timestamp fromDate, Timestamp toDate) {
		try {
			return getCaseLogsByCaseAndDatesAndStatusChange(ChildCareConstants.CASE_CODE_KEY, fromDate, toDate, getCaseStatusContract().toString(), getCaseStatusReady().toString());
		}
		catch (FinderException e) {
			return new ArrayList();
		}
	}

	public Collection getCaseLogAlteredContracts(Timestamp fromDate, Timestamp toDate) {
		try {
			return getCaseLogsByCaseAndDatesAndStatusChange(ChildCareConstants.CASE_CODE_KEY, fromDate, toDate, getCaseStatusReady().toString(), getCaseStatusReady().toString());
		}
		catch (FinderException e) {
			return new ArrayList();
		}
	}

	public Collection getCaseLogTerminatedContracts(Timestamp fromDate, Timestamp toDate) {
		try {
			return getCaseLogsByCaseAndDatesAndStatusChange(ChildCareConstants.CASE_CODE_KEY, fromDate, toDate, getCaseStatusReady().toString(), getCaseStatusCancelled().toString());
		}
		catch (FinderException e) {
			return new ArrayList();
		}
	}

	public boolean importChildToProvider(int applicationID, int childID, int providerID, int groupID, int careTime, int employmentTypeID, int schoolTypeID, String comment, IWTimestamp fromDate, IWTimestamp toDate, Locale locale, User parent, User admin) throws AlreadyCreatedException {
		return importChildToProvider(applicationID, childID, providerID, groupID, careTime, employmentTypeID, schoolTypeID, comment, fromDate, toDate, locale, parent, admin, false);
	}

	public boolean importChildToProvider(int applicationID, int childID, int providerID, int groupID, int careTime, int employmentTypeID, int schoolTypeID, String comment, IWTimestamp fromDate, IWTimestamp toDate, Locale locale, User parent, User admin, boolean canCreateMultiple) throws AlreadyCreatedException {
		return importChildToProvider(applicationID, childID, providerID, groupID, careTime, employmentTypeID, schoolTypeID, comment, fromDate, toDate, locale, parent, admin, canCreateMultiple, null, null, false, null, false, null);
	}

	public boolean importChildToProvider(int applicationID, int childID, int providerID, int groupID, int careTime, int employmentTypeID, int schoolTypeID, String comment, IWTimestamp fromDate, IWTimestamp toDate, Locale locale, User parent, User admin, boolean canCreateMultiple, IWTimestamp lastReplyDate, String preSchool, boolean extraContract, String extraContractMessage, boolean extraContractOther, String extraContractOtherMessage) throws AlreadyCreatedException {
		UserTransaction t = getSessionContext().getUserTransaction();

		if (!canCreateMultiple) {
			try {
				String[] status = { String.valueOf(getStatusCancelled()), String.valueOf(getStatusReady())};
				ChildCareApplication application = getChildCareApplicationHome().findApplicationByChildAndApplicationStatus(childID, status);
				if (application != null) { throw new AlreadyCreatedException(); }
			}
			catch (FinderException fe) {
			}
		}

		try {
			t.begin();
			boolean isUpdate = false;
			boolean finalize = false;

			ChildCareApplication application = getApplication(applicationID);
			if (application == null) {
				try {
					application = getUnhandledApplicationsByChildAndProvider(childID, providerID);
				}
				catch (FinderException fe) {
					application = getChildCareApplicationHome().create();
				}
			}
			else {
				isUpdate = true;
				if (application.getApplicationStatus() == getStatusContract()) finalize = true;
			}

			User child = getUserBusiness().getUser(childID);

			application.setChildId(childID);
			application.setProviderId(providerID);
			application.setFromDate(fromDate.getDate());
			if (toDate != null) application.setRejectionDate(toDate.getDate());
			if (careTime != -1) application.setCareTime(careTime);
			application.setOwner(parent);
			application.setChoiceNumber(1);
			application.setMessage(comment);
			GrantedCheck check = getCheckBusiness().getGrantedCheckByChild(childID);
			if (check == null) {
				int checkID = getCheckBusiness().createGrantedCheck(child);
				check = getCheckBusiness().getGrantedCheck(checkID);
			}
			application.setCheck(check);
			if (preSchool != null) application.setPreSchool(preSchool);
			if (lastReplyDate != null) application.setOfferValidUntil(lastReplyDate.getDate());
			application.setHasExtraContract(extraContract);
			if (extraContractMessage != null) application.setExtraContractMessage(extraContractMessage);
			application.setHasExtraContractOther(extraContractOther);
			if (extraContractOtherMessage != null) application.setExtraContractMessageOther(extraContractOtherMessage);

			//SchoolClassMember student = null;
			if (!isUpdate || finalize) {
				Timestamp removedDate = null;
				if (toDate != null) removedDate = toDate.getTimestamp();

				if (groupID == -1) {
					groupID = createDefaultGroup(providerID);
					if (groupID == -1) return false;
				}
				if (schoolTypeID == -1) schoolTypeID = getSchoolBusiness().getSchoolTypeIdFromSchoolClass(groupID);
				getSchoolBusiness().storeSchoolClassMemberCC(childID, groupID, schoolTypeID, fromDate.getTimestamp(), removedDate, ((Integer) admin.getPrimaryKey()).intValue(), comment);
			}

			if (finalize) {
				alterValidFromDate(application, fromDate.getDate(), employmentTypeID, locale, admin);
			}
			else {
				ITextXMLHandler pdfHandler = new ITextXMLHandler(ITextXMLHandler.TXT + ITextXMLHandler.PDF);
				List buffers = pdfHandler.writeToBuffers(getTagMap(application, locale, fromDate, false), getXMLContractPdfURL(getIWApplicationContext().getIWMainApplication().getBundle(se.idega.idegaweb.commune.presentation.CommuneBlock.IW_BUNDLE_IDENTIFIER), locale));
				if (buffers != null && buffers.size() == 2) {
					String contractText = pdfHandler.bufferToString((MemoryFileBuffer) buffers.get(1));
					ICFile contractFile = pdfHandler.writeToDatabase((MemoryFileBuffer) buffers.get(0), "contract.pdf", pdfHandler.getPDFMimeType());
					ContractService service = (ContractService) getServiceInstance(ContractService.class);
					Contract contract = service.getContractHome().create(((Integer) application.getOwner().getPrimaryKey()).intValue(), getContractCategory(), fromDate, toDate, "C", contractText);
					int contractID = ((Integer) contract.getPrimaryKey()).intValue();

					//contractFile.addTo(Contract.class,contractID);
					contract.addFileToContract(contractFile);

					application.setContractId(contractID);
					application.setContractFileId(((Integer) contractFile.getPrimaryKey()).intValue());
					if (toDate != null) {
						application.setApplicationStatus(getStatusCancelled());
						changeCaseStatus(application, getCaseStatusCancelled().getStatus(), admin);
					}
					else {
						if (isUpdate) {
							application.setApplicationStatus(getStatusContract());
							changeCaseStatus(application, getCaseStatusContract().getStatus(), admin);
						}
						else {
							application.setApplicationStatus(getStatusReady());
							changeCaseStatus(application, getCaseStatusReady().getStatus(), admin);
						}
					}

					addContractToArchive(-1,-1, false,application, contractID, fromDate.getDate(), employmentTypeID,((Integer)parent.getPrimaryKey()).intValue(),admin,false,-1,-1,null);
					/* included in the addContractToArchive
					if (archive != null) {
						archive.setInvoiceReceiver(parent);
						archive.store();
					}*/
					application.store();
				}
			}

			t.commit();
			return true;
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
		return false;
	}

	private int createDefaultGroup(int providerID) {
		String className = "Placerade barn";
		SchoolClass group = null;
		try {
			group = getSchoolBusiness().getSchoolClassHome().findByNameAndSchool(className, providerID);
		}
		catch (Exception e) {
			try {
				group = getSchoolBusiness().storeSchoolClass(-1, className, providerID, -1, -1, -1);
			}
			catch (RemoteException e1) {
				return -1;
			}
		}

		if (group != null) { return ((Integer) group.getPrimaryKey()).intValue(); }
		return -1;
	}

	public Collection findAllEmploymentTypes() {
		try {
			EmploymentTypeHome home = (EmploymentTypeHome) IDOLookup.getHome(EmploymentType.class);

			return home.findAll();
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void updateMissingPlacements() {
		try {
			Collection applications = getChildCareApplicationHome().findApplicationsWithoutPlacing();
			Iterator iter = applications.iterator();
			while (iter.hasNext()) {
				ChildCareApplication application = (ChildCareApplication) iter.next();
				IWTimestamp fromDate = new IWTimestamp(application.getFromDate());
				Timestamp endDate = null;
				if (application.getRejectionDate() != null) endDate = new IWTimestamp(application.getRejectionDate()).getTimestamp();

				try {
					Collection contracts = getChildCareContractArchiveHome().findByApplication(((Integer) application.getPrimaryKey()).intValue());
					SchoolClass group = getSchoolBusiness().getSchoolClassHome().findOneBySchool(application.getProviderId());
					int groupID = ((Integer) group.getPrimaryKey()).intValue();
					int schoolTypeID = getSchoolBusiness().getSchoolTypeIdFromSchoolClass(groupID);
					SchoolClassMember member = getSchoolBusiness().storeSchoolClassMemberCC(application.getChildId(), groupID, schoolTypeID, fromDate.getTimestamp(), endDate, -1, null);
					Iterator iterator = contracts.iterator();
					while (iterator.hasNext()) {
						ChildCareContract contract = (ChildCareContract) iterator.next();
						contract.setSchoolClassMember(member);
						contract.store();
					}
				}
				catch (FinderException fe) {
					fe.printStackTrace();
				}
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			System.out.println("[ChildCareApplication]: No applications found with missing placements.");
		}
	}

	public Collection findUnhandledApplicationsNotInCommune() {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			String caseStatus[] = { getCaseStatusOpen().getStatus(), getCaseStatusContract().getStatus()};
			IWBundle iwb = getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
			int areaID = Integer.parseInt(iwb.getProperty(PROP_OUTSIDE_SCHOOL_AREA, "-1"));

			return home.findApplicationsInSchoolAreaByStatus(areaID, caseStatus, 1);
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

	public Collection findSentInAndRejectedApplicationsByArea(Object area, int monthsInQueue, int weeksToPlacementDate, boolean firstHandOnly, String caseCode) throws FinderException {
		IWTimestamp months = new IWTimestamp();
		months.addMonths(-monthsInQueue);

		IWTimestamp weeks = new IWTimestamp();
		weeks.addWeeks(weeksToPlacementDate);

		String applicationStatus[] = { String.valueOf(getStatusSentIn())/*
																																		 * ,
																																		 * String.valueOf(getStatusRejected())
																																		 */};
		return getChildCareApplicationHome().findAllByAreaAndApplicationStatus(area, applicationStatus, caseCode, months.getDate(), weeks.getDate(), firstHandOnly);
	}

	public Collection findRejectedApplicationsByChild(int childID) throws FinderException {
		String applicationStatus[] = { String.valueOf(getStatusRejected())};
		return getChildCareApplicationHome().findApplicationsByChildAndApplicationStatus(childID, applicationStatus);
	}

	public String getStatusString(char status) {
		if (status == getStatusCancelled()) {
			return getLocalizedString("child_care.status_cancelled", "Cancelled");
		}
		else if (status == getStatusContract()) {
			return getLocalizedString("child_care.status_contract", "Contract");
		}
		else if (status == getStatusRejected()) {
			return getLocalizedString("child_care.status_rejected", "Rejected");
		}
		else if (status == getStatusAccepted()) {
			return getLocalizedString("child_care.status_accepted", "Accepted");
		}
		else if (status == getStatusMoved()) {
			return getLocalizedString("child_care.status_moved", "Moved");
		}
		else if (status == getStatusSentIn()) {
			return getLocalizedString("child_care.status_open", "Open");
		}
		else if (status == getStatusParentsAccept()) {
			return getLocalizedString("child_care.status_parents_accept", "Parents accept");
		}
		else if (status == getStatusReady()) {
			return getLocalizedString("child_care.status_ready", "Ready");
		}
		else if (status == getStatusNotAnswered()) {
			return getLocalizedString("child_care.status_not_answered", "Not answered");
		}
		else if (status == getStatusPriority()) {
			return getLocalizedString("child_care.status_priority", "Priority");
		}
		else if (status == getStatusNewChoice()) {
			return getLocalizedString("child_care.status_new_choice", "New Choice");
		}
		else if (status == getStatusDenied()) {
			return getLocalizedString("child_care.status_denied", "Denied");
		}
		else if (status == ChildCareConstants.STATUS_DELETED) {
			return getLocalizedString("child_care.status_deleted", "Deleted");
		}
		else if (status == ChildCareConstants.STATUS_TIMED_OUT) { return getLocalizedString("child_care.status_timed_out", "Timed out"); }

		return "";
	}
	public String getStatusStringAbbr(char status) {
		if (status == getStatusCancelled()) {
			return getLocalizedString("child_care.status_cancelled_abbr", "Cancelled");
		}
		else if (status == getStatusContract()) {
			return getLocalizedString("child_care.status_contract_abbr", "Contract");
		}
		else if (status == getStatusAccepted()) {
			return getLocalizedString("child_care.status_accepted_abbr", "Accepted");
		}
		else if (status == getStatusMoved()) {
			return getLocalizedString("child_care.status_moved_abbr", "Moved");
		}
		else if (status == getStatusSentIn()) {
			return getLocalizedString("child_care.status_open_abbr", "Open");
		}
		else if (status == getStatusParentsAccept()) {
			return getLocalizedString("child_care.status_parents_accept_abbr", "Parents accept");
		}
		else if (status == getStatusReady()) {
			return getLocalizedString("child_care.status_ready_abbr", "Ready");
		}
		else if (status == getStatusPriority()) {
			return getLocalizedString("child_care.status_priority_abbr", "Priority");
		}
		else if (status == getStatusNewChoice()) {
			return getLocalizedString("child_care.status_new_choice_abbr", "New Choice");
		}
		else if (status == getStatusRejected()) {
			return getLocalizedString("child_care.status_rejected_abbr", "Rejected");
		}
		else if (status == getStatusNotAnswered()) {
			return getLocalizedString("child_care.status_not_answered_abbr", "Not answered");
		}
		else if (status == getStatusDenied()) {
			return getLocalizedString("child_care.status_denied_abbr", "Denied");
		}
		else if (status == ChildCareConstants.STATUS_DELETED) {
			return getLocalizedString("child_care.status_deleted_abbr", "Deleted");
		}
		else if (status == ChildCareConstants.STATUS_TIMED_OUT) { return getLocalizedString("child_care.status_timed_out_abbr", "Timed out"); }

		return "";
	}

	public boolean wasRejectedByParent(ChildCareApplication application) {
		return hasStatusChange(application, getCaseStatusGranted().getStatus(), getCaseStatusInactive().getStatus());
	}
	/**
	 * Checks if the schoolclass belongs to schooltype
	 */
	public boolean isSchoolClassBelongingToSchooltype(int schoolClassId,int schoolTypeId ) {
		try {
			SchoolClass schoolClass = getSchoolBusiness().getSchoolClassHome().findByPrimaryKey(new Integer(schoolClassId));
			return schoolClass.getSchoolTypeId() == schoolTypeId;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Checks if a change only in schooltype but not group  of the archive's classmember 
	 */
	public boolean isTryingToChangeSchoolTypeButNotSchoolClass(int currentArchiveID,int schoolTypeId, int schoolClassId){
		try {
			ChildCareContract currentArchive = getChildCareContractArchiveHome().findByPrimaryKey(new Integer(currentArchiveID));
			if(currentArchive!=null){
				SchoolClassMember currentClassMember = currentArchive.getSchoolClassMember();
				return currentClassMember!=null && currentClassMember.getSchoolTypeId()!=schoolTypeId  && currentClassMember.getSchoolClassId() == schoolClassId;
			}
		} catch (FinderException e) {
		}
		return false;
	}
	
	 private String getSQL(){return ""
	     +" select sch.sch_school_id,sch.school_name,"
	     +" pr.comm_childcare_prognosis_id,pr.UPDATED_DATE,pr.THREE_MONTHS_PROGNOSIS,pr.ONE_YEAR_PROGNOSIS,"
	     +" pr.THREE_MONTHS_PRIORITY,pr.ONE_YEAR_PRIORITY,pr.PROVIDER_CAPACITY,"
	     +" count(c.child_id) "
	     +" from comm_childcare c , proc_case p ,sch_school sch, comm_childcare_prognosis pr,sch_school_sch_school_type m,sch_school_type st,ic_commune comm"
	     +" WHERE c.COMM_CHILDCARE_ID=p.proc_case_id "
	     +" and c.provider_id = sch.sch_school_id"
	     +" and sch.commune = comm.ic_commune_id"
	     +" and pr.provider_id = sch.sch_school_id"
	     +" and m.sch_school_id = sch.sch_school_id" 
	     +" and m.sch_school_type_id = st.sch_school_type_id"
	     +" and comm.default_commune = 'Y'"
	     +" and st.school_category = 'CHILD_CARE'"
	     +" AND p.case_status NOT IN ('DELE', 'TYST', 'UPPS', 'KLAR')"
	     +" group by sch.sch_school_id,sch.school_name,"
	     +" pr.comm_childcare_prognosis_id,pr.UPDATED_DATE,pr.THREE_MONTHS_PROGNOSIS,pr.ONE_YEAR_PROGNOSIS,"
	     +" pr.THREE_MONTHS_PRIORITY,pr.ONE_YEAR_PRIORITY,pr.PROVIDER_CAPACITY";
	     }
	     
	 /**
	  * Returns a collection of ProviderStat objects
	  * @return
	  * @throws FinderException
	  */
	     public Collection getProviderStats(Locale sortLocale) throws FinderException{
	     		Connection conn = null;
	 		PreparedStatement Stmt = null;
	 		Vector vector = new Vector();
	 		try {
	 			conn = ConnectionBroker.getConnection();
	 			String s = getSQL();
	 			Stmt = conn.prepareStatement(s);
	 			ResultSet RS = Stmt.executeQuery();
	 			
	 			while (RS.next()) {
	 			   ProviderStat bean = new ProviderStat();
	 			   bean.setProviderID(new Integer(RS.getInt(1)));
	 			   bean.setProviderName(RS.getString(2));
	 			   bean.setPrognosisID(new Integer(RS.getInt(3)));
	 			   bean.setLastUpdate(RS.getDate(4));
	 			   bean.setThreeMonthsPrognosis(new Integer(RS.getInt(5)));
	 			   bean.setOneYearPrognosis(new Integer(RS.getInt(6)));
	 			   bean.setThreeMonthsPriority(new Integer(RS.getInt(7)));
	 			   bean.setOneYearPriority(new Integer(RS.getInt(8)));
	 			   bean.setProviderCapacity(new Integer(RS.getInt(9)));
	 			   bean.setQueueTotal(new Integer(RS.getInt(10)));
	 			   vector.add(bean);
	 			}
	 			RS.close();
	 			
	 		} catch (SQLException sqle) {
	 			throw new FinderException(sqle.getMessage());
	 		} finally {
	 			if (Stmt != null) {
	 				try {
	 					Stmt.close();
	 				} catch (SQLException e) {
	 					e.printStackTrace();
	 				}
	 			}
	 			if (conn != null) {
	 				ConnectionBroker.freeConnection( conn);
	 			}
	 		}
	 		Collections.sort(vector, new ProviderStatComparator(sortLocale));
	 		return vector;
	 		
	     }
	     
	     
	     public PlacementHelper getPlacementHelper(Integer applicationID){
	         ChildCareApplication application = getApplication(applicationID.intValue());
	         IWBundle bundle = getIWApplicationContext().getIWMainApplication().getBundle(getBundleIdentifier());
	         String className = bundle.getProperty(PLACEMENT_HELPER,DefaultPlacementHelper.class.getName());
	         PlacementHelper helper = null;
	         if(className!=null){
	             try {
                    helper = (PlacementHelper)Class.forName(className).newInstance();
                } catch (Exception e) {
                    helper = new DefaultPlacementHelper();
                }
	         }
	         else{
	             helper = new DefaultPlacementHelper();
	         }
	         helper.setApplication(application);
	         helper.setContract(getValidContract(applicationID.intValue()));
             return helper;
	     }

	 	public boolean setUserAsDeceased(Integer userID, java.util.Date deceasedDate) throws RemoteException {
			// Remove the deceased user as invoice receiver for 
			try {
				ChildCareContractHome ccch = (ChildCareContractHome)getIDOHome(ChildCareContract.class);
				Collection activeOrFutureContracts = ccch.findByInvoiceReceiverActiveOrFuture(userID,new java.sql.Date(deceasedDate.getTime()));
				for (Iterator iter = activeOrFutureContracts.iterator(); iter
						.hasNext();) {
					ChildCareContract contract = (ChildCareContract) iter.next();
					contract.setInvoiceReceiverID(null);
					contract.store();
				}
			} catch (IDOStoreException e1) {
				logError("Invoice reciver could not be set as null for deceased user "+userID);
				return false;
			} catch (FinderException e1) {
				return false;	
			}
			return true;
	 	}
	 	
		private CareBusiness getCareBusiness() {
			try {
				return (CareBusiness) this.getServiceInstance(CareBusiness.class);
			}
			catch (RemoteException e) {
				throw new IBORuntimeException(e.getMessage());
			}
		}
		
		public boolean rejectApplication(ChildCareApplication application, String subject, String message, User user) {
			UserTransaction t = getSessionContext().getUserTransaction();
			try {
				t.begin();
				CaseBusiness caseBiz = (CaseBusiness) getServiceInstance(CaseBusiness.class);
				IWTimestamp now = new IWTimestamp();
				application.setRejectionDate(now.getDate());
				application.setApplicationStatus(SchoolConstants.STATUS_DENIED);
				caseBiz.changeCaseStatus(application, getCaseStatusDenied().getStatus(), user);
				sendMessageToParents(application, subject, message);

				if (isAfterSchoolApplication(application)) {
					Iterator iter = application.getChildrenIterator();
					if (iter != null) {
						while (iter.hasNext()) {
							Case element = (Case) iter.next();
							if (isAfterSchoolApplication(element) && element.getCaseStatus().equals(getCaseStatusInactive())) {
								application = getApplication(((Integer) element.getPrimaryKey()).intValue());
								application.setApplicationStatus(SchoolConstants.STATUS_SENT_IN);
								caseBiz.changeCaseStatus(application, getCaseStatusPreliminary().getStatus(), user);

								subject = this.getLocalizedString("after_school.application_received_subject", "After school care application received.");
								message = this.getLocalizedString("after_school.application_received_body", "We have received you application for {0}, {2}, to {1}.");
								sendMessageToParents(application, subject, message);
							}
						}
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

		public boolean rejectApplication(int applicationId, String subject, String body, User user) {
			try {
				ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
				ChildCareApplication appl = home.findByPrimaryKey(new Integer(applicationId));
				return rejectApplication(appl, subject, body, user);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}

			return false;
		}
		
		public void sendMessageToParents(ChildCareApplication application, String subject, String body) {
			sendMessageToParents(application, subject, body, false);
		}

		public void sendMessageToParents(ChildCareApplication application, String subject, String body, boolean alwaysSendLetter) {
			sendMessageToParents(application, subject, body, body, alwaysSendLetter);
		}

		public void sendMessageToParents(ChildCareApplication application, String subject, String body, String letterBody, boolean alwaysSendLetter) {
			try {
				User child = application.getChild();
				//Object[] arguments = { child.getNameLastFirst(true), application.getProvider().getSchoolName(), PersonalIDFormatter.format(child.getPersonalID(), getIWApplicationContext().getApplicationSettings().getDefaultLocale()), application.getLastReplyDate() != null ? new IWTimestamp(application.getLastReplyDate()).getLocaleDate(getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT) : "xxx", application.getOfferValidUntil() != null ? new IWTimestamp(application.getOfferValidUntil()).getLocaleDate(getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT) : ""};
				Object[] arguments = { child.getName(), application.getProvider().getSchoolName(), PersonalIDFormatter.format(child.getPersonalID(), getIWApplicationContext().getApplicationSettings().getDefaultLocale()), application.getLastReplyDate() != null ? new IWTimestamp(application.getLastReplyDate()).getLocaleDate(getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT) : "xxx", application.getOfferValidUntil() != null ? new IWTimestamp(application.getOfferValidUntil()).getLocaleDate(getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT) : ""};

				User appParent = application.getOwner();
				if (getUserBusiness().getMemberFamilyLogic().isChildInCustodyOf(child, appParent)) {
					Message message = getMessageBusiness().createUserMessage(application, appParent, subject, MessageFormat.format(body, arguments), MessageFormat.format(letterBody, arguments), true, alwaysSendLetter);
					message.setParentCase(application);
					message.store();
				}

				try {
					Collection parents = getUserBusiness().getMemberFamilyLogic().getCustodiansFor(child);
					Iterator iter = parents.iterator();
					while (iter.hasNext()) {
						User parent = (User) iter.next();
						if (!getUserBusiness().haveSameAddress(parent, appParent)) {
							getMessageBusiness().createUserMessage(application, parent, subject, MessageFormat.format(body, arguments), MessageFormat.format(letterBody, arguments), true, alwaysSendLetter);
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

		public boolean isAfterSchoolApplication(Case application) {
			if (application.getCode().equals(SchoolConstants.AFTER_SCHOOL_CASE_CODE_KEY)) return true;
			return false;
		}

		public ChildCareApplication getApplication(int applicationID) {
			try {
				return getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
			}
			catch (FinderException e) {
				return null;
			}
		}
}