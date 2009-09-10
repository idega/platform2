package com.idega.block.building.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface ApartmentSubcategoryHome extends IDOHome {
	public ApartmentSubcategory create() throws CreateException;

	public ApartmentSubcategory findByPrimaryKey(Object pk)
			throws FinderException;

	public Collection findAll() throws FinderException;

	public Collection findByCategory(Integer categoryID) throws FinderException;

	public Collection findByCategory(Integer[] categoryID)
			throws FinderException;
}