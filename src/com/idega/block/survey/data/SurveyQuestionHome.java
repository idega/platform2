package com.idega.block.survey.data;


public interface SurveyQuestionHome extends com.idega.data.IDOHome
{
 public SurveyQuestion create() throws javax.ejb.CreateException;
 public SurveyQuestion findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}