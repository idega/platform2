/**
 * 
 */
package se.idega.idegaweb.commune.accounting.school.business;




import com.idega.business.IBOHome;

/**
 * @author Dainis
 *
 */
public interface ProviderBusinessHome extends IBOHome {
    public ProviderBusiness create() throws javax.ejb.CreateException,
            java.rmi.RemoteException;

}
