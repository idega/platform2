package com.idega.block.albumcollection.data;

import com.idega.core.data.ICFile;


public interface Track extends com.idega.data.IDOLegacyEntity
{
 public int getAlbumId();
 public java.lang.String getDescription();
 public int getLength();
 public int getLyricId();
 public java.lang.String getName();
 public int getNumber();
 public void setAlbumId(int p0);
 public void setDescription(java.lang.String p0);
 public void setLength(int p0);
 public void setLyricId(int p0);
 public void setLyricIdAsNull()throws java.sql.SQLException;
 public void setName(java.lang.String p0);
 public void setNumber(int p0);
 public ICFile getTrack();
 public int getTrackID();
 public void setTrackID(Integer fileID);
 public void setTrackID(int fileID);
}
