package is.idega.idegaweb.campus.block.mailinglist.data;


public class EmailLetterHomeImpl extends com.idega.data.IDOFactory implements EmailLetterHome
{
 protected Class getEntityInterfaceClass(){
  return EmailLetter.class;
 }


 public EmailLetter create() throws javax.ejb.CreateException{
  return (EmailLetter) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((EmailLetterBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByType(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((EmailLetterBMPBean)entity).ejbFindByType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public EmailLetter findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (EmailLetter) super.findByPrimaryKeyIDO(pk);
 }



}