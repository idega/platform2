package com.idega.block.survey.data;


public interface SurveyParticipant extends com.idega.data.IDOEntity
{
 public java.lang.String getParticipantName();
 public com.idega.block.survey.data.SurveyEntity getSurvey();
 public void setParticipantName(java.lang.String p0);
 public void setSurvey(com.idega.block.survey.data.SurveyEntity p0);
}
