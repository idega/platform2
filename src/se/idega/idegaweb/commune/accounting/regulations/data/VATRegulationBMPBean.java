/*
 * $Id: VATRegulationBMPBean.java,v 1.4 2003/08/21 15:58:22 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.data;

import java.util.Collection;
import java.sql.Date;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * Entity bean for VATRegulation entries.
 * <p>
 * Last modified: $Date: 2003/08/21 15:58:22 $ by $Author: anders $
 *
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $Revision: 1.4 $
 */
public class VATRegulationBMPBean extends GenericEntity implements VATRegulation {

	private static final String ENTITY_NAME = "cacc_vat_regulation";

	private static final String COLUMN_VAT_REGULATION_ID = "vat_regulation_id";
	private static final String COLUMN_PERIOD_FROM = "period_from";
	private static final String COLUMN_PERIOD_TO = "period_to";
	private static final String COLUMN_DESCRIPTION = "description";
	private static final String COLUMN_VAT_PERCENT = "vat_percent";
	private static final String COLUMN_PAYMENT_FLOW_TYPE_ID = "direction_id";
	private static final String COLUMN_PROVIDER_TYPE_ID = "provider_type_id";
	
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
		addAttribute(COLUMN_PERIOD_FROM, "From period YYMM", true, true, Date.class);
		addAttribute(COLUMN_PERIOD_TO, "To period YYMM", true, true, Date.class);
		addAttribute(COLUMN_DESCRIPTION, "Description of the VAT regulation", true, true, java.lang.String.class);
		addAttribute(COLUMN_VAT_PERCENT, "VAT percent value", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_PAYMENT_FLOW_TYPE_ID, "Direction of payment flow (foreign key)", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_PROVIDER_TYPE_ID, "Provider type (foreign key)", true, true, java.lang.Integer.class);		
		
		addManyToOneRelationship(COLUMN_PAYMENT_FLOW_TYPE_ID, PaymentFlowType.class);
		addManyToOneRelationship(COLUMN_PROVIDER_TYPE_ID, ProviderType.class);
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

	public int getVATPercent() {
		return getIntColumnValue(COLUMN_VAT_PERCENT);	
	}
	
	public PaymentFlowType getPaymentFlowType() {
		return (PaymentFlowType) getColumnValue(COLUMN_PAYMENT_FLOW_TYPE_ID);	
	}
	
	public ProviderType getProviderType() {
		return (ProviderType) getColumnValue(COLUMN_PAYMENT_FLOW_TYPE_ID);	
	}

	public void setPeriodFrom(Date from) { 
		setColumn(COLUMN_PERIOD_FROM, from); 
	}

	public void setPeriodTo(Date to) { 
		setColumn(COLUMN_PERIOD_TO, to); 
	}

	public void setDescription(String description) { 
		setColumn(COLUMN_DESCRIPTION, description); 
	}

	public void setVATPercent(int percent) { 
		setColumn(COLUMN_VAT_PERCENT, percent); 
	}

	public void setProviderTypeId(int id) { 
		setColumn(COLUMN_PROVIDER_TYPE_ID, id); 
	}

	public void setPaymentFlowTypeId(int id) { 
		setColumn(COLUMN_PAYMENT_FLOW_TYPE_ID, id); 
	}

	/**
	 * Finds all VAT regulations.
	 * @return collection of all VAT regulation objects
	 * @throws FinderException
	 */
	public Collection ejbFindAll() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.append(getEntityName());
		return idoFindPKsBySQL(sql.toString());
	}
	
	/**
	 * Finds all VAT regulations for the specified time period.
	 * @param from the start of the period
	 * @param to the end of the period
	 * @return collection of all VAT regulation for the specified period
	 * @throws FinderException
	 */
	public Collection ejbFindByPeriod(Date from, Date to) throws FinderException {
//		IDOQuery sql = idoQuery();
//		sql.appendSelectAllFrom(this);
//		sql.appendWhere(COLUMN_PERIOD_FROM);
//		sql.appendGreaterThanOrEqualsSign();
//		sql.append("'" + from + "'");
//		sql.appendAnd();
//		sql.appendLessThanOrEqualsSign();		
//		sql.append("'" + to + "'");
//		sql.appendOrderBy();
//		String[] s = {COLUMN_PERIOD_FROM, COLUMN_PERIOD_TO, COLUMN_DESCRIPTION};
//		sql.appendCommaDelimited(s);
//		return idoFindPKsByQuery(sql);
		return idoFindPKsBySQL("select * from " + ENTITY_NAME);
	}		  
}
