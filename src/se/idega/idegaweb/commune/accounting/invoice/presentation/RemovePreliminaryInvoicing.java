package se.idega.idegaweb.commune.accounting.invoice.presentation;

import java.rmi.RemoteException;
import java.sql.Date;

import se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness;
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
		
		DateInput monthInput = new DateInput(PARAM_MONTH,true);
		monthInput.setToCurrentDate();
		monthInput.setToShowDay(false);
		
		InputContainer month = getInputContainer(PARAM_MONTH,"Month", monthInput);
		form.add(month);

		GenericButton saveButton = this.getSaveButton();
//		GenericButton cancelButton = this.getCancelButton();
		form.add(saveButton);
//		form.add(cancelButton);
	}
	
	/**
	 * @param iwc
	 */
	private void handleAction(String schoolCategory, IWContext iwc) {
		if(iwc.isParameterSet(PARAM_SAVE)){
			handleSave(schoolCategory, iwc);
		}
	}
	
	/**
	 * @param iwc
	 */
	private void handleSave(String schoolCategory, IWContext iwc) {
		try {
			InvoiceBusiness invoiceBusiness = (InvoiceBusiness)IBOLookup.getServiceInstance(iwc, InvoiceBusiness.class);
			invoiceBusiness.removePreliminaryInvoice(new Date(System.currentTimeMillis()), schoolCategory);
			add(this.localize(PREFIX+"records_removed","Records have been removed."));
		} catch (Exception e) {
			add(new ExceptionWrapper(e));
		}
	}

	public PostingBusinessHome getPostingBusinessHome() throws RemoteException {
		return (PostingBusinessHome) IDOLookup.getHome(PostingBusiness.class);
	}
}
