/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.data;



import com.idega.block.school.data.SchoolCategory;
import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface ExportDataMapping extends IDOEntity {
	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getPrimaryKeyClass
	 */
	public Class getPrimaryKeyClass();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getOperationalField
	 */
	public SchoolCategory getOperationalField();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getJournalNumber
	 */
	public String getJournalNumber();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getAccount
	 */
	public String getAccount();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getCounterAccount
	 */
	public String getCounterAccount();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getPayableAccount
	 */
	public String getPayableAccount();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getCustomerClaimAccount
	 */
	public String getCustomerClaimAccount();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getAccountSettlementType
	 */
	public int getAccountSettlementType();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getStandardPaymentDay
	 */
	public int getStandardPaymentDay();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getCashFlowIn
	 */
	public boolean getCashFlowIn();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getCashFlowOut
	 */
	public boolean getCashFlowOut();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getProviderAuthorization
	 */
	public boolean getProviderAuthorization();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getFileCreationFolder
	 */
	public String getFileCreationFolder();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getIFSFileFolder
	 */
	public String getIFSFileFolder();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getExportFileFolder
	 */
	public String getExportFileFolder();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getFileBackupFolder
	 */
	public String getFileBackupFolder();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getListCreationFolder
	 */
	public String getListCreationFolder();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getListBackupFolder
	 */
	public String getListBackupFolder();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getUseSpecifiedNumberOfDaysPrMonth
	 */
	public boolean getUseSpecifiedNumberOfDaysPrMonth();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getSpecifiedNumberOfDaysPrMonth
	 */
	public int getSpecifiedNumberOfDaysPrMonth();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getCreatePaymentsForCommuneProvidersOutsideCommune
	 */
	public boolean getCreatePaymentsForCommuneProvidersOutsideCommune();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getUseAgeInPosting
	 */
	public boolean getUseAgeInPosting();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getUseCareTimeInPosting
	 */
	public boolean getUseCareTimeInPosting();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getInvoiceLimit
	 */
	public float getInvoiceLimit();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#getSkipStudentsOutsideCommune
	 */
	public boolean getSkipStudentsOutsideCommune();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setOperationalField
	 */
	public void setOperationalField(String operationalField);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setOperationalField
	 */
	public void setOperationalField(SchoolCategory operationalField);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setJournalNumber
	 */
	public void setJournalNumber(String journalNumber);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setAccount
	 */
	public void setAccount(String account);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setCounterAccount
	 */
	public void setCounterAccount(String counterAccount);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setPayableAccount
	 */
	public void setPayableAccount(String payableAccount);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setCustomerClaimAccount
	 */
	public void setCustomerClaimAccount(String customerClaimAccount);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setAccountSettlementType
	 */
	public void setAccountSettlementType(int accountSettlementType);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setStandardPaymentDay
	 */
	public void setStandardPaymentDay(int standardPaymentDay);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setCashFlowIn
	 */
	public void setCashFlowIn(boolean cashFlowIn);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setCashFlowOut
	 */
	public void setCashFlowOut(boolean cashFlowOut);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setProviderAuthorization
	 */
	public void setProviderAuthorization(boolean providerAuthorization);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setFileCreationFolder
	 */
	public void setFileCreationFolder(String folder);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setIFSFileFolder
	 */
	public void setIFSFileFolder(String folder);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setExportFileFolder
	 */
	public void setExportFileFolder(String folder);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setFileBackupFolder
	 */
	public void setFileBackupFolder(String folder);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setListCreationFolder
	 */
	public void setListCreationFolder(String folder);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setListBackupFolder
	 */
	public void setListBackupFolder(String folder);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setUseSpecifiedNumberOfDaysPrMonth
	 */
	public void setUseSpecifiedNumberOfDaysPrMonth(boolean useSpecifiedDays);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setSpecifiedNumberOfDaysPrMonth
	 */
	public void setSpecifiedNumberOfDaysPrMonth(int specifiedDays);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setCreatePaymentsForCommuneProvidersOutsideCommune
	 */
	public void setCreatePaymentsForCommuneProvidersOutsideCommune(
			boolean createPayments);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setUseAgeInPosting
	 */
	public void setUseAgeInPosting(boolean useAge);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setUseCareTimeInPosting
	 */
	public void setUseCareTimeInPosting(boolean useCareTime);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setInvoiceLimit
	 */
	public void setInvoiceLimit(float limit);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingBMPBean#setSkipStudentsOutsideCommune
	 */
	public void setSkipStudentsOutsideCommune(boolean skip);

}
