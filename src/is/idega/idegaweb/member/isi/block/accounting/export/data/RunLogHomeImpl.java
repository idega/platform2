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
public class RunLogHomeImpl extends IDOFactory implements RunLogHome {
	protected Class getEntityInterfaceClass() {
		return RunLog.class;
	}

	public RunLog create() throws javax.ejb.CreateException {
		return (RunLog) super.createIDO();
	}

	public RunLog findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (RunLog) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((RunLogBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
