/*
 * Created on 21.8.2004
 */
package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Run;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import com.idega.core.location.data.Country;
import com.idega.presentation.IWContext;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


/**
 * @author laddi
 */
public interface RunBusiness {

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#saveUser
	 */
	public int saveUser(String name, String ssn, IWTimestamp dateOfBirth, String gender, String address, String postal, String city, String country, String tel, String mobile, String email) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#isRegisteredInRun
	 */
	public boolean isRegisteredInRun(int runID, int userID) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#isRegisteredInRun
	 */
	public boolean isRegisteredInRun(int runID, String personalID) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getAgeFromPersonalID
	 */
	public int getAgeFromPersonalID(String personalID) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#saveRun
	 */
	public void saveRun(int userID, String run, String distance, String year, String nationality, String tshirt, String chipOwnershipStatus, String chipNumber, String groupName, String bestTime, String goalTime, Locale locale) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#savePayment
	 */
	public void savePayment(int userID, int distanceID, String payMethod, String amount) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#savePaymentByUserID
	 */
	public void savePaymentByUserID(int userID, String payMethod, String amount) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#updateParticipantAndChip
	 */
	public void updateParticipantAndChip(int userID, String partiNr, String chipNr) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunObjByUserIDandDistanceID
	 */
	public Run getRunObjByUserIDandDistanceID(int userID, int distanceID) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunnersByDistance
	 */
	public Collection getRunnersByDistance(Group distance, Group runGroup) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#setParticipantNumber
	 */
	public void setParticipantNumber(Run participant) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#sendMessage
	 */
	public void sendMessage(String email, String subject, String body) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#createNewGroupYear
	 */
	public void createNewGroupYear(IWContext iwc, Group run, String year) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRuns
	 */
	public Collection getRuns() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunsForUser
	 */
	public Collection getRunsForUser(User user) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getYears
	 */
	public Collection getYears(Group run) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getYearsMap
	 */
	public Map getYearsMap(Group run) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getDistancesMap
	 */
	public List getDistancesMap(Group run, String year) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getDistanceByUserID
	 */
	public Group getDistanceByUserID(int userID) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getUserAge
	 */
	public int getUserAge(User user) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getCountries
	 */
	public Collection getCountries() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getCountryByNationality
	 */
	public Country getCountryByNationality(Object nationality) throws java.rmi.RemoteException;

}
