package com.idega.block.text.data;


public interface ContentHome extends com.idega.data.IDOHome
{
 public Content create() throws javax.ejb.CreateException;
 public Content createLegacy();
 public Content findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Content findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Content findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}