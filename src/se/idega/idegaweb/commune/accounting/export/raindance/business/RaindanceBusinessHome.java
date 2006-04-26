/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.raindance.business;




import com.idega.business.IBOHome;

/**
 * @author bluebottle
 *
 */
public interface RaindanceBusinessHome extends IBOHome {
	public RaindanceBusiness create() throws javax.ejb.CreateException,
			java.rmi.RemoteException;

}
