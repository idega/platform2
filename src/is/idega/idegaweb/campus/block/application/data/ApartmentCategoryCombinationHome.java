package is.idega.idegaweb.campus.block.application.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface ApartmentCategoryCombinationHome extends IDOHome {
	public ApartmentCategoryCombination create() throws CreateException;

	public ApartmentCategoryCombination findByPrimaryKey(Object pk)
			throws FinderException;

	public Collection findAll() throws FinderException;
}