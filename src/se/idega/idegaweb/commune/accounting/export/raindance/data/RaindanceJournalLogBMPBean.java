package se.idega.idegaweb.commune.accounting.export.raindance.data;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolCategory;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;

public class RaindanceJournalLogBMPBean extends GenericEntity implements
		RaindanceJournalLog {

	private static final String ENTITY_NAME = "cacc_raindance_log";

	private static final String COLUMN_SCHOOL_CATEGORY = "school_category";

	private static final String COLUMN_LOCALIZED_EVENT_KEY = "loc_event_key";

	private static final String COLUMN_EVENT_DATE = "event_date";

	private static final String COLUMN_USER = "user_id";

	private static final String EVENT_CREATED_KEY = "cacc_export_create_file";

	private static final String EVENT_DELETED_KEY = "cacc_export_delete_file";

	private static final String EVENT_SENT_KEY = "cacc_export_send_file";

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
		addAttribute(COLUMN_LOCALIZED_EVENT_KEY,
				"Key to localized description of event", true, true,
				java.lang.String.class);
		addAttribute(COLUMN_EVENT_DATE, "The date of the logged event", true,
				true, java.sql.Timestamp.class);
		addManyToOneRelationship(COLUMN_USER, User.class);
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

	public void setEventFileCreated() {
		setColumn(COLUMN_LOCALIZED_EVENT_KEY, EVENT_CREATED_KEY);
	}

	public void setEventFileDeleted() {
		setColumn(COLUMN_LOCALIZED_EVENT_KEY, EVENT_DELETED_KEY);
	}

	public void setEventFileSent() {
		setColumn(COLUMN_LOCALIZED_EVENT_KEY, EVENT_SENT_KEY);
	}

	public void setLocalizedEventKey(String key) {
		setColumn(COLUMN_LOCALIZED_EVENT_KEY, key);
	}

	public String getEventFileCreated() {
		return EVENT_CREATED_KEY;
	}

	public String getEventFileDeleted() {
		return EVENT_DELETED_KEY;
	}

	public String getEventFileSent() {
		return EVENT_SENT_KEY;
	}

	public String getLocalizedEventKey() {
		return getStringColumnValue(COLUMN_LOCALIZED_EVENT_KEY);
	}

	public void setEventDate(Timestamp date) {
		setColumn(COLUMN_EVENT_DATE, date);
	}

	public Timestamp getEventDate() {
		return (Timestamp) getColumnValue(COLUMN_EVENT_DATE);
	}

	public void setUserId(int id) {
		setColumn(COLUMN_USER, id);
	}

	public void setUser(User user) {
		setColumn(COLUMN_USER, user);
	}

	public int getUserId() {
		return getIntColumnValue(COLUMN_USER);
	}

	public User getUser() {
		return (User) getColumnValue(COLUMN_USER);
	}

	public Collection ejbFindAll() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);

		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindAllBySchoolCategory(String category)
			throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEqualsWithSingleQuotes(COLUMN_SCHOOL_CATEGORY,
				category);

		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindAllBySchoolCategory(SchoolCategory category)
			throws FinderException {
		return ejbFindAllBySchoolCategory((String) category.getPrimaryKey());
	}
}