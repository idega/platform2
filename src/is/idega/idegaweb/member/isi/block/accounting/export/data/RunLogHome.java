/**
 * 
 */
package is.idega.idegaweb.member.isi.block.accounting.export.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface RunLogHome extends IDOHome {
	public RunLog create() throws javax.ejb.CreateException;

	public RunLog findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.export.data.RunLogBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

}
