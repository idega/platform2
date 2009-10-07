package com.idega.block.building.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ComplexHome extends IDOHome {
	public Complex create() throws CreateException;

	public Complex findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;

	public Collection findAllIncludingLocked() throws FinderException;

	public Complex findComplexByName(String name) throws FinderException;
}