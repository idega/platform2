package is.idega.experimental.idotest;


public class QuestionHomeImpl extends com.idega.data.IDOFactory implements QuestionHome
{
 protected Class getEntityInterfaceClass(){
  return Question.class;
 }


 public Question create() throws javax.ejb.CreateException{
  return (Question) super.createIDO();
 }


public java.util.Collection findAllQuestionsContaining(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((QuestionBMPBean)entity).ejbFindAllQuestionsContaining(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllQuestionsNotContaining(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((QuestionBMPBean)entity).ejbFindAllQuestionsNotContaining(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Question findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Question) super.findByPrimaryKeyIDO(pk);
 }


public int getNumberOfQuestions()throws javax.ejb.EJBException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((QuestionBMPBean)entity).ejbHomeGetNumberOfQuestions();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}