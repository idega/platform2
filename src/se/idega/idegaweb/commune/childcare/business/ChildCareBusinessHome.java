/**
 * 
 */
package se.idega.idegaweb.commune.childcare.business;





import com.idega.business.IBOHome;

/**
 * @author trn
 *
 */
public interface ChildCareBusinessHome extends IBOHome {
	public ChildCareBusiness create() throws javax.ejb.CreateException,
			java.rmi.RemoteException;

}
