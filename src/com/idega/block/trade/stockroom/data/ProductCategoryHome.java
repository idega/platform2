package com.idega.block.trade.stockroom.data;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;


/**
 * @author gimmi
 */
public interface ProductCategoryHome extends IDOHome {

	public ProductCategory create() throws javax.ejb.CreateException;

	public ProductCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	public ProductCategory findByPrimaryKey(int id) throws javax.ejb.FinderException;

	public ProductCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductCategoryBMPBean#ejbHomeGetProductCategory
	 */
	public ProductCategory getProductCategory(String type) throws FinderException, RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ProductCategoryBMPBean#ejbHomeFindAll
	 */
	public Collection findAll() throws FinderException;
}
