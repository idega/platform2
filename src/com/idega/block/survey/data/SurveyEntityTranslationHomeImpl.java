package com.idega.block.survey.data;


public class SurveyEntityTranslationHomeImpl extends com.idega.data.IDOFactory implements SurveyEntityTranslationHome
{
 protected Class getEntityInterfaceClass(){
  return SurveyEntityTranslation.class;
 }


 public SurveyEntityTranslation create() throws javax.ejb.CreateException{
  return (SurveyEntityTranslation) super.createIDO();
 }


 public SurveyEntityTranslation findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SurveyEntityTranslation) super.findByPrimaryKeyIDO(pk);
 }



}