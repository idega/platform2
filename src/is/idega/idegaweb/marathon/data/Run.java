/*
 * Created on 17.8.2004
 */
package is.idega.idegaweb.marathon.data;



import com.idega.data.IDOEntity;
import com.idega.user.data.Group;


/**
 * @author laddi
 */
public interface Run extends IDOEntity {

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getRunID
	 */
	public int getRunID();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getRunTypeGroupID
	 */
	public int getRunTypeGroupID();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getRunYearGroupID
	 */
	public int getRunYearGroupID();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getRunDistanceGroupID
	 */
	public int getRunDistanceGroupID();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getRunDistanceGroup
	 */
	public Group getRunDistanceGroup();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getRunGroupGroupID
	 */
	public int getRunGroupGroupID();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getRunTime
	 */
	public int getRunTime();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getChipTime
	 */
	public int getChipTime();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getUserID
	 */
	public int getUserID();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getChipNumber
	 */
	public String getChipNumber();
	
	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getChipOwnershipStatus
	 */
	public String getChipOwnershipStatus();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getUserNationality
	 */
	public String getUserNationality();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getTShirtSize
	 */
	public String getTShirtSize();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getRunGroupName
	 */
	public String getRunGroupName();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getBestTime
	 */
	public String getBestTime();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getGoalTime
	 */
	public String getGoalTime();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getParticipantNumber
	 */
	public int getParticipantNumber();
	
	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getPayMethod
	 */
	public String getPayMethod();
	
	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getPayedAmount
	 */
	public String getPayedAmount();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setRunTypeGroupID
	 */
	public void setRunTypeGroupID(int runTypeGroupID);

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setRunYearGroupID
	 */
	public void setRunYearGroupID(int runYearGroupID);

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setRunDistanceGroupID
	 */
	public void setRunDistanceGroupID(int runDisGroupID);

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setRunGroupGroupID
	 */
	public void setRunGroupGroupID(int runGroupGroupID);

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setRunTime
	 */
	public void setRunTime(double runTime);

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setChipTime
	 */
	public void setChipTime(double chipTime);

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setUserID
	 */
	public void setUserID(int userID);

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setChipNumber
	 */
	public void setChipNumber(String chipNumber);

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setChipOwnershipStatus
	 */
	public void setChipOwnershipStatus(String ownershipStatus);
	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setUserNationality
	 */
	public void setUserNationality(String nationality);

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setTShirtSize
	 */
	public void setTShirtSize(String tShirtSize);

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setRunGroupName
	 */
	public void setRunGroupName(String runGrName);

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setBestTime
	 */
	public void setBestTime(String bestTime);

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setGoalTime
	 */
	public void setGoalTime(String goalTime);

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setParticipantNumber
	 */
	public void setParticipantNumber(int participantNumber);
	
	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setPayMethod
	 */
	public void setPayMethod(String payMethod);
	
	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setPayedAmount
	 */
	public void setPayedAmount(String amount);

}
