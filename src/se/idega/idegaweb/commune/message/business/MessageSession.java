package se.idega.idegaweb.commune.message.business;

import java.rmi.RemoteException;

import com.idega.user.data.User;

/**
 * @author Laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface MessageSession {

	public boolean getIfUserPreferesMessageByEmail(User user)throws RemoteException;
	public boolean getIfUserPreferesMessageInMessageBox(User user)throws RemoteException;
	public void setIfUserPreferesMessageByEmail(User user,boolean preference)throws RemoteException;
	public void setIfUserPreferesMessageInMessageBox(User user,boolean preference)throws RemoteException;
}
