package com.idega.block.email.data;


public interface MailLetterHome extends com.idega.data.IDOHome
{
 public MailLetter create() throws javax.ejb.CreateException;
 public MailLetter createLegacy();
 public MailLetter findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public MailLetter findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public MailLetter findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}