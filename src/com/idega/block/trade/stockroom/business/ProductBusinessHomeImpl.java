package com.idega.block.trade.stockroom.business;

import com.idega.business.IBOHomeImpl;


/**
 * @author gimmi
 */
public class ProductBusinessHomeImpl extends IBOHomeImpl implements ProductBusinessHome {

	protected Class getBeanInterfaceClass() {
		return ProductBusiness.class;
	}

	public ProductBusiness create() throws javax.ejb.CreateException {
		return (ProductBusiness) super.createIBO();
	}
}
