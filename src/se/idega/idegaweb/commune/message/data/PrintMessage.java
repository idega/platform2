package se.idega.idegaweb.commune.message.data;

/**
 * Title: PrintMessage.java
 * Description: 
 * Company: idegaweb 
 * @author aron
 * @version 1.0
 * 
 * 
 * 
 */
public interface PrintMessage extends Message {
	public String getPrintType() throws java.rmi.RemoteException;
	public void setMessageData(com.idega.core.data.ICFile p0)throws java.rmi.RemoteException;
 	public void setMessageData(int p0)throws java.rmi.RemoteException;
 	public int getMessageDataFileID()throws java.rmi.RemoteException, java.rmi.RemoteException;
 	
}
