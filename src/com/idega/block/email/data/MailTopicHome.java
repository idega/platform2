package com.idega.block.email.data;


public interface MailTopicHome extends com.idega.data.IDOHome
{
 public MailTopic create() throws javax.ejb.CreateException;
 public MailTopic createLegacy();
 public MailTopic findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public MailTopic findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public MailTopic findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}