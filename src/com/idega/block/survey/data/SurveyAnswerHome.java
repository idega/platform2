package com.idega.block.survey.data;


public interface SurveyAnswerHome extends com.idega.data.IDOHome
{
 public SurveyAnswer create() throws javax.ejb.CreateException;
 public SurveyAnswer findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findQuestionsAnswer(com.idega.block.survey.data.SurveyQuestion p0)throws javax.ejb.FinderException;

}