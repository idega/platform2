package is.idega.idegaweb.campus.block.application.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface WaitingListHome extends IDOHome {
	public WaitingList create() throws CreateException;

	public WaitingList findByPrimaryKey(Object pk) throws FinderException;

	public Collection findByApartmentSubcategoryForApplicationType(int subcatId)
			throws FinderException;

	public Collection findByApartmentSubcategoryForTransferType(int subcatId)
			throws FinderException;

	public Collection findByApartmentSubcategory(int subcatId)
			throws FinderException;

	public Collection findNextForTransferByApartmentSubcategory(int subcatId,
			int orderedFrom, String setTranserferToPriorityLevel)
			throws FinderException;

	public Collection findByApartmentSubcategory(int[] subcatId)
			throws FinderException;

	public Collection findByApplicantID(Integer ID) throws FinderException;

	public Collection findBySQL(String sql) throws FinderException;
}