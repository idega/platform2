package is.idega.idegaweb.member.business;


public interface MemberUserBusiness extends com.idega.business.IBOService,com.idega.user.business.UserBusiness
{
 public java.util.Collection getAllClubDivisionsForLeague(com.idega.user.data.Group p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getAllClubGroups()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getAllLeagueGroups()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getAllRegionalUnionGroups()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.Group getClubForGroup(com.idega.user.data.Group p0,com.idega.idegaweb.IWUserContext p1)throws is.idega.idegaweb.member.business.NoClubFoundException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getClubGroupsForRegionUnionGroup(com.idega.user.data.Group p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getClubListForUser(com.idega.user.data.User p0)throws is.idega.idegaweb.member.business.NoClubFoundException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getClubListForUserFromTopNodes(com.idega.user.data.User p0,com.idega.idegaweb.IWUserContext p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getClubMemberNumberForUser(com.idega.user.data.User p0,com.idega.user.data.Group p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.Group getFederationGroupForClubGroup(com.idega.user.data.Group p0)throws is.idega.idegaweb.member.business.NoFederationFoundException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getFederationListForUserFromTopNodes(com.idega.user.data.User p0,com.idega.idegaweb.IWUserContext p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getGroupListForUserFromTopNodesAndGroupType(com.idega.user.data.User p0,java.lang.String p1,com.idega.idegaweb.IWUserContext p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getLeagueGroupListForClubGroup(com.idega.user.data.Group p0)throws is.idega.idegaweb.member.business.NoLeagueFoundException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getLeaguesListForUserFromTopNodes(com.idega.user.data.User p0,com.idega.idegaweb.IWUserContext p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.Group getRegionalUnionGroupForClubGroup(com.idega.user.data.Group p0)throws is.idega.idegaweb.member.business.NoRegionalUnionFoundException, java.rmi.RemoteException;
 public java.util.List getRegionalUnionListForUserFromTopNodes(com.idega.user.data.User p0,com.idega.idegaweb.IWUserContext p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getUnionListForUserFromTopNodes(com.idega.user.data.User p0,com.idega.idegaweb.IWUserContext p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean isClubUsingTheMemberSystem(com.idega.user.data.Group p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean moveUserBetweenDivisions(com.idega.user.data.User p0,com.idega.user.data.Group p1,com.idega.user.data.Group p2,com.idega.util.IWTimestamp p3,com.idega.util.IWTimestamp p4,com.idega.idegaweb.IWUserContext p5)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean sendEmailFromIWMemberSystemAdministrator(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4)throws javax.mail.MessagingException, java.rmi.RemoteException;
 public boolean setClubMemberNumberForUser(java.lang.String p0,com.idega.user.data.User p1,com.idega.user.data.Group p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
