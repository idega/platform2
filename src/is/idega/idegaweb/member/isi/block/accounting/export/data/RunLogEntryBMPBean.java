package is.idega.idegaweb.member.isi.block.accounting.export.data;

import java.sql.Timestamp;

import com.idega.data.GenericEntity;

public class RunLogEntryBMPBean extends GenericEntity implements RunLogEntry {

	protected final static String ENTITY_NAME = "isi_run_log_entry";
	
	protected final static String COLUMN_RUN_LOG = "isi_run_log_id";

	protected final static String COLUMN_DATE_OF_ENTRY = "date_of_entry";
	
	protected final static String COLUMN_ENTRY = "entry";
	
	protected final static String COLUMN_IS_ERROR = "is_error";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_RUN_LOG, RunLog.class);
		addAttribute(COLUMN_DATE_OF_ENTRY, "Date of entry", Timestamp.class);
		addAttribute(COLUMN_ENTRY, "Entry", String.class, 512);
		addAttribute(COLUMN_IS_ERROR, "Is error", Boolean.class);
	}

	//getters
	public int getRunLogID() {
		return getIntColumnValue(COLUMN_RUN_LOG);
	}
	
	public RunLog getRunLog() {
		return (RunLog) getColumnValue(COLUMN_RUN_LOG);
	}
	
	public Timestamp getDateOfEntry() {
		return getTimestampColumnValue(COLUMN_DATE_OF_ENTRY);
	}
	
	public String getEntry() {
		return getStringColumnValue(COLUMN_ENTRY);
	}
	
	public boolean getIsError() {
		return getBooleanColumnValue(COLUMN_IS_ERROR, false);
	}
	
	//setters
	public void setRunLogID(int id) {
		setColumn(COLUMN_RUN_LOG, id);
	}
	
	public void setRunLog(RunLog log) {
		setColumn(COLUMN_RUN_LOG, log);
	}
	
	public void setDateOfEntry(Timestamp dateOfEntry) {
		setColumn(COLUMN_DATE_OF_ENTRY, dateOfEntry);
	}
	
	public void setEntry(String entry) {
		setColumn(COLUMN_ENTRY, entry);
	}
	
	public void setIsError(boolean isError) {
		setColumn(COLUMN_IS_ERROR, isError);
	}
}