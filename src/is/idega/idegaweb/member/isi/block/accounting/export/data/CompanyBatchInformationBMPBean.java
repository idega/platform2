package is.idega.idegaweb.member.isi.block.accounting.export.data;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

public class CompanyBatchInformationBMPBean extends GenericEntity implements
		CompanyBatchInformation {

	protected final static String ENTITY_NAME = "isi_company_batch_info";

	protected final static String COLUMN_COMPANY_NUMBER = "company_number";

	protected final static String COLUMN_BATCH_NUMBER = "batch_number";

	protected final static String COLUMN_BATCH_MONTH = "batch_month";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(COLUMN_COMPANY_NUMBER, "Company number", String.class, 10);
		setAsPrimaryKey(COLUMN_COMPANY_NUMBER, true);
		addAttribute(COLUMN_BATCH_NUMBER, "Batch number", Integer.class);
		addAttribute(COLUMN_BATCH_MONTH, "Batch month", String.class, 2);
	}

	public Class getPrimaryKeyClass() {
		return String.class;
	}

	public String getIDColumnName() {
		return COLUMN_COMPANY_NUMBER;
	}

	public void setCompanyNumber(String companyNumber) {
		setColumn(COLUMN_COMPANY_NUMBER, companyNumber);
	}

	public void setBatchNumber(int batchNumber) {
		setColumn(COLUMN_BATCH_NUMBER, batchNumber);
	}

	public void setBatchMonth(String batchMonth) {
		setColumn(COLUMN_BATCH_MONTH, batchMonth);
	}

	public String getCompanyNumber() {
		return getStringColumnValue(COLUMN_COMPANY_NUMBER);
	}

	public int getBatchNumber() {
		return getIntColumnValue(COLUMN_BATCH_NUMBER, 0);
	}

	public String getBatchMonth() {
		return getStringColumnValue(COLUMN_BATCH_MONTH);
	}

	public Object ejbFindByCompanyNumber(String companyNumber)
			throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_COMPANY_NUMBER, companyNumber);

		return idoFindOnePKByQuery(sql);
	}
}