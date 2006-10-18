/**
 * 
 */
package is.idega.idegaweb.campus.block.application.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface WaitingListHome extends IDOHome {
	public WaitingList create() throws javax.ejb.CreateException;

	public WaitingList findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#ejbFindByApartmentTypeAndComplexForApplicationType
	 */
	public Collection findByApartmentTypeAndComplexForApplicationType(
			int aprtId, int complexId) throws FinderException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#ejbFindByApartmentTypeAndComplexForTransferType
	 */
	public Collection findByApartmentTypeAndComplexForTransferType(int aprtId,
			int complexId) throws FinderException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#ejbFindByApartmentTypeAndComplex
	 */
	public Collection findByApartmentTypeAndComplex(int aprtId, int complexId)
			throws FinderException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#ejbFindNextForTransferByApartmentTypeAndComplex
	 */
	public Collection findNextForTransferByApartmentTypeAndComplex(int aprtId,
			int complexId, int orderedFrom) throws FinderException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#ejbFindByApartmentType
	 */
	public Collection findByApartmentType(int[] aprtId) throws FinderException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#ejbFindByApplicantID
	 */
	public Collection findByApplicantID(Integer ID) throws FinderException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean#ejbFindBySQL
	 */
	public Collection findBySQL(String sql) throws FinderException;

}
