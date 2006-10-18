/**
 * 
 */
package is.idega.idegaweb.campus.block.allocation.business;




import com.idega.business.IBOHome;

/**
 * @author bluebottle
 *
 */
public interface ContractServiceHome extends IBOHome {
	public ContractService create() throws javax.ejb.CreateException,
			java.rmi.RemoteException;

}
