package com.idega.block.survey.data;


public class SurveyAnswerHomeImpl extends com.idega.data.IDOFactory implements SurveyAnswerHome
{
 protected Class getEntityInterfaceClass(){
  return SurveyAnswer.class;
 }


 public SurveyAnswer create() throws javax.ejb.CreateException{
  return (SurveyAnswer) super.createIDO();
 }


public java.util.Collection findQuestionsAnswer(com.idega.block.survey.data.SurveyQuestion p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SurveyAnswerBMPBean)entity).ejbFindQuestionsAnswer(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SurveyAnswer findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SurveyAnswer) super.findByPrimaryKeyIDO(pk);
 }



}