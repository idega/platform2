package is.idega.block.nationalregister.data;


import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface NationalRegisterDeceasedHome extends IDOHome {

	public NationalRegisterDeceased create() throws CreateException;

	public NationalRegisterDeceased findByPrimaryKey(Object pk) throws FinderException;
	
	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterDeceasedBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException, RemoteException;

	/**
	 * @see is.idega.block.nationalregister.data.NationalRegisterDeceasedBMPBean#ejbFindAllBySSN
	 */
	public Collection findAllBySSN(String ssn) throws FinderException, RemoteException;

}