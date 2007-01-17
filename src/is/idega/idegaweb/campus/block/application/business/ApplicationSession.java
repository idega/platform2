package is.idega.idegaweb.campus.block.application.business;


import com.idega.business.IBOSession;
import java.rmi.RemoteException;

public interface ApplicationSession extends IBOSession {
	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationSessionBean#setIsNewUser
	 */
	public void setIsNewUser(boolean isNewUser) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationSessionBean#getIsNewUser
	 */
	public boolean getIsNewUser() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationSessionBean#setSubject
	 */
	public void setSubject(String subject) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationSessionBean#getSubject
	 */
	public String getSubject() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationSessionBean#setApartmentCategory
	 */
	public void setApartmentCategory(String apartmentCategory) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.campus.block.application.business.ApplicationSessionBean#getApartmentCategory
	 */
	public String getApartmentCategory() throws RemoteException;
}