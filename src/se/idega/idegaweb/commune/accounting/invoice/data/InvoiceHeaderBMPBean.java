package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;

import com.idega.block.school.data.School;
import com.idega.data.GenericEntity;
import com.idega.data.IDOLegacyEntity;
import com.idega.user.data.*;
/**
 * @author Joakim
 *
 */
public class InvoiceHeaderBMPBean extends GenericEntity implements 
//InvoiceHeader, 
IDOLegacyEntity {
	private static final String ENTITY_NAME = "cacc_invoice_header";

	private static final String COLUMN_MAIN_ACTIVITY = "main_activity";	
	//TODO (JJ) This should probably be a reference to huvudverksamhet...
	private static final String COLUMN_PERIOD = "period";
	private static final String COLUMN_CUSTODIAN_ID = "custodian_id";		//Invoice receiver
	private static final String COLUMN_REFERENCE = "reference";
	//TODO (JJ) ref to Bun person???
	private static final String COLUMN_STATUS = "status";
	private static final String COLUMN_DATE_CREATED = "date_created";
	private static final String COLUMN_DATE_ADJUSTED = "date_adjusted";
	private static final String COLUMN_DATE_TRANSACTION = "date_transaction";
	private static final String COLUMN_CREATED_BY = "created_by";
	private static final String COLUMN_CHANGED_BY = "changed_by";
	private static final String COLUMN_OWN_POSTING = "own_postiong";
	private static final String COLUMN_DOUBLE_POSTING = "double_posting";
	private static final String COLUMN_TOTAL_AMOUNT_WITHOUT_VAT = "total_amount_without_vat";
	private static final String COLUMN_TOTAL_VAT_AMOUNT = "total_vat_amount";
	/**
	 * @see com.idega.data.IDOLegacyEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}
	/**
	 * @see com.idega.data.IDOLegacyEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_MAIN_ACTIVITY, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_PERIOD, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_CUSTODIAN_ID, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_REFERENCE, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_STATUS, "", true, true, java.lang.String.class, 1);
		addAttribute(COLUMN_DATE_CREATED, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_DATE_ADJUSTED, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_DATE_TRANSACTION, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_CREATED_BY, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_CHANGED_BY, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_OWN_POSTING, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_DOUBLE_POSTING, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_TOTAL_AMOUNT_WITHOUT_VAT, "", true, true, java.lang.Float.class);
		addAttribute(COLUMN_TOTAL_VAT_AMOUNT, "", true, true, java.lang.Float.class);

		addManyToOneRelationship(COLUMN_CUSTODIAN_ID, User.class);
//		addManyToOneRelationship(COLUMN_REFERENCE, .class);
//TODO (JJ) find the clas for the BUN reference
//TODO (JJ) need to get the reference to main activity/huvudverksamhet
	}
	public int getMainActivity() {
		return getIntColumnValue(COLUMN_MAIN_ACTIVITY);
	}
	public Date getPeriod() {
		return getDateColumnValue(COLUMN_PERIOD);
	}
	public int getCustodianId() {
		return getIntColumnValue(COLUMN_CUSTODIAN_ID);
	}
	public int getReference() {
		return getIntColumnValue(COLUMN_REFERENCE);
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
	public float getTotalAmountWithoutVAT() {
		return getFloatColumnValue(COLUMN_TOTAL_AMOUNT_WITHOUT_VAT);
	}
	public float getTotalVATAmount() {
		return getFloatColumnValue(COLUMN_TOTAL_VAT_AMOUNT);
	}


	public void setMainActivity(int i) {
		setColumn(COLUMN_MAIN_ACTIVITY, i);
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
	public void setReference(int i) {
		setColumn(COLUMN_REFERENCE, i);
	}
	public void setReference(School s) {
		setColumn(COLUMN_REFERENCE, s);
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
	public void setTotalAmountWithoutVAT(float f) {
		setColumn(COLUMN_TOTAL_AMOUNT_WITHOUT_VAT, f);
	}
	public void setTotalVATAmount(float f) {
		setColumn(COLUMN_TOTAL_VAT_AMOUNT, f);
	}
}

