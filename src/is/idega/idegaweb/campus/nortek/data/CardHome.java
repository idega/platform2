package is.idega.idegaweb.campus.nortek.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import com.idega.user.data.User;
import javax.ejb.FinderException;
import java.util.Collection;

public interface CardHome extends IDOHome {
	public Card create() throws CreateException;

	public Card findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;

	public Card findByUser(User user) throws FinderException;

	public Collection findAllByValid(boolean valid) throws FinderException;
}