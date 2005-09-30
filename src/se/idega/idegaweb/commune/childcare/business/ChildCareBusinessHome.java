/**
 * 
 */
package se.idega.idegaweb.commune.childcare.business;





import com.idega.business.IBOHome;

/**
 * @author Dainis
 *
 */
public interface ChildCareBusinessHome extends IBOHome {
    public ChildCareBusiness create() throws javax.ejb.CreateException,
            java.rmi.RemoteException;

}
