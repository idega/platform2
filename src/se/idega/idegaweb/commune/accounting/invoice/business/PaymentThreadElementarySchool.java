package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
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
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecTypeHome;
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

/**
 * @author Joakim
 *
 */
public class PaymentThreadElementarySchool extends BillingThread{
	PaymentHeader paymentHeader;
	Date currentDate = new Date( System.currentTimeMillis());
	
	public PaymentThreadElementarySchool(Date month, IWContext iwc){
		super(month,iwc);
	}
	
	public void run(){
		//Create all the billing info derrived from the contracts
		contracts();
		//Create all the billing info derrived from the regular payments
		regularPayment();
		//VAT
		calcVAT();
	}
	
	private void contracts(){
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
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PostingParametersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PostingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MissingMandatoryFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates all the invoice headers, invoice records, payment headers and payment records
	 * for the Regular payments
	 */
	private void regularPayment(){
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

	private SchoolCategoryHome getSchoolCategoryHome() throws RemoteException {
		return (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
	}

	private RegulationSpecTypeHome getRegulationSpecTypeHome() throws RemoteException {
		return (RegulationSpecTypeHome) IDOLookup.getHome(RegulationSpecType.class);
	}

	private ChildCareApplicationHome getChildCareApplicationHome() throws RemoteException {
		return (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
	}

}
