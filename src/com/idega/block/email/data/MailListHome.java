package com.idega.block.email.data;


public interface MailListHome extends com.idega.data.IDOHome
{
 public MailList create() throws javax.ejb.CreateException;
 public MailList createLegacy();
 public MailList findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public MailList findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public MailList findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}