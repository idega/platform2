package com.idega.block.trade.stockroom.business;

import com.idega.business.IBOHomeImpl;


/**
 * @author gimmi
 */
public class StockroomBusinessHomeImpl extends IBOHomeImpl implements StockroomBusinessHome {

	protected Class getBeanInterfaceClass() {
		return StockroomBusiness.class;
	}

	public StockroomBusiness create() throws javax.ejb.CreateException {
		return (StockroomBusiness) super.createIBO();
	}
}
