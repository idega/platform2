/*
 * Created on 1.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.util.Date;

import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.school.data.Provider;



import com.idega.block.school.data.School;
import com.idega.data.GenericEntity;
import com.idega.user.data.User;


/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class RegularInvoiceEntryBMPBean extends GenericEntity implements RegularInvoiceEntry {


	private static final String COLUMN_OWN_POSTING = "own_posting";
	private static final String COLUMN_DOUBLE_POSTING = "double_posting";
	private static final String COLUMN_NOTE = "note";
	private static final String COLUMN_VAT = "vat";
	private static final String COLUMN_VAT_REG_ID = "vat_reg_id";
	private static final String COLUMN_AMOUNT = "amount";
	private static final String COLUMN_SCHOOL_ID = "school_id";
	private static final String COLUMN_REG_SPEC_TYPE_ID = "reg_spec_type";
	private static final String COLUMN_USER_ID = "user_id";
	private static final String COLUMN_PLACING = "placing";
	private static final String COLUMN_TO = "to";
	private static final String COLUMN_FROM = "from";
	
	private static final String ENTITY_NAME = "cacc_regular_invoice_entry";
	
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_OWN_POSTING, "", true, true, java.lang.String.class);
		addAttribute(COLUMN_DOUBLE_POSTING, "", true, true, java.lang.String.class);
		addAttribute(COLUMN_NOTE, "", true, true, java.lang.String.class);
		addAttribute(COLUMN_VAT, "", true, true, java.lang.Float.class);
		addAttribute(COLUMN_VAT_REG_ID, "", true, true, java.lang.Integer.class, 1);
		addAttribute(COLUMN_AMOUNT, "", true, true, java.lang.Float.class);
		addAttribute(COLUMN_SCHOOL_ID, "", true, true, java.lang.Integer.class, 1000);
		addAttribute(COLUMN_REG_SPEC_TYPE_ID, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_USER_ID, "", true, true, java.lang.Integer.class, 1000);
		addAttribute(COLUMN_PLACING, "", true, true, java.lang.String.class);
		addAttribute(COLUMN_TO, "", true, true, java.sql.Date.class, 1000);
		addAttribute(COLUMN_FROM, "", true, true, java.sql.Date.class);

		addManyToOneRelationship(COLUMN_USER_ID, User.class);
		addManyToOneRelationship(COLUMN_SCHOOL_ID, Provider.class);
		addManyToOneRelationship(COLUMN_REG_SPEC_TYPE_ID, RegulationSpecType.class);				
	}
	

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}
			

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getFrom()
	 */
	public Date getFrom() {
		return getDateColumnValue(COLUMN_FROM);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getTo()
	 */
	public Date getTo() {
		return getDateColumnValue(COLUMN_TO);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getPlacing()
	 */
	public String getPlacing() {
		return getStringColumnValue(COLUMN_PLACING);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getUser()
	 */
	public User getUser() {
		return (User) getColumnValue(COLUMN_USER_ID);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getRegSpecType()
	 */
	public RegulationSpecType getRegSpecType() {
		return (RegulationSpecType) getColumnValue(COLUMN_REG_SPEC_TYPE_ID);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getProvider()
	 */
	public School getProvider() {
		return (School) getColumnValue(COLUMN_SCHOOL_ID);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getDoublePostin()
	 */
	public String getDoublePosting() {
		return getStringColumnValue(COLUMN_DOUBLE_POSTING);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getAmount()
	 */
	public float getAmount() {
		return getFloatColumnValue(COLUMN_AMOUNT);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getVAT()
	 */
	public float getVAT() {
		return getFloatColumnValue(COLUMN_VAT);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getVatRegulationID()
	 */
	public int getVatRegulationID() {
		return getIntColumnValue(COLUMN_VAT_REG_ID);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getNote()
	 */
	public String getNote() {
		return getStringColumnValue(COLUMN_NOTE);
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getOwnPosting()
	 */
	public String getOwnPosting() {
		return getStringColumnValue(COLUMN_OWN_POSTING);
	}
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#setFrom(java.util.Date)
	 */
	public void setFrom(Date from) {
		setColumn(COLUMN_FROM, from);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#setTo(java.util.Date)
	 */
	public void setTo(Date to) {
		setColumn(COLUMN_TO, to);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#setPlacing(java.lang.String)
	 */
	public void setPlacing(String plascing) {
		setColumn(COLUMN_PLACING, plascing);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#setAmount(float)
	 */
	public void setAmount(float amount) {
		setColumn(COLUMN_AMOUNT, amount);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#setNote(java.lang.String)
	 */
	public void setNote(String note) {
		setColumn(COLUMN_NOTE, note);
	}



}
