package se.idega.idegaweb.commune.accounting.posting.data;


public class PostingStringHomeImpl extends com.idega.data.IDOFactory implements PostingStringHome
{
 protected Class getEntityInterfaceClass(){
  return PostingString.class;
 }


 public PostingString create() throws javax.ejb.CreateException{
  return (PostingString) super.createIDO();
 }


public java.util.Collection findKonterignStrings()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PostingStringBMPBean)entity).ejbFindKonterignStrings();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public PostingString findPostingStringByDate(java.sql.Date p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((PostingStringBMPBean)entity).ejbFindPostingStringByDate(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public PostingString findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PostingString) super.findByPrimaryKeyIDO(pk);
 }



}