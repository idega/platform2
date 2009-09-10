package com.idega.block.dictionary.data;


public class WordHomeImpl extends com.idega.data.IDOFactory implements WordHome
{
 protected Class getEntityInterfaceClass(){
  return Word.class;
 }


 public Word create() throws javax.ejb.CreateException{
  return (Word) super.createIDO();
 }


public java.util.Collection findAllWordsByCategory(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WordBMPBean)entity).ejbFindAllWordsByCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllWordsInCategories(int[] p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WordBMPBean)entity).ejbFindAllWordsInCategories(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllWordsContaining(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WordBMPBean)entity).ejbFindAllWordsContaining(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Word findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Word) super.findByPrimaryKeyIDO(pk);
 }



}