package com.idega.block.poll.data;

import javax.ejb.*;

public interface PollEntity extends com.idega.data.IDOLegacyEntity
{
 public void delete()throws java.sql.SQLException;
 public java.lang.String getAttribute();
 public java.lang.String getIDColumnName();
 public int getPollQuestionID();
 public void setAttribute(java.lang.String p0);
 public void setPollQuestionID(int p0);
}
