package se.idega.idegaweb.commune.message.data;


public interface UserMessageHome extends com.idega.data.IDOHome, MessageHome
{
// public UserMessage create() throws javax.ejb.CreateException, java.rmi.RemoteException;
// public UserMessage findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findMessages(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;

}