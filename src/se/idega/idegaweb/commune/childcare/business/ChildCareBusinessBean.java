/*
 * $Id:$ Copyright (C) 2002 Idega hf. All Rights Reserved. This software is the
 * proprietary information of Idega hf. Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import is.idega.block.family.business.NoCustodianFound;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import java.util.logging.Level;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import se.idega.block.pki.business.NBSLoginBusinessBean;
import se.idega.idegaweb.commune.accounting.userinfo.business.UserInfoService;
import se.idega.idegaweb.commune.accounting.userinfo.data.HouseHoldFamily;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.business.Constants;
import se.idega.idegaweb.commune.care.business.AlreadyCreatedException;
import se.idega.idegaweb.commune.care.business.CareBusiness;
import se.idega.idegaweb.commune.care.business.CareConstants;
import se.idega.idegaweb.commune.care.business.CareInvoiceBusiness;
import se.idega.idegaweb.commune.care.business.DefaultPlacementHelper;
import se.idega.idegaweb.commune.care.business.PlacementHelper;
import se.idega.idegaweb.commune.care.check.data.Check;
import se.idega.idegaweb.commune.care.check.data.GrantedCheck;
import se.idega.idegaweb.commune.care.data.ApplicationPriority;
import se.idega.idegaweb.commune.care.data.ApplicationPriorityHome;
import se.idega.idegaweb.commune.care.data.CareTime;
import se.idega.idegaweb.commune.care.data.CareTimeBMPBean;
import se.idega.idegaweb.commune.care.data.CareTimeHome;
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
import se.idega.idegaweb.commune.childcare.event.ChildCareEventListener;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.data.Message;

import com.idega.block.contract.business.ContractService;
import com.idega.block.contract.data.Contract;
import com.idega.block.contract.data.ContractTag;
import com.idega.block.contract.data.ContractTagHome;
import com.idega.block.pdf.ITextXMLHandler;
import com.idega.block.pdf.business.PrintingContext;
import com.idega.block.pdf.business.PrintingService;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.process.data.Case;
import com.idega.block.school.business.SchoolAreaComparator;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolComparator;
import com.idega.block.school.business.SchoolTypeComparator;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberLog;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.block.school.data.SchoolUser;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Phone;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
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
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.FileUtil;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.database.ConnectionBroker;
import com.idega.util.text.Name;
import com.lowagie.text.ElementTags;
import com.lowagie.text.xml.XmlPeer;

/**
 * This class does something very clever.....
 * 
 * @author palli
 * @version 1.0
 */
public class ChildCareBusinessBean extends CaseBusinessBean implements ChildCareBusiness, CaseBusiness {

	private static String PROP_OUTSIDE_SCHOOL_AREA = "not_in_commune_school_area";

	final int DBV_WITH_PLACE = 0;

	final int DBV_WITHOUT_PLACE = 1;

	final int FS_WITH_PLACE = 2;

	final int FS_WITHOUT_PLACE = 3;

	private final static String PROPERTY_CONTRACT_CATEGORY = "childcare_contract_category";

	private final static String PROPERTY_HAS_THREE_MONTHS_RULE = "use_three_months_rule";
	
	private final static String PROPERTY_USE_VACANCIES = "use_vacancies";
	
	private final static String PROPERTY_USE_EMPLOYMENT = "use_employment";
	
	private final static String PROPERTY_USE_PARENTAL= "use_parental";
	
	private final static String PROPERTY_USE_PRESCHOOL_LINE = "use_preschool_line";

	private final static String PROPERTY_SEND_JOINT_MESSAGE_TO_OTHER_CUSTODIAN = "send_joint_message_to_other_custodian_on_child_care_choice";

	private final static String STATUS_NOT_PROCESSED = String.valueOf(ChildCareConstants.STATUS_SENT_IN);

	private final static String[] STATUS_IN_QUEUE = { String.valueOf(ChildCareConstants.STATUS_SENT_IN), String.valueOf(ChildCareConstants.STATUS_PRIORITY), String.valueOf(ChildCareConstants.STATUS_ACCEPTED), String.valueOf(ChildCareConstants.STATUS_PARENTS_ACCEPT), String.valueOf(ChildCareConstants.STATUS_CONTRACT) };

	private static final String PLACEMENT_HELPER = "PlacementHelper";

	public String getBundleIdentifier() {
		return Constants.IW_BUNDLE_IDENTIFIER;

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
			String[] caseStatus = { getCaseStatusOpen().getStatus(), getCaseStatusGranted().getStatus() };
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
			if (applications > 0)
				return true;
			return false;
		}
		catch (IDOException e) {
			return false;
		}
	}

	public void updatePrognosis(int providerID, int threeMonthsPrognosis, int oneYearPrognosis, int threeMonthsPriority, int oneYearPriority, int providerCapacity, int vacancies, String providerComments) {
		try {
			ChildCarePrognosis prognosis = getPrognosis(providerID);
			if (prognosis == null)
				prognosis = getChildCarePrognosisHome().create();

			prognosis.setProviderID(providerID);
			prognosis.setThreeMonthsPrognosis(threeMonthsPrognosis);
			prognosis.setOneYearPrognosis(oneYearPrognosis);
			prognosis.setThreeMonthsPriority(threeMonthsPriority);
			prognosis.setOneYearPriority(oneYearPriority);
			prognosis.setProviderCapacity(providerCapacity);
			if (vacancies != -1)
				prognosis.setVacancies(vacancies);

			prognosis.setProviderComments(providerComments);
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
		IWBundle bundle = getIWApplicationContext().getIWMainApplication().getBundle(getBundleIdentifier());

		try {
			t.begin();
			ChildCareApplication appl = null;
			User child = getUserBusiness().getUser(childId);
			IWTimestamp now = new IWTimestamp();
			String[] caseStatus = { getCaseStatusOpen().getStatus(), getCaseStatusGranted().getStatus() };
			boolean hasThreeMonthsRule = bundle.getBooleanProperty(PROPERTY_HAS_THREE_MONTHS_RULE, true);
			boolean sendJointMessageToOtherCustodian = bundle.getBooleanProperty(PROPERTY_SEND_JOINT_MESSAGE_TO_OTHER_CUSTODIAN, false);
			Collection applications = new ArrayList();

			if (hasThreeMonthsRule) {
				try {
					/*
					 * IWTimestamp dateOfBirth = new IWTimestamp(child.getDateOfBirth());
					 * now = new IWTimestamp(); int days =
					 * IWTimestamp.getDaysBetween(child.getDateOfBirth()); if (days < 90) {
					 * dateOfBirth.addMonths(3); now = new IWTimestamp(dateOfBirth); }
					 */
					// Bug fixed, 90 days is not the magic number !! ( aron )
					IWTimestamp dateOfBirth = new IWTimestamp(child.getDateOfBirth());
					dateOfBirth.addMonths(3);
					int days = IWTimestamp.getDaysBetween(dateOfBirth, now);
					if (days <= 0) {
						now = new IWTimestamp(dateOfBirth);
					}
				}
				catch (NullPointerException e) {
					now = new IWTimestamp();
				}
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
						applications.add(appl);
					}
				}
			}

			if (sendJointMessageToOtherCustodian) {
				sendMessageToOtherParent(applications);
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

	public void sendMessageToOtherParent(Collection applications) {
		try {
			Object[] arguments = { "", "-", "-", "-", "-", "-" };

			User child = null;
			User appParent = null;
			boolean first = true;
			int i = 1;
			Iterator iter = applications.iterator();
			while (iter.hasNext()) {
				ChildCareApplication application = (ChildCareApplication) iter.next();
				if (first) {
					first = false;
					child = application.getChild();
					appParent = application.getOwner();
					arguments[0] = child.getName();
				}
				arguments[i] = application.getProvider().getSchoolName();
				i++;
			}

			String subject = getLocalizedString("child_care.child_care_choices_separated_subject", "Child care choices registered");
			String body = getLocalizedString("child_care.child_care_choices_separated_body", "The other custodian for {0} has created the following care choices:\n\n1. {1}\n2. {2}\n3. {3}\n4. {4}5. {5}");

			if (child != null && appParent != null) {
				try {
					Collection parents = getUserBusiness().getMemberFamilyLogic().getCustodiansFor(child);
					iter = parents.iterator();
					while (iter.hasNext()) {
						User parent = (User) iter.next();
						if (!getUserBusiness().haveSameAddress(parent, appParent)) {
							getMessageBusiness().createUserMessage(parent, subject, MessageFormat.format(body, arguments));
						}
					}
				}
				catch (NoCustodianFound ncf) {
					ncf.printStackTrace();
				}
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
	}

	private boolean hasQueuePriority(User child, int providerID) throws RemoteException {
		SchoolClassMember member = null;
		try {
			member = getLatestPlacement(((Integer) child.getPrimaryKey()).intValue(), providerID);
			if (member != null && member.getNeedsSpecialAttention())
				return true;
		}
		catch (FinderException e) {
			member = null;
		}

		Collection parents = null;
		UserInfoService uis = (UserInfoService) getServiceInstance(UserInfoService.class);
		try {
			parents = uis.getCustodiansAndTheirPartnersAtSameAddress(child);
		} catch (Exception e) {}
		
		if (parents != null) {
			IWTimestamp stamp = new IWTimestamp();
			Iterator iter = parents.iterator();
			while (iter.hasNext()) {
				User parent = (User) iter.next();
				HouseHoldFamily f = uis.getHouseHoldFamily(parent);
				Collection children = f.getChildren();
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

	private boolean canChangeApplication(ChildCareApplication application, int newProviderID, IWTimestamp newFromDate, IWTimestamp newQueueDate) {
		int oldProviderID = application.getProviderId();
		IWTimestamp oldFromDate = new IWTimestamp();
		IWTimestamp oldQueueDate = new IWTimestamp();
		if (application.getFromDate() != null)
			oldFromDate = new IWTimestamp(application.getFromDate());
		if (application.getQueueDate() != null)
			oldQueueDate = new IWTimestamp(application.getQueueDate());

		if (oldProviderID != newProviderID)
			return true;
		else {
			if (!oldFromDate.equals(newFromDate))
				return true;
			if (!oldQueueDate.equals(newQueueDate))
				return true;
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
			// Object[] arguments = { child.getNameLastFirst(true),
			// application.getProvider().getSchoolName(), new
			// IWTimestamp(application.getFromDate()).toSQLDateString(),
			// child.getPersonalID()}; //Malin 040824
			Object[] arguments = { child.getName(), application.getProvider().getSchoolName(), new IWTimestamp(application.getFromDate()).toSQLDateString(), child.getPersonalID() };

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
					if (((Integer) element.getPrimaryKey()).intValue() == ((Integer) queue.getPrimaryKey()).intValue())
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

	public Collection getApplicationsByProviderAndApplicationStatus(int providerID, String applicationStatus) {
		try {
			String[] caseStatus = { applicationStatus };
			return getChildCareApplicationHome().findApplicationsByProviderAndApplicationStatus(providerID, caseStatus);
		}
		catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Collection getTerminatedApplicationsByProviderAndApplicationStatus(int providerID, String applicationStatus) {
		try {
			IWTimestamp terminatedDate = new IWTimestamp();
			String[] caseStatus = { applicationStatus };
			return getChildCareApplicationHome().findApplicationsByProviderAndApplicationStatusAndTerminatedDate(providerID, caseStatus, terminatedDate.getDate());
		}
		catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Collection getInactiveApplicationsByProvider(int providerID) {
		try {
			IWTimestamp terminatedDate = new IWTimestamp();
			String[] caseStatus = { String.valueOf(getStatusCancelled()), String.valueOf(getStatusDenied()), String.valueOf(getStatusNotAnswered()), String.valueOf(getStatusRejected()), String.valueOf(getStatusTimedOut()), String.valueOf(getStatusDeleted()) };
			return getChildCareApplicationHome().findApplicationsByProviderAndApplicationStatusAndTerminatedDate(providerID, caseStatus, terminatedDate.getDate());
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
			String[] caseStatus = { getCaseStatusOpen().getStatus(), getCaseStatusPending().getStatus() };
			return getChildCareApplicationHome().getNumberOfApplicationsByStatus(providerID, caseStatus);
		}
		catch (IDOException ie) {
			return 0;
		}
	}

	public int getNumberOfApplicationsByProvider(int providerID) {
		try {
			String[] caseStatus = { getCaseStatusDeleted().getStatus(), getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus() };

			return getChildCareApplicationHome().getNumberOfApplications(providerID, caseStatus);
		}
		catch (IDOException ie) {
			return 0;
		}
	}

	public int getNumberOfApplicationsByProvider(int providerID, int sortBy, Date fromDate, Date toDate) {
		try {
			String[] caseStatus = { getCaseStatusDeleted().getStatus(), getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus() };

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

	public Collection getApplicationsInQueueBeforeDate(int providerID, Date beforeDate) throws FinderException {
		String[] caseStatus = { getCaseStatusOpen().getStatus() };
		return getChildCareApplicationHome().findApplicationsByProviderAndBeforeDate(providerID, beforeDate, caseStatus);
	}

	public Collection getPendingApplications(int childID) {
		return getPendingApplications(childID, null);
	}

	public Collection getPendingApplications(int childID, String caseCode) {
		try {
			String[] caseStatus = { getCaseStatusPending().getStatus() };
			return getChildCareApplicationHome().findApplicationByChildAndInStatus(childID, caseStatus, caseCode);
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public boolean hasPendingApplications(int childID, String caseCode) {
		try {
			String[] caseStatus = { getCaseStatusPending().getStatus() };
			return getChildCareApplicationHome().getNumberOfApplicationsForChildInStatus(childID, caseStatus, caseCode) > 0;
		}
		catch (IDOException ie) {
			return false;
		}
	}

	public boolean hasActiveApplications(int childID, String caseCode, Date activeDate) {
		String[] caseStatus = { getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus() };
		try {
			return getChildCareApplicationHome().getNumberOfApplicationsByStatusAndActiveDate(childID, caseStatus, caseCode, activeDate) > 0;
		}
		catch (IDOException ie) {
			return false;
		}
	}

	public boolean removePendingFromQueue(User performer) {
		IWTimestamp lastReplyDate = new IWTimestamp();
		String[] caseStatus = { getCaseStatusPending().getStatus() };

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
			String[] caseStatus = { getCaseStatusDeleted().getStatus(), getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus() };
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
			String[] caseStatus = { getCaseStatusDeleted().getStatus(), getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus() };

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
			String[] caseStatus = { getCaseStatusDeleted().getStatus(), getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus() };

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
			String[] caseStatus = { getCaseStatusDeleted().getStatus(), getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusReady().getStatus(), getCaseStatusDenied().getStatus(), getCaseStatusContract().getStatus(), getCaseStatusPreliminary().getStatus() };

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
		String[] statuses = { String.valueOf(getStatusSentIn()), String.valueOf(getStatusPriority()), String.valueOf(getStatusAccepted()), String.valueOf(getStatusParentsAccept()) };
		return getChildCareApplicationHome().findApplicationByChildAndProviderAndStatus(childID, providerID, statuses);
	}

	public Collection getUnhandledApplicationsByProvider(School provider) {
		// try {
		return getUnhandledApplicationsByProvider(((Integer) provider.getPrimaryKey()).intValue());
		// }
		// catch (RemoteException e) {
		// e.printStackTrace();
		// return null;
		// }
	}

	public Collection getUnhandledApplicationsByProvider(User provider) {
		try {
			School school;
			school = getFirstProviderForUser(provider);

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
		// try {
		return getUnsignedApplicationsByProvider(((Integer) provider.getPrimaryKey()).intValue());
		// }
		// catch (RemoteException e) {
		// return null;
		// }
	}

	public Collection getUnsignedApplicationsByProvider(User provider) {
		try {
			School school;
			school = getFirstProviderForUser(provider);

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

	public boolean isAfterSchoolApplication(int applicationID) {
		try {
			return isAfterSchoolApplication(getChildCareApplication(applicationID));
		}
		catch (FinderException fe) {
			return false;
		}
	}

	public boolean placeApplication(int applicationID, String subject, String body, String childCareTime, int groupID, int schoolTypeID, int employmentTypeID, User user, Locale locale) {
		UserTransaction t = super.getSessionContext().getUserTransaction();
		try {
			t.begin();
			ChildCareApplication application = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
			application.setCareTime(childCareTime);
			if (groupID != -1) {
				IWTimestamp fromDate = new IWTimestamp(application.getFromDate());
				SchoolClassMember member = getSchoolBusiness().storeSchoolClassMemberCC(application.getChildId(), groupID, schoolTypeID, fromDate.getTimestamp(), ((Integer) user.getPrimaryKey()).intValue());
				getSchoolBusiness().addToSchoolClassMemberLog(member, member.getSchoolClass(), fromDate.getDate(), null, user);
				sendMessageToParents(application, subject, body);
			}
			alterValidFromDate(application, application.getFromDate(), employmentTypeID, locale, user);
			application.setApplicationStatus(getStatusReady());
			application.store();
			try {
				User child = application.getChild();
				
				Collection parents = null;
				UserInfoService uis = (UserInfoService) getServiceInstance(UserInfoService.class);
				try {
					parents = uis.getCustodiansAndTheirPartnersAtSameAddress(child);
				} catch (Exception e) {}
				
				if (parents != null) {
					Iterator iter = parents.iterator();
					String[] statuses = { String.valueOf(getStatusSentIn()), String.valueOf(getStatusAccepted()), String.valueOf(getStatusParentsAccept()), String.valueOf(getStatusContract()), String.valueOf(getStatusReady()), String.valueOf(getStatusParentTerminated()), String.valueOf(getStatusWaiting()) };
					while (iter.hasNext()) {
						User parent = (User) iter.next();
						HouseHoldFamily f = uis.getHouseHoldFamily(parent);
						Collection children = f.getChildren();
						if (children != null) {
							Iterator iterator = children.iterator();
							while (iterator.hasNext()) {
								User sibling = (User) iterator.next();
								if (((Integer) child.getPrimaryKey()).intValue() != ((Integer) sibling.getPrimaryKey()).intValue()) {
									ChildCareApplication siblingApp = this.getApplicationForChildAndProviderinStatus(((Integer) sibling.getPrimaryKey()).intValue(), application.getProviderId(), statuses);
									if (siblingApp != null) {
										siblingApp.setHasQueuePriority(true);
										siblingApp.store();
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {}
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
		alterValidFromDate(applicationID, newDate, employmentTypeID, -1, -1, locale, user);
	}
	
	public void alterValidFromDate(int applicationID, Date newDate, int employmentTypeID, int schoolTypeID, int schoolClassID, Locale locale, User user) throws RemoteException, NoPlacementFoundException {
		try {
			ChildCareApplication application = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
			alterValidFromDate(application, newDate, employmentTypeID, schoolTypeID, schoolClassID, locale, user);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void alterValidFromDate(ChildCareApplication application, Date newDate, int employmentTypeID, Locale locale, User user) throws RemoteException, NoPlacementFoundException {
		alterValidFromDate(application, newDate, employmentTypeID, -1, -1, locale, user);
	}
	
	public void alterValidFromDate(ChildCareApplication application, Date newDate, int employmentTypeID, int schoolTypeID, int schoolClassID, Locale locale, User user) throws RemoteException, NoPlacementFoundException {
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
		addContractToArchive(oldFileID, -1, false, application, -1, fromDate.getDate(), employmentTypeID, -1, user, false, -1, -1, null);
		application.store();
		try {
			SchoolClassMember member = getLatestPlacement(application.getChildId(), application.getProviderId());
			if (schoolTypeID != -1 && schoolClassID != -1) {
				member.setSchoolTypeId(schoolTypeID);
				member.setSchoolClassId(schoolClassID);
				try {
					SchoolClassMemberLog log = getSchoolBusiness().getSchoolClassMemberLogHome().findOpenLogByUser(member);
					log.setSchoolClass(new Integer(schoolClassID));
					log.setStartDate(fromDate.getDate());
					log.store();
				}
				catch (FinderException fe) {
					//No log found...
				}
			}
			member.setRegisterDate(fromDate.getTimestamp());
			member.store();
		}
		catch (FinderException e) {
			// Placing not yet created...
		}
	}

	public boolean alterContract(int childcareContractID, String careTime, Date fromDate, Date endDate, Locale locale, User performer, int employmentType, int invoiceReceiver, int schoolType, int schoolClass) {
		try {
			return alterContract(getChildCareContractArchiveHome().findByPrimaryKey(new Integer(childcareContractID)), careTime, fromDate, endDate, locale, performer, employmentType, invoiceReceiver, schoolType, schoolClass);
		}
		catch (FinderException fe) {
			return false;
		}
	}

	/**
	 * Changes childcare contract, care time, start date, end date. When change
	 * made to those fields a new file is created and old file swapped out
	 * 
	 * 
	 */
	public boolean alterContract(ChildCareContract childcareContract, String careTime, Date fromDate, Date endDate, Locale locale, User performer, int employmentType, int invoiceReceiver, int schoolType, int schoolClass) {
		UserTransaction trans = getSessionContext().getUserTransaction();
		try {
			trans.begin();

			if (fromDate == null)
				throw new NullPointerException("Argument from date is null");

			ChildCareApplication application = childcareContract.getApplication();

			// possible bug found here, allowing null values for start date, which
			// should be possible (aron 06.09.2004)
			if (fromDate != null) {
				childcareContract.setValidFromDate(fromDate);
			}
			childcareContract.setTerminatedDate(endDate);

			if (invoiceReceiver > 0)
				childcareContract.setInvoiceReceiverID(invoiceReceiver);

			if (employmentType > 0)
				childcareContract.setEmploymentType(employmentType);

			if (careTime != null && !childcareContract.getCareTime().equals(careTime)) {
				childcareContract.setCareTime(careTime);
			}

			ICFile contractFile = recreateContractFile(childcareContract, locale);
			childcareContract.setContractFile(contractFile);
			childcareContract.store();

			if (application.getContractId() == childcareContract.getContractID()) {
				application.setContractFileId(((Integer) contractFile.getPrimaryKey()).intValue());
			}
			verifyApplication(childcareContract, application, null, performer, schoolType, schoolClass, false);
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
	 * Update contract with new field values, and recreate file attached to it.
	 * The old file is removed from the contract, and replaced with a new one
	 * 
	 * @throws IDORemoveRelationshipException
	 * @throws IWBundleDoesNotExist
	 * @throws RemoteException
	 * @throws IDOAddRelationshipException
	 */
	public ICFile recreateContractFile(ChildCareContract archive, Locale locale) throws IDORemoveRelationshipException, RemoteException, IWBundleDoesNotExist, IDOAddRelationshipException {
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

	protected void verifyApplication(ChildCareContract lastContract, ChildCareApplication application, SchoolClassMember member, User performer, boolean removal) throws RemoteException {
		verifyApplication(lastContract, application, member, performer, -1, -1, removal);
	}

	protected void verifyApplication(ChildCareContract lastContract, ChildCareApplication application, SchoolClassMember member, User performer, int schoolTypeId, int schoolClassId, boolean removal) throws RemoteException {
		try {
			if (lastContract == null)
				lastContract = getChildCareContractArchiveHome().findLatestContractByApplication(((Integer) application.getPrimaryKey()).intValue());

			ChildCareContract firstContract = getChildCareContractArchiveHome().findFirstContractByApplication(((Integer) application.getPrimaryKey()).intValue());

			application.setFromDate(firstContract.getValidFromDate());
			if (removal) {
				lastContract.setTerminatedDate(application.getRejectionDate());
				lastContract.store();
			}
			else {
				application.setRejectionDate(lastContract.getTerminatedDate());
			}
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
			if (placement != null) {
				Collection contractPlacements = getChildCareContractArchiveHome().findAllBySchoolClassMember(placement);
				// only allow update when only one contract linked to the classmember
				// or the one being changed is the first contract
				if (lastContract.getTerminatedDate() != null) {
					placement.setRemovedDate((new IWTimestamp(lastContract.getTerminatedDate())).getTimestamp());
					getSchoolBusiness().addToSchoolClassMemberLog(placement, placement.getSchoolClass(), lastContract.getTerminatedDate(), performer);
				}
				else {
					placement.setRemovedDate(null);
				}
				if (contractPlacements.size() == 1) {
					placement.setRegisterDate((new IWTimestamp(lastContract.getValidFromDate())).getTimestamp());
					if (schoolTypeId > 0)
						placement.setSchoolTypeId(schoolTypeId);
					if (schoolClassId > 0) {
						placement.setSchoolClassId(schoolClassId);
					}
					placement.store();
				}
				// create new placement and attach to archive
				else if (schoolTypeId > 0 && schoolClassId > 0 && placement.getSchoolTypeId() != schoolTypeId) {
					SchoolClassMember newPlacement = createNewPlacement(new Integer(lastContract.getChildID()), new Integer(schoolTypeId), new Integer(schoolClassId), placement, new IWTimestamp(lastContract.getValidFromDate()), performer);
					lastContract.setSchoolClassMember(newPlacement);
					if (lastContract.getTerminatedDate() != null) {
						placement.setRemovedDate((new IWTimestamp(lastContract.getTerminatedDate())).getTimestamp());
						placement.store();
						getSchoolBusiness().addToSchoolClassMemberLog(placement, placement.getSchoolClass(), lastContract.getTerminatedDate(), performer);
					}
					lastContract.store();
					getSchoolBusiness().addToSchoolClassMemberLog(newPlacement, newPlacement.getSchoolClass(), new IWTimestamp(newPlacement.getRegisterDate()).getDate(), null, performer);
				}
				try {
					SchoolClassMemberLog log = getSchoolBusiness().getSchoolClassMemberLogHome().findByPlacementAndDate(placement, lastContract.getTerminatedDate() != null ? lastContract.getTerminatedDate() : lastContract.getValidFromDate());
					if (schoolClassId != -1) {
						log.setSchoolClass(new Integer(schoolClassId));
						log.store();
					}
				}
				catch (FinderException fe) {
					//No log entry found...
				}

				getSchoolBusiness().alignLogs(placement);
			}
		}
		catch (FinderException fe) {
			application.setContractId(null);
			application.setContractFileId(null);
			application.setApplicationStatus(getStatusDeleted());
			if (member != null) {
				member.setRemovedDate(member.getRegisterDate());
				member.store();
				getSchoolBusiness().alignLogs(member);
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

	public void moveToGroup(int placementID, int schoolClassID, User performer) throws RemoteException {
		try {
			SchoolClassMember classMember = getSchoolBusiness().getSchoolClassMemberHome().findByPrimaryKey(new Integer(placementID));
			SchoolClass oldSchoolClass = classMember.getSchoolClass();

			classMember.setSchoolClassId(schoolClassID);
			classMember.store();

			IWTimestamp startDate = new IWTimestamp();
			IWTimestamp endDate = new IWTimestamp();
			endDate.addDays(-1);

			try {
				SchoolClass newSchoolClass = getSchoolBusiness().getSchoolClassHome().findByPrimaryKey(new Integer(schoolClassID));
				getSchoolBusiness().addToSchoolClassMemberLog(classMember, oldSchoolClass, endDate.getDate(), performer);
				getSchoolBusiness().addToSchoolClassMemberLog(classMember, newSchoolClass, startDate.getDate(), null, performer);
			}
			catch (FinderException fe) {
				log(fe);
			}
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void removeFromProvider(int placementID, Timestamp date, boolean parentalLeave, String message, User performer) {
		try {
			SchoolClassMember classMember = getSchoolBusiness().getSchoolClassMemberHome().findByPrimaryKey(new Integer(placementID));
			classMember.setRemovedDate(date);
			classMember.setNeedsSpecialAttention(parentalLeave);
			classMember.setNotes(message);
			classMember.store();

			IWTimestamp endDate = new IWTimestamp(date);
			getSchoolBusiness().addToSchoolClassMemberLog(classMember, classMember.getSchoolClass(), endDate.getDate(), performer);
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

	public SchoolClassMember getLatestPlacement(int childID, int providerID) throws FinderException {
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
	
	public void createCancelForm(ChildCareApplication application, Date cancelDate, Locale locale) {
		UserTransaction t = getSessionContext().getUserTransaction();
		try {
			t.begin();
      
			application.setRequestedCancelDate(cancelDate);
			application.setApplicationStatus(getStatusWaiting());

			MemoryFileBuffer buffer = new MemoryFileBuffer();
      OutputStream mos = new MemoryOutputStream(buffer);
      InputStream mis = new MemoryInputStream(buffer);
     
      PrintingContext pcx = new CancelFormContext(getIWApplicationContext(), application, locale);
      pcx.setDocumentStream(mos);
      getPrintingService().printDocument(pcx);
      ICFile file = createFile(null, "cancel_form", mis, buffer.length());

			application.setCancelFormFile(file);
			application.store();
			
			t.commit();
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

  private ICFile createFile(ICFile file,String fileName,InputStream is,int length) throws CreateException{
    if(file==null) {
    		file = getICFileHome().create();
    }
    
    if(!fileName.endsWith(".pdf") &&  !fileName.endsWith(".PDF")) {
    		fileName +=".pdf";
    }
    
    file.setFileValue(is);
		file.setMimeType("application/x-pdf");
		
		file.setName(fileName );
		file.setFileSize(length);
		file.store();
		return file;
  }

  private ICFileHome getICFileHome() {
		try {
			return (ICFileHome) getIDOHome(ICFile.class);
		}
		catch (Exception e) {
			throw new IBORuntimeException(e);
		}
	}
    
  private PrintingService getPrintingService()throws IBOLookupException {
    return (PrintingService)getServiceInstance(PrintingService.class);
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

			removeFromProvider(placementID, date.getTimestamp(), parentalLeave, message, user);
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

	public boolean removeFromQueue(ChildCareApplication application, User user) {
		return removeFromQueue(application, user, null);
	}

	private boolean removeFromQueue(ChildCareApplication application, User user, int[] providerIDs) {
		if (providerIDs != null) {
			for (int i = 0; i < providerIDs.length; i++) {
				if (application.getProviderId() == providerIDs[i]) {
					return false;
				}
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
			String caseStatus[] = { getCaseStatusPreliminary().getStatus(), getCaseStatusContract().getStatus() };

			return getChildCareApplicationHome().findActiveApplicationByChildAndStatus(childID, caseStatus);
		}
		catch (FinderException e) {
			return null;
		}
	}

	public Collection getApplicationsByProvider(int providerId) {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
			String caseStatus[] = { getCaseStatusOpen().getStatus(), getCaseStatusPreliminary().getStatus(), getCaseStatusContract().getStatus() };

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
			String caseStatus[] = { getCaseStatusOpen().getStatus(), getCaseStatusGranted().getStatus() };

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
			String[] caseStatus = { getCaseStatusReady().getStatus(), getCaseStatusCancelled().getStatus() };
			return getChildCareApplicationHome().findApplicationsByProviderAndStatus(providerID, caseStatus);
		}
		catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Collection getApplicationsByProvider(School provider) {
		// try {
		return getApplicationsByProvider(((Integer) provider.getPrimaryKey()).intValue());
		// }
		// catch (RemoteException e) {
		// return null;
		// }
	}

	public Collection getApplicationsByProvider(User provider) {
		try {
			School school;
			school = getFirstProviderForUser(provider);

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
		String applicationStatus[] = { String.valueOf(getStatusRejected()), String.valueOf(getStatusTimedOut()), String.valueOf(getStatusDenied()) };
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

	public SchoolClassMember createNewPlacement(int applicationID, int schooltypeID, int schoolclassID, SchoolClassMember oldStudent, IWTimestamp validFrom, User user) throws RemoteException, EJBException {
		return createNewPlacement(getApplication(applicationID), (schooltypeID), (schoolclassID), oldStudent, validFrom, user);
	}

	public SchoolClassMember createNewPlacement(ChildCareApplication application, int schooltypeID, int schoolclassID, SchoolClassMember oldStudent, IWTimestamp validFrom, User user) throws RemoteException, EJBException {
		return createNewPlacement(new Integer(application.getChildId()), new Integer(schooltypeID), new Integer(schoolclassID), oldStudent, validFrom, user);
	}

	public SchoolClassMember createNewPlacement(Integer childID, Integer schooltypeID, Integer schoolclassID, SchoolClassMember oldStudent, IWTimestamp validFrom, User user) throws RemoteException, EJBException {

		SchoolClassMember member = null;
		if (schoolclassID.intValue() != -1) {
			IWTimestamp endDate = new IWTimestamp(validFrom);
			endDate.addDays(-1);

			if (oldStudent != null) {
				oldStudent.setRemovedDate(endDate.getTimestamp());
				oldStudent.store();
			}

			member = getSchoolBusiness().storeSchoolClassMemberCC(childID.intValue(), schoolclassID.intValue(), schooltypeID.intValue(), validFrom.getTimestamp(), ((Integer) user.getPrimaryKey()).intValue());
			// archive.setSchoolClassMember(member);
			// archive.store();
		}
		return member;

	}

	public boolean assignContractToApplication(int applicationID, int oldArchiveID, String childCareTime, IWTimestamp validFrom, int employmentTypeID, User user, Locale locale, boolean changeStatus) {
		return assignContractToApplication(applicationID, -1, childCareTime, validFrom, employmentTypeID, user, locale, changeStatus, false, -1, -1);
	}

	public boolean isOnlyGroupChange(int applicationId, String careTime, Date validFrom, int schoolTypeId) {
		boolean isOnlyGroupChange = false;

		try {
			ChildCareContract archive = getContractByApplicationAndDate(applicationId, validFrom);
			SchoolClassMember member = archive.getSchoolClassMember();
			
			String oldCareTime = archive.getCareTime();
			int oldSchoolTypeId = member.getSchoolTypeId();
			
			if (careTime.equals(oldCareTime) && schoolTypeId == oldSchoolTypeId) {
				isOnlyGroupChange = true;
			}
		} catch (Exception e) {}
		
		return isOnlyGroupChange;
	}
	
	public void changeGroup(int applicationId, Date validFrom, int schoolClassId, User user) {
		UserTransaction transaction = getSessionContext().getUserTransaction();
		try {
			transaction.begin();
			ChildCareContract archive = getContractByApplicationAndDate(applicationId, validFrom);
			SchoolClassMember member = archive.getSchoolClassMember();
			SchoolClass group = getSchoolBusiness().getSchoolClassHome().findByPrimaryKey(new Integer(schoolClassId));
			SchoolClassMemberLog pastLog = null;
			try {
				pastLog = getSchoolBusiness().getSchoolClassMemberLogHome().findByPlacementAndDateBack(member, validFrom);
			}
			catch (FinderException fe) {
				//No past log found...
			}
			
			boolean updateDone = false;
			Date validTo = null;
			if (pastLog != null) {
				if (pastLog.getStartDate().compareTo(validFrom) == 0) {
					pastLog.setSchoolClass(group);
					pastLog.setUserPlacing(user);
					pastLog.store();
					updateDone = true;
				}
				else {
					validTo = pastLog.getEndDate();
				}
			}
			if (!updateDone) {
				IWTimestamp t = new IWTimestamp(validFrom);
				t.addDays(-1);
				getSchoolBusiness().addToSchoolClassMemberLog(member, member.getSchoolClass(), t.getDate(), user);
				getSchoolBusiness().addToSchoolClassMemberLog(member, group, validFrom, validTo, user);
			}		
			member.setSchoolClass(group);
			member.store();

			transaction.commit();
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			try {
				transaction.rollback();
			} catch (SystemException ex) {
				ex.printStackTrace();
			}
		}		
	}
	
	public boolean assignContractToApplication(int applicationID, int archiveID, String childCareTime, IWTimestamp validFrom, int employmentTypeID, User user, Locale locale, boolean changeStatus, boolean createNewStudent, int schoolTypeId, int schoolClassId) {
		UserTransaction transaction = getSessionContext().getUserTransaction();
		try {
			transaction.begin();
			ChildCareApplication application = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
			application.setCareTime(childCareTime);

			if (validFrom == null)
				validFrom = new IWTimestamp(application.getFromDate());
			int oldArchiveID = archiveID;
			if (oldArchiveID <= 0) {
				ChildCareContract con = getLatestContractByApplication(((Integer) application.getPrimaryKey()).intValue());// getLatestContract(application.getChildId());
				oldArchiveID = con != null ? ((Integer) con.getPrimaryKey()).intValue() : -1;

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

			int invoiceReceiverId = -1;
			SchoolClassMember oldStudent = null;
			int oldContractFileID = -1;
			int oldSchoolTypeID = -1;
			String oldCareTime = null;
			if (oldArchiveID > 0) {// && application.getContractFileId()>0){

				ChildCareContract con = getChildCareContractArchiveHome().findByPrimaryKey(new Integer(oldArchiveID));
				if (con != null) {
					oldCareTime = con.getCareTime();
					oldContractFileID = con.getContractFileID();
					invoiceReceiverId = con.getInvoiceReceiverID();
					oldStudent = con.getSchoolClassMember();
					oldSchoolTypeID = oldStudent.getSchoolTypeId();
				}
			}
			
			boolean createNew = false;
			if (oldCareTime != null && !oldCareTime.equals(childCareTime)) {
				oldContractFileID = -1;
			}
			if (oldSchoolTypeID != -1 && schoolTypeId != -1 && oldSchoolTypeID != schoolTypeId) {
				oldContractFileID = -1;
			}
			if (oldContractFileID == -1) {
				createNew = true;
			}

			boolean hasBankId = new NBSLoginBusinessBean().hasBankLogin(application.getOwner());

			if (createNew && createContractContentToApplication(application, locale, validFrom, changeStatus, hasBankId)) {
				if (changeStatus) {
					application.setApplicationStatus(getStatusContract());
					changeCaseStatus(application, getCaseStatusContract().getStatus(), user);
					createMessagesForParentsOnContractCreation(application, locale, hasBankId);
				}
				else {
					changeCaseStatus(application, application.getCaseStatus().getStatus(), user);
					createMessagesForParentsOnContractCareTimeAlter(application, locale, hasBankId);
				}
			}

			addContractToArchive(oldContractFileID, oldArchiveID, createNew, application, application.getContractId(), validFrom.getDate(), employmentTypeID, invoiceReceiverId, user, createNewStudent, schoolTypeId, schoolClassId, oldStudent);
			application.store();

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
	private void createMessagesForParentsOnContractCreation(ChildCareApplication application, Locale locale, boolean hasBankId) {
		String localizeBankIdPrefix = hasBankId ? "_bankId" : "";
		String defaultContractCreatedBody = hasBankId ? "Your child care contract for {0} has been created. " + "Please sign the contract.\n\nWith best regards,\n{1}" : "Your child care contract for {0} has been created and will be sent to you in a few days. " + "Please write in the desired care time, sign it and then return the contract to us.\n\nWith best regards,\n{1}";
		String subject = getLocalizedString("child_care.contract_created_subject", "A child care contract has been created", locale);
		String body = getLocalizedString("child_care.contract_created_body" + localizeBankIdPrefix, defaultContractCreatedBody, locale);
		sendMessageToParents(application, subject, body);
	}

	private void createMessagesForParentsOnContractCareTimeAlter(ChildCareApplication application, Locale locale, boolean hasBankId) {
		String localizeBankIdPrefix = hasBankId ? "_bankId" : "";
		String defaultContractChangedBody = hasBankId ? "Your child care contract with altered care time for {0} has been created. " + "Please sign the contract.\n\nWith best regards,\n{1}" : "Your child care contract with altered care time for {0} has been created and will be sent to you in a few days. " + "Please write in the desired care time, sign it and then return the contract to us.\n\nWith best regards,\n{1}";
		String subject = getLocalizedString("child_care.alter_caretime_subject", "A contract with changed care time has been created", locale);
		String body = getLocalizedString("child_care.alter_caretime_body" + localizeBankIdPrefix, defaultContractChangedBody, locale);
		sendMessageToParents(application, subject, body);

	}

	private boolean createContractContentToApplication(ChildCareApplication application, Locale locale, IWTimestamp validFrom, boolean changeStatus, boolean hasBankId) {
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

				// contractFile.addTo(Contract.class,contractID);
				contract.addFileToContract(contractFile);

				application.setContractId(contractID);
				application.setContractFileId(((Integer) contractFile.getPrimaryKey()).intValue());
				return true;
			}
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (IDOAddRelationshipException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (IWBundleDoesNotExist e) {
			e.printStackTrace();
		}
		catch (EJBException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
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
			// System.out.println("Token: " + readLine);
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
				done = assignContractToApplication(Integer.parseInt(id), -1, null, null, -1, user, locale, false, false, -1, -1);
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
			String[] caseStatus = { getCaseStatusDeleted().getStatus(), getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusDenied().getStatus(), getCaseStatusReady().getStatus() };
			return getChildCareApplicationHome().findApplicationByChildAndNotInStatus(childId, caseStatus, caseCode);
		}
		catch (FinderException fe) {
			return new ArrayList(0);
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
			String[] caseStatus = { getCaseStatusDeleted().getStatus(), getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusDenied().getStatus() };
			return getChildCareApplicationHome().getNumberOfApplicationsForChildNotInStatus(childID, caseStatus, caseCode);
		}
		catch (IDOException ie) {
			return 0;
		}
	}

	public boolean hasOutstandingOffers(int childID, String caseCode) {
		int numberOfOffers = getNumberOfApplicationsForChildByStatus(childID, getCaseStatusGranted().getStatus(), caseCode);
		if (numberOfOffers > 0)
			return true;
		return false;
	}

	public ChildCareApplication getApplicationForChildAndProvider(int childID, int providerID) {
		try {
			String[] statuses = { String.valueOf(getStatusReady()), String.valueOf(getStatusParentTerminated()), String.valueOf(getStatusWaiting()) };
			return getChildCareApplicationHome().findApplicationByChildAndProviderAndStatus(childID, providerID, statuses);
		}
		catch (FinderException fe) {
			return null;
		}
	}
	
	public ChildCareApplication getApplicationForChildAndProviderinStatus(int childID, int providerID) {
		try {
			String[] statuses = { String.valueOf(getStatusReady()), String.valueOf(getStatusParentTerminated()), String.valueOf(getStatusWaiting()), String.valueOf(getStatusCancelled()) };
			return getChildCareApplicationHome().findApplicationByChildAndProviderAndStatus(childID, providerID, statuses);
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public ChildCareApplication getApplicationForChildAndProviderinStatus(int childID, int providerID, String[] statuses) {
		try {
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
			return new ArrayList(0);
		}
		catch (FinderException e) {
			e.printStackTrace();
			return new ArrayList(0);
		}
	}

	public Collection findAllPendingApplications() {
		try {
			ChildCareApplicationHome home = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);

			return home.findAllCasesByStatus(getCaseStatusPending().getStatus());
		}
		catch (RemoteException e) {
			e.printStackTrace();
			return new ArrayList(0);
		}
		catch (FinderException e) {
			e.printStackTrace();
			return new ArrayList(0);
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
				}
				catch (Exception e) {
				}
			}

			return appl;
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList(0);
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

	public void deleteOffer(int applicationID, User performer) {
		UserTransaction t = getSessionContext().getUserTransaction();
		try {
			t.begin();
			ChildCareApplication application = getChildCareApplication(applicationID);
			application.setApplicationStatus(getStatusSentIn());
			application.setOfferValidUntil(null);
			application.setHasDateSet(false);
			application.setHasPriority(false);
			changeCaseStatus(application, getCaseStatusOpen().getStatus(), performer);

			Collection contracts = getContractsByApplication(applicationID);
			Iterator iter = contracts.iterator();
			while (iter.hasNext()) {
				ChildCareContract archive = (ChildCareContract) iter.next();
				Contract contract = archive.getContract();				
				archive.remove();				
				if (contract != null) {
					contract.setStatus("T");
					contract.store();
				}
			}
			application.store();
			t.commit();
		} catch (Exception e) {
			log(e);
			try {
				t.rollback();
			} catch (SystemException se) {}
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
			if (CareConstants.CASE_CODE_KEY.equals(caseCode) || CareConstants.AFTER_SCHOOL_CASE_CODE_KEY.equals(caseCode)) {
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

	protected HashMap getTagMap(ChildCareApplication application, Locale locale, String careTime, IWTimestamp validFrom, boolean isChange) throws RemoteException {
		return getTagMap(application, locale, careTime, validFrom, isChange, "...");
	}

	protected HashMap getTagMap(ChildCareApplication application, Locale locale, IWTimestamp validFrom, boolean isChange, String emptyCareTimeValue) throws RemoteException {
		return getTagMap(application, locale, application.getCareTime(), validFrom, isChange, emptyCareTimeValue);
	}

	protected HashMap getTagMap(ChildCareApplication application, Locale locale, String careTime, IWTimestamp validFrom, boolean isChange, String emptyCareTimeValue) throws RemoteException {
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
				if (((Integer) parent.getPrimaryKey()).intValue() != ((Integer) parent1.getPrimaryKey()).intValue())
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
			PostalCode code = address.getPostalCode();
			if (code != null) {
				addressString += ", " + code.getPostalAddress();
			}
		}
		
		String addressStreetString = "-";
		if (address != null) {
			addressStreetString = address.getStreetAddress();			
		}
		
		String addressPostalString = "-";
		if (address != null) {
			PostalCode code = address.getPostalCode();
			if (code != null) {
				addressPostalString = code.getPostalAddress();
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

		peer = new XmlPeer(ElementTags.CHUNK, "preschool");
		peer.setContent(application.getPreSchool());
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "ctChange");
		if (isChange) {
			peer.setContent("x");
		}
		else
			peer.setContent("  ");
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "ctNew");
		if (!isChange) {
			peer.setContent("x");
		}
		else
			peer.setContent("  ");
		map.put(peer.getAlias(), peer);
		
		
		String careTimeTemp = "  ";
		peer = new XmlPeer(ElementTags.CHUNK, "careTimeFSKHEL");
		if (careTime != null && isChange) {
			try {
				Integer.parseInt(careTime);
			}
			catch (NumberFormatException nfe) {
					if (careTime.equals("FSKHEL"))
						careTimeTemp = "x";
					else
						careTimeTemp = "  ";
			}
			peer.setContent(String.valueOf(careTimeTemp));
		}
		else
			peer.setContent("  ");
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "careTimeFSKDEL");
		if (careTime != null && isChange) {
			try {
				Integer.parseInt(careTime);
			}
			catch (NumberFormatException nfe) {
					if (careTime.equals("FSKDEL"))
						careTimeTemp = "x";
					else
						careTimeTemp="  ";
			}
			peer.setContent(String.valueOf(careTimeTemp));
		}
		else
			peer.setContent("  ");
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "careTimeFSKDEL4-5A");
		if (careTime != null && isChange) {
			try {
				Integer.parseInt(careTime);
			}
			catch (NumberFormatException nfe) {
					if (careTime.equals("FSKDEL4-5A"))
						careTimeTemp = "x";
					else
						careTimeTemp="  ";
			}
			peer.setContent(String.valueOf(careTimeTemp));
		}
		else
			peer.setContent("  ");
		map.put(peer.getAlias(), peer);
		
		
		peer = new XmlPeer(ElementTags.CHUNK, "careTimeFSKHEL4-5");
		if (careTime != null && isChange) {
			try {
				Integer.parseInt(careTime);
			}
			catch (NumberFormatException nfe) {
					if (careTime.equals("FSKHEL4-5"))
						careTimeTemp = "x";
					else
						careTimeTemp="  ";
			}
			peer.setContent(String.valueOf(careTimeTemp));
		}
		else
			peer.setContent("  ");
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "careTime");
		if (careTime != null && !isChange) {
			try {
				Integer.parseInt(careTime);
			}
			catch (NumberFormatException nfe) {
				try {
					CareTime time = getCareTime(careTime);
					careTime = getLocalizedString(time.getLocalizedKey(), careTime);
				}
				catch (FinderException fe) {
					log(fe);
				}
			}
			peer.setContent(String.valueOf(careTime));
		}
		else
			peer.setContent(emptyCareTimeValue);
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "careTimeChange");
		if (careTime != null && isChange) {
			try {
				Integer.parseInt(careTime);
			}
			catch (NumberFormatException nfe) {
				try {
					CareTime time = getCareTime(careTime);
					careTime = getLocalizedString(time.getLocalizedKey(), careTime);
				}
				catch (FinderException fe) {
					log(fe);
				}
			}
			peer.setContent(String.valueOf(careTime));
		}
		else
			peer.setContent("....");
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "childName");
		// peer.setContent(TextSoap.convertSpecialCharactersToNumeric(child.getName()));
		peer.setContent(child.getName());
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "personalID");
		peer.setContent(PersonalIDFormatter.format(child.getPersonalID(), locale));
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "provider");
		// peer.setContent(TextSoap.convertSpecialCharactersToNumeric(provider.getSchoolName()));
		peer.setContent(provider.getSchoolName());
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "providerAddress");
		// peer.setContent(TextSoap.convertSpecialCharactersToNumeric(provider.getSchoolName()));
		peer.setContent(provider.getSchoolAddress());
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "providerPostal");
		peer.setContent(provider.getSchoolZipCode() + " " + provider.getSchoolZipArea());
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "parent1");
		// peer.setContent(TextSoap.convertSpecialCharactersToNumeric(parent1Name));
		peer.setContent(parent1Name);
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "parent2");
		// peer.setContent(TextSoap.convertSpecialCharactersToNumeric(parent2Name));
		peer.setContent(parent2Name);
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "personalID1");
		peer.setContent(parent1PersonalID);
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "personalID2");
		peer.setContent(parent2PersonalID);
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "address");
		// peer.setContent(TextSoap.convertSpecialCharactersToNumeric(addressString));
		peer.setContent(addressString);
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "postalAddress");
		// peer.setContent(TextSoap.convertSpecialCharactersToNumeric(addressString));
		peer.setContent(addressPostalString);
		map.put(peer.getAlias(), peer);
		
		peer = new XmlPeer(ElementTags.CHUNK, "streetAddress");
		// peer.setContent(TextSoap.convertSpecialCharactersToNumeric(addressString));
		peer.setContent(addressStreetString);
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "phone");
		peer.setContent(phoneString);
		map.put(peer.getAlias(), peer);

		return map;
	}

	protected HashMap getCancelFormTagMap(ChildCareApplication application, Locale locale) throws RemoteException {
		HashMap map = new HashMap();
		User child = application.getChild();
		User parent = application.getOwner();
		Address address = getUserBusiness().getUsersMainAddress(parent);
		IWTimestamp endDate = new IWTimestamp(application.getRequestedCancelDate());
		
		School provider = application.getProvider();

		String parentName = parent.getName();

		String addressString = "-";
		String postalString = "-";
		if (address != null) {
			addressString = address.getStreetAddress();
			PostalCode code = address.getPostalCode();
			if (code != null) {
				postalString = code.getPostalAddress();
			}
		}

		IWTimestamp stamp = new IWTimestamp();
		XmlPeer peer = new XmlPeer(ElementTags.CHUNK, "created");
		peer.setContent(stamp.getLocaleDate(locale, IWTimestamp.SHORT));
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "endDate");
		peer.setContent(endDate.getLocaleDate(locale, IWTimestamp.SHORT));
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

		peer = new XmlPeer(ElementTags.CHUNK, "providerAddress");
		peer.setContent(provider.getSchoolVisitAddress());
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "providerPostal");
		peer.setContent(provider.getSchoolZipCode() + " " + provider.getSchoolZipArea());
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "parent");
		peer.setContent(parentName);
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "address");
		peer.setContent(addressString);
		map.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "postal");
		peer.setContent(postalString);
		map.put(peer.getAlias(), peer);

		return map;
	}

	public String getXMLContractTxtURL(IWBundle iwb, Locale locale) {
		return iwb.getResourcesRealPath(locale) + FileUtil.getFileSeparator() + "childcare_contract_txt.xml";
	}

	public String getXMLContractPdfURL(IWBundle iwb, Locale locale) {
		return iwb.getResourcesRealPath(locale) + FileUtil.getFileSeparator() + "childcare_contract.xml";
	}

	public String getXMLCancelFormPdfURL(IWBundle iwb, Locale locale) {
		return iwb.getResourcesRealPath(locale) + FileUtil.getFileSeparator() + "childcare_cancel_form.xml";
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
		ApplicationPriorityHome home = (ApplicationPriorityHome) this.getIDOHome(ApplicationPriority.class);
		try {
			ApplicationPriority ap = home.create();
			ap.setPriorityDate(new Date(System.currentTimeMillis()));
			ap.setApplication(application);
			ap.setMessage(body);
			ap.store();
		} catch (Exception e) {
			log(e);
		}
//		getMessageBusiness().sendMessageToCommuneAdministrators(application, message, body);
	}

	public boolean hasBeenPlacedWithOtherProvider(int childID, int providerID) {
		try {
			String caseStatus[] = { getCaseStatusReady().getStatus() };
			int applications = getChildCareApplicationHome().getNumberOfPlacedApplications(childID, providerID, caseStatus);
			if (applications > 0)
				return true;
			return false;
		}
		catch (IDOException e) {
			return false;
		}
	}
	
	public boolean hasSchoolPlacement(User child) {
		try {
			return getSchoolBusiness().getSchoolClassMemberHome().getNumberOfPlacingsBySchoolCategory(child, getSchoolBusiness().getCategoryElementarySchool()) > 0;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		catch (IDOException ie) {
			log(ie);
		}
		return false;
	}

	private ChildCareContract addContractToArchive(int contractFileID, int oldArchiveID, boolean createNew, ChildCareApplication application, int contractID, Date validFrom, int employmentTypeID, int invoiceReceiverId, User user, boolean createNewStudent, int schoolTypeId, int schoolClassId, SchoolClassMember oldStudent) throws NoPlacementFoundException {
		try {
			ChildCareContract archive = null, oldArchive = null;

			int applicationID = ((Integer) application.getPrimaryKey()).intValue();
			if (contractFileID != -1) {
				if (createNew) {
					archive = getChildCareContractArchiveHome().create();
				}
				else {
					archive = getChildCareContractArchiveHome().findByContractFileID(contractFileID);
				}
			}
			
			if (archive == null) {
				archive = getChildCareContractArchiveHome().create();
			}
			archive.store();

			if (oldArchiveID > 0) {
				oldArchive = getChildCareContractArchiveHome().findByPrimaryKey(new Integer(oldArchiveID));
			}

			if (oldArchiveID != ((Integer) archive.getPrimaryKey()).intValue()) {
				archive.setChildID(application.getChildId());
				archive.setApplication(application);
				archive.setCreatedDate(new IWTimestamp().getDate());
				archive.setValidFromDate(validFrom);
				archive.setCareTime(application.getCareTime());
				if (employmentTypeID != -1) {
					archive.setEmploymentType(employmentTypeID);
				}
			}
			
			archive.setContractFileID(application.getContractFileId());
			if (contractID != -1) {
				archive.setContractID(contractID);
			}
			if (application.getRejectionDate() != null) {
				archive.setTerminatedDate(application.getRejectionDate());
			}

			if (invoiceReceiverId > 0) {
				archive.setInvoiceReceiverID(invoiceReceiverId);
			}
			else if (oldArchive != null && oldArchive.getInvoiceReceiverID() > 0 && oldArchive.getChildID() == application.getChildId()) {
				archive.setInvoiceReceiverID(oldArchive.getInvoiceReceiverID());
			}
			
			if (application.getApplicationStatus() != getStatusContract()) {
				try {
					int oldSchoolClassID = -1;
					SchoolClassMember student = null;
					if (oldStudent == null && oldArchive != null) {
						oldStudent = oldArchive.getSchoolClassMember();
					}
					if (oldStudent != null) {
						oldSchoolClassID = oldStudent.getSchoolClassId();
						if (schoolClassId > 0 && oldSchoolClassID != schoolClassId) {
							IWTimestamp endDate = new IWTimestamp(validFrom);
							endDate.addDays(-1);
							SchoolClass oldSchoolClass = oldStudent.getSchoolClass();
							getSchoolBusiness().addToSchoolClassMemberLog(oldStudent, oldSchoolClass, endDate.getDate(), user);
						}
						getSchoolBusiness().alignLogs(oldStudent);
					}
					if (oldStudent == null) {
						oldStudent = getLatestPlacement(application.getChildId(), application.getProviderId());
						oldSchoolClassID = oldStudent.getSchoolClassId();
					}
					if (createNewStudent && oldStudent != null && oldStudent.getSchoolTypeId() != schoolTypeId) {
						// end old placement with the chosen date -1 and create new
						// placement
						student = createNewPlacement(application, schoolTypeId, schoolClassId, oldStudent, new IWTimestamp(validFrom), user);
						try {
							SchoolClass schoolClass = getSchoolBusiness().getSchoolClassHome().findByPrimaryKey(new Integer(schoolClassId));
							getSchoolBusiness().addToSchoolClassMemberLog(student, schoolClass, validFrom, null, user);
							createNewStudent = false;
						}
						catch (FinderException fe) {
							log(fe);
						}
					}
					if (student == null) {
						student = oldStudent;
					}
					if (student == null) {
						student = getLatestPlacement(application.getChildId(), application.getProviderId());
					}
					if (((Integer) student.getStudent().getPrimaryKey()).intValue() == application.getChildId()) {
						archive.setSchoolClassMember(student);
						if (schoolClassId != -1) {
							student.setSchoolClassId(schoolClassId);
							student.store();
							if (oldSchoolClassID != -1 && oldSchoolClassID == schoolClassId) {
								createNewStudent = false;
							}
							if (createNewStudent) {
								try {
									SchoolClass schoolClass = getSchoolBusiness().getSchoolClassHome().findByPrimaryKey(new Integer(schoolClassId));
									getSchoolBusiness().addToSchoolClassMemberLog(student, schoolClass, validFrom, application.getRejectionDate(), user);
								}
								catch (FinderException fe) {
									log(fe);
								}
							}
							getSchoolBusiness().alignLogs(student);
						}
					}
					else {
						throw new NoPlacementFoundException("Classmember record's child id doesn't match application's child id");
					}
				}
				catch (FinderException fe) {
					throw new NoPlacementFoundException(fe);
				}
				catch (RemoteException e) {
					throw new NoPlacementFoundException(e);
				}
				catch (EJBException e) {
					throw new NoPlacementFoundException(e);
				}
			}
			
			// test also for futurecontracts if oldContract is provided
			// IWTimestamp fromDate = new IWTimestamp(validFrom);
			if (oldArchive != null && !oldArchive.equals(archive) && hasFutureContracts(applicationID)) {
				try {
					Collection futureContracts = getChildCareContractArchiveHome().findFutureContractsByApplication(applicationID, validFrom);
					IWTimestamp earliestFutureStartDate = null;
					for (Iterator iter = futureContracts.iterator(); iter.hasNext();) {
						ChildCareContract futureContract = (ChildCareContract) iter.next();
						IWTimestamp newDate = new IWTimestamp(futureContract.getValidFromDate());
						if (earliestFutureStartDate == null) {
							earliestFutureStartDate = new IWTimestamp(newDate);
						}
						else if (newDate.isEarlierThan(earliestFutureStartDate)) {
							earliestFutureStartDate = new IWTimestamp(newDate);
						}
					}
					earliestFutureStartDate.addDays(-1);
					terminateContract(archive, earliestFutureStartDate.getDate(), false);
				}
				catch (FinderException e1) {
					e1.printStackTrace();
				}
			}

			archive.store();
			if (oldArchive != null && !oldArchive.equals(archive)) {
				IWTimestamp terminationDate = new IWTimestamp(validFrom);
				terminationDate.addDays(-1);
				if (oldArchive != null) {
					terminateContract(oldArchive, terminationDate.getDate(), false);
				}
	
				if (contractFileID != -1) {
					terminateContract(contractFileID, terminationDate.getDate(), false);
				}
	
				if (contractFileID != -1) {
					Contract contract = archive.getContract();
					contract.setValidFrom(validFrom);
					contract.store();
				}
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

	public Collection getLatestContractsForChild(int childID, int maxNumberOfContracts) {
		try {
			return getChildCareContractArchiveHome().findByChild(childID, maxNumberOfContracts, -1);
		}
		catch (FinderException e) {
			return new Vector();
		}
	}

	private int terminateContract(int contractFileID, Date terminatedDate, boolean removePlacing) {

		return terminateContract(getContractFile(contractFileID), terminatedDate, removePlacing);
	}

	private int terminateContract(ChildCareContract archive, Date terminatedDate, boolean removePlacing) {

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

					if (removePlacing)
						deleteFromProvider(application.getChildId(), application.getProviderId());

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
		Object[] arguments = { choice.getChild().getFirstName(), String.valueOf(choice.getChoiceNumber()), choice.getProvider().getName() };

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
	public char getStatusWaiting() {
		return ChildCareConstants.STATUS_WAITING;
	}

	/**
	 * @return char
	 */
	public char getStatusParentTerminated() {
		return ChildCareConstants.STATUS_PARENT_TERMINATED;
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
			String[] caseStatus = { getCaseStatusDeletedString(), getCaseStatusInactiveString(), getCaseStatusCancelledString(), getCaseStatusReadyString(), getCaseStatusWaiting().getStatus(), getCaseStatusPending().getStatus()};
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

	public int getQueueTotalBeforeUpdate(int providerID) {
		try {
			IWTimestamp to = IWTimestamp.RightNow();
			to.addYears(10);
			Date toDate = to.getDate();
			return getChildCareQueueHome().getNumberInQueue(providerID, toDate);
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

			String[] caseStatus = { getCaseStatusDeleted().getStatus(), getCaseStatusInactiveString(), getCaseStatusCancelledString(), getCaseStatusReadyString(), getCaseStatusWaiting().getStatus(), getCaseStatusPending().getStatus()};

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
							if (provider.getInvisibleForCitizen()) {
								addSchool = false;
							}
							else {
								addSchool = true;
								if (currentSchool != null) {
									addSchool = !currentSchool.equals(provider);
								}
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
//Malin
	public ChildCareContract getValidContractForChild(int childID) {
		IWTimestamp stamp = new IWTimestamp();
		return getValidContractForChild(childID, stamp.getDate());
	}
	
	public ChildCareContract getValidContractForChild(int childID, Date validDate) {
		try {
			//return getChildCareContractArchiveHome().findValidContractBySchoolClassMemberID(schClassMemberID, validDate);
			return getChildCareContractArchiveHome().findValidContractByChild(childID);
		}
		catch (FinderException fe) {
			try {
				return getContractFile(getApplication(childID).getContractFileId());
			}
			catch (NullPointerException e) {
				return null;
			}
		}
	}
	
//end malin
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
			if (numberOfApplications > 0)
				return true;
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
			SchoolClassMember member = archive.getSchoolClassMember();
			int memberID = archive.getSchoolClassMemberId();
			try {
				SchoolClassMemberLog log = getSchoolBusiness().getSchoolClassMemberLogHome().findByPlacementAndDate(member, archive.getValidFromDate());
				log.setEndDate(archive.getTerminatedDate());
				log.store();
				
				member.setSchoolClassId(log.getSchoolClassID());
				member.store();
			}
			catch (FinderException fe) {
				//Nothing done...
			}

			Collection contracts = getChildCareContractArchiveHome().findFutureContractsByApplication(applicationID, date);
			Iterator iter = contracts.iterator();
			while (iter.hasNext()) {
				archive = (ChildCareContract) iter.next();
				member = archive.getSchoolClassMember();
				try {
					Contract contract = archive.getContract();
					contract.setStatus("T");
					contract.store();
				}
				catch (Exception e) {
				}
				try {
					SchoolClassMemberLog log = getSchoolBusiness().getSchoolClassMemberLogHome().findByPlacementAndDate(member, archive.getValidFromDate());
					log.remove();
				}
				catch (FinderException fe) {
					log(fe);
				}
				archive.remove();
				if (memberID != ((Integer) member.getPrimaryKey()).intValue()) {
					member.remove();
				}
				else {
					if (application.getRejectionDate() != null)
						member.setRemovedDate(new IWTimestamp(application.getRejectionDate()).getTimestamp());
					else
						member.setRemovedDate(null);
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
		}
	}

	public void removeLatestFutureContract(int applicationID, Date earliestAllowedRemoveDate, User performer) {
		UserTransaction t = getSessionContext().getUserTransaction();

		try {
			t.begin();			

			ChildCareApplication application = getApplication(applicationID);
			ChildCareContract latestContract = getLatestContractByApplication(applicationID);
			SchoolClassMember latestMember = latestContract.getSchoolClassMember();
			SchoolClassMemberLog log = getSchoolBusiness().getSchoolClassMemberLogHome().findLatestLogByUser(latestMember);
			
			IWTimestamp earliestDate = new IWTimestamp(earliestAllowedRemoveDate);
			IWTimestamp logStart = new IWTimestamp(log.getStartDate());
			IWTimestamp contractStart = new IWTimestamp(latestContract.getValidFromDate());
			
			boolean removeContract = false;
			boolean logRemoved = false;
			
			if (log != null) {
				if (logStart.isLaterThan(contractStart)) {
					// Only log needs to be removed
					if (logStart.isLaterThanOrEquals(earliestDate)) {
						log.remove();
						logRemoved = true;
					}
				} else if (logStart.equals(contractStart)) {
					// Log start equals contract start, remove both log and contract
					if (logStart.isLaterThanOrEquals(earliestDate)) {
						log.remove();
						logRemoved = true;
						removeContract = true;
					}
				} else if (contractStart.isLaterThanOrEquals(earliestDate)) {
					// Don't remove log, only contract
					removeContract = true;
				}
			} else if (contractStart.isLaterThanOrEquals(earliestDate)) {
				removeContract = true;
			}
			
			if (removeContract) {
				Contract contract = latestContract.getContract();				

				latestContract.remove();
				
				if (contract != null) {
					contract.setStatus("T");
					contract.store();
				}
				
				ChildCareContract newLatestContract = getLatestContractByApplication(applicationID);

				if (newLatestContract != null) {
					newLatestContract.setTerminatedDate(application.getRejectionDate());
					newLatestContract.store();
					if (newLatestContract.getSchoolClassMemberId() != ((Integer) latestMember.getPrimaryKey()).intValue()) {
						latestMember.remove();
						latestMember = newLatestContract.getSchoolClassMember();
					}
				} else {
					// No contracts, set application to status sent_in
					application.setContractFileId(null);
					application.setContractId(null);
					application.setCareTime(null);
					application.setRejectionDate(null);
					application.setCancelConfirmationReceived(null);
					application.setCancelMessage(null);
					application.setHasDateSet(false);
					application.setApplicationStatus(getStatusSentIn());
					application.store();
					
					changeCaseStatus(application, getCaseStatusOpen().getStatus(), performer);

					latestMember.remove();
				}
			}
			
			if (logRemoved) {
				// Remove end date on prior log
				SchoolClassMemberLog priorLog = getSchoolBusiness().getSchoolClassMemberLogHome().findLatestLogByUser(latestMember);
				if (priorLog != null) {
					priorLog.setEndDate(application.getRejectionDate());
					priorLog.store();
				}
			}			

			t.commit();			
		} catch (Exception e) {
			log(e);
			try {
				t.rollback();
			} catch (SystemException ex) {
				log(e);
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
	
	public boolean hasContractRelation(SchoolClassMember member) {
		try {
			return getChildCareContractArchiveHome().getCountBySchoolClassMember(member) > 0;
		}
		catch (IDOException ie) {
			ie.printStackTrace();
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

			if (!hasContractRelation(member)) {
				member.remove();
			}
			
			verifyApplication(null, application, member, performer, true);

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
			CareInvoiceBusiness business = (CareInvoiceBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), CareInvoiceBusiness.class);
			business.removeInvoiceRecords(contract);
		}
		catch (IBOLookupException ile) {
			log(Level.INFO, "[ChildCareBusiness] CareInvoiceBusiness is not installed");
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
				if (!getCareBusiness().hasGrantedCheck(child))
					getCheckBusiness().createGrantedCheck(child);
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
							if (hasActivePlacement(childID)) {
								length = 4;
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

	public boolean hasActivePlacement(int childID) {
		return getActivePlacement(childID) != null;
	}

	public boolean canCancelContract(int applicationID) {
		int numberOfContracts = getNumberOfContractsForApplication(applicationID);
		if (numberOfContracts > 1) {
			if (hasFutureContracts(applicationID))
				return false;
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
		if (numberOfContracts > 0)
			return true;
		return false;
	}

	public boolean hasActiveContract(int applicationID) {
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

	public int getNumberOfFutureContracts(int applicationID) {
		return getNumberOfFutureContracts(applicationID, new IWTimestamp().getDate());
	}

	public int getNumberOfFutureContracts(int applicationID, Date from) {
		try {
			return getChildCareContractArchiveHome().getFutureContractsCountByApplication(applicationID, from);
		}
		catch (IDOException e) {
			return 0;
		}
	}

	public boolean hasFutureLogs(int applicationID, Date from) {
		try {
			ChildCareContract contract = getLatestContractByApplication(applicationID);
			SchoolClassMemberLog log = getSchoolBusiness().getSchoolClassMemberLogHome().findLatestLogByUser(contract.getSchoolClassMember());
			return log.getStartDate().compareTo(from) >= 0;
		}
		catch (Exception e) {
			return false;
		}
	}

	public boolean hasUnansweredOffers(int childID, String caseCode) {
		try {
			int numberOfOffers = getChildCareApplicationHome().getNumberOfApplicationsForChild(childID, getCaseStatusGranted().getStatus(), caseCode);
			if (numberOfOffers > 0)
				return true;
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
				if (app.isActive()) {
					return app;
				}
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
			if (numberOfPlacings > 0)
				return true;
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
			if (numberOfPlacings > 0)
				return true;
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

	public ChildCareContract getLatestContractByApplication(int applicationID) {
		try {
			return getChildCareContractArchiveHome().findLatestContractByApplication(applicationID);
		}
		catch (FinderException e) {
			return null;
		}
	}

	public ChildCareContract getContractByApplicationAndDate(int applicationID, Date date) {
		try {
			return getChildCareContractArchiveHome().findContractByApplicationAndDate(applicationID, date);
		}
		catch (FinderException e) {
			return null;
		}
	}

	public Collection getCaseLogNewContracts(Timestamp fromDate, Timestamp toDate) {
		try {
			return getCaseLogsByCaseAndDatesAndStatusChange(CareConstants.CASE_CODE_KEY, fromDate, toDate, getCaseStatusContract().toString(), getCaseStatusReady().toString());
		}
		catch (FinderException e) {
			return new ArrayList();
		}
	}

	public Collection getCaseLogAlteredContracts(Timestamp fromDate, Timestamp toDate) {
		try {
			return getCaseLogsByCaseAndDatesAndStatusChange(CareConstants.CASE_CODE_KEY, fromDate, toDate, getCaseStatusReady().toString(), getCaseStatusReady().toString());
		}
		catch (FinderException e) {
			return new ArrayList();
		}
	}

	public Collection getCaseLogTerminatedContracts(Timestamp fromDate, Timestamp toDate) {
		try {
			return getCaseLogsByCaseAndDatesAndStatusChange(CareConstants.CASE_CODE_KEY, fromDate, toDate, getCaseStatusReady().toString(), getCaseStatusCancelled().toString());
		}
		catch (FinderException e) {
			return new ArrayList();
		}
	}

	public boolean importChildToProvider(int applicationID, int childID, int providerID, int groupID, String careTime, int employmentTypeID, int schoolTypeID, String comment, IWTimestamp fromDate, IWTimestamp toDate, Locale locale, User parent, User admin) throws AlreadyCreatedException {
		return importChildToProvider(applicationID, childID, providerID, groupID, careTime, employmentTypeID, schoolTypeID, comment, fromDate, toDate, locale, parent, admin, false);
	}

	public boolean importChildToProvider(int applicationID, int childID, int providerID, int groupID, String careTime, int employmentTypeID, int schoolTypeID, String comment, IWTimestamp fromDate, IWTimestamp toDate, Locale locale, User parent, User admin, boolean canCreateMultiple) throws AlreadyCreatedException {
		return importChildToProvider(applicationID, childID, providerID, groupID, careTime, employmentTypeID, schoolTypeID, comment, fromDate, toDate, locale, parent, admin, canCreateMultiple, null, null, false, null, false, null);
	}

	public boolean importChildToProvider(int applicationID, int childID, int providerID, int groupID, String careTime, int employmentTypeID, int schoolTypeID, String comment, IWTimestamp fromDate, IWTimestamp toDate, Locale locale, User parent, User admin, boolean canCreateMultiple, IWTimestamp lastReplyDate, String preSchool, boolean extraContract, String extraContractMessage, boolean extraContractOther, String extraContractOtherMessage) throws AlreadyCreatedException {
		UserTransaction t = getSessionContext().getUserTransaction();

		if (!canCreateMultiple) {
			try {
				String[] status = { String.valueOf(getStatusCancelled()), String.valueOf(getStatusReady()) };
				ChildCareApplication application = getChildCareApplicationHome().findApplicationByChildAndApplicationStatus(childID, status);
				if (application != null) {
					throw new AlreadyCreatedException();
				}
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
				if (application.getApplicationStatus() == getStatusContract())
					finalize = true;
			}

			User child = getUserBusiness().getUser(childID);

			application.setChildId(childID);
			application.setProviderId(providerID);
			application.setFromDate(fromDate.getDate());
			if (toDate != null)
				application.setRejectionDate(toDate.getDate());
			if (careTime != null)
				application.setCareTime(careTime);
			application.setOwner(parent);
			application.setChoiceNumber(1);
			application.setMessage(comment);
			GrantedCheck check = getCheckBusiness().getGrantedCheckByChild(childID);
			if (check == null) {
				int checkID = getCheckBusiness().createGrantedCheck(child);
				check = getCheckBusiness().getGrantedCheck(checkID);
			}
			application.setCheck(check);
			if (preSchool != null)
				application.setPreSchool(preSchool);
			if (lastReplyDate != null)
				application.setOfferValidUntil(lastReplyDate.getDate());
			application.setHasExtraContract(extraContract);
			if (extraContractMessage != null)
				application.setExtraContractMessage(extraContractMessage);
			application.setHasExtraContractOther(extraContractOther);
			if (extraContractOtherMessage != null)
				application.setExtraContractMessageOther(extraContractOtherMessage);

			// SchoolClassMember student = null;
			if (!isUpdate || finalize) {
				Timestamp removedDate = null;
				Date removed = null;
				if (toDate != null) {
					removedDate = toDate.getTimestamp();
					removed = toDate.getDate();
				}

				if (groupID == -1) {
					groupID = createDefaultGroup(providerID);
					if (groupID == -1)
						return false;
				}
				if (schoolTypeID == -1)
					schoolTypeID = getSchoolBusiness().getSchoolTypeIdFromSchoolClass(groupID);
				SchoolClassMember member = getSchoolBusiness().storeSchoolClassMemberCC(childID, groupID, schoolTypeID, fromDate.getTimestamp(), removedDate, ((Integer) admin.getPrimaryKey()).intValue(), comment);
				
				try {
					SchoolClass schoolClass = getSchoolBusiness().getSchoolClassHome().findByPrimaryKey(new Integer(groupID));
					getSchoolBusiness().addToSchoolClassMemberLog(member, schoolClass, fromDate.getDate(), removed, admin);
				}
				catch (FinderException fe) {
					//School class not found...
				}
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

					// contractFile.addTo(Contract.class,contractID);
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

					addContractToArchive(-1, -1, false, application, contractID, fromDate.getDate(), employmentTypeID, ((Integer) parent.getPrimaryKey()).intValue(), admin, false, -1, groupID, null);
					/*
					 * included in the addContractToArchive if (archive != null) {
					 * archive.setInvoiceReceiver(parent); archive.store(); }
					 */
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

		if (group != null) {
			return ((Integer) group.getPrimaryKey()).intValue();
		}
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
				if (application.getRejectionDate() != null)
					endDate = new IWTimestamp(application.getRejectionDate()).getTimestamp();

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
			String caseStatus[] = { getCaseStatusOpen().getStatus(), getCaseStatusContract().getStatus() };
			IWBundle iwb = getIWApplicationContext().getIWMainApplication().getBundle(getBundleIdentifier());
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

		String applicationStatus[] = { String.valueOf(getStatusSentIn()) /*
																																			 * ,
																																			 * String.valueOf(getStatusRejected())
																																			 */};
		return getChildCareApplicationHome().findAllByAreaAndApplicationStatus(area, applicationStatus, caseCode, months.getDate(), weeks.getDate(), firstHandOnly);
	}

	public Collection findRejectedApplicationsByChild(int childID) throws FinderException {
		String applicationStatus[] = { String.valueOf(getStatusRejected()) };
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
		else if (status == ChildCareConstants.STATUS_TIMED_OUT) {
			return getLocalizedString("child_care.status_timed_out", "Timed out");
		}

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
		else if (status == ChildCareConstants.STATUS_TIMED_OUT) {
			return getLocalizedString("child_care.status_timed_out_abbr", "Timed out");
		}

		return "";
	}

	public boolean wasRejectedByParent(ChildCareApplication application) {
		return hasStatusChange(application, getCaseStatusGranted().getStatus(), getCaseStatusInactive().getStatus());
	}

	/**
	 * Checks if the schoolclass belongs to schooltype
	 */
	public boolean isSchoolClassBelongingToSchooltype(int schoolClassId, int schoolTypeId) {
		try {
			SchoolClass schoolClass = getSchoolBusiness().getSchoolClassHome().findByPrimaryKey(new Integer(schoolClassId));
			return schoolClass.getSchoolTypeId() == schoolTypeId;
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Checks if a change only in schooltype but not group of the archive's
	 * classmember
	 */
	public boolean isTryingToChangeSchoolTypeButNotSchoolClass(int currentArchiveID, int schoolTypeId, int schoolClassId) {
		try {
			ChildCareContract currentArchive = getChildCareContractArchiveHome().findByPrimaryKey(new Integer(currentArchiveID));
			if (currentArchive != null) {
				SchoolClassMember currentClassMember = currentArchive.getSchoolClassMember();
				return currentClassMember != null && currentClassMember.getSchoolTypeId() != schoolTypeId && currentClassMember.getSchoolClassId() == schoolClassId;
			}
		}
		catch (FinderException e) {
		}
		return false;
	}

	private String getSQL() {
//		return "" + " select sch.sch_school_id,sch.school_name," + " pr.comm_childcare_prognosis_id,pr.UPDATED_DATE,pr.THREE_MONTHS_PROGNOSIS,pr.ONE_YEAR_PROGNOSIS," + " pr.THREE_MONTHS_PRIORITY,pr.ONE_YEAR_PRIORITY,pr.PROVIDER_CAPACITY," + " count(c.child_id) " + " from comm_childcare c , proc_case p ,sch_school sch, comm_childcare_prognosis pr,sch_school_sch_school_type m,sch_school_type st,ic_commune comm" + " WHERE c.COMM_CHILDCARE_ID=p.proc_case_id " + " and c.provider_id = sch.sch_school_id" + " and sch.commune = comm.ic_commune_id" + " and pr.provider_id = sch.sch_school_id" + " and m.sch_school_id = sch.sch_school_id" + " and m.sch_school_type_id = st.sch_school_type_id" + " and comm.default_commune = 'Y'" + " and st.school_category = 'CHILD_CARE'" + " AND p.case_status NOT IN ('DELE', 'TYST', 'UPPS', 'KLAR')" + " group by sch.sch_school_id,sch.school_name," + " pr.comm_childcare_prognosis_id,pr.UPDATED_DATE,pr.THREE_MONTHS_PROGNOSIS,pr.ONE_YEAR_PROGNOSIS," + " pr.THREE_MONTHS_PRIORITY,pr.ONE_YEAR_PRIORITY,pr.PROVIDER_CAPACITY";
		
	//	return "select sch.sch_school_id,sch.school_name, pr.comm_childcare_prognosis_id,pr.UPDATED_DATE,pr.THREE_MONTHS_PROGNOSIS,pr.ONE_YEAR_PROGNOSIS, pr.THREE_MONTHS_PRIORITY,pr.ONE_YEAR_PRIORITY,pr.PROVIDER_CAPACITY, count(c.child_id), pr.VACANCIES from  sch_school_sch_school_type m,sch_school_type st,ic_commune comm, sch_school sch left join comm_childcare_prognosis pr on sch.sch_school_id=pr.provider_id left join comm_childcare c on sch.sch_school_id=c.provider_id where sch.commune = comm.ic_commune_id and m.sch_school_id = sch.sch_school_id and m.sch_school_type_id = st.sch_school_type_id and comm.default_commune = 'Y' and st.school_category = 'CHILD_CARE' group by sch.sch_school_id,sch.school_name, pr.comm_childcare_prognosis_id,pr.UPDATED_DATE,pr.THREE_MONTHS_PROGNOSIS,pr.ONE_YEAR_PROGNOSIS, pr.THREE_MONTHS_PRIORITY,pr.ONE_YEAR_PRIORITY,pr.PROVIDER_CAPACITY,pr.VACANCIES";
////return "select sch.sch_school_id,sch.school_name, pr.comm_childcare_prognosis_id,pr.UPDATED_DATE,pr.THREE_MONTHS_PROGNOSIS,pr.ONE_YEAR_PROGNOSIS, pr.THREE_MONTHS_PRIORITY,pr.ONE_YEAR_PRIORITY,pr.PROVIDER_CAPACITY, count(c.child_id), pr.VACANCIES from  sch_school_sch_school_type m,sch_school_type st,ic_commune comm, sch_school sch left join comm_childcare_prognosis pr on sch.sch_school_id=pr.provider_id left join comm_childcare c on sch.sch_school_id=c.provider_id left join proc_case p on c.COMM_CHILDCARE_ID=p.proc_case_id where sch.commune = comm.ic_commune_id and m.sch_school_id = sch.sch_school_id and m.sch_school_type_id = st.sch_school_type_id and comm.default_commune = 'Y' and st.school_category = 'CHILD_CARE' AND (p.case_status is null or p.case_status NOT IN ('DELE', 'TYST', 'UPPS', 'KLAR')) group by sch.sch_school_id,sch.school_name, pr.comm_childcare_prognosis_id,pr.UPDATED_DATE,pr.THREE_MONTHS_PROGNOSIS,pr.ONE_YEAR_PROGNOSIS, pr.THREE_MONTHS_PRIORITY,pr.ONE_YEAR_PRIORITY,pr.PROVIDER_CAPACITY,pr.VACANCIES"; 050329
		return "select sch.sch_school_id,sch.school_name, pr.comm_childcare_prognosis_id,pr.UPDATED_DATE,pr.THREE_MONTHS_PROGNOSIS,pr.ONE_YEAR_PROGNOSIS, pr.THREE_MONTHS_PRIORITY,pr.ONE_YEAR_PRIORITY,pr.PROVIDER_CAPACITY, count(c.child_id), pr.VACANCIES from  sch_school_sch_school_type m,sch_school_type st,ic_commune comm, sch_school sch left join comm_childcare_prognosis pr on sch.sch_school_id=pr.provider_id left join comm_childcare c on sch.sch_school_id=c.provider_id left join proc_case p on c.COMM_CHILDCARE_ID=p.proc_case_id where sch.commune = comm.ic_commune_id and m.sch_school_id = sch.sch_school_id and m.sch_school_type_id = st.sch_school_type_id and (st.IS_FREETIME_TYPE is null or st.IS_FREETIME_TYPE = 'N') and comm.default_commune = 'Y' and st.school_category = 'CHILD_CARE' AND (p.case_status is null or p.case_status NOT IN ('DELE', 'TYST', 'UPPS', 'KLAR')) group by sch.sch_school_id,sch.school_name, pr.comm_childcare_prognosis_id,pr.UPDATED_DATE,pr.THREE_MONTHS_PROGNOSIS,pr.ONE_YEAR_PROGNOSIS, pr.THREE_MONTHS_PRIORITY,pr.ONE_YEAR_PRIORITY,pr.PROVIDER_CAPACITY,pr.VACANCIES";
	}
	
	private String getSQLProviders() {
		return "select sch.sch_school_id,sch.school_name, pr.comm_childcare_prognosis_id,pr.UPDATED_DATE,pr.THREE_MONTHS_PROGNOSIS,pr.ONE_YEAR_PROGNOSIS, pr.THREE_MONTHS_PRIORITY,pr.ONE_YEAR_PRIORITY,pr.PROVIDER_CAPACITY, count(c.child_id), pr.VACANCIES from  sch_school_sch_school_type m,sch_school_type st,ic_commune comm, sch_school sch left join comm_childcare_prognosis pr on sch.sch_school_id=pr.provider_id left join comm_childcare c on sch.sch_school_id=c.provider_id where sch.commune = comm.ic_commune_id and m.sch_school_id = sch.sch_school_id and m.sch_school_type_id = st.sch_school_type_id and comm.default_commune = 'Y' and st.school_category = 'CHILD_CARE' group by sch.sch_school_id,sch.school_name, pr.comm_childcare_prognosis_id,pr.UPDATED_DATE,pr.THREE_MONTHS_PROGNOSIS,pr.ONE_YEAR_PROGNOSIS, pr.THREE_MONTHS_PRIORITY,pr.ONE_YEAR_PRIORITY,pr.PROVIDER_CAPACITY,pr.VACANCIES";
	}

	/**
	 * Returns a collection of ProviderStat objects
	 * 
	 * @return
	 * @throws FinderException
	 */
	public Collection getProviderStats(Locale sortLocale) throws FinderException {
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
				bean.setVacancies(new Integer(RS.getInt(11)));
				vector.add(bean);
			}
			RS.close();

		}
		catch (SQLException sqle) {
			throw new FinderException(sqle.getMessage());
		}
		finally {
			if (Stmt != null) {
				try {
					Stmt.close();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				ConnectionBroker.freeConnection(conn);
			}
		}
		Collections.sort(vector, new ProviderStatComparator(sortLocale));
		return vector;

	}
	
	public Collection getProviderStatsBeforeUpdate(Locale sortLocale) throws FinderException {
		Connection conn = null;
		PreparedStatement Stmt = null;
		Vector vector = new Vector();
		try {
			conn = ConnectionBroker.getConnection();
			String s = getSQLProviders();
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
				bean.setVacancies(new Integer(RS.getInt(11)));
				vector.add(bean);
			}
			RS.close();

		}
		catch (SQLException sqle) {
			throw new FinderException(sqle.getMessage());
		}
		finally {
			if (Stmt != null) {
				try {
					Stmt.close();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				ConnectionBroker.freeConnection(conn);
			}
		}
		Collections.sort(vector, new ProviderStatComparator(sortLocale));
		return vector;

	}
	
	private SchoolClassMemberLog getLatestPlacementLogByContract(ChildCareContract contract) {
		try {
			return getSchoolBusiness().getSchoolClassMemberLogHome().findLatestLogByUser(contract.getSchoolClassMember());
		}
		catch (FinderException fe) {
			return null;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public PlacementHelper getPlacementHelper(Integer applicationID) {
		ChildCareApplication application = getApplication(applicationID.intValue());
		ChildCareContract contract = getLatestContractByApplication(applicationID.intValue());
		SchoolClassMemberLog log = null;
		if (contract != null)
			log = getLatestPlacementLogByContract(contract);
		IWBundle bundle = getIWApplicationContext().getIWMainApplication().getBundle(getBundleIdentifier());
		String className = bundle.getProperty(PLACEMENT_HELPER, DefaultPlacementHelper.class.getName());
		PlacementHelper helper = null;
		if (className != null) {
			try {
				helper = (PlacementHelper) Class.forName(className).newInstance();
			}
			catch (Exception e) {
				helper = new DefaultPlacementHelper();
			}
		}
		else {
			helper = new DefaultPlacementHelper();
		}
		helper.setApplication(application);
		helper.setContract(contract);
		if (contract != null)
			helper.setPlacementLog(log);
		return helper;
	}

	public boolean setUserAsDeceased(Integer userID, java.util.Date deceasedDate) throws RemoteException {
		// Remove the deceased user as invoice receiver for
		try {
			ChildCareContractHome ccch = (ChildCareContractHome) getIDOHome(ChildCareContract.class);
			Collection activeOrFutureContracts = ccch.findByInvoiceReceiverActiveOrFuture(userID, new java.sql.Date(deceasedDate.getTime()));
			for (Iterator iter = activeOrFutureContracts.iterator(); iter.hasNext();) {
				ChildCareContract contract = (ChildCareContract) iter.next();
				contract.setInvoiceReceiverID(null);
				contract.store();
			}
		}
		catch (IDOStoreException e1) {
			logError("Invoice reciver could not be set as null for deceased user " + userID);
			return false;
		}
		catch (FinderException e1) {
			return false;
		}
		return true;
	}

	protected CareBusiness getCareBusiness() {
		try {
			return (CareBusiness) this.getServiceInstance(CareBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	public void deleteApplication(int applicationID, User user, Locale locale) {
		try {
			ChildCareApplication application = getChildCareApplicationHome().findByPrimaryKey(new Integer(applicationID));
			IWTimestamp now = new IWTimestamp();
			application.setRejectionDate(now.getDate());
			application.setApplicationStatus(ChildCareConstants.STATUS_DELETED);
			changeCaseStatus(application, getCaseStatusDeleted().getStatus(), user);
			
			String subject = getLocalizedString("child_care.application_deleted_school_subject", "Application removed from queue", locale);
			String body = getLocalizedString("child_care.application_deleted_school_body", "Your queue placement for {0} at {1} has been removed because the child doesn't qualify for a child care placement any longer.", locale);
			
			sendMessageToParents(application, subject, body);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public boolean rejectApplication(ChildCareApplication application, String subject, String message, User user) {
		UserTransaction t = getSessionContext().getUserTransaction();
		try {
			t.begin();
			CaseBusiness caseBiz = (CaseBusiness) getServiceInstance(CaseBusiness.class);
			IWTimestamp now = new IWTimestamp();
			application.setRejectionDate(now.getDate());
			application.setApplicationStatus(ChildCareConstants.STATUS_DENIED);
			caseBiz.changeCaseStatus(application, getCaseStatusDenied().getStatus(), user);
			sendMessageToParents(application, subject, message);

			if (isAfterSchoolApplication(application)) {
				Iterator iter = application.getChildrenIterator();
				if (iter != null) {
					while (iter.hasNext()) {
						Case element = (Case) iter.next();
						if (isAfterSchoolApplication(element) && element.getCaseStatus().equals(getCaseStatusInactive())) {
							application = getApplication(((Integer) element.getPrimaryKey()).intValue());
							application.setApplicationStatus(ChildCareConstants.STATUS_SENT_IN);
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
		sendMessageToParents(application, subject, body, body, alwaysSendLetter, true);
	}

	public void sendMessageToParents(ChildCareApplication application, String subject, String body, String letterBody, boolean alwaysSendLetter) {
		sendMessageToParents(application, subject, body, letterBody, alwaysSendLetter, true);
	}

	public void sendMessageToParents(ChildCareApplication application, String subject, String body, String letterBody, boolean alwaysSendLetter, boolean sendToOtherParent) {
		try {
			User child = application.getChild();
			Object[] arguments = { new Name(child.getFirstName(), child.getMiddleName(), child.getLastName()).getName(getIWApplicationContext().getApplicationSettings().getDefaultLocale(), true), application.getProvider().getSchoolName(), PersonalIDFormatter.format(child.getPersonalID(), getIWApplicationContext().getApplicationSettings().getDefaultLocale()), application.getLastReplyDate() != null ? new IWTimestamp(application.getLastReplyDate()).getLocaleDate(getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT) : "xxx", application.getOfferValidUntil() != null ? new IWTimestamp(application.getOfferValidUntil()).getLocaleDate(getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT) : "" };

			User appParent = application.getOwner();
			if (getUserBusiness().getMemberFamilyLogic().isChildInCustodyOf(child, appParent)) {
				Message message = getMessageBusiness().createUserMessage(application, appParent, subject, MessageFormat.format(body, arguments), MessageFormat.format(letterBody, arguments), true, alwaysSendLetter);
				message.setParentCase(application);
				message.store();
			}
			else {
				sendToOtherParent = true;
			}
			
			if (sendToOtherParent) {
				try {
					Collection parents = getUserBusiness().getMemberFamilyLogic().getCustodiansFor(child);
					Iterator iter = parents.iterator();
					while (iter.hasNext()) {
						User parent = (User) iter.next();
						if (!parent.equals(appParent)) {
//						if (!getUserBusiness().haveSameAddress(parent, appParent)) {
							boolean sendLetter = !getUserBusiness().haveSameAddress(parent, appParent);
							getMessageBusiness().createUserMessage(application, parent, subject, MessageFormat.format(body, arguments), MessageFormat.format(letterBody, arguments), sendLetter, alwaysSendLetter);
//						}
						}
					}
				}
				catch (NoCustodianFound ncf) {
					ncf.printStackTrace();
				}
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
	}

	public boolean isAfterSchoolApplication(Case application) {
		if (application.getCode().equals(CareConstants.AFTER_SCHOOL_CASE_CODE_KEY))
			return true;
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

	/**
	 * Method getFirstProviderForUser. If there is no school that the user then
	 * the method throws a FinderException.
	 * 
	 * @param user
	 *          a user
	 * @return School that is the first school that the user is a manager for.
	 * @throws javax.ejb.FinderException
	 *           if ther is no school that the user manages.
	 */
	public School getFirstProviderForUser(User user) throws FinderException, RemoteException {
		SchoolBusiness schoolBusiness = getSchoolBusiness();

		try {
			Group rootGroup = schoolBusiness.getRootProviderAdministratorGroup();
			// if user is a SchoolAdministrator
			if (user.hasRelationTo(rootGroup)) {
				Collection schools = getSchoolHome().findAllBySchoolGroup(user);
				if (!schools.isEmpty()) {
					Iterator iter = schools.iterator();
					while (iter.hasNext()) {
						School school = (School) iter.next();
						return school;
					}
				}
			}
		}
		catch (CreateException e) {
			e.printStackTrace();
		}

		throw new FinderException("No school found that " + user.getName() + " manages");
	}

	public SchoolHome getSchoolHome() throws java.rmi.RemoteException {
		return getSchoolBusiness().getSchoolHome();
	}

	public Map getSchoolTypeClassMap(Collection schoolTypes, int schoolID, int seasonID, Boolean showSubGroups, Boolean showNonSeasonGroups, String noSchoolClassFoundEntry) {
		try {
			SortedMap typeMap = new TreeMap(new SchoolTypeComparator());
			if (schoolTypes != null) {
				Map groupMap = null;
				SchoolType schoolType = null;
				SchoolBusiness sb = getSchoolBusiness();
				Collection groups = null;
				SchoolClass group = null;

				Iterator iter = schoolTypes.iterator();
				while (iter.hasNext()) {
					groupMap = new HashMap();

					schoolType = (SchoolType) iter.next();
					groups = sb.findSchoolClassesBySchoolAndSchoolTypeAndSeason(schoolID, ((Integer) schoolType.getPrimaryKey()).intValue(), seasonID, showSubGroups, showNonSeasonGroups);
					if (groups != null && groups.size() > 0) {
						Iterator iterator = groups.iterator();
						while (iterator.hasNext()) {
							group = (SchoolClass) iterator.next();
							groupMap.put(group, group);
						}
					}
					else {
						groupMap.put("-1", noSchoolClassFoundEntry);
					}
					typeMap.put(schoolType, groupMap);
				}
			}
			return typeMap;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

	private CareTimeHome getCareTimeHome() {
		try {
			return (CareTimeHome) IDOLookup.getHome(CareTime.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public Collection getCareTimes() {
		try {
			return getCareTimeHome().findAll();
		}
		catch (FinderException fe) {
			return new ArrayList();
		}
	}

	public Collection getCareTimes(User child) {	
		Collection all = getCareTimes();
		IWTimestamp childDate = new IWTimestamp(child.getDateOfBirth());
		childDate.setAsDate();
		IWTimestamp today = IWTimestamp.RightNow();
		today.setAsDate();
		List list = new ArrayList();
		Iterator iter = all.iterator();
		while (iter.hasNext()) {
			CareTime ct = (CareTime) iter.next();
			// Hard coded values for Sollentuna
			if (ct.getCode().equals(CareTimeBMPBean.CODE_FSKHEL)) {
				int childYears = today.getYear() - childDate.getYear();
				if (today.getMonth() > 8) {
					childYears++;
				} else if (today.getMonth() == 8 && today.getDay() >= 14) {
					childYears++;
				}
				if (childYears < 4) {
					list.add(ct);
				}				
			} else if (ct.getCode().equals(CareTimeBMPBean.CODE_FSKHEL4_5)) {
				int childYears = today.getYear() - childDate.getYear();
				if (today.getMonth() > 8) {
					childYears++;
				} else if (today.getMonth() == 8 && today.getDay() >= 15) {
					childYears++;
				}
				if (childYears >= 4) {
					list.add(ct);
				}				
			} else if (ct.getCode().equals(CareTimeBMPBean.CODE_FSKDEL)) {
				list.add(ct);
			} else if (ct.getCode().equals(CareTimeBMPBean.CODE_FSKDEL4_5)) {				
				int childYears = today.getYear() - childDate.getYear();
				if (today.getMonth() > 8) {
					childYears++;
				} else if (today.getMonth() == 8 && today.getDay() >= 15) {
					childYears++;
				}
				if (childYears >= 4) {
					list.add(ct);
				}				
			}
		}
		return list;
	}

	public CareTime getCareTime(String careTime) throws FinderException {
		return getCareTimeHome().findByPrimaryKey(careTime);
	}
	
	public boolean getUseVacancies(){
		IWBundle bundle = getIWApplicationContext().getIWMainApplication().getBundle(getBundleIdentifier());
		boolean useVacancies = bundle.getBooleanProperty(PROPERTY_USE_VACANCIES, false);
		
		return useVacancies;
	}
	
	public boolean getUseEmployment(){
		IWBundle bundle = getIWApplicationContext().getIWMainApplication().getBundle(getBundleIdentifier());
		boolean useEmployment = bundle.getBooleanProperty(PROPERTY_USE_EMPLOYMENT, true);
		
		return useEmployment;
	}
	
	public boolean getUseParental(){
		IWBundle bundle = getIWApplicationContext().getIWMainApplication().getBundle(getBundleIdentifier());
		boolean useParental = bundle.getBooleanProperty(PROPERTY_USE_PARENTAL, true);
		
		return useParental;
	}
	
	public boolean getUsePreschoolLine(){
		IWBundle bundle = getIWApplicationContext().getIWMainApplication().getBundle(getBundleIdentifier());
		boolean usePreschoolLine = bundle.getBooleanProperty(PROPERTY_USE_PRESCHOOL_LINE, true);
		
		return usePreschoolLine;
	}

	public Map getCaseParameters(Case theCase) {
		ChildCareApplication application = (ChildCareApplication) theCase;
		
		Map map = new HashMap();
		map.put("cc_user_id", String.valueOf(application.getChildId()));
		
		return null;
	}
	
	public Class getEventListener() {
		return ChildCareEventListener.class;
	}
}