/**
 * 
 */
package se.idega.idegaweb.commune.childcare.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.care.business.CareBusiness;
import se.idega.idegaweb.commune.care.data.AfterSchoolChoice;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.AfterSchoolCareDays;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBOService;
import com.idega.data.IDOCreateException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


/**
 * <p>
 * TODO Dainis Describe Type AfterSchoolBusiness
 * </p>
 *  Last modified: $Date: 2006/03/08 17:06:37 $ by $Author: dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.14.2.3 $
 */
public interface AfterSchoolBusiness extends IBOService, CaseBusiness {

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getBundleIdentifier
	 */
	public String getBundleIdentifier() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getAfterSchoolChoice
	 */
	public AfterSchoolChoice getAfterSchoolChoice(Object afterSchoolChoiceID) throws FinderException,
			java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#findChoicesByProvider
	 */
	public Collection findChoicesByProvider(int providerID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#findChoicesByProvider
	 */
	public Collection findChoicesByProvider(int providerID, String sorting) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#findChoicesByChildAndChoiceNumberAndSeason
	 */
	public AfterSchoolChoice findChoicesByChildAndChoiceNumberAndSeason(Integer childID, int choiceNumber,
			Integer seasonID) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getOngoingAndNextSeasons
	 */
	public Collection getOngoingAndNextSeasons() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#hasOpenApplication
	 */
	public boolean hasOpenApplication(User child, SchoolSeason season, int choiceNumber)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#hasCancelledApplication
	 */
	public boolean hasCancelledApplication(User child, SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#findChoiceByChild
	 */
	public AfterSchoolChoice findChoiceByChild(User child, SchoolSeason season, int choiceNumber)
			throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#acceptAfterSchoolChoice
	 */
	public boolean acceptAfterSchoolChoice(Object afterSchoolChoiceID, User performer) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#denyAfterSchoolChoice
	 */
	public boolean denyAfterSchoolChoice(Object afterSchoolChoiceID, User performer) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#createAfterSchoolChoice
	 */
	public AfterSchoolChoice createAfterSchoolChoice(IWTimestamp stamp, User user, Integer childID, Integer providerID,
			Integer choiceNumber, String message, CaseStatus caseStatus, Case parentCase, Date placementDate,
			SchoolSeason season, String subject, String body) throws CreateException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#createAfterSchoolChoice
	 */
	public AfterSchoolChoice createAfterSchoolChoice(IWTimestamp stamp, User user, Integer childID, Integer providerID,
			Integer choiceNumber, String message, CaseStatus caseStatus, Case parentCase, Date placementDate,
			SchoolSeason season, String subject, String body, boolean isFClassAndPrio) throws CreateException,
			RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#createAfterSchoolChoices
	 */
	public List createAfterSchoolChoices(User user, Integer childId, Integer[] providerIDs, String message,
			String[] placementDates, SchoolSeason season, String subject, String body) throws IDOCreateException,
			java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#createAfterSchoolChoices
	 */
	public List createAfterSchoolChoices(User user, Integer childId, Integer[] providerIDs, String message,
			String[] placementDates, SchoolSeason season, String subject, String body, boolean isFClassAndPrio)
			throws IDOCreateException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#createContractsForChildrenWithSchoolPlacement
	 */
	public Collection createContractsForChildrenWithSchoolPlacement(int providerId, User user, Locale locale)
			throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getDays
	 */
	public Collection getDays(AfterSchoolChoice choice) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getDay
	 */
	public AfterSchoolCareDays getDay(AfterSchoolChoice choice, int dayOfWeek) throws FinderException,
			java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#storeDays
	 */
	public void storeDays(ChildCareApplication application, int[] dayOfWeek, String[] timeOfDeparture,
			boolean[] pickedUp) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getDefaultGroup
	 */
	public SchoolClass getDefaultGroup(Object schoolPK, Object seasonPK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getDefaultGroup
	 */
	public SchoolClass getDefaultGroup(School school, SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#storeAfterSchoolCare
	 */
	public AfterSchoolChoice storeAfterSchoolCare(IWTimestamp stamp, User user, User child, School provider,
			String message, SchoolSeason season, int[] days, String[] timeOfDeparture, boolean[] pickedUp,
			String payerName, String payerPersonalID, String cardType, String cardNumber, int validMonth, int validYear)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#storeAfterSchoolCare
	 */
	public AfterSchoolChoice storeAfterSchoolCare(IWTimestamp stamp, User user, User child, School provider,
			String message, SchoolSeason season, int[] days, String[] timeOfDeparture, boolean[] pickedUp,
			boolean wantsRefreshments, String payerName, String payerPersonalID, String cardType, String cardNumber,
			int validMonth, int validYear) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getSchoolBusiness
	 */
	public SchoolBusiness getSchoolBusiness() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getSchoolChoiceBusiness
	 */
	public SchoolChoiceBusiness getSchoolChoiceBusiness() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getChildCareBusiness
	 */
	public ChildCareBusiness getChildCareBusiness() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getCareBusiness
	 */
	public CareBusiness getCareBusiness() throws java.rmi.RemoteException;
}
