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
import se.idega.idegaweb.commune.accounting.regulations.business.PaymentFlowConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.RuleTypeConstant;
import se.idega.idegaweb.commune.accounting.school.data.Provider;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplicationHome;

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
	
	public PaymentThreadElementarySchool(Date month, IWContext iwc){
		super(month,iwc);
	}
	
	public void run(){
		Date currentDate = new Date( System.currentTimeMillis());
		Collection regulationArray = new ArrayList();
//		Collection applications;
		ArrayList conditions = new ArrayList();
		boolean first;
		ExportDataMapping categoryPosting;
//		PostingDetail postingDetail;

		try {
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
//								ChildCareApplication application = (ChildCareApplication) applicationIter.next();
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
//								postingDetail = regBus.getPostingDetailForContract(
//									0.0f,application.getContract());
//								createPaymentRecord(postingDetail,ownPosting,doublePosting);
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
		}
	}
	
	private SchoolHome getSchoolHome() throws RemoteException {
		return (SchoolHome) IDOLookup.getHome(School.class);
	}

	private SchoolCategoryHome getSchoolCategoryHome() throws RemoteException {
		return (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
	}

	private ChildCareApplicationHome getChildCareApplicationHome() throws RemoteException {
		return (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
	}

}
