package se.idega.idegaweb.commune.childcare.presentation.admin;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.care.business.ProviderSession;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import se.idega.idegaweb.commune.care.data.ChildCareContract;
import se.idega.idegaweb.commune.care.data.ChildCareContractHome;
import se.idega.idegaweb.commune.care.data.EmploymentType;
import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.event.ChildCareEventListener;
import se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock;

import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.presentation.SchoolClassDropdownDouble;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;

/**
 * @author gimmi
 */
public class ContractEditor extends ChildCareBlock {

	private ChildCareApplication application;

	private static String ACTION = "ce_a";

	private static String ACTION_EDIT = "ce_e";

	private static String ACTION_UPDATE = "ce_u";

	private static String ACTION_DELETE = "ce_d";

	public static String PARAMETER_CONTRACT_ID = "p_ci";

	public static String PARAMETER_APPLICATION_ID = "p_ai";

	private static String PARAMETER_FROM_DATE = "p_fd";

	private static String PARAMETER_CANCELLED_DATE = "p_cd";

	private static String PARAMETER_CLEAR_DATE = "p_cld";

	private static String PARAMETER_CARE_TIME = "p_ct";

	private static String PARAMETER_EMPLOYMENT_TYPE = "p_et";

	private static String PARAMETER_INVOICE_REVCEIVER = "p_ir";

	private static String PARAMETER_SCHOOL_TYPE = "p_sct";

	private static String PARAMETER_SCHOOL_CLASS = "p_scc";

	private boolean showBackButton = true;

	public void init(IWContext iwc) throws Exception {
		int applId = session.getApplicationID();
		if (iwc.isParameterSet(PARAMETER_APPLICATION_ID)) {
			try {
				applId = Integer.parseInt(iwc.getParameter(PARAMETER_APPLICATION_ID));
			}
			catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (applId > 0) {
			application = getBusiness().getApplication(applId);

			String action = iwc.getParameter(ACTION);
			if (ACTION_UPDATE.equals(action)) {
				if (handleUpdate(iwc)) {
					displayContracts(iwc);
				}
				else {
					displayEditor(iwc);
				}
			}
			else if (ACTION_DELETE.equals(action)) {
				deleteContract(iwc);
				displayContracts(iwc);
			}
			else if (ACTION_EDIT.equals(action) || iwc.isParameterSet(PARAMETER_CONTRACT_ID)) {
				displayEditor(iwc);
				displayContracts(iwc);
			}
			else {
				displayContracts(iwc);
			}
		}
		else {
			add(localize("child_care.no_application_selected", " No application selected "));
		}
		if (showBackButton) {
			add(Text.getBreak());
			GenericButton back = (GenericButton) getStyledInterface(new GenericButton("back", localize("child_care.select_new_application", "Select new application")));
			back.setPageToOpen(getBackPage());
			add(back);
		}

	}

	public void setShowBackButton(boolean flag) {
		this.showBackButton = flag;
	}

	private void deleteContract(IWContext iwc) {
		try {
			ChildCareContractHome contractHome = (ChildCareContractHome) IDOLookup.getHome(ChildCareContract.class);
			ChildCareContract contract = contractHome.findByPrimaryKey(new Integer(iwc.getParameter(PARAMETER_CONTRACT_ID)));
			getBusiness().removeContract(contract, iwc.getCurrentUser());
			add(getLocalizedSmallHeader("child_care.delete_successful", "Application successfully deleted"));
		}
		catch (Exception e) {
			add(getLocalizedSmallHeader("child_care.delete_failed", "Application was NOT deleted"));
		}
	}

	private boolean handleUpdate(IWContext iwc) {
		try {
			ChildCareContractHome contractHome = (ChildCareContractHome) IDOLookup.getHome(ChildCareContract.class);
			ChildCareContract contract = contractHome.findByPrimaryKey(new Integer(iwc.getParameter(PARAMETER_CONTRACT_ID)));
			// SchoolClassMember placement = contract.getSchoolClassMember();
			removeProviderSession(iwc);

			String fromDate = iwc.getParameter(PARAMETER_FROM_DATE);
			String toDate = iwc.getParameter(PARAMETER_CANCELLED_DATE);
			String careTime = iwc.getParameter(PARAMETER_CARE_TIME);
			String empType = iwc.getParameter(PARAMETER_EMPLOYMENT_TYPE);
			String inRe = iwc.getParameter(PARAMETER_INVOICE_REVCEIVER);
			String schType = iwc.getParameter(PARAMETER_SCHOOL_TYPE);
			String schClass = iwc.getParameter(PARAMETER_SCHOOL_CLASS);
			boolean clearTerminationDate = iwc.isParameterSet(PARAMETER_CLEAR_DATE);

			int iEmpType = -1;
			Date dFromDate = null;
			Date dToDate = null;
			int invoiceReceiver = -1;
			int schoolType = -1;
			int schoolClass = -1;

			try {
				iEmpType = Integer.parseInt(empType);
			}
			catch (Exception e) {
			}

			try {
				dFromDate = (new IWTimestamp(fromDate)).getDate();
			}
			catch (Exception e) {
			}

			try {
				if (!clearTerminationDate)
					dToDate = (new IWTimestamp(toDate)).getDate();
			}
			catch (Exception ignore) {
			}

			try {
				invoiceReceiver = Integer.parseInt(inRe);
			}
			catch (Exception ignore) {
			}

			try {
				schoolType = Integer.parseInt(schType);
			}
			catch (Exception ignore) {
			}

			try {
				schoolClass = Integer.parseInt(schClass);
			}
			catch (Exception ignore) {
			}

			boolean success = false;

			if (dFromDate != null) {
				success = getBusiness().alterContract(contract, careTime, dFromDate, dToDate, iwc.getCurrentLocale(), iwc.getCurrentUser(), iEmpType, invoiceReceiver, schoolType, schoolClass);
				/*
				 * if (iEmpType > 0) { contract.setEmploymentType(iEmpType); } if
				 * (invoiceReceiver > 0) {
				 * contract.setInvoiceReceiverID(invoiceReceiver); } contract.store();
				 * 
				 * if (placement != null && schoolType > 0) {
				 * placement.setSchoolTypeId(schoolType); placement.store(); }
				 */
			}

			add(getLocalizedSmallHeader("child_care.update_success", "Update success"));
			return success;
		}
		catch (Exception e) {
			e.printStackTrace();
			add(getLocalizedSmallHeader("child_care.update_failed", "Update failed"));
			return false;
		}
	}

	private void displayEditor(IWContext iwc) {
		Form form = new Form();
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		form.add(table);
		form.maintainParameter(PARAMETER_APPLICATION_ID);

		int row = 1;

		try {
			ChildCareContractHome contractHome = (ChildCareContractHome) IDOLookup.getHome(ChildCareContract.class);
			ChildCareContract contract = contractHome.findByPrimaryKey(new Integer(iwc.getParameter(PARAMETER_CONTRACT_ID)));
			ChildCareContract firstContract = contractHome.findFirstContractByApplication(contract.getApplicationID());
			boolean isFirstContract = contract.getPrimaryKey().equals(firstContract.getPrimaryKey());

			EmploymentType et = contract.getEmploymentType();
			int etId = -1;
			if (et != null) {
				etId = new Integer(et.getPrimaryKey().toString()).intValue();
			}

			// School school = contract.getApplication().getProvider();
			SchoolClassMember placement = contract.getSchoolClassMember();
			getProviderSession(iwc, placement.getSchoolClass().getSchoolId());
			Collection custodians = getBusiness().getUserBusiness().getParentsForChild(contract.getChild());
			/*
			 * Collection schoolTypes = null; try { schoolTypes =
			 * school.getSchoolTypes(); } catch (IDOException e) {
			 * e.printStackTrace(); schoolTypes = null; }
			 */

			DateInput from = (DateInput) getStyledInterface(new DateInput(PARAMETER_FROM_DATE));
			IWTimestamp stamp = IWTimestamp.RightNow();
			from.setYearRange(stamp.getYear() - 2, stamp.getYear() + 3);
			if (isFirstContract) {
				try {
					ChildCareContract latestContract = contractHome.findLatestNotByApplication(contract.getApplicationID(), contract.getValidFromDate());
					if (latestContract.getTerminatedDate() != null) {
						IWTimestamp earliestDate = new IWTimestamp(latestContract.getTerminatedDate());
						earliestDate.addDays(1);
						from.setEarliestPossibleDate(earliestDate.getDate(), localize("child_care.earliest_possible_date", "The earliest possible date is: ") + earliestDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT));
					}
				}
				catch (FinderException fe) {
					//No old contract found...
				}
			}

			DateInput cancelled = (DateInput) getStyledInterface(new DateInput(PARAMETER_CANCELLED_DATE));
			cancelled.setYearRange(stamp.getYear() - 2, stamp.getYear() + 3);
			if (contract.getTerminatedDate() != null) {
				cancelled.setDate(contract.getTerminatedDate());
			}
			if (contract.getValidFromDate() != null) {
				from.setDate(contract.getValidFromDate());
			}

			CheckBox clearTerminatedDate = new CheckBox(PARAMETER_CLEAR_DATE);
			clearTerminatedDate.setToolTip(localize("child_care.clear_termination_date", "Clear termination date"));

			ChildCareContract latestTerminatedContract = null;
			ChildCareContract nextContract = null;
			ChildCareContract previousContract = null;
			try {
				latestTerminatedContract = contractHome.findLatestTerminatedContractByChild(contract.getChildID(), null);
			}
			catch (FinderException e1) {
				// e1.printStackTrace();
			}
			try {
				nextContract = contractHome.findNextContractByChild(contract);
			}
			catch (FinderException e2) {
				// e2.printStackTrace();
			}
			try {
				previousContract = contractHome.findPreviousTerminatedContractByChild(contract);
			}
			catch (FinderException e3) {
				// e3.printStackTrace();
			}
			if (previousContract != null) {
				IWTimestamp previous = new IWTimestamp(previousContract.getTerminatedDate());
				previous.addDays(1);
				from.setEarliestPossibleDate(previous.getDate(), localize("child_care.date_warning.start_earlier_than_previous_termination", "You can not choose a start date earlier than previous termination date."));
			}
			if (nextContract != null) {
				IWTimestamp next = new IWTimestamp(nextContract.getValidFromDate());
				next.addDays(-1);
				cancelled.setLatestPossibleDate(next.getDate(), localize("child_care.date_warning.termination_later_than_nex_startdate", "You can not choose a termination date later than next start date"));
			}

			InterfaceObject careTime = null;
			if (isUsePredefinedCareTimeValues()) {
				DropdownMenu menu = getCareTimeMenu(PARAMETER_CARE_TIME);
				if (contract.getCareTime() != null) {
					menu.setSelectedElement(contract.getCareTime());
				}
				careTime = menu;
			}
			else {
				TextInput input = (TextInput) getStyledInterface(new TextInput(PARAMETER_CARE_TIME));
				input.setAsNotEmpty(localize("child_care.child_care_time_required", "You must fill in the child care time."));
				input.setAsIntegers(localize("child_care.only_integers_allowed", "Not a valid child care time."));
				input.setSize(10);
				if (contract.getCareTime() != null) {
					input.setContent(contract.getCareTime());
				}
				careTime = input;
			}

			DropdownMenu mCustodians = (DropdownMenu) getStyledInterface(new DropdownMenu(custodians, PARAMETER_INVOICE_REVCEIVER));
			if (contract.getInvoiceReceiverID() > 0) {
				mCustodians.setSelectedElement(contract.getInvoiceReceiverID());
			}
			/*
			 * DropdownMenu mSchoolType = (DropdownMenu)getStyledInterface(new
			 * DropdownMenu(PARAMETER_SCHOOL_TYPE)); SelectorUtility su = new
			 * SelectorUtility(); mSchoolType = (DropdownMenu)
			 * su.getSelectorFromIDOEntities(mSchoolType, schoolTypes,
			 * "getLocalizationKey", getResourceBundle()); if (placement != null &&
			 * placement.getSchoolTypeId() > 0) {
			 * mSchoolType.setSelectedElement(placement.getSchoolTypeId()); }
			 */

			Collection types = null;
			try {
				types = application.getProvider().findRelatedSchoolTypes();

			}
			catch (IDORelationshipException e) {
				e.printStackTrace();
			}
			catch (EJBException e) {
				e.printStackTrace();
			}

			SchoolClassDropdownDouble schoolClasses = new SchoolClassDropdownDouble(PARAMETER_SCHOOL_TYPE, PARAMETER_SCHOOL_CLASS);
			// schoolClasses.setLayoutVertical(true);
			// schoolClasses.setPrimaryLabel(getSmallText(localize("child_care.school_type",
			// "School type")));
			// schoolClasses.setSecondaryLabel(getSmallText(localize("child_care.school_class",
			// "School class")));
			// schoolClasses.setVerticalSpaceBetween(15);
			// schoolClasses.setSpaceBetween(15);
			// schoolClasses.setNoDataListEntry(localize("child_care.no_school_classes","No
			// school classes"));
			schoolClasses = (SchoolClassDropdownDouble) getStyledInterface(schoolClasses);
			// int classID = archive.getSchoolClassMember().getSchoolClassId();
			schoolClasses.getSecondaryDropdown().setAsNotEmpty(localize("child_care.must_choose_a_school_class", "You must choose a school class"));
			schoolClasses.getPrimaryDropdown().setAsNotEmpty(localize("child_care.must_choose_a_school_type", "You must choose a school type"));

			if (!types.isEmpty()) {
				ChildCareBusiness childCareBusiness = (ChildCareBusiness) IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);
				Map typeGroupMap = childCareBusiness.getSchoolTypeClassMap(types, application.getProviderId(), getSession().getSeasonID(), null, null, localize("child_care.no_school_classes", "No school classes"));
				if (typeGroupMap != null) {
					Iterator iter = typeGroupMap.keySet().iterator();
					while (iter.hasNext()) {
						SchoolType schoolType = (SchoolType) iter.next();
						schoolClasses.addMenuElement(schoolType.getPrimaryKey().toString(), schoolType.getSchoolTypeName(), (Map) typeGroupMap.get(schoolType));
					}
				}
			}

			if (placement != null)
				schoolClasses.setSelectedValues(String.valueOf(placement.getSchoolTypeId()), String.valueOf(placement.getSchoolClassId()));

			DropdownMenu employmentTypes = getEmploymentTypes(PARAMETER_EMPLOYMENT_TYPE, etId);

			employmentTypes.setAsNotEmpty(localize("child_care.must_select_employment_type", "You must select employment type."), "-1");
			employmentTypes = (DropdownMenu) getStyledInterface(employmentTypes);

			table.add(getSmallHeader(contract.getChild().getName() + " - " + contract.getApplication().getProvider().getName()), 1, row);
			table.mergeCells(1, row, 2, row++);

			table.add(new HiddenInput(PARAMETER_CONTRACT_ID, contract.getPrimaryKey().toString()));
			table.add(getLocalizedSmallText("child_care.valid_from", "Valid from"), 1, row);
			table.add(from, 2, row++);
			table.add(getLocalizedSmallText("child_care.terminated", "Terminated"), 1, row);
			table.add(cancelled, 2, row);
			if (latestTerminatedContract != null && nextContract == null && latestTerminatedContract.getPrimaryKey().equals(contract.getPrimaryKey()))
				table.add(clearTerminatedDate, 2, row);
			row++;
			table.add(getLocalizedSmallText("child_care.care_time", "Care time"), 1, row);
			table.add(careTime, 2, row++);
			table.add(getLocalizedSmallText("child_care.employment", "Employment"), 1, row);
			table.add(employmentTypes, 2, row++);
			table.add(getLocalizedSmallText("child_care.invoice_receiver", "Invoice receiver"), 1, row);
			table.add(mCustodians, 2, row++);
			table.add(getLocalizedSmallText("child_care.school_type", "School type"), 1, row);
			// table.add(mSchoolType, 2, row++);
			table.add(schoolClasses.getPrimaryDropdown(), 2, row++);
			table.add(getLocalizedSmallText("child_care.school_class", "School class"), 1, row);
			table.add(schoolClasses.getSecondaryDropdown(), 2, row++);
			table.add(schoolClasses, 2, row);

			row++;
			GenericButton createGroup = new SubmitButton(getResourceBundle().getLocalizedImageButton("child_care.create_group", "Create group"));
			// TODO get some school class creator window
			//createGroup.setWindowToOpen(CentralPlacementSchoolGroupEditor.class);
			createGroup.setDisabled(true);
			table.add(createGroup, 2, row);

			Link back = new Link(getResourceBundle().getLocalizedImageButton("child_care.cancel", "Cancel"));
			back.maintainParameter(PARAMETER_APPLICATION_ID, iwc);
			table.add(back, 1, row);

			SubmitButton update = new SubmitButton(getResourceBundle().getLocalizedImageButton("child_care.update", "Update"), ACTION, ACTION_UPDATE);
			table.add(update, 2, row);
			table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);

		}
		catch (Exception e) {
			logError(e.getMessage());
		}

		// table.add();
		table.setHorizontalZebraColored(getZebraColor1(), getZebraColor2());
		table.setRowColor(1, getHeaderColor());

		add(form);
	}

	private void displayContracts(IWContext iwc) throws RemoteException {
		Collection contracts = null;
		try {
			contracts = getBusiness().getContractsByApplication(Integer.parseInt(application.getPrimaryKey().toString()));
		}
		catch (EJBException e) {
			logError(e.getMessage());
		}

		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(11);

		int row = 1;
		int column = 1;

		table.mergeCells(1, row, 5, row);
		table.add(getSmallHeader(application.getProvider().getName()), 1, row);

		++row;
		table.add(getLocalizedSmallHeader("child_care.name", "Name"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.personal_id", "Personal ID"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.created", "Created"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.valid_from", "Valid from"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.terminated", "Terminated"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.status", "Status"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.care_time", "Care time"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.employment", "Employment"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.school_type", "School type"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.school_class", "School class"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.invoice_receiver", "Invoice receiver"), column++, row);
		table.setRowColor(row++, getHeaderColor());

		boolean showComment = false;
		boolean showNotActiveComment = false;
		boolean showRemovedComment = false;

		if (contracts != null && !contracts.isEmpty()) {
			ChildCareContract contract;
			SchoolClassMember placement;
			User child;
			IWTimestamp created;
			IWTimestamp validFrom;
			IWTimestamp cancelledDate;
			EmploymentType et;
			Link editLink = null;
			Link deleteLink = null;
			Link viewContract;
			Link archive;
			boolean isCancelled;
			boolean isNotYetActive;
			boolean hasComment = false;
			IWTimestamp dateNow = new IWTimestamp();

			Iterator iter = contracts.iterator();
			while (iter.hasNext()) {
				// contract =
				// getBusiness().getValidContract(((Integer)application.getPrimaryKey()).intValue());
				contract = (ChildCareContract) iter.next();
				placement = contract.getSchoolClassMember();
				child = contract.getChild();
				hasComment = true;
				column = 1;

				if (row % 2 == 0)
					table.setRowColor(row, getZebraColor1());
				else
					table.setRowColor(row, getZebraColor2());

				if (contract != null) {
					created = new IWTimestamp(contract.getCreatedDate());
					if (contract.getValidFromDate() != null) {
						validFrom = new IWTimestamp(contract.getValidFromDate());
					}
					else {
						validFrom = null;
					}
					if (contract.getTerminatedDate() != null) {
						cancelledDate = new IWTimestamp(contract.getTerminatedDate());
						isCancelled = cancelledDate.isEarlierThan(dateNow);
					}
					else {
						cancelledDate = null;
						isCancelled = false;
					}

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

					// viewContract = new
					// Link(getPDFIcon(localize("child_care.view_contract","View
					// contract")));
					// viewContract.setFile(contract.getContractFileID());
					// viewContract.setTarget(Link.TARGET_NEW_WINDOW);
					viewContract = getPDFLink(contract.getContractFileID(), localize("child_care.view_contract", "View contract"));

					editLink = new Link(getEditIcon(localize("child_care.edit_contract", "Edit contract")));
					editLink.addParameter(ACTION, ACTION_EDIT);
					editLink.addParameter(PARAMETER_CONTRACT_ID, contract.getPrimaryKey().toString());
					editLink.addParameter(PARAMETER_APPLICATION_ID, contract.getApplicationID());

					deleteLink = new Link(getDeleteIcon(localize("child_care.delete_contract", "Delete contract")));
					deleteLink.addParameter(ACTION, ACTION_DELETE);
					deleteLink.addParameter(PARAMETER_CONTRACT_ID, contract.getPrimaryKey().toString());
					deleteLink.addParameter(PARAMETER_APPLICATION_ID, contract.getApplicationID());

					if (isNotYetActive) {
						showComment = true;
						hasComment = true;
						showNotActiveComment = true;
						table.add(getSmallErrorText("*"), column, row);
					}
					if (contract.getTerminatedDate() != null) {
						showComment = true;
						hasComment = true;
						showRemovedComment = true;
						table.add(getSmallErrorText("+"), column, row);
					}

					if (hasComment)
						table.add(getSmallErrorText(Text.NON_BREAKING_SPACE), column, row);
					if (getResponsePage() != null) {
						Name name = new Name(child.getFirstName(), child.getMiddleName(), child.getLastName());
						archive = getSmallLink(name.getName(iwc.getApplicationSettings().getDefaultLocale(), true));
						archive.setEventListener(ChildCareEventListener.class);
						archive.addParameter(getSession().getParameterUserID(), contract.getChildID());
						archive.addParameter(getSession().getParameterApplicationID(), contract.getApplicationID());
						archive.setPage(getResponsePage());
						table.add(archive, column++, row);
					}
					else {
						Name name = new Name(child.getFirstName(), child.getMiddleName(), child.getLastName());
						table.add(getSmallText(name.getName(iwc.getApplicationSettings().getDefaultLocale(), true)), column++, row);
					}

					table.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), column++, row);
					table.add(getSmallText(created.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
					if (validFrom != null)
						table.add(getSmallText(validFrom.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
					else
						table.add(getSmallText("-"), column++, row);
					if (cancelledDate != null)
						table.add(getSmallText(cancelledDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
					else
						table.add(getSmallText("-"), column++, row);
					if (isCancelled)
						table.add(getSmallText(localize("child_care.status_cancelled", "Cancelled")), column, row);
					else
						table.add(getSmallText(localize("child_care.status_active", "Active")), column, row);
					column++;
					table.add(getSmallText(getCareTime(contract.getCareTime())), column++, row);

					et = contract.getEmploymentType();
					if (et != null) {
						table.add(getSmallText(localize(et.getLocalizationKey(), et.getLocalizationKey())), column, row);
					}
					else {
						table.add(getSmallText("-"), column, row);
					}
					column++;

					if (placement != null && placement.getSchoolTypeId() > 0) {
						table.add(getSmallText(localize(placement.getSchoolType().getLocalizationKey(), placement.getSchoolType().getLocalizationKey())), column, row);
					}
					else {
						table.add(getSmallText("-"), column, row);
					}
					column++;

					if (placement != null && placement.getSchoolClassId() > 0) {
						table.add(getSmallText((placement.getSchoolClass().getSchoolClassName())), column, row);
					}
					else {
						table.add(getSmallText("-"), column, row);
					}
					column++;

					if (contract.getInvoiceReceiver() != null) {
						table.add(getSmallText(contract.getInvoiceReceiver().getName()), column, row);
					}
					else {
						table.add(getSmallText("-"), column, row);
					}
					column++;

					table.setWidth(column, row, 12);
					table.add(viewContract, column++, row);

					table.setWidth(column, row, 12);
					table.add(editLink, column++, row);
					table.add(Text.NON_BREAKING_SPACE);
					table.setWidth(column, row, 12);
					table.add(deleteLink, column++, row);
				}
				else {
					Name name = new Name(child.getFirstName(), child.getMiddleName(), child.getLastName());
					table.add(getSmallText(name.getName(iwc.getApplicationSettings().getDefaultLocale(), true)), column++, row);
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
		else {
			table.add(getSmallErrorText(localize("child_care.no_contract_for_applicaiton", "no contract for application")), 1, row);
			table.mergeCells(1, row, table.getColumns(), row);
		}

		if (showComment) {
			table.setHeight(row++, 2);
			if (showNotActiveComment) {
				table.mergeCells(1, row, table.getColumns(), row);
				table.add(getSmallErrorText("*"), 1, row);
				table.add(getSmallText(Text.NON_BREAKING_SPACE + localize("child_care.not_yet_active_placing", "Placing not yet active")), 1, row++);
			}
			if (showRemovedComment) {
				table.mergeCells(1, row, table.getColumns(), row);
				table.add(getSmallErrorText("+"), 1, row);
				table.add(getSmallText(Text.NON_BREAKING_SPACE + localize("child_care.contract_ended", "Contract has termination date")), 1, row++);
			}
		}

		// }
		// contract.get
		add(table);
	}

	private ProviderSession getProviderSession(IWApplicationContext iwac, int providerID) throws RemoteException {
		ProviderSession sess = (ProviderSession) IBOLookup.getServiceInstance(iwac, ProviderSession.class);
		sess.setProviderID(providerID);
		return sess;
	}

	private void removeProviderSession(IWUserContext iwuc) throws RemoveException, RemoteException {
		IBOLookup.removeSessionInstance(iwuc, ProviderSession.class);
	}

}
