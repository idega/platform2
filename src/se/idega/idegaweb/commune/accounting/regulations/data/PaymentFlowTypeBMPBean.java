/*
 * $Id: PaymentFlowTypeBMPBean.java,v 1.8 2003/09/03 08:12:06 anders Exp $
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
 * Last modified: $Date: 2003/09/03 08:12:06 $ by $Author: anders $
 *
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $Revision: 1.8 $
 */
public class PaymentFlowTypeBMPBean  extends GenericEntity implements PaymentFlowType {

	private static final String ENTITY_NAME = "cacc_payment_flow_type";

	private static final String COLUMN_PAYMENT_FLOW_TYPE_ID = "payment_flow_type_id";
	private static final String COLUMN_LOCALIZATION_KEY = "localization_key";

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

		PaymentFlowTypeHome home = (PaymentFlowTypeHome) IDOLookup.getHome(PaymentFlowType.class);
		final String [] data = {
				KEY_PREFIX + "in", 
				KEY_PREFIX + "out"
		};
		
		for (int i = 0; i < data.length; i++) {
			PaymentFlowType pft = home.create();
			pft.setLocalizationKey(data[i]);
			pft.store();
		}
	}

	/**
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_LOCALIZATION_KEY, "Localization key for this type", true, true, String.class);
		setAsPrimaryKey(getIDColumnName(), true);
	}
	
	public String getLocalizationKey() {
		return getStringColumnValue(COLUMN_LOCALIZATION_KEY);
	}

	public void setLocalizationKey(String localizationKey) { 
		setColumn(COLUMN_LOCALIZATION_KEY, localizationKey); 
	}

	/**
	 * Finds all payment flow types.
	 * @return collection of all payment flow types found
	 * @throws FinderException
	 */
	public Collection ejbFindAll() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		return idoFindPKsBySQL(sql.toString());
	}
}
