/*
 * $Id: GolfUserPluginBusinessBean.java,v 1.13.4.4 2006/03/30 14:55:22 eiki Exp $
 * Created on Nov 15, 2004
 * 
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package is.idega.idegaweb.golf.business.plugin;

import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.presentation.GolferTab;
import is.idega.idegaweb.golf.util.GolfConstants;
import is.idega.idegaweb.member.business.MemberUserBusiness;
import is.idega.idegaweb.member.business.NoClubFoundException;
import is.idega.idegaweb.member.business.NoDivisionFoundException;
import is.idega.idegaweb.member.util.IWMemberConstants;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOServiceBean;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.business.AddressBusiness;
import com.idega.core.location.data.Address;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.FileUtil;
import com.idega.util.ListUtil;

/**
 * A user application plugin for various golf specific stuff such as the Golfer
 * Info tab. Last modified: $Date: 2006/03/30 14:55:22 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">Eirikur S. Hrafnsson</a>
 * @version $Revision: 1.13.4.4 $
 */
public class GolfUserPluginBusinessBean extends IBOServiceBean implements UserGroupPlugInBusiness,
		GolfUserPluginBusiness {

	public static final String WS_ERROR_MSG_4_NO_PERSON_FOUND_WITH_THE_SOCIAL_SECURITY_NUMBER = "-4, No person found with the social security number : ";
	public static final String WS_MSG_SUCCESS = "success";
	public static final String WS_ERROR_MSG_5_UNKNOWN_CLUB_MEMBERSHIP_TYPE = "-5, Unknown club membership type : ";
	public static final String WS_ERROR_MSG_6_PARTIAL_SUCCESS_BUT_FAILED_TO_UPDATE_GOLF_IS = "-6, partial success but failed to update golf.is";
	public static final String WS_ERROR_MSG_3_CLUB_HAS_NO_PLAYER_GROUPS = "-3, Club has no player groups";
	public static final String WS_ERROR_MSG_2_CLUB_IS_NOT_A_GOLF_CLUB = "-2, Club is not a golf club";
	public static final String WS_ERROR_MSG_1_NO_CLUB_WITH_THE_NUMBER = "-1, No club with the number : ";
	public static final String GSI_CLUB_NUMBER = "07";
	public static final String GSI_UUID = "f5dbebeb-79f1-11d9-bd42-054a20130abb";

	private Collection clubs;
	private GroupBusiness groupBiz;
	private UserBusiness userBiz;
	
	/**
	 * 
	 */
	public GolfUserPluginBusinessBean() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterGroupCreateOrUpdate(com.idega.user.data.Group)
	 */
	public void afterGroupCreateOrUpdate(Group group, Group parentGroup) throws CreateException, RemoteException {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterUserCreateOrUpdate(com.idega.user.data.User)
	 */
	public void afterUserCreateOrUpdate(User user, Group parentGroup) throws CreateException, RemoteException {
		String subClubs = user.getMetaData(GolfConstants.SUB_CLUBS_META_DATA_KEY);
		String mainClub = user.getMetaData(GolfConstants.MAIN_CLUB_META_DATA_KEY);
		if (subClubs == null) {
			subClubs = "";
		}
		if (mainClub == null) {
			mainClub = "";
		}
		String golfURL = "http://www.golf.is/?";
		String requestToGolf = "";
		try {
			requestToGolf = GolfConstants.SUB_CLUBS_META_DATA_KEY + "=" + URLEncoder.encode(subClubs, "ISO-8859-1")
					+ "&" + GolfConstants.MAIN_CLUB_META_DATA_KEY + "=" + URLEncoder.encode(mainClub, "ISO-8859-1")
					+ "&" + GolfConstants.MEMBER_UUID + "=" + URLEncoder.encode(user.getUniqueId(), "ISO-8859-1") + "&"
					+ GolfConstants.MEMBER_PIN + "=" + URLEncoder.encode(user.getPersonalID(), "ISO-8859-1") + "&"
					+ GolfConstants.MEMBER_NAME + "=" + URLEncoder.encode(user.getName(), "ISO-8859-1");
			if (user.getDateOfBirth() != null) {
				requestToGolf = requestToGolf + "&" + GolfConstants.MEMBER_DATE_OF_BIRTH + "="
						+ URLEncoder.encode(user.getDateOfBirth().toString(), "ISO-8859-1");
			}
			Gender genderBean = user.getGender();
			if (genderBean != null) {
				String gender = null;
				if (genderBean.isMaleGender()) {
					gender = "M";
				}
				else {
					gender = "F";
				}
				requestToGolf += "&" + GolfConstants.MEMBER_GENDER + "=" + URLEncoder.encode(gender, "ISO-8859-1");
			}
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		golfURL += requestToGolf;
		// String html =
		// do the request
		FileUtil.getStringFromURL(golfURL);
		System.out.println("Syncing with golf.is : " + golfURL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeGroupRemove(com.idega.user.data.Group)
	 */
	public void beforeGroupRemove(Group group, Group parentGroup) throws RemoveException, RemoteException {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeUserRemove(com.idega.user.data.User)
	 */
	public void beforeUserRemove(User user, Group parentGroup) throws RemoveException, RemoteException {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupPropertiesTabs(com.idega.user.data.Group)
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupToolbarElements(com.idega.user.data.Group)
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getMainToolbarElements()
	 */
	public List getMainToolbarElements() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getUserPropertiesTabs(com.idega.user.data.User)
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException {
		// only add the tab if superuser or a club admin (golfclub in top nodes)
		// or a golf union admin
		IWContext iwc = IWContext.getInstance();
		if (iwc != null) {
			List tabs = new ArrayList();
			if (!iwc.isSuperAdmin()) {
				// this might mean not all e.g. division admin might see this
				// tab, if so then add a role like "Golf Admin"
				boolean showTab = isCurrentUserGolfAdmin(iwc);
				if (showTab) {
					tabs.add(new GolferTab());
				}
			}
			else {
				tabs.add(new GolferTab());
			}
			return (tabs.isEmpty()) ? null : tabs;
		}
		return null;
	}

	/**
	 * @param iwc
	 * @return
	 * @throws RemoteException
	 */
	public boolean isCurrentUserGolfAdmin(IWContext iwc) throws RemoteException {
		boolean isGolfAdmin = false;
		UserBusiness userBiz = getUserBusiness();
		Collection groups = userBiz.getUsersTopGroupNodesByViewAndOwnerPermissions(iwc.getCurrentUser(), iwc);
		if (groups != null && !groups.isEmpty()) {
			Iterator iter = groups.iterator();
			while (iter.hasNext() && !isGolfAdmin) {
				Group group = (Group) iter.next();
				String type = group.getGroupType();
				String name = group.getName();
				if ((name.startsWith("Golf") || name.startsWith("golf"))
						&& (type.equals(IWMemberConstants.GROUP_TYPE_CLUB) || type.equals(IWMemberConstants.GROUP_TYPE_LEAGUE)
						|| type.equals(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION))) {
					isGolfAdmin = true;
					break;
				}
			}
		}
		return isGolfAdmin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateEditor(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateViewer(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserAssignableFromGroupToGroup(com.idega.user.data.User,
	 *      com.idega.user.data.Group, com.idega.user.data.Group)
	 */
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserSuitedForGroup(com.idega.user.data.User,
	 *      com.idega.user.data.Group)
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Clubs must start their name with "Golf".
	 * 
	 * @return A list of golfclubs (Groups)
	 */
	public Collection getGolfClubs() {
		// if(clubs==null){
		try {
			clubs = getGroupBusiness().getGroupsByGroupTypeAndFirstPartOfName(GolfConstants.GROUP_TYPE_CLUB, "Golf");
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// }
		return clubs;
	}

	public GroupBusiness getGroupBusiness() {
		if (groupBiz == null) {
			try {
				groupBiz = (GroupBusiness) this.getServiceInstance(GroupBusiness.class);
			}
			catch (IBOLookupException e) {
				e.printStackTrace();
			}
		}
		return groupBiz;
	}

	public UserBusiness getUserBusiness() {
		if (userBiz == null) {
			try {
				userBiz = (UserBusiness) this.getServiceInstance(UserBusiness.class);
			}
			catch (IBOLookupException e) {
				e.printStackTrace();
			}
		}
		return userBiz;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#canCreateSubGroup(com.idega.user.data.Group,java.lang.String)
	 */
	public String canCreateSubGroup(Group group, String groupTypeOfSubGroup) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param ssn
	 *            The persons social security number
	 * @param clubNumber
	 *            The number of the golf club (lotto number)
	 * @param clubMembershipType
	 *            The type of membership , "main" or "sub"
	 * @return
	 * @throws RemoteException 
	 * @throws  
	 */
	public String registerGolferToClubAndGolfDotIs(String ssn, String clubNumber, String clubMembershipType) throws RemoteException {
		GroupBusiness groupBiz = getGroupBusiness();
		UserBusiness userBiz = getUserBusiness();
		User user = null;
		try {
			user = userBiz.getUser(ssn);
		}
		catch (FinderException e) {
			e.printStackTrace();
			return WS_ERROR_MSG_4_NO_PERSON_FOUND_WITH_THE_SOCIAL_SECURITY_NUMBER + ssn;
		}
		Collection clubs = groupBiz.getGroupsByMetaDataKeyAndValue(IWMemberConstants.META_DATA_CLUB_NUMBER, clubNumber);
		
		if (clubs != null && !clubs.isEmpty() && clubs.size() == 1) {
			Group club = (Group) clubs.iterator().next();
			String clubAbbreviation = club.getAbbrevation();
			
			// check connection with GSI on the club itself and then under a
			// division
			// add user to the first iwme_member group and set his metadata
			List divisionType = new ArrayList();
			divisionType.add(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION);
			
			Collection divisions = groupBiz.getChildGroups(club, divisionType, true);
			
			Group division = null;
			for (Iterator iter = divisions.iterator(); iter.hasNext();) {
				division = (Group) iter.next();
				boolean correctLeague = isCorrectLeague(division);
				if (correctLeague) {
					break;
				}
				else {
					division = null;
				}
			}
			
			if (division != null) {
				String[] groupTypes = { IWMemberConstants.GROUP_TYPE_CLUB_PLAYER };
				Collection playerGroups = groupBiz.getChildGroupsRecursive(division, groupTypes, true);
				if (playerGroups != null && !playerGroups.isEmpty()) {
					Group firstPlayerGroup = (Group) playerGroups.iterator().next();
					firstPlayerGroup.addGroup(user);
					
					String subClubs = user.getMetaData(GolfConstants.SUB_CLUBS_META_DATA_KEY);
					String mainClub = user.getMetaData(GolfConstants.MAIN_CLUB_META_DATA_KEY);
					if (subClubs == null) {
						subClubs = "";
					}
					if (mainClub == null) {
						mainClub = "";
					}
					
					List subClubList = getListFromSubClubsString(subClubs);
					
					if("main".equalsIgnoreCase(clubMembershipType)){
						if(subClubList.contains(clubAbbreviation)){
							subClubList.remove(clubAbbreviation);
						}
						if(!"".equals(mainClub) && !mainClub.equals(clubAbbreviation)){
							subClubList.remove(mainClub);
							//to avoid duplicates
							subClubList.add(mainClub);
						}
						
						mainClub = clubAbbreviation;
					}
					else if("sub".equalsIgnoreCase(clubMembershipType)){
						if(!subClubList.contains(clubAbbreviation)){
							subClubList.add(clubAbbreviation);
						}
						if(mainClub.equals(clubAbbreviation)){
							mainClub = null;
						}
						
					}
					else{
						return WS_ERROR_MSG_5_UNKNOWN_CLUB_MEMBERSHIP_TYPE+clubMembershipType;
					}
					
					user.setMetaData(GolfConstants.SUB_CLUBS_META_DATA_KEY, ListUtil.convertListOfStringsToCommaseparatedString(subClubList));
					user.setMetaData(GolfConstants.MAIN_CLUB_META_DATA_KEY, mainClub);
					user.store();
					
					try {
						afterUserCreateOrUpdate(user,firstPlayerGroup);
					}
					catch (CreateException e) {
						e.printStackTrace();
						return WS_ERROR_MSG_6_PARTIAL_SUCCESS_BUT_FAILED_TO_UPDATE_GOLF_IS;
					}
					
					return WS_MSG_SUCCESS;
				}
				else {
					return WS_ERROR_MSG_3_CLUB_HAS_NO_PLAYER_GROUPS;
				}
			}
			else {
				return WS_ERROR_MSG_2_CLUB_IS_NOT_A_GOLF_CLUB;
			}
		}
		else {
			return WS_ERROR_MSG_1_NO_CLUB_WITH_THE_NUMBER + clubNumber;
		}
	}

	/**
	 * @param clubNumber
	 * @param uuid
	 * @return
	 * @throws FinderException
	 * @throws RemoteException
	 * @throws NumberFormatException
	 * @throws IBOLookupException
	 */
	protected boolean isCorrectLeague(Group targetGroup) throws RemoteException {
		// check if the division of the target group is connected to GSI
		try {
			Group league = getLeagueForGroup(targetGroup);
			String leagueNumber = league.getMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER);
			String uuid = league.getUniqueId();
			leagueNumber = leagueNumber.trim();
			return GSI_CLUB_NUMBER.equals(leagueNumber) || uuid.equals(GSI_UUID);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Changes the users metadata and syncs with golf.is
	 * @param ssn
	 * @param clubNumber
	 * @return
	 * @throws RemoteException
	 */
	public String disableGolferInClub(String ssn, String clubNumber) throws RemoteException{
		GroupBusiness groupBiz = getGroupBusiness();
		UserBusiness userBiz = getUserBusiness();
		User user = null;
		try {
			user = userBiz.getUser(ssn);
		}
		catch (FinderException e) {
			e.printStackTrace();
			return WS_ERROR_MSG_4_NO_PERSON_FOUND_WITH_THE_SOCIAL_SECURITY_NUMBER + ssn;
		}
		Collection clubs = groupBiz.getGroupsByMetaDataKeyAndValue(IWMemberConstants.META_DATA_CLUB_NUMBER, clubNumber);
		
		if (clubs != null && !clubs.isEmpty() && clubs.size() == 1) {
			Group club = (Group) clubs.iterator().next();
			
			String clubAbbreviation = club.getAbbrevation();
			if(clubAbbreviation!=null && !"".equals(clubAbbreviation)){				
				String mainClub = user.getMetaData(GolfConstants.MAIN_CLUB_META_DATA_KEY);
				String subClubs = user.getMetaData(GolfConstants.SUB_CLUBS_META_DATA_KEY);
				
				if (subClubs != null) {
					List subs = getListFromSubClubsString(subClubs);
					if(subs.contains(clubAbbreviation)){
						subs.remove(clubAbbreviation);
					}
					if(!subs.isEmpty()){
						user.setMetaData(GolfConstants.SUB_CLUBS_META_DATA_KEY, ListUtil.convertListOfStringsToCommaseparatedString(subs));
					}
					else{
						//so we erase it on golf.is
						user.setMetaData(GolfConstants.SUB_CLUBS_META_DATA_KEY, "");
					}
				}else{
					//so we erase it on golf.is
					user.setMetaData(GolfConstants.SUB_CLUBS_META_DATA_KEY, "");
				}
				
				
				if (mainClub !=null) {
					if(mainClub.equalsIgnoreCase(clubAbbreviation)){
						user.setMetaData(GolfConstants.MAIN_CLUB_META_DATA_KEY, "");
					}
				}
				else{
//					so we erase it on golf.is
					user.setMetaData(GolfConstants.MAIN_CLUB_META_DATA_KEY, "");		
				}
				
				//...and store
				user.store();
				
				try {
					//sync with golf.is
					afterUserCreateOrUpdate(user,null);
				}
				catch (CreateException e) {
					e.printStackTrace();
					return WS_ERROR_MSG_6_PARTIAL_SUCCESS_BUT_FAILED_TO_UPDATE_GOLF_IS;
				}
				
				
			}
			else{
				return WS_ERROR_MSG_1_NO_CLUB_WITH_THE_NUMBER+clubNumber+" Club abbreviation was null";
			}
			
			
			
			
		}
		else{
			return WS_ERROR_MSG_1_NO_CLUB_WITH_THE_NUMBER+clubNumber;
		}
		
		return WS_MSG_SUCCESS;
	}

	/**
	 * @param targetGroup
	 * @param biz
	 * @return
	 * @throws NoDivisionFoundException
	 * @throws RemoteException
	 * @throws NoClubFoundException
	 * @throws FinderException
	 * @throws IBOLookupException
	 */
	protected Group getLeagueForGroup(Group targetGroup) throws NoDivisionFoundException, RemoteException,
			NoClubFoundException, FinderException, IBOLookupException {
		MemberUserBusiness biz = getMemberUserBusiness();
		Group division;
		if (IWMemberConstants.GROUP_TYPE_CLUB_DIVISION.equals(targetGroup.getGroupType())) {
			division = targetGroup;
		}
		else {
			division = biz.getDivisionForGroup(targetGroup);
		}
		String leagueId = division.getMetaData(IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION);
		// if it is a single division club get the number from the club
		if (leagueId == null || "".equals(leagueId)) {
			Group club = biz.getClubForGroup(targetGroup);
			leagueId = club.getMetaData(IWMemberConstants.META_DATA_CLUB_LEAGUE_CONNECTION);
		}
		
		Group league = getGroupBusiness().getGroupByGroupID(Integer.parseInt(leagueId));
		return league;
	}

	private MemberUserBusiness getMemberUserBusiness() throws IBOLookupException {
		return (MemberUserBusiness) getServiceInstance(MemberUserBusiness.class);
	}
	
	private List getListFromSubClubsString(String subClubAbbreviations) {
		return ListUtil.convertCommaSeparatedStringToList(subClubAbbreviations);
	}
	
	
	/**
	 * @param ssn The golfers social security number
	 * @return Basic information about the golfer such as name,address,phone,handicap,sub and main clubs
	 * @throws Exception 
	 */
	public Map getGolferInfo(String ssn) throws Exception{
		Map info = new HashMap();
		UserBusiness userBiz = getUserBusiness();
		
		try {
			User user = userBiz.getUser(ssn);
			//general stuff
			info.put("name", user.getName());
			info.put("ssn", user.getPersonalID());
			info.put("date-of-birth", user.getDateOfBirth().toString());
			info.put("gender", user.getGender().getName());
			
			//address
			Address address = userBiz.getUsersMainAddress(user); 
			if(address!=null){
				String addressString = getAddressBusiness().getFullAddressString(address);
				info.put("address",addressString);
			}
			
			//email
			Collection emails = user.getEmails();
			if (emails != null && !emails.isEmpty()) {
				Email email = (Email) emails.iterator().next();
				info.put("email",email.getEmailAddress());
			}
			
			//phone
			try {
				Phone fax;
				fax = getUserBusiness().getPhoneHome().findUsersFaxPhone(user);
				info.put("fax", fax.getNumber());
			}
			catch (FinderException e1) {
				//e1.printStackTrace();
			}
			
			
			try {
				Phone home = getUserBusiness().getPhoneHome().findUsersHomePhone(user);
				info.put("home", home.getNumber());
			}
			catch (FinderException e1) {
				//e1.printStackTrace();
			}
			try {
				Phone mobile = getUserBusiness().getPhoneHome().findUsersMobilePhone(user);
				info.put("mobile", mobile.getNumber());
			}
			catch (FinderException e1) {
				//e1.printStackTrace();
			}
			
			//golf stuff
			String mainClub = user.getMetaData(GolfConstants.MAIN_CLUB_META_DATA_KEY);
			info.put("main-club", mainClub);
			
			String subClubs = user.getMetaData(GolfConstants.SUB_CLUBS_META_DATA_KEY);
			info.put("sub-clubs-csv", subClubs);
			
			try {
				MemberHome mHome = (MemberHome) IDOLookup.getHome(Member.class);
				Member member = mHome.findMemberByIWMemberSystemUser(user);
				String handicap = Float.toString(member.getHandicap());
				info.put("handicap", handicap);
			}
			catch (FinderException f) {
				System.err.println("No record in Member for user: "+user.getName()+" ssn: "+user.getPersonalID());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		catch (FinderException e) {
			e.printStackTrace();
			throw new Exception(WS_ERROR_MSG_4_NO_PERSON_FOUND_WITH_THE_SOCIAL_SECURITY_NUMBER + ssn);
		}

		return info;
	}
	
	
	
	protected AddressBusiness getAddressBusiness() throws RemoteException {
		return (AddressBusiness) IBOLookup.getServiceInstance(IWMainApplication.getDefaultIWApplicationContext(),
				AddressBusiness.class);
	}
	
}
