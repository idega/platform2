package is.idega.block.nationalregister.business;

import com.idega.business.IBOHome;


/**
 * @author Joakim
 *
 */
public interface NationalRegisterBusinessHome extends IBOHome {

	public NationalRegisterBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
