/*
 * Created on 26.3.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractArchive;
import se.idega.idegaweb.commune.childcare.event.ChildCareEventListener;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
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
		if (getSession().hasPrognosis() || isCommuneAdministrator(iwc)) {
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
			IWTimestamp dateNow = new IWTimestamp();
			
			table.add(getLocalizedSmallHeader("child_care.name","Name"), column++, row);
			table.add(getLocalizedSmallHeader("child_care.personal_id","Personal ID"), column++, row);
			table.add(getLocalizedSmallHeader("child_care.created","Created"), column++, row);
			table.add(getLocalizedSmallHeader("child_care.valid_from","Valid from"), column++, row);
			table.add(getLocalizedSmallHeader("child_care.status","Status"), column++, row);
			table.add(getLocalizedSmallHeader("child_care.care_time","Care time"), column++, row++);
	
			boolean showComment = false;
			boolean showNotActiveComment = false;
			boolean showRemovedComment = false;

			Collection contracts = getBusiness().getAcceptedApplicationsByProvider(getSession().getChildCareID());
			if (contracts != null) {
				ChildCareApplication application;
				ChildCareContractArchive contract;
				User child;
				IWTimestamp created;
				IWTimestamp validFrom;
				Link alterCareTime = null;
				Link viewContract;
				Link archive;
				boolean isCancelled;
				boolean isNotYetActive;
				boolean hasComment = false;
				
				Iterator iter = contracts.iterator();
				while (iter.hasNext()) {
					column = 1;
					application = (ChildCareApplication) iter.next();
					contract = getBusiness().getValidContract(((Integer)application.getPrimaryKey()).intValue());
					child = application.getChild();
					hasComment = true;

					if (row % 2 == 0)
						table.setRowColor(row, getZebraColor1());
					else
						table.setRowColor(row, getZebraColor2());
		
					if (contract != null) {
						created = new IWTimestamp(contract.getCreatedDate());
						validFrom = new IWTimestamp(contract.getValidFromDate());
						if (application.getRejectionDate() != null) {
							IWTimestamp cancelledDate = new IWTimestamp(application.getRejectionDate());
							isCancelled = cancelledDate.isEarlierThan(dateNow);
						}
						else
							isCancelled = false;
							
						if (dateNow.isEarlierThan(validFrom))
							isNotYetActive = true;
						else
							isNotYetActive = false;
						
						viewContract = new Link(getPDFIcon(localize("child_care.view_contract","View contract")));
						viewContract.setFile(contract.getContractFileID());
						viewContract.setTarget(Link.TARGET_NEW_WINDOW);
						
						if (isNotYetActive) {
							alterCareTime = new Link(this.getEditIcon(localize("child_care.alter_placement_date_for_child","Alter the placement date for this child.")));
							alterCareTime.addParameter(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_ALTER_VALID_FROM_DATE);
						}
						else {
							alterCareTime = new Link(this.getEditIcon(localize("child_care.alter_care_time_for_child","Alter the care time for this child.")));
							alterCareTime.addParameter(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_ALTER_CARE_TIME);
						}
						alterCareTime.setWindowToOpen(ChildCareWindow.class);
						alterCareTime.addParameter(ChildCareAdminWindow.PARAMETER_APPLICATION_ID, application.getPrimaryKey().toString());
						alterCareTime.addParameter(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
						
						if (isNotYetActive) {
							showComment = true;
							hasComment = true;
							showNotActiveComment = true;
							table.add(getSmallErrorText("*"), column, row);
						}
						if (application.getRejectionDate() != null) {
							showComment = true;
							hasComment = true;
							showRemovedComment = true;
							table.add(getSmallErrorText("+"), column, row);
						}
						
						if (hasComment)
							table.add(getSmallErrorText(Text.NON_BREAKING_SPACE), column, row);
						if (getResponsePage() != null) {
							archive = getSmallLink(child.getNameLastFirst(true));
							archive.setEventListener(ChildCareEventListener.class);
							archive.addParameter(getSession().getParameterUserID(), application.getChildId());
							archive.addParameter(getSession().getParameterApplicationID(), ((Integer)application.getPrimaryKey()).intValue());
							archive.setPage(getResponsePage());
							table.add(archive, column++, row);
						}
						else
							table.add(getSmallText(child.getNameLastFirst(true)), column++, row);
						table.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), column++, row);
						table.add(getSmallText(created.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
						table.add(getSmallText(validFrom.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
						if (application.getApplicationStatus() == getBusiness().getStatusCancelled() && isCancelled)
							table.add(getSmallText(localize("child_care.status_cancelled","Cancelled")), column, row);
						else
							table.add(getSmallText(localize("child_care.status_active","Active")), column, row);
						column++;
						table.add(getSmallText(String.valueOf(contract.getCareTime())), column++, row);
						
						table.setWidth(column, row, 12);
						table.add(viewContract, column++, row);
		
						if (allowAlter) {
							table.setWidth(column, row, 12);
							if (getBusiness().getNumberOfFutureContracts(((Integer)application.getPrimaryKey()).intValue()) < 2)
								table.add(alterCareTime, column++, row);
						}
					}
					else {
						table.add(getSmallText(child.getNameLastFirst(true)), column++, row);
						table.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), column++, row);
					}
					row++;
				}
				table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
				table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
				table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
				table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);
				table.setColumnAlignment(6, Table.HORIZONTAL_ALIGN_CENTER);
			}
			if (showComment) {
				table.setHeight(2, row++);
				if (showNotActiveComment) {
					table.mergeCells(1, row, table.getColumns(), row);
					table.add(getSmallErrorText("*"), 1, row);
					table.add(getSmallText(Text.NON_BREAKING_SPACE + localize("child_care.not_yet_active_placing","Placing not yet active")), 1, row++);
				}
				if (showRemovedComment) {
					table.mergeCells(1, row, table.getColumns(), row);
					table.add(getSmallErrorText("+"), 1, row);
					table.add(getSmallText(Text.NON_BREAKING_SPACE + localize("child_care.contract_ended","Contract has termination date")), 1, row++);
				}
			}
	
			add(table);		
		}
		else {
			add(getSmallErrorText(localize("child_care.prognosis_must_be_set","Prognosis must be set or updated before you can continue!")));
		}
	}
	/**
	 * @param b
	 */
	public void setAllowAlter(boolean b) {
		allowAlter = b;
	}

}