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

import se.idega.idegaweb.commune.care.data.AfterSchoolChoice;
import se.idega.idegaweb.commune.care.data.AfterSchoolChoiceHome;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;

import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDOStoreException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * AfterSchoolBusinessBean
 * @author aron 
 * @version 1.0
 */
public class AfterSchoolBusinessBean extends ChildCareBusinessBean implements AfterSchoolBusiness {
	
	private AfterSchoolChoiceHome getAfterSchoolChoiceHome() {
		try {
			return (AfterSchoolChoiceHome) IDOLookup.getHome(AfterSchoolChoice.class);
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
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusDenied().getStatus(), getCaseStatusReady().getStatus(), getCaseStatusDeleted().getStatus() };
			return getAfterSchoolChoiceHome().findAllCasesByProviderAndNotInStatus(providerID, caseStatus);
		}
		catch (FinderException e) {
			return new ArrayList();
		}
	}
	
	public Collection findChoicesByProvider(int providerID, String sorting) {
		try {
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusCancelled().getStatus(), getCaseStatusDenied().getStatus(), getCaseStatusReady().getStatus(), getCaseStatusDeleted().getStatus() };
			return getAfterSchoolChoiceHome().findAllCasesByProviderAndNotInStatus(providerID, caseStatus, sorting);
		}
		catch (FinderException e) {
			return new ArrayList();
		}
	}
	
	public AfterSchoolChoice findChoicesByChildAndChoiceNumberAndSeason(Integer childID, int choiceNumber, Integer seasonID) throws FinderException {
		String[] caseStatus = { getCaseStatusPreliminary().getStatus(), getCaseStatusInactive().getStatus() };
		return getAfterSchoolChoiceHome().findByChildAndChoiceNumberAndSeason(childID, new Integer(choiceNumber), seasonID, caseStatus);
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

	private AfterSchoolChoice createAfterSchoolChoice(IWTimestamp stamp, User user, Integer childID, Integer providerID, Integer choiceNumber, String message, CaseStatus caseStatus, Case parentCase, Date placementDate, SchoolSeason season, String subject, String body) throws CreateException, RemoteException {
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
				choice = findChoicesByChildAndChoiceNumberAndSeason(childID, choiceNumber.intValue(), (Integer) season.getPrimaryKey());
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
	
	public List createAfterSchoolChoices(User user, Integer childId, Integer[] providerIDs, String message, String[] placementDates, SchoolSeason season, String subject, String body) throws IDOCreateException {
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
					choice = createAfterSchoolChoice(timeNow, user, childId, providerIDs[i], new Integer(i + 1), message, status, choice, stamp.getDate(), season, subject, body);
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
				hasSchoolPlacement = getSchoolBusiness().hasActivePlacement(application.getChildId(), providerId, getSchoolBusiness().getCategoryElementarySchool());
			} catch (RemoteException e) {}
			if (hasSchoolPlacement && (application.getApplicationStatus() == getStatusSentIn())) {
				if (!application.getHasDateSet()) {
					SchoolClassMember placement = null;
					try {
						SchoolClassMemberHome home = getSchoolBusiness().getSchoolClassMemberHome();
						placement = home.findNotTerminatedByStudentSchoolAndCategory(application.getChildId(), providerId, getSchoolBusiness().getCategoryElementarySchool());
					} catch (Exception e) {}
					if (placement != null) {
						application.setFromDate(new Date(placement.getRegisterDate().getTime()));
					}
				}
				if (!hasActivePlacementNotWithProvider(application.getChildId(), providerId)) {
					if (assignContractToApplication(((Integer) application.getPrimaryKey()).intValue(), -1, null, null, -1, user, locale, true)) {
						users.add(application.getChild());
					}
				}
			}
		}
		return users;
	}
}
