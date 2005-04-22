/*
 * Created on 2005-apr-21
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
public interface SchoolStudyPathGroupHome extends IDOHome {
	public SchoolStudyPathGroup create() throws javax.ejb.CreateException;

	public SchoolStudyPathGroup findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolStudyPathGroupBMPBean#ejbFindAllStudyPathGroups
	 */
	public Collection findAllStudyPathGroups() throws FinderException;

}
