package is.idega.idegaweb.campus.block.questionaire1011.data;


public class QuestionaireHomeImpl extends com.idega.data.IDOFactory implements QuestionaireHome
{
 protected Class getEntityInterfaceClass(){
  return Questionaire.class;
 }


 public Questionaire create() throws javax.ejb.CreateException{
  return (Questionaire) super.createIDO();
 }


public java.util.Collection findAllByUser(com.idega.user.data.User p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((QuestionaireBMPBean)entity).ejbFindAllByUser(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByUser(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((QuestionaireBMPBean)entity).ejbFindAllByUser(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Questionaire findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Questionaire) super.findByPrimaryKeyIDO(pk);
 }



}