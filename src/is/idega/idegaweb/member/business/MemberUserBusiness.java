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
	public boolean moveUserBetweenDivisions(User user, Group fromDiv, Group toDiv, IWTimestamp term, IWTimestamp init, IWUserContext iwuc) throws RemoteException;
	public Collection getAllClubDivisionsForLeague(Group league) throws RemoteException;
	/**
	 * @param user
	 * @return a Group of the type iwme_league
	 */
	public List getLeaguesListForUser(User user, IWUserContext iwuc) throws RemoteException;
	public boolean sendEmailFromIWMemberSystemAdministrator(String toEmailAddress, String CC, String BCC,String subject, String theMessageBody, IWUserContext iwuc) throws MessagingException, RemoteException;
	public Group getClubForGroup(Group group, IWUserContext iwuc) throws NoClubFoundException, RemoteException;
	public List getClubListForUser(User user) throws NoClubFoundException,RemoteException;
	public boolean setClubMemberNumberForUser(String number, User user, Group club) throws RemoteException;
	
	
}
