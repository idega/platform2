package se.idega.idegaweb.commune.message.business;

import java.rmi.RemoteException;

/**
 * @author Laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface MessageSession {

	public boolean getIfUserPreferesMessageByEmail()throws RemoteException;
	public boolean getIfUserPreferesMessageInMessageBox()throws RemoteException;
	public void setIfUserPreferesMessageByEmail(boolean preference)throws RemoteException;
	public void setIfUserPreferesMessageInMessageBox(boolean preference)throws RemoteException;
}
