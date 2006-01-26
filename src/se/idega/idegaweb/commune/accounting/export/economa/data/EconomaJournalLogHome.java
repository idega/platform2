/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.economa.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolCategory;
import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface EconomaJournalLogHome extends IDOHome {
	public EconomaJournalLog create() throws javax.ejb.CreateException;

	public EconomaJournalLog findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#ejbFindAllBySchoolCategory
	 */
	public Collection findAllBySchoolCategory(String category)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#ejbFindAllBySchoolCategory
	 */
	public Collection findAllBySchoolCategory(SchoolCategory category)
			throws FinderException;

}
