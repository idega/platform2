/*
 * Created on 26.3.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractArchive;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.GenericButton;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * @author laddi
 */
public class ChildCareContracts extends ChildCareBlock {

	private boolean allowAlter = true;
	
	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		Table table = new Table();
		table.setWidth(getWidth());
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		if (allowAlter)
			table.setColumns(8);
		else
			table.setColumns(7);
		table.setRowColor(1, getHeaderColor());
		int column = 1;
		int row = 1;
		
		table.add(getLocalizedSmallHeader("child_care.name","Name"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.personal_id","Personal ID"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.created","Created"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.valid_from","Valid from"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.status","Status"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.care_time","Care time"), column++, row++);

		Collection contracts = getBusiness().getAcceptedApplicationsByProvider(getSession().getChildCareID());
		if (contracts != null) {
			ChildCareApplication application;
			ChildCareContractArchive contract;
			User child;
			IWTimestamp created;
			IWTimestamp validFrom;
			Link alterCareTime;
			Link viewContract;
			
			Iterator iter = contracts.iterator();
			while (iter.hasNext()) {
				column = 1;
				application = (ChildCareApplication) iter.next();
				contract = getBusiness().getContractFile(application.getContractFileId());
				child = application.getChild();
				created = new IWTimestamp(contract.getCreatedDate());
				validFrom = new IWTimestamp(contract.getValidFromDate());
				
				viewContract = new Link(getPDFIcon(localize("child_care.view_contract","View contract")));
				viewContract.setFile(application.getContractFileId());
				viewContract.setTarget(Link.TARGET_NEW_WINDOW);
				
				alterCareTime = new Link(this.getEditIcon(localize("child_care.alter_care_time_for_child","Alter the care time for this child.")));
				alterCareTime.setWindowToOpen(ChildCareWindow.class);
				alterCareTime.addParameter(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_ALTER_CARE_TIME);
				alterCareTime.addParameter(ChildCareAdminWindow.PARAMETER_APPLICATION_ID, application.getPrimaryKey().toString());
				
				if (row % 2 == 0)
					table.setRowColor(row, getZebraColor1());
				else
					table.setRowColor(row, getZebraColor2());

				table.add(getSmallText(child.getNameLastFirst(true)), column++, row);
				table.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), column++, row);
				table.add(getSmallText(created.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
				table.add(getSmallText(validFrom.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
				if (application.getApplicationStatus() == getBusiness().getStatusReady())
					table.add(getSmallText(localize("child_care.status_ready","Active")), column, row);
				else if (application.getApplicationStatus() == getBusiness().getStatusCancelled())
					table.add(getSmallText(localize("child_care.status_cancelled","Cancelled")), column, row);
				column++;
				table.add(getSmallText(String.valueOf(application.getCareTime())), column++, row);
				
				table.setWidth(column, row, 12);
				table.add(viewContract, column++, row);

				if (allowAlter && application.getApplicationStatus() == getBusiness().getStatusReady()) {
					table.setWidth(column, row, 12);
					table.add(alterCareTime, column++, row);
				}
				row++;
			}
			table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
			table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
			table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
			table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);
			table.setColumnAlignment(6, Table.HORIZONTAL_ALIGN_CENTER);
		}

		add(table);		
	}
	/**
	 * @param b
	 */
	public void setAllowAlter(boolean b) {
		allowAlter = b;
	}

}