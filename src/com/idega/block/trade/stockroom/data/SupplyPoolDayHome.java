package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;


/**
 * @author gimmi
 */
public interface SupplyPoolDayHome extends IDOHome {

	public SupplyPoolDay create() throws javax.ejb.CreateException;

	public SupplyPoolDay findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolDayBMPBean#ejbFindByPrimaryKey
	 */
	public SupplyPoolDay findByPrimaryKey(SupplyPoolDayPK primaryKey) throws FinderException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolDayBMPBean#ejbCreate
	 */
	public SupplyPoolDay create(SupplyPoolDayPK primaryKey) throws CreateException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplyPoolDayBMPBean#ejbFindBySupplyPool
	 */
	public Collection findBySupplyPool(SupplyPool supplyPool) throws FinderException;
}
