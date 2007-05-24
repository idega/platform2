package is.idega.idegaweb.campus.block.application.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface AppliedHome extends IDOHome {
	public Applied create() throws CreateException;

	public Applied findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;

	public Collection findByApplicationID(Integer ID) throws FinderException;

	public Collection findBySQL(String sql) throws FinderException;
}