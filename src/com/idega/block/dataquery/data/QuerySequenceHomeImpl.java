package com.idega.block.dataquery.data;


public class QuerySequenceHomeImpl extends com.idega.data.IDOFactory implements QuerySequenceHome
{
 protected Class getEntityInterfaceClass(){
  return QuerySequence.class;
 }


 public QuerySequence create() throws javax.ejb.CreateException{
  return (QuerySequence) super.createIDO();
 }


public QuerySequence findByName(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((QuerySequenceBMPBean)entity).ejbFindByName(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public QuerySequence findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (QuerySequence) super.findByPrimaryKeyIDO(pk);
 }



}