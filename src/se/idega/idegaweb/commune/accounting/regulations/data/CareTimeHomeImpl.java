/**
 * 
 */
package se.idega.idegaweb.commune.accounting.regulations.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class CareTimeHomeImpl extends IDOFactory implements CareTimeHome {
	protected Class getEntityInterfaceClass() {
		return CareTime.class;
	}

	public CareTime create() throws javax.ejb.CreateException {
		return (CareTime) super.createIDO();
	}

	public CareTime findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (CareTime) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllCareTimeValues() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CareTimeBMPBean) entity)
				.ejbFindAllCareTimeValues();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
