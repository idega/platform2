package com.idega.block.survey.data;


public interface SurveyQuestion extends com.idega.data.IDOEntity
{
 public char getAnswerType();
 public java.lang.String getQuestion(com.idega.core.localisation.data.ICLocale p0)throws com.idega.data.IDOLookupException,javax.ejb.FinderException;
 public void setAnswerType(char p0);
 public void setQuestion(java.lang.String p0,com.idega.core.localisation.data.ICLocale p1)throws com.idega.data.IDOLookupException,javax.ejb.CreateException;
 public void setRemoved(com.idega.user.data.User p0);
 public void store();
}
