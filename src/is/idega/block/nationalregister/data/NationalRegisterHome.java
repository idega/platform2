package is.idega.block.nationalregister.data;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;


/**
 * @author Joakim
 *
 */
public interface NationalRegisterHome extends IDOHome {

	public NationalRegister create() throws javax.ejb.CreateException, java.rmi.RemoteException;

	public NationalRegister findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException, RemoteException;

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterBMPBean#ejbFindAllBySSN
	 */
	public Collection findAllBySSN(String ssn) throws FinderException, RemoteException;
}
