/*
 * Created on Jun 30, 2004
 */
package is.idega.idegaweb.marathon.business;

import is.idega.block.family.business.FamilyLogic;
import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.data.ParticipantHome;
import is.idega.idegaweb.marathon.data.Run;
import is.idega.idegaweb.marathon.data.Year;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.transaction.UserTransaction;
import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.creditcard.business.CreditCardBusiness;
import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard.data.KortathjonustanMerchant;
import com.idega.block.creditcard.data.KortathjonustanMerchantHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.contact.data.Email;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.AddressHome;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.CountryHome;
import com.idega.core.location.data.PostalCode;
import com.idega.core.location.data.PostalCodeHome;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOStoreException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.UnavailableIWContext;
import com.idega.presentation.IWContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;
import com.idega.util.text.Name;

/**
 * Description: Business bean (service) for run... <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * 
 * @author birna
 */
public class RunBusinessBean extends IBOServiceBean implements RunBusiness {

	private final static String IW_BUNDLE_IDENTIFIER = IWMarathonConstants.IW_BUNDLE_IDENTIFIER;

	private static String DEFAULT_SMTP_MAILSERVER = "mail.agurait.com";

	private static String PROP_SYSTEM_SMTP_MAILSERVER = "messagebox_smtp_mailserver";
	private static String PROP_CC_ADDRESS = "messagebox_cc_address";

	private static String PROP_MESSAGEBOX_FROM_ADDRESS = "messagebox_from_mailaddress";

	private static String DEFAULT_MESSAGEBOX_FROM_ADDRESS = "messagebox@idega.com";
	private static String DEFAULT_CC_ADDRESS = "hjordis@ibr.is";

	/**
	 * saves information on the user - creates a new user if he doesn't exsist..
	 */
	private User saveUser(String name, String ssn, IWTimestamp dateOfBirth, Gender gender, String address, String postal, String city, Country country) {
		User user = null;
		try {
			if (dateOfBirth == null) {
				dateOfBirth = getBirthDateFromSSN(ssn);
			}
			Name fullName = new Name(name);
			user = getUserBiz().createUser(fullName.getFirstName(), fullName.getMiddleName(), fullName.getLastName(), null, gender, dateOfBirth);
			user.store();

			if (address != null && !address.equals("")) {
				AddressHome addressHome = (AddressHome) getIDOHome(Address.class);
				Address a = addressHome.create();
				a.setStreetName(address);
				a.setCity(city);
				a.setCountry(country);
				a.store();

				PostalCodeHome postalHome = (PostalCodeHome) getIDOHome(PostalCode.class);
				Integer countryID = (Integer) country.getPrimaryKey();
				PostalCode p = null;
				try {
					p = postalHome.findByPostalCodeAndCountryId(postal, countryID.intValue());
				}
				catch (FinderException fe) {
					p = postalHome.create();
					p.setCountry(country);
					p.setPostalCode(postal);
					p.setName(city);
					p.store();
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
			user.store();
		}
		catch (RemoteException rme) {
		}
		catch (CreateException cre) {
		}
		return user;
	}
	
	public boolean isRegisteredInRun(int runID, int userID) {
		try {
			User user = getUserBiz().getUserHome().findByPrimaryKey(new Integer(userID));
			
			return getUserBiz().isMemberOfGroup(runID, user);
		}
		catch (RemoteException re) {
			log(re);
		}
		catch (FinderException fe) {
			//User does not exist in database...
		}
		return false;
	}
	
	public boolean isRegisteredInRun(String year, Group run, User user) {
		try {
			Group runYear = null;
			String[] types = { IWMarathonConstants.GROUP_TYPE_RUN_YEAR };
			Collection years = getGroupBiz().getChildGroups(run, types, true);
			Iterator iter = years.iterator();
			while (iter.hasNext()) {
				Group yearGroup = (Group) iter.next();
				if (yearGroup.getName().equals(year)) {
					runYear = yearGroup;
					break;
				}
			}
			
			if (runYear == null) {
				return false;
			}
			
			Participant participant = ((ParticipantHome) IDOLookup.getHome(Participant.class)).findByUserAndRun(user, run, runYear);
			return true;
		}
		catch (FinderException fe) {
			return false;
		}
		catch (RemoteException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	public boolean doesGroupExist(Object distancePK, String groupName) {
		try {
			return ((ParticipantHome) IDOLookup.getHome(Participant.class)).getCountByDistanceAndGroupName(distancePK, groupName) > 0;
		}
		catch (IDOException ie) {
			ie.printStackTrace();
			return false;
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	public boolean isRegisteredInRun(int runID, String personalID) {
		try {
			User user = getUserBiz().getUserHome().findByPersonalID(personalID);
			
			return getUserBiz().isMemberOfGroup(runID, user);
		}
		catch (RemoteException re) {
			log(re);
		}
		catch (FinderException fe) {
			//User does not exist in database...
		}
		return false;
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
	
	public int getAgeFromPersonalID(String personalID) {
		if (personalID != null) {
			IWTimestamp dateOfBirth = getBirthDateFromSSN(personalID);
			if (dateOfBirth != null) {
				Age age = new Age(dateOfBirth.getDate());
				return age.getYears();
			}
		}
		return -1;
	}

	/**
	 * saves information on the run for the specific user puts user in the right
	 * group
	 */
	public void saveRun(int userID, String run, String distance, String year, String nationality, String tshirt, String chipOwnershipStatus, String chipNumber, String groupName, String bestTime, String goalTime, Locale locale) {
		Group groupRun = null;
		Group disGroup = null;
		int ageGenderGroupID = -1;
		User user = null;
		try {
			groupRun = getGroupBiz().getGroupByGroupID(Integer.parseInt(run));
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
			user = getUserBiz().getUser(userID);
			int age = getUserAge(user);
			if (distance != null && !distance.equals("")) {
				int disGroupID = Integer.parseInt(distance);
				
				try {
					disGroup = getGroupBiz().getGroupByGroupID(disGroupID);
					distanceType = disGroup.getName();
				}
				catch (UnavailableIWContext e1) {
					e1.printStackTrace();
				}
				catch (FinderException e1) {
					e1.printStackTrace();
				}
				String[] groupType = { IWMarathonConstants.GROUP_TYPE_RUN_GROUP };
				Collection groups = getGroupBiz().getChildGroupsRecursive(disGroup, groupType, true);
				Iterator groupsIter = groups.iterator();
				while (groupsIter.hasNext()) {
					Group group = (Group) groupsIter.next();
					if (group.getName().equals(getGroupName(age, groupRun, user.getGenderID()))) {
						ageGenderGroupID = Integer.parseInt(group.getPrimaryKey().toString());
						group.addGroup(user);
						group.store();
					}
				}
			}
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			Participant r = runHome.create();
			r.setUserID(userID);
			r.setRunTypeGroupID(Integer.parseInt(run));
			r.setRunDistanceGroupID(Integer.parseInt(distance));
			r.setRunYearGroupID(Integer.parseInt(year));
			if(ageGenderGroupID != -1) {
				r.setRunGroupGroupID(ageGenderGroupID);
			}
			r.setTShirtSize(tshirt);
			r.setChipOwnershipStatus(chipOwnershipStatus);
			r.setChipNumber(chipNumber);
			r.setRunGroupName(groupName);
			r.setUserNationality(nationality);
			if (bestTime != null && !bestTime.equals("")) {
				r.setBestTime(bestTime);
			}
			if (goalTime != null && !goalTime.equals("")) {
				r.setGoalTime(goalTime);
			}
			if (distanceType != null) {
				try {
					int participantNumber = runHome.getNextAvailableParticipantNumber(distance, getMinParticipantNumber(distanceType, groupRun.getName()), getMaxParticipantNumber(distanceType, groupRun.getName()));
					if (participantNumber == 0) {
						participantNumber = getMinParticipantNumber(distanceType, groupRun.getName());
					}
					else {
						participantNumber++;
					}
					r.setParticipantNumber(participantNumber);
				}
				catch (IDOException ie) {
					ie.printStackTrace();
				}
			}
			r.store();

			Email email = getUserBiz().getUserMail(user);
			if (groupRun != null && user != null && email != null && email.getEmailAddress() != null) {
				IWResourceBundle iwrb = getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
				Object[] args = { user.getName(), iwrb.getLocalizedString(groupRun.getName(),groupRun.getName()), iwrb.getLocalizedString(disGroup.getName(),disGroup.getName()), iwrb.getLocalizedString(tshirt, tshirt) };
				String subject = iwrb.getLocalizedString("registration_received_subject_mail", "Your registration has been received.");
				String body = MessageFormat.format(iwrb.getLocalizedString("registration_received_body_mail", "Your registration has been received."), args);
				sendMessage(email.getEmailAddress(), subject, body);
			}
		}
		catch (RemoteException rme) {
		}
		catch (CreateException cre) {
		}
	}
	
	public Participant importParticipant(User user, Group run, Group year, Group distance, Country country) throws CreateException {
		try {
			Group ageGenderGroup = getAgeGroup(user, run, distance);
			ageGenderGroup.addGroup(user);

			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			Participant participant = runHome.create();
			participant.setUser(user);
			participant.setRunTypeGroup(run);
			participant.setRunDistanceGroup(distance);
			participant.setRunYearGroup(year);
			participant.setRunGroupGroup(ageGenderGroup);
			if (country != null) {
				participant.setUserNationality(country.getName());
			}
			participant.store();
			
			return participant;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public Collection saveParticipants(Collection runners, String email, String hiddenCardNumber, double amount, IWTimestamp date, Locale locale) throws IDOCreateException {
		Collection participants = new ArrayList();

		UserTransaction trans = getSessionContext().getUserTransaction();
		try {
			trans.begin();
			Iterator iter = runners.iterator();
			while (iter.hasNext()) {
				Runner runner = (Runner) iter.next();
				User user = runner.getUser();
				if (user == null) {
					user = saveUser(runner.getName(), runner.getPersonalID(), new IWTimestamp(runner.getDateOfBirth()), runner.getGender(), runner.getAddress(), runner.getPostalCode(), runner.getCity(), runner.getCountry());
				}
				
				Group ageGenderGroup = getAgeGroup(user, runner.getRun(), runner.getDistance());
				ageGenderGroup.addGroup(user);
				Group yearGroup = (Group) runner.getDistance().getParentNode();
				Group run = runner.getRun();
				Group distance = runner.getDistance();
				
				try {
					ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
					Participant participant = runHome.create();
					participant.setUser(user);
					participant.setRunTypeGroup(run);
					participant.setRunDistanceGroup(distance);
					participant.setRunYearGroup(yearGroup);
					participant.setRunGroupGroup(ageGenderGroup);
					if (runner.getAmount() > 0) {
						participant.setPayedAmount(String.valueOf(runner.getAmount()));
					}
					
					participant.setTShirtSize(runner.getShirtSize());
					if (runner.isOwnChip()) {
						participant.setChipOwnershipStatus(IWMarathonConstants.CHIP_OWN);
					}
					else if (runner.isRentChip()) {
						participant.setChipOwnershipStatus(IWMarathonConstants.CHIP_RENT);
					}
					else if (runner.isBuyChip()) {
						participant.setChipOwnershipStatus(IWMarathonConstants.CHIP_BUY);
					}
					participant.setChipNumber(runner.getChipNumber());
					participant.setUserNationality(runner.getNationality().getName());
					if (runner.getDistance() != null) {
						participant.setParticipantNumber(getNextAvailableParticipantNumber(runner.getRun(), runner.getDistance()));
					}
					participant.store();
					participants.add(participant);
					
					getUserBiz().updateUserHomePhone(user, runner.getHomePhone());
					getUserBiz().updateUserMobilePhone(user, runner.getMobilePhone());
					getUserBiz().updateUserMail(user, runner.getEmail());

					if (runner.getEmail() != null) {
						IWResourceBundle iwrb = getIWApplicationContext().getIWMainApplication().getBundle(IWMarathonConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
						Object[] args = { user.getName(), iwrb.getLocalizedString(run.getName(),run.getName()), iwrb.getLocalizedString(distance.getName(),distance.getName()), iwrb.getLocalizedString("shirt_size." + runner.getShirtSize(), runner.getShirtSize()), String.valueOf(participant.getParticipantNumber()) };
						String subject = iwrb.getLocalizedString("registration_received_subject_mail", "Your registration has been received.");
						String body = MessageFormat.format(iwrb.getLocalizedString("registration_received_body_mail", "Your registration has been received."), args);
						sendMessage(runner.getEmail(), subject, body);
					}
				}
				catch (CreateException ce) {
					ce.printStackTrace();
				}
				catch (RemoteException re) {
					throw new IBORuntimeException(re);
				}
			}

			if (email != null) {
				IWResourceBundle iwrb = getIWApplicationContext().getIWMainApplication().getBundle(IWMarathonConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
				Object[] args = { hiddenCardNumber, String.valueOf(amount), date.getLocaleDateAndTime(locale, IWTimestamp.SHORT, IWTimestamp.SHORT) };
				String subject = iwrb.getLocalizedString("receipt_subject_mail", "Your receipt for registration on Marathon.is");
				String body = MessageFormat.format(iwrb.getLocalizedString("receipt_body_mail", "Your registration has been received."), args);
				sendMessage(email, subject, body);
			}
			trans.commit();
		}
		catch (Exception ex) {
			try {
				trans.rollback();
			}
			catch (javax.transaction.SystemException e) {
				throw new IDOCreateException(e.getMessage());
			}
			ex.printStackTrace();
			throw new IDOCreateException(ex);
		}
		
		return participants;
	}
	
	private int getNextAvailableParticipantNumber(Run run, Distance distance) {
		int number = distance.getNextAvailableParticipantNumber();
		int minNumber = getMinParticipantNumber(distance.getName(), run.getName());
		int maxNumber = getMaxParticipantNumber(distance.getName(), run.getName());
		if (number == -1) {
			number = minNumber;
		}
		if (number > maxNumber) {
			return minNumber;
		}
		
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			while (number <= maxNumber) {
				if (runHome.getCountByDistanceAndNumber(distance.getPrimaryKey(), number) == 0) {
					distance.setNextAvailableParticipantNumber(number + 1);
					distance.store();
					return number;
				}
				else {
					number++;
				}
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		catch (IDOException ie) {
			ie.printStackTrace();
		}
		return minNumber;
	}
	
	public void addParticipantsToGroup(String[] participants, String[] bestTimes, String[] estimatedTimes, String groupName) {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			
			for (int i = 0; i < participants.length; i++) {
				try {
					Participant participant = runHome.findByPrimaryKey(new Integer(participants[i]));
					participant.setBestTime(bestTimes[i]);
					participant.setGoalTime(estimatedTimes[i]);
					participant.setRunGroupName(groupName);
					participant.store();
				}
				catch (FinderException fe) {
					fe.printStackTrace();
				}
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private Group getAgeGroup(User user, Run run, Distance distance) {
		return getAgeGroup(user, (Group) run, distance);
	}
	
	private Group getAgeGroup(User user, Group run, Group distance) {
		Year year = null;
		try {
			year = ConverterUtility.getInstance().convertGroupToYear((Group) distance.getParentNode());
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}

		IWTimestamp runDate = new IWTimestamp();
		runDate.setYear(Integer.parseInt(year.getName()));
		if (year != null && year.getRunDate() != null) {
			runDate = new IWTimestamp(year.getRunDate());
		}
		IWTimestamp dateOfBirth = new IWTimestamp(user.getDateOfBirth());
		dateOfBirth.setDay(1);
		dateOfBirth.setMonth(1);
		Age age = new Age(dateOfBirth.getDate());
		
		String[] groupType = { IWMarathonConstants.GROUP_TYPE_RUN_GROUP };
		try {
			Collection groups = getGroupBiz().getChildGroupsRecursive(distance, groupType, true);
			
			Iterator groupsIter = groups.iterator();
			while (groupsIter.hasNext()) {
				Group group = (Group) groupsIter.next();
				if (group.getName().equals(getGroupName(age.getYears(runDate.getDate()), run, user.getGenderID()))) {
					return group;
				}
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		return null;
	}
	
	public void finishPayment(String properties) throws CreditCardAuthorizationException {
		try {
			CreditCardClient client = getCreditCardBusiness().getCreditCardClient(getCreditCardMerchant());
			client.finishTransaction(properties);
		}
		catch (CreditCardAuthorizationException ccae) {
			throw ccae;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			throw new CreditCardAuthorizationException("Online payment failed. Unknown error.");
		}
	}
	
	public String authorizePayment(String nameOnCard, String cardNumber, String monthExpires, String yearExpires, String ccVerifyNumber, double amount, String currency, String referenceNumber) throws CreditCardAuthorizationException {
		try {
			CreditCardClient client = getCreditCardBusiness().getCreditCardClient(getCreditCardMerchant());
			return client.creditcardAuthorization(nameOnCard, cardNumber, monthExpires, yearExpires, ccVerifyNumber, amount, currency, referenceNumber);
		}
		catch (CreditCardAuthorizationException ccae) {
			throw ccae;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			throw new CreditCardAuthorizationException("Online payment failed. Unknown error.");
		}
	}
	
	public float getPriceForRunner(Runner runner, Locale locale, float chipDiscount, float chipPrice) {
		Age age = null;
		if (runner.getUser() != null) {
			int groupID = Integer.parseInt(getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getProperty(IWMarathonConstants.PROPERTY_STAFF_GROUP_ID, "-1"));
			if (groupID != -1) {
				try {
					Group group = getUserBiz().getGroupBusiness().getGroupByGroupID(groupID);
					if (runner.getUser().hasRelationTo(group)) {
						return 0;
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
			}
			
			age = new Age(runner.getUser().getDateOfBirth());
		}
		else {
			age = new Age(runner.getDateOfBirth());
		}
		boolean isChild = age.getYears() <= 12;
		
		float runnerPrice = isChild ? runner.getDistance().getChildrenPrice(locale) : runner.getDistance().getPrice(locale);
		if (runner.isOwnChip() || runner.isBuyChip()) {
			runnerPrice = runnerPrice - chipDiscount;
			if (runner.isBuyChip()) {
				runnerPrice += chipPrice;
			}
		}
		return runnerPrice;
	}
	
	public int getNumberOfChildren(Collection runners) {
		int numberOfChildren = 0;
		Collection tempRunners = new ArrayList(runners);

		try {
			Runner parent1 = null;
			Runner parent2 = null;

			boolean hasParents = false;
			Iterator iter = tempRunners.iterator();
			while (iter.hasNext()) {
				Runner runner = (Runner) iter.next();
				Iterator iterator = tempRunners.iterator();
				while (iterator.hasNext()) {
					Runner otherRunner = (Runner) iterator.next();
					if (runner.getUser() != null && otherRunner.getUser() != null && !runner.getUser().equals(otherRunner.getUser()) && runner.getDistance().isFamilyDiscount()) {
						if (getFamilyLogic().isSpouseOf(runner.getUser(), otherRunner.getUser()) && otherRunner.getDistance().isFamilyDiscount()) {
							hasParents = true;
							parent1 = runner;
							parent2 = otherRunner;
							break;
						}
					}
				}
				if (hasParents) {
					break;
				}
			}
			
			if (hasParents) {
				tempRunners.remove(parent1);
				tempRunners.remove(parent2);
				
				Iterator iterator = tempRunners.iterator();
				while (iterator.hasNext()) {
					Runner runner = (Runner) iterator.next();
					if (getFamilyLogic().isParentOf(parent1.getUser(), runner.getUser()) || getFamilyLogic().isParentOf(parent2.getUser(), runner.getUser())) {
						if (runner.getDistance().isFamilyDiscount()) {
							numberOfChildren++;
						}
					}
				}
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		
		return numberOfChildren;
	}
	
	private FamilyLogic getFamilyLogic() {
		try {
			return (FamilyLogic) getServiceInstance(FamilyLogic.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	public Collection getCreditCardImages() {
		try {
			return getCreditCardBusiness().getCreditCardTypeImages(getCreditCardBusiness().getCreditCardClient(getCreditCardMerchant()));
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}
	
	private CreditCardMerchant getCreditCardMerchant() throws FinderException {
		String merchantPK = getIWApplicationContext().getIWMainApplication().getBundle(IWMarathonConstants.IW_BUNDLE_IDENTIFIER).getProperty(IWMarathonConstants.PROPERTY_MERCHANT_PK);
		if (merchantPK != null) {
			try {
				return ((KortathjonustanMerchantHome) IDOLookup.getHome(KortathjonustanMerchant.class)).findByPrimaryKey(new Integer(merchantPK));
			}
			catch (IDOLookupException ile) {
				throw new IBORuntimeException(ile);
			}
		}
		return null;
	}
	
	public void savePayment(int userID, int distanceID, String payMethod, String amount) {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			Participant run = runHome.findByUserIDandDistanceID(userID,distanceID);
			if(run != null) {
				run.setPayMethod(payMethod);
				run.setPayedAmount(amount);
				run.store();
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void savePaymentByUserID(int userID, String payMethod, String amount) {
		try {
			try {
				ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
				Collection runObjs = runHome.findByUserID(userID);
				if(runObjs != null) {
					Iterator runIt = runObjs.iterator();
					while(runIt.hasNext()) {
						Participant run = (Participant) runIt.next();
						if(run != null) {
							run.setPayMethod(payMethod);
							run.setPayedAmount(amount);
							run.store();
						}
					}
				
				}
			}
			catch (IDOStoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch (FinderException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		} 
	}
	
	public void updateParticipantAndChip(int userID,String partiNr, String chipNr) {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			Collection runObjs = runHome.findByUserID(userID);
			if(runObjs != null) {
				Iterator runIt = runObjs.iterator();
				while(runIt.hasNext()) {
					Participant run = (Participant) runIt.next();
					if(run != null) {
						if(partiNr != null && !partiNr.equals("")) {
							run.setParticipantNumber(Integer.parseInt(partiNr));
						}
						if(chipNr != null && !chipNr.equals("")) {
							run.setChipNumber(chipNr);
						}
						run.store();
					}
				}
			
			}
		}
		catch (IDOStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void updateTeamName(int userID, int groupID, String teamName) {
		Participant run = null;
		if(groupID != -1) {
			run = getRunObjByUserAndGroup(userID, groupID);
		}
		if(run != null) {
			if(teamName != null && !teamName.equals("")) {
				run.setRunGroupName(teamName);
				run.store();
			}
		}
	}
	public void updateRunAndChipTimes(int userID, int groupID, String runTime, String chipTime) {
		Participant run = null;
		if(groupID != -1) {
			run = getRunObjByUserAndGroup(userID, groupID);
		}
		if(run != null) {
			if(runTime != null) {
				runTime.trim();
				if (!runTime.equals("")) {
					run.setRunTime(Integer.parseInt(runTime));
				}
			}
			if(chipTime != null) {
				chipTime.trim();
				if (!chipTime.equals("")) {
					run.setChipTime(Integer.parseInt(chipTime));
				}
			}
			run.store();
		}
	}
	
	public void updateRunForParticipant(Participant participant, int bibNumber, String runTime, String chipTime, String splitTime1, String splitTime2) {
		if(runTime != null) {
			runTime = runTime.trim();
			if (!runTime.equals("")) {
				participant.setRunTime(convertTimeToInt(runTime));
			}
		}
		if(chipTime != null) {
			chipTime = chipTime.trim();
			if(!chipTime.equals("")) {
				participant.setChipTime(convertTimeToInt(chipTime));
			}
		}
		if(splitTime1 != null) {
			splitTime1 = splitTime1.trim();
			if(!splitTime1.equals("")) {
				participant.setSplitTime1(convertTimeToInt(splitTime1));
			}
		}
		if(splitTime2 != null) {
			splitTime2 = splitTime2.trim();
			if(!splitTime2.equals("")) {
				participant.setSplitTime2(convertTimeToInt(splitTime2));
			}
		}
		participant.setParticipantNumber(bibNumber);
		participant.store();
	}
	
	private int convertTimeToInt(String time) {
		int index = time.lastIndexOf(":");
		int hours = 0;
		int minutes = 0;
		int seconds = Integer.parseInt(time.substring(index + 1));
		time = time.substring(0, index);
		if (time.indexOf(":") != -1) {
			hours = Integer.parseInt(time.substring(0, time.indexOf(":")));
			minutes = Integer.parseInt(time.substring(time.indexOf(":") + 1));
		}
		else {
			minutes = Integer.parseInt(time);
		}
		seconds += hours * 60 * 60;
		seconds += minutes * 60;
		
		return seconds;
	}
	
	public Participant getRunObjByUserIDandDistanceID(int userID, int distanceID) {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findByUserIDandDistanceID(userID,distanceID);
		}
		catch (RemoteException e) {
			log(e);
		}
		catch (FinderException e) {
			log(e);
		}
		 
		return null;
	}
	
	public Participant getParticipantByDistanceAndParticipantNumber(Object distancePK, int participantNumber) throws FinderException {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findByDistanceAndParticpantNumber(distancePK, participantNumber);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	public Participant getParticipantByRunAndYear(User user, Group run, Group year) throws FinderException {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findByUserAndRun(user, run, year);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	public Group getRunGroupByGroupId(Integer groupId) {
		try {
			GroupHome groupHome = (GroupHome) getIDOHome(Group.class);
			return groupHome.findByPrimaryKey(groupId);
		}
		catch (RemoteException e) {
			log(e);
		}
		catch (FinderException e) {
			log(e);
		}
		return null;
	}
	public Participant getRunObjByUserAndGroup(int userID, int groupID) {
		Participant run = null;
		int yearGroupID = -1;
		int distanceGroupID = -1;
		int runGroupID = -1;
		Group group = null;
		Collection parentGroups = null;
		try {
			group = getGroupBiz().getGroupByGroupID(groupID);
			if(group != null) {
				parentGroups = getGroupBiz().getParentGroupsRecursive(group);
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
		if(parentGroups != null && !parentGroups.isEmpty()) {
			Iterator groupIter = parentGroups.iterator();
			while(groupIter.hasNext()) {
				Group parentGroup = (Group) groupIter.next();
				if(parentGroup.getGroupType().equals(IWMarathonConstants.GROUP_TYPE_RUN)) {
					runGroupID = Integer.parseInt(parentGroup.getPrimaryKey().toString());
				}
				else if(parentGroup.getGroupType().equals(IWMarathonConstants.GROUP_TYPE_RUN_YEAR)) {
					yearGroupID = Integer.parseInt(parentGroup.getPrimaryKey().toString());
				}
				else if(parentGroup.getGroupType().equals(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE)) {
					distanceGroupID = Integer.parseInt(parentGroup.getPrimaryKey().toString());
				}
			}
		}
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			if(runGroupID != -1 && yearGroupID != -1 && distanceGroupID != -1) {
				Collection runObjs = runHome.findByUserAndParentGroup(userID,runGroupID,2004,distanceGroupID);
				if(runObjs != null && !runObjs.isEmpty()) {
					Iterator runIt = runObjs.iterator();
					while(runIt.hasNext()) {
						Participant runObj = (Participant) runIt.next();
						run = runObj;
					}
				}
			}
		}
		catch (RemoteException e1) {
			e1.printStackTrace();
		}
		catch (FinderException e1) {
			e1.printStackTrace();
		}
		return run;
	}
	
	public Collection getRunnersByDistance(Group distance, Group runGroup) {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findAllByDistanceAndGroup(distance, runGroup);
		}
		catch (RemoteException e) {
			log(e);
		}
		catch (FinderException e) {
			log(e);
		}
		 
		return new ArrayList();
	}
	
	public void setParticipantNumber(Participant participant, String run) {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);

			int groupID = participant.getRunDistanceGroupID();
			Group group = null;
			try {
				group = getGroupBiz().getGroupByGroupID(groupID);
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
			if(group!=null) {
				int participantNumber = runHome.getNextAvailableParticipantNumber(group.getPrimaryKey(), getMinParticipantNumber(group.getName(), run), getMaxParticipantNumber(group.getName(), run));
				if (participantNumber == 0) {
					participantNumber = getMinParticipantNumber(group.getName(), run);
				}
				else {
					participantNumber++;
				}
				participant.setParticipantNumber(participantNumber);
				participant.store();

			}
		}
		catch (IDOException ie) {
			log(ie);
		}
		catch (RemoteException re) {
			log(re);
		}
	}

	public void sendMessage(String email, String subject, String body) {

		boolean sendEmail = true;
		String sSendEmail = this.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getProperty(IWMarathonConstants.PROPERTY_SEND_EMAILS);
		if ("no".equalsIgnoreCase(sSendEmail)) {
			sendEmail = false;
		}
		
		if (sendEmail) {
			String mailServer = DEFAULT_SMTP_MAILSERVER;
			String fromAddress = DEFAULT_MESSAGEBOX_FROM_ADDRESS;
			String cc = DEFAULT_CC_ADDRESS;
			try {
				IWBundle iwb = getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
				mailServer = iwb.getProperty(PROP_SYSTEM_SMTP_MAILSERVER, DEFAULT_SMTP_MAILSERVER);
				fromAddress = iwb.getProperty(PROP_MESSAGEBOX_FROM_ADDRESS, DEFAULT_MESSAGEBOX_FROM_ADDRESS);
				cc = iwb.getProperty(PROP_CC_ADDRESS, DEFAULT_CC_ADDRESS);
			}
			catch (Exception e) {
				System.err.println("MessageBusinessBean: Error getting mail property from bundle");
				e.printStackTrace();
			}
	
			cc = "";
			
			try {
				com.idega.util.SendMail.send(fromAddress, email.trim(), cc, "", mailServer, subject, body);
			}
			catch (javax.mail.MessagingException me) {
				System.err.println("Error sending mail to address: " + email + " Message was: " + me.getMessage());
			}
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
	
	public String[] getDistancesForRun(Group run) {
		String runName = run.getName();
		String[] disForMarathon = { IWMarathonConstants.DISTANCE_42, IWMarathonConstants.DISTANCE_21, IWMarathonConstants.DISTANCE_10, IWMarathonConstants.DISTANCE_3, IWMarathonConstants.DISTANCE_CHARITY_42, IWMarathonConstants.DISTANCE_CHARITY_21 };
		String[] disForLaugavegur = { IWMarathonConstants.DISTANCE_55_WITH_BUS, IWMarathonConstants.DISTANCE_55 };
		String[] disForMidnight = { IWMarathonConstants.DISTANCE_10, IWMarathonConstants.DISTANCE_5, IWMarathonConstants.DISTANCE_3 };
		String[] disForRollerSkate = { IWMarathonConstants.DISTANCE_10, IWMarathonConstants.DISTANCE_5 };

		if (runName.equals("Rvk Marathon")) {
			return disForMarathon;
		}
		else if (runName.equals("Midnight Run")) {
			return disForMidnight;
		}
		else if (runName.equals("Laugavegur")) {
			return disForLaugavegur;
		}
		else if (runName.equals("Roller Skate")) {
			return disForRollerSkate;
		}
		return null;
	}

	public void createNewGroupYear(IWContext iwc, Group run, String year, String[] priceISK, String[] priceEUR, String[] useChips, String[] childrenPriceISK, String[] childrenPriceEUR, String[] familyDiscount, String[] allowsGroups) {
		String runName = run.getName();
		Group group = null;
		try {
			group = getGroupBiz().createGroupUnder(year, null, run);
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
		String[] grForMarathon = { IWMarathonConstants.FEMALE_14, IWMarathonConstants.FEMALE_15_17, IWMarathonConstants.FEMALE_18_39, IWMarathonConstants.FEMALE_40_49, IWMarathonConstants.FEMALE_50_59, IWMarathonConstants.FEMALE_60, IWMarathonConstants.MALE_14, IWMarathonConstants.MALE_15_17, IWMarathonConstants.MALE_18_39, IWMarathonConstants.MALE_40_49, IWMarathonConstants.MALE_50_59, IWMarathonConstants.MALE_60 };
		String[] grForLaugavegur = { IWMarathonConstants.FEMALE_18_29, IWMarathonConstants.FEMALE_30_39, IWMarathonConstants.FEMALE_40_49, IWMarathonConstants.FEMALE_50, IWMarathonConstants.MALE_18_29, IWMarathonConstants.MALE_30_39, IWMarathonConstants.MALE_40_49, IWMarathonConstants.MALE_50_59, IWMarathonConstants.MALE_60 };
		String[] grForMidnight = { IWMarathonConstants.FEMALE_18, IWMarathonConstants.FEMALE_19_39, IWMarathonConstants.FEMALE_40_49, IWMarathonConstants.FEMALE_50, IWMarathonConstants.MALE_18, IWMarathonConstants.MALE_19_39, IWMarathonConstants.MALE_40_49, IWMarathonConstants.MALE_50 };
		//TODO: remove this hack - set metadata on the groups containing the
		// specific run...
		if (runName.equals("Rvk Marathon")) {
			generateSubGroups(iwc, group, getDistancesForRun(run), grForMarathon, priceISK, priceEUR, useChips, childrenPriceISK, childrenPriceEUR, familyDiscount, allowsGroups);
		}
		else if (runName.equals("Midnight Run")) {
			generateSubGroups(iwc, group, getDistancesForRun(run), grForMidnight, priceISK, priceEUR, useChips, childrenPriceISK, childrenPriceEUR, familyDiscount, allowsGroups);
		}
		else if (runName.equals("Laugavegur")) {
			generateSubGroups(iwc, group, getDistancesForRun(run), grForLaugavegur, priceISK, priceEUR, useChips, childrenPriceISK, childrenPriceEUR, familyDiscount, allowsGroups);
		}
		else if (runName.equals("Roller Skate")) {
			generateSubGroups(iwc, group, getDistancesForRun(run), grForMarathon, priceISK, priceEUR, useChips, childrenPriceISK, childrenPriceEUR, familyDiscount, allowsGroups);
		}
	}

	/**
	 * @param iwc
	 * @param group
	 * @param disForMarathon
	 * @param grForMarathon
	 */
	private void generateSubGroups(IWContext iwc, Group group, String[] dis, String[] gr, String[] priceISK, String[] priceEUR, String[] useChips, String[] childrenPriceISK, String[] childrenPriceEUR, String[] familyDiscount, String[] allowsGroups) {
		for (int i = 0; i < dis.length; i++) {
			Group distance = null;
			try {
				distance = getGroupBiz().createGroupUnder(dis[i], null, group);
				distance.setGroupType(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE);
				distance.store();
				try {
					Distance distanceGroup = ConverterUtility.getInstance().convertGroupToDistance(distance);
					distanceGroup.setUseChip(new Boolean(useChips[i]).booleanValue());
					distanceGroup.setPriceInISK(Float.parseFloat(priceISK[i]));
					distanceGroup.setPriceInEUR(Float.parseFloat(priceEUR[i]));
					distanceGroup.setFamilyDiscount(new Boolean(familyDiscount[i]).booleanValue());
					if (childrenPriceISK[i] != null && childrenPriceISK[i].length() > 0) {
						distanceGroup.setChildrenPriceInISK(Float.parseFloat(childrenPriceISK[i]));
					}
					if (childrenPriceEUR[i] != null && childrenPriceEUR[i].length() > 0) {
						distanceGroup.setChildrenPriceInEUR(Float.parseFloat(childrenPriceEUR[i]));
					}
					distanceGroup.setAllowsGroups(new Boolean(allowsGroups[i]).booleanValue());
					distanceGroup.store();
				}
				catch (FinderException fe) {
					fe.printStackTrace();
				}
				for (int j = 0; j < gr.length; j++) {
					Group g = null;
					try {
						g = getGroupBiz().createGroupUnder(gr[j], null, distance);
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
		Collection runs = null;
		String[] type = { IWMarathonConstants.GROUP_TYPE_RUN };
		try {
			runs = getGroupBiz().getGroups(type, true);
		}
		catch (Exception e) {
			runs = null;
		}
		return runs;
	}
	public Collection getRunsForUser(User user) {
		Collection groups = null;
		Collection runs = new ArrayList();
		String[] typeRun = { IWMarathonConstants.GROUP_TYPE_RUN };
		String[] typeGroup = { IWMarathonConstants.GROUP_TYPE_RUN_GROUP };
		try {
			groups = getUserBiz().getUserGroups(user,typeGroup,true);
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
			groups = null;
		}
		catch (RemoteException e) {
			e.printStackTrace();
			groups = null;
		}
		if(groups != null) {
			Iterator groupsIter = groups.iterator();
			while(groupsIter.hasNext()) {
				Group group = (Group) groupsIter.next();
				Collection r = null;
				try {
					r = getGroupBiz().getParentGroupsRecursive(group,typeRun,true);
				}
				catch (IBOLookupException e1) {
					e1.printStackTrace();
				}
				catch (EJBException e1) {
					e1.printStackTrace();
				}
				catch (RemoteException e1) {
					e1.printStackTrace();
				}
				if(r != null) {
					Iterator rIter = r.iterator();
					while(rIter.hasNext()) {
						Group run = (Group) rIter.next();
						if(run != null) {
							runs.add(run);
						}
					}
				}
			}
		}
		return runs;
		
	}
	
	public Group getRunGroupOfTypeForGroup(Group group, String type) {
		
		String[] types = {type};
		Collection r = null;
		Group run = null;

		try {
			r = getGroupBiz().getParentGroupsRecursive(group,types,true);
		}
		catch (RemoteException e1) {
			e1.printStackTrace();
		}
		if(r != null) {
			Iterator rIter = r.iterator();
			if(rIter.hasNext()) {
				 run = (Group) rIter.next();
			}
		}
		return run;
	}

	/**
	 * gets all the "gender/age" groups for the user
	 * e.g. "female_14", "male_14", "female_14_17", "male_14_17", "female_18_39", "male_18_39" ...
	 * 
	 * @return a Collection of the "iwma_run_group" types
	 */
	public Collection getRunGroupsForUser(User user) {
		Collection groups = null;
		String[] typeGroup = { IWMarathonConstants.GROUP_TYPE_RUN_GROUP };
		try {
			groups = getUserBiz().getUserGroups(user, typeGroup, true);
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
			groups = null;
		}
		catch (RemoteException e) {
			e.printStackTrace();
			groups = null;
		}
		return groups;
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
	    return getYears(run, null);
	}
	
	public Collection getYears(Group run, String yearFilter) {
		Collection years = null;
		Collection type = new ArrayList();
		type.add(IWMarathonConstants.GROUP_TYPE_RUN_YEAR);
		try {
			years = getGroupBiz().getChildGroupsResultFiltered(run, yearFilter, type, true);
		}
		catch (Exception e) {
			e.printStackTrace();
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
	    return getYearsMap(run, null);
	}
	    
	public Map getYearsMap(Group run, String groupNameFilter) {
		Map yearsMap = new LinkedHashMap();
		Iterator yearsIter = run.getChildrenIterator();
		while (yearsIter.hasNext()) {
			Group year = (Group) yearsIter.next();
			try {
				yearsMap.put(year.getName(), ConverterUtility.getInstance().convertGroupToYear(year));
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
		}
		return yearsMap;
	}

	/**
	 * Gets a Collection of distances for a specific run and year. Distances are
	 * groups with the group type "iwma_distance".
	 * 
	 * @param Group
	 *            run - the supersupergroup of the specific run
	 * @param year -
	 *            the year of the run
	 * @return Collection of all distances for a specific run on a specific year
	 */
	public List getDistancesMap(Group run, String year) {
		List distances = null;
		Collection type = new ArrayList();
		type.add(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE);
		if (run != null) {
			Collection years = getYears(run, year);
			if (years != null) {
				Iterator yearsIter = years.iterator();
				while (yearsIter.hasNext()) {
					Group y = (Group) yearsIter.next();
					if (y.getName().equals(year)) {
						try {
							distances = new ArrayList(getGroupBiz().getChildGroupsRecursiveResultFiltered(y, type, true));
						}
						catch (Exception e) {
							distances = null;
						}
					}
				}
				if(distances != null) {
					Collections.sort(distances, new RunDistanceComparator());
				}
			}
		}
		
		return distances;
	}
	public Group getDistanceByUserID(int userID) {
		Group dis = null;
		Collection groups = null;
		String[] type = { IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE };
		try {
			groups = getUserBiz().getUserGroups(getUserBiz().getUser(userID),type,true);
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
			groups = null;
		}
		catch (RemoteException e) {
			e.printStackTrace();
			groups = null;
		}
		if(groups != null) {
			Iterator i = groups.iterator();
			while(i.hasNext()) {
				Group g = (Group) i.next();
				dis = g;/*
				Group g = (Group) i.next();
				Collection parentGr = null;
				try {
					parentGr = getGroupBiz().getParentGroups(g);
				}
				catch (IBOLookupException e1) {
					e1.printStackTrace();
				}
				catch (RemoteException e1) {
					e1.printStackTrace();
				}
				if(parentGr != null) {
					Iterator j = parentGr.iterator();
					while(j.hasNext()) {
						Group pg = (Group) j.next();
						if(pg.getName().equals("Rvk Marathon")) {
							dis = g;
						}
					}
				}
			*/}
		}
		return dis;
		
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

	private GroupBusiness getGroupBiz() throws IBOLookupException {
		GroupBusiness business = (GroupBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), GroupBusiness.class);
		return business;
	}

	private CreditCardBusiness getCreditCardBusiness() {
		try {
			return (CreditCardBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), CreditCardBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public UserBusiness getUserBiz() throws IBOLookupException {
		UserBusiness business = (UserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);
		return business;
	}

	private int getMaxParticipantNumber(String distanceType, String run) {
		if (distanceType.equals(IWMarathonConstants.DISTANCE_55) || distanceType.equals(IWMarathonConstants.DISTANCE_55_WITH_BUS)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_55;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_42)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_42;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_21)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_21;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_CHARITY_42)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_CHARITY_42;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_CHARITY_21)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_CHARITY_21;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_10)) {
			if (run.equals("Midnight Run")) {
				return IWMarathonConstants.MAX_NUMBER_DISTANCE_MIDNIGHT_10;
			}
			else {
				return IWMarathonConstants.MAX_NUMBER_DISTANCE_10;
			}
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_5)) {
			if (run.equals("Midnight Run")) {
				return IWMarathonConstants.MAX_NUMBER_DISTANCE_MIDNIGHT_5;
			}
			else {
				return IWMarathonConstants.MAX_NUMBER_DISTANCE_5;
			}
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_3)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_3;
		}
		return 0;
	}

	private int getMinParticipantNumber(String distanceType, String run) {
		if (distanceType.equals(IWMarathonConstants.DISTANCE_55) || distanceType.equals(IWMarathonConstants.DISTANCE_55_WITH_BUS)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_55;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_42)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_42;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_21)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_21;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_CHARITY_42)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_CHARITY_42;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_CHARITY_21)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_CHARITY_21;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_10)) {
			if (run.equals("Midnight Run")) {
				return IWMarathonConstants.MIN_NUMBER_DISTANCE_MIDNIGHT_10;
			}
			else {
				return IWMarathonConstants.MIN_NUMBER_DISTANCE_10;
			}
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_5)) {
			if (run.equals("Midnight Run")) {
				return IWMarathonConstants.MIN_NUMBER_DISTANCE_MIDNIGHT_5;
			}
			else {
				return IWMarathonConstants.MIN_NUMBER_DISTANCE_5;
			}
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_3)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_3;
		}
		return 0;
	}
	
	public Country getCountryByNationality(Object nationality) {
		Country country = null;
		try {
			CountryHome home = (CountryHome) getIDOHome(Country.class);
			try {
				int countryPK = Integer.parseInt(nationality.toString());
				country = home.findByPrimaryKey(new Integer(countryPK));
			}
			catch (NumberFormatException nfe) {
				country = home.findByIsoAbbreviation(nationality.toString());
			}
		}
		catch (FinderException fe) {
			//log(fe);
		}
		catch (RemoteException re) {
			//log(re);
		}
		return country;
	}
}