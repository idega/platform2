package com.idega.block.quote.data;


public class QuoteEntityHomeImpl extends com.idega.data.IDOFactory implements QuoteEntityHome
{
 protected Class getEntityInterfaceClass(){
  return QuoteEntity.class;
 }

 public QuoteEntity create() throws javax.ejb.CreateException{
  return (QuoteEntity) super.idoCreate();
 }

 public QuoteEntity findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (QuoteEntity) super.idoFindByPrimaryKey(id);
 }

 public QuoteEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (QuoteEntity) super.idoFindByPrimaryKey(pk);
 }

public java.util.Collection findAllQuotesByLocale(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((QuoteEntityBMPBean)entity).ejbFindAllQuotesByLocale(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}


}