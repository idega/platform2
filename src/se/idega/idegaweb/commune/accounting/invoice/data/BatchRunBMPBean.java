package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;

import com.idega.block.school.data.SchoolType;
import com.idega.data.GenericEntity;

/**
 * Holds information about a batchrun for an invoice build (Used in fonster 33 in the C&P req. spec.)
 * Related to a set of rows in BatchRunError.
 * 
 * @author Joakim
 * @see se.idega.idegaweb.commune.accounting.invoice.data.BatchRunErrorBMPBean
 */
public class BatchRunBMPBean extends GenericEntity implements BatchRun{
	
	private static final String ENTITY_NAME = "cacc_batch_run";

	private static final String COLUMN_OPERATION = "operation";
	private static final String COLUMN_PERIOD = "period";
	private static final String COLUMN_START = "start";
	private static final String COLUMN_STOP = "stop";

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
		addAttribute(COLUMN_OPERATION, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_PERIOD, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_START, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_STOP, "", true, true, java.sql.Date.class);

		addManyToOneRelationship(COLUMN_OPERATION, SchoolType.class);
	}
	
	public int getOperationID() {
		return getIntColumnValue(COLUMN_OPERATION);
	}
	public int getOrder() {
		return getIntColumnValue(COLUMN_PERIOD);
	}
	public Date getStart() {
		return getDateColumnValue(COLUMN_START);
	}
	public Date getEnd() {
		return getDateColumnValue(COLUMN_STOP);
	}


	public void setOperationID(int i) {
		setColumn(COLUMN_OPERATION, i);
	}
	public void setOrder(int i) {
		setColumn(COLUMN_PERIOD, i);
	}
	public void setStart(Date d) {
		setColumn(COLUMN_START, d);
	}
	public void setEnd(Date d) {
		setColumn(COLUMN_STOP, d);
	}
}
