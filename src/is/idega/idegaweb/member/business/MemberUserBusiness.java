package is.idega.idegaweb.member.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import com.idega.idegaweb.IWUserContext;
import com.idega.user.data.Group;
import com.idega.user.data.User;


public interface MemberUserBusiness extends com.idega.business.IBOService,com.idega.user.business.UserBusiness
{
 public java.util.Collection getAllClubDivisionsForLeague(com.idega.user.data.Group p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getAllClubGroups()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getAllLeagueGroups()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getAllRegionalUnionGroups()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.Group getClubForGroup(com.idega.user.data.Group p0,com.idega.idegaweb.IWUserContext p1)throws is.idega.idegaweb.member.business.NoClubFoundException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getClubListForUser(com.idega.user.data.User p0)throws is.idega.idegaweb.member.business.NoClubFoundException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getClubMemberNumberForUser(com.idega.user.data.User p0,com.idega.user.data.Group p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean moveUserBetweenDivisions(com.idega.user.data.User p0,com.idega.user.data.Group p1,com.idega.user.data.Group p2,com.idega.util.IWTimestamp p3,com.idega.util.IWTimestamp p4,com.idega.idegaweb.IWUserContext p5)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean sendEmailFromIWMemberSystemAdministrator(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,com.idega.idegaweb.IWUserContext p5)throws javax.mail.MessagingException, java.rmi.RemoteException;
 public boolean setClubMemberNumberForUser(java.lang.String p0,com.idega.user.data.User p1,com.idega.user.data.Group p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public Collection getClubGroupsForRegionUnionGroup(Group regionalUnion) throws RemoteException;
 public boolean isClubUsingTheMemberSystem(Group group) throws RemoteException;
 
 public List getLeaguesListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException;
 public List getFederationListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException;
 public List getUnionListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException;
 public List getClubListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException;
 public List getRegionalUnionListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException;
 public List getGroupListForUserFromTopNodesAndGroupType(User user, String groupType, IWUserContext iwuc) throws RemoteException;
 
 
 
}
