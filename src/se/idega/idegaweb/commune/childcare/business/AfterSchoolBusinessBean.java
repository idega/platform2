/*
 * Created on Sep 15, 2003
 *
 */
package se.idega.idegaweb.commune.childcare.business;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.childcare.data.AfterSchoolChoice;
import se.idega.idegaweb.commune.childcare.data.AfterSchoolChoiceHome;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.data.SchoolSeason;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOStoreException;
import com.idega.util.IWTimestamp;
/**
 * AfterSchoolBusinessBean
 * @author aron 
 * @version 1.0
 */
public class AfterSchoolBusinessBean extends ChildCareBusinessBean implements AfterSchoolBusiness{
	public SchoolChoiceBusiness getSchoolChoiceBusiness() throws RemoteException {
		return (SchoolChoiceBusiness) this.getServiceInstance(SchoolChoiceBusiness.class);
	}
	public AfterSchoolChoiceHome getAfterSchoolChoiceHome() throws RemoteException {
		return (AfterSchoolChoiceHome) IDOLookup.getHome(AfterSchoolChoice.class);
	}
	
	public AfterSchoolChoice createAfterSchoolChoice(
		Integer userID,
		Integer childID,
		Integer providerID,
		Integer choiceNumber,
		String message,
		java.sql.Timestamp choiceDate,
		CaseStatus caseStatus,
		Case parentCase,
		Date placementDate,
		SchoolSeason season)
		throws CreateException, RemoteException, FinderException {
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
			Collection choices =
				getAfterSchoolChoiceHome().findByChildAndSeason(childID, ((Integer) season.getPrimaryKey()));
			if (!choices.isEmpty()) {
				Iterator iter = choices.iterator();
				while (iter.hasNext()) {
					AfterSchoolChoice element = (AfterSchoolChoice) iter.next();
					if (element.getChoiceNumber().intValue() == choiceNumber.intValue()) {
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
		choice.setChildId(childID);
		if (providerID != null)
			choice.setProviderID(providerID);
		choice.setChoiceNumber(choiceNumber);
		//choice.setSchoolChoiceDate(choiceDate);
		choice.setMessage(message);
		if (season != null) {
			Integer seasonId = (Integer) season.getPrimaryKey();
			choice.setSchoolSeasonId(seasonId);
		}
		if (placementDate != null)
			choice.setPlacementDate(placementDate);
		IWTimestamp stamp = new IWTimestamp();
		stamp.addSeconds((10 - (choiceNumber.intValue() * 10)));
		choice.setCreated(stamp.getTimestamp());
		choice.setCaseStatus(caseStatus);
		/*
		if (caseStatus.getStatus().equalsIgnoreCase("PREL")) {
			sendMessageToParentOrChild(choice.getOwner(), choice.getChild(), getPreliminaryMessageSubject(), getPreliminaryMessageBody(choice));
		//					getMessageBusiness().createUserMessage(choice.getOwner(), getPreliminaryMessageSubject(), getPreliminaryMessageBody(choice));
		}
		*/
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
	public List createAfterSchoolChoices(
		Integer userId,
		Integer childId,
		Integer[] providerIDs,
		String message,
		Date placementDate,
		SchoolSeason season)
		throws IDOCreateException {
		/*if(changeOfSchool){
			AfterSchoolChoice choice = createSchoolChangeChoice(userId,childId,school_type_id,current_school,chosen_school_1,grade,method,workSituation1,workSituation2,language,message,keepChildrenCare,autoAssign,custodiansAgree,schoolCatalogue, season);
			ArrayList list = new ArrayList(1);
			list.add(choice);
			return list;
		}else*/ {
			int caseCount = 3;
			java.sql.Timestamp time = new java.sql.Timestamp(System.currentTimeMillis());
			List returnList = new Vector(3);
			javax.transaction.UserTransaction trans = this.getSessionContext().getUserTransaction();
			try {
				trans.begin();
				CaseStatus first = getCaseStatus("PREL");
				CaseStatus other = getCaseStatus(super.getCaseStatusInactive().getStatus());
				//Integer[] providerIDs = { providerID1,providerID2,providerID3 };
				AfterSchoolChoice choice = null;
				for (int i = 0; i < caseCount; i++) {
					choice =
						createAfterSchoolChoice(
							userId,
							childId,
							providerIDs[i],
							new Integer(i + 1),
							message,
							time,
							i == 0 ? first : other,
							choice,
							placementDate,
							season);
					returnList.add(choice);
				}
				//handleSeparatedParentApplication(childId,userId,returnList,false);
				trans.commit();
				/*
					int previousSeasonID = getCommuneSchoolBusiness().getPreviousSchoolSeasonID(getCommuneSchoolBusiness().getCurrentSchoolSeasonID());
					if (previousSeasonID != -1)
						getCommuneSchoolBusiness().setNeedsSpecialAttention(childId, previousSeasonID,true);
				*/
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
}
