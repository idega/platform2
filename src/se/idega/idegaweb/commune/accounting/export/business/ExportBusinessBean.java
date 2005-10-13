/*
 * Created on 8.9.2003
 */
package se.idega.idegaweb.commune.accounting.export.business;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.export.data.ExportDataMappingHome;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORuntimeException;

/**
 * @author laddi
 */
public class ExportBusinessBean extends IBOServiceBean implements ExportBusiness {
	
	private static final int ACCOUNT_SETTLEMENT_TYPE_DAY_BY_DAY = 1;
	private static final int ACCOUNT_SETTLEMENT_TYPE_SPECIFIC_DATE = 2;

	/**
	 * Returns the day by day settlement accounting type.
	 * @return int
	 */
	public int getAccountSettlementTypeDayByDay() {
		return ACCOUNT_SETTLEMENT_TYPE_DAY_BY_DAY;
	}

	/**
	 * Returns the specific date settlement accounting type.
	 * @return int
	 */
	public int getAccountSettlementTypeSpecificDate() {
		return ACCOUNT_SETTLEMENT_TYPE_SPECIFIC_DATE;
	}

	protected ExportDataMappingHome getExportDataMappingHome() {
		try {
			return (ExportDataMappingHome) IDOLookup.getHome(ExportDataMapping.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e.getMessage());
		}
	}
	
	public ExportDataMapping getExportDataMapping(String operationalField) throws FinderException {
		return getExportDataMappingHome().findByPrimaryKey(operationalField);
	}
	
	/**
	 * Stores the export data for a specific operational field, creating a new entry if none exists.
	 */
	public void storeExportDataMapping(String operationalField, String journalNumber, String account, String counterAccount, String payableAccount, String customerClaimAccount, String fileCreationFolder, String IFSFileFolder, String fileBackupFolder, String listCreationFolder, String listBackupFolder, int accountSettlementType, int standardPaymentDay, boolean cashFlowIn, boolean cashFlowOut, boolean providerAuthorization, boolean createOutSideCommune, boolean useSpecificDays, int specificDays) {
		ExportDataMapping mapping = null;
		try {
			mapping = getExportDataMapping(operationalField);
		}
		catch (FinderException fe) {
			try {
				mapping = getExportDataMappingHome().create();
			}
			catch (CreateException ce) {
				ce.printStackTrace();
				mapping = null;
			}
		}
		
		if (mapping != null) {
			mapping.setOperationalField(operationalField);
			mapping.setJournalNumber(journalNumber);
			mapping.setAccount(account);
			mapping.setCounterAccount(counterAccount);
			mapping.setPayableAccount(payableAccount);
			mapping.setCustomerClaimAccount(customerClaimAccount);
			mapping.setFileCreationFolder(fileCreationFolder);
			mapping.setIFSFileFolder(IFSFileFolder);
			mapping.setFileBackupFolder(fileBackupFolder);
			mapping.setListCreationFolder(listCreationFolder);
			mapping.setListBackupFolder(listBackupFolder);
			mapping.setAccountSettlementType(accountSettlementType);
			mapping.setStandardPaymentDay(standardPaymentDay);
			mapping.setCashFlowIn(cashFlowIn);
			mapping.setCashFlowOut(cashFlowOut);
			mapping.setProviderAuthorization(providerAuthorization);
			
			mapping.setCreatePaymentsForCommuneProvidersOutsideCommune(createOutSideCommune);
			mapping.setUseSpecifiedNumberOfDaysPrMonth(useSpecificDays);
			mapping.setSpecifiedNumberOfDaysPrMonth(specificDays);
			mapping.store();
		}
	}
	
	/**
	 * Returns all <code>SchoolCategory</code> data beans from the database.
	 */
	public Collection getAllOperationalFields() {
		try {
			SchoolBusiness business = (SchoolBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolBusiness.class);
			return business.getSchoolCategories();
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}
}