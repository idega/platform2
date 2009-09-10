package com.idega.block.albumcollection.data;


public interface CritiqueHome extends com.idega.data.IDOHome
{
 public Critique create() throws javax.ejb.CreateException;
 public Critique createLegacy();
 public Critique findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Critique findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Critique findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}