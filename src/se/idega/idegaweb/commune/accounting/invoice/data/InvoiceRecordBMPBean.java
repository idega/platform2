package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.care.data.ChildCareContract;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberBMPBean;
import com.idega.block.school.data.SchoolType;
import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;
import com.idega.user.data.UserBMPBean;
import com.idega.util.CalendarMonth;
import com.idega.util.IWTimestamp;

/**
 * This is the data bean for the "faktureringsrad", "fakturarad" och "detaljutbetalningspost" 
 * (They are all the same dataobject) in the Kravspecifikation Check and Peng. 
 * 
 * @author Joakim
 */
public class InvoiceRecordBMPBean extends GenericEntity implements InvoiceRecord  {
	public static final String ENTITY_NAME = "cacc_invoice_record";

	public static final String COLUMN_INVOICE_HEADER = "invoice_header";
	public static final String COLUMN_PAYMENT_RECORD_ID = "payment_record_id";
	public static final String COLUMN_PROVIDER_ID = "provider_id";
	public static final String COLUMN_SCHOOL_CLASS_MEMBER_ID = "sch_class_member_id";
	public static final String COLUMN_SCHOOL_TYPE_ID = "sch_school_type_id";
	public static final String COLUMN_INVOICE_TEXT = "invoice_text";
	public static final String COLUMN_INVOICE_TEXT_2 = "invoice_text_2";
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
	//public static final String COLUMN_VAT_TYPE = "vat_type";
	public static final String COLUMN_VAT_RULE_REGULATION_ID = "VAT_RULE_REGULATION_ID";
	public static final String COLUMN_CHILDCARE_CONTRACT_ID = "comm_childcare_archive_id";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_INVOICE_TEXT, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_INVOICE_TEXT_2, "", true, true, java.lang.String.class, 1000);
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
		//addAttribute(COLUMN_VAT_TYPE, "", true, true, java.lang.Integer.class);
		addManyToOneRelationship(COLUMN_VAT_RULE_REGULATION_ID,Regulation.class);
		
		addManyToOneRelationship(COLUMN_INVOICE_HEADER, InvoiceHeader.class);
		addManyToOneRelationship(COLUMN_PAYMENT_RECORD_ID, PaymentRecord.class);
		addManyToOneRelationship(COLUMN_SCHOOL_CLASS_MEMBER_ID, SchoolClassMember.class);
		addManyToOneRelationship(COLUMN_SCHOOL_TYPE_ID, SchoolType.class);
		addManyToOneRelationship(COLUMN_PROVIDER_ID, School.class);
		addManyToOneRelationship(COLUMN_REG_SPEC_TYPE_ID, RegulationSpecType.class);
		addManyToOneRelationship(COLUMN_CHILDCARE_CONTRACT_ID, ChildCareContract.class);
		
		setNullable(COLUMN_INVOICE_HEADER, true);
	}
	public InvoiceHeader getInvoiceHeader() {
		return (InvoiceHeader) getColumnValue(COLUMN_INVOICE_HEADER);
	}
	public int getInvoiceHeaderId() {
		return getIntColumnValue(COLUMN_INVOICE_HEADER);
	}
	public int getPaymentRecordId() {
		return getIntColumnValue(COLUMN_PAYMENT_RECORD_ID);
	}
	public PaymentRecord getPaymentRecord() {
		return (PaymentRecord) getColumnValue(COLUMN_PAYMENT_RECORD_ID);
	}
	public int getProviderId() {
		return getIntColumnValue(COLUMN_PROVIDER_ID);
	}
	public School getProvider() {
		return (School) getColumnValue(COLUMN_PROVIDER_ID);
	}
	public int getSchoolClassMemberId() {
		return getIntColumnValue(COLUMN_SCHOOL_CLASS_MEMBER_ID);
	}
	public SchoolClassMember getSchoolClassMember() {
		return (SchoolClassMember) getColumnValue(COLUMN_SCHOOL_CLASS_MEMBER_ID);
	}
	public int getSchoolTypeId() {
		return getIntColumnValue(COLUMN_SCHOOL_TYPE_ID);
	}
	public SchoolType getSchoolType () {
		return (SchoolType) getColumnValue(COLUMN_SCHOOL_TYPE_ID);
	}
	public String getInvoiceText() {
		return getStringColumnValue(COLUMN_INVOICE_TEXT);
	}
	public String getInvoiceText2() {
		return getStringColumnValue(COLUMN_INVOICE_TEXT_2);
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
	public ChildCareContract getChildCareContract () {
		return (ChildCareContract) getColumnValue (COLUMN_CHILDCARE_CONTRACT_ID);
	}

	public void setInvoiceHeaderId(int i) {
		setColumn(COLUMN_INVOICE_HEADER, i);
	}
	public void setInvoiceHeader(InvoiceHeader i){
		setColumn(COLUMN_INVOICE_HEADER, i);
	}
	public void setPaymentRecordId(int i) {
		setColumn(COLUMN_PAYMENT_RECORD_ID, i);
	}
	public void setPaymentRecord(PaymentRecord p) {
		setColumn(COLUMN_PAYMENT_RECORD_ID, p);
	}
	public void setProviderId(int i) {
		setColumn(COLUMN_PROVIDER_ID, i);
	}
	public void setProvider(School s) {
		setColumn(COLUMN_PROVIDER_ID, s);
	}
	public void setSchoolClassMemberId(int i) {
		setColumn(COLUMN_SCHOOL_CLASS_MEMBER_ID, i);
	}
	public void setSchoolClassMember(SchoolClassMember scm) {
		setColumn(COLUMN_SCHOOL_CLASS_MEMBER_ID, scm);
	}
	public void setSchoolTypeId(int i) {
		setColumn(COLUMN_SCHOOL_TYPE_ID, i);
	}
	public void setSchoolType (SchoolType st) {
		setColumn(COLUMN_SCHOOL_TYPE_ID, st);
	}
	public void setInvoiceText(String s) {
		setColumn(COLUMN_INVOICE_TEXT, s);
	}
	public void setInvoiceText2(String s) {
		setColumn(COLUMN_INVOICE_TEXT_2, s);
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
	/*
	public int getVATType() {
		return getIntColumnValue(COLUMN_VAT_TYPE);
	}
	
	public void setVATType(int i) {
		setColumn(COLUMN_VAT_TYPE, i);
	}
	*/
	public int getVATRuleRegulationId() {
		return getIntColumnValue(COLUMN_VAT_RULE_REGULATION_ID);
	}
	public void setVATRuleRegulation(int regulationId) {
		setColumn(COLUMN_VAT_RULE_REGULATION_ID, regulationId);
	}
	public Regulation getVATRuleRegulation() {
		return (Regulation)getColumnValue(COLUMN_VAT_RULE_REGULATION_ID);
	}
	public void setVATRuleRegulation(Regulation regulation) {
		setColumn(COLUMN_VAT_RULE_REGULATION_ID, regulation);
	}	
	public void setChildCareContract (final ChildCareContract contract) {
		setColumn (COLUMN_CHILDCARE_CONTRACT_ID, contract);
	}

	public Collection ejbFindByInvoiceHeader(InvoiceHeader invoiceHeader)
		throws FinderException {
        final String R_ = "r."; // sql alias for invoice record
        final String U_ = "u."; // sql alias for user
        final String M_ = "m."; // sql alias for schoolclassmember
        final String [] tableNames =
						{ getTableName (), UserBMPBean.TABLE_NAME,
							SchoolClassMemberBMPBean.SCHOOLCLASSMEMBER };
        final String [] tableAliases = { "r", "u", "m" };
				final IDOQuery sql = idoQuery ();
        sql.appendSelect().append (R_).appendStar ();
				sql.appendFrom (tableNames, tableAliases);
				sql.appendWhereEquals (R_ + COLUMN_INVOICE_HEADER, invoiceHeader);
				sql.appendAndEquals (R_ + COLUMN_SCHOOL_CLASS_MEMBER_ID,
														 M_ + SchoolClassMemberBMPBean.SCHOOLCLASSMEMBERID);
				sql.appendAndEquals (M_ + SchoolClassMemberBMPBean.MEMBER,
														 U_ + User.FIELD_USER_ID);
				sql.appendOrderBy (new String []
					{ U_ + User.FIELD_DATE_OF_BIRTH + " desc",
						R_ + COLUMN_CHILDCARE_CONTRACT_ID, R_ + COLUMN_ORDER_ID });
				return idoFindPKsByQuery (sql);
	}
	
	public Collection ejbFindByContract(ChildCareContract contract) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEquals(COLUMN_CHILDCARE_CONTRACT_ID, contract);
		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindByPaymentRecord (PaymentRecord paymentRecord)
        throws FinderException {
		final IDOQuery sql = idoQuery ();
		sql.appendSelectAllFrom (this);
		sql.appendWhereEquals (COLUMN_PAYMENT_RECORD_ID, paymentRecord);
		return idoFindPKsByQuery (sql);
	}

	public Collection ejbFindByPaymentRecords (PaymentRecord [] paymentRecords)
        throws FinderException {
		if (0 >= paymentRecords.length) throw new FinderException ();
		final IDOQuery sql = idoQuery ();
		sql.appendSelectAllFrom (this);
		sql.appendWhere (COLUMN_PAYMENT_RECORD_ID).append (" in ( ");
		sql.appendCommaDelimited (Arrays.asList (paymentRecords));
		sql.append (" ) ");
		return idoFindPKsByQuery (sql);
	}

	public int ejbHomeGetIndividualCountByPaymentRecords (PaymentRecord [] paymentRecords) throws IDOException {
		if (0 >= paymentRecords.length) return 0;
		final String R_ = "r."; // sql alias for invoice record
		final String M_ = "m."; // sql alias for schoolclassmember
		final String [] tableNames =
				{ getTableName (), SchoolClassMemberBMPBean.SCHOOLCLASSMEMBER };
		final String [] tableAliases = { "r", "m" };
		final IDOQuery sql = idoQuery ();
		sql.appendSelect ().append (" count (distinct "
																+ M_ + SchoolClassMemberBMPBean.MEMBER + ") ");
		sql.appendFrom (tableNames, tableAliases);
		sql.appendWhere (R_ + COLUMN_PAYMENT_RECORD_ID).append (" in ( ");
		sql.appendCommaDelimited (Arrays.asList (paymentRecords));
		sql.append (" ) ");
		sql.appendAndEquals (R_ + COLUMN_SCHOOL_CLASS_MEMBER_ID,
												 M_ + SchoolClassMemberBMPBean.SCHOOLCLASSMEMBERID);
		return idoGetNumberOfRecords(sql);
	}

	public Collection ejbFindByPaymentRecordOrderedByStudentName (PaymentRecord paymentRecord)
        throws FinderException {
		final String R_ = "r."; // sql alias for invoice record
		final String U_ = "u."; // sql alias for user
		final String M_ = "m."; // sql alias for schoolclassmember
		final String [] tableNames =
				{ getTableName (), UserBMPBean.TABLE_NAME,
					SchoolClassMemberBMPBean.SCHOOLCLASSMEMBER };
		final String [] tableAliases = { "r", "u", "m" };
		final IDOQuery sql = idoQuery ();
		sql.appendSelect().append (R_ + getIDColumnName());
		sql.appendFrom (tableNames, tableAliases);
		sql.appendWhereEquals (R_ + COLUMN_PAYMENT_RECORD_ID, paymentRecord);
		sql.appendAndEquals (R_ + COLUMN_SCHOOL_CLASS_MEMBER_ID,
												 M_ + SchoolClassMemberBMPBean.SCHOOLCLASSMEMBERID);
		sql.appendAndEquals (M_ + SchoolClassMemberBMPBean.MEMBER,
												 U_ + User.FIELD_USER_ID);
		sql.appendOrderBy (new String []
			{ U_ + User.FIELD_LAST_NAME, U_ + User.FIELD_FIRST_NAME });
		return idoFindPKsByQuery (sql);
	}

	/**
	 * Gets the # of placements handled for the given category and period
	 * @param schoolCategoryID
	 * @param period
	 * @return # of placements
	 * @throws FinderException
	 * @throws IDOException
	 */
	public int ejbHomeGetPlacementCountForSchoolCategoryAndPeriod(String schoolCategoryID, Date period) throws IDOException {
		IWTimestamp start = new IWTimestamp(period);
		start.setAsDate();
		start.setDay(1);
		IWTimestamp end = new IWTimestamp(start);
		end.addMonths(1);
		end.addDays(-1);
		
		IDOQuery sql = idoQuery();
		sql.append("select count(r.cacc_invoice_record_id) from "+getEntityName());
		sql.append(" r, cacc_invoice_header h ");
		sql.appendWhereEqualsQuoted("h.school_category_id", schoolCategoryID);
		sql.appendAnd().append("h.period").appendGreaterThanOrEqualsSign().append(start.getDate());
		sql.appendAnd().append("h.period").appendLessThanSign().append(end.getDate());
		sql.appendAnd().append("r."+COLUMN_INVOICE_HEADER+" = h.cacc_invoice_header_id");
		return idoGetNumberOfRecords(sql);
	}

	/** Gets the number of handled children for the specified school types and period
	 * @param schoolTypes
	 * @param month
	 * @return number of handled children
	 */
	public int ejbHomeGetNumberOfHandledChildrenForSchoolTypesAndMonth(Collection schoolTypes, CalendarMonth month) throws IDOException {
		// get the first and the last date of the month
		Date firstDate = month.getFirstDateOfMonth();
		Date lastDate = month.getLastDateOfMonth();
		IDOQuery sql = idoQuery();
		// select count (distinct mem.ic_user_id) from
		sql.append("select count (distinct mem.").append(SchoolClassMemberBMPBean.MEMBER).append(" ) ").appendFrom();
		// sch_class_member mem, cacc_invoice_record rec
		sql.append(SchoolClassMemberBMPBean.SCHOOLCLASSMEMBER).append(" mem, ").append(getEntityName()).append(" rec ");
		// where mem.sch_class_member_id = rec.sch_class_member_id
		sql.appendWhere();
		sql.append("mem.").append(COLUMN_SCHOOL_CLASS_MEMBER_ID).appendEqualSign().append("rec.").append(COLUMN_SCHOOL_CLASS_MEMBER_ID);
		// and
		sql.appendAnd();
		//  rec.period_start_check <= '2003-11-30' and rec.period_end_check >= '2003-11-01'
		sql.append("rec.").append(COLUMN_PERIOD_START_CHECK).appendLessThanOrEqualsSign().append(lastDate);
		sql.appendAnd();
		sql.append("rec.").append(COLUMN_PERIOD_END_CHECK).appendGreaterThanOrEqualsSign().append(firstDate);
		// and
		sql.appendAnd();
		// rec.sch_school_type_id in (4,2,3) 
		sql.append(" rec.").append(COLUMN_SCHOOL_TYPE_ID).appendInCollection(schoolTypes);
		return idoGetNumberOfRecords(sql);
	}
	
	public double ejbHomeGetTotalAmountForSchoolTypesAndMonth(Collection schoolTypes, CalendarMonth month) throws IDOException {
		// get the first and the last date of the month
		Date firstDate = month.getFirstDateOfMonth();
		Date lastDate = month.getLastDateOfMonth();
		IDOQuery sql = idoQuery();
		// select sum(AMOUNT) from cacc_invoice_record
		sql.appendSelectSumFrom(COLUMN_AMOUNT, getEntityName());
		// where
		sql.appendWhere();
		//  period_start_check <= '2003-11-30' and period_end_check >= '2003-11-01'
		sql.append(COLUMN_PERIOD_START_CHECK).appendLessThanOrEqualsSign().append(lastDate);
		sql.appendAnd();
		sql.append(COLUMN_PERIOD_END_CHECK).appendGreaterThanOrEqualsSign().append(firstDate);
		// and
		sql.appendAnd();
		// sch_school_type_id in (4,2,3) 
		sql.append(COLUMN_SCHOOL_TYPE_ID).appendInCollection(schoolTypes);
		return idoGetValueFromSingleValueResultSet(sql);
	}

	/**
	 * Gets a Collection of payment records for the specified month and category
	 * @param month
	 * @return Collection of payment records
	 * @throws FinderException
	 */
	public Collection ejbFindByMonthAndCategory(CalendarMonth month,String categoryId) throws FinderException {
		/*IWTimestamp start = new IWTimestamp(month);
		 start.setAsDate();
		 start.setDay(1);
		 IWTimestamp end = new IWTimestamp(start);
		 end.addMonths(1);*/
		Date start = month.getFirstDateOfMonth();
		Date end = month.getLastDateOfMonth();
		IDOQuery sql = idoQuery();
		sql.append("select r.* from "+getEntityName());
		sql.append(" r, sch_school_type st ");
		sql.appendWhere("r."+COLUMN_PERIOD_START_CHECK).appendGreaterThanOrEqualsSign().append(start);
		sql.appendAnd().append("r."+COLUMN_PERIOD_END_CHECK).appendLessThanOrEqualsSign().append(end);
		sql.appendAndEquals("r."+COLUMN_SCHOOL_TYPE_ID, "st.sch_school_type_id");
		sql.appendAndEqualsQuoted("st.school_category",categoryId);
		return idoFindPKsByQuery(sql);
	}	
}		
