package is.idega.idegaweb.campus.data;

import com.idega.util.IWTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class SystemPropertiesBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.campus.data.SystemProperties {

	public SystemPropertiesBMPBean() {
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(getColumnNameContractYears(), "Contract Years", true, true, java.lang.Integer.class);
		addAttribute(getColumnNameContractDate(), "Contract Date", true, true, java.sql.Date.class);
		addAttribute(getColumnNameCypherKey(), "Cypher Key", true, true, java.lang.String.class, 4000);
		addAttribute(getColumnNameAdminEmail(), "Admin email", true, true, java.lang.String.class, 1000);
		addAttribute(getColumnNameEmailHost(), "email host", true, true, java.lang.String.class, 1000);
		addAttribute(getColumnNameDefaultGroup(), "default group", true, true, java.lang.Integer.class);
		addAttribute(getColumnNameTermOfNotice(), "term of notice", true, true, java.lang.Integer.class);
		addAttribute(getColumnNameTermOfNoticeMonths(), "term of notice months", true, true, java.lang.Integer.class);
	}

	public String getEntityName() {

		return getEntityTableName();

	}

	public static String getEntityTableName() {
		return "CAM_SYS_PROPS";
	}

	public static String getColumnNameContractYears() {
		return "CONTRACT_YEARS";
	}

	public static String getColumnNameContractDate() {
		return "CONTRACT_DATE";
	}

	public static String getColumnNameCypherKey() {
		return "CYPHERKEY";
	}

	public static String getColumnNameAdminEmail() {
		return "ADMIN_EMAIL";
	}

	public static String getColumnNameEmailHost() {
		return "EMAIL_HOST";
	}

	public static String getColumnNameDefaultGroup() {
		return "DEFAULT_GROUP";
	}

	public static String getColumnNameTermOfNotice() {
		return "TERM_OF_NOTICE";
	}

	public static String getColumnNameTermOfNoticeMonths() {
		return "MONTHS_NOTICE";
	}


	public void setContractYears(int years) {

		setColumn(getColumnNameContractYears(), years);

	}

	public int getContractYears() {

		return getIntColumnValue(getColumnNameContractYears());

	}

	public void setContractDate(java.sql.Date date) {

		setColumn(getColumnNameContractDate(), date);

	}

	public java.sql.Date getContractDate() {

		return ((java.sql.Date) getColumnValue(getColumnNameContractDate()));

	}

	public void setCypherKey(String key) {

		setColumn(getColumnNameCypherKey(), key);

	}

	public String getCypherKey() {

		return getStringColumnValue(getColumnNameCypherKey());

	}

	public void setAdminEmail(String email) {

		setColumn(getColumnNameAdminEmail(), email);

	}

	public String getAdminEmail() {

		return getStringColumnValue(getColumnNameAdminEmail());

	}

	public void setEmailHost(String host) {

		setColumn(getColumnNameEmailHost(), host);

	}

	public int getDefaultGroup() {

		return getIntColumnValue(getColumnNameDefaultGroup());

	}

	public void setDefaultGroup(int host) {

		setColumn(getColumnNameDefaultGroup(), host);

	}

	public String getEmailHost() {

		return getStringColumnValue(getColumnNameEmailHost());

	}

	public void setTermOfNotice(long term) {

		setColumn(getColumnNameTermOfNotice(), (int) term);

	}

	public long getTermOfNotice() {

		return (long) getIntColumnValue(getColumnNameTermOfNotice());

	}

	public long getTermOfNoticeDays() {

		return getTermOfNotice();

	}

	public void setTermOfNoticeMonths(long term) {

		setColumn(getColumnNameTermOfNoticeMonths(), (int) term);

	}

	public long getTermOfNoticeMonths() {

		return (long) getIntColumnValue(getColumnNameTermOfNoticeMonths());

	}


	public void insert() throws java.sql.SQLException {

	}

	public void delete() throws java.sql.SQLException {

	}

	public java.sql.Date getValidToDate() {

		int years = this.getContractYears();

		if (this.getContractYears() > 0) {

			IWTimestamp now = IWTimestamp.RightNow();

			IWTimestamp iT = new IWTimestamp(1, now.getMonth(), now.getYear() + years);

			return iT.getSQLDate();

		}

		else
			return this.getContractDate();

	}

}
