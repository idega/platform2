package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;

import com.idega.data.GenericEntity;

/**
 * @author Joakim
 *
 */
public class PaymentRecordBMPBean  extends GenericEntity implements PaymentRecord {
	private static final String ENTITY_NAME = "cacc_invoice_record";

	private static final String COLUMN_PAYMENT_HEADER = "payment_header";
	private static final String COLUMN_PAYMENT_TEXT = "pament_text";
	private static final String COLUMN_DATE_CREATED = "date_created";
	private static final String COLUMN_CREATED_BY = "created_by";
	private static final String COLUMN_DATE_CHANGED = "date_changed";
	private static final String COLUMN_CHANGED_BY = "changed_by";
	private static final String COLUMN_AMOUNT = "amount";
	private static final String COLUMN_AMOUNT_VAT = "amount_vat";
	private static final String COLUMN_NOTES = "notes";
	private static final String COLUMN_RULE_SPEC_TYPE = "rule_spec_type";
	private static final String COLUMN_OWN_POSTING = "own_posting";
	private static final String COLUMN_DOUBLE_POSTING = "double_posting";
	private static final String COLUMN_VAT_TYPE = "vat_type";
	
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
		addAttribute(COLUMN_PAYMENT_HEADER, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_PAYMENT_TEXT, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_DATE_CREATED, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_CREATED_BY, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_DATE_CHANGED, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_CHANGED_BY, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_AMOUNT, "", true, true, java.lang.Float.class);
		addAttribute(COLUMN_AMOUNT_VAT, "", true, true, java.lang.Float.class);
		addAttribute(COLUMN_NOTES, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_RULE_SPEC_TYPE, "", true, true, java.lang.String.class, 255);
		addAttribute(COLUMN_OWN_POSTING, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_DOUBLE_POSTING, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_VAT_TYPE, "", true, true, java.lang.Integer.class);

//TODO Create PaymentHeader		addManyToOneRelationship(COLUMN_PAYMENT_HEADER, PaymentHeader.class);
	}
	public int getPaymentHeader() {
		return getIntColumnValue(COLUMN_PAYMENT_HEADER);
	}
	public String getPaymentText() {
		return getStringColumnValue(COLUMN_PAYMENT_TEXT);
	}
	public Date getDateCreated() {
		return getDateColumnValue(COLUMN_DATE_CREATED);
	}
	public String getCreadedBy() {
		return getStringColumnValue(COLUMN_CREATED_BY);
	}
	public Date getDateChanged() {
		return getDateColumnValue(COLUMN_DATE_CHANGED);
	}
	public String getChangedBy() {
		return getStringColumnValue(COLUMN_CHANGED_BY);
	}
	public float getAmount() {
		return getFloatColumnValue(COLUMN_AMOUNT);
	}
	public float getAmountVAT() {
		return getFloatColumnValue(COLUMN_AMOUNT_VAT);
	}
	public String getNotes() {
		return getStringColumnValue(COLUMN_NOTES);
	}
	public String getRuleSpecType() {
		return getStringColumnValue(COLUMN_RULE_SPEC_TYPE);
	}
	public String getOwnPosting() {
		return getStringColumnValue(COLUMN_OWN_POSTING);
	}
	public String getDoublePosting() {
		return getStringColumnValue(COLUMN_DOUBLE_POSTING);
	}
	public int getVATType() {
		return getIntColumnValue(COLUMN_VAT_TYPE);
	}


	public void setPaymentHeader(int i) {
		setColumn(COLUMN_PAYMENT_HEADER, i);
	}
	public void setPaymentText(String s) {
		setColumn(COLUMN_PAYMENT_TEXT, s);
	}
	public void setDateCreated(Date d) {
		setColumn(COLUMN_DATE_CREATED, d);
	}
	public void setCreatedBy(String s) {
		setColumn(COLUMN_CREATED_BY, s);
	}
	public void setDateChanged(Date d) {
		setColumn(COLUMN_DATE_CHANGED, d);
	}
	public void setChangedBy(String s) {
		setColumn(COLUMN_CHANGED_BY, s);
	}
	public void setAmount(float f) {
		setColumn(COLUMN_AMOUNT, f);
	}
	public void setAmountVAT(float f) {
		setColumn(COLUMN_AMOUNT_VAT, f);
	}
	public void setNotes(String s) {
		setColumn(COLUMN_NOTES, s);
	}
	public void setRuleSpecType(String s) {
		setColumn(COLUMN_RULE_SPEC_TYPE, s);
	}
	public void setOwnPosting(String s) {
		setColumn(COLUMN_OWN_POSTING, s);
	}
	public void setDoublePosting(String s) {
		setColumn(COLUMN_DOUBLE_POSTING, s);
	}
	public void setVATType(int i) {
		setColumn(COLUMN_VAT_TYPE, i);
	}
}
