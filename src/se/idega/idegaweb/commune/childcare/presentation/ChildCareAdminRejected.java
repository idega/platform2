/*
 * Created on 8.4.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;
import com.idega.util.IWCalendar;
import com.idega.util.PersonalIDFormatter;

/**
 * @author laddi
 */
public class ChildCareAdminRejected extends ChildCareBlock {

	private static final String PARAMETER_APPLICATION_ID = "ccr_application_id";
	private boolean _requiresPrognosis = true;

	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		if (canSeeRejected()) {
			performAction(iwc);
			add(getApplicationTable(iwc));
		}
		else {
			add(getSmallErrorText(localize("child_care.prognosis_must_be_set","Prognosis must be set or updated before you can continue!")));
		}
	}
	
	protected boolean canSeeRejected() {
		boolean hasPrognosis = false;
		if (_requiresPrognosis) {
			try {
				hasPrognosis = getSession().hasPrognosis();
			}
			catch (RemoteException e) {
				hasPrognosis = false;
			}
		}
		else
			hasPrognosis = true;
		
		return hasPrognosis;
	}
	
	private Form getApplicationTable(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.setMethod("get");
		form.add(new HiddenInput(PARAMETER_APPLICATION_ID, ""));
		
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(6);
		table.setRowColor(1, getHeaderColor());
		form.add(table);
		int row = 1;
		int column = 1;
			
		table.add(getLocalizedSmallHeader("child_care.name","Name"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.personal_id","Personal ID"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.status","Status"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.placement_date","Placement date"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.reject_date","Reject date"), column++, row++);
				
		Collection applications = getBusiness().getInactiveApplicationsByProvider(getSession().getChildCareID());
		if (applications != null && !applications.isEmpty()) {
			ChildCareApplication application;
			User child;
			IWCalendar placementDate;
			IWCalendar rejectDate;
			SubmitButton activateApplication;
				
			Iterator iter = applications.iterator();
			while (iter.hasNext()) {
				column = 1;
				application = (ChildCareApplication) iter.next();
				child = application.getChild();
				placementDate = new IWCalendar(iwc.getCurrentLocale(), application.getCreated());
				if (application.getRejectionDate() != null)
					rejectDate = new IWCalendar(iwc.getCurrentLocale(), application.getRejectionDate());
				else	
					rejectDate = null;
						
				if (row % 2 == 0)
					table.setRowColor(row, getZebraColor1());
				else
					table.setRowColor(row, getZebraColor2());
					
				table.add(getSmallText(child.getNameLastFirst(true)), column++, row);
				table.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), column++, row);
				table.add(getSmallText(getStatusString(application)), column++, row);
				table.add(getSmallText(placementDate.getLocaleDate(IWCalendar.SHORT)), column++, row);
				if (rejectDate != null)
					table.add(getSmallText(rejectDate.getLocaleDate(IWCalendar.SHORT)), column, row);
				column++;
				if (application.getApplicationStatus() == getBusiness().getStatusCancelled() || (application.getApplicationStatus() == getBusiness().getStatusNotAnswered() && application.getQueueDate() != null)) {
					String description = null;
					if (application.getApplicationStatus() == getBusiness().getStatusNotAnswered()) {
						description = localize("child_care.activate_application", "Click to reactivate application");
					}
					else if (application.getApplicationStatus() == getBusiness().getStatusCancelled()) {
						description = localize("child_care.reactivate_placement", "Click to reactivate placement");
					}
					
					activateApplication = new SubmitButton(getDeleteIcon(description));
					activateApplication.setDescription(description);
					activateApplication.setValueOnClick(PARAMETER_APPLICATION_ID, application.getPrimaryKey().toString());
					activateApplication.setSubmitConfirm(localize("school.confirm_activation","Are you sure you want to reactivate this application?"));
					table.add(activateApplication, column, row++);
				}
				else
					row++;
			}
			table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
			table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
			table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
			table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);
		}
			
		return form;
	}
	
	private void performAction(IWContext iwc) throws RemoteException{
		if (iwc.isParameterSet(PARAMETER_APPLICATION_ID)) {
			int applicationID = Integer.parseInt(iwc.getParameter(PARAMETER_APPLICATION_ID));
			getBusiness().reactivateApplication(applicationID, iwc.getCurrentUser());
		}
	}
	
	/**
	 * @param requiresPrognosis The requiresPrognosis to set.
	 */
	public void setRequiresPrognosis(boolean requiresPrognosis) {
		this._requiresPrognosis = requiresPrognosis;
	}
}