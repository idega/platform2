package se.idega.idegaweb.commune.accounting.invoice.data;
import java.sql.Date;
import java.sql.Timestamp;

import com.idega.block.school.data.SchoolCategory;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.util.CalendarMonth;
/**
 * Holds information about a batchrun for an invoice build (Used in fonster 33 in the C&P req. spec.)
 * Related to a set of rows in BatchRunError.
 * 
 * @author Joakim
 * @see se.idega.idegaweb.commune.accounting.invoice.data.BatchRunErrorBMPBean
 */
public class BatchRunBMPBean extends GenericEntity implements BatchRun {
	private static final String ENTITY_NAME = "cacc_batch_run";
	private static final String COLUMN_SCHOOL_CATEGORY_ID = "school_category_id";
	private static final String COLUMN_PERIOD = "period";
	private static final String COLUMN_START = "start_date";
	private static final String COLUMN_STOP = "stop_date";
	private static final String COLUMN_TEST = "test";
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
		addAttribute(COLUMN_PERIOD, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_START, "", true, true, java.sql.Timestamp.class);
		addAttribute(COLUMN_STOP, "", true, true, java.sql.Timestamp.class);
		addAttribute(COLUMN_TEST, "", true, true, java.lang.Boolean.class);
	
		
		addManyToOneRelationship(COLUMN_SCHOOL_CATEGORY_ID, SchoolCategory.class);
	}
	
	protected void setDefaultvalues(){
		setTest(false);
	}
	
	public String getSchoolCategoryID() {
		return getStringColumnValue(COLUMN_SCHOOL_CATEGORY_ID);
	}
	public Date getPeriod() {
		return getDateColumnValue(COLUMN_PERIOD);
	}
	public Timestamp getStart() {
		return getTimestampColumnValue(COLUMN_START);
	}
	public Timestamp getEnd() {
		return getTimestampColumnValue(COLUMN_STOP);
	}
	public boolean getTest() {
		return getBooleanColumnValue(COLUMN_TEST);
	}	
	
	public void setSchoolCategoryID(String i) {
		setColumn(COLUMN_SCHOOL_CATEGORY_ID, i);
	}
	public void setSchoolCategoryID(SchoolCategory s) {
		setColumn(COLUMN_SCHOOL_CATEGORY_ID, s);
	}
	public void setPeriod(Date d) {
		setColumn(COLUMN_PERIOD, d);
	}
	public void setStart(Timestamp d) {
		setColumn(COLUMN_START, d);
	}
	public void setEnd(Timestamp d) {
		setColumn(COLUMN_STOP, d);
	}
	public void setTest(boolean test) {
		setColumn(COLUMN_TEST, test);
	}
		
	
	/**
	 * Gets the CalendarMonth for the Period
	 * @return
	 */
	public CalendarMonth getMonth(){
		return new CalendarMonth(getPeriod());
	}
	
	/**
	 *	Finds one Batchrun from a schoolCategory. There should be max one schoolcategory
	 *	@throws javax.ejb.FinderException if no SchoolType is found.	
	 */
	public Integer ejbFindBySchoolCategory(SchoolCategory schoolCategory, boolean test) throws javax.ejb.FinderException {
		IDOQuery query = this.idoQueryGetSelect();
		query.appendWhereEqualsQuoted(COLUMN_SCHOOL_CATEGORY_ID, (String)schoolCategory.getPrimaryKey());
		query.appendAndEquals(COLUMN_TEST, test);
		return (Integer) idoFindOnePKByQuery(query);
	}
}
