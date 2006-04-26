/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.raindance.data;


import com.idega.block.school.data.SchoolCategory;
import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface RaindanceCheckHeaderHome extends IDOHome {
	public RaindanceCheckHeader create() throws javax.ejb.CreateException;

	public RaindanceCheckHeader findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckHeaderBMPBean#ejbFindBySchoolCategory
	 */
	public RaindanceCheckHeader findBySchoolCategory(
			SchoolCategory schoolCategory) throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckHeaderBMPBean#ejbFindBySchoolCategory
	 */
	public RaindanceCheckHeader findBySchoolCategory(String schoolCategory)
			throws javax.ejb.FinderException;

}
