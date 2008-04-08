package com.idega.block.finance.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class AccountKeyHomeImpl extends IDOFactory implements AccountKeyHome {
	public Class getEntityInterfaceClass() {
		return AccountKey.class;
	}

	public AccountKey create() throws CreateException {
		return (AccountKey) super.createIDO();
	}

	public AccountKey findByPrimaryKey(Object pk) throws FinderException {
		return (AccountKey) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AccountKeyBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySQL(String sql) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AccountKeyBMPBean) entity).ejbFindBySQL(sql);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByCategory(Integer categoryID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AccountKeyBMPBean) entity)
				.ejbFindByCategory(categoryID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByPrimaryKeys(String[] keys) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AccountKeyBMPBean) entity)
				.ejbFindByPrimaryKeys(keys);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}