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
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import se.idega.idegaweb.commune.childcare.data.AfterSchoolChoice;
import se.idega.idegaweb.commune.childcare.data.AfterSchoolChoiceHome;

import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
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
			String[] caseStatus = { getCaseStatusInactive().getStatus(), getCaseStatusDenied().getStatus(), getCaseStatusReady().getStatus() };
			return getAfterSchoolChoiceHome().findAllCasesByProviderAndNotInStatus(providerID, caseStatus);
		}
		catch (FinderException e) {
			return new ArrayList();
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}
	
	public AfterSchoolChoice findChoicesByChildAndChoiceNumberAndSeason(Integer childID, int choiceNumber, Integer seasonID) throws FinderException {
		try {
			String[] caseStatus = { getCaseStatusPreliminary().getStatus(), getCaseStatusInactive().getStatus() };
			return getAfterSchoolChoiceHome().findByChildAndChoiceNumberAndSeason(childID, new Integer(choiceNumber), seasonID, caseStatus);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}
	
	public boolean acceptAfterSchoolChoice(Object afterSchoolChoiceID, User performer) {
		AfterSchoolChoice choice = null;
		try {
			choice = getAfterSchoolChoice(afterSchoolChoiceID);
		}
		catch (FinderException fe) {
			return false;
		}
		
		try {
			changeCaseStatus(choice, getCaseStatusGranted().getStatus(), performer);
		}
		catch (RemoteException re) {
			return false;
		}
		
		return true;
	}
	
	public boolean denyAfterSchoolChoice(Object afterSchoolChoiceID, User performer) {
		UserTransaction transaction = getSessionContext().getUserTransaction();
		try {
			transaction.begin();

			AfterSchoolChoice choice = getAfterSchoolChoice(afterSchoolChoiceID);
			changeCaseStatus(choice, getCaseStatusDenied().getStatus(), performer);

			Iterator children = choice.getChildren();
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

	private AfterSchoolChoice createAfterSchoolChoice(User user, Integer childID, Integer providerID, Integer choiceNumber, String message, CaseStatus caseStatus, Case parentCase, Date placementDate, SchoolSeason season, String subject, String body) throws CreateException, RemoteException {
		if (season == null) {
			try {
				season = getSchoolChoiceBusiness().getCurrentSeason();
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
		IWTimestamp stamp = new IWTimestamp();
		choice.setQueueDate(stamp.getDate());
		stamp.addSeconds((10 - (choiceNumber.intValue() * 10)));
		choice.setCreated(stamp.getTimestamp());
		choice.setCaseStatus(caseStatus);
		choice.setApplicationStatus(getStatusSentIn());
		
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
			AfterSchoolChoice choice = null;
			for (int i = 0; i < caseCount; i++) {
				if (providerIDs[i] != null && providerIDs[i].intValue() > 0) {
					stamp = new IWTimestamp(placementDates[i]);
					choice = createAfterSchoolChoice(user, childId, providerIDs[i], new Integer(i + 1), message, i == 0 ? first : other, choice, stamp.getDate(), season, subject, body);
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
}
