package com.idega.block.questions.data;

import javax.ejb.FinderException;


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

/* (non-Javadoc)
 * @see com.idega.block.questions.data.QuestionHome#findRandom()
 */
public Question findRandom(String[] categoryIds) throws FinderException {
    com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
    Object pk = ((QuestionBMPBean)entity).ejbFindRandom(categoryIds);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);

}

 public Question findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Question) super.findByPrimaryKeyIDO(pk);
 }



}