package com.idega.block.building.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface BuildingHome extends IDOHome {
	public Building create() throws CreateException;

	public Building findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;

	public Collection findAllIncludingLocked() throws FinderException;

	public Collection findByComplex(Integer complexID) throws FinderException;

	public Building findByComplexAndName(String name, Complex complex)
			throws FinderException;

	public Collection getImageFilesByComplex(Integer complexID)
			throws FinderException;

	public Collection findByComplex(Complex complex) throws FinderException;
}