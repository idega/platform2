package is.idega.idegaweb.golf.business;

import is.idega.idegaweb.golf.clubs.presentation.UnionCorrect;
import is.idega.idegaweb.golf.entity.AddressHome;
import is.idega.idegaweb.golf.entity.Country;
import is.idega.idegaweb.golf.entity.CountryHome;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.PhoneHome;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;
import is.idega.idegaweb.golf.entity.ZipCode;
import is.idega.idegaweb.golf.entity.ZipCodeHome;
import is.idega.idegaweb.golf.legacy.business.GolfLegacyBusiness;
import is.idega.idegaweb.golf.util.GolfConstants;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.ibm.icu.util.StringTokenizer;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOServiceBean;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.GenderHome;
import com.idega.user.data.Group;
import com.idega.user.data.MetadataConstants;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;

/**
 * This UserGroupPlugin is used to synchronize the golf data tables with the user system. <br>
 * Methods in this plugin are called after the LDAPReplicator has replicated a user.
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>,<a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class UserSynchronizationBusinessBean extends IBOServiceBean implements UserSynchronizationBusiness,UserGroupPlugInBusiness,GolfConstants {

	private static int USERS_PER_CHECK = 10;
	
	private UserHome uHome;
	private MemberHome mHome;
	private GenderHome gHome;
	private AddressHome oldAddressHome;
	private ZipCodeHome zHome;
	private CountryHome cHome;
	private PhoneHome oldPhoneHome;
	private UnionHome unionHome;
	private UnionCorrect unionCorrect = new UnionCorrect();
	private UserBusiness userBusiness;
	private GolfLegacyBusiness golfLegacyBiz;
	private static HashMap genders = new HashMap();
	private static HashMap postalToAreaCode = new HashMap();
	private static HashMap countries = new HashMap();
	private boolean initDone = false;
	private static String NO_CLUB_ABBR = "-";
	
	public UserSynchronizationBusinessBean() {
		super();
	}

	public synchronized boolean sync() {
		try {
			init();
			Collection unionGroups = userBusiness.getGroupHome().findGroupsByType(GolfConstants.GROUP_TYPE_CLUB);
			if (unionGroups != null) {
				Iterator iter = unionGroups.iterator();
				Collection users;
				Group group;
				Union union;
				User user;
				Member member;
				while (iter.hasNext()) {
					group = (Group) iter.next();
					users = userBusiness.getGroupBusiness().getUsers(group);
					if (users != null) {
						union = getUnionFromGroup(group);
						//System.out.println("Synchronizing Union : "+group.getAbbrevation()+" ("+group.getPrimaryKey().toString()+")");
						Iterator userIter = users.iterator();
						while (userIter.hasNext() && union != null) {
							user = (User) userIter.next();
							if (user.getPersonalID() != null) {
								System.out.print("Synchronizing user : "+user.getPersonalID()+" ("+user.getID()+") ");
								member = getMemberFromUser(user);
								try {
									System.out.print(".");
									synchronizeAddresses(user, member);
									System.out.print(".");
									synchronizePhones(user, member);
									System.out.print(".");
									unionCorrect.setMainUnion(member, union.getID());

									System.out.println("done.");
								}
								catch (SQLException e1) {
									System.out.println("failed ("+e1.getMessage()+")");
								}

								//synchronizeUser( user, getMemberFromUser(user));
							} else {
								System.out.println("Synchronizing SKIPPING user : "+user.getName()+" ("+user.getID()+") : personalID = NULL");
							}
						}
					}
				}
			}
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private Member getMemberFromUser(User user) throws RemoteException, CreateException{
		Member member = null;
		try {
			member = mHome.findMemberByIWMemberSystemUser(user);
		} catch (FinderException f) {
			try {
				member = mHome.findBySSN(user.getPersonalID());
				member.setICUser(user);
				member.store();
			} catch (FinderException f1) {
				member = createMemberFromUser(user);
			}
		}
		return member;
	}
	
	public synchronized boolean synchronize() {
		try {
			init();
			int currentCheck = 0;
			boolean isSynched = false;
			Collection icUsers;
			Iterator icUserIter;
			User icUser;
			Member member = null;
			int returnedEnts = USERS_PER_CHECK;
			while (!isSynched && returnedEnts == USERS_PER_CHECK) {
				icUsers = getUserBatch(uHome, currentCheck);
				returnedEnts = 0;
				if (icUsers != null) {
					icUserIter = icUsers.iterator();
					while (icUserIter.hasNext() && !isSynched) {
						++returnedEnts;
						icUser = (User) icUserIter.next();
						try {
							member = mHome.findMemberByIWMemberSystemUser(icUser);
							isSynched = true;
						} catch (FinderException f) {
							try {
								member = mHome.findBySSN(icUser.getPersonalID());
							} catch (FinderException f1) {}
							
							synchronizeUser(icUser, member);
						}
						
						
					}
				}
				
				++currentCheck;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @throws IDOLookupException
	 * @throws IBOLookupException
	 */
	private void init() throws IDOLookupException, IBOLookupException {
		if(!initDone){
			uHome = (UserHome) IDOLookup.getHome(User.class);
			mHome = (MemberHome) IDOLookup.getHome(Member.class);
			gHome = (GenderHome) IDOLookup.getHome(Gender.class);
			oldAddressHome = (AddressHome) IDOLookup.getHome(is.idega.idegaweb.golf.entity.Address.class);
			zHome = (ZipCodeHome) IDOLookup.getHome(ZipCode.class);
			cHome = (CountryHome) IDOLookup.getHome(Country.class);
			oldPhoneHome = (PhoneHome) IDOLookup.getHome(is.idega.idegaweb.golf.entity.Phone.class);
			unionHome = (UnionHome) IDOLookup.getHome(Union.class);
			userBusiness = (UserBusiness) getServiceInstance(UserBusiness.class);
			golfLegacyBiz = (GolfLegacyBusiness) getServiceInstance(GolfLegacyBusiness.class);
			initDone = true;
		}
	}

	private void synchronizeUser(User user, Member member) throws CreateException, RemoteException {
		if (user.getPersonalID() != null) {
			if (member == null) {
				member = createMemberFromUser(user);
			}
			System.out.print("Synchronizing user : "+user.getPersonalID()+" ("+user.getID()+") ");
			System.out.print(".");
			synchronizeAddresses(user, member);
			System.out.print(".");
			synchronizePhones(user, member);
			System.out.print(".");
			
			synchronizeUnion(user, member);
			
			System.out.println("done.");
		}
		else {
			System.out.println("Synchronizing SKIPPING user : "+user.getName()+" ("+user.getID()+") : personalID = NULL");
		}
	}
	
	private void synchronizeUnion(User user, Member member) throws RemoteException {
		String mainUnion = user.getMetaData(MetadataConstants.MAIN_CLUB_GOLF_META_DATA_KEY);
		mainUnion = ("".equals(mainUnion))?NO_CLUB_ABBR:mainUnion;
		//todo remove the connection to the club
		String subUnions = user.getMetaData(MetadataConstants.SUB_CLUBS_GOLF_META_DATA_KEY);
		subUnions = ("".equals(subUnions))?null:subUnions;
		
		//todo optimize this. it now always has to change every unionmemberinfo for each abbreviation!
		if(subUnions!=null){
			StringTokenizer tokens = new StringTokenizer(subUnions,",");
			while (tokens.hasMoreTokens()) {
				String subAbbr = tokens.nextToken();
				Union sub = getUnionFromAbbreviation(subAbbr);
				if(sub!=null){
					try {
						unionCorrect.setMainUnion(member,sub.getID());
					}
					catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}

		//must be done last!
		Union main = getUnionFromAbbreviation(mainUnion);
		if(main!=null){
			try {
				unionCorrect.setMainUnion(member, main.getID());
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param group
	 * @param union
	 * @return
	 */
	private Union getUnionFromGroup(Group group) {
		Union union = null;
		try { 
			union = unionHome.findUnionByIWMemberSystemGroup(group); 
		} catch (FinderException e) {
			try {
				union = unionHome.findByAbbreviation(group.getAbbrevation());
				union.setICGroup(group);
				union.store();
			}
			catch (FinderException e1) {
				System.out.println("UserSynchronizationBusinessBean : union not found : "+group.getAbbrevation());
				union = null;
			}
		}
		return union;
	}
	
	private Union getUnionFromAbbreviation(String abbr) {
		Union union = null;
		GroupBusiness groupBiz;
		try {
			groupBiz = (GroupBusiness) this.getServiceInstance(GroupBusiness.class);
			Collection groups = groupBiz.getGroupsByAbbreviation(abbr);
			if(groups!=null && !groups.isEmpty() && groups.size()==1){
				union = getUnionFromGroup((Group)groups.iterator().next());
			}
			
		}
		catch (IBOLookupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return union;
	}

	private void synchronizePhones(User user, Member member) throws CreateException {
		try {
			Collection phones = user.getPhones();
			is.idega.idegaweb.golf.entity.Phone[] memberPhones = member.getPhone();
			Phone phone;
			if (phones != null) {
				boolean phoneExists = false;
				Iterator iter = phones.iterator();
				while (iter.hasNext()) {
					phone = (Phone) iter.next();
					phoneExists = false;
					for (int i = 0; i < memberPhones.length && !phoneExists; i++) {
						if (phone.getNumber().equals(memberPhones[i].getNumber())) {
							phoneExists = true;
						}
					}
					
					if (!phoneExists) {
						is.idega.idegaweb.golf.entity.Phone ph = oldPhoneHome.create();
						ph.setNumber(phone.getNumber());
						if (phone.getPhoneTypeId() > 0) {
							ph.setPhoneTypeId(phone.getPhoneTypeId());
						}
						ph.store();
					}
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void synchronizeAddresses(User user, Member member) throws CreateException {
		try {
			is.idega.idegaweb.golf.entity.Address[] memberAddresses = member.getAddress();
			Collection coll = user.getAddresses();
			if (coll != null) {
				Iterator iter = coll.iterator();
				Address address;
				Country country;
				ZipCode zip;
				boolean addressExists = false;
				while (iter.hasNext()) {
					addressExists = false;
					address = (Address) iter.next();
					for (int i = 0; i < memberAddresses.length && !addressExists; i++) {
						if (address.getStreetName().equals(memberAddresses[i].getStreet())) {
							addressExists = true;
						}
					}
					
					if (!addressExists) {
						is.idega.idegaweb.golf.entity.Address addr = oldAddressHome.create();
						addr.setStreet(address.getStreetName());
						addr.setStreetNumber(address.getStreetNumber());
						
						int postalCode = address.getPostalCodeID();
						if (postalCode > 0 && postalToAreaCode.get(new Integer(postalCode)) == null) {
							try {
								zip = zHome.findByCode(address.getPostalCode().getPostalCode());
								addr.setZipcode(zip);
								postalToAreaCode.put(new Integer(postalCode), zip.getIDInteger());
							} catch (FinderException e) {
								System.out.print("z");
								//System.out.println("UserSynchronizationBusinessBean : zip code not found : "+address.getPostalCode().getPostalCode());
							}
						} else if (postalCode > 0) {
							addr.setZipcodeId((Integer) postalToAreaCode.get(new Integer(postalCode)));
						}
						
						int countryId = address.getCountryId();
						if (countryId > 0 && countries.get(new Integer(countryId)) == null) {
							try {
								country = cHome.findByAbbreviation(address.getCountry().getIsoAbbreviation());
								addr.setCountryId(country.getID());
								countries.put(new Integer(countryId), country.getIDInteger());
							}
							catch (FinderException e) {
								System.out.println("UserSynchronizationBusinessBean : country not found : "+address.getCountry().getIsoAbbreviation());
							}
						} else if (countryId > 0) {
							addr.setCountryId( (Integer) countries.get(new Integer(countryId)));
						}
						
						addr.setAddressType("home");
						addr.store();
					}
					
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Member createMemberFromUser(User user) throws CreateException, RemoteException {
		Member member = mHome.create();
		member.setFirstName(user.getFirstName());
		member.setMiddleName(user.getMiddleName());
		member.setLastName(user.getLastName());
		member.setDateOfBirth(user.getDateOfBirth());
		
		int genderId = user.getGenderID();
		if (genderId > 0) {
			if (genders.get(new Integer(genderId)) == null) {
				try {
					Gender gender = gHome.findByPrimaryKey(new Integer(genderId));
					genders.put(new Integer(genderId), gender.getName().substring(0,1).toUpperCase());
				} catch (FinderException e) {
					e.printStackTrace();
				}
			}
			member.setGender((String) genders.get(new Integer(genderId)));
		}
		member.setSocialSecurityNumber(user.getPersonalID());
		Collection emails = user.getEmails();
		if (emails != null && !emails.isEmpty()) {
			Iterator iter = emails.iterator();
			member.setEmail(((Email) iter.next()).getEmailAddress());
		}
		member.setFullName();
		member.setICUser(user);
		member.store();
		return member;
	}

	private Collection getUserBatch(UserHome uHome, int currentCheck) throws FinderException {
		return uHome.findNewestUsers(USERS_PER_CHECK, currentCheck*USERS_PER_CHECK);
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeUserRemove(com.idega.user.data.User)
	 */
	public void beforeUserRemove(User user) throws RemoveException, RemoteException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterUserCreateOrUpdate(com.idega.user.data.User)
	 */
	public void afterUserCreateOrUpdate(User user) throws CreateException, RemoteException {		
		init();
		if (user.getPersonalID() != null) {
			//System.out.println("Synchronizing user : "+user.getPersonalID()+" ("+user.getID()+") ");
			Member member = getMemberFromUser(user);
//			try {
				synchronizeAddresses(user, member);
				synchronizePhones(user, member);
				synchronizeUnion(user, member);
//			}
//			catch (SQLException e1) {
//				System.out.println("failed ("+e1.getMessage()+")");
//			}
		}
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeGroupRemove(com.idega.user.data.Group)
	 */
	public void beforeGroupRemove(Group group) throws RemoveException, RemoteException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterGroupCreateOrUpdate(com.idega.user.data.Group)
	 */
	public void afterGroupCreateOrUpdate(Group group) throws CreateException, RemoteException {
		//if the group is a club then search for the union and update it, create if it does not exist
//		init();
//		Union union = getUnionFromGroup(group);
//		if(union==null){
//			
//			
//		}
		
		
		
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getPresentationObjectClass()
	 */
	public Class getPresentationObjectClass() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateEditor(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateViewer(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getUserPropertiesTabs(com.idega.user.data.User)
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupPropertiesTabs(com.idega.user.data.Group)
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getMainToolbarElements()
	 */
	public List getMainToolbarElements() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupToolbarElements(com.idega.user.data.Group)
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getListViewerFields()
	 */
	public Collection getListViewerFields() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#findGroupsByFields(java.util.Collection, java.util.Collection, java.util.Collection)
	 */
	public Collection findGroupsByFields(Collection listViewerFields, Collection finderOperators, Collection listViewerFieldValues) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserAssignableFromGroupToGroup(com.idega.user.data.User, com.idega.user.data.Group, com.idega.user.data.Group)
	 */
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserSuitedForGroup(com.idega.user.data.User, com.idega.user.data.Group)
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
}