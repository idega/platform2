/*
 * Created on 21.8.2004
 */
package is.idega.idegaweb.marathon.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.data.IDOHome;
import com.idega.user.data.Group;


/**
 * @author laddi
 */
public interface RunHome extends IDOHome {

	public Run create() throws javax.ejb.CreateException, java.rmi.RemoteException;

	public Run findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#ejbHomeGetNextAvailableParticipantNumber
	 */
	public int getNextAvailableParticipantNumber(int min, int max) throws IDOException;

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#ejbFindAllByDistanceAndGroup
	 */
	public Collection findAllByDistanceAndGroup(Group distance, Group runGroup) throws FinderException;

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#ejbFindByUserIDandDistanceID
	 */
	public Collection findByUserAndParentGroups(int userID,int runGroupID,int yearGroupID,int distanceGroupID) throws FinderException;
	public Run findByUserIDandDistanceID(int userID, int distanceID) throws FinderException;

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#ejbFindByUserID
	 */
	public Collection findByUserID(int userID) throws FinderException;

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#ejbFindAllWithoutChipNumber
	 */
	public Collection findAllWithoutChipNumber(int distanceIDtoIgnore) throws FinderException;

}
