/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.raindance.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolCategory;
import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class RaindanceJournalLogHomeImpl extends IDOFactory implements
		RaindanceJournalLogHome {
	protected Class getEntityInterfaceClass() {
		return RaindanceJournalLog.class;
	}

	public RaindanceJournalLog create() throws javax.ejb.CreateException {
		return (RaindanceJournalLog) super.createIDO();
	}

	public RaindanceJournalLog findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (RaindanceJournalLog) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((RaindanceJournalLogBMPBean) entity)
				.ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllBySchoolCategory(String category)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((RaindanceJournalLogBMPBean) entity)
				.ejbFindAllBySchoolCategory(category);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllBySchoolCategory(SchoolCategory category)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((RaindanceJournalLogBMPBean) entity)
				.ejbFindAllBySchoolCategory(category);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
