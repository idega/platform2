package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.event.ChildCareEventListener;

import com.idega.block.school.data.School;
import com.idega.core.builder.data.ICPage;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * @author laddi
 */
public class ChildCareAdminApplication extends ChildCareBlock {

	private boolean showParentsAgree = false;
	private boolean showRecreateContract = false;

	private static final String PARAMETER_COMMENTS = "cc_comments";
	private static final String PARAMETER_CREATE_CONTRACT = "cc_create_contract";
	
	private static final String ACTION_CREATE_REGULAR_CONTRACT = "cc_create_regular_contract";
	private static final String ACTION_CREATE_BANKID_CONTRACT = "cc_create_bankid_contract";
	
	private User child;
	private ChildCareApplication application;
	private boolean isAdministrator;
	private ICPage contractsPage;
	private Boolean _canEdit;
	private boolean _useSubmitConfirm;
	
	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		parse(iwc);
		isAdministrator = isCommuneAdministrator(iwc);
		if (_canEdit != null)
			isAdministrator = !_canEdit.booleanValue();

		Table table = new Table(1,7);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());
		table.setHeight(2, 12);
		table.setHeight(4, 6);
		table.setHeight(6, 12);
		
		if (getSession().getChildID() != -1) {
			table.add(getInformationTable(iwc), 1, 1);
			table.add(getApplicationTable(iwc), 1, 3);
			table.add(getLegendTable(), 1, 5);
			table.add(getButtonTable(iwc, !isAdministrator), 1, 7);
		}
		else {
			table.add(this.getLocalizedHeader("child_care.no_child_or_application_found","No child or application found."), 1, 1);
			table.add(getButtonTable(iwc, false), 1, 3);
		}
		
		add(table);
	}

	protected Form getInformationTable(IWContext iwc) throws RemoteException {
		Form form = new Form();
		
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setColumns(3);
		table.setWidth(1, "100");
		table.setWidth(2, "6");
		form.add(table);
		int row = 1;
		
		child = getBusiness().getUserBusiness().getUser(getSession().getChildID());
		if (child != null) {
			Address address = getBusiness().getUserBusiness().getUsersMainAddress(child);
			Collection parents = getBusiness().getUserBusiness().getParentsForChild(child);
			
			table.add(getLocalizedSmallHeader("child_care.child","Child"), 1, row);
			table.add(getSmallText(child.getNameLastFirst(true)), 3, row);
			table.add(getSmallText(" - "), 3, row);
			table.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), 3, row++);
			
			if (address != null) {
				table.add(getLocalizedSmallHeader("child_care.address","Address"), 1, row);
				table.add(getSmallText(address.getStreetAddress()), 3, row);
				if (address.getPostalAddress() != null)
					table.add(getSmallText(", "+address.getPostalAddress()), 3, row);
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

					table.add(getSmallText(parent.getNameLastFirst(true)), 3, row);
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
		 
			School provider = getBusiness().getCurrentProviderByPlacement(getSession().getChildID());
			String school = null;
			if (provider != null){
				school = provider.getName();				
			} else {
				school = localize("child_care.no_current_provider", "No current provider");
			}
				
			
			if (school != null){
				table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
				table.add(getLocalizedSmallHeader("child_care.current_provider","Current provider"), 1, row);
				table.add(getSmallText(school), 3, row++);
				table.setHeight(row++, 12);	
			}
			
			
			if (getSession().getApplicationID() != -1 && !isAdministrator) {
				application = getBusiness().getApplication(getSession().getApplicationID());
				if (application != null) {
					if (application.getMessage() != null) {
						table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
						table.add(getLocalizedSmallHeader("child_care.message","Message"), 1, row);
						table.add(getSmallText(application.getMessage()), 3, row++);
						table.setHeight(row++, 12);
					}
					
					if (application.getApplicationStatus() == getBusiness().getStatusAccepted()) {
						IWTimestamp validUntil = new IWTimestamp(application.getOfferValidUntil());
						table.add(getLocalizedSmallHeader("child_care.reply_date","Reply date"), 1, row);
						table.add(getSmallText(validUntil.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), 3, row++);
						table.setHeight(row++, 12);
					}
				
					table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
					table.add(getLocalizedSmallHeader("child_care.comments","Comments"), 1, row);
				
					TextArea comments = (TextArea) getStyledInterface(new TextArea(PARAMETER_COMMENTS));
					comments.setWidth(Table.HUNDRED_PERCENT);
					comments.setRows(4);
					if (application.getPresentation() != null)
						comments.setContent(application.getPresentation());
					table.add(comments, 3, row++);
					SubmitButton saveComments = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.save_comments","Save comments")));
					table.setAlignment(3, row, Table.HORIZONTAL_ALIGN_RIGHT);
					table.add(saveComments, 3, row);
				}
			}
		}
		
		return form;
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
		Link archive;

		Collection applications = getBusiness().getApplicationsForChild(child, getSession().getCaseCode());
		Iterator iter = applications.iterator();
		while (iter.hasNext()) {
			column = 1;
			application = (ChildCareApplication) iter.next();
			phone = getBusiness().getSchoolBusiness().getSchoolPhone(application.getProviderId());
			queueDate = new IWTimestamp(application.getQueueDate());
			placementDate = new IWTimestamp(application.getFromDate());
			if (application.getProviderId() == getSession().getChildCareID())
				isCurrentProvider = true;
			else
				isCurrentProvider = false;
				
			if (application.getApplicationStatus() == getBusiness().getStatusAccepted()) {
				table.setRowColor(row, ACCEPTED_COLOR);
			}
			else if (application.getApplicationStatus() == getBusiness().getStatusParentsAccept()) {
				table.setRowColor(row, PARENTS_ACCEPTED_COLOR);
			}
			else if (application.getApplicationStatus() == getBusiness().getStatusContract()) {
				table.setRowColor(row, CONTRACT_COLOR);
			}
			else {
				if (row % 2 == 0)
					table.setRowColor(row, getZebraColor1());
				else
					table.setRowColor(row, getZebraColor2());
			}

			if (contractsPage != null) {
				archive = getLink(child.getNameLastFirst(true),isCurrentProvider);
				archive.setEventListener(ChildCareEventListener.class);
				archive.addParameter(getSession().getParameterUserID(), application.getChildId());
				archive.addParameter(getSession().getParameterApplicationID(), ((Integer)application.getPrimaryKey()).intValue());
				archive.setPage(getResponsePage());
				table.add(archive, column++, row);
			}
			else
				table.add(getText(application.getProvider().getSchoolName(), isCurrentProvider), column++, row);
			table.add(getText(getStatusString(application), isCurrentProvider), column++, row);
			if (phone != null)
				table.add(getText(phone, isCurrentProvider), column, row);
			column++;
			table.add(getText(queueDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT), isCurrentProvider), column++, row);
			table.add(getText(placementDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT), isCurrentProvider), column++, row++);
		}
		table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);
		
		return table;
	}
	
	protected Form getButtonTable(IWContext iwc, boolean showAllButtons) throws RemoteException {
		Form form = new Form();
		form.add(new HiddenInput(PARAMETER_CREATE_CONTRACT, ""));
		
		Table table = new Table();
		table.setColumns(7);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(2, "4");
		table.setWidth(4, "4");
		table.setWidth(6, "4");
		table.setWidth(7, "100%");
		form.add(table);
		
		String dateWarning = null;
		
		GenericButton back = (GenericButton) getStyledInterface(new GenericButton("back",localize("back","Back")));
		back.setPageToOpen(getResponsePage());
		table.add(back, 1, 1);
		
		if (showAllButtons) {
			int numberInQueue = 1;
			boolean hasPriority = application.getHasPriority();
			boolean isAfterSchoolApplication = getBusiness().isAfterSchoolApplication(application);
			if (!isAfterSchoolApplication)
				numberInQueue = getBusiness().getNumberInQueueByStatus(application);
			char status = application.getApplicationStatus();

			if (status == getBusiness().getStatusSentIn()) {
				int column = 3;
				if (numberInQueue == 1 || hasPriority) {
					GenericButton changeDate = getButton("change_date", localize("child_care.change_date","Change date"), ChildCareAdminWindow.METHOD_CHANGE_DATE);
					table.add(changeDate, column++, 1);
					column++;

					GenericButton offer = (GenericButton) getStyledInterface(new GenericButton("offer", localize("child_care.offer_placing","Offer placing")));
					if (application.getHasDateSet()) {
						offer.setWindowToOpen(ChildCareWindow.class);
						offer.addParameterToWindow(ChildCareAdminWindow.PARAMETER_APPLICATION_ID, String.valueOf(getSession().getApplicationID()));
						offer.addParameterToWindow(ChildCareAdminWindow.PARAMETER_USER_ID, String.valueOf(getSession().getChildID()));
						offer.addParameterToWindow(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_OFFER);
						offer.addParameterToWindow(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
					}
					else
						offer.setDisabled(true);
					table.add(offer, column++, 1);
				}
				else {
					GenericButton priority = getButton("priority", localize("child_care.grant_priority","Grant priority"), ChildCareAdminWindow.METHOD_GRANT_PRIORITY);
					table.add(priority, column++, 1);
				}
				column++;

				if (isAfterSchoolApplication) {
					GenericButton reject = (GenericButton) getStyledInterface(new GenericButton("reject", localize("child_care.reject_application","Reject application")));
					reject.setWindowToOpen(ChildCareWindow.class);
					reject.addParameterToWindow(ChildCareAdminWindow.PARAMETER_APPLICATION_ID, String.valueOf(getSession().getApplicationID()));
					reject.addParameterToWindow(ChildCareAdminWindow.PARAMETER_USER_ID, String.valueOf(getSession().getChildID()));
					reject.addParameterToWindow(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_REJECT_APPLICATION);
					reject.addParameterToWindow(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
					table.add(reject, column++, 1);
				}
			}
			else if (status == getBusiness().getStatusAccepted()) {
				int column = 3;
				if (showParentsAgree) {
					GenericButton parentsAgree = getButton("parents_agree", localize("child_care.parents_agree","Parents agree"), -1);
					parentsAgree.addParameterToWindow(ChildCareAdminWindow.PARAMETER_ACTION, ChildCareAdminWindow.ACTION_PARENTS_AGREE);
					table.add(parentsAgree, column++, 1);
					column++;
				}
				
				IWTimestamp dateNow = new IWTimestamp();
				IWTimestamp validUntil = new IWTimestamp(application.getOfferValidUntil());
				if (dateNow.isLaterThanOrEquals(validUntil)) {
					GenericButton offer = (GenericButton) getStyledInterface(new GenericButton("change_offer", localize("child_care.change_offer_date","Change offer date")));
					offer.setWindowToOpen(ChildCareWindow.class);
					offer.addParameterToWindow(ChildCareAdminWindow.PARAMETER_APPLICATION_ID, String.valueOf(getSession().getApplicationID()));
					offer.addParameterToWindow(ChildCareAdminWindow.PARAMETER_USER_ID, String.valueOf(getSession().getChildID()));
					offer.addParameterToWindow(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_CHANGE_OFFER);
					offer.addParameterToWindow(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
					table.add(offer, column++, 1);
					column++;
					
					GenericButton removeFromQueue = (GenericButton) getStyledInterface(new GenericButton("remove_from_queue", localize("child_care.remove_from_queue","Remove from queue")));
					removeFromQueue.setWindowToOpen(ChildCareWindow.class);
					removeFromQueue.addParameterToWindow(ChildCareAdminWindow.PARAMETER_APPLICATION_ID, String.valueOf(getSession().getApplicationID()));
					removeFromQueue.addParameterToWindow(ChildCareAdminWindow.PARAMETER_USER_ID, String.valueOf(getSession().getChildID()));
					removeFromQueue.addParameterToWindow(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_RETRACT_OFFER);
					removeFromQueue.addParameterToWindow(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
					table.add(removeFromQueue, column, 1);
				}
			}
			else if (status == getBusiness().getStatusParentsAccept()) {
				SubmitButton createContract = null;
				GenericButton disabledCreateContract = null;
				
				if (getBusiness().getUserBusiness().hasBankLogin(application.getOwner())) {
					createContract = (SubmitButton) getButton(new SubmitButton(localize("child_care.create_contract_for_digital_signing","Create contract for BankID")));
					createContract.setValueOnClick(PARAMETER_CREATE_CONTRACT, ACTION_CREATE_BANKID_CONTRACT);
					if (_useSubmitConfirm) {
						createContract.setSingleSubmitConfirm(localize("child_care.confirm_create_contract", "OK to proceed with creating contract?"));
					}
					form.setToDisableOnSubmit(createContract, true);
							
					disabledCreateContract = (GenericButton) getStyledInterface(new GenericButton("create_contract", localize("child_care.create_contract_for_digital_signing","Create contract for BankID")));
					disabledCreateContract.setDisabled(true);
				}
				else {
					createContract = (SubmitButton) getButton(new SubmitButton(localize("child_care.create_contract","Create contract")));
					createContract.setValueOnClick(PARAMETER_CREATE_CONTRACT, ACTION_CREATE_REGULAR_CONTRACT);
					if (_useSubmitConfirm) {
						createContract.setSingleSubmitConfirm(localize("child_care.confirm_create_contract", "OK to proceed with creating contract?"));
					}
					form.setToDisableOnSubmit(createContract, true);
					
					disabledCreateContract = (GenericButton) getStyledInterface(new GenericButton("create_contract", localize("child_care.create_contract","Create contract")));
				  disabledCreateContract.setDisabled(true);
				}

				GenericButton changeDate = getButton("change_date", localize("child_care.change_date","Change date"), ChildCareAdminWindow.METHOD_CHANGE_DATE);
					
				if (getBusiness().hasActivePlacementNotWithProvider(getSession().getChildID(), getSession().getChildCareID())) {
					table.add(disabledCreateContract, 3, 1);
					dateWarning = localize("child_care.child_has_active_contract", "Child has an active contract");
				}
				else {
					if (getBusiness().hasTerminationInFutureNotWithProvider(getSession().getChildID(), getSession().getChildCareID())) {
						ChildCareContract archive = getBusiness().getLatestTerminatedContract(getSession().getChildID());
						if (archive != null) {
							IWTimestamp terminationDate = new IWTimestamp(archive.getTerminatedDate());
							IWTimestamp validFrom = new IWTimestamp(application.getFromDate());
							dateWarning = localize("child_care.earliest_possible_placement_date", "Earliest possible placement date") + ": " + terminationDate.getLocaleDate(iwc.getLocale(), IWTimestamp.SHORT);
							if (terminationDate.isLaterThanOrEquals(validFrom)) {
								terminationDate.addDays(1);
								changeDate.addParameterToWindow(ChildCareAdminWindow.PARAMETER_EARLIEST_DATE, terminationDate.toString());
								table.add(changeDate, 3, 1);
								table.add(disabledCreateContract, 5, 1);
							}
							else {
								table.add(changeDate, 3, 1);
								table.add(createContract, 5, 1);
							}
						}
						else {
							table.add(changeDate, 3, 1);
							table.add(createContract, 5, 1);
						}
					}
					else {
						table.add(changeDate, 3, 1);
						table.add(createContract, 5, 1);
					}
				}
			}
			else if (status == getBusiness().getStatusContract()) {
				int column = 3;

				GenericButton viewContract = (GenericButton) getStyledInterface(new GenericButton("view_contract", localize("child_care.view_contract","View contract")));
				viewContract.setFileToOpen(application.getContractFileId());
				table.add(viewContract, column++, 1);
				column++;

				if (showRecreateContract) {
					GenericButton recreateContract = getButton("recreate_contract", localize("child_care.recreate_contract", "Recreate contract"), -1);
					recreateContract.addParameterToWindow(ChildCareAdminWindow.PARAMETER_ACTION, ChildCareAdminWindow.ACTION_CREATE_CONTRACT);
					table.add(recreateContract, column++, 1);
					column++;
				}
				
				GenericButton placeInGroup = null;

				if (getBusiness().getUserBusiness().hasBankLogin(application.getOwner())) {
					if (! application.getContract().isSigned()) {
						placeInGroup = (GenericButton) getStyledInterface(new GenericButton("place_in_group", localize("child_care.place_in_group","Place in group")));
						placeInGroup.setDisabled(true);
						table.add(placeInGroup, column, 1);
					}
					else {
						placeInGroup = getButton("place_in_group", localize("child_care.place_in_group","Place in group"), ChildCareAdminWindow.METHOD_PLACE_IN_GROUP);
						table.add(placeInGroup, column, 1);
					}
				}
				else {
					placeInGroup = getButton("place_in_group", localize("child_care.place_in_group","Place in group"), ChildCareAdminWindow.METHOD_PLACE_IN_GROUP);
					table.add(placeInGroup, column, 1);
				}
			}
		}
		
		if (dateWarning != null) {
			table.setHeight(2, 6);
			table.mergeCells(1, 3, table.getColumns(), 3);
			table.add(getSmallErrorText(dateWarning), 1, 3);
		}
		
		return form;
	}
	
	protected GenericButton getButton(String name, String value, int method)  throws RemoteException {
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
	
	protected Link getLink(String text, boolean isCurrentProvider) {
		if (isCurrentProvider)
			return this.getSmallHeaderLink(text);
		else
			return getSmallLink(text);
	}
	
	private void parse(IWContext iwc) throws RemoteException {
		getBusiness().saveComments(getSession().getApplicationID(), iwc.getParameter(PARAMETER_COMMENTS));
		
		if (iwc.isParameterSet(PARAMETER_CREATE_CONTRACT)) {
			if (iwc.getParameter(PARAMETER_CREATE_CONTRACT).equals(ACTION_CREATE_REGULAR_CONTRACT)) {
				getBusiness().assignContractToApplication(getSession().getApplicationID(), -1, null, -1, iwc.getCurrentUser(), iwc.getCurrentLocale(), true);
			}
			else if (iwc.getParameter(PARAMETER_CREATE_CONTRACT).equals(ACTION_CREATE_BANKID_CONTRACT)) {
				getBusiness().assignContractToApplication(getSession().getApplicationID(), -1, null, -1, iwc.getCurrentUser(), iwc.getCurrentLocale(), true);
			}
		}
	}

	public void setContractsPage(ICPage page) {
		contractsPage = page;
	}

	public void setCanEdit(boolean b) {
		_canEdit = new Boolean(b);
	}

	/**
	 * @param b
	 */
	public void setShowParentsAgree(boolean b) {
		showParentsAgree = b;
	}
	
	/**
	 * @param b
	 */
	public void setShowRecreateContract(boolean b) {
		showRecreateContract = b;
	}

	/**
	 * @param useSubmitConfirm The useSubmitConfirm to set.
	 */
	public void setToUseSubmitConfirm(boolean useSubmitConfirm) {
		this._useSubmitConfirm = useSubmitConfirm;
	}
}