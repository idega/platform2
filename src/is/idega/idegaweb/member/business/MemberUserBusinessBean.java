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

import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserBusinessBean;
import com.idega.user.data.Group;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.core.data.Email;

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
	public boolean moveUserBetweenDivisions(User user, Group fromDivisionGroup, Group toDivisionGroup, IWTimestamp term, IWTimestamp init, IWUserContext iwuc) {
		//this method get the parents of the user and finds out which is of the correct type and then uses that.
		//the division that the user is sent to must have a child group of type iwme_temporary
		
		try {
			GroupBusiness groupBiz = getGroupBusiness();
			Collection usersParentGroup = groupBiz.getParentGroupsRecursive(user);
			Collection childrenOfToDivisonGroup = groupBiz.getChildGroups(toDivisionGroup);
			Collection parentsOfToDivisionGroup = groupBiz.getParentGroupsRecursive(toDivisionGroup);

			Group fromLeagueGroup = groupBiz.getGroupByGroupID(Integer.parseInt(fromDivisionGroup.getMetaData(IWMemberConstants.META_DATA_LEAGUE_CONNECTION)));
			Group toLeagueGroup = groupBiz.getGroupByGroupID(Integer.parseInt(toDivisionGroup.getMetaData(IWMemberConstants.META_DATA_LEAGUE_CONNECTION)));
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
							fromRegionalUnionGroup = parentGroup;
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
	private boolean sendEmailsForMembersTransfer(User user, Group fromDivisionGroup, Group toDivisionGroup, Group fromLeagueGroup, Group toLeagueGroup, Group fromRegionalUnionGroup, Group toRegionalUnionGroup, IWTimestamp term, IWTimestamp init, IWUserContext iwuc) {
		
		if(!fromLeagueGroup.equals(toLeagueGroup)){
			System.err.println("MemberUserBusiness : Error transfering user because the leagues are not the same! from: "+fromLeagueGroup.getName()+" to: "+toLeagueGroup.getName());
			return false;
		}
		
		
		IWResourceBundle iwrb = this.getIWApplicationContext().getApplication().getBundle(IW_BUNDLE_IDENTIFIER).getResourceBundle(iwuc.getCurrentLocale());
		String toEmailAddress;
		
		String subject = iwrb.getLocalizedString("member_transfer.email_subject","IWMember transfer announcement");
		StringBuffer theMessageBody = new StringBuffer();
		try {
			theMessageBody.append(iwrb.getLocalizedString("member_transfer.email_body_automatic_message_text","This is an automatic confirmation message for a member transfer.\n"))
			.append(iwrb.getLocalizedString("member_transfer.email_main_text","The member stated in this email will transfered from : "))
			.append(getGroupBusiness().getNameOfGroupWithParentName(fromDivisionGroup))
			.append(" to : ").append(getGroupBusiness().getNameOfGroupWithParentName(toDivisionGroup)).append(".\n");
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
		if(userEmails!=null && userEmails.isEmpty()){
			toEmailAddress = ((Email) userEmails.iterator().next()).getEmailAddress();
			try {
				sendEmailFromIWMemberSystemAdministrator(toEmailAddress, null, null, subject, body, iwuc);
			}
			catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		
		Collection leagueEmails = fromLeagueGroup.getEmails();//is same as to league
		if(leagueEmails!=null && leagueEmails.isEmpty()){
			toEmailAddress = ((Email) leagueEmails.iterator().next()).getEmailAddress();
			try {
				sendEmailFromIWMemberSystemAdministrator(toEmailAddress, null, null, subject, body, iwuc);
			}
			catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		
		Collection toRegionalEmails = toRegionalUnionGroup.getEmails();
		if(toRegionalEmails!=null && toRegionalEmails.isEmpty()){
			toEmailAddress = ((Email) toRegionalEmails.iterator().next()).getEmailAddress();
			try {
				sendEmailFromIWMemberSystemAdministrator(toEmailAddress, null, null, subject, body, iwuc);
			}
			catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		
		Collection fromRegionalEmails = fromRegionalUnionGroup.getEmails();
		if(fromRegionalEmails!=null && fromRegionalEmails.isEmpty()){
			toEmailAddress = ((Email) fromRegionalEmails.iterator().next()).getEmailAddress();
			try {
				sendEmailFromIWMemberSystemAdministrator(toEmailAddress, null, null, subject, body, iwuc);
			}
			catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		

		return true;
	}
	
	
	public boolean sendEmailFromIWMemberSystemAdministrator(String toEmailAddress, String CC, String BCC,String subject, String theMessageBody, IWUserContext iwuc) throws MessagingException{
		String systemEmailAddress = this.getIWApplicationContext().getApplicationSettings().getProperty(IWMemberConstants.APPLICATION_PARAMETER_ADMINISTRATOR_MAIN_EMAIL);
		String systemMailServer = this.getIWApplicationContext().getApplicationSettings().getProperty(IWMemberConstants.APPLICATION_PARAMETER_MAIL_SERVER);
		com.idega.util.SendMail.send(systemEmailAddress,toEmailAddress,CC,BCC,systemMailServer,subject,theMessageBody);
		
		return true;
	}

	//temp refactor this class to MemberBusiness or move this method to that class
	/**
	 * @return A collection of groups (of the type iwme_club_division)
	 */
	public Collection getAllClubDivisionsForLeague(Group league){
		Collection groups = null;
		
		try {
			groups  = getGroupHome().findGroupsByMetaData(IWMemberConstants.META_DATA_LEAGUE_CONNECTION,league.getPrimaryKey().toString());
		}
		catch (EJBException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		
		return groups;
	}
	
	public List getLeaguesListForUser(User user, IWUserContext iwuc) throws RemoteException{
		Collection tops = getUsersTopGroupNodesByViewAndOwnerPermissions(user,iwuc);
		List list = new Vector();
		if(tops!=null && !tops.isEmpty()){
			Iterator iter = tops.iterator();
			while (iter.hasNext()) {
				Group group = (Group) iter.next();
				if(IWMemberConstants.GROUP_TYPE_LEAGUE.equals(group.getGroupType())){
					list.add(group);
				}
			}
			
		}
		return list;
		
		
	}
	
	
	
	
	
}