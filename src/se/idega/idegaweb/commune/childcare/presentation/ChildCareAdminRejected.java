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
import com.idega.user.data.User;
import com.idega.util.IWCalendar;
import com.idega.util.PersonalIDFormatter;

/**
 * @author laddi
 */
public class ChildCareAdminRejected extends ChildCareBlock {

	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		if (getSession().hasPrognosis()) {
			add(getApplicationTable(iwc));
		}
		else {
			add(getSmallErrorText(localize("child_care.prognosis_must_be_set","Prognosis must be set or updated before you can continue!")));
		}
	}
	
	private Table getApplicationTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(5);
		table.setRowColor(1, getHeaderColor());
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
					table.add(getSmallText(rejectDate.getLocaleDate(IWCalendar.SHORT)), column++, row++);
				else
					row++;
			}
			table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
			table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
			table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
			table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);
		}
			
		return table;
	}
}