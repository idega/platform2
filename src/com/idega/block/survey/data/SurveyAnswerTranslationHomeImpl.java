package com.idega.block.survey.data;


public class SurveyAnswerTranslationHomeImpl extends com.idega.data.IDOFactory implements SurveyAnswerTranslationHome
{
 protected Class getEntityInterfaceClass(){
  return SurveyAnswerTranslation.class;
 }


 public SurveyAnswerTranslation create() throws javax.ejb.CreateException{
  return (SurveyAnswerTranslation) super.createIDO();
 }


public SurveyAnswerTranslation findAnswerTranslation(com.idega.block.survey.data.SurveyAnswer p0,com.idega.core.localisation.data.ICLocale p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SurveyAnswerTranslationBMPBean)entity).ejbFindAnswerTranslation(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public SurveyAnswerTranslation findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SurveyAnswerTranslation) super.findByPrimaryKeyIDO(pk);
 }



}