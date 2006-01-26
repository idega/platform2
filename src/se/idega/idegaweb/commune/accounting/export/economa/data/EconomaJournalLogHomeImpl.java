/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.economa.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolCategory;
import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class EconomaJournalLogHomeImpl extends IDOFactory implements
		EconomaJournalLogHome {
	protected Class getEntityInterfaceClass() {
		return EconomaJournalLog.class;
	}

	public EconomaJournalLog create() throws javax.ejb.CreateException {
		return (EconomaJournalLog) super.createIDO();
	}

	public EconomaJournalLog findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (EconomaJournalLog) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((EconomaJournalLogBMPBean) entity)
				.ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllBySchoolCategory(String category)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((EconomaJournalLogBMPBean) entity)
				.ejbFindAllBySchoolCategory(category);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllBySchoolCategory(SchoolCategory category)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((EconomaJournalLogBMPBean) entity)
				.ejbFindAllBySchoolCategory(category);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
