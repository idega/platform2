package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;


/**
 * @author gimmi
 */
public interface SupplyPoolHome extends IDOHome {

	public SupplyPool create() throws javax.ejb.CreateException;

	public SupplyPool findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolBMPBean#ejbFindBySupplier
	 */
	public Collection findBySupplier(Supplier supplier) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolBMPBean#ejbFindByProduct
	 */
	public SupplyPool findByProduct(Product product) throws IDORelationshipException, FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolBMPBean#ejbFindByProduct
	 */
	public SupplyPool findByProduct(Object productPK) throws IDORelationshipException, FinderException;
}
