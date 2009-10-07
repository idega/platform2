package com.idega.block.building.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ApartmentCategoryHome extends IDOHome {
	public ApartmentCategory create() throws CreateException;

	public ApartmentCategory findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;

	public ApartmentCategory findByName(String name) throws FinderException;

	public Collection findByComplex(Integer complexID) throws FinderException;
}