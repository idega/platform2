package se.idega.idegaweb.commune.message.data;


public interface MessageHome extends com.idega.data.IDOHome
{
 public Message create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Message findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findMessages(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;

}