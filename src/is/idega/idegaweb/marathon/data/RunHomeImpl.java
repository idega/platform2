/*
 * Created on 21.8.2004
 */
package is.idega.idegaweb.marathon.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.data.IDOFactory;
import com.idega.user.data.Group;


/**
 * @author laddi
 */
public class RunHomeImpl extends IDOFactory implements RunHome {

	protected Class getEntityInterfaceClass() {
		return Run.class;
	}

	public Run create() throws javax.ejb.CreateException {
		return (Run) super.createIDO();
	}

	public Run findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (Run) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((RunBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getNextAvailableParticipantNumber(int min, int max) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((RunBMPBean) entity).ejbHomeGetNextAvailableParticipantNumber(min, max);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findAllByDistanceAndGroup(Group distance, Group runGroup) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((RunBMPBean) entity).ejbFindAllByDistanceAndGroup(distance, runGroup);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Run findByUserIDandDistanceID(int userID, int distanceID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((RunBMPBean) entity).ejbFindByUserIDandDistanceID(userID, distanceID);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findByUserID(int userID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((RunBMPBean) entity).ejbFindByUserID(userID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	
	public Collection findByUserAndParentGroups(int userID,int runGroupID,int yearGroupID,int distanceGroupID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((RunBMPBean) entity).ejbFindByUserAndParentGroup(userID, runGroupID, yearGroupID, distanceGroupID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllWithoutChipNumber(int distanceIDtoIgnore) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((RunBMPBean) entity).ejbFindAllWithoutChipNumber(distanceIDtoIgnore);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
