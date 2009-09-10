package com.idega.block.building.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface ComplexHome extends IDOHome {
	public Complex create() throws CreateException;

	public Complex findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;

	public Collection findAllIncludingLocked() throws FinderException;
}