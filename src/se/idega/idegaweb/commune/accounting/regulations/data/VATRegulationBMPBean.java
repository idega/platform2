/*
 * $Id: VATRegulationBMPBean.java,v 1.1 2003/08/18 14:45:16 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDOQuery;

/**
 * Entity bean for VATRegulation entries.
 * <p>
 * Last modified: $Date: 2003/08/18 14:45:16 $ by $Author: anders $
 *
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $Revision: 1.1 $
 */
public class VATRegulationBMPBean extends GenericEntity implements VATRegulation {

	private static final String ENTITY_NAME = "cacc_vat_regulation";

	private static final String COLUMN_PERIOD_FROM = "period_from";
	private static final String COLUMN_PERIOD_TO = "period_to";
	private static final String COLUMN_DESCRIPTION = "description";
	private static final String COLUMN_VAT_PERCENT = "vat_percent";
	private static final String COLUMN_PAYMENT_FLOW_TYPE = "direction";
	private static final String COLUMN_PROVIDER_TYPE = "provider_type";
	
	/**
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/**
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_PERIOD_FROM, "From period YYMM", true, true, java.lang.String.class, 4);
		addAttribute(COLUMN_PERIOD_TO, "To period YYMM", true, true, java.lang.String.class, 4);
		addAttribute(COLUMN_DESCRIPTION, "Description of the VAT regulation", true, true, java.lang.String.class);
		addAttribute(COLUMN_VAT_PERCENT, "VAT percent value", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_PAYMENT_FLOW_TYPE, "Direction of payment flow (foreign key)", true, true, java.lang.Integer.class);
		addAttribute(COLUMN_PROVIDER_TYPE, "Provider type (foreign key)", true, true, java.lang.Integer.class);		
		
		addManyToOneRelationship(COLUMN_PAYMENT_FLOW_TYPE, PaymentFlowType.class);
		addManyToOneRelationship(COLUMN_PROVIDER_TYPE, ProviderType.class);
	}

	public String getPeriodFrom() {
		return getStringColumnValue(COLUMN_PERIOD_FROM);	
	}

	public String getPeriodTo() {
		return getStringColumnValue(COLUMN_PERIOD_TO);	
	}

	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);	
	}

	public int getVATPercent() {
		return getIntColumnValue(COLUMN_VAT_PERCENT);	
	}
	
	public PaymentFlowType getPaymentFlowType() {
		return (PaymentFlowType) getColumnValue(COLUMN_PAYMENT_FLOW_TYPE);	
	}
	
	public ProviderType getProviderType() {
		return (ProviderType) getColumnValue(COLUMN_PAYMENT_FLOW_TYPE);	
	}
	
	/**
	 * Finds all VAT regulations for the specified time period.
	 * @param from the start of the period (YYMM)
	 * @param to the end of the period (YYMM)
	 * @return
	 * @throws FinderException
	 */
	public Collection ejbFindByPeriod(String from, String to) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
//		sql.appendWhereEquals(COLUMN_CP_KONTERING_STRING_ID, PostingStringId);
//		sql.appendOrderBy(COLUMN_ORDER_NR);

		return idoFindPKsByQuery(sql);
	}		
	
}
