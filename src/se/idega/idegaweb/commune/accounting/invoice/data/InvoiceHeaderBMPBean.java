package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolCategory;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.*;

/**
 * The databean for the "Fakturahuvud" in the C&P Req spec. The invoice header holds all the 
 * information that is the same for all the invoice records that it is related to.
 * 
 * @author Joakim
 */
public class InvoiceHeaderBMPBean extends GenericEntity implements InvoiceHeader {
	private static final String ENTITY_NAME = "cacc_invoice_header";

	private static final String COLUMN_SCHOOL_CATEGORY_ID = "main_school_category_id";
	private static final String COLUMN_PERIOD = "period";
	private static final String COLUMN_CUSTODIAN_ID = "custodian_id";	//Invoice receiver
	private static final String COLUMN_STATUS = "status";
	private static final String COLUMN_DATE_CREATED = "date_created";
	private static final String COLUMN_DATE_ADJUSTED = "date_adjusted";
	private static final String COLUMN_DATE_TRANSACTION = "date_transaction";
	private static final String COLUMN_CREATED_BY = "created_by";
	private static final String COLUMN_CHANGED_BY = "changed_by";
	private static final String COLUMN_OWN_POSTING = "own_postiong";
	private static final String COLUMN_DOUBLE_POSTING = "double_posting";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_SCHOOL_CATEGORY_ID, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_PERIOD, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_CUSTODIAN_ID, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_STATUS, "", true, true, java.lang.String.class, 1);
		addAttribute(COLUMN_DATE_CREATED, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_DATE_ADJUSTED, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_DATE_TRANSACTION, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_CREATED_BY, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_CHANGED_BY, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_OWN_POSTING, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_DOUBLE_POSTING, "", true, true, java.lang.String.class, 1000);

		addManyToOneRelationship(COLUMN_SCHOOL_CATEGORY_ID, SchoolCategory.class);
		addManyToOneRelationship(COLUMN_CUSTODIAN_ID, User.class);
	}
	public int getSchoolCategoryID() {
		return getIntColumnValue(COLUMN_SCHOOL_CATEGORY_ID);
	}
	public Date getPeriod() {
		return getDateColumnValue(COLUMN_PERIOD);
	}
	public int getCustodianId() {
		return getIntColumnValue(COLUMN_CUSTODIAN_ID);
	}
	public char getStatus() {
		return getCharColumnValue(COLUMN_STATUS);
	}
	public Date getDateCreated() {
		return getDateColumnValue(COLUMN_DATE_CREATED);
	}
	public Date getDateAdjusted() {
		return getDateColumnValue(COLUMN_DATE_ADJUSTED);
	}
	public Date getDateJournalEntry() {
		return getDateColumnValue(COLUMN_DATE_TRANSACTION);
	}
	public String getCreatedBy() {
		return getStringColumnValue(COLUMN_CREATED_BY);
	}
	public String getChangedBy() {
		return getStringColumnValue(COLUMN_CHANGED_BY);
	}
	public String getOwnPosting() {
		return getStringColumnValue(COLUMN_OWN_POSTING);
	}
	public String getDoublePosting() {
		return getStringColumnValue(COLUMN_DOUBLE_POSTING);
	}


	public void setSchoolCagtegoryID(int i) {
		setColumn(COLUMN_SCHOOL_CATEGORY_ID, i);
	}
	public void setSchoolCagtegoryID(SchoolCategory sc) {
		setColumn(COLUMN_SCHOOL_CATEGORY_ID, sc);
	}
	public void setPeriod(Date d) {
		setColumn(COLUMN_PERIOD, d);
	}
	public void setCustodianId(int i) {
		setColumn(COLUMN_CUSTODIAN_ID, i);
	}
	public void setCustodianId(User u) {
		setColumn(COLUMN_CUSTODIAN_ID, u);
	}
	public void setStatus(char c) {
		setColumn(COLUMN_STATUS, c);
	}
	public void setDateCreated(Date d) {
		setColumn(COLUMN_DATE_CREATED, d);
	}
	public void setDateAdjusted(Date d) {
		setColumn(COLUMN_DATE_ADJUSTED, d);
	}
	public void setDateTransactionEntry(Date d) {
		setColumn(COLUMN_DATE_TRANSACTION, d);
	}
	public void setCreatedBy(String s) {
		setColumn(COLUMN_CREATED_BY, s);
	}
	public void setChangedBy(String s) {
		setColumn(COLUMN_CHANGED_BY, s);
	}
	public void setOwnPosting(String s) {
		setColumn(COLUMN_OWN_POSTING, s);
	}
	public void setDoublePosting(String s) {
		setColumn(COLUMN_DOUBLE_POSTING, s);
	}
	
	public Integer ejbFindByCustodian(User custodian) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_CUSTODIAN_ID, custodian.getPrimaryKey());
		return (Integer)idoFindOnePKByQuery(sql);
	}
}

