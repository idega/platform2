/*
 * Created on Nov 3, 2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package se.agura.applications.vacation.data;

import java.sql.Date;
import java.util.Iterator;
import java.util.Map;
import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.user.data.User;

/**
 * @author Anna
 */
public class VacationRequestBMPBean extends AbstractCaseBMPBean implements VacationRequest {

	public String getCaseCodeKey() {
		return "VACATIN";// ONLY SEVEN LETTERS ALLOWED HERE!
	}

	public String getCaseCodeDescription() {
		return "Vacation requests";
	}

	public static final String ENTITY_NAME = "vac_vacation_request";

	public static final String COLUMN_VACATION_REQUEST_ID = "vacation_request_id";

	public static final String COLUMN_VACATION_TYPE = "vacation_type";

	public static final String COLUMN_FROM_DATE = "from_date";

	public static final String COLUMN_TO_DATE = "to_date";

	public static final String COLUMN_ORDINARY_WORKING_HOURS = "ordinary_working_hours";

	public static final String COLUMN_CREATED_DATE = "created_date";

	public static final String COLUMN_IC_USER_ID = "ic_user_id";

	public static final String COLUMN_GRANTED_DATE = "granted_date";

	public static final String COLUMN_REJECTED_DATE = "rejected_date";

	public static final String COLUMN_DECISION_BY = "decision_by";

	public static final String COLUMN_COMMENT = "notes";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(COLUMN_VACATION_REQUEST_ID);
		setAsPrimaryKey(COLUMN_VACATION_REQUEST_ID, true);
		addAttribute(COLUMN_FROM_DATE, "Date from", Date.class);
		addAttribute(COLUMN_TO_DATE, "Date to", Date.class);
		addAttribute(COLUMN_ORDINARY_WORKING_HOURS, "Ordinal working hours", Integer.class);
		addAttribute(COLUMN_CREATED_DATE, "Date", Date.class);
		addManyToOneRelationship(COLUMN_IC_USER_ID, User.class);
		addAttribute(COLUMN_GRANTED_DATE, "Date", Date.class);
		addAttribute(COLUMN_REJECTED_DATE, "Date", Date.class);
		addManyToOneRelationship(COLUMN_DECISION_BY, User.class);
		addAttribute(COLUMN_COMMENT, "Comment", String.class);
		addManyToOneRelationship(COLUMN_VACATION_TYPE, VacationType.class);
		addMetaDataRelationship();
	}

	// /////////////////////////////////////////////////
	// getters
	// /////////////////////////////////////////////////
	public Date getFromDate() {
		return (Date) getColumnValue(COLUMN_FROM_DATE);
	}

	public Date getToDate() {
		return (Date) getColumnValue(COLUMN_TO_DATE);
	}

	public int getOrdinaryWorkingHours() {
		return getIntColumnValue(COLUMN_ORDINARY_WORKING_HOURS);
	}

	public VacationType getVacationType() {
		return (VacationType) getColumnValue(COLUMN_VACATION_TYPE);
	}

	public Date getCreatedDate() {
		return (Date) getColumnValue(COLUMN_CREATED_DATE);
	}

	public User getUser() {
		return (User) getColumnValue(COLUMN_IC_USER_ID);
	}

	public Date getGrantedDate() {
		return (Date) getColumnValue(COLUMN_GRANTED_DATE);
	}

	public Date getRejectedDate() {
		return (Date) getColumnValue(COLUMN_REJECTED_DATE);
	}

	public User getDecisionBy() {
		return (User) getColumnValue(COLUMN_DECISION_BY);
	}

	public String getComment() {
		return getStringColumnValue(COLUMN_COMMENT);
	}

	// /////////////////////////////////////////////////
	// setters
	// //////////////////////////////////////////////// /
	public void setFromDate(Date fromDate) {
		setColumn(COLUMN_FROM_DATE, fromDate);
	}

	public void setToDate(Date toDate) {
		setColumn(COLUMN_TO_DATE, toDate);
	}

	public void setOrdinaryWorkingHours(Integer ordinaryWorkingHour) {
		setColumn(COLUMN_ORDINARY_WORKING_HOURS, ordinaryWorkingHour);
	}

	public void setVacationType(VacationType vacationType) {
		setColumn(COLUMN_VACATION_TYPE, vacationType);
	}

	public void setCreatedDate(Date createdDate) {
		setColumn(COLUMN_CREATED_DATE, createdDate);
	}

	public void setUser(User icUserId) {
		setColumn(COLUMN_IC_USER_ID, icUserId);
	}

	public void setGrantedDate(Date grantedDate) {
		setColumn(COLUMN_GRANTED_DATE, grantedDate);
	}

	public void setRejectedDate(Date rejectedDate) {
		setColumn(COLUMN_REJECTED_DATE, rejectedDate);
	}

	public void setDecisionBy(User decisionBy) {
		setColumn(COLUMN_DECISION_BY, decisionBy);
	}

	public void setComment(String comment) {
		setColumn(COLUMN_COMMENT, comment);
	}

	// Metadata methods
	public void setExtraTypeInformation(String key, String value, String type) {
		addMetaData(key, value, type);
	}

	public String getExtraTypeInformation(String key) {
		return getMetaData(key);
	}

	public String getExtraTypeInformationType(String key) {
		Map map = this.getMetaDataTypes();
		if (map != null) {
			return (String) map.get(key);
		}
		return null;
	}

	public void removeExtraTypeInformation(String key) {
		removeMetaData(key);
	}

	public void removeAllExtraTypeInformation() {
		Map attributes = this.getMetaDataAttributes();
		Iterator iter = attributes.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			removeExtraTypeInformation(key);
		}
	}
	/* (non-Javadoc)
	 * @see com.idega.block.process.data.AbstractCaseBMPBean#getCaseStatusDescriptions()
	 */
	public String[] getCaseStatusDescriptions() {
		String[] descriptions = { "Open", "Silent", "Ready", "Deleted", "Rejected", "Granted" };
		return descriptions;
	}
	/* (non-Javadoc)
	 * @see com.idega.block.process.data.AbstractCaseBMPBean#getCaseStatusKeys()
	 */
	public String[] getCaseStatusKeys() {
		String[] keys = { "UBEH", "TYST", "KLAR", "DELE", "AVSL", "BVJD" };
		return keys;
	}
}