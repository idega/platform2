package com.idega.block.banner.data;


public interface AdEntity extends com.idega.data.IDOLegacyEntity
{
 public void delete()throws java.sql.SQLException;
 public java.lang.String getAdName();
 public java.sql.Timestamp getBeginDate();
 public java.sql.Timestamp getEndDate();
 public int getHits();
 public java.lang.String getIDColumnName();
 public int getImpressions();
 public int getMaxHits();
 public int getMaxImpressions();
 public java.lang.String getURL();
 public int getUserID();
 public void setAdName(java.lang.String p0);
 public void setBeginDate(java.sql.Timestamp p0);
 public void setEndDate(java.sql.Timestamp p0);
 public void setHits(int p0);
 public void setImpressions(int p0);
 public void setMaxHits(int p0);
 public void setMaxImpressions(int p0);
 public void setURL(java.lang.String p0);
 public void setUserID(int p0);
}
