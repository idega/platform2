package com.idega.block.questions.data;


public class QuestionHomeImpl extends com.idega.data.IDOFactory implements QuestionHome
{
 protected Class getEntityInterfaceClass(){
  return Question.class;
 }


 public Question create() throws javax.ejb.CreateException{
  return (Question) super.createIDO();
 }


public java.util.Collection findAllByCategory(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((QuestionBMPBean)entity).ejbFindAllByCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllInvalidByCategory(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((QuestionBMPBean)entity).ejbFindAllInvalidByCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Question findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Question) super.findByPrimaryKeyIDO(pk);
 }



}