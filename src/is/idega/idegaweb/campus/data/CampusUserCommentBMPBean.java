package is.idega.idegaweb.campus.data;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;

public class CampusUserCommentBMPBean extends GenericEntity implements
		CampusUserComment {

	private static String ENTITY_NAME = "cam_user_comment";
	private static final String COLUMN_USER = "user_id";
	private static final String COLUMN_COMMENT = "commentstring";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_USER, User.class);
		addAttribute(COLUMN_COMMENT, "Comment", String.class, 1000);
	}

	//getters
	public User getUser() {
		return (User) getColumnValue(COLUMN_USER);
	}
	
	public String getComment() {
		return getStringColumnValue(COLUMN_COMMENT);
	}
	//setters
	public void setUser(User user) {
		setColumn(COLUMN_USER, user);
	}
	
	public void setComment(String comment) {
		setColumn(COLUMN_COMMENT, comment);
	}
	
	//ejb
	public Object ejbFindByUser(User user) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_USER, user);
		
		return idoFindOnePKByQuery(query);
	}
}
