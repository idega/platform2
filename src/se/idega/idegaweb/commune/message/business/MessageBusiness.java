package se.idega.idegaweb.commune.message.business;

import javax.ejb.*;

public interface MessageBusiness extends com.idega.business.IBOService
{
 public java.util.Collection findMessages(int p0)throws java.lang.Exception, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.data.Message getMessage(int p0)throws java.lang.Exception, java.rmi.RemoteException;
}
