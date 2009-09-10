package com.idega.block.email.data;


public interface MailTopicHome extends com.idega.data.IDOHome
{
 public MailTopic create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public MailTopic findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public MailTopic findOneByListId(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;

}