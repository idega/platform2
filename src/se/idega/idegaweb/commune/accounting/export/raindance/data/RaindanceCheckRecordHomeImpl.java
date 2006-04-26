/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.raindance.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class RaindanceCheckRecordHomeImpl extends IDOFactory implements
		RaindanceCheckRecordHome {
	protected Class getEntityInterfaceClass() {
		return RaindanceCheckRecord.class;
	}

	public RaindanceCheckRecord create() throws javax.ejb.CreateException {
		return (RaindanceCheckRecord) super.createIDO();
	}

	public RaindanceCheckRecord findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (RaindanceCheckRecord) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((RaindanceCheckRecordBMPBean) entity)
				.ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByHeaderId(int id) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((RaindanceCheckRecordBMPBean) entity)
				.ejbFindAllByHeaderId(id);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByHeader(RaindanceCheckHeader header)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((RaindanceCheckRecordBMPBean) entity)
				.ejbFindAllByHeader(header);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
