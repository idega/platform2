package se.idega.idegaweb.commune.accounting.invoice.data;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLegacyEntity;
import com.idega.user.data.*;

/**
 * @author Joakim
 *
 */
public class InvoiceRecordBMPBean extends GenericEntity implements InvoiceRecord, IDOLegacyEntity {
	private static final String ENTITY_NAME = "cacc_invoice_record";

	private static final String COLUMN_INVOICE_HEADER = "invoice_header";
	private static final String COLUMN_USER_ID = "user_id";
	private static final String COLUMN_TEXT = "text";
	private static final String COLUMN_DAYS = "days";
	private static final String COLUMN_SUM = "sum";
	private static final String COLUMN_NOTES = "notes";
	private static final String COLUMN_ORDER_ID = "order_id";
	private static final String COLUMN_POSTING_DETAILS = "posting_details";
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
		addAttribute(COLUMN_INVOICE_HEADER, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_USER_ID, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_TEXT, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_DAYS, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_SUM, "", true, true, java.lang.Float.class);
		addAttribute(COLUMN_NOTES, "", true, true, java.lang.String.class, 1000);
		addAttribute(COLUMN_ORDER_ID, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_POSTING_DETAILS, "", true, true, java.lang.String.class, 1000);

		addManyToOneRelationship(COLUMN_INVOICE_HEADER, InvoiceHeader.class);
		addManyToOneRelationship(COLUMN_USER_ID, User.class);
	}
	public int getInvoiceheader() {
		return getIntColumnValue(COLUMN_INVOICE_HEADER);
	}
	public int getUserId() {
		return getIntColumnValue(COLUMN_USER_ID);
	}
	public String getText() {
		return getStringColumnValue(COLUMN_TEXT);
	}
	public int getDays() {
		return getIntColumnValue(COLUMN_DAYS);
	}
	public float getSum() {
		return getFloatColumnValue(COLUMN_SUM);
	}
	public String getNotes() {
		return getStringColumnValue(COLUMN_NOTES);
	}
	public int getOrderId() {
		return getIntColumnValue(COLUMN_ORDER_ID);
	}
	public String getPostingDetails() {
		return getStringColumnValue(COLUMN_POSTING_DETAILS);
	}


	public void setInvoiceHeader(int i) {
		setColumn(COLUMN_INVOICE_HEADER, i);
	}
	public void setUserId(int i) {
		setColumn(COLUMN_USER_ID, i);
	}
	public void setText(String s) {
		setColumn(COLUMN_TEXT, s);
	}
	public void setDays(int i) {
		setColumn(COLUMN_DAYS, i);
	}
	public void setSum(float f) {
		setColumn(COLUMN_SUM, f);
	}
	public void setNotes(String s) {
		setColumn(COLUMN_NOTES, s);
	}
	public void setOrderId(int i) {
		setColumn(COLUMN_ORDER_ID, i);
	}
	public void setPostingDetails(String s) {
		setColumn(COLUMN_POSTING_DETAILS, s);
	}
}
