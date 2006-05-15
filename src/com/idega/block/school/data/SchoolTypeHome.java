/**
 * 
 */
package com.idega.block.school.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;


/**
 * <p>
 * TODO is Describe Type SchoolTypeHome
 * </p>
 *  Last modified: $Date: 2006/05/15 09:43:28 $ by $Author: igors $
 * 
 * @author <a href="mailto:is@idega.com">is</a>
 * @version $Revision: 1.9.2.1 $
 */
public interface SchoolTypeHome extends IDOHome {

	public SchoolType create() throws javax.ejb.CreateException;

	public SchoolType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#ejbFindAllSchoolTypes
	 */
	public Collection findAllSchoolTypes() throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#ejbFindAllByCategory
	 */
	public Collection findAllByCategory(String category) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#ejbFindAllByCategoryTest
	 */
	public String findAllByCategoryTest(String category) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#ejbFindAllByCategory
	 */
	public Collection findAllByCategory(String category, boolean showFreetimeTypes) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#ejbFindByTypeKey
	 */
	public SchoolType findByTypeKey(String typeKey) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#ejbFindByName
	 */
	public SchoolType findByName(String name) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#ejbFindByTypeString
	 */
	public SchoolType findByTypeString(String typeString) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#ejbFindAllFreetimeTypes
	 */
	public Collection findAllFreetimeTypes() throws FinderException;

	/**
	 * @see com.idega.block.school.data.SchoolTypeBMPBean#ejbFindAllFreetimeTypes
	 */
	public Collection findAllFreetimeTypes(String category) throws FinderException;
}
