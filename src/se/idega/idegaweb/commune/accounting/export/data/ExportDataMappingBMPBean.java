/*
 * Created on 8.9.2003
 */
package se.idega.idegaweb.commune.accounting.export.data;

import javax.ejb.FinderException;

import java.util.Collection;

import com.idega.block.school.data.SchoolCategory;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * @author laddi
 * 
 * A data bean to set the values needed to export accounting strings to external systems.
 */
public class ExportDataMappingBMPBean extends GenericEntity implements ExportDataMapping {

	private static final String ENTITY_NAME = "cacc_data_mapping";

	private static final String COLUMN_OPERATIONAL_FIELD = "operational_field";
	private static final String COLUMN_JOURNAL_NUMBER = "journal_number";
	private static final String COLUMN_ACCOUNT = "account";
	private static final String COLUMN_COUNTER_ACCOUNT = "counter_account";
	private static final String COLUMN_PAYABLE_ACCOUNT = "payable_account";
	private static final String COLUMN_CUSTOMER_CLAIM_ACCOUNT = "customer_claim_account";
	private static final String COLUMN_ACCOUNT_SETTLEMENT_TYPE = "account_settlement_type";
	private static final String COLUMN_CASH_FLOW_IN = "cash_flow_in";
	private static final String COLUMN_CASH_FLOW_OUT = "cash_flow_out";
	private static final String COLUMN_PROVIDER_AUTHORIZATION = "provider_authorization";

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getIDColumnName()
	 */
	public String getIDColumnName() {
		return COLUMN_OPERATIONAL_FIELD;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOEntityBean#getPrimaryKeyClass()
	 */
	public Class getPrimaryKeyClass() {
		return String.class;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addOneToOneRelationship(COLUMN_OPERATIONAL_FIELD, SchoolCategory.class);
		setAsPrimaryKey(COLUMN_OPERATIONAL_FIELD, true);
		
		addAttribute(COLUMN_JOURNAL_NUMBER, "Journal number", String.class, 255);
		addAttribute(COLUMN_ACCOUNT, "Account number", String.class, 255);
		addAttribute(COLUMN_COUNTER_ACCOUNT, "Counter account number", String.class, 255);
		addAttribute(COLUMN_PAYABLE_ACCOUNT, "Payable account number", String.class, 255);
		addAttribute(COLUMN_CUSTOMER_CLAIM_ACCOUNT, "Customer claim account number", String.class, 255);

		addAttribute(COLUMN_ACCOUNT_SETTLEMENT_TYPE, "Account settlement type", Integer.class);

		addAttribute(COLUMN_CASH_FLOW_IN, "Has cash flow in", Boolean.class);
		addAttribute(COLUMN_CASH_FLOW_OUT, "Has cash flow out", Boolean.class);
		addAttribute(COLUMN_PROVIDER_AUTHORIZATION, "Requires provider authorization", Boolean.class);
	}
	
	
	//Getters
	public SchoolCategory getOperationalField() {
		return (SchoolCategory) getColumnValue(COLUMN_OPERATIONAL_FIELD);
	}
	
	public String getJournalNumber() {
		return getStringColumnValue(COLUMN_JOURNAL_NUMBER);
	}

	public String getAccount() {
		return getStringColumnValue(COLUMN_ACCOUNT);
	}

	public String getCounterAccount() {
		return getStringColumnValue(COLUMN_COUNTER_ACCOUNT);
	}

	public String getPayableAccount() {
		return getStringColumnValue(COLUMN_PAYABLE_ACCOUNT);
	}

	public String getCustomerClaimAccount() {
		return getStringColumnValue(COLUMN_CUSTOMER_CLAIM_ACCOUNT);
	}

	public int getAccountSettlementType() {
		return getIntColumnValue(COLUMN_ACCOUNT_SETTLEMENT_TYPE);
	}

	public boolean getCashFlowIn() {
		return getBooleanColumnValue(COLUMN_CASH_FLOW_IN, false);
	}

	public boolean getCashFlowOut() {
		return getBooleanColumnValue(COLUMN_CASH_FLOW_OUT, false);
	}

	public boolean getProviderAuthorization() {
		return getBooleanColumnValue(COLUMN_PROVIDER_AUTHORIZATION, false);
	}
	
	
	//Setters
	public void setOperationalField(String operationalField) {
		setColumn(COLUMN_OPERATIONAL_FIELD, operationalField);
	}

	public void setOperationalField(SchoolCategory operationalField) {
		setColumn(COLUMN_OPERATIONAL_FIELD, operationalField);
	}

	public void setJournalNumber(String journalNumber) {
		setColumn(COLUMN_JOURNAL_NUMBER, journalNumber);
	}

	public void setAccount(String account) {
		setColumn(COLUMN_ACCOUNT, account);
	}

	public void setCounterAccount(String counterAccount) {
		setColumn(COLUMN_COUNTER_ACCOUNT, counterAccount);
	}

	public void setPayableAccount(String payableAccount) {
		setColumn(COLUMN_PAYABLE_ACCOUNT, payableAccount);
	}

	public void setCustomerClaimAccount(String customerClaimAccount) {
		setColumn(COLUMN_CUSTOMER_CLAIM_ACCOUNT, customerClaimAccount);
	}

	public void setAccountSettlementType(int accountSettlementType) {
		setColumn(COLUMN_ACCOUNT_SETTLEMENT_TYPE, accountSettlementType);
	}

	public void setCashFlowIn(boolean cashFlowIn) {
		setColumn(COLUMN_CASH_FLOW_IN, cashFlowIn);
	}

	public void setCashFlowOut(boolean cashFlowOut) {
		setColumn(COLUMN_CASH_FLOW_OUT, cashFlowOut);
	}

	public void setProviderAuthorization(boolean providerAuthorization) {
		setColumn(COLUMN_PROVIDER_AUTHORIZATION, providerAuthorization);
	}
	
	
	//Find methods
	public Collection ejbFindAll() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		
		return idoFindPKsByQuery(query);
	}
}