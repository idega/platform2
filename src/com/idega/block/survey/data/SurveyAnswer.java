package com.idega.block.survey.data;


public interface SurveyAnswer extends com.idega.data.IDOEntity
{
 public java.lang.String getAnswer(com.idega.core.localisation.data.ICLocale p0)throws com.idega.data.IDOLookupException,javax.ejb.FinderException;
 public com.idega.block.survey.data.SurveyQuestion getSurveyQuestion();
 public void setAnswer(java.lang.String p0,com.idega.core.localisation.data.ICLocale p1)throws com.idega.data.IDOLookupException,javax.ejb.CreateException;
 public void setSurveyQuestion(com.idega.block.survey.data.SurveyQuestion p0);
 public void store();
}
