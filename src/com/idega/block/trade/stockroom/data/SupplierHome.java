package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;


/**
 * @author gimmi
 */
public interface SupplierHome extends IDOHome {

	public Supplier create() throws javax.ejb.CreateException, java.rmi.RemoteException;

	public Supplier findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

	public Supplier findByPrimaryKey(int id) throws javax.ejb.FinderException, java.rmi.RemoteException;

	public Supplier findByPrimaryKeyLegacy(int id) throws java.sql.SQLException, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#ejbFindWithTPosMerchant
	 */
	public Collection findWithTPosMerchant() throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#ejbFindAllByGroupID
	 */
	public Collection findAllByGroupID(int groupID) throws FinderException;
}
