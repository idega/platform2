package com.idega.block.survey.data;


public interface SurveyReply extends com.idega.data.IDOEntity
{

 public static final int SURVEY_ANSWER_MAX_LENGTH = 500;
	
 public java.lang.String getAnswer();
 public java.lang.String getParticipantKey();
 public com.idega.block.survey.data.SurveyQuestion getQuestion();
 public com.idega.block.survey.data.SurveyEntity getSurvey();
 public void setAnswer(com.idega.block.survey.data.SurveyAnswer p0);
 public void setAnswer(java.lang.String p0);
 public void setAnswerPK(java.lang.Object p0);
 public void setParticipantKey(java.lang.String p0);
 public void setQuestion(com.idega.block.survey.data.SurveyQuestion p0);
 public void setQuestionPK(java.lang.Object p0);
 public void setSurvey(com.idega.block.survey.data.SurveyEntity p0);
 public void setSurveyPK(java.lang.Object p0);
}
