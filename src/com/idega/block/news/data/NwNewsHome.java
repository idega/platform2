package com.idega.block.news.data;


public interface NwNewsHome extends com.idega.data.IDOHome
{
 public NwNews create() throws javax.ejb.CreateException;
 public NwNews createLegacy();
 public NwNews findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public NwNews findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public NwNews findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}