package se.idega.idegaweb.commune.accounting.export.raindance.data;

import java.sql.Timestamp;

import com.idega.block.school.data.SchoolCategory;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

public class RaindanceCheckHeaderBMPBean extends GenericEntity implements
		RaindanceCheckHeader {

	private static final String ENTITY_NAME = "cacc_raindance_check_header";

	private static final String COLUMN_SCHOOL_CATEGORY = "school_category";
	private static final String COLUMN_STATUS = "status";
	private static final String COLUMN_EVENT_DATE = "event_date";
	private static final String COLUMN_EVENT_START_TIME = "start_time";
	private static final String COLUMN_EVENT_END_TIME = "end_time";

	private static final String EVENT_CREATED_KEY = "cacc_raindance_check_create_file";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_SCHOOL_CATEGORY, SchoolCategory.class);
		addAttribute(COLUMN_STATUS, "Key to localized status", true, true, java.lang.String.class);
		addAttribute(COLUMN_EVENT_DATE, "The date of the logged event", true, true, java.sql.Timestamp.class);
		addAttribute(COLUMN_EVENT_START_TIME, "The start time of the logged event", true, true, java.sql.Timestamp.class);
		addAttribute(COLUMN_EVENT_END_TIME, "The end time of the logged event", true, true, java.sql.Timestamp.class);
	}

	public void setSchoolCategoryString(String category) {
		setColumn(COLUMN_SCHOOL_CATEGORY, category);
	}

	public void setSchoolCategory(SchoolCategory category) {
		setColumn(COLUMN_SCHOOL_CATEGORY, category);
	}

	public String getSchoolCategoryString() {
		return getStringColumnValue(COLUMN_SCHOOL_CATEGORY);
	}

	public SchoolCategory getSchoolCategory() {
		return (SchoolCategory) getColumnValue(COLUMN_SCHOOL_CATEGORY);
	}

	public void setStatusFileCreated() {
		setColumn(COLUMN_STATUS, EVENT_CREATED_KEY);
	}

	public void setStatus(String key) {
		setColumn(COLUMN_STATUS, key);
	}

	public String getStatusFileCreated() {
		return EVENT_CREATED_KEY;
	}

	public String getStatus() {
		return getStringColumnValue(COLUMN_STATUS);
	}

	public void setEventDate(Timestamp date) {
		setColumn(COLUMN_EVENT_DATE, date);
	}

	public Timestamp getEventDate() {
		return (Timestamp) getColumnValue(COLUMN_EVENT_DATE);
	}

	public void setEventStartTime(Timestamp time) {
		setColumn(COLUMN_EVENT_START_TIME, time);
	}

	public Timestamp getEventStartTime() {
		return (Timestamp) getColumnValue(COLUMN_EVENT_START_TIME);
	}

	public void setEventEndTime(Timestamp time) {
		setColumn(COLUMN_EVENT_END_TIME, time);
	}

	public Timestamp getEventEndTime() {
		return (Timestamp) getColumnValue(COLUMN_EVENT_END_TIME);
	}

	public Integer ejbFindBySchoolCategory(SchoolCategory schoolCategory) throws javax.ejb.FinderException {
		return ejbFindBySchoolCategory((String) schoolCategory.getPrimaryKey());
	}

	public Integer ejbFindBySchoolCategory(String schoolCategory) throws javax.ejb.FinderException {
		IDOQuery query = this.idoQueryGetSelect();
		query.appendWhereEqualsQuoted(COLUMN_SCHOOL_CATEGORY, schoolCategory);
		return (Integer) idoFindOnePKByQuery(query);
	}
}