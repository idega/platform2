package com.idega.block.mailinglist.data;


public interface MailAccountHome extends com.idega.data.IDOHome
{
 public MailAccount create() throws javax.ejb.CreateException;
 public MailAccount createLegacy();
 public MailAccount findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public MailAccount findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public MailAccount findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}