package se.idega.idegaweb.commune.accounting.posting.data;


public class PostingFieldHomeImpl extends com.idega.data.IDOFactory implements PostingFieldHome
{
 protected Class getEntityInterfaceClass(){
  return PostingField.class;
 }


 public PostingField create() throws javax.ejb.CreateException{
  return (PostingField) super.createIDO();
 }


public java.util.Collection findAllFieldsByPostingString(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PostingFieldBMPBean)entity).ejbFindAllFieldsByPostingString(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public PostingField findFieldByPostingStringAndFieldNo(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((PostingFieldBMPBean)entity).ejbFindFieldByPostingStringAndFieldNo(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public PostingField findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PostingField) super.findByPrimaryKeyIDO(pk);
 }



}