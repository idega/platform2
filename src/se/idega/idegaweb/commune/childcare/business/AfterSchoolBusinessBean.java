/*
 * Created on Sep 15, 2003
 */
package se.idega.idegaweb.commune.childcare.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import se.idega.idegaweb.commune.care.business.PlacementHelper;
import se.idega.idegaweb.commune.care.data.AfterSchoolChoice;
import se.idega.idegaweb.commune.care.data.AfterSchoolChoiceHome;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.AfterSchoolCareDays;
import se.idega.idegaweb.commune.childcare.data.AfterSchoolCareDaysHome;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessage;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessageHome;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBORuntimeException;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDOStoreException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * AfterSchoolBusinessBean
 * 
 * @author aron
 * @version 1.0
 */
public class AfterSchoolBusinessBean extends ChildCareBusinessBean implements ChildCareBusiness, AfterSchoolBusiness {

	private AfterSchoolChoiceHome getAfterSchoolChoiceHome() {
		try {
			return (AfterSchoolChoiceHome) IDOLookup.getHome(AfterSchoolChoice.class);
		}
		catch (IDOLookupException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	private AfterSchoolCareDaysHome getAfterSchoolCareDaysHome() {
		try {
			return (AfterSchoolCareDaysHome) IDOLookup.getHome(AfterSchoolCareDays.class);
		}
		catch (IDOLookupException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	public AfterSchoolChoice getAfterSchoolChoice(Object afterSchoolChoiceID) throws FinderException {
		return getAfterSchoolChoiceHome().findByPrimaryKey(afterSchoolChoiceID);
	}

	public Collection findChoicesByProvider(int providerID) {
		try {
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(),
					getCaseStatusDenied().getStatus(), getCaseStatusReady().getStatus(), getCaseStatusDeleted().getStatus() };
			return getAfterSchoolChoiceHome().findAllCasesByProviderAndNotInStatus(providerID, caseStatus);
		}
		catch (FinderException e) {
			return new ArrayList();
		}
	}

	public Collection findChoicesByProvider(int providerID, String sorting) {
		try {
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(),
					getCaseStatusDenied().getStatus(), getCaseStatusReady().getStatus(), getCaseStatusDeleted().getStatus() };
			return getAfterSchoolChoiceHome().findAllCasesByProviderAndNotInStatus(providerID, caseStatus, sorting);
		}
		catch (FinderException e) {
			return new ArrayList();
		}
	}

	public AfterSchoolChoice findChoicesByChildAndChoiceNumberAndSeason(Integer childID, int choiceNumber,
			Integer seasonID) throws FinderException {
		String[] caseStatus = { getCaseStatusPreliminary().getStatus(), getCaseStatusInactive().getStatus() };
		return getAfterSchoolChoiceHome().findByChildAndChoiceNumberAndSeason(childID, new Integer(choiceNumber), seasonID,
				caseStatus);
	}

	public boolean acceptAfterSchoolChoice(Object afterSchoolChoiceID, User performer) {
		AfterSchoolChoice choice = null;
		try {
			choice = getAfterSchoolChoice(afterSchoolChoiceID);
		}
		catch (FinderException fe) {
			return false;
		}
		changeCaseStatus(choice, getCaseStatusGranted().getStatus(), performer);
		return true;
	}

	public boolean denyAfterSchoolChoice(Object afterSchoolChoiceID, User performer) {
		UserTransaction transaction = getSessionContext().getUserTransaction();
		try {
			transaction.begin();
			AfterSchoolChoice choice = getAfterSchoolChoice(afterSchoolChoiceID);
			changeCaseStatus(choice, getCaseStatusDenied().getStatus(), performer);
			Iterator children = choice.getChildrenIterator();
			if (children != null) {
				while (children.hasNext()) {
					choice = (AfterSchoolChoice) children.next();
					changeCaseStatus(choice, getCaseStatusPreliminary().getStatus(), performer);
				}
			}
			transaction.commit();
		}
		catch (Exception e) {
			try {
				transaction.rollback();
			}
			catch (SystemException se) {
				throw new IBORuntimeException(se.getMessage());
			}
			throw new IBORuntimeException(e.getMessage());
		}
		return true;
	}

	public AfterSchoolChoice createAfterSchoolChoice(IWTimestamp stamp, User user, Integer childID, Integer providerID,
			Integer choiceNumber, String message, CaseStatus caseStatus, Case parentCase, Date placementDate,
			SchoolSeason season, String subject, String body) throws CreateException, RemoteException {
		if (season == null) {
			try {
				season = getCareBusiness().getCurrentSeason();
			}
			catch (FinderException fex) {
				season = null;
			}
		}
		AfterSchoolChoice choice = null;
		if (season != null) {
			try {
				choice = findChoicesByChildAndChoiceNumberAndSeason(childID, choiceNumber.intValue(),
						(Integer) season.getPrimaryKey());
			}
			catch (FinderException fex) {
				choice = null;
			}
		}
		if (choice == null) {
			AfterSchoolChoiceHome home = this.getAfterSchoolChoiceHome();
			choice = home.create();
		}
		choice.setOwner(user);
		choice.setChildId(childID.intValue());
		if (providerID != null)
			choice.setProviderId(providerID.intValue());
		choice.setChoiceNumber(choiceNumber.intValue());
		choice.setMessage(message);
		if (season != null) {
			Integer seasonId = (Integer) season.getPrimaryKey();
			choice.setSchoolSeasonId(seasonId.intValue());
		}
		if (placementDate != null)
			choice.setFromDate(placementDate);
		choice.setQueueDate(stamp.getDate());
		stamp.addSeconds(1 - choiceNumber.intValue());
		choice.setCreated(stamp.getTimestamp());
		choice.setCaseStatus(caseStatus);
		choice.setApplicationStatus(getStatusSentIn());
		choice.setHasPriority(true);
		if (caseStatus.equals(getCaseStatusPreliminary())) {
			sendMessageToParents(choice, subject, body);
		}
		if (parentCase != null)
			choice.setParentCase(parentCase);
		try {
			choice.store();
		}
		catch (IDOStoreException idos) {
			idos.printStackTrace();
			throw new IDOCreateException(idos);
		}
		return choice;
	}

	public List createAfterSchoolChoices(User user, Integer childId, Integer[] providerIDs, String message,
			String[] placementDates, SchoolSeason season, String subject, String body) throws IDOCreateException {
		int caseCount = 3;
		IWTimestamp stamp;
		List returnList = new Vector(3);
		javax.transaction.UserTransaction trans = this.getSessionContext().getUserTransaction();
		try {
			trans.begin();
			CaseStatus first = super.getCaseStatusPreliminary();
			CaseStatus other = super.getCaseStatusInactive();
			CaseStatus status = null;
			boolean firstIsFamilyFreetime = false;
			AfterSchoolChoice choice = null;
			IWTimestamp timeNow = new IWTimestamp();
			for (int i = 0; i < caseCount; i++) {
				if (providerIDs[i] != null && providerIDs[i].intValue() > 0) {
					stamp = new IWTimestamp(placementDates[i]);
					if (i == 0) {
						status = first;
						firstIsFamilyFreetime = isFamilyFreetime(providerIDs[i]);
					}
					else {
						if (i == 1 && firstIsFamilyFreetime) {
							status = first;
						}
						else {
							status = other;
						}
					}
					choice = createAfterSchoolChoice(timeNow, user, childId, providerIDs[i], new Integer(i + 1), message, status,
							choice, stamp.getDate(), season, subject, body);
					returnList.add(choice);
				}
			}
			trans.commit();
			return returnList;
		}
		catch (Exception ex) {
			try {
				trans.rollback();
			}
			catch (javax.transaction.SystemException e) {
				throw new IDOCreateException(e.getMessage());
			}
			ex.printStackTrace();
			throw new IDOCreateException(ex);
		}
	}

	private boolean isFamilyFreetime(Integer providerID) {
		try {
			School provider = getSchoolBusiness().getSchool(providerID);
			Collection types = provider.findRelatedSchoolTypes();
			Iterator iter = types.iterator();
			while (iter.hasNext()) {
				SchoolType element = (SchoolType) iter.next();
				if (element.getIsFamilyFreetimeType()) {
					return true;
				}
			}
		}
		catch (RemoteException re) {
			log(re);
		}
		catch (IDORelationshipException ire) {
			log(ire);
		}
		return false;
	}

	public Collection createContractsForChildrenWithSchoolPlacement(int providerId, User user, Locale locale) {
		Collection users = new ArrayList();
		Collection applications = findChoicesByProvider(providerId, "c.QUEUE_DATE");
		Iterator iter = applications.iterator();
		while (iter.hasNext()) {
			ChildCareApplication application = (ChildCareApplication) iter.next();
			boolean hasSchoolPlacement = false;
			try {
				hasSchoolPlacement = getSchoolBusiness().hasActivePlacement(application.getChildId(), providerId,
						getSchoolBusiness().getCategoryElementarySchool());
			}
			catch (RemoteException e) {
			}
			if (hasSchoolPlacement && (application.getApplicationStatus() == getStatusSentIn())) {
				Date date = application.getFromDate();
				try {
					PlacementHelper helper = getPlacementHelper((Integer) application.getPrimaryKey());
					java.util.Date earliestDate = helper.getEarliestPlacementDate();
					if (earliestDate != null) {
						date = new Date(earliestDate.getTime());
					}
				}
				catch (Exception e) {
					log(e);
				}
				if (application.getFromDate() == null) {
					continue;
				}
				else {
					if (application.getFromDate().getTime() > date.getTime()) {
						date = application.getFromDate();
					}
				}
				if (date != null) {
					application.setFromDate(date);
					application.setHasDateSet(true);
					application.store();
				}
				else {
					continue;
				}
				ICFile contractFile = assignContractToApplication(((Integer) application.getPrimaryKey()).intValue(), -1, null, null, -1, user,
						locale, true);
				if (contractFile != null) {
					try {
						PrintedLetterMessage message = ((PrintedLetterMessageHome) IDOLookup.getHome(PrintedLetterMessage.class)).create();
						message.setOwner(application.getOwner());
						message.setParentCase(application);
						message.setSubject(getLocalizedString("after_school_care_contract_pdf", "After school care contract"));
						message.setBody("");
						message.setMessageData(contractFile);
						message.store();
						getMessageBusiness().flagMessageAsPrinted(user, message);
					}
					catch (CreateException ce) {
						ce.printStackTrace();
					}
					catch (IDOLookupException ile) {
						ile.printStackTrace();
					}
					catch (RemoteException re) {
						re.printStackTrace();
					}
					users.add(application.getChild());
				}
			}
		}
		return users;
	}
	
	public void storeDays(ChildCareApplication application, int[] dayOfWeek, String[] timeOfDeparture, boolean[] pickedUp) {
		try {
			System.out.println(timeOfDeparture);
			for (int a = 0; a < dayOfWeek.length; a++) {
				if (timeOfDeparture[a] != "-1") {
					AfterSchoolCareDays days = getAfterSchoolCareDaysHome().create();
					days.setApplication(application);
					days.setDayOfWeek(dayOfWeek[a]);
					days.setPickedUp(pickedUp[a]);
					
					IWTimestamp stamp = new IWTimestamp(timeOfDeparture[a]);
					stamp.setAsTime();
					days.setTimeOfDeparture(stamp.getTime());
					days.store();
				}
			}
		}
		catch (CreateException ce) {
			ce.printStackTrace();
		}
	}

	public SchoolClass getDefaultGroup(Object schoolPK, Object seasonPK) {
		try {
			School school = getSchoolBusiness().getSchool(schoolPK);
			SchoolSeason season = getSchoolBusiness().getSchoolSeason(seasonPK);
			return getDefaultGroup(school, season);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public SchoolClass getDefaultGroup(School school, SchoolSeason season) {
		try {
			try {
				Collection groups = getSchoolBusiness().getSchoolClassHome().findBySchoolAndSeason(school, season);
				if (!groups.isEmpty()) {
					Iterator iter = groups.iterator();
					while (iter.hasNext()) {
						return (SchoolClass) iter.next();
					}
				}
				throw new FinderException();
			}
			catch (FinderException fe) {
				try {
					SchoolClass group = getSchoolBusiness().getSchoolClassHome().create();
					group.setSchool(school);
					group.setSchoolSeason(season);
					group.setValid(true);
					group.setSchoolClassName(season.getName());
					group.store();
					
					return group;
				}
				catch (CreateException ce) {
					throw new IBORuntimeException(ce);
				}
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public boolean storeAfterSchoolCare(IWTimestamp stamp, User user, User child, School provider, String message, SchoolSeason season, int[] days, String[] timeOfDeparture, boolean[] pickedUp, String payerName, String payerPersonalID, String cardType, String cardNumber, int validMonth, int validYear) {
		try {
			String subject = "";
			String body = "";
			
			AfterSchoolChoice choice = createAfterSchoolChoice(stamp, user, (Integer) child.getPrimaryKey(), (Integer) provider.getPrimaryKey(), new Integer(1), message, getCaseStatusReady(), null, season.getSchoolSeasonStart(), season, subject, body);
			if (payerName != null) {
				choice.setPayerName(payerName);
				choice.setPayerPersonalID(payerPersonalID);
				choice.setCardType(cardType);
				choice.setCardNumber(cardNumber);
				choice.setCardValidMonth(validMonth);
				choice.setCardValidYear(validYear);
				choice.store();
			}
			
			//TODO: Fix this mess...
			//storeDays(choice, days, timeOfDeparture, pickedUp);
			
			IWTimestamp registerDate = new IWTimestamp(season.getSchoolSeasonStart());
			
			SchoolClass group = getDefaultGroup(provider, season);
			SchoolClassMember member = getSchoolBusiness().storeSchoolClassMemberCC(((Integer) child.getPrimaryKey()).intValue(), ((Integer) group.getPrimaryKey()).intValue(), group.getSchoolTypeId(), registerDate.getTimestamp(), ((Integer) user.getPrimaryKey()).intValue(), message);
			// returns false if storing failed else true
			return (member != null);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		catch (CreateException ce) {
			ce.printStackTrace();
			return false;
		}
	}
}