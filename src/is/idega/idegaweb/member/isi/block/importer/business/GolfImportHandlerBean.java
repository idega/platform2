package is.idega.idegaweb.member.isi.block.importer.business;

import is.idega.idegaweb.member.util.IWMemberConstants;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import com.idega.block.importer.business.ImportFileHandler;
import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOSessionBean;
import com.idega.core.location.business.AddressBusiness;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.Timer;
import com.idega.util.text.TextSoap;

/**
 * <p>
 * Title: GolfImportHandlerBean
 * </p>
 * <p>
 * Description: An import file handler that reads file with personalIds and
 * names and other golf related data and if the PIN exists in the database it
 * adds that user to the correct temporary group under the golf club
 * </p>
 * File format is a semicolon seperated list with the values
 * SSN,Name,Handicap,Club abbreviation, status (A/I), Club membership status
 * (main/sub),email,phone number
 * <p>
 * Idega Software Copyright (c) 2004
 * </p>
 * <p>
 * Company: Idega Software
 * </p>
 * 
 * @author <a href="mailto:eiki@idega.is">Eirikur Sveinn Hrafnsson </a>
 * @version 1.0
 */
public class GolfImportHandlerBean extends IBOSessionBean implements ImportFileHandler, GolfImportHandler,
		UserGroupPlugInBusiness {

	
private static final String MAIN_CLUB_TYPE = "main";
	private static final int PIN_COLUMN = 0;

	private static final int NAME_COLUMN = 1;

	private static final int HANDICAP_COLUMN = 2;

	private static final int CLUB_ABBR_COLUMN = 3;

	private static final int STATUS_COLUMN = 4;

	private static final int MEMBERSHIP_STATUS_COLUMN = 5;

	private static final int EMAIL_COLUMN = 6;

	private static final int PHONE_COLUMN = 7;

	private static final String NO_VALUE = "NOVALUE";

	private static final String NO_CLUB = "-";

	private List userProperties;

	private UserHome home;

	private AddressBusiness addressBiz;

	private UserBusiness userBiz;

	private GroupBusiness groupBiz;

	private Group rootGroup;

	private ImportFile file;

	private UserTransaction transaction;

	private ArrayList failedRecords;

	private Gender male;

	private Gender female;

	private Map clubsMap;

	private Map clubsTempGroupMap;

	private static final String TEMPORARY_GROUP_NAME = "Golf import";

	private User currentUser;

	private static final String META_PREFIX = "golf_";
	
	private static final String SUB_CLUBS_META_DATA_KEY = META_PREFIX+"sub_clubs";
		
	private static final String MAIN_CLUB_META_DATA_KEY = META_PREFIX+"main_club";
		
	private static final String HANDICAP_META_DATA_KEY = META_PREFIX+"handicap";

	public GolfImportHandlerBean() {
	}

	public boolean handleRecords() throws RemoteException {
		transaction = this.getSessionContext().getUserTransaction();
		Timer clock = new Timer();
		clock.start();
		try {
			//initialize business beans and data homes
			userBiz = (UserBusiness) this.getServiceInstance(UserBusiness.class);
			groupBiz = (GroupBusiness) this.getServiceInstance(GroupBusiness.class);
			clubsMap = new HashMap();
			clubsTempGroupMap = new HashMap();
			currentUser = getUserContext().getCurrentUser();
			//addressBiz = (AddressBusiness)
			// this.getServiceInstance(AddressBusiness.class);
			failedRecords = new ArrayList();
			//if the transaction failes all the users and their relations are
			// removed
			transaction.begin();
			//iterate through the records and process them
			String item;
			int count = 0;
			while (!(item = (String) file.getNextRecord()).equals("")) {
				count++;
				if (!processRecord(item))
					failedRecords.add(item);
				
				if(count%100 == 0){
					System.out.println("GolfImporter: "+count+" records done in : " + clock.getTime() + " ms  OR "+ ((int) (clock.getTime() / 1000)) + " s, "+failedRecords.size()+" failed records.");
				}
			}
			clock.stop();
			System.out.println("Time to handleRecords: " + clock.getTime() + " ms  OR "
					+ ((int) (clock.getTime() / 1000)) + " s, "+failedRecords.size()+" failed records.");
			// System.gc();
			//success commit changes
			transaction.commit();
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			try {
				transaction.rollback();
			}
			catch (SystemException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	private boolean processRecord(String record) throws RemoteException {
		//The record is processed like this:
		//1.find the club group by the club abbreviation (and store in hashmap)
		//2.find the owner of the group
		//3.create a new temporary group under the temp groups (and store in a
		// hashmap)
		//4.make the owner in 2. the owner/s of this group
		//5.add user to the group if found by personalid
		//6.add the golf info as metadata
		record = TextSoap.findAndCut(record, "\"");
		
		userProperties = file.getValuesFromRecordString(record);
		User user = null;
		//variables
		try {
			String PIN = (String) userProperties.get(PIN_COLUMN);
			if(PIN.length()==9){
				PIN = "0"+PIN;
			}
			String name = ((String) userProperties.get(NAME_COLUMN)).trim();
			String handicap = ((String) userProperties.get(HANDICAP_COLUMN)).trim();
			String clubAbbr = ((String) userProperties.get(CLUB_ABBR_COLUMN)).trim();
			String status = ((String) userProperties.get(STATUS_COLUMN)).trim();
			String membershipStatus = ((String) userProperties.get(MEMBERSHIP_STATUS_COLUMN)).trim();
			String email = ((String) userProperties.get(EMAIL_COLUMN)).trim();
			String phone = ((String) userProperties.get(PHONE_COLUMN)).trim();
			
			//only import active members
			if (NO_CLUB.equals(clubAbbr) || "I".equals(status)) {
				return true;
			}
			
			if(!NO_CLUB.equals(clubAbbr) && "A".equals(status)){
				//get the club
				Group club = getClubGroup(clubAbbr);
				//continue and get the temporary group or create it
				if (club != null) {
					Group importGroup = (Group) clubsTempGroupMap.get(clubAbbr);
					if (importGroup == null) {
						importGroup = getOrCreateImportGroup(clubAbbr, club);
					}
					
					if (importGroup != null) {
						
						user = userBiz.getUser(PIN);
						
						//ONLY TEMP SHOULD NOT CREATE!
						//user = userBiz.createUserByPersonalIDIfDoesNotExist(name, PIN, null, null);
						if(!NO_VALUE.equals(handicap)){
							user.setMetaData(HANDICAP_META_DATA_KEY, handicap);
						}
						//user.setMetaData(META_PREFIX+"status", status);
						
						setClubMetaData(user, clubAbbr, membershipStatus);
						
						user.store();

						if(!NO_VALUE.equals(email)){
							userBiz.updateUserMail(user, email);
						}
							
						if(!NO_VALUE.equals(phone)){
							if(phone.startsWith("6") || phone.startsWith("8")){
								userBiz.updateUserMobilePhone(user, phone);
							}
							else{
								userBiz.updateUserHomePhone(user, phone);
							}
						}
						
						importGroup.addGroup(user);
					}
					else {
						//no temp group found and could not be created
						return false;
					}
				}
				else {
					//there must be exactly one group with this abbreviation
					return false;
				}
			}
			else {
				return false;
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage() + ", failed record was = " + record);
			return false;
		}
		user = null;
		return true;
	}

	private void setClubMetaData(User user, String clubAbbr, String membership_status) {
		if(membership_status.equalsIgnoreCase(MAIN_CLUB_TYPE)){
			String abbr = user.getMetaData(MAIN_CLUB_META_DATA_KEY);
			if(abbr!=null && !abbr.equals(clubAbbr)){
				//move the main club to a sub club
				addToSubClubs(abbr,user);
				user.setMetaData(MAIN_CLUB_META_DATA_KEY,clubAbbr);
			}else{
				user.setMetaData(MAIN_CLUB_META_DATA_KEY,clubAbbr);
			}
		}else{
			addToSubClubs(clubAbbr,user);
		}
	}

	/**
	 * Adds the club abbreviation string to a list of comma separeted values and stores the new value if needed in metadata 
	 * @param abbr
	 */
	private void addToSubClubs(String abbr,User user) {
		String subClubs = user.getMetaData(SUB_CLUBS_META_DATA_KEY);
		if(subClubs==null){
			subClubs = abbr+",";
			
		}
		else{
			if(subClubs.indexOf(abbr+",")<0){
				subClubs+=abbr+",";
			}
		}
		
		user.setMetaData(SUB_CLUBS_META_DATA_KEY,subClubs);
	}

	private Group getOrCreateImportGroup(String clubAbbr, Group club) throws RemoteException, CreateException {
		Group importGroup = null;
		String[] tempTypeArray = { IWMemberConstants.GROUP_TYPE_TEMPORARY };
		Collection tempSuperGroup = club.getChildGroups(tempTypeArray, true);
		if (!tempSuperGroup.isEmpty() && tempSuperGroup.size() == 1) {
			Group tSuper = (Group) tempSuperGroup.iterator().next();
			importGroup = getImportGroup(tSuper);
			if (importGroup == null) {
				importGroup = createImportGroup(tSuper);
			}
		}
		if (importGroup != null) {
			clubsTempGroupMap.put(clubAbbr, importGroup);
		}
		return importGroup;
	}

	/**
	 * @param super1
	 * @return @throws
	 *         CreateException
	 * @throws RemoteException
	 */
	private Group createImportGroup(Group tSuper) throws RemoteException, CreateException {
		Collection owners = groupBiz.getOwnerUsersForGroup(tSuper);
		
		Group importGroup = groupBiz.createGroupUnder(TEMPORARY_GROUP_NAME, "import from golf.is",IWMemberConstants.GROUP_TYPE_TEMPORARY, tSuper);
		if (owners != null && !owners.isEmpty()) {
			Iterator ownerIter = owners.iterator();
			while (ownerIter.hasNext()) {
				User owner = (User) ownerIter.next();
				groupBiz.applyOwnerAndAllGroupPermissionsToNewlyCreatedGroupForUserAndHisPrimaryGroup(getUserContext(),
						importGroup, owner);
			}
		}
			
	      //set current user as owner of group
	      groupBiz.applyUserAsGroupsOwner(getUserContext(), importGroup, currentUser);
		
      return importGroup;
		
	}

	private Group getImportGroup(Group tempSuperGroup) {
		String[] tempTypeArray = { IWMemberConstants.GROUP_TYPE_TEMPORARY };
		Collection tempGroups = tempSuperGroup.getChildGroups(tempTypeArray, true);
		if (!tempGroups.isEmpty()) {
			Iterator iter = tempGroups.iterator();
			while (iter.hasNext()) {
				Group temp = (Group) iter.next();
				if (temp.getName().equals(TEMPORARY_GROUP_NAME)) {
					return temp;
				}
			}
		}
		return null;
	}

	private Group getClubGroup(String clubAbbr) throws RemoteException {
		Group club = (Group) clubsMap.get(clubAbbr);
		if (club == null) {
			Collection clubs = groupBiz.getGroupsByAbbreviation(clubAbbr);
			if (!clubs.isEmpty() && clubs.size() == 1) {
				club = (Group) clubs.iterator().next();
				clubsMap.put(clubAbbr, club);
			}
		}
		return club;
	}

	public void setImportFile(ImportFile file) {
		this.file = file;
	}

	/**
	 * @see com.idega.block.importer.business.ImportFileHandler#setRootGroup(Group)
	 */
	public void setRootGroup(Group group) {
		rootGroup = group;
	}

	/**
	 * @see com.idega.block.importer.business.ImportFileHandler#getFailedRecords()
	 */
	public List getFailedRecords() {
		return failedRecords;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeUserRemove(com.idega.user.data.User)
	 */
	public void beforeUserRemove(User user) throws RemoveException, RemoteException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterUserCreate(com.idega.user.data.User)
	 */
	public void afterUserCreate(User user) throws CreateException, RemoteException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeGroupRemove(com.idega.user.data.Group)
	 */
	public void beforeGroupRemove(Group group) throws RemoveException, RemoteException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterGroupCreate(com.idega.user.data.Group)
	 */
	public void afterGroupCreate(Group group) throws CreateException, RemoteException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getPresentationObjectClass()
	 */
	public Class getPresentationObjectClass() throws RemoteException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateEditor(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateViewer(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getUserPropertiesTabs(com.idega.user.data.User)
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupPropertiesTabs(com.idega.user.data.Group)
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getMainToolbarElements()
	 */
	public List getMainToolbarElements() throws RemoteException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupToolbarElements(com.idega.user.data.Group)
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException {
		List list = new ArrayList(1);
		list.add(new GolfImportHandlerPlugin());
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getListViewerFields()
	 */
	public Collection getListViewerFields() throws RemoteException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#findGroupsByFields(java.util.Collection,
	 *      java.util.Collection, java.util.Collection)
	 */
	public Collection findGroupsByFields(Collection listViewerFields, Collection finderOperators,
			Collection listViewerFieldValues) throws RemoteException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserAssignableFromGroupToGroup(com.idega.user.data.User,
	 *      com.idega.user.data.Group, com.idega.user.data.Group)
	 */
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserSuitedForGroup(com.idega.user.data.User,
	 *      com.idega.user.data.Group)
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup) {
		return null;
	}
}