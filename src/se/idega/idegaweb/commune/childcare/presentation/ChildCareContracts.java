/*
 * Created on 26.3.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import se.idega.idegaweb.commune.care.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.event.ChildCareEventListener;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;

/**
 * @author laddi
 */
public class ChildCareContracts extends ChildCareBlock {

	private boolean allowAlter = true;
	private boolean _requiresPrognosis;
	private int allowedFutureContracts = 2;
	
	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		if (canSeeContracts(iwc)) {
			Table table = new Table();
			table.setWidth(getWidth());
			table.setCellpadding(getCellpadding());
			table.setCellspacing(getCellspacing());
			if (allowAlter)
				table.setColumns(8);
			else
				table.setColumns(7);
			if (useStyleNames()) {
				table.setRowStyleClass(1, getHeaderRowClass());
			}
			else {
				table.setRowColor(1, getHeaderColor());
			}
			int column = 1;
			int row = 1;
			IWTimestamp dateNow = new IWTimestamp();
			
			if (useStyleNames()) {
				table.setCellpaddingLeft(1, row, 12);
				table.setCellpaddingRight(table.getColumns(), row, 12);
			}
			table.add(getLocalizedSmallHeader("child_care.name","Name"), column++, row);
			table.add(getLocalizedSmallHeader("child_care.personal_id","Personal ID"), column++, row);
			table.add(getLocalizedSmallHeader("child_care.created","Created"), column++, row);
			table.add(getLocalizedSmallHeader("child_care.valid_from","Valid from"), column++, row);
			table.add(getLocalizedSmallHeader("child_care.status","Status"), column++, row);
			table.add(getLocalizedSmallHeader("child_care.care_time","Care time"), column++, row);
			if(isCommuneAdministrator(iwc))
				table.add(getLocalizedSmallHeader("child_care.invoice_receiver","Invoice Receiver"), column++, row);
			row++;
	
			boolean showComment = false;
			boolean showNotActiveComment = false;
			boolean showRemovedComment = false;

			Collection contracts = getBusiness().getAcceptedApplicationsByProvider(getSession().getChildCareID());
			if (contracts != null) {
				ChildCareApplication application;
				ChildCareContract contract;
				User child;
				IWTimestamp created;
				IWTimestamp validFrom;
				Link alterCareTime = null;
				Link viewContract;
				Link archive;
				boolean isCancelled;
				boolean isNotYetActive;
				boolean hasComment = false;
				String name = null;
				
				Iterator iter = contracts.iterator();
				while (iter.hasNext()) {
					column = 1;
					application = (ChildCareApplication) iter.next();
					contract = getBusiness().getValidContract(((Integer)application.getPrimaryKey()).intValue());
					child = application.getChild();
					hasComment = true;

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
					
					if (contract != null) {
						created = new IWTimestamp(contract.getCreatedDate());
						if (contract.getValidFromDate() != null)
							validFrom = new IWTimestamp(contract.getValidFromDate());
						else
							validFrom = null;
						if (application.getRejectionDate() != null) {
							IWTimestamp cancelledDate = new IWTimestamp(application.getRejectionDate());
							isCancelled = cancelledDate.isEarlierThan(dateNow);
						}
						else
							isCancelled = false;
							
						if (validFrom != null) {
							if (dateNow.isEarlierThan(validFrom))
								isNotYetActive = true;
							else
								isNotYetActive = false;
						}
						else {
							isNotYetActive = false;
							isCancelled = true;
						}
						
						//viewContract = new Link(getPDFIcon(localize("child_care.view_contract","View contract")));
						//viewContract.setFile(contract.getContractFileID());
						//viewContract.setTarget(Link.TARGET_NEW_WINDOW);
						viewContract = getPDFLink(contract.getContractFileID(),localize("child_care.view_contract","View contract"));
						
						if (isNotYetActive) {
							alterCareTime = new Link(this.getEditIcon(localize("child_care.alter_placement_date_for_child","Alter the placement date for this child.")));
							alterCareTime.addParameter(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_ALTER_VALID_FROM_DATE);
						}
						else {
							//alterCareTime = new Link(this.getEditIcon(localize("child_care.alter_care_time_for_child","Alter the care time for this child.")));
							alterCareTime = new Link(this.getEditIcon(localize("child_care.alter_contract_or_schooltype_for_child","Alter the contract/schooltype for this child.")));
							alterCareTime.addParameter(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_ALTER_CARE_TIME);
						}
						alterCareTime.setWindowToOpen(ChildCareWindow.class);
						alterCareTime.addParameter(ChildCareAdminWindow.PARAMETER_APPLICATION_ID, application.getPrimaryKey().toString());
						alterCareTime.addParameter(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
						
						if (!isCancelled){						
						
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
							name = getBusiness().getUserBusiness().getNameLastFirst(child, true);
							archive = getSmallLink(name);
							archive.setEventListener(ChildCareEventListener.class);
							archive.addParameter(getSession().getParameterUserID(), application.getChildId());
							archive.addParameter(getSession().getParameterApplicationID(), ((Integer)application.getPrimaryKey()).intValue());
							archive.setPage(getResponsePage());
							table.add(archive, column++, row);
						}
						else {
							Name userName = new Name(child.getFirstName(), child.getMiddleName(), child.getLastName());
							table.add(getSmallText(userName.getName(iwc.getApplicationSettings().getDefaultLocale(), true)), column++, row);
						}
						table.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), column++, row);
						table.add(getSmallText(created.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
						if (validFrom != null)
							table.add(getSmallText(validFrom.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
						else
							table.add(getSmallText("-"), column++, row);
						if (application.getApplicationStatus() == getBusiness().getStatusCancelled() && isCancelled)
							table.add(getSmallText(localize("child_care.status_cancelled","Cancelled")), column, row);
						else
							table.add(getSmallText(localize("child_care.status_active","Active")), column, row);
						column++;
						table.add(getSmallText(getCareTime(contract.getCareTime())), column++, row);
						if(isCommuneAdministrator(iwc)){
							if(contract.getInvoiceReceiverID()>0){
								table.add(getSmallText(contract.getInvoiceReceiver().getName()),column++,row);
							}
							else{
								table.add(getSmallErrorText(localize("child_care.no_invoice_receiver","No receiver")),column++,row);
							}
						}
						
						table.setWidth(column, row, 12);
						table.add(viewContract, column++, row);
		
						if (allowAlter) {
							table.setWidth(column, row, 12);
							int futureContractCount = getBusiness().getNumberOfFutureContracts(((Integer)application.getPrimaryKey()).intValue());
							if ( futureContractCount < allowedFutureContracts)
								table.add(alterCareTime, column++, row);
							else
								table.add(getInformationIcon(localize("child_care.to_many_future_contracts","To many future contracts")),column,row);
						}
						row++;
					}
						
					}
					else {
						Name userName = new Name(child.getFirstName(), child.getMiddleName(), child.getLastName());
						table.add(getSmallText(userName.getName(iwc.getApplicationSettings().getDefaultLocale(), true)), column++, row);
						table.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), column++, row);
						row++;
					}
					
					
				
			}
				table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
				table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
				table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
				table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);
				table.setColumnAlignment(6, Table.HORIZONTAL_ALIGN_CENTER);
			}
		
			if (showComment) {
				table.setHeight(row++, 2);
				if (showNotActiveComment) {
					table.mergeCells(1, row, table.getColumns(), row);
					if (useStyleNames()) {
						table.setCellpaddingLeft(1, row, 12);
					}
					table.add(getSmallErrorText("*"), 1, row);
					table.add(getSmallText(Text.NON_BREAKING_SPACE + localize("child_care.not_yet_active_placing","Placing not yet active")), 1, row++);
				}
				if (showRemovedComment) {
					table.mergeCells(1, row, table.getColumns(), row);
					if (useStyleNames()) {
						table.setCellpaddingLeft(1, row, 12);
					}
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
	
	protected boolean canSeeContracts(IWContext iwc) {
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
		return hasPrognosis || isCommuneAdministrator(iwc);
	}
	
	/**
	 * @param b
	 */
	public void setAllowAlter(boolean b) {
		allowAlter = b;
	}
	
	public void setRequiresPrognosis(boolean requiresPrognosis) {
		_requiresPrognosis = requiresPrognosis;
	}

	/**
	 * @param allowedFutureContracts The allowedFutureContracts to set.
	 */
	public void setAllowedFutureContracts(int allowedFutureContracts) {
		this.allowedFutureContracts = allowedFutureContracts;
	}
}