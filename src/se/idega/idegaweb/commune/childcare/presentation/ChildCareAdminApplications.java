/*
 * Created on 26.11.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.event.ChildCareEventListener;

import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * @author laddi
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ChildCareAdminApplications extends ChildCareBlock {

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		Table applicationTable = new Table();
		applicationTable.setWidth(getWidth());
		applicationTable.setCellpadding(getCellpadding());
		applicationTable.setCellspacing(getCellspacing());
		applicationTable.setColumns(5);
		applicationTable.setRowColor(1, getHeaderColor());
		applicationTable.setWidth(5, 12);
		int row = 1;
		int column = 1;
		
		applicationTable.add(getLocalizedSmallHeader("child_care.name","Name"), column++, row);
		applicationTable.add(getLocalizedSmallHeader("child_care.personal_id","Personal ID"), column++, row);
		applicationTable.add(getLocalizedSmallHeader("child_care.address","Address"), column++, row);
		applicationTable.add(getLocalizedSmallHeader("child_care.phone","Phone"), column, row++);
		applicationTable.add(getLocalizedSmallHeader("child_care.queue_date","Queue date"), column, row++);
		applicationTable.add(getLocalizedSmallHeader("child_care.placement_date","Placement date"), column++, row++);
		
		Collection applications = getApplicationCollection();
		if (applications != null && !applications.isEmpty()) {
			ChildCareApplication application;
			User child;
			Address address;
			Phone phone;
			Link link;
			Link viewContract;
			boolean hasContract = false;
			IWTimestamp queueDate;
			IWTimestamp placementDate;
			
			Iterator iter = applications.iterator();
			while (iter.hasNext()) {
				column = 1;
				application = (ChildCareApplication) iter.next();
				child = application.getChild();
				address = getBusiness().getUserBusiness().getUsersMainAddress(child);
				phone = getBusiness().getUserBusiness().getChildHomePhone(child);
				if (application.getQueueDate() != null) {
					queueDate = new IWTimestamp(application.getQueueDate());
				}
				else {
					queueDate = null;
				}
				if (application.getFromDate() != null) {
					placementDate = new IWTimestamp(application.getFromDate());
				}
				else {
					placementDate = null;
				}
				
				if (application.getApplicationStatus() == getBusiness().getStatusContract()) {
					hasContract = true;
					applicationTable.setRowColor(row, CONTRACT_COLOR);
				}
				else {
					hasContract = false;
					if (row % 2 == 0)
						applicationTable.setRowColor(row, getZebraColor1());
					else
						applicationTable.setRowColor(row, getZebraColor2());
				}
				
				link = getSmallLink(child.getNameLastFirst(true));
				link.setEventListener(ChildCareEventListener.class);
				link.setParameter(getSession().getParameterUserID(), String.valueOf(application.getChildId()));
				link.setParameter(getSession().getParameterApplicationID(), application.getPrimaryKey().toString());
				if (getResponsePage() != null)
					link.setPage(getResponsePage());
				
				applicationTable.add(link, column++, row);
				applicationTable.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), column++, row);
				if (address != null)
					applicationTable.add(getSmallText(address.getStreetAddress()), column++, row);
				else
					applicationTable.add(getSmallText("-"), column++, row);
				if (phone != null)
					applicationTable.add(getSmallText(phone.getNumber()), column++, row);
				else
					applicationTable.add(getSmallText("-"), column++, row);
				if (queueDate != null)
					applicationTable.add(getSmallText(queueDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
				else
					applicationTable.add(getSmallText("-"), column++, row);
				if (placementDate != null)
					applicationTable.add(getSmallText(placementDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
				else
					applicationTable.add(getSmallText("-"), column++, row);
				if (hasContract) {
					viewContract = new Link(getPDFIcon(localize("child_care.view_contract","View contract")));
					viewContract.setFile(application.getContractFileId());
					viewContract.setTarget(Link.TARGET_NEW_WINDOW);
					applicationTable.add(viewContract, column, row);
				}
				row++;
			}
			applicationTable.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
			applicationTable.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
		}
		
		add(applicationTable);
	}
	
	private Collection getApplicationCollection() throws RemoteException {
		Collection applications = getBusiness().findUnhandledApplicationsNotInCommune();
		return applications;
	}
}