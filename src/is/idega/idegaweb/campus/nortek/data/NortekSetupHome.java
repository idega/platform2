package is.idega.idegaweb.campus.nortek.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface NortekSetupHome extends IDOHome {
	public NortekSetup create() throws CreateException;

	public NortekSetup findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;

	public NortekSetup findEntry() throws FinderException;
}