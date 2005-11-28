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
public class SpouseOccupationHomeImpl extends IDOFactory implements
		SpouseOccupationHome {
	protected Class getEntityInterfaceClass() {
		return SpouseOccupation.class;
	}

	public SpouseOccupation create() throws javax.ejb.CreateException {
		return (SpouseOccupation) super.createIDO();
	}

	public SpouseOccupation findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (SpouseOccupation) super.findByPrimaryKeyIDO(pk);
	}

	public java.util.Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SpouseOccupationBMPBean) entity)
				.ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
