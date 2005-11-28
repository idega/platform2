/**
 * 
 */
package is.idega.idegaweb.campus.block.application.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;

/**
 * @author bluebottle
 *
 */
public interface CampusApplicationHome extends IDOHome {
	public CampusApplication create() throws javax.ejb.CreateException;

	public CampusApplication findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#ejbFindAllByApplicationId
	 */
	public java.util.Collection findAllByApplicationId(int id)
			throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#ejbFindBySubjectAndStatus
	 */
	public Collection findBySubjectAndStatus(Integer subjectID, String status,
			String order) throws FinderException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#ejbFindBySubjectAndStatus
	 */
	public Collection findBySubjectAndStatus(Integer subjectID, String status,
			String order, int numberOfRecords, int startingIndex)
			throws FinderException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#ejbHomeGetCountBySubjectAndStatus
	 */
	public int getCountBySubjectAndStatus(Integer subjectID, String status)
			throws IDORelationshipException, IDOException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#ejbFindBySQL
	 */
	public Collection findBySQL(String sql) throws FinderException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationBMPBean#ejbFindByApartmentTypeAndComplex
	 */
	public Collection findByApartmentTypeAndComplex(Integer typeId,
			Integer complexID) throws FinderException;

}
