package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;


/**
 * @author gimmi
 */
public class PriceCategoryHomeImpl extends IDOFactory implements PriceCategoryHome {

	protected Class getEntityInterfaceClass() {
		return PriceCategory.class;
	}

	public PriceCategory create() throws javax.ejb.CreateException {
		return (PriceCategory) super.createIDO();
	}

	public PriceCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (PriceCategory) super.findByPrimaryKeyIDO(pk);
	}

	public PriceCategory createLegacy() {
		try {
			return create();
		}
		catch (javax.ejb.CreateException ce) {
			throw new RuntimeException("CreateException:" + ce.getMessage());
		}
	}

	public PriceCategory findByPrimaryKey(int id) throws javax.ejb.FinderException {
		return (PriceCategory) super.findByPrimaryKeyIDO(id);
	}

	public PriceCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException {
		try {
			return findByPrimaryKey(id);
		}
		catch (javax.ejb.FinderException fe) {
			throw new java.sql.SQLException("FinderException:" + fe.getMessage());
		}
	}

	public PriceCategory findByKey(String key) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((PriceCategoryBMPBean) entity).ejbFindByKey(key);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findBySupplierAndCountAsPerson(int supplierID, boolean countAsPerson) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PriceCategoryBMPBean) entity).ejbFindBySupplierAndCountAsPerson(supplierID,
				countAsPerson);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
