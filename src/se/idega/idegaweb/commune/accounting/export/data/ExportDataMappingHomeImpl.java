/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class ExportDataMappingHomeImpl extends IDOFactory implements
		ExportDataMappingHome {
	protected Class getEntityInterfaceClass() {
		return ExportDataMapping.class;
	}

	public ExportDataMapping create() throws javax.ejb.CreateException {
		return (ExportDataMapping) super.createIDO();
	}

	public ExportDataMapping findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (ExportDataMapping) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ExportDataMappingBMPBean) entity)
				.ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
