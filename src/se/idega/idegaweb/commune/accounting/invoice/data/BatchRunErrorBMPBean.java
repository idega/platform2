package se.idega.idegaweb.commune.accounting.invoice.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;

/**
 * Holds information about each possible error that might have occured during the batchrun
 * See fonster 33 C&P req.Spec.
 *  
 * @author Joakim
 * 
 * @see BatchRun
 */
public class BatchRunErrorBMPBean extends GenericEntity implements BatchRunError{
	private static final String ENTITY_NAME = "cacc_batch_run_error";

	private static final String COLUMN_BATCH_RUN_ID = "batch_run_id";
	private static final String COLUMN_ORDER = "order_nr";
	private static final String COLUMN_RELATED = "related";
	private static final String COLUMN_DESCRIPTION = "description";

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
		addAttribute(COLUMN_BATCH_RUN_ID, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_ORDER, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_RELATED, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_DESCRIPTION, "", true, true, java.lang.String.class, 1000);

//		addManyToOneRelationship(COLUMN_BATCH_RUN_ID, BatchRun.class);
	}
	public int getBatchRunID() {
		return getIntColumnValue(COLUMN_BATCH_RUN_ID);
	}
	public int getOrder() {
		return getIntColumnValue(COLUMN_ORDER);
	}
	public String getRelated() {
		return getStringColumnValue(COLUMN_RELATED);
	}
	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);
	}


	public void setBatchRunID(int i) {
		setColumn(COLUMN_BATCH_RUN_ID, i);
	}
	public void setOrder(int i) {
		setColumn(COLUMN_ORDER, i);
	}
	public void setRelated(String s) {
		setColumn(COLUMN_RELATED, s);
	}
	public void setDescription(String s) {
		setColumn(COLUMN_DESCRIPTION, s);
	}

	public Collection ejbFindAllOrdered() throws FinderException {
		return idoFindAllIDsOrderedBySQL(COLUMN_ORDER);
	}
}

