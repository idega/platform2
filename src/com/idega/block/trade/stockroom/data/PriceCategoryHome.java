package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;


/**
 * @author gimmi
 */
public interface PriceCategoryHome extends IDOHome {

	public PriceCategory create() throws javax.ejb.CreateException, java.rmi.RemoteException;

	public PriceCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

	public PriceCategory findByPrimaryKey(int id) throws javax.ejb.FinderException, java.rmi.RemoteException;

	public PriceCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.data.PriceCategoryBMPBean#ejbFindByKey
	 */
	public PriceCategory findByKey(String key) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.PriceCategoryBMPBean#ejbFindBySupplierAndCountAsPerson
	 */
	public Collection findBySupplierAndCountAsPerson(int supplierID, boolean countAsPerson) throws FinderException;
}
