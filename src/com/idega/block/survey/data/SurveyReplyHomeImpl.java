package com.idega.block.survey.data;


public class SurveyReplyHomeImpl extends com.idega.data.IDOFactory implements SurveyReplyHome
{
 protected Class getEntityInterfaceClass(){
  return SurveyReply.class;
 }


 public SurveyReply create() throws javax.ejb.CreateException{
  return (SurveyReply) super.createIDO();
 }


 public SurveyReply findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SurveyReply) super.findByPrimaryKeyIDO(pk);
 }



}