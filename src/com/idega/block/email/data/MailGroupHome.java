package com.idega.block.email.data;


public interface MailGroupHome extends com.idega.data.IDOHome
{
 public MailGroup create() throws javax.ejb.CreateException;
 public MailGroup createLegacy();
 public MailGroup findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public MailGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public MailGroup findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}