package com.idega.block.building.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import com.idega.data.IDOException;
import java.util.Collection;

public interface ApartmentHome extends IDOHome {
	public Apartment create() throws CreateException;

	public Apartment findByPrimaryKey(Object pk) throws FinderException;

	public Collection findBySQL(String sql) throws FinderException;

	public Collection findByType(Integer typeID) throws FinderException;

	public Collection findeByTypeAndComplex(Integer typeID, Integer complexID)
			throws FinderException;

	public Collection findByName(String name) throws FinderException;

	public int getRentableCount() throws IDOException;

	public int getTypeAndComplexCount(Integer typeID, Integer complexID)
			throws IDOException;

	public Collection findBySearch(Integer complexID, Integer buildingID,
			Integer floorID, Integer typeID, Integer subcategoryID,
			boolean order) throws FinderException;

	public Collection findByFloor(Integer floorID) throws FinderException;

	public Apartment findByFloorAndTypeAndName(String name, Floor floor,
			ApartmentType type) throws FinderException;

	public Collection findAll() throws FinderException;

	public Collection findByFloor(Floor floor) throws FinderException;
}