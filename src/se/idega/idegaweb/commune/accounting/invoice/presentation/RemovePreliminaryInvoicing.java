package se.idega.idegaweb.commune.accounting.invoice.presentation;

import java.rmi.RemoteException;

import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.accounting.invoice.business.BatchAlreadyRunningException;
import se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness;
import se.idega.idegaweb.commune.accounting.invoice.business.SchoolCategoryNotFoundException;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessHome;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;

import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.InputContainer;
import com.idega.util.CalendarMonth;
import com.idega.util.IWTimestamp;

/**
 * Makes it possible to remove all preliminary billing and invoicing information 
 * according the the timeperiod and School category selected.
 * 
 * @author Joakim
 * 
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness
 */
public class RemovePreliminaryInvoicing  extends AccountingBlock{
	
	private static String PREFIX="cacc_removepi_";
	private static String PARAM_MONTH=PREFIX+"month";

	public void init(IWContext iwc){
		OperationalFieldsMenu opFields = new OperationalFieldsMenu();
		String schoolCategory=null;
		  
		try {
			schoolCategory = getSession().getOperationalField();
		} catch (RemoteException e) {
			e.printStackTrace();
			add(new ExceptionWrapper(e, this));
		}

		handleAction(schoolCategory, iwc);
		
		Form form = new Form();
		
		add(opFields);
		add(form);
		
		DateInput monthInput = (DateInput) iwc.getApplicationAttribute(PARAM_MONTH+iwc.getCurrentUserId());
		if (monthInput == null) {
			monthInput = new DateInput(PARAM_MONTH);
			monthInput.setToCurrentDate();	
			monthInput.setToShowDay(false);
			int currentYear = java.util.Calendar.getInstance ().get (java.util.Calendar.YEAR);
			monthInput.setYearRange(currentYear - 1, currentYear + 1);	
									
			iwc.setApplicationAttribute(PARAM_MONTH+iwc.getCurrentUserId(), monthInput);						
		}
		String date = iwc.getParameter(PARAM_MONTH);
		if(date!=null){
			monthInput.setDate(new IWTimestamp(date).getDate());
		}

		InputContainer month = getInputContainer(PARAM_MONTH,"Month", monthInput);
		form.add(month);

		GenericButton saveButton = this.getSaveButton();
		form.add(saveButton);
	}
	
	/**
	 * @param iwc
	 */
	private void handleAction(String schoolCategory, IWContext iwc) {
		if(iwc.isParameterSet(PARAM_SAVE)){
			String date = iwc.getParameter(PARAM_MONTH);
			CalendarMonth month = new CalendarMonth(new IWTimestamp(date));
			handleSave(schoolCategory, month, iwc);
		}
	}
	
	/**
	 * @param iwc
	 */
	private void handleSave(String schoolCategory, CalendarMonth month, IWContext iwc) {
		try {
			InvoiceBusiness invoiceBusiness = (InvoiceBusiness)IBOLookup.getServiceInstance(iwc, InvoiceBusiness.class);
/*			
			SchoolCategoryHome sch = (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
			if (sch.findChildcareCategory().getCategory().equals(schoolCategory)) {
				if(BatchRunSemaphore.getChildcareRunSemaphore()){
					invoiceBusiness.removePreliminaryInvoice(month, schoolCategory);
					add(this.localize(PREFIX+"records_removed","Records have been removed."));
					BatchRunSemaphore.releaseChildcareRunSemaphore();
				}else{
					add(this.localize(PREFIX+"data_of_this_type_is_being_updated_by_the_system_Please_wait_until_it_is_done_and_try_again",
							"Data of this type is being updated by the system. Please wait until it is done and try again."));
				}
			} else if (sch.findElementarySchoolCategory().getCategory().equals(schoolCategory)) {
				if(BatchRunSemaphore.getElementaryRunSemaphore()){
					invoiceBusiness.removePreliminaryInvoice(month, schoolCategory);
					add(this.localize(PREFIX+"records_removed","Records have been removed."));
					BatchRunSemaphore.releaseElementaryRunSemaphore();
				}else{
					add(this.localize(PREFIX+"data_of_this_type_is_being_updated_by_the_system_Please_wait_until_it_is_done_and_try_again",
					"Data of this type is being updated by the system. Please wait until it is done and try again."));
				}
			} else if (sch.findHighSchoolCategory().getCategory().equals(schoolCategory)) {
				if(BatchRunSemaphore.getHighRunSemaphore()){
					invoiceBusiness.removePreliminaryInvoice(month, schoolCategory);
					add(this.localize(PREFIX+"records_removed","Records have been removed."));
					BatchRunSemaphore.releaseHighRunSemaphore();
				}else{
					throw new BatchAlreadyRunningException("HighSchool");
				}
			} else {
				throw new SchoolCategoryNotFoundException("Couldn't find any Schoolcategory for billing named " + schoolCategory);
			}
*/			
			
			invoiceBusiness.removePreliminaryInvoice(month, schoolCategory);
			invoiceBusiness.removePreliminaryPayment(month, schoolCategory);
			add(this.localize(PREFIX+"records_removed","Records have been removed."));
			
			
		} catch (RemoveException e) {
			add(this.localize(PREFIX+"There_are_records_with_status_'Locked'_and/or_'History',_therefore_deletes_are_not_allowed","There are records with status 'Locked' and/or 'History', therefore deletes are not allowed."));
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e));
		} catch (BatchAlreadyRunningException e) {
			add(this.localize(PREFIX+PREFIX+"data_of_this_type_is_being_updated_by_the_system_Please_wait_until_it_is_done_and_try_again",
					"Data of this type is being updated by the system. Please wait until it is done and try again."));
		} catch (SchoolCategoryNotFoundException e) {
			add(this.localize(PREFIX+"Please_select_school_category","Please select school category"));
		} catch (Exception e) {
			add(new ExceptionWrapper(e));
		}
	}

	public PostingBusinessHome getPostingBusinessHome() throws RemoteException {
		return (PostingBusinessHome) IDOLookup.getHome(PostingBusiness.class);
	}
}
