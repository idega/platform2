package com.idega.block.building.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface FloorHome extends IDOHome {
	public Floor create() throws CreateException;

	public Floor findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;

	public Collection findByBuilding(Integer buildingID) throws FinderException;

	public Floor findByBuildingAndName(String name, Building building)
			throws FinderException;
}