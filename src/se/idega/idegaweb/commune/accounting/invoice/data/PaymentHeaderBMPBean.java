package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolBMPBean;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolManagementType;
import com.idega.block.school.data.SchoolManagementTypeHome;
import com.idega.core.location.data.Commune;
import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;
import com.idega.util.CalendarMonth;
import com.idega.util.IWTimestamp;
import com.idega.util.TimePeriod;

/**
 * The databean for the payment header. The payment header holds all the 
 * information that is the same for all the payment records that it is related to.
 * 
 * @author Joakim
 */
public class PaymentHeaderBMPBean extends GenericEntity implements PaymentHeader {
	private static final String ENTITY_NAME = "cacc_payment_header";

	private static final String COLUMN_SCHOOL_ID = "school_id";
	private static final String COLUMN_SCHOOL_CATEGORY_ID = "school_category_id";
	private static final String COLUMN_SIGNATURE = "signature";
	private static final String COLUMN_DATE_ATTESTED = "date_attested";
	private static final String COLUMN_STATUS = "status";
	private static final String COLUMN_PERIOD = "period";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_SCHOOL_ID, School.class);
		addManyToOneRelationship(COLUMN_SCHOOL_CATEGORY_ID, SchoolCategory.class);
		addManyToOneRelationship(COLUMN_SIGNATURE, User.class);
		addAttribute(COLUMN_STATUS, "", true, true, java.lang.String.class, 1);
		addAttribute(COLUMN_DATE_ATTESTED, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_PERIOD, "", true, true, java.sql.Date.class);
	}
	public int getSchoolID() {
		return getIntColumnValue(COLUMN_SCHOOL_ID);
	}
	public School getSchool() {
		return (School) getColumnValue(COLUMN_SCHOOL_ID);
	}
	public String getSchoolCategoryID() {
		return getStringColumnValue(COLUMN_SCHOOL_CATEGORY_ID);
	}
	public SchoolCategory getSchoolCategory() {
		return (SchoolCategory) getColumnValue(COLUMN_SCHOOL_CATEGORY_ID);
	}
	public int getSignatureID() {
		return getIntColumnValue(COLUMN_SIGNATURE);
	}
	public char getStatus() {
		return getCharColumnValue(COLUMN_STATUS);
	}
	public Date getDateAttested() {
		return getDateColumnValue(COLUMN_DATE_ATTESTED);
	}
	public Date getPeriod() {
		return getDateColumnValue(COLUMN_PERIOD);
	}

	public void setSchoolID(int i) {
		setColumn(COLUMN_SCHOOL_ID, i);
	}
	public void setSchool(School s) {
		setColumn(COLUMN_SCHOOL_ID, s);
	}
	public void setSchoolCategoryID(int i) {
		setColumn(COLUMN_SCHOOL_CATEGORY_ID, i);
	}
	public void setSchoolCategory(SchoolCategory s) {
		setColumn(COLUMN_SCHOOL_CATEGORY_ID, s);
	}
	public void setSignaturelID(int i) {
		setColumn(COLUMN_SIGNATURE, i);
	}
	public void setSignaturelID(User u) {
		setColumn(COLUMN_SIGNATURE, u);
	}
	public void setStatus(char c) {
		setColumn(COLUMN_STATUS, c);
	}
	public void setDateAttested(Date d) {
		setColumn(COLUMN_DATE_ATTESTED, d);
	}
	public void setPeriod(Date d) {
		setColumn(COLUMN_PERIOD, d);
	}

	/**
	 * Finds one school for the given input paramters
	 * @param school
	 * @param schoolCategory
	 * @param period
	 * @return
	 * @throws FinderException
	 */
	public Integer ejbFindBySchoolCategorySchoolPeriod(School school, SchoolCategory schoolCategory, Date period) throws FinderException {
		IWTimestamp start = new IWTimestamp(period);
		start.setAsDate();
		start.setDay(1);
		IWTimestamp end = new IWTimestamp(start);
		end.addMonths(1);

		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_SCHOOL_ID, school.getPrimaryKey());
		sql.appendAndEqualsQuoted(COLUMN_SCHOOL_CATEGORY_ID, (String) schoolCategory.getPrimaryKey());
		sql.appendAnd().append(COLUMN_PERIOD).appendGreaterThanOrEqualsSign().append(start.getDate());
		sql.appendAnd().append(COLUMN_PERIOD).appendLessThanSign().append(end.getDate());
		return (Integer) idoFindOnePKByQuery(sql);
	}

	public Integer ejbFindBySchoolCategoryAndSchoolAndPeriodAndStatus(School school, SchoolCategory schoolCategory, TimePeriod period, String status) throws FinderException {
		return (Integer) idoFindOnePKByQuery(findBySchoolCategoryAndSchoolAndPeriodAndStatusSQL(school, schoolCategory, period, status));
	}
	
	public Collection ejbFindAllBySchoolCategoryAndSchoolAndPeriodAndStatus(School school, SchoolCategory schoolCategory, TimePeriod period, String status) throws FinderException {
		return idoFindPKsByQuery(findBySchoolCategoryAndSchoolAndPeriodAndStatusSQL(school, schoolCategory, period, status));
	}	
	
	private IDOQuery findBySchoolCategoryAndSchoolAndPeriodAndStatusSQL(School school, SchoolCategory schoolCategory, TimePeriod period, String status) {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom (this);
		sql.appendWhereEquals (COLUMN_SCHOOL_ID, school);
		sql.appendAndEqualsQuoted (COLUMN_SCHOOL_CATEGORY_ID,
															 (String) schoolCategory.getPrimaryKey());
		sql.appendAndEqualsQuoted (COLUMN_STATUS, status);
		sql.appendAnd ();
		sql.appendWithinDates(COLUMN_PERIOD, period.getFirstTimestamp().getDate (),
													period.getLastTimestamp ().getDate ());
		return sql;		
	}

	/**
	 * Gets # of providers for the given input parameters
	 * @param schoolCategoryID
	 * @param period
	 * @return
	 * @throws FinderException
	 * @throws IDOException
	 */
	public int ejbHomeGetProviderCountForSchoolCategoryAndPeriod(String schoolCategoryID, Date period) throws IDOException {
		IWTimestamp start = new IWTimestamp(period);
		start.setAsDate();
		start.setDay(1);
		IWTimestamp end = new IWTimestamp(start);
		end.addMonths(1);

		IDOQuery sql = idoQuery();
		sql.appendSelect().append("count (distinct " + COLUMN_SCHOOL_ID + ") from " + getEntityName());
		sql.appendWhereEqualsQuoted(COLUMN_SCHOOL_CATEGORY_ID, schoolCategoryID);
		sql.appendAnd().append(COLUMN_PERIOD).appendGreaterThanOrEqualsSign().append(start.getDate());
		sql.appendAnd().append(COLUMN_PERIOD).appendLessThanSign().append(end.getDate());
		return idoGetNumberOfRecords(sql);
	}

	/**
	 * Gets # of placements for the given input parameters
	 * @param schoolID
	 * @param period
	 * @return
	 * @throws FinderException
	 * @throws IDOException
	 */
	public int ejbHomeGetPlacementCountForSchoolAndPeriod(int schoolID, Date period, String schoolCategoryID) throws IDOException {
		IWTimestamp start = new IWTimestamp(period);
		start.setAsDate();
		start.setDay(1);
		IWTimestamp end = new IWTimestamp(start);
		end.addMonths(1);

		IDOQuery sql = idoQuery();
		sql.appendSelect().append("count (distinct m.ic_user_id) from " + getEntityName() + " p, sch_school_class c, sch_class_member m, sch_school s");
		sql.appendWhereEqualsQuoted("p.school_category_id", schoolCategoryID);
		sql.appendAnd().append("p.school_id = c.school_id");
		sql.appendAnd().append("p.school_id = s.sch_school_id");
		sql.appendAnd().append("c.sch_school_class_id = m.sch_school_class_id");
		sql.appendAnd().append(COLUMN_PERIOD).appendGreaterThanOrEqualsSign().append(start.getDate());
		sql.appendAnd().append(COLUMN_PERIOD).appendLessThanSign().append(end.getDate());
		sql.appendAndEquals("p." + COLUMN_SCHOOL_ID, schoolID);
		return idoGetNumberOfRecords(sql);
	}


	/**
	 * Finds a collection of Payment headers with a certain status
	 * 
	 * @param schoolCategoryPK SchoolCategory primaryKey
	 * @param status Status
	 * @return Collection of PaymentHeader objects
	 * @throws FinderException
	 */
	public Collection ejbFindBySchoolAndSchoolCategoryPKAndStatus(Object schoolPK, Object schoolCategoryPK, String status) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_STATUS, status)
		.appendAndEquals(COLUMN_SCHOOL_ID, schoolPK.toString())
		.appendAndEqualsQuoted(COLUMN_SCHOOL_CATEGORY_ID, schoolCategoryPK.toString());
		return idoFindPKsByQuery(sql);
	}
	
	/**
	 * Finds a collection of Payment headers for private providers given the input parameters
	 * 
	 * @param schoolCategory
	 * @param period
	 * @return
	 * @throws IDOLookupException
	 * @throws EJBException
	 * @throws FinderException
	 */
	public Collection ejbFindBySchoolCategoryAndPeriodForPrivate(SchoolCategory schoolCategory, Date period) throws IDOLookupException, EJBException, FinderException {
		IWTimestamp start = new IWTimestamp(period);
		start.setAsDate();
		start.setDay(1);
		IWTimestamp end = new IWTimestamp(start);
		end.addMonths(1);

		String managementType = (String) ((SchoolManagementTypeHome) IDOLookup.getHome(SchoolManagementType.class)).findPrivateManagementType().getPrimaryKey();

		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(ENTITY_NAME + " ph, " + SchoolBMPBean.SCHOOL + " s");
		sql.appendWhere("ph." + COLUMN_PERIOD).appendGreaterThanOrEqualsSign().append(start.getDate());
		sql.appendAnd().append("ph." + COLUMN_PERIOD).appendLessThanSign().append(end.getDate());
		sql.appendAndEqualsQuoted("ph." + COLUMN_SCHOOL_CATEGORY_ID, (String) schoolCategory.getPrimaryKey());
		sql.appendAndEquals("ph." + COLUMN_SCHOOL_ID, "s.sch_school_id");
		sql.appendAndEqualsQuoted("s.management_type", managementType);
		return idoFindPKsBySQL(sql.toString());
	}

	/**
	 * Finds a collection of Payment headers for 
	 * given the input parameters
	 * 
	 * @param status
	 * @param schoolID
	 * @return
	 * @throws IDOLookupException
	 * @throws EJBException
	 * @throws FinderException
	 */
	public Collection ejbFindByStatusAndSchoolId(char status, int schoolID) throws EJBException, FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEqualsWithSingleQuotes(COLUMN_STATUS, String.valueOf(status));
		sql.appendAndEquals(COLUMN_SCHOOL_ID, schoolID);
		return idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindBySchoolCategoryAndSchoolAndPeriod(final String schoolCategory, final Integer providerId, final Date startPeriod, final Date endPeriod) throws FinderException {
		final IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_SCHOOL_ID, providerId + "").appendAndEqualsQuoted(COLUMN_SCHOOL_CATEGORY_ID, schoolCategory);
		if (null != startPeriod) {
            final IWTimestamp startStamp = new IWTimestamp(startPeriod);
            startStamp.setAsDate ();
            startStamp.setDay (1);
			sql.appendAnd().append(COLUMN_PERIOD).appendGreaterThanOrEqualsSign().append(startStamp.getDate ());
		}
		if (null != endPeriod) {
            final IWTimestamp endStamp = new IWTimestamp (endPeriod);
            endStamp.setAsDate ();
            endStamp.setDay (1);
            endStamp.addMonths (1);
			sql.appendAnd ().append (endStamp.getDate ()).appendGreaterThanSign().append(COLUMN_PERIOD);
		}
		return idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindBySchoolCategoryStatusInCommuneWithCommunalManagement(String schoolCategory, char status) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.append("select p.* from " + ENTITY_NAME + " p, cacc_provider_acc_prop prop, cacc_provider_type t");
		sql.appendWhereEqualsWithSingleQuotes("p." + COLUMN_SCHOOL_CATEGORY_ID,schoolCategory);
		sql.appendAndEqualsQuoted("p." + COLUMN_STATUS,String.valueOf(status));
		sql.appendAndEquals("p." + COLUMN_SCHOOL_ID, "prop.school_id");
		sql.appendAndEquals("prop.provider_type_id", "t.provider_type_id");
		sql.appendAndEqualsQuoted("t.localization_key","cacc_provider_type.commune");
		
		System.out.println("sql = " + sql.toString());
		
		return idoFindPKsByQuery(sql);
	}

	public Collection ejbFindBySchoolCategoryStatusInCommuneWithoutCommunalManagement(String schoolCategory, char status) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.append("select p.* from " + ENTITY_NAME + " p, cacc_provider_acc_prop prop, cacc_provider_type t");
		sql.appendWhereEqualsWithSingleQuotes("p." + COLUMN_SCHOOL_CATEGORY_ID,schoolCategory);
		sql.appendAndEqualsQuoted("p." + COLUMN_STATUS,String.valueOf(status));
		sql.appendAndEquals("p." + COLUMN_SCHOOL_ID, "prop.school_id");
		sql.appendAndEquals("prop.provider_type_id", "t.provider_type_id");
		sql.appendAnd();
		sql.append("t.localization_key");
		sql.appendNOTEqual();
		sql.appendQuoted("cacc_provider_type.commune");
		
		System.out.println("sql = " + sql.toString());
		
		return idoFindPKsByQuery(sql);
	}

	public Collection ejbFindBySchoolCategoryStatusOutsideCommuneOrWithoutCommunalManagement(String schoolCategory, char status) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.append("select p.* from " + ENTITY_NAME + " p, cacc_provider_acc_prop prop, cacc_provider_type t");
		sql.appendWhereEqualsWithSingleQuotes("p." + COLUMN_SCHOOL_CATEGORY_ID,schoolCategory);
		sql.appendAndEqualsQuoted("p." + COLUMN_STATUS,String.valueOf(status));
		sql.appendAndEquals("p." + COLUMN_SCHOOL_ID, "prop.school_id");
		sql.appendAndEquals("prop.provider_type_id", "t.provider_type_id");
		sql.appendAndEqualsQuoted("t.localization_key","cacc_provider_type.private");
		
		return idoFindPKsBySQL(sql.toString());
	}
	
	public Collection ejbFindBySchoolCategoryStatusOutsideCommuneWithCommunalManagement(String schoolCategory, char status, Commune commune) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.append("select p.* from " + ENTITY_NAME + " p, cacc_provider_acc_prop prop, cacc_provider_type t, sch_school s");
		sql.appendWhereEqualsWithSingleQuotes("p." + COLUMN_SCHOOL_CATEGORY_ID,schoolCategory);
		sql.appendAndEqualsQuoted("p." + COLUMN_STATUS,String.valueOf(status));
		sql.appendAndEquals("p." + COLUMN_SCHOOL_ID, "prop.school_id");
		sql.appendAndEquals("prop.provider_type_id", "t.provider_type_id");
		sql.appendAndEqualsQuoted("t.localization_key","cacc_provider_type.commune");
		sql.appendAndEquals("p." + COLUMN_SCHOOL_ID, "s.sch_school_id");
		sql.appendAndEquals("s.commune", commune);
		
		return idoFindPKsBySQL(sql.toString());
	}
		
	public Collection ejbFindBySchoolCategoryAndStatus(String schoolCategory, char status) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEqualsWithSingleQuotes(COLUMN_SCHOOL_CATEGORY_ID,schoolCategory);
		sql.appendAndEqualsQuoted(COLUMN_STATUS, String.valueOf(status));
		
		return idoFindPKsByQuery(sql);
	}


	/**
	 * Finds a collection of distinct Payment headers given the input parameters
	 * 
	 * @param schoolCategory
	 * @param period
	 * @return
	 * @throws IDOLookupException
	 * @throws EJBException
	 * @throws FinderException
	 */
	public Collection ejbFindBySchoolCategoryAndPeriod(String sc, Date period) throws EJBException, FinderException {
		IWTimestamp start = new IWTimestamp(period);
		start.setAsDate();
		start.setDay(1);
		IWTimestamp end = new IWTimestamp(start);
		end.addMonths(1);
		IDOQuery sql = idoQuery();
		sql.append("select * from "+ ENTITY_NAME + " ph, " + SchoolBMPBean.SCHOOL + " s");
		sql.appendWhere("ph." + COLUMN_PERIOD).appendGreaterThanOrEqualsSign().append(start.getDate());
		sql.appendAnd().append("ph." + COLUMN_PERIOD).appendLessThanSign().append(end.getDate());
		sql.appendAndEqualsQuoted("ph." + COLUMN_SCHOOL_CATEGORY_ID, sc);
		sql.appendAndEquals("ph." + COLUMN_SCHOOL_ID, "s.sch_school_id");
		sql.appendOrderBy("s.school_name");
		return idoFindPKsBySQL(sql.toString());
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
	
	public Collection ejbFindByMonthAndSchoolCategory(CalendarMonth month, SchoolCategory schoolCategory) throws FinderException {
		IDOQuery query = idoQueryFindByMonth(month);
		query.appendAndEqualsQuoted(COLUMN_SCHOOL_CATEGORY_ID, (String)schoolCategory.getPrimaryKey());
		return idoFindPKsByQuery(query);
	}
}
