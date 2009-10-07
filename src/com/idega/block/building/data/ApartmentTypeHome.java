package com.idega.block.building.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ApartmentTypeHome extends IDOHome {
	public ApartmentType create() throws CreateException;

	public ApartmentType findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;

	public Collection findAllIncludingLocked() throws FinderException;

	public Collection findByBuilding(Integer buildingID) throws FinderException;

	public Collection findBySubcategory(Integer categoryID)
			throws FinderException;

	public ApartmentType findBySubcategoryAndAbbrevation(String abbrevation,
			ApartmentSubcategory subCategory) throws FinderException;

	public Collection findByComplex(Integer complexID) throws FinderException;
}