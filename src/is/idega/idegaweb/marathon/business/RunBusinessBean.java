/*
 * Created on Jun 30, 2004
 */
package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Run;
import is.idega.idegaweb.marathon.data.RunHome;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOServiceBean;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.EmailHome;
import com.idega.core.contact.data.Phone;
import com.idega.core.contact.data.PhoneHome;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.AddressHome;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.CountryHome;
import com.idega.core.location.data.PostalCode;
import com.idega.core.location.data.PostalCodeHome;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOException;
import com.idega.idegaweb.UnavailableIWContext;
import com.idega.presentation.IWContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * Description: Business bean (service) for run... <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * 
 * @author birna
 */
public class RunBusinessBean extends IBOServiceBean implements RunBusiness {

	/**
	 * saves information on the user - creates a new user if he doesn't exsist..
	 */
	public int saveUser(String name, String ssn, String gender, String address, String postal, String city,
			String country, String tel, String mobile, String email) {
		User user = null;
		try {
			user = getUserBiz(IWContext.getInstance()).createUserByPersonalIDIfDoesNotExist(name, ssn, null, null);
			user.store();
			if (gender != null && !gender.equals("")) {
				user.setGender(Integer.parseInt(gender));
			}
			if (address != null && !address.equals("")) {
				AddressHome addressHome = (AddressHome) getIDOHome(Address.class);
				Address a = addressHome.create();
				a.setStreetName(address);
				a.setCity(city);
				a.store();
				CountryHome countryHome = (CountryHome) getIDOHome(Country.class);
				Country c = null;
				try {
					c = countryHome.findByPrimaryKey(Integer.valueOf(country));
				}
				catch (FinderException fe) {
					c = null;
				}
				if (c != null) {
					a.setCountry(c);
				}
				PostalCodeHome postalHome = (PostalCodeHome) getIDOHome(PostalCode.class);
				Integer countryID = (Integer) c.getPrimaryKey();
				PostalCode p = null;
				try {
					p = postalHome.findByPostalCodeAndCountryId(postal, countryID.intValue());
				}
				catch (FinderException fe) {
					p = null;
				}
				if (p != null) {
					a.setPostalCode(p);
				}
				a.store();
				try {
					user.addAddress(a);
				}
				catch (IDOAddRelationshipException idoEx) {
				}
			}
			//TODO: look up the users phonenumbers and email - only create new
			// if doesn't exist - else update or do nothing if same data
			if (tel != null && !tel.equals("")) {
				PhoneHome telHome = (PhoneHome) getIDOHome(Phone.class);
				Phone phone = null;
				try {
					phone = telHome.create();
					phone.setNumber(tel);
					phone.setPhoneTypeId(1);
					phone.store();
				}
				catch (CreateException cre) {
					phone = null;
				}
				if (phone != null) {
					try {
						user.addPhone(phone);
					}
					catch (IDOAddRelationshipException idoEx) {
					}
				}
			}
			if (mobile != null && !mobile.equals("")) {
				PhoneHome mobileHome = (PhoneHome) getIDOHome(Phone.class);
				Phone mob = null;
				try {
					mob = mobileHome.create();
					mob.setNumber(mobile);
					mob.setPhoneTypeId(3);
					mob.store();
				}
				catch (CreateException cre) {
					mob = null;
				}
				if (mob != null) {
					try {
						user.addPhone(mob);
					}
					catch (IDOAddRelationshipException idoEx) {
					}
				}
			}
			if (email != null && !email.equals("")) {
				EmailHome eHome = (EmailHome) getIDOHome(Email.class);
				Email emil = null;
				try {
					emil = eHome.create();
					emil.setEmailAddress(email);
					emil.store();
				}
				catch (CreateException cre) {
					emil = null;
				}
				if (emil != null) {
					try {
						user.addEmail(emil);
					}
					catch (IDOAddRelationshipException idoEx) {
					}
				}
			}
			user.store();
		}
		catch (RemoteException rme) {
		}
		catch (CreateException cre) {
		}
		return Integer.parseInt(String.valueOf(user.getPrimaryKey()));
	}

	/**
	 * saves information on the run for the specific user puts user in the right
	 * group
	 */
	public void saveRun(int userID, String run, String distance, String year, String nationality, String tshirt,
			String chipNumber, String groupName, String bestTime, String goalTime) {
		Group groupRun = null;
		try {
			groupRun = getGroupBiz(IWContext.getInstance()).getGroupByGroupID(Integer.parseInt(run));
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (UnavailableIWContext e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		String distanceType = null;
		try {
			User user = getUserBiz(IWContext.getInstance()).getUser(userID);
			user.setDateOfBirth(getBirthDateFromSSN(user.getPersonalID()).getDate());
			user.store();
			int age = getUserAge(user);
			if (distance != null && !distance.equals("")) {
				int disGroupID = Integer.parseInt(distance);
				Group disGroup = null;
				try {
					disGroup = getGroupBiz(IWContext.getInstance()).getGroupByGroupID(disGroupID);
					distanceType = disGroup.getName();
				}
				catch (UnavailableIWContext e1) {
					e1.printStackTrace();
				}
				catch (FinderException e1) {
					e1.printStackTrace();
				}
				String[] groupType = { IWMarathonConstants.GROUP_TYPE_RUN_GROUP };
				Collection groups = getGroupBiz(IWContext.getInstance()).getChildGroupsRecursive(disGroup, groupType,
						true);
				Iterator groupsIter = groups.iterator();
				while (groupsIter.hasNext()) {
					Group group = (Group) groupsIter.next();
					if (group.getName().equals(getGroupName(age, groupRun, user.getGenderID()))) {
						group.addGroup(user);
						group.store();
					}
				}
			}
			RunHome runHome = (RunHome) getIDOHome(Run.class);
			Run r = runHome.create();
			r.setUserID(userID);
			r.setRunTypeGroupID(Integer.parseInt(run));
			r.setRunDistanceGroupID(Integer.parseInt(distance));
			r.setRunYearGroupID(Integer.parseInt(year));
			r.setTShirtSize(tshirt);
			r.setChipNumber(chipNumber);
			r.setRunGroupName(groupName);
			r.setUserNationality(nationality);
			if (bestTime != null && !bestTime.equals("")) {
				r.setBestTime(Integer.parseInt(bestTime));
			}
			if (goalTime != null && !goalTime.equals("")) {
				r.setGoalTime(Integer.parseInt(goalTime));
			}
			if (distanceType != null) {
				try {
					int participantNumber = runHome.getNextAvailableParticipantNumber(getMinParticipantNumber(distanceType), getMaxParticipantNumber(distanceType)) + 1;
					r.setParticipantNumber(participantNumber);
				}
				catch (IDOException ie) {
					ie.printStackTrace();
				}
			}
			r.store();
		}
		catch (RemoteException rme) {
		}
		catch (CreateException cre) {
		}
	}

	/**
	 * 
	 * @param pin -
	 *            a social security number - format ddmmyyxxxx or ddmmyyyy
	 * @return IWTimstamp - the date of birth from the pin..
	 */
	private IWTimestamp getBirthDateFromSSN(String pin) {
		//pin format = 14011973
		if (pin.length() == 8) {
			int edd = Integer.parseInt(pin.substring(0, 2));
			int emm = Integer.parseInt(pin.substring(2, 4));
			int eyyyy = Integer.parseInt(pin.substring(4, 8));
			IWTimestamp dob = new IWTimestamp(edd, emm, eyyyy);
			return dob;
		}
		//  pin format = 140173xxxx ddmmyyxxxx
		else if (pin.length() == 10) {
			int dd = Integer.parseInt(pin.substring(0, 2));
			int mm = Integer.parseInt(pin.substring(2, 4));
			int yy = Integer.parseInt(pin.substring(4, 6));
			int century = Integer.parseInt(pin.substring(9, 10));
			int yyyy = 0;
			if (century == 9) {
				yyyy = yy + 1900;
			}
			else if (century == 0) {
				yyyy = yy + 2000;
			}
			IWTimestamp dob = new IWTimestamp(dd, mm, yyyy);
			return dob;
		}
		else {
			return null;
		}
	}

	/**
	 * @param age
	 * @param nameOfGroup
	 * @return
	 */
	private String getGroupName(int age, Group group, int genderID) {
		String runName = group.getName();
		String nameOfGroup = "";
		if (runName.equals("Rvk Marathon") || runName.equals("Roller Skate")) {
			if (age <= 14) {
				if (genderID == 2)
					nameOfGroup = IWMarathonConstants.FEMALE_14;
				else
					nameOfGroup = IWMarathonConstants.MALE_14;
			}
			else if (age > 14 && age <= 17) {
				if (genderID == 2)
					nameOfGroup = IWMarathonConstants.FEMALE_15_17;
				else
					nameOfGroup = IWMarathonConstants.MALE_15_17;
			}
			else if (age > 17 && age <= 39) {
				if (genderID == 2)
					nameOfGroup = IWMarathonConstants.FEMALE_18_39;
				else
					nameOfGroup = IWMarathonConstants.MALE_18_39;
			}
			else if (age > 39 && age <= 49) {
				if (genderID == 2)
					nameOfGroup = IWMarathonConstants.FEMALE_40_49;
				else
					nameOfGroup = IWMarathonConstants.MALE_40_49;
			}
			else if (age > 49 && age <= 59) {
				if (genderID == 2)
					nameOfGroup = IWMarathonConstants.FEMALE_50_59;
				else
					nameOfGroup = IWMarathonConstants.MALE_50_59;
			}
			else if (age > 59) {
				if (genderID == 2)
					nameOfGroup = IWMarathonConstants.FEMALE_60;
				else
					nameOfGroup = IWMarathonConstants.MALE_60;
			}
		}
		else if (runName.equals("Midnight Run")) {
			if (age <= 18) {
				if (genderID == 2)
					nameOfGroup = IWMarathonConstants.FEMALE_18;
				else
					nameOfGroup = IWMarathonConstants.MALE_18;
			}
			else if (age > 18 && age <= 39) {
				if (genderID == 2)
					nameOfGroup = IWMarathonConstants.FEMALE_19_39;
				else
					nameOfGroup = IWMarathonConstants.MALE_19_39;
			}
			else if (age > 39 && age <= 49) {
				if (genderID == 2)
					nameOfGroup = IWMarathonConstants.FEMALE_40_49;
				else
					nameOfGroup = IWMarathonConstants.MALE_40_49;
			}
			else if (age > 49) {
				if (genderID == 2)
					nameOfGroup = IWMarathonConstants.FEMALE_50;
				else
					nameOfGroup = IWMarathonConstants.MALE_50;
			}
		}
		else if (runName.equals("Laugavegur")) {
			if (age > 17 && age <= 29) {
				if (genderID == 2)
					nameOfGroup = IWMarathonConstants.FEMALE_18_29;
				else
					nameOfGroup = IWMarathonConstants.MALE_18_29;
			}
			else if (age > 29 && age <= 39) {
				if (genderID == 2)
					nameOfGroup = IWMarathonConstants.FEMALE_30_39;
				else
					nameOfGroup = IWMarathonConstants.MALE_30_39;
			}
			else if (age > 39 && age <= 49) {
				if (genderID == 2)
					nameOfGroup = IWMarathonConstants.FEMALE_40_49;
				else
					nameOfGroup = IWMarathonConstants.MALE_40_49;
			}
			else if (age > 49 && age <= 59) {
				if (genderID == 2)
					nameOfGroup = IWMarathonConstants.FEMALE_50;
				else
					nameOfGroup = IWMarathonConstants.MALE_50_59;
			}
			else if (age > 59) {
				if (genderID == 2)
					nameOfGroup = IWMarathonConstants.FEMALE_50;
				else
					nameOfGroup = IWMarathonConstants.MALE_60;
			}
		}
		return nameOfGroup;
	}

	public void createNewGroupYear(IWContext iwc, Group run, String year) {
		String runName = run.getName();
		Group group = null;
		try {
			group = getGroupBiz(iwc).createGroupUnder(year, null, run);
			group.setGroupType(IWMarathonConstants.GROUP_TYPE_RUN_YEAR);
			group.store();
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
		String[] disForMarathon = { IWMarathonConstants.DISTANCE_42, IWMarathonConstants.DISTANCE_21,
				IWMarathonConstants.DISTANCE_10, IWMarathonConstants.DISTANCE_7, IWMarathonConstants.DISTANCE_3 };
		String[] disForLaugavegur = { IWMarathonConstants.DISTANCE_55 };
		String[] disForMidnight = { IWMarathonConstants.DISTANCE_3, IWMarathonConstants.DISTANCE_5,
				IWMarathonConstants.DISTANCE_10 };
		String[] disForRollerSkate = { IWMarathonConstants.DISTANCE_10 };
		String[] grForMarathon = { IWMarathonConstants.FEMALE_14, IWMarathonConstants.FEMALE_15_17,
				IWMarathonConstants.FEMALE_18_39, IWMarathonConstants.FEMALE_40_49, IWMarathonConstants.FEMALE_50_59,
				IWMarathonConstants.FEMALE_60, IWMarathonConstants.MALE_14, IWMarathonConstants.MALE_15_17,
				IWMarathonConstants.MALE_18_39, IWMarathonConstants.MALE_40_49, IWMarathonConstants.MALE_50_59,
				IWMarathonConstants.MALE_60 };
		String[] grForLaugavegur = { IWMarathonConstants.FEMALE_18_29, IWMarathonConstants.FEMALE_30_39,
				IWMarathonConstants.FEMALE_40_49, IWMarathonConstants.FEMALE_50, IWMarathonConstants.MALE_18_29,
				IWMarathonConstants.MALE_30_39, IWMarathonConstants.MALE_40_49, IWMarathonConstants.MALE_50_59,
				IWMarathonConstants.MALE_60 };
		String[] grForMidnight = { IWMarathonConstants.FEMALE_18, IWMarathonConstants.FEMALE_19_39,
				IWMarathonConstants.FEMALE_40_49, IWMarathonConstants.FEMALE_50, IWMarathonConstants.MALE_18,
				IWMarathonConstants.MALE_19_39, IWMarathonConstants.MALE_40_49, IWMarathonConstants.MALE_50 };
		//TODO: remove this hack - set metadata on the groups containing the
		// specific run...
		if (runName.equals("Rvk Marathon")) {
			generateSubGroups(iwc, group, disForMarathon, grForMarathon);
		}
		else if (runName.equals("Midnight Run")) {
			generateSubGroups(iwc, group, disForMidnight, grForMidnight);
		}
		else if (runName.equals("Laugavegur")) {
			generateSubGroups(iwc, group, disForLaugavegur, grForLaugavegur);
		}
		else if (runName.equals("Roller Skate")) {
			generateSubGroups(iwc, group, disForRollerSkate, grForMarathon);
		}
	}

	/**
	 * @param iwc
	 * @param group
	 * @param disForMarathon
	 * @param grForMarathon
	 */
	private void generateSubGroups(IWContext iwc, Group group, String[] dis, String[] gr) {
		for (int i = 0; i < dis.length; i++) {
			Group distance = null;
			try {
				distance = getGroupBiz(iwc).createGroupUnder(dis[i], null, group);
				distance.setGroupType(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE);
				distance.store();
				for (int j = 0; j < gr.length; j++) {
					Group g = null;
					try {
						g = getGroupBiz(iwc).createGroupUnder(gr[j], null, distance);
						g.setGroupType(IWMarathonConstants.GROUP_TYPE_RUN_GROUP);
						g.store();
					}
					catch (Exception e) {
					}
				}
			}
			catch (IBOLookupException e1) {
				e1.printStackTrace();
			}
			catch (RemoteException e1) {
				e1.printStackTrace();
			}
			catch (CreateException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * gets all the run type groups of the type
	 * "iwma_run_marathon","iwma_run_laugavegur" and "iwma_run_midnight"
	 * 
	 * @return a Collection of the run types
	 */
	public Collection getRuns() {
		IWContext iwc = IWContext.getInstance();
		Collection runs = null;
		String[] type = { IWMarathonConstants.GROUP_TYPE_RUN };
		try {
			runs = getGroupBiz(iwc).getGroups(type, true);
		}
		catch (Exception e) {
			runs = null;
		}
		return runs;
	}

	/**
	 * Gets all the years that exist for a specific run. The years are groups
	 * with the group type "iwma_year"
	 * 
	 * @param run -
	 *            the supergroup of the years
	 * @return Collection of the years for the specific run
	 */
	public Collection getYears(Group run) {
		IWContext iwc = IWContext.getInstance();
		Collection years = null;
		Collection type = new ArrayList();
		type.add(IWMarathonConstants.GROUP_TYPE_RUN_YEAR);
		try {
			years = getGroupBiz(iwc).getChildGroupsRecursiveResultFiltered(run, type, true);
		}
		catch (Exception e) {
			years = null;
		}
		return years;
	}

	/**
	 * Gets all the years that exist for a specific run. The years are groups
	 * with the group type "iwma_year". This method returns a Map instead of a
	 * Collection. This is for example used to display the years in a secondary
	 * dropdown menu when displaying two connected dropdowns.
	 * 
	 * @param Group
	 *            run - the supergroup of the years
	 * @return Map of the years for the specific run
	 */
	public Map getYearsMap(Group run) {
		Map yearsMap = new LinkedHashMap();
		Iterator yearsIter = getYears(run).iterator();
		while (yearsIter.hasNext()) {
			Group year = (Group) yearsIter.next();
			yearsMap.put(year.getPrimaryKey().toString(), year.getName());
		}
		return yearsMap;
	}

	/**
	 * Gets a Map of distances for a specific run and year. Distances are groups
	 * with the group type "iwma_distance".
	 * 
	 * @param Group
	 *            run - the supersupergroup of the specific run
	 * @param year -
	 *            the year of the run
	 * @return Map of all distances for a specific run on a specific year
	 */
	public Map getDistancesMap(Group run, String year) {
		IWContext iwc = IWContext.getInstance();
		Map disMap = new LinkedHashMap();
		Collection distances = null;
		Collection type = new ArrayList();
		type.add(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE);
		Iterator yearsIter = getYears(run).iterator();
		while (yearsIter.hasNext()) {
			Group y = (Group) yearsIter.next();
			if (y.getName().equals(year)) {
				try {
					distances = getGroupBiz(iwc).getChildGroupsRecursiveResultFiltered(y, type, true);
				}
				catch (Exception e) {
					distances = null;
				}
			}
			else {
				distances = null;
			}
		}
		if (distances != null) {
			Iterator disIter = distances.iterator();
			while (disIter.hasNext()) {
				Group dis = (Group) disIter.next();
				disMap.put(dis.getPrimaryKey().toString(), dis.getName());
			}
		}
		return disMap;
	}

	/**
	 * 
	 * @param user
	 * @return an int representing the age of the user
	 */
	public int getUserAge(User user) {
		Date dob = user.getDateOfBirth();
		IWTimestamp t = new IWTimestamp(dob);
		int birthYear = t.getYear();
		IWTimestamp time = IWTimestamp.RightNow();
		int year = time.getYear();
		return year - birthYear;
	}

	/**
	 * Gets all countries. This method is for example used when displaying a
	 * dropdown menu of all countries
	 * 
	 * @return Colleciton of all countries
	 */
	public Collection getCountries() {
		Collection countries = null;
		try {
			CountryHome countryHome = (CountryHome) getIDOHome(Country.class);
			countries = new ArrayList(countryHome.findAll());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return countries;
	}

	private GroupBusiness getGroupBiz(IWContext iwc) throws IBOLookupException {
		GroupBusiness business = (GroupBusiness) IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
		return business;
	}

	private UserBusiness getUserBiz(IWContext iwc) throws IBOLookupException {
		UserBusiness business = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
		return business;
	}

	private int getMaxParticipantNumber(String distanceType) {
		if (distanceType.equals(IWMarathonConstants.DISTANCE_55)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_55;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_42)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_42;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_21)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_21;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_10)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_10;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_7)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_7;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_5)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_5;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_3)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_3;
		}
		return 0;
	}

	private int getMinParticipantNumber(String distanceType) {
		if (distanceType.equals(IWMarathonConstants.DISTANCE_55)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_55;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_42)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_42;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_21)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_21;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_10)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_10;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_7)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_7;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_5)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_5;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_3)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_3;
		}
		return 0;
	}
}