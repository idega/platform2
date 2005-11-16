/**
 * 
 */
package se.idega.idegaweb.commune.childcare.business;




import com.idega.business.IBOHome;

/**
 * @author Dainis
 *
 */
public interface AfterSchoolBusinessHome extends IBOHome {
	public AfterSchoolBusiness create() throws javax.ejb.CreateException,
			java.rmi.RemoteException;

}
