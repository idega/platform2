/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.raindance.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolCategory;
import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface RaindanceJournalLogHome extends IDOHome {
	public RaindanceJournalLog create() throws javax.ejb.CreateException;

	public RaindanceJournalLog findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#ejbFindAllBySchoolCategory
	 */
	public Collection findAllBySchoolCategory(String category)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#ejbFindAllBySchoolCategory
	 */
	public Collection findAllBySchoolCategory(SchoolCategory category)
			throws FinderException;

}
