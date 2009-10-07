package com.idega.block.finance.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.data.IDOEntity;
import javax.ejb.FinderException;
import java.util.Collection;

public class TariffHomeImpl extends IDOFactory implements TariffHome {
	public Class getEntityInterfaceClass() {
		return Tariff.class;
	}

	public Tariff create() throws CreateException {
		return (Tariff) super.createIDO();
	}

	public Tariff findByPrimaryKey(Object pk) throws FinderException {
		return (Tariff) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByPrimaryKeyArray(String[] array)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((TariffBMPBean) entity)
				.ejbFindAllByPrimaryKeyArray(array);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByColumnOrdered(String column, String value,
			String order) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((TariffBMPBean) entity).ejbFindAllByColumnOrdered(
				column, value, order);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByColumnOrdered(String column, String value,
			String column2, String value2, String order) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((TariffBMPBean) entity).ejbFindAllByColumnOrdered(
				column, value, column2, value2, order);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByColumn(String column, String value)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((TariffBMPBean) entity).ejbFindAllByColumn(column,
				value);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByColumn(String column, int value)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((TariffBMPBean) entity).ejbFindAllByColumn(column,
				value);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByTariffGroup(Integer groupId) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((TariffBMPBean) entity).ejbFindByTariffGroup(groupId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByAttribute(String attribute) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((TariffBMPBean) entity).ejbFindByAttribute(attribute);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}