package se.idega.idegaweb.commune.account.citizen.business;

import java.rmi.RemoteException;

/**
 * @author alindman
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface CitizenAccountSession {

	public boolean getIfUserUsesCOAddress() throws RemoteException;
	public void setIfUserUsesCOAddress(boolean preference) throws RemoteException;
}
