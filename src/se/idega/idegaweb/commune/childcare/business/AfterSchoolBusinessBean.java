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
			return getAfterSchoolChoiceHome().findAllCasesByProviderAndStatus(providerID, getCaseStatusPreliminary());
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
			String[] caseStatus = { getCaseStatusOpen().getStatus(), getCaseStatusGranted().getStatus() };
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

	public AfterSchoolChoice createAfterSchoolChoice(Integer userID, Integer childID, Integer providerID, Integer choiceNumber, String message, java.sql.Timestamp choiceDate, CaseStatus caseStatus, Case parentCase, Date placementDate, SchoolSeason season) throws CreateException, RemoteException, FinderException {
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
			Collection choices = getAfterSchoolChoiceHome().findByChildAndSeason(childID, ((Integer) season.getPrimaryKey()));
			if (!choices.isEmpty()) {
				Iterator iter = choices.iterator();
				while (iter.hasNext()) {
					AfterSchoolChoice element = (AfterSchoolChoice) iter.next();
					if (element.getChoiceNumber() == choiceNumber.intValue()) {
						choice = element;
						continue;
					}
				}
			}
		}
		if (choice == null) {
			AfterSchoolChoiceHome home = this.getAfterSchoolChoiceHome();
			choice = home.create();
		}
		try {
			choice.setOwner(getUser(userID.intValue()));
		}
		catch (FinderException fex) {
			throw new IDOCreateException(fex);
		}
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
		stamp.addSeconds((10 - (choiceNumber.intValue() * 10)));
		choice.setCreated(stamp.getTimestamp());
		choice.setCaseStatus(caseStatus);

		if (parentCase != null)
			choice.setParentCase(parentCase);
		try {
			choice.store();
		}
		catch (IDOStoreException idos) {
			throw new IDOCreateException(idos);
		}
		return choice;
	}
	
	public List createAfterSchoolChoices(Integer userId, Integer childId, Integer[] providerIDs, String message, Date placementDate, SchoolSeason season) throws IDOCreateException {
		int caseCount = 3;
		IWTimestamp stamp = new IWTimestamp();
		List returnList = new Vector(3);
		javax.transaction.UserTransaction trans = this.getSessionContext().getUserTransaction();

		try {
			trans.begin();
			CaseStatus first = getCaseStatus("PREL");
			CaseStatus other = getCaseStatus(super.getCaseStatusInactive().getStatus());
			AfterSchoolChoice choice = null;
			for (int i = 0; i < caseCount; i++) {
				choice = createAfterSchoolChoice(userId, childId, providerIDs[i], new Integer(i + 1), message, stamp.getTimestamp(), i == 0 ? first : other, choice, placementDate, season);
				returnList.add(choice);
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
			throw new IDOCreateException(ex.getMessage());
		}
	}
}
