/**
 * 
 */
package is.idega.idegaweb.campus.business;



import com.idega.business.IBOHome;

/**
 * @author bluebottle
 *
 */
public interface CampusServiceHome extends IBOHome {
	public CampusService create() throws javax.ejb.CreateException,
			java.rmi.RemoteException;

}
