package com.idega.block.albumcollection.data;

public interface Album extends com.idega.data.IDOLegacyEntity
{
 public int getAlbumTypeId();
 public int getBackCoverFileId();
 public java.lang.String getDescription();
 public int getFrontCoverFileId();
 public java.lang.String getName();
 public java.lang.String getOrder();
 public java.sql.Date getPublishingDay();
 public void setAlbumTypeId(int p0);
 public void setAlbumTypeId(java.lang.Integer p0);
 public void setBackCoverFileId(int p0);
 public void setBackCoverFileIdAsNull()throws java.sql.SQLException;
 public void setDescription(java.lang.String p0);
 public void setFrontCoverFileId(int p0);
 public void setFrontCoverFileIdAsNull()throws java.sql.SQLException;
 public void setName(java.lang.String p0);
 public void setOrder(java.lang.String p0);
 public void setPublishingDay(java.sql.Date p0);
}
