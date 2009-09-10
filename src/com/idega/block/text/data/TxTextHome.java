package com.idega.block.text.data;


public interface TxTextHome extends com.idega.data.IDOHome
{
 public TxText create() throws javax.ejb.CreateException;
 public TxText createLegacy();
 public TxText findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TxText findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TxText findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}