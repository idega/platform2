package com.idega.block.building.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface ApartmentViewHome extends IDOHome {
	public ApartmentView create() throws CreateException;

	public ApartmentView findByPrimaryKey(Object pk) throws FinderException;

	public Collection findByComplex(Integer complexID) throws FinderException;

	public Collection findByBuilding(Integer buildingID) throws FinderException;

	public Collection findByFloor(Integer floorID) throws FinderException;

	public Collection findByCategory(Integer categoryID) throws FinderException;

	public Collection findByType(Integer typeID) throws FinderException;

	public Collection findBySubcategory(Integer subcategoryID)
			throws FinderException;

	public Collection findByApartmentName(String name) throws FinderException;

	public Collection findAll() throws FinderException;
}