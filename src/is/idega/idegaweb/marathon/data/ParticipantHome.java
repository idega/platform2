/*
 * $Id: ParticipantHome.java,v 1.5 2005/07/27 10:55:52 laddi Exp $
 * Created on Jul 27, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOException;
import com.idega.data.IDOHome;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/07/27 10:55:52 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.5 $
 */
public interface ParticipantHome extends IDOHome {

	public Participant create() throws javax.ejb.CreateException;

	public Participant findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#ejbHomeGetNextAvailableParticipantNumber
	 */
	public int getNextAvailableParticipantNumber(Object distancePK, int min, int max) throws IDOException;

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#ejbHomeGetNumberOfParticipantsByDistance
	 */
	public int getNumberOfParticipantsByDistance(Object distancePK, int min, int max) throws IDOException;

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#ejbHomeGetCountByDistanceAndNumber
	 */
	public int getCountByDistanceAndNumber(Object distancePK, int number) throws IDOException;

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#ejbHomeGetCountByDistanceAndGroupName
	 */
	public int getCountByDistanceAndGroupName(Object distancePK, String groupName) throws IDOException;

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#ejbFindAllByDistanceAndGroup
	 */
	public Collection findAllByDistanceAndGroup(Group distance, Group runGroup) throws FinderException;

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#ejbFindByUserIDandDistanceID
	 */
	public Participant findByUserIDandDistanceID(int userID, int distanceID) throws FinderException;

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#ejbFindByDistanceAndParticpantNumber
	 */
	public Participant findByDistanceAndParticpantNumber(Object distancePK, int participantNumber) throws FinderException;

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#ejbFindByUserAndRun
	 */
	public Participant findByUserAndRun(User user, Group run, Group year) throws FinderException;

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#ejbFindByUserAndParentGroup
	 */
	public Collection findByUserAndParentGroup(int userID, int runGroupID, int yearGroupID, int distanceGroupID)
			throws FinderException;

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#ejbFindByUserID
	 */
	public Collection findByUserID(int userID) throws FinderException;

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#ejbFindAllWithoutChipNumber
	 */
	public Collection findAllWithoutChipNumber(int distanceIDtoIgnore) throws FinderException;
}
