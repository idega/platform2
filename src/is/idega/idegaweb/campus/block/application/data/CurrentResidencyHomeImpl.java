/**
 * 
 */
package is.idega.idegaweb.campus.block.application.data;


import javax.ejb.FinderException;

import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class CurrentResidencyHomeImpl extends IDOFactory implements
		CurrentResidencyHome {
	protected Class getEntityInterfaceClass() {
		return CurrentResidency.class;
	}

	public CurrentResidency create() throws javax.ejb.CreateException {
		return (CurrentResidency) super.createIDO();
	}

	public CurrentResidency findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (CurrentResidency) super.findByPrimaryKeyIDO(pk);
	}

	public java.util.Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CurrentResidencyBMPBean) entity)
				.ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
