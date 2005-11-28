/**
 * 
 */
package is.idega.idegaweb.campus.block.application.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface SchoolHome extends IDOHome {
	public School create() throws javax.ejb.CreateException;

	public School findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.SchoolBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

}
