package com.idega.block.survey.data;


public interface SurveyQuestionTranslationHome extends com.idega.data.IDOHome
{
 public SurveyQuestionTranslation create() throws javax.ejb.CreateException;
 public SurveyQuestionTranslation findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public SurveyQuestionTranslation findQuestionTranslation(com.idega.block.survey.data.SurveyQuestion p0,com.idega.core.localisation.data.ICLocale p1)throws javax.ejb.FinderException;

}