/*
 * $Id: ProviderAccountingPropertiesBMPBean.java,v 1.2 2004/10/15 10:41:51 thomas Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.care.data;

import com.idega.block.school.data.School;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import java.util.Collection;
import javax.ejb.FinderException;

/**
 * Entity bean holding accounting information for school (provider) entries.
 * <p>
 * Last modified: $Date: 2004/10/15 10:41:51 $ by $Author: thomas $
 *
 * @author Anders Lindman
 * @version $Revision: 1.2 $
 */
public class ProviderAccountingPropertiesBMPBean extends GenericEntity implements ProviderAccountingProperties {

	private static final String ENTITY_NAME = "cacc_provider_acc_prop";

	private static final String COLUMN_SCHOOL_ID = "school_id";
	private static final String COLUMN_PROVIDER_TYPE_ID = "provider_type_id";
	private static final String COLUMN_STATISTICS_TYPE = "statistics_type";
	private static final String COLUMN_PAYMENT_BY_INVOICE = "payment_by_invoice";
	private static final String COLUMN_STATE_SUBSIDY_GRANT = "state_subsidy_grant";
	private static final String COLUMN_POSTGIRO = "postgiro";
	private static final String COLUMN_BANKGIRO = "bankgiro";
	private static final String COLUMN_OWN_POSTING = "own_posting";
	private static final String COLUMN_DOUBLE_POSTING = "double_posting";
		
	/**
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	/**
	 * @see com.idega.data.GenericEntity#getIdColumnName()
	 */
	public String getIDColumnName() {
		return COLUMN_SCHOOL_ID;
	}

	/**
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addOneToOneRelationship(getIDColumnName(), School.class);
		setAsPrimaryKey (getIDColumnName(), true);

		addAttribute(COLUMN_PROVIDER_TYPE_ID, "Provider type (foreign key)", true, true, 
				Integer.class, "many-to-one", ProviderType.class);
		addAttribute(COLUMN_STATISTICS_TYPE, "Provider statistics type (foreign key)", true, true, 
				String.class, "many-to-one", ProviderStatisticsType.class);
		addAttribute(COLUMN_PAYMENT_BY_INVOICE, "Invoice yes/no", true, true, Boolean.class);
		addAttribute(COLUMN_STATE_SUBSIDY_GRANT, "State subsidy grant yes/no", true, true, Boolean.class);
		addAttribute(COLUMN_POSTGIRO, "Postgiro", true, true, String.class);
		addAttribute(COLUMN_BANKGIRO, "Bankgiro", true, true, String.class);
		addAttribute(COLUMN_OWN_POSTING, "Own posting string", true, true, String.class, 1000);
		addAttribute(COLUMN_DOUBLE_POSTING, "Double posting string", true, true, String.class, 1000);
	}
	
	public School getSchool() {
		return (School) getColumnValue(COLUMN_SCHOOL_ID);	
	}

	public int getSchoolId() {
		return getIntColumnValue(COLUMN_SCHOOL_ID);	
	}
	
	public ProviderType getProviderType() {
		return (ProviderType) getColumnValue(COLUMN_PROVIDER_TYPE_ID);	
	}

	public int getProviderTypeId() {
		return getIntColumnValue(COLUMN_PROVIDER_TYPE_ID);	
	}

	public String getStatisticsType() {
		return getStringColumnValue(COLUMN_STATISTICS_TYPE);	
	}

	public boolean getPaymentByInvoice() {
		Boolean b = (Boolean) getColumnValue(COLUMN_PAYMENT_BY_INVOICE);
		if (b != null) {
			return b.booleanValue();
		} else {
			return false;
		}
	}

	public boolean getStateSubsidyGrant() {
		Boolean b = (Boolean) getColumnValue(COLUMN_STATE_SUBSIDY_GRANT);
		if (b != null) {
			return b.booleanValue();
		} else {
			return false;
		}
	}

	public String getPostgiro() {
		return getStringColumnValue(COLUMN_POSTGIRO);	
	}

	public String getBankgiro() {
		return getStringColumnValue(COLUMN_BANKGIRO);	
	}

	public String getOwnPosting() {
		return getStringColumnValue(COLUMN_OWN_POSTING);	
	}

	public String getDoublePosting() {
		return getStringColumnValue(COLUMN_DOUBLE_POSTING);	
	}

	public void setSchoolId(int id) { 
		setColumn(COLUMN_SCHOOL_ID, id); 
	}

	public void setProviderTypeId(int id) { 
		setColumn(COLUMN_PROVIDER_TYPE_ID, id); 
	}

	public void setStatisticsType(String type) { 
		setColumn(COLUMN_STATISTICS_TYPE, type); 
	}

	public void setPaymentByInvoice(boolean b) { 
		setColumn(COLUMN_PAYMENT_BY_INVOICE, b); 
	}

	public void setStateSubsidyGrant(boolean b) { 
		setColumn(COLUMN_STATE_SUBSIDY_GRANT, b); 
	}

	public void setPostgiro(String postgiro) { 
		setColumn(COLUMN_POSTGIRO, postgiro); 
	}

	public void setBankgiro(String bankgiro) { 
		setColumn(COLUMN_BANKGIRO, bankgiro); 
	}

	public void setOwnPosting(String s) { 
		setColumn(COLUMN_OWN_POSTING, s); 
	}

	public void setDoublePosting(String s) { 
		setColumn(COLUMN_DOUBLE_POSTING, s); 
	}

	public Collection ejbFindAllByPaymentByInvoice
		(final boolean hasPaymentByInvoice)
		throws FinderException {
		final IDOQuery sql = idoQuery ();
		sql.appendSelectAllFrom (getTableName());
		sql.appendWhereEquals (COLUMN_PAYMENT_BY_INVOICE, hasPaymentByInvoice);
		return idoFindPKsBySQL (sql.toString());		
	}
}
