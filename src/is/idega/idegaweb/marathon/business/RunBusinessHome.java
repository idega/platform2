/*
 * Created on 21.8.2004
 */
package is.idega.idegaweb.marathon.business;




import com.idega.business.IBOHome;


/**
 * @author laddi
 */
public interface RunBusinessHome extends IBOHome {

	public RunBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}
