/*
 * Created on 17.8.2004
 */
package is.idega.idegaweb.marathon.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.data.IDOFactory;


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

}
