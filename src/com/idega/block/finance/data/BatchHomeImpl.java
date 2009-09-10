package com.idega.block.finance.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class BatchHomeImpl extends IDOFactory implements BatchHome {
	public Class getEntityInterfaceClass() {
		return Batch.class;
	}

	public Batch create() throws CreateException {
		return (Batch) super.createIDO();
	}

	public Batch findByPrimaryKey(Object pk) throws FinderException {
		return (Batch) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((BatchBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Batch findUnsent() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((BatchBMPBean) entity).ejbFindUnsent();
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllNewestFirst() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((BatchBMPBean) entity).ejbFindAllNewestFirst();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}