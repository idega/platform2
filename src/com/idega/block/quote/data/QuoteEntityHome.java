package com.idega.block.quote.data;


public interface QuoteEntityHome extends com.idega.data.IDOHome
{
 public QuoteEntity create() throws javax.ejb.CreateException;
 public QuoteEntity createLegacy();
 public QuoteEntity findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public QuoteEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public QuoteEntity findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}