package com.idega.block.poll.data;


public interface PollQuestion extends com.idega.data.IDOLegacyEntity
{
 public void delete()throws java.sql.SQLException;
 public java.sql.Timestamp getEndTime();
 public java.lang.String getIDColumnName();
 public java.sql.Timestamp getStartTime();
 public int getUserID();
 public void setEndTime(java.sql.Timestamp p0);
 public void setStartTime(java.sql.Timestamp p0);
 public void setUserID(int p0);
}
