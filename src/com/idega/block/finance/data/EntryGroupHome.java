package com.idega.block.finance.data;


public interface EntryGroupHome extends com.idega.data.IDOHome
{
 public EntryGroup create() throws javax.ejb.CreateException;
 public EntryGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;

}