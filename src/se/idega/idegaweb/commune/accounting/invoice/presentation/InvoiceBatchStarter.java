package se.idega.idegaweb.commune.accounting.invoice.presentation;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;

import com.idega.business.IBOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.InputContainer;
import com.idega.util.IWTimestamp;

/**
 * Starts the batch run that will create billing and invoicing information
 * according to the parameters set in the UI.
 * 
 * @author Joakim
 * 
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness
 * @see se.idega.idegaweb.commune.accounting.invoice.business.BillingThread
 */
public class InvoiceBatchStarter extends AccountingBlock{
	
	private static String PREFIX="cacc_invbs_";
	private static String PARAM_MONTH=PREFIX+"month";
	private static String PARAM_READ_DATE=PREFIX+"read_date";
	DateInput monthInput;
	DateInput dateInput;
	DateInput readDateInput;	

	public void init(IWContext iwc){
	
		String schoolCategory=null;
		OperationalFieldsMenu opFields = new OperationalFieldsMenu();
		try {
			schoolCategory = getSession().getOperationalField();
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e, this));
			e.printStackTrace();
		}

		handleAction(iwc,schoolCategory);
		
		add(opFields);

		Form form = new Form();
		add(form);
		
		monthInput = new DateInput(PARAM_MONTH,true);
		monthInput.setToCurrentDate();
		monthInput.setToShowDay(false);
		
		InputContainer month = getInputContainer(PARAM_MONTH,"Month", monthInput);

		readDateInput = new DateInput(PARAM_READ_DATE,true);	

		InputContainer readDate = getInputContainer(PARAM_READ_DATE,"Read date", readDateInput);
		try {
			InvoiceBusiness invoiceBusiness = (InvoiceBusiness)IBOLookup.getServiceInstance(iwc, InvoiceBusiness.class);
			if(invoiceBusiness.isHighShool(schoolCategory)){
				form.add(readDate);
			}else{
				form.add(month);
			}
		} catch (IDOLookupException e) {
			add(new ExceptionWrapper(e, this));
			e.printStackTrace();
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e, this));
			e.printStackTrace();
		} catch (FinderException e) {
			add(new ExceptionWrapper(e, this));
			e.printStackTrace();
		}
		
		GenericButton saveButton = this.getSaveButton();
		GenericButton cancelButton = this.getCancelButton();
		form.add(saveButton);
		form.add(cancelButton);
	}
	
	/**
	 * @param iwc
	 */
	private void handleAction(IWContext iwc, String schoolCategory) {
		if(iwc.isParameterSet(PARAM_SAVE)){
			handleSave(iwc, schoolCategory);
		}
	}
	
	/**
	 * @param iwc
	 */
	private void handleSave(IWContext iwc, String schoolCategory) {
		try {
			InvoiceBusiness invoiceBusiness = (InvoiceBusiness)IBOLookup.getServiceInstance(iwc, InvoiceBusiness.class);
			invoiceBusiness.startPostingBatch(new IWTimestamp(iwc.getParameter(PARAM_MONTH)).getDate(), schoolCategory, iwc);
		} catch (Exception e) {
			add(new ExceptionWrapper(e));
		}
	}
}
