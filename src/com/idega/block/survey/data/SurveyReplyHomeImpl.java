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

public java.util.Collection findByQuestions(java.util.Collection p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SurveyReplyBMPBean)entity).ejbFindByQuestions(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SurveyReply findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SurveyReply) super.findByPrimaryKeyIDO(pk);
 }


public int getCountByQuestion(com.idega.block.survey.data.SurveyQuestion p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SurveyReplyBMPBean)entity).ejbHomeGetCountByQuestion(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountByQuestionAndAnswer(com.idega.block.survey.data.SurveyQuestion p0,com.idega.block.survey.data.SurveyAnswer p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SurveyReplyBMPBean)entity).ejbHomeGetCountByQuestionAndAnswer(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}