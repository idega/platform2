package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;


/**
 * @author gimmi
 */
public class SupplierHomeImpl extends IDOFactory implements SupplierHome {

	protected Class getEntityInterfaceClass() {
		return Supplier.class;
	}

	public Supplier create() throws javax.ejb.CreateException {
		return (Supplier) super.createIDO();
	}

	public Supplier findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (Supplier) super.findByPrimaryKeyIDO(pk);
	}

	public Supplier createLegacy() {
		try {
			return create();
		}
		catch (javax.ejb.CreateException ce) {
			throw new RuntimeException("CreateException:" + ce.getMessage());
		}
	}

	public Supplier findByPrimaryKey(int id) throws javax.ejb.FinderException {
		return (Supplier) super.findByPrimaryKeyIDO(id);
	}

	public Supplier findByPrimaryKeyLegacy(int id) throws java.sql.SQLException {
		try {
			return findByPrimaryKey(id);
		}
		catch (javax.ejb.FinderException fe) {
			throw new java.sql.SQLException("FinderException:" + fe.getMessage());
		}
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SupplierBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findWithTPosMerchant() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SupplierBMPBean) entity).ejbFindWithTPosMerchant();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByGroupID(int groupID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SupplierBMPBean) entity).ejbFindAllByGroupID(groupID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
