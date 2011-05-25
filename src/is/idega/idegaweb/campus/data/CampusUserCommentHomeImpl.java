package is.idega.idegaweb.campus.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;
import javax.ejb.FinderException;

public class CampusUserCommentHomeImpl extends IDOFactory implements
		CampusUserCommentHome {
	public Class getEntityInterfaceClass() {
		return CampusUserComment.class;
	}

	public CampusUserComment create() throws CreateException {
		return (CampusUserComment) super.createIDO();
	}

	public CampusUserComment findByPrimaryKey(Object pk) throws FinderException {
		return (CampusUserComment) super.findByPrimaryKeyIDO(pk);
	}

	public CampusUserComment findByUser(User user) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((CampusUserCommentBMPBean) entity).ejbFindByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}