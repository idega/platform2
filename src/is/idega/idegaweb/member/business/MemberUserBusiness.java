package is.idega.idegaweb.member.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import com.idega.business.IBOService;
import com.idega.idegaweb.IWUserContext;
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
	public void moveUserBetweenDivisions(User user, Group fromDiv, Group toDiv, IWTimestamp term, IWTimestamp init);
	public Collection getAllClubDivisionsForLeague(Group league);
	/**
	 * @param user
	 * @return a Group of the type iwme_league
	 */
	public List getLeaguesListForUser(User user, IWUserContext iwuc) throws RemoteException;
}
