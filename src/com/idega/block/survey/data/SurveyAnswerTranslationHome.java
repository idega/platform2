package com.idega.block.survey.data;


public interface SurveyAnswerTranslationHome extends com.idega.data.IDOHome
{
 public SurveyAnswerTranslation create() throws javax.ejb.CreateException;
 public SurveyAnswerTranslation findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public SurveyAnswerTranslation findAnswerTranslation(com.idega.block.survey.data.SurveyAnswer p0,com.idega.core.localisation.data.ICLocale p1)throws javax.ejb.FinderException;

}