/**
 * 
 */
package is.idega.idegaweb.campus.block.application.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class SchoolHomeImpl extends IDOFactory implements SchoolHome {
	protected Class getEntityInterfaceClass() {
		return School.class;
	}

	public School create() throws javax.ejb.CreateException {
		return (School) super.createIDO();
	}

	public School findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (School) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
