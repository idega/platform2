package se.idega.idegaweb.commune.accounting.invoice.data;

import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolClassMemberBMPBean;
import com.idega.block.school.data.SchoolTypeBMPBean;
import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;
import com.idega.user.data.UserBMPBean;
import com.idega.util.CalendarMonth;
import java.sql.Date;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;

/**
 * The databean for the invoice header. The invoice header holds all the 
 * information that is the same for all the invoice records that it is related to.
 * 
 * @author Joakim
 */
public class InvoiceHeaderBMPBean extends GenericEntity implements InvoiceHeader 
{
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
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_PERIOD, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_STATUS, "", true, true, java.lang.String.class, 1);
		addAttribute(COLUMN_DATE_CREATED, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_DATE_ADJUSTED, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_DATE_TRANSACTION, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_CREATED_BY, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_CHANGED_BY, "", true, true, java.lang.String.class, 1000);

		addManyToOneRelationship(COLUMN_SCHOOL_CATEGORY_ID, SchoolCategory.class);
		addManyToOneRelationship(COLUMN_CUSTODIAN_ID, User.class);
	}
	public String getSchoolCategoryID() {
		return getStringColumnValue(COLUMN_SCHOOL_CATEGORY_ID);
	}
	public SchoolCategory getSchoolCategory() {
		return (SchoolCategory) getColumnValue(COLUMN_SCHOOL_CATEGORY_ID);
	}
	public Date getPeriod() {
		return getDateColumnValue(COLUMN_PERIOD);
	}
	public int getCustodianId() {
		return getIntColumnValue(COLUMN_CUSTODIAN_ID);
	}
	public User getCustodian() {
		return (User) getColumnValue(COLUMN_CUSTODIAN_ID);
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


	public void setSchoolCategoryID(String i) {
		setColumn(COLUMN_SCHOOL_CATEGORY_ID, i);
	}
	public void setSchoolCategory (SchoolCategory sc) {
		setColumn(COLUMN_SCHOOL_CATEGORY_ID, sc);
	}
	public void setPeriod(Date d) {
		setColumn(COLUMN_PERIOD, d);
	}
	public void setCustodianId(int i) {
		setColumn(COLUMN_CUSTODIAN_ID, i);
	}
	public void setCustodian(User u) {
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
	
	public Integer ejbFindByCustodian(User custodian) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_CUSTODIAN_ID, custodian.getPrimaryKey());
		return (Integer)idoFindOnePKByQuery(sql);
	}
	
	public Integer ejbFindByCustodianAndMonth(User custodian, CalendarMonth month) throws FinderException {
		Date start = month.getFirstDateOfMonth();
		Date end = month.getLastDateOfMonth();
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_CUSTODIAN_ID, custodian.getPrimaryKey());
		sql.appendAnd().append(COLUMN_PERIOD).appendGreaterThanOrEqualsSign().append(start);
		sql.appendAnd().append(COLUMN_PERIOD).appendLessThanOrEqualsSign().append(end);
		return (Integer)idoFindOnePKByQuery(sql);
	}
	
	public Integer ejbFindByCustodianID(int custodianID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_CUSTODIAN_ID, custodianID);
		return (Integer)idoFindOnePKByQuery(sql);
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
	
	public Collection ejbFindByMonth(CalendarMonth month) throws FinderException {
		IDOQuery query = idoQueryFindByMonth(month);
		return idoFindPKsByQuery(query);
	}
	
	public Collection ejbFindByMonthAndSchoolCategory(CalendarMonth month, SchoolCategory schoolCategory) throws FinderException {
		IDOQuery query = idoQueryFindByMonth(month);
		query.appendAndEqualsQuoted(COLUMN_SCHOOL_CATEGORY_ID, (String)schoolCategory.getPrimaryKey());
		return idoFindPKsByQuery(query);
	}
	
	public int ejbHomeGetNumberOfInvoicesForSchoolCategoryAndMonth(String schoolCategoryID, CalendarMonth month) throws IDOException {
		Date start = month.getFirstDateOfMonth();
		Date end = month.getLastDateOfMonth();
		IDOQuery query = idoQuery();
		query.appendSelectCountFrom(this);
		query.appendWhere(COLUMN_PERIOD).appendGreaterThanOrEqualsSign().append(start);
		query.appendAnd().append(COLUMN_PERIOD).appendLessThanOrEqualsSign().append(end);
		query.appendAndEqualsQuoted(COLUMN_SCHOOL_CATEGORY_ID, schoolCategoryID);
		return idoGetNumberOfRecords(query);
	}

	public int ejbHomeGetNumberOfInvoicesForCurrentMonth() throws IDOException {
		IDOQuery query = idoQuery();
		query.appendSelectCountFrom(this);
		query.appendWhereEquals(COLUMN_STATUS, "'P'");
		return idoGetNumberOfRecords(query);
	}

	public int ejbHomeGetNumberOfInvoicesForMonth(CalendarMonth month) throws IDOException {
		Date start = month.getFirstDateOfMonth();
		Date end = month.getLastDateOfMonth();
		IDOQuery query = idoQuery();
		query.appendSelectCountFrom(this);
		query.appendWhere(COLUMN_PERIOD).appendGreaterThanOrEqualsSign().append(start);
		query.appendAnd().append(COLUMN_PERIOD).appendLessThanOrEqualsSign().append(end);
		return idoGetNumberOfRecords(query);
	}
	
	public int ejbHomeGetNumberOfChildrenForCurrentMonth() throws IDOException {
		IDOQuery query = idoQuery();
		query.appendSelectCountFrom();
		query.append("(select distinct a.child_id from cacc_invoice_header h, cacc_invoice_record r, comm_childcare_archive a where h.status='P' and a.comm_childcare_archive_id=r.comm_childcare_archive_id and r.invoice_header = h.cacc_invoice_header_id) a");
		//query.appendWhereEquals(COLUMN_STATUS, "'P'");		
		return idoGetNumberOfRecords(query);
	}

	public int ejbHomeGetNumberOfChildrenForMonth(CalendarMonth month) throws IDOException {
		Date start = month.getFirstDateOfMonth();
		Date end = month.getLastDateOfMonth();
		IDOQuery query = idoQuery();
		query.appendSelectCountFrom();
		query.append("(select distinct a.child_id from cacc_invoice_header h, cacc_invoice_record r, comm_childcare_archive a where h.status='P'"); 
		query.appendAnd().append(COLUMN_PERIOD).appendGreaterThanOrEqualsSign().append(start);
		query.appendAnd().append(COLUMN_PERIOD).appendLessThanOrEqualsSign().append(end);
		query.append("and a.comm_childcare_archive_id=r.comm_childcare_archive_id and r.invoice_header = h.cacc_invoice_header_id) a");		
	//query.appendWhereEquals(COLUMN_STATUS, "'P'");		
		return idoGetNumberOfRecords(query);
	}
	
	public int ejbHomeGetTotalInvoiceRecordAmountForCurrentMonth() throws IDOException {
		IDOQuery query = idoQuery();
		//query.appendSelectCountFrom();
		query.append("select  round(sum(r.amount)) from cacc_invoice_header h, cacc_invoice_record r where h.status='P' and r.invoice_header = h.cacc_invoice_header_id"); 	
		//query.appendWhereEquals(COLUMN_STATUS, "'P'");		
		return idoGetNumberOfRecords(query);
	}

	public int ejbHomeGetTotalInvoiceRecordAmountForMonth(CalendarMonth month) throws IDOException {
		Date start = month.getFirstDateOfMonth();
		Date end = month.getLastDateOfMonth();
		IDOQuery query = idoQuery();
		//query.appendSelectCountFrom();
		query.append("select  round(sum(r.amount)) from cacc_invoice_header h, cacc_invoice_record r where r.invoice_header = h.cacc_invoice_header_id"); 
		query.appendAnd().append(COLUMN_PERIOD).appendGreaterThanOrEqualsSign().append(start);
		query.appendAnd().append(COLUMN_PERIOD).appendLessThanOrEqualsSign().append(end);		
		//query.appendWhereEquals(COLUMN_STATUS, "'P'");		
		return idoGetNumberOfRecords(query);
	}
	

    /**
     * Retreives a collection of all InvoiceHeaders where the user given is
     * either custodian or the child and in the period. If any of the dates
     * are null, that constraint will be ignored.
     *
     * @param user the user to search for
     * @param fromDate first month in search span
     * @param toDate last month in search span
     * @return collection of invoice headers
     */
    public Collection ejbFindByCustodianOrChild
        (final String schoolCategory, final User user,
         final Collection custodians, final java.util.Date fromDate,
         java.util.Date toDate) throws FinderException {
		final IDOQuery sql = idoQuery ();
        final String H_ = "h."; // sql alias for invoice header
        final String U_ = "u."; // sql alias for user
        final String R_ = "r."; // sql alias for invoice record
        final String M_ = "m."; // sql alias for schoolclassmember
        final String T_ = "t."; // sql alias for school type
        final Date fromPeriod = getPeriod (fromDate, 0);
        final Date toPeriod = getPeriod (toDate, 1);
        final String [] outerTableNames =
                { getTableName (), UserBMPBean.TABLE_NAME };
        final String [] outerTableAliases = { "h", "u" };
        final String [] innerTableNames =
                { InvoiceRecordBMPBean.ENTITY_NAME,
                  SchoolClassMemberBMPBean.SCHOOLCLASSMEMBER,
                  SchoolTypeBMPBean.SCHOOLTYPE };
        final String [] innerTableAliases = { "r", "m", "t" };

        sql.appendSelect()
                .append (H_)
                .appendStar ()
                .appendFrom (outerTableNames, outerTableAliases)
                .appendWhere ()
                .appendLeftParenthesis ()
                .appendEquals (H_ + COLUMN_CUSTODIAN_ID, user);

        // << inner 'exists' selection starts here
        sql.appendOr ()
                .append (" exists ")
                .appendLeftParenthesis ()
                .appendSelect()
                .append (H_)
                .appendStar ()
                .appendFrom (innerTableNames, innerTableAliases)
                .appendWhere ()
                .appendEquals (H_ + ENTITY_NAME + "_id",
                               R_ + InvoiceRecordBMPBean.COLUMN_INVOICE_HEADER)
                .appendAndEquals
                (R_ + InvoiceRecordBMPBean.COLUMN_SCHOOL_CLASS_MEMBER_ID,
                 M_ + SchoolClassMemberBMPBean.SCHOOLCLASSMEMBERID)
                .appendAndEquals (M_ + SchoolClassMemberBMPBean.MEMBER, user)
                .appendAndEquals (M_ + SchoolClassMemberBMPBean.SCHOOL_TYPE,
                                  T_ + SchoolTypeBMPBean.SCHOOLTYPE + "_id");
        if (null != schoolCategory && 0 < schoolCategory.length ()) {
            sql.appendAndEqualsQuoted (T_ + SchoolTypeBMPBean.SCHOOLCATEGORY,
                                       schoolCategory);
        }
        sql.appendRightParenthesis ();
        // inner 'exists' selection ends here >>

        for (Iterator i = custodians.iterator (); i.hasNext ();) {
            final User custodian = (User) i.next ();
            sql.appendOrEquals (H_ + COLUMN_CUSTODIAN_ID, custodian);
        }
        sql.appendRightParenthesis ()
                .appendAndEquals (U_ + User.FIELD_USER_ID, user);
        if (null != fromPeriod) {
            sql.appendAnd ()
                    .append (H_ + COLUMN_PERIOD)
                    .appendGreaterThanOrEqualsSign ()
                    .append (fromPeriod);
        }
        if (null != toPeriod) {
            sql.appendAnd ()
                    .append (toPeriod)
                    .appendGreaterThanSign ()
                    .append (H_ + COLUMN_PERIOD);
        }
        sql.appendOrderBy (U_ + User.FIELD_PERSONAL_ID);

        return idoFindPKsBySQL (sql.toString ());
    }

    /**
     * Calculates a new java.sql.Date the 1st of this month and then adds the
     * given number of moths
     *
     * @param date a date any day in amonth
     * @param monthOffset add this amont of monts to return value
     * @return date of the 1st day in a month
     */
    private static Date getPeriod (final java.util.Date date,
                                   final int monthOffset) {
        if (null == date) return null;
        final Calendar calendar = Calendar.getInstance ();
        calendar.setTime (date);
        calendar.set (calendar.get (Calendar.YEAR),
                      calendar.get (Calendar.MONTH) + monthOffset, 1, 0, 0);
        return new Date (calendar.getTimeInMillis ());

    }

	public Collection ejbFindByStatusAndCategory(String status, String schoolCategory) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEqualsWithSingleQuotes(COLUMN_STATUS,status);
		sql.appendAndEqualsQuoted(COLUMN_SCHOOL_CATEGORY_ID, schoolCategory);
		
		return idoFindPKsByQuery(sql);
	}
}
