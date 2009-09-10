package com.idega.block.trade.data;

import com.idega.data.IDOEntity;
import com.idega.user.data.Group;


/**
 * @author gimmi
 */
public interface CreditCardInformation extends IDOEntity {

	/**
	 * @see com.idega.block.trade.data.CreditCardInformationBMPBean#getMerchantPKString
	 */
	public String getMerchantPKString();

	/**
	 * @see com.idega.block.trade.data.CreditCardInformationBMPBean#getType
	 */
	public String getType();

	/**
	 * @see com.idega.block.trade.data.CreditCardInformationBMPBean#getSupplierManager
	 */
	public Group getSupplierManager();

	/**
	 * @see com.idega.block.trade.data.CreditCardInformationBMPBean#setMerchantPK
	 */
	public void setMerchantPK(Object pk);

	/**
	 * @see com.idega.block.trade.data.CreditCardInformationBMPBean#setType
	 */
	public void setType(String type);

	/**
	 * @see com.idega.block.trade.data.CreditCardInformationBMPBean#setSupplierManager
	 */
	public void setSupplierManager(Group supplierManager);
}
