package com.idega.block.trade.stockroom.business;

import com.idega.business.IBOHome;


/**
 * @author gimmi
 */
public interface StockroomBusinessHome extends IBOHome {

	public StockroomBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
