/*
 * $Id: VATRegulationBMPBean.java,v 1.15 2004/10/15 10:36:38 thomas Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;
import se.idega.idegaweb.commune.care.data.ProviderType;

import com.idega.block.school.data.SchoolCategory;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.util.CalendarMonth;

/**
 * Entity bean for VATRegulation entries.
 * <p>
 * Last modified: $Date: 2004/10/15 10:36:38 $ by $Author: thomas $
 *
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $Revision: 1.15 $
 */
public class VATRegulationBMPBean extends GenericEntity implements VATRegulation {

	private static final String ENTITY_NAME = "cacc_vat_regulation";

	private static final String COLUMN_VAT_REGULATION_ID = "vat_regulation_id";
	private static final String COLUMN_PERIOD_FROM = "period_from";
	private static final String COLUMN_PERIOD_TO = "period_to";
	private static final String COLUMN_DESCRIPTION = "description";
	private static final String COLUMN_VAT_PERCENT = "vat_percent";
	private static final String COLUMN_PAYMENT_FLOW_TYPE_ID = "payment_flow_type_id";
	private static final String COLUMN_PROVIDER_TYPE_ID = "provider_type_id";
	private static final String COLUMN_CATEGORY = "category";
		
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
		return COLUMN_VAT_REGULATION_ID;
	}

	/**
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		setAsPrimaryKey (getIDColumnName(), true);

		addAttribute(COLUMN_PERIOD_FROM, "From period", true, true, Date.class);
		addAttribute(COLUMN_PERIOD_TO, "To period", true, true, Date.class);
		addAttribute(COLUMN_DESCRIPTION, "Description of the VAT regulation", true, true, java.lang.String.class);
		addAttribute(COLUMN_VAT_PERCENT, "VAT percent value", true, true, java.lang.Float.class);
		addAttribute(COLUMN_PAYMENT_FLOW_TYPE_ID, "Direction of payment flow (foreign key)", true, true, 
				Integer.class, "many-to-one", PaymentFlowType.class);
		addAttribute(COLUMN_PROVIDER_TYPE_ID, "Provider type (foreign key)", true, true, 
				Integer.class, "many-to-one", ProviderType.class);
		addAttribute(COLUMN_CATEGORY, "Operational field (school category) (foreign key)", true, true, 
				String.class, "many-to-one", SchoolCategory.class);
	}

	public Date getPeriodFrom() {
		return (Date) getColumnValue(COLUMN_PERIOD_FROM);	
	}

	public Date getPeriodTo() {
		return (Date) getColumnValue(COLUMN_PERIOD_TO);	
	}

	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);	
	}

	public float getVATPercent() {
		return getFloatColumnValue(COLUMN_VAT_PERCENT);	
	}
	
	public PaymentFlowType getPaymentFlowType() {
		return (PaymentFlowType) getColumnValue(COLUMN_PAYMENT_FLOW_TYPE_ID);	
	}
	
	public ProviderType getProviderType() {
		return (ProviderType) getColumnValue(COLUMN_PROVIDER_TYPE_ID);	
	}
	
	public int getPaymentFlowTypeId() {
		return getIntColumnValue(COLUMN_PAYMENT_FLOW_TYPE_ID);	
	}

	public int getProviderTypeId() {
		return getIntColumnValue(COLUMN_PROVIDER_TYPE_ID);	
	}
	
	public String getCategory() {
		return getStringColumnValue(COLUMN_CATEGORY);	
	}

	public void setPeriodFrom(Date from) { 
		setColumn(COLUMN_PERIOD_FROM, from); 
	}

	public void setPeriodTo(Date to) { 
		CalendarMonth month = new CalendarMonth(to);
		setColumn(COLUMN_PERIOD_TO, month.getLastDateOfMonth()); 
	}

	public void setDescription(String description) { 
		setColumn(COLUMN_DESCRIPTION, description); 
	}

	public void setVATPercent(float percent) { 
		setColumn(COLUMN_VAT_PERCENT, percent); 
	}

	public void setProviderTypeId(int id) { 
		setColumn(COLUMN_PROVIDER_TYPE_ID, id); 
	}

	public void setPaymentFlowTypeId(int id) { 
		setColumn(COLUMN_PAYMENT_FLOW_TYPE_ID, id); 
	}

	public void setCategory(String category) { 
		setColumn(COLUMN_CATEGORY, category); 
	}

	/**
	 * Finds all VAT regulations.
	 * @return collection of all VAT regulation objects
	 * @throws FinderException
	 */
	public Collection ejbFindAll() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendOrderBy();
		String[] s = {COLUMN_PERIOD_FROM + " desc", COLUMN_PERIOD_TO + " desc", COLUMN_DESCRIPTION};
		sql.appendCommaDelimited(s);
		return idoFindPKsBySQL(sql.toString());
	}

	/**
	 * Finds all VAT regulations with the specified category (operational field).
	 * @param category the school category (operational field)
	 * @return collection of the VAT regulation objects found
	 * @throws FinderException
	 */
	public Collection ejbFindByCategory(String category) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEqualsQuoted(COLUMN_CATEGORY, category);
		sql.appendOrderBy();
		String[] s = {COLUMN_PERIOD_FROM + " desc", COLUMN_PERIOD_TO + " desc", COLUMN_DESCRIPTION};
		sql.appendCommaDelimited(s);
		return idoFindPKsBySQL(sql.toString());
	}
	
	/**
	 * Finds all VAT regulations for the specified time period and category.
	 * @param from the start of the period
	 * @param to the end of the period
	 * @param category the school category (operational field)
	 * @return collection of all VAT regulation for the specified period
	 * @throws FinderException
	 */
	public Collection ejbFindByPeriod(Date from, Date to, String category) throws FinderException {
		to = getEndOfMonth(to);
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEqualsQuoted(COLUMN_CATEGORY, category);
		if (from != null) {
			sql.appendAnd().append(COLUMN_PERIOD_FROM);
			sql.appendGreaterThanOrEqualsSign();
			sql.append("'" + from + "'");
			if (to != null) {
				sql.appendAnd();
				sql.append(COLUMN_PERIOD_FROM);
				sql.appendLessThanOrEqualsSign();		
				sql.append("'" + to + "'");
			}
		} else if (to != null) {
			sql.appendAnd().append(COLUMN_PERIOD_FROM);
			sql.appendLessThanOrEqualsSign();		
			sql.append("'" + to + "'");
		}
		sql.appendOrderBy();
		String[] s = {COLUMN_PERIOD_FROM + " desc", COLUMN_PERIOD_TO + " desc", COLUMN_DESCRIPTION};
		sql.appendCommaDelimited(s);
		return idoFindPKsByQuery(sql);
	}		  

	/*
	 * This is a fix to always make sure the last date in the (to) month is covered
	 * See nacp377 
	 */
	private Date getEndOfMonth(Date date) {
		CalendarMonth fixedDate = new CalendarMonth(date);
		return fixedDate.getLastDateOfMonth();
	}	
}
