package com.idega.block.finance.data;


public interface RoundInfoHome extends com.idega.data.IDOHome
{
 public RoundInfo create() throws javax.ejb.CreateException;
 public RoundInfo createLegacy();
 public RoundInfo findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public RoundInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public RoundInfo findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}