/*
 * $Id: ParticipantHomeImpl.java,v 1.1 2005/05/24 12:06:29 laddi Exp $
 * Created on May 24, 2005
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
import com.idega.data.IDOFactory;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/05/24 12:06:29 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class ParticipantHomeImpl extends IDOFactory implements ParticipantHome {

	protected Class getEntityInterfaceClass() {
		return Participant.class;
	}

	public Participant create() throws javax.ejb.CreateException {
		return (Participant) super.createIDO();
	}

	public Participant findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (Participant) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ParticipantBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getNextAvailableParticipantNumber(int min, int max) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((ParticipantBMPBean) entity).ejbHomeGetNextAvailableParticipantNumber(min, max);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findAllByDistanceAndGroup(Group distance, Group runGroup) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ParticipantBMPBean) entity).ejbFindAllByDistanceAndGroup(distance, runGroup);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Participant findByUserIDandDistanceID(int userID, int distanceID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ParticipantBMPBean) entity).ejbFindByUserIDandDistanceID(userID, distanceID);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Participant findByUserAndRun(User user, Group run) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ParticipantBMPBean) entity).ejbFindByUserAndRun(user, run);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findByUserAndParentGroup(int userID, int runGroupID, int yearGroupID, int distanceGroupID)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ParticipantBMPBean) entity).ejbFindByUserAndParentGroup(userID, runGroupID,
				yearGroupID, distanceGroupID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByUserID(int userID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ParticipantBMPBean) entity).ejbFindByUserID(userID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllWithoutChipNumber(int distanceIDtoIgnore) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ParticipantBMPBean) entity).ejbFindAllWithoutChipNumber(distanceIDtoIgnore);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
