package com.idega.block.trade.stockroom.business;

import com.idega.business.IBOHomeImpl;


/**
 * @author gimmi
 */
public class SupplierManagerBusinessHomeImpl extends IBOHomeImpl implements SupplierManagerBusinessHome {

	protected Class getBeanInterfaceClass() {
		return SupplierManagerBusiness.class;
	}

	public SupplierManagerBusiness create() throws javax.ejb.CreateException {
		return (SupplierManagerBusiness) super.createIBO();
	}
}
