package com.idega.block.survey.data;


public interface SurveyReplyHome extends com.idega.data.IDOHome
{
 public SurveyReply create() throws javax.ejb.CreateException;
 public SurveyReply findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByQuestion(com.idega.block.survey.data.SurveyQuestion p0)throws javax.ejb.FinderException;
 public java.util.Collection findByQuestions(java.util.Collection p0)throws javax.ejb.FinderException;
 public int getCountByQuestion(com.idega.block.survey.data.SurveyQuestion p0)throws com.idega.data.IDOException;
 public int getCountByQuestionAndAnswer(com.idega.block.survey.data.SurveyQuestion p0,com.idega.block.survey.data.SurveyAnswer p1)throws com.idega.data.IDOException;

}