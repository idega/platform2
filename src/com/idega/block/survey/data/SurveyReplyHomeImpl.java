package com.idega.block.survey.data;


public class SurveyReplyHomeImpl extends com.idega.data.IDOFactory implements SurveyReplyHome
{
 protected Class getEntityInterfaceClass(){
  return SurveyReply.class;
 }


 public SurveyReply create() throws javax.ejb.CreateException{
  return (SurveyReply) super.createIDO();
 }


public java.util.Collection findByQuestion(com.idega.block.survey.data.SurveyQuestion p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SurveyReplyBMPBean)entity).ejbFindByQuestion(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SurveyReply findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SurveyReply) super.findByPrimaryKeyIDO(pk);
 }



}