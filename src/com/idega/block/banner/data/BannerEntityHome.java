package com.idega.block.banner.data;


public interface BannerEntityHome extends com.idega.data.IDOHome
{
 public BannerEntity create() throws javax.ejb.CreateException;
 public BannerEntity createLegacy();
 public BannerEntity findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public BannerEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public BannerEntity findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}