/*
 * $Id: ProviderTypeBMPBean.java,v 1.1 2003/08/18 14:45:16 anders Exp $
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

import com.idega.data.GenericEntity;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDOQuery;

/**
 * Entity bean for the provider type (childcare, school, e t c).
 * <p>
 * Last modified: $Date: 2003/08/18 14:45:16 $ by $Author: anders $
 *
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $Revision: 1.1 $
 */
public class ProviderTypeBMPBean  extends GenericEntity implements PaymentFlowType {

	private static final String ENTITY_NAME = "cacc_payment_flow_type";

	private static final String COLUMN_TEXT_KEY = "text_key";

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
		addAttribute(COLUMN_TEXT_KEY, "Text key for this type", true, true, String.class);
		setAsPrimaryKey (getIDColumnName(), true);
	}
	
	public String getTextKey() {
		return (String) getStringColumnValue(COLUMN_TEXT_KEY);
	}

	public void setTextKey(String textKey) { 
		setColumn(COLUMN_TEXT_KEY, textKey); 
	}

	/**
	 * Finds all provider types.
	 * @return collection of all provider types found
	 * @throws FinderException
	 */
	public Collection ejbFindAllPaymentFlowTypes() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.append(getEntityName());
		return idoFindPKsBySQL(sql.toString());
	}

	/**
	 * Returns the provider type for the specified id or null if not found.
	 * @param id the unique id for the provider type
	 * @return the provider type found
	 * @throws FinderException
	 */
	public Object ejbFindRegulationSpecType(int id) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(getIDColumnName(), id);
		return idoFindOnePKByQuery(sql);
	}
}
