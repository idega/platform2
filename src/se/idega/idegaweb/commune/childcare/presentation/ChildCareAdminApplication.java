package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;

import com.idega.core.data.Address;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.GenericButton;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * @author laddi
 */
public class ChildCareAdminApplication extends ChildCareBlock {

	private User child;
	private ChildCareApplication application;
	
	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		Table table = new Table(1,5);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());
		table.setHeight(2, 12);
		table.setHeight(4, 12);
		
		if (getSession().getChildID() != -1 && getSession().getApplicationID() != -1) {
			table.add(getInformationTable(iwc), 1, 1);
			table.add(getApplicationTable(iwc), 1, 3);
			table.add(getButtonTable(true), 1, 5);
		}
		else {
			table.add(this.getLocalizedHeader("child_care.no_child_or_application_found","No child or application found."), 1, 1);
			table.add(getButtonTable(false), 1, 3);
		}
		
		add(table);
	}

	protected Table getInformationTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setColumns(3);
		table.setWidth(1, "100");
		table.setWidth(2, "6");
		int row = 1;
		
		child = getBusiness().getUserBusiness().getUser(getSession().getChildID());
		application = getBusiness().getApplication(getSession().getApplicationID());
		if (child != null) {
			Address address = getBusiness().getUserBusiness().getUsersMainAddress(child);
			Collection parents = getBusiness().getUserBusiness().getParentsForChild(child);
			
			table.add(getLocalizedSmallHeader("child_care.child","Child"), 1, row);
			table.add(getSmallText(child.getNameLastFirst(true)), 3, row);
			table.add(getSmallText(" - "), 3, row);
			table.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), 3, row++);
			
			if (address != null) {
				table.add(getLocalizedSmallHeader("child_care.address","Address"), 1, row);
				table.add(getSmallText(address.getStreetAddress()), 3, row++);
			}
			
			table.setHeight(row++, 12);
			
			if (parents != null) {
				table.add(getLocalizedSmallHeader("child_care.parents","Parents"), 1, row);
				Iterator iter = parents.iterator();
				while (iter.hasNext()) {
					User parent = (User) iter.next();
					address = getBusiness().getUserBusiness().getUsersMainAddress(parent);

					table.add(getSmallText(parent.getNameLastFirst(true)), 3, row);
					table.add(getSmallText(" - "), 3, row);
					table.add(getSmallText(PersonalIDFormatter.format(parent.getPersonalID(), iwc.getCurrentLocale())), 3, row++);
			
					if (address != null) {
						table.add(getSmallText(address.getStreetAddress()), 3, row++);
					}
			
					table.setHeight(row++, 12);
				}
			}
			
			if (application != null) {
				if (application.getPresentation() != null) {
					table.add(getLocalizedSmallHeader("child_care.message","Message"), 1, row);
					table.add(getSmallText(application.getPresentation()), 3, row);
				}
			}
		}
		
		return table;
	}
	
	protected Table getApplicationTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(5);
		table.setRowColor(1, getHeaderColor());
		int row = 1;
		int column = 1;
			
		table.add(getLocalizedSmallHeader("child_care.provider","Provider"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.status","Status"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.phone","Phone"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.queue_date","Queue date"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.placement_date","Placement date"), column, row++);
			
		ChildCareApplication application;
		String phone;
		IWTimestamp queueDate;
		IWTimestamp placementDate;
		boolean isCurrentProvider = false;

		Collection applications = getBusiness().getApplicationsForChild(child);
		Iterator iter = applications.iterator();
		while (iter.hasNext()) {
			column = 1;
			application = (ChildCareApplication) iter.next();
			phone = getBusiness().getSchoolBusiness().getSchoolPhone(application.getProviderId());
			queueDate = new IWTimestamp(application.getCreated());
			placementDate = new IWTimestamp(application.getFromDate());
			if (application.getProviderId() == getSession().getChildCareID())
				isCurrentProvider = true;
			else
				isCurrentProvider = false;
				
			if (row % 2 == 0)
				table.setRowColor(row, getZebraColor1());
			else
				table.setRowColor(row, getZebraColor2());

			table.add(getText(application.getProvider().getSchoolName(), isCurrentProvider), column++, row);
			table.add(getText(getBusiness().getLocalizedCaseStatusDescription(application.getCaseStatus(), iwc.getCurrentLocale()), isCurrentProvider), column++, row);
			if (phone != null)
				table.add(getText(phone, isCurrentProvider), column, row);
			column++;
			table.add(getText(queueDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT), isCurrentProvider), column++, row);
			table.add(getText(placementDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT), isCurrentProvider), column++, row++);
		}
		
		return table;
	}
	
	protected Table getButtonTable(boolean showAllButtons) throws RemoteException {
		Table table = new Table(7,1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(2, "4");
		table.setWidth(4, "4");
		table.setWidth(6, "4");
		
		GenericButton back = (GenericButton) getStyledInterface(new GenericButton("back",localize("back","Back")));
		back.setPageToOpen(getResponsePage());
		table.add(back, 1, 1);
		
		if (showAllButtons) {
			int numberInQueue = getBusiness().getNumberInQueueByStatus(application);
			boolean hasPriority = application.getHasPriority();
			char status = application.getApplicationStatus();

			GenericButton offer = getButton("offer", localize("child_care.offer_placing","Offer placing"), ChildCareAdminWindow.METHOD_OFFER);
			GenericButton priority = getButton("priority", localize("child_care.grant_priority","Grant priority"), ChildCareAdminWindow.METHOD_GRANT_PRIORITY);
			GenericButton changeDate = getButton("change_date", localize("child_care.change_date","Change date"), ChildCareAdminWindow.METHOD_CHANGE_DATE);

			if (status == getBusiness().getStatusSentIn()) {
				if (numberInQueue == 1 || hasPriority) {
					table.add(offer, 3, 1);
					table.add(changeDate, 5, 1);
				}
				else
					table.add(priority, 3, 1);
			}
			else if (status == getBusiness().getStatusAccepted()) {
			}
			
		}
		
		return table;
	}
	
	protected GenericButton getButton(String name, String value, int method) {
		GenericButton button = (GenericButton) getStyledInterface(new GenericButton(name, value));
		button.setWindowToOpen(ChildCareWindow.class);
		button.addParameterToWindow(ChildCareAdminWindow.PARAMETER_APPLICATION_ID, String.valueOf(getSession().getApplicationID()));
		button.addParameterToWindow(ChildCareAdminWindow.PARAMETER_USER_ID, String.valueOf(getSession().getChildID()));
		button.addParameterToWindow(ChildCareAdminWindow.PARAMETER_METHOD, method);
		button.addParameterToWindow(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
		
		return button;
	}
	
	protected Text getText(String text, boolean isCurrentProvider) {
		if (isCurrentProvider)
			return getSmallHeader(text);
		else
			return getSmallText(text);
	}
}