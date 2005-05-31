/*
 * $Id: RunBusiness.java,v 1.22 2005/05/31 19:04:35 laddi Exp $
 * Created on May 31, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Participant;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.FinderException;
import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.business.IBOService;
import com.idega.core.location.data.Country;
import com.idega.data.IDOCreateException;
import com.idega.presentation.IWContext;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2005/05/31 19:04:35 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.22 $
 */
public interface RunBusiness extends IBOService {

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#isRegisteredInRun
	 */
	public boolean isRegisteredInRun(int runID, int userID) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#isRegisteredInRun
	 */
	public boolean isRegisteredInRun(String year, Group run, User user) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#doesGroupExist
	 */
	public boolean doesGroupExist(Object distancePK, String groupName) throws java.rmi.RemoteException;

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
	public void saveRun(int userID, String run, String distance, String year, String nationality, String tshirt,
			String chipOwnershipStatus, String chipNumber, String groupName, String bestTime, String goalTime, Locale locale)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#saveParticipants
	 */
	public Collection saveParticipants(Collection runners, String email, String hiddenCardNumber, double amount,
			IWTimestamp date, Locale locale) throws IDOCreateException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#addParticipantsToGroup
	 */
	public void addParticipantsToGroup(String[] participants, String[] bestTimes, String[] estimatedTimes,
			String groupName) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#finishPayment
	 */
	public void finishPayment(String properties) throws CreditCardAuthorizationException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#authorizePayment
	 */
	public String authorizePayment(String nameOnCard, String cardNumber, String monthExpires, String yearExpires,
			String ccVerifyNumber, double amount, String currency, String referenceNumber)
			throws CreditCardAuthorizationException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getPriceForRunner
	 */
	public float getPriceForRunner(Runner runner, Locale locale, float chipDiscount) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getNumberOfChildren
	 */
	public int getNumberOfChildren(Collection runners) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getCreditCardImages
	 */
	public Collection getCreditCardImages() throws java.rmi.RemoteException;

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
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#updateTeamName
	 */
	public void updateTeamName(int userID, int groupID, String teamName) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#updateRunAndChipTimes
	 */
	public void updateRunAndChipTimes(int userID, int groupID, String runTime, String chipTime)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunObjByUserIDandDistanceID
	 */
	public Participant getRunObjByUserIDandDistanceID(int userID, int distanceID) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getParticipantByDistanceAndParticipantNumber
	 */
	public Participant getParticipantByDistanceAndParticipantNumber(Object distancePK, int participantNumber)
			throws FinderException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunGroupByGroupId
	 */
	public Group getRunGroupByGroupId(Integer groupId) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunObjByUserAndGroup
	 */
	public Participant getRunObjByUserAndGroup(int userID, int groupID) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunnersByDistance
	 */
	public Collection getRunnersByDistance(Group distance, Group runGroup) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#setParticipantNumber
	 */
	public void setParticipantNumber(Participant participant) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#sendMessage
	 */
	public void sendMessage(String email, String subject, String body) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getDistancesForRun
	 */
	public String[] getDistancesForRun(Group run) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#createNewGroupYear
	 */
	public void createNewGroupYear(IWContext iwc, Group run, String year, String[] priceISK, String[] priceEUR,
			String[] useChips, String[] childrenPriceISK, String[] childrenPriceEUR, String[] familyDiscount,
			String[] allowsGroups) throws java.rmi.RemoteException;

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
