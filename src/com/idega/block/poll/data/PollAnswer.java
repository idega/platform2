package com.idega.block.poll.data;


public interface PollAnswer extends com.idega.data.IDOLegacyEntity
{
 public void delete()throws java.sql.SQLException;
 public int getHits();
 public java.lang.String getIDColumnName();
 public int getPollQuestionID();
 public void setHits(int p0);
 public void setPollQuestionID(int p0);
}
