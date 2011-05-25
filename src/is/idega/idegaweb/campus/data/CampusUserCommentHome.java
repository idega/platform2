package is.idega.idegaweb.campus.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import com.idega.user.data.User;
import javax.ejb.FinderException;

public interface CampusUserCommentHome extends IDOHome {
	public CampusUserComment create() throws CreateException;

	public CampusUserComment findByPrimaryKey(Object pk) throws FinderException;

	public CampusUserComment findByUser(User user) throws FinderException;
}