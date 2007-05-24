package com.idega.block.building.data;


import com.idega.data.IDOException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class ApartmentHomeImpl extends IDOFactory implements ApartmentHome {
	public Class getEntityInterfaceClass() {
		return Apartment.class;
	}

	public Apartment create() throws CreateException {
		return (Apartment) super.createIDO();
	}

	public Apartment findByPrimaryKey(Object pk) throws FinderException {
		return (Apartment) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findBySQL(String sql) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentBMPBean) entity).ejbFindBySQL(sql);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByType(Integer typeID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentBMPBean) entity).ejbFindByType(typeID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findeByTypeAndComplex(Integer typeID, Integer complexID)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentBMPBean) entity).ejbFindeByTypeAndComplex(
				typeID, complexID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByName(String name) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentBMPBean) entity).ejbFindByName(name);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getRentableCount() throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((ApartmentBMPBean) entity).ejbHomeGetRentableCount();
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getTypeAndComplexCount(Integer typeID, Integer complexID)
			throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((ApartmentBMPBean) entity)
				.ejbHomeGetTypeAndComplexCount(typeID, complexID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findBySearch(Integer complexID, Integer buildingID,
			Integer floorID, Integer typeID, Integer subcategoryID,
			boolean order) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentBMPBean) entity).ejbFindBySearch(complexID,
				buildingID, floorID, typeID, subcategoryID, order);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByFloor(Integer floorID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentBMPBean) entity).ejbFindByFloor(floorID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByFloor(Floor floor) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentBMPBean) entity).ejbFindByFloor(floor);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}