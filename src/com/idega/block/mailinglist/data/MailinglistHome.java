package com.idega.block.mailinglist.data;


public interface MailinglistHome extends com.idega.data.IDOHome
{
 public Mailinglist create() throws javax.ejb.CreateException;
 public Mailinglist createLegacy();
 public Mailinglist findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Mailinglist findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Mailinglist findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}