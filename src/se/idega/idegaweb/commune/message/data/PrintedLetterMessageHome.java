package se.idega.idegaweb.commune.message.data;


public interface PrintedLetterMessageHome extends com.idega.data.IDOHome
{
 public PrintedLetterMessage create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public PrintedLetterMessage findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findMessages(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;

}