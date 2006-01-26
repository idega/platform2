/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.economa.business;




import com.idega.business.IBOHome;

/**
 * @author bluebottle
 *
 */
public interface EconomaBusinessHome extends IBOHome {
	public EconomaBusiness create() throws javax.ejb.CreateException,
			java.rmi.RemoteException;

}
