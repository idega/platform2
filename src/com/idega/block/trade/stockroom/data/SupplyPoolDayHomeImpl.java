package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;


/**
 * @author gimmi
 */
public class SupplyPoolDayHomeImpl extends IDOFactory implements SupplyPoolDayHome {

	protected Class getEntityInterfaceClass() {
		return SupplyPoolDay.class;
	}

	public SupplyPoolDay create() throws javax.ejb.CreateException {
		return (SupplyPoolDay) super.createIDO();
	}

	public SupplyPoolDay findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (SupplyPoolDay) super.findByPrimaryKeyIDO(pk);
	}

	public SupplyPoolDay findByPrimaryKey(SupplyPoolDayPK primaryKey) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SupplyPoolDayBMPBean) entity).ejbFindByPrimaryKey(primaryKey);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SupplyPoolDay create(SupplyPoolDayPK primaryKey) throws CreateException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SupplyPoolDayBMPBean) entity).ejbCreate(primaryKey);
		//((SupplyPoolDayBMPBean) entity).ejbPostCreate();
		this.idoCheckInPooledEntity(entity);
		try {
			return this.findByPrimaryKey(pk);
		}
		catch (javax.ejb.FinderException fe) {
			throw new com.idega.data.IDOCreateException(fe);
		}
		catch (Exception e) {
			throw new com.idega.data.IDOCreateException(e);
		}
	}

	public Collection findBySupplyPool(SupplyPool supplyPool) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SupplyPoolDayBMPBean) entity).ejbFindBySupplyPool(supplyPool);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
