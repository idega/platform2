package com.idega.block.finance.data;


public interface EntryKeyHome extends com.idega.data.IDOHome
{
 public EntryKey create() throws javax.ejb.CreateException;
 public EntryKey findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}