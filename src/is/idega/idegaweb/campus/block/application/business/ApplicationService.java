package is.idega.idegaweb.campus.block.application.business;


import is.idega.idegaweb.campus.block.application.data.ApplicantFamily;
import is.idega.idegaweb.campus.block.application.data.AppliedHome;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.data.CampusApplicationHome;
import is.idega.idegaweb.campus.block.application.data.CurrentResidencyHome;
import is.idega.idegaweb.campus.block.application.data.Priority;
import is.idega.idegaweb.campus.block.application.data.PriorityHome;
import is.idega.idegaweb.campus.block.application.data.SpouseOccupationHome;
import is.idega.idegaweb.campus.block.application.data.WaitingList;
import is.idega.idegaweb.campus.block.application.data.WaitingListHome;
import is.idega.idegaweb.campus.block.mailinglist.business.MailingListService;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.application.data.Application;
import com.idega.block.application.data.ApplicationHome;
import com.idega.block.application.data.ApplicationSubjectHome;
import com.idega.block.application.data.ApplicationSubjectInfoHome;
import com.idega.block.building.business.BuildingService;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.IWTimestamp;

public interface ApplicationService extends com.idega.block.application.business.ApplicationService {
	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#storeApplicationSubject
	 */
	public void storeApplicationSubject(String description, IWTimestamp expires)
			throws CreateException, RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#storeApplicationStatus
	 */
	public boolean storeApplicationStatus(Integer ID, String status,
			int transferInterval, String setTranserferToPriorityLevel)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#createWaitinglistTransfers
	 */
	public void createWaitinglistTransfers(Applicant Appli,
			CampusApplication CA, int transferInterval,
			String setTranserferToPriorityLevel) throws CreateException,
			RemoteException, FinderException, SQLException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#garbageApplication
	 */
	public void garbageApplication(Integer ID) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#storePriorityLevel
	 */
	public void storePriorityLevel(Integer ID, String level)
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#createApplicationSubject
	 */
	public void createApplicationSubject(String sDesc, String sDate)
			throws CreateException, RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#addApplicantChild
	 */
	public void addApplicantChild(Applicant parentApplicant, Map children,
			String childName, String childSSN, int childId)
			throws RemoteException, CreateException, SQLException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#storeWholeApplication
	 */
	public CampusApplication storeWholeApplication(Integer campusApplicationID,
			Integer subjectID, ApplicantInfo applicantInfo,
			ApartmentInfo apartmentInfo, SpouseInfo spouseInfo,
			List childrenInfo) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#storeWholeApplication
	 */
	public CampusApplication storeWholeApplication(Integer campusApplicationID,
			Integer subjectID, ApplicantInfo applicantInfo,
			ApartmentInfo apartmentInfo, SpouseInfo spouseInfo,
			List childrenInfo, String status) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#storeApartmentInfo
	 */
	public void storeApartmentInfo(CampusApplication campusApplication,
			Collection capplieds, ApartmentInfo apartmentInfo)
			throws RemoteException, CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getWaitinglists
	 */
	public Collection getWaitinglists(Integer applicantId)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getApplicationInfo
	 */
	public CampusApplicationHolder getApplicationInfo(int applicationId)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getApplicationInfo
	 */
	public CampusApplicationHolder getApplicationInfo(Application a)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getApplicantInfo
	 */
	public CampusApplicationHolder getApplicantInfo(int iApplicantId)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getApplicantInfo
	 */
	public CampusApplicationHolder getApplicantInfo(Applicant eApplicant)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getApplicantEmail
	 */
	public String[] getApplicantEmail(int iApplicantId) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getBuildingService
	 */
	public BuildingService getBuildingService() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getMailingListService
	 */
	public MailingListService getMailingListService() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getApplicantHome
	 */
	public ApplicantHome getApplicantHome() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getApplicationHome
	 */
	public ApplicationHome getApplicationHome() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getAppliedHome
	 */
	public AppliedHome getAppliedHome() throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getCampusApplicationHome
	 */
	public CampusApplicationHome getCampusApplicationHome()
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getWaitingListHome
	 */
	public WaitingListHome getWaitingListHome() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getSubjectHome
	 */
	public ApplicationSubjectHome getSubjectHome() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getSubjectInfoHome
	 */
	public ApplicationSubjectInfoHome getSubjectInfoHome()
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getResidencyHome
	 */
	public CurrentResidencyHome getResidencyHome() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getSpouseOccupationHome
	 */
	public SpouseOccupationHome getSpouseOccupationHome()
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getPriorityHome
	 */
	public PriorityHome getPriorityHome() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getRightPlaceForTransfer
	 */
	public WaitingList getRightPlaceForTransfer(WaitingList wl,
			int transferInterval, String setTranserferToPriorityLevel)
			throws RemoteException, FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getComplexSubcategoryHelpersByCategory
	 */
	public Collection getComplexSubcategoryHelpersByCategory(Integer categoryID)
			throws RemoteException, FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getComplexSubcategoryHelpers
	 */
	public Collection getComplexSubcategoryHelpers() throws RemoteException,
			FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getComplexSubcategoryHelpers
	 */
	public Collection getComplexSubcategoryHelpers(Collection complexTypes)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getStatus
	 */
	public String getStatus(String status, IWResourceBundle iwrb)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#confirmOnWaitingList
	 */
	public boolean confirmOnWaitingList(Integer waitingListId,
			boolean stayOnList) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#storePhoneAndEmail
	 */
	public void storePhoneAndEmail(Integer campusApplicationID, String phone,
			String email) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#storePriority
	 */
	public Priority storePriority(String code, String description,
			String hexColor) throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#removePriority
	 */
	public void removePriority(String id) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getPriorityColorMap
	 */
	public Map getPriorityColorMap() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getApplicantFamily
	 */
	public ApplicantFamily getApplicantFamily(Applicant applicant)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getSpouseForApplicant
	 */
	public Applicant getSpouseForApplicant(Applicant applicant)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationServiceBean#getChildrenForApplication
	 */
	public Collection getChildrenForApplication(Applicant applicant)
			throws RemoteException;
}