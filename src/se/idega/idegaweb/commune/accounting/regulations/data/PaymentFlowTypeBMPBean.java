/*
 * $Id: PaymentFlowTypeBMPBean.java,v 1.4 2003/08/20 09:02:30 anders Exp $
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
import com.idega.data.IDOQuery;
import com.idega.data.IDOLookup;

/**
 * Entity bean for the payment flow type (in, out, e t c).
 * <p>
 * Last modified: $Date: 2003/08/20 09:02:30 $ by $Author: anders $
 *
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $Revision: 1.4 $
 */
public class PaymentFlowTypeBMPBean  extends GenericEntity implements PaymentFlowType {

	private static final String ENTITY_NAME = "cacc_payment_flow_type";

	private static final String COLUMN_PAYMENT_FLOW_TYPE_ID = "payment_flow_type_id";
	private static final String COLUMN_TEXT_KEY = "text_key";

	private static final String KEY_PREFIX = ENTITY_NAME + ".";

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
		return COLUMN_PAYMENT_FLOW_TYPE_ID;
	}

	/**
	 * @see com.idega.data.GenericEntity#insertStartData()
	 */
	public void insertStartData () throws Exception {
		super.insertStartData ();
        
		System.out.println ("¤¤¤ Invoked " + ENTITY_NAME + ".insertStartData ()");

		PaymentFlowTypeHome home = (PaymentFlowTypeHome) IDOLookup.getHome(ActivityType.class);
		final String [] data = {
				KEY_PREFIX + "in", 
				KEY_PREFIX + "out"
		};
		
		for (int i = 0; i < data.length; i++) {
			PaymentFlowType pft = home.create();
			pft.setTextKey(data[i]);
			pft.store();
		}
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
	 * Finds all payment flow types.
	 * @return collection of all payment flow types found
	 * @throws FinderException
	 */
	public Collection ejbFindAllPaymentFlowTypes() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.append(getEntityName());
		return idoFindPKsBySQL(sql.toString());
	}

	/**
	 * Returns the payment flow type for the specified id or null if not found.
	 * @param id the unique id for the payment flow type
	 * @return the payment flow type found
	 * @throws FinderException
	 */
	public Object ejbFindRegulationSpecType(int id) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(getIDColumnName(), id);
		return idoFindOnePKByQuery(sql);
	}
}
