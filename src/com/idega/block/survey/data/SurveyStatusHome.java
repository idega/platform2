package com.idega.block.survey.data;


public interface SurveyStatusHome extends com.idega.data.IDOHome
{
 public SurveyStatus create() throws javax.ejb.CreateException;
 public SurveyStatus findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllBySurvey(com.idega.block.survey.data.SurveyEntity p0)throws javax.ejb.FinderException;
 public SurveyStatus findBySurvey(com.idega.block.survey.data.SurveyEntity p0)throws javax.ejb.FinderException;

}