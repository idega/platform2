package se.idega.idegaweb.commune.message.business;

import javax.ejb.*;

public interface MessageBusiness extends com.idega.business.IBOService
{
 public se.idega.idegaweb.commune.message.data.Message createUserMessage(int p0,java.lang.String p1,java.lang.String p2)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public java.util.Collection findMessages(int p0)throws java.lang.Exception, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message getMessage(java.lang.String p0,int p1)throws java.lang.Exception, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createPrintArchivationMessage(int p0,java.lang.String p1,java.lang.String p2)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message createPrintedLetterMessage(int p0,java.lang.String p1,java.lang.String p2)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
}
