package com.idega.block.trade.stockroom.business;

import com.idega.business.IBOHomeImpl;


/**
 * @author gimmi
 */
public class ResellerManagerHomeImpl extends IBOHomeImpl implements ResellerManagerHome {

	protected Class getBeanInterfaceClass() {
		return ResellerManager.class;
	}

	public ResellerManager create() throws javax.ejb.CreateException {
		return (ResellerManager) super.createIBO();
	}
}
