/*
 * $Id: KSIUserGroupPluginBusinessBean.java,v 1.10.4.4 2006/05/18 14:48:59 gimmi Exp $
 * Created on Jul 3, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.leagues.business;

import is.idega.block.nationalregister.business.NationalRegisterBusiness;
import is.idega.block.nationalregister.data.NationalRegister;
import is.idega.idegaweb.member.business.MemberUserBusiness;
import is.idega.idegaweb.member.business.NoClubFoundException;
import is.idega.idegaweb.member.business.NoDivisionFoundException;
import is.idega.idegaweb.member.business.plugins.AgeGenderPluginBusinessBean;
import is.idega.idegaweb.member.util.IWMemberConstants;
import is.ksi.www2.ssl.vefthjon_Felix.Felagsmadur.FelagsmadurLocator;
import is.ksi.www2.ssl.vefthjon_Felix.Felagsmadur.FelagsmadurSoap;
import is.ksi.www2.ssl.vefthjon_Felix.Felagsmadur.TVilla;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import org.apache.axis.client.Stub;
import org.apache.axis.message.SOAPHeaderElement;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOService;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


public class KSIUserGroupPluginBusinessBean extends AgeGenderPluginBusinessBean implements UserGroupPlugInBusiness,IBOService,KSIUserGroupPluginBusiness{
	
	public static final String KSI_CLUB_EXCHANGE_ADMIN_UUID = "8f52479b-e980-11d9-ba1c-17f9583fc65f";
	public static final String KSI_UUID = "f3d0b26f-79f1-11d9-bd42-054a20130abb";
	public static final String KSI_CLUB_NUMBER = "14";
	public static final String WS_RETURN_VALUE_SUCCESS = "success";
	public static final String ISI_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	public void afterUserCreateOrUpdate(User user, Group parentGroup) throws CreateException, RemoteException {
		//after a player is registered he must be added to the KSI system via a webservice
		boolean useWebService = useWebService(parentGroup);
		MemberUserBusiness biz = getMemberUserBusiness();
		
		if(useWebService){
			IWResourceBundle iwrb = getResourceBundle();
			Group club;
			try {
				club = biz.getClubForGroup(parentGroup);
			}
			catch (NoClubFoundException e) {
				throw new CreateException(iwrb.getLocalizedString("ksi.no_club_for_target_group","There is no club for the target group!"));
			}
			
			String clubNumber = club.getMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER);
			String clubName = club.getName();
			if( clubNumber!=null && !"".equals(clubNumber) ){
				if(clubName!=null && !"".equals(clubName)){
					try {
						String msg = registerPlayerToClubViaWebService(user.getPersonalID(),Integer.parseInt(clubNumber.trim()),clubName);

						Object[] messageFormatVariables = {user.getPersonalID(),parentGroup.getName(),parentGroup.getUniqueId(),club.getName(),club.getUniqueId(),msg};
						
						if(!msg.equals(WS_RETURN_VALUE_SUCCESS)){
							throw new CreateException(iwrb.getLocalizedAndFormattedString("ksi.webservice_failed_to_create_user_in_ksi_system",
									"Failed to create the user in the KSI system (webservice) for pin:{0} in group : {1}:{2} in club : {3}:{4} The message was {5}",messageFormatVariables));
						}
					}
					catch (NumberFormatException e) {
						e.printStackTrace();
						throw new CreateException(iwrb.getLocalizedString("ksi.club_number_is_not_a_number","The club number is not an integer!"));
					}
					catch (ServiceException e) {
						e.printStackTrace();
						throw new CreateException(iwrb.getLocalizedString("ksi.webservice_service_error","The KSI Webservice failed or was unreachable, the error was: ")+e.getMessage());
					}
				}
				else{
					throw new CreateException(iwrb.getLocalizedString("ksi.no_club_name_for_target_group","No name was found for target group's club."));
				}
			}
			else{
				throw new CreateException(iwrb.getLocalizedString("ksi.no_club_number_for_target_group", "No club number was found for target group's club."));
			}
		}
	}
	
	public String isUserSuitedForGroup(User user, Group targetGroup) throws RemoteException {
		String errorMessage = super.isUserSuitedForGroup(user,targetGroup);
		
		if(errorMessage==null){
			boolean isClubExchangeDep = isClubMemberExchangeDependent(targetGroup);
			boolean isNationalityDep = isNationalityDependent(targetGroup);
			IWResourceBundle iwrb = getResourceBundle();
			
			if(isClubExchangeDep || isNationalityDep){
				MemberUserBusiness biz = getMemberUserBusiness();
				
				////////////////////////////
				//INIT OF VARIABLES STARTS//
				boolean usingWebService = useWebService(targetGroup);
				boolean playerRegisteredInWebService = false;
				boolean playerRegisteredInOtherClubInWebService = false;
				int clubNumberFromWebService = -1;
				if(usingWebService){
					try {
						clubNumberFromWebService = getClubNumberForPlayerFromWebService(user.getPersonalID());
					}
					catch (ServiceException e1) {
						e1.printStackTrace();
						iwrb.getLocalizedString("ksi.webservice_unreachable","KSI webservice cannot be reached, please call ISI or KSI and let them know.");
					}
					
					String wsClubNumb = Integer.toString(clubNumberFromWebService);
					String clubNumber = null;
					try {
						clubNumber = biz.getClubNumberForGroup(targetGroup);
					}
					catch (NoClubFoundException e1) {
						return iwrb.getLocalizedString("ksi.no_club_for_target_group","There is no club for the target group!");
					}
					
					
					if(clubNumber!=null && !"".equals(clubNumber)){
						clubNumber = clubNumber.trim();
					}
					else{
						return iwrb.getLocalizedString("ksi.no_club_number","The club of the target group is missing its club number, please let ISI know about it!");
					}
					
					//Via webservice
					playerRegisteredInWebService = (clubNumberFromWebService>-1);
					playerRegisteredInOtherClubInWebService = (playerRegisteredInWebService) &&  (!wsClubNumb.equals(clubNumber));
				}
				
				//Via member system
				boolean playerRegisteredInOtherClubInMemberSystem = false;
				try {
					playerRegisteredInOtherClubInMemberSystem = isRegisteredPlayerInOtherClubThanTargetGroupBelongsTo(user,targetGroup);
				}
				catch (NoClubFoundException e1) {
					return iwrb.getLocalizedString("ksi.no_club_for_target_group","There is no club for the target group!");
				}
				catch (NoDivisionFoundException e1) {
					return iwrb.getLocalizedString("ksi.no_division_for_target_group","There is no division for the target group!");
				}
				catch (FinderException e1) {
					e1.printStackTrace();
					return iwrb.getLocalizedString("ksi.no_league_for_target_groups_division","There is no league connected to the division of the target group");
				}
				//INIT OF VARIABLES ENDS//
				/////////////////////////
				
				
				if(isClubExchangeDep){
					try {
						//TODO add league for target group and other clubs number (felix or webservice) for better messages
						errorMessage = checkClubExchangeDependency(user,targetGroup,playerRegisteredInOtherClubInMemberSystem,usingWebService,playerRegisteredInWebService,playerRegisteredInOtherClubInWebService,clubNumberFromWebService);
					}
					catch (NoDivisionFoundException e) {
						return iwrb.getLocalizedString("ksi.no_division_for_target_group","There is no division for the target group!");
					}
					catch (NoClubFoundException e) {
						return iwrb.getLocalizedString("ksi.no_club_for_target_group","There is no club for the target group!");
					}
				}
				
				if(isNationalityDep && errorMessage==null){
					errorMessage = checkNationality(user,targetGroup,playerRegisteredInOtherClubInMemberSystem,usingWebService,playerRegisteredInWebService,playerRegisteredInOtherClubInWebService,clubNumberFromWebService);
				}
			}
		}
		
		return errorMessage;
	}
	
	protected boolean isRegisteredPlayerInOtherClubThanTargetGroupBelongsTo(User user, Group targetGroup) throws RemoteException, FinderException {
		
		MemberUserBusiness biz = getMemberUserBusiness();
		
		Group clubForTargetGroup = biz.getClubForGroup(targetGroup);
		Group league = getLeagueForGroup(targetGroup);
		String leagueNR = league.getMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER);
		String uuid = league.getUniqueId();
		
		
		Collection parents = user.getParentGroups();
		for (Iterator iter = parents.iterator(); iter.hasNext();) {
			Group group = (Group) iter.next();
			if(group.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER)){
				if(isCorrectLeague(group,leagueNR,uuid)){
					try {
						Group clubForGroup = biz.getClubForGroup(group);
						if(!clubForGroup.equals(clubForTargetGroup)){
							return true;
						}
					}
					catch (NoClubFoundException e) {
						//do nothing, but the player group is obviously created in the wrong way or has been deleted
						e.printStackTrace();
					}
				}
			}
		}
		
		
		
		return false;	
	}
	
	
	protected boolean useWebService(Group targetGroup) throws RemoteException {
		if(targetGroup==null || !IWMemberConstants.GROUP_TYPE_CLUB_PLAYER.equals(targetGroup.getGroupType())){
			return false;
		}
//		KSI is number 14 and this uuid is from felixsport.is
		return isCorrectLeague(targetGroup,KSI_CLUB_NUMBER,KSI_UUID);
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
	protected boolean isCorrectLeague(Group targetGroup, String correctLeagueNumber, String correctUUID) throws RemoteException {
		//check if the division of the target group is connected to KSI
		try {
			Group league = getLeagueForGroup(targetGroup);
			String leagueNumber = league.getMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER);
			String uuid = league.getUniqueId();
			leagueNumber = leagueNumber.trim();
			
			return  correctLeagueNumber.equals(leagueNumber) || uuid.equals(correctUUID);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		return false;
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
	protected Group getLeagueForGroup(Group targetGroup) throws NoDivisionFoundException, RemoteException, NoClubFoundException, FinderException, IBOLookupException {
		MemberUserBusiness biz = getMemberUserBusiness();
		Group division;
		if(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION.equals(targetGroup.getGroupType())){
			division = targetGroup;
		}
		else{
			division = biz.getDivisionForGroup(targetGroup);
		}
		String leagueId = division.getMetaData(IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION);
		
//		if it is a single division club get the number from the club		
		if(leagueId==null || "".equals(leagueId)){
			Group club = biz.getClubForGroup(targetGroup);
			leagueId = club.getMetaData(IWMemberConstants.META_DATA_CLUB_LEAGUE_CONNECTION);
		}
		
		Group league = getGroupBusiness().getGroupByGroupID(Integer.parseInt(leagueId));
		
		return league;
	}
	
	/**
	 * Calls the KSI webservice with the players personalId as a parameter. The service will return -1 if not found but the club number if found.
	 * @param personalId
	 * @return -1 or the club number
	 * @throws ServiceException 
	 * @throws RemoteException 
	 */
	public int getClubNumberForPlayerFromWebService(String personalId) throws ServiceException, RemoteException{
		FelagsmadurLocator locator = new FelagsmadurLocator();
		FelagsmadurSoap wservice = locator.getFelagsmadurSoap();
		
		int returnValue = wservice.felagsmadur_til(personalId);
		
		System.out.println("Webservice: felagsmadur til ("+personalId+" : "+returnValue);
		
		return returnValue;
	}
	
	/**
	 * Registers a player to a club by its club number and club name via a webservice.
	 * If successful this method should return WS_RETURN_VALUE_SUCCESS ("success") and if not the error_number+" "+error_text.
	 * @param personalId
	 * @param clubNumber
	 * @param clubName
	 * @return WS_RETURN_VALUE_SUCCESS ("success") if ok but error_number+" "+error_text if not
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	public String registerPlayerToClubViaWebService(String personalId, int clubNumber, String clubName) throws RemoteException, ServiceException{
		FelagsmadurLocator locator = new FelagsmadurLocator();
		QName serviceName = locator.getServiceName();
		//FelagsmadurSoap_PortType wservice = locator.getFelagsmadurSoap(new URL("http://127.0.0.1:8080/ssl/vefthjon_felix/felagsmadur.asmx?"));
		FelagsmadurSoap wservice = locator.getFelagsmadurSoap();
		
		SOAPHeaderElement authHeader = new SOAPHeaderElement(serviceName.getNamespaceURI(),"AuthHeader");
		//authHeader.setMustUnderstand(true);
		
		try {
			SOAPElement userName = authHeader.addChildElement("UserName");
			userName.addTextNode("felix7");
			SOAPElement password = authHeader.addChildElement("Password");
			password.addTextNode("r2bold5");
		
			Stub stub = (Stub) wservice;
			stub.setHeader(authHeader);
		
		}
		catch (SOAPException e) {
			e.printStackTrace();
		}
	
		TVilla msg =  wservice.felagsmadur_Skra(personalId,clubNumber,clubName);
		int error = msg.getIVilla();
		
		String text = msg.getSVilla_texti();
		
		if(error==0 || error==-2){
			return WS_RETURN_VALUE_SUCCESS;
			//-2 is really an error and the player shouldn't be registered however we already checked if it was allowed...
		}
		else{
			return error+" "+text;
		}
	}
	
	protected String checkClubExchangeDependency(User user, Group targetGroup, boolean playerRegisteredInOtherClubInMemberSystem, boolean usingWebService, boolean playerRegisteredInWebService, boolean playerRegisteredInOtherClubInWebService, int clubNumberFromWebService) throws NoDivisionFoundException, RemoteException, NoClubFoundException {
		IWResourceBundle iwrb = getResourceBundle();
		if(playerRegisteredInOtherClubInMemberSystem){
			//here we need the club...
			return iwrb.getLocalizedString("ksi.player_in_another_club_in_member_system","The player is registered to another club at the moment please contact that club and have him removed or contact the league and ask for a club member exchange");
		}else if(usingWebService){
			if(playerRegisteredInWebService){
				if(playerRegisteredInOtherClubInWebService){
					return  iwrb.getLocalizedString("ksi.player_in_another_club_in_ksi_system","The player is registered to another club you need to contact the league and apply for a club member exchange");
				}
			}
		}
		
		return null;
	}
	
	/**
	 * @param user
	 * @param playerRegisteredInOtherClubInWebService 
	 * @param playerRegisteredInTargetClubInWebService 
	 * @param playerRegisteredInWebService 
	 * @param callWebService2 
	 * @param playerRegisteredInOtherClubInMemberSystem 
	 * @param clubNumberFromWebService 
	 * @throws IBOLookupException
	 * @throws RemoteException
	 */
	protected String checkNationality(User user, Group targetGroup, boolean playerRegisteredInOtherClubInMemberSystem, boolean usingWebService, boolean playerRegisteredInWebService, boolean playerRegisteredInOtherClubInWebService, int clubNumberFromWebService) throws IBOLookupException, RemoteException {
//		o Ef a_ kennitala vi_komandi er ekki til á skrá hjá KSÍ e_a er ekki skrá_ í anna_ knattspyrnufélag
//		ß Kanna_ er hvort a_ ríkisfang vi_komandi sé erlent.
//		∑ Sé _a_ erlent _á birtist or_sending: “Jón Jónsson, kt. 123456-7890, er me_ erlent ríkisfang.  Vinsamlega hafi_ samband vi_ KSÍ (finna útfrá sérsambandstengingu), _ar sem kanna _arf hvort a_ vi_komandi hafi leikheimild í ö_ru landi.”
//		ß Ef ríkisfang er ekki erlent.
//		∑ Vi_komandi skráist í félagi_ -> láta ksí vita
//		o Ef a_ vi_komandi er nú _egar skrá_ur í félagi_ samkvæmt grunni KSÍ
//		ß Vi_komandi skráist í félagi_
//		o Ef a_ vi_komandi er nú _egar skrá_ur í anna_ félag
//		ß Or_sending birtist á skjánum: “Jón Jónsson, kt. 123456-7890, er nú _egar skrá_ur í Knattspyrnufélagi_ X.”
//		ß Hva› me› ﬂegar menn eru ﬂegar skrá›ir í felix í ö›ru félagi
		boolean icelandicNationality = hasIcelandicCitizenship(user);
		IWResourceBundle iwrb = getResourceBundle();
		
		if(!icelandicNationality && !usingWebService){
			return  iwrb.getLocalizedString("ksi.no_icelandic_citizenship","The player does not have an Icelandic citizenship and might still have a foreign players permit. Please contact the league and ask for a club member exchange.");
		}
		else if(playerRegisteredInOtherClubInMemberSystem){
			//here we need the club...
			return iwrb.getLocalizedString("ksi.player_in_another_club_in_member_system","The player is registered to another club at the moment please contact that club and have him removed or contact the league and ask for a club member exchange");	
		}
		else if(!icelandicNationality && usingWebService){
			if(playerRegisteredInWebService){
				if(playerRegisteredInOtherClubInWebService){
					return  iwrb.getLocalizedString("ksi.player_in_another_club_in_ksi_system","The player is registered to another club you need to contact the league and apply for a club member exchange");
				}
			}
			else{
				return  iwrb.getLocalizedString("ksi.no_icelandic_citizenship","The player does not have an Icelandic citizenship and might still have a foreign players permit. Please contact the league and ask for a club member exchange.");
			}
		}
		
		
		return null;
	}
	
	
	/**
	 * @param user
	 * @param register
	 * @return false if not an icelandic citizen or if there is no record of the player in the icelandic national registry table
	 * @throws RemoteException
	 */
	protected boolean hasIcelandicCitizenship(User user) throws RemoteException {
		NationalRegisterBusiness register = (NationalRegisterBusiness) getServiceInstance(NationalRegisterBusiness.class);
		String pin = user.getPersonalID();
		NationalRegister entry = register.getEntryBySSN(pin);
		if(entry==null){
			return false;
		}
		
		String nationality = entry.getNationality();
		if(nationality==null){
			return true;
		}
		else{
			nationality = nationality.trim();
			return ("".equals(nationality)) || ("IS".equalsIgnoreCase(nationality));
		}
	}
	
	public MemberUserBusiness getMemberUserBusiness() throws IBOLookupException{
		return (MemberUserBusiness) getServiceInstance(MemberUserBusiness.class);
	}
	
	public GroupBusiness getGroupBusiness() throws IBOLookupException{
		return (GroupBusiness) getServiceInstance(GroupBusiness.class);
	}
	
	public UserBusiness getUserBusiness() throws IBOLookupException{
		return (UserBusiness) getServiceInstance(UserBusiness.class);
	}
	
//	public CalBusiness getCalBusiness() throws IBOLookupException{
//	return (CalBusiness) getServiceInstance(CalBusiness.class);
//	}
	
	public String doClubMemberExchange(String personalIdOfPlayer, String clubNumberToRegisterTo, String dateOfActivation) throws RemoteException {
		
		UserBusiness biz = getUserBusiness();
		User ksiUser = null;
		User player = null;
		try {
			ksiUser = biz.getUserByUniqueId(KSI_CLUB_EXCHANGE_ADMIN_UUID);
		}
		catch (FinderException e) {
			e.printStackTrace();
			return "1-Could not find KSI-Felagaskipti user";
		}
		try {
			player = biz.getUser(personalIdOfPlayer);
		}
		catch (FinderException e) {
			e.printStackTrace();
			return "2-Could not find person with the personalId: "+personalIdOfPlayer+" in the database!";
		}
		
		Date date = Date.valueOf(dateOfActivation);
		IWTimestamp now = new IWTimestamp();
		IWTimestamp dateWithTime = new IWTimestamp(date);
		
		//if date is today, then set the date equal to now so the group relation is changed "right away"
		if(dateWithTime.getDay()==now.getDay() && dateWithTime.getMonth()==now.getMonth() && dateWithTime.getYear()==now.getYear()){
			dateWithTime = now;
		}
		else{
			//set midnight of the day before
			dateWithTime.addDays(-1);
			dateWithTime.setHour(23);
			dateWithTime.setMinute(59);
		}
		
		Collection clubs = getGroupBusiness().getGroupsByMetaDataKeyAndValue(IWMemberConstants.META_DATA_CLUB_NUMBER,clubNumberToRegisterTo);
		Group clubTo = null;
		if(!clubs.isEmpty() && clubs.size()==1){
			clubTo = (Group)clubs.iterator().next();
		}
		else{
			return "3-Cannot find club with the number: "+clubNumberToRegisterTo;
		}
		
		Collection parents = player.getParentGroups();
		for (Iterator iter = parents.iterator(); iter.hasNext();) {
			Group group = (Group) iter.next();
			if(group.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER)){
				if(isCorrectLeague(group,KSI_CLUB_NUMBER,KSI_UUID)){
					try {
						Group clubForGroup = getMemberUserBusiness().getClubForGroup(group);
						if(!clubForGroup.equals(clubTo)){
							Collection col = getGroupBusiness().getGroupRelationHome().findGroupsRelationshipsContainingBiDirectional( ((Integer)group.getPrimaryKey()).intValue(), ((Integer)player.getPrimaryKey()).intValue());
							
							if(col!=null && !col.isEmpty()){
								Iterator iterator = col.iterator();
								while (iterator.hasNext()) {
									GroupRelation rel = (GroupRelation) iterator.next();
									if(!rel.isPassive()){
										rel.setPassivePending();
										rel.setTerminationDate((dateWithTime).getTimestamp());
										rel.setPassiveBy(((Integer)ksiUser.getPrimaryKey()).intValue());
										rel.store();
									}
								}
							}
						}
					}
					catch (Exception e){
						e.printStackTrace();
						return "4-Failed to remove the player from group: "+group.getName()+ " uuid:"+group.getUniqueId()+ " . Error msg:"+e.getMessage();
					}
				}
			}
		}
		
		return WS_RETURN_VALUE_SUCCESS;
	}
	
	protected String getBundleIdentifier() {
		return ISI_BUNDLE_IDENTIFIER;
	}
}