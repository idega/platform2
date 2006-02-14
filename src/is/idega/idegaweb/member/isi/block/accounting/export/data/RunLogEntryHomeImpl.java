/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.export.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class RunLogEntryHomeImpl extends IDOFactory implements RunLogEntryHome {
	protected Class getEntityInterfaceClass() {
		return RunLogEntry.class;
	}

	public RunLogEntry create() throws javax.ejb.CreateException {
		return (RunLogEntry) super.createIDO();
	}

	public RunLogEntry findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (RunLogEntry) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((RunLogEntryBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByRunLog(RunLog log) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((RunLogEntryBMPBean) entity)
				.ejbFindByRunLog(log);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByRunLogID(int logID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((RunLogEntryBMPBean) entity)
				.ejbFindByRunLogID(logID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
