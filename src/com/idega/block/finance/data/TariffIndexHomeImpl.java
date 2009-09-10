package com.idega.block.finance.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class TariffIndexHomeImpl extends IDOFactory implements TariffIndexHome {
	public Class getEntityInterfaceClass() {
		return TariffIndex.class;
	}

	public TariffIndex create() throws CreateException {
		return (TariffIndex) super.createIDO();
	}

	public TariffIndex findByPrimaryKey(Object pk) throws FinderException {
		return (TariffIndex) super.findByPrimaryKeyIDO(pk);
	}

	public TariffIndex findLastByType(String type) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((TariffIndexBMPBean) entity).ejbFindLastByType(type);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findLastTypeGrouped() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((TariffIndexBMPBean) entity).ejbFindLastTypeGrouped();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}