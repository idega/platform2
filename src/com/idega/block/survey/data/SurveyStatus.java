package com.idega.block.survey.data;


public interface SurveyStatus extends com.idega.data.IDOEntity
{
 public boolean getIsModified();
 public com.idega.core.file.data.ICFile getReportFile();
 public com.idega.block.survey.data.SurveyEntity getSurvey();
 public java.sql.Timestamp getTimeOfStatus();
 public void setIsModified(boolean p0);
 public void setReportFile(com.idega.core.file.data.ICFile p0);
 public void setSurvey(com.idega.block.survey.data.SurveyEntity p0);
 public void store();
}
