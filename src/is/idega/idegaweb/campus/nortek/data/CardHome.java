package is.idega.idegaweb.campus.nortek.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface CardHome extends IDOHome {
	public Card create() throws CreateException;

	public Card findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;
}