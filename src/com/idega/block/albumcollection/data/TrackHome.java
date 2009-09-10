package com.idega.block.albumcollection.data;


public interface TrackHome extends com.idega.data.IDOHome
{
 public Track create() throws javax.ejb.CreateException;
 public Track createLegacy();
 public Track findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Track findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Track findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}