package se.idega.idegaweb.commune.accounting.invoice.data;
import java.sql.Date;
import java.sql.Timestamp;

import com.idega.block.school.data.SchoolCategory;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
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
		
		addManyToOneRelationship(COLUMN_SCHOOL_CATEGORY_ID, SchoolCategory.class);
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
	
	
	/**
	 *	Finds one Batchrun from a schoolCategory. There should be max one schoolcategory
	 *	@throws javax.ejb.FinderException if no SchoolType is found.	
	 */
	public Integer ejbFindBySchoolCategory(SchoolCategory schoolCategory) throws javax.ejb.FinderException {
		IDOQuery query = this.idoQueryGetSelect();
		query.appendWhereEqualsQuoted(COLUMN_SCHOOL_CATEGORY_ID, (String)schoolCategory.getPrimaryKey());
		return (Integer) idoFindOnePKByQuery(query);
	}
}
