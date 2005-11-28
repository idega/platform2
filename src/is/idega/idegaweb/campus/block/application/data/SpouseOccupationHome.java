/**
 * 
 */
package is.idega.idegaweb.campus.block.application.data;


import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface SpouseOccupationHome extends IDOHome {
	public SpouseOccupation create() throws javax.ejb.CreateException;

	public SpouseOccupation findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.SpouseOccupationBMPBean#ejbFindAll
	 */
	public java.util.Collection findAll() throws FinderException;

}
