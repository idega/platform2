/*
 * Created on 26.3.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.trade.data;

import com.idega.data.GenericEntity;

/**
 * @author gimmi
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class CreditCardInformationBMPBean extends GenericEntity implements CreditCardInformation {

	private static final String ENTITY_NAME = "CC_INFORMATION";
	private static final String COLUMN_TYPE = "CC_TYPE";
	private static final String COLUMN_MERCHANT_PK = "CC_MERCHANT_PK";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	public void initializeAttributes() {
		this.addAttribute(getIDColumnName());
		this.addAttribute(COLUMN_TYPE, "type", true, true, String.class);
		this.addAttribute(COLUMN_MERCHANT_PK, "merchantPK", true, true, String.class);
	}

	public String getMerchantPKString() {
		return getStringColumnValue(COLUMN_MERCHANT_PK);
	}
	
	public String getType() {
		return getStringColumnValue(COLUMN_TYPE);
	}

	public void setMerchantPK(Object pk) {
		setColumn(COLUMN_MERCHANT_PK, pk);
	}

	public void setType(String type) {
		setColumn(COLUMN_TYPE, type);
	}
	
}
