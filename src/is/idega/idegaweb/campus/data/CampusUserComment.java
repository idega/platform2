package is.idega.idegaweb.campus.data;


import com.idega.data.IDOEntity;
import com.idega.user.data.User;

public interface CampusUserComment extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.campus.data.CampusUserCommentBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see is.idega.idegaweb.campus.data.CampusUserCommentBMPBean#getComment
	 */
	public String getComment();

	/**
	 * @see is.idega.idegaweb.campus.data.CampusUserCommentBMPBean#setUser
	 */
	public void setUser(User user);

	/**
	 * @see is.idega.idegaweb.campus.data.CampusUserCommentBMPBean#setComment
	 */
	public void setComment(String comment);
}