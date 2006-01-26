/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.economa.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class EconomaCheckRecordHomeImpl extends IDOFactory implements
		EconomaCheckRecordHome {
	protected Class getEntityInterfaceClass() {
		return EconomaCheckRecord.class;
	}

	public EconomaCheckRecord create() throws javax.ejb.CreateException {
		return (EconomaCheckRecord) super.createIDO();
	}

	public EconomaCheckRecord findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (EconomaCheckRecord) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((EconomaCheckRecordBMPBean) entity)
				.ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByHeaderId(int id) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((EconomaCheckRecordBMPBean) entity)
				.ejbFindAllByHeaderId(id);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByHeader(EconomaCheckHeader header)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((EconomaCheckRecordBMPBean) entity)
				.ejbFindAllByHeader(header);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
