package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.invoice.data.ConstantStatus;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry;
import se.idega.idegaweb.commune.accounting.posting.business.MissingMandatoryFieldException;
import se.idega.idegaweb.commune.accounting.posting.business.PostingException;
import se.idega.idegaweb.commune.accounting.posting.business.PostingParametersException;
import se.idega.idegaweb.commune.accounting.regulations.business.PaymentFlowConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.RuleTypeConstant;
import se.idega.idegaweb.commune.accounting.regulations.data.PostingDetail;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.school.data.Provider;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplicationHome;
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolManagementType;
import com.idega.block.school.data.SchoolManagementTypeHome;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;

/**
 * Abstract class that holds all the logic that is common for the shool billing
 * 
 * @author Joakim
 * 
 * @see se.idega.idegaweb.commune.accounting.invoice.business.PaymentThreadElementarySchool
 * @see se.idega.idegaweb.commune.accounting.invoice.business.PaymentThreadHighSchool
 * @see se.idega.idegaweb.commune.accounting.invoice.business.BillingThread
 */
public abstract class PaymentThreadSchool extends BillingThread{
	PaymentHeader paymentHeader;
	Date currentDate;
	
	public PaymentThreadSchool(Date month, IWContext iwc){
		super(month,iwc);
		currentDate = month;
	}
	
	
	protected void contracts(){
		Collection regulationArray = new ArrayList();
		ArrayList conditions = new ArrayList();
		boolean first;
		ExportDataMapping categoryPosting;
		PostingDetail postingDetail;

		try {
			//Set the category parameter to ElementarySchool
			category = getSchoolCategoryHome().findElementarySchoolCategory();
			categoryPosting = (ExportDataMapping) IDOLookup.getHome(ExportDataMapping.class).
					findByPrimaryKeyIDO(category.getPrimaryKey());

			RegulationsBusiness regBus = getRegulationsBusiness();
			Integer privateManagementType = (Integer)((SchoolManagementTypeHome) IDOLookup.getHome(SchoolManagementType.class)).findPrivateManagementType().getPrimaryKey();

			Iterator schoolIter = getSchoolHome().findAllInHomeCommuneByCategory(category).iterator();
			//Go through all elementary schools
			while(schoolIter.hasNext()){
				try{
					school = (School) schoolIter.next();
					Provider provider = new Provider(((Integer)school.getPrimaryKey()).intValue());
					//Only look at those not "payment by invoice"
					//Check if it is private or in Nacka
					if(school.getCommune().getIsDefault()||
							(school.getManagementType().getPrimaryKey().equals(privateManagementType)&&
							!provider.getPaymentByInvoice())){
						
						//Get all the rules for this contract
						regulationArray = regBus.getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(
							category.getLocalizedKey(),//The ID that selects barnomsorg in the regulation
							PaymentFlowConstant.OUT, 		//The payment flow is out
							currentDate,					//Current date to select the correct date range
							RuleTypeConstant.DERIVED,		//The conditiontype
							conditions						//The conditions that need to fulfilled
							);

						first = true;
						Iterator regulationIter = regulationArray.iterator();
						while(regulationIter.hasNext())
						{
							//NOTE this should be changed to use ...ByDateRange when changed to date range rathre than day by day calculation
							Iterator contractIter = getChildCareContractHome().findValidContractByProvider(((Integer)school.getPrimaryKey()).intValue(),currentDate).iterator();
//							Iterator applicationIter = getChildCareApplicationHome().findApplicationsByProviderAndDate(((Integer)school.getPrimaryKey()).intValue(), 
//									((Integer)school.getPrimaryKey()).intValue(),currentDate).iterator();
							while(contractIter.hasNext()){
//								ChildCareApplication application = (ChildCareApplication) applicationIter.next();
								ChildCareContract contract = (ChildCareContract) contractIter.next();
								if(first){
									paymentHeader = (PaymentHeader) IDOLookup.create(PaymentHeader.class);
									paymentHeader.setSchoolID(school);
									paymentHeader.setSchoolCategoryID(category);
									if(categoryPosting.getProviderAuthorization()){
										paymentHeader.setStatus(ConstantStatus.BASE);
									} else {
										paymentHeader.setStatus(ConstantStatus.PRELIMINARY);
									}
									first = false;
								}
//								ChildCareContract contract = getChildCareContractHome().findApplicationByContract(((Integer)application.getPrimaryKey()).intValue());
								calculateTime(contract.getValidFromDate(),contract.getTerminatedDate());
								//Get the posting details for the contract
								postingDetail = regBus.getPostingDetailForContract(0.0f,contract);
								RegulationSpecType regSpecType = getRegulationSpecTypeHome().
										findByRegulationSpecType(postingDetail.getRuleSpecType());
								int schoolType = ((Integer)contract.getSchoolClassMmeber().
										getSchoolClass().getSchoolType().getPrimaryKey()).intValue();
								String[] postings = compilePostingStrings(
										schoolType, ((Integer)regSpecType.getPrimaryKey()).intValue(), provider);
								createPaymentRecord(postingDetail,postings[0],postings[1]);
							}
						}
					}
				} catch (RemoteException e) {
					e.printStackTrace();
					createNewErrorMessage(school.getName(),"invoice.DBError");
				} catch (FinderException e) {
					e.printStackTrace();
					createNewErrorMessage(school.getName(),"invoice.CouldNotFindContractForSchool");
				} catch (CreateException e) {
					e.printStackTrace();
					createNewErrorMessage(school.getName(),"invoice.CouldNotInsertIntoDatabase");
				} catch (PostingParametersException e) {
					e.printStackTrace();
					createNewErrorMessage(school.getName(),"invoice.PostingParameterIncorrect");
				} catch (PostingException e) {
					e.printStackTrace();
					createNewErrorMessage(school.getName(),"invoice.PostingString");
				} catch (MissingMandatoryFieldException e) {
					e.printStackTrace();
					createNewErrorMessage(school.getName(),"invoice.PostingStringIsMissingMandatoryField");
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			createNewErrorMessage("invoice.ContractCreation","invoice.DBError");
		} catch (FinderException e) {
			e.printStackTrace();
			createNewErrorMessage("invoice.ContractCreation","invoice.CouldNotFindSchoolCategory");
		} catch (EJBException e) {
			e.printStackTrace();
			createNewErrorMessage("invoice.ContractCreation","invoice.CouldNotFindHomeCommune");
		} catch (CreateException e) {
			e.printStackTrace();
			createNewErrorMessage("invoice.ContractCreation","invoice.CouldNotFindHomeCommune");
		}
	}
	
	/**
	 * Creates all the invoice headers, invoice records, payment headers and payment records
	 * for the Regular payments
	 */
	protected void regularPayment(){
		PostingDetail postingDetail = null;
		
		try {
			//TODO (JJ) change this to RegularPaymentBusiness when Roar is done with that
			Iterator regularPaymentIter = getRegularInvoiceBusiness().findRegularInvoicesForPeriode(startPeriod.getDate(), endPeriod.getDate()).iterator();
			//Go through all the regular payments
			while(regularPaymentIter.hasNext()){
				//TODO (JJ) change this to RegularPaymentEntry when Roar is done with that
				RegularInvoiceEntry regularPaymentEntry = (RegularInvoiceEntry)regularPaymentIter.next();
				postingDetail = new PostingDetail(regularPaymentEntry);
				createPaymentRecord(postingDetail,regularPaymentEntry.getOwnPosting(), regularPaymentEntry.getDoublePosting());
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(postingDetail != null){
				createNewErrorMessage(postingDetail.getTerm(),"payment.DBSetupProblem");
			}else{
				createNewErrorMessage("payment.severeError","payment.DBSetupProblem");
			}
		}
	}
	
	/*
	 * Overridden function until billing is done by period instead of date (non-Javadoc)
	 * Now we always bill for the whole month...
	 * Just remove this function when changing to date range
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.BillingThread#calculateTime(java.sql.Date, java.sql.Date)
	 */
	/**
	 * calculatest the number of days and months between the start and end date 
	 * and sets the local variables monts and days
	 * 
	 * @param start
	 * @param end
	 */
	protected void calculateTime(Date start, Date end){
		startTime = new IWTimestamp(startPeriod);
		startTime.setAsDate();
		//Then get end date
		endTime = new IWTimestamp(endPeriod);
		endTime.setAsDate();
		//calc the how many months are in the given time.
		months = 1.0f;
		days = IWTimestamp.getDaysBetween(startTime, endTime);
	}

	private SchoolHome getSchoolHome() throws RemoteException {
		return (SchoolHome) IDOLookup.getHome(School.class);
	}

	protected SchoolCategoryHome getSchoolCategoryHome() throws RemoteException {
		return (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
	}

	protected ChildCareApplicationHome getChildCareApplicationHome() throws RemoteException {
		return (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
	}

}
