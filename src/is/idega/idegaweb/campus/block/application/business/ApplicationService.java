package is.idega.idegaweb.campus.block.application.business;

import is.idega.idegaweb.campus.block.application.data.AppliedHome;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.data.CampusApplicationHome;
import is.idega.idegaweb.campus.block.application.data.CurrentResidencyHome;
import is.idega.idegaweb.campus.block.application.data.SpouseOccupationHome;
import is.idega.idegaweb.campus.block.application.data.WaitingList;
import is.idega.idegaweb.campus.block.application.data.WaitingListHome;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

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


public interface ApplicationService extends com.idega.business.IBOService 
{
	public String[] getApplicantEmail(int iApplicantId);
	public ApplicantHome getApplicantHome() throws RemoteException;
	public CampusApplicationHolder getApplicantInfo(Applicant eApplicant) ;
	public CampusApplicationHolder getApplicantInfo(int iApplicantId);
	public ApplicationHome getApplicationHome() throws RemoteException;
	public AppliedHome getAppliedHome() throws RemoteException;
	public CampusApplicationHome getCampusApplicationHome() throws RemoteException;
	public WaitingListHome getWaitingListHome() throws RemoteException ;
	public ApplicationSubjectInfoHome getSubjectInfoHome() throws RemoteException;
	public CurrentResidencyHome getResidencyHome() throws RemoteException;
	public SpouseOccupationHome getSpouseOccupationHome() throws RemoteException;
	public  WaitingList getRightPlaceForTransfer(WaitingList wl)throws RemoteException,FinderException ;
	public int getMaxTransferInWaitingList(int typeId, int cmplxId) ;
	public Collection getComplexTypeHelpersByCategory(Integer categoryID)throws RemoteException,FinderException;
	public Collection getComplexTypeHelpers()throws RemoteException, FinderException;
	public Collection getComplexTypeHelpers(Collection complexTypes);
	public ApplicationSubjectHome getSubjectHome() throws RemoteException;
	public  BuildingService getBuildingService() throws RemoteException ;
	public void storeApplicationStatus(Integer campusApplicationID, String status) ;
	public void storeApartmentInfo(			CampusApplication campusApplication,			Collection capplieds,			ApartmentInfo apartmentInfo)throws RemoteException, CreateException;
	public void createApplicationSubject(String sDesc, String sDate) throws CreateException, RemoteException;
	public void storePriorityLevel(Integer ID, String level) throws RemoteException;
	public CampusApplication storeWholeApplication(			Integer ID,			Integer subjectID,			ApplicantInfo applicantInfo,			ApartmentInfo apartmentInfo,			SpouseInfo spouseInfo,			List childrenInfo);
	public CampusApplication storeWholeApplication(			Integer ID,			Integer subjectID,			ApplicantInfo applicantInfo,			ApartmentInfo apartmentInfo,			SpouseInfo spouseInfo,			List childrenInfo,String status);
	public CampusApplicationHolder getApplicationInfo(Application a);
	public CampusApplicationHolder getApplicationInfo(int applicationId);
	public void storeApplicationSubject(String description,IWTimestamp expires) throws CreateException,RemoteException;
	public String getStatus(String status,IWResourceBundle iwrb) throws RemoteException;
	public boolean confirmOnWaitingList(Integer waitingListId, boolean stayOnList)throws RemoteException;
	public void storePhoneAndEmail(Integer campusApplicationID,String phone,String email) throws RemoteException;
	
}
