package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import com.idega.block.school.data.SchoolClassMember;

import com.idega.block.school.data.School;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.util.IWTimestamp;

/**
 * This is the data bean for the "faktureringsrad", "fakturarad" och "detaljutbetalningspost" 
 * (They are all the same dataobject) in the Kravspecifikation Check and Peng. 
 * 
 * @author Joakim
 */
public class InvoiceRecordBMPBean extends GenericEntity implements InvoiceRecord {
	public static final String ENTITY_NAME = "cacc_invoice_record";

	public static final String COLUMN_INVOICE_HEADER = "invoice_header";
	public static final String COLUMN_PAYMENT_RECORD_ID = "payment_record_id";
	public static final String COLUMN_PROVIDER_ID = "provider_id";
	public static final String COLUMN_SCHOOL_CLASS_MEMBER_ID = "sch_class_member_id";
	public static final String COLUMN_INVOICE_TEXT = "invoice_text";
	public static final String COLUMN_RULE_TEXT = "rule_text";
	public static final String COLUMN_DAYS = "days";
	public static final String COLUMN_PERIOD_START_CHECK = "period_start_check";
	public static final String COLUMN_PERIOD_END_CHECK = "period_end_check";
	public static final String COLUMN_PERIOD_START_PLACEMENT = "period_start_placement";
	public static final String COLUMN_PERIOD_END_PLACEMENT = "period_end_placement";
	public static final String COLUMN_DATE_CREATED = "date_created";
	public static final String COLUMN_CREATED_BY = "created_by";
	public static final String COLUMN_DATE_CHANGED = "date_changed";
	public static final String COLUMN_CHANGED_BY = "changed_by";
	public static final String COLUMN_AMOUNT = "amount";
	public static final String COLUMN_AMOUNT_VAT = "amount_vat";
	public static final String COLUMN_NOTES = "notes";
	public static final String COLUMN_ORDER_ID = "order_id";
	public static final String COLUMN_REG_SPEC_TYPE_ID = "reg_spec_type_id";
	public static final String COLUMN_OWN_POSTING = "own_posting";
	public static final String COLUMN_DOUBLE_POSTING = "double_posting";
	public static final String COLUMN_VAT_TYPE = "vat_type";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
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
		addAttribute(COLUMN_AMOUNT_VAT, "", true, true, java.lang.Float.class);
		addAttribute(COLUMN_NOTES, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_ORDER_ID, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_OWN_POSTING, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_DOUBLE_POSTING, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_VAT_TYPE, "", true, true, java.lang.Integer.class);

		addManyToOneRelationship(COLUMN_INVOICE_HEADER, InvoiceHeader.class);
		addManyToOneRelationship(COLUMN_PAYMENT_RECORD_ID, PaymentRecord.class);
		addManyToOneRelationship(COLUMN_SCHOOL_CLASS_MEMBER_ID, SchoolClassMember.class);
		addManyToOneRelationship(COLUMN_PROVIDER_ID, School.class);
		addManyToOneRelationship(COLUMN_REG_SPEC_TYPE_ID, RegulationSpecType.class);
		
		setNullable(COLUMN_INVOICE_HEADER, true);
	}
	public int getInvoiceHeader() {
		return getIntColumnValue(COLUMN_INVOICE_HEADER);
	}
	public int getPaymentRecordId() {
		return getIntColumnValue(COLUMN_PAYMENT_RECORD_ID);
	}
	public int getProviderId() {
		return getIntColumnValue(COLUMN_PROVIDER_ID);
	}
	public int getSchoolClassMemberId() {
		return getIntColumnValue(COLUMN_SCHOOL_CLASS_MEMBER_ID);
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
	public String getCreatedBy() {
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
	public int getOrderId() {
		return getIntColumnValue(COLUMN_ORDER_ID);
	}
	public int getRegSpecTypeId() {
		return getIntColumnValue(COLUMN_REG_SPEC_TYPE_ID);
	}
	public RegulationSpecType getRegSpecType() {
		return (RegulationSpecType) getColumnValue(COLUMN_REG_SPEC_TYPE_ID);
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
	public void setPaymentRecordId(PaymentRecord p) {
		setColumn(COLUMN_PAYMENT_RECORD_ID, p);
	}
	public void setProviderId(int i) {
		setColumn(COLUMN_PROVIDER_ID, i);
	}
	public void setProviderId(School s) {
		setColumn(COLUMN_PROVIDER_ID, s);
	}
	public void setSchoolClassMemberId(int i) {
		setColumn(COLUMN_SCHOOL_CLASS_MEMBER_ID, i);
	}
	public void setSchoolClassMemberId(SchoolClassMember scm) {
		setColumn(COLUMN_SCHOOL_CLASS_MEMBER_ID, scm);
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
	public void setAmountVAT(float f) {
		setColumn(COLUMN_AMOUNT_VAT, f);
	}
	public void setNotes(String s) {
		setColumn(COLUMN_NOTES, s);
	}
	public void setOrderId(int i) {
		setColumn(COLUMN_ORDER_ID, i);
	}
	public void setRegSpecTypeId(int i) {
		setColumn(COLUMN_REG_SPEC_TYPE_ID, i);
	}
	public void setRegSpecType(RegulationSpecType r) {
		setColumn(COLUMN_REG_SPEC_TYPE_ID, r);
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
	
	public Collection ejbFindByMonth(Date month) throws FinderException {
		IWTimestamp start = new IWTimestamp(month);
		start.setAsDate();
		start.setDay(1);
		IWTimestamp end = new IWTimestamp(start);
		end.addMonths(1);
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere(COLUMN_DATE_CREATED).appendGreaterThanOrEqualsSign().append(start.getDate());
		sql.appendAnd().append(COLUMN_DATE_CREATED).appendLessThanSign().append(end.getDate());
		return idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindByInvoiceHeader(InvoiceHeader invoiceHeader) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_INVOICE_HEADER,invoiceHeader.getPrimaryKey());
		return idoFindPKsByQuery(sql);
	}

	public Collection ejbFindByPaymentRecord (PaymentRecord paymentRecord)
        throws FinderException {
		final IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom (this)
                .appendWhereEquals (COLUMN_PAYMENT_RECORD_ID,
                                    paymentRecord.getPrimaryKey ());
		return idoFindPKsByQuery (sql);
	}

}
