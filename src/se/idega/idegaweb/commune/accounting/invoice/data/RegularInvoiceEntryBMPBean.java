/*
 * Created on 1.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.VATRule;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class RegularInvoiceEntryBMPBean extends GenericEntity implements RegularInvoiceEntry {

	private static final String COLUMN_PRIMARY_KEY = "cacc_reg_inv_entry_id";
	private static final String COLUMN_EDIT_NAME = "edit_name";
	private static final String COLUMN_EDIT_DATE = "edit_date";
	private static final String COLUMN_CREATED_NAME = "created_date";
	private static final String COLUMN_CREATED_DATE = "created_name";
	private static final String COLUMN_OWN_POSTING = "own_posting";
	private static final String COLUMN_DOUBLE_POSTING = "double_posting";
	private static final String COLUMN_NOTE = "note";
	private static final String COLUMN_VAT = "vat";
	private static final String COLUMN_VAT_RULE_ID = "vat_rule_id";
	private static final String COLUMN_AMOUNT = "amount";
	private static final String COLUMN_SCHOOL_ID = "school_id";
	private static final String COLUMN_REG_SPEC_TYPE_ID = "reg_spec_type";
	private static final String COLUMN_USER_ID = "user_id";
	private static final String COLUMN_PLACING = "placing";
	private static final String COLUMN_TO = "periode_to";
	private static final String COLUMN_FROM = "periode_from";
	private static final String COLUMN_SCHOOL_CATEGORY_ID = "school_category_id";
	
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
//		addAttribute(COLUMN_VAT_RULE_ID, "", true, true, java.lang.Integer.class, 1);
		addAttribute(COLUMN_AMOUNT, "", true, true, java.lang.Float.class);
//		addAttribute(COLUMN_SCHOOL_ID, "", true, true, java.lang.Integer.class);
//		addAttribute(COLUMN_REG_SPEC_TYPE_ID, "", true, true, java.lang.Integer.class);
//		addAttribute(COLUMN_USER_ID, "", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_PLACING, "", true, true, java.lang.String.class);
		addAttribute(COLUMN_TO, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_FROM, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_EDIT_NAME, "", true, true, java.lang.String.class);
		addAttribute(COLUMN_EDIT_DATE, "", true, true, java.sql.Date.class);
		addAttribute(COLUMN_CREATED_NAME, "", true, true, java.lang.String.class);
		addAttribute(COLUMN_CREATED_DATE, "", true, true, java.sql.Date.class);
//		addAttribute(COLUMN_SCHOOL_CATEGORY_ID, "", true, true, java.lang.String.class);

		addManyToOneRelationship(COLUMN_USER_ID, User.class);
		addManyToOneRelationship(COLUMN_SCHOOL_ID, School.class);
		addManyToOneRelationship(COLUMN_REG_SPEC_TYPE_ID, RegulationSpecType.class);	
		addManyToOneRelationship(COLUMN_SCHOOL_CATEGORY_ID, SchoolCategory.class);			
		addManyToOneRelationship(COLUMN_VAT_RULE_ID, VATRule.class);			
	}
	

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}
			
	public String getIDColumnName(){
		return COLUMN_PRIMARY_KEY;
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
	public User getChild() {
		return (User) getColumnValue(COLUMN_USER_ID);
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getUser()
	 */
	public int getChildId() {
		return getIntColumnValue(COLUMN_USER_ID);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getRegSpecType()
	 */
	public RegulationSpecType getRegSpecType() {
		return (RegulationSpecType) getColumnValue(COLUMN_REG_SPEC_TYPE_ID);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getRegSpecType()
	 */
	public int getRegSpecTypeId() {
		return getIntColumnValue(COLUMN_REG_SPEC_TYPE_ID);
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getSchool()
	 */
	public School getSchool() {
		try{
			return (School) IDOLookup.findByPrimaryKey(School.class, getSchoolId());
		}catch( FinderException ex){
			return null;
		}catch( IDOLookupException ex){
			return null;
		}		
	}
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getSchoolId()
	 */
	public int getSchoolId() {
		return getIntColumnValue(COLUMN_SCHOOL_ID);
	}	
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getSchoolId()
	 */
	public String getSchoolCategoryId() {
		return getStringColumnValue(COLUMN_SCHOOL_CATEGORY_ID);
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
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getVatRegulation()
	 */
	public VATRule getVatRule() {
		return (VATRule) getColumnValue(COLUMN_VAT_RULE_ID);
	}
	

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getVatRegulationID()
	 */
	public int getVatRuleId() {
		return getIntColumnValue(COLUMN_VAT_RULE_ID);
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
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getCreatedDate()
	 */
	public Date getCreatedDate() {
		return getDateColumnValue(COLUMN_CREATED_DATE);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getCreatedName()
	 */
	public String getCreatedName() {
		return getStringColumnValue(COLUMN_CREATED_NAME);	
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getEditDate()
	 */
	public Date getEditDate() {
		return getDateColumnValue(COLUMN_EDIT_DATE);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getEditdName()
	 */
	public String getEditName() {
		return getStringColumnValue(COLUMN_EDIT_NAME);	
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


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#setUser(com.idega.user.data.User)
	 */
	public void setChild(User user) {
		setColumn(COLUMN_USER_ID, user.getPrimaryKey());
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#setRegSpecType(se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType)
	 */
	public void setRegSpecType(RegulationSpecType regType) {
		setColumn(COLUMN_REG_SPEC_TYPE_ID, regType);
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#setRegSpecTypeId(int)
	 */
	public void setRegSpecTypeId(int regTypeId){
		setColumn(COLUMN_REG_SPEC_TYPE_ID, regTypeId);		
	}
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#setProvider(com.idega.block.school.data.School)
	 */
	public void setSchoolId(int schoolId) {
		setColumn(COLUMN_SCHOOL_ID, schoolId);
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#getSchoolId()
	 */
	public void setSchoolCategoryId(String schoolCategoryId) {
		setColumn(COLUMN_SCHOOL_CATEGORY_ID, schoolCategoryId);
	}
	

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#setVAT(float)
	 */
	public void setVAT(float vat) {
		setColumn(COLUMN_VAT, vat);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#setVatRegulation(se.idega.idegaweb.commune.accounting.regulations.data.VATRegulation)
	 */
	public void setVatRule(VATRule vatRule) {
		setColumn(COLUMN_VAT_RULE_ID, vatRule);
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#setVatRegulationId(int)
	 */
	public void setVatRuleId(int vatRegId){
		setColumn(COLUMN_VAT_RULE_ID, vatRegId);		
	}
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#setOwnPosting(java.lang.String)
	 */
	public void setOwnPosting(String ownPosting) {
		setColumn(COLUMN_OWN_POSTING, ownPosting);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#setDoublePosting(java.lang.String)
	 */
	public void setDoublePosting(String doublePosting) {
		setColumn(COLUMN_DOUBLE_POSTING, doublePosting);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#setCreatedDate(java.sql.Date)
	 */
	public void setCreatedDate(Date date) {
		setColumn(COLUMN_CREATED_DATE, date);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#setCreatedSign(java.lang.String)
	 */
	public void setCreatedSign(String name) {
		setColumn(COLUMN_CREATED_NAME, name);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#setEditDate(java.sql.Date)
	 */
	public void setEditDate(Date date) {
		setColumn(COLUMN_EDIT_DATE, date);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry#setEditSign(java.lang.String)
	 */
	public void setEditSign(String name) {
		setColumn(COLUMN_EDIT_NAME, name);
	}

	public Collection ejbFindRegularInvoicesForPeriodAndChildAndCategoryExceptType(Date from, Date to, int childUserId, String schoolCategoryId,  int regulationSpecTypeId) throws FinderException {
		
				IDOQuery query = idoQuery() 
				.appendSelectAllFrom(this)
				.appendWhereEquals(COLUMN_USER_ID, childUserId)
				.appendAndEqualsQuoted(COLUMN_SCHOOL_CATEGORY_ID, schoolCategoryId)
				.appendAnd()
				.append(COLUMN_FROM)
				.appendLessThanOrEqualsSign()
				.append(to)
				.appendAnd()
				.appendLeftParenthesis()
				.append(COLUMN_TO)
				.appendGreaterThanOrEqualsSign()
				.append(from)
//		.appendOr()
//		.append(COLUMN_TO)
//		.appendIsNull()
				.appendRightParenthesis()
				.appendAnd()
				.append(COLUMN_REG_SPEC_TYPE_ID)
				.appendNOTEqual()
				.append(regulationSpecTypeId);

				return idoFindPKsByQuery(query);
	}	
	
	public Collection ejbFindRegularInvoicesForPeriodAndCategoryExceptType(Date firstDateOfMonthForPeriod, String category, int regulationSpecTypeId) throws FinderException{
		IDOQuery sql = idoQuery();
		IWTimestamp endOfPeriod = new IWTimestamp(firstDateOfMonthForPeriod);
		endOfPeriod.addMonths(1);
		sql.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_SCHOOL_CATEGORY_ID, category);
		sql.appendAnd().append(COLUMN_FROM).appendLessThanSign().append(endOfPeriod.getDate());
		sql.appendAnd().appendLeftParenthesis().append(COLUMN_TO).appendGreaterThanOrEqualsSign().append(firstDateOfMonthForPeriod);
		sql.appendOr().append(COLUMN_TO).append(" is null").appendRightParenthesis();
		sql.appendAnd().append(COLUMN_REG_SPEC_TYPE_ID).appendNOTEqual().append(regulationSpecTypeId);
//		System.out.println("SQL:"+sql);
		return idoFindPKsByQuery(sql);
	}

	public Collection ejbFindRegularInvoicesForPeriodAndChildAndCategoryAndRegSpecType(Date date, int childUserId, String category, int regSpecTypeId) throws FinderException{
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEqualsQuoted(COLUMN_SCHOOL_CATEGORY_ID, category);
		sql.appendAnd().append(COLUMN_FROM).appendLessThanOrEqualsSign().append(date);
		sql.appendAnd().appendLeftParenthesis().append(COLUMN_TO).appendGreaterThanOrEqualsSign().append(date);
		sql.appendOr().append(COLUMN_TO).append(" is null").appendRightParenthesis();
		sql.appendAndEquals(COLUMN_REG_SPEC_TYPE_ID,regSpecTypeId);
		sql.appendAndEquals(COLUMN_USER_ID, childUserId);
		
//		System.out.println("SQL = " + sql);
		
		return idoFindPKsByQuery(sql);
	}

}