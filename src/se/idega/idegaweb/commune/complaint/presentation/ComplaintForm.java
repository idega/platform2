package se.idega.idegaweb.commune.complaint.presentation;

import com.idega.block.process.data.CaseCode;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;

import se.idega.idegaweb.commune.complaint.business.ComplaintBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * @author laddi
 */

public class ComplaintForm extends CommuneBlock {

	//private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";

	private final static int ACTION_VIEW_FORM = 1;
	private final static int ACTION_FORM_SUBMITTED = 2;
	private final static String PARAM_FORM_SUBMITTED = "co_form_submit";

	private final String PARAM_COMPLAINT_TYPE = "co_type";
	private final String PARAM_COMPLAINT = "co_complaint";
	private final String PARAM_DESCRIPTION = "co_description";

	public ComplaintForm() {
	}

	public void main(IWContext iwc) {
		this.setResourceBundle(getResourceBundle(iwc));

		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_VIEW_FORM :
					viewForm(iwc);
					break;
				case ACTION_FORM_SUBMITTED :
					formSubmitted(iwc);
					break;
				default :
					break;
			}
		}
		catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}

	private int parseAction(IWContext iwc) {
		int action = ACTION_VIEW_FORM;

		if (iwc.isParameterSet(PARAM_FORM_SUBMITTED)) {
			action = ACTION_FORM_SUBMITTED;
		}

		return action;
	}

	private void viewForm(IWContext iwc) {
		Form form = new Form();
		form.add(new HiddenInput(PARAM_FORM_SUBMITTED, "true"));

		Table table = new Table(1,4);
		table.setWidth(600);
		table.setCellspacing(0);
		table.setCellpadding(14);
		table.setColor(getBackgroundColor());
		int row = 1;
		
		TextInput textInput = new TextInput(PARAM_COMPLAINT);
		textInput.setLength(30);
		TextArea textArea = new TextArea(PARAM_DESCRIPTION);
		textArea.setWidth(Table.HUNDRED_PERCENT);
		textArea.setHeight("100");
		SubmitButton submit = new SubmitButton(getResourceBundle().getLocalizedString("complaint.submit","Submit"));
		submit.setAsImageButton(true);
		
		table.add(getLocalizedHeader("complaint.complaint_type", "The complaint regards"),1,1);
		table.add(new Break(),1,row);
		table.add(getTypeDropdown(iwc),1,row++);
		table.add(getLocalizedHeader("complaint.complaint", "Complaint"),1,row);
		table.add(new Break(),1,row);
		table.add(textInput,1,row++);
		table.add(getLocalizedHeader("complaint.description", "Details"),1,row);
		table.add(new Break(),1,row);
		table.add(textArea,1,row++);
		table.add(submit,1,row);
		
		form.add(table);
		add(form);
	}

	private void formSubmitted(IWContext iwc) throws CreateException,RemoteException {
		CaseCode complaintType = null;
		try {
			complaintType = getComplaintBusiness(iwc).getCaseCode(iwc.getParameter(PARAM_COMPLAINT_TYPE));
		}
		catch (FinderException e) {
			complaintType = null;
		}

		String complaint = iwc.getParameter(PARAM_COMPLAINT);
		String description = iwc.getParameter(PARAM_DESCRIPTION);

		getComplaintBusiness(iwc).createComplaint(complaint, description, complaintType, iwc.getCurrentUser());	
		add(this.getLocalizedHeader("complaint.complaint_submitted", "Complaint sent"));
		add(new Break(2));
		
		viewForm(iwc);
	}

	private DropdownMenu getTypeDropdown(IWContext iwc) {
		DropdownMenu dropdown = new DropdownMenu(PARAM_COMPLAINT_TYPE);
		dropdown.addMenuElementFirst("-1", "");
			
		try {
			Collection collection = getComplaintBusiness(iwc).findAllComplaintTypes();

			Iterator iter = collection.iterator();
			while (iter.hasNext()) {
				CaseCode code = (CaseCode) iter.next();
				dropdown.addMenuElement(code.getPrimaryKey().toString(), getComplaintBusiness(iwc).getLocalizedCaseDescription(code,iwc.getCurrentLocale()));
			}
		}
		catch (FinderException fe) {
			fe.printStackTrace(System.err);
		}
		catch (RemoteException re) {
			re.printStackTrace(System.err);
		}

		return dropdown;
	}

	private ComplaintBusiness getComplaintBusiness(IWContext iwc) throws RemoteException {
		return (ComplaintBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ComplaintBusiness.class);
	}

	/* Commented out since it is never used...
	private MessageBusiness getMessageBusiness(IWContext iwc) throws RemoteException {
		return (MessageBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, MessageBusiness.class);
	}*/
}
