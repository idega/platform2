package com.idega.block.albumcollection.data;


public interface LyricHome extends com.idega.data.IDOHome
{
 public Lyric create() throws javax.ejb.CreateException;
 public Lyric createLegacy();
 public Lyric findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Lyric findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Lyric findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}