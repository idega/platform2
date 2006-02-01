/**
 * 
 */
package is.idega.block.nationalregister.data;


import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface NationalRegisterFateHome extends IDOHome {
	public NationalRegisterFate create() throws javax.ejb.CreateException;

	public NationalRegisterFate findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterFateBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterFateBMPBean#ejbFindByFateCode
	 */
	public NationalRegisterFate findByFateCode(String fateCode)
			throws FinderException;

}
