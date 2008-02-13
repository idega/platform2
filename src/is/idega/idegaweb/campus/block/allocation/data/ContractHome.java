package is.idega.idegaweb.campus.block.allocation.data;


import com.idega.data.IDOException;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.sql.Date;

public interface ContractHome extends IDOHome {
	public Contract create() throws CreateException;

	public Contract findByPrimaryKey(Object pk) throws FinderException;

	public Collection findByApplicantID(Integer ID) throws FinderException;

	public Collection findByUserID(Integer ID) throws FinderException;

	public Collection findByApartmentAndUser(Integer AID, Integer UID)
			throws FinderException;

	public Collection findByUserAndRented(Integer ID, Boolean rented)
			throws FinderException;

	public Collection findByApartmentID(Integer ID) throws FinderException;

	public Collection findByApartmentAndStatus(Integer ID, String status)
			throws FinderException;

	public Collection findByApartmentAndStatus(Integer ID, String[] status)
			throws FinderException;

	public Collection findByApplicantAndStatus(Integer ID, String status)
			throws FinderException;

	public Collection findByApplicantAndRented(Integer ID, Boolean rented)
			throws FinderException;

	public Collection findByApartmentAndRented(Integer ID, Boolean rented)
			throws FinderException;

	public Collection findByStatus(String status) throws FinderException;

	public Collection findAll() throws FinderException;

	public Collection findBySQL(String sql) throws FinderException;

	public Collection findByApplicant(Integer ID) throws FinderException;

	public Collection findByApplicantInCreatedStatus(Integer applicant)
			throws FinderException;

	public Date getLastValidToForApartment(Integer apartment)
			throws FinderException;

	public Date getLastValidFromForApartment(Integer apartment)
			throws FinderException;

	public Collection findBySearchConditions(String status, Integer complexId,
			Integer buildingId, Integer floorId, Integer typeId,
			Integer categoryId, int order, int returnResultSize,
			int startingIndex) throws FinderException;

	public int countBySearchConditions(String status, Integer complexId,
			Integer buildingId, Integer floorId, Integer typeId,
			Integer categoryId, int order) throws IDOException;

	public Collection findByComplexAndBuildingAndApartmentName(
			Integer complexID, Integer buildingID, String apartmentName)
			throws FinderException;

	public Collection findByComplexAndRented(Integer complexID, boolean rented)
			throws FinderException;

	public Collection findByPersonalID(String ID) throws FinderException;

	public Collection getUnsignedApplicants(String personalID)
			throws FinderException;

	public Collection findByStatusAndValidBeforeDate(String status, Date date)
			throws FinderException;

	public Collection findByStatusAndChangeDate(String status, Date date)
			throws FinderException;

	public Collection findByStatusAndOverLapPeriodMultiples(String[] status,
			Date from, Date to) throws FinderException;

	public Collection findByUserAndStatus(Integer userId, String[] status)
			throws FinderException;

	public Collection findByUserAndStatus(Integer userId, String status)
			throws FinderException;

	public Collection findByUserAndStatusAndRentedBeforeDate(Integer userId,
			String status, Date date) throws FinderException;
}