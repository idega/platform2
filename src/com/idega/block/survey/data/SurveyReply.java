package com.idega.block.survey.data;


public interface SurveyReply extends com.idega.data.IDOEntity
{
 public java.lang.String getAnswer();
 public java.lang.String getParticipantKey();
 public com.idega.block.survey.data.SurveyQuestion getQuestion();
 public com.idega.block.survey.data.SurveyEntity getSurvey();
 public void setAnswer(java.lang.String p0);
 public void setParticipantKey(java.lang.String p0);
 public void setQuestion(com.idega.block.survey.data.SurveyQuestion p0);
 public void setSurvey(com.idega.block.survey.data.SurveyEntity p0);
}
