package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;


/**
 * @author gimmi
 */
public class ResellerHomeImpl extends IDOFactory implements ResellerHome {

	protected Class getEntityInterfaceClass() {
		return Reseller.class;
	}

	public Reseller create() throws javax.ejb.CreateException {
		return (Reseller) super.createIDO();
	}

	public Reseller findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (Reseller) super.findByPrimaryKeyIDO(pk);
	}

	public Reseller createLegacy() {
		try {
			return create();
		}
		catch (javax.ejb.CreateException ce) {
			throw new RuntimeException("CreateException:" + ce.getMessage());
		}
	}

	public Reseller findByPrimaryKey(int id) throws javax.ejb.FinderException {
		return (Reseller) super.findByPrimaryKeyIDO(id);
	}

	public Reseller findByPrimaryKeyLegacy(int id) throws java.sql.SQLException {
		try {
			return findByPrimaryKey(id);
		}
		catch (javax.ejb.FinderException fe) {
			throw new java.sql.SQLException("FinderException:" + fe.getMessage());
		}
	}

	public Collection findAllByGroupID(Object groupPK) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ResellerBMPBean) entity).ejbFindAllByGroupID(groupPK);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
