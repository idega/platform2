package com.idega.block.survey.data;


public class SurveyQuestionTranslationHomeImpl extends com.idega.data.IDOFactory implements SurveyQuestionTranslationHome
{
 protected Class getEntityInterfaceClass(){
  return SurveyQuestionTranslation.class;
 }


 public SurveyQuestionTranslation create() throws javax.ejb.CreateException{
  return (SurveyQuestionTranslation) super.createIDO();
 }


public SurveyQuestionTranslation findQuestionTranslation(com.idega.block.survey.data.SurveyQuestion p0,com.idega.core.localisation.data.ICLocale p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SurveyQuestionTranslationBMPBean)entity).ejbFindQuestionTranslation(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public SurveyQuestionTranslation findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SurveyQuestionTranslation) super.findByPrimaryKeyIDO(pk);
 }



}