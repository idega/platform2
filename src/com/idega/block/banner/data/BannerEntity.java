package com.idega.block.banner.data;


public interface BannerEntity extends com.idega.data.IDOLegacyEntity
{
 public void delete()throws java.sql.SQLException;
 public java.lang.String getAttribute();
 public java.lang.String getIDColumnName();
 public void setAttribute(java.lang.String p0);
}
