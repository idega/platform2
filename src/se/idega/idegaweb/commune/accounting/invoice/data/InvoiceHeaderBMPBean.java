package se.idega.idegaweb.commune.accounting.invoice.data;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLegacyEntity;
import com.idega.user.data.*;
/**
 * @author Joakim
 *
 */
public class InvoiceHeaderBMPBean extends GenericEntity implements InvoiceHeader, IDOLegacyEntity {
	private static final String ENTITY_NAME = "cacc_invoice_header";

	private static final String COLUMN_MAIN_ACTIVITY = "main_activity";
	private static final String COLUMN_PERIOD = "period";
	private static final String COLUMN_USER = "user";
	private static final String COLUMN_REFERENCE = "reference";
	private static final String COLUMN_STATUS = "status";
	private static final String COLUMN_DATE_CREATED = "date_created";
	private static final String COLUMN_DATE_ADJUSTED = "date_adjusted";
	private static final String COLUMN_DATE_JOURNAL_ENTRY = "journal_entry";
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
		addAttribute(COLUMN_MAIN_ACTIVITY, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_PERIOD, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_USER, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_REFERENCE, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_STATUS, "", true, true, java.lang.String.class, 1);
		addAttribute(COLUMN_DATE_CREATED, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_DATE_ADJUSTED, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_DATE_JOURNAL_ENTRY, "", true, true, java.lang.String.class, 1000);

		addManyToOneRelationship(COLUMN_USER, User.class);
	}
	public String getMainActivity() {
		return getStringColumnValue(COLUMN_MAIN_ACTIVITY);
	}
	public String getPeriod() {
		return getStringColumnValue(COLUMN_PERIOD);
	}
	public String getUser() {
		return getStringColumnValue(COLUMN_USER);
	}
	public String getReference() {
		return getStringColumnValue(COLUMN_REFERENCE);
	}
	public String getStatus() {
		return getStringColumnValue(COLUMN_STATUS);
	}
	public String getDateCreated() {
		return getStringColumnValue(COLUMN_DATE_CREATED);
	}
	public String getDateAdjusted() {
		return getStringColumnValue(COLUMN_DATE_ADJUSTED);
	}
	public String getDateJournalEntry() {
		return getStringColumnValue(COLUMN_DATE_JOURNAL_ENTRY);
	}


	public void setMainActivity(String s) {
		setColumn(COLUMN_MAIN_ACTIVITY, s);
	}
	public void setPeriod(String s) {
		setColumn(COLUMN_PERIOD, s);
	}
	public void setUser(String s) {
		setColumn(COLUMN_USER, s);
	}
	public void setReference(String s) {
		setColumn(COLUMN_REFERENCE, s);
	}
	public void setStatus(String s) {
		setColumn(COLUMN_STATUS, s);
	}
	public void setDateCreated(String s) {
		setColumn(COLUMN_DATE_CREATED, s);
	}
	public void setDateAdjusted(String s) {
		setColumn(COLUMN_DATE_ADJUSTED, s);
	}
	public void setDateJournalEntry(String s) {
		setColumn(COLUMN_DATE_JOURNAL_ENTRY, s);
	}

}

