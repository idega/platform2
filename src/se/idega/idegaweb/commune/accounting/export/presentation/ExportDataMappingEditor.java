/*
 * Created on 8.9.2003
 */
package se.idega.idegaweb.commune.accounting.export.presentation;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.posting.business.PostingParametersException;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;

import com.idega.presentation.IWContext;
import com.idega.presentation.Script;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.IntegerInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author laddi
 */
public class ExportDataMappingEditor extends AccountingBlock {

	private static final String PARAMETER_SAVE = "edm_save";
	
	private static final String PARAMETER_OPERATIONAL_FIELD = "edm_operational_field";
	private static final String PARAMETER_JOURNAL_NUMBER = "edm_journal_number";
//	private static final String PARAMETER_ACCOUNT = "edm_account";
//	private static final String PARAMETER_COUNTER_ACCOUNT = "edm_counter_account";
//	private static final String PARAMETER_PAYABLE_ACCOUNT = "edm_payable_account";
//	private static final String PARAMETER_CUSTOMER_CLAIM_ACCOUNT = "edm_customer_claim_account";
	private static final String PARAMETER_FILE_CREATION_FOLDER = "edm_file_creation_folder";
	private static final String PARAMETER_IFS_FILE_FOLDER = "edm_ifs_file_folder";
	private static final String PARAMETER_FILE_BACKUP_FOLDER = "edm_ifs_file_backup_folder";
	private static final String PARAMETER_LIST_CREATION_FOLDER = "edm_ifs_list_folder";
	private static final String PARAMETER_LIST_BACKUP_FOLDER = "edm_ifs_list_backup_folder";
	private static final String PARAMETER_ACCOUNT_SETTLEMENT_TYPE = "edm_account_settlement_type";
	private static final String PARAMETER_STANDARD_PAYMENT_DAY = "edm_standard_payment_day";
	private static final String PARAMETER_CASH_FLOW_IN = "edm_cash_flow_in";
	private static final String PARAMETER_CASH_FLOW_OUT = "edm_cash_flow_out";
	private static final String PARAMETER_PROVIDER_AUTHORIZATION = "edm_provider_authorization";
	private static final String PARAMETER_CREATE_PAYMENT_OUTSIDE_COMMUNE = "edm_create_outside_payment";
	private static final String PARAMETER_USE_SPECIFIED_DAYS = "edm_use_specified_days";
	private static final String PARAMETER_SPECIFIED_DAYS = "edm_specified_days";

	private final static String KEY_ERROR_LENGTH	= "posting_parm_edit.error_length";
	
	private int _inputWidth = 100;
	
	private String _operationalField = null;
	private ExportDataMapping _mapping = null;

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		parseAction(iwc);
		drawForm();
	}
	
	private void drawForm() throws RemoteException {
		Form form = new Form();
		form.setOnSubmit("return validateForm();");
		
		Script script = new Script();
		script.addFunction("validateForm", getValidateFormScript());

		Table table = new Table(3, 37);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(2, 12);
		table.setWidth(3, Table.HUNDRED_PERCENT);
		table.setWidth(getWidth());
		int row = 1;
		
		table.add(getHeader(localize("export.export_data_mapping_editor", "Export data mapping editor")), 1, row);
		table.setRowColor(1, getHeaderColor());
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_CENTER);
		table.setCellpadding(1, row, 3);
		table.mergeCells(1, row, table.getColumns(), row++);
		table.setHeight(row++, 12);

		DropdownMenu operationalField = this.getDropdownMenuLocalized(PARAMETER_OPERATIONAL_FIELD, getBusiness().getExportBusiness().getAllOperationalFields(), "getLocalizedKey");
		operationalField.addMenuElementFirst("", localize("export.select_operational_field", "Select operational field"));
		operationalField.setToSubmit();
		if (_operationalField != null)
			operationalField.setSelectedElement(_operationalField);
			
		table.add(getSmallHeader(localize("export.operational_field", "Operational field") + ":"), 1, row);
		table.setNoWrap(1, row);
		table.add(operationalField, 3, row++);
		
		TextInput journalNumber = getTextInput(PARAMETER_JOURNAL_NUMBER, "", _inputWidth, 255);
		if (_mapping != null && _mapping.getJournalNumber() != null)
			journalNumber.setContent(_mapping.getJournalNumber());
		
		table.setHeight(row++, 3);
		table.add(getSmallHeader(localize("export.journal_number", "Journal number") + ":"), 1, row);
		table.setNoWrap(1, row);
		table.add(journalNumber, 3, row++);

		TextInput fileCreationFolder = getTextInput(PARAMETER_FILE_CREATION_FOLDER, "", _inputWidth * 3, 255);
		if (_mapping != null && _mapping.getFileCreationFolder() != null)
			fileCreationFolder.setContent(_mapping.getFileCreationFolder());
		
		table.setHeight(row++, 3);
		table.add(getSmallHeader(localize("export.file_creation_folder", "File creation folder") + ":"), 1, row);
		table.setNoWrap(1, row);
		table.add(fileCreationFolder, 3, row++);
		
		TextInput IFSFileFolder = getTextInput(PARAMETER_IFS_FILE_FOLDER, "", _inputWidth * 3, 255);
		if (_mapping != null && _mapping.getIFSFileFolder() != null)
			IFSFileFolder.setContent(_mapping.getIFSFileFolder());
		
		table.setHeight(row++, 3);
		table.add(getSmallHeader(localize("export.ifs_file_folder", "IFS file folder") + ":"), 1, row);
		table.setNoWrap(1, row);
		table.add(IFSFileFolder, 3, row++);

		TextInput fileBackupFolder = getTextInput(PARAMETER_FILE_BACKUP_FOLDER, "", _inputWidth * 3, 255);
		if (_mapping != null && _mapping.getFileBackupFolder() != null)
			fileBackupFolder.setContent(_mapping.getFileBackupFolder());
		
		table.setHeight(row++, 3);
		table.add(getSmallHeader(localize("export.file_backup_folder", "File backup folder") + ":"), 1, row);
		table.setNoWrap(1, row);
		table.add(fileBackupFolder, 3, row++);

		TextInput listCreationFolder = getTextInput(PARAMETER_LIST_CREATION_FOLDER, "", _inputWidth * 3, 255);
		if (_mapping != null && _mapping.getListCreationFolder() != null)
			listCreationFolder.setContent(_mapping.getListCreationFolder());
		
		table.setHeight(row++, 3);
		table.add(getSmallHeader(localize("export.list_creation_folder", "List creation folder") + ":"), 1, row);
		table.setNoWrap(1, row);
		table.add(listCreationFolder, 3, row++);
		
		TextInput listBackupFolder = getTextInput(PARAMETER_LIST_BACKUP_FOLDER, "", _inputWidth * 3, 255);
		if (_mapping != null && _mapping.getListBackupFolder() != null)
			listBackupFolder.setContent(_mapping.getListBackupFolder());
		
		table.setHeight(row++, 3);
		table.add(getSmallHeader(localize("export.list_backup_folder", "List backup folder") + ":"), 1, row);
		table.setNoWrap(1, row);
		table.add(listBackupFolder, 3, row++);
		
		DropdownMenu accountSettlementType = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_ACCOUNT_SETTLEMENT_TYPE));
		accountSettlementType.addMenuElement(getBusiness().getExportBusiness().getAccountSettlementTypeDayByDay(), localize("export.type_day_by_day", "Day by day"));
		accountSettlementType.addMenuElement(getBusiness().getExportBusiness().getAccountSettlementTypeSpecificDate(), localize("export.type_specific_date", "Specific date"));
		if (_mapping != null && _mapping.getAccountSettlementType() != -1)
			accountSettlementType.setSelectedElement(_mapping.getAccountSettlementType());
		
		table.setHeight(row++, 3);
		table.add(getSmallHeader(localize("export.account_settlement_type", "Account settlement type") + ":"), 1, row);
		table.setNoWrap(1, row);
		table.add(accountSettlementType, 3, row++);
		
		DropdownMenu standardPaymentDay = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_STANDARD_PAYMENT_DAY));
		standardPaymentDay.addMenuElement(-1, localize("export.select_day", "Select day"));
		for (int i = 1; i <= 31; i++) {
			standardPaymentDay.addMenuElement(i, String.valueOf(i));
		}
		if (_mapping != null && _mapping.getStandardPaymentDay() != -1)
			standardPaymentDay.setSelectedElement(_mapping.getStandardPaymentDay());
		
		table.setHeight(row++, 3);
		table.add(getSmallHeader(localize("export.standard_payment_day", "Standard payment day") + ":"), 1, row);
		table.setNoWrap(1, row);
		table.add(standardPaymentDay, 3, row++);
		
		CheckBox cashFlowIn = getCheckBox(PARAMETER_CASH_FLOW_IN, "true");
		if (_mapping != null)
			cashFlowIn.setChecked(_mapping.getCashFlowIn());
		
		table.setHeight(row++, 12);
		table.mergeCells(1, row, 3, row);
		table.add(cashFlowIn, 1, row);
		table.add(getSmallHeader(Text.NON_BREAKING_SPACE + localize("export.cash_flow_in", "Cash flow in")), 1, row++);
		
		CheckBox cashFlowOut = getCheckBox(PARAMETER_CASH_FLOW_OUT, "true");
		if (_mapping != null)
			cashFlowOut.setChecked(_mapping.getCashFlowOut());
		
		table.setHeight(row++, 3);
		table.mergeCells(1, row, 3, row);
		table.add(cashFlowOut, 1, row);
		table.add(getSmallHeader(Text.NON_BREAKING_SPACE + localize("export.cash_flow_out", "Cash flow out")), 1, row++);
		
		CheckBox providerAuthorization = getCheckBox(PARAMETER_PROVIDER_AUTHORIZATION, "true");
		if (_mapping != null)
			providerAuthorization.setChecked(_mapping.getProviderAuthorization());
		
		table.setHeight(row++, 9);
		table.mergeCells(1, row, 3, row);
		table.add(providerAuthorization, 1, row);
		table.add(getSmallHeader(Text.NON_BREAKING_SPACE + localize("export.provider_authorization", "Provider authorization")), 1, row++);

		CheckBox createPayments = getCheckBox(PARAMETER_CREATE_PAYMENT_OUTSIDE_COMMUNE, "true");
		if (_mapping != null) {
			createPayments.setChecked(_mapping.getCreatePaymentsForCommuneProvidersOutsideCommune());
		}

		table.setHeight(row++, 9);
		table.mergeCells(1, row, 3, row);
		table.add(createPayments, 1, row);
		table.add(getSmallHeader(Text.NON_BREAKING_SPACE + localize("export.create_outside_payments", "Create payments for commune schools outside default commune")), 1, row++);

		CheckBox useSpecifiedDays = getCheckBox(PARAMETER_USE_SPECIFIED_DAYS, "true");
		IntegerInput specifiedDays = getIntegerInput(PARAMETER_SPECIFIED_DAYS, 0);
		if (_mapping != null) {
			useSpecifiedDays.setChecked(_mapping.getUseSpecifiedNumberOfDaysPrMonth());
			specifiedDays.setValue(_mapping.getSpecifiedNumberOfDaysPrMonth());
		}

		table.setHeight(row++, 9);
		table.mergeCells(1, row, 3, row);
		table.add(useSpecifiedDays, 1, row);
		table.add(getSmallHeader(Text.NON_BREAKING_SPACE + localize("export.use_specified_days", "Use specified days pr. month") + Text.NON_BREAKING_SPACE), 1, row);
		table.add(specifiedDays, 1, row);
		table.add(getSmallHeader(Text.NON_BREAKING_SPACE + localize("export.days","days")), 1, row);
		
		form.add(table);

		String accountString = "";
		String counterAccountString = "";
		String payableAccountString = "";
		String customerClaimAccountString = "";
		if (_mapping != null) {
			accountString = _mapping.getAccount();
			counterAccountString = _mapping.getCounterAccount();
			payableAccountString = _mapping.getPayableAccount();
			customerClaimAccountString = _mapping.getCustomerClaimAccount();			
		}
		AccountBlock ab = new AccountBlock(accountString, counterAccountString, payableAccountString, customerClaimAccountString);
		form.add(ab);

		SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("save", "Save"), PARAMETER_SAVE, "true"));
		if (_operationalField == null)
			save.setDisabled(true);
		
		form.add(Text.getBreak());
		form.add(save);
		
		form.add(script);		
		add(form);
	}
	
	private String getValidateFormScript() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("function validateForm() {").append("\n\t\t");
		buffer.append("var inChecked = findObj('"+ExportDataMappingEditor.PARAMETER_CASH_FLOW_IN+"').checked;").append("\n\t\t");
		buffer.append("var outChecked = findObj('"+ExportDataMappingEditor.PARAMETER_CASH_FLOW_OUT+"').checked;").append("\n\t\t");
		buffer.append("if (!inChecked && !outChecked) {").append("\n\t\t\t");
		buffer.append("alert('"+localize("export.must_check_one", "You must check at least IN or OUT.")+"');").append("\n\t\t\t");
		buffer.append("return false;").append("\n\t\t");
		buffer.append("}").append("\n\t\t");
		buffer.append("return true;").append("\n").append("}");
			
		return buffer.toString();
	}
	
	private void parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_OPERATIONAL_FIELD)) {
			_operationalField = iwc.getParameter(PARAMETER_OPERATIONAL_FIELD);
		}
			
		if (iwc.isParameterSet(PARAMETER_SAVE)) {
			String account = "";
			String counterAccount = "";
			String payableAccount = "";
			String customerClaimAccount = "";
			AccountBlock ab = null;
			try {
				ab = new AccountBlock(iwc);
				account = ab.getOwnPosting();
				counterAccount = ab.getDoublePosting();
				payableAccount = ab.getPayableAccount();
				customerClaimAccount = ab.getCustomerClaimAccount();
			} catch (PostingParametersException e) {
				if(e.getTextKey().compareTo(KEY_ERROR_LENGTH) == 0) {
					add(getErrorText(localize(e.getTextKey(), "Fel längd på fält: ") + e.getDefaultText()));
				} else {
					add(getErrorText(localize(e.getTextKey(), e.getDefaultText())));
				}
			}
			
			boolean cashFlowIn = false;
			boolean cashFlowOut = false;
			boolean providerAuthorization = false;
			
			if (iwc.isParameterSet(PARAMETER_CASH_FLOW_IN))
				cashFlowIn = true;
			if (iwc.isParameterSet(PARAMETER_CASH_FLOW_OUT))
				cashFlowOut = true;
			if (iwc.isParameterSet(PARAMETER_PROVIDER_AUTHORIZATION))
				providerAuthorization = true;
			
			boolean createForOutsideCommune = false;
			boolean useSpecificDays = false;
			int specificDays = -1;
			
			if (iwc.isParameterSet(PARAMETER_CREATE_PAYMENT_OUTSIDE_COMMUNE)) {
				createForOutsideCommune = true;
			}
			if (iwc.isParameterSet(PARAMETER_USE_SPECIFIED_DAYS)) {
				useSpecificDays = true;
				if (iwc.isParameterSet(PARAMETER_SPECIFIED_DAYS)) {
					String days = iwc.getParameter(PARAMETER_SPECIFIED_DAYS);
					specificDays = Integer.parseInt(days);
				}
			}
			
			try {
				getBusiness().getExportBusiness().storeExportDataMapping(
						_operationalField, 
						iwc.getParameter(PARAMETER_JOURNAL_NUMBER), 
						account, 
						counterAccount, 
						payableAccount, 
						customerClaimAccount, 
						iwc.getParameter(PARAMETER_FILE_CREATION_FOLDER), 
						iwc.getParameter(PARAMETER_IFS_FILE_FOLDER), 
						iwc.getParameter(PARAMETER_FILE_BACKUP_FOLDER), 
						iwc.getParameter(PARAMETER_LIST_CREATION_FOLDER), 
						iwc.getParameter(PARAMETER_LIST_BACKUP_FOLDER), 
						Integer.parseInt(iwc.getParameter(PARAMETER_ACCOUNT_SETTLEMENT_TYPE)), 
						Integer.parseInt(iwc.getParameter(PARAMETER_STANDARD_PAYMENT_DAY)), 
						cashFlowIn, 
						cashFlowOut, 
						providerAuthorization,
						createForOutsideCommune,
						useSpecificDays,
						specificDays);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
		if (_operationalField != null) {
			try {
				_mapping = getBusiness().getExportBusiness().getExportDataMapping(_operationalField);
			}
			catch (RemoteException e) {
				_mapping = null;
			}
			catch (FinderException e) {
				_mapping = null;
			}
		}
	}

	/**
	 * @param inputWidth
	 */
	public void setInputWidth(int inputWidth) {
		_inputWidth = inputWidth;
	}
}