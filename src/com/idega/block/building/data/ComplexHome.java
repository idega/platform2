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
public interface ComplexHome extends IDOHome {
	public Complex create() throws javax.ejb.CreateException;

	public Complex findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.building.data.ComplexBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

}
