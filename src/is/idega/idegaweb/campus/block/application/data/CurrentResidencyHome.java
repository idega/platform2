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
public interface CurrentResidencyHome extends IDOHome {
	public CurrentResidency create() throws javax.ejb.CreateException;

	public CurrentResidency findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.CurrentResidencyBMPBean#ejbFindAll
	 */
	public java.util.Collection findAll() throws FinderException;

}
