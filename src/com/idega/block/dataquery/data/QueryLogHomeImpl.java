package com.idega.block.dataquery.data;


public class QueryLogHomeImpl extends com.idega.data.IDOFactory implements QueryLogHome
{
 protected Class getEntityInterfaceClass(){
  return QueryLog.class;
 }


 public QueryLog create() throws javax.ejb.CreateException{
  return (QueryLog) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((QueryLogBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByTransactionID(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((QueryLogBMPBean)entity).ejbFindByTransactionID(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public QueryLog findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (QueryLog) super.findByPrimaryKeyIDO(pk);
 }



}