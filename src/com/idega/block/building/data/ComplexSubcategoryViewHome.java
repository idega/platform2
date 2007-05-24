package com.idega.block.building.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface ComplexSubcategoryViewHome extends IDOHome {
	public ComplexSubcategoryView create() throws CreateException;

	public ComplexSubcategoryView findByPrimaryKey(Object pk)
			throws FinderException;

	public ComplexSubcategoryView findByPrimaryKey(ComplexSubcategoryViewKey primaryKey)
			throws FinderException;

	public ComplexSubcategoryView create(ComplexSubcategoryViewKey primaryKey)
			throws CreateException;

	public Collection findAll() throws FinderException;

	public Collection findByCategory(Integer categoryID) throws FinderException;
}