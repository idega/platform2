package com.idega.block.finance.data;


public interface AccountPhoneEntryHome extends com.idega.data.IDOHome
{
 public AccountPhoneEntry create() throws javax.ejb.CreateException;
 public AccountPhoneEntry createLegacy();
 public AccountPhoneEntry findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public AccountPhoneEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public AccountPhoneEntry findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}