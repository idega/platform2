package se.idega.idegaweb.commune.accounting.invoice.presentation;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessHome;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.InputContainer;
import com.idega.user.data.User;

/**
 * @author Joakim
 *
 */
public class InvoiceBatchStarter extends AccountingBlock{
	
	private static String PREFIX="cacc_invbs_";
	private static String PARAM_MONTH=PREFIX+"month";
	private static String PARAM_READ_DATE=PREFIX+"read_date";

	public void init(IWContext iwc){
		
		handleAction(iwc);
		
		Form form = new Form();
		add(form);
		
		DateInput monthInput = new DateInput(PARAM_MONTH,true);
		monthInput.setToCurrentDate();
		monthInput.setToShowDay(false);
		
		InputContainer month = getInputContainer(PARAM_MONTH,"Month", monthInput);
		form.add(month);

		DateInput readDateInput = new DateInput(PARAM_READ_DATE,true);	

		InputContainer readDate = getInputContainer(PARAM_READ_DATE,"Read date", readDateInput);
		form.add(readDate);
		
		GenericButton saveButton = this.getSaveButton();
		GenericButton cancelButton = this.getCancelButton();
		form.add(saveButton);
		form.add(cancelButton);
	}
	
	/**
	 * @param iwc
	 */
	private void handleAction(IWContext iwc) {
		if(iwc.isParameterSet(PARAM_SAVE)){
			handleSave(iwc);
		}
	}
	
	/**
	 * @param iwc
	 */
	private void handleSave(IWContext iwc) {
		// TODO Auto-generated method stub
		User user = iwc.getCurrentUser();
		add("Save pressed for user: "+user.getName());
	}

	public PostingBusinessHome getPostingBusinessHome() throws RemoteException {
		return (PostingBusinessHome) IDOLookup.getHome(PostingBusiness.class);
	}
}
