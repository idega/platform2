/**
 * 
 */
package se.idega.idegaweb.commune.adulteducation.accounting.data;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolType;
import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface BatchParametersHome extends IDOHome {
	public BatchParameters create() throws javax.ejb.CreateException;

	public BatchParameters findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#ejbFindBySchoolType
	 */
	public BatchParameters findBySchoolType(SchoolType schoolType)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#ejbFindBySchoolTypeId
	 */
	public BatchParameters findBySchoolTypeId(int schoolTypeId)
			throws FinderException;

}
