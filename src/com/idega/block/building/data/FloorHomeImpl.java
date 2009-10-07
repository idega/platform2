package com.idega.block.building.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.data.IDOEntity;
import javax.ejb.FinderException;
import java.util.Collection;

public class FloorHomeImpl extends IDOFactory implements FloorHome {
	public Class getEntityInterfaceClass() {
		return Floor.class;
	}

	public Floor create() throws CreateException {
		return (Floor) super.createIDO();
	}

	public Floor findByPrimaryKey(Object pk) throws FinderException {
		return (Floor) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((FloorBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByBuilding(Integer buildingID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((FloorBMPBean) entity).ejbFindByBuilding(buildingID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Floor findByBuildingAndName(String name, Building building)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((FloorBMPBean) entity).ejbFindByBuildingAndName(name,
				building);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}