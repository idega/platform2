package is.idega.idegaweb.campus.block.phone.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface CampusPhoneHome extends IDOHome {
	public CampusPhone create() throws CreateException;

	public CampusPhone findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;

	public Collection findByPhoneNumber(String number) throws FinderException;
}