/*
 * Created on 17.8.2004
 */
package is.idega.idegaweb.marathon.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.data.IDOHome;


/**
 * @author laddi
 */
public interface RunHome extends IDOHome {

	public Run create() throws javax.ejb.CreateException;

	public Run findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
	
	public Collection findRunByUserIDandDistanceID(int userID,int distanceID) throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#ejbHomeGetNextAvailableParticipantNumber
	 */
	public int getNextAvailableParticipantNumber(int min, int max) throws IDOException;

}
