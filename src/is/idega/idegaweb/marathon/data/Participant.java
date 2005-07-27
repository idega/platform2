/*
 * $Id: Participant.java,v 1.5 2005/07/27 10:55:52 laddi Exp $
 * Created on Jul 27, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.data;

import com.idega.data.IDOEntity;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/07/27 10:55:52 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.5 $
 */
public interface Participant extends IDOEntity {

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunID
	 */
	public int getRunID();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunTypeGroupID
	 */
	public int getRunTypeGroupID();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunTypeGroup
	 */
	public Group getRunTypeGroup();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunYearGroupID
	 */
	public int getRunYearGroupID();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunYearGroup
	 */
	public Group getRunYearGroup();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunDistanceGroupID
	 */
	public int getRunDistanceGroupID();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunDistanceGroup
	 */
	public Group getRunDistanceGroup();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunGroupGroupID
	 */
	public int getRunGroupGroupID();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunGroupGroup
	 */
	public Group getRunGroupGroup();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunTime
	 */
	public int getRunTime();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getChipTime
	 */
	public int getChipTime();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getChipOwnershipStatus
	 */
	public String getChipOwnershipStatus();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getUserID
	 */
	public int getUserID();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getChipNumber
	 */
	public String getChipNumber();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getChipBunchNumber
	 */
	public String getChipBunchNumber();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getUserNationality
	 */
	public String getUserNationality();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getTShirtSize
	 */
	public String getTShirtSize();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunGroupName
	 */
	public String getRunGroupName();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getBestTime
	 */
	public String getBestTime();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getGoalTime
	 */
	public String getGoalTime();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getParticipantNumber
	 */
	public int getParticipantNumber();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getPayMethod
	 */
	public String getPayMethod();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getPayedAmount
	 */
	public String getPayedAmount();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunTypeGroupID
	 */
	public void setRunTypeGroupID(int runTypeGroupID);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunTypeGroup
	 */
	public void setRunTypeGroup(Group runTypeGroup);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunYearGroupID
	 */
	public void setRunYearGroupID(int runYearGroupID);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunYearGroup
	 */
	public void setRunYearGroup(Group runYearGroup);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunDistanceGroupID
	 */
	public void setRunDistanceGroupID(int runDisGroupID);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunDistanceGroup
	 */
	public void setRunDistanceGroup(Group runDisGroup);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunGroupGroupID
	 */
	public void setRunGroupGroupID(int runGroupGroupID);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunGroupGroup
	 */
	public void setRunGroupGroup(Group runGroupGroup);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunTime
	 */
	public void setRunTime(int runTime);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setChipTime
	 */
	public void setChipTime(int chipTime);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setChipOwnershipStatus
	 */
	public void setChipOwnershipStatus(String ownershipStatus);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setUserID
	 */
	public void setUserID(int userID);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setUser
	 */
	public void setUser(User user);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setChipNumber
	 */
	public void setChipNumber(String chipNumber);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setChipBunchNumber
	 */
	public void setChipBunchNumber(String chipBunchNumber);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setUserNationality
	 */
	public void setUserNationality(String nationality);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setTShirtSize
	 */
	public void setTShirtSize(String tShirtSize);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunGroupName
	 */
	public void setRunGroupName(String runGrName);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setBestTime
	 */
	public void setBestTime(String bestTime);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setGoalTime
	 */
	public void setGoalTime(String goalTime);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setParticipantNumber
	 */
	public void setParticipantNumber(int participantNumber);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setPayMethod
	 */
	public void setPayMethod(String payMethod);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setPayedAmount
	 */
	public void setPayedAmount(String amount);
}
