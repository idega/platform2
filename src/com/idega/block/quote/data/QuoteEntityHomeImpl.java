package com.idega.block.quote.data;


public class QuoteEntityHomeImpl extends com.idega.data.IDOFactory implements QuoteEntityHome
{
 protected Class getEntityInterfaceClass(){
  return QuoteEntity.class;
 }


 public QuoteEntity create() throws javax.ejb.CreateException{
  return (QuoteEntity) super.createIDO();
 }


public java.util.Collection findAllQuotesByLocale(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((QuoteEntityBMPBean)entity).ejbFindAllQuotesByLocale(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public QuoteEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (QuoteEntity) super.findByPrimaryKeyIDO(pk);
 }


public int getNumberOfQuotes(int p0)throws javax.ejb.FinderException,com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((QuoteEntityBMPBean)entity).ejbHomeGetNumberOfQuotes(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}