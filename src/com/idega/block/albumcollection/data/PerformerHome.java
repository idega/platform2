package com.idega.block.albumcollection.data;


public interface PerformerHome extends com.idega.data.IDOHome
{
 public Performer create() throws javax.ejb.CreateException;
 public Performer createLegacy();
 public Performer findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Performer findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Performer findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}