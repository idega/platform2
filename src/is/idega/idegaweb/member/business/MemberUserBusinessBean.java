package is.idega.idegaweb.member.business;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.mail.MessagingException;

import com.idega.core.contact.data.Email;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserBusinessBean;
import com.idega.user.data.Group;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;

/**
 * Description:	Use this business class to handle member information
 * Copyright:    Copyright (c) 2003
 * Company:      Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 * @version 1.1
 */
public class MemberUserBusinessBean extends UserBusinessBean implements MemberUserBusiness, UserBusiness {
	
	
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.business.MemberUserBusiness#moveUserBetweenDivisions(com.idega.user.data.User, com.idega.user.data.Group, com.idega.user.data.Group, com.idega.util.IWTimestamp, com.idega.util.IWTimestamp)
	 */
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	public boolean moveUserBetweenDivisions(User user, Group fromDivisionGroup, Group toDivisionGroup, IWTimestamp term, IWTimestamp init, IWUserContext iwuc)  throws RemoteException{
		//this method get the parents of the user and finds out which is of the correct type and then uses that.
		//the division that the user is sent to must have a child group of type iwme_temporary
		
		try {
			GroupBusiness groupBiz = getGroupBusiness();
			Collection usersParentGroup = groupBiz.getParentGroups(user);
			Collection childrenOfToDivisonGroup = groupBiz.getChildGroups(toDivisionGroup);
			Collection parentsOfToDivisionGroup = groupBiz.getParentGroupsRecursive(toDivisionGroup);

			Group fromLeagueGroup = groupBiz.getGroupByGroupID(Integer.parseInt(fromDivisionGroup.getMetaData(IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION)));
			Group toLeagueGroup = groupBiz.getGroupByGroupID(Integer.parseInt(toDivisionGroup.getMetaData(IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION)));
			Group fromRegionalUnionGroup = null;
			Group toRegionalUnionGroup = null; 
			
			//find the player groups relations and set them to passive_pending
			if (usersParentGroup != null && !usersParentGroup.isEmpty() && childrenOfToDivisonGroup!=null && !childrenOfToDivisonGroup.isEmpty() ) {//user must have parents!
				Iterator iter = usersParentGroup.iterator();
				
				boolean existsInFromDivision = false;
				
				//handle from division
				while (iter.hasNext()) {
					Group parent = (Group) iter.next();
					String type = parent.getGroupType();
					if ( IWMemberConstants.GROUP_TYPE_CLUB_PLAYER.equals(type) ) {
						Collection par = groupBiz.getParentGroupsRecursive(parent);
						if (par.contains(fromDivisionGroup)) {
							
							//find the regional union
							Iterator parIter = par.iterator();
							while (parIter.hasNext()) {
								Group parentGroup = (Group) parIter.next();
								if( IWMemberConstants.GROUP_TYPE_REGIONAL_UNION.equals(parentGroup.getGroupType()) ){
									fromRegionalUnionGroup = parentGroup;
									break;
								}
							}
							
							//change all relation within that division to pending. To much maybe?
							existsInFromDivision = true;
							Collection col = groupBiz.getGroupRelationHome().findGroupsRelationshipsContainingBiDirectional( ((Integer)fromDivisionGroup.getPrimaryKey()).intValue(), ((Integer)parent.getPrimaryKey()).intValue() , "ST_ACTIVE" ); //Status liklega otharfi
							if(col!=null && !col.isEmpty()){
								Iterator iterator = col.iterator();
								while (iterator.hasNext()) {
									GroupRelation rel = (GroupRelation) iterator.next();
									rel.setPassivePending();
									rel.setTerminationDate(term.getTimestamp());
									rel.store();
									//set passive by?	
								}
							}
						}
					}
				}
						
//			handle to division
				if(existsInFromDivision){
					
					//find the regional union
					Iterator parIter2 = parentsOfToDivisionGroup.iterator();
					while (parIter2.hasNext()) {
						Group parentGroup = (Group) parIter2.next();
						if( IWMemberConstants.GROUP_TYPE_REGIONAL_UNION.equals(parentGroup.getGroupType()) ){
							toRegionalUnionGroup = parentGroup;
							break;
						}
					}
									
					//set the users relations to the new divisions temporary group to active_pending
					Iterator iter2 = childrenOfToDivisonGroup.iterator();
	
					while (iter2.hasNext()) {
						Group child = (Group) iter2.next();
						String type = child.getGroupType();
						if (type.equals(IWMemberConstants.GROUP_TYPE_TEMPORARY)) {
							
							GroupRelation rel = groupBiz.getGroupRelationHome().create();
							rel.setRelatedGroup(user);
							rel.setGroup(child);
							rel.setRelationshipType(IWMemberConstants.GROUP_RELATION_TYPE_PARENT);
							rel.setActivePending();
							rel.setInitiationDate(init.getTimestamp());
							rel.store();
							break;//should only have one temp group!
						}
					}
					
					return sendEmailsForMembersTransfer(user,fromDivisionGroup,toDivisionGroup,fromLeagueGroup,toLeagueGroup, fromRegionalUnionGroup, toRegionalUnionGroup, term, init, iwuc );	
				}
						
			}
				
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}
	
	/**
	 * Sends out a report via email to all parties concerned
	 * @return boolean true if no errors occurred
	 */
	private boolean sendEmailsForMembersTransfer(User user, Group fromDivisionGroup, Group toDivisionGroup, Group fromLeagueGroup, Group toLeagueGroup, Group fromRegionalUnionGroup, Group toRegionalUnionGroup, IWTimestamp term, IWTimestamp init, IWUserContext iwuc)  throws RemoteException{
		
		if(!fromLeagueGroup.equals(toLeagueGroup)){
			System.err.println("MemberUserBusiness : Error transfering user because the leagues are not the same! from: "+fromLeagueGroup.getName()+" to: "+toLeagueGroup.getName());
			return false;
		}
		
		
		IWResourceBundle iwrb = this.getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getResourceBundle(iwuc.getCurrentLocale());
		String toEmailAddress;
		
		String subject = iwrb.getLocalizedString("member_transfer.email_subject","IWMember transfer announcement");
		StringBuffer theMessageBody = new StringBuffer();
		try {
			theMessageBody.append(iwrb.getLocalizedString("member_transfer.email_body_automatic_message_text","This is an automatic confirmation message for a member transfer.")).append("\n\n")
			.append(iwrb.getLocalizedString("member_transfer.email_main_text","The member stated in this email will transfered from : "))
			.append(getGroupBusiness().getNameOfGroupWithParentName(fromDivisionGroup))
			.append(iwrb.getLocalizedString("member_transfer.email_main_to"," to : ")).append(getGroupBusiness().getNameOfGroupWithParentName(toDivisionGroup)).append(".\n");
		}
		catch (RemoteException e1) {
			
			e1.printStackTrace();
			return false;
		}
		
		theMessageBody.append(iwrb.getLocalizedString("member_transfer.email_main_text_user_name","User : ")).append(user.getName()).append("\n")
		.append(iwrb.getLocalizedString("member_transfer.email_main_text_user_pin","Social security number : ")).append(user.getPersonalID()).append("\n")
		.append(iwrb.getLocalizedString("member_transfer.email_main_text_date_from","Date from : ")).append(term.toString()).append("\n")
		.append(iwrb.getLocalizedString("member_transfer.email_main_text_date_to","Date to : ")).append(init.toString()).append("\n");
		
		String body = theMessageBody.toString();
		
		Collection userEmails = user.getEmails();
		if(userEmails!=null && !userEmails.isEmpty()){
			toEmailAddress = ((Email) userEmails.iterator().next()).getEmailAddress();
			try {
				sendEmailFromIWMemberSystemAdministrator(toEmailAddress, null, null, subject, body);
			}
			catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		
		Collection leagueEmails = fromLeagueGroup.getEmails();//is same as to league
		if(leagueEmails!=null && !leagueEmails.isEmpty()){
			toEmailAddress = ((Email) leagueEmails.iterator().next()).getEmailAddress();
			try {
				sendEmailFromIWMemberSystemAdministrator(toEmailAddress, null, null, subject, body);
			}
			catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		
		if(toRegionalUnionGroup!=null){
			Collection toRegionalEmails = toRegionalUnionGroup.getEmails();
			if(toRegionalEmails!=null && !toRegionalEmails.isEmpty()){
				toEmailAddress = ((Email) toRegionalEmails.iterator().next()).getEmailAddress();
				try {
					sendEmailFromIWMemberSystemAdministrator(toEmailAddress, null, null, subject, body);
				}
				catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(toRegionalUnionGroup!=null){
			Collection fromRegionalEmails = fromRegionalUnionGroup.getEmails();
			if(fromRegionalEmails!=null && !fromRegionalEmails.isEmpty()){
				toEmailAddress = ((Email) fromRegionalEmails.iterator().next()).getEmailAddress();
				try {
					sendEmailFromIWMemberSystemAdministrator(toEmailAddress, null, null, subject, body);
				}
				catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
		

		return true;
	}
	
	
	/**
	 * Checks the groups parents for a regional union and returns the first it finds.
	 * @param club (Group)
	 * @return the regional union for that club if it exists, otherwise throws an exception
	 * @throws NoRegionalUnionFoundException
	 */
	public Group getRegionalUnionGroupForClubGroup(Group club) throws NoRegionalUnionFoundException{
		List parents = club.getParentGroups();
		Iterator it = parents.iterator();
		Group regionalUnionGroup = null;
		
		if (it != null) {
			while (it.hasNext()) {
				Group parent = (Group) it.next();
				if (IWMemberConstants.GROUP_TYPE_REGIONAL_UNION.equals(parent.getGroupType())){
					regionalUnionGroup = parent;
				}			
			}
		}
		
		if(regionalUnionGroup==null){
			throw new NoRegionalUnionFoundException(club.getName());
		}
		else{
			return regionalUnionGroup;
		}
	}
	
	/**
	 * TEMP IMPLEMENTATION : Gets all groups of the type iwme_federation if there are more than one than it only returns the first one!
	 * @param club (Group)
	 * @return the federation union for that club if it exists, otherwise throws an exception
	 * @throws NoFederationFoundException
	 */
	public Group getFederationGroupForClubGroup(Group club) throws NoFederationFoundException, RemoteException{
		try {
			Collection federations = this.getGroupBusiness().getGroupHome().findGroupsByType(IWMemberConstants.GROUP_TYPE_FEDERATION);
			return (Group) federations.iterator().next();
			
		} catch (FinderException e) {
			e.printStackTrace();
			throw new NoFederationFoundException(club.getName());
		}
		
	}
	
	/**
	 * Checks the groups children for club divisons and then gets the division league connection and add to the list it returns
	 * @param club (Group)
	 * @return a list of leagues for that club if it has connections to any, otherwise throws an exception
	 * @throws NoLeagueFoundException
	 */
	public List getLeagueGroupListForClubGroup(Group club) throws NoLeagueFoundException, RemoteException{
		String[] divisionType = {IWMemberConstants.GROUP_TYPE_CLUB_DIVISION};
		List children = club.getChildGroups(divisionType,true);
		List list = new ArrayList();
		
		if(children!=null && !children.isEmpty()){
			
			Iterator it = children.iterator();
			
			while (it.hasNext()) {
				Group div = (Group) it.next();
				String leagueId = div.getMetaData(IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION);		
				if(leagueId!=null){
					int id;
					try {
						id = Integer.parseInt(leagueId);
						Group league;
						league = this.getGroupBusiness().getGroupByGroupID(id);
						list.add(league);
					}
					catch (NumberFormatException e) {
						e.printStackTrace();
					}
					catch (FinderException e1) {
						e1.printStackTrace();
					}

				}
			}
		}
		
		
		if(list.isEmpty()){
			throw new NoLeagueFoundException(club.getName());
		}
		else{
			return list;
		}
		
	}
	
	
	
	
	public boolean sendEmailFromIWMemberSystemAdministrator(String toEmailAddress, String CC, String BCC,String subject, String theMessageBody) throws MessagingException{
		String systemEmailAddress = this.getIWApplicationContext().getApplicationSettings().getProperty(IWMemberConstants.APPLICATION_PARAMETER_ADMINISTRATOR_MAIN_EMAIL);
		String systemMailServer = this.getIWApplicationContext().getApplicationSettings().getProperty(IWMemberConstants.APPLICATION_PARAMETER_MAIL_SERVER);
		com.idega.util.SendMail.send(systemEmailAddress,toEmailAddress,CC,BCC,systemMailServer,subject,theMessageBody);
		
		return true;
	}
	

	//temp refactor this class to MemberBusiness or move this method to that class
	/**
	 * @return A collection of groups (of the type iwme_club_division)
	 */
	public Collection getAllClubDivisionsForLeague(Group league) throws RemoteException{
		Collection groups = null;
		
		try {
			groups  = getGroupHome().findGroupsByMetaData(IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION,league.getPrimaryKey().toString());
		}
		catch (EJBException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		
		return groups;
	}
	
	/*
	 * Return a list of League groups if the user has a league as a top node.
	 */
	public List getLeaguesListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException{
		return getGroupListForUserFromTopNodesAndGroupType(user,IWMemberConstants.GROUP_TYPE_LEAGUE,iwuc);
	}
	
	/*
	 * Return a list of Federation groups if the user has a Federation as a top node.
	 */
	public List getFederationListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException{
		return getGroupListForUserFromTopNodesAndGroupType(user,IWMemberConstants.GROUP_TYPE_FEDERATION,iwuc);
	}
	
	/*
	 * Return a list of Union groups if the user has a Union as a top node.
	 */
	public List getUnionListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException{
		return getGroupListForUserFromTopNodesAndGroupType(user,IWMemberConstants.GROUP_TYPE_UNION,iwuc);
	}
	
	/*
	  * Return a list of Club groups if the user has a club as a top node.
	 */
	public List getClubListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException{
		return getGroupListForUserFromTopNodesAndGroupType(user,IWMemberConstants.GROUP_TYPE_CLUB,iwuc);
	}
	
	/*
	 * Return a list of Regional Union groups if the user has a Regional Union as a top node.
	 */
	public List getRegionalUnionListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException{
		return getGroupListForUserFromTopNodesAndGroupType(user,IWMemberConstants.GROUP_TYPE_REGIONAL_UNION,iwuc);
	}
	
	public List getGroupListForUserFromTopNodesAndGroupType(User user, String groupType, IWUserContext iwuc) throws RemoteException{
		Collection tops = getUsersTopGroupNodesByViewAndOwnerPermissions(user,iwuc);
		List list = new Vector();
		if(tops!=null && !tops.isEmpty()){
			Iterator iter = tops.iterator();
			while (iter.hasNext()) {
				Group group = (Group) iter.next();
				if(groupType.equals(group.getGroupType())){
					list.add(group);
				}
			}
			
		}
		return list;
		
	}
	

	
	/** 
	 * @return All groups with the type iwme_regional_union
	 * @throws RemoteException
	 */
	public Collection getAllRegionalUnionGroups() throws RemoteException{
		try {
			return this.getGroupBusiness().getGroupHome().findGroupsByType(IWMemberConstants.GROUP_TYPE_REGIONAL_UNION);
		}
		catch (FinderException e) {
			return ListUtil.getEmptyList();
		}
	} 
	
	/** 
	 * @return All groups with the type iwme_league
	 * @throws RemoteException
	 */
	public Collection getAllLeagueGroups() throws RemoteException{
		try {
			return this.getGroupBusiness().getGroupHome().findGroupsByType(IWMemberConstants.GROUP_TYPE_LEAGUE);
		}
		catch (FinderException e) {
			return ListUtil.getEmptyList();
		}
	} 
	
	/** 
	 * @return All groups with the type iwme_club
	 * @throws RemoteException
	 */
	public Collection getAllClubGroups() throws RemoteException{
		try {
			return this.getGroupBusiness().getGroupHome().findGroupsByType(IWMemberConstants.GROUP_TYPE_CLUB);
		}
		catch (FinderException e) {
			return ListUtil.getEmptyList();
		}
	} 
	
	
	
	/** 
	 * @return All groups with the type iwme_club that are children of the supplied regional union group
	 */
	public Collection getClubGroupsForRegionUnionGroup(Group regionalUnion) throws RemoteException{
		String[] clubType = { IWMemberConstants.GROUP_TYPE_CLUB };
		return regionalUnion.getChildGroups(clubType,true);
	} 
	
	
	/*
	 * Returns a list of all the clubs the user is a member of.
	 */
	public List getClubListForUser(User user) throws NoClubFoundException,RemoteException{
		Collection parents = getGroupBusiness().getParentGroupsRecursive(user);
		List list = new Vector();
		if(parents!=null && !parents.isEmpty()){
			Iterator iter = parents.iterator();
			while (iter.hasNext()) {
				Group group = (Group) iter.next();
				if(IWMemberConstants.GROUP_TYPE_CLUB.equals(group.getGroupType())){
					list.add(group);
				}
			}
		}
		
		if(list.isEmpty()){
			//if no club is found we throw the exception
			throw new NoClubFoundException(user.getName());
		}
		else return list;
	}
	
	
	
	/*
		* Returns the club that is a parent for this group.
	 */
	public Group getClubForGroup(Group group) throws NoClubFoundException, RemoteException{
		Collection parents = getGroupBusiness().getParentGroupsRecursive(group);

		if(parents!=null && !parents.isEmpty()){
			Iterator iter = parents.iterator();
			while (iter.hasNext()) {
				Group parentGroup = (Group) iter.next();
				if(IWMemberConstants.GROUP_TYPE_CLUB.equals(parentGroup.getGroupType())){
					return parentGroup;//there should only be one
				}
			}
		}
		
		//if no club is found we throw the exception
		throw new NoClubFoundException(group.getName());		
	}
	
	/**
	 * A method to find the first Division for a club.
	 * @param club
	 * @return
	 */
	public Group getDivisionForClub(Group club) throws NoDivisionFoundException, RemoteException {
		Collection children = club.getChildren();
		
		if (children != null && !children.isEmpty()) {
			Iterator it = children.iterator();
			while (it.hasNext()) {
				Group child = (Group) it.next();
				if (child.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION)) {
					return child;
				}
			}
		}
		
		//if no club is found we throw the exception
		throw new NoDivisionFoundException(club.getName());
	}

	
	public String getClubMemberNumberForUser(User user, Group club) throws RemoteException{
		String id = user.getMetaData(IWMemberConstants.META_DATA_USER_CLUB_MEMBER_NUMBER_PREFIX+club.getPrimaryKey().toString());
		if(id!=null){
			return id;
		}else{
			return null;
		}	
	}
	
	/**
	 * Checks the metadata (IWMemberConstants.META_DATA_CLUB_USING_SYSTEM) of a group to see if is using the member system
	 * @param group
	 * @return a boolean
	 */
	public boolean isClubUsingTheMemberSystem(Group group) throws RemoteException{
		String using = group.getMetaData(IWMemberConstants.META_DATA_CLUB_USING_SYSTEM);
		return ( using != null && "TRUE".equals(using.toUpperCase()));
	}
	
	/**
	 * @return false if number is already taken, else true
	 */
	public synchronized boolean setClubMemberNumberForUser(String number, User user, Group club) throws RemoteException{
		
		boolean setNumber = false;
		String clubId = club.getPrimaryKey().toString();
		
		try {
			Collection users = getUserHome().findUsersByMetaData(IWMemberConstants.META_DATA_USER_CLUB_MEMBER_NUMBER_PREFIX+clubId,number);
			Iterator iter = users.iterator();
			
			while (iter.hasNext()) {
				User thingy = (User) iter.next();
				if(thingy.equals(user)){
					setNumber = true;//updating
				}
				break;//only one user should have this number
			}
		}
		catch (EJBException e) {
			e.printStackTrace();
			return false;
		}
		catch (FinderException e) {
			setNumber = true;
		}
		
		if(setNumber){
			user.setMetaData(IWMemberConstants.META_DATA_USER_CLUB_MEMBER_NUMBER_PREFIX+clubId,number);
			user.store();
		}
		
		return true;
	}
	
	
	
	
	
}