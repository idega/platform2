package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;


/**
 * @author gimmi
 */
public interface ResellerHome extends IDOHome {

	public Reseller create() throws javax.ejb.CreateException;

	public Reseller createLegacy();

	public Reseller findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	public Reseller findByPrimaryKey(int id) throws javax.ejb.FinderException;

	public Reseller findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#ejbFindAllByGroupID
	 */
	public Collection findAllByGroupID(Object groupPK) throws FinderException;
}
