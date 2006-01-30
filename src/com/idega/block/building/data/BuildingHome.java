/**
 * 
 */
package com.idega.block.building.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface BuildingHome extends IDOHome {
	public Building create() throws javax.ejb.CreateException;

	public Building findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#ejbFindByComplex
	 */
	public Collection findByComplex(Integer complexID) throws FinderException;

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#ejbHomeGetImageFilesByComplex
	 */
	public Collection getImageFilesByComplex(Integer complexID)
			throws FinderException;

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#ejbFindByComplex
	 */
	public Collection findByComplex(Complex complex) throws FinderException;

}
