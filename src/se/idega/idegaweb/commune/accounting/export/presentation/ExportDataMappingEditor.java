/*
 * Created on 8.9.2003
 */
package se.idega.idegaweb.commune.accounting.export.presentation;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author laddi
 */
public class ExportDataMappingEditor extends AccountingBlock {

	private static final String PARAMETER_SAVE = "edm_save";
	
	private static final String PARAMETER_OPERATIONAL_FIELD = "edm_operational_field";
	private static final String PARAMETER_JOURNAL_NUMBER = "edm_journal_number";
	private static final String PARAMETER_ACCOUNT = "edm_account";
	private static final String PARAMETER_COUNTER_ACCOUNT = "edm_counter_account";
	private static final String PARAMETER_PAYABLE_ACCOUNT = "edm_payable_account";
	private static final String PARAMETER_CUSTOMER_CLAIM_ACCOUNT = "edm_customer_claim_account";
	private static final String PARAMETER_ACCOUNT_SETTLEMENT_TYPE = "edm_account_settlement_type";
	private static final String PARAMETER_CASH_FLOW_IN = "edm_cash_flow_in";
	private static final String PARAMETER_CASH_FLOW_OUT = "edm_cash_flow_out";
	private static final String PARAMETER_PROVIDER_AUTHORIZATION = "edm_provider_authorization";
	
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
		
		Table table = new Table(3, 21);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(2, 12);
		int row = 1;
		
		DropdownMenu operationalField = this.getDropdownMenuLocalized(PARAMETER_OPERATIONAL_FIELD, getBusiness().getExportBusiness().getAllOperationalFields(), "getLocalizedKey");
		operationalField.addMenuElementFirst("", localize("export.select_operational_field", "Select operational field"));
		operationalField.setToSubmit();
		if (_operationalField != null)
			operationalField.setSelectedElement(_operationalField);
			
		table.add(getSmallHeader(localize("export.operational_field", "Operational field") + ":"), 1, row);
		table.add(operationalField, 3, row++);
		
		TextInput journalNumber = getTextInput(PARAMETER_JOURNAL_NUMBER, "", _inputWidth, 255);
		if (_mapping != null && _mapping.getJournalNumber() != null)
			journalNumber.setContent(_mapping.getJournalNumber());
		
		table.setHeight(row++, 3);
		table.add(getSmallHeader(localize("export.journal_number", "Journal number") + ":"), 1, row);
		table.add(journalNumber, 3, row++);
		
		TextInput account = getTextInput(PARAMETER_ACCOUNT, "", _inputWidth, 255);
		if (_mapping != null && _mapping.getAccount() != null)
			account.setContent(_mapping.getAccount());
		
		table.setHeight(row++, 3);
		table.add(getSmallHeader(localize("export.account", "Account") + ":"), 1, row);
		table.add(account, 3, row++);
		
		TextInput counterAccount = getTextInput(PARAMETER_COUNTER_ACCOUNT, "", _inputWidth, 255);
		if (_mapping != null && _mapping.getCounterAccount() != null)
			counterAccount.setContent(_mapping.getCounterAccount());
		
		table.setHeight(row++, 3);
		table.add(getSmallHeader(localize("export.counter_account", "Counter account") + ":"), 1, row);
		table.add(counterAccount, 3, row++);
		
		TextInput payableAccount = getTextInput(PARAMETER_PAYABLE_ACCOUNT, "", _inputWidth, 255);
		if (_mapping != null && _mapping.getPayableAccount() != null)
			payableAccount.setContent(_mapping.getPayableAccount());
		
		table.setHeight(row++, 3);
		table.add(getSmallHeader(localize("export.payable_account", "Payable account") + ":"), 1, row);
		table.add(payableAccount, 3, row++);
		
		TextInput customerClaimAccount = getTextInput(PARAMETER_CUSTOMER_CLAIM_ACCOUNT, "", _inputWidth, 255);
		if (_mapping != null && _mapping.getCustomerClaimAccount() != null)
			customerClaimAccount.setContent(_mapping.getCustomerClaimAccount());
		
		table.setHeight(row++, 3);
		table.add(getSmallHeader(localize("export.customer_claim_account", "Customer claim account") + ":"), 1, row);
		table.add(customerClaimAccount, 3, row++);
		
		DropdownMenu accountSettlementType = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_ACCOUNT_SETTLEMENT_TYPE));
		accountSettlementType.addMenuElement(getBusiness().getExportBusiness().getAccountSettlementTypeDayByDay(), localize("export.type_day_by_day", "Day by day"));
		accountSettlementType.addMenuElement(getBusiness().getExportBusiness().getAccountSettlementTypeSpecificDate(), localize("export.type_specific_date", "Specific date"));
		if (_mapping != null && _mapping.getAccountSettlementType() != -1)
			accountSettlementType.setSelectedElement(_mapping.getAccountSettlementType());
		
		table.setHeight(row++, 3);
		table.add(getSmallHeader(localize("export.account_settlement_type", "Account settlement type") + ":"), 1, row);
		table.add(accountSettlementType, 3, row++);
		
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
		
		table.setHeight(row++, 3);
		table.mergeCells(1, row, 3, row);
		table.add(providerAuthorization, 1, row);
		table.add(getSmallHeader(Text.NON_BREAKING_SPACE + localize("export.provider_authorization", "Provider authorization")), 1, row++);
		
		SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("save", "Save"), PARAMETER_SAVE, "true"));
		if (_operationalField == null)
			save.setDisabled(true);
		
		table.setHeight(row++, 12);
		table.add(save, 1, row);
		
		form.add(table);
		add(form);
	}
	
	private void parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_OPERATIONAL_FIELD)) {
			_operationalField = iwc.getParameter(PARAMETER_OPERATIONAL_FIELD);
		}
			
		if (iwc.isParameterSet(PARAMETER_SAVE)) {
			boolean cashFlowIn = false;
			boolean cashFlowOut = false;
			boolean providerAuthorization = false;
			
			if (iwc.isParameterSet(PARAMETER_CASH_FLOW_IN))
				cashFlowIn = true;
			if (iwc.isParameterSet(PARAMETER_CASH_FLOW_OUT))
				cashFlowOut = true;
			if (iwc.isParameterSet(PARAMETER_PROVIDER_AUTHORIZATION))
				providerAuthorization = true;
				
			try {
				getBusiness().getExportBusiness().storeExportDataMapping(_operationalField, iwc.getParameter(PARAMETER_JOURNAL_NUMBER), iwc.getParameter(PARAMETER_ACCOUNT), iwc.getParameter(PARAMETER_COUNTER_ACCOUNT), iwc.getParameter(PARAMETER_PAYABLE_ACCOUNT), iwc.getParameter(PARAMETER_CUSTOMER_CLAIM_ACCOUNT), Integer.parseInt(iwc.getParameter(PARAMETER_ACCOUNT_SETTLEMENT_TYPE)), cashFlowIn, cashFlowOut, providerAuthorization);
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