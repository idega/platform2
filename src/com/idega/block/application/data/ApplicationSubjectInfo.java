package com.idega.block.application.data;


public interface ApplicationSubjectInfo extends com.idega.data.IDOEntity
{
 public int getApplicationCount();
 public java.sql.Timestamp getLastStatusChange();
 public java.sql.Timestamp getLastSubmitted();
 public java.lang.String getStatus();
 public int getSubjectId();
 public void initializeAttributes();
}
