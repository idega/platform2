package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.invoice.data.*;
import se.idega.idegaweb.commune.accounting.posting.business.*;
import se.idega.idegaweb.commune.accounting.regulations.business.*;
import se.idega.idegaweb.commune.accounting.regulations.data.*;
import se.idega.idegaweb.commune.accounting.school.data.Provider;
import se.idega.idegaweb.commune.childcare.data.*;

import com.idega.block.school.data.*;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;

/**
 * @author Joakim
 *
 */
public abstract class PaymentThreadSchool extends BillingThread{
	PaymentHeader paymentHeader;
	Date currentDate = new Date( System.currentTimeMillis());
	
	public PaymentThreadSchool(Date month, IWContext iwc){
		super(month,iwc);
	}
	
	
	protected void contracts(){
		Collection regulationArray = new ArrayList();
//		Collection applications;
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

			Iterator schoolIter = getSchoolHome().findAllByCategory(category).iterator();
			//Go through all elementary schools
			while(schoolIter.hasNext()){
				try{
					school = (School) schoolIter.next();
					Provider provider = new Provider(((Integer)school.getPrimaryKey()).intValue());
					//Only look at those not "payment by invoice"
					if(!provider.getPaymentByInvoice()){
						Integer privateManagementType = (Integer)((SchoolManagementTypeHome) IDOLookup.getHome(SchoolManagementType.class)).findPrivateManagementType().getPrimaryKey();
						//Check if it is private or in Nacka
						//TODO (JJ) how do I find out if it is in the home commune
						if(school.getManagementType().getPrimaryKey().equals(privateManagementType)){
							
							//Get all the rules for this contract
							//TODO (JJ) This is a func that Thomas will provide.
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
								Iterator applicationIter = getChildCareApplicationHome().findApplicationsByProviderAndDate(
										((Integer)school.getPrimaryKey()).intValue(),currentDate).iterator();
								while(applicationIter.hasNext()){
									ChildCareApplication application = (ChildCareApplication) applicationIter.next();
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
									ChildCareContract contract = getChildCareContractHome().findApplicationByContract(((Integer)application.getPrimaryKey()).intValue());
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
			createNewErrorMessage(school.getName(),"invoice.DBError");
		} catch (FinderException e) {
			e.printStackTrace();
			createNewErrorMessage(school.getName(),"invoice.CouldNotFindSchoolCategory");
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

	private SchoolHome getSchoolHome() throws RemoteException {
		return (SchoolHome) IDOLookup.getHome(School.class);
	}

	protected SchoolCategoryHome getSchoolCategoryHome() throws RemoteException {
		return (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
	}

	private RegulationSpecTypeHome getRegulationSpecTypeHome() throws RemoteException {
		return (RegulationSpecTypeHome) IDOLookup.getHome(RegulationSpecType.class);
	}

	protected ChildCareApplicationHome getChildCareApplicationHome() throws RemoteException {
		return (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
	}

}
