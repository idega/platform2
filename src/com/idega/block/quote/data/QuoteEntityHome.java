package com.idega.block.quote.data;


public interface QuoteEntityHome extends com.idega.data.IDOHome
{
 public QuoteEntity create() throws javax.ejb.CreateException;
 public QuoteEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllQuotesByLocale(int p0)throws javax.ejb.FinderException;
 public int getNumberOfQuotes(int p0)throws javax.ejb.FinderException,com.idega.data.IDOException;

}