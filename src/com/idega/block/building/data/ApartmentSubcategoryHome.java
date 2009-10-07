package com.idega.block.building.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ApartmentSubcategoryHome extends IDOHome {
	public ApartmentSubcategory create() throws CreateException;

	public ApartmentSubcategory findByPrimaryKey(Object pk)
			throws FinderException;

	public Collection findAll() throws FinderException;

	public Collection findByCategory(Integer categoryID) throws FinderException;

	public ApartmentSubcategory findByCategoryAndName(String name,
			ApartmentCategory category) throws FinderException;

	public Collection findByCategory(Integer[] categoryID)
			throws FinderException;
}