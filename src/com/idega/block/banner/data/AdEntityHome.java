package com.idega.block.banner.data;


public interface AdEntityHome extends com.idega.data.IDOHome
{
 public AdEntity create() throws javax.ejb.CreateException;
 public AdEntity createLegacy();
 public AdEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public AdEntity findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public AdEntity findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}