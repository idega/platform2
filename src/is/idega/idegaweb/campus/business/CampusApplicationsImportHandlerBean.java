package is.idega.idegaweb.campus.business;

import is.idega.idegaweb.campus.block.application.data.Applied;
import is.idega.idegaweb.campus.block.application.data.AppliedHome;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.data.CampusApplicationHome;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.application.data.Application;
import com.idega.block.application.data.ApplicationHome;
import com.idega.block.importer.business.ImportFileHandler;
import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOSessionBean;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;
import com.idega.util.Timer;
import com.idega.util.text.TextSoap;
/**
 * <p>Title: CampusApplicationsImportHandlerBean</p>
 * <p>Description: Total copy paste class for a quick import OR A simple import file handler that reads file with personalIds and names etc and created campus applications from them</p>
 * <p>Idega Software Copyright (c) 2007</p>
 * <p>Company: Idega Software</p>
 * @author <a href="mailto:eiki@idega.is">Eirikur Sveinn Hrafnsson</a>
 * @version 1.0
 */
public class CampusApplicationsImportHandlerBean extends IBOSessionBean implements ImportFileHandler,CampusApplicationsImportHandler {

	private static final long serialVersionUID = 1L;

	private static final String EMPTY = "EMPTY";

	private List applicationProperties;

	private ImportFile file;
	private UserTransaction transaction;
	private ArrayList failedRecords;
	
	private ApplicantHome applicantHome;
	private ApplicationHome applicationHome;
	private CampusApplicationHome campusApplicationHome;
	private AppliedHome appliedHome;
		
	
	public CampusApplicationsImportHandlerBean() {
	}
	
	
	public boolean handleRecords() throws RemoteException {
		this.transaction = this.getSessionContext().getUserTransaction();
		Timer clock = new Timer();
		clock.start();
		try {
			//initialize business beans and data homes
						
			this.applicantHome = (ApplicantHome) this.getIDOHome(Applicant.class);
			this.applicationHome = (ApplicationHome) this.getIDOHome(Application.class);
			this.campusApplicationHome = (CampusApplicationHome) this.getIDOHome(CampusApplication.class);
			this.appliedHome = (AppliedHome) this.getIDOHome(Applied.class);
			
			
			this.failedRecords = new ArrayList();
			
			this.transaction.begin();
			//iterate through the records and process them
			String item;
			int count = 0;
			while (!(item = (String) this.file.getNextRecord()).equals("")) {
				count++;
				if (!processRecord(item)) {
					this.failedRecords.add(item);
				}
			}
			clock.stop();
			System.out.println("Time to handleRecords: " + clock.getTime() + " ms  OR " + ((int) (clock.getTime() / 1000)) + " s.");
			// System.gc();
			//success commit changes
			this.transaction.commit();
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			try {
				this.transaction.rollback();
			}
			catch (SystemException e) {
				e.printStackTrace();
			}
			return false;
		}
	}
	
	
	private boolean processRecord(String record) throws RemoteException {
	  	this.applicationProperties = this.file.getValuesFromRecordString(record);
		
				
		try{
//			hard coded 3 bedroom apartment type for keilir
			int subCategoryId = 8;
			//hard coded subject id for the year 2007-2008
			int appSubjectId = 82;
			//hard coded, application sent status
			String sentStatus = "S";
			
			
			int counter = 0;
			String name = (String) this.applicationProperties.get(counter);
			
			String PIN = (String) this.applicationProperties.get(++counter);
			PIN = TextSoap.findAndCut(PIN, "-");
			PIN = TextSoap.removeWhiteSpace(PIN);
			String address1 = (String) this.applicationProperties.get(++counter);
			String address2 = (String) this.applicationProperties.get(++counter);
			String currentAddress1 = (String) this.applicationProperties.get(++counter);
			String currentAddress2 = (String) this.applicationProperties.get(++counter);
			String email = (String) this.applicationProperties.get(++counter);
			String homePhone = (String) this.applicationProperties.get(++counter);
			String mobile = (String) this.applicationProperties.get(++counter);
			String child1 = (String) this.applicationProperties.get(++counter);
			String child2 = (String) this.applicationProperties.get(++counter);
			String child3 = (String) this.applicationProperties.get(++counter);
			String child4 = (String) this.applicationProperties.get(++counter);
			String firstChoice = (String) this.applicationProperties.get(++counter);
			String secondChoice = (String) this.applicationProperties.get(++counter);
			String thirdChoice = (String) this.applicationProperties.get(++counter);
			String requestedStartDate = (String) this.applicationProperties.get(++counter);
			String comments = (String) this.applicationProperties.get(++counter);
			
			StringBuffer otherInfo = new StringBuffer();
			if(!EMPTY.equals(firstChoice)){
				otherInfo.append("1. val : ").append(firstChoice).append("\n");
			}
			if(!EMPTY.equals(secondChoice)){
				otherInfo.append("2. val : ").append(secondChoice).append("\n");
			}
			if(!EMPTY.equals(thirdChoice)){
				otherInfo.append("3. val : ").append(thirdChoice).append("\n");
			}
			if(!EMPTY.equals(requestedStartDate)){
				otherInfo.append("Byrja : ").append(requestedStartDate).append("\n");
			}
			
			if(!EMPTY.equals(child1)){
				otherInfo.append("Barn 1 : ").append(child1).append("\n");
			}
			if(!EMPTY.equals(child2)){
				otherInfo.append("Barn 2 : ").append(child2).append("\n");
			}
			if(!EMPTY.equals(child3)){
				otherInfo.append("Barn 3 : ").append(child3).append("\n");
			}
			if(!EMPTY.equals(child4)){
				otherInfo.append("Barn 4 : ").append(child4).append("\n");
			}
			
			otherInfo.append(comments);
			
			//address
			String legalAddress = address1;
			if(!EMPTY.equals(address2)){
				legalAddress+=", "+address2;
			}
			
			String currentAddress = null;
			if(!EMPTY.equals(currentAddress1)){
				currentAddress = currentAddress1;
				if(!EMPTY.equals(currentAddress2)){
					currentAddress+=", "+currentAddress2;
				}
			}
			
			//REAL stuff below...
			try {
				Collection applicants = applicantHome.findBySSN(PIN);
				if(applicants!=null && !applicants.isEmpty()){
					return false;//already done this one
				}
				else{
					createNewData(subCategoryId, appSubjectId, sentStatus, PIN, name, email, homePhone, mobile, otherInfo, legalAddress, currentAddress);
				}
				
			} catch (FinderException e) {
				//not sure if this is thrown, just in case create
				createNewData(subCategoryId, appSubjectId, sentStatus, PIN, name, email, homePhone, mobile, otherInfo, legalAddress, currentAddress);
			}
			
			
		}
		catch (IndexOutOfBoundsException e4) {
			return false;
		} catch (CreateException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}


	protected void createNewData(int subCategoryId, int appSubjectId, String sentStatus, String PIN, String name, String email, String homePhone, String mobile, StringBuffer otherInfo, String legalAddress, String currentAddress) throws CreateException {
		//CREATE THE APPLICANT
		Applicant applicant = createApplicant(sentStatus, PIN, name, homePhone, mobile, legalAddress, currentAddress);

		
//				CREATE THE APPLICATION
		Application application = createApplication(appSubjectId, applicant);
		
		//CREATE FIRST CHOICE
		createChoices(subCategoryId, application);
		

		//CREATE THE EXTRA INFO APPLICATION
		createCampusApplication(email, mobile, otherInfo, application);
	}


	protected void createCampusApplication(String email, String mobile, StringBuffer otherInfo, Application application) throws CreateException {
		CampusApplication campusApplication = campusApplicationHome.create();
		campusApplication.setAppApplicationId((Integer)application.getPrimaryKey());
		if(!EMPTY.equals(mobile)){
			campusApplication.setContactPhone(mobile);
		}
		campusApplication.setEmail(email);
		campusApplication.setOtherInfo(otherInfo.toString());
		campusApplication.store();
	}


	protected void createChoices(int subCategoryId, Application application) throws CreateException {
		Applied appliedChoice = appliedHome.create();
		appliedChoice.setSubcategoryID(subCategoryId);
		appliedChoice.setOrder(1);
		appliedChoice.setApplicationId((Integer)application.getPrimaryKey());
		
		appliedChoice.store();
	}


	protected Application createApplication(int appSubjectId, Applicant applicant) throws CreateException {
		Application application = applicationHome.create();
		application.setApplicantId((Integer)applicant.getPrimaryKey());
		application.setSubjectId(appSubjectId);
		application.setSubmitted(IWTimestamp.getTimestampRightNow());
		application.setStatusSubmitted();
		application.setStatusChanged(IWTimestamp.getTimestampRightNow());
		application.store();
		return application;
	}


	protected Applicant createApplicant(String sentStatus, String PIN, String name, String homePhone, String mobile, String legalAddress, String currentAddress) throws CreateException {
		Applicant applicant = applicantHome.create();
		
		applicant.setFullName(name);
		applicant.setSSN(PIN);
		applicant.setStatus(sentStatus);
		if(!EMPTY.equals(mobile)){
			applicant.setMobilePhone(mobile);
		}
		applicant.setLegalResidence(legalAddress);
		
		if(!EMPTY.equals(homePhone)){
			applicant.setResidencePhone(homePhone);
		}
		
		if(currentAddress!=null){
			applicant.setResidence(currentAddress);
		}
		
		applicant.store();
		return applicant;
	}
	

	
	public void setImportFile(ImportFile file) {
		this.file = file;
	}
	/**
	 * @see com.idega.block.importer.business.ImportFileHandler#setRootGroup(Group)
	 */
	public void setRootGroup(Group group) {
		//not used
	}
	/**
	* @see com.idega.block.importer.business.ImportFileHandler#getFailedRecords()
	*/
	public List getFailedRecords() {
		return this.failedRecords;
	}
}