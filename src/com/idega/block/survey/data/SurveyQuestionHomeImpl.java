package com.idega.block.survey.data;


public class SurveyQuestionHomeImpl extends com.idega.data.IDOFactory implements SurveyQuestionHome
{
 protected Class getEntityInterfaceClass(){
  return SurveyQuestion.class;
 }


 public SurveyQuestion create() throws javax.ejb.CreateException{
  return (SurveyQuestion) super.createIDO();
 }


 public SurveyQuestion findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SurveyQuestion) super.findByPrimaryKeyIDO(pk);
 }



}