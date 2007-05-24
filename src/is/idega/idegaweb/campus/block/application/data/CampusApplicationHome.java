package is.idega.idegaweb.campus.block.application.data;


import com.idega.data.IDOException;
import com.idega.data.IDORelationshipException;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface CampusApplicationHome extends IDOHome {
	public CampusApplication create() throws CreateException;

	public CampusApplication findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAllByApplicationId(int id) throws FinderException;

	public Collection findBySubjectAndStatus(Integer subjectID, String status,
			String order) throws FinderException;

	public Collection findBySubjectAndStatus(Integer subjectID, String status,
			String order, int numberOfRecords, int startingIndex)
			throws FinderException;

	public int getCountBySubjectAndStatus(Integer subjectID, String status)
			throws IDORelationshipException, IDOException;

	public Collection findAll() throws FinderException;

	public Collection findBySQL(String sql) throws FinderException;

	public Collection findByApartmentTypeAndComplex(Integer typeId,
			Integer complexID) throws FinderException;

	public Collection findBySubcategoryAndComplex(Integer subcatId,
			Integer complexID) throws FinderException;
}