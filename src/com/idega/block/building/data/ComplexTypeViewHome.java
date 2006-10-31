package com.idega.block.building.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface ComplexTypeViewHome extends IDOHome {
	public ComplexTypeView create() throws CreateException;

	public ComplexTypeView findByPrimaryKey(Object pk) throws FinderException;

	public ComplexTypeView findByPrimaryKey(ComplexTypeViewKey primaryKey) throws FinderException;

	public ComplexTypeView create(ComplexTypeViewKey primaryKey) throws CreateException;

	public Collection findAll() throws FinderException;

	public Collection findByCategory(Integer categoryID) throws FinderException;
}