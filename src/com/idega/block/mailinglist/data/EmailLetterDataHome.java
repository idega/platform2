package com.idega.block.mailinglist.data;


public interface EmailLetterDataHome extends com.idega.data.IDOHome
{
 public EmailLetterData create() throws javax.ejb.CreateException;
 public EmailLetterData createLegacy();
 public EmailLetterData findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public EmailLetterData findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public EmailLetterData findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}