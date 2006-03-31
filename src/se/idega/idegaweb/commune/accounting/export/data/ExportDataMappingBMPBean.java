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
 * A data bean to set the values needed to export accounting strings to external
 * systems.
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

	private static final String COLUMN_STANDARD_PAYMENT_DAY = "standard_payment_day";

	private static final String COLUMN_FILE_CREATION_FOLDER = "file_folder";

	private static final String COLUMN_IFS_FILE_FOLDER = "ifs_file_folder";

	private static final String COLUMN_EXPORT_FILE_FOLDER = "export_file_folder";
	
	private static final String COLUMN_FILE_BACKUP_FOLDER = "file_backup_folder";

	private static final String COLUMN_LIST_CREATION_FOLDER = "list_folder";

	private static final String COLUMN_LIST_BACKUP_FOLDER = "list_backup_folder";

	private static final String COLUMN_USE_SPECIFIED_DAYS_PR_MONTH = "use_specified_days_month";

	private static final String COLUMN_DAYS_PR_MONTH = "specified_day";

	private static final String COLUMN_PAYMENTS_FOR_COMMUNES_OUTSIDE = "payments_for_comm_outside";
	
	private static final String COLUMN_USE_AGE_IN_POSTING = "use_age_in_posting";
	
	private static final String COLUMN_USE_CARE_TIME_IN_POSTING = "use_care_time_in_posting";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.GenericEntity#getIDColumnName()
	 */
	public String getIDColumnName() {
		return COLUMN_OPERATIONAL_FIELD;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.IDOEntityBean#getPrimaryKeyClass()
	 */
	public Class getPrimaryKeyClass() {
		return String.class;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		addAttribute(COLUMN_STANDARD_PAYMENT_DAY, "Standard payment day", Integer.class);
		addAttribute(COLUMN_CASH_FLOW_IN, "Has cash flow in", Boolean.class);
		addAttribute(COLUMN_CASH_FLOW_OUT, "Has cash flow out", Boolean.class);
		addAttribute(COLUMN_PROVIDER_AUTHORIZATION, "Requires provider authorization", Boolean.class);
		addAttribute(COLUMN_FILE_CREATION_FOLDER, "Folder to create files for this type in", String.class, 255);
		addAttribute(COLUMN_IFS_FILE_FOLDER, "Folder where IFS reads files for this type", String.class, 255);
		addAttribute(COLUMN_EXPORT_FILE_FOLDER, "Folder where external system reads files", String.class, 255);
		addAttribute(COLUMN_LIST_CREATION_FOLDER, "Folder to create lists for this type in", String.class, 255);
		addAttribute(COLUMN_USE_SPECIFIED_DAYS_PR_MONTH, "Whether or not to use a specified nr. of days pr month in calculations", Boolean.class);
		addAttribute(COLUMN_DAYS_PR_MONTH, "The number of specified days pr. month", Integer.class);
		addAttribute(COLUMN_PAYMENTS_FOR_COMMUNES_OUTSIDE, "Pay commune providers outside of commune", Boolean.class);
		addAttribute(COLUMN_USE_AGE_IN_POSTING, "Use age when getting posting parameters", Boolean.class);
		addAttribute(COLUMN_USE_CARE_TIME_IN_POSTING, "Use care time when getting posting parameters", Boolean.class);
	}

	// Getters
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

	public int getStandardPaymentDay() {
		return getIntColumnValue(COLUMN_STANDARD_PAYMENT_DAY);
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

	public String getFileCreationFolder() {
		return getStringColumnValue(COLUMN_FILE_CREATION_FOLDER);
	}

	public String getIFSFileFolder() {
		return getStringColumnValue(COLUMN_IFS_FILE_FOLDER);
	}

	public String getExportFileFolder() {
		return getStringColumnValue(COLUMN_EXPORT_FILE_FOLDER);
	}

	public String getFileBackupFolder() {
		return getStringColumnValue(COLUMN_FILE_BACKUP_FOLDER);
	}

	public String getListCreationFolder() {
		return getStringColumnValue(COLUMN_LIST_CREATION_FOLDER);
	}

	public String getListBackupFolder() {
		return getStringColumnValue(COLUMN_LIST_BACKUP_FOLDER);
	}
	
	public boolean getUseSpecifiedNumberOfDaysPrMonth() {
		return getBooleanColumnValue(COLUMN_USE_SPECIFIED_DAYS_PR_MONTH, false);
	}
	
	public int getSpecifiedNumberOfDaysPrMonth() {
		return getIntColumnValue(COLUMN_DAYS_PR_MONTH, 0);
	}
	
	public boolean getCreatePaymentsForCommuneProvidersOutsideCommune() {
		return getBooleanColumnValue(COLUMN_PAYMENTS_FOR_COMMUNES_OUTSIDE, false);
	}
	
	public boolean getUseAgeInPosting() {
		return getBooleanColumnValue(COLUMN_USE_AGE_IN_POSTING, false);
	}
	
	public boolean getUseCareTimeInPosting() {
		return getBooleanColumnValue(COLUMN_USE_CARE_TIME_IN_POSTING, false);
	}

	// Setters
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

	public void setStandardPaymentDay(int standardPaymentDay) {
		setColumn(COLUMN_STANDARD_PAYMENT_DAY, standardPaymentDay);
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

	public void setFileCreationFolder(String folder) {
		setColumn(COLUMN_FILE_CREATION_FOLDER, folder);
	}

	public void setIFSFileFolder(String folder) {
		setColumn(COLUMN_IFS_FILE_FOLDER, folder);
	}

	public void setExportFileFolder(String folder) {
		setColumn(COLUMN_EXPORT_FILE_FOLDER, folder);
	}

	public void setFileBackupFolder(String folder) {
		setColumn(COLUMN_FILE_BACKUP_FOLDER, folder);
	}

	public void setListCreationFolder(String folder) {
		setColumn(COLUMN_LIST_CREATION_FOLDER, folder);
	}

	public void setListBackupFolder(String folder) {
		setColumn(COLUMN_LIST_BACKUP_FOLDER, folder);
	}

	public void setUseSpecifiedNumberOfDaysPrMonth(boolean useSpecifiedDays) {
		setColumn(COLUMN_USE_SPECIFIED_DAYS_PR_MONTH, useSpecifiedDays);
	}
	
	public void setSpecifiedNumberOfDaysPrMonth(int specifiedDays) {
		setColumn(COLUMN_DAYS_PR_MONTH, specifiedDays);
	}
	
	public void setCreatePaymentsForCommuneProvidersOutsideCommune(boolean createPayments) {
		setColumn(COLUMN_PAYMENTS_FOR_COMMUNES_OUTSIDE, createPayments);
	}
	
	public void setUseAgeInPosting(boolean useAge) {
		setColumn(COLUMN_USE_AGE_IN_POSTING, useAge);
	}
	
	public void setUseCareTimeInPosting(boolean useCareTime) {
		setColumn(COLUMN_USE_CARE_TIME_IN_POSTING, useCareTime);
	}

	// Find methods
	public Collection ejbFindAll() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);

		return idoFindPKsByQuery(query);
	}
}