package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;

import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.util.CalendarMonth;
import com.idega.util.IWTimestamp;

/**
 * Bean holding all the information for a payment record
 * 
 * @author Joakim
 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader
 */
public class PaymentRecordBMPBean  extends GenericEntity implements PaymentRecord {
	private static final String ENTITY_NAME = "cacc_payment_record";

	private static final String COLUMN_PAYMENT_HEADER = "payment_header";
	private static final String COLUMN_STATUS = "status";
	private static final String COLUMN_PERIOD = "period";
	private static final String COLUMN_PAYMENT_TEXT = "pament_text";
	private static final String COLUMN_DATE_CREATED = "date_created";
	private static final String COLUMN_CREATED_BY = "created_by";
	private static final String COLUMN_DATE_CHANGED = "date_changed";
	private static final String COLUMN_CHANGED_BY = "changed_by";
	private static final String COLUMN_DATE_TRANSACTION = "date_transaction";
	private static final String COLUMN_PLACEMENTS = "placements";
	private static final String COLUMN_PIECE_AMOUNT = "piece_amount";
	private static final String COLUMN_TOT_AMOUNT = "total_amount";
	private static final String COLUMN_TOT_AMOUNT_VAT = "total_amount_vat";
	private static final String COLUMN_NOTES = "notes";
	private static final String COLUMN_RULE_SPEC_TYPE = "rule_spec_type";
	private static final String COLUMN_OWN_POSTING = "own_posting";
	private static final String COLUMN_DOUBLE_POSTING = "double_posting";
	//private static final String COLUMN_VAT_TYPE = "vat_type";
	private static final String COLUMN_VAT_RULE_REGULATION_ID="VAT_RULE_REGULATION_ID";
	private static final String COLUMN_ORDER_ID = "order_id";
	private static final String COLUMN_VERNR = "vernr";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_STATUS, "", true, true, java.lang.String.class, 1);
		addAttribute(COLUMN_PERIOD, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_PAYMENT_TEXT, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_DATE_CREATED, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_CREATED_BY, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_DATE_CHANGED, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_CHANGED_BY, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_DATE_TRANSACTION, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_PLACEMENTS, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_PIECE_AMOUNT, "", true, true, java.lang.Float.class);
		addAttribute(COLUMN_TOT_AMOUNT, "", true, true, java.lang.Float.class);
		addAttribute(COLUMN_TOT_AMOUNT_VAT, "", true, true, java.lang.Float.class);
		addAttribute(COLUMN_NOTES, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_RULE_SPEC_TYPE, "", true, true, java.lang.String.class, 255);
		addAttribute(COLUMN_OWN_POSTING, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_DOUBLE_POSTING, "", true, true, java.lang.String.class, 1000);
		//addAttribute(COLUMN_VAT_TYPE, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_VERNR, "", String.class, 255);

		addAttribute(COLUMN_ORDER_ID, "", true, true, java.lang.Integer.class);
		addManyToOneRelationship(COLUMN_PAYMENT_HEADER, PaymentHeader.class);
		addManyToOneRelationship(COLUMN_VAT_RULE_REGULATION_ID, Regulation.class);
	}
	public int getPaymentHeaderId() {
		return getIntColumnValue(COLUMN_PAYMENT_HEADER);
	}
	public PaymentHeader getPaymentHeader() {
		return (PaymentHeader) getColumnValue(COLUMN_PAYMENT_HEADER);
	}
	public char getStatus() {
		return getCharColumnValue(COLUMN_STATUS);
	}
	public Date getPeriod() {
		return getDateColumnValue(COLUMN_PERIOD);
	}
	public String getPaymentText() {
		return getStringColumnValue(COLUMN_PAYMENT_TEXT);
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
	public Date getDateTransaction() {
		return getDateColumnValue(COLUMN_DATE_TRANSACTION);
	}
	public int getPlacements() {
		return getIntColumnValue(COLUMN_PLACEMENTS);
	}
	public float getPieceAmount() {
		return getFloatColumnValue(COLUMN_PIECE_AMOUNT);
	}
	public float getTotalAmount() {
		return getFloatColumnValue(COLUMN_TOT_AMOUNT);
	}
	public float getTotalAmountVAT() {
		return getFloatColumnValue(COLUMN_TOT_AMOUNT_VAT);
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
	/*
	public int getVATType() {
		return getIntColumnValue(COLUMN_VAT_TYPE);
	}
	public void setVATType(int i) {
		setColumn(COLUMN_VAT_TYPE, i);
	}
	*/
	public Regulation getVATRuleRegulation() {
		return (Regulation)getColumnValue(COLUMN_VAT_RULE_REGULATION_ID);
	}	
	public int getVATRuleRegulationId() {
		return getIntColumnValue(COLUMN_VAT_RULE_REGULATION_ID);
	}
	public void setVATRuleRegulationId(int regulationId) {
		setColumn(COLUMN_VAT_RULE_REGULATION_ID, regulationId);
	}
	public void setVATRuleRegulation(Regulation vatRegulation) {
		setColumn(COLUMN_VAT_RULE_REGULATION_ID, vatRegulation);
	}	
	public int getOrderId() {
		return getIntColumnValue(COLUMN_ORDER_ID);
	}

	public void setPaymentHeaderId(int i) {
		setColumn(COLUMN_PAYMENT_HEADER, i);
	}
	public void setPaymentHeader(PaymentHeader p) {
		setColumn(COLUMN_PAYMENT_HEADER, p);
	}
	public void setPeriod(Date d) {
		setColumn(COLUMN_PERIOD, d);
	}
	public void setStatus(char c) {
		setColumn(COLUMN_STATUS, c);
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
	public void setDateTransaction(Date d) {
		setColumn(COLUMN_DATE_TRANSACTION, d);
	}
	public void setPlacements(int i) {
		setColumn(COLUMN_PLACEMENTS, i);
	}
	public void setPieceAmount(float f) {
		setColumn(COLUMN_PIECE_AMOUNT, f);
	}
	public void setTotalAmount(float f) {
		setColumn(COLUMN_TOT_AMOUNT, f);
	}
	public void setTotalAmountVAT(float f) {
		setColumn(COLUMN_TOT_AMOUNT_VAT, f);
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
	public void setOrderId(int i) {
		setColumn(COLUMN_ORDER_ID, i);
	}
	
	public void setVernr(String vernr) {
		setColumn(COLUMN_VERNR, vernr);
	}
	
	public String getVernr() {
		return getStringColumnValue(COLUMN_VERNR);
	}
	
	/**
	 * Finds all the payment records that are related to the given payment header
	 * @param paymentHeader
	 * @return
	 * @throws FinderException
	 */
	public Collection ejbFindByPaymentHeader(PaymentHeader paymentHeader) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_PAYMENT_HEADER, paymentHeader.getPrimaryKey());
		sql.appendOrderBy(COLUMN_ORDER_ID);
		return idoFindPKsByQuery(sql);
	}
	
	public Collection ejbFindByPaymentHeaders (final Collection headers)
		throws FinderException {
		final IDOQuery sql = idoQuery ();
		sql.appendSelectAllFrom (this);
		boolean isFirstHeader = true;
		for (Iterator i = headers.iterator (); i.hasNext ();) {
			final PaymentHeader header = (PaymentHeader) i.next ();
			if (isFirstHeader) {
				sql.appendWhereEquals (COLUMN_PAYMENT_HEADER,
															 header.getPrimaryKey());
				isFirstHeader = false;
			} else {
				sql.appendOrEquals (COLUMN_PAYMENT_HEADER,
														header.getPrimaryKey());
			}
		}
		sql.appendOrderBy (COLUMN_PERIOD + "," + COLUMN_ORDER_ID);
		
		return idoFindPKsByQuery (sql);
	}
	
	
	/**
	 * Finds a payment record for the given posting strings
	 * @param ownPostingString
	 * @param doublePostingString 
	 * @param ruleSpecType
	 * @return
	 * @throws FinderException if none was found
	 */
	public Integer ejbFindByPostingStrings(String ownPostingString,String doublePostingString) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEqualsQuoted(COLUMN_OWN_POSTING,ownPostingString);
		sql.appendAndEqualsQuoted(COLUMN_DOUBLE_POSTING,doublePostingString);
		return (Integer)idoFindOnePKByQuery(sql);
	}
	

	/**
	 * Finds a payment record for the given posting strings and the rule specification type
	 * @param ownPostingString
	 * @param doublePostingString 
	 * @param ruleSpecType
	 * @param month The month to find in.
	 * @return
	 * @throws FinderException if none was found
	 */
	public Integer ejbFindByPostingStringsAndRuleSpecTypeAndPaymentTextAndMonth(String ownPostingString,String doublePostingString,String ruleSpecType,String text,CalendarMonth month) throws FinderException {
		IDOQuery sql = idoQueryFindByMonth(month);
		sql.appendAndEqualsQuoted(COLUMN_OWN_POSTING,ownPostingString);
		sql.appendAndEqualsQuoted(COLUMN_DOUBLE_POSTING,doublePostingString);
		sql.appendAndEqualsQuoted(COLUMN_RULE_SPEC_TYPE,ruleSpecType);
		sql.appendAndEqualsQuoted(COLUMN_PAYMENT_TEXT,text);
		return (Integer)idoFindOnePKByQuery(sql);
	}	
	
	public Integer ejbFindByPaymentHeaderAndPostingStringsAndRuleSpecTypeAndPaymentTextAndMonth(PaymentHeader header, String ownPostingString,String doublePostingString,String ruleSpecType,String text,CalendarMonth month) throws FinderException {
		IDOQuery sql = idoQueryFindByMonth(month);
		sql.appendAndEquals(COLUMN_PAYMENT_HEADER,header);
		sql.appendAndEqualsQuoted(COLUMN_OWN_POSTING,ownPostingString);
		sql.appendAndEqualsQuoted(COLUMN_DOUBLE_POSTING,doublePostingString);
		sql.appendAndEqualsQuoted(COLUMN_RULE_SPEC_TYPE,ruleSpecType);
		sql.appendAndEqualsQuoted(COLUMN_PAYMENT_TEXT,text);
		return (Integer)idoFindOnePKByQuery(sql);
	}

	/**
	 * Finds a payment record for the given posting strings and regulation which is a VAT rule regulation
	 * @param ownPostingString
	 * @param doublePostingString 
	 * @param vatRuleRegulation a Regulation of type VAT (Moms)
	 * @param month The month to find in.
	 * @return
	 * @throws FinderException if none was found
	 */
	public Integer ejbFindByPaymentHeaderAndPostingStringsAndVATRuleRegulationAndPaymentTextAndMonth(PaymentHeader pHeader,String ownPostingString,String doublePostingString,Regulation vatRuleRegulation,String text,CalendarMonth month) throws FinderException {
		IDOQuery sql = idoQueryFindByMonth(month);
		sql.appendAndEquals(COLUMN_PAYMENT_HEADER,pHeader.getPrimaryKey().toString());
		sql.appendAndEqualsQuoted(COLUMN_OWN_POSTING,ownPostingString);
		sql.appendAndEqualsQuoted(COLUMN_DOUBLE_POSTING,doublePostingString);
		if(vatRuleRegulation!=null){
			sql.appendAndEquals(COLUMN_VAT_RULE_REGULATION_ID,vatRuleRegulation.getPrimaryKey().toString());
		}
		sql.appendAndEqualsQuoted(COLUMN_PAYMENT_TEXT,text);
		return (Integer)idoFindOnePKByQuery(sql);
	}		
	
	public Integer ejbFindByPostingStringsAndVATRuleRegulationAndPaymentTextAndMonthAndStatus(String ownPostingString,String doublePostingString,Regulation vatRuleRegulation,String text,CalendarMonth month,char status) throws FinderException {
		IDOQuery sql = idoQueryFindByMonth(month);
		sql.appendAndEqualsQuoted(COLUMN_OWN_POSTING,ownPostingString);
		sql.appendAndEqualsQuoted(COLUMN_DOUBLE_POSTING,doublePostingString);
		if(vatRuleRegulation!=null){
			sql.appendAndEquals(COLUMN_VAT_RULE_REGULATION_ID,vatRuleRegulation.getPrimaryKey().toString());
		}
		sql.appendAndEqualsQuoted(COLUMN_PAYMENT_TEXT,text);
		sql.appendAndEqualsQuoted(COLUMN_STATUS,status + "");
		return (Integer)idoFindOnePKByQuery(sql);
	}		
	
	/**
	 * Gets a Collection of payment records for the specified month and all categories
	 * @param month
	 * @return Collection of payment records
	 * @throws FinderException
	 */
	public Collection ejbFindByMonth(CalendarMonth month) throws FinderException {
		/*IWTimestamp start = new IWTimestamp(month);
		start.setAsDate();
		start.setDay(1);
		IWTimestamp end = new IWTimestamp(start);
		end.addMonths(1);*/
		IDOQuery query = idoQueryFindByMonth(month);
		return idoFindPKsByQuery(query);
	}
	
	protected IDOQuery idoQueryFindByMonth(CalendarMonth month){
		Date start = month.getFirstDateOfMonth();
		Date end = month.getLastDateOfMonth();
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhere(COLUMN_PERIOD).appendGreaterThanOrEqualsSign().append(start);
		query.appendAnd().append(COLUMN_PERIOD).appendLessThanOrEqualsSign().append(end);
		return query;
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
		sql.append(" r, cacc_payment_header h ");
		sql.appendWhere("r."+COLUMN_PERIOD).appendGreaterThanOrEqualsSign().append(start);
		sql.appendAnd().append("r."+COLUMN_PERIOD).appendLessThanOrEqualsSign().append(end);
		//sql.appendAnd().append("(").appendEqualsQuoted("r."+COLUMN_STATUS,""+ConstantStatus.LOCKED);
		//sql.appendOrEqualsQuoted("r."+COLUMN_STATUS,""+ConstantStatus.HISTORY).append(")");
		sql.appendAndEqualsQuoted("h.school_category_id", categoryId);
		sql.appendAnd().append("r."+COLUMN_PAYMENT_HEADER+" = h.cacc_payment_header_id");
		return idoFindPKsByQuery(sql);
	}	
	
	/**
	 * 
	 * @param month
	 * @return
	 * @throws FinderException
	 */
	public int ejbHomeGetCountForMonthAndStatusLH(CalendarMonth month) throws IDOException {
		Date start = month.getFirstDateOfMonth();
		Date end = month.getLastDateOfMonth();
		IDOQuery sql = idoQuery();
		sql.append("select count(*) from "+getEntityName());
		sql.appendWhere(COLUMN_PERIOD).appendGreaterThanOrEqualsSign().append(start);
		sql.appendAnd().append(COLUMN_PERIOD).appendLessThanOrEqualsSign().append(end);
		sql.appendAnd().append("(").appendEqualsQuoted(COLUMN_STATUS,""+ConstantStatus.LOCKED);
		sql.appendOrEqualsQuoted(COLUMN_STATUS,""+ConstantStatus.HISTORY).append(")");

		return idoGetNumberOfRecords(sql);
	}

	/**
	 * 
	 * @param month
	 * @return
	 * @throws FinderException
	 */
	public int ejbHomeGetCountForMonthCategoryAndStatusLH(CalendarMonth month, String category) throws IDOException {
		Date start = month.getFirstDateOfMonth();
		Date end = month.getLastDateOfMonth();
		IDOQuery sql = idoQuery();
		sql.append("select count(r.cacc_payment_record_id) from "+getEntityName());
		sql.append(" r, cacc_payment_header h ");
		sql.appendWhere("r."+COLUMN_PERIOD).appendGreaterThanOrEqualsSign().append(start);
		sql.appendAnd().append("r."+COLUMN_PERIOD).appendLessThanOrEqualsSign().append(end);
		sql.appendAnd().append("(").appendEqualsQuoted("r."+COLUMN_STATUS,""+ConstantStatus.LOCKED);
		sql.appendOrEqualsQuoted("r."+COLUMN_STATUS,""+ConstantStatus.HISTORY).append(")");
		sql.appendAndEqualsQuoted("h.school_category_id", category);
		sql.appendAnd().append("r."+COLUMN_PAYMENT_HEADER+" = h.cacc_payment_header_id");

		return idoGetNumberOfRecords(sql);
	}

	/**
	 * 
	 * @param month
	 * @return
	 * @throws FinderException
	 */
	public int ejbHomeGetCountForMonthCategoryAndStatusLHorT(CalendarMonth month, String category) throws IDOException {
		Date start = month.getFirstDateOfMonth();
		Date end = month.getLastDateOfMonth();
		IDOQuery sql = idoQuery();
		sql.append("select count(r.cacc_payment_record_id) from "+getEntityName());
		sql.append(" r, cacc_payment_header h ");
		sql.appendWhere("r."+COLUMN_PERIOD).appendGreaterThanOrEqualsSign().append(start);
		sql.appendAnd().append("r."+COLUMN_PERIOD).appendLessThanOrEqualsSign().append(end);
		sql.appendAnd().append("(").appendEqualsQuoted("r."+COLUMN_STATUS,""+ConstantStatus.LOCKED);
		sql.appendOrEqualsQuoted("r."+COLUMN_STATUS,""+ConstantStatus.TEST);
		sql.appendOrEqualsQuoted("r."+COLUMN_STATUS,""+ConstantStatus.HISTORY).append(")");
		sql.appendAndEqualsQuoted("h.school_category_id", category);
		sql.appendAnd().append("r."+COLUMN_PAYMENT_HEADER+" = h.cacc_payment_header_id");

		return idoGetNumberOfRecords(sql);
	}

	/**
	 * Gets the # of placements handled for the given category and period
	 * @param schoolCategoryID
	 * @param period
	 * @return # of placements
	 * @throws FinderException
	 * @throws IDOException
	 */
	public int ejbHomeGetPlacementCountForSchoolCategoryAndMonth(String schoolCategoryID, CalendarMonth month) throws IDOException {
		/*IWTimestamp start = new IWTimestamp(period);
		start.setAsDate();
		start.setDay(1);
		IWTimestamp end = new IWTimestamp(start);
		end.addMonths(1);*/
		Date start = month.getFirstDateOfMonth();
		Date end = month.getFirstDateOfMonth();
		
		IDOQuery sql = idoQuery();
		sql.append("select sum(r."+COLUMN_PLACEMENTS+") from "+getEntityName());
		sql.append(" r, cacc_payment_header h ");
		sql.appendWhereEqualsQuoted("h.school_category_id", schoolCategoryID);
		sql.appendAnd().append("h.period").appendGreaterThanOrEqualsSign().append(start);
		sql.appendAnd().append("h.period").appendLessThanOrEqualsSign().append(end);
		sql.appendAnd().append("r."+COLUMN_PAYMENT_HEADER+" = h.cacc_payment_header_id");
		return idoGetNumberOfRecords(sql);
	}


	/**
	 * Gets the # of placements handled for the given schoolID, period and category
	 * @param schoolCategoryID
	 * @param period
	 * @return # of placements
	 * @throws FinderException
	 * @throws IDOException
	 */
	public int ejbHomeGetPlacementCountForSchoolIdAndDateAndSchoolCategory(int schoolID,  Date period, String schoolCategoryID ) throws IDOException {

		IWTimestamp start = new IWTimestamp(period);
		start.setAsDate();
		start.setDay(1);
		IWTimestamp end = new IWTimestamp(start);
		end.addMonths(1);
		
		IDOQuery sql = idoQuery();
		
		sql.appendSelect().append("count (distinct m.ic_user_id) from " + getEntityName() + " p, sch_class_member m , cacc_payment_header h, cacc_invoice_record i");
		sql.appendWhereEqualsQuoted("h.school_category_id", schoolCategoryID);
		sql.appendAnd().append("p.payment_header = h.cacc_payment_header_id");
		sql.appendAnd().append("i.invoice_header = p."+getIDColumnName());
		sql.appendAnd().append("p."+COLUMN_PERIOD).appendGreaterThanOrEqualsSign().append(start.getDate());
		sql.appendAnd().append("p."+COLUMN_PERIOD).appendLessThanSign().append(end.getDate());
		sql.appendAndEquals("h.school_id", schoolID);
//		System.out.println(sql.toString());
		return idoGetNumberOfRecords(sql);
	}


	/**
	 * Gets tottal amount paid for the given category and period
	 * @param schoolCategoryID
	 * @param period
	 * @return
	 * @throws FinderException
	 * @throws IDOException
	 */
	public int ejbHomeGetTotalVATAmountForPaymentHeaderAndMonthAndVATRuleRegulation(PaymentHeader ph,CalendarMonth month,Regulation vatRuleRegulation) throws IDOException {
		/*IWTimestamp start = new IWTimestamp(period);
		start.setAsDate();
		start.setDay(1);
		IWTimestamp end = new IWTimestamp(start);
		end.addMonths(1);*/
		//int pHeaderId = ((Number)ph.getPrimaryKey()).intValue();
		IWTimestamp start = month.getFirstTimestamp();
		IWTimestamp end = month.getLastTimestamp();
		
		IDOQuery sql = idoQuery();
		sql.append("select sum("+COLUMN_TOT_AMOUNT_VAT+") from "+getEntityName());
		sql.append(" r, cacc_payment_header h ");
		sql.appendWhereEqualsQuoted("h.cacc_payment_header_id", ph.getPrimaryKey().toString());
		sql.appendAnd().append("h.period").appendGreaterThanOrEqualsSign().append(start.getDate());
		sql.appendAnd().append("h.period").appendLessThanOrEqualsSign().append(end.getDate());
		sql.appendAnd().append("r."+COLUMN_PAYMENT_HEADER+" = h.cacc_payment_header_id");
		sql.appendAnd().appendWhereEqualsQuoted("r."+COLUMN_VAT_RULE_REGULATION_ID,vatRuleRegulation.getPrimaryKey().toString());
		return idoGetNumberOfRecords(sql);
	}	
	
	
	/**
	 * Gets tottal amount paid for the given category and period
	 * @param schoolCategoryID
	 * @param period
	 * @return
	 * @throws FinderException
	 * @throws IDOException
	 */
	public int ejbHomeGetTotAmountForSchoolCategoryAndPeriod(String schoolCategoryID, Date period) throws IDOException {
		IWTimestamp start = new IWTimestamp(period);
		start.setAsDate();
		start.setDay(1);
		IWTimestamp end = new IWTimestamp(start);
		end.addMonths(1);
		
		IDOQuery sql = idoQuery();
		sql.append("select sum("+COLUMN_TOT_AMOUNT+") from "+getEntityName());
		sql.append(" r, cacc_payment_header h ");
		sql.appendWhereEqualsQuoted("h.school_category_id", schoolCategoryID);
		sql.appendAnd().append("h.period").appendGreaterThanOrEqualsSign().append(start.getDate());
		sql.appendAnd().append("h.period").appendLessThanSign().append(end.getDate());
		sql.appendAnd().append("h.period").appendLessThanSign().append(end.getDate());
		sql.appendAnd().append("r."+COLUMN_PAYMENT_HEADER+" = h.cacc_payment_header_id");
		return idoGetNumberOfRecords(sql);
	}

	/**
	 * Gets tottal amount paid for the given provider and period
	 * @param providerID
	 * @param period
	 * @return
	 * @throws FinderException
	 * @throws IDOException
	 */
	
	public int ejbHomeGetTotAmountForProviderAndPeriod(int providerID, Date period, String schoolCategoryID) throws IDOException {
		IWTimestamp start = new IWTimestamp(period);
		start.setAsDate();
		start.setDay(1);
		IWTimestamp end = new IWTimestamp(start);
		end.addMonths(1);
		
		IDOQuery sql = idoQuery();
		sql.append("select sum("+COLUMN_TOT_AMOUNT+") from "+getEntityName());
		sql.append(" r, cacc_payment_header h ");
		sql.appendWhereEquals("h.school_id", providerID);
		sql.appendAndEqualsQuoted("h.school_category_id", schoolCategoryID);
		sql.appendAnd().append("h.period").appendGreaterThanOrEqualsSign().append(start.getDate());
		sql.appendAnd().append("h.period").appendLessThanSign().append(end.getDate());
		sql.appendAnd().append("r."+COLUMN_PAYMENT_HEADER+" = h.cacc_payment_header_id");
//		System.out.println(sql.toString());
		return idoGetNumberOfRecords(sql);
	}

}
	
