package com.idega.block.building.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface ApartmentTypeHome extends IDOHome {
	public ApartmentType create() throws CreateException;

	public ApartmentType findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;

	public Collection findAllIncludingLocked() throws FinderException;

	public Collection findByBuilding(Integer buildingID) throws FinderException;

	public Collection findByCategory(Integer categoryID) throws FinderException;

	public Collection findByComplex(Integer complexID) throws FinderException;

	public Collection findFromSameComplex(ApartmentType thetype) throws FinderException;
}