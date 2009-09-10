package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;
import com.idega.data.IDORelationshipException;


/**
 * @author gimmi
 */
public class SupplyPoolHomeImpl extends IDOFactory implements SupplyPoolHome {

	protected Class getEntityInterfaceClass() {
		return SupplyPool.class;
	}

	public SupplyPool create() throws javax.ejb.CreateException {
		return (SupplyPool) super.createIDO();
	}

	public SupplyPool findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (SupplyPool) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SupplyPoolBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySupplier(Supplier supplier) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SupplyPoolBMPBean) entity).ejbFindBySupplier(supplier);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SupplyPool findByProduct(Product product) throws IDORelationshipException, FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SupplyPoolBMPBean) entity).ejbFindByProduct(product);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SupplyPool findByProduct(Object productPK) throws IDORelationshipException, FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SupplyPoolBMPBean) entity).ejbFindByProduct(productPK);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}
