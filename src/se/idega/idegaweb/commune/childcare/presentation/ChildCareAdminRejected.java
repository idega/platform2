/*
 * Created on 8.4.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import is.idega.block.family.business.FamilyLogic;
import is.idega.idegaweb.member.business.NoCustodianFound;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.event.ChildCareEventListener;

import com.idega.core.contact.data.Email;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.business.NoEmailFoundException;
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
			
			Table table = new Table(1, 3);
			table.setCellpadding(0);
			table.setCellspacing(0);
			table.setWidth(getWidth());
			table.setHeight(2, 12);
			if (useStyleNames()) {
				table.setCellpaddingLeft(1, 1, 12);
				table.setCellpaddingRight(1, 1, 12);
			}
			
			table.add(getNavigationForm(), 1, 1);
			table.add(getApplicationTable(iwc), 1, 3);
			
			add(table);
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
		form.add(new HiddenInput(PARAMETER_APPLICATION_ID, ""));
		
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(7);
		if (useStyleNames()) {
			table.setRowStyleClass(1, getHeaderRowClass());
		}
		else {
			table.setRowColor(1, getHeaderColor());
		}
		form.add(table);
		int row = 1;
		int column = 1;
			
		if (useStyleNames()) {
			table.setCellpaddingLeft(1, row, 12);
			table.setCellpaddingRight(table.getColumns(), row, 12);
		}
		table.add(getLocalizedSmallHeader("child_care.name","Name"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.personal_id","Personal ID"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.status","Status"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.queue_date","Queue date"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.placement_date","Placement date"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.reject_date","Reject date"), column++, row++);
				
		Collection applications = null;
		if (getSession().getStatus() != null) {
			applications = getBusiness().getApplicationsByProviderAndApplicationStatus(getSession().getChildCareID(), getSession().getStatus());
		}
		else {
			applications = getBusiness().getInactiveApplicationsByProvider(getSession().getChildCareID());
		}
		if (applications != null && !applications.isEmpty()) {
			ChildCareApplication application;
			User child;
			IWCalendar queueDate;
			IWCalendar placementDate;
			IWCalendar rejectDate;
			SubmitButton activateApplication;
			int numberOfChildren = applications.size();
			String name = null;
			Link emailLink = null;
			String emails = null;
			Email email = null;
			
			Iterator iter = applications.iterator();
			while (iter.hasNext()) {
				column = 1;
				application = (ChildCareApplication) iter.next();
				child = application.getChild();
				emailLink = null;
				email = null;
				emails = "";
				
				if (application.getQueueDate() != null)
					queueDate = new IWCalendar(iwc.getCurrentLocale(), application.getQueueDate());
				else	
					queueDate = null;
				placementDate = new IWCalendar(iwc.getCurrentLocale(), application.getCreated());
				if (application.getRejectionDate() != null)
					rejectDate = new IWCalendar(iwc.getCurrentLocale(), application.getRejectionDate());
				else	
					rejectDate = null;
						
				if (useStyleNames()) {
					if (row % 2 == 0) {
						table.setRowStyleClass(row, getDarkRowClass());
					}
					else {
						table.setRowStyleClass(row, getLightRowClass());
					}
					table.setCellpaddingLeft(1, row, 12);
					table.setCellpaddingRight(table.getColumns(), row, 12);
				}
				else {
					if (row % 2 == 0)
						table.setRowColor(row, getZebraColor1());
					else
						table.setRowColor(row, getZebraColor2());
				}

				//// email to parent
				try {
					Collection parents = getMemberFamilyLogic(iwc).getCustodiansFor(child);
					
					if (parents != null && !parents.isEmpty()) {
						Iterator iterPar = parents.iterator();
						while (iterPar.hasNext()) {
							User parent = (User) iterPar.next();
							
							try {
								email = getCommuneUserBusiness(iwc).getUsersMainEmail(parent);
								if (email != null && email.getEmailAddress() != null && !email.getEmailAddress().equals(" ")) {
									emailLink = this.getSmallLink(email.getEmailAddress());
									if (emails != null)
										emails = emails + "; " + email.getEmailAddress();
									else										
									emails = email.getEmailAddress();
									
									emailLink.setURL("mailto:" + emails);
								}
																
							}
							catch (NoEmailFoundException nef) {
								log(nef);
							}
							
						}
					}
				}catch (NoCustodianFound ncf) {
				}
				
				name = getBusiness().getUserBusiness().getNameLastFirst(child, true);
				if (emailLink != null){
					emailLink.setText(name);
					table.add(emailLink, column++, row);				
					
				} else {
					table.add(getSmallText(name), column++, row);
				}
				
				table.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), column++, row);
				table.add(getSmallText(getStatusString(application)), column++, row);
				if (queueDate != null)
					table.add(getSmallText(queueDate.getLocaleDate(IWCalendar.SHORT)), column, row);
				column++;
				table.add(getSmallText(placementDate.getLocaleDate(IWCalendar.SHORT)), column++, row);
				if (rejectDate != null)
					table.add(getSmallText(rejectDate.getLocaleDate(IWCalendar.SHORT)), column, row);
				column++;
				if (application.getApplicationStatus() == getBusiness().getStatusCancelled() || ((application.getApplicationStatus() == getBusiness().getStatusNotAnswered() || application.getApplicationStatus() == getBusiness().getStatusTimedOut()) && application.getQueueDate() != null)) {
					String description = null;
					if (application.getApplicationStatus() == getBusiness().getStatusNotAnswered() || application.getApplicationStatus() == getBusiness().getStatusTimedOut()) {
						description = localize("child_care.activate_application", "Click to reactivate application");
					}
					else if (application.getApplicationStatus() == getBusiness().getStatusCancelled()) {
						description = localize("child_care.reactivate_placement", "Click to reactivate placement");
					}
					
					activateApplication = new SubmitButton(getEditIcon(description));
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
			
			table.setHeight(row++, 6);
			table.mergeCells(1, row, table.getColumns(), row);
			if (useStyleNames()) {
				table.setCellpaddingLeft(1, row, 12);
			}
			table.add(getSmallText(localize("child_care.number_of_rejected_applications", "Number of rejected applications: ") + String.valueOf(numberOfChildren)), 1, row);
		}
			
		return form;
	}
	
	private Form getNavigationForm() throws RemoteException {
		Form form = new Form();
		form.setEventListener(ChildCareEventListener.class);
		
		DropdownMenu statuses = getRejectedStatuses();
		statuses.setToSubmit();
		form.add(statuses);
		
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
	private CommuneUserBusiness getCommuneUserBusiness(IWContext iwc) throws RemoteException {
		return (CommuneUserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}
	private FamilyLogic getMemberFamilyLogic(IWContext iwc) throws RemoteException {
		return (FamilyLogic) com.idega.business.IBOLookup.getServiceInstance(iwc, FamilyLogic.class);
	}
}