package is.idega.idegaweb.member.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import javax.mail.MessagingException;

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
	 * @return success
	 */
	public boolean moveUserBetweenDivisions(User user, Group fromDiv, Group toDiv, IWTimestamp term, IWTimestamp init, IWUserContext iwuc);
	public Collection getAllClubDivisionsForLeague(Group league);
	/**
	 * @param user
	 * @return a Group of the type iwme_league
	 */
	public List getLeaguesListForUser(User user, IWUserContext iwuc) throws RemoteException;
	public boolean sendEmailFromIWMemberSystemAdministrator(String toEmailAddress, String CC, String BCC,String subject, String theMessageBody, IWUserContext iwuc) throws MessagingException;
}
