package se.idega.idegaweb.commune.message.data;


public interface SystemArchivationMessageHome extends PrintMessageHome
{
 public java.util.Collection findMessages(com.idega.user.data.User p0)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findPrintedMessages(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findUnPrintedMessages(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findPrintedMessages()throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findUnPrintedMessages()throws javax.ejb.FinderException,java.rmi.RemoteException;
 public int getNumberOfUnPrintedMessages();
 public java.lang.String[] getPrintMessageTypes();

}