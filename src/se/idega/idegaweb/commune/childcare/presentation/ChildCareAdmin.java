package se.idega.idegaweb.commune.childcare.presentation;

import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;

import com.idega.core.data.Address;
import com.idega.presentation.CollectionNavigator;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.user.data.User;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * @author laddi
 */
public class ChildCareAdmin extends ChildCareBlock {

	private int _numberPerPage = 15;

	/*
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		int applicantsSize = getBusiness().getNumberOfUnhandledApplicationsByProvider(getSession().getChildCareID());
	
		Table table = new Table(1,3);
		table.setWidth(getWidth());
		table.setHeight(2, 6);
		table.setCellpadding(0);
		table.setCellspacing(0);
		add(table);

		CollectionNavigator navigator = new CollectionNavigator(applicantsSize);
		navigator.setTextStyle(STYLENAME_SMALL_TEXT);
		navigator.setLinkStyle(STYLENAME_SMALL_LINK);
		navigator.setNumberOfEntriesPerPage(_numberPerPage);
		table.add(navigator, 1, 1);

		Collection applications = getBusiness().getUnhandledApplicationsByProvider(getSession().getChildCareID(), _numberPerPage, navigator.getStart(iwc));
		if (applications != null && !applications.isEmpty()) {
			ChildCareApplication application;
			User child;
			Address address;
			IWCalendar queueDate;
			IWCalendar placementDate;
			Link link;
			
			Table applicationTable = new Table();
			applicationTable.setWidth(Table.HUNDRED_PERCENT);
			applicationTable.setCellpadding(getCellpadding());
			applicationTable.setCellspacing(getCellspacing());
			applicationTable.setColumns(7);
			applicationTable.setRowColor(1, getHeaderColor());
			table.add(applicationTable, 1, 3);
			int row = 1;
			int column = 1;
			
			applicationTable.add(getLocalizedSmallHeader("child_care.name","Name"), column++, row);
			applicationTable.add(getLocalizedSmallHeader("child_care.personal_id","Personal ID"), column++, row);
			applicationTable.add(getLocalizedSmallHeader("child_care.address","Address"), column++, row);
			applicationTable.add(getLocalizedSmallHeader("child_care.queue_date","Queue date"), column++, row);
			applicationTable.add(getLocalizedSmallHeader("child_care.placement_date","Placement date"), column++, row);
			applicationTable.add(getLocalizedSmallHeader("child_care.order","Order"), column++, row);
			applicationTable.add(getLocalizedSmallHeader("child_care.queue_order","Queue order"), column++, row++);
			
			Iterator iter = applications.iterator();
			while (iter.hasNext()) {
				column = 1;
				application = (ChildCareApplication) iter.next();
				child = application.getChild();
				address = getBusiness().getUserBusiness().getUsersMainAddress(child);
				queueDate = new IWCalendar(iwc.getCurrentLocale(), application.getCreated());
				placementDate = new IWCalendar(iwc.getCurrentLocale(), application.getFromDate());
				
				if (row % 2 == 0)
					applicationTable.setRowColor(row, getZebraColor1());
				else
					applicationTable.setRowColor(row, getZebraColor2());

				link = (Link) this.getSmallLink(child.getNameLastFirst(true));
				link.setWindowToOpen(ChildCareWindow.class);
				link.setParameter(ChildCareAdminWindow.PARAMETER_METHOD, String.valueOf(ChildCareAdminWindow.METHOD_OVERVIEW));
				link.setParameter(ChildCareAdminWindow.PARAMETER_USER_ID, String.valueOf(application.getChildId()));
				link.setParameter(ChildCareAdminWindow.PARAMETER_APPLICATION_ID, application.getPrimaryKey().toString());
				link.setParameter(ChildCareAdminWindow.PARAMETER_PAGE_ID, String.valueOf(getParentPage().getPageID()));

				applicationTable.add(link, column++, row);
				applicationTable.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), column++, row);
				if (address != null)
					applicationTable.add(getSmallText(address.getStreetAddress()), column, row);
				column++;
				applicationTable.add(getSmallText(queueDate.getLocaleDate(IWCalendar.SHORT)), column++, row);
				applicationTable.add(getSmallText(placementDate.getLocaleDate(IWCalendar.SHORT)), column++, row);
				applicationTable.add(getSmallText("-"), column++, row);
				applicationTable.add(getSmallText("-"), column, row++);
			}
		}
	}

}
