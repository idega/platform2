package com.idega.block.survey.data;


public interface SurveyEntityTranslationHome extends com.idega.data.IDOHome
{
 public SurveyEntityTranslation create() throws javax.ejb.CreateException;
 public SurveyEntityTranslation findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}