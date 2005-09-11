/*
 * Created on 2005-sep-09
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.school.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface SchoolTypeHome extends IDOHome {
	public SchoolType create() throws javax.ejb.CreateException;

	public SchoolType findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#ejbFindAllSchoolTypes
	 */
	public Collection findAllSchoolTypes() throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#ejbFindAllByCategory
	 */
	public Collection findAllByCategory(String category)
			throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#ejbFindAllByCategory
	 */
	public Collection findAllByCategory(String category,
			boolean showFreetimeTypes) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#ejbFindByTypeKey
	 */
	public SchoolType findByTypeKey(String typeKey)
			throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#ejbFindByTypeString
	 */
	public SchoolType findByTypeString(String typeString)
			throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#ejbFindAllFreetimeTypes
	 */
	public Collection findAllFreetimeTypes() throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#ejbFindAllFreetimeTypes
	 */
	public Collection findAllFreetimeTypes(String category)
			throws FinderException;

}
