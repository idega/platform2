/*
 * Created on 27.3.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.care.business.PlacementHelper;
import se.idega.idegaweb.commune.care.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.business.ChildCareConstants;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberLog;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;

/**
 * @author laddi
 */
public class ChildCareChildContracts extends ChildCareBlock {

	public static final String PARAMETER_APPLICATION_ID = "ccc_application_id";
	private boolean insideWindow = false;
	private boolean useFuturePayment = false;
	
	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());

		GenericButton back = null;
		if(insideWindow){
			CloseButton close = new CloseButton(localize("close","Close"));
			close = (CloseButton)getStyledInterface(close);
			back = close;
		}
		else{
			 back = (GenericButton) getStyledInterface(new GenericButton("back",localize("back","Back")));
			back.setPageToOpen(getResponsePage());
		}

		if (getChildID(iwc) != -1) {
			parse(iwc);
			
			int row = 1;
			if (useStyleNames()) {
				table.setCellpaddingLeft(1, row, 12);
				table.setCellpaddingRight(1, row, 12);
			}
			table.add(getInformationTable(iwc), 1, row++);
			table.setHeight(row++, 12);
			
			table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
			if (useStyleNames()) {
				table.setCellpaddingLeft(1, row, 12);
				table.setCellpaddingRight(1, row, 12);
			}
			table.add(getRemoveContractsForm(iwc), 1, row++);
			table.setHeight(row++, 6);
				
			table.add(getContractsTable(iwc), 1, row++);
			table.setHeight(row++, 12);
			if (useStyleNames()) {
				table.setCellpaddingLeft(1, row, 12);
				table.setCellpaddingRight(1, row, 12);
			}
			table.add(back, 1, row);
		}
		else {
			table.add(getLocalizedHeader("child_care.no_child_or_application_found","No child or application found."), 1, 1);
			table.setHeight(2, 12);
			table.add(back, 1, 3);

			if (useStyleNames()) {
				table.setCellpaddingLeft(1, 1, 12);
				table.setCellpaddingLeft(1, 3, 12);
			}
		}
		
		add(table);
	}
	
	protected Table getContractsTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(getWidth());
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(6);
		if (useStyleNames()) {
			table.setRowStyleClass(1, getHeaderRowClass());
		}
		else {
			table.setRowColor(1, getHeaderColor());
		}
		int column = 1;
		int row = 1;
			
		if (useStyleNames()) {
			table.setCellpaddingLeft(1, row, 12);
			table.setCellpaddingRight(table.getColumns(), row, 12);
		}
		table.add(getLocalizedSmallHeader("child_care.provider","Provider"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.group","Group"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.created","Created"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.valid_from","Valid from"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.terminated","Terminated"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.care_time","Care time"), column++, row++);
		
		ChildCareContract contract;
		School provider;
		SchoolClass group;
		SchoolClassMember member;
		Collection logs = null;
		IWTimestamp created;
		IWTimestamp validFrom;
		IWTimestamp terminated = null;
		Link viewContract;
		String careTime;
		boolean isActive = false;
		IWTimestamp stampNow = new IWTimestamp();
		
		Collection contracts = null;
		// preventing overruling the child parameter when session got application reference
		if(iwc.isParameterSet(ChildCareConstants.PARAMETER_CHILD_ID)){
			contracts = getBusiness().getContractsByChild(getChildID(iwc));
		}
		else if (getSession().getApplicationID() != -1) {
			contracts = getBusiness().getContractsByApplication(getSession().getApplicationID());
		}
		else {
			contracts = getBusiness().getContractsByChild(getChildID(iwc));
		}

		int contractCount = 0;
		Iterator iter = contracts.iterator();
		while (iter.hasNext()) {
			column = 1;
			contract = (ChildCareContract) iter.next();
			contractCount++;
			Link viewCancelFile = null;
			provider = contract.getApplication().getProvider();
			created = new IWTimestamp(contract.getCreatedDate());
			member = contract.getSchoolClassMember();
			group = member.getSchoolClass();
			if (contract.getTerminatedDate() != null) {
				terminated = new IWTimestamp(contract.getTerminatedDate());
			}
			else {
				terminated = null;
			}
			if (contract.getValidFromDate() != null) {
				if (!iter.hasNext()) {
					validFrom = new IWTimestamp(member.getRegisterDate());
				}
				else {
					validFrom = new IWTimestamp(contract.getValidFromDate());
				}
				try {
					logs = getBusiness().getSchoolBusiness().getSchoolClassMemberLogHome().findByPlacementAndDates(member, validFrom.getDate(), terminated != null ? terminated.getDate() : null);
					if (logs.isEmpty()) {
						SchoolClassMemberLog log = getBusiness().getSchoolBusiness().getSchoolClassMemberLogHome().findByPlacementAndDate(member, validFrom.getDate());
						logs.add(log);
					}
				}
				catch (FinderException fe) {
					group = member.getSchoolClass();
				}
			}
			else {
				validFrom = null;
			}
			careTime = getCareTime(contract.getCareTime());
				
			if (stampNow.isLaterThanOrEquals(validFrom)) {
				isActive = true;
				if (contract.getTerminatedDate() != null) {
					if (terminated.isLaterThanOrEquals(stampNow)) {
						isActive = true;
					}
					else {
						isActive = false;
					}
				}
			}

			viewContract = getPDFLink(contract.getContractFileID(),localize("child_care.view_contract","View contract"));

			if (contractCount == 1) {
				int fileId = contract.getApplication().getCancelFormFileID();
				if (fileId > 0) {
					viewCancelFile = getPDFLink(fileId, localize("child_care.view_cancel_file", "View cancel contract document"));
				}
			}
			
			if (logs != null && !logs.isEmpty()) {
				Iterator iterator = logs.iterator();
				boolean first = true;
				while (iterator.hasNext()) {
					SchoolClassMemberLog log = (SchoolClassMemberLog) iterator.next();
					IWTimestamp startDate = null;
					IWTimestamp endDate = null;
					if (!iterator.hasNext()) {
						startDate = new IWTimestamp(contract.getValidFromDate());
					}
					else {
						startDate = new IWTimestamp(log.getStartDate());
					}
					
					if (first) {
						if (contract.getTerminatedDate() != null) {
							endDate = contract.getTerminatedDate() != null ? new IWTimestamp(contract.getTerminatedDate()) : null;
						}
						else {
							endDate = log.getEndDate() != null ? new IWTimestamp(log.getEndDate()) : null;
						}
						first = false;
					}
					else {
						endDate = log.getEndDate() != null ? new IWTimestamp(log.getEndDate()) : null;
					}
					
					row = addToTable(iwc, table, row, isActive, provider, log.getSchoolClass(), created, startDate, endDate, careTime, viewContract, viewCancelFile);
				}
			}
			else {
				row = addToTable(iwc, table, row, isActive, provider, group, created, validFrom, terminated, careTime, viewContract, viewCancelFile);
			}
		}
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);

		return table;
	}
	
	private int addToTable(IWContext iwc, Table table, int row, boolean isActive, School provider, SchoolClass group, IWTimestamp created, IWTimestamp validFrom, IWTimestamp terminated, String careTime, Link viewContract, Link viewCancelFile) {
		int column = 1;
		
		table.add(getText(provider.getSchoolName(), isActive), column++, row);
		table.add(getText(group.getName(), isActive), column++, row);
		table.add(getText(created.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT), isActive), column++, row);
		if (validFrom != null) {
			table.add(getText(validFrom.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT), isActive), column++, row);
		}
		else {
			table.add(getText("-", isActive), column++, row);
		}
		if (terminated != null) {
			table.add(getText(terminated.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT), isActive), column++, row);
		}
		else {
			table.add(getText("-", isActive), column++, row);
		}
		table.add(getText(careTime, isActive), column++, row);
				
		table.setWidth(column, row, 12);
		table.add(viewContract, column++, row);
		if (viewCancelFile != null) {
			table.add(viewCancelFile, column++, row);
		}

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

		row++;
		return row;
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
		
		User child = getBusiness().getUserBusiness().getUser(getChildID(iwc));
		if (child != null) {
			Address address = getBusiness().getUserBusiness().getUsersMainAddress(child);
			Collection parents = getBusiness().getUserBusiness().getParentsForChild(child);
			
			table.add(getLocalizedSmallHeader("child_care.child","Child"), 1, row);
			Name name = new Name(child.getFirstName(), child.getMiddleName(), child.getLastName());
			table.add(getSmallText(name.getName(iwc.getApplicationSettings().getDefaultLocale(), true)), 3, row);
			table.add(getSmallText(" - "), 3, row);
			table.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), 3, row++);
			
			if (address != null) {
				table.add(getLocalizedSmallHeader("child_care.address","Address"), 1, row);
				table.add(getSmallText(address.getStreetAddress()), 3, row);
				if (address.getPostalAddress() != null) {
					table.add(getSmallText(", "+address.getPostalAddress()), 3, row);
				}
				row++;
			}
			
			table.setHeight(row++, 12);
			
			if (parents != null) {
				table.add(getLocalizedSmallHeader("child_care.parents","Parents"), 1, row);
				Phone phone;
				Email email;

				Iterator iter = parents.iterator();
				while (iter.hasNext()) {
					User parent = (User) iter.next();
					address = getBusiness().getUserBusiness().getUsersMainAddress(parent);
					email = getBusiness().getUserBusiness().getEmail(parent);
					phone = getBusiness().getUserBusiness().getHomePhone(parent);

					name = new Name(parent.getFirstName(), parent.getMiddleName(), parent.getLastName());
					table.add(getSmallText(name.getName(iwc.getApplicationSettings().getDefaultLocale(), true)), 3, row);
					table.add(getSmallText(" - "), 3, row);
					table.add(getSmallText(PersonalIDFormatter.format(parent.getPersonalID(), iwc.getCurrentLocale())), 3, row++);
			
					if (address != null) {
						table.add(getSmallText(address.getStreetAddress()), 3, row);
						if (address.getPostalAddress() != null)
							table.add(getSmallText(", "+address.getPostalAddress()), 3, row);
						row++;
					}
					if (phone != null && phone.getNumber() != null) {
						table.add(getSmallText(localize("child_care.phone","Phone")+": "), 3, row);
						table.add(getSmallText(phone.getNumber()), 3, row++);
					}
					if (email != null && email.getEmailAddress() != null) {
						Link link = getSmallLink(email.getEmailAddress());
						link.setURL("mailto:"+email.getEmailAddress(), false, false);
						table.add(link, 3, row++);
					}
			
					table.setHeight(row++, 12);
				}
			}
		}
		
		return table;
	}
	
	protected Form getRemoveContractsForm(IWContext iwc) throws RemoteException {
		Form form = new Form();
		int applicationId = getSession().getApplicationID();
		form.add(new HiddenInput(PARAMETER_APPLICATION_ID, String.valueOf(applicationId)));
		
		SubmitButton removeContracts = (SubmitButton) getStyledInterface(new SubmitButton("remove", localize("child_care.remove_future_contracts","Remove future contracts")));
		form.add(removeContracts);
		if (!getBusiness().hasFutureContracts(applicationId) && !getBusiness().hasFutureLogs(applicationId, new java.sql.Date(System.currentTimeMillis()))) {
			removeContracts.setDisabled(true);			
		} else {
			java.sql.Date earliestPossibleRemoveDate = new java.sql.Date(getEarliestPossibleContractRemoveDate().getTime());
			if (getBusiness().getNumberOfFutureContracts(applicationId, earliestPossibleRemoveDate) > 0 ||
					getBusiness().hasFutureLogs(applicationId, earliestPossibleRemoveDate)) {
				removeContracts.setSingleSubmitConfirm(localize("child_care.submit_contract_delete", "Are you sure you want to remove future contracts for this application?"));
			} else {
				removeContracts.setOnSubmitFunction("removeContract", "function removeContract() { alert('" + localize("child_care.only_admin_delete_future_contract", "Earliest possible date to remove contract is") + 
						" " + new IWTimestamp(earliestPossibleRemoveDate).getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT) + "'); return false; }");
			}
		}
	
		return form;
	}
	
	private Date getEarliestPossibleContractRemoveDate() throws RemoteException {
		Date earliestPossibleRemoveDate = null;
		if (useFuturePayment) {
			IWTimestamp t = new IWTimestamp();
			t.setDay(1);
			t.addMonths(3);
			t.addDays(1);
			earliestPossibleRemoveDate = t.getDate();
		} else {
			PlacementHelper helper = getBusiness().getPlacementHelper(new Integer(getSession().getApplicationID()));
			earliestPossibleRemoveDate = helper.getEarliestPlacementDate();				
		}
		return earliestPossibleRemoveDate;
	}

	protected Text getText(String text, boolean isActive) {
		if (isActive) {
			return getSmallHeader(text);
		}
		else {
			return getSmallText(text);
		}
	}
	
	protected Link getLink(String text, boolean isActive) {
		if (isActive) {
			return getSmallHeaderLink(text);
		}
		else {
			return getSmallLink(text);
		}
	}
	
	private void parse(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_APPLICATION_ID)) {
			int applicationID = Integer.parseInt(iwc.getParameter(PARAMETER_APPLICATION_ID));
			try {
				getBusiness().removeLatestFutureContract(applicationID, new java.sql.Date(getEarliestPossibleContractRemoveDate().getTime()));
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	private int getChildID(IWContext iwc) throws RemoteException {
		if(iwc.isParameterSet(ChildCareConstants.PARAMETER_CHILD_ID)) {
			return Integer.parseInt(iwc.getParameter(ChildCareConstants.PARAMETER_CHILD_ID));
		}
		else {
			return getSession().getChildID();
		}
	}
	
	/**
	 * @return
	 */
	public boolean isInsideWindow() {
		return insideWindow;
	}

	/**
	 * @param insideWindow
	 */
	public void setInsideWindow(boolean insideWindow) {
		this.insideWindow = insideWindow;
	}
	
	public boolean getUseFuturePayment() {
		return useFuturePayment;
	}
	
	public void setUseFuturePayment(boolean b) {
		useFuturePayment = b;
	}
}