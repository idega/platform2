package se.idega.idegaweb.commune.message.data;


public interface SystemArchivationMessageHome extends com.idega.data.IDOHome
{
 public SystemArchivationMessage create() throws javax.ejb.CreateException;
 public SystemArchivationMessage findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findMessages(com.idega.user.data.User p0)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findPrintedMessages(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findUnPrintedMessages(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public int getNumberOfUnPrintedMessages();
 public java.lang.String[] getPrintMessageTypes();

}