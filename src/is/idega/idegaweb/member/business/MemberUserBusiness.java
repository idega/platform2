/*
 * $Id: MemberUserBusiness.java,v 1.10 2005/01/04 15:44:23 palli Exp $
 * Created on Jan 4, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
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


/**
 * 
 *  Last modified: $Date: 2005/01/04 15:44:23 $ by $Author: palli $
 * 
 * @author <a href="mailto:palli@idega.com">palli</a>
 * @version $Revision: 1.10 $
 */
public interface MemberUserBusiness extends IBOService, UserBusiness {

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#moveUserBetweenDivisions
	 */
	public boolean moveUserBetweenDivisions(User user, Group fromDivisionGroup, Group toDivisionGroup,
			IWTimestamp term, IWTimestamp init, IWUserContext iwuc) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getRegionalUnionGroupForClubGroup
	 */
	public Group getRegionalUnionGroupForClubGroup(Group club) throws NoRegionalUnionFoundException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getFederationGroupForClubGroup
	 */
	public Group getFederationGroupForClubGroup(Group club) throws NoFederationFoundException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getLeagueGroupListForClubGroup
	 */
	public List getLeagueGroupListForClubGroup(Group club) throws NoLeagueFoundException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#sendEmailFromIWMemberSystemAdministrator
	 */
	public boolean sendEmailFromIWMemberSystemAdministrator(String toEmailAddress, String CC, String BCC,
			String subject, String theMessageBody) throws MessagingException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getAllClubDivisionsForLeague
	 */
	public Collection getAllClubDivisionsForLeague(Group league) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getLeaguesListForUserFromTopNodes
	 */
	public List getLeaguesListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getFederationListForUserFromTopNodes
	 */
	public List getFederationListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getUnionListForUserFromTopNodes
	 */
	public List getUnionListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getClubListForUserFromTopNodes
	 */
	public List getClubListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getRegionalUnionListForUserFromTopNodes
	 */
	public List getRegionalUnionListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getGroupListForUserFromTopNodesAndGroupType
	 */
	public List getGroupListForUserFromTopNodesAndGroupType(User user, String groupType, IWUserContext iwuc)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getAllRegionalUnionGroups
	 */
	public Collection getAllRegionalUnionGroups() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getAllLeagueGroups
	 */
	public Collection getAllLeagueGroups() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getAllClubGroups
	 */
	public Collection getAllClubGroups() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getClubGroupsForRegionUnionGroup
	 */
	public Collection getClubGroupsForRegionUnionGroup(Group regionalUnion) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getClubListForUser
	 */
	public List getClubListForUser(User user) throws NoClubFoundException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getClubForGroup
	 */
	public Group getClubForGroup(Group group) throws NoClubFoundException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getDivisionForClub
	 */
	public Group getDivisionForClub(Group club) throws NoDivisionFoundException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getClubMemberNumberForUser
	 */
	public String getClubMemberNumberForUser(User user, Group club) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#isClubUsingTheMemberSystem
	 */
	public boolean isClubUsingTheMemberSystem(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#setClubMemberNumberForUser
	 */
	public boolean setClubMemberNumberForUser(String number, User user, Group club) throws RemoteException;
}
