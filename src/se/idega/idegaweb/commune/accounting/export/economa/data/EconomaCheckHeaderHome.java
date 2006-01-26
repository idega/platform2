/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.economa.data;


import com.idega.block.school.data.SchoolCategory;
import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface EconomaCheckHeaderHome extends IDOHome {
	public EconomaCheckHeader create() throws javax.ejb.CreateException;

	public EconomaCheckHeader findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckHeaderBMPBean#ejbFindBySchoolCategory
	 */
	public EconomaCheckHeader findBySchoolCategory(SchoolCategory schoolCategory)
			throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckHeaderBMPBean#ejbFindBySchoolCategory
	 */
	public EconomaCheckHeader findBySchoolCategory(String schoolCategory)
			throws javax.ejb.FinderException;

}
