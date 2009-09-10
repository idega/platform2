package com.idega.block.text.data;


public interface LocalizedTextHome extends com.idega.data.IDOHome
{
 public LocalizedText create() throws javax.ejb.CreateException;
 public LocalizedText createLegacy();
 public LocalizedText findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public LocalizedText findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public LocalizedText findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}