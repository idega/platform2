package is.idega.idegaweb.member.business;

import com.idega.business.IBOService;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


public interface MemberUserBusiness extends IBOService,UserBusiness
{

	/**
	 * @param user
	 * @param fromDiv
	 * @param toDiv
	 * @param term
	 * @param init
	 */
	void moveUserBetweenDivisions(User user, Group fromDiv, Group toDiv, IWTimestamp term, IWTimestamp init);
}
