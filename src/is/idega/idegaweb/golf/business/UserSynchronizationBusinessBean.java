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
import is.idega.idegaweb.golf.util.GolfConstants;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOServiceBean;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.GenderHome;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;

/**
 * @author gimmi
 */
public class UserSynchronizationBusinessBean extends IBOServiceBean implements UserSynchronizationBusiness {

	private static int USERS_PER_CHECK = 10;
	
	private UserHome uHome;
	private MemberHome mHome;
	private GenderHome gHome;
	private AddressHome oldAddressHome;
	private ZipCodeHome zHome;
	private CountryHome cHome;
	private PhoneHome oldPhoneHome;
	private UnionHome unionHome;
	private UnionCorrect unionCorrect = new UnionCorrect();;
	private UserBusiness userBusiness;
	private static HashMap genders = new HashMap();
	private static HashMap postalToAreaCode = new HashMap();
	private static HashMap countries = new HashMap();
	
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
		uHome = (UserHome) IDOLookup.getHome(User.class);
		mHome = (MemberHome) IDOLookup.getHome(Member.class);
		gHome = (GenderHome) IDOLookup.getHome(Gender.class);
		oldAddressHome = (AddressHome) IDOLookup.getHome(is.idega.idegaweb.golf.entity.Address.class);
		zHome = (ZipCodeHome) IDOLookup.getHome(ZipCode.class);
		cHome = (CountryHome) IDOLookup.getHome(Country.class);
		oldPhoneHome = (PhoneHome) IDOLookup.getHome(is.idega.idegaweb.golf.entity.Phone.class);
		unionHome = (UnionHome) IDOLookup.getHome(Union.class);
		userBusiness = (UserBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), UserBusiness.class);
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
		Collection coll = userBusiness.getUserGroups(user, new String[]{GolfConstants.GROUP_TYPE_CLUB}, true);
		if (coll != null) {
			Group group;
			Union union;
			Iterator iter = coll.iterator();
			
			while (iter.hasNext()) {
				group = (Group) iter.next();
				union = getUnionFromGroup(group);

				if (union != null){
					try {
						unionCorrect.setMainUnion(member, union.getID());
					}
					catch (SQLException e1) {
						System.out.println("UserSynchronizationBusinessBean : failed to set main union : "+member.getSocialSecurityNumber()+" ("+member.getID()+")");
					}
				}
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

	/*
	 * IC_USER UNION_MEMBER_INFO
	 * 
	 * Spyrja Gumma œt ’ group_type IWME_CLUB
	 * 
	 * member union_member_info
	 * 
	 * user <- member
	 * 
	 * Byrja meÝ user...
	 */

}