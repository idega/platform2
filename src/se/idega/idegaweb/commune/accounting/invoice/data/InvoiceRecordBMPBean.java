package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLegacyEntity;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractArchive;

/**
 * @author Joakim
 * This is the data bean for the "faktureringsrad", "fakturarad" och "detaljutbetalningspost" 
 * (They are all the same thign) in the Kravspecifikation Check and Peng. 
 */
public class InvoiceRecordBMPBean extends GenericEntity implements InvoiceRecord, IDOLegacyEntity {
	private static final String ENTITY_NAME = "cacc_invoice_record";

	private static final String COLUMN_INVOICE_HEADER = "invoice_header";
	private static final String COLUMN_PAYMENT_RECORD_ID = "payment_record_id";
	private static final String COLUMN_PROVIDER_ID = "provider_id";
	private static final String COLUMN_CONTRACT_ID = "contract_id";
	private static final String COLUMN_INVOICE_TEXT = "invoice_text";
	private static final String COLUMN_RULE_TEXT = "rule_text";
	private static final String COLUMN_DAYS = "days";
	private static final String COLUMN_PERIOD_START_CHECK = "period_start_check";
	private static final String COLUMN_PERIOD_END_CHECK = "period_end_check";
	private static final String COLUMN_PERIOD_START_PLACEMENT = "period_start_placement";
	private static final String COLUMN_PERIOD_END_PLACEMENT = "period_end_placement";
	private static final String COLUMN_DATE_CREATED = "date_created";
	private static final String COLUMN_CREATED_BY = "created_by";
	private static final String COLUMN_DATE_CHANGED = "date_changed";
	private static final String COLUMN_CHANGED_BY = "changed_by";
	private static final String COLUMN_AMOUNT = "amount";
	private static final String COLUMN_NOTES = "notes";
	private static final String COLUMN_ORDER_ID = "order_id";
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
		addAttribute(COLUMN_INVOICE_HEADER, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_PAYMENT_RECORD_ID, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_PROVIDER_ID, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_CONTRACT_ID, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_INVOICE_TEXT, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_RULE_TEXT, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_DAYS, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_PERIOD_START_CHECK, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_PERIOD_END_CHECK, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_PERIOD_START_PLACEMENT, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_PERIOD_END_PLACEMENT, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_DATE_CREATED, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_CREATED_BY, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_DATE_CHANGED, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_CHANGED_BY, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_AMOUNT, "", true, true, java.lang.Float.class);
		addAttribute(COLUMN_NOTES, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_ORDER_ID, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_RULE_SPEC_TYPE, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_OWN_POSTING, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_DOUBLE_POSTING, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_VAT_TYPE, "", true, true, java.lang.Integer.class);

		addManyToOneRelationship(COLUMN_INVOICE_HEADER, InvoiceHeader.class);
		addManyToOneRelationship(COLUMN_CONTRACT_ID, ChildCareContractArchive.class);
	}
	public int getInvoiceheader() {
		return getIntColumnValue(COLUMN_INVOICE_HEADER);
	}
	public int getPaymentRecordId() {
		return getIntColumnValue(COLUMN_PAYMENT_RECORD_ID);
	}
	public int getProviderId() {
		return getIntColumnValue(COLUMN_PROVIDER_ID);
	}
	public int getContractId() {
		return getIntColumnValue(COLUMN_CONTRACT_ID);
	}
	public String getInvoiceText() {
		return getStringColumnValue(COLUMN_INVOICE_TEXT);
	}
	public String getRuleText() {
		return getStringColumnValue(COLUMN_RULE_TEXT);
	}
	public int getDays() {
		return getIntColumnValue(COLUMN_DAYS);
	}
	public Date getPeriodStartCheck() {
		return getDateColumnValue(COLUMN_PERIOD_START_CHECK);
	}
	public Date getPeriodEndCheck() {
		return getDateColumnValue(COLUMN_PERIOD_END_CHECK);
	}
	public Date getPeriodStartPlacement() {
		return getDateColumnValue(COLUMN_PERIOD_START_PLACEMENT);
	}
	public Date getPeriodEndPlacement() {
		return getDateColumnValue(COLUMN_PERIOD_END_PLACEMENT);
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
	public String getNotes() {
		return getStringColumnValue(COLUMN_NOTES);
	}
	public int getOrderId() {
		return getIntColumnValue(COLUMN_ORDER_ID);
	}
	public int getRuleSpecType() {
		return getIntColumnValue(COLUMN_RULE_SPEC_TYPE);
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


	public void setInvoiceHeader(int i) {
		setColumn(COLUMN_INVOICE_HEADER, i);
	}
	public void setInvoiceHeader(InvoiceHeader i){
		setColumn(COLUMN_INVOICE_HEADER, i);
	}
	public void setPaymentRecordId(int i) {
		setColumn(COLUMN_PAYMENT_RECORD_ID, i);
	}
	public void setColumnProviderId(int i) {
		setColumn(COLUMN_PROVIDER_ID, i);
	}
	public void setContractId(int i) {
		setColumn(COLUMN_CONTRACT_ID, i);
	}
	public void setInvoiceText(String s) {
		setColumn(COLUMN_INVOICE_TEXT, s);
	}
	public void setRuleText(String s) {
		setColumn(COLUMN_RULE_TEXT, s);
	}
	public void setDays(int i) {
		setColumn(COLUMN_DAYS, i);
	}
	public void setPeriodStartCheck(Date d) {
		setColumn(COLUMN_PERIOD_START_CHECK, d);
	}
	public void setPeriodEndCheck(Date d) {
		setColumn(COLUMN_PERIOD_END_CHECK, d);
	}
	public void setPeriodStartPlacement(Date d) {
		setColumn(COLUMN_PERIOD_START_PLACEMENT, d);
	}
	public void setPeriodEndPlacement(Date d) {
		setColumn(COLUMN_PERIOD_END_PLACEMENT, d);
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
	public void setNotes(String s) {
		setColumn(COLUMN_NOTES, s);
	}
	public void setOrderId(int i) {
		setColumn(COLUMN_ORDER_ID, i);
	}
	public void setRuleSpecType(int i) {
		setColumn(COLUMN_RULE_SPEC_TYPE, i);
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
